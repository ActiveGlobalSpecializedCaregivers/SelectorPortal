package com.cloudaxis.agsc.portal.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.cloudaxis.agsc.portal.dao.EmailHistoryDAO;
import com.cloudaxis.agsc.portal.dao.UserDAO;
import com.cloudaxis.agsc.portal.helpers.MD5Util;
import com.cloudaxis.agsc.portal.helpers.StringUtil;
import com.cloudaxis.agsc.portal.model.Caregiver;
import com.cloudaxis.agsc.portal.model.EmailHistory;
import com.cloudaxis.agsc.portal.model.EmailTemplates;
import com.cloudaxis.agsc.portal.model.Profile;
import com.cloudaxis.agsc.portal.model.SendCV;
import com.cloudaxis.agsc.portal.model.User;
import com.cloudaxis.agsc.portal.model.UsersChangePassword;

@Service
public class EmailService {

	private final static Logger logger	= Logger.getLogger(EmailService.class);

	@Autowired
	private JavaMailSenderImpl	mailSender;

	@Autowired
	private EmailHistoryDAO		emailHistoryDAO;

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private FileService fileService;
	
	private static final String charPath = File.separator;
	
	/**
	 * Send acknowledgment email to the Applicant after job application
	 * received.
	 * 
	 * @param profile
	 * @param user
	 * @throws IOException
	 * @throws MessagingException
	 * @author Vineela Sharma
	 */
	public void sendAcknowledgement(Profile profile, User user) throws IOException, MessagingException {

		MimeMessage message = mailSender.createMimeMessage();
		Properties mailProperties = mailSender.getJavaMailProperties();
		String senderAdress = mailProperties.getProperty("mail.sender");
		// Set up TO, FROM, CC, SUBJECT, RECEIPIENT
		message.setFrom(new InternetAddress(senderAdress));
		message.setRecipient(RecipientType.TO, new InternetAddress(profile.getEmail()));
		message.setRecipient(RecipientType.BCC, new InternetAddress(profile.getEmail()));
		message.setSubject(mailProperties.getProperty("mail.subject.acknowledge"));
		message.setSentDate(new Date());

		// Prepare and set content
		StringBuilder contentHtml = new StringBuilder();
		contentHtml.append("<b>Dear " + profile.getFull_name() + "</b>,</br></br>")
				.append("Thank you for your interest in <i><b>Active Global Specialised Caregivers.</b></i></br></br>")
				.append("We will carefully consider your application and will contact you if you are shortlisted to continue in the recruitment process.</br></br>")
				.append("Wish you the very best in this selection process.</br></br>")
				.append("<b>Active Global Specialised Caregivers</b><br></br>")
				.append("51 Goldhill Plaza, #22-06/07</br>Singapore 308900</br>EA Licence: 13C6324</br></br>")
				.append("Unit 801, 8/F, Lucky Centre, 165-171</br>Wan Chai Road, Wan Chai,</br>EA Licence:43220</br></br>");
		message.setContent(contentHtml.toString(), "text/html;charset=UTF-8");

		// Send the mail
		mailSender.send(message);

		// Save to Email history
		EmailHistory emailHistory = new EmailHistory();
		emailHistory.setUserId("Active Global Specialised Caregivers");
		emailHistory.setCandidateId(profile.getUser_id());
		emailHistory.setSubject(message.getSubject());
		emailHistory.setContent(message.getContent().toString());
		emailHistory.setSendDate(message.getSentDate());
		emailHistory.setSenderEmail(senderAdress);
		emailHistory.setSenderName("Active Global Specialised Caregivers");
		emailHistory.setReceiveEmail(profile.getEmail());
		emailHistory.setReceiveName(profile.getFull_name());
		emailHistory.setType(0);

		emailHistoryDAO.save(emailHistory);
	}

