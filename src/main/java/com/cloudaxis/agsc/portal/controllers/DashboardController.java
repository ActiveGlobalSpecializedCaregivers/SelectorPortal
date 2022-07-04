package com.cloudaxis.agsc.portal.controllers;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloudaxis.agsc.portal.helpers.DefaultResumeMultipartFile;
import com.cloudaxis.agsc.portal.helpers.ExportCaregiverlUtils;
import com.cloudaxis.agsc.portal.helpers.RandomCodeUtils;
import com.cloudaxis.agsc.portal.helpers.StringUtil;
import com.cloudaxis.agsc.portal.model.AutomatedEmailsTemplates;
import com.cloudaxis.agsc.portal.model.Bio;
import com.cloudaxis.agsc.portal.model.Caregiver;
import com.cloudaxis.agsc.portal.model.Comment;
import com.cloudaxis.agsc.portal.model.DocumentFile;
import com.cloudaxis.agsc.portal.model.EmailHistory;
import com.cloudaxis.agsc.portal.model.EmailTemplates;
import com.cloudaxis.agsc.portal.model.EvaluationModel;
import com.cloudaxis.agsc.portal.model.EvaluationTemplate;
import com.cloudaxis.agsc.portal.model.Profile;
import com.cloudaxis.agsc.portal.model.SearchHistory;
import com.cloudaxis.agsc.portal.model.SearchParamOfApplicant;
import com.cloudaxis.agsc.portal.model.User;
import com.cloudaxis.agsc.portal.model.Workflow;
import com.cloudaxis.agsc.portal.service.AssessmentService;
import com.cloudaxis.agsc.portal.service.EmailHistoryService;
import com.cloudaxis.agsc.portal.service.EmailService;
import com.cloudaxis.agsc.portal.service.EmailTemplatesService;
import com.cloudaxis.agsc.portal.service.FileService;
import com.cloudaxis.agsc.portal.service.GmailService;
import com.cloudaxis.agsc.portal.service.SearchHistoryService;
import com.cloudaxis.agsc.portal.service.SelectedCaregiverService;
import com.cloudaxis.agsc.portal.service.UserService;
import com.cloudaxis.agsc.portal.service.WorkflowService;
import com.cloudaxis.agsc.portal.validator.UserValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.WriterException;
import com.mysql.jdbc.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import static com.mysql.jdbc.StringUtils.isNullOrEmpty;

@Controller
@RequestMapping("/")
public class DashboardController extends AbstractController {

	protected Logger logger = Logger.getLogger(DashboardController.class);

	@Autowired
	Environment env;

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private SelectedCaregiverService selectedCaregiverService;

	@Autowired
	private UserService userService;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private EmailTemplatesService		emailTemplatesService;

	@Autowired
	private SearchHistoryService searchHistoryService;

	@Autowired
	private FileService fileService;

	@Autowired
	private AssessmentService assessmentService;

	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private EmailHistoryService emailHistoryService;
	
	@Autowired
	private GmailService gmailService;
	
	private static final String charPath = File.separator;
	
	public DashboardController() {
		super(DashboardController.class);
	}

