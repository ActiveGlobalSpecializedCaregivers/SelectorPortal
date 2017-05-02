
package com.cloudaxis.agsc.portal.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Caregiver implements Serializable{

	@JsonProperty("user_id")
	public String userId;

	@JsonProperty("app_id")
	public String appId;

	@JsonProperty("email")
	public String email;

	@JsonProperty("full_name")
	public String fullName;

	@JsonProperty("first_name")
	public String firstName;

	@JsonProperty("last_name")
	public String lastName;
	@JsonProperty("gender")
	public String gender;
	@JsonProperty("dob")
	public Date dateOfBirth;
	
	@JsonProperty("d_of_birth")
	public String d_of_birth;
	
	@JsonProperty("age")
	public String age;
	@JsonProperty("country_of_birth")
	public String countryOfBirth;
	@JsonProperty("has_children")
	public String hasChildren;
	@JsonProperty("siblings")
	public String siblings;
	@JsonProperty("languages")
	public String languages;
	@JsonProperty("nationality")
	public String nationality;
	@JsonProperty("educational_level")
	public String educationalLevel;
	@JsonProperty("certified_cpr")
	public String certifiedCpr;
	@JsonProperty("exp")
	public String exp;
	@JsonProperty("marital_status")
	public String maritalStatus;
	@JsonProperty("religion")
	public String religion;
	@JsonProperty("mobile")
	public String mobile;
	@JsonProperty("height")
	public String height;
	@JsonProperty("weight")
	public String weight;
	@JsonProperty("motivation")
	public String motivation;
	@JsonProperty("about")
	public String about;
	@JsonProperty("education")
	public String education;
	@JsonProperty("specialities")
	public String specialities;
	@JsonProperty("hobbies")
	public String hobbies;
	@JsonProperty("availability")
	public String availability;
	@JsonProperty("date_applied")
	public Date dateApplied;
	
	@JsonProperty("d_applied")
	public String d_applied;
	
	@JsonProperty("work_in_hk")
	public String workInHK;
	@JsonProperty("work_in_sg")
	public String workInSG;
	@JsonProperty("work_in_tw")
	public String workInTW;
	@JsonProperty("salary_hkd")
	public String salaryHKD;
	@JsonProperty("salary_sgd")
	public String salarySGD;
	@JsonProperty("salary_twd")
	public String salaryTWD;
	@JsonProperty("food_choice")
	public String foodChoice;
	@JsonProperty("photo")
	public String photo;
	@JsonProperty("skype")
	public String skype;
	@JsonProperty("resume")
	public String resume;
	@JsonProperty("tag")
	public int tag;
	@JsonProperty("tag_id")
	public int tagId;
	@JsonProperty("tag_status")
	public String tagStatus;
	@JsonProperty("tagged_date")
	public Date taggedDate;
	@JsonProperty("contracted_to")
	public String contractedTo;
	@JsonProperty("tagged_to")
	public String taggedTo;
	@JsonProperty("assigned")
	public String assigned;
	@JsonProperty("location")
	public String location;
	@JsonProperty("registration_number")
	public String registrationNumber;
	@JsonProperty("remarks")
	public String remarks;
	@JsonProperty("nearest_airport")
	public String nearestAirport;
	@JsonProperty("current_address")
	public String currentAddress;
	@JsonProperty("number_of_children")
	public int numberOfChildren;
	@JsonProperty("children_names")
	public String childrenNames;
	@JsonProperty("worked_in_sg")
	public String workedInSG;
	@JsonProperty("allergies")
	public String allergies;
	@JsonProperty("diagnosed_conditions")
	public String diagnosedConditions;
	@JsonProperty("video_url")
	public String videoURL;
	@JsonProperty("date_created")
	public Date dateCreated;
	
	// add some field [because not enough fields to the application for
	// registration]
	@JsonProperty("applicant_status")
	public String resumatorStatus; // ARE YOU INTERESTED IN THIS
									// JOB[YES/NO]

	@JsonProperty("applyLocations")
	public String applyLocations; // work in hk/work in sg

	@JsonProperty("children_name_age")
	public String childrenNameAge; // If you have children, please list
									// their names & ages

	@JsonProperty("hold_passport")
	public String holdPassport;

	@JsonProperty("year_graduation")
	public String yearGraduation;

	@JsonProperty("year_studies")
	public String yearStudies;

	@JsonProperty("caregiver_before_exp")
	public String caregiverBeforeExp;

	@JsonProperty("sg_fin")
	public String sgFin;

	@JsonProperty("current_job")
	public String currentJob;

	@JsonProperty("current_job_time")
	public String currentJobTime;

	@JsonProperty("time_of_sg")
	public String timeOfSg; // time for sg

	@JsonProperty("history_of_treatment")
	public String historyOfTreatment; // about the treatment/ medication

	@JsonProperty("interview_time")
	public String interviewTime;

	@JsonProperty("referrer")
	public String referrer; // referred by a friend

	// url(photo)
	@JsonProperty("photo_passport")
	public String photoPassport; // jsp:passport

	@JsonProperty("photo_degrees")
	public String photoDegrees; // jsp:degrees

	@JsonProperty("other_files")
	public String otherFiles; // jsp:other_file
	
	@JsonProperty("bioid")
	public String bioId;

	@JsonProperty("medical_cert_verified")
	public String medicalCertVerified;
	
	@JsonProperty("medical_cert_validator")
	public String medicalCertValidator;
	
	@JsonProperty("newFlag")
	public String newCaregiverFlag;
	
	public int status;
	public Date lastModified;
	public List<String> languageList;
	public List<String> diagnosedList;
	public List<String> specialitiesList;
	public String selectStatusVal;
	
	@JsonProperty("tesda_ncii")
	public String tesdaNcii;	
	
	@JsonProperty("registered_concorde")
	private String registeredConcorde;
	
	@JsonProperty("pre_deployment")
	private String preDeployment;
	
	@JsonProperty("dateOfPlacement")
	private Date dateOfPlacement;
	
	private Date dateOfAwaitingDocument;
	
	private Date dateOfSchedulingInterview;
	
	private Date dateOfInterviewScheduled;
	
	private Date dateOfShortlisted;
	
	private Date dateOfNotSelected;
	
	private Date dateOfShortlistedWithDiffAvail;
	
	private Date dateOfReadyForPlacement;
	
	private Date dateOfTagged;
	
	private Date dateOfOnHold;
	
	private Date dateOfBlacklisted;
	
	@JsonProperty("numbersOfPlacement")
	private String numbersOfPlacement;
	
	private Bio bio;
	
	private Date markedAsRedayTime;
	
	private Date markedAdvancedPlacementScheme;
	
	private Date markedRegisteredWithConcorde;
	
	private Date markedMedicalCertVerify;
	
	private String resumeUrl;
	
	@JsonProperty("tag_origin")
	private int tagOrigin;
	
	public List<String> getDiagnosedList() {
		return diagnosedList;
	}

	public void setDiagnosedList(List<String> diagnosedList) {
		this.diagnosedList = diagnosedList;
	}

	public List<String> getLanguageList() {
		return languageList;
	}
	
	public void setLanguageList(List<String> languageList) {
		this.languageList = languageList;
	}
	
	public List<String> getSpecialitiesList() {
		return specialitiesList;
	}

	public void setSpecialitiesList(List<String> specialitiesList) {
		this.specialitiesList = specialitiesList;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getCountryOfBirth() {
		return countryOfBirth;
	}

	public void setCountryOfBirth(String countryOfBirth) {
		this.countryOfBirth = countryOfBirth;
	}

	public String getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(String hasChildren) {
		this.hasChildren = hasChildren;
	}

	public String getSiblings() {
		return siblings;
	}

	public void setSiblings(String siblings) {
		this.siblings = siblings;
	}

	public String getLanguages() {
		return languages;
	}

	public void setLanguages(String languages) {
		this.languages = languages;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getEducationalLevel() {
		return educationalLevel;
	}

	public void setEducationalLevel(String educationalLevel) {
		this.educationalLevel = educationalLevel;
	}

	public String getCertifiedCpr() {
		return certifiedCpr;
	}

	public void setCertifiedCpr(String certifiedCpr) {
		this.certifiedCpr = certifiedCpr;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getMotivation() {
		return motivation;
	}

	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getSpecialities() {
		return specialities;
	}

	public void setSpecialities(String specialities) {
		this.specialities = specialities;
	}

	public String getHobbies() {
		return hobbies;
	}

	public void setHobbies(String hobbies) {
		this.hobbies = hobbies;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public Date getDateApplied() {
		return dateApplied;
	}

	public void setDateApplied(Date dateApplied) {
		this.dateApplied = dateApplied;
	}

	public String getWorkInHK() {
		return workInHK;
	}

	public void setWorkInHK(String workInHK) {
		this.workInHK = workInHK;
	}

	public String getWorkInSG() {
		return workInSG;
	}

	public void setWorkInSG(String workInSG) {
		this.workInSG = workInSG;
	}

	public String getWorkInTW() {
		return workInTW;
	}

	public void setWorkInTW(String workInTW) {
		this.workInTW = workInTW;
	}

	public String getSalaryHKD() {
		return salaryHKD;
	}

	public void setSalaryHKD(String salaryHKD) {
		this.salaryHKD = salaryHKD;
	}

	public String getSalarySGD() {
		return salarySGD;
	}

	public void setSalarySGD(String salarySGD) {
		this.salarySGD = salarySGD;
	}

	public String getSalaryTWD() {
		return salaryTWD;
	}

	public void setSalaryTWD(String salaryTWD) {
		this.salaryTWD = salaryTWD;
	}

	public String getFoodChoice() {
		return foodChoice;
	}

	public void setFoodChoice(String foodChoice) {
		this.foodChoice = foodChoice;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getSkype() {
		return skype;
	}

	public void setSkype(String skype) {
		this.skype = skype;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	public String getTagStatus() {
		return tagStatus;
	}

	public void setTagStatus(String tagStatus) {
		this.tagStatus = tagStatus;
	}

	public Date getTaggedDate() {
		return taggedDate;
	}

	public void setTaggedDate(Date taggedDate) {
		this.taggedDate = taggedDate;
	}

	public String getContractedTo() {
		return contractedTo;
	}

	public void setContractedTo(String contractedTo) {
		this.contractedTo = contractedTo;
	}

	public String getTaggedTo() {
		return taggedTo;
	}

	public void setTaggedTo(String taggedTo) {
		this.taggedTo = taggedTo;
	}

	public String getAssigned() {
		return assigned;
	}

	public void setAssigned(String assigned) {
		this.assigned = assigned;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getNearestAirport() {
		return nearestAirport;
	}

	public void setNearestAirport(String nearestAirport) {
		this.nearestAirport = nearestAirport;
	}

	public String getCurrentAddress() {
		return currentAddress;
	}

	public void setCurrentAddress(String currentAddress) {
		this.currentAddress = currentAddress;
	}

	public int getNumberOfChildren() {
		return numberOfChildren;
	}

	public void setNumberOfChildren(int numberOfChildren) {
		this.numberOfChildren = numberOfChildren;
	}

	public String getChildrenNames() {
		return childrenNames;
	}

	public void setChildrenNames(String childrenNames) {
		this.childrenNames = childrenNames;
	}

	public String getWorkedInSG() {
		return workedInSG;
	}

	public void setWorkedInSG(String workedInSG) {
		this.workedInSG = workedInSG;
	}

	public String getAllergies() {
		return allergies;
	}

	public void setAllergies(String allergies) {
		this.allergies = allergies;
	}

	public String getDiagnosedConditions() {
		return diagnosedConditions;
	}

	public void setDiagnosedConditions(String diagnosedConditions) {
		this.diagnosedConditions = diagnosedConditions;
	}

	public String getVideoURL() {
		return videoURL;
	}

	public void setVideoURL(String videoURL) {
		this.videoURL = videoURL;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getResumatorStatus() {
		return resumatorStatus;
	}

	public void setResumatorStatus(String resumatorStatus) {
		this.resumatorStatus = resumatorStatus;
	}

	public String getApplyLocations() {
		return applyLocations;
	}

	public void setApplyLocations(String applyLocations) {
		this.applyLocations = applyLocations;
	}

	public String getChildrenNameAge() {
		return childrenNameAge;
	}

	public void setChildrenNameAge(String childrenNameAge) {
		this.childrenNameAge = childrenNameAge;
	}

	public String getHoldPassport() {
		return holdPassport;
	}

	public void setHoldPassport(String holdPassport) {
		this.holdPassport = holdPassport;
	}

	public String getYearGraduation() {
		return yearGraduation;
	}

	public void setYearGraduation(String yearGraduation) {
		this.yearGraduation = yearGraduation;
	}

	public String getYearStudies() {
		return yearStudies;
	}

	public void setYearStudies(String yearStudies) {
		this.yearStudies = yearStudies;
	}

	public String getCaregiverBeforeExp() {
		return caregiverBeforeExp;
	}

	public void setCaregiverBeforeExp(String caregiverBeforeExp) {
		this.caregiverBeforeExp = caregiverBeforeExp;
	}

	public String getSgFin() {
		return sgFin;
	}

	public void setSgFin(String sgFin) {
		this.sgFin = sgFin;
	}

	public String getCurrentJob() {
		return currentJob;
	}

	public void setCurrentJob(String currentJob) {
		this.currentJob = currentJob;
	}

	public String getCurrentJobTime() {
		return currentJobTime;
	}

	public void setCurrentJobTime(String currentJobTime) {
		this.currentJobTime = currentJobTime;
	}

	public String getTimeOfSg() {
		return timeOfSg;
	}

	public void setTimeOfSg(String timeOfSg) {
		this.timeOfSg = timeOfSg;
	}

	public String getHistoryOfTreatment() {
		return historyOfTreatment;
	}

	public void setHistoryOfTreatment(String historyOfTreatment) {
		this.historyOfTreatment = historyOfTreatment;
	}

	public String getInterviewTime() {
		return interviewTime;
	}

	public void setInterviewTime(String interviewTime) {
		this.interviewTime = interviewTime;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public String getPhotoPassport() {
		return photoPassport;
	}

	public void setPhotoPassport(String photoPassport) {
		this.photoPassport = photoPassport;
	}

	public String getPhotoDegrees() {
		return photoDegrees;
	}

	public void setPhotoDegrees(String photoDegrees) {
		this.photoDegrees = photoDegrees;
	}

	public String getOtherFiles() {
		return otherFiles;
	}

	public void setOtherFiles(String otherFiles) {
		this.otherFiles = otherFiles;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	public Date getLastModified() {
		return lastModified;
	}
	
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSelectStatusVal() {
		return selectStatusVal;
	}
	
	public void setSelectStatusVal(String selectStatusVal) {
		this.selectStatusVal = selectStatusVal;
	}

	public String getTesdaNcii() {
		return tesdaNcii;
	}

	public void setTesdaNcii(String tesdaNcii) {
		this.tesdaNcii = tesdaNcii;
	}
	
	public String getRegisteredConcorde() {
		return registeredConcorde;
	}

	public void setRegisteredConcorde(String registeredConcorde) {
		this.registeredConcorde = registeredConcorde;
	}

	public String getPreDeployment() {
		return preDeployment;
	}

	public void setPreDeployment(String preDeployment) {
		this.preDeployment = preDeployment;
	}

	public String getBioId() {
		return bioId;
	}

	public void setBioId(String bioId) {
		this.bioId = bioId;
	}

	public String getMedicalCertVerified() {
		return medicalCertVerified;
	}

	public void setMedicalCertVerified(String medicalCertVerified) {
		this.medicalCertVerified = medicalCertVerified;
	}

	public String getMedicalCertValidator() {
		return medicalCertValidator;
	}

	public void setMedicalCertValidator(String medicalCertValidator) {
		this.medicalCertValidator = medicalCertValidator;
	}

	public Date getMarkedAsRedayTime() {
		return markedAsRedayTime;
	}

	public void setMarkedAsRedayTime(Date markedAsRedayTime) {
		this.markedAsRedayTime = markedAsRedayTime;
	}

	public Date getMarkedAdvancedPlacementScheme() {
		return markedAdvancedPlacementScheme;
	}

	public void setMarkedAdvancedPlacementScheme(Date markedAdvancedPlacementScheme) {
		this.markedAdvancedPlacementScheme = markedAdvancedPlacementScheme;
	}

	public Date getMarkedRegisteredWithConcorde() {
		return markedRegisteredWithConcorde;
	}

	public void setMarkedRegisteredWithConcorde(Date markedRegisteredWithConcorde) {
		this.markedRegisteredWithConcorde = markedRegisteredWithConcorde;
	}

	public Date getMarkedMedicalCertVerify() {
		return markedMedicalCertVerify;
	}

	public void setMarkedMedicalCertVerify(Date markedMedicalCertVerify) {
		this.markedMedicalCertVerify = markedMedicalCertVerify;
	}

	public Bio getBio() {
		return bio;
	}
	
	public void setBio(Bio bio) {
		this.bio = bio;
	}
	
	public String getD_applied() {
		return d_applied;
	}
	
	public void setD_applied(String d_applied) {
		this.d_applied = d_applied;
	}

	public String getD_of_birth() {
		return d_of_birth;
	}
	
	public void setD_of_birth(String d_of_birth) {
		this.d_of_birth = d_of_birth;
	}

	public String getNewCaregiverFlag() {
		return newCaregiverFlag;
	}

	public void setNewCaregiverFlag(String newCaregiverFlag) {
		this.newCaregiverFlag = newCaregiverFlag;
	}

	public Date getDateOfPlacement() {
		return dateOfPlacement;
	}

	public void setDateOfPlacement(Date dateOfPlacement) {
		this.dateOfPlacement = dateOfPlacement;
	}

	public String getNumbersOfPlacement() {
		return numbersOfPlacement;
	}

	public void setNumbersOfPlacement(String numbersOfPlacement) {
		this.numbersOfPlacement = numbersOfPlacement;
	}

	public String getResumeUrl() {
		return resumeUrl;
	}

	public void setResumeUrl(String resumeUrl) {
		this.resumeUrl = resumeUrl;
	}

	public Date getDateOfAwaitingDocument() {
		return dateOfAwaitingDocument;
	}

	public void setDateOfAwaitingDocument(Date dateOfAwaitingDocument) {
		this.dateOfAwaitingDocument = dateOfAwaitingDocument;
	}

	public Date getDateOfSchedulingInterview() {
		return dateOfSchedulingInterview;
	}

	public void setDateOfSchedulingInterview(Date dateOfSchedulingInterview) {
		this.dateOfSchedulingInterview = dateOfSchedulingInterview;
	}

	public Date getDateOfInterviewScheduled() {
		return dateOfInterviewScheduled;
	}

	public void setDateOfInterviewScheduled(Date dateOfInterviewScheduled) {
		this.dateOfInterviewScheduled = dateOfInterviewScheduled;
	}

	public Date getDateOfShortlisted() {
		return dateOfShortlisted;
	}

	public void setDateOfShortlisted(Date dateOfShortlisted) {
		this.dateOfShortlisted = dateOfShortlisted;
	}

	public Date getDateOfNotSelected() {
		return dateOfNotSelected;
	}

	public void setDateOfNotSelected(Date dateOfNotSelected) {
		this.dateOfNotSelected = dateOfNotSelected;
	}

	public Date getDateOfShortlistedWithDiffAvail() {
		return dateOfShortlistedWithDiffAvail;
	}

	public void setDateOfShortlistedWithDiffAvail(Date dateOfShortlistedWithDiffAvail) {
		this.dateOfShortlistedWithDiffAvail = dateOfShortlistedWithDiffAvail;
	}

	public Date getDateOfReadyForPlacement() {
		return dateOfReadyForPlacement;
	}

	public void setDateOfReadyForPlacement(Date dateOfReadyForPlacement) {
		this.dateOfReadyForPlacement = dateOfReadyForPlacement;
	}

	public Date getDateOfTagged() {
		return dateOfTagged;
	}

	public void setDateOfTagged(Date dateOfTagged) {
		this.dateOfTagged = dateOfTagged;
	}

	public Date getDateOfOnHold() {
		return dateOfOnHold;
	}

	public void setDateOfOnHold(Date dateOfOnHold) {
		this.dateOfOnHold = dateOfOnHold;
	}

	public Date getDateOfBlacklisted() {
		return dateOfBlacklisted;
	}

	public void setDateOfBlacklisted(Date dateOfBlacklisted) {
		this.dateOfBlacklisted = dateOfBlacklisted;
	}

	public int getTagOrigin() {
		return tagOrigin;
	}

	public void setTagOrigin(int tagOrigin) {
		this.tagOrigin = tagOrigin;
	}


    @Override
    public String toString() {
        return "Caregiver{" +
               "about='" + about + '\'' +
               ", userId='" + userId + '\'' +
               ", appId='" + appId + '\'' +
               ", email='" + email + '\'' +
               ", fullName='" + fullName + '\'' +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", gender='" + gender + '\'' +
               ", dateOfBirth=" + dateOfBirth +
               ", d_of_birth='" + d_of_birth + '\'' +
               ", age='" + age + '\'' +
               ", countryOfBirth='" + countryOfBirth + '\'' +
               ", hasChildren='" + hasChildren + '\'' +
               ", siblings='" + siblings + '\'' +
               ", languages='" + languages + '\'' +
               ", nationality='" + nationality + '\'' +
               ", educationalLevel='" + educationalLevel + '\'' +
               ", certifiedCpr='" + certifiedCpr + '\'' +
               ", exp='" + exp + '\'' +
               ", maritalStatus='" + maritalStatus + '\'' +
               ", religion='" + religion + '\'' +
               ", mobile='" + mobile + '\'' +
               ", height='" + height + '\'' +
               ", weight='" + weight + '\'' +
               ", motivation='" + motivation + '\'' +
               ", education='" + education + '\'' +
               ", specialities='" + specialities + '\'' +
               ", hobbies='" + hobbies + '\'' +
               ", availability='" + availability + '\'' +
               ", dateApplied=" + dateApplied +
               ", d_applied='" + d_applied + '\'' +
               ", workInHK='" + workInHK + '\'' +
               ", workInSG='" + workInSG + '\'' +
               ", workInTW='" + workInTW + '\'' +
               ", salaryHKD='" + salaryHKD + '\'' +
               ", salarySGD='" + salarySGD + '\'' +
               ", salaryTWD='" + salaryTWD + '\'' +
               ", foodChoice='" + foodChoice + '\'' +
               ", photo='" + photo + '\'' +
               ", skype='" + skype + '\'' +
               ", resume='" + resume + '\'' +
               ", tag=" + tag +
               ", tagId=" + tagId +
               ", tagStatus='" + tagStatus + '\'' +
               ", taggedDate=" + taggedDate +
               ", contractedTo='" + contractedTo + '\'' +
               ", taggedTo='" + taggedTo + '\'' +
               ", assigned='" + assigned + '\'' +
               ", location='" + location + '\'' +
               ", registrationNumber='" + registrationNumber + '\'' +
               ", remarks='" + remarks + '\'' +
               ", nearestAirport='" + nearestAirport + '\'' +
               ", currentAddress='" + currentAddress + '\'' +
               ", numberOfChildren=" + numberOfChildren +
               ", childrenNames='" + childrenNames + '\'' +
               ", workedInSG='" + workedInSG + '\'' +
               ", allergies='" + allergies + '\'' +
               ", diagnosedConditions='" + diagnosedConditions + '\'' +
               ", videoURL='" + videoURL + '\'' +
               ", dateCreated=" + dateCreated +
               ", resumatorStatus='" + resumatorStatus + '\'' +
               ", applyLocations='" + applyLocations + '\'' +
               ", childrenNameAge='" + childrenNameAge + '\'' +
               ", holdPassport='" + holdPassport + '\'' +
               ", yearGraduation='" + yearGraduation + '\'' +
               ", yearStudies='" + yearStudies + '\'' +
               ", caregiverBeforeExp='" + caregiverBeforeExp + '\'' +
               ", sgFin='" + sgFin + '\'' +
               ", currentJob='" + currentJob + '\'' +
               ", currentJobTime='" + currentJobTime + '\'' +
               ", timeOfSg='" + timeOfSg + '\'' +
               ", historyOfTreatment='" + historyOfTreatment + '\'' +
               ", interviewTime='" + interviewTime + '\'' +
               ", referrer='" + referrer + '\'' +
               ", photoPassport='" + photoPassport + '\'' +
               ", photoDegrees='" + photoDegrees + '\'' +
               ", otherFiles='" + otherFiles + '\'' +
               ", bioId='" + bioId + '\'' +
               ", medicalCertVerified='" + medicalCertVerified + '\'' +
               ", medicalCertValidator='" + medicalCertValidator + '\'' +
               ", newCaregiverFlag='" + newCaregiverFlag + '\'' +
               ", status=" + status +
               ", lastModified=" + lastModified +
               ", languageList=" + languageList +
               ", diagnosedList=" + diagnosedList +
               ", specialitiesList=" + specialitiesList +
               ", selectStatusVal='" + selectStatusVal + '\'' +
               ", tesdaNcii='" + tesdaNcii + '\'' +
               ", registeredConcorde='" + registeredConcorde + '\'' +
               ", preDeployment='" + preDeployment + '\'' +
               ", dateOfPlacement=" + dateOfPlacement +
               ", dateOfAwaitingDocument=" + dateOfAwaitingDocument +
               ", dateOfSchedulingInterview=" + dateOfSchedulingInterview +
               ", dateOfInterviewScheduled=" + dateOfInterviewScheduled +
               ", dateOfShortlisted=" + dateOfShortlisted +
               ", dateOfNotSelected=" + dateOfNotSelected +
               ", dateOfShortlistedWithDiffAvail=" + dateOfShortlistedWithDiffAvail +
               ", dateOfReadyForPlacement=" + dateOfReadyForPlacement +
               ", dateOfTagged=" + dateOfTagged +
               ", dateOfOnHold=" + dateOfOnHold +
               ", dateOfBlacklisted=" + dateOfBlacklisted +
               ", numbersOfPlacement='" + numbersOfPlacement + '\'' +
               ", bio=" + bio +
               ", markedAsRedayTime=" + markedAsRedayTime +
               ", markedAdvancedPlacementScheme=" + markedAdvancedPlacementScheme +
               ", markedRegisteredWithConcorde=" + markedRegisteredWithConcorde +
               ", markedMedicalCertVerify=" + markedMedicalCertVerify +
               ", resumeUrl='" + resumeUrl + '\'' +
               ", tagOrigin=" + tagOrigin +
               '}';
    }
}
