package com.bridgelabz.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.model.ResetPassword;
import com.bridgelabz.model.Response;
import com.bridgelabz.model.User;
import com.bridgelabz.service.JmsMessageSendingService;
import com.bridgelabz.service.TokenOperation;
import com.bridgelabz.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

@RestController
public class ForgotPasswordController {

	@Autowired
	private UserService userService;

	@Autowired
	private TokenOperation tokenOperation;

	@Autowired
	private JmsMessageSendingService jmsMessageSendingService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	private static final String KEY = "!12@3#abcde";

	@RequestMapping(value = "/forgotPassword", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response forgotPassword(@RequestBody User user, HttpServletResponse httpResponse,HttpServletRequest request) {
		user = userService.getUserByEmail(user.getEmailId());
		Response response = new Response();
		if (user == null) {
			response.setMessage("no such email-Id is present");
			return response;
		} else {
			String token = tokenOperation.generateExpiryToken(String.valueOf(user.getId()), KEY, 3600000);
			String message = "Click To resetPassword-><a href=\""+request.getRequestURL()+"/validate/"+token+"\" >" + token
					+ " </a>";
			jmsMessageSendingService.sendMessage(message, user.getEmailId());
			response.setMessage("Email-Sent");
			return response;
		}
	}

	@RequestMapping(value = "/forgotPassword/resetPassword/{token:.+}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response resetPassword(@PathVariable String token, @RequestBody ResetPassword resetPassword) {
		Response response = new Response();
		if (resetPassword.getNewPassword().equals(resetPassword.getReEnterNewPassword())) {
			try {
				Claims claim = tokenOperation.parseTheToken(KEY, token);
				int id = Integer.parseInt(claim.getSubject());
				String newPassword = passwordEncoder.encode(resetPassword.getNewPassword());
				userService.updatePasswordOfUser(newPassword, id);
				response.setMessage("password is reset");
				return response;
			} catch (ExpiredJwtException e) {
				e.printStackTrace();
				response.setMessage("Token Expired");
				return response;
			} catch (NumberFormatException e) {
				e.printStackTrace();
				response.setMessage("Input Problem");
				return response;
			}
		} else {
			response.setMessage("Password not matched");
			return response;
		}
	}
	
	@RequestMapping(value = "/forgotPassword/validate/{token:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response checkResponseFromEmailAndRedirect(@PathVariable String token,HttpServletResponse httpResponse) {
		Response response = new Response();
		try {
			Claims claim = tokenOperation.parseTheToken(KEY, token);response.setMessage("Okay");
			int id = Integer.parseInt(claim.getSubject());
			if(id!=0) {
				httpResponse.sendRedirect("http://localhost:8080/ToDo/#!/resetPassword/#"+token);
			}
			else
				response.setMessage("Not-Okay");
			return response;
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			response.setMessage("Token Expired");
			return response;
		} catch (IOException e) {
			e.printStackTrace();
			response.setMessage("Problem in Rediecting");
			return response;
		}		
	}

}
