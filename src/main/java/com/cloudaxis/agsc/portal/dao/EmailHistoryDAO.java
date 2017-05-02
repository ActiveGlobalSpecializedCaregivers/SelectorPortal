package com.cloudaxis.agsc.portal.dao;

import java.util.List;

import com.cloudaxis.agsc.portal.model.Caregiver;
import com.cloudaxis.agsc.portal.model.EmailHistory;

public interface EmailHistoryDAO {
	
	public long save(EmailHistory emailHistory);

	public List<EmailHistory> getEmailHistoryByCaregiverId(Caregiver caregiver);

	public List<EmailHistory> receiveEmailList(String senderEmail);

	public void updateAttachment(EmailHistory emailHistory);

	public int getReceiveCount(String email, int flag);

	public List<String> getMessageIds(String email);

	public void toS3Attachment(EmailHistory emailHistory);
	
}
