package com.cloudaxis.agsc.portal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudaxis.agsc.portal.dao.EmailHistoryDAO;
import com.cloudaxis.agsc.portal.dao.EmailTemplatesDAO;
import com.cloudaxis.agsc.portal.model.AutomatedEmailsTemplates;
import com.cloudaxis.agsc.portal.model.Caregiver;
import com.cloudaxis.agsc.portal.model.EmailHistory;
import com.cloudaxis.agsc.portal.model.EmailTemplates;

@Service
public class EmailTemplatesService {
	@Autowired
	private EmailTemplatesDAO emailTemplatesDAO;
	@Autowired 
	private EmailHistoryDAO emailHistoryDAO;
	
	public long save(EmailTemplates emailTemplates) {
		return emailTemplatesDAO.save(emailTemplates);
	}

	public List<?> get(EmailTemplates emailTemplates) {
		return emailTemplatesDAO.get(emailTemplates);
	}

	public List<EmailHistory> emailList(Caregiver caregiver) {
		return emailTemplatesDAO.emailList(caregiver);
	}

	public long saveEmail(EmailHistory emailHistory) {
		return emailTemplatesDAO.saveEmail(emailHistory);
	}

	public EmailHistory getEmail(EmailHistory emailHistory) {
		return emailTemplatesDAO.getEmail(emailHistory);
	}

	public List<EmailTemplates> list() {
		return emailTemplatesDAO.list();
	}

	public EmailTemplates getTemplate(EmailTemplates emailTemplates) {
		return emailTemplatesDAO.getTemplate(emailTemplates);
	}

	public void delEmail(String ids) {
		emailTemplatesDAO.delEmail(ids);
	}

	public void deleteTemplate(String id) {
		emailTemplatesDAO.deleteTemplate(id);
	}

	public List<AutomatedEmailsTemplates> workflowStepTemplateList() {
		return emailTemplatesDAO.workflowStepTemplateList();
	}

	public void editAutomatedEmailsTemplate(AutomatedEmailsTemplates automatedEmailsTemplates) {
		emailTemplatesDAO.editAutomatedEmailsTemplate(automatedEmailsTemplates);
	}

	public EmailTemplates getAutomateEmailTemplate(String id) {
		return emailTemplatesDAO.getAutomateEmailTemplate(id);
	}

	public void editEmailTemplate(EmailTemplates emailTemplates) {
		emailTemplatesDAO.editEmailTemplate(emailTemplates);		
	}

	public void uploadFlag(EmailHistory emailHistory) {
		emailTemplatesDAO.uploadFlag(emailHistory);
	}

	public void delReceiveEmail(String ids) {
		emailTemplatesDAO.delReceiveEmail(ids);
	}

	public void toS3Attachment(EmailHistory emailHistory) {
		emailHistoryDAO.toS3Attachment(emailHistory);
	}
	
}
