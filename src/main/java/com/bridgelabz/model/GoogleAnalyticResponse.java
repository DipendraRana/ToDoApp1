package com.bridgelabz.model;

import java.util.List;

public class GoogleAnalyticResponse {
	
	private String sessionDuration;
	
	private String pagesPerSession;
	
	private List<Pages> pages;

	public String getsessionDuration() {
		return sessionDuration;
	}

	public void setsessionDuration(String sessionDuration) {
		this.sessionDuration = sessionDuration;
	}

	public String getPagesPerSession() {
		return pagesPerSession;
	}

	public void setPagesPerSession(String pagesPerSession) {
		this.pagesPerSession = pagesPerSession;
	}

	public List<Pages> getPages() {
		return pages;
	}

	public void setPages(List<Pages> pages) {
		this.pages = pages;
	}

}