	/**
	 * Controller that retrieves the information for the dashboard
	 * 
	 * @param searchHistory 
	 * @param tag return search datas
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/candidates" }, method = RequestMethod.GET)
	public String getDashboard(ModelMap model, HttpServletResponse response, HttpServletRequest request, 
			String tag, SearchHistory searchHistory, String searchStatus) {
		String[] searchList = request.getParameterValues("searchList"); // get the search condition
		List<Caregiver> caregiverList = null;
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(searchHistory != null && searchHistory.getId() != null){
			searchHistory = searchHistoryService.get(searchHistory);
			searchList = searchHistory.getQuery().split(",");
			model.addAttribute("searchHistory", searchHistory);
		}

		List<SearchParamOfApplicant> searchParamList = new ArrayList<SearchParamOfApplicant>();
		logger.info("/candidates, searchList=" + Arrays.toString(searchList));
		if (searchList != null) {

			List<String> criteria = new ArrayList<>();
			String[] crit = searchList[0].split(",");
			for (int i = 0; i < crit.length; i++) {
				if(!isNullOrEmpty(crit[i])){
					criteria.add(crit[i].substring(2, crit[i].length()));
					if(!isNullOrEmpty(crit[i])){
						SearchParamOfApplicant searchParamOfApplicant = new SearchParamOfApplicant();
						String[] paramInfo = crit[i].split("=");
						searchParamOfApplicant.setQuestionFlag(paramInfo[0].substring(0, 1));
						searchParamOfApplicant.setTitle(paramInfo[0]);
						searchParamOfApplicant.setCondition(paramInfo[1]);
						if(paramInfo.length > 2){
							searchParamOfApplicant.setInput(paramInfo[2]);	
						}
						searchParamList.add(searchParamOfApplicant);
					}
				}
			}
			
			caregiverList = selectedCaregiverService.searchByProfileFilter(criteria);		

		}
		else {
			caregiverList = selectedCaregiverService.caregiverList();
		}

		//run saved search
		List<SearchHistory> shlist = searchHistoryService.getList();

		model.addAttribute("shlist", shlist);
		model.addAttribute("user",user);
		model.addAttribute("list", caregiverList);
		model.addAttribute("tag", tag);
		model.addAttribute("searchStatus", searchStatus);
		model.addAttribute("selectStatusList", emailTemplatesService.workflowStepTemplateList());
		
		String jsonString = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			jsonString = mapper.writeValueAsString(searchParamList);
			model.addAttribute("searchParamList", jsonString);
		} catch (JsonProcessingException e) {
			logger.error("JSON data could not be created for the search parameters", e);
		}
		if(searchList !=null && searchList.length>0) {
			model.addAttribute("searchList", searchList[0]);
		}
		return "candidates/list";

	}
	
	@RequestMapping(value = { "/candidates/search/exprot" }, method = RequestMethod.POST)
	public void exportSearchCandidates(ModelMap model, HttpServletResponse response, HttpServletRequest request, 
			String tag, SearchHistory searchHistory, String searchStatus) {
		String[] searchList = request.getParameterValues("searchList"); // get the search condition
		List<Caregiver> caregiverList = null;
		if (searchList != null) {
			List<String> criteria = new ArrayList<>();
			String[] crit = searchList[0].split(",");
			for (int i = 0; i < crit.length; i++) {
				if(!isNullOrEmpty(crit[i])){
					criteria.add(crit[i].substring(2, crit[i].length()));
				}
			}
			caregiverList = selectedCaregiverService.searchByProfileFilter(criteria);		
		}
		else {
			caregiverList = selectedCaregiverService.caregiverList();
		}
		new ExportCaregiverlUtils().exportCaregiverToExcel(response, caregiverList);
	}
	
	@RequestMapping("candidates/search")
	public void saveCandidateSearch(@RequestParam("searchTitle") String searchTitle,
			@RequestParam("searchList") String searchList, @RequestParam("shareWithOthers") String shareWithOthers,
			HttpServletResponse response, HttpServletRequest request) throws IOException {

		List<?> list= searchHistoryService.getListNum(searchTitle);
		JSONObject jsonObj = new JSONObject();
		if(list.size() > 0){
			jsonObj.put("msg","The search name already exists!");
			jsonObj.put("flag", true);
		}else{
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			selectedCaregiverService.saveSearch(searchTitle, user, shareWithOthers, searchList);
			jsonObj.put("msg","Saved successfully!");
		}
		
		response.getWriter().print(jsonObj.toString());
	}

	@RequestMapping(value = { "/admin/users" }, method = RequestMethod.GET)
	public String showUsers(ModelMap model) {
		List<User> users = userService.getUsers();
		model.addAttribute("users", users);
		return "admin/userList";

	}

	// return the apply page
	@RequestMapping("apply")
	public String apply() {
		return "application/apply";
	}

	/**
	 * Save application form data supplied by the applicant
	 * 
	 * @param request
	 * @param response
	 * @param profile
	 *            applicas's information
	 * @param model
	 * @return
	 * @throws Exception
	 * 
	 * @author Damien
	 */
	@RequestMapping("profileAdd")
	public String createNewProfile(MultipartHttpServletRequest request, HttpServletResponse response, Profile profile,
			Model model) throws Exception {
		profile = selectedCaregiverService.createNewProfile(request, response, profile);
		model.addAttribute("profile", profile);

		return "/application/received";
	}

