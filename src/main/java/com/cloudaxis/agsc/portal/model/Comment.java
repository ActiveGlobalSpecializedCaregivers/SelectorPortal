package com.cloudaxis.agsc.portal.model;

import java.util.Date;

public class Comment {

	private Integer id;
	private Integer userId;
	private Integer candidateId;
	private String comment;
	private Date createDate;
	private Date updateDate;
	private String userName;
	private String userInitials;

	public Comment() {
	}


	public Comment(Integer userId, Integer candidateId, String comment, Date createDate, Date updateDate) {
		super();
		this.userId = userId;
		this.candidateId = candidateId;
		this.comment = comment;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}


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


	
	public String getComment() {
		return comment;
	}


	
	public void setComment(String comment) {
		this.comment = comment;
	}


	
	public Date getCreateDate() {
		return createDate;
	}


	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	
	public Date getUpdateDate() {
		return updateDate;
	}


	
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}


	
	public String getUserName() {
		return userName;
	}


	
	public void setUserName(String userName) {
		this.userName = userName;
	}


	
	public String getUserInitials() {
		return userInitials;
	}


	
	public void setUserInitials(String userInitials) {
		this.userInitials = userInitials;
	}

	

}
