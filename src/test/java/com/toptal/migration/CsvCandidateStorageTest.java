package com.toptal.migration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.cloudaxis.agsc.portal.model.Caregiver;
import com.toptal.migration.model.CandidateDocument;
import com.toptal.migration.model.CandidateEmail;
import com.toptal.migration.model.CandidateEvaluation;
import com.toptal.migration.model.CandidateFeedback;
import com.toptal.migration.model.CandidateQuestionnaire;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * <code>CsvCandidateStorageTest</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class CsvCandidateStorageTest {

    @Test
    public void testCandidateLoad(){
        ArrayList<String> lines = new ArrayList<>(3);
        lines.add("prospect_id,prospect_first_name,prospect_last_name,prospect_email,prospect_address,prospect_city,prospect_state,prospect_postal,prospect_phone,prospect_location,eeo_gender,eeo_race,eeo_disability,prospect_salary,prospect_linkedin,prospect_start_date,prospect_referrer,prospect_languages,prospect_college,prospect_gpa,prospect_twitter,prospect_date_applied,prospect_source,resume_filename,job_id,prospect_status");
        lines.add("prospect_20130716074311_9GND5E9WMGGUAJCV,Clement,Tonui,tonuiclement@gmail.com,,,,,+254720578924,\"   \",\"Not Provided\",,\"Decline to answer\",,,,,,,,,2013-07-16,,Tonui_Clement_20130716074311.doc,job_20130403030147_OGBMMRFWW9KDOLUG,Location");
        lines.add("prospect_20130403050601_3F0JF0C25VRISFMB,Sampath,Kannagi,kannagis1@yahoo.com,,,,,9791633341,\"   \",\"Not Provided\",,\"Decline to answer\",,,,,,,,,2013-04-03,,Kannagi_Sampath_20130403053742.docx,job_20130403030147_OGBMMRFWW9KDOLUG,\"Not a Fit\"");

        DataFileProcessorFactory factory = new TestDataFileProcessorFactory(lines);
        CsvCandidateStorage candidateStorage = new CsvCandidateStorage(factory);

        Caregiver candidate = candidateStorage.loadCandidate("prospect_20130716074311_9GND5E9WMGGUAJCV");
        assertNotNull(candidate);
        assertEquals("Clement", candidate.getFirstName());
        assertEquals("Tonui", candidate.getLastName());
        assertEquals("tonuiclement@gmail.com", candidate.getEmail());
        assertEquals("+254720578924", candidate.getMobile());

        candidate = candidateStorage.loadCandidate("prospect_20130403050601_3F0JF0C25VRISFMB");
        assertNotNull(candidate);
        assertEquals("Sampath", candidate.getFirstName());
        assertEquals("Kannagi", candidate.getLastName());
        assertEquals("kannagis1@yahoo.com", candidate.getEmail());
        assertEquals("9791633341", candidate.getMobile());

        candidate = candidateStorage.loadCandidate(
                "prospect_20130403050601_3F0JF0C25VRISFMB_INVALID");
        assertNull(candidate);
    }

    @Test
    public void testCandidateDocumentLoad(){
        ArrayList<String> lines = new ArrayList<>(8);

        lines.add("prospect_id,document_name");
        lines.add("prospect_20130716074311_9GND5E9WMGGUAJCV,clem_picture.jpg");
        lines.add("prospect_20130716074311_9GND5E9WMGGUAJCV,jobapplicationforthepostofmidwifesavemymother.zip");
        lines.add("prospect_20130403050601_3F0JF0C25VRISFMB,CURRICULUM_VITAE.docx");
        lines.add("prospect_20130404074316_SZLJIOLW4WPB88CH,Vineesh_Vincent.pdf");
        lines.add("prospect_20130404074316_SZLJIOLW4WPB88CH,degree_Vineesh.jpg");
        lines.add("prospect_20130404074316_SZLJIOLW4WPB88CH,passport_Vineesh.jpg");
        lines.add("prospect_20130404074316_SZLJIOLW4WPB88CH,Vineesh_Vincent_AG.pdf");

        DataFileProcessorFactory factory = new TestDataFileProcessorFactory(lines);
        CsvCandidateStorage candidateStorage = new CsvCandidateStorage(factory);

        CandidateDocument[] documents = candidateStorage.loadCandidateDocuments(
                "prospect_20130716074311_9GND5E9WMGGUAJCV");
        assertNotNull(documents);
        assertEquals(2, documents.length);
        assertTrue(documents[0].getFileName().equals("clem_picture.jpg")
                   || documents[0].getFileName().equals("jobapplicationforthepostofmidwifesavemymother.zip"));
        assertTrue(documents[1].getFileName().equals("clem_picture.jpg")
                   || documents[1].getFileName().equals("jobapplicationforthepostofmidwifesavemymother.zip"));

        documents = candidateStorage.loadCandidateDocuments(
                "prospect_20130403050601_3F0JF0C25VRISFMB");
        assertNotNull(documents);
        assertEquals(1, documents.length);
        assertTrue(documents[0].getFileName().equals("CURRICULUM_VITAE.docx"));

        documents = candidateStorage.loadCandidateDocuments(
                "prospect_20130404074316_SZLJIOLW4WPB88CH");
        assertNotNull(documents);
        assertEquals(4, documents.length);

        documents = candidateStorage.loadCandidateDocuments(
                "prospect_20130403050601_3F0JF0C25VRISFMB_INVALID");
        assertNull(documents);
    }

    @Test
    public void testCandidateFilesLoad(){
        ArrayList<String> lines = new ArrayList<>(8);

        lines.add("prospect_id,document_name");
        lines.add("prospect_20130716074311_9GND5E9WMGGUAJCV,clem_picture.jpg");
        lines.add("prospect_20130716074311_9GND5E9WMGGUAJCV,jobapplicationforthepostofmidwifesavemymother.zip");
        lines.add("prospect_20130403050601_3F0JF0C25VRISFMB,CURRICULUM_VITAE.docx");
        lines.add("prospect_20130404074316_SZLJIOLW4WPB88CH,Vineesh_Vincent.pdf");
        lines.add("prospect_20130404074316_SZLJIOLW4WPB88CH,degree_Vineesh.jpg");
        lines.add("prospect_20130404074316_SZLJIOLW4WPB88CH,passport_Vineesh.jpg");
        lines.add("prospect_20130404074316_SZLJIOLW4WPB88CH,Vineesh_Vincent_AG.pdf");

        DataFileProcessorFactory factory = new TestDataFileProcessorFactory(lines);
        CsvCandidateStorage candidateStorage = new CsvCandidateStorage(factory);

        CandidateDocument[] documents = candidateStorage.loadCandidateFiles(
                "prospect_20130716074311_9GND5E9WMGGUAJCV");
        assertNotNull(documents);
        assertEquals(2, documents.length);
        assertTrue(documents[0].getFileName().equals("clem_picture.jpg")
                   || documents[0].getFileName().equals("jobapplicationforthepostofmidwifesavemymother.zip"));
        assertTrue(documents[1].getFileName().equals("clem_picture.jpg")
                   || documents[1].getFileName().equals("jobapplicationforthepostofmidwifesavemymother.zip"));

        documents = candidateStorage.loadCandidateFiles("prospect_20130403050601_3F0JF0C25VRISFMB");
        assertNotNull(documents);
        assertEquals(1, documents.length);
        assertTrue(documents[0].getFileName().equals("CURRICULUM_VITAE.docx"));

        documents = candidateStorage.loadCandidateFiles("prospect_20130404074316_SZLJIOLW4WPB88CH");
        assertNotNull(documents);
        assertEquals(4, documents.length);

        documents = candidateStorage.loadCandidateFiles(
                "prospect_20130403050601_3F0JF0C25VRISFMB_INVALID");
        assertNull(documents);
    }

    @Test
    public void testCandidateQuestionnairesLoad(){
        ArrayList<String> lines = new ArrayList<>(8);

        lines.add("prospect_id,prospect_first_name,prospect_last_name,prospect_email,job_title,questionnaire_name,question,answer");
        lines.add("prospect_20130409074101_JGXT1RGKX97V5WIF,Sudhatai,Gangadhar Tribhuwan,tribhuwansudha1973@gmail.com,Singapore / Hong Kong Live-In Caregiver,Caregiver Pre-Qualification Survey,Are you certified for CPR / First Aid?,Yes");
        lines.add("prospect_20130614111241_A3E26PTEBTRJXQRI,daljeet,kaur,luckydilpreet@gmail.com,Singapore / Hong Kong Live-In Caregiver,Caregiver Pre-Qualification Survey,Are you certified for CPR / First Aid?,Yes");
        lines.add("prospect_20130625093251_EKOAYBCJBIKIPS1J,Vasantha,Singe,a.vasanthasinge@yahoo.com,Singapore / Hong Kong Live-In Caregiver,Caregiver Pre-Qualification Survey,Are you certified for CPR / First Aid?,Yes");

        DataFileProcessorFactory factory = new TestDataFileProcessorFactory(lines);
        CsvCandidateStorage candidateStorage = new CsvCandidateStorage(factory);

        CandidateQuestionnaire[] questionnaires = candidateStorage.loadCandidateQuestionnaires(
                "prospect_20130409074101_JGXT1RGKX97V5WIF");
        assertNotNull(questionnaires);
        assertEquals(1, questionnaires.length);
        assertEquals("tribhuwansudha1973@gmail.com", questionnaires[0].getProspectEmail());
        assertEquals("Singapore / Hong Kong Live-In Caregiver", questionnaires[0].getJobTitle());
        assertEquals("Caregiver Pre-Qualification Survey", questionnaires[0].getQuestionnaireName());
        assertEquals("Are you certified for CPR / First Aid?", questionnaires[0].getQuestion());
        assertEquals("Are you certified for CPR / First Aid?", questionnaires[0].getQuestion());

        questionnaires = candidateStorage.loadCandidateQuestionnaires(
                "prospect_20130403050601_3F0JF0C25VRISFMB_INVALID");
        assertNull(questionnaires);
    }


    @Test
    public void testCandidateEmailLoad(){
        ArrayList<String> lines = new ArrayList<>(100);

        lines.add("comm_id,prospect_id,sender_email,recipient_email,cc,bcc,subject,body,sent,,,,,,,");
        lines.add("comm_20130405100518_DUST4A8YVAYF7CZV,prospect_20130405100037_UBI3F13DMXLQPFHW,sofie@activeglobalcaregiver.com,yorelle@activeglobalcaregiver.com,,,Sofie Moreels has evaluated the applicant Manasa Godati,\"Sofie Moreels has evaluated the applicant Manasa Godati using the evaluation template \"\"Caregiver interview evaluation\"\".");
        lines.add("");
        lines.add("You can review all evaluations completed for this applicant by clicking the link below:");
        lines.add("");
        lines.add("https://app.theresumator.com/app/resumes/profile/prospect_20130405100037_UBI3F13DMXLQPFHW");
        lines.add("");
        lines.add("Cheers,");
        lines.add("");
        lines.add("        - The Resumator Folks");
        lines.add("\",5/4/2013 10:05,,,,,,,");

        lines.add("comm_20130410121029_PNRVPTYKTXG9ZYBT,prospect_20130407223840_8OQ5XYCAOZSSIIBM,sofie@activeglobalcaregiver.com,yorelle@activeglobalcaregiver.com,,,Sofie Moreels has evaluated the applicant Aneeshamol Parapallithara Purushothaman,\"Sofie Moreels has evaluated the applicant Aneeshamol Parapallithara Purushothaman using the evaluation template \"\"Caregiver interview evaluation\"\".");
        lines.add("You can review all evaluations completed for this applicant by clicking the link below:");
        lines.add("https://app.theresumator.com/app/resumes/profile/prospect_20130407223840_8OQ5XYCAOZSSIIBM");
        lines.add("Cheers,");
        lines.add("        - The Resumator Folks");
        lines.add("\",10/4/2013 12:10,,,,,,,");

        lines.add("comm_20130415033300_KT9W6HUSVPXXISQZ,prospect_20130410090254_FFOPQHVASUYDW2LW,sofie@activeglobalcaregiver.com,itsmesuresh1987@gmail.com,,,your application for Live-In Caregiver in Singapore,\"Dear SURESH,");
        lines.add("thank you for your interest in Active Global Specialised Caregivers. We have successfully received your application. Would you be available for a 30 minutes telephone or Skype interview on Thursday, at 9AM IST? If this does not suit you, kindly let me know your availability. Thank you for your cooperation.");
        lines.add("Looking forward to speaking to you!");
        lines.add("Kind regards,");
        lines.add("        Sofie");
        lines.add("PS: Please note that you have applied for a job as live-in caregiver. If you are only looking for a job in a hospital, we will not be able to accommodate you at this moment.");
        lines.add("       ________________________________");
        lines.add("Sofie Moreels");
        lines.add("Active Global Specialised Caregivers");
        lines.add("Caregiving Consultant");
        lines.add("sofie@activeglobalcaregiver.com");
        lines.add("Office: +65 6536 0086");
        lines.add("Active Global Specialised Caregivers");
        lines.add("3 CHURCH STREET");
        lines.add("#16-06 SAMSUNG HUB");
        lines.add("SINGAPORE 049483");
        lines.add("EA Licence: 13C6324");
        lines.add("Registration: R1326018\",15/4/2013 3:33,,,,,,,");
        lines.add("comm_20130416165707_WBUGBWKCMP6I0VMU,prospect_20130414062246_THSPHWXD1K3JVZGC," +
                  "shinojnc@gmail.com,sofie@activeglobalcaregiver.com,,,RE:  your application for Live-In " +
                  "Caregiver in Singapore,\"Would you please arrange me a telephone call on Thursday 1.30pm." +
                  "I have sent my certificates including my registration, diploma certificates, transcript" +
                  " and experience certificates along with the copy of my passport. Would you please go through " +
                  "that. Thanking you, yours faithfully, SHINOJ NEELANKAVIL CHUMMAR.\",16/4/2013 16:57,,,,,,,");

        DataFileProcessorFactory factory = new TestDataFileProcessorFactory(lines);
        CsvCandidateStorage candidateStorage = new CsvCandidateStorage(factory);

        CandidateEmail[] emails = candidateStorage.loadCandidateEmails(
                "prospect_20130405100037_UBI3F13DMXLQPFHW");
        assertNotNull(emails);
        assertEquals(1, emails.length);
        assertEquals("sofie@activeglobalcaregiver.com", emails[0].getSenderEmail());
        assertEquals("yorelle@activeglobalcaregiver.com", emails[0].getRecipientEmail());
        assertEquals("Sofie Moreels has evaluated the applicant Manasa Godati", emails[0].getSubject());
        assertEquals("Sofie Moreels has evaluated the applicant Manasa Godati using the evaluation template \"\"Caregiver interview evaluation\"\".\n\n" +
                     "You can review all evaluations completed for this applicant by clicking the link below:\n\n" +
                     "https://app.theresumator.com/app/resumes/profile/prospect_20130405100037_UBI3F13DMXLQPFHW\n\n" +
                     "Cheers,\n\n" +
                     "        - The Resumator Folks", emails[0].getBody());


        emails = candidateStorage.loadCandidateEmails("prospect_20130410090254_FFOPQHVASUYDW2LW");

        assertNotNull(emails);
        assertEquals(1, emails.length);
        assertEquals("sofie@activeglobalcaregiver.com", emails[0].getSenderEmail());
        assertEquals("itsmesuresh1987@gmail.com", emails[0].getRecipientEmail());
        assertEquals("your application for Live-In Caregiver in Singapore", emails[0].getSubject());
        assertEquals("Dear SURESH,\n" +
                     "thank you for your interest in Active Global Specialised Caregivers. We have successfully received your application. " +
                     "Would you be available for a 30 minutes telephone or Skype interview on Thursday, at 9AM IST? " +
                     "If this does not suit you, kindly let me know your availability. Thank you for your cooperation.\n" +
                     "Looking forward to speaking to you!\n" +
                     "Kind regards,\n" +
                     "        Sofie\n" +
                     "PS: Please note that you have applied for a job as live-in caregiver. If you are only looking for a " +
                     "job in a hospital, we will not be able to accommodate you at this moment.\n" +
                     "       ________________________________\n" +
                     "Sofie Moreels\n" +
                     "Active Global Specialised Caregivers\n" +
                     "Caregiving Consultant\n" +
                     "sofie@activeglobalcaregiver.com\n" +
                     "Office: +65 6536 0086\n" +
                     "Active Global Specialised Caregivers\n" +
                     "3 CHURCH STREET\n" +
                     "#16-06 SAMSUNG HUB\n" +
                     "SINGAPORE 049483\n" +
                     "EA Licence: 13C6324\n" +
                     "Registration: R1326018", emails[0].getBody());

        emails = candidateStorage.loadCandidateEmails("prospect_20130414062246_THSPHWXD1K3JVZGC");

        assertNotNull(emails);
        assertEquals(1, emails.length);
        assertEquals("shinojnc@gmail.com", emails[0].getSenderEmail());
        assertEquals("sofie@activeglobalcaregiver.com", emails[0].getRecipientEmail());
        assertEquals("RE:  your application for Live-In Caregiver in Singapore", emails[0].getSubject());
        assertEquals("Would you please arrange me a telephone call on Thursday 1.30pm.I have sent " +
                     "my certificates including my registration, diploma certificates, transcript " +
                     "and experience certificates along with the copy of my passport. Would you " +
                     "please go through that. Thanking you, yours faithfully, " +
                     "SHINOJ NEELANKAVIL CHUMMAR.", emails[0].getBody());

        emails = candidateStorage.loadCandidateEmails(
                "prospect_20130403050601_3F0JF0C25VRISFMB_INVALID");
        assertNull(emails);
    }

    @Test
    public void testCandidateEvaluationLoad(){

        ArrayList<String> lines = new ArrayList<>(100);

        lines.add("prospect_id,prospect_first_name,prospect_last_name,evaluation_date,evaluation_name,evaluation_note,skill_name,skill_weight,skill_rating,rating_comment");

        lines.add("prospect_20131207024532_MVIOBYNRBFSZNTFY,Smitha,Tiwari,2013-12-09,\"Caregiver interview evaluation\",\"Smitha Thiwari is 33 years old diploma nurse with nearly 12 years of experience.");
        lines.add("She has experience in general surgery and maternity.");
        lines.add("She can take care of any type of patients.");
        lines.add("Agrees for a salary of 850 SGD.");
        lines.add("Available only by first or second week of March.");
        lines.add("");
        lines.add("\"\"Her motive is to give complete care to a  single patient\"\".\",\"Understands and agrees with the requirements of being a Live-in Caregiver\",15,15,\"Understands and agrees that she has to stay at home and take care of one patient.\"");

        lines.add("prospect_20131207024532_MVIOBYNRBFSZNTFY,Smitha,Tiwari,2013-12-09,\"Caregiver interview evaluation\",\"Smitha Thiwari is 33 years old diploma nurse with nearly 12 years of experience.");
        lines.add("She has experience in general surgery and maternity.");
        lines.add("She can take care of any type of patients.");
        lines.add("Agrees for a salary of 850 SGD.");
        lines.add("Available only by first or second week of March.");
        lines.add("");
        lines.add("\"\"Her motive is to give complete care to a  single patient\"\".\",\"Motivation for being a caregiver\",20,20,\"She says while she was working in the maternity ward,there was a lady in the labour room ,who was fully dilated and was informed to doctor as only doctors were allowed to take delivery,But the doctor didn't attend on time so she has to carry out the delivery .So the lady and child was saved and were very thankful to her.\"");

        lines.add("prospect_20131207024532_MVIOBYNRBFSZNTFY,Smitha,Tiwari,2013-12-09,\"Caregiver interview evaluation\",\"Smitha Thiwari is 33 years old diploma nurse with nearly 12 years of experience.");
        lines.add("She has experience in general surgery and maternity.");
        lines.add("She can take care of any type of patients.");
        lines.add("Agrees for a salary of 850 SGD.");
        lines.add("Available only by first or second week of March.");
        lines.add("");
        lines.add("\"\"Her motive is to give complete care to a  single patient\"\".\",\"Interpersonal skills in dealing with clients\",15,12,\"She knows the communication skills and stresses the importance of respecting other religions.\"");

        lines.add("prospect_20131207024532_MVIOBYNRBFSZNTFY,Smitha,Tiwari,2013-12-09,\"Caregiver interview evaluation\",\"Smitha Thiwari is 33 years old diploma nurse with nearly 12 years of experience.");
        lines.add("She has experience in general surgery and maternity.");
        lines.add("She can take care of any type of patients.");
        lines.add("Agrees for a salary of 850 SGD.");
        lines.add("Available only by first or second week of March.");
        lines.add("");
        lines.add("\"\"Her motive is to give complete care to a  single patient\"\".\",\"Nursing hands-on experience and knowledge\",20,16,\"Very good.When asked about taking care of a patient who had fallen down,she told about checking response,observation of vital signs and injury, and shifting with support.Regarding care of patient with catheter ,she mentioned the points like cleaning from inside out,emptying the bag and maintaining the level of the bag.About prevention of bed sore she stressed the points like changing position,dry clean bed sheet,ambulation and exercise etc.\"");

        lines.add("prospect_20131207024532_MVIOBYNRBFSZNTFY,Smitha,Tiwari,2013-12-09,\"Caregiver interview evaluation\",\"Smitha Thiwari is 33 years old diploma nurse with nearly 12 years of experience.");
        lines.add("She has experience in general surgery and maternity.");
        lines.add("She can take care of any type of patients.");
        lines.add("Agrees for a salary of 850 SGD.");
        lines.add("Available only by first or second week of March.");
        lines.add("");
        lines.add("\"\"Her motive is to give complete care to a  single patient\"\".\",\"Commitment for the job\",20,20,\"She says unless there is extreme emergency she does not want to travel.\"");

        lines.add("prospect_20131207024532_MVIOBYNRBFSZNTFY,Smitha,Tiwari,2013-12-09,\"Caregiver interview evaluation\",\"Smitha Thiwari is 33 years old diploma nurse with nearly 12 years of experience.");
        lines.add("She has experience in general surgery and maternity.");
        lines.add("She can take care of any type of patients.");
        lines.add("Agrees for a salary of 850 SGD.");
        lines.add("Available only by first or second week of March.");
        lines.add("");
        lines.add("\"\"Her motive is to give complete care to a  single patient\"\".\",\"Can understand & communicate in English\",10,8,\"Understand and communicate English very well.\"");

        lines.add("prospect_20160705025611_FBS501RPMU21FSTQ,Pranav,Patel,2013-04-22,\"Caregiver interview evaluation\",\"Overall, a perfect candidate for the job: motivated, caring personality, OK with the requirements.");
        lines.add("Happy to take care of any type of patient, can multitask: wound care, ENT (ear, nose, throat), cancer. No experience with burns.");
        lines.add("Happy with 850~1000 SGD/month.");
        lines.add("In Malaysia for aprox 3 months.\",\"Understands and agrees with the requirements of being a Live-in Caregiver\",15,20,\"100% OK.\"");

        lines.add("prospect_20130407223840_8OQ5XYCAOZSSIIBM,Aneeshamol,\"Parapallithara Purushothaman\",2013-04-10,\"Caregiver interview evaluation\"," +
                  "\"Overall, I had a very good feeling about Aneeshamol. She sounds genuinely interested in the job and will care for her client as if it was her own family.\"," +
                  "\"Understands and agrees with the requirements of being a Live-in Caregiver\",15,20," +
                  "\"Went over all the restrictions with her - even though she recently got married, she is ok with everything.\"");


        DataFileProcessorFactory factory = new TestDataFileProcessorFactory(lines);
        CsvCandidateStorage candidateStorage = new CsvCandidateStorage(factory);

        CandidateEvaluation[] evaluations = candidateStorage.loadCandidateEvaluations(
                "prospect_20131207024532_MVIOBYNRBFSZNTFY");
        assertNotNull(evaluations);
        assertEquals(6, evaluations.length);

        assertEquals("\"Caregiver interview evaluation\"", evaluations[0].getEvaluationName());
        assertEquals("Smitha Thiwari is 33 years old diploma nurse with nearly 12 years of experience.\n" +
                     "She has experience in general surgery and maternity.\n" +
                     "She can take care of any type of patients.\n" +
                     "Agrees for a salary of 850 SGD.\n" +
                     "Available only by first or second week of March.\n" +
                     "\n" +
                     "\"\"Her motive is to give complete care to a  single patient\"\".", evaluations[0].getEvaluationNote());

        assertEquals("\"Understands and agrees with the requirements of being a Live-in Caregiver\"", evaluations[0].getSkillName());
        assertEquals("15", evaluations[0].getSkillRating());
        assertEquals("15", evaluations[0].getSkillWeight());
        assertEquals("\"Understands and agrees that she has to stay at home and take care of one patient.\"", evaluations[0].getRatingComment());

        evaluations = candidateStorage.loadCandidateEvaluations(
                "prospect_20130407223840_8OQ5XYCAOZSSIIBM");
        assertNotNull(evaluations);
        assertEquals(1, evaluations.length);

        assertEquals(
                "Overall, I had a very good feeling about Aneeshamol. She sounds genuinely interested " +
                "in the job and will care for her client as if it was her own family.",
                evaluations[0].getEvaluationNote());


        evaluations = candidateStorage.loadCandidateEvaluations(
                "prospect_20130403050601_3F0JF0C25VRISFMB_INVALID");
        assertNull(evaluations);
    }


    @Test
    public void testCandidateFeedbackLoad(){

        ArrayList<String> lines = new ArrayList<>(100);

        lines.add("prospect_id,prospect_first_name,prospect_last_name,prospect_email,feedback_text");

        lines.add(",,,,\"Not available on scheduled interview time - several attempts\"");
        lines.add("prospect_20130403050601_3F0JF0C25VRISFMB,Sampath,Kannagi,kannagis1@yahoo.com,\"Sampath lives in Singapore. Interview in the office was scheduled on Friday, 5th April, but Sampath did not show up,     nor did she notify. She had a second chance for an interview on Monday, 8th April, but did not show up again.");
        lines.add("Since she is not reliable, she is not a good fit for the company.");
        lines.add("Sofie - 08/04/13 10.43am\"");
        lines.add(",,,,\"Might be overqualified? Is a master in nursing and says in her rйsumй that she is looking for \"\"advanced health care\"\" or to obtain a lecturer position or clinical practitioner role.");
        lines.add("Sofie - 08/04/13 12.58pm\"");
        lines.add(",,,,\"Is interested to work in nursing home or hospital, not home situation.\"");
        lines.add("Sofie - 08/04/13 7.48PM\"");
        lines.add("prospect_20130407025150_LCR7BWIPCIPSSLI3,Simranjit,KAUR`,simranjit9891@gmail.com,\"If possible, asks for female client. Works with surgery patients now.\"");

        DataFileProcessorFactory factory = new TestDataFileProcessorFactory(lines);
        CsvCandidateStorage candidateStorage = new CsvCandidateStorage(factory);

        CandidateFeedback[] feedback = candidateStorage.loadCandidateFeedback(
                "prospect_20130403050601_3F0JF0C25VRISFMB");
        assertNotNull(feedback);
        assertEquals(1, feedback.length);

        assertEquals("kannagis1@yahoo.com", feedback[0].getProspectEmail());
        assertEquals("\"Sampath lives in Singapore. Interview in the office was scheduled on Friday, " +
                     "5th April, but Sampath did not show up,     nor did she notify. " +
                     "She had a second chance for an interview on Monday, 8th April, but did not show up again.\n" +
                     "Since she is not reliable, she is not a good fit for the company.\n" +
                     "Sofie - 08/04/13 10.43am\"",
                feedback[0].getFeedbackText());

        feedback = candidateStorage.loadCandidateFeedback(
                "prospect_20130403050601_3F0JF0C25VRISFMB_INVALID");
        assertNull(feedback);
    }

    @Test
    public void testCandidateQuestionnaireLoad(){
        ArrayList<String> lines = new ArrayList<>(3);

        lines.add("prospect_id,prospect_first_name,prospect_last_name,prospect_email,job_title,questionnaire_name,question,answer");

        lines.add("prospect_20130409074101_JGXT1RGKX97V5WIF,Sudhatai,Gangadhar Tribhuwan,tribhuwansudha1973@gmail.com,Singapore / Hong Kong Live-In Caregiver,Caregiver Pre-Qualification Survey,Are you certified for CPR / First Aid?,Yes");
        lines.add("prospect_20160104115559_HFLR15PO1JRKP0KH,Jovelle,Caluyong,ellevoj1720@yahoo.com,Singapore / Hong Kong Live-In Caregiver,Caregiver Pre-Qualification Survey,\"Assuming we offer you a job, and we have already secured a visa/worked Permit for you, how soon can you come to Singapore? Please be truthful and realistic, depending on your current notice for resignation and personal commitments. Please note that most of our clients are in a rush to get a caregiver and are not ready to wait a month for you.");
        lines.add("At the same time, if you commit to a certain availability it means you are able to come in this timeframe. This is IMPORTANT.\",Immediate");

        DataFileProcessorFactory factory = new TestDataFileProcessorFactory(lines);
        CsvCandidateStorage candidateStorage = new CsvCandidateStorage(factory);

        CandidateQuestionnaire[] questionnaires = candidateStorage.loadCandidateQuestionnaires(
                "prospect_20130409074101_JGXT1RGKX97V5WIF");
        assertNotNull(questionnaires);
        assertEquals(1, questionnaires.length);

        assertEquals("tribhuwansudha1973@gmail.com", questionnaires[0].getProspectEmail());
        assertEquals("Are you certified for CPR / First Aid?",questionnaires[0].getQuestion());
        assertEquals("Yes",questionnaires[0].getAnswer());

        questionnaires = candidateStorage.loadCandidateQuestionnaires(
                "prospect_20160104115559_HFLR15PO1JRKP0KH");
        assertNotNull(questionnaires);
        assertEquals(1, questionnaires.length);

        assertEquals("ellevoj1720@yahoo.com", questionnaires[0].getProspectEmail());
        assertEquals("Assuming we offer you a job, and we have already secured a visa/worked Permit for you, " +
                     "how soon can you come to Singapore? Please be truthful and realistic, depending on your current " +
                     "notice for resignation and personal commitments. Please note that most of our clients are in a " +
                     "rush to get a caregiver and are not ready to wait a month for you.\nAt the same time, if " +
                     "you commit to a certain availability it means you are able to come in this timeframe. " +
                     "This is IMPORTANT.",questionnaires[0].getQuestion());
        assertEquals("Immediate",questionnaires[0].getAnswer());

        questionnaires = candidateStorage.loadCandidateQuestionnaires(
                "prospect_20130403050601_3F0JF0C25VRISFMB_INVALID");
        assertNull(questionnaires);

    }


    @Test
    public void testEvaluationName() throws IOException {
        String evaluationsFileName = "D:\\projects\\toptal\\agcs\\agsc-portal\\data\\data_records\\candidate_evaluations.csv";

        DataFileProcessorFactory factory = dataKey -> {
            if (dataKey.equals("candidates.evaluations.filename")) {
                return new DefaultDataFileProcessor(new File(evaluationsFileName));
            }
            return null;
        };
        CsvCandidateStorage candidateStorage = new CsvCandidateStorage(factory);

        BufferedReader prospectIdReader = new BufferedReader(new FileReader("D:\\projects\\toptal\\agcs\\agsc-portal\\data\\data_records\\prospect_ids.txt"));
        String prospectId;
        int count = 0;
        try {
            while ((prospectId = prospectIdReader.readLine()) != null){
                count++;
                final CandidateEvaluation[] candidateEvaluations = candidateStorage.loadCandidateEvaluations(
                        prospectId);
                if(candidateEvaluations == null){
                    System.out.println("No evaluation found for prospect:"+prospectId);
                    continue;
                }
                for(CandidateEvaluation evaluation : candidateEvaluations){
                    assertEquals("\"Caregiver interview evaluation\"", evaluation.getEvaluationName());
                    System.out.println("evaluation.getSkillName() = " + evaluation.getSkillName());
                    assertTrue(evaluation.getSkillName().startsWith("\"Understands")
                               || evaluation.getSkillName().startsWith("\"Motivation")
                               || evaluation.getSkillName().startsWith("\"Commitment")
                               || evaluation.getSkillName().startsWith("\"Nursing")
                               || evaluation.getSkillName().startsWith("\"Can")
                               || evaluation.getSkillName().startsWith("\"Interpersonal"));
                }
            }
        }
        finally {
            System.out.println("count = " + count);
        }
    }

    @Test
    public void testQuestionnaireName() throws IOException {
        String evaluationsFileName = "D:\\projects\\toptal\\agcs\\agsc-portal\\data\\data_records\\candidate_questionnaires.csv";

        DataFileProcessorFactory factory = dataKey -> {
            if (dataKey.equals("candidates.questionnaires.filename")) {
                return new DefaultDataFileProcessor(new File(evaluationsFileName));
            }
            return null;
        };
        CsvCandidateStorage candidateStorage = new CsvCandidateStorage(factory);

        BufferedReader prospectIdReader = new BufferedReader(new FileReader("D:\\projects\\toptal\\agcs\\agsc-portal\\data\\data_records\\prospect_ids.txt"));
        String prospectId;
        int count = 0;
        HashSet<String> names = new HashSet<>();
        try {
            while ((prospectId = prospectIdReader.readLine()) != null){
                count++;
                final CandidateQuestionnaire[] candidateQuestionnaires = candidateStorage.loadCandidateQuestionnaires(
                        prospectId);
                if(candidateQuestionnaires == null){
                    System.out.println("No evaluation found for prospect:"+prospectId);
                    continue;
                }
                for(CandidateQuestionnaire questionnaire : candidateQuestionnaires){
                    names.add("----"+questionnaire.getQuestion());
                }
            }
        }
        finally {
            System.out.println("count = " + count);
            System.out.println("questions: ");
            for(String name : names){
                System.out.println(name);
            }
        }
    }
}
