package com.bridgelabz.service;

import java.util.List;

import com.bridgelabz.model.User;

public interface UserService {
	
	public List<String> getAllUser();
	
	public User getUserByEmail(String emailId);
	
	public int updatePasswordOfUser(String password,int id);
	
	public void updateUser(User user);

	public String getUserPassword(String emailId);
}
