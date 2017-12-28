package com.bridgelabz.service;

import org.springframework.stereotype.Service;

import com.bridgelabz.model.Events;
import com.bridgelabz.model.GoogleAnalyticResponse;
import com.bridgelabz.model.Pages;
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
import java.util.List;

import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.model.ColumnHeader;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.DateRangeValues;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.MetricHeaderEntry;
import com.google.api.services.analyticsreporting.v4.model.Report;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;
import com.google.api.services.analyticsreporting.v4.model.ReportRow;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class GoogleAnalyticServiceImpliment implements GoogleAnalyticService {

	private static final String APPLICATION_NAME = "Hello Analytics Reporting";
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final String KEY_FILE_LOCATION = "/home/bridgeit/Documents/GoogleAnalytic-675e00d288ed.json";
	private static final String VIEW_ID = "166707789";
	private JsonObject responseJsonObject = new JsonObject();

	/**
	 * Initializes an Analytics Reporting API V4 service object.
	 *
	 * @return An authorized Analytics Reporting API V4 service object.
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public AnalyticsReporting initializeAnalyticsReporting() throws GeneralSecurityException, IOException {

		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(KEY_FILE_LOCATION))
				.createScoped(AnalyticsReportingScopes.all());

		return new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build();
	}

	/**
	 * Queries the Analytics Reporting API V4.
	 *
	 * @param service
	 *            An authorized Analytics Reporting API V4 service object.
	 * @return GetReportResponse The Analytics Reporting API V4 response.
	 * @throws IOException
	 */
	public GetReportsResponse getReport(AnalyticsReporting service, String startDate, String endDate,
			String[] dimension, String[] metric) throws IOException {

		DateRange dateRange = new DateRange();
		dateRange.setStartDate(startDate);
		dateRange.setEndDate(endDate);

		List<Metric> allMetrics = new ArrayList<Metric>();
		for (int metricCount = 0; metricCount < metric.length; metricCount++)
			allMetrics.add(new Metric().setExpression(metric[metricCount]));

		List<Dimension> allDimensions = new ArrayList<Dimension>();
		for (int dimentionCount = 0; dimentionCount < dimension.length; dimentionCount++)
			allDimensions.add(new Dimension().setName(dimension[dimentionCount]));

		ReportRequest request = new ReportRequest().setViewId(VIEW_ID).setDateRanges(Arrays.asList(dateRange))
				.setMetrics(allMetrics).setDimensions(allDimensions);

		ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
		requests.add(request);

		GetReportsRequest getReport = new GetReportsRequest().setReportRequests(requests);

		GetReportsResponse response = service.reports().batchGet(getReport).execute();

		return response;
	}

	public GetReportsResponse getEventResponse() {
		GetReportsResponse getReportsResponse = null;
		try {
			AnalyticsReporting analyticsReporting = initializeAnalyticsReporting();
			String[] allDimensions = { "ga:pageTitle", "ga:eventAction", "ga:eventCategory" };
			String[] allMetrics = { "ga:totalEvents" };
			getReportsResponse = getReport(analyticsReporting, "2017-12-19", "2017-12-23", allDimensions, allMetrics);
			responseJsonObject.add("event", createCustomJson(getReportsResponse));
			return getReportsResponse;
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
			return getReportsResponse;
		}
	}

	public GetReportsResponse getPageResponse() {
		GetReportsResponse getReportsResponse = null;
		try {
			AnalyticsReporting analyticsReporting = initializeAnalyticsReporting();
			String[] allDimensions = { "ga:pageTitle" };
			String[] allMetrics = {"ga:pageviews", "ga:timeOnPage"};
			getReportsResponse = getReport(analyticsReporting, "2017-12-19", "2017-12-23", allDimensions, allMetrics);
			responseJsonObject.add("page", createCustomJson(getReportsResponse));
			return getReportsResponse;
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
			return getReportsResponse;
		}
	}

	public GetReportsResponse getSessionResponse() {
		GetReportsResponse getReportsResponse = null;
		try {
			AnalyticsReporting analyticsReporting = initializeAnalyticsReporting();
			String[] allDimensions = {};
			String[] allMetrics = {"ga:sessionDuration", "ga:pageviewsPerSession"};
			getReportsResponse = getReport(analyticsReporting, "2017-12-19", "2017-12-23", allDimensions, allMetrics);
			responseJsonObject.add("session", createCustomJson(getReportsResponse));
			return getReportsResponse;
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
			return getReportsResponse;
		}
	}

	public JsonArray createCustomJson(GetReportsResponse response) {
		JsonArray array = new JsonArray();
		for (Report report : response.getReports()) {
			ColumnHeader header = report.getColumnHeader();
			List<String> dimensionHeaders = header.getDimensions();
			List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
			List<ReportRow> rows = report.getData().getRows();

			for (ReportRow row : rows) {
				List<String> dimensions = row.getDimensions();
				List<DateRangeValues> metrics = row.getMetrics();
				JsonObject object = new JsonObject();
				if (dimensionHeaders != null) {
					for (int dimesionCount = 0; dimesionCount < dimensionHeaders.size() && dimesionCount < dimensions.size(); dimesionCount++) {
						object.addProperty(dimensionHeaders.get(dimesionCount), dimensions.get(dimesionCount));
					}
				}

				if (metricHeaders != null) {
					for (int metricCount = 0; metricCount < metrics.size(); metricCount++) {
						DateRangeValues values = metrics.get(metricCount);
						for (int metricValueCount = 0; metricValueCount < values.getValues().size() && metricValueCount < metricHeaders.size(); metricValueCount++) {
							object.addProperty(metricHeaders.get(metricValueCount).getName(), values.getValues().get(metricValueCount));
						}
					}
				}
				array.add(object);
			}
		}
		return array;
	}

	public GoogleAnalyticResponse setCustomEventReport() {
		JsonArray sessionArray=responseJsonObject.getAsJsonArray("session");
		JsonArray pageArray=responseJsonObject.getAsJsonArray("page");
		JsonArray eventArray=responseJsonObject.getAsJsonArray("event");
		GoogleAnalyticResponse analyticResponse=null;
		Pages pages=null;
		Events events=null;
		if(sessionArray!=null) {
			for(int sessionCount=0;sessionCount<sessionArray.size();sessionCount++) {
				analyticResponse=new GoogleAnalyticResponse();
				JsonObject sessionObject=(JsonObject) sessionArray.get(sessionCount);
				analyticResponse.setsessionDuration(sessionObject.get("ga:sessionDuration").getAsString());
				analyticResponse.setPagesPerSession(sessionObject.get("ga:pageviewsPerSession").getAsString());
				List<Pages> listOfPages=new ArrayList<Pages>();
				if(pageArray!=null) {
					for(int pageCount=0;pageCount<pageArray.size();pageCount++) {
						JsonObject pageObject=(JsonObject) pageArray.get(pageCount);
						pages=new Pages();
						pages.setPageTitle(pageObject.get("ga:pageTitle").getAsString());
						pages.setAvgTimeOnPage(pageObject.get("ga:timeOnPage").getAsString());
						pages.setPageViews(pageObject.get("ga:pageviews").getAsString());
						List<Events> listOfEvents=new ArrayList<Events>();
						if(eventArray!=null) {
							for(int eventCount=0;eventCount<eventArray.size();eventCount++) {
								events=new Events();
								JsonObject eventObject=(JsonObject) eventArray.get(eventCount);
								if(eventObject.get("ga:pageTitle").getAsString().equals(pages.getPageTitle())) {
									events.setEventCategory(eventObject.get("ga:eventCategory").getAsString());
									events.setEventAction(eventObject.get("ga:eventAction").getAsString());
									events.setTotalEvent(eventObject.get("ga:totalEvents").getAsString());
									listOfEvents.add(events);
								}
								
							}	
						}
						pages.setEvents(listOfEvents);
						listOfPages.add(pages);
					}	
				}
				analyticResponse.setPages(listOfPages);
			}	
		}
		return analyticResponse;
	}

}
