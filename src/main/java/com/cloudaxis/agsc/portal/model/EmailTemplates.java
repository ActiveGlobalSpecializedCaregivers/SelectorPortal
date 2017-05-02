package com.cloudaxis.agsc.portal.model;

import java.util.List;

public class EmailTemplates {
	private String id;
	private String name;
	private String subject;
	private String content;
	private String templateType;
	private String attachment;
	private List<DocumentFile> attachments;
	
	public EmailTemplates() {
		super();
	}

	public EmailTemplates(String id, String name, String subject, String content, String templateType) {
		super();
		this.id = id;
		this.name = name;
		this.subject = subject;
		this.content = content;
		this.templateType = templateType;
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
	
	public String getTemplateType() {
		return templateType;
	}
	
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	
	public String getAttachment() {
		return attachment;
	}
	
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public List<DocumentFile> getAttachments() {
		return attachments;
	}
	
	public void setAttachments(List<DocumentFile> attachments) {
		this.attachments = attachments;
	}
	
}
