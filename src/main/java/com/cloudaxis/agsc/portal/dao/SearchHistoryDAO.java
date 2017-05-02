package com.cloudaxis.agsc.portal.dao;

import java.util.List;

import com.cloudaxis.agsc.portal.model.SearchHistory;

public interface SearchHistoryDAO {

	public List<SearchHistory> getList();

	public SearchHistory get(SearchHistory searchHistory);

	public void delete(SearchHistory searchHistory);

	public List<?> getListNum(String searchTitile);
	
}
