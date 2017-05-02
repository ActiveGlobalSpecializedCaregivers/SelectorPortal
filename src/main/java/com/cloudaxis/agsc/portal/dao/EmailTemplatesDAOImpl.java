package com.cloudaxis.agsc.portal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.cloudaxis.agsc.portal.model.AutomatedEmailsTemplates;
import com.cloudaxis.agsc.portal.model.Caregiver;
import com.cloudaxis.agsc.portal.model.EmailHistory;
import com.cloudaxis.agsc.portal.model.EmailTemplates;

@Repository
public class EmailTemplatesDAOImpl implements EmailTemplatesDAO {
	
	protected Logger logger = Logger.getLogger(EmailTemplatesDAOImpl.class);
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<EmailTemplates> list() {
		String sql = "select * from email_templates;";
		List<EmailTemplates> templateList = new ArrayList<EmailTemplates>();
		
		try {
			List<Map<String, Object>> rows =  jdbcTemplate.queryForList(sql);
			for(Map<String, Object> row : rows){
				EmailTemplates emailTemplates = new EmailTemplates();
				emailTemplates.setId(String.valueOf(row.get("id")));
				emailTemplates.setName((String) row.get("name"));
				emailTemplates.setSubject((String) row.get("subject"));
				
				templateList.add(emailTemplates);
			}
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception search emailTemplates from the database", e);
		}
		
		return templateList;
	}

