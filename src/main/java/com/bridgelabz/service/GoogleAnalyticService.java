package com.bridgelabz.service;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;

public interface GoogleAnalyticService {
	
	public AnalyticsReporting initializeAnalyticsReporting() throws GeneralSecurityException, IOException;

	public GetReportsResponse getReport(AnalyticsReporting service) throws IOException ;
}
