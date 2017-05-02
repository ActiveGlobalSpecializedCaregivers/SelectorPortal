package com.cloudaxis.agsc.portal.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cloudaxis.agsc.portal.model.EvaluationAnswer;
import com.cloudaxis.agsc.portal.model.EvaluationQuestion;
import com.cloudaxis.agsc.portal.model.EvaluationTemplate;

@Repository
public class EvaluationDAOImpl implements EvaluationDAO {

	protected  Logger logger = Logger.getLogger(EvaluationDAOImpl.class);
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<EvaluationAnswer> getEvaluationAnswerListByApplicationId(Integer applicationId) {
		List<EvaluationAnswer> evaluationAnswerList = new ArrayList<EvaluationAnswer>();
		
		String sql = "select template.template_name, users.username, question.question, question.score_percentage, question.selections, evaluation_user.selection, evaluation_user.questionid, evaluation_user.templateid, evaluation_user.comment from evaluation_user"
				+ " left join evaluation_template template on evaluation_user.templateid = template.id "
				+ " left join evaluation_question question on question.id = evaluation_user.questionid"
				+ " left join users on users.user_id = evaluation_user.evaluatedid"
				+ " where applicationid = ?";
		try{
			List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, applicationId);
			for(Map<String, Object> result : resultList){
				EvaluationAnswer evaluationAnswer = new EvaluationAnswer();
				evaluationAnswer.setTemplateid((Integer) result.get("templateid"));
				evaluationAnswer.setTemplateName((String) result.get("template_name"));
				evaluationAnswer.setEvaluatedName((String) result.get("username"));
				evaluationAnswer.setQuestionid((Integer) result.get("questionid"));
				evaluationAnswer.setQuestion((String) result.get("question"));
				evaluationAnswer.setPercentage((String) result.get("score_percentage"));
				evaluationAnswer.setQuestionSelection((String) result.get("selections"));
				evaluationAnswer.setEvaluationScore((String) result.get("selection"));
				evaluationAnswer.setComment((String) result.get("comment"));
				evaluationAnswerList.add(evaluationAnswer);
			}
		}catch(DataAccessException e){
			logger.error("Data Access Exception retrieving the list of user's evaluation answer list", e);
		}
		return evaluationAnswerList;
	}
	
	@Override
	public List<EvaluationTemplate> getEvaluationTemplateList() {
		List<EvaluationTemplate> templateNameList = new ArrayList<EvaluationTemplate>();
		
		String sql = "select * from evaluation_template ;";
		try{
			List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
			for(Map<String, Object> result : resultList){
				EvaluationTemplate template = new EvaluationTemplate();
				template.setId((Integer)result.get("id"));
				template.setName((String) result.get("template_name"));
				templateNameList.add(template);
			}
		}catch(DataAccessException e){
			logger.error("Data Access Exception retrieving the list of evaluation template", e);
		}
		
		return templateNameList;
	}

	@Override
	public List<EvaluationQuestion> getEvaluationQuestionByTemplateId(int evaluationTemplateId) {
		List<EvaluationQuestion> questionList = new ArrayList<EvaluationQuestion>();
		
		String sql = "select * from evaluation_question where templateid = ? ;";
		try{
			List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, evaluationTemplateId);
			for(Map<String, Object> result : resultList){
				EvaluationQuestion evaluationQuestion = new EvaluationQuestion();
				evaluationQuestion.setQuestionid((Integer) result.get("id"));
				evaluationQuestion.setQuestion((String) result.get("question"));
				evaluationQuestion.setPercentage((String) result.get("score_percentage"));
				evaluationQuestion.setSelections((String) result.get("selections"));
				questionList.add(evaluationQuestion);
			}
		}catch(DataAccessException e){
			logger.error("Data Access Exception retrieving the list of questions of the evaluation", e);
		}
		return questionList;
	}


	@Override
	public void saveEvaluationAnswer(EvaluationAnswer evaluationAnswer) {
		String sql = "insert into evaluation_user(applicationid, evaluatedid, templateid, questionid, selection, comment) values (?, ?, ?, ?, ?, ?);";
		try{
			jdbcTemplate.update(sql, new Object[] {
					evaluationAnswer.getApplicationid(),
					evaluationAnswer.getEvaluatedid(),
					evaluationAnswer.getTemplateid(),
					evaluationAnswer.getQuestionid(),
					evaluationAnswer.getEvaluationScore(),
					evaluationAnswer.getComment()
			});
		}catch(DataAccessException e){
			logger.error("Data Access Exception inserting user's evaluation answers", e);
		}
	}

	@Override
	public Set<Integer> getEvaluationTemplateIdsByApplicationId(Integer applicantId) {
		Set<Integer> templateIdSet= new HashSet<Integer>();
		String sql = "select templateid from evaluation_user where applicationid = ?;";
		try{
			List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, applicantId);
			for(Map<String, Object> result : resultList){
				templateIdSet.add((Integer)result.get("templateid"));
			}
		}catch(DataAccessException e){
			logger.error("Data Access Exception retrieving evaluation template ids", e);
		}
		return templateIdSet;
	}

	@Override
	public void deleteEvaluationByApplicantIdAndTemplateId(String applicantId, String templateId) {
		String sql = "delete from evaluation_user where applicationid = " + applicantId + " and templateid = " + templateId + ";"; 
		try{
			jdbcTemplate.execute(sql);
		}catch(DataAccessException e){
			logger.error("Data Access Exception deleting evaluation");
		}
	}

}
