package com.cloudaxis.agsc.portal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cloudaxis.agsc.portal.helpers.ApplicationDict;
import com.cloudaxis.agsc.portal.helpers.StringUtil;
import com.cloudaxis.agsc.portal.model.Bio;
import com.cloudaxis.agsc.portal.model.Caregiver;
import com.cloudaxis.agsc.portal.model.Comment;
import com.cloudaxis.agsc.portal.model.SendCV;
import com.cloudaxis.agsc.portal.model.User;

public class SelectedCaregiverDAOImpl implements SelectedCaregiverDAO {

	private static final int TIME_THRESHOLD_FOR_NEW_CANDIDATE = 7 * 24 * 60 * 60 * 1000; // 7 days
	protected Logger		logger	= Logger.getLogger(SelectedCaregiverDAOImpl.class);

	private JdbcTemplate	jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<Caregiver> listCaregivers() {

		List<Caregiver> caregiverList = new ArrayList<Caregiver>();

		String sql = "select * from candidate_profile";

		try {
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

			for (Map<String, Object> row : list) {
				Caregiver c = new Caregiver();
				c.setUserId(String.valueOf(row.get("user_id")));
				c.setEmail(String.valueOf(row.get("email")));
				c.setFullName(String.valueOf(row.get("full_name")));
				c.setFirstName(String.valueOf(row.get("first_name")));
				c.setLastName(String.valueOf(row.get("last_name")));
				c.setGender(String.valueOf(row.get("gender")));
				c.setDateOfBirth((Date) row.get("dob"));
				c.setAge(String.valueOf(row.get("age")));
				c.setCountryOfBirth(String.valueOf(row.get("country_of_birth")));
				c.setHasChildren(String.valueOf(row.get("has_children")));
				c.setSiblings(String.valueOf(row.get("siblings")));
				c.setLanguages(String.valueOf(row.get("languages")));
				c.setNationality(String.valueOf(row.get("nationality")));
				c.setEducationalLevel(String.valueOf(row.get("educational_level")));
				c.setCertifiedCpr(String.valueOf(row.get("certified_cpr")));
				c.setExp(String.valueOf(row.get("exp")));
				c.setMaritalStatus(String.valueOf(row.get("marital_status")));
				c.setReligion(String.valueOf(row.get("religion")));
				c.setMobile(String.valueOf(row.get("mobile")));
				c.setHeight(String.valueOf(row.get("height")));
				c.setWeight(String.valueOf(row.get("weight")));
				c.setMotivation(String.valueOf(row.get("motivation")));
				c.setAbout(String.valueOf(row.get("about")));
				c.setEducation(String.valueOf(row.get("education")));
				c.setSpecialities(String.valueOf(row.get("specialties")));
				c.setHobbies(String.valueOf(row.get("hobbies")));
				c.setAvailability(String.valueOf(row.get("availability")));
				c.setDateApplied((Date) row.get("date_applied"));
				c.setWorkInHK(String.valueOf(row.get("work_in_hk")));
				c.setWorkInSG(String.valueOf(row.get("work_in_sg")));
				c.setWorkInTW(String.valueOf(row.get("work_in_tw")));
				c.setSalaryHKD(String.valueOf(row.get("salary_hkd")));
				c.setSalarySGD(String.valueOf(row.get("salary_sgd")));
				c.setSalaryTWD(String.valueOf(row.get("salary_twd")));
				c.setFoodChoice(String.valueOf(row.get("food_choice")));
				c.setPhoto(String.valueOf(row.get("photo")));
				c.setSkype(String.valueOf(row.get("skype")));
				c.setResume(String.valueOf(row.get("resume")));
				c.setTag((Integer) row.get("tag"));
				c.setTagId((Integer) row.get("tag_id"));
				c.setTagStatus(String.valueOf(row.get("tag_status")));
				c.setTaggedDate((Date) row.get(row.get("tagged_date")));
				c.setContractedTo(String.valueOf(row.get("contracted_to")));
				c.setTaggedTo(String.valueOf(row.get("tagged_to")));
				c.setAssigned(String.valueOf(row.get("assigned")));
				c.setLocation(String.valueOf(row.get("location")));
				c.setRegistrationNumber(String.valueOf(row.get("registration_number")));
				c.setRemarks(String.valueOf(row.get("remarks")));
				c.setNearestAirport(String.valueOf(row.get("nearest_airport")));
				c.setCurrentAddress(String.valueOf(row.get("current_address")));
				c.setNumberOfChildren((Integer) row.get("number_of_children"));
				c.setChildrenNames(String.valueOf(row.get("children_names")));
				c.setWorkedInSG(String.valueOf(row.get("worked_in_sg")));
				c.setAllergies(String.valueOf(row.get("allergies")));
				c.setDiagnosedConditions(String.valueOf(row.get("diagnosed_conditions")));
				c.setVideoURL(String.valueOf(row.get("video_url")));
				c.setDateCreated((Date) row.get(row.get("datecreated")));
				c.setStatus(Integer.valueOf(String.valueOf(row.get("status"))));
				c.setTesdaNcii((String) row.get("tesda_ncii"));
				c.setRegisteredConcorde((String) row.get("registered_concorde"));
				c.setPreDeployment((String) row.get("pre_deployment"));
				c.setNumbersOfPlacement((String) row.get("numbers_of_placement"));
				caregiverList.add(c);
			}
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception retrieving the list of companies", e);
		}

		return caregiverList;
	}
	
	@Override
	public List<Caregiver> list() {
		List<Caregiver> caregiverList = new ArrayList<Caregiver>();
		//String sql = "select * from candidate_profile where status='0' OR status='2';";			//select status[New Applicant=0,Shortlisted=2]
		String sql = "select * from candidate_profile where 1 = 1 ";			
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	
		if("ROLE_PH_RECRUITING_PARTNER".equals(user.getRole())){
			sql += " and nationality='Philippines'";
		}else if(("ROLE_SALES_SG").equals(user.getRole())){
			sql += " and work_in_sg = 'Yes' ";
		}else if(("ROLE_SALES_HK").equals(user.getRole())){
			sql += " and work_in_hk = 'Yes' ";
		}else if(("ROLE_SALES_TW").equals(user.getRole())){
			sql += " and work_in_tw = 'Yes' ";
		}

		try {
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

			for (Map<String, Object> row : list) {
				Caregiver c = new Caregiver();
				c.setSgFin(String.valueOf(row.get("sg_fin")));
				c.setUserId(String.valueOf(row.get("user_id")));
				c.setEmail(String.valueOf(row.get("email")));
				c.setFullName(String.valueOf(row.get("full_name")));
				c.setFirstName(String.valueOf(row.get("first_name")));
				c.setLastName(String.valueOf(row.get("last_name")));
				c.setGender(String.valueOf(row.get("gender")));
				c.setDateOfBirth((Date) row.get("dob"));
				c.setAge(String.valueOf(row.get("age")));
				c.setCountryOfBirth(String.valueOf(row.get("country_of_birth")));
				c.setHasChildren(String.valueOf(row.get("has_children")));
				c.setSiblings(String.valueOf(row.get("siblings")));
				c.setLanguages(String.valueOf(row.get("languages")));
				c.setNationality(String.valueOf(row.get("nationality")));
				c.setEducationalLevel(String.valueOf(row.get("educational_level")));
				c.setCertifiedCpr(String.valueOf(row.get("certified_cpr")));
				c.setExp(String.valueOf(row.get("exp")));
				c.setMaritalStatus(String.valueOf(row.get("marital_status")));
				c.setReligion(String.valueOf(row.get("religion")));
				c.setMobile(String.valueOf(row.get("mobile")));
				c.setHeight(String.valueOf(row.get("height")));
				c.setWeight(String.valueOf(row.get("weight")));
				c.setMotivation(String.valueOf(row.get("motivation")));
				c.setAbout(String.valueOf(row.get("about")));
				c.setEducation(String.valueOf(row.get("education")));
				c.setSpecialities(String.valueOf(row.get("specialties")));
				c.setHobbies(String.valueOf(row.get("hobbies")));
				c.setAvailability(String.valueOf(row.get("availability")));
				c.setDateApplied((Date) row.get("date_applied"));
				c.setWorkInHK(String.valueOf(row.get("work_in_hk")));
				c.setWorkInSG(String.valueOf(row.get("work_in_sg")));
				c.setWorkInTW(String.valueOf(row.get("work_in_tw")));
				c.setSalaryHKD(String.valueOf(row.get("salary_hkd")));
				c.setSalarySGD(String.valueOf(row.get("salary_sgd")));
				c.setSalaryTWD(String.valueOf(row.get("salary_twd")));
				c.setFoodChoice(String.valueOf(row.get("food_choice")));
				c.setPhoto(String.valueOf(row.get("photo")));
				c.setSkype(String.valueOf(row.get("skype")));
				c.setResume(String.valueOf(row.get("resume")));
				c.setTag((Integer) row.get("tag"));
				c.setTagId((Integer) row.get("tag_id"));
				c.setTagStatus(String.valueOf(row.get("tag_status")));
				c.setTaggedDate((Date) row.get(row.get("tagged_date")));
				c.setContractedTo(String.valueOf(row.get("contracted_to")));
				c.setTaggedTo(String.valueOf(row.get("tagged_to")));
				c.setAssigned(String.valueOf(row.get("assigned")));
				c.setLocation(String.valueOf(row.get("location")));
				c.setRegistrationNumber(String.valueOf(row.get("registration_number")));
				c.setRemarks(String.valueOf(row.get("remarks")));
				c.setNearestAirport(String.valueOf(row.get("nearest_airport")));
				c.setCurrentAddress(String.valueOf(row.get("current_address")));
				c.setNumberOfChildren((Integer) row.get("number_of_children"));
				c.setChildrenNames(String.valueOf(row.get("children_names")));
				c.setWorkedInSG(String.valueOf(row.get("worked_in_sg")));
				c.setAllergies(String.valueOf(row.get("allergies")));
				c.setDiagnosedConditions(String.valueOf(row.get("diagnosed_conditions")));
				c.setVideoURL(String.valueOf(row.get("video_url")));
				c.setDateCreated((Date) row.get(row.get("datecreated")));
				c.setResumatorStatus(String.valueOf(row.get("applicant_status")));
				c.setStatus(Integer.valueOf(String.valueOf(row.get("status"))));
				c.setLastModified((Date) row.get("last_modified"));
				c.setTesdaNcii((String) row.get("tesda_ncii"));
				c.setRegisteredConcorde((String) row.get("registered_concorde"));
				c.setPreDeployment((String) row.get("pre_deployment"));
				c.setNumbersOfPlacement((String) row.get("numbers_of_placement"));
				c.setMarkedAdvancedPlacementScheme((Date)row.get("marked_as_advance_placement_scheme"));
				c.setMarkedMedicalCertVerify((Date)row.get("marked_as_medical_cer_verify"));
				c.setMarkedRegisteredWithConcorde((Date)row.get("marked_as_registered_with_concorde"));
				c.setDateOfAwaitingDocument((Date)row.get("date_awaiting_documents"));
				c.setDateOfSchedulingInterview((Date)row.get("date_scheduling_interview"));
				c.setDateOfInterviewScheduled((Date)row.get("date_interview_scheduled"));
				c.setDateOfShortlisted((Date)row.get("date_shortlisted"));
				c.setDateOfReadyForPlacement((Date)row.get("date_ready_for_placement"));
				c.setDateOfNotSelected((Date)row.get("date_not_selected"));
				c.setDateOfShortlistedWithDiffAvail((Date)row.get("date_shortlisted_with_diff_avail"));
				c.setDateOfTagged((Date)row.get("date_tagged"));
				c.setDateOfOnHold((Date)row.get("date_on_hold"));
				c.setDateOfBlacklisted((Date)row.get("date_blacklisted"));
				caregiverList.add(c);
			}
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception retrieving the list of companies", e);
		}

		return caregiverList;
	}

