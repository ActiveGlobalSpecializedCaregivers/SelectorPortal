package com.cloudaxis.agsc.portal.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloudaxis.agsc.portal.dao.SelectedCaregiverDAOImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloudaxis.agsc.portal.model.Bio;
import com.cloudaxis.agsc.portal.model.CandidateStatusAmount;
import com.cloudaxis.agsc.portal.model.Caregiver;
import com.cloudaxis.agsc.portal.model.SearchParamOfCandidate;
import com.cloudaxis.agsc.portal.model.User;
import com.cloudaxis.agsc.portal.service.SelectedCaregiverService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/shortlist")
public class ShortlistController extends AbstractController{

	public ShortlistController() {
		super(ShortlistController.class);
	}

	protected Logger logger	= Logger.getLogger(ShortlistController.class);

	@Autowired
	SelectedCaregiverService selectedCaregiverService;
	
	@RequestMapping(value="/getStatusAmount", method = RequestMethod.GET)
	public String getStatusAmount(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws IOException{
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		CandidateStatusAmount candidateStatusAmount = selectedCaregiverService.getStatusAmount(user.getAuthorities().toString());
		model.addAttribute("candidateStatusAmount", candidateStatusAmount);
		return "shortlist/menu";
	}
	
	@RequestMapping(value="/getCandidatesByStatus", method = RequestMethod.POST)
	public @ResponseBody String getCandidatesByStatus(@RequestParam("status") String status, @RequestParam("filter") String queryParam){
		String jsonString = null;
		ObjectMapper mapper = new ObjectMapper();
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		logger.info("getCandidatesByStatus status:"+status+" queryParams:"+queryParam);
		try{
			List<Caregiver> candidateList;
			if(StringUtils.isNotEmpty(queryParam)){
				SearchParamOfCandidate searchParam = new ObjectMapper().readValue(queryParam, SearchParamOfCandidate.class);
				candidateList = selectedCaregiverService.processTaggedStatus(
						selectedCaregiverService.getCandidatesByParams(searchParam, status,
																	   user.getAuthorities().toString()), false, user);
			}else{
				candidateList = selectedCaregiverService.processTaggedStatus(
						selectedCaregiverService.getCandidatesByStatus(status, user.getAuthorities().toString()), true,
						user);
			}
			jsonString = mapper.writeValueAsString(candidateList);
		}catch(IOException e){
			logger.error("JSON data could not be created for the shortlist page", e);
		}
		return jsonString;
	}
	
	@RequestMapping(value="/saveCandidate", method = RequestMethod.POST)
	public void saveCandidate(HttpServletRequest request, HttpServletResponse response, @RequestParam("adminId") String adminId, @RequestParam("adminFirstName") String adminFirstName, @RequestParam("adminLastName") String adminLastName) throws IOException, ParseException {
		String action = request.getParameter("action");
		logger.info("saveCandidate invoked. Action:" + action);
		if(StringUtils.isBlank(request.getParameter("data[user_id]")) && "edit".equalsIgnoreCase(action))
		{
			logger.info("Missing user_id in the request parameters for candidate editing.");
			throw new IOException("Invalid data - missing candidate unique key for edit action");
		}
		Caregiver candidate = new Caregiver();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		candidate.setUserId(request.getParameter("data[user_id]"));
		candidate.setAppId(request.getParameter("data[app_id]"));
		if(StringUtils.isNoneBlank(request.getParameter("data[tag_id]"))){
			candidate.setTagId(Integer.parseInt(request.getParameter("data[tag_id]")));
		}
		String adminName = adminFirstName + " " + adminLastName;
		// status
		String tag = request.getParameter("data[tag]");
		String appliedDate = request.getParameter("data[d_applied]");
		String tagOrigin = request.getParameter("data[tag_origin]");
		candidate.setTagStatus(request.getParameter("data[tag_status]"));
		candidate.setDateApplied(dateFormat.parse(appliedDate));
		if(StringUtils.isNotBlank(request.getParameter("data[numbersOfPlacement]"))){
			candidate.setNumbersOfPlacement(request.getParameter("data[numbersOfPlacement]"));
		}else{
			candidate.setNumbersOfPlacement("0");
		}
		if(StringUtils.isNoneBlank(tag)){
			int tagNum = Integer.parseInt(tag);
			candidate.setTag(tagNum);
			candidate.setStatus(getStatusByTag(tagNum));
			if(!tag.equals(tagOrigin)){
				candidate.setTagStatus(getTagStatus(tagNum, adminName));
				if(tagNum == 0){
					candidate.setMarkedAsRedayTime(new Date());
				}else{
					candidate.setMarkedAsRedayTime(null);
				} 
				if(tagNum == 1){
					candidate.setTagId(Integer.parseInt(adminId));
				}else if(tagNum == 2){
					candidate.setDateOfPlacement(new Date());
					int numbersOfPlacement = Integer.parseInt(candidate.getNumbersOfPlacement()) + 1;
					candidate.setNumbersOfPlacement(String.valueOf(numbersOfPlacement));
				}
			}
		}
		candidate.setTaggedTo(request.getParameter("data[tagged_to]"));
		candidate.setResume(request.getParameter("data[resume]"));
		candidate.setContractedTo(request.getParameter("data[contracted_to]"));
		candidate.setFullName(request.getParameter("data[full_name]"));
		candidate.setAppId(request.getParameter("data[app_id]"));
		candidate.setEmail(request.getParameter("data[email]"));
		candidate.setMobile(request.getParameter("data[mobile]"));
		candidate.setVideoURL(request.getParameter("data[video_url]"));
		candidate.setWorkInSG(request.getParameter("data[work_in_sg]"));
		candidate.setWorkInHK(request.getParameter("data[work_in_hk]"));
		candidate.setWorkInTW(request.getParameter("data[work_in_tw]"));
		candidate.setWorkedInSG(request.getParameter("data[worked_in_sg]"));
		candidate.setSkype(request.getParameter("data[skype]"));
		candidate.setGender(request.getParameter("data[gender]"));
		String birthOfDate = request.getParameter("data[d_of_birth]");
		if(StringUtils.isNotEmpty(birthOfDate)){
			candidate.setDateOfBirth(dateFormat.parse(birthOfDate));		
			Calendar today = Calendar.getInstance(); 
			Calendar birthdate = Calendar.getInstance();
			birthdate.setTime(candidate.getDateOfBirth());
			Integer age = today.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);
			candidate.setAge(String.valueOf(age));
		}else{
			candidate.setAge(request.getParameter("data[age]"));
		}
		candidate.setCountryOfBirth(request.getParameter("data[country_of_birth]"));
		candidate.setNearestAirport(request.getParameter("data[nearest_airport]"));
		candidate.setHasChildren(request.getParameter("data[has_children]"));
		candidate.setNationality(request.getParameter("data[nationality]"));
		candidate.setHeight(request.getParameter("data[height]"));
		candidate.setWeight(request.getParameter("data[weight]"));
		candidate.setMaritalStatus(request.getParameter("data[marital_status]"));
		candidate.setChildrenNames(request.getParameter("data[children_names]"));
		candidate.setSiblings(request.getParameter("data[siblings]"));
		candidate.setLanguages(request.getParameter("data[languages]"));
		candidate.setReligion(request.getParameter("data[religion]"));
		candidate.setFoodChoice(request.getParameter("data[food_choice]"));
		candidate.setEducationalLevel(request.getParameter("data[educational_level]"));
		candidate.setExp(request.getParameter("data[exp]"));
		candidate.setCertifiedCpr(request.getParameter("data[certified_cpr]"));
		candidate.setSpecialities(request.getParameter("data[specialities]"));
		candidate.setAvailability(request.getParameter("data[availability]"));
		candidate.setMotivation(request.getParameter("data[motivation]"));
		candidate.setCurrentAddress(request.getParameter("data[current_address]"));
		candidate.setAllergies(request.getParameter("data[allergies]"));
		candidate.setDiagnosedConditions(request.getParameter("data[diagnosed_conditions]"));
		candidate.setSalaryHKD(request.getParameter("data[salary_hkd]"));
		candidate.setSalarySGD(request.getParameter("data[salary_sgd]"));
		candidate.setSalaryTWD(request.getParameter("data[salary_twd]"));
		candidate.setLastModified(new Date());
		String[] names = candidate.getFullName().split(" ");
		candidate.setFirstName(names[0]);
		if(names.length > 1){
			String lastname = names[1];
			for(int i = 2; i < names.length; i++){
				lastname += " " + names[i];
			}
			candidate.setLastName(lastname);
		}
		Bio bio  = new Bio();
		if(StringUtils.isNotBlank(request.getParameter("data[user_id]"))){
			bio.setCaregiverId(Integer.parseInt(request.getParameter("data[user_id]")));
		}
		bio.setNursingExperience(request.getParameter("data[specialities]"));
		bio.setTrainedToCprOrFA(request.getParameter("data[certified_cpr]"));
		bio.setCandidateBasicInformation(request.getParameter("data[about]"));
		bio.setEducationAndExperience(request.getParameter("data[education]"));
		bio.setHobby(request.getParameter("data[hobbies]"));
		bio.setLastModifyTime(new Date());
		logger.info("editCaregiverInfo candidate:"+candidate);
		Integer id = selectedCaregiverService.saveCandidateOfDashboard(candidate);
		logger.info("resulting ID:"+id);
		logger.info("Saving bio:"+bio);
		selectedCaregiverService.saveBioInfoByCaregiverIdOfDashboard(bio, candidate.getGender(), id);
		JSONObject jsonObj = new JSONObject();
		response.getWriter().print(jsonObj.toString());
	}


	private int getStatusByTag(int tag) {
		switch (tag) {
			case 1:
				return 8; // tagged
			case 2:
				return 9; // contracted
			case 3:
				return 5; // on hold
			default:
				return 7; // available
			}
	}
	
	private String getTagStatus(int tag, String adminName) {
		if(tag == 1){
			return "<a href='#' class='btn btn-success btn-xs'>Tagged by " + adminName + "</a>";
		}else if(tag == 2){
			return "<a href='#' class='btn btn-success btn-xs'>Contracted by " + adminName + "</a>";
		}
		return null;
	}
	
	@RequestMapping(value="/emailVerify")
	public void emailVerify(HttpServletRequest request, HttpServletResponse response, @RequestParam("candidateId") String candidateId, @RequestParam("email") String email) throws IOException{
		boolean isIdentity = true;
		if(StringUtils.isNotBlank(candidateId)){
			isIdentity = selectedCaregiverService.emailVerify(email, candidateId);
		}else{
			isIdentity = selectedCaregiverService.emailVerify(email);
		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("emailVerify", isIdentity);
		response.getWriter().print(jsonObj.toString());
	}
	
}
