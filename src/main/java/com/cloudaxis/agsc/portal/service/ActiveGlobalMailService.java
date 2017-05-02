package com.cloudaxis.agsc.portal.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cloudaxis.agsc.portal.model.EmailHistory;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.LogicalOperator;
import microsoft.exchange.webservices.data.core.enumeration.service.ConflictResolutionMode;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceResponseException;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.EmailMessageSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.Attachment;
import microsoft.exchange.webservices.data.property.complex.AttachmentCollection;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.EmailAddressCollection;
import microsoft.exchange.webservices.data.property.complex.FileAttachment;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;
import microsoft.exchange.webservices.data.search.filter.SearchFilter;
import microsoft.exchange.webservices.data.search.filter.SearchFilter.IsEqualTo;

@Service
public class ActiveGlobalMailService {
	
	@Autowired
	Environment env;
	
	@Autowired
	private EmailHistoryService emailHistoryService;

	private static Logger logger	= Logger.getLogger(ActiveGlobalMailService.class);

	// TODO Please add below constants to configuration file
	private static final ExchangeVersion EXCHANGE_SERVER_VERSION = ExchangeVersion.Exchange2010_SP2;
	private static final String EMAIL_SERVER_ADDRESS = "https://outlook.office365.com/EWS/Exchange.asmx";
	private static final String RECEIPIENT_EMAIL_ADDRESS = "damien.huang@cloudaxis.com";
	private static final String RECEIPIENT_EMAIL_PASSWORD = "Da358332805";

	private static final boolean RETRIEVE_READ_MAIL = false;
	private static final int MAX_MAIL_NUMBER_PER_RETRIEVE = 50;
	private static final int MAILS_TO_BE_RETRIEVE_WITHIN_MINUTES = 100;

	private static ExchangeService emailService = null;
	private static SearchFilter searchFilterList = null;
	private static ItemView view = null;
	private static FindItemsResults<Item> mailsFound = null;

	public void getUnreanEmail(String path) throws Exception {
		initEmailService();

		// To send a mail
		//sendMail(RECEIPIENT_EMAIL_ADDRESS, "Hello world!", "Testing EWS send mail API by Damien");

		retrieveUnreadMails();

		saveMail(path);
	}
	
	public void initEmailService() throws URISyntaxException {
		if (StringUtils.isBlank(RECEIPIENT_EMAIL_PASSWORD)) {
			throw new InvalidParameterException("Please input your own email and password in constant RECEIPIENT_EMAIL_PASSWORD)!");
		}
		ExchangeCredentials credentials = new WebCredentials(RECEIPIENT_EMAIL_ADDRESS, RECEIPIENT_EMAIL_PASSWORD);

		emailService = new ExchangeService(EXCHANGE_SERVER_VERSION);
		emailService.setCredentials(credentials);
		emailService.setUrl(new URI(EMAIL_SERVER_ADDRESS));
	}

	public void retrieveUnreadMails() throws Exception {
		initSearchFilter();
		mailsFound = emailService.findItems(WellKnownFolderName.Inbox, searchFilterList, view);
		
		try {
			if(mailsFound.getTotalCount() > 0){
				emailService.loadPropertiesForItems(mailsFound, PropertySet.FirstClassProperties);
			}
		} catch (Exception e) {
			logger.error("\n\n ***** No mail found, details: \n\n {}",  e);
		}
	}

	public void initSearchFilter() {
		Calendar localCalendar = Calendar.getInstance();
		localCalendar.add(Calendar.MINUTE, -MAILS_TO_BE_RETRIEVE_WITHIN_MINUTES);
		Date time = localCalendar.getTime();
		SearchFilter.IsGreaterThan receiveDateFilter = new SearchFilter.IsGreaterThan(EmailMessageSchema.DateTimeReceived, time);

		IsEqualTo isReadFilter = new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, RETRIEVE_READ_MAIL);

		searchFilterList = new SearchFilter.SearchFilterCollection(LogicalOperator.And, receiveDateFilter, isReadFilter);

