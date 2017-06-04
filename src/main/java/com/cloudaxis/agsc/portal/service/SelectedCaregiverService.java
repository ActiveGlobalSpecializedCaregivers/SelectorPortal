package com.cloudaxis.agsc.portal.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.StringUtils;
import com.cloudaxis.agsc.portal.constants.ImageThumbnailConstants;
import com.cloudaxis.agsc.portal.dao.SelectedCaregiverDAO;
import com.cloudaxis.agsc.portal.dao.UserProfileDAO;
import com.cloudaxis.agsc.portal.helpers.ImageUtils;
import com.cloudaxis.agsc.portal.helpers.StringUtil;
import com.cloudaxis.agsc.portal.model.Bio;
import com.cloudaxis.agsc.portal.model.CandidateStatusAmount;
import com.cloudaxis.agsc.portal.model.Caregiver;
import com.cloudaxis.agsc.portal.model.Comment;
import com.cloudaxis.agsc.portal.model.Profile;
import com.cloudaxis.agsc.portal.model.SearchParamOfCandidate;
import com.cloudaxis.agsc.portal.model.SendCV;
import com.cloudaxis.agsc.portal.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;

public class SelectedCaregiverService {

	protected Logger		logger	= Logger.getLogger(SelectedCaregiverService.class);

	private String space = " ";
	private String separator = ", ";
	private String period = ". ";
	
	@Autowired
	Environment				env;

	@Autowired
	SelectedCaregiverDAO	selectedCaregiverDAO;

	@Autowired
	UserProfileDAO			userProfileDAO;

	@Autowired
	private EmailService	emailService;
	
	@Autowired
	private FileService 	fileService;

	@Autowired
	private UserService     userService;
	
	public String getCaregivers() {

		List<Caregiver> list = getCaregiverList();

		// create a JSON string
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;

		try {
			jsonString = mapper.writeValueAsString(list);
		}
		catch (JsonProcessingException e) {
			logger.error("JSON data could not be created for the Region billing", e);
		}

		return jsonString;

	}
	
	public List<Caregiver> getCaregiverList() {
		return selectedCaregiverDAO.listCaregivers();
	}
	
	public List<Caregiver> caregiverList() {
		return selectedCaregiverDAO.list();
	}

	public Caregiver getCaregiver(String userId) {
		Caregiver caregiver = selectedCaregiverDAO.getCaregiver(userId);
		if(!StringUtils.isNullOrEmpty(caregiver.getMedicalCertValidator())){
			User validator = userService.getUserById(Integer.parseInt(caregiver.getMedicalCertValidator()));
			caregiver.setMedicalCertValidator(validator.getFirstName() + " " + validator.getLastName());
		}
		return caregiver;
	}

	public void save(Caregiver caregiver) {
		selectedCaregiverDAO.save(caregiver);
	}

	public void editStatus(Caregiver caregiver, User user, String changeStatus) {
		if("7".equals(changeStatus)){
			caregiver.setTag(0);
			caregiver.setMarkedAsRedayTime(new Date());
		}else{
			caregiver.setMarkedAsRedayTime(null);
		}
		if("9".equals(changeStatus)){
			caregiver.setDateOfPlacement(new Date());
			int numbersOfPlacment = Integer.parseInt(caregiver.getNumbersOfPlacement()) + 1;
			caregiver.setNumbersOfPlacement(String.valueOf(numbersOfPlacment));
			String tagStatus = "<a href='#' class='btn btn-success btn-xs'>Contracted by " + user.getFirstName() + " " + user.getLastName()  + " </a>";
			caregiver.setTagStatus(tagStatus);
			caregiver.setTag(2);
		}else if("8".equals(changeStatus)){
			String tagStatus = "<a href='#' class='btn btn-success btn-xs'>Tagged by " + user.getFirstName() + " " + user.getLastName()  + " </a>";
			caregiver.setTagStatus(tagStatus);
			caregiver.setTag(1);
		}else if("5".equals(changeStatus)){
			caregiver.setTag(3);
		}else{
			caregiver.setTagStatus(null);
		}
		
		selectedCaregiverDAO.editStatus(caregiver, user, changeStatus);
		selectedCaregiverDAO.addHistory(caregiver.getUserId().toString(), user.getUserId().toString(), "status", String.valueOf(caregiver.getStatus()), changeStatus);
	}

	public List<Comment> getCommentsByCaregiverId(Integer caregiverId) {
		return selectedCaregiverDAO.getCommentsByCaregiverId(caregiverId);
	}

	public void postNewComment(String comment, User user, String caregiverId) {

		Comment newComment = new Comment(user.getUserId(), Integer.valueOf(caregiverId), comment, new Date(),
				new Date());
		selectedCaregiverDAO.postNewComment(newComment);

	}

	public List<Comment> updateCommentsWithUserName(List<Comment> comments) {
		return selectedCaregiverDAO.updateCommentsWithUserNameAndInitials(comments);
	}

