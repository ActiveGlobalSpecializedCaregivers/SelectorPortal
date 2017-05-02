package com.cloudaxis.agsc.portal.service;

import java.util.List;

import com.cloudaxis.agsc.portal.model.EvaluationModel;
import com.cloudaxis.agsc.portal.model.EvaluationQuestion;
import com.cloudaxis.agsc.portal.model.EvaluationTemplate;

public interface AssessmentService {
	
	public List<EvaluationModel> getEvaluationAnswerListByApplicationId(String applicationId);

	public List<EvaluationTemplate> getTemplateList();
	
	public List<EvaluationQuestion> getEvaluationQuestionListByTemplateId(int templateId);

	public void saveEvaluationAnswerList(String applicantId, String evaluatedId, String templateId,
			String[] questionIdArr, String[] selectionArr, String[] commentArr, String summary);

	public List<Integer> getEvaluationTemplateIdsByApplicationId(String applicantId);

	public void deleteEvaluationByApplicantIdAndTemplateId(String applicantId, String templateId);
}
