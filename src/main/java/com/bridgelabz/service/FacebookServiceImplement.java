package com.bridgelabz.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.transaction.Transactional;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class FacebookServiceImplement implements FacebookService {

	public static final String FB_APP_ID = "370919266686959";
	public static final String FB_APP_SECRET = "f874ba5a55963c72f245e93ce4c99e34";
	public static final String REDIRECT_URI = "http://localhost:8080/ToDo/connectFB";

	@Override
	public String getFacbookLoginUrl(String code) {
		String facebookLoginURL = "";
		try {
			facebookLoginURL = "https://www.facebook.com/v2.11/dialog/oauth?client_id=" + FB_APP_ID + "&redirect_uri="
					+ URLEncoder.encode(REDIRECT_URI, "UTF-8") + "&client_secret=" + FB_APP_SECRET + "&state=" + code;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return facebookLoginURL;
	}

	@Override
	public String getAccessToken(String code) throws UnsupportedEncodingException {
		String facebookUrl = "https://graph.facebook.com/v2.11/oauth/access_token?" + "client_id=" + FB_APP_ID
				+ "&redirect_uri="+URLEncoder.encode(REDIRECT_URI, "UTF-8") + "&client_secret="+FB_APP_SECRET + "&code="+code;
		ResteasyClient restCall=new ResteasyClientBuilder().build();
		ResteasyWebTarget target=restCall.target(facebookUrl);
		Form form=new Form();
		form.param("client_id", FB_APP_ID);
		form.param("client_secret", FB_APP_SECRET);
		form.param("redirect_uri", REDIRECT_URI);
		form.param("code", code);
		form.param("grant_type", "authorization_code");
		
		Response response=target.request().accept(MediaType.APPLICATION_JSON).post(Entity.form(form));
		String facebookAccessToken=response.readEntity(String.class);
		ObjectMapper mapper=new ObjectMapper();
		String accessToken=null;
		try {
			accessToken = mapper.readTree(facebookAccessToken).get("access_token").asText();
		} catch (Exception e) {
				e.printStackTrace();
		}		
		return accessToken;
	}

	@Override
	public JsonNode getUserProfile(String accessToken) {
		String facebookUserURL = "https://graph.facebook.com/v2.11/me?access_token=" + accessToken
				+ "&fields=id,name,email,picture";
		ResteasyClient restCall=new ResteasyClientBuilder().build();
		ResteasyWebTarget target=restCall.target(facebookUserURL);
		String headerAuth = "Bearer" + accessToken;
		Response response = target.request().header("Authorization", headerAuth).accept(MediaType.APPLICATION_JSON).get();
		String profile =  response.readEntity(String.class);
		ObjectMapper mapper=new ObjectMapper();
		JsonNode facebookProfile = null;
		try {
			facebookProfile = mapper.readTree(profile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		restCall.close();
		return facebookProfile;
	}

}
