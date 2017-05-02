package com.cloudaxis.agsc.portal.model;

import java.util.Date;

public class Workflow {
	private Integer id;
	private Integer userId;
	private Integer candidateId;
	private Date changeDate;
	private String statusFrom;		//the value's type is int
	private String statusTo;		//the value's type is int
	private String userName;		//first_name+last_name
	private String statusFromVal;
	private String statusToVal;
	private String workflowType;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getCandidateId() {
		return candidateId;
	}
	
	public void setCandidateId(Integer candidateId) {
		this.candidateId = candidateId;
	}
	
	public Date getChangeDate() {
		return changeDate;
	}
	
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	
	public String getStatusFrom() {
		return statusFrom;
	}

	public void setStatusFrom(String statusFrom) {
		this.statusFrom = statusFrom;
	}

	public String getStatusTo() {
		return statusTo;
	}

	public void setStatusTo(String statusTo) {
		this.statusTo = statusTo;
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStatusFromVal() {
		return statusFromVal;
	}

	public void setStatusFromVal(String statusFromVal) {
		this.statusFromVal = statusFromVal;
	}

	public String getStatusToVal() {
		return statusToVal;
	}

	public void setStatusToVal(String statusToVal) {
		this.statusToVal = statusToVal;
	}

	public String getWorkflowType() {
		return workflowType;
	}

	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
	}
	
	
}
