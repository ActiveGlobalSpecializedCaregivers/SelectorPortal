package com.cloudaxis.agsc.portal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudaxis.agsc.portal.dao.EmailTemplatesDAO;
import com.cloudaxis.agsc.portal.dao.SearchHistoryDAO;
import com.cloudaxis.agsc.portal.model.SearchHistory;

@Service
public class SearchHistoryService {

	@Autowired
	private SearchHistoryDAO searchHistoryDAO;

	
	public List<SearchHistory> getList() {
		return searchHistoryDAO.getList();
	}


	public SearchHistory get(SearchHistory searchHistory) {
		return searchHistoryDAO.get(searchHistory);
	}


	public void delete(SearchHistory searchHistory) {
		searchHistoryDAO.delete(searchHistory);
	}


	public List<?> getListNum(String searchTitile) {
		return searchHistoryDAO.getListNum(searchTitile);
	}
	
}
