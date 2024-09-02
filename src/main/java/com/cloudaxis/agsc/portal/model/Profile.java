package com.cloudaxis.agsc.portal.model;

import java.util.Date;

public class Profile {

	private String user_id;
	private String app_id;
	private String email;
	private String full_name;
	private String first_name;
	private String last_name;
	private String gender;
	private Date dob; // jsp:birth_date
	private String age;
	private String emergency_contact_name;
	private String emergency_contact_phone;
	private String emergency_contact_relationship;
	private String country_of_birth;
	private String has_children;
	private String siblings;
	private String languages;
	private String nationality;
	private String educational_level;
	private String certified_cpr;
	private String exp;
	private String marital_status;
	private String religion;
	private String mobile;
	private String height;
	private String weight;
	private String motivation;
	private String about;
	private String education;
	private String specialties;
	private String hobbies;
	private String availability;
	private Date date_applied;
	private String work_in_hk;
	private String work_in_sg;
	private String work_in_tw;
	private String salary_hkd;
	private String salary_sgd;
	private String salary_twd;
	private String food_choice;
	private String skype;
	private String resume;
	private int tag;
	private int tag_id;
	private Date tagged_date;
	private String tag_status;
	private String contracted_to;
	private String tagged_to;
	private String assigned;
	private String location;
	private String registration_number;
	private String remarks;
	private String nearest_airport;
	private String current_address;
	private int number_of_children;
	private String children_names;
	private String worked_in_sg;
	private String allergies;
	private String diagnosed_conditions;
	private String video_url;
	private Date datecreated;

	private String applicant_status; // ARE YOU INTERESTED IN THIS
										// JOB[YES/NO]
	private String applyLocations; // work in hk/work in sg
	private String children_name_age; // If you have children, please list
										// their names & ages
	private String hold_passport;
	private String year_graduation;
	private String year_studies;
	private String caregiver_before_exp;
	private String sg_fin;
	private String current_job;
	private String current_job_time;
	private String time_of_sg; // time for sg
	private String history_of_treatment; // about the treatment/ medication
	private String interview_time;
	private String referrer; // referred by a friend

	// url(photo)
	private String photo; // jsp:my_photo
	private String photo_passport; // jsp:passport
	private String photo_degrees; // jsp:degrees
	private String other_files; // jsp:other_file
	private Date last_modified;
	private int status;			//workflow status
	private String tesda_ncii;
	private String pre_deployment;
	private String registered_concorde;
	private String evaluationSummary;
	private String medicalCertVerified;
	private Date markedAsRreadyTime;
	private String medicalCertValidator;

    public Profile() {
        skype = "";
        current_address = "";
        children_name_age = "";
        hold_passport = "";
        worked_in_sg = "";
        current_job = "";
        current_job_time = "";
        availability = "";
        referrer = "";
    }