	/**
	 * Check if the user exists by mailbox
	 * 
	 * @param email			new candidate mail
	 * @return
	 */
	@RequestMapping("checkCandidate")
	public @ResponseBody String checkCandidate(@RequestParam(value = "email", required = true) String email){
		int count = selectedCaregiverService.checkExistCandidate(email);
		
		String msg = null;
		if(count > 0){
			 msg = "{\"status\":\"error\", \"msg\":\"The mailbox already exists!\"}";
		}else{
			msg = "{\"status\":\"success\"}";
		}
		return msg;
	}
	
	
	/**
	 * get the candidate's
	 * profile,questionnaire,comments,emails'history,status'history and files
	 * 
	 * Get active applicant details with profile,questionnaire,comments,email history,status history and files tabs
	 * @param caregiver    User's profile information
	 * @param model    According to the user's APP_ID returns the user's one of resume
	 * @param active for binding tab(0:PROFILE,1:QUESTIONNAIRE,2:COMMENTS,3:EMAILS,4:FILES,5:HISTORY)
	 * @return
	 * @param request
	 * @param response
	 * @throws Exception
	 * 
	 * @author Damien
	 */
	@RequestMapping("dashboard/getCandidate")
	public String getCandidate(String active, HttpServletRequest request, HttpServletResponse response,
			Caregiver caregiver, Model model) throws Exception {

		String path = request.getSession().getServletContext().getRealPath(charPath) + "resources" + charPath;
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<AutomatedEmailsTemplates> selectStatusList = emailTemplatesService.workflowStepTemplateList();
		
		if (caregiver != null && caregiver.getUserId() != null) {
			caregiver = selectedCaregiverService.getCaregiver(caregiver.getUserId());
			
			String caregiverBeforeExp = caregiver.getCaregiverBeforeExp();
			if(!isNullOrEmpty(caregiverBeforeExp)){
				if(caregiverBeforeExp.contains("*")){
					caregiver.setCaregiverBeforeExp(caregiverBeforeExp.replaceAll("\\*", ""));
				}
			}
			if(StringUtil.isBlank(active) || "0".equals(active)){		//profile
				active = "0";
				model.addAttribute("userJson", "[]");
				/*if(!StringUtil.isBlank(caregiver.getResume())){
					String key = caregiver.getUserId().concat("/resume/"+caregiver.getResume());
					String url = fileService.getFileUrl(key);
					caregiver.setResumeUrl(url);
				}*/
				if(!StringUtil.isBlank(caregiver.getResume())){
					String key = caregiver.getUserId() +"/resume/"+ caregiver.getResume();
					fileService.getFile(key, path);
				}
				
			}else if("1".equals(active)){		//questionnaire
				List<String> languageList = new ArrayList<String>();
				List<String> diagnosedList = new ArrayList<String>();
				List<String> specialitiesList = new ArrayList<String>();

				String[] languages = caregiver.getLanguages().split(",");
				String[] diagnosedConditions = caregiver.getDiagnosedConditions().split(",");
				String[] specialities = caregiver.getSpecialities().split(",");
				for (String language : languages) {
					languageList.add(language);
				}
				for (String dc : diagnosedConditions) {
					diagnosedList.add(dc);
				}
				for(String s : specialities){
					specialitiesList.add(s);
				}
				caregiver.setLanguageList(languageList);
				caregiver.setDiagnosedList(diagnosedList);
				caregiver.setSpecialitiesList(specialitiesList);
				model.addAttribute("userJson", "[]");
			}else if("2".equals(active)){		//comments
				List<Comment> comments = null;
				String userInitials = String.valueOf(user.getFirstName().charAt(0))
						+ String.valueOf(user.getLastName().charAt(0));
				comments = selectedCaregiverService.getCommentsByCaregiverId(Integer.valueOf(caregiver.getUserId()));
				
				model.addAttribute("userInitials", userInitials);
				model.addAttribute("comments", comments);
				model.addAttribute("userJson", "[]");
			}else if("3".equals(active)){		//assessment
				// evaluation template
				List<EvaluationModel> evaluationModelList = assessmentService.getEvaluationAnswerListByApplicationId(caregiver.getUserId().toString());
				List<EvaluationTemplate> evaluationTemplateList = getEvaluationTemplateList(evaluationModelList);
				model.addAttribute("evaluationTemplateList", evaluationTemplateList);
				model.addAttribute("evaluationModelList", evaluationModelList);
				model.addAttribute("userJson", "[]");
			}else if("4".equals(active)){       //bio
				Bio bio = selectedCaregiverService.getBioByCaregiverId(caregiver);
				model.addAttribute("bio", bio);
				model.addAttribute("userJson", "[]");
			}else if("5".equals(active)){		//email
				//get the email history messageId (receive email) 
				List<String> listMessageId = emailHistoryService.getMessageIds(caregiver.getEmail());
				
				gmailService.readMail(path.concat("attachment"+charPath), caregiver.getEmail(),listMessageId);		//get unread email(server inbox)
				
				// email history
				List<EmailHistory> emailList = emailTemplatesService.emailList(caregiver);
				
				//email receive history
				List<EmailHistory> receiveEmailList = emailHistoryService.receiveEmailList(caregiver.getEmail());
				
				//get the new receive mail count
				int count = emailHistoryService.getReceiveCount(caregiver.getEmail(),1);		//0:have read mail; 1:unread mail
				
				// user List
				List<User> userList = userService.getUsers();
				List<Map<String, String>> listUserMap = new ArrayList<>();
				for (User u : userList) {
					Map<String, String> userMap = new HashMap<String, String>();
					userMap.put("id", String.valueOf(u.getUserId()));
					userMap.put("name", u.getFirstName() + " " + u.getLastName() + "[" + u.getEmail() + "]");
					listUserMap.add(userMap);
				}
				
				// templates list
				List<EmailTemplates> templateList = emailTemplatesService.list();
				
				//AGSC users
				ObjectMapper mapper = new ObjectMapper();
				String userJson = mapper.writeValueAsString(listUserMap);
				
				model.addAttribute("userJson", userJson);			
				model.addAttribute("templateList", templateList);
				model.addAttribute("userList", userList);
				model.addAttribute("emailList", emailList);
				model.addAttribute("receiveEmailList", receiveEmailList);
				model.addAttribute("count", count);
			}else if("6".equals(active)){		//document
				//get the all files
				List<DocumentFile> dfList = new ArrayList<DocumentFile>();
				dfList = fileService.downloadCaregiverDocuments(path, caregiver, dfList);
				model.addAttribute("dfList",dfList);
				model.addAttribute("userJson", "[]");
			}else if("7".equals(active)){		//history
				// find workflow history by the userId
				List<Workflow> workflowList = new ArrayList<Workflow>();
				workflowList = workflowService.list(caregiver.getUserId());
				for (Workflow w : workflowList) {
					if("status".equals(w.getWorkflowType())){
						for(AutomatedEmailsTemplates a : selectStatusList){
							if(w.getStatusFrom().equals(a.getId())){
								w.setStatusFromVal(a.getName());
							}
							if(w.getStatusTo().equals(a.getId())){
								w.setStatusToVal(a.getName());
							}
						}
					}else{
						w.setStatusFromVal(w.getStatusFrom());
						w.setStatusToVal(w.getStatusTo());
					}
				}
				
				model.addAttribute("workflowList", workflowList);
				model.addAttribute("userJson", "[]");
			}
		}

		model.addAttribute("caregiver", caregiver);
		model.addAttribute("active", active);
		model.addAttribute("selectStatusList", selectStatusList);
		model.addAttribute("loginUser", user);

		return "dashboard/dashboardList";
	}

