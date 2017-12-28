package com.bridgelabz.model;

import java.util.List;

public class Pages {
	
	private String pageTitle;
	
	private String avgTimeOnPage;
	
	private String pageViews;
	
	public List<Events> events;

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getAvgTimeOnPage() {
		return avgTimeOnPage;
	}

	public void setAvgTimeOnPage(String avgTimeOnPage) {
		this.avgTimeOnPage = avgTimeOnPage;
	}

	public List<Events> getEvents() {
		return events;
	}

	public void setEvents(List<Events> events) {
		this.events = events;
	}

	public String getPageViews() {
		return pageViews;
	}

	public void setPageViews(String pageViews) {
		this.pageViews = pageViews;
	}

}
