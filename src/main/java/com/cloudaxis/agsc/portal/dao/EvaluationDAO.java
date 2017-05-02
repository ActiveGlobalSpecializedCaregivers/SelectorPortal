package com.cloudaxis.agsc.portal.dao;

import java.util.List;
import java.util.Set;

import com.cloudaxis.agsc.portal.model.EvaluationAnswer;
import com.cloudaxis.agsc.portal.model.EvaluationQuestion;
import com.cloudaxis.agsc.portal.model.EvaluationTemplate;

public interface EvaluationDAO {
	
	/**
	 * get the evaluation answer list by application id
	 */
	List<EvaluationAnswer> getEvaluationAnswerListByApplicationId(Integer applicationId);

	/**
	 * get evaluation template list for the evaluate candidate
	 */
	List<EvaluationTemplate> getEvaluationTemplateList();
	
	/**
	 * get the evaluation questions according to the template
	 */
	List<EvaluationQuestion> getEvaluationQuestionByTemplateId(int evaluationTemplateId);

	/**
	 * save user's the answer of the evaluation questions
	 */
	void saveEvaluationAnswer(EvaluationAnswer evaluationAnswer);

	/**
	 * get user's evaluation template ids 
	 */
	Set<Integer> getEvaluationTemplateIdsByApplicationId(Integer applicantId);

	/**
	 * delete user's evaluation according to template id 
	 */
	void deleteEvaluationByApplicantIdAndTemplateId(String applicantId, String templateId);
	
}
