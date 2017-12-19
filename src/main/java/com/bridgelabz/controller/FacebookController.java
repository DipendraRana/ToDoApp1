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
import com.bridgelabz.service.FacebookService;
import com.bridgelabz.service.RegistrationService;
import com.bridgelabz.service.TokenOperation;
import com.bridgelabz.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
public class FacebookController {

	@Autowired
	private FacebookService facebookService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RegistrationService registrationSerivce;
	
	@Autowired
	private TokenOperation tokenOperation;
	
	private static final String KEY = "!12@3#abcde";

	@RequestMapping("/facebookLogin")
	public void onClickOfFacbookLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String univarsalUniqueID = UUID.randomUUID().toString();
		String facebookLoginURL = facebookService.getFacbookLoginUrl(univarsalUniqueID);
		request.getSession().setAttribute("uniqueId", univarsalUniqueID);
		response.sendRedirect(facebookLoginURL);
	}

	@RequestMapping("/connectFB")
	public void handlingRedirectURL(HttpServletRequest request, HttpServletResponse response,HttpSession session) throws IOException {
		String sessionStoredState = (String) request.getSession().getAttribute("uniqueId");
		String facbookUrlStoredState = request.getParameter("state");
		if (sessionStoredState == null || !sessionStoredState.equals(facbookUrlStoredState))
			response.sendRedirect("facebookLogin");
		String error = request.getParameter("error");
		if (error != null)
			response.sendRedirect("Login");
		String authenticationCode=request.getParameter("code");
		String accessToken=facebookService.getAccessToken(authenticationCode);
		JsonNode userProfile=facebookService.getUserProfile(accessToken);
		String emailId=userProfile.get("email").asText();
		User user=userService.getUserByEmail(emailId);
		if(user!=null&&user.getPassword()==null) {
			session.setAttribute("token", tokenOperation.generateTokenWithExpire(user.getEmailId(), "emailId", KEY,
					3600000, user.getId()));
			response.sendRedirect("/ToDo/#!/intermediate");
			System.out.println("User Already Present");
		}else if(user==null) {
			user=new User();
			user.setUserName(userProfile.get("name").asText());
			user.setEmailId(emailId);
			user.setValidToken(true);
			user.setPicture(userProfile.get("picture").get("data").get("url").asText());
			registrationSerivce.register(user);
			session.setAttribute("token", tokenOperation.generateTokenWithExpire(user.getEmailId(), "emailId", KEY,
					3600000, user.getId()));
			response.sendRedirect("/ToDo/#!/intermediate");
		}	
		else {
			response.sendRedirect("/ToDo/#!/login");
		}
	}

}
