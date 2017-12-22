package com.bridgelabz.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.service.GoogleAnalyticService;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;

@RestController
public class GoogleAnalytics {
	
	@Autowired
	GoogleAnalyticService googleAnalyticService;
	
	@RequestMapping(value="/getGoogleAnalyticData", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public GetReportsResponse getResponse() {
		GetReportsResponse getReportsResponse=null;
		try {
			AnalyticsReporting analyticsReporting = googleAnalyticService.initializeAnalyticsReporting();
			getReportsResponse=googleAnalyticService.getReport(analyticsReporting);
			return getReportsResponse;
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
			return getReportsResponse;
		} 
	}	

}
