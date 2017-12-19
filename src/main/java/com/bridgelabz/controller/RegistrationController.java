package com.bridgelabz.controller;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.filters.Validate;
import com.bridgelabz.model.Response;
import com.bridgelabz.model.User;
import com.bridgelabz.service.JmsMessageSendingService;
import com.bridgelabz.service.RegistrationService;
import com.bridgelabz.service.TokenOperationImplement;

import io.jsonwebtoken.Claims;

@RestController
public class RegistrationController {

	@Autowired
	private RegistrationService registerService;

	@Autowired
	private JmsMessageSendingService jmsMessageSendingService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private TokenOperationImplement tokenOperation;

	private static final String KEY = "!12@3#abcde";

	@RequestMapping(value = "/registration", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response Register(@RequestBody User user, HttpServletRequest request) {
		Response response = new Response();
		if (Validate.validateEmailId(user.getEmailId()) && Validate.validateMobileNumber(user.getMobileNumber())
				&& Validate.validatePassword(user.getPassword()) && Validate.validateUserName(user.getUserName())) {
			try {
				user.setValidToken(false);
				user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypting the password
				registerService.register(user);

				// Generating JWT token for authentication
				String sendingToken = tokenOperation.generateExpiryToken(String.valueOf(user.getId()), KEY,86400000);
				String message = "<a href=\"" + request.getRequestURL() + "/activate/" + sendingToken + "\" >"
						+ request.getRequestURL() + "</a>";
				jmsMessageSendingService.sendMessage(message, user.getEmailId());
				response.setMessage("registration succesfull");
				return response;
			} catch (PersistenceException e) {
				e.printStackTrace();
				response.setMessage("registration Failed");
				return response;
			}
		} 
		else {
			response.setMessage("There is Error in page");
			return response;
		}
	}

	@RequestMapping(value = "/registration/activate/{token:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response authorizeTheUser(@PathVariable("token") String token) {
		// getting id of user from JWT Token
		Response response = new Response();
		Claims claim = tokenOperation.parseTheToken(KEY, token);
		int id = Integer.parseInt(claim.getSubject());
		if (registerService.updateTheValidationToken(id) == 1)
			response.setMessage("Account Activated");
		else
			response.setMessage("Account Activation Failed");
		return response;
	}
}
