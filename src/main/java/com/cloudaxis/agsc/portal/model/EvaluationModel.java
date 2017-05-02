package com.cloudaxis.agsc.portal.model;

import java.util.List;

public class EvaluationModel {

	private String applicantId;
	
	private String templateId;
	
	private String templateName;
	
	private String divId;
	
	private String summary;
	
	private List<EvaluationAnswer> anwserList;



	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	public String getDivId() {
		return divId;
	}

	public void setDivId(String divId) {
		this.divId = divId;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<EvaluationAnswer> getAnwserList() {
		return anwserList;
	}

	public void setAnwserList(List<EvaluationAnswer> anwserList) {
		this.anwserList = anwserList;
	}
	
}
