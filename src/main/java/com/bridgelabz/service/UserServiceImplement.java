package com.bridgelabz.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.dao.UserDao;
import com.bridgelabz.model.User;

@Service
@Transactional
public class UserServiceImplement implements UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public List<String> getAllUser(){
		return userDao.getAllUser();
	}

	@Override
	public User getUserByEmail(String emailId) {
		return userDao.getUserByEmail(emailId);
	}

	@Override
	public int updatePasswordOfUser(String password, int id) {
		return userDao.updatePasswordOfUser(password, id);
	}
	
	@Override
	public void updateUser(User user) {
		userDao.updateUser(user);;
	}
	
	@Override
	public String getUserPassword(String emailId) {
		return userDao.getUserPassword(emailId);
	}

}
