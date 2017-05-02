package com.cloudaxis.agsc.portal.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cloudaxis.agsc.portal.helpers.StringUtil;
import com.cloudaxis.agsc.portal.model.AutomatedEmailsTemplates;
import com.cloudaxis.agsc.portal.model.DocumentFile;
import com.cloudaxis.agsc.portal.model.EmailHistory;
import com.cloudaxis.agsc.portal.model.EmailTemplates;
import com.cloudaxis.agsc.portal.model.User;
import com.cloudaxis.agsc.portal.service.EmailService;
import com.cloudaxis.agsc.portal.service.EmailTemplatesService;
import com.cloudaxis.agsc.portal.service.FileService;
import com.cloudaxis.agsc.portal.service.GmailService;
import com.cloudaxis.agsc.portal.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Damien
 *
 */

@Controller
@RequestMapping("/emailTemplates")
public class EmailTemplatesController {
	
	@Autowired
	Environment env;
	
	@Autowired
	private EmailTemplatesService emailTemplatesService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private GmailService gmailService;
	
	private static final String charPath = File.separator;
	
	/**
	 * save email templates
	 * 
	 * @param userId
	 * @param emailTemplates			template data
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/save")
	public void save(EmailTemplates emailTemplates, String userId, HttpServletResponse response, MultipartHttpServletRequest request) 
			throws IOException{
		//search record number
		List num = emailTemplatesService.get(emailTemplates);
		
		JSONObject json = new JSONObject();
		if(num.size()>0){
			json.put("error", "The template's name already exists!");
		}else{
			String subject = request.getParameter("toSubject");
			String content = request.getParameter("to_content");
			emailTemplates.setSubject(subject);
			emailTemplates.setContent(content);
			
			long id = emailTemplatesService.save(emailTemplates);	
			
			//List<MultipartFile> fileList = request.getFiles("fileList");
			//upload attachment to s3
			String[] attachmentNames = emailTemplates.getAttachment().split(",");
			for(String attachmentName : attachmentNames){
				String path = request.getSession().getServletContext().getRealPath(charPath) + "resources"+charPath+"attachment"+charPath;
				String attachmentPath = path + userId +charPath+ attachmentName;
				File file = new File(attachmentPath);
				if(file.exists()){
					String key = "templates/" +id+ "/" + attachmentName;
					fileService.uploadLocalDocument(file, key);
					
					file.delete();
				}
			}
			/*if(emailTemplates.getAttachment() != null){
				//fileService.uploadTemplatesAttachment(fileList, id);
			}*/
			
