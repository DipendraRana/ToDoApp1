package com.bridgelabz.service;

import java.io.UnsupportedEncodingException;

import com.fasterxml.jackson.databind.JsonNode;

public interface FacebookService {
	
	public String getFacbookLoginUrl(String code);
	
	public String getAccessToken(String code) throws UnsupportedEncodingException;
	
	public JsonNode getUserProfile(String accessToken);

}
