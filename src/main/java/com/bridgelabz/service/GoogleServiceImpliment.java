package com.bridgelabz.service;

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
public class GoogleServiceImpliment implements GoogleService {

	private static final String Google_Clent_ID = "1079577890078-l3ku7cp6o6579dq1ip9cc13i1q6tj52g.apps.googleusercontent.com";
	private static final String Google_Clent_Secret = "djTz9nyJXCuDXk6yPwQaXuHb";
	private static final String Google_Redirect_URI = "http://localhost:8080/ToDo/connectGoogle";
	private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";

	@Override
	public String getGoogleLoginUrl(String stateToken) {
		String googleLoginURL = "";
		try {
			googleLoginURL = "https://accounts.google.com/o/oauth2/auth?client_id=" + Google_Clent_ID + "&redirect_uri="
					+ URLEncoder.encode(Google_Redirect_URI, "UTF-8") + "&state=" + stateToken
					+ "&response_type=code&scope=profile email&approval_prompt=force&access_type=offline";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return googleLoginURL;
	}

	@Override
	public String getAccessToken(String authoraztinCode) {
		String accessTokenURL = "https://accounts.google.com/o/oauth2/token";
		ResteasyClient restCall = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = restCall.target(accessTokenURL);
		Form form = new Form();
		form.param("client_id", Google_Clent_ID);
		form.param("client_secret", Google_Clent_Secret);
		form.param("redirect_uri", Google_Redirect_URI);
		form.param("code", authoraztinCode);
		form.param("grant_type", "authorization_code");
		Response response = target.request().accept(MediaType.APPLICATION_JSON).post(Entity.form(form));

		String token = response.readEntity(String.class);
		ObjectMapper mapper = new ObjectMapper();
		String accessToken = null;
		try {
			accessToken = mapper.readTree(token).get("access_token").asText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		restCall.close();
		return accessToken;
	}

	@Override
	public JsonNode getUserProfile(String accessToken) {
		ResteasyClient restCall = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = restCall.target(USER_INFO_URL);

		String headerAuth = "Bearer " + accessToken;
		Response response = target.request().header("Authorization", headerAuth).accept(MediaType.APPLICATION_JSON)
				.get();

		String profile = response.readEntity(String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode googleprofile = null;
		try {
			googleprofile = mapper.readTree(profile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		restCall.close();
		return googleprofile;
	}

}
