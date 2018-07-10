package com.cloudaxis.agsc.portal.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloudaxis.agsc.portal.helpers.StringUtil;
import com.cloudaxis.agsc.portal.model.Bio;
import com.cloudaxis.agsc.portal.model.Caregiver;
import com.cloudaxis.agsc.portal.model.SendCV;
import com.cloudaxis.agsc.portal.model.User;
import com.cloudaxis.agsc.portal.service.EmailService;
import com.cloudaxis.agsc.portal.service.FileService;
import com.cloudaxis.agsc.portal.service.SelectedCaregiverService;
import com.cloudaxis.agsc.portal.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping("/")
public class CaregiverSelectorController extends AbstractController {

	protected Logger			logger	= Logger.getLogger(CaregiverSelectorController.class);

	@Autowired
	Environment					env;

	@Autowired
	SelectedCaregiverService	selectedCaregiverService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private UserService userService;

	private static final String charPath = File.separator;
	
	
	public CaregiverSelectorController() {
		super(CaregiverSelectorController.class);
	}

	/**
	 * Comes from the legacy code. Allows for storing the page number of the
	 * search in the session.
	 * 
	 * @param pageNum
	 * @return
	 */
	@RequestMapping(value = { "/ajax/addPageSession" }, method = RequestMethod.POST)
	public @ResponseBody String addPageSession(
			@RequestParam(value = "page_num", required = true) String pageNum) {

		String msg = "{\"status\":\"success\", \"msg\":\"page number added\"}";

		return msg;

	}

	@RequestMapping("/shortlist/menu")
	public String getShortlistMenu() {

		return "shortlist/menu";
	}

	@RequestMapping(value = { "/shortlist/search" }, method = RequestMethod.GET)
	public String getShortlist(ModelMap model) {
		
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String medical = "";
		String language = "";
		String religion = "";
		String level = "";
		String food = "";
		String marital = "";
		String gender = "";
		String avail = "";
		String age = "";
		String exp = "";

		model.addAttribute("medical", medical);
		model.addAttribute("language", language);
		model.addAttribute("religion", religion);
		model.addAttribute("level", level);
		model.addAttribute("food", food);
		model.addAttribute("marital", marital);
		model.addAttribute("gender", gender);
		model.addAttribute("avail", avail);
		model.addAttribute("age", age);
		model.addAttribute("exp", exp);
		model.addAttribute("user", user);
		
		return "shortlist/search";
	}

	/**
	 * Controller that retrieves the information for the dashboard
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/ajax/caregivers", method = RequestMethod.GET)
	public @ResponseBody String getFilteredCaregivers(
			String filter) {

		String jsonString = selectedCaregiverService.getCaregivers();

		return jsonString;

	}

	/*
	 * @RequestMapping(value = "/ajax/caregiverList", method =
	 * RequestMethod.GET) public @ResponseBody String getCaregiverList(
	 * @RequestParam(value = "userId", required = false) String userId){
	 * //JSONObject jo = new JSONObject(); String data =
	 * selectedCaregiverService.get(userId); //jo.put("data", data);
	 * //System.out.println(jo.toString()); return data; }
	 */