	private List<EvaluationTemplate> getEvaluationTemplateList(List<EvaluationModel> evaluationModelList) {
		List<EvaluationTemplate> userTemplateList = new ArrayList<EvaluationTemplate>();
		List<EvaluationTemplate> templateList = assessmentService.getTemplateList();
		Map<String, String> userEvaluationMap = new HashMap<String, String>();
		for(EvaluationModel model : evaluationModelList){
			userEvaluationMap.put(model.getTemplateName(), model.getTemplateName());
		}
		for(EvaluationTemplate template : templateList){
			if(!userEvaluationMap.containsKey(template.getName())){
				userTemplateList.add(template);
			}
		}
		return userTemplateList;
	}

	//get the applicant all files
	@SuppressWarnings("unused")
	private Map<String, String> fileMap(Map<String, String> fileMap, Caregiver caregiver) {
		
		for(String degrees : caregiver.getPhotoDegrees().split(",")){
			fileMap.put(degrees, "degrees" +charPath+ degrees);
		}
		for(String resume : caregiver.getResume().split(",")){
			//fileMap.put(resume.substring(resume.lastIndexOf("/")+1, resume.length()), resume.substring(resume.indexOf("/")+1, resume.length()));
			fileMap.put(resume, "resume" +charPath+ resume);
		}
		for(String my_photo : caregiver.getPhoto().split(",")){
			fileMap.put(my_photo, "my_photo" +charPath+ my_photo);
		}
		for(String other_file : caregiver.getOtherFiles().split(",")){
			fileMap.put(other_file, "other_file" +charPath+ other_file);
		}
		for(String passport : caregiver.getPhotoPassport().split(",")){
			fileMap.put(passport, "passport" +charPath+ passport);
		}
		
		return fileMap;
	}