	@Override
	public Caregiver getCaregiver(String userId) {
		Caregiver c = new Caregiver();
		String sql = "select * from candidate_profile where user_id = ?;";

		try {
			Map<String, Object> row = jdbcTemplate.queryForMap(sql, new Object[] { userId });
			if (row != null && row.size() > 0) {
				c.setUserId(String.valueOf(row.get("user_id")));
				c.setEmail(String.valueOf(row.get("email")));
				c.setFullName(String.valueOf(row.get("full_name")));
				c.setFirstName(String.valueOf(row.get("first_name")));
				c.setLastName(String.valueOf(row.get("last_name")));
				c.setGender(String.valueOf(row.get("gender")));
				c.setDateOfBirth((Date) row.get("dob"));
				c.setAge(String.valueOf(row.get("age")));
				c.setCountryOfBirth(String.valueOf(row.get("country_of_birth")));
				c.setHasChildren(String.valueOf(row.get("has_children")));
				c.setSiblings(String.valueOf(row.get("siblings")));
				c.setLanguages(String.valueOf(row.get("languages")));
				c.setNationality(String.valueOf(row.get("nationality")));
				c.setEducationalLevel(String.valueOf(row.get("educational_level")));
				c.setCertifiedCpr(String.valueOf(row.get("certified_cpr")));
				c.setExp(String.valueOf(row.get("exp")));
				c.setMaritalStatus(String.valueOf(row.get("marital_status")));
				c.setReligion(String.valueOf(row.get("religion")));
				c.setMobile(String.valueOf(row.get("mobile")));
				c.setHeight(String.valueOf(row.get("height")));
				c.setWeight(String.valueOf(row.get("weight")));
				c.setMotivation(String.valueOf(row.get("motivation")));
				c.setAbout(String.valueOf(row.get("about")));
				c.setEducation(String.valueOf(row.get("education")));
				c.setSpecialities(String.valueOf(row.get("specialties")));
				c.setHobbies(String.valueOf(row.get("hobbies")));
				c.setAvailability(String.valueOf(row.get("availability")));
				c.setDateApplied((Date) row.get("date_applied"));
				c.setWorkInHK(String.valueOf(row.get("work_in_hk")));
				c.setWorkInSG(String.valueOf(row.get("work_in_sg")));
				c.setWorkInTW(String.valueOf(row.get("work_in_tw")));
				c.setSalaryHKD(String.valueOf(row.get("salary_hkd")));
				c.setSalarySGD(String.valueOf(row.get("salary_sgd")));
				c.setSalaryTWD(String.valueOf(row.get("salary_twd")));
				c.setFoodChoice(String.valueOf(row.get("food_choice")));
				c.setPhoto(String.valueOf(row.get("photo")));
				c.setSkype(String.valueOf(row.get("skype")));
				c.setResume(String.valueOf(row.get("resume")));
				c.setTag((Integer) row.get("tag"));
				c.setTagId((Integer) row.get("tag_id"));
				c.setTagStatus(String.valueOf(row.get("tag_status")));
				c.setTaggedDate((Date) row.get(row.get("tagged_date")));
				c.setContractedTo(String.valueOf(row.get("contracted_to")));
				c.setTaggedTo(String.valueOf(row.get("tagged_to")));
				c.setAssigned(String.valueOf(row.get("assigned")));
				c.setLocation(String.valueOf(row.get("location")));
				c.setRegistrationNumber(String.valueOf(row.get("registration_number")));
				c.setRemarks(String.valueOf(row.get("remarks")));
				c.setNearestAirport(String.valueOf(row.get("nearest_airport")));
				c.setCurrentAddress(String.valueOf(row.get("current_address")));
				c.setNumberOfChildren((Integer) row.get("number_of_children"));
				c.setChildrenNames(String.valueOf(row.get("children_names")));
				c.setWorkedInSG(String.valueOf(row.get("worked_in_sg")));
				c.setAllergies(String.valueOf(row.get("allergies")));
				c.setDiagnosedConditions(String.valueOf(row.get("diagnosed_conditions")));
				c.setVideoURL(String.valueOf(row.get("video_url")));
				c.setDateCreated((Date) row.get(row.get("datecreated")));
				c.setResumatorStatus(String.valueOf(row.get("applicant_status")));
				c.setApplyLocations(String.valueOf(row.get("applyLocations")));
				c.setChildrenNameAge(String.valueOf(row.get("children_name_age")));
				c.setHoldPassport(String.valueOf(row.get("hold_passport")));
				c.setYearGraduation(String.valueOf(row.get("year_graduation")));
				c.setYearStudies(String.valueOf(row.get("year_studies")));
				c.setCaregiverBeforeExp(String.valueOf(row.get("caregiver_before_exp")));
				c.setSgFin(String.valueOf(row.get("sg_fin")));
				c.setCurrentJob(String.valueOf(row.get("current_job")));
				c.setCurrentJobTime(String.valueOf(row.get("current_job_time")));
				c.setTimeOfSg(String.valueOf(row.get("time_of_sg")));
				c.setHistoryOfTreatment(String.valueOf(row.get("history_of_treatment")));
				c.setInterviewTime(String.valueOf(row.get("interview_time")));
				c.setReferrer(String.valueOf(row.get("referrer")));
				c.setPhotoPassport(String.valueOf(row.get("photo_passport")));
				c.setPhotoDegrees(String.valueOf(row.get("photo_degrees")));
				c.setOtherFiles(String.valueOf(row.get("other_files")));
				c.setStatus(Integer.valueOf(String.valueOf(row.get("status"))));
				c.setTesdaNcii((String) row.get("tesda_ncii"));
				c.setRegisteredConcorde((String) row.get("registered_concorde"));
				c.setPreDeployment((String) row.get("pre_deployment"));
				c.setMedicalCertVerified((String) row.get("medical_cert_verified"));
				c.setMedicalCertValidator((String)row.get("medical_cert_validator"));
				c.setNumbersOfPlacement((String)row.get("numbers_of_placement"));
			}
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception retrieving the list of companies"+ e);
		}

		return c;
	}

	@Override
	public void save(Caregiver caregiver) {
		String sql = "update candidate_profile set gender=?,dob=?,country_of_birth=?,nearest_airport=?,nationality=?," // 5
				+ "current_address=?,height=?,weight=?,marital_status=?,has_children=?," // 10
				+ "languages=?,religion=?,food_choice=?,hold_passport=?,education=?," // 15
				+ "educational_level=?,exp=?,certified_cpr=?,work_in_sg=?,diagnosed_conditions=?,"	//20
				+ "hobbies=?,last_modified=?,first_name=?,last_name=?,full_name=?,"		//25
				+ "mobile=?,email=?,work_in_sg=?,work_in_hk=?,skype=?,"	//30
				+ "children_name_age=?,siblings=?,year_graduation=?,caregiver_before_exp=?,year_studies=?,"	//35
				+ "worked_in_sg=?,sg_fin=?,location=?,specialties=?,current_job=?," //40
				+ "current_job_time=?,motivation=?,time_of_sg=?,availability=?,allergies=?," //45
				+ "history_of_treatment=?,tesda_ncii=?,referrer=?,interview_time=?" //49		
				+ " where user_id='" + caregiver.getUserId() + "';";

		try {
			jdbcTemplate.update(sql, new Object[] {
					caregiver.getGender(),
					caregiver.getDateOfBirth(),
					caregiver.getCountryOfBirth(),
					caregiver.getNearestAirport(),
					caregiver.getNationality(),
					caregiver.getCurrentAddress(),
					caregiver.getHeight(),
					caregiver.getWeight(),
					caregiver.getMaritalStatus(),
					caregiver.getHasChildren(), // 10
					caregiver.getLanguages(),
					caregiver.getReligion(),
					caregiver.getFoodChoice(),
					caregiver.getHoldPassport(),
					caregiver.getEducation(),
					caregiver.getEducationalLevel(),
					caregiver.getExp(),
					caregiver.getCertifiedCpr(),
					caregiver.getWorkInSG(),
					caregiver.getDiagnosedConditions(),	//20
					caregiver.getHobbies(),
					new Date(),
					caregiver.getFirstName(),
					caregiver.getLastName(),
					caregiver.getFirstName()+" "+caregiver.getLastName(),
					caregiver.getMobile(),
					caregiver.getEmail(),
					caregiver.getWorkInSG(),
					caregiver.getWorkInHK(),
					caregiver.getSkype(),				//30
					caregiver.getChildrenNameAge(),
					caregiver.getSiblings(),
					caregiver.getYearGraduation(),
					caregiver.getCaregiverBeforeExp(),
					caregiver.getYearStudies(),
					caregiver.getWorkedInSG(),
					caregiver.getSgFin(),
					caregiver.getLocation(),
					caregiver.getSpecialities(),
					caregiver.getCurrentJob(),			//40
					caregiver.getCurrentJobTime(),
					caregiver.getMotivation(),
					caregiver.getTimeOfSg(),
					caregiver.getAvailability(),
					caregiver.getAllergies(),
					caregiver.getHistoryOfTreatment(),
					caregiver.getTesdaNcii(),
					caregiver.getReferrer(),
					caregiver.getInterviewTime()	//49
			});
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception retrieving the list of companies", e);
		}
	}

