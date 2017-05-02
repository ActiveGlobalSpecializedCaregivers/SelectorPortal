package com.cloudaxis.agsc.portal.model;

import java.util.Date;
import java.util.List;

public class EmailHistory {
	private String id;
	private String userId;
	private String candidateId;
	private String subject;
	private String content;
	private Date createDate;
	private String userEmail;
	private String candidateEmail;
	private String senderName;
	private String receiveName;
	private String ccEmail;
	private String bccEmail;
	private String attachment;
	private Integer type;			//0:send email   //1:receive email
	private String senderEmail;
	private String receiveEmail;
	private Date sendDate;
	private List<DocumentFile> attachments;
	private Integer flag;			//0:have read mail,1:unread mail
	private String messageId; 		//receive email message id
	private String toS3Attachment;			//already uploaded attachment(receive mail)
	
	public EmailHistory() {
		super();
	}

	public EmailHistory(String id, String userId, String candidateId, String subject, String content, Date createDate) {
		super();
		this.id = id;
		this.userId = userId;
		this.candidateId = candidateId;
		this.subject = subject;
		this.content = content;
		this.createDate = createDate;
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
	
	public String getCandidateId() {
		return candidateId;
	}
	
	public void setCandidateId(String candidateId) {
		this.candidateId = candidateId;
	}

	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	
	public String getUserEmail() {
		return userEmail;
	}

	
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	
	public String getCandidateEmail() {
		return candidateEmail;
	}

	public void setCandidateEmail(String candidateEmail) {
		this.candidateEmail = candidateEmail;
	}
	
	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
	public String getReceiveName() {
		return receiveName;
	}
	
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	
	public String getCcEmail() {
		return ccEmail;
	}
	
	public void setCcEmail(String ccEmail) {
		this.ccEmail = ccEmail;
	}
	
	public String getBccEmail() {
		return bccEmail;
	}
	
	public void setBccEmail(String bccEmail) {
		this.bccEmail = bccEmail;
	}
	
	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public String getSenderEmail() {
		return senderEmail;
	}
	
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}
	
	public String getReceiveEmail() {
		return receiveEmail;
	}
	
	public void setReceiveEmail(String receiveEmail) {
		this.receiveEmail = receiveEmail;
	}
	
	public Date getSendDate() {
		return sendDate;
	}
	
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	
	public List<DocumentFile> getAttachments() {
		return attachments;
	}
	
	public void setAttachments(List<DocumentFile> attachments) {
		this.attachments = attachments;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getToS3Attachment() {
		return toS3Attachment;
	}

	public void setToS3Attachment(String toS3Attachment) {
		this.toS3Attachment = toS3Attachment;
	}

	@Override
	public String toString() {
		return "EmailHistory [id=" + id + ", userId=" + userId + ", candidateId=" + candidateId + ", subject=" + subject
				+ ", content=" + content + ", createDate=" + createDate + ", userEmail=" + userEmail + ", candidateEmail="
				+ candidateEmail + ", senderName=" + senderName + ", receiveName=" + receiveName + "]";
	}
}