	public Profile createNewProfile(MultipartHttpServletRequest request, HttpServletResponse response,
			Profile profile) {

		try {

			// List of files that will be uploaded to S3
			List<MultipartFile> filesToBeUploaded = new ArrayList<MultipartFile>();

			String path = request.getSession().getServletContext().getRealPath("/") + "resources\\";
			User user = new User();
			user.setUserId(1);

			String birth_date = request.getParameter("birth_date");
			if (!StringUtil.isBlank(birth_date)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date dob = sdf.parse(birth_date);
				int age = new Date().getYear() - dob.getYear();
				profile.setAge(String.valueOf(age));
				profile.setDob(dob);
			}

			profile.setWork_in_tw("NO");
			profile.setWork_in_sg("NO");
			profile.setWork_in_hk("NO");
			profile.setMedicalCertVerified("No");
			profile.setRegistered_concorde("No");
			profile.setPre_deployment("No");

			if (profile.getApplyLocations().contains("Singapore")) {
				profile.setWork_in_sg("YES");
			}
			if (profile.getApplyLocations().contains("Hong Kong")) {
				profile.setWork_in_hk("YES");
			}

			/** Resume **/
			if (StringUtil.isBlank(profile.getResume())) {
				MultipartFile resume = request.getFile("resume1");
				String filename = resume.getOriginalFilename();
				/*if (!StringUtil.isBlank(filename)) {
					profile.setResume(filename);
				}else{
					profile.setResume("resume.pdf");
				}*/
				/*String suffixName = filename.substring(filename.lastIndexOf(".")+1, filename.length());
				if("pdf".equals(suffixName) || "PDF".equals(suffixName)){
					profile.setResume("resume.pdf");
				}else{
					profile.setResume(filename);
				}*/
				if(resume.isEmpty()){
					profile.setResume("resume.pdf");
				}else{
					profile.setResume(filename);
				}
				
				filesToBeUploaded.add(resume);
			}
			else {

				// create .pdf file
				String filePath = path + "userfiles\\" + "id" + "\\resume1";

				File file = new File(filePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				filePath = filePath + "\\resume.pdf";
				/*
				 * The first parameter is the page size. The next few parameters
				 * are left margin, right, top and bottom margins
				 */
				Document document = new Document(PageSize.A4, 50, 50, 50, 50);
				document.open();

				Anchor anchor = new Anchor(profile.getResume());
				document.add(anchor);

				document.close();

				profile.setResume("resume.pdf");
			}
			/** Passport Photo **/
			MultipartFile passport_file = request.getFile("passport");
			if (!passport_file.isEmpty()) {
				String passportFilename = passport_file.getOriginalFilename();
				if (!StringUtil.isBlank(passportFilename)) {
					filesToBeUploaded.add(passport_file);
					profile.setPhoto_passport(passportFilename);
				}
			}

			/** Degree Photos **/
			List<MultipartFile> degree_files = request.getFiles("degrees");
			if (degree_files.size() > 0) {
				String str = "";
				boolean first  = true;
				for (MultipartFile mfile : degree_files) {
					String filename = mfile.getOriginalFilename();
					if (!StringUtil.isBlank(filename)) {
						filesToBeUploaded.add(mfile);
						if(first){							
							str = filename;
							first = false;
						}else {
							str = str + "," + filename;
						}
					}
				}
				profile.setPhoto_degrees(str);
			}

			/** Other Files **/
			List<MultipartFile> other_files = request.getFiles("other_file");
			if (other_files.size() > 0) {
				String str = "";
				boolean first  = true;
				for (MultipartFile mfile : other_files) {
					String filename = mfile.getOriginalFilename();
					if (!StringUtil.isBlank(filename)) {
						filesToBeUploaded.add(mfile);
						if(first){							
							str = filename;
							first = false;
						} else {
							str = str + "," + filename;
						}
					}
				}
				profile.setOther_files(str);
			}
			
			/** Personal Photo **/
			MultipartFile photo_file = request.getFile("my_photo");
			if (!photo_file.isEmpty()) {
				String filename = photo_file.getOriginalFilename();
				if (!StringUtil.isBlank(filename)) {
					filesToBeUploaded.add(photo_file);
					
					String type = photo_file.getContentType();
					if(!type.contains("jpg") || !type.contains("png") || !type.contains("jpeg")){
						String otherFile = profile.getOther_files();
						if(!StringUtil.isBlank(otherFile)){
							profile.setOther_files(otherFile.concat(","+filename));
						}else{
							profile.setOther_files(filename);
						}
					}else{
						profile.setPhoto(filename);
					}
				}
			}
			
			profile.setDatecreated(new Date());
			profile.setFull_name(profile.getFirst_name() + " " + profile.getLast_name());
			profile.setDate_applied(new Date());

			// send to the database and return random USER_ID
			long caregiverId = userProfileDAO.addProfile(profile);
			profile.setUser_id(String.valueOf(caregiverId));

			// save bio
			saveBio(String.valueOf(caregiverId));
			
			// Upload files to S3
			fileService.uploadFiles(filesToBeUploaded, String.valueOf(caregiverId), request);

			// Send acknowledgement email to Applicant
			emailService.sendAcknowledgement(profile, user);
		}
		catch (MessagingException messagingException) {
			logger.error("Unable to send acknowledgement email: " + messagingException.getMessage());

		}
		catch (IOException ioException) {
			logger.error("Unable to upload files to S3 server: " + ioException.getMessage());
		}
		catch (ParseException parseException) {
			logger.error("Unable to parse Date of birth: " + parseException.getMessage());
		}
		catch (DocumentException documentException) {
			logger.error(documentException.getMessage());
		}

		return profile;
	}


	public List<Caregiver> searchByProfileFilter(List<String> criteria) {
		return selectedCaregiverDAO.searchByProfileFilter(criteria);
	}

	public void saveSearch(String searchTitle, User user, String shareWithOtherUsers, String searchList) {
		selectedCaregiverDAO.saveSearch(searchTitle, user, shareWithOtherUsers, searchList);
	}

	public void uploadFile(MultipartFile file, Caregiver caregiver, String type) throws IOException {
		AmazonS3 s3client = new AmazonS3Client();
		String region = env.getProperty("aws.region");
		String[] regionSuffix = region.split("-");
		String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];

		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);
		
