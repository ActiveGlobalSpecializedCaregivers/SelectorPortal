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

import com.cloudaxis.agsc.portal.model.SearchHistory;

@Repository
public class SearchHistoryDAOImpl implements SearchHistoryDAO{
	protected Logger logger = Logger.getLogger(SearchHistoryDAOImpl.class);
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<SearchHistory> getList() {
		String sql = "SELECT * FROM search_history;";
		List<SearchHistory> shlist = new ArrayList<SearchHistory>();
		
		try {
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			for(Map<String, Object> m : list){
				SearchHistory searchHistory = new SearchHistory();
				searchHistory.setId((Integer) m.get("id"));
				searchHistory.setName((String) m.get("name"));
				searchHistory.setQuery((String) m.get("query"));
				searchHistory.setShareStatus((String) m.get("share_status"));
				searchHistory.setUserId((Integer) m.get("user_id"));
				searchHistory.setCreateDate((Date) m.get("date"));
				shlist.add(searchHistory);
			}
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception retrieving the list of SearchHistory", e);
		}
		
		return shlist;
	}

	@Override
	public SearchHistory get(SearchHistory searchHistory) {
		String sql = "SELECT * FROM search_history WHERE id=?;";
		
		try {
			Map<String, Object> m = jdbcTemplate.queryForMap(sql, new Object[]{searchHistory.getId()});
			if(m !=null && m.size()>0){
				searchHistory.setName((String) m.get("name"));
				searchHistory.setQuery((String) m.get("query"));
				searchHistory.setShareStatus((String) m.get("share_status"));
				searchHistory.setUserId((Integer) m.get("user_id"));
				searchHistory.setCreateDate((Date) m.get("date"));
			}
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception retrieving a SearchHistory", e);
		}
		
		return searchHistory;
	}

	@Override
	public void delete(SearchHistory searchHistory) {
		String sql = "DELETE FROM search_history where id=?;";
		try {
			jdbcTemplate.update(sql, new Object[] { searchHistory.getId() });
		}
		catch (DataAccessException e) {
			logger.error("Delete the search history", e);
		}
	}

	@Override
	public List<?> getListNum(String searchTitile) {
		String sql = "SELECT * FROM search_history WHERE name=?;";
		List<?> num = null;
		try {
			num = jdbcTemplate.queryForList(sql, new Object[]{searchTitile});
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception search a searchHistory from the database", e);
		}
		return num;
	}
}
