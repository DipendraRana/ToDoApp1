package com.bridgelabz.service;

import java.io.IOException;

import com.bridgelabz.model.User;

public interface LoginService {
	
	public String validationOfEmailId(String emailId);

	public String validationOfPassword(String password);
	
	public User validateTheUser(String emailId) throws IOException;
}