	/**
	 * edit the questionnaire
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param caregiver
	 *            the candidate's datas
	 *            
	 * @author Damien
	 */
	@RequestMapping("dashboard/candidateForm")
	public String candidateForm(String active, Caregiver caregiver, HttpServletRequest request,
			HttpServletResponse response, Model model) throws ParseException {
		List<String> languageList = caregiver.getLanguageList();
		List<String> diagnosedList = caregiver.getDiagnosedList();
		List<String> specialitiesList = caregiver.getSpecialitiesList();
		String languages = "";
		String diagnosedConditions = "";
		String specialities = "";

		for (int i = 0; i < languageList.size(); i++) {
			if (i < languageList.size() - 1) {
				languages = languages + languageList.get(i) + ",";
			}
			else {
				languages = languages + languageList.get(i);
			}
		}
		for (int i = 0; i < diagnosedList.size(); i++) {
			if (i < diagnosedList.size() - 1) {
				diagnosedConditions = diagnosedConditions + diagnosedList.get(i) + ",";
			}
			else {
				diagnosedConditions = diagnosedConditions + diagnosedList.get(i);
			}
		}
		for(int i=0; i<specialitiesList.size() ;i++){
			if (i < specialitiesList.size() - 1) {
				specialities = specialities + specialitiesList.get(i) + ",";
			}
			else {
				specialities = specialities + specialitiesList.get(i);
			}
		}

		caregiver.setDiagnosedConditions(diagnosedConditions);
		caregiver.setLanguages(languages);
		caregiver.setSpecialities(specialities);
		selectedCaregiverService.save(caregiver);		//update the question
		// update bio
		Bio bio = selectedCaregiverService.combineBioByCaregiverId(caregiver.getUserId());
		bio.setWorkInHK(caregiver.getWorkInHK());
		bio.setWorkInSG(caregiver.getWorkInSG());
		bio.setWorkInTW(caregiver.getWorkInTW());
		bio.setNumberOfPlacements(caregiver.getNumbersOfPlacement());
		selectedCaregiverService.editBioInfoByCaregiverId(bio);
		return "redirect:getCandidate?userId=" + caregiver.getUserId() + "&active=" + active;
	}

	/**
	 * edit the workflow status and send email
	 * 
	 * @param changeStatus
	 *            the application's status
	 * @param caregiver
	 *            the candidate datas
	 * @param changeStatus
	 *            the status for number
	 * @param emailHistory
	 *            use to save email
	 * @param toEmail
	 *            the addressee's address
	 * @param emailTemplates
	 *            use to save email template
	 * @param active
	 *            use to return tab index
	 *            
	 * @author Damien
	 */
	@RequestMapping("dashboard/changeStatus")
	public String changeStatus(EmailHistory emailHistory, String toEmail, String active, EmailTemplates emailTemplates,
			Caregiver caregiver, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		selectedCaregiverService.editStatus(caregiver, user, emailTemplates.getTemplateType());

		//Send notification email
		if(!StringUtil.isBlank(caregiver.getEmail()) && !StringUtil.isBlank(emailTemplates.getTemplateType())){
			emailTemplates = emailTemplatesService.getAutomateEmailTemplate(emailTemplates.getTemplateType());

			if(null != emailTemplates.getName()){
				emailService.sendShortlistedNotification(request, emailTemplates, caregiver, user);
			}
		}
	
		return "redirect:getCandidate?userId="+caregiver.getUserId()+"&active="+active;
	}

	//resolve date format
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	/**
	 * return the forgot password home page
	 * 
	 * @author Damien
	 * @return
	 */
	@RequestMapping("user/forgot_password")
	public String forgotPwd() {

		return "forgot/forgotPwd";
	}

	/**
	 * return the page to send security key by email
	 *
	 * @return
	 */
	@RequestMapping("user/send_security_code_email")
	public String emailSecurityKey() {

		return "forgot/sendSecurityCodeEmail";
	}

	/**
	 * verify the mail code is correct
	 * 
	 * @author Damien
	 * @return
	 */
	@RequestMapping("user/checkIdentity")
	public String checkIdentity(HttpServletResponse response, HttpServletRequest request, User user,
			Model model) {
		model.addAttribute("user", user);
		return "forgot/checkIdentity";
	}

	/**
	 * return username
	 * 
	 * @param response
	 * @param request
	 * @param username
	 * @param model
	 * @return
	 * 
	 * @author Damien
	 */
	@RequestMapping("user/setNewPassword")
	public String setNewPassword(HttpServletResponse response, HttpServletRequest request, User user,
			Model model, BindingResult bindingResult) {
		
		userValidator.validateEmail(user, bindingResult);
		if (bindingResult.hasErrors()) {
			return "forgot/checkIdentity";
		}
		
		model.addAttribute("user", user);
		return "forgot/setNewPassword";
	}

	/**
	 * reset user's password
	 * 
	 * @param response
	 * @param request
	 * @param user
	 * @return
	 * 
	 * @author Damien
	 */
	@RequestMapping("user/complete")
	public String complete(HttpServletResponse response, HttpServletRequest request, User user, BindingResult bindingResult) {
		userValidator.validatePwd(user, bindingResult);
		if (bindingResult.hasErrors()) {
			return "forgot/setNewPassword";
		}else{
			userService.setNewPwd(user);
			return "forgot/complete";
		}
	}

	/**
	 * get the mail code from the mail server
	 * 
	 * @param request
	 * @param response
	 * @param email
	 *            the user's email
	 * @return
	 * @throws IOException
	 * @throws MessagingException
	 * 
	 * @author Damien
	 */
	@RequestMapping("user/verify") 
	public void sendVerificationCode(HttpServletRequest request, HttpServletResponse response, String email)
			throws IOException, MessagingException {
		emailService.sendVerificationCode(request, response, email);

	}

