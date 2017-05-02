package com.cloudaxis.agsc.portal.model;

import java.util.Date;

public class AutomatedEmailsTemplates {
	private String id;
	private String name;
	private String templateId;
	private Date modifiedDate;
	private String sort;		//display the order
	
	@Override
	public String toString() {
		return "AutomatedEmailsTemplates [id=" + id + ", name=" + name + ", templateId=" + templateId
				+ ", modifiedDate=" + modifiedDate + "]";
	}
	
	public AutomatedEmailsTemplates() {
		super();
	}

	public AutomatedEmailsTemplates(String id, String name, String templateId, Date modifiedDate) {
		super();
		this.id = id;
		this.name = name;
		this.templateId = templateId;
		this.modifiedDate = modifiedDate;
	}

	
	public String getId() {
		return id;
	}

	
	public void setId(String id) {
		this.id = id;
	}

	
	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	
	public String getTemplateId() {
		return templateId;
	}

	
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	
	public Date getModifiedDate() {
		return modifiedDate;
	}

	
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

}