	@Override
	public long save(EmailTemplates emailTemplates) {
		final String sql = "insert into email_templates(name,subject,content,attachment) values(?,?,?,?);";
		
		final Object[] innerO=new Object[]{
							emailTemplates.getName(),
							emailTemplates.getSubject(),
							emailTemplates.getContent(),
							emailTemplates.getAttachment()
								};
		
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = jdbcTemplate.getDataSource()
							.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					for (int i = 0; i < innerO.length; i++) {
						ps.setObject(i + 1, innerO[i]);
					}
					return ps;
				}
			}, keyHolder);
		}
		
		catch (DataAccessException e) {
			logger.error("Data Access Exception inserting emailTemplates into the database", e);
		}
		return keyHolder.getKey().intValue();
		
	}

	@Override
	public List<?> get(EmailTemplates emailTemplates) {
		String sql = "select * from email_templates where name='"+emailTemplates.getName()+"';";
		List<?> num = null;
		try {
			num = jdbcTemplate.queryForList(sql);
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception search a emailTemplate from the database", e);
		}
		return num;
	}

	//search the email history
	@Override
	public List<EmailHistory> emailList(Caregiver caregiver) {
		String sql = "SELECT * FROM email_history WHERE type='0' and candidate_id='"+caregiver.userId+"';";
		
		List<EmailHistory> emailList = new ArrayList<EmailHistory>();
		try {
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			for(Map<String, Object> map : list){
				EmailHistory emailHistory = new EmailHistory();
				emailHistory.setId(String.valueOf(map.get("id")));
				emailHistory.setSubject((String) map.get("subject"));
				emailHistory.setCreateDate((Date) map.get("create_date"));
				emailHistory.setReceiveName((String) map.get("receive_name"));
				emailHistory.setSenderName((String) map.get("sender_name"));
				
				emailList.add(emailHistory);
			}
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception search email hisrory from the database", e);
		}
		return emailList;
	}

	@Override
	public long saveEmail(EmailHistory emailHistory) {
		final String sql = "insert into email_history(user_id,candidate_id,subject,content,create_date,"
				+ "cc_email,bcc_email,attachment,type,receive_email,sender_email,send_date,sender_name,receive_name)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		
		final Object[] innerO=new Object[]{
				emailHistory.getUserId(),
				emailHistory.getCandidateId(),
				emailHistory.getSubject(),
				emailHistory.getContent(),
				new Date(),
				emailHistory.getCcEmail(),
				emailHistory.getBccEmail(),
				emailHistory.getAttachment(),
				0,
				emailHistory.getReceiveEmail(),
				emailHistory.getSenderEmail(),		
				emailHistory.getSendDate(),
				emailHistory.getSenderName(),
				emailHistory.getReceiveName()		//13
				};
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = jdbcTemplate.getDataSource()
							.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					for (int i = 0; i < innerO.length; i++) {
						ps.setObject(i + 1, innerO[i]);
					}
					return ps;
				}
			}, keyHolder);
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception inserting email history into the database", e);
		}
		
		return keyHolder.getKey().intValue();
	}

	@Override
	public EmailHistory getEmail(EmailHistory emailHistory) {
		/*String sql = "select e.*,u.FIRST_NAME,u.LAST_NAME,c.full_name from email_history e "
				+ "left join users u on u.USER_ID=e.user_id "
				+ "left join candidate_profile c on c.user_id=e.candidate_id "
				+ "where id=?;";*/
		String sql = "SELECT * FROM email_history WHERE id=?";
		
		try {
			Map<String, Object> row = jdbcTemplate.queryForMap(sql, new Object[]{emailHistory.getId()});
			if(row !=null && row.size()>0){
				emailHistory.setReceiveName((String) row.get("receive_name"));
				emailHistory.setReceiveEmail((String) row.get("receive_email"));
				emailHistory.setSenderEmail((String) row.get("sender_email"));
				emailHistory.setSenderName((String)row.get("sender_name"));
				emailHistory.setSubject((String) row.get("subject"));
				emailHistory.setContent((String) row.get("content"));
				emailHistory.setCcEmail((String) row.get("cc_email"));
				emailHistory.setBccEmail((String) row.get("bcc_email"));
				emailHistory.setCreateDate((Date) row.get("create_date"));
				emailHistory.setAttachment((String) row.get("attachment"));
				emailHistory.setType((Integer) row.get("type"));
				emailHistory.setFlag((Integer) row.get("flag"));
				emailHistory.setSendDate((Date) row.get("send_date"));
				emailHistory.setMessageId((String) row.get("messageId"));
				emailHistory.setToS3Attachment((String) row.get("to_s3_attachment"));
			}
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception search email from the database", e);
		}
		
		return emailHistory;
	}

	@Override
	public EmailTemplates getTemplate(EmailTemplates emailTemplates) {
		String sql = "select * from email_templates where id=?;";
		
		try {
			Map<String, Object> map = jdbcTemplate.queryForMap(sql,new Object[]{emailTemplates.getId()});
			if(map.size()>0 && map!=null){
				emailTemplates.setName((String) map.get("name"));
				emailTemplates.setSubject((String) map.get("subject"));
				emailTemplates.setContent((String) map.get("content"));
				emailTemplates.setAttachment((String) map.get("attachment"));
			}
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception search email template from the database", e);
		}

		return emailTemplates;
	}

	@Override
	public void delEmail(String ids) {
		final String[] id = ids.split(",");
		String sql = "DELETE FROM email_history where id = ?;";
		
		try {
			jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setString(1, id[i]);
				}
				
				@Override
				public int getBatchSize() {
					return id.length;
				}
			});
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception delete email history from the database", e);
		}
		
	}

	@Override
	public void deleteTemplate(String id) {
		String sql = "DELETE FROM email_templates WHERE id=?";
		
		try {
			jdbcTemplate.update(sql, new Object[]{id});
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception delete email template from the database", e);
		}
		
	}

	@Override
	public List<AutomatedEmailsTemplates> workflowStepTemplateList() {
		String sql = "SELECT * FROM automated_emails_templates ORDER BY sort ASC";
		
		List<AutomatedEmailsTemplates> list = new ArrayList<AutomatedEmailsTemplates>();
		try {
			List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sql);
			for(Map<String, Object> row : dataList){
				AutomatedEmailsTemplates automatedEmailsTemplates = new AutomatedEmailsTemplates();
				
				automatedEmailsTemplates.setId(String.valueOf(row.get("id")));
				automatedEmailsTemplates.setName((String) row.get("name"));
				automatedEmailsTemplates.setTemplateId((String) row.get("template_id"));
				automatedEmailsTemplates.setModifiedDate((Date) row.get("modified_date"));
				automatedEmailsTemplates.setSort(String.valueOf(row.get("sort")));
				
				list.add(automatedEmailsTemplates);
			}
			
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception select automated_emails_templatesfrom the database", e);
		}
		
		return list;
	}

	@Override
	public void editAutomatedEmailsTemplate(AutomatedEmailsTemplates automatedEmailsTemplates) {
		String sql = "UPDATE automated_emails_templates SET template_id=?,modified_date=? WHERE id=?;";
		
		try {
			jdbcTemplate.update(sql, new Object[]{automatedEmailsTemplates.getTemplateId(),
					new Date(),
					automatedEmailsTemplates.getId()
					});
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception update automated_emails_templates from the database", e);
		}
			
	}

	@Override
	public EmailTemplates getAutomateEmailTemplate(String id) {
		String sql = "SELECT e.* FROM automated_emails_templates a "
				+ "left join email_templates e on e.id=a.template_id "
				+ "WHERE a.id='"+id+"'";
		EmailTemplates emailTemplates = new EmailTemplates();
		
		Map<String, Object> row = jdbcTemplate.queryForMap(sql);
		if(null!= row && row.size() > 0){
			emailTemplates.setId(String.valueOf(row.get("id")));
			emailTemplates.setName((String) row.get("name"));
			emailTemplates.setSubject((String) row.get("subject"));
			emailTemplates.setContent((String) row.get("content"));
			emailTemplates.setAttachment((String) row.get("attachment"));
		}
		return emailTemplates;
	}

	@Override
	public void editEmailTemplate(EmailTemplates emailTemplates) {
		String sql = "UPDATE email_templates SET name=?,subject=?,content=?,attachment=? WHERE id=?";
		
		try {
			jdbcTemplate.update(sql,new Object[]{
					emailTemplates.getName(),
					emailTemplates.getSubject(),
					emailTemplates.getContent(),
					emailTemplates.getAttachment(),
					emailTemplates.getId()
			});
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception update email_templates from the database", e);
		}
	}

	@Override
	public void uploadFlag(EmailHistory emailHistory) {
		String sql = "UPDATE email_history SET flag=? WHERE id=?";
		
		try {
			jdbcTemplate.update(sql, new Object[]{
				emailHistory.getFlag(),
				emailHistory.getId()
			});
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception update email history from the database", e);
		}
	}

	@Override
	public void delReceiveEmail(String ids) {
		String sql = "UPDATE email_history SET del_flag='1' WHERE id=?";
		final String[] id = ids.split(",");
		try {
			//jdbcTemplate.batchUpdate
			jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setString(1, id[i]);
				}
				
				@Override
				public int getBatchSize() {
					return id.length;
				}
			});
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception update email history from the database", e);
		}
	}

	
}
