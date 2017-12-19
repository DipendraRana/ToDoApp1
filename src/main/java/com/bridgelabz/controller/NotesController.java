package com.bridgelabz.controller;

import java.io.IOException;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.model.Note;
import com.bridgelabz.model.Response;
import com.bridgelabz.model.UrlDataObject;
import com.bridgelabz.model.User;
import com.bridgelabz.service.GetUrlMetaData;
import com.bridgelabz.service.NoteService;
import com.bridgelabz.service.TokenOperation;
import com.bridgelabz.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

@RestController
public class NotesController {

	@Autowired
	private NoteService noteService;

	@Autowired
	private TokenOperation tokenOperation;

	@Autowired
	private UserService userService;
	
	@Autowired
	private GetUrlMetaData getUrlMetaData;

	private static final String KEY = "!12@3#abcde";

	@RequestMapping(value = "/getNotes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Note> gettingAllNotes(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader("token");
		List<Note> list = null;
		try {
			Claims claim = tokenOperation.parseTheToken(KEY, token);
			int id = Integer.parseInt(claim.getSubject());
			list = noteService.getTheNotes(id);
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			response.addHeader("Error", "Expired");
		}
		return list;
	}

	@RequestMapping(value = "/getCollaborators", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<User> gettingAllCollaboratorsOfTheNote(@RequestBody Note note, HttpServletRequest request,
			HttpServletResponse response) {
		String token = request.getHeader("token");
		List<User> list = null;
		try {
			tokenOperation.parseTheToken(KEY, token);
			list = noteService.getAllCollaboratedUserOfNote(note.getNoteId());
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			response.addHeader("Error", "Expired");
		}
		return list;
	}

	@RequestMapping(value = "/getOwner", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody User gettingTheOwner(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader("token");
		User user = null;
		try {
			Claims claim = tokenOperation.parseTheToken(KEY, token);
			String emailId = (String) claim.get("emailId");
			user = userService.getUserByEmail(emailId);
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			response.addHeader("Error", "Expired");
		}
		return user;
	}

	@RequestMapping(value = "/getAllUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<String> gettingAllUser(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader("token");
		List<String> allEmails = null;
		try {
			tokenOperation.parseTheToken(KEY, token);
			allEmails = userService.getAllUser();
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			response.addHeader("Error", "Expired");
		}
		return allEmails;
	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody User updatingTheUser(@RequestBody User user, HttpServletRequest request,
			HttpServletResponse response) {
		String token = request.getHeader("token");
		try {
			Claims claim = tokenOperation.parseTheToken(KEY, token);
			String emailId = (String) claim.get("emailId");
			user.setPassword(userService.getUserPassword(emailId));
			userService.updateUser(user);
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			response.addHeader("Error", "Expired");
		}
		return user;
	}

	@RequestMapping(value = "/addCollaborator", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<User> gettingTheSpecificUser(@RequestBody Note note, HttpServletRequest request,
			HttpServletResponse response) {
		String token = request.getHeader("token");
		String emailId = request.getHeader("emailId");
		try {
			tokenOperation.parseTheToken(KEY, token);
			User user = userService.getUserByEmail(emailId);
			note.setUser(note.getUser());
			note.getCollaboratedUser().add(user);
			noteService.updateTheNote(note);
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			response.addHeader("Error", "Expired");
		}
		return note.getCollaboratedUser();
	}

	@RequestMapping(value = "/saveNote", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveTheNote(@RequestBody Note note, HttpServletRequest request) {
		Response response = new Response();
		String token = request.getHeader("token");
		try {
			Claims claim = tokenOperation.parseTheToken(KEY, token);
			String emailId = (String) claim.get("emailId");
			User user = userService.getUserByEmail(emailId);
			note.setUser(user);
			int id = noteService.saveTheNote(note);
			if (id != 0)
				response.setMessage("Successfully saved");
			else
				response.setMessage("Failed to save");
			return response;
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			response.setMessage("Token Expired");
			return response;
		}
	}

	@RequestMapping(value = { "/updateNote" }, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateTheNote(@RequestBody Note note, HttpServletRequest request) {
		Response response = new Response();
		String token = request.getHeader("token");
		try {
			tokenOperation.parseTheToken(KEY, token);
			note.setUser(note.getUser());
			noteService.updateTheNote(note);
			response.setMessage("update succesfull");
			return response;
		} catch (PersistenceException e) {
			e.printStackTrace();
			response.setMessage("update failed");
			return response;
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			response.setMessage("Token Expired");
			return response;
		}
	}

	@RequestMapping(value = "/deleteNote", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response deleteTheNote(@RequestBody Note note, HttpServletRequest request) {
		Response response = new Response();
		String token = request.getHeader("token");
		try {
			tokenOperation.parseTheToken(KEY, token);
			note.setUser(note.getUser());
			int noOfRowsaffected = noteService.deleteTheNote(note);
			if (noOfRowsaffected != 0) {
				response.setMessage("note deleted");
				return response;
			} else {
				response.setMessage("note is not deleted");
				return response;
			}
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			response.setMessage("Token Expired");
			return response;
		}
	}

	@RequestMapping(value = "/emptyTrash", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response emptyTrash(HttpServletRequest request) {
		Response response = new Response();
		String token = request.getHeader("token");
		try {
			tokenOperation.parseTheToken(KEY, token);
			int noOfRowsaffected = noteService.emptyTrash();
			if (noOfRowsaffected != 0) {
				response.setMessage("note deleted");
				return response;
			} else {
				response.setMessage("note is not deleted");
				return response;
			}
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			response.setMessage("Token Expired");
			return response;
		}
	}

	@RequestMapping(value = "/urlMetadata", method = RequestMethod.PUT,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody UrlDataObject getUrlMetadata(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader("token");
		String url = request.getHeader("url");
		try {
			tokenOperation.parseTheToken(KEY, token);
			return getUrlMetaData.getMetadataFromUrl(url);		
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			response.addHeader("Error", "Expired");
		} catch (IOException e) {
			response.addHeader("Error", "Problem with link");
			e.printStackTrace();
		}
		return null;
	}

}
