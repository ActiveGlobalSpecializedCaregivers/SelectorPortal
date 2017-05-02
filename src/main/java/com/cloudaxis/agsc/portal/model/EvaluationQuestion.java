package com.cloudaxis.agsc.portal.model;

import java.util.List;

public class EvaluationQuestion {
	
	private Integer questionid;

	private String question;
	
	private String selections;
	
	private List<String> selectionList; 
	
	private String percentage;
	
	private String comments;
	
	

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

	public List<String> getSelectionList() {
		return selectionList;
	}

	public String getSelections() {
		return selections;
	}

	public void setSelections(String selections) {
		this.selections = selections;
	}

	public void setSelectionList(List<String> selectionList) {
		this.selectionList = selectionList;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
}