	@RequestMapping(value = "/comment", method = RequestMethod.POST)
	public String postNewComment(@RequestParam("caregiverId") String caregiverId,
			@RequestParam("comment") String comment, HttpServletRequest request, HttpServletResponse response) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		selectedCaregiverService.postNewComment(comment, user, caregiverId);
		return "redirect:dashboard/getCandidate?userId=" + caregiverId + "&active=2";
	}

	@ResponseBody
	@RequestMapping(value="/caregiverSelector/deleteComment",method = RequestMethod.GET)
	public String deleteComment(@RequestParam("id") String id, 
			HttpServletRequest request, HttpServletResponse response){
		selectedCaregiverService.deleteComment(id);
		
		String msg = "{\"msg\":\"The comment has been deleted!\"}";

		return msg;
	}
	
	/**
	 * send cv by email
	 * 
	 * @param sData			//candidate's id
	 * @param client_email	//Addressee 
	 * @param email_msg		//email content
	 * @return
	 * @throws Exception 
	 */
	//@RequestMapping(value = { "/admin/ajax/send_client_cv" }, method = RequestMethod.POST)
	@RequestMapping("/admin/ajax/send_client_cv")
	public @ResponseBody String sendClientCV(@RequestParam(value = "sData", required = true) String sData,
			@RequestParam(value = "client_email", required = true) String client_email,
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "email_msg", required = true) String email_msg, HttpServletRequest request) throws Exception {
		
		List<Caregiver> listCaregiver = selectedCaregiverService.getCaregiverListIds(sData);
		
		SendCV cv = new SendCV();
		cv.setSentTo(client_email);
		cv.setUserId(userId);
		cv.setEmailMsg(email_msg);
		User user = null;
		if(!StringUtil.isBlank(userId)){
			user = userService.getUserById(Integer.valueOf(userId));
		}
		
		for(Caregiver caregiver: listCaregiver){
			emailService.sendCV(user,cv,sData,caregiver,request);							//send emdil
		}
		
		selectedCaregiverService.saveSendCV(cv, sData);		//save data to database
		String msg = "{\"status\":\"success\", \"msg\":\"The mail has been sent out!\"}";

		return msg;

	}
	
	/**	display the sent CV history
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/admin/users/sent_history")
	public String getSendCv(Model model){
		List <SendCV> list= selectedCaregiverService.getSendCv();
		
		model.addAttribute("list", list);
		return "cvHistory/cvHistoryList";
		
	}
	
	/**
	 * get the view CV or print CV
	 * 
	 * @param response
	 * @param tab		determine whether or not print CV
	 * @param model
	 * @param request
	 * @param userId
	 * @param resume		
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/admin/users/view_cv")
	public String getCV(HttpServletRequest request,HttpServletResponse response, String userId, String tab, Model model) throws Exception{
		Caregiver caregiver = selectedCaregiverService.getCaregiverBio(userId);
		if(StringUtils.isNotBlank(caregiver.getBio().getExperienceDetails())){
			caregiver.getBio().setEducationAndExperience(caregiver.getBio().getEducationAndExperience() + caregiver.getBio().getExperienceDetails());;
		}
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String photoPath="";
		if(!StringUtil.isBlank(caregiver.getPhoto())){
			String key = userId + "/my_photo" + "/" + caregiver.getPhoto();
			photoPath = fileService.getFileUrl(key);
			
		}
		List<String> specialitiesList = new ArrayList<String>();
		List<String> languageList = new ArrayList<String>();
		String[] languages = caregiver.getLanguages().split(",");
		
		String[] specialities = null;
		if(!StringUtil.isBlank(caregiver.getSpecialities())){
			specialities = caregiver.getSpecialities().split(",");
			for(String s : specialities){
				specialitiesList.add(s);
			}
		}
		
		for (String language : languages) {
			languageList.add(language);
		}
		caregiver.setLanguageList(languageList);
		caregiver.setSpecialitiesList(specialitiesList);
		
		Bio bio = caregiver.getBio();
		bio.setExperienceList(specialitiesList);
		
		caregiver.setBio(bio);
		model.addAttribute("photoPath", photoPath);
		model.addAttribute("caregiver", caregiver);
		model.addAttribute("user", user);
		
		if("print".equals(tab)){
			return "cv/printCv";
		}else{
			return "cv/viewCv";
		}
	}
	
	/**
	 * upload cv photo
	 * 
	 * @param request
	 * @param response
	 * @param userId			candidate's id
	 * @throws Exception
	 */
	@RequestMapping("admin/users/upload_img")
	public void uploadPhoto(MultipartHttpServletRequest request,HttpServletResponse response, String userId) throws Exception{
		MultipartFile file = request.getFile("userphoto");
		
		Caregiver caregiver = selectedCaregiverService.getCaregiver(userId);
		
		if(!StringUtil.isBlank(caregiver.getPhoto())){
			fileService.deleteFileFromS3(userId+"/my_photo/"+caregiver.getPhoto());
		}/*else{
			caregiver.setPhoto(caregiver.getPhoto().concat(","+file.getOriginalFilename()));
		}*/
		caregiver.setPhoto(file.getOriginalFilename());
		fileService.uploadPhoto(file,userId);			//upload photo to s3
		selectedCaregiverService.updatePhoto(caregiver);
		
		//String error = "{\"status\":\"success\", \"msg\":\"The photo has been uploaded!\"}";
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("msg","The photo has been uploaded!");
		
		response.getWriter().print(jsonObj.toString());
	
	}
	
	/**
	 * download cv pdf
	 * 
	 * @param userId
	 * @throws Exception 
	 */
	@RequestMapping("admin/download/pdf")
	public void downloadPdf(String userId, HttpServletRequest request, HttpServletResponse response) throws Exception{
		Caregiver caregiver = selectedCaregiverService.getCaregiver(userId);
		
		String path = request.getSession().getServletContext().getRealPath(charPath) + "resources" +charPath;
		String photoPath = path + "img" +charPath+ "no_image.png";
		String photo = caregiver.getPhoto();
		if(!StringUtil.isBlank(photo)){
			String[] photos = photo.split(",");
			//photoPath = path + caregiver.getUserId() +charPath+ "my_photo" +charPath+ photos[0];
			
			//get the photo Url from s3
			photoPath = fileService.getFileUrl(caregiver.getUserId() + "/my_photo/" + photos[0]);
		}
		
		//create data
		String downloadPdf = fileService.downloadPdf(caregiver,photoPath,path);
		
		//download pdf
	  	response.setCharacterEncoding("UTF-8");
	    response.setContentType("multipart/form-data");
	  	String displayName = downloadPdf.substring(downloadPdf.lastIndexOf(charPath)+1);
	  	File file = new File(downloadPdf);
	  	response.setHeader("Content-Disposition", "attachment; filename="+new String(displayName.getBytes("gb2312"), "ISO8859-1"));
		response.setHeader("Content-Length", String.valueOf(file.length()));		
		ServletOutputStream output = null;
		FileInputStream fis = null;			
	    try {
	    	fis = new FileInputStream(file);
	    	output = response.getOutputStream();
		  
	    	int buffsize=1024*100;
	    	byte[] buff = new byte[buffsize];
	    	int count = -1;

	    	while((count=fis.read(buff,0,buffsize))!=-1){
	    		output.write(buff, 0, count);
	    		output.flush();
	    	}
	    }catch (IOException e) {
	    	throw e;
	    }finally{
			if(output != null)
			{
			   output.close();
			   output = null;
			}	    	
			if(fis != null)
			{
			   fis.close();
			   fis = null;
			}
	    }
		
	}
	
	/**
	 * crop image
	 * 
	 * @param request			
	 * @param userId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("admin/users/photoCrop")
	public String photoCrop(MultipartHttpServletRequest request, String userId) throws IOException{
		MultipartFile file = request.getFile("image_file");
		String x = request.getParameter("x1");
		String y = request.getParameter("y1");
		String w = request.getParameter("w");
		String h = request.getParameter("h");
		
		String path = request.getSession().getServletContext().getRealPath(charPath) + "resources"+charPath + "temImage" +charPath;	
		File f = fileService.cutImage(path,file, Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(w), Integer.valueOf(h));
		if(f.exists()){
			String key = userId + "/my_photo/"; 
			
			Caregiver caregiver = selectedCaregiverService.getCaregiver(userId);		//get caregiver data
			
			if(!StringUtil.isBlank(caregiver.getPhoto())){
				String newKey = userId+"/other_file/";
				fileService.moveFileFromS3(caregiver.getPhoto(),key,newKey);		//move file to other folder
			}
			caregiver.setPhoto(f.getName());
			fileService.uploadPhotoToS3(f, key);		//upload photo to s3
			selectedCaregiverService.updatePhoto(caregiver);
			
			f.delete();
		}
		
		return "redirect:/admin/users/view_cv?userId="+userId;
	}
}
