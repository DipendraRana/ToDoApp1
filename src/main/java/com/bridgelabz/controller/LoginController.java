package com.bridgelabz.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.model.Response;
import com.bridgelabz.model.User;
import com.bridgelabz.service.LoginService;
import com.bridgelabz.service.TokenOperation;

@RestController
public class LoginController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private TokenOperation tokenOperation;

	private static final String KEY = "!12@3#abcde";

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response login(@RequestBody User user, HttpServletResponse response, HttpSession session)
			throws IOException {
		String receivedPassword = user.getPassword();
		Response message = new Response();
		try {
			user = loginService.validateTheUser(user.getEmailId());
			if (user.getPassword() != null && BCrypt.checkpw(receivedPassword, user.getPassword())) {
				response.addHeader("token", tokenOperation.generateTokenWithExpire(user.getEmailId(), "emailId", KEY,
						3600000, user.getId()));
				message.setMessage("succesfull");
				return message;
			} else {
				message.setMessage("login unsuccesfull");
				return message;
			}
		} catch (NullPointerException e) {
			message.setMessage("Not Activated");
			return message;
		}
	}

	@RequestMapping(value = "/intermediateLogin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void logout(HttpServletRequest request,HttpSession session,HttpServletResponse response) {
		String token=(String) session.getAttribute("token");
		session.removeAttribute("token");
		response.setHeader("token", token);
	}

}
