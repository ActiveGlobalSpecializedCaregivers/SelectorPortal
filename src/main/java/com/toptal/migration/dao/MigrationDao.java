package com.toptal.migration.dao;

import java.util.Date;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * <code>MigrationDao</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class MigrationDao {
    private static final Logger logger = Logger.getLogger(MigrationDao.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MigrationDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Integer findCandidateByEmail(String email) {
        String sql = "SELECT user_id FROM candidate_profile WHERE email= ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{email}, Integer.class);
    }

    public Integer findCandidate(String fullName, Date dob) {
        String sql = "SELECT user_id FROM candidate_profile WHERE full_name = ? and dob = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{fullName, dob}, Integer.class);
    }

    public Integer findCandidate(String fullName) {
        String sql = "SELECT user_id FROM candidate_profile WHERE full_name = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{fullName}, Integer.class);
    }

    public void updateAppliedDate(Integer userId, Date appliedDate){
        String sql = "UPDATE candidate_profile SET date_applied=? "
                     + "WHERE user_id=?;";

        jdbcTemplate.update(sql, new Object[]{appliedDate, userId});
    }

    public void updateCandidateResume(String userId, String fileName) {
        String sql = "UPDATE candidate_profile SET resume=? "
                     + "WHERE user_id=?;";

        jdbcTemplate.update(sql, new Object[]{fileName, userId});
    }
}