	@Override
	public void editStatus(Caregiver caregiver, User user, String changeStatus) {
		logger.info("editStatus, caregiverId="+caregiver.getUserId()+" status="+changeStatus);
		String dateOfChangeStatus = ApplicationDict.getDateOfStatusMap().get(changeStatus);
		String sql = "update candidate_profile set status=?," +
					 " last_modified=SYSDATE()," +
					 " marked_as_ready_time = ?," +
					 " date_of_placement = ?," +
					 " numbers_of_placement = ?," +
					 " tag = ?," +
					 " tag_status = ?," +
					 " tagged_to = ?" +
					 (dateOfChangeStatus == null ? " " : " ," + dateOfChangeStatus + " = ?  ") +
					 "where user_id="+ caregiver.getUserId() + ";";
		try {
			ArrayList values = new ArrayList(8);
			values.add(changeStatus);
			values.add(caregiver.getMarkedAsRedayTime());
			values.add(caregiver.getDateOfPlacement());
			values.add(caregiver.getNumbersOfPlacement());
			values.add(caregiver.getTag());
			values.add(caregiver.getTaggedTo());
			values.add(caregiver.getTagStatus());
			if(dateOfChangeStatus != null){
				values.add(new Date());
			}
			jdbcTemplate.update(sql, values.toArray());
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception updating status of candidate", e);
		}
	}
	
	@Override
	public void addHistory(String applicantId, String userId, String workflow, String statusFrom, String statusTo) {
		String editStatus = "insert into workflow_history(candidate_id,user_id,status_from,status_to,workflow,change_date) "
				+ "values(" + applicantId + "," + userId+ "," + "\"" +statusFrom + "\","
				+ "\"" + statusTo + "\",\"" + workflow + "\",SYSDATE());";
		try {
			jdbcTemplate.update(editStatus);
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception inserting history", e);
		}
		
	}

	@Override
	public void postNewComment(Comment comment) {
		String sql = " insert into comments_history(user_id, candidate_id, comment, create_date, update_date) values(?, ?, ?, ?, ?); ";

		try {

			jdbcTemplate.update(sql, new Object[] { comment.getUserId(),
					comment.getCandidateId(),
					comment.getComment(),
					comment.getCreateDate(),
					comment.getUpdateDate()
			});
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception inserting a User into the database", e);
		}
	}

	@Override
	public List<Comment> getCommentsByCaregiverId(Integer caregiverId) {
		List<Comment> comments = new ArrayList<Comment>();

		String sql = "SELECT c.*,u.first_name, u.last_name FROM comments_history c "
				+ "LEFT JOIN users u ON u.user_id=c.user_id where candidate_id=" + caregiverId;

		try {
			List<Map<String, Object>> commentsList = jdbcTemplate.queryForList(sql);

			for (Map<String, Object> row : commentsList) {
				Comment comment = new Comment();
				String firstName = String.valueOf(row.get("first_name"));
				String lastName = String.valueOf(row.get("last_name"));
				comment.setId(Integer.valueOf(row.get("id").toString()));
				comment.setComment(String.valueOf(row.get("comment")));
				comment.setUserId(Integer.valueOf(row.get("user_id").toString()));
				comment.setCandidateId(caregiverId);
				comment.setCreateDate((Date) row.get("create_date"));
				comment.setUserName(firstName + " " + lastName);
                String firstInitial = !StringUtils.isEmpty(firstName) ? String.valueOf(firstName.charAt(0)) : "";
                String lastInitial = !StringUtils.isEmpty(lastName)
									 && !lastName.endsWith(" ") ? String.valueOf(lastName.charAt(lastName.indexOf(" ") + 1)) : "";
                comment.setUserInitials(firstInitial + lastInitial);

				comments.add(comment);
			}

		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception retrieving the list of companies", e);
		}

		return comments;
	}

	@Override
	public List<Comment> updateCommentsWithUserNameAndInitials(List<Comment> comments) {
		List<Comment> updatedComments = new ArrayList<>();

		String sql;
		for (Comment comment : comments) {

			sql = "select * from users where user_id=" + comment.getUserId();

			List<Map<String, Object>> userList = jdbcTemplate.queryForList(sql);
			for (Map<String, Object> row : userList) {
				String firstName = String.valueOf(row.get("first_name"));
				String lastName = String.valueOf(row.get("last_name"));
				comment.setUserName(firstName + " " + lastName);
				comment.setUserInitials(String.valueOf(firstName.charAt(0))
						+ String.valueOf(lastName.charAt(lastName.indexOf(" ") + 1)));
				updatedComments.add(comment);
			}

		}

		return updatedComments;
	}

	@Override
	public List<Caregiver> searchByProfileFilter(List<String> criteria) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM candidate_profile where 1 = 1 ");

		if("ROLE_PH_RECRUITING_PARTNER".equals(user.getRole())){
			criteria.add("nationality=exactly=Philippines");
		}else if(("ROLE_SALES_SG").equals(user.getRole())){
			query.append(" AND WORK_IN_SG = 'Yes' ");
		}else if(("ROLE_SALES_HK").equals(user.getRole())){
			query.append(" AND WORK_IN_HK = 'Yes' ");
		}else if(("ROLE_SALES_TW").equals(user.getRole())){
			query.append(" AND WORK_IN_TW = 'Yes' ");
		}
		
		for (String string : criteria) {
			String formattedCondition = getFormattedQueryCondition(string);
			String searchParam = formattedCondition.trim();
			if (!StringUtil.isBlank(searchParam)) {
				if(searchParam.startsWith("date_of_change")){
					String condition = searchParam.replace("date_of_change", "");
					query.append(" AND ( date_applied " + condition + " OR date_of_placement " + condition + ")");
				}else if(searchParam.startsWith("specialties")){
					String condition = searchParam.replace("specialties", "").replace(" =", "").replace("'", "");	
					query.append(" AND specialties like '%" + condition + "%' ");
				}else{
					query.append(" AND " + formattedCondition);
				}
			}
		}

		
		List<Caregiver> caregiverList = new ArrayList<Caregiver>();
		try {
			logger.info("Running SQL:"+query);
			List<Map<String, Object>> list = jdbcTemplate.queryForList(query.toString());

			for (Map<String, Object> row : list) {
				Caregiver c = new Caregiver();
				c.setSgFin(String.valueOf(row.get("sg_fin")));
				c.setUserId(String.valueOf(row.get("user_id")));
				c.setEmail(String.valueOf(row.get("email")));
				c.setFullName(String.valueOf(row.get("full_name")));
				c.setFirstName(String.valueOf(row.get("first_name")));
				c.setLastName(String.valueOf(row.get("last_name")));
				c.setGender(String.valueOf(row.get("gender")));
				c.setDateOfBirth((Date) row.get("dob"));
				c.setAge(String.valueOf(row.get("age")));
				c.setCountryOfBirth(String.valueOf(row.get("country_of_birth")));
				c.setHasChildren(String.valueOf(row.get("has_children")));
				c.setSiblings(String.valueOf(row.get("siblings")));
				c.setLanguages(String.valueOf(row.get("languages")));
				c.setNationality(String.valueOf(row.get("nationality")));
				c.setEducationalLevel(String.valueOf(row.get("educational_level")));
				c.setCertifiedCpr(String.valueOf(row.get("certified_cpr")));
				c.setExp(String.valueOf(row.get("exp")));
				c.setMaritalStatus(String.valueOf(row.get("marital_status")));
				c.setReligion(String.valueOf(row.get("religion")));
				c.setMobile(String.valueOf(row.get("mobile")));
				c.setHeight(String.valueOf(row.get("height")));
				c.setWeight(String.valueOf(row.get("weight")));
				c.setMotivation(String.valueOf(row.get("motivation")));
				c.setAbout(String.valueOf(row.get("about")));
				c.setEducation(String.valueOf(row.get("education")));
				c.setSpecialities(String.valueOf(row.get("specialties")));
				c.setHobbies(String.valueOf(row.get("hobbies")));
				c.setAvailability(String.valueOf(row.get("availability")));
				c.setDateApplied((Date) row.get("date_applied"));
				c.setWorkInHK(String.valueOf(row.get("work_in_hk")));
				c.setWorkInSG(String.valueOf(row.get("work_in_sg")));
				c.setWorkInTW(String.valueOf(row.get("work_in_tw")));
				c.setSalaryHKD(String.valueOf(row.get("salary_hkd")));
				c.setSalarySGD(String.valueOf(row.get("salary_sgd")));
				c.setSalaryTWD(String.valueOf(row.get("salary_twd")));
				c.setFoodChoice(String.valueOf(row.get("food_choice")));
				c.setPhoto(String.valueOf(row.get("photo")));
				c.setSkype(String.valueOf(row.get("skype")));
				c.setResume(String.valueOf(row.get("resume")));
				c.setTag((Integer) row.get("tag"));
				c.setTagId((Integer) row.get("tag_id"));
				c.setTagStatus(String.valueOf(row.get("tag_status")));
				c.setTaggedDate((Date) row.get("tagged_date"));
				c.setContractedTo(String.valueOf(row.get("contracted_to")));
				c.setTaggedTo(String.valueOf(row.get("tagged_to")));
				c.setAssigned(String.valueOf(row.get("assigned")));
				c.setLocation(String.valueOf(row.get("location")));
				c.setRegistrationNumber(String.valueOf(row.get("registration_number")));
				c.setRemarks(String.valueOf(row.get("remarks")));
				c.setNearestAirport(String.valueOf(row.get("nearest_airport")));
				c.setCurrentAddress(String.valueOf(row.get("current_address")));
				c.setNumberOfChildren((Integer) row.get("number_of_children"));
				c.setChildrenNames(String.valueOf(row.get("children_names")));
				c.setWorkedInSG(String.valueOf(row.get("worked_in_sg")));
				c.setAllergies(String.valueOf(row.get("allergies")));
				c.setDiagnosedConditions(String.valueOf(row.get("diagnosed_conditions")));
				c.setVideoURL(String.valueOf(row.get("video_url")));
				c.setDateCreated((Date) row.get(row.get("datecreated")));
				c.setResumatorStatus(String.valueOf(row.get("applicant_status")));
				c.setStatus(Integer.valueOf(String.valueOf(row.get("status"))));
				c.setLastModified((Date) row.get("last_modified"));
				c.setTesdaNcii((String) row.get("tesda_ncii"));
				c.setRegisteredConcorde((String) row.get("registered_concorde"));
				c.setPreDeployment((String) row.get("pre_deployment"));
				c.setMedicalCertVerified((String) row.get("medical_cert_verified"));
				c.setNumbersOfPlacement((String) row.get("numbers_of_placement"));
				c.setDateOfPlacement((Date)row.get("date_of_placement"));
				c.setMarkedAdvancedPlacementScheme((Date)row.get("marked_as_advance_placement_scheme"));
				c.setMarkedMedicalCertVerify((Date)row.get("marked_as_medical_cer_verify"));
				c.setMarkedRegisteredWithConcorde((Date)row.get("marked_as_registered_with_concorde"));
				c.setDateOfAwaitingDocument((Date)row.get("date_awaiting_documents"));
				c.setDateOfSchedulingInterview((Date)row.get("date_scheduling_interview"));
				c.setDateOfInterviewScheduled((Date)row.get("date_interview_scheduled"));
				c.setDateOfShortlisted((Date)row.get("date_shortlisted"));
				c.setDateOfReadyForPlacement((Date)row.get("date_ready_for_placement"));
				c.setDateOfNotSelected((Date)row.get("date_not_selected"));
				c.setDateOfShortlistedWithDiffAvail((Date)row.get("date_shortlisted_with_diff_avail"));
				c.setDateOfTagged((Date)row.get("date_tagged"));
				c.setDateOfOnHold((Date)row.get("date_on_hold"));
				c.setDateOfBlacklisted((Date)row.get("date_blacklisted"));
				caregiverList.add(c);
			}
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception retrieving the list of companies", e);
		}

