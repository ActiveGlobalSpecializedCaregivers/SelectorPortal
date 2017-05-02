package com.cloudaxis.agsc.portal.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cloudaxis.agsc.portal.model.Workflow;

@Repository
public class WorkflowDAOImpl implements WorkflowDAO{
	protected Logger logger = Logger.getLogger(WorkflowDAOImpl.class);

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	/**
	 * The userId is candidate_id 
	 */
	public List<Workflow> list(String userId) {
		String sql = "SELECT w.*,u.first_name, u.last_name FROM workflow_history w "
				+ "LEFT JOIN users u ON u.USER_ID=w.user_id where w.candidate_id ='" +userId+ "' order by w.change_date desc ;";
		
		List<Workflow> WorkflowList = new ArrayList<Workflow>();
		
		try {
			List<Map<String, Object>> mapList =jdbcTemplate.queryForList(sql);

			for (Map<String, Object> row : mapList) {
				Workflow workflow = new Workflow();
				workflow.setChangeDate((Date) row.get("change_date"));
				workflow.setUserName((String) row.get("first_name")+" "+(String) row.get("last_name"));		//is modify the candidate status's name
				workflow.setStatusFrom((String) row.get("status_from"));
				workflow.setStatusTo((String) row.get("status_to"));
				workflow.setWorkflowType((String) row.get("workflow"));
				WorkflowList.add(workflow);
			}
		}
		catch (DataAccessException e) {
			e.printStackTrace();
		}
		return WorkflowList;
	}
		
}
