package com.bridgelabz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.model.Label;
import com.bridgelabz.model.Note;
import com.bridgelabz.model.Response;
import com.bridgelabz.model.User;
import com.bridgelabz.service.LabelService;
import com.bridgelabz.service.TokenOperation;
import com.bridgelabz.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

@RestController
public class LabelController {
	
	@Autowired
	private TokenOperation tokenOperation;
	
	@Autowired
	private LabelService labelService;
	
	@Autowired
	private UserService userService;
	
	private static final String KEY = "!12@3#abcde";
	
	@RequestMapping(value = "/getLabels", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Label> getLabels(HttpServletRequest request) {
		String token = request.getHeader("token");
		List<Label> list = null;
		try {
			Claims claim = tokenOperation.parseTheToken(KEY, token);
			int id = Integer.parseInt(claim.getSubject());
			list = labelService.getAllLabels(id);
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
		}
		return list;	
	}
	
	@RequestMapping(value = "/getNotesOfLabel", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Note> getAllNotesOfTheLabel(@RequestBody Label label, HttpServletRequest request){
		String token = request.getHeader("token");
		List<Note> list = null;
		try {
			Claims claim = tokenOperation.parseTheToken(KEY, token);
			int id = Integer.parseInt(claim.getSubject());
			list = labelService.getAllNotesOfThisLabel(label , id);
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@RequestMapping(value = "/updateLabel", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateLabel(@RequestBody Label label , HttpServletRequest request) {
		String token = request.getHeader("token");
		Response response = new Response();
		try {
			Claims claim = tokenOperation.parseTheToken(KEY, token);
			String emailId = (String) claim.get("emailId");
			User user = userService.getUserByEmail(emailId);
			label.setUser(user);
			labelService.updateLabel(label);
			response.setMessage("succesfull");
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			response.setMessage("Token Expired");
		}
		return response;
	}
	
	@RequestMapping(value = "/deleteLabel", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response deleteLabel(@RequestBody Label label , HttpServletRequest request) {
		String token = request.getHeader("token");
		Response response = new Response();
		try {
			Claims claim = tokenOperation.parseTheToken(KEY, token);
			String emailId = (String) claim.get("emailId");
			User user = userService.getUserByEmail(emailId);
			label.setUser(user);
			labelService.deleteLabel(label);
			response.setMessage("succesfull");
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			response.setMessage("Token Expired");
		}
		return response;
	}
	
	@RequestMapping(value = "/createLabel", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response createLabel(@RequestBody Label label, HttpServletRequest request) {
		String token = request.getHeader("token");
		Response response = new Response();
		try {
			Claims claim = tokenOperation.parseTheToken(KEY, token);
			String emailId = (String) claim.get("emailId");
			User user = userService.getUserByEmail(emailId);
			label.setUser(user);
			labelService.createLabel(label);
			response.setMessage("succesfull");
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			response.setMessage("Token Expired");
		}
		return response;
	}
}