    public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFull_name() {
		return full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getCountry_of_birth() {
		return country_of_birth;
	}

	public void setCountry_of_birth(String country_of_birth) {
		this.country_of_birth = country_of_birth;
	}

	public String getHas_children() {
		return has_children;
	}

	public void setHas_children(String has_children) {
		this.has_children = has_children;
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

	public String getEducational_level() {
		return educational_level;
	}

	public void setEducational_level(String educational_level) {
		this.educational_level = educational_level;
	}

	public String getCertified_cpr() {
		return certified_cpr;
	}

	public void setCertified_cpr(String certified_cpr) {
		this.certified_cpr = certified_cpr;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public String getMarital_status() {
		return marital_status;
	}

	public void setMarital_status(String marital_status) {
		this.marital_status = marital_status;
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

	public String getSpecialties() {
		return specialties;
	}

	public void setSpecialties(String specialties) {
		this.specialties = specialties;
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

	public Date getDate_applied() {
		return date_applied;
	}

	public void setDate_applied(Date date_applied) {
		this.date_applied = date_applied;
	}

	public String getWork_in_hk() {
		return work_in_hk;
	}

	public void setWork_in_hk(String work_in_hk) {
		this.work_in_hk = work_in_hk;
	}

	public String getWork_in_sg() {
		return work_in_sg;
	}

	public void setWork_in_sg(String work_in_sg) {
		this.work_in_sg = work_in_sg;
	}

	public String getWork_in_tw() {
		return work_in_tw;
	}

	public void setWork_in_tw(String work_in_tw) {
		this.work_in_tw = work_in_tw;
	}

	public String getSalary_hkd() {
		return salary_hkd;
	}

	public void setSalary_hkd(String salary_hkd) {
		this.salary_hkd = salary_hkd;
	}

	public String getSalary_sgd() {
		return salary_sgd;
	}

	public void setSalary_sgd(String salary_sgd) {
		this.salary_sgd = salary_sgd;
	}

	public String getSalary_twd() {
		return salary_twd;
	}

	public void setSalary_twd(String salary_twd) {
		this.salary_twd = salary_twd;
	}

	public String getFood_choice() {
		return food_choice;
	}

	public void setFood_choice(String food_choice) {
		this.food_choice = food_choice;
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

	public int getTag_id() {
		return tag_id;
	}

	public void setTag_id(int tag_id) {
		this.tag_id = tag_id;
	}

	public Date getTagged_date() {
		return tagged_date;
	}

	public void setTagged_date(Date tagged_date) {
		this.tagged_date = tagged_date;
	}

	public String getTag_status() {
		return tag_status;
	}

	public void setTag_status(String tag_status) {
		this.tag_status = tag_status;
	}

	public String getContracted_to() {
		return contracted_to;
	}

	public void setContracted_to(String contracted_to) {
		this.contracted_to = contracted_to;
	}

	public String getTagged_to() {
		return tagged_to;
	}

	public void setTagged_to(String tagged_to) {
		this.tagged_to = tagged_to;
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

	public String getRegistration_number() {
		return registration_number;
	}

	public void setRegistration_number(String registration_number) {
		this.registration_number = registration_number;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getNearest_airport() {
		return nearest_airport;
	}

	public void setNearest_airport(String nearest_airport) {
		this.nearest_airport = nearest_airport;
	}

	public String getCurrent_address() {
		return current_address;
	}

	public void setCurrent_address(String current_address) {
		this.current_address = current_address;
	}

	public int getNumber_of_children() {
		return number_of_children;
	}

	public void setNumber_of_children(int number_of_children) {
		this.number_of_children = number_of_children;
	}

	public String getChildren_names() {
		return children_names;
	}

	public void setChildren_names(String children_names) {
		this.children_names = children_names;
	}

	public String getWorked_in_sg() {
		return worked_in_sg;
	}

	public void setWorked_in_sg(String worked_in_sg) {
		this.worked_in_sg = worked_in_sg;
	}

	public String getAllergies() {
		return allergies;
	}

	public void setAllergies(String allergies) {
		this.allergies = allergies;
	}

	public String getDiagnosed_conditions() {
		return diagnosed_conditions;
	}

	public void setDiagnosed_conditions(String diagnosed_conditions) {
		this.diagnosed_conditions = diagnosed_conditions;
	}

	public String getVideo_url() {
		return video_url;
	}

	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}

	public Date getDatecreated() {
		return datecreated;
	}

	public void setDatecreated(Date datecreated) {
		this.datecreated = datecreated;
	}

	public String getApplicant_status() {
		return applicant_status;
	}

	public void setApplicant_status(String applicant_status) {
		this.applicant_status = applicant_status;
	}

	public String getApplyLocations() {
		return applyLocations;
	}

	public void setApplyLocations(String applyLocations) {
		this.applyLocations = applyLocations;
	}

	public String getChildren_name_age() {
		return children_name_age;
	}

	public void setChildren_name_age(String children_name_age) {
		this.children_name_age = children_name_age;
	}

	public String getHold_passport() {
		return hold_passport;
	}

	public void setHold_passport(String hold_passport) {
		this.hold_passport = hold_passport;
	}

	public String getYear_graduation() {
		return year_graduation;
	}

	public void setYear_graduation(String year_graduation) {
		this.year_graduation = year_graduation;
	}

	public String getYear_studies() {
		return year_studies;
	}

	public void setYear_studies(String year_studies) {
		this.year_studies = year_studies;
	}

	public String getCaregiver_before_exp() {
		return caregiver_before_exp;
	}

	public void setCaregiver_before_exp(String caregiver_before_exp) {
		this.caregiver_before_exp = caregiver_before_exp;
	}

	public String getSg_fin() {
		return sg_fin;
	}

	public void setSg_fin(String sg_fin) {
		this.sg_fin = sg_fin;
	}

	public String getCurrent_job() {
		return current_job;
	}

	public void setCurrent_job(String current_job) {
		this.current_job = current_job;
	}

	public String getCurrent_job_time() {
		return current_job_time;
	}

	public void setCurrent_job_time(String current_job_time) {
		this.current_job_time = current_job_time;
	}

	public String getTime_of_sg() {
		return time_of_sg;
	}

	public void setTime_of_sg(String time_of_sg) {
		this.time_of_sg = time_of_sg;
	}

	public String getHistory_of_treatment() {
		return history_of_treatment;
	}

	public void setHistory_of_treatment(String history_of_treatment) {
		this.history_of_treatment = history_of_treatment;
	}

	public String getInterview_time() {
		return interview_time;
	}

	public void setInterview_time(String interview_time) {
		this.interview_time = interview_time;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public String getPhoto_passport() {
		return photo_passport;
	}

	public void setPhoto_passport(String photo_passport) {
		this.photo_passport = photo_passport;
	}

	public String getPhoto_degrees() {
		return photo_degrees;
	}

	public void setPhoto_degrees(String photo_degrees) {
		this.photo_degrees = photo_degrees;
	}

	public String getOther_files() {
		return other_files;
	}

	public void setOther_files(String other_files) {
		this.other_files = other_files;
	}
	
	public Date getLast_modified() {
		return last_modified;
	}

	public void setLast_modified(Date last_modified) {
		this.last_modified = last_modified;
	}

	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getTesda_ncii() {
		return tesda_ncii;
	}

	public void setTesda_ncii(String tesda_ncii) {
		this.tesda_ncii = tesda_ncii;
	}

	public String getPre_deployment() {
		return pre_deployment;
	}
	
	public void setPre_deployment(String pre_deployment) {
		this.pre_deployment = pre_deployment;
	}

	public String getRegistered_concorde() {
		return registered_concorde;
	}
	
	public void setRegistered_concorde(String registered_concorde) {
		this.registered_concorde = registered_concorde;
	}

	public String getEvaluationSummary() {
		return evaluationSummary;
	}

	public void setEvaluationSummary(String evaluationSummary) {
		this.evaluationSummary = evaluationSummary;
	}

	public String getMedicalCertVerified() {
		return medicalCertVerified;
	}

	public void setMedicalCertVerified(String medicalCertVerified) {
		this.medicalCertVerified = medicalCertVerified;
	}
	
	public Date getMarkedAsRreadyTime() {
		return markedAsRreadyTime;
	}
	
	public void setMarkedAsRreadyTime(Date markedAsRreadyTime) {
		this.markedAsRreadyTime = markedAsRreadyTime;
	}
	
	public String getMedicalCertValidator() {
		return medicalCertValidator;
	}

	public void setMedicalCertValidator(String medicalCertValidator) {
		this.medicalCertValidator = medicalCertValidator;
	}

	public String getEmergency_contact_name() {
		return emergency_contact_name;
	}

	public void setEmergency_contact_name(String emergency_contact_name) {
		this.emergency_contact_name = emergency_contact_name;
	}

	public String getEmergency_contact_phone() {
		return emergency_contact_phone;
	}

	public void setEmergency_contact_phone(String emergency_contact_phone) {
		this.emergency_contact_phone = emergency_contact_phone;
	}

	public String getEmergency_contact_relationship() {
		return emergency_contact_relationship;
	}

	public void setEmergency_contact_relationship(String emergency_contact_relationship) {
		this.emergency_contact_relationship = emergency_contact_relationship;
	}

	@Override
    public String toString() {
        return "Profile{" +
               "about='" + about + '\'' +
               ", user_id='" + user_id + '\'' +
               ", app_id='" + app_id + '\'' +
               ", email='" + email + '\'' +
               ", full_name='" + full_name + '\'' +
               ", first_name='" + first_name + '\'' +
               ", last_name='" + last_name + '\'' +
               ", gender='" + gender + '\'' +
               ", dob=" + dob +
               ", age='" + age + '\'' +
               ", country_of_birth='" + country_of_birth + '\'' +
               ", has_children='" + has_children + '\'' +
               ", siblings='" + siblings + '\'' +
               ", languages='" + languages + '\'' +
               ", nationality='" + nationality + '\'' +
               ", educational_level='" + educational_level + '\'' +
               ", certified_cpr='" + certified_cpr + '\'' +
               ", exp='" + exp + '\'' +
               ", marital_status='" + marital_status + '\'' +
               ", religion='" + religion + '\'' +
               ", mobile='" + mobile + '\'' +
               ", emergency_contact_name='" + emergency_contact_name + '\'' +
               ", emergency_contact_phone='" + emergency_contact_phone + '\'' +
               ", emergency_contact_relationship='" + emergency_contact_relationship + '\'' +
               ", height='" + height + '\'' +
               ", weight='" + weight + '\'' +
               ", motivation='" + motivation + '\'' +
               ", education='" + education + '\'' +
               ", specialties='" + specialties + '\'' +
               ", hobbies='" + hobbies + '\'' +
               ", availability='" + availability + '\'' +
               ", date_applied=" + date_applied +
               ", work_in_hk='" + work_in_hk + '\'' +
               ", work_in_sg='" + work_in_sg + '\'' +
               ", work_in_tw='" + work_in_tw + '\'' +
               ", salary_hkd='" + salary_hkd + '\'' +
               ", salary_sgd='" + salary_sgd + '\'' +
               ", salary_twd='" + salary_twd + '\'' +
               ", food_choice='" + food_choice + '\'' +
               ", skype='" + skype + '\'' +
               ", resume='" + resume + '\'' +
               ", tag=" + tag +
               ", tag_id=" + tag_id +
               ", tagged_date=" + tagged_date +
               ", tag_status='" + tag_status + '\'' +
               ", contracted_to='" + contracted_to + '\'' +
               ", tagged_to='" + tagged_to + '\'' +
               ", assigned='" + assigned + '\'' +
               ", location='" + location + '\'' +
               ", registration_number='" + registration_number + '\'' +
               ", remarks='" + remarks + '\'' +
               ", nearest_airport='" + nearest_airport + '\'' +
               ", current_address='" + current_address + '\'' +
               ", number_of_children=" + number_of_children +
               ", children_names='" + children_names + '\'' +
               ", worked_in_sg='" + worked_in_sg + '\'' +
               ", allergies='" + allergies + '\'' +
               ", diagnosed_conditions='" + diagnosed_conditions + '\'' +
               ", video_url='" + video_url + '\'' +
               ", datecreated=" + datecreated +
               ", applicant_status='" + applicant_status + '\'' +
               ", applyLocations='" + applyLocations + '\'' +
               ", children_name_age='" + children_name_age + '\'' +
               ", hold_passport='" + hold_passport + '\'' +
               ", year_graduation='" + year_graduation + '\'' +
               ", year_studies='" + year_studies + '\'' +
               ", caregiver_before_exp='" + caregiver_before_exp + '\'' +
               ", sg_fin='" + sg_fin + '\'' +
               ", current_job='" + current_job + '\'' +
               ", current_job_time='" + current_job_time + '\'' +
               ", time_of_sg='" + time_of_sg + '\'' +
               ", history_of_treatment='" + history_of_treatment + '\'' +
               ", interview_time='" + interview_time + '\'' +
               ", referrer='" + referrer + '\'' +
               ", photo='" + photo + '\'' +
               ", photo_passport='" + photo_passport + '\'' +
               ", photo_degrees='" + photo_degrees + '\'' +
               ", other_files='" + other_files + '\'' +
               ", last_modified=" + last_modified +
               ", status=" + status +
               ", tesda_ncii='" + tesda_ncii + '\'' +
               ", pre_deployment='" + pre_deployment + '\'' +
               ", registered_concorde='" + registered_concorde + '\'' +
               ", evaluationSummary='" + evaluationSummary + '\'' +
               ", medicalCertVerified='" + medicalCertVerified + '\'' +
               ", markedAsRreadyTime=" + markedAsRreadyTime +
               ", medicalCertValidator='" + medicalCertValidator + '\'' +
               '}';
    }
}
