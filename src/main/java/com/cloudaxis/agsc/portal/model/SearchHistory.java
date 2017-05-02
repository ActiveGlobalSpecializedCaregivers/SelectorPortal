package com.cloudaxis.agsc.portal.model;

import java.util.Date;

public class SearchHistory {
	private Integer id;
	private String name;
	private Integer userId;
	private String shareStatus;
	private String query;
	private Date createDate;
	
	public SearchHistory() {
		super();
	}

	public SearchHistory(Integer id, String name, Integer userId, String shareStatus, String query, Date createDate) {
		super();
		this.id = id;
		this.name = name;
		this.userId = userId;
		this.shareStatus = shareStatus;
		this.query = query;
		this.createDate = createDate;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public String getShareStatus() {
		return shareStatus;
	}

	public void setShareStatus(String shareStatus) {
		this.shareStatus = shareStatus;
	}
	
	public String getQuery() {
		return query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}

	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