	/**
	 * Sends email with QR code containing security key for 2FA code generation
	 */
	@RequestMapping("user/sendSecurityCode")
	public void sendSecurityCode(HttpServletRequest request, HttpServletResponse response, String username)
			throws IOException, MessagingException, WriterException {

		User user = userService.updateSecretKey(username);
		emailService.sendSecurityCodeEmail(user);

		response.sendRedirect("/login");
	}

	@RequestMapping("user/createCode")
	public void createCode(HttpServletRequest request, HttpServletResponse response, Model model) {
		RandomCodeUtils codeUtils = new RandomCodeUtils();
		codeUtils.createRandomCode(request, response);
	}

	@RequestMapping("search/profile")
	public void searchProfile(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<String> criteria = new ArrayList<>();
		selectedCaregiverService.searchByProfileFilter(criteria);
	}

	@RequestMapping("user/checkRandomCode")
	public void checkRandomCode(String username, String inputCode, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// User
		JSONObject jsonObj = new JSONObject();
		RandomCodeUtils codeUtils = new RandomCodeUtils();

		User user = userService.getUser(username);
		boolean flag = codeUtils.checkRandomCode(request, inputCode);

		if (user != null && flag) {
			jsonObj.put("username", true);
		}
		else if (user != null) {
			jsonObj.put("username", false);
		}
		else if (flag) {
			jsonObj.put("flag", true);
		}
		else {
			jsonObj.put("flag", false);
		}
		response.getWriter().print(jsonObj.toString());
	}

	/**
	 * delete existing search
	 * 
	 * @param request
	 * @param response
	 * @param searchHistory		search's information
	 * @throws IOException
	 * @author Damien
	 */
	@RequestMapping("search/delete")
	public void deleteSearch(HttpServletRequest request, HttpServletResponse response, SearchHistory searchHistory) throws IOException{
		JSONObject jsonObj = new JSONObject();
		if(!StringUtil.isBlank(String.valueOf(searchHistory.getId()))){
			searchHistoryService.delete(searchHistory);
			jsonObj.put("msg", "Successfully deleted!");
		}
		response.getWriter().print(jsonObj.toString());
	}
	
