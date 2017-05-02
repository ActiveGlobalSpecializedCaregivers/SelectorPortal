package com.cloudaxis.agsc.portal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudaxis.agsc.portal.dao.WorkflowDAO;
import com.cloudaxis.agsc.portal.model.Workflow;

@Service
public class WorkflowService {
	
	@Autowired
	private WorkflowDAO workflowDAO;
	
	public List<Workflow> list(String userId) {
		return workflowDAO.list(userId);
	}

}
