package com.cloudaxis.agsc.portal.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FromStringTerm;
import javax.mail.search.MessageIDTerm;
import javax.mail.search.SearchTerm;

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
import com.cloudaxis.agsc.portal.helpers.StringUtil;
import com.cloudaxis.agsc.portal.model.EmailHistory;
import com.google.common.collect.Lists;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

@Service
public class GmailService {
	
	@Autowired
	Environment env;
	
	@Autowired
	private EmailHistoryService emailHistoryService;
	
	private static Logger logger	= Logger.getLogger(GmailService.class);
	
	/**
	 * get the attachment by the mail's messageId
	 * 
	 * @param messageId			mail's messageId
	 * @param path				the file path
	 * @param fileName			get the file by the fileName from mail
	 */
	public void searchMail(String messageId,String path,String fileName){
		try {  
        	String username = env.getProperty("mail.username");
        	String password = env.getProperty("mail.password");
            Properties prop = System.getProperties();  
            prop.put("mail.store.protocol", "imaps");  
      
            Session session = Session.getInstance(prop);  
            IMAPStore store = (IMAPStore) session.getStore("imaps"); // use imap session mechanism to connect server
            
            store.connect("imap.googlemail.com",username, password);
            IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX"); 
            folder.open(Folder.READ_WRITE);  
            
            //get messages from a MessageID of mail
            SearchTerm st = new MessageIDTerm(messageId);
            Message [] messages = folder.search(st);  
            
            for(Message m : messages){
            	MimeMessage msg = (MimeMessage) m;
            	
            	getNoExistAttachment(msg,path,fileName,new StringBuffer());
            }
            
            //release
            if (folder != null){
            	folder.close(true);
            }  
            if (store != null){
            	store.close();
            }  
        } catch (Exception e) {  
        	logger.error(e); 
        }  
	}
	
	/**
	 * 
	 * @param path	attachment path
	 * @param emailAddress	sender's email
	 * @param listMessageId 
	 */
	public void readMail(String path, String emailAddress, List<String> listMessageId){  
        try {  
        	String username = env.getProperty("mail.username");
        	String password = env.getProperty("mail.password");
            Properties prop = System.getProperties();  
            prop.put("mail.store.protocol", "imaps");  
      
            Session session = Session.getInstance(prop);  
            IMAPStore store = (IMAPStore) session.getStore("imaps"); // use imap session mechanism to connect server
            
            store.connect("imap.googlemail.com",username, password);
            IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX"); 
            folder.open(Folder.READ_WRITE);  
            
            //get all unread mail  
            //Message[] messages = folder.getMessages(folder.getMessageCount()-folder.getUnreadMessageCount()+1,folder.getMessageCount());  
            //parseMessage(messages,path,emailAddress); //analysis mail
            
            //get messages from a mail
            SearchTerm st = new FromStringTerm(emailAddress);
            Message [] messages = folder.search(st);  
            parseMessage(messages,path,listMessageId); //analysis mail
            
            //release
            if (folder != null){
            	folder.close(true);
            }  
            if (store != null){
            	store.close();
            }  
        } catch (Exception e) {  
        	logger.error(e); 
        }  
    }  
	
