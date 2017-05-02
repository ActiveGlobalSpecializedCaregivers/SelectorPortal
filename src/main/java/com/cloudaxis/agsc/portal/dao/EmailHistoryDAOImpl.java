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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.cloudaxis.agsc.portal.helpers.StringUtil;
import com.cloudaxis.agsc.portal.model.Caregiver;
import com.cloudaxis.agsc.portal.model.EmailHistory;

@Repository
public class EmailHistoryDAOImpl implements EmailHistoryDAO {

	protected Logger		logger	= Logger.getLogger(EmailHistoryDAOImpl.class);

	private JdbcTemplate	jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public long save(EmailHistory emailHistory) {
		final String sql = "insert into email_history(subject,content,create_date,cc_email,attachment,"
				+ "type,receive_email,sender_email,send_date,sender_name,receive_name,user_id,candidate_id,"
				+ "flag,messageId) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

		final Object[] innerO=new Object[]{
				emailHistory.getSubject(),
				emailHistory.getContent(),
				new Date(),
				emailHistory.getCcEmail(),
				emailHistory.getAttachment(),
				emailHistory.getType(),
				emailHistory.getReceiveEmail(),
				emailHistory.getSenderEmail(),
				emailHistory.getSendDate(),
				emailHistory.getSenderName(),
				emailHistory.getReceiveName(),
				emailHistory.getUserId(),
				emailHistory.getCandidateId(),
				emailHistory.getFlag(),
				emailHistory.getMessageId()		//15
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
	public List<EmailHistory> getEmailHistoryByCaregiverId(Caregiver caregiver) {
		String sql = "select e.*,c.full_name,c.email,u.FIRST_NAME,u.LAST_NAME,u.EMAIL from email_history e "
				+ "left join candidate_profile c on c.user_id=e.candidate_id "
				+ "left join users u on u.USER_ID=e.user_id where candidate_id='" + caregiver.userId + "';";

		List<EmailHistory> emailList = new ArrayList<EmailHistory>();
		try {
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			for (Map<String, Object> map : list) {
				EmailHistory emailHistory = new EmailHistory();
				emailHistory.setId(String.valueOf(map.get("id")));
				emailHistory.setCandidateId(String.valueOf(map.get("candidate_id")));
				emailHistory.setCandidateEmail((String) map.get("email"));
				emailHistory.setReceiveName((String) map.get("full_name"));
				emailHistory.setUserEmail((String) map.get("EMAIL"));
				emailHistory.setUserId(String.valueOf(map.get("user_id")));
				emailHistory.setSenderName((String) map.get("FIRST_NAME") + " " + (String) map.get("LAST_NAME"));
				emailHistory.setCreateDate((Date) map.get("create_date"));
				emailHistory.setSubject((String) map.get("subject"));
				emailHistory.setContent((String) map.get("content"));

				emailList.add(emailHistory);
			}
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception search email hisrory from the database", e);
		}
		return emailList;
	}

	@Override
	public List<EmailHistory> receiveEmailList(String senderEmail) {
		String sql = "SELECT * FROM email_history WHERE type='1' and del_flag='0' and sender_email=?";
		
		List<EmailHistory> emailList = new ArrayList<EmailHistory>();
		
		try {
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,new Object[]{senderEmail});
			for(Map<String, Object> map : list){
				EmailHistory emailHistory = new EmailHistory();
				emailHistory.setId(String.valueOf(map.get("id")));
				emailHistory.setCreateDate((Date) map.get("create_date"));
				emailHistory.setSendDate((Date) map.get("send_date"));
				emailHistory.setSubject((String) map.get("subject"));
				emailHistory.setContent((String) map.get("content"));
				emailHistory.setReceiveEmail((String) map.get("receive_email"));
				emailHistory.setSenderEmail((String) map.get("sender_email"));
				emailHistory.setAttachment((String) map.get("attachment"));
				emailHistory.setReceiveName((String) map.get("receive_name"));
				emailHistory.setSenderName((String) map.get("sender_name"));
				emailHistory.setFlag((Integer) map.get("flag"));
				
				emailList.add(emailHistory);
			}
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception search email hisrory from the database", e);
		}
		return emailList;
	}

	@Override
	public void updateAttachment(EmailHistory emailHistory) {
		String sql = "UPDATE email_history SET attachment=? WHERE id=?";
		
		try {
			jdbcTemplate.update(sql, new Object[]{emailHistory.getAttachment(),emailHistory.getId()});
		} catch (DataAccessException e) {
			logger.error("Data Access Exception update email hisrory from the database", e);
		}
		
	}

	@Override
	public int getReceiveCount(String email, int flag) {
		String sql = "SELECT count(*) FROM email_history WHERE del_flag='0' and flag='" +flag+ "' and type='1'" + "and sender_email='"+email+"'"; 
		
		try {
			return jdbcTemplate.queryForObject(sql, Integer.class);
		} catch (DataAccessException e) {
			logger.error("Data Access Exception select email hisrory from the database", e);
		}
		
		return 0;
	}

	@Override
	public List<String> getMessageIds(String email) {
		String sql = "SELECT messageId FROM email_history WHERE sender_email='"+email+"'";
		
		List<String> listMessageId = new ArrayList<String>();
		
		try {
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			for(Map<String, Object> map : list){
				if(!StringUtil.isBlank((String) map.get("messageId"))){
					listMessageId.add((String) map.get("messageId"));
				}
			}
		} catch (DataAccessException e) {
			logger.error("Data Access Exception select email hisrory from the database", e);
		}
		
		return listMessageId;
	}

	@Override
	public void toS3Attachment(EmailHistory emailHistory) {
		String sql = "UPDATE email_history SET to_s3_attachment=? WHERE id=?";
		
		try {
			jdbcTemplate.update(sql, new Object[]{emailHistory.getToS3Attachment(),emailHistory.getId()});
		} catch (DataAccessException e) {
			logger.error("Data Access Exception update email hisrory from the database", e);
		}
	}

}
