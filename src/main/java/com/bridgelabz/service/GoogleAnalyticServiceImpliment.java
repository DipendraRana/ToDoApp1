package com.bridgelabz.service;

import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;

import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;

@Service
public class GoogleAnalyticServiceImpliment implements GoogleAnalyticService {
	
	private static final String APPLICATION_NAME = "Hello Analytics Reporting";
	  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	  private static final String KEY_FILE_LOCATION = "/home/bridgeit/Documents/GoogleAnalytic-675e00d288ed.json";
	  private static final String VIEW_ID = "166707789";

	  /**
	   * Initializes an Analytics Reporting API V4 service object.
	   *
	   * @return An authorized Analytics Reporting API V4 service object.
	   * @throws IOException
	   * @throws GeneralSecurityException
	   */
	  public AnalyticsReporting initializeAnalyticsReporting() throws GeneralSecurityException, IOException {

	    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
	    GoogleCredential credential = GoogleCredential
	        .fromStream(new FileInputStream(KEY_FILE_LOCATION))
	        .createScoped(AnalyticsReportingScopes.all());

	    return new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential)
	        .setApplicationName(APPLICATION_NAME).build();
	  }

	  /**
	   * Queries the Analytics Reporting API V4.
	   *
	   * @param service An authorized Analytics Reporting API V4 service object.
	   * @return GetReportResponse The Analytics Reporting API V4 response.
	   * @throws IOException
	   */
	  public GetReportsResponse getReport(AnalyticsReporting service) throws IOException {

	    DateRange dateRange = new DateRange();
	    dateRange.setStartDate("2017-12-19");
	    dateRange.setEndDate("2017-12-22");

	    Metric totalEvent = new Metric()
		        .setExpression("ga:totalEvents");

	    Dimension  pageTitle= new Dimension().setName("ga:pageTitle");
	    Dimension  eventAction= new Dimension().setName("ga:eventAction");
	    Dimension  eventCategory= new Dimension().setName("ga:eventCategory");

	    ReportRequest request = new ReportRequest()
	        .setViewId(VIEW_ID)
	        .setDateRanges(Arrays.asList(dateRange))
	        .setMetrics(Arrays.asList(totalEvent))
	        .setDimensions(Arrays.asList(pageTitle,eventCategory,eventAction));

	    ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
	    requests.add(request);

	    GetReportsRequest getReport = new GetReportsRequest()
	        .setReportRequests(requests);
	    
	    GetReportsResponse response = service.reports().batchGet(getReport).execute();

	    return response;
	  }
	
}
