package com.toptal.migration;

import com.cloudaxis.agsc.portal.model.Caregiver;
import com.toptal.migration.model.CandidateDocument;
import com.toptal.migration.model.CandidateDocumentMapping;
import com.toptal.migration.model.CandidateEmail;
import com.toptal.migration.model.CandidateEvaluation;
import com.toptal.migration.model.CandidateFeedback;
import com.toptal.migration.model.CandidateNote;
import com.toptal.migration.model.CandidateQuestionnaire;
import com.toptal.migration.model.DateUtils;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <code>CsvCandidateStorage</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class CsvCandidateStorage implements CandidateStorage {
    private final DataFileProcessorFactory dataFileProcessorFactory;
    private HashMap<String, ArrayList<CandidateDocument>> candidateDocuments;
    private HashMap<String, ArrayList<CandidateDocument>> candidateFiles;
    private HashMap<String, ArrayList<CandidateEvaluation>> candidateEvaluations;
    private HashMap<String, ArrayList<CandidateFeedback>> candidateFeedback;
    private HashMap<String, ArrayList<CandidateNote>> candidateNotes;
    private HashMap<String, ArrayList<CandidateQuestionnaire>> candidateQuestionnaires;
    private HashMap<String, ArrayList<CandidateEmail>> candidateEmails;
    private HashMap<String, Caregiver> candidates;
    private HashMap<String, CandidateDocument> candidateResumes;
    private HashMap<String, Map<String, CandidateDocumentMapping>> candidateDocumentMappings;
    private HashMap<Class, Integer> errors;

    public CsvCandidateStorage(DataFileProcessorFactory dataFileProcessorFactory){
        this.dataFileProcessorFactory = dataFileProcessorFactory;
    }

    @Override
    public Caregiver loadCandidate(String prospectId) {
        if(candidates == null){
            loadCandidates();
        }
        return candidates.get(prospectId);
    }

    private void loadCandidates() {
        candidates = new HashMap<>();
        loadFile("candidates.filename", candidates, this::defaultInputParse, this::createCandidate);
    }

    private Caregiver createCandidate(String[] fields) {
        Caregiver candidate = new Caregiver();
        candidate.setFirstName(fields[1]);
        candidate.setLastName(fields[2]);
        candidate.setEmail(fields[3]);
        candidate.setMobile(fields[8]);
        candidate.setDateApplied(DateUtils.parse(fields[21], new SimpleDateFormat("yyyy-MM-dd")));
        return candidate;
    }

    private <T> void loadFile(String fileKey, Map map,
                              InputParser<T> inputParser, Creator<T> creator){


        try (DataFileProcessor dataFileProcessor = dataFileProcessorFactory.getDataFileProcessor(fileKey)) {

            dataFileProcessor.nextLine();// skip header
            inputParser.parse(dataFileProcessor, creator, map);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading file:"+fileKey);
        }
    }

    private <T> void defaultInputParse(DataFileProcessor dataFileProcessor, Creator<T> creator, Map<String, T> map)
            throws IOException {
        while(dataFileProcessor.hasNextLine()){
            String line = dataFileProcessor.nextLine();
            String[] fields = line.split(",");
            if(!fields[0].trim().isEmpty()) {
                T entry = creator.create(fields);
                map.put(fields[0].trim(), entry);
            }
        }
    }

    private <T> void documentMapperParse(DataFileProcessor dataFileProcessor, Creator<T> creator, Map<String, T> map)
            throws IOException {
        while(dataFileProcessor.hasNextLine()){
            String line = dataFileProcessor.nextLine();
            String[] fields = line.split(";");
            if(!fields[0].trim().isEmpty()) {
                T entry = creator.create(fields);
                map.put(fields[0].trim(), entry);
            }
        }
    }

    private <T> void joinInputParser(DataFileProcessor dataFileProcessor,
                                     Creator<T> creator,
                                     Map<String, ArrayList<T>> map) throws IOException {
        while(dataFileProcessor.hasNextLine()){
            String[] fields = dataFileProcessor.nextLine().split(",");
            createEntity(fields, map, creator);
        }
    }

    private <T> void createEntity(String[] fields,
                                  Map<String, ArrayList<T>> map,
                                  Creator<T> creator) {
        createEntity(fields, 0, map, creator);
    }

    private <T> void createEntity(String[] fields,
                                   int prospectIdIndex,
                                   Map<String, ArrayList<T>> map,
                                   Creator<T> creator) {
        if(fields != null){
            try {
                String prospectId = fields[prospectIdIndex].trim();
                if(!prospectId.isEmpty()) {
                    T entry = creator.create(fields);
                    ArrayList<T> values = map.get(prospectId);
                    if(values == null){
                        values = new ArrayList<>();
                        map.put(prospectId, values);
                    }
                    values.add(entry);
                }
            }
            catch (RuntimeException e) {
                System.out.println("Error creating entity: "+ Arrays.toString(fields));
                System.out.println("Error = " + e);

                if(errors == null){
                    errors = new HashMap<>();
                }
                Integer errorCount = errors.get(creator.getClass());
                if(errorCount == null){
                    errorCount = 0;
                }
                errorCount += 1;
                errors.put(creator.getClass(), errorCount);
            }
        }
    }

    @Override
    public CandidateDocument[] loadCandidateDocuments(String prospectId) {
        if(candidateDocuments == null){
            loadCandidateDocuments();
        }
        ArrayList<CandidateDocument> prospectDocuments = candidateDocuments.get(prospectId);
        return prospectDocuments == null
               ? null : prospectDocuments.toArray(new CandidateDocument[prospectDocuments.size()]);
    }

    private void loadCandidateDocuments() {
        candidateDocuments = new HashMap<>();
        loadFile("candidates.documents.filename", candidateDocuments, this::joinInputParser,
                     CandidateDocument::new);
    }

    @Override
    public CandidateEvaluation[] loadCandidateEvaluations(String prospectId) {
        if(candidateEvaluations == null){
            loadCandidateEvaluations();
        }
        ArrayList<CandidateEvaluation> evaluations = candidateEvaluations.get(prospectId);
        return evaluations == null
               ? null : evaluations.toArray(new CandidateEvaluation[evaluations.size()]);
    }

    private void loadCandidateEvaluations() {
        candidateEvaluations = new HashMap<>();
        loadFile("candidates.evaluations.filename", candidateEvaluations,
                 this::evaluationParser, CandidateEvaluation::new);
    }

    private void evaluationParser(DataFileProcessor dataFileProcessor,
                                  Creator<CandidateEvaluation> evaluationCreator,
                                  Map<String, ArrayList<CandidateEvaluation>> map)
            throws IOException {

        LinkedList<String> fieldList = new LinkedList<>();
        while(dataFileProcessor.hasNextLine()){
            String line = dataFileProcessor.nextLine();

            if(line.startsWith("prospect_")){
                if(!fieldList.isEmpty()){
                    Collections.reverse(fieldList);
                    createEntity(fieldList.toArray(new String[fieldList.size()]), map, evaluationCreator);
                    fieldList.clear();
                }
                splitUpTo(fieldList, line, 5);

                String lastField = fieldList.pop();
                int index = lastField.indexOf("\",");// some lines have all in one line
                if(index >= 0){
                    String lastCell = lastField.substring(0, index);
                    fieldList.push(lastCell);

                    line = lastField.substring(index + 2);
                    parseEvaluationAfterNote(fieldList, line);
                }
                else{
                    fieldList.push(lastField);
                }
            }
            else{
                processContinuousRowsForEvaluations(fieldList, line);
            }
        }
        if(!fieldList.isEmpty()) {
            Collections.reverse(fieldList);
            createEntity(fieldList.toArray(new String[fieldList.size()]), map, evaluationCreator);
        }
    }

    private void processContinuousRowsForEvaluations(LinkedList<String> fieldList, String line) {
        if(fieldList.size() == 6) { // at the evaluation note cell
            int cellEndIndex = line.indexOf("\",");
            if (cellEndIndex < 0) {
                appendToLast(fieldList, line);
            } else {
                String lastCellPart = line.substring(0, cellEndIndex);
                appendToLast(fieldList, lastCellPart);

                line = line.substring(cellEndIndex + 2);
                parseEvaluationAfterNote(fieldList, line);

            }
        } else if(fieldList.size() == 10){ // this is last cell (size == 10) - just append to the last cell
            appendToLast(fieldList, line);
        }
        else{
            System.out.println("Ignoring line:"+line);
            System.out.println("fields:"+fieldList);
        }
    }

    private void processContinuousRowsForQuestionnaire(LinkedList<String> fieldList, String line) {
        if(fieldList.size() == 7) { // at the question cell
            int cellEndIndex = line.indexOf("\",");
            if (cellEndIndex < 0) {
                appendToLast(fieldList, line);
            } else {
                String lastCellPart = line.substring(0, cellEndIndex);
                appendToLast(fieldList, lastCellPart);

                line = line.substring(cellEndIndex + 2);
                fieldList.push(line);
            }
        } else if(fieldList.size() == 8){ // this is last cell (size == 8) - just append to the last cell
            appendToLast(fieldList, line);
        }
        else{
            System.out.println("Ignoring line:"+line);
            System.out.println("fields:"+fieldList);
        }
    }

    private void parseEvaluationAfterNote(LinkedList<String> fieldList, String line) {
        // parse it manually, as last cell might contain commas
        int commaIndex = line.indexOf(",");
        while (commaIndex > 0) {
            if (fieldList.size() < 9) {
                fieldList.push(line.substring(0, commaIndex));
            } else {
                fieldList.push(line); // the last cell can also span multiple rows and contain commas
                break;
            }

            line = line.substring(commaIndex + 1);
            commaIndex = line.indexOf(",");
        }
        fieldList.push(line);// what ever left in it
    }

    private void pushLineToStack(LinkedList<String> fieldList, String line) {
        String[] fields = line.split(",");
        for(String field : fields){
            fieldList.push(field);
        }
    }

    private void appendToLast(LinkedList<String> fieldList, String line) {
        String last = fieldList.pop();
        fieldList.push(last +"\n"+ line);
    }

    @Override
    public CandidateFeedback[] loadCandidateFeedback(String prospectId) {
        if(candidateFeedback == null){
            loadCandidateFeedback();
        }
        final ArrayList<CandidateFeedback> prospectFeedback = candidateFeedback.get(prospectId);
        return prospectFeedback == null
               ? null : prospectFeedback.toArray(new CandidateFeedback[prospectFeedback.size()]);
    }

    private void loadCandidateFeedback() {
        candidateFeedback = new HashMap<>();
        loadFile("candidates.feedback.filename", candidateFeedback, this::feedbackParser,
                 CandidateFeedback::new);
    }

    private void feedbackParser(DataFileProcessor dataFileProcessor,
                                  Creator<CandidateFeedback> feedbackCreator,
                                  Map<String, ArrayList<CandidateFeedback>> map)
            throws IOException {

        LinkedList<String> fieldList = new LinkedList<>();
        while(dataFileProcessor.hasNextLine()){
            String line = dataFileProcessor.nextLine();

            if(line.startsWith("prospect_")){
                if(!fieldList.isEmpty()){
                    Collections.reverse(fieldList);
                    createEntity(fieldList.toArray(new String[fieldList.size()]), map, feedbackCreator);
                    fieldList.clear();
                }
                splitUpTo(fieldList, line, 4);
            }
            else if(line.startsWith(",,,,")){
                if(!fieldList.isEmpty()){
                    Collections.reverse(fieldList);
                    createEntity(fieldList.toArray(new String[fieldList.size()]), map, feedbackCreator);
                    fieldList.clear();
                }
                // skip those lines - they are tied to noone
            }
            else{
                if(!fieldList.isEmpty()) {
                    appendToLast(fieldList, line);
                }
            }
        }
        if(!fieldList.isEmpty()) {
            Collections.reverse(fieldList);
            createEntity(fieldList.toArray(new String[fieldList.size()]), map, feedbackCreator);
        }
    }

    /**
     * Splits line into fields using commas up to fieldCount. When reached fieldCount, the remaining
     * of the line is added to the list
     * @param fieldList the list to populate
     * @param line the line to split
     * @param fieldCount the number of fields, after that remaining line added to the list as single field.
     */
    private void splitUpTo(LinkedList<String> fieldList, String line, int fieldCount) {
        for(int i = 0; i < fieldCount; i++){
            int index = line.indexOf(",");
            if(index < 0){
                fieldList.push(line);
                line = null;
                break;
            }
            String field = line.substring(0, index);
            fieldList.push(field);
            line = line.substring(index+1);
        }

        if(line != null){
            fieldList.push(line);
        }
    }

    @Override
    public CandidateDocument[] loadCandidateFiles(String prospectId) {
        if(candidateFiles == null){
            loadCandidateFiles();
        }
        ArrayList<CandidateDocument> prospectFiles = candidateFiles.get(prospectId);
        return prospectFiles == null
               ? null : prospectFiles.toArray(new CandidateDocument[prospectFiles.size()]);
    }

    private void loadCandidateFiles() {
        candidateFiles = new HashMap<>();
        loadFile("candidates.files.filename", candidateFiles, this::joinInputParser, CandidateDocument::new);
    }

    @Override
    public CandidateNote[] loadCandidateNotes(String prospectId) {
        if(candidateNotes == null){
            loadCandidateNotes();
        }
        return candidateNotes.get(prospectId).toArray(new CandidateNote[candidateNotes.get(prospectId)
                .size()]);
    }

    private void loadCandidateNotes() {
        candidateNotes = new HashMap<>();
        loadFile("candidates.notes.filename", candidateNotes, this::joinInputParser,
                 CandidateNote::new);
    }

    @Override
    public CandidateQuestionnaire[] loadCandidateQuestionnaires(String prospectId) {
        if(candidateQuestionnaires == null){
            loadCandidateQuestionnaires();
        }
        ArrayList<CandidateQuestionnaire> questionnaires = candidateQuestionnaires.get(
                prospectId);
        return questionnaires == null
               ? null : questionnaires.toArray(new CandidateQuestionnaire[questionnaires.size()]);
    }

    private void loadCandidateQuestionnaires() {
        candidateQuestionnaires = new HashMap<>();
        loadFile("candidates.questionnaires.filename", candidateQuestionnaires, this::questionnaireParser,
                 CandidateQuestionnaire::new);
    }

    private void questionnaireParser(DataFileProcessor dataFileProcessor,
                                  Creator<CandidateQuestionnaire> evaluationCreator,
                                  Map<String, ArrayList<CandidateQuestionnaire>> map)
            throws IOException {

        LinkedList<String> fieldList = new LinkedList<>();
        while(dataFileProcessor.hasNextLine()){
            String line = dataFileProcessor.nextLine();

            if(line.startsWith("prospect_")){
                if(!fieldList.isEmpty()){
                    Collections.reverse(fieldList);
                    createEntity(fieldList.toArray(new String[fieldList.size()]), map, evaluationCreator);
                    fieldList.clear();
                }
                splitQuestionnaireRecord(fieldList, line);
            }
            else{
                processContinuousRowsForQuestionnaire(fieldList, line);
            }
        }
        if(!fieldList.isEmpty()) {
            Collections.reverse(fieldList);
            createEntity(fieldList.toArray(new String[fieldList.size()]), map, evaluationCreator);
        }
    }

    private void splitQuestionnaireRecord(LinkedList<String> fieldList, String line) {
        for(int i = 0; i < 6; i++){
            int index = line.indexOf(",");
            if(index < 0){
                fieldList.push(line);
                line = null;
                break;
            }
            String field = line.substring(0, index);
            fieldList.push(field);
            line = line.substring(index+1);
        }

        if(line != null){
            if(!line.startsWith("\"")){
                int index = line.indexOf(",");
                if(index >= 0){
                    String q = line.substring(0, index);
                    String a = line.substring(index+1);
                    fieldList.push(q);
                    fieldList.push(a);
                }
                else{
                    fieldList.push(line);
                }
            }
            else{
                int index = line.indexOf("\",");
                if(index >= 0){
                    String q = line.substring(0, index);
                    String a = line.substring(index+2);
                    fieldList.push(q);
                    fieldList.push(a);
                }
                else{
                    fieldList.push(line);
                }
            }
        }
    }

    @Override
    public CandidateEmail[] loadCandidateEmails(String prospectId) {
        if(candidateEmails == null){
            loadCandidateEmails();
        }
        ArrayList<CandidateEmail> emails = candidateEmails.get(prospectId);
        return emails == null
               ? null : emails.toArray(new CandidateEmail[emails.size()]);
    }

    @Override
    public CandidateDocument loadCandidateResume(String prospectId) {
        if(candidateResumes == null){
            loadCandidateResumes();
        }
        return candidateResumes.get(prospectId);
    }

    @Override
    public Map<String, CandidateDocumentMapping> loadCandidateDocumentMappings(String prospectId)
    {
        if(candidateDocumentMappings == null){
            loadCandidateMappings();
        }
        return candidateDocumentMappings.get(prospectId);
    }

    private void loadCandidateMappings()
    {
        candidateDocumentMappings = new HashMap<>();
        loadFile("candidates.mappings.filename", candidateDocumentMappings, this::documentMapperParse, this::createCandidateDocumentMapping);
    }

    private void loadCandidateResumes() {
        candidateResumes = new HashMap<>();
        loadFile("candidates.filename", candidateResumes, this::defaultInputParse, this::createCandidateResume);
    }

    private CandidateDocument createCandidateResume(String[] fields) {
        return new CandidateDocument(new String[]{fields[0], fields[23]});
    }

    private Map<String, CandidateDocumentMapping> createCandidateDocumentMapping(String[] fields) {
        Map<String, CandidateDocumentMapping> candidateMappings = candidateDocumentMappings.get(fields[0]);
        if(candidateMappings == null){
            candidateMappings = new HashMap<>(10);
        }
        candidateMappings.put(fields[4], new CandidateDocumentMapping(fields));
        return candidateMappings;
    }

    private void loadCandidateEmails() {
        candidateEmails = new HashMap<>();
        loadFile("candidates.emails.filename", candidateEmails, this::emailParser, CandidateEmail::new);
    }

    private void emailParser(DataFileProcessor dataFileProcessor,
                             Creator<CandidateEmail> emailCreator,
                             Map<String, ArrayList<CandidateEmail>> map) throws IOException {
        LinkedList<String> fieldList = new LinkedList<>();
        Pattern pattern = Pattern.compile("comm_(.*),prospect_(.*),(.*)");
        int errorCount = 0;
        int totalCount = 0;
        while(dataFileProcessor.hasNextLine()){
            String line = dataFileProcessor.nextLine();

            Matcher matcher = pattern.matcher(line);

            try {
                if(matcher.matches()){
                    if(!fieldList.isEmpty()){
                        Collections.reverse(fieldList);
                        createEntity(fieldList.toArray(new String[fieldList.size()]), 1, map, emailCreator);
                        fieldList.clear();
                        totalCount++;
                    }
                    splitUpTo(fieldList, line, 7);
                    handleLastEmailField(fieldList);
                }
                else if (!fieldList.isEmpty()){
                    processContinuousRowsForEmail(fieldList, line);
                }
            }
            catch (Throwable e) {
//                System.out.println("error processing candidate email:"+fieldList);
//                System.out.println("error = " + e);
                errorCount++;
            }
        }
        System.out.println("Done processing candidate emails, errorCount = " + errorCount);
        if(!fieldList.isEmpty()) {
            Collections.reverse(fieldList);
            createEntity(fieldList.toArray(new String[fieldList.size()]), 1, map, emailCreator);
            totalCount++;
        }
        System.out.println("Errors creating email entity:"+
                           (errors == null ? 0 : errors.get(emailCreator.getClass())));

        System.out.println("out of:"+totalCount);
    }

    private void handleLastEmailField(LinkedList<String> fieldList) {
        if(fieldList.size() == 8){
            String lastField = fieldList.pop();

            if(lastField.contains("\",")){
                int index = lastField.lastIndexOf("\",");
                if(index > 0){
                    if(lastField.charAt(index - 1) != '"'){
                        String line = lastField;
                        lastField = line.substring(0, index);
                        fieldList.push(lastField);

                        line = line.substring(index + 2);

                        pushLineToStack(fieldList, line);
                    }
                }
            }
            else{
                fieldList.push(lastField);
            }
        }

    }

    private void processContinuousRowsForEmail(LinkedList<String> fieldList, String line) {
        if(fieldList.size() < 8){
            int cellEndIndex = line.indexOf("\",");
            if (cellEndIndex < 0) {
                appendToLast(fieldList, line);
            } else {
                String lastCellPart = line.substring(0, cellEndIndex);
                appendToLast(fieldList, lastCellPart);

                line = line.substring(cellEndIndex + 2);
                splitUpTo(fieldList, line, 7);
            }
        }
        else if(fieldList.size() == 8) { // at the email body cell
            int cellEndIndex = line.indexOf("\"\",");
            if(cellEndIndex >= 0){
                appendToLast(fieldList, line);
            }
            else {
                cellEndIndex = line.indexOf("\",");
                if (cellEndIndex < 0) {
                    appendToLast(fieldList, line);
                } else {
                    String lastCellPart = line.substring(0, cellEndIndex);
                    appendToLast(fieldList, lastCellPart);

                    line = line.substring(cellEndIndex + 2);
                    pushLineToStack(fieldList, line);
                }
            }
        } else{
            fieldList.clear();
            throw new RuntimeException("Invalid file content, line:"+line);
        }
    }

    @FunctionalInterface
    private interface Creator<T> {
        T create(String[] fields);
    }

    @FunctionalInterface
    private interface InputParser<T>{
        void parse(DataFileProcessor dataFileProcessor, Creator<T> creator, Map map)
                throws IOException;
    }
}
