package com.cloudaxis.agsc.portal.dao;

import java.util.List;

import com.cloudaxis.agsc.portal.model.Workflow;

public interface WorkflowDAO {

	public List<Workflow> list(String userId);

}
