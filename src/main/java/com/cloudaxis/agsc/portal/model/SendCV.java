package com.cloudaxis.agsc.portal.model;

import java.util.Date;

public class SendCV {
	private String id;
	private String userId;
	private String sentCv;		//represent the candidateId
	private String sentTo;		//receiver's email address
	private Date date;
	private String emailMsg;
	private String fullName;
	
	public SendCV() {
		super();
	}

	public SendCV(String id, String userId, String sentCv, String sentTo, Date date, String emailMsg, String fullName) {
		super();
		this.id = id;
		this.userId = userId;
		this.sentCv = sentCv;
		this.sentTo = sentTo;
		this.date = date;
		this.emailMsg = emailMsg;
		this.fullName = fullName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getSentCv() {
		return sentCv;
	}

	public void setSentCv(String sentCv) {
		this.sentCv = sentCv;
	}
	
	public String getSentTo() {
		return sentTo;
	}

	public void setSentTo(String sentTo) {
		this.sentTo = sentTo;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getEmailMsg() {
		return emailMsg;
	}
	
	public void setEmailMsg(String emailMsg) {
		this.emailMsg = emailMsg;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public String toString() {
		return "SendCV [id=" + id + ", userId=" + userId + ", sentCv=" + sentCv + ", sentTo=" + sentTo + ", date="
				+ date + ", emailMsg=" + emailMsg +", fullName=" + fullName +"]";
	}
	
}
