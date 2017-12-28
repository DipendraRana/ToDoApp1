package com.bridgelabz.service;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.bridgelabz.model.GoogleAnalyticResponse;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.gson.JsonArray;

public interface GoogleAnalyticService {
	
	public AnalyticsReporting initializeAnalyticsReporting() throws GeneralSecurityException, IOException;

	public GetReportsResponse getReport(AnalyticsReporting service,String startDate,String endDate,String[] dimension,String[] metric) throws IOException ;

	public JsonArray createCustomJson(GetReportsResponse response);
	
	public GetReportsResponse getEventResponse();
	
	public GetReportsResponse getPageResponse();
	
	public GetReportsResponse getSessionResponse();
	
	public GoogleAnalyticResponse setCustomEventReport();
}