	/**  
     *  
     * @param messages analysis mails
	 * @param listMessageId 
     */    
    private void parseMessage(Message[] messages, String path, List<String> listMessageId) throws MessagingException, IOException {    
        for(Message m : messages){
        	MimeMessage msg = (MimeMessage) m;
        		
        	//get unread message
        	if(!listMessageId.contains(msg.getMessageID())){		
        		
        		EmailHistory emailHistory = new EmailHistory();
        		InternetAddress sender = (InternetAddress) msg.getSender();
        		
	    		emailHistory.setCcEmail(getReceiveAddress(msg, RecipientType.CC));
	    		emailHistory.setSenderEmail(sender.getAddress());
	    		emailHistory.setSenderName(sender.getPersonal());
	    		emailHistory.setReceiveEmail(getReceiveAddress(msg, RecipientType.TO));
	    		emailHistory.setReceiveName(getReceiveName(msg, RecipientType.TO));
	    		emailHistory.setSubject(MimeUtility.decodeText(msg.getSubject()));
	    		emailHistory.setSendDate(msg.getSentDate());
	    		emailHistory.setType(1);		//0:send,1:receive
	    		emailHistory.setFlag(1);		//0:have read mail ,1:unread mail(only send email function)
	    		emailHistory.setMessageId(msg.getMessageID());
	    		
	    		//get the content
	    		StringBuffer content = new StringBuffer();
	    		getMailTextContent(msg, content);
	    		String a = content.toString();
	    		if(a.contains("<body>") && a.contains("</body>")){
	    			emailHistory.setContent(a.substring(a.indexOf("<body>")+6,a.lastIndexOf("</body>")));
	    		}else{
	    			emailHistory.setContent(a);
	    		}
	    		
	    		//save data to database
	    		long id = emailHistoryService.save(emailHistory);
	    		
	    		List<File> lf = Lists.newArrayList();
	    		if (isContainAttachment(msg)) {		//attachment  
	    			StringBuffer attachment = new StringBuffer(); 
	    			
					getAttachment(msg,path+id+File.separator,lf,attachment);			//get the attachment and download to the server
					
			        if(attachment.length() > 0){
			        	if(attachment.lastIndexOf(",") > 0){
			        		attachment.deleteCharAt(attachment.length()-1);
			        	}
			        }
					
					emailHistory.setAttachment(attachment.toString());
					
					emailHistory.setId(String.valueOf(id));
					emailHistoryService.updateAttachment(emailHistory);		//update the attachment
	            }
	    		
	    		/*if(lf.size() > 0){		//upload file to s3
					uploadAttachmentToS3(lf,id);
				}*/
	    		
	    		msg.setFlag(Flags.Flag.SEEN, true);
        	}
        	/*MimeMessage msg = (MimeMessage) m;   
        	InternetAddress sender = (InternetAddress) msg.getSender();
        	Flags flags = msg.getFlags();
        	if(emailAddress.equals(sender.getAddress()) && !flags.contains(Flags.Flag.SEEN)){
        		
	    		EmailHistory emailHistory = new EmailHistory();
	    		emailHistory.setCcEmail(getReceiveAddress(msg, RecipientType.CC));
	    		emailHistory.setSenderEmail(sender.getAddress());
	    		emailHistory.setSenderName(sender.getPersonal());
	    		emailHistory.setReceiveEmail(getReceiveAddress(msg, RecipientType.TO));
	    		emailHistory.setReceiveName(getReceiveName(msg, RecipientType.TO));
	    		emailHistory.setSubject(MimeUtility.decodeText(msg.getSubject()));
	    		emailHistory.setSendDate(msg.getSentDate());
	    		emailHistory.setType(1);		//0:send,1:receive
	    		
	    		//get the content
	    		StringBuffer content = new StringBuffer();
	    		getMailTextContent(msg, content);
	    		String a = content.toString();
	    		//emailHistory.setContent(content.toString());
	    		emailHistory.setContent(a.substring(a.indexOf("<body>")+6,a.lastIndexOf("</body>")));
	    		
	    		//save data to database
	    		long id = emailHistoryService.save(emailHistory);
	    		
	    		List<File> lf = Lists.newArrayList();
	    		if (isContainAttachment(msg)) {		//attachment  
	    			StringBuffer attachment = new StringBuffer(); 
	    			
					getAttachment(msg,path+id,lf,attachment);			//get the attachment 
					
					emailHistory.setAttachment(attachment.toString());
	            }
	    		
	    		
	    		if(lf.size() > 0){		//upload file to s3
					uploadAttachmentToS3(lf,id);
				}
	    		
	    		msg.setFlag(Flags.Flag.SEEN, true);
        	}*/
        	
        }  
        
    }  
    
