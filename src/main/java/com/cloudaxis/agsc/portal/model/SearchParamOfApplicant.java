package com.cloudaxis.agsc.portal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchParamOfApplicant {
	
	@JsonProperty("questionFlag")
	private String questionFlag;

	@JsonProperty("title")
	private String title;
	
	@JsonProperty("condition")
	private String condition;
	
	@JsonProperty("input")
	private String input;

	

	public String getQuestionFlag() {
		return questionFlag;
	}

	public void setQuestionFlag(String questionFlag) {
		this.questionFlag = questionFlag;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}
	
	
}

