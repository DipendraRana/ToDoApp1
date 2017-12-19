package com.bridgelabz.dao;

import javax.persistence.PersistenceException;

import com.bridgelabz.model.User;

public interface RegistrationDao {
	
	public int register(User user) throws PersistenceException;
	
	public int updateTheValidationToken(int id);
	
	public int deleteTheUserForFailedValidation(int id);

}
