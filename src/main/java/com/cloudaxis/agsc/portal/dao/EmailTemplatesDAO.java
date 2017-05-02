package com.cloudaxis.agsc.portal.dao;

import java.util.List;

import com.cloudaxis.agsc.portal.model.AutomatedEmailsTemplates;
import com.cloudaxis.agsc.portal.model.Caregiver;
import com.cloudaxis.agsc.portal.model.EmailHistory;
import com.cloudaxis.agsc.portal.model.EmailTemplates;

public interface EmailTemplatesDAO {
	public List<EmailTemplates> list();
	
	public long save(EmailTemplates emailTemplates);

	public List<?> get(EmailTemplates emailTemplates);

	public List<EmailHistory> emailList(Caregiver caregiver);

	public long saveEmail(EmailHistory emailHistory);

	public EmailHistory getEmail(EmailHistory emailHistory);

	public EmailTemplates getTemplate(EmailTemplates emailTemplates);

	public void delEmail(String ids);

	public void deleteTemplate(String id);

	public List<AutomatedEmailsTemplates> workflowStepTemplateList();

	public void editAutomatedEmailsTemplate(AutomatedEmailsTemplates automatedEmailsTemplates);

	public EmailTemplates getAutomateEmailTemplate(String id);

	public void editEmailTemplate(EmailTemplates emailTemplates);

	public void uploadFlag(EmailHistory emailHistory);

	public void delReceiveEmail(String ids);
}
