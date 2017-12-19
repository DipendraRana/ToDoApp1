package com.bridgelabz.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface GoogleService {
	
	public String getGoogleLoginUrl(String stateToken);
	
	public String getAccessToken(String authoraztinCode) ;
	
	public JsonNode getUserProfile(String accessToken);

}
