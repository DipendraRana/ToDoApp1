package com.bridgelabz.controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.model.User;
import com.bridgelabz.service.GoogleService;
import com.bridgelabz.service.RegistrationService;
import com.bridgelabz.service.TokenOperation;
import com.bridgelabz.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
public class GoogleController {

	@Autowired
	private GoogleService googleService;

	@Autowired
	private RegistrationService registrationSerivce;

	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenOperation tokenOperation;

	private static final String KEY = "!12@3#abcde";

	@RequestMapping("/googleLogin")
	public void onClickOfGoogleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String univarsalUniqueID = UUID.randomUUID().toString();
		String googleLoginUrl = googleService.getGoogleLoginUrl(univarsalUniqueID);
		request.getSession().setAttribute("uniqueId", univarsalUniqueID);
		response.sendRedirect(googleLoginUrl);
	}

	@RequestMapping("/connectGoogle")
	public void handlingRedirectURL(HttpServletRequest request, HttpServletResponse response,HttpSession session) throws IOException {
		String sessionStoredState = (String) request.getSession().getAttribute("uniqueId");
		String googleUrlStoredState = request.getParameter("state");
		if (sessionStoredState == null || !sessionStoredState.equals(googleUrlStoredState))
			response.sendRedirect("googleLogin");
		String error = request.getParameter("error");
		if (error != null)
			response.sendRedirect("Login");
		String authenticationCode=request.getParameter("code");
		String accessToken=googleService.getAccessToken(authenticationCode);
		JsonNode userProfile=googleService.getUserProfile(accessToken);
		String emailId=userProfile.get("email").asText();
		User user=userService.getUserByEmail(emailId);
		if(user!=null&&user.getPassword()==null) {
			session.setAttribute("token", tokenOperation.generateTokenWithExpire(user.getEmailId(), "emailId", KEY,
					3600000, user.getId()));
			response.sendRedirect("/ToDo/#!/intermediate");
		}else if(user==null) {
			user=new User();
			user.setUserName(userProfile.get("name").asText());
			user.setEmailId(emailId);
			user.setValidToken(true);
			if(userProfile.get("picture").asText()!=null)
				user.setPicture(userProfile.get("picture").asText());
			registrationSerivce.register(user);
			session.setAttribute("token", tokenOperation.generateTokenWithExpire(user.getEmailId(), "emailId", KEY,
					3600000, user.getId()));
			response.sendRedirect("/ToDo/#!/intermediate");
		}else {
			response.sendRedirect("/ToDo/#!/login");
		}
	}

}