		view = new ItemView(MAX_MAIL_NUMBER_PER_RETRIEVE);
	}

	public void sendMail(String receipent, String subject, String messageBody) throws Exception, ServiceLocalException {
		EmailMessage message = new EmailMessage(emailService);
		message.setSubject(subject);
		message.setBody(MessageBody.getMessageBodyFromText(messageBody));
		message.getToRecipients().add(receipent);
		message.send();
	}

	public void saveMail(String path) throws Exception {
		if (mailsFound == null) {
			return;
		}
		for (Item mail : mailsFound.getItems()) {
			// mail.load();
			EmailMessage emailMessage = (EmailMessage) mail;
			//sender address
			String senderEmailAddress  = emailMessage.getSender().getAddress();
			//attachments		down to location
			List<File> attachmentsDownloaded = downloadAttachments(mail,path,senderEmailAddress);
			String attachment = "";
			for(int j=0; j<attachmentsDownloaded.size(); j++){
				File f = attachmentsDownloaded.get(j);
				String[] fileNames = f.getName().split("\\.");
				String fileName ="";
				if(fileNames.length > 3){
					fileName = fileNames[0];
					for(int i=1; i<fileNames.length; i++){
						if(i != fileNames.length-2){
							fileName = fileName + "." + fileNames[i];
						}
					}
				}else{
					fileName = fileNames[0] +"."+ fileNames[fileNames.length-1];
				}
				
				if(j < attachmentsDownloaded.size()-1){
					attachment = attachment + fileName+",";
				}else{
					attachment = attachment + fileName;
				}
			}
			
			long id = saveMailToDatabase(mail, attachment);		//save the email to Database
			
			if(attachmentsDownloaded.size() > 0){		//upload file to s3
				uploadAttachmentToS3(attachmentsDownloaded,id);
			}
			
		}
	}

	public long saveMailToDatabase(Item mail, String attachment) throws ServiceResponseException, Exception {
		EmailMessage emailMessage = (EmailMessage) mail;
		EmailAddress sender = emailMessage.getSender();
		String senderEmailAddress = sender.getAddress();
		String senderName = sender.getName();
		String subject = emailMessage.getSubject();
		
		EmailAddress addressee = emailMessage.getReceivedBy();
		String addresseeEmailAddress = addressee.getAddress();
		String addresseeName = addressee.getName();
		
		Date sendDate =emailMessage.getDateTimeSent();
		//ccreceive
		EmailAddressCollection ccreceiveEmailAddress = emailMessage.getCcRecipients();
		String ccEmail = "";
		for(int i=0; i<ccreceiveEmailAddress.getCount(); i++){
			EmailAddress ccED = new EmailAddress();
			if(i < ccreceiveEmailAddress.getCount() - 1){
				ccEmail = ccEmail + ccED.getAddress()+",";
			}else{
				ccEmail = ccEmail+ccED.getAddress();
			}
		}
		
		//body
		String body = emailMessage.getBody().toString();
		String content="";
		content= body.substring(body.indexOf("<body>")+6, body.indexOf("</body>"));
		
		EmailHistory emailHistory = new EmailHistory();
		emailHistory.setCcEmail(ccEmail);
		emailHistory.setSubject(subject);
		emailHistory.setContent(content);
		emailHistory.setAttachment(attachment);
		emailHistory.setSenderEmail(senderEmailAddress);
		emailHistory.setSenderName(senderName);
		emailHistory.setReceiveEmail(addresseeEmailAddress);
		emailHistory.setReceiveName(addresseeName);
		emailHistory.setSendDate(sendDate);
		emailHistory.setType(1);		//0:send,1:receive
		long id = emailHistoryService.save(emailHistory);
		
		emailMessage.setIsRead(true);
		emailMessage.update(ConflictResolutionMode.AutoResolve);
		
		return id;
	}

	// Upload files to S3, return S3 file informations
	public void uploadAttachmentToS3(List<File> files, long id) {
		AmazonS3 s3client = new AmazonS3Client();
		String region = env.getProperty("aws.region");
		String[] regionSuffix = region.split("-");
		String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];

		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);
		try {
			for(int j=0; j<files.size(); j++){
				File file = files.get(j);
				String[] fileNames = file.getName().split("\\.");
				String fileName ="";
				if(fileNames.length > 3){
					fileName = fileNames[0];
					for(int i=1; i<fileNames.length; i++){
						if(i != fileNames.length-2){
							fileName = fileName + "." + fileNames[i];
						}
					}
				}else{
					fileName = fileNames[0] +"."+ fileNames[fileNames.length-1];
				}
				
				String key = "attachment/" +id+ "/" +fileName;
				PutObjectRequest req = new PutObjectRequest(bucketName, key, file);
				s3client.putObject(req);
			}
		}
		catch (AmazonServiceException ase) {
		}
		catch (AmazonClientException ace) {
		}
	}

	public List<File> downloadAttachments(Item mail,String path, String senderEmailAddress) throws ServiceLocalException, IOException, Exception {
		List<File> downloadedAttachmentList = Lists.newArrayList();
		AttachmentCollection attachments = mail.getAttachments();
		FileAttachment remoteFile = null;
		for (Attachment attachment : attachments) {
			try {
				remoteFile = (FileAttachment) attachment;
			} catch (Exception e) {
				continue;
			}
			String fullFileName = remoteFile.getName();
			String fileExtension = Files.getFileExtension(fullFileName);
			String fileName = Files.getNameWithoutExtension(fullFileName);
			File localFile = File.createTempFile(fileName.concat("."), ".".concat(fileExtension));
			remoteFile.load(localFile.getAbsolutePath());
			downloadedAttachmentList.add(localFile);
			//FileInputStream fileInputStream = new FileInputStream(localFile);  
			//path = path+senderEmailAddress+"\\";
			//uploadFile(fileInputStream,path,fullFileName);
		}
		return downloadedAttachmentList;
	}

	
	/**
	 * upload on the server
	 * 
	 * @author Damien
	 * @param is
	 * @param path
	 * @param key
	 * @throws Exception
	 */
	public void uploadFile(FileInputStream fis, String path, String key) throws Exception {
		OutputStream os = null;
		String newpath = (path + key).replaceAll("\\\\", "/");
		File file = new File(newpath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		try {
			os = new FileOutputStream(file);
			int bytes = 0;
			byte[] buffer = new byte[8192];
			while ((bytes = fis.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytes);
			}
			os.close();
			fis.close();
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				}
				catch (Exception e1) {
					;
				}
			}
			if (fis != null) {
				try {
					fis.close();
					fis = null;
				}
				catch (Exception e1) {
					;
				}
			}
		}
	}
}
