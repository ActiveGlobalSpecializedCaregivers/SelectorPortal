package com.cloudaxis.agsc.portal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudaxis.agsc.portal.dao.EvaluationDAO;
import com.cloudaxis.agsc.portal.model.EvaluationAnswer;
import com.cloudaxis.agsc.portal.model.EvaluationModel;
import com.cloudaxis.agsc.portal.model.EvaluationQuestion;
import com.cloudaxis.agsc.portal.model.EvaluationTemplate;

@Service
public class AssessmentServiceImpl implements AssessmentService {

	@Autowired
	private EvaluationDAO evaluationDAO;
	
	@Override
	public List<EvaluationModel> getEvaluationAnswerListByApplicationId(String applicationId) {
		List<EvaluationAnswer> answerList =  evaluationDAO.getEvaluationAnswerListByApplicationId(Integer.parseInt(applicationId));
		for(EvaluationAnswer answer : answerList){
			answer.setQuestionSelection(getQuestionSelectionForAnswer(answer.getQuestionSelection()));
		}
		List<EvaluationModel> evaluationModelList = getEvaluationModelList(answerList, applicationId);
		return evaluationModelList;
	}

	private String getQuestionSelectionForAnswer(String selections) {
		String selection = null;
		List<String> selectionList = getSelectionList(selections);
		if(selectionList.size() > 0){
			if(selectionList.size() == 2){
				selection = selectionList.get(0) + "(1) -> " + selectionList.get(1) + "(2)";
			}else{
				selection = "Lowest(1) -> Highest(" + selectionList.size() + ")";
			}
		}
		return selection;
	}
	
	private List<EvaluationModel> getEvaluationModelList(List<EvaluationAnswer> answerList, String applicationId) {
		List<EvaluationModel> modelList = new ArrayList<EvaluationModel>();
		Map<String, List<EvaluationAnswer>> anwserMap = new HashMap<String, List<EvaluationAnswer>>();
		for(EvaluationAnswer evaluationAnswer : answerList){
			if(anwserMap.containsKey(evaluationAnswer.getTemplateName())){
				anwserMap.get(evaluationAnswer.getTemplateName()).add(evaluationAnswer);
			}else{
				List<EvaluationAnswer> answerListByTemplateName = new ArrayList<EvaluationAnswer>();
				answerListByTemplateName.add(evaluationAnswer);
				anwserMap.put(evaluationAnswer.getTemplateName(), answerListByTemplateName);
			}
		}
		for(Map.Entry<String, List<EvaluationAnswer>> entry : anwserMap.entrySet()){
			EvaluationModel model = new EvaluationModel();
			model.setTemplateName(entry.getKey());
			model.setAnwserList(entry.getValue());
			model.setSummary(getSummaryFromAnswerList(entry.getValue()));
			model.setTemplateId(getTemplateIdFromAnswerList(entry.getValue()));
			modelList.add(model);
		}
		for(EvaluationModel model : modelList){
			model.setDivId(model.getTemplateName().replace(" ", ""));
			model.setApplicantId(applicationId);
		}
		return modelList;
	}

	private String getSummaryFromAnswerList(List<EvaluationAnswer> answerList) {
		String summary = null;
		for(EvaluationAnswer answer : answerList){
			if(answer.getQuestionid() == -1){
				summary = answer.getComment();
				break;
			}
		}
		return summary;
	}
	
	private String getTemplateIdFromAnswerList(List<EvaluationAnswer> answerList) {
		String templateId = "";
		if(answerList.size() > 0){
			templateId = answerList.get(0).getTemplateid().toString();
		}
		return templateId;
	}

	@Override
	public List<EvaluationTemplate> getTemplateList() {
		return evaluationDAO.getEvaluationTemplateList();
	}

	@Override
	public List<EvaluationQuestion> getEvaluationQuestionListByTemplateId(int templateId) {
		List<EvaluationQuestion> questionList = evaluationDAO.getEvaluationQuestionByTemplateId(templateId);
		for(EvaluationQuestion evaluationQuestion : questionList){
			evaluationQuestion.setSelectionList(getSelectionList(evaluationQuestion.getSelections()));
		}
		return questionList;
	}

	// There are different selections in the different questions, so we distinguish them by comma
	private List<String> getSelectionList(String selections) {
		List<String> selectionList = new ArrayList<String>();
		if(selections != null){
			String[] selectionArr = selections.split(",");
			for(String selection : selectionArr){
				selectionList.add(selection);
			}
		}
		return selectionList;
	}

	@Override
	public void saveEvaluationAnswerList(String applicantId, String evaluatedId, String templateId,
			String[] questionIdArr, String[] selectionArr, String[] commentArr, String summary) {
		for(int i = 0 ; i < questionIdArr.length ; i++){
			EvaluationAnswer answer = new EvaluationAnswer();
			answer.setApplicationid(Integer.parseInt(applicantId));
			answer.setEvaluatedid(Integer.parseInt(evaluatedId));
			answer.setTemplateid(Integer.parseInt(templateId));
			answer.setQuestionid(Integer.parseInt(questionIdArr[i]));
			answer.setEvaluationScore(selectionArr[i]);
			answer.setComment(commentArr[i]);
			evaluationDAO.saveEvaluationAnswer(answer);
		}
		EvaluationAnswer evalutionSummary = new EvaluationAnswer();
		evalutionSummary.setApplicationid(Integer.parseInt(applicantId));
		evalutionSummary.setEvaluatedid(Integer.parseInt(evaluatedId));
		evalutionSummary.setTemplateid(Integer.parseInt(templateId));
		evalutionSummary.setQuestionid(-1);
		evalutionSummary.setComment(summary);
		evaluationDAO.saveEvaluationAnswer(evalutionSummary);
	}

	@Override
	public List<Integer> getEvaluationTemplateIdsByApplicationId(String applicantId) {
		List<Integer> templateIdList = new ArrayList<Integer>();
		Set<Integer> set = evaluationDAO.getEvaluationTemplateIdsByApplicationId(Integer.parseInt(applicantId));
		Object[] idsArr = set.toArray();
		for(Object id : idsArr){
			templateIdList.add((Integer)id);	
		}
		return templateIdList;
	}

	@Override
	public void deleteEvaluationByApplicantIdAndTemplateId(String applicantId, String templateId) {
		evaluationDAO.deleteEvaluationByApplicantIdAndTemplateId(applicantId, templateId);
	}
	
}
 