	/**
	 * upload document
	 * 
	 * @param request
	 * @param caregiver
	 * @param active
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("file/saveDocument")
	public String saveDocument(MultipartHttpServletRequest request, Caregiver caregiver, String active) throws IOException{
		//MultipartFile file = request.getFile("a_file");		//get the file
		String type = request.getParameter("s_type");       //get the file type
		List<MultipartFile> fileList = request.getFiles("a_file");		//get the files
		caregiver = selectedCaregiverService.getCaregiver(caregiver.getUserId());
		
		if (fileList.size() > 0) {
			for(MultipartFile file : fileList){
				String fileName = file.getOriginalFilename();
				// Upload files to S3
				selectedCaregiverService.uploadFile(file, caregiver, type);
				if("resume".equals(type)){
					/*if(!"resume.pdf".equals(fileName) && !"resume.PDF".equals(fileName)){
						if("".equals(caregiver.getResume().trim())){
							caregiver.setResume(fileName);
						}else{
							caregiver.setResume(caregiver.getResume().concat(","+fileName));
						}
						
					}*/
					caregiver.setResume(fileName);
				}else if("my_photo".equals(type)){
					/*if(caregiver.getPhoto().trim().equals("")){
						caregiver.setPhoto(fileName);
					}else{
						caregiver.setPhoto(caregiver.getPhoto()+","+fileName);
					}*/
					caregiver.setPhoto(fileName);
				}else if("passport".equals(type)){
					if(caregiver.getPhotoPassport().trim().equals("")){
						caregiver.setPhotoPassport(fileName);
					}else{
						caregiver.setPhotoPassport(caregiver.getPhotoPassport()+","+fileName);
					}
				}else if("degrees".equals(type)){
					if(caregiver.getPhotoDegrees().trim().equals("")){
						caregiver.setPhotoDegrees(fileName);
					}else{
						caregiver.setPhotoDegrees(caregiver.getPhotoDegrees()+","+fileName);
					}
				}else if("other_file".equals(type)){
					if(caregiver.getOtherFiles().trim().equals("")) {
						caregiver.setOtherFiles(fileName);
					} else {
						caregiver.setOtherFiles(caregiver.getOtherFiles()+","+fileName);
					}
				}
				
			}
			if(caregiver.getResume() == null){
				uploadDefaultResume(caregiver);
			}
			selectedCaregiverService.update(caregiver);
		}
		
		return "redirect:/dashboard/getCandidate?userId=" + caregiver.getUserId() + "&active=" + active;
	}

	/**
	 * Uploads default resume for caregiver.
	 * @param caregiver
	 */
	private void uploadDefaultResume(Caregiver caregiver) throws IOException
	{
		DefaultResumeMultipartFile defaultResumeFile = DefaultResumeMultipartFile.getInstance();
		selectedCaregiverService.uploadFile(defaultResumeFile, caregiver, "resume");
		caregiver.setResume(defaultResumeFile.getOriginalFilename());
	}

	@RequestMapping(value = "/candidate/changeTheStatusOfRegisteredConcorde", method = RequestMethod.POST)
	public void changeTheStatisOfRegisteredConcorde(@RequestParam("caregiverId") String caregiverId, @RequestParam("status") String status, HttpServletRequest request, HttpServletResponse response){
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Integer userId = user.getUserId();
		selectedCaregiverService.changeTheStatisOfRegisteredConcorde(userId.toString(), caregiverId, status);
	}
	
	@RequestMapping(value = "/candidate/changeTheStatusOfAdvancePlacementScheme", method = RequestMethod.POST)
	public void changeTheStatusOfAdvancePlacementScheme(@RequestParam("caregiverId") String caregiverId, @RequestParam("status") String status, HttpServletRequest request, HttpServletResponse response){
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Integer userId = user.getUserId();
		selectedCaregiverService.changeTheStatusOfAdvancePlacementScheme(userId.toString(),caregiverId, status);
	}
	
	@RequestMapping(value = "/candidate/changeTheStatusOfMedicalCertVerified", method = RequestMethod.POST)
	public void changeTheStatusOfMedicalCertVerified(@RequestParam("caregiverId") String caregiverId, @RequestParam("status") String status, HttpServletRequest request, HttpServletResponse response){
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Integer userId = user.getUserId();
		selectedCaregiverService.changeTheStatusOfMedicalCertVerified(userId.toString(), caregiverId, status);
	}
	
	@RequestMapping(value= "/bio/editBioInfoByCaregiverId", method = RequestMethod.POST)
	public String editBioInfoByCaregiverId(Bio bio, HttpServletRequest request, HttpServletResponse response){
		selectedCaregiverService.editBioInfoByCaregiverId(bio);
		return "redirect:/dashboard/getCandidate?userId=" + bio.getCaregiverId() + "&active=" + 4;
	}
	
	/**
	 * update caregiver information
	 * 
	 * @param caregiver			caregiver information
	 * @param userId			user's id
	 * @param fullName			user's name
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("admin/ajax/update_cv")
	public String updateCv(Caregiver caregiver, HttpServletRequest request, String loginUserId,String loginUserName,
			String dob, HttpServletResponse response) throws ParseException {
		String tagged = request.getParameter("tagged_to");
		String contracted = request.getParameter("contracted_to");
		String tagFlag = request.getParameter("tag_flag");
		Date today = new Date();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if(org.apache.commons.lang3.StringUtils.isNotEmpty(dob)) {
			Date dateOfBirth = sdf.parse(dob);
			caregiver.setDateOfBirth(dateOfBirth);

			int age = today.getYear() - dateOfBirth.getYear();
			caregiver.setAge(String.valueOf(age));
		}
		boolean statusChanged = false;
		
		if(caregiver.getTag() == 0 && !tagFlag.equals(String.valueOf(caregiver.getTag()))){
			caregiver.setTaggedTo("");
			caregiver.setContractedTo("");
			caregiver.setTagStatus("<a href='#' class='btn btn-default btn-xs'>Available</a>");
			caregiver.setStatus(7);
			caregiver.setMarkedAsRedayTime(new Date());
			statusChanged = true;
		}else if(caregiver.getTag() == 1 && !tagged.equals(caregiver.getTaggedTo())){		//[0:Available,1:Tagged,2:Contracted,3:On hold]	[available:7  tagged:8 contracted:9 on hold:5]
			caregiver.setTagStatus("<a href='#' class='btn btn-success btn-xs'>Tagged by " +loginUserName+ "</a>");
			caregiver.setTagId(Integer.valueOf(loginUserId));
			caregiver.setContractedTo("");
			caregiver.setTaggedDate(new Date());
			caregiver.setStatus(8);
			statusChanged = true;
		}else if(caregiver.getTag() == 2 && !contracted.equals(caregiver.getContractedTo())){
			caregiver.setTagStatus("<a href='#' class='btn btn-success btn-xs'>Contracted by " +loginUserName+ "</a>");
			caregiver.setTaggedTo("");
			caregiver.setDateOfPlacement(new Date());
			caregiver.setStatus(9);
			statusChanged = true;
			if(!StringUtil.isBlank(caregiver.getNumbersOfPlacement())){
				caregiver.setNumbersOfPlacement(String.valueOf(Integer.valueOf(caregiver.getNumbersOfPlacement())+1));
			}else{
				caregiver.setNumbersOfPlacement("1");
			}
		}else if(caregiver.getTag() == 3 && !tagFlag.equals(String.valueOf(caregiver.getTag()))){
			caregiver.setTagStatus("<a href='#' class='btn btn-default btn-xs'>On hold</a>");
			caregiver.setTaggedTo("");
			caregiver.setContractedTo("");
			caregiver.setStatus(5);
			statusChanged = true;
		}else{
			Caregiver c2 = selectedCaregiverService.getCaregiver(caregiver.getUserId());
			caregiver.setTag(c2.getTag());
			caregiver.setTaggedDate(c2.getTaggedDate());
			caregiver.setTaggedTo(c2.getTaggedTo());
			caregiver.setStatus(c2.getStatus());
			caregiver.setContractedTo(c2.getContractedTo());
			caregiver.setTagStatus(c2.getTagStatus());
			caregiver.setNumbersOfPlacement(c2.getNumbersOfPlacement());
			caregiver.setDateOfPlacement(c2.getDateOfPlacement());
			caregiver.setTagId(c2.getTagId());
			caregiver.setMarkedAsRedayTime(c2.getMarkedAsRedayTime());
		}
		
		String[] name = caregiver.getFullName().split(" ");
		if(null != name && name.length > 1){
			caregiver.setFirstName(name[0]);
			caregiver.setLastName(name[1]);
		}else{
			caregiver.setFirstName(caregiver.getFullName());
			caregiver.setLastName("");
		}
		
		String specialities = "";
		String languages = "";
		
		if(null != caregiver.getLanguageList() &&caregiver.getLanguageList().size()>0){
			for (int i = 0; i < caregiver.getLanguageList().size(); i++) {
				if (i < caregiver.getLanguageList().size() - 1) {
					languages = languages + caregiver.getLanguageList().get(i) + ",";
				}
				else {
					languages = languages + caregiver.getLanguageList().get(i);
				}
			}
		}
		
		if(null != caregiver.getBio().getExperienceList() && caregiver.getBio().getExperienceList().size()>0){
			List<String> experienceList = caregiver.getBio().getExperienceList();
			for(int i=0; i<experienceList.size() ;i++){
				if (i < experienceList.size() - 1) {
					specialities = specialities + experienceList.get(i) + ",";
				}
				else {
					specialities = specialities + experienceList.get(i);
				}
			}
		}
		Bio bio = caregiver.getBio();
		
		caregiver.setAbout(bio.getCandidateBasicInformation());
		caregiver.setSpecialities(specialities);
		caregiver.setCertifiedCpr(bio.getTrainedToCprOrFA());
		caregiver.setHobbies(bio.getHobby());
		caregiver.setLanguages(languages);
		caregiver.setLastModified(today);
		caregiver.setCertifiedCpr(caregiver.getBio().getTrainedToCprOrFA());
		
		bio.setLastModifyTime(new Date());
		bio.setNursingExperience(specialities);
		bio.setCaregiverId(Integer.valueOf(caregiver.getUserId()));
		
		//save caregiver
		selectedCaregiverService.editCaregiverCv(caregiver);

		// update status history
		if(statusChanged){
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			selectedCaregiverService.addStatusChangeHistory(caregiver, user, String.valueOf(caregiver.getStatus()));
		}
		
		//edit bio
		selectedCaregiverService.editCvBio(bio, Integer.valueOf(caregiver.getUserId()));
		
		return "redirect:/admin/users/view_cv?userId="+caregiver.getUserId();
	}
	
	@RequestMapping(value="/candidates/deleteCandidatesByIds",method = RequestMethod.POST)
	public String deleteCandidatesByIds(@RequestParam("ids") String ids,HttpServletRequest request,HttpServletResponse response){
		selectedCaregiverService.deleteCandidatesByIds(ids);
		return "/candidates";
	}
	
	/**
	 * Identify duplicate exist candidate(by mailbox)
	 * 
	 * @param email			new email
	 * @param userEmail		original email
	 * @return
	 */
	@RequestMapping("dashboard/checkExistCandidate")
	public @ResponseBody String checkExistCandidate(@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "userEmail", required = true)String userEmail){
		int count = selectedCaregiverService.checkExistCandidate(email,userEmail);
		
		String msg = null;
		if(count > 0){
			 msg = "{\"status\":\"error\", \"msg\":\"The mailbox already exists!\"}";
		}else{
			msg = "{\"status\":\"success\"}";
		}
		return msg;
	}
}
