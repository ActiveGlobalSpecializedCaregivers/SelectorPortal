package com.toptal.migration;

import com.cloudaxis.agsc.portal.helpers.StringUtil;
import com.cloudaxis.agsc.portal.model.Profile;
import com.toptal.migration.dao.MigrationDao;
import com.toptal.migration.model.CandidateQuestionnaire;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 * <code>CreateStrategy</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class CreateStrategy extends AbstractStrategy{

    private static final Logger logger = Logger.getLogger(CreateStrategy.class);
    public CreateStrategy(CandidateMigrationContext migrationContext, MigrationDao migrationDao) {
        super(migrationContext, migrationDao);
    }

    @Override
    public void migrateCandidate() throws IOException {
        try {
            createCandidateRecord();
        }
        catch (ParseException e) {
            logger.error("Error creating candidate record:"+e);
            throw new IOException("Invalid file format:"+e, e);
        }
        migrateAttachments();

        migrateEvaluations();

        migrateComments();

        migrateEmails();
    }

    private Profile createProfile(CandidateQuestionnaire[] candidateQuestionnaire)
            throws ParseException {
        Profile profile = new Profile();
        profile.setWork_in_tw("NO");
        profile.setWork_in_sg("NO");
        profile.setWork_in_hk("NO");
        profile.setMedicalCertVerified("No");
        profile.setRegistered_concorde("No");
        profile.setPre_deployment("No");
        profile.setExp("0");

        if(candidateQuestionnaire == null){
            logger.info("questionnaire is not found - returning empty profile");
            // set default values:
            profile.setGender("");
            profile.setLanguages("");
            profile.setNationality("");
            profile.setEducational_level("");
            profile.setCertified_cpr("No");
            profile.setMarital_status("");
            profile.setReligion("");
            profile.setFood_choice("No restrictions");
            profile.setStatus(1);

            return profile;
        }

        for(CandidateQuestionnaire questionnaire : candidateQuestionnaire){
            String question = questionnaire.getQuestion();
            String answer = questionnaire.getAnswer();
            if(question.startsWith("Please give us the best Days")) {
                profile.setInterview_time(answer);
            }
            else if(question.startsWith("Please give us your Skype")) {
                profile.setSkype(answer);
            }
            else if(question.startsWith("What is your country of")) {
                profile.setNationality(answer);
            }
            else if(question.startsWith("Are you certified for CPR")) {
                profile.setCertified_cpr(answer);
            }
            else if(question.startsWith("Have you worked in Singapore")) {
                answer = com.toptal.migration.model.StringUtil.stripQuotes(answer);
                if(!"yes".equalsIgnoreCase(answer) && !"no".equalsIgnoreCase(answer)){
                    logger.info("Replacing answer for Have you worked in Singapore ["+answer+"] to No");
                    answer = "No";
                }
                profile.setWork_in_sg(answer);
            }
            else if(question.startsWith("Number of years of studies")) {
                profile.setYear_studies(answer);
            }
            else if(question.startsWith("Year of Graduation")){
                profile.setYear_graduation(answer);
            }
            else if(question.startsWith("For how long would you like to work as a Live-In Caregiver with Active Global?")){
                profile.setTime_of_sg(answer);
            }
            else if(question.startsWith("FOR FILIPINO CAREGIVERS ONLY: HAVE YOU OBTAINED TESDA NCII Certification")){
                profile.setTesda_ncii(answer);
            }
            else if(question.startsWith("What is your religion?")){
                profile.setReligion(answer);
            }
            else if(question.startsWith("What is your marital status")){
                profile.setMarital_status(answer);
            }
            else if(question.startsWith("If you have children")){
                profile.setChildren_name_age(answer);
            }
            else if(question.startsWith("Do you follow a specific diet?")){
                profile.setFood_choice(answer);
            }
            else if(question.startsWith("What is your height in centimeters")){
                profile.setHeight(answer);
            }
            else if(question.startsWith("What is your weight in kg")){
                profile.setWeight(answer);
            }
            else if(question.startsWith("Current role / job")){
                if(!questionnaire.getQuestionnaireName().equals("Selected Caregiver Biodata")){
                    profile.setCurrent_job(answer);
                }
            }
            else if(question.startsWith("The job opportunities we are currently offering concern only LIVE AT HOME CAREGIVING ")){
                profile.setApplicant_status(answer);
            }
            else if(question.startsWith("We are currently offering Live-In Caregiver opportunities both in HONG KONG and SINGAPORE")){
                profile.setApplyLocations(answer);
            }
            else if(question.startsWith("If you checked any of the boxes")){
                profile.setHistory_of_treatment(answer);
            }
            else if(question.startsWith("How many siblings to you have")){
                profile.setSiblings(answer);
            }
            else if(question.startsWith("What is your Gender?")){
                profile.setGender(answer);
            }
            else if(question.startsWith("Which languages do you speak")){
                profile.setLanguages(answer);
            }
            else if(question.startsWith("What are your hobbies")){
                profile.setHobbies(answer);
            }
            else if(question.startsWith("For how long have you been in this job")){
                profile.setCurrent_job_time(answer);
            }
            else if(question.startsWith("What is the airport nearest to your home town")){
                profile.setNearest_airport(answer);
            }
            else if(question.startsWith("Do you have children")){
                profile.setHas_children(answer);
            }
            else if(question.startsWith("what is your date of birth")){
                String birth_date = answer;
                if (!StringUtil.isBlank(birth_date)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date dob = sdf.parse(birth_date);
                    int age = new Date().getYear() - dob.getYear();
                    profile.setAge(String.valueOf(age));
                    profile.setDob(dob);
                }
            }
            else if(question.startsWith("Have you worked as a Caregiver before")){
                profile.setCaregiver_before_exp(answer);
            }
            else if(question.startsWith("What is your place of birth")){
                profile.setCountry_of_birth(answer);
            }
            else if(question.startsWith("What is your main motivation to become a ")){
                profile.setMotivation(answer);
            }
            else if(question.startsWith("What Nursing degree")){
                profile.setEducational_level(answer);
            }
            else if(question.startsWith("Do you have or were you ever diagnosed with ")){
                profile.setDiagnosed_conditions(answer);
            }
            else if(question.startsWith("If you have any allergies")){
                profile.setAllergies(answer);
            }
            else if(question.startsWith("Which Nursing School did you go to")){
                profile.setEducation(answer);
            }
            else if(question.startsWith("Please let us know if you have experience ")){
                profile.setSpecialties(answer);
            }
            else if(question.startsWith("Assuming we offer you a job")){
                profile.setAvailability(answer);
            }
            else{
                System.out.println("unexpected question = " + question);
            }
        }
        logger.info("Configured candidate profile:" + profile);
        return profile;
    }

    protected void createCandidateRecord() throws ParseException {
        createCandidate();

        updateCandidateStatus();
    }

    private void updateCandidateStatus() {
        // update status
        String status = migrationContext.getCandidateStatus();
        String[] tokens = status.split("-");
        logger.info("updating candidate status to " + tokens[0].trim());
        migrationContext.getBaseServices().updateCandidateStatus(candidate,
                                                                 migrationContext.getUser(), tokens[0].trim());
    }

    private void createCandidate() throws ParseException {
        CandidateStorage candidateStorage = migrationContext.getCandidateStorage();
        candidate = candidateStorage.loadCandidate(migrationContext.getProspectId());

        candidate.setFullName(migrationContext.getSelectorName());
        logger.info("Candidate loaded from data file.");

        final CandidateQuestionnaire[] candidateQuestionnaire = candidateStorage.loadCandidateQuestionnaires(
                migrationContext.getProspectId());
        logger.info("Found questionnaires for candidate:" +
                    (candidateQuestionnaire == null ? "NULL" : candidateQuestionnaire.length));
        Profile candidateProfile = createProfile(candidateQuestionnaire);

        candidateProfile.setEmail(candidate.getEmail());
        candidateProfile.setFull_name(candidate.getFullName());
        candidateProfile.setFirst_name(candidate.getFirstName());
        candidateProfile.setLast_name(candidate.getLastName());
        candidateProfile.setMobile(candidate.getMobile());
        candidateProfile.setDate_applied(candidate.getDateApplied());

        final BaseServices baseServices = migrationContext.getBaseServices();
        int userId = baseServices.createProfile(candidateProfile);
        logger.info("Saved candidate profile to DB, got userId=" + userId);

        candidate.setUserId(String.valueOf(userId));
    }
}
