 package com.bridgelabz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.model.GoogleAnalyticResponse;
import com.bridgelabz.service.GoogleAnalyticService;

@RestController
public class GoogleAnalytics {
	
	@Autowired
	GoogleAnalyticService googleAnalyticService;
	
	@RequestMapping(value="/getGoogleAnalyticData", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public GoogleAnalyticResponse getAnalyticResponse() {
		googleAnalyticService.getSessionResponse();
		googleAnalyticService.getPageResponse();
		googleAnalyticService.getEventResponse();
		return googleAnalyticService.setCustomEventReport();
	}
}
