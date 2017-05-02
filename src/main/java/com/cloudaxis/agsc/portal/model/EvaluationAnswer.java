package com.cloudaxis.agsc.portal.model;

public class EvaluationAnswer {
	
	private Integer applicationid;
	
	private Integer evaluatedid;
	
	private String evaluatedName;
	
	private Integer templateid;
	
	private String templateName;
	
	private Integer questionid;
	
	private String question;
	
	private String percentage;
	
	private String questionSelection;
	
	private String evaluationScore;
	
	private String comment;

	
	
	public Integer getApplicationid() {
		return applicationid;
	}

	public void setApplicationid(Integer applicationid) {
		this.applicationid = applicationid;
	}

	public Integer getEvaluatedid() {
		return evaluatedid;
	}

	public void setEvaluatedid(Integer evaluatedid) {
		this.evaluatedid = evaluatedid;
	}

	public String getEvaluatedName() {
		return evaluatedName;
	}

	public void setEvaluatedName(String evaluatedName) {
		this.evaluatedName = evaluatedName;
	}

	public Integer getTemplateid() {
		return templateid;
	}

	public void setTemplateid(Integer templateid) {
		this.templateid = templateid;
	}
	
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Integer getQuestionid() {
		return questionid;
	}

	public void setQuestionid(Integer questionid) {
		this.questionid = questionid;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public String getQuestionSelection() {
		return questionSelection;
	}

	public void setQuestionSelection(String questionSelection) {
		this.questionSelection = questionSelection;
	}

	public String getEvaluationScore() {
		return evaluationScore;
	}

	public void setEvaluationScore(String evaluationScore) {
		this.evaluationScore = evaluationScore;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