	/**
	 * Generate and send verification code
	 * 
	 * @param request
	 * @param response
	 * @param email
	 *            - user's email
	 * @return
	 * @throws IOException
	 * @throws MessagingException
	 * @author Damien
	 */
	public void sendVerificationCode(HttpServletRequest request, HttpServletResponse response, String email)
			throws IOException, MessagingException {

		MimeMessage message = mailSender.createMimeMessage();
		Properties mailProperties = mailSender.getJavaMailProperties();

		// Set up TO, FROM, CC, SUBJECT, RECEIPIENT
		message.setFrom(new InternetAddress(mailProperties.getProperty("mail.sender")));
		message.setRecipient(RecipientType.TO, new InternetAddress(email));
		message.setRecipient(RecipientType.BCC, new InternetAddress(mailProperties.getProperty("mail.sender")));
		message.setSubject(mailProperties.getProperty("mail.subject.forgot.password"));

		// Prepare and set content
		int verificationCode = (int) (Math.random() * 9000 + 1000);
		message.setContent("<p><br><br>Here is your verification code: <b>" + verificationCode + "</b></p>",
				"text/html;charset=UTF-8");

		// Send the mail
		mailSender.send(message);

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("code", verificationCode);
		response.getWriter().print(jsonObj.toString());
	}

