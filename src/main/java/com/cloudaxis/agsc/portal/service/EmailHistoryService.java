package com.cloudaxis.agsc.portal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudaxis.agsc.portal.dao.EmailHistoryDAO;
import com.cloudaxis.agsc.portal.model.EmailHistory;

@Service
public class EmailHistoryService {
	@Autowired
	private EmailHistoryDAO emailHistoryDAO;

	public long save(EmailHistory emailHistory) {
		return emailHistoryDAO.save(emailHistory);
	}

	public List<EmailHistory> receiveEmailList(String senderEmail) {
		return emailHistoryDAO.receiveEmailList(senderEmail);
	}

	public void updateAttachment(EmailHistory emailHistory) {
		emailHistoryDAO.updateAttachment(emailHistory);
	}

	public int getReceiveCount(String email, int flag) {
		return emailHistoryDAO.getReceiveCount(email,flag);
	}

	public List<String> getMessageIds(String email) {
		return emailHistoryDAO.getMessageIds(email);
	}

	
}