			json.put("msg", "Saved successfully!");
		}
		response.getWriter().print(json.toString());
	}
	
	/**
	 * send email and save the email
	 * 
	 * @param emailHistory
	 * @param active
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws MessagingException
	 */
	@RequestMapping("/send")
	public String send(EmailHistory emailHistory, String active, HttpServletRequest request, HttpServletResponse response)
			throws IOException, MessagingException{
		
		//get the email address
		String cc = "";
		String bcc = "";
		if(!StringUtil.isBlank(emailHistory.getCcEmail())){
			String[] ccEmail = emailHistory.getCcEmail().split(",");
			cc = getUserEmail(ccEmail);			
		}
		if(!StringUtil.isBlank(emailHistory.getBccEmail())){
			String[] bccEmail = emailHistory.getBccEmail().split(",");
			bcc = getUserEmail(bccEmail);
		}
		String path = request.getSession().getServletContext().getRealPath(charPath) + "resources"+charPath+"attachment"+charPath;
		String filePath = path + emailHistory.getCandidateId();
		File file = new File(filePath);
		emailService.sendMail(emailHistory, cc, bcc, file);
		long id = emailTemplatesService.saveEmail(emailHistory);
		
		//get the temporary attachment path, upload to s3
		if(file.exists() && id>0){
			File[] fileList = file.listFiles();
			String attachment = emailHistory.getAttachment();
			for(File f : fileList){
				for(String name : attachment.split(",")){
					if(name.equals(f.getName())){
						String key = emailHistory.getCandidateId() + "/attachment/" + id + "/" +f.getName();
						fileService.uploadLocalDocument(f, key);			//upload attachment to S3
					}
				}
				if(f.exists()){
					f.delete();
				}
			}
		}
		return "redirect:/dashboard/getCandidate?userId="+emailHistory.getCandidateId()+"&active="+active;
	}
	
	/**
	 * get the email
	 * 
	 * @param userId
	 * @param emailHistory
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping("/getEmail")
	public void getEmail(EmailHistory emailHistory, String userId, HttpServletResponse response, HttpServletRequest request) 
			throws Exception{
		emailHistory = emailTemplatesService.getEmail(emailHistory);
		String attachment = emailHistory.getAttachment();
		if(emailHistory.getType() == 0){		//0:send email; 1:receive email
			if(!StringUtil.isBlank(attachment)){
				//String path = request.getSession().getServletContext().getRealPath(charPath) + "resources" +charPath;
				String key = userId + "/attachment/" + emailHistory.getId() + "/";
				List<DocumentFile> dfList = new ArrayList<DocumentFile>();	
				fileService.getEmailAttachment(dfList,key,attachment);			//get the attachment data from s3
				
				if(dfList.size()>0){
					emailHistory.setAttachments(dfList);
				}
			}
		}else if(emailHistory.getType() == 1){		//"agsc/mail/id/"
			if(emailHistory.getFlag() != null && emailHistory.getFlag() == 1){			//Flag this email as read			[1:unread, 0:read]
				emailHistory.setFlag(0);
				emailTemplatesService.uploadFlag(emailHistory);
			}
			
			List<DocumentFile> dfList = new ArrayList<DocumentFile>();
			
			if(!StringUtil.isBlank(attachment)){
				String path = request.getSession().getServletContext().getRealPath(charPath) + "resources" +charPath;
				String key = "attachment/" + emailHistory.getId() + "/";
				
				for(String fileName : attachment.split(",")){
					if(!StringUtil.isBlank(emailHistory.getToS3Attachment())){
						if(emailHistory.getToS3Attachment().contains(fileName)){	//this fileName already uploaded to S3
							String key1 = userId + "/attachment/";
							DocumentFile df = new DocumentFile();
							df = fileService.getDocument(key1,fileName);			//download the attachment to the server
							if(df.getName() != null){
								dfList.add(df);
							}
						}else{
							DocumentFile df = new DocumentFile();
							File file = new File(path + key, fileName);
							if(file.exists()){
								df.setName(fileName);
								df.setPath(key);
								df.setSize(file.length());
								df.setType("local");
								dfList.add(df);
							}else{				//the server does not have this attachment,so go to the mail get it
								//get the attachment by the mail's messageID
								gmailService.searchMail(emailHistory.getMessageId(), path + key, fileName);
								
								File f = new File(path + key, fileName); 
								if(f.exists()){
									df.setName(fileName);
									df.setPath(key);
									df.setSize(f.length());
									df.setType("local");
									dfList.add(df);
								}
							}
						}
					}else{			//No one uploaded to S3
						DocumentFile df = new DocumentFile();
						File file = new File(path + key, fileName);
						if(file.exists()){
							df.setName(fileName);
							df.setPath(key);
							df.setSize(file.length());
							df.setType("local");
							dfList.add(df);
						}else{
							gmailService.searchMail(emailHistory.getMessageId(), path + key, fileName);
							
							File f = new File(path + key, fileName); 
							if(f.exists()){
								df.setName(fileName);
								df.setPath(key);
								df.setSize(f.length());
								df.setType("local");
								dfList.add(df);
							}
						}
					}
				}
			}
			
			if(dfList.size()>0){
				emailHistory.setAttachments(dfList);
			}
		}
		//change the emailHistory object to json
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(emailHistory);  
		
		response.getWriter().print(json);
	}
	
	/**
	 * get the email template
	 * 
	 * @param userId
	 * @param emailTemplates
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping("/getTemplate")
	public void getTemplate(EmailTemplates emailTemplates, String userId, HttpServletResponse response, HttpServletRequest request) throws Exception{
		if(!StringUtil.isBlank(emailTemplates.getId())){
			emailTemplates = emailTemplatesService.getTemplate(emailTemplates);
			if(emailTemplates.getAttachment() != null){
				//String path = request.getSession().getServletContext().getRealPath(charPath) + "resources" +charPath;
				String key = "templates/"+emailTemplates.getId()+"/";

				List<DocumentFile> dfList = new ArrayList<DocumentFile>();
				//fileService.downLoadAttachment(dfList, path, key, userId);			//download the attachment to the server
				fileService.downLoadAttachments(emailTemplates.getAttachment(),dfList,key);
				
				if(dfList.size()>0){
					emailTemplates.setAttachments(dfList);
				}
			}
			
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(emailTemplates);  
			
			response.getWriter().print(json);
		}
		
	}
	
	
	/**
	 * delete email
	 * 
	 * @param request
	 * @param response
	 * @param ids
	 * @param active
	 * @param userId
	 * @return
	 */
	@RequestMapping("/delEmail")
	public String delEmail(HttpServletRequest request, HttpServletResponse response,
			String ids, String active, String userId,String type){
		if(ids != null){
			if("0".equals(type)){			//send email
				emailTemplatesService.delEmail(ids);
			}else if("1".equals(type)){		//receive email
				emailTemplatesService.delReceiveEmail(ids);			//change del_flag='0' to del_flag='1'
			}
			fileService.deleteAttachment(ids);       //delete attachments
		}
		return "redirect:/dashboard/getCandidate?userId="+userId+"&active="+active;
	}

	/** get the email list by the user's last_name and first_name
	 */
	private String getUserEmail(String[] emails){
		String email = "";
		for(String ccE : emails){
			String[] cc = ccE.split(" ");			//split first_name and last_name
			if(cc.length == 2){
				User user = userService.getUserByFirstNameAndLastName(cc[0],cc[1]);
				if(user!=null){
					email = !StringUtil.isBlank(email.trim()) ? email + "," + user.getEmail() : user.getEmail();
				}
			}
			else {
				email = !StringUtil.isBlank(email.trim()) ? email + "," + cc[0] : cc[0];
			}
			
		}
		return email;
	}
	
	/**
	 * delete emailTemplate
	 * 
	 * @param id
	 */
	@RequestMapping(value="/deleteTemplate", method = RequestMethod.POST)
	public void deleteTemplate(@RequestParam(value="id") String id, HttpServletResponse response){
		emailTemplatesService.deleteTemplate(id);
		String key = "templates/".concat(id);
		fileService.deleteS3File(key);
	}  
	
	/**
	 * set workflow step to automated emails
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("setEmailTemplate")
	public String setEmailTemplate(Model model){
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<AutomatedEmailsTemplates> list = emailTemplatesService.workflowStepTemplateList();
		List<EmailTemplates> listTemplates = emailTemplatesService.list();
		
		String status = env.getProperty("status");
		List<String> statusList = new ArrayList<String>();
		for(String s : status.split(",")){
			statusList.add(s);
		}
		
		model.addAttribute("user", user);
		model.addAttribute("listTemplates", listTemplates);
		model.addAttribute("list", list);
		model.addAttribute("statusList", statusList);
		return "emailTemplates/emailTemplateForm";
	}
	
	@RequestMapping("editAutomatedEmailsTemplate")
	public void editAutomatedEmailsTemplate(AutomatedEmailsTemplates automatedEmailsTemplates,HttpServletResponse response){
		emailTemplatesService.editAutomatedEmailsTemplate(automatedEmailsTemplates);
	}
	
	/**
	 * edit Template data
	 * 
	 * @param emailTemplates	the template data
	 * @throws IOException 
	 */
	@RequestMapping("editTemplate")
	public String editTemplate(EmailTemplates emailTemplates,String originalAttachment,String userId,String active,HttpServletRequest request) throws IOException{
		if(!StringUtil.isBlank(originalAttachment)){
			for(String fileName : originalAttachment.split(",")){
				if(emailTemplates.getAttachment() != null && emailTemplates.getAttachment() != ""){
					if(!emailTemplates.getAttachment().contains(fileName)){
						String key = "templates/" + emailTemplates.getId() + "/" +fileName;
						fileService.deleteFileFromS3(key);			//delete one file
					}
				}
			}
		}
		
		emailTemplatesService.editEmailTemplate(emailTemplates);
		
		String path = request.getSession().getServletContext().getRealPath(charPath) + "resources"+charPath+"templates"+charPath;
		String filePath = path + emailTemplates.getId();
		File file = new File(filePath);
		
		//get the temporary attachment path, upload to s3
		if(file.exists()){
			File[] fileList = file.listFiles();
			String attachment = emailTemplates.getAttachment();
			for(File f : fileList){
				if(attachment.contains(f.getName())){
					String key = "templates/" + emailTemplates.getId() + "/" +f.getName();
					fileService.uploadLocalDocument(f, key);
				}
				f.delete();
			}
		}
		
		if(file.isDirectory()){
			file.delete();
		}
		return "redirect:/dashboard/getCandidate?userId="+userId+"&active=5";
	}
	
	/**
	 * upload the server file(receive email attachment) to S3
	 * 
	 * @param userId		candidate id
	 * @param path			file key(like: attachment\312\a.jpg)
	 * @param fileName		file name
	 * @param request
	 * @param response
	 * @return				return message(Uploaded successfully or failed)
	 * @throws IOException 
	 */
	@RequestMapping("uploadAttachment")
	public @ResponseBody String uploadAttachment(String userId, String path, String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException{
		String serverPath = request.getSession().getServletContext().getRealPath(charPath) + "resources"+charPath;
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(!StringUtil.isBlank(path)){
			File file = new File(serverPath+path,fileName); 
			if(file.exists()){
				String[] paths = path.split("/");			//get the emailHistory id		like:12/12/
				String id = paths[1];
				EmailHistory emailHistory = new EmailHistory();
				emailHistory.setId(id);
				emailHistory = emailTemplatesService.getEmail(emailHistory);
				
				//upload the attachment to S3
				String key = userId + "/attachment/" + fileName;
				fileService.uploadLocalDocument(file, key);		//upload local attachment
				
				String url = fileService.getFileUrl(key);		//get the file url 
				map.put("msg", "Attachment has been uploaded");
				map.put("url", url);
				
				if(!StringUtil.isBlank(emailHistory.getToS3Attachment())){
					emailHistory.setToS3Attachment(emailHistory.getToS3Attachment().concat(","+fileName));
				}else{
					emailHistory.setToS3Attachment(fileName);
				}
				emailTemplatesService.toS3Attachment(emailHistory);
				
				file.delete();
				fileService.deleteDirectory(new File(serverPath+path));
			}else{
				map.put("msg", "Attachment does not exist");
			}
		}else{
			map.put("msg", "Attachment does not exist");
		}
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(map); 
		
		return json;
	}
}