	/**
	 * Send a notification email to applicant when status changed to Shortlisted
	 * 
	 * @param request
	 * @param response
	 * @param caregiver
	 *            - user's email
	 * @return
	 * @author Vineela Sharma
	 * @param user
	 * @throws Exception 
	 */
	public void sendShortlistedNotification(HttpServletRequest request, EmailTemplates emailTemplates, Caregiver caregiver, User user)
			throws Exception {

		MimeMessage message = mailSender.createMimeMessage();
		Properties mailProperties = mailSender.getJavaMailProperties();
		String senderAddress = mailProperties.getProperty("mail.sender");
		// Set up TO, FROM, CC, SUBJECT, RECEIPIENT
		message.setFrom(new InternetAddress(senderAddress));
		message.setRecipient(RecipientType.TO, new InternetAddress(caregiver.getEmail()));
		message.setSubject(emailTemplates.getSubject());
		message.setSentDate(new Date());

		// Prepare and set content
        Multipart multipart = new MimeMultipart();
        BodyPart contentPart = new MimeBodyPart();
        contentPart.setContent(emailTemplates.getContent(), "text/html;charset=UTF-8");
        multipart.addBodyPart(contentPart);
		
        //add attachment
        String attachment = emailTemplates.getAttachment();
        List<File> fileList= new ArrayList<File>();
        if(!StringUtil.isBlank(attachment)){
	        String[] attachments = attachment.split(",");
	        String path = request.getSession().getServletContext().getRealPath(charPath) + "resources" + charPath;
	        for(String attachmentName : attachments){
	        	String key = "templates/".concat(emailTemplates.getId()+"/"+attachmentName);
	        	File file = new File(path+key);
	        	
	        	fileService.getFile(key, path);
	        	
	        	if(file.exists() && file.isFile()){
	        		fileList.add(file);
	        		
	        		BodyPart attachmentBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(file);
					attachmentBodyPart.setDataHandler(new DataHandler(source));
					attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachmentName));	//Avoid distortion
					multipart.addBodyPart(attachmentBodyPart);
	        	}
	        }
        }
        
        message.setContent(multipart);
		
		
		// Send the mail
		mailSender.send(message);

		if(fileList.size() > 0){
			for(File f : fileList){
				fileService.deleteFile(f);
			}
		}
		
		// Save to Email history
		EmailHistory emailHistory = new EmailHistory();
		emailHistory.setUserId(String.valueOf(user.getUserId()));
		emailHistory.setCandidateId(caregiver.getUserId());
		emailHistory.setSubject(message.getSubject());
		emailHistory.setContent(message.getContent().toString());
		emailHistory.setSendDate(message.getSentDate());
		emailHistory.setReceiveEmail(caregiver.getEmail());
		emailHistory.setReceiveName(caregiver.getFullName());
		emailHistory.setSenderEmail(senderAddress);
		emailHistory.setSenderName(user.getFirstName()+" "+user.getLastName());
		emailHistory.setType(0);

		emailHistoryDAO.save(emailHistory);

	}

	/**
	 * create a new user,and send the email to the user's mail,and let him to set his password
	 * 
	 * @param user
	 * @param request 
	 * @throws MessagingException 
	 */
	public void setPwd(User user, HttpServletRequest request) throws MessagingException {
		
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		String toMd5= MD5Util.string2MD5(uuid);
		//String key = MD5Util.convertMD5(toMd5);
		String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		MimeMessage message = mailSender.createMimeMessage();
		Properties mailProperties = mailSender.getJavaMailProperties();

		String fullName = user.getFirstName()+" "+user.getLastName();
		// Set up TO, FROM, CC, SUBJECT, RECEIPIENT
		message.setFrom(new InternetAddress(mailProperties.getProperty("mail.sender")));
		message.setRecipient(RecipientType.TO, new InternetAddress(user.getEmail()));
		message.setSubject("[AGSC]" +fullName+ ", welcome to AGSC");
		message.setSentDate(new Date());

		// Prepare and set content
		message.setContent("Hi " + user.getEmail()+"," 
						+ "<br><br>Your administrator has set up an AGSC account for you! "
						+ "<br><br>Your username is: <strong>" +user.getUsername()+ "</strong>"
						+ "<br><br>This invitation is valid for only 7 days. If the invitation has expired, contact your administrator."
						+ "<br><br>Cheers,"
						+ "<br>The AGSC"
						+ "<br><a class="+"btn btn-primary" + " href =" + url + "/user/setPwd/" + toMd5 + "?username=" + user.getUsername() + ">Set my password</a>",
						"text/html;charset=UTF-8");

		// Send the mail
		mailSender.send(message);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, 7);
		UsersChangePassword changePassword = new UsersChangePassword();
		changePassword.setKey(uuid);	
		changePassword.setUsername(user.getUsername());
		changePassword.setExpiredDate(cal.getTime());
		changePassword.setCreateDate(new Date());
		userDAO.saveKey(changePassword);
		
	}

	public EmailHistory sendMail(EmailHistory emailHistory, String ccEmail, String bccEmail, File file)
			throws IOException, MessagingException {

		MimeMessage message = mailSender.createMimeMessage();
		Properties mailProperties = mailSender.getJavaMailProperties();
		String senderEmail = mailProperties.getProperty("mail.sender");

		// Set up TO, FROM, CC, SUBJECT, RECEIPIENT
		message.setFrom(new InternetAddress(senderEmail, emailHistory.getSenderName()));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailHistory.getReceiveEmail()));

		//cc email address
		if(!StringUtil.isBlank(ccEmail)){
			InternetAddress[] ccList = InternetAddress.parse(ccEmail);
			message.setRecipients(RecipientType.CC, (Address[])ccList);
			
		}
		
		//bcc email address
		if(!StringUtil.isBlank(bccEmail)){
			InternetAddress[] bccList = InternetAddress.parse(bccEmail); 
			message.setRecipients(Message.RecipientType.BCC, (Address[])bccList);
		}
		
		message.setSubject(emailHistory.getSubject());
		message.setSentDate(new Date());

		//send attachment
        Multipart multipart = new MimeMultipart();
        BodyPart contentPart = new MimeBodyPart();
        contentPart.setContent(emailHistory.getContent(), "text/html;charset=UTF-8");
        multipart.addBodyPart(contentPart);
		
        //add attachment
        String attachment = emailHistory.getAttachment();
        if(!StringUtil.isBlank(attachment)){
        	if(file.exists()){
    			File[] fileList = file.listFiles();
    			for(File f : fileList){
    				for(String name : attachment.split(",")){
    					if(name.equals(f.getName())){
    						BodyPart attachmentBodyPart = new MimeBodyPart();
        					DataSource source = new FileDataSource(f);
        					attachmentBodyPart.setDataHandler(new DataHandler(source));
        					attachmentBodyPart.setFileName(MimeUtility.encodeWord(f.getName()));	//Avoid distortion
        					multipart.addBodyPart(attachmentBodyPart);
    					}
    				}
    			}
    		}
        	
        }
        message.setContent(multipart);
        message.saveChanges();
		// Send the mail
		mailSender.send(message);
		
		emailHistory.setSenderEmail(senderEmail);
		emailHistory.setSendDate(message.getSentDate());
		return emailHistory;
	}

	/**
	 * send cv
	 * @param cv
	 * @param sData candidate's id
	 * @param caregiver candidate's content
	 * @param user	user data
	 * @throws Exception 
	 */
	public SendCV sendCV(User user, SendCV cv, String sData, Caregiver caregiver, HttpServletRequest request) throws Exception {

		logger.info("sendCv candidate:"+caregiver);
		logger.info("user:"+user);

		MimeMessage message = mailSender.createMimeMessage();
		Properties mailProperties = mailSender.getJavaMailProperties();
		String senderEmail = user.getEmail() == null ? mailProperties.getProperty("mail.sender") : user.getEmail();
        logger.info("senderEmail:"+senderEmail);

		// Set up TO, FROM, CC, SUBJECT, RECEIPIENT
		message.setFrom(new InternetAddress(senderEmail));
		message.setSender(new InternetAddress(senderEmail));
		String[] sentToArray = cv.getSentTo().split(";");
		for(String sentTo : sentToArray){
			if(sentTo.isEmpty()){
				continue;
			}
			message.addRecipient(RecipientType.TO, new InternetAddress(sentTo));
		}
		message.setSubject(caregiver.getFullName() + " Candidates CV");
		message.setSentDate(new Date());

		//get the html content to the email content 
		String path = request.getSession().getServletContext().getRealPath(charPath);	
		String filePath = path + "WEB-INF" +charPath+ "jsp" +charPath+ "cv" +charPath+ "sentCvToPdf.html";
		String str = readFully(filePath);

		//get photo path        
		String keyPath = path + "resources" +charPath;
		String photoPath="";
		if(caregiver.getPhoto() != null){
			String photo = caregiver.getPhoto().split(",")[0];
			if(!StringUtil.isBlank(photo)){
				String key = caregiver.getUserId() + "/my_photo" + "/" + photo;
				
				//To save the photo Address
				//windows
				photoPath = keyPath + key;
				if("\\".equals(charPath)){   
					photoPath = photoPath.replace("/", "\\");
				} 
				//linux
				if("/".equals(charPath)){   
					photoPath = photoPath.replace("\\", "/");
				}
				
				//download photo to the server
				fileService.getFile(key,keyPath);
				File f = new File(photoPath);
				if(!f.exists()){
					photoPath  = keyPath+ "img" +charPath+ "no_image.png";
				}
			}else{
				photoPath  = keyPath+ "img" +charPath+ "no_image.png";
			}
		}else{
			photoPath  = keyPath + "img" +charPath+ "no_image.png";
		}
		
		//insert data to the html page
		str = str.replace("${caregiver.fullName}",caregiver.getFullName());
		str = str.replace("${caregiver.gender}",caregiver.getGender());
		str = str.replace("${caregiver.educationalLevel}",caregiver.getEducationalLevel());
		str = str.replace("${caregiver.exp}",caregiver.getExp());
		str = str.replace("${caregiver.availability}",caregiver.getAvailability());
		str = str.replace("${caregiver.languages}",caregiver.getLanguages());
		str = str.replace("${caregiver.maritalStatus}",caregiver.getMaritalStatus());
		str = str.replace("${caregiver.age}",caregiver.getAge());
		str = str.replace("${caregiver.siblings}",caregiver.getSiblings());
		str = str.replace("${caregiver.religion}",caregiver.getReligion());
		str = str.replace("${caregiver.foodChoice}",caregiver.getFoodChoice());
		str = str.replace("${caregiver.nationality}",caregiver.getNationality());
		str = str.replace("${caregiver.countryOfBirth}",caregiver.getCountryOfBirth());
		str = str.replace("${caregiver.hasChildren}",caregiver.getHasChildren());
		str = str.replace("${caregiver.weight}",caregiver.getWeight());
		str = str.replace("${caregiver.height}",caregiver.getHeight());
		str = str.replace("${caregiver.motivation}",caregiver.getMotivation());
		
		if(user != null){
			if(user.getRegistrationNumber() != null){
				str = str.replace("${registrationNumber}",user.getRegistrationNumber());
			}else{
				str = str.replace("${registrationNumber}","");
			}
			
			String role = user.getAuthorities().toString();
			
			if(role.contains("ROLE_SALES_HK")){
				str = str.replace("${salary}",caregiver.getSalaryHKD());
				str = str.replace("${expectedSalary}", "Expected salary in HKD");
			}else if(role.contains("ROLE_SALES_TW")){
				str = str.replace("${salary}",caregiver.getSalaryTWD());
				str = str.replace("${expectedSalary}", "Expected salary in TWD");
			}else{
				str = str.replace("${salary}",caregiver.getSalarySGD());
				str = str.replace("${expectedSalary}", "Expected salary in SGD");
			}
		}
		
		//bio
		if(!StringUtil.isBlank(caregiver.getBio().getCandidateBasicInformation())){
			str = str.replace("${caregiver.about}",caregiver.getBio().getCandidateBasicInformation());
		}else{
			str = str.replace("${caregiver.about}","");
		}
		
		if(!StringUtil.isBlank(caregiver.getBio().getEducationAndExperience())){
			str = str.replace("${caregiver.education}",caregiver.getBio().getEducationAndExperience());
		}else{
			str = str.replace("${caregiver.education}","");
		}
		
		if(!StringUtil.isBlank(caregiver.getBio().getTrainedToCprOrFA())){
			str = str.replace("${caregiver.certifiedCpr}",caregiver.getBio().getTrainedToCprOrFA());
		}else{
			str = str.replace("${caregiver.certifiedCpr}","");
		}
		
		if(null != caregiver.getBio().getNursingExperience()){
			str = str.replace("${caregiver.specialities}",caregiver.getBio().getNursingExperience());
		}else{
			str = str.replace("${caregiver.specialities}","");
		}
		
		if(!StringUtil.isBlank(caregiver.getBio().getHobby())){
			str = str.replace("${caregiver.hobbies}",caregiver.getBio().getHobby());
		}else{
			str = str.replace("${caregiver.hobbies}","");
		}

		String formattedDate = "unknown";
		if(caregiver.getDateOfBirth() != null) {
			formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(caregiver.getDateOfBirth());
		}
		str = str.replace("${caregiver.dateOfBirth}", formattedDate);

		BodyPart content = new MimeBodyPart();
		content.setText(cv.getEmailMsg());

		MimeBodyPart attachment = createAttachment(caregiver, str, path, photoPath);
	    MimeMultipart allPart = new MimeMultipart();
	    allPart.addBodyPart(content);
	    allPart.addBodyPart(attachment);
	    message.setContent(allPart);
		
	    message.saveChanges();
		mailSender.send(message);
		
		if(caregiver.getPhoto() != null){
			if(photoPath.contains(caregiver.getUserId())){
				File file = new File(photoPath);
				fileService.deleteFile(file);
			}
		}
		
		cv.setDate(message.getSentDate());
		return cv;
		
	}

	private static String readFully(String filePath)
	{
		String str = "";
		try {
			String tempStr = "";
			FileInputStream is = new FileInputStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
	        while ((tempStr = br.readLine()) != null){
	        	str = str + tempStr ;
	        }
	        is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}


	/**
	   * create the email content and contain picture
	   * 
	   *
	   * @param str
	   * @param caregiver
	 *@param html
	 * @param path
	 * @param photoPath    @return
	 *
	   * @throws Exception
	   */
	  private MimeBodyPart createAttachment(Caregiver caregiver,
											String html,
											String path,
											String photoPath)
	      throws Exception {

	  	byte[] pdf = convertToPdf(html, path, photoPath);

	    MimeBodyPart attachment = new MimeBodyPart();
	    DataSource dataSource = new ByteArrayDataSource(pdf, "application/pdf");
	    attachment.setDataHandler(new DataHandler(dataSource));
	    attachment.setFileName(caregiver.getFullName() + "_Resume.pdf");

	    return attachment;
	  }

	private static  byte[] convertToPdf(String html, String imageRootPath, String photoPath) throws DocumentException, IOException
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LETTER);
		PdfWriter writer = PdfWriter.getInstance(document, outputStream);
		document.open();
		document.addAuthor("Active Global Specialised Caregivers");
		document.addSubject("Resume");
		document.addCreationDate();
		document.addTitle("Resume");

		CSSResolver cssResolver =XMLWorkerHelper.getInstance().getDefaultCssResolver(true);

		// HTML
		HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
		htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
		htmlContext.autoBookmark(false);

		AbstractImageProvider imageProvider = setupImages(imageRootPath, photoPath);
		htmlContext.setImageProvider(imageProvider);

		// Pipelines
		PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
		HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, pdf);
		CssResolverPipeline css = new CssResolverPipeline(cssResolver, htmlPipeline);

		// XML Worker
		XMLWorker worker = new XMLWorker(css, true);
		XMLParser p = new XMLParser(worker);
		p.parse(new StringReader(html));

		// step 5
		document.close();

		return outputStream.toByteArray();
	}

	private static AbstractImageProvider setupImages(String imageRootPath, String photoPath) throws BadElementException, IOException
	{
		AbstractImageProvider imageProvider = new AbstractImageProvider()
		{
			public String getImageRootPath()
			{
				return imageRootPath;
			}
		};
		//replace pictures
		String heardPath = imageRootPath + "resources" +charPath+ "img" +charPath+ "assets-edm" +charPath;

		imageProvider.store("${logoactiveglobalcaregiver}", Image.getInstance(heardPath.concat("logo-active-global-caregiver.jpg")));
		imageProvider.store("${blockquote}", Image.getInstance(heardPath.concat("blockquote.jpg")));
		imageProvider.store("${header-about}", Image.getInstance(heardPath.concat("header-about.jpg")));
		imageProvider.store("${header-education}", Image.getInstance(heardPath.concat("header-education.jpg")));
		imageProvider.store("${header-certified}", Image.getInstance(heardPath.concat("header-certified.jpg")));
		imageProvider.store("${header-nursing-exp}", Image.getInstance(heardPath.concat("header-nursing-exp.jpg")));
		imageProvider.store("${header-hobbies}", Image.getInstance(heardPath.concat("header-hobbies.jpg")));

		if(photoPath != null)
		{
			imageProvider.store("${photo}", Image.getInstance(photoPath));
		}

		return imageProvider;
	}

	public static void main(String[] args) throws IOException, DocumentException
	{
		String file = "D:\\projects\\toptal\\agsc\\SelectorPortal\\src\\main\\webapp\\WEB-INF\\jsp\\cv\\sentCvToPdf.html";
		String str = readFully(file);
		byte[] pdf = convertToPdf(str, "D:\\projects\\toptal\\agsc\\SelectorPortal\\src\\main\\webapp\\", null);
		FileOutputStream fos = new FileOutputStream("test.pdf");
		fos.write(pdf);
		fos.close();
	}
}
