package com.cloudaxis.agsc.portal.helpers;

import java.util.HashMap;

public class ApplicationDict {

	private static final String[] gender = { "Male", "Female" };
	private static final String[] nationality = { "Bangladesh", "India", "Indonesia", "Myanmar","Philippines","Sri Lanka","Others"};
	private static final String[] maritalStatus = { "Single", "Married", "Divorced/Separated", "Widowed" };
	private static final String[] hasChildren = {"No child", "1 child", "2 children", "3 children", "4 children", "5 children",
			"6 children", "7 children", "8 children" };
	private static final String[] languages = { "Arabic", "Bengali", "Burmese", "Cantonese", "English", "Hokkien",
			"Hakka", "Mandarin Chinese", "Malay",
			"Tamil", "Tagalog", "Telugu", "Teochew", "Hindi", "Malayalam", "Sinhalese", "Others" };
	private static final String[] religion = { "Christian", "Buddhist", "Hindu", "Muslim", "Sikh", "Others" };
	private static final String[] foodChoice = { "No restrictions", "Vegetarian", "Halal" };
	private static final String[] educationalLevel = { "Caregiver Certificate", "Nurse Assistant Diploma", "Nurse Diploma",
			"Bachelor of Sciences in Nursing or higher" };
	private static final String[] Y_N = { "Yes", "No" };
	private static final String[] diagnosedConditions = { "Mental illness", "Epilepsy", "Asthma", "Diabetes", "Hypertension",
			"Tuberculosis",
			"Heart disease", "Malaria", "Operations", "Physical disabilities", "Sexually transmitted diseases",
			"Other conditions (ex.: thyroid conditions, migraine, chronic pain, any condition that requires regular medication =&gt; please specify in the next question)",
			"None of the above mentioned" };
	/*private static final String[] selectStatus = {"New Applicant","Scheduling Interview","Shortlisted","Awaiting Documents","Not Selected / Not Interested for now","On Hold","Shortlisted with differed availability",
			"Ready For Placement","Tagged","Contracted","Interview Scheduled","Blacklisted"};
	*/
	private static final HashMap<String, String> selectStatus = new HashMap<String, String>() {
		{
			put("0","New Applicant");
			put("3","Awaiting Documents");
			put("1","Scheduling Interview");
			put("10","Interview Scheduled");
			put("2","Shortlisted");
			put("4","Not Selected / Not Interested for now");
			put("6","Shortlisted with differed availability");
			put("7","Ready For Placement");
			put("8","Tagged");
			put("9","Contracted");
			put("5","On Hold");
			put("11","Blacklisted");
		}
	};
	
	private static final String[] specialities = {"Bed sores","Cancer","Dementia / Alzheimer's","Diabetes patients (including administering insulin)","General wound care",
			"Geriatric","New-born babies (healthy)","New-born babies (with medical problems)","NGT (nasogastric tube feeding)","Paediatric","PEG (percutaneous endoscopic gastrostomy feeding tube)",
			"Psychiatric/Special needs patients","Stoma care","Stroke patients","Suctioning of airways","Tracheostomy care","Urinary catheter","Simple cases only", "Chemotherapy", "Dialysis", "Respiratory Therapy"};
	private static final String[] availability = {"Immediate","2-3 weeks","1 month","applicant_no_selection"};
	private static final HashMap<String, String> roleMap = new HashMap<String, String>() {
        {
            put("ROLE_ADMIN", "ADMIN");
            put("ROLE_SALES", "SALES");
            put("ROLE_RECRUITER","RECRUITER");
            put("ROLE_PH_RECRUITING_PARTNER","PH RECRUITING PARTNER");
            put("ROLE_HOSPITAL","HOSPITAL");
            put("ROLE_SALES_SG", "SALES_SG");
            put("ROLE_SALES_HK", "SALES_HK");
            put("ROLE_SALES_TW", "SALES_TW");
        }
    };
	
    private static final HashMap<String, String> tag = new HashMap<String, String>() {
    	{
    		put("0", "Available");
    		put("1", "Tagged");
    		put("2", "Contracted");
    		put("3", "On hold");
    	}
    };
    
    private static final HashMap<String, String> dateOfStatusMap = new HashMap<String, String>(){
    	{
    		put("0", "date_applied");
    		put("3", "date_awaiting_documents");
    		put("1", "date_scheduling_interview");
    		put("10", "date_interview_scheduled");
    		put("2", "date_shortlisted");
    		put("4", "date_not_selected");
    		put("6", "date_shortlisted_with_diff_avail");
    		put("7", "date_ready_for_placement");
    		put("8", "date_tagged");
    		put("9", "date_of_placement");
    		put("5", "date_on_hold");
    		put("11", "date_blacklisted");
    	}
    };
    
	public static String[] getGender() {
		return gender;
	}
	
	public static String[] getNationality() {
		return nationality;
	}
	
	public static String[] getMaritalstatus() {
		return maritalStatus;
	}
	
	public static String[] getHaschildren() {
		return hasChildren;
	}
	
	public static String[] getLanguages() {
		return languages;
	}
	
	public static String[] getReligion() {
		return religion;
	}
	
	public static String[] getFoodchoice() {
		return foodChoice;
	}
	
	public static String[] getEducationallevel() {
		return educationalLevel;
	}
	
	public static String[] getDiagnosedconditions() {
		return diagnosedConditions;
	}
	
	public static String[] getSpecialities() {
		return specialities;
	}

	public static String[] getAvailability() {
		return availability;
	}

	public static HashMap<String, String> getTag() {
		return tag;
	}
	
	public static HashMap<String, String> getRolemap() {
		return roleMap;
	}
	
	public static String[] getyN() {
		return Y_N;
	}

	public static HashMap<String, String> getSelectstatus() {
		return selectStatus;
	}

	public static HashMap<String, String> getDateOfStatusMap(){
		return dateOfStatusMap;
	}
}