		String key = "";
		String fileName = file.getOriginalFilename();
		key = caregiver.getUserId() + "/" + type + "/" + fileName;
		
		ImageInputStream imageInputStream = null;
		try {
			 File fileOfImage = new File(fileName);
	         file.transferTo(fileOfImage);
	            
			if("resume".equals(type) && !StringUtil.isBlank(caregiver.getResume())){		//delete original's resume
				String originalPath = caregiver.getUserId()+"/" +type+"/" +caregiver.getResume();
				
				ListObjectsRequest documentsList = new ListObjectsRequest()
						.withBucketName(bucketName)
						.withPrefix(caregiver.getUserId()+"/" +type+"/");
				ObjectListing objects = s3client.listObjects(documentsList);
				for(S3ObjectSummary s3ObjectSummary: objects.getObjectSummaries()){
					if(s3ObjectSummary.getKey().contains(caregiver.getResume())){
						CopyObjectRequest copyObjRequest = new CopyObjectRequest(
								bucketName,originalPath,bucketName,caregiver.getUserId()+"/other_file/"+caregiver.getResume());
						
						s3client.copyObject(copyObjRequest);
						s3client.deleteObject(new DeleteObjectRequest(bucketName, originalPath));
					}
				}
			}else if("my_photo".equals(type) && !StringUtil.isBlank(caregiver.getPhoto())){
				String originalPath = caregiver.getUserId()+"/" +type+"/" +caregiver.getPhoto();
				
				ListObjectsRequest documentsList = new ListObjectsRequest()
						.withBucketName(bucketName)
						.withPrefix(caregiver.getUserId()+"/" +type+"/");
				ObjectListing objects = s3client.listObjects(documentsList);
				for(S3ObjectSummary s3ObjectSummary: objects.getObjectSummaries()){
					if(s3ObjectSummary.getKey().contains(caregiver.getPhoto())){
						CopyObjectRequest copyObjRequest = new CopyObjectRequest(
								bucketName,originalPath,bucketName,caregiver.getUserId()+"/other_file/"+caregiver.getPhoto());
						
						s3client.copyObject(copyObjRequest);
						s3client.deleteObject(new DeleteObjectRequest(bucketName, originalPath));
					}
				}
			}
			
	        /*if("resume".equals(type)){
		         if("resume.pdf".equals(fileName) || "resume.PDF".equals(fileName)){		//delete original's resume
					String originalPath = caregiver.getUserId().concat("/resume/resume.pdf");
					s3client.deleteObject(new DeleteObjectRequest(bucketName, originalPath));
				}
	        }*/
	        
	        //upload file
            logger.info("uploading file "+fileOfImage+" to bucket:"+bucketName+" with key:"+key);
			PutObjectRequest req = new PutObjectRequest(bucketName, key, fileOfImage);
			s3client.putObject(req);
			
			// thumbnail image
            boolean isImage = false;
            imageInputStream = ImageIO.createImageInputStream(fileOfImage);
            Iterator<ImageReader> iter = ImageIO.getImageReaders(imageInputStream);
            if (iter.hasNext()) {
                isImage = true;
            }
            if(isImage){
                BufferedImage originalBufferedImage = ImageIO.read(imageInputStream);
                BufferedImage resizedImage = new ImageUtils().createThumbnailImage(originalBufferedImage);

                File newFile = new File(fileName + ImageThumbnailConstants.FILE_SUFFIX);
                ImageIO.write(resizedImage, "jpg", newFile);
                String image_thumbnail_name = key + ImageThumbnailConstants.FILE_SUFFIX;
                PutObjectRequest req_img_thumbnail = new PutObjectRequest(bucketName, image_thumbnail_name, newFile);
                s3client.putObject(req_img_thumbnail);
            }
            // TODO: verify!!!!
            fileOfImage.delete();
		}
		catch (AmazonServiceException ase) {
			logger.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
		}
		catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			logger.error("Error Message: " + ace.getMessage());
		}
	}

	public void update(Caregiver caregiver) {
		selectedCaregiverDAO.update(caregiver);
	}


	public void changeTheStatisOfRegisteredConcorde(String userId, String caregiverId, String status) {
		selectedCaregiverDAO.changeTheStatisOfRegisteredConcorde(userId, caregiverId, status);
		String statusFrom = getStatusFrom(status);
		selectedCaregiverDAO.addHistory(caregiverId, userId, "Registered with Concorde", statusFrom, status);
	}

	private String getStatusFrom(String status) {
		if("YES".equals(status)){
			return "NO";
		}else{
			return "YES";
		}
	}

	public void changeTheStatusOfAdvancePlacementScheme(String userId, String caregiverId, String status) {
		selectedCaregiverDAO.changeTheStatusOfAdvancePlacementScheme(userId, caregiverId, status);
		String statusFrom = getStatusFrom(status);
		selectedCaregiverDAO.addHistory(caregiverId, userId, "Advance Placement Scheme", statusFrom, status);
	}

	public void changeTheStatusOfMedicalCertVerified(String validatorId, String caregiverId, String status) {
		String statusFrom = getStatusFrom(status);
		selectedCaregiverDAO.addHistory(caregiverId, validatorId, "Medical Cert Verified", statusFrom, status);
		if("NO".equals(status)){
			validatorId = null;
		}
		selectedCaregiverDAO.changeTheStatusOfMedicalCertVerified(validatorId, caregiverId, status);
	}
	
	public void saveBio(String caregiverId){
		Bio bio = combineBioByCaregiverId(caregiverId);
		selectedCaregiverDAO.saveBio(bio);
	}

	public Bio combineBioByCaregiverId(String caregiverId) {
		Caregiver caregiver = selectedCaregiverDAO.getCaregiver(caregiverId);
		Bio bio = new Bio();
		bio.setCaregiverId(Integer.parseInt(caregiverId));
		bio.setCandidateBasicInformation(getBasicInfo(caregiver));
		bio.setEducationAndExperience(getEducationAndExperience(caregiver));
		bio.setExperienceDetails(getExperienceDetails(caregiver.getCaregiverBeforeExp(), caregiver.getGender()));
		bio.setTrainedToCprOrFA(caregiver.getCertifiedCpr());
		bio.setNursingExperience(caregiver.getSpecialities());
		bio.setHobby(getHobby(caregiver.getHobbies()));
		bio.setSalaryInHKD(caregiver.getSalaryHKD());
		bio.setSalaryInSGD(caregiver.getSalarySGD());
		bio.setSalaryInTWD(caregiver.getSalaryTWD());
		return bio;
	}

	private String getExperienceDetails(String caregiverBeforeExp, String gender) {
		if(!StringUtils.isNullOrEmpty(caregiverBeforeExp)){
			String details = getExperienceTitle(gender);
			if(caregiverBeforeExp.contains("*")){
				details += caregiverBeforeExp;
			}else{
				details += "*" + caregiverBeforeExp;
			}
			return details;
		}else{
			return null;
		}
	}

	public Bio getBioByCaregiverId(Caregiver caregiver) {
		Bio bio = selectedCaregiverDAO.getBioByCaregiverId(Integer.parseInt(caregiver.getUserId()));

		if(bio != null){
			
			if(bio.getValidatorId() != null){
				User evaluator = userService.getUserById(bio.getValidatorId());
				bio.setValidator(evaluator.getFirstName() + " " + evaluator.getLastName());
			}
			
			String experienceDetails = bio.getExperienceDetails();
			if(!StringUtils.isNullOrEmpty(experienceDetails)){
				List<String> experienceList = new ArrayList<String>();
				
				experienceDetails = experienceDetails.replaceAll("\n|\r|\t", "");
				String[] detailsArr = experienceDetails.split("\\*");
				
				// experience title
				bio.setExperienceTitle(detailsArr[0]);
				
				// experience details
				for(int i= 1 ; i < detailsArr.length; i++){
					if(!StringUtils.isNullOrEmpty(detailsArr[i])){
						experienceList.add(detailsArr[i]);	
					}
				}
				bio.setExperienceList(experienceList);
				bio.setExperienceDetails(getExperienceInfo(bio.getExperienceDetails(), bio.getExperienceTitle()));
			}
			if(!StringUtils.isNullOrEmpty(bio.getCandidateBasicInformation())){
				bio.setCandidateBasicInformation(bio.getCandidateBasicInformation().replace("\r\n", ""));
			}
			if(!StringUtils.isNullOrEmpty(bio.getEducationAndExperience())){
				bio.setEducationAndExperience(bio.getEducationAndExperience().replace("\r\n", ""));
			}
			bio.setNumberOfPlacements(caregiver.getNumbersOfPlacement());
			bio.setWorkInSG(caregiver.getWorkInSG());
			bio.setWorkInHK(caregiver.getWorkInHK());
			bio.setWorkInTW(caregiver.getWorkInTW());
			bio.setSalaryInSGD(caregiver.getSalarySGD());
			bio.setSalaryInHKD(caregiver.getSalaryHKD());
			bio.setSalaryInTWD(caregiver.getSalaryTWD());
		}
		
		return bio;
	}

	private String getExperienceInfo(String experienceDetails, String experienceTitle) {
		String info ="";
		experienceDetails = experienceDetails.substring(experienceTitle.length());
		String[] experienceArr = experienceDetails.split("\\*");
		for(String str : experienceArr){
			if(!StringUtils.isNullOrEmpty(str.replaceAll("\r|\n|\t", ""))){
				info += "*" + str;
			}
		}
		return info;
	}

	private String getBasicInfo(Caregiver candidate) {
		StringBuffer basicInfo = new StringBuffer();
		basicInfo.append(candidate.getFullName()).append(space)
				.append("is").append(space).append(candidate.getAge()).append(space)
				.append("years old, and is a").append(space).append(getEduactionLevel(candidate.getEducationalLevel())).append(space)
				.append("from").append(space).append(candidate.getCountryOfBirth()).append(period);
		
		basicInfo.append(getPronun(candidate.getGender(), false)).append(" is ").append(candidate.getMaritalStatus().toLowerCase())
				 .append(" and ").append(getDiet(candidate.getFoodChoice())).append(period);
		
		basicInfo.append(getPronun(candidate.getGender(), false)).append(space).append("can speak").append(space).append(getLanguage(candidate.getLanguages())).append(period);
		
		return basicInfo.toString();
	}

	private String getEduactionLevel(String educationalLevel) {
		String level = "";
		if("Caregiver Certificate".equals(educationalLevel)){
			level = "Nursing Aid";
		}else if("Nurse Assistant Diploma".equals(educationalLevel)){
			level = "Auxiliary Nurse";
		}else if("Nurse Diploma".equals(educationalLevel)){
			level = "Diploma Nurse";
		}else if("Bachelor of Sciences in Nursing or higher".equals(educationalLevel)){
			level = "BSc Nurse";
		}
		return level;
	}

	private String getDiet(String foodChoice) {
		String diet = "";
		if("No restrictions".equals(foodChoice)){
			diet = "has no dietary restrictions";
		}else if("Vegetarian".equals(foodChoice)){
			diet = "is a vegetarian";
		}else if("Halal".equals(foodChoice)){
			diet = "eats only Halal food";
		}
		return diet;
	}
	
	private String getLanguage(String languages) {
		String languageTotal = "";
		String[] languageArr = languages.split(",");
		languageTotal = languageArr[0];
		for(int i = 1 ; i < languageArr.length; i ++){
			if(i == languageArr.length - 1){
				languageTotal += " and " + languageArr[i];
				break;
			}else{
				languageTotal += separator + languageArr[i];
			}
		}
		return languageTotal;
	}
	
	private String getPronun(String gender, boolean lowercase) {
		String pronoun = "He";
		if("Female".equals(gender)){
			pronoun = "She";
		}
		if(lowercase){
			pronoun = pronoun.toLowerCase();
		}
		return pronoun;
	}
	
	private String getEducationAndExperience(Caregiver candidate) {
		
		StringBuffer educationAndExperience = new StringBuffer();
		
		educationAndExperience.append(candidate.getFirstName()).append(space).append("has successfully completed her").append(space)
			.append(getTypeOfEducation(candidate.getEducationalLevel())).append(space).append("education at").append(space).append(candidate.getEducation()).append(separator);
		
		educationAndExperience.append(candidate.getCountryOfBirth()).append(space).append("in").append(space)
							  .append(candidate.getYearGraduation()).append(period);
		
		educationAndExperience.append("Overall ").append(getPronun(candidate.getGender(), true)).append(" has ")
							  .append(getWorkingYear(candidate.getExp())).append(space)
							  .append("of relevant professional experience").append(period);
		
		return educationAndExperience.toString();
	}

	private String getTypeOfEducation(String educationalLevel) {
		String type = "Nursing";
		if("Caregiver Certificate".equals(educationalLevel)){
			type = "Nursing aid";
		}
		return type;
	}
	
	private String getWorkingYear(String exp) {
		String workingYear = exp + space;
		if(exp.contains(".")){
			if(Float.parseFloat(exp.trim()) > 1){
				workingYear += "years";
			}else{
				workingYear += "year";
			}
		}else{
			if(Integer.parseInt(exp.trim()) > 1){
				workingYear += "years";
			}else{
				workingYear += "year";
			}
		}
		return workingYear;
	}
	
	private String getExperienceTitle(String gender) {
		String title = getGoodsPronun(gender) + space + "experience includes:";
		return title;
	}
	
	private String getGoodsPronun(String gender) {
		if("Female".equals(gender)){
			return "Her";
		}else{
			return "His";
		}
	}
	
	private String getHobby(String hobbies) {
		StringBuffer hobby = new StringBuffer();
		hobby.append("I like").append(space).append(hobbies).append(period);
		return hobby.toString();
	}

	public void editBioInfoByCaregiverId(Bio bio) {
		String experienceDetails = bio.getExperienceDetails();
		if(!StringUtils.isNullOrEmpty(experienceDetails)){
			bio.setExperienceDetails(experienceDetails);
		}
		selectedCaregiverDAO.editBioInfoByCaregiverId(bio);
		selectedCaregiverDAO.editCaregiverInfo(bio);
	}
	
	public void saveSendCV(SendCV cv, String sData) {
		selectedCaregiverDAO.saveSendCV(cv, sData);
	}

	public List<SendCV> getSendCv() {
		return selectedCaregiverDAO.getSendCv();
		
	}
	

	public CandidateStatusAmount getStatusAmount(String role) {
		CandidateStatusAmount candidateStatusAmount = new CandidateStatusAmount();
		String querySql = getRoleSql(role);
		candidateStatusAmount.setTotalAmount(selectedCaregiverDAO.getTotalAmount(querySql));
		candidateStatusAmount.setAvailableAmount(selectedCaregiverDAO.getAvailableAmount(querySql));
		candidateStatusAmount.setContractedAmount(selectedCaregiverDAO.getContractedAmount(querySql));
		candidateStatusAmount.setNewAmount(selectedCaregiverDAO.getNewAmount(querySql));
		candidateStatusAmount.setTaggedAmount(selectedCaregiverDAO.getTaggedAmount(querySql));
		candidateStatusAmount.setOnHoldAmount(selectedCaregiverDAO.getOnHoldAmount(querySql));
		return candidateStatusAmount;
	}

	public List<Caregiver> getCaregiverListIds(String ids) {
		return selectedCaregiverDAO.getCaregiverListIds(ids);
	}

	public void updateCaregiver(Caregiver caregiver) {
		selectedCaregiverDAO.updateCaregiver(caregiver);
	}

	public void updatePhoto(Caregiver caregiver) {
		selectedCaregiverDAO.updatePhoto(caregiver);
	}

	public Caregiver getCaregiverBio(String userId) {
		return selectedCaregiverDAO.getCaregiverBio(userId);
	}

	public List<Caregiver> getCandidatesByStatus(String status, String role) {
		String queryInfo = "where 1 = 1 " + getCandidateStatusSql(status) + getRoleSql(role);
		return selectedCaregiverDAO.getCandidatesByParams(queryInfo);
	}

	private String getRoleSql(String role) {
		String roleSql = "";
		if(role.contains("ROLE_HOSPITAL")){
			roleSql = " and pre_deployment = 'YES' ";
		}else if(role.contains("ROLE_SALES_SG")){
			roleSql = " and work_in_sg = 'Yes' ";
		}else if(role.contains("ROLE_SALES_HK")){
			roleSql = " and work_in_hk = 'Yes' ";
		}else if(role.contains("ROLE_SALES_TW")){
			roleSql = " and work_in_tw = 'Yes' ";
		}
		return roleSql;
	}

	public List<Caregiver> getCandidatesByParams(SearchParamOfCandidate searchParam, String status, String role) {
		StringBuilder queryInfo = new StringBuilder("where 1 = 1 " + getCandidateStatusSql(status));
		queryInfo.append(getRoleSql(role));
		if(!StringUtils.isNullOrEmpty(searchParam.getAvail())){
			queryInfo.append(" and availability in (" + getParamStr(searchParam.getAvail()) + ")");
		}
		if(!StringUtils.isNullOrEmpty(searchParam.getChildren())){
			if("No Children".equals(searchParam.getChildren())){
				queryInfo.append("and ( has_children = 'No Children' or has_children = 'no child' or has_children = '' ) ");
			}else if("Has Children".equals(searchParam.getChildren())){
				queryInfo.append("and has_children != 'No Children' and has_children != 'no child' and has_children != '' ");
			}
		}
		if(!StringUtils.isNullOrEmpty(searchParam.getFood())){
			queryInfo.append(" and food_choice in (" + getParamStr(searchParam.getFood()) + ")");
		}
		if(!StringUtils.isNullOrEmpty(searchParam.getGender())){
			if(searchParam.getGender().split(",").length == 1){
				queryInfo.append(" and gender in ( '" + searchParam.getGender() + "')");
			}
		}
		if(!StringUtils.isNullOrEmpty(searchParam.getLevel())){
			queryInfo.append(" and educational_level in (" + getParamStr(searchParam.getLevel()) + ")");
		}
		if(!StringUtils.isNullOrEmpty(searchParam.getMarital())){
			queryInfo.append(" and marital_status in (" + getParamStr(searchParam.getMarital()) + ")");
		}
		if(!StringUtils.isNullOrEmpty(searchParam.getReligion())){
			queryInfo.append(" and religion in (" + getParamStr(searchParam.getReligion()) + ")");
		}
		List<Caregiver> candidateList = selectedCaregiverDAO.getCandidatesByParams(queryInfo.toString());
		if(!StringUtils.isNullOrEmpty(searchParam.getMedical())){
			candidateList = getCandidateListByExperience(searchParam.getMedical(), candidateList);
		}
		if(!StringUtils.isNullOrEmpty(searchParam.getLanguage())){
			candidateList = getCandidateListByLanguage(searchParam.getLanguage(), candidateList);
		}
		if(!StringUtils.isNullOrEmpty(searchParam.getAge())){
			candidateList = getCandidateListByAge(searchParam.getAge(), candidateList);
		}
		if(!StringUtils.isNullOrEmpty(searchParam.getExp())){
			candidateList = getCandidateByExp(searchParam.getExp(), candidateList);
		}
		return candidateList;
	}

	private String getParamStr(String message) {
		String[] paramArr = message.split(",");
		String param = "'"+paramArr[0]+"'";
		for(int i = 1 ; i < paramArr.length; i++){
			param += "," + "'" + paramArr[i] + "'";
		}
		return param;
	}

	private String getCandidateStatusSql(String status){
		String sql = "";
		if("-1".equals(status)){
			sql = "and status = 7 and (DATEDIFF(marked_as_ready_time, now()) < 7  or DATEDIFF(date_ready_for_placement, now()) < 7)";
		}else if(!"0".equals(status)){
			sql = "and status = " + status;
		}else if("0".equals(status)){
			sql = "and status in (5, 7, 8, 9)";
		}
		return sql;
	}
	
	private List<Caregiver> getCandidateListByExperience(String medical, List<Caregiver> candidateList) {
		List<Caregiver> candidateListNew = new ArrayList<Caregiver>();
		String[] queryParams = medical.split(",");
		List<String> queryList = arrayToList(queryParams);
		for(Caregiver caregiver :  candidateList){
			String[] specialitesArr = caregiver.getSpecialities().split(",");
			List<String> specilitesList = arrayToList(specialitesArr);
			if(specilitesList.containsAll(queryList)){
				candidateListNew.add(caregiver);
			}
		}
		return candidateListNew;
	}
	
	
	
	private List<String> arrayToList(String[] stringArr) {
		List<String> stringList = Lists.newArrayList();
		for(String str : stringArr){
			stringList.add(str);
		}
		return stringList;
	}

	private List<Caregiver> getCandidateListByLanguage(String language, List<Caregiver> candidateList) {
		List<Caregiver> candidateListNew = new ArrayList<Caregiver>();
		String[] queryParams = language.split(",");
		List<String> queryList = arrayToList(queryParams);
		for(Caregiver caregiver :  candidateList){
			String[] specialitesArr = caregiver.getLanguages().split(",");
			List<String> specilitesList = arrayToList(specialitesArr);
			if(specilitesList.containsAll(queryList)){
				candidateListNew.add(caregiver);
			}
		}
		return candidateListNew;
	}
	
	private List<Caregiver> getCandidateListByAge(String age, List<Caregiver> candidateList) {
		List<Caregiver> candidateListNew = new ArrayList<Caregiver>();
		String[] queryParams = age.split(",");
		for(Caregiver caregiver :  candidateList){
			for(String param : queryParams){
				Integer ageOfCandidate = Integer.parseInt(caregiver.getAge());
				if("23-30".equals(param)){
					if(ageOfCandidate >= 23 && ageOfCandidate <= 30){
						candidateListNew.add(caregiver);
						break;	
					}
				}else if("31-40".equals(param)){
					if(ageOfCandidate >= 31 && ageOfCandidate <= 40){
						candidateListNew.add(caregiver);
						break;	
					}
				}else{
					if(ageOfCandidate > 40){
						candidateListNew.add(caregiver);
						break;	
					}	
				}
			}
		}
		return candidateListNew;
	}

	private List<Caregiver> getCandidateByExp(String exp, List<Caregiver> candidateList) {
		List<Caregiver> candidateListNew = new ArrayList<Caregiver>();
		String[] queryParams = exp.split(",");
		for(Caregiver caregiver :  candidateList){
			float expOfCandidate = Float.parseFloat(caregiver.getExp());
			for(String param : queryParams){
				if("<=5".equals(param)){
					if(expOfCandidate <= 5){
						candidateListNew.add(caregiver);
						break;
					}
				}else{
					if(expOfCandidate > 5){
						candidateListNew.add(caregiver);
						break;
					}
				}
			}
		}
		return candidateListNew;
	}

	public Integer saveCandidateOfDashboard(Caregiver candidate) {
		if(!StringUtils.isNullOrEmpty(candidate.getUserId())){
			selectedCaregiverDAO.editCaregiverOfDashboard(candidate);
			return Integer.parseInt(candidate.getUserId());
		}else{
			int id = selectedCaregiverDAO.saveCaregiverOfDashboard(candidate);
			return id;
		}
	}

	public void saveBioInfoByCaregiverIdOfDashboard(Bio bio, String gender, Integer candidateId) {
		String experience = bio.getEducationAndExperience();
		String title = getExperienceTitle(gender);
		if(experience.contains("*")){
			String[] experienceArr = experience.split("\\*");
			if(experienceArr[0].contains(title)){
				bio.setEducationAndExperience(experienceArr[0].replace(title, ""));
			}else{
				bio.setEducationAndExperience(experienceArr[0]);
			}
			String experienceDetails = getExperienceTitle(gender);
			for(int i = 1 ; i < experienceArr.length; i++){
				experienceDetails += "*" + experienceArr[i];
			}
			bio.setExperienceDetails(experienceDetails);
		}else{
			bio.setEducationAndExperience(experience);
		}
		if(bio.getCaregiverId() != null){
			selectedCaregiverDAO.editBioInfoByCaregiverId(bio);
		}else{
			bio.setCaregiverId(candidateId);
			selectedCaregiverDAO.saveBio(bio);
		}

	}

	public void editCaregiverCv(Caregiver caregiver) {
		selectedCaregiverDAO.editCaregiverCv(caregiver);
	}

	public void deleteCandidatesByIds(String ids) {
		ids = ids.substring(0, ids.length() -1); // remove the last separator
		selectedCaregiverDAO.deleteCandidatesByIds(ids);
	}

	public void deleteComment(String id) {
		selectedCaregiverDAO.deleteComment(id);
	}

	public void editCvBio(Bio bio, Integer userId) {
		if(!bio.getEducationAndExperience().contains("*")){
			bio.setExperienceDetails("");
		}else{
			String[] titleAndDetails = bio.getEducationAndExperience().split("\\*");
			bio.setEducationAndExperience(titleAndDetails[0]);
			String details = "";
			for(int i = 1 ; i < titleAndDetails.length; i ++){
				details += "*" + titleAndDetails[i];
			}
			bio.setExperienceDetails(details);
		}
		selectedCaregiverDAO.editCvBio(bio,userId);
	}

	public int checkExistCandidate(String email) {
		return selectedCaregiverDAO.checkExistCandidate(email);
	}

	public int checkExistCandidate(String email, String userEmail) {
		return selectedCaregiverDAO.checkExistCandidate(email,userEmail);
	}

	public boolean emailVerify(String email) {
		boolean isIdentity = true;
		int count = checkExistCandidate(email);
		if(count > 0){
			isIdentity = false;
		}
		return isIdentity;
	}

	public boolean emailVerify(String currentEmail, String candidateId) {
		boolean isIdentity = true;
		String email = selectedCaregiverDAO.getEmailByCandidateId(candidateId);
		if(!email.equals(currentEmail)){
			isIdentity = emailVerify(currentEmail);
		}
		return isIdentity;
	}

	/**
	 * Processes list of candidates, looking for Tagged candidates and resetting them back to ReadyForPlacement status
	 * if they are tagged for more then 7 days.
	 * @param candidateList the candidates to process
	 * @param includeUpdatedCaregiver true to exclude candidates from the resulting list, false just to update them
	 * @param user the user initiated this request
	 * @return the list of caregivers
	 */
	public List<Caregiver> processTaggedStatus(List<Caregiver> candidateList,
											   boolean includeUpdatedCaregiver,
											   User user)
	{
		ArrayList<Caregiver> resultList = new ArrayList<>(candidateList.size());
		for(Caregiver caregiver : candidateList){
			if(caregiver.getStatus() == 8){// Tagged
				logger.info("processing tagged caregiver, tag_date="+caregiver.getTaggedDate());
				if (caregiver.getTaggedDate() != null)
				{
					if (daysAfter(caregiver.getTaggedDate(), 7))
					{
						// update status to ReadyForPlacement (7)
						logger.info("setting status to 7");
						editStatus(caregiver, user, "7");
						if (includeUpdatedCaregiver)
						{
							resultList.add(caregiver);
						}
						continue;
					}
					else{
						caregiver.setNewCaregiverFlag("new");
					}
				}
			}
			resultList.add(caregiver);
		}
		return resultList;
	}

	/**
	 * Determines if passed specified number of days since that date.
	 * @param date the base date
	 * @param days the number of days
	 * @return true if now date > date + days
	 */
	private boolean daysAfter(Date date, int days)
	{
		boolean before = date.toInstant().plus(days, ChronoUnit.DAYS).isBefore(Instant.now());
		logger.info("daysAfter: date = "+date+" after?"+before);
		return before;
	}
}
