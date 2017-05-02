package com.cloudaxis.agsc.portal.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.cloudaxis.agsc.portal.model.Profile;
import com.cloudaxis.agsc.portal.model.User;

@Repository
public class UserProfileDAOImpl implements UserProfileDAO {

	protected Logger logger = Logger.getLogger(UserProfileDAOImpl.class);

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void updateProfile(Profile profile) {
		// TODO Auto-generated method stub

	}

	public int addProfile(Profile profile) {
		final String sql = "insert into candidate_profile values("
				+ "?,?,?,?,?,?,?,?,?,?,"   // 10
				+ "?,?,?,?,?,?,?,?,?,?,"   // 20
				+ "?,?,?,?,?,?,?,?,?,?,"   // 30
				+ "?,?,?,?,?,?,?,?,?,?,"   // 40
				+ "?,?,?,?,?,?,?,?,?,?,"   // 50
				+ "?,?,?,?,?,?,?,?,?,?,"   // 60
				+ "?,?,?,?,?,?,?,?,?,?,"   // 70
				+ "?,?,?,?,?,?,?,?,?,?,"   // 80
				+ "?,?,?,?,?,?,?,?,?,?,"   // 90
				+ "?,?,?,?,?,?,?,?,?,?);"; //100
		final Object[] innerO = new Object[] {
				profile.getUser_id(),
				profile.getApp_id(),
				profile.getEmail(),
				profile.getFull_name(),
				profile.getFirst_name(),
				profile.getLast_name(),
				profile.getGender(),
				profile.getDob(),
				profile.getAge(),
				profile.getCountry_of_birth(), // 10
				profile.getHas_children(),
				profile.getSiblings(),
				profile.getLanguages(),
				profile.getNationality(),
				profile.getEducational_level(),
				profile.getCertified_cpr(),
				profile.getExp(),
				profile.getMarital_status(),
				profile.getReligion(),
				profile.getMobile(), // 20
				profile.getHeight(),
				profile.getWeight(),
				profile.getMotivation(),
				profile.getAbout(),
				profile.getEducation(),
				profile.getSpecialties(),
				profile.getHobbies(),
				profile.getAvailability(),
				profile.getDate_applied(),
				profile.getWork_in_hk(), // 30
				profile.getWork_in_sg(),
				profile.getWork_in_tw(),
				profile.getSalary_hkd(),
				profile.getSalary_sgd(),
				profile.getSalary_twd(),
				profile.getFood_choice(),
				profile.getPhoto(),
				profile.getSkype(),
				profile.getResume(),
				profile.getTag(), // 40
				profile.getTag_id(),
				profile.getTag_status(),
				profile.getTagged_date(),
				profile.getContracted_to(),
				profile.getTagged_to(),
				profile.getAssigned(),
				profile.getLocation(),
				profile.getRegistration_number(),
				profile.getRemarks(),
				profile.getNearest_airport(), // 50
				profile.getCurrent_address(),
				profile.getNumber_of_children(),
				profile.getChildren_names(),
				profile.getWork_in_sg(),
				profile.getAllergies(),
				profile.getDiagnosed_conditions(),
				profile.getVideo_url(),
				profile.getApplicant_status(),
				profile.getDatecreated(),
				profile.getChildren_name_age(), // 60
				profile.getHold_passport(),
				profile.getYear_graduation(),
				profile.getYear_studies(),
				profile.getCaregiver_before_exp(),
				profile.getSg_fin(),
				profile.getCurrent_job(),
				profile.getTime_of_sg(),
				profile.getCurrent_job_time(),
				profile.getHistory_of_treatment(),
				profile.getInterview_time(), // 70
				profile.getReferrer(),
				profile.getPhoto_passport(),
				profile.getPhoto_degrees(),
				profile.getOther_files(), 
				new Date(),
				0,
				profile.getTesda_ncii(),
				profile.getRegistered_concorde(),
				profile.getPre_deployment(),
				profile.getEvaluationSummary(), //80
				profile.getMedicalCertVerified(),		
				profile.getMarkedAsRreadyTime(),
				null,
				null,
				null,
				null,
				profile.getMedicalCertValidator(),
				null,
				null,
				null,//90
				null, 
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				0	// 100
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
			logger.error("Data Access Exception inserting a UserProfile into the database", e);
		}
		return keyHolder.getKey().intValue();
	}

	@Override
	public void saveMailHistory(Profile profile, MimeMessage message, User user) throws IOException, MessagingException{
		String sql = " insert into email_history(user_id, candidate_id, subject, content, date) values(?, ?, ?, ?, ?); ";

		try {

			jdbcTemplate.update(sql, new Object[] { user.getUserId(),
					profile.getUser_id(),
					message.getSubject(),
					message.getContent().toString(),
					message.getSentDate()
			});
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception inserting email history into the database", e);
		}
		
	}

}