    // Upload files to S3, return S3 file informations
	private void uploadAttachmentToS3(List<File> files, long id) {
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
				
				if(file.isFile() && file.exists()){
					file.delete();
				}
				
			}
		}
		catch (AmazonServiceException ase) {
		}
		catch (AmazonClientException ace) {
		}
	}

	/**  
     * get the receiver address
     * <p>Message.RecipientType.TO  receiver</p>  
     * <p>Message.RecipientType.CC  cc</p>  
     * <p>Message.RecipientType.BCC bcc</p>  
     * @param msg 		mail content
     * @param type 		mail type  
     * @return  
     * @throws MessagingException  
     */    
    private String getReceiveAddress(MimeMessage msg, Message.RecipientType type) throws MessagingException {    
        StringBuffer receiveAddress = new StringBuffer();    
        Address[] addresss = msg.getRecipients(type);    
         
        if(null != addresss && addresss.length >0){
        	for (Address address : addresss) {    
        		InternetAddress internetAddress = (InternetAddress)address;    
        		receiveAddress.append(internetAddress.getAddress()).append(",");    
        	}    
        	if(receiveAddress.length() > 0){
        		receiveAddress.deleteCharAt(receiveAddress.length()-1); 
        	}    
        }
            
        return receiveAddress.toString();    
    }   
    
    private String getReceiveName(MimeMessage msg, Message.RecipientType type) throws MessagingException {    
        StringBuffer receiveAddress = new StringBuffer();    
        Address[] addresss = msg.getRecipients(type);    
         
        if(null != addresss && addresss.length >0){
        	for (Address address : addresss) {    
        		InternetAddress internetAddress = (InternetAddress)address; 
        		if(!StringUtil.isBlank(internetAddress.getPersonal())){
        			receiveAddress.append(internetAddress.getPersonal()).append(",");    
        		}else{
        			receiveAddress.append("Active Global Specialised Caregivers").append(",");
        		}
        	}    
        	if(receiveAddress.length() > 0){
        		receiveAddress.deleteCharAt(receiveAddress.length()-1); 
        	}    
        }
            
        return receiveAddress.toString();    
    }   
    
    /**  
     * get the attachment 
     * @param msg mail content  
     * @return true/false 
     * @throws MessagingException  
     * @throws IOException  
     */    
    private boolean isContainAttachment(Part part) throws MessagingException, IOException {    
        boolean flag = false;    
        if (part.isMimeType("multipart/*")) {    
            MimeMultipart multipart = (MimeMultipart) part.getContent();    
            int partCount = multipart.getCount();    
            for (int i = 0; i < partCount; i++) {    
                BodyPart bodyPart = multipart.getBodyPart(i);    
                String disp = bodyPart.getDisposition();    
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {    
                    flag = true;    
                } else if (bodyPart.isMimeType("multipart/*")) {    
                    flag = isContainAttachment(bodyPart);    
                } else {    
                    String contentType = bodyPart.getContentType();    
                    if (contentType.indexOf("application") != -1) {    
                        flag = true;    
                    }      
                        
                    if (contentType.indexOf("name") != -1) {    
                        flag = true;    
                    }     
                }    
                    
                if (flag) break;    
            }    
        } else if (part.isMimeType("message/rfc822")) {    
            flag = isContainAttachment((Part)part.getContent());    
        }    
        return flag;    
    }    
      
    private String getAttachment(Part part, String destDir, List<File> lf, StringBuffer attachment) throws UnsupportedEncodingException, MessagingException,    
            FileNotFoundException, IOException {    
    	
        if (part.isMimeType("multipart/*")) {    
            Multipart multipart = (Multipart) part.getContent();  
           
            //multiple attachment
            int partCount = multipart.getCount();    
            for (int i = 0; i < partCount; i++) {    
                BodyPart bodyPart = multipart.getBodyPart(i);    

                String disp = bodyPart.getDisposition(); 
                String fileName = bodyPart.getFileName();
                if(!StringUtil.isBlank(fileName)){			//decode special characters
                	fileName = MimeUtility.decodeText(bodyPart.getFileName());
                }
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                	if(attachment.indexOf(fileName) > -1){
                		fileName = fileName.replace(".", "(".concat(String.valueOf(i)).concat(")."));
                	}
                    attachment.append(fileName).append(",");
                  //save to local
                    File file = saveFile(bodyPart.getInputStream(), destDir, fileName);    
        			lf.add(file);
                    
                } else if (bodyPart.isMimeType("multipart/*")) {    
                	getAttachment(bodyPart,destDir,lf,attachment);    
                } else {    
                    String contentType = bodyPart.getContentType();    
                    if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {   
                    	if(attachment.indexOf(fileName) > -1){
                    		fileName = fileName.replace(".", "(".concat(String.valueOf(i)).concat(")."));
                    	}
                    	attachment.append(fileName).append(",");
                    	//save to local
            			File file = saveFile(bodyPart.getInputStream(), destDir, fileName);    
            			lf.add(file);
                    }    
                }    
            }    
        } else if (part.isMimeType("message/rfc822")) {    
        	getAttachment((Part) part.getContent(),destDir,lf,attachment);    
        } 
        
        return attachment.toString();
    }  

    private void getNoExistAttachment(Part part, String destDir, String originalAttachment, StringBuffer attachment) throws UnsupportedEncodingException, MessagingException,    
	    FileNotFoundException, IOException {    
	
		if (part.isMimeType("multipart/*")) {    
		    Multipart multipart = (Multipart) part.getContent();  
		   
		    //multiple attachment
		    int partCount = multipart.getCount();    
		    for (int i = 0; i < partCount; i++) {    
		        BodyPart bodyPart = multipart.getBodyPart(i);    
		
		        String disp = bodyPart.getDisposition(); 
		        String fileName = bodyPart.getFileName();
		        if(!StringUtil.isBlank(fileName)){			//decode special characters
		        	fileName = MimeUtility.decodeText(bodyPart.getFileName());
		        }
		        if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
		        	if(attachment.indexOf(fileName) > -1){
		        		fileName = fileName.replace(".", "(".concat(String.valueOf(i)).concat(")."));
		        	}
		            attachment.append(fileName).append(",");
	
		            //save to local
		            if(originalAttachment.equals(fileName)){
		            	saveFile(bodyPart.getInputStream(), destDir, fileName);    
		            }
		            
		        } else if (bodyPart.isMimeType("multipart/*")) {    
		        	getNoExistAttachment(bodyPart,destDir,originalAttachment,attachment);    
		        } else {    
		            String contentType = bodyPart.getContentType();    
		            if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {   
		            	if(attachment.indexOf(fileName) > -1){
		            		fileName = fileName.replace(".", "(".concat(String.valueOf(i)).concat(")."));
		            	}
		            	attachment.append(fileName).append(",");
		            	//save to local
		            	if(originalAttachment.equals(fileName)){
			            	saveFile(bodyPart.getInputStream(), destDir, fileName);    
			            } 
		            }    
		        }    
		    }    
		} else if (part.isMimeType("message/rfc822")) {    
			getNoExistAttachment((Part) part.getContent(),destDir,originalAttachment,attachment);    
		} 
	} 
    
    private File saveFile(InputStream is, String destDir, String fileName)    
            throws FileNotFoundException, IOException { 
    	
    	File file = new File(destDir + fileName);
    	
    	if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
    	
        BufferedInputStream bis = new BufferedInputStream(is);    
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));    
        int len = -1;    
        while ((len = bis.read()) != -1) {    
            bos.write(len);  
            bos.flush();  
        }  
        bos.close();    
        bis.close();    

        return file;
    }   
    
    
    /**  
     * get the content
     * @param part 
     * @param content 
     * @throws MessagingException  
     * @throws IOException  
     */    
    public void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {  
    	
        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;     
        if (part.isMimeType("text/HTML") && !isContainTextAttach) {    
            content.append(part.getContent().toString());    
        } else if (part.isMimeType("message/rfc822")) {     
            getMailTextContent((Part)part.getContent(),content);    
        } else if (part.isMimeType("multipart/*")) {    
            Multipart multipart = (Multipart) part.getContent();    
            int partCount = multipart.getCount();    
            for (int i = 0; i < partCount; i++) {    
                BodyPart bodyPart = multipart.getBodyPart(i);    
                getMailTextContent(bodyPart,content);    
            }    
        }   
        
    }  
}
    