		logger.info("Found "+caregiverList.size()+" entries");
		return caregiverList;
	}

	private String getFormattedQueryCondition(String condition) {
		String[] string = condition.split("=");
		String columnName = "";
		String expression = "";
		String searchText = "";
		
		if("workingplace".equals(string[0])){
			String sql = "";
			if(string[2].startsWith("1")){
				sql = "work_in_hk = 'Yes' ";
			}else{
				sql = "work_in_hk = 'No' ";
			}
			if(string[2].endsWith("1")){
				sql += "and work_in_sg = 'Yes' ";
			}else{
				sql += "and work_in_sg = 'No' ";
			}
			return sql;
		}else if (string.length == 3) {
			columnName = string[0];
			expression = string[1];
			searchText = string[2];

			
			switch (expression) {
				case "exactly":
					expression = "='" + searchText + "'";
					break;
				case "contain":
					expression = "like " + "'%" + searchText.replace(' ', '%') + "%'";
					break;
				case "notcontain":
					expression = "not like " + "'%" + searchText + "%'";
					break;
				case "greater":
					if (columnName.equals("height") || columnName.equals("weight") || columnName.equals("age") || columnName.equals("numbers_of_placement")) {
						expression = ">" + searchText;
					}
					else {
						expression = ">'" + searchText + "'";
					}
					break;
				case "greaterequal":
					if (columnName.equals("height") || columnName.equals("weight") || columnName.equals("age") || columnName.equals("numbers_of_placement")) {
						expression = ">=" + searchText;
					}
					else {
						expression = ">='" + searchText + "'";
					}
					break;
				case "less":
					if (columnName.equals("height") || columnName.equals("weight") || columnName.equals("age") || columnName.equals("numbers_of_placement")) {
						expression = "<" + searchText;
					}
					else {
						expression = "<'" + searchText + "'";
					}
					break;
				case "lessequal":
					if (columnName.equals("height") || columnName.equals("weight") || columnName.equals("age") || columnName.equals("numbers_of_placement")) {
						expression = "<=" + searchText;
					}
					else {
						expression = "<='" + searchText + "'";
					}
					break;
				case "empty":
					expression = "is null";
					break;
				case "notempty":
					expression = "is not null";
					break;
				case "in":
					expression = " = '"+searchText+"' or " +
								 columnName +" like '"+searchText+",%' or "+
								 columnName +" like '%,"+searchText+",%' or " +
								 columnName +" like '%,"+searchText+"'";
					break;
				default:
					break;
			}
		}
        return "(" + columnName + " " + expression+")";
	}

	@Override
	public void saveSearch(String searchTitle, User user, String shareWithOtherAGSCUsers, String searchList) {
		String sql = " insert into search_history(name, user_id, share_status, query, create_date) values(?, ?, ?, ?, ?) ";

		try {

			jdbcTemplate.update(sql, new Object[] { searchTitle,
					user.getUserId(),
					shareWithOtherAGSCUsers,
					searchList,
					new Date()
			});
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception inserting a User into the database", e);
		}
	}

	@Override
	public void update(Caregiver caregiver) {
		String sql = "UPDATE candidate_profile SET resume=?,photo=?,photo_passport=?,photo_degrees=?,other_files=?,last_modified=? "
				+ "WHERE user_id='"+caregiver.getUserId()+"';";
		
		try {
			jdbcTemplate.update(sql, new Object[]{
					caregiver.getResume(),
					caregiver.getPhoto(),
					caregiver.getPhotoPassport(),
					caregiver.getPhotoDegrees(),
					caregiver.getOtherFiles(),
					new Date()
			});
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception updating a candidate into the database", e);
		}
	}

	@Override
	public void changeTheStatisOfRegisteredConcorde(String userId, String caregiverId, String status) {
		String sql = "update candidate_profile set registered_concorde = ?, userid_registerd_with_concorde = ?, marked_as_registered_with_concorde = ?, last_modified=?  where user_id = ?";
		Date date = getChangeStatusDate(status);
		try{
			jdbcTemplate.update(sql, new Object[]{status, userId, date, new Date(), caregiverId});
		}catch(DataAccessException e){
			logger.error("Data Access Exception updating a candidate's status of registering with concorde into the database", e);
		}
	}

	@Override
	public void changeTheStatusOfAdvancePlacementScheme(String userId, String caregiverId, String status) {
		String sql = "update candidate_profile set pre_deployment = ?, userid_advanced_placement_schema = ?, marked_as_advance_placement_scheme = ?, last_modified=? where user_id = ?";
		Date date = getChangeStatusDate(status);
		try{
			jdbcTemplate.update(sql, new Object[]{status, userId, date, new Date(), caregiverId});
		}catch(DataAccessException e){
			logger.error("Data Access Exception updating a candidate's status of advance placement scheme into the database", e);
		}
	}

	private Date getChangeStatusDate(String status) {
		Date date = new Date();
		if("NO".equals(status)){
			date = null;
		}
		return date;
	}

	@Override
	public void changeTheStatusOfMedicalCertVerified(String validatorId, String caregiverId, String status) {
		String sql = "update candidate_profile set medical_cert_verified = ?, medical_cert_validator = ?, marked_as_medical_cer_verify = ?, last_modified = ? where user_id = ?";
		Date date = getChangeStatusDate(status);
		try{
			jdbcTemplate.update(sql, new Object[]{status, validatorId, date, new Date(), caregiverId});
		}catch(DataAccessException e){
			logger.error("Data Access Exception updating a candidate's status of advance placement scheme in the database", e);
		}
	}

	@Override
	public void saveBio(Bio bio) {
		String sql = "insert into bio(candidateid, basic_info, education_experience, experience_details, cpr_or_first_aid, nursing_experience, hobby, last_modify) values (?,?,?,?,?,?,?,?)";
		
		try{
			jdbcTemplate.update(sql, new Object[]{
					bio.getCaregiverId(), 
					StringUtil.filterSurrogateCharacters(bio.getCandidateBasicInformation()) ,
					StringUtil.filterSurrogateCharacters(bio.getEducationAndExperience()),
					StringUtil.filterSurrogateCharacters(bio.getExperienceDetails()),
					bio.getTrainedToCprOrFA(),
					StringUtil.filterSurrogateCharacters(bio.getNursingExperience()),
					StringUtil.filterSurrogateCharacters(bio.getHobby()),
					new Date()
			});
		}catch(DataAccessException e){
			logger.error("Data Access Exception adding a candidate's bio into the database", e);
		}
	}

	@Override
	public Bio getBioByCaregiverId(int caregiverId) {
		Bio bio = null;
		
		String sql = "select * from bio where candidateid = " + caregiverId;
		try{
			List<Map<String, Object>> resultMap = jdbcTemplate.queryForList(sql);
			if(resultMap.size() > 0){
				bio = new Bio();
				for(Map<String, Object> result : resultMap){
					bio.setCaregiverId((Integer)result.get("candidateid"));
					bio.setValidatorId((Integer)result.get("validatorid"));
					bio.setCandidateBasicInformation((String)result.get("basic_info"));
					bio.setEducationAndExperience((String)result.get("education_experience"));
					bio.setExperienceDetails((String)result.get("experience_details"));
					bio.setTrainedToCprOrFA((String)result.get("cpr_or_first_aid"));
					bio.setNursingExperience((String)result.get("nursing_experience"));
					bio.setHobby((String)result.get("hobby"));	
					bio.setLastModifyTime((Date)result.get("last_modify"));
				}
			}		
		}catch(DataAccessException e){
			logger.error("Data Access Exception getting a candidats's bio from the database", e);
		}
		return bio;
	}

	@Override
	public void editBioInfoByCaregiverId(Bio bio) {
		String sql = "update bio set validatorid = ? , basic_info = ?, education_experience = ?, experience_details = ?, cpr_or_first_aid = ?, nursing_experience = ?, hobby = ?, last_modify = ? where candidateid = ?";
		
		try{
			jdbcTemplate.update(sql, new Object[]{
				bio.getValidatorId(),
				bio.getCandidateBasicInformation(),
				bio.getEducationAndExperience(),
				bio.getExperienceDetails(),
				bio.getTrainedToCprOrFA(),
				bio.getNursingExperience(),
				bio.getHobby(),
				new Date(),
				bio.getCaregiverId()
			});
		}catch(DataAccessException e){
			logger.error("Data Access Exception updating a candidate's bio in the database", e);
		}
	}

	@Override
	public Integer getTotalAmount(String querySql) {
		String sql = "select count(user_id) from candidate_profile where status in (5, 7, 8, 9) " + querySql + " ;";
		try{
			return jdbcTemplate.queryForObject(sql, Integer.class);
		}catch(DataAccessException e){
			logger.error("Data Access Exception getting total candidate's amount in the database", e);
		}
		return 0;
	}

	@Override
	public Integer getAvailableAmount(String querySql) {
		String sql = "select count(user_id) from candidate_profile where status = 7 " + querySql + ";";
		try{
			return jdbcTemplate.queryForObject(sql, Integer.class);
		}catch(DataAccessException e){
			logger.error("Data Access Exception getting avaliable candidate's amount in the database", e);
		}
		return 0;
	}

	@Override
	public Integer getContractedAmount(String querySql) {
		String sql = "select count(user_id) from candidate_profile where status = 9 " + querySql + ";";
		try{
			return jdbcTemplate.queryForObject(sql, Integer.class);
		}catch(DataAccessException e){
			logger.error("Data Access Exception getting contracted candidate's amount in the database", e);
		}
		return 0;
	}

	@Override
	public Integer getNewAmount(String querySql) {
		String sql = "select count(user_id) from candidate_profile where status = 7 and DATEDIFF(now(), marked_as_ready_time) < 7  " + querySql + ";";
		try{
			return jdbcTemplate.queryForObject(sql, Integer.class);
		}catch(DataAccessException e){
			logger.error("Data Access Exception getting new candidate's amount in the database", e);
		}
		return 0;
	}

	@Override
	public Integer getTaggedAmount(String querySql) {
		String sql = "select count(user_id) from candidate_profile where status = 8 " + querySql + ";";
		try{
			return jdbcTemplate.queryForObject(sql, Integer.class);
		}catch(DataAccessException e){
			logger.error("Data Access Exception getting tagged candidate's amount in the database", e);
		}
		return 0;
	}

	@Override
	public Integer getOnHoldAmount(String querySql) {
		String sql = "select count(user_id) from candidate_profile where status = 5 " + querySql + ";";
		try{
			return jdbcTemplate.queryForObject(sql, Integer.class);
		}catch(DataAccessException e){
			logger.error("Data Access Exception getting on-hold candidate's amount in the database", e);
		}
		return 0;
	}

	public void saveSendCV(SendCV cv, String sData) {
		//String sql = "INSERT INTO sent_history(user_id,sent_cv,sent_to,date) values(?,?,?,?);";
		final SendCV sendCV = cv;
		final String[] ids = sData.split(",");
		String sql= "INSERT INTO sent_history(user_id,sent_cv,sent_to,date) values(?,?,?,?);";
		
		try{
			jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter(){
				public void setValues(PreparedStatement ps,int i)throws SQLException{
					String id=ids[i];
					ps.setString(1, sendCV.getUserId());
					ps.setString(2, id);
					ps.setString(3, sendCV.getSentTo());
					ps.setTimestamp(4, new Timestamp(sendCV.getDate().getTime()));
				}
				public int getBatchSize(){
					return ids.length;
				}
			});
		}catch(DataAccessException e){
			logger.error("Data Access Exception updating a sendCV history of advance placement scheme into the database", e);
		}
	}

	@Override
	public List<SendCV> getSendCv() {
		List<SendCV> list = new ArrayList<SendCV>();
		String sql = "SELECT s.*,c.full_name FROM sent_history s LEFT JOIN candidate_profile c on c.user_id = s.sent_cv";
		try {
			List<Map<String, Object>> map = jdbcTemplate.queryForList(sql);
			for(Map<String, Object> m : map){
				SendCV sendCV = new SendCV();
				sendCV.setFullName((String) m.get("full_name"));
				sendCV.setSentTo((String) m.get("sent_to"));
				sendCV.setDate((Date) m.get("date"));
				
				list.add(sendCV);
			}
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception retrieving the list of sendCV", e);
		}
		
		return list;

	}

	@Override
	public List<Caregiver> getCaregiverListIds(String ids) {
		String[] id = ids.split(",");
		String dataId = "";
		for(int i = 0; i<id.length; i++){
			if(i < id.length-1){
				dataId = dataId +"'"+ id[i] +"',";
			}else{
				dataId = dataId +"'"+ id[i] +"'";
			}
		}
		
		String sql = "SELECT * FROM candidate_profile c "
				+ "LEFT JOIN bio b ON b.candidateid=c.user_id "
				+ "WHERE c.user_id in (" +dataId+ ")";
		List<Caregiver> list = new ArrayList<Caregiver>();
		
		try {
			List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
			for(Map<String, Object> row : mapList){
				Caregiver c = new Caregiver();
				Bio bio = new Bio();
				
				c.setUserId(String.valueOf(row.get("user_id")));
				c.setEmail(String.valueOf(row.get("email")));
				c.setFullName(String.valueOf(row.get("full_name")));
				c.setFirstName(String.valueOf(row.get("first_name")));
				c.setLastName(String.valueOf(row.get("last_name")));
				c.setGender(String.valueOf(row.get("gender")));
				c.setDateOfBirth((Date) row.get("dob"));
				c.setAge(String.valueOf(row.get("age")));
				c.setCountryOfBirth(String.valueOf(row.get("country_of_birth")));
				c.setHasChildren(String.valueOf(row.get("has_children")));
				c.setSiblings(String.valueOf(row.get("siblings")));
				c.setLanguages(String.valueOf(row.get("languages")));
				c.setNationality(String.valueOf(row.get("nationality")));
				c.setEducationalLevel(String.valueOf(row.get("educational_level")));
				c.setCertifiedCpr(String.valueOf(row.get("certified_cpr")));
				c.setExp(String.valueOf(row.get("exp")));
				c.setMaritalStatus(String.valueOf(row.get("marital_status")));
				c.setReligion(String.valueOf(row.get("religion")));
				c.setMobile(String.valueOf(row.get("mobile")));
				c.setHeight(String.valueOf(row.get("height")));
				c.setWeight(String.valueOf(row.get("weight")));
				c.setMotivation(String.valueOf(row.get("motivation")));
				c.setAbout(String.valueOf(row.get("about")));
				c.setEducation(String.valueOf(row.get("education")));
				c.setSpecialities(String.valueOf(row.get("specialties")));
				c.setHobbies(String.valueOf(row.get("hobbies")));
				c.setAvailability(String.valueOf(row.get("availability")));
				c.setDateApplied((Date) row.get("date_applied"));
				c.setWorkInHK(String.valueOf(row.get("work_in_hk")));
				c.setWorkInSG(String.valueOf(row.get("work_in_sg")));
				c.setWorkInTW(String.valueOf(row.get("work_in_tw")));
				c.setSalaryHKD(String.valueOf(row.get("salary_hkd")));
				c.setSalarySGD(String.valueOf(row.get("salary_sgd")));
				c.setSalaryTWD(String.valueOf(row.get("salary_twd")));
				c.setFoodChoice(String.valueOf(row.get("food_choice")));
				c.setPhoto(String.valueOf(row.get("photo")));
				c.setSkype(String.valueOf(row.get("skype")));
				c.setResume(String.valueOf(row.get("resume")));
				c.setTag((Integer) row.get("tag"));
				c.setTagId((Integer) row.get("tag_id"));
				c.setTagStatus(String.valueOf(row.get("tag_status")));
				c.setTaggedDate((Date) row.get(row.get("tagged_date")));
				c.setContractedTo(String.valueOf(row.get("contracted_to")));
				c.setTaggedTo(String.valueOf(row.get("tagged_to")));
				c.setAssigned(String.valueOf(row.get("assigned")));
				c.setLocation(String.valueOf(row.get("location")));
				c.setRegistrationNumber(String.valueOf(row.get("registration_number")));
				c.setRemarks(String.valueOf(row.get("remarks")));
				c.setNearestAirport(String.valueOf(row.get("nearest_airport")));
				c.setCurrentAddress(String.valueOf(row.get("current_address")));
				c.setNumberOfChildren((Integer) row.get("number_of_children"));
				c.setChildrenNames(String.valueOf(row.get("children_names")));
				c.setWorkedInSG(String.valueOf(row.get("worked_in_sg")));
				c.setAllergies(String.valueOf(row.get("allergies")));
				c.setDiagnosedConditions(String.valueOf(row.get("diagnosed_conditions")));
				c.setVideoURL(String.valueOf(row.get("video_url")));
				c.setDateCreated((Date) row.get(row.get("datecreated")));
				c.setResumatorStatus(String.valueOf(row.get("applicant_status")));
				c.setApplyLocations(String.valueOf(row.get("applyLocations")));
				c.setChildrenNameAge(String.valueOf(row.get("children_name_age")));
				c.setHoldPassport(String.valueOf(row.get("hold_passport")));
				c.setYearGraduation(String.valueOf(row.get("year_graduation")));
				c.setYearStudies(String.valueOf(row.get("year_studies")));
				c.setCaregiverBeforeExp(String.valueOf(row.get("caregiver_before_exp")));
				c.setSgFin(String.valueOf(row.get("sg_fin")));
				c.setCurrentJob(String.valueOf(row.get("current_job")));
				c.setCurrentJobTime(String.valueOf(row.get("current_job_time")));
				c.setTimeOfSg(String.valueOf(row.get("time_of_sg")));
				c.setHistoryOfTreatment(String.valueOf(row.get("history_of_treatment")));
				c.setInterviewTime(String.valueOf(row.get("interview_time")));
				c.setReferrer(String.valueOf(row.get("referrer")));
				c.setPhotoPassport(String.valueOf(row.get("photo_passport")));
				c.setPhotoDegrees(String.valueOf(row.get("photo_degrees")));
				c.setOtherFiles(String.valueOf(row.get("other_files")));
				c.setStatus(Integer.valueOf(String.valueOf(row.get("status"))));
				c.setTesdaNcii((String) row.get("tesda_ncii"));
				c.setRegisteredConcorde((String) row.get("registered_concorde"));
				c.setPreDeployment((String) row.get("pre_deployment"));
				c.setMedicalCertVerified((String) row.get("medical_cert_verified"));
				c.setNumbersOfPlacement((String) row.get("numbers_of_placement"));
				
				//bio
				bio.setCandidateBasicInformation((String)row.get("basic_info"));
				bio.setEducationAndExperience((String)row.get("education_experience"));
				bio.setExperienceDetails((String)row.get("experience_details"));
				bio.setTrainedToCprOrFA((String)row.get("cpr_or_first_aid"));
				bio.setNursingExperience((String)row.get("nursing_experience"));
				bio.setHobby((String)row.get("hobby"));
				
				c.setBio(bio);
				list.add(c);
			}
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception retrieving the list of caregiver", e);
		}
		
		return list;
	}

	@Override
	public void updateCaregiver(Caregiver caregiver) {
		String sql = "update candidate_profile set first_name=?,last_name=?,full_name=?,tag=?,tagged_to=?,"		//5
					+ "contracted_to=?,gender=?,educational_level=?,exp=?,languages=?,"							//10
					+ "salary_hkd=?,salary_sgd=?,salary_twd=?,marital_status=?,age=?,"							//15
					+ "dob=?,religion=?,food_choice=?,nationality=?,country_of_birth=?,"						//20
					+ "has_children=?,height=?,weight=?,motivation=?,about=?,"									//25
					+ "education=?,specialties=?,certified_cpr=?,hobbies=?,mobile=?,"							//30
					+ "skype=?,remarks=?,last_modified=?,tagged_date=?,tag_status=?,tag_id=? WHERE user_id=?";					//36							//33
		
		try {
			jdbcTemplate.update(sql, new Object[]{
									caregiver.getFirstName(),
									caregiver.getLastName(),
									caregiver.getFullName(),
									caregiver.getTag(),
									caregiver.getTaggedTo(),
									caregiver.getContractedTo(),
									caregiver.getGender(),
									caregiver.getEducationalLevel(),
									caregiver.getExp(),
									caregiver.getLanguageList(),		//10
									caregiver.getSalaryHKD(),
									caregiver.getSalarySGD(),
									caregiver.getSalaryTWD(),
									caregiver.getMaritalStatus(),
									caregiver.getAge(),
									caregiver.getDateOfBirth(),
									caregiver.getReligion(),
									caregiver.getFoodChoice(),
									caregiver.getNationality(),
									caregiver.getCountryOfBirth(),		//20
									caregiver.getHasChildren(),
									caregiver.getHeight(),
									caregiver.getWeight(),
									caregiver.getMotivation(),
									caregiver.getAbout(),
									caregiver.getEducation(),
									caregiver.getSpecialitiesList(),
									caregiver.getCertifiedCpr(),
									caregiver.getHobbies(),
									caregiver.getMobile(),				//30
									caregiver.getSkype(),
									caregiver.getRemarks(),
									new Date(),
									caregiver.getTaggedDate(),
									caregiver.getTagStatus(),
									caregiver.getTagId(),
									caregiver.getUserId()				//37
								});
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception updating a candidate into the database", e);
		}
		
		
	}

	@Override
	public void updatePhoto(Caregiver caregiver) {
		String sql = "UPDATE candidate_profile SET photo=? WHERE user_id=?";
		
		try {
			jdbcTemplate.update(sql, new Object[]{caregiver.getPhoto(),caregiver.getUserId()});
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception updating a candidate's photo into the database", e);
		}
	}

	@Override
	public Caregiver getCaregiverBio(String userId) {
		Caregiver c = new Caregiver();
		Bio bio = new Bio();
		String sql = "SELECT * FROM candidate_profile c "
				+ "LEFT JOIN bio b ON b.candidateid=c.user_id "
				+ "WHERE c.user_id=?";
		
		try {
			Map<String, Object> row = jdbcTemplate.queryForMap(sql,new Object[]{userId});
			if(row != null && row.size()>0){
				c.setSgFin(String.valueOf(row.get("sg_fin")));
				c.setUserId(String.valueOf(row.get("user_id")));
				c.setEmail(String.valueOf(row.get("email")));
				c.setFullName(String.valueOf(row.get("full_name")));
				c.setFirstName(String.valueOf(row.get("first_name")));
				c.setLastName(String.valueOf(row.get("last_name")));
				c.setGender(String.valueOf(row.get("gender")));
				c.setDateOfBirth((Date) row.get("dob"));
				c.setAge(String.valueOf(row.get("age")));
				c.setCountryOfBirth(String.valueOf(row.get("country_of_birth")));
				c.setHasChildren(String.valueOf(row.get("has_children")));
				c.setSiblings(String.valueOf(row.get("siblings")));
				c.setLanguages(String.valueOf(row.get("languages")));
				c.setNationality(String.valueOf(row.get("nationality")));
				c.setEducationalLevel(String.valueOf(row.get("educational_level")));
				c.setCertifiedCpr(String.valueOf(row.get("certified_cpr")));
				c.setExp(String.valueOf(row.get("exp")));
				c.setMaritalStatus(String.valueOf(row.get("marital_status")));
				c.setReligion(String.valueOf(row.get("religion")));
				c.setMobile(String.valueOf(row.get("mobile")));
				c.setHeight(String.valueOf(row.get("height")));
				c.setWeight(String.valueOf(row.get("weight")));
				c.setMotivation(String.valueOf(row.get("motivation")));
				c.setAbout(String.valueOf(row.get("about")));
				c.setEducation(String.valueOf(row.get("education")));
				c.setSpecialities(String.valueOf(row.get("specialties")));
				c.setHobbies(String.valueOf(row.get("hobbies")));
				c.setAvailability(String.valueOf(row.get("availability")));
				c.setDateApplied((Date) row.get("date_applied"));
				c.setWorkInHK(String.valueOf(row.get("work_in_hk")));
				c.setWorkInSG(String.valueOf(row.get("work_in_sg")));
				c.setWorkInTW(String.valueOf(row.get("work_in_tw")));
				c.setSalaryHKD(String.valueOf(row.get("salary_hkd")));
				c.setSalarySGD(String.valueOf(row.get("salary_sgd")));
				c.setSalaryTWD(String.valueOf(row.get("salary_twd")));
				c.setFoodChoice(String.valueOf(row.get("food_choice")));
				c.setPhoto(String.valueOf(row.get("photo")));
				c.setSkype(String.valueOf(row.get("skype")));
				c.setResume(String.valueOf(row.get("resume")));
				c.setTag((Integer) row.get("tag"));
				c.setTagId((Integer) row.get("tag_id"));
				c.setTagStatus(String.valueOf(row.get("tag_status")));
				c.setTaggedDate((Date) row.get(row.get("tagged_date")));
				c.setContractedTo(String.valueOf(row.get("contracted_to")));
				c.setTaggedTo(String.valueOf(row.get("tagged_to")));
				c.setAssigned(String.valueOf(row.get("assigned")));
				c.setLocation(String.valueOf(row.get("location")));
				c.setRegistrationNumber(String.valueOf(row.get("registration_number")));
				c.setRemarks(String.valueOf(row.get("remarks")));
				c.setNearestAirport(String.valueOf(row.get("nearest_airport")));
				c.setCurrentAddress(String.valueOf(row.get("current_address")));
				c.setNumberOfChildren((Integer) row.get("number_of_children"));
				c.setChildrenNames(String.valueOf(row.get("children_names")));
				c.setWorkedInSG(String.valueOf(row.get("worked_in_sg")));
				c.setAllergies(String.valueOf(row.get("allergies")));
				c.setDiagnosedConditions(String.valueOf(row.get("diagnosed_conditions")));
				c.setVideoURL(String.valueOf(row.get("video_url")));
				c.setDateCreated((Date) row.get(row.get("datecreated")));
				c.setResumatorStatus(String.valueOf(row.get("applicant_status")));
				c.setStatus(Integer.valueOf(String.valueOf(row.get("status"))));
				c.setLastModified((Date) row.get("last_modified"));
				c.setTesdaNcii((String) row.get("tesda_ncii"));
				c.setRegisteredConcorde((String) row.get("registered_concorde"));
				c.setPreDeployment((String) row.get("pre_deployment"));
				c.setDateApplied((Date) row.get("date_applied"));
				c.setNumbersOfPlacement((String) row.get("numbers_of_placement"));
				
				//bio
				bio.setCandidateBasicInformation((String)row.get("basic_info"));
				bio.setEducationAndExperience((String)row.get("education_experience"));
				bio.setExperienceDetails((String)row.get("experience_details"));
				bio.setTrainedToCprOrFA((String)row.get("cpr_or_first_aid"));
				bio.setNursingExperience((String)row.get("nursing_experience"));
				bio.setHobby((String)row.get("hobby"));	
				
				c.setBio(bio);
			}
		}catch (DataAccessException e) {
			logger.error("Data Access Exception searching candidate_profile", e);
		}
		return c;
	}
	
	public List<Caregiver> getCandidatesByParams(String queryInfo) {
		List<Caregiver> candidateList = new ArrayList<Caregiver>();
		String sql = "select * from candidate_profile left join bio on candidate_profile.user_id = bio.candidateid " + queryInfo + ";";
		logger.info("getCandidatesByParams sql: "+sql);
		try{
			List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			for(Map<String, Object> result : resultList){
				Caregiver candidate = new Caregiver();
				candidate.setUserId(result.get("user_id").toString());
				candidate.setFullName((String) result.get("full_name"));
				//candidate.setDateApplied((Date)result.get("date_applied"));
				candidate.setD_applied(sdf.format(result.get("date_applied")));
				
				candidate.setAppId((String)result.get("app_id"));
				candidate.setAge((String)result.get("age"));
				candidate.setEmail((String)result.get("email"));
				candidate.setMobile((String)result.get("mobile"));
				candidate.setVideoURL((String)result.get("video_url"));
				candidate.setWorkInHK((String)result.get("work_in_hk"));
				candidate.setWorkInSG((String)result.get("work_in_sg"));
				candidate.setWorkInTW((String)result.get("work_in_tw"));
				candidate.setSkype((String)result.get("skype"));
				candidate.setGender((String)result.get("gender"));
				Object dob = result.get("dob");
				if(dob != null) {
					candidate.setD_of_birth(sdf.format(dob));
				}
				
				candidate.setCountryOfBirth((String)result.get("country_of_birth"));
				candidate.setNearestAirport((String)result.get("nearest_airport"));
				candidate.setNationality((String)result.get("nationality"));
				candidate.setHeight((String)result.get("height"));
				candidate.setWeight((String)result.get("weight"));
				candidate.setMaritalStatus((String)result.get("marital_status"));
				candidate.setHasChildren((String)result.get("has_children"));
				candidate.setSiblings((String)result.get("siblings"));
				candidate.setEducationalLevel((String)result.get("educational_level"));
				candidate.setExp((String)result.get("exp"));
				candidate.setAvailability((String)result.get("availability"));
				candidate.setSpecialities((String)result.get("specialties"));
				candidate.setLanguages((String)result.get("languages"));
				candidate.setReligion((String)result.get("religion"));
				candidate.setFoodChoice((String)result.get("food_choice"));
				candidate.setCertifiedCpr((String)result.get("certified_cpr"));
				candidate.setAvailability((String)result.get("availability"));
				candidate.setMotivation((String)result.get("motivation"));
				candidate.setWorkedInSG((String)result.get("worked_in_sg"));
				candidate.setAllergies((String)result.get("allergies"));
				candidate.setStatus((Integer)result.get("status"));
				candidate.setTag((int) result.get("tag"));
				candidate.setTagOrigin(candidate.getTag());
				candidate.setTaggedDate((Date) result.get("tagged_date"));
				candidate.setTaggedTo((String)result.get("tagged_to"));
				candidate.setTagStatus((String) result.get("tag_status"));
				candidate.setContractedTo((String)result.get("contracted_to"));
				candidate.setDiagnosedConditions((String)result.get("diagnosed_conditions"));
				candidate.setAbout((String)result.get("basic_info"));
				candidate.setChildrenNames((String)result.get("children_names"));
				candidate.setAppId((String)result.get("app_id"));
				StringBuffer sb = new StringBuffer();
				if(StringUtils.isNoneBlank((String)result.get("education_experience"))){
					sb.append((String)result.get("education_experience"));
				}
				if(StringUtils.isNoneBlank((String)result.get("experience_details"))){
					sb.append((String)result.get("experience_details"));
				}
				candidate.setEducation(sb.toString());
				candidate.setHobbies((String)result.get("hobby"));
				candidate.setCurrentAddress((String)result.get("current_address"));
				candidate.setSalaryHKD((String)result.get("salary_hkd"));
				candidate.setSalarySGD((String)result.get("salary_sgd"));
				candidate.setSalaryTWD((String)result.get("salary_twd"));
				candidate.setMarkedAsRedayTime((Date)result.get("marked_as_ready_time"));
				candidate.setNumbersOfPlacement((String) result.get("numbers_of_placement"));
				candidate.setDateOfReadyForPlacement((Date)result.get("date_ready_for_placement"));
				if(candidate.getDateOfReadyForPlacement() != null){
					long diff = System.currentTimeMillis() - candidate.getDateOfReadyForPlacement().getTime();
					if(diff <= TIME_THRESHOLD_FOR_NEW_CANDIDATE){
						candidate.setNewCaregiverFlag("new");
					}
				}
				if(candidate.getMarkedAsRedayTime() != null){
					long diff = System.currentTimeMillis() - candidate.getMarkedAsRedayTime().getTime();
					if(diff <= TIME_THRESHOLD_FOR_NEW_CANDIDATE){
						candidate.setNewCaregiverFlag("new");
					}
				}
				candidateList.add(candidate);
			}
		}catch(DataAccessException e){
			logger.error("Data Access Exception retrieving the list of candidate_profile according to the status", e);
		}
		logger.info("Found "+candidateList.size()+" entries");
		return candidateList;
	}

	@Override
	public int saveCaregiverOfDashboard(final Caregiver candidate) {
		final String sql = "insert into candidate_profile("
				+ "tag, contracted_to, tagged_to, date_applied, full_name, "
				+ "email, mobile, video_url, work_in_sg, work_in_hk, "
				+ "work_in_tw, skype, gender, dob, age, "
				+ "country_of_birth, nearest_airport, nationality, height, weight, "
				+ "marital_status, has_children, children_names, siblings, languages, "
				+ "religion, food_choice, educational_level, exp, certified_cpr, "
				+ "specialties, availability, motivation, worked_in_sg, current_address, "
				+ "allergies, diagnosed_conditions, last_modified, first_name, last_name, "
				+ "app_id, status, marked_as_ready_time, tag_status, tag_id, "
				+ "salary_hkd, salary_sgd, salary_twd, date_of_placement, numbers_of_placement"
				+ ") values("
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?"
				+ ")";
		try{
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator(){
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException{
					PreparedStatement ps = jdbcTemplate.getDataSource().getConnection().prepareStatement(sql, new String[]{
							"tag","contracted_to","tagged_to","date_applied","full_name",
							"email","mobile","video_url","work_in_sg","work_in_hk",
							"work_in_tw","skype","gender","dob","age",
							"country_of_birth","nearest_airport","nationality","height","weight",
							"marital_status","has_children","children_names","siblings","languages",
							"religion","food_choice","educational_level","exp","certified_cpr",
							"specialties","availability","motivation","worked_in_sg","current_address",
							"allergies","diagnosed_conditions","last_modified","first_name","last_name",
							"app_id", "status", "marked_as_ready_time", "tag_status", "tag_id",
							"salary_hkd", "salary_sgd", "salary_twd", "date_of_placement", "numbers_of_placement" 
					});
					ps.setInt(1, candidate.getTag());
					ps.setString(2, candidate.getContractedTo());
					ps.setString(3, candidate.getTaggedTo());
					ps.setDate(4, new java.sql.Date(candidate.getDateApplied().getTime()));
					ps.setString(5, candidate.getFullName());
					ps.setString(6, candidate.getEmail());
					ps.setString(7, candidate.getMobile());
					ps.setString(8, candidate.getVideoURL());
					ps.setString(9, candidate.getWorkInSG());
					ps.setString(10, candidate.getWorkInHK());
					ps.setString(11, candidate.getWorkInTW());
					ps.setString(12, candidate.getSkype());
					ps.setString(13, candidate.getGender());
					if(candidate.getDateOfBirth() != null){
						ps.setDate(14, new java.sql.Date(candidate.getDateOfBirth().getTime()));	
					}else{
						ps.setDate(14, null);
					}
					ps.setString(15, candidate.getAge());
					ps.setString(16, candidate.getCountryOfBirth());
					ps.setString(17, candidate.getNearestAirport());
					ps.setString(18, candidate.getNationality());
					ps.setString(19, candidate.getHeight());
					ps.setString(20, candidate.getWeight());
					ps.setString(21, candidate.getMaritalStatus());
					ps.setString(22, candidate.getHasChildren());
					ps.setString(23, candidate.getChildrenNames());
					ps.setString(24, candidate.getSiblings());
					ps.setString(25, candidate.getLanguages());
					ps.setString(26, candidate.getReligion());
					ps.setString(27, candidate.getFoodChoice());
					ps.setString(28, candidate.getEducationalLevel());
					ps.setString(29, candidate.getExp());
					ps.setString(30, candidate.getCertifiedCpr());
					ps.setString(31, candidate.getSpecialities());
					ps.setString(32, candidate.getAvailability());
					ps.setString(33, candidate.getMotivation());
					ps.setString(34, candidate.getWorkedInSG());
					ps.setString(35, candidate.getCurrentAddress());
					ps.setString(36, candidate.getAllergies());
					ps.setString(37, candidate.getDiagnosedConditions());
					ps.setDate(38, new java.sql.Date(new Date().getTime()));
					ps.setString(39, candidate.getFirstName());
					ps.setString(40, candidate.getLastName());
					ps.setString(41, candidate.getAppId());
					ps.setInt(42, candidate.getStatus());
					if(candidate.getMarkedAsRedayTime() != null){
						ps.setDate(43, new java.sql.Date(candidate.getMarkedAsRedayTime().getTime()));
					}else{
						ps.setDate(43, null);
					}
					ps.setString(44, candidate.getTagStatus());
					ps.setInt(45, candidate.getTagId());
					ps.setString(46, candidate.getSalaryHKD());
					ps.setString(47, candidate.getSalarySGD());
					ps.setString(48, candidate.getSalaryTWD());
					if(candidate.getDateOfPlacement() != null){
						ps.setDate(49, new java.sql.Date(candidate.getDateOfPlacement().getTime()));
					}else{
						ps.setDate(49, null);
					}
					ps.setString(50, candidate.getNumbersOfPlacement());
					return ps;
				}
			}, keyHolder);
			return keyHolder.getKey().intValue();
		}catch(DataAccessException e){
			logger.error("Data Access Exception adding candidate to candidate_profile in the dashboard page", e);
		}
		return 0;
	}

	@Override
	public void editCaregiverOfDashboard(Caregiver candidate) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String sql = "update candidate_profile set "
					 + "tag = ?, contracted_to = ?, tagged_to = ? , tagged_date = ?, date_applied = ?, full_name = ?, "
					 + "email = ?, mobile = ?, video_url = ?, work_in_sg = ?, work_in_hk = ?, "
					 + "work_in_tw = ?, skype = ?, gender = ?, dob = ?, age = ?, "
					 + "country_of_birth = ?, nearest_airport = ?, nationality = ?, height = ?, weight = ?, "
					 + "marital_status = ?, has_children = ?, children_names = ?, siblings = ?, languages = ?, "
					 + "religion = ?, food_choice = ?, educational_level = ?, exp = ?, certified_cpr = ?, "
					 + "specialties = ?, availability = ?, motivation = ?, worked_in_sg= ?, current_address = ?, "
					 + "allergies = ?, diagnosed_conditions = ?, last_modified = ?, first_name = ?, last_name = ?, "
					 + "app_id = ?, status = ?, marked_as_ready_time = ?, tag_status = ?, "
					 + "tag_id = ?, "
					 + createUpdateSalaryStatement(user)
					 + "date_of_placement = ?, "
					 + "numbers_of_placement = ? "
					 + "where user_id = ?";
		try{
			ArrayList values = new ArrayList();
			values.add(candidate.getTag());
			values.add(candidate.getContractedTo());
			values.add(candidate.getTaggedTo());
			values.add(candidate.getTaggedDate());
			values.add(candidate.getDateApplied());
			values.add(candidate.getFullName());
			values.add(candidate.getEmail());
			values.add(candidate.getMobile());
			values.add(candidate.getVideoURL());
			values.add(candidate.getWorkInSG());
			values.add(candidate.getWorkInHK());
			values.add(candidate.getWorkInTW());
			values.add(candidate.getSkype());
			values.add(candidate.getGender());
			values.add(candidate.getDateOfBirth());
			values.add(candidate.getAge());
			values.add(candidate.getCountryOfBirth());

			values.add(candidate.getNearestAirport());
			values.add(candidate.getNationality());
			values.add(candidate.getHeight());
			values.add(candidate.getWeight());
			values.add(candidate.getMaritalStatus());
			values.add(candidate.getHasChildren());
			values.add(candidate.getChildrenNames());
			values.add(candidate.getSiblings());
			values.add(candidate.getLanguages());
			values.add(candidate.getReligion());
			values.add(candidate.getFoodChoice());
			values.add(candidate.getEducationalLevel());
			values.add(candidate.getExp());
			values.add(candidate.getCertifiedCpr());
			values.add(candidate.getSpecialities());
			values.add(candidate.getAvailability());
			values.add(candidate.getMotivation());
			values.add(candidate.getWorkedInSG());
			values.add(candidate.getCurrentAddress());
			values.add(candidate.getAllergies());
			values.add(candidate.getDiagnosedConditions());
			values.add(candidate.getLastModified());
			values.add(candidate.getFirstName());
			values.add(candidate.getLastName());
			values.add(candidate.getAppId());
			values.add(candidate.getStatus());
			values.add(candidate.getMarkedAsRedayTime());
			values.add(candidate.getTagStatus());
			values.add(candidate.getTagId());

			addSalaryParameters(user, values, candidate.getSalaryHKD(), candidate.getSalarySGD(),
								candidate.getSalaryTWD());

			values.add(candidate.getDateOfPlacement());
			values.add(candidate.getNumbersOfPlacement());
			values.add(candidate.getUserId());

			logger.info("editCaregiverOfDashboard sql:"+sql);
			logger.info("values:"+values);
			logger.info("user:"+user+" auth:"+user.getAuthorities());

			jdbcTemplate.update(sql, values.toArray() );
		}catch(DataAccessException e ){
			logger.error("Data Access Exception editing candidate_profile", e);
		}
	}

	private void addSalaryParameters(User user,
									 ArrayList values,
									 String salaryHKD,
									 String salarySGD,
									 String salaryTWD)
	{
		if(userHasRole(user, "ROLE_ADMIN") || userHasRole(user, "ROLE_SUB_ADMIN") || userHasRole(user, "ROLE_SALES_HK"))
        {
            values.add(salaryHKD);
        }

		if(userHasRole(user, "ROLE_ADMIN") || userHasRole(user, "ROLE_SUB_ADMIN") || userHasRole(user, "ROLE_SALES_SG"))
        {
            values.add(salarySGD);
        }

		if(userHasRole(user, "ROLE_ADMIN") || userHasRole(user, "ROLE_SUB_ADMIN") || userHasRole(user, "ROLE_SALES_TW"))
        {
            values.add(salaryTWD);
        }
	}

	private String createUpdateSalaryStatement(User user)
	{
		return (userHasRole(user, "ROLE_ADMIN") || userHasRole(user, "ROLE_SUB_ADMIN") || userHasRole(user, "ROLE_SALES_HK") ? "salary_hkd = ?, " : "")
			   + (userHasRole(user, "ROLE_ADMIN") || userHasRole(user, "ROLE_SUB_ADMIN") || userHasRole(user, "ROLE_SALES_SG") ? "salary_sgd = ?, " : "")
			   + (userHasRole(user, "ROLE_ADMIN") || userHasRole(user, "ROLE_SUB_ADMIN") || userHasRole(user, "ROLE_SALES_TW") ? "salary_twd = ?, " : "");
	}

	private boolean userHasRole(User user, String role)
	{
		return user.getAuthorities().toString().contains(role);
	}

	@Override
	public void editCaregiverCv(Caregiver candidate) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String sql = "update candidate_profile set "
				+ "full_name = ?, tag = ?, tagged_to = ?, contracted_to = ?, gender = ?, "
				+ "educational_level = ?, exp = ?, availability = ?, languages = ?,"
				+ createUpdateSalaryStatement(user)
				+ "marital_status=?, age = ?, dob=?, "
				+ "siblings=?, religion=?, food_choice=?, nationality=?, country_of_birth=?, "
				+ "has_children=?, height=?, weight=?, motivation = ?, mobile = ?, "
				+ "skype = ?, remarks=?, last_modified = ?, first_name = ?, last_name = ?, "
				+ "tag_status=?, tagged_date=?, tag_id=?, about=?, exp=?, "
				+ "specialties=?, certified_cpr=?, hobbies=?, status=?, date_of_placement=?, "
				+ "numbers_of_placement=?, marked_as_ready_time=?, email = ?, video_url=? "				//44
				+ "where user_id = ?";
		try{
			ArrayList values = new ArrayList();
			values.add(candidate.getFullName());
			values.add(candidate.getTag());
			values.add(candidate.getTaggedTo());
			values.add(candidate.getContractedTo());
			values.add(candidate.getGender());		//5
			values.add(candidate.getEducationalLevel());
			values.add(candidate.getExp());
			values.add(candidate.getAvailability());
			values.add(candidate.getLanguages());
			addSalaryParameters(user, values,
								candidate.getSalaryHKD(), candidate.getSalarySGD(), candidate.getSalaryTWD());
			values.add(candidate.getMaritalStatus());
			values.add(candidate.getAge());
			values.add(candidate.getDateOfBirth());	//15
			values.add(candidate.getSiblings());
			values.add(candidate.getReligion());
			values.add(candidate.getFoodChoice());
			values.add(candidate.getNationality());
			values.add(candidate.getCountryOfBirth());	//20
			values.add(candidate.getHasChildren());
			values.add(candidate.getHeight());
			values.add(candidate.getWeight());
			values.add(candidate.getMotivation());
			values.add(candidate.getMobile());		//25
			values.add(candidate.getSkype());
			values.add(candidate.getRemarks());
			values.add(candidate.getLastModified());
			values.add(candidate.getFirstName());
			values.add(candidate.getLastName());		//30
			values.add(candidate.getTagStatus());
			values.add(candidate.getTaggedDate());
			values.add(candidate.getTagId());
			values.add(candidate.getAbout());
			values.add(candidate.getExp());			//35
			values.add(candidate.getSpecialities());
			values.add(candidate.getCertifiedCpr());
			values.add(candidate.getHobbies());
			values.add(candidate.getStatus());
			values.add(candidate.getDateOfPlacement());  //40
			values.add(candidate.getNumbersOfPlacement());
			values.add(candidate.getMarkedAsRedayTime());
			values.add(candidate.getEmail());
			values.add(candidate.getVideoURL());	//44
			values.add(candidate.getUserId());

			logger.info("editCaregiverCv sql:"+sql);
			logger.info("values:"+values);
			logger.info("user:"+user+" auth:"+user.getAuthorities());

			jdbcTemplate.update(sql, values.toArray());
		}catch(DataAccessException e ){
			logger.error("Data Access Exception editing candidate_profile", e);
		}
	}

	@Override
	public void deleteCandidatesByIds(String ids) {
		String sql = "delete from candidate_profile where user_id in (" + ids + ") ;";
		
		try{
			jdbcTemplate.execute(sql);
		}catch(DataAccessException e){
			logger.error("Data Acces Exception deleting candidate_profile according to the key");
		}
	}

	@Override
	public void editCaregiverInfo(Bio bio) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String sql = "update candidate_profile set work_in_sg = ?, work_in_hk = ?, work_in_tw = ?, " +
					 createUpdateSalaryStatement(user) +
					 "numbers_of_placement = ? where user_id = ? ;";
		try{
			ArrayList values = new ArrayList();

			values.add(bio.getWorkInSG());
			values.add(bio.getWorkInHK());
			values.add(bio.getWorkInTW());
			addSalaryParameters(user, values, bio.getSalaryInHKD(), bio.getSalaryInSGD(), bio.getSalaryInTWD());
			values.add(bio.getNumberOfPlacements());
			values.add(bio.getCaregiverId());

			logger.info("editCaregiverInfo sql:"+sql);
			logger.info("values:"+values);
			logger.info("user:"+user+" auth:"+user.getAuthorities());

			jdbcTemplate.update(sql, values.toArray());
		}catch(DataAccessException e){
			logger.error("Data Access Exception updating candidate's work location of candidate_profile", e);
		}
	}

	@Override
	public void deleteComment(String id) {
		String sql = "DELETE FROM comments_history where id='"+id+"'";
		
		try{
			jdbcTemplate.update(sql);
		}catch(DataAccessException e){
			logger.error("Data Access Exception deleting a comment from comments_history", e);
		}
	}

	@Override
	public void editCvBio(Bio bio, Integer userId) {
		String sql = "update bio set basic_info = ?, education_experience = ?, experience_details = ?, cpr_or_first_aid = ?, "
				+ "nursing_experience = ?, hobby = ?, last_modify = ? where candidateid = ?";
		logger.info("editCvBio, sql:"+sql);
		logger.info("bio:"+bio);
		logger.info("userId:"+userId);
		try{
			jdbcTemplate.update(sql, new Object[]{
				bio.getCandidateBasicInformation(),
				bio.getEducationAndExperience(),
				bio.getExperienceDetails(),
				bio.getTrainedToCprOrFA(),
				bio.getNursingExperience(),
				bio.getHobby(),
				new Date(),
				userId
			});
		}catch(DataAccessException e){
			logger.error("Data Access Exception updating a candidate's bio in the database", e);
		}
	}

	@Override
	public int checkExistCandidate(String email) {
		String sql = "SELECT COUNT(*) FROM candidate_profile WHERE email='"+email+"'";
		try {
			return jdbcTemplate.queryForObject(sql, Integer.class);
		} catch (DataAccessException e) {
			logger.error("Data Access Exception calculate the number of candidates in the database", e);
		}
		
		return 0;
	}

	@Override
	public int checkExistCandidate(String email, String userEmail) {
		String sql = "SELECT COUNT(*) FROM candidate_profile WHERE email='"+email+"' and email !='"+userEmail+"'";
		try {
			return jdbcTemplate.queryForObject(sql, Integer.class);
		} catch (DataAccessException e) {
			logger.error("Data Access Exception calculate the number of candidates in the database", e);
		}
		
		return 0;
	}

	@Override
	public String getEmailByCandidateId(String candidateId) {
		String email = null;
		String sql = "SELECT email FROM candidate_profile WHERE user_id = " + candidateId;
		try {
			email = jdbcTemplate.queryForObject(sql, String.class);
		} catch (DataAccessException e) {
			logger.error("Data Access Exception calculate the number of candidates in the database", e);
		}
		return email;
	}

}