package com.bridgelabz.service;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.dao.RegistrationDao;
import com.bridgelabz.model.User;

@Service
@Transactional
public class RegistrationSerivceImplement implements RegistrationService {
	
	@Autowired
	private RegistrationDao registrationdao;
	
	@Override
	public void register(User user) throws PersistenceException {
		registrationdao.register(user);
	}

	@Override
	public int updateTheValidationToken(int id) {
		return registrationdao.updateTheValidationToken(id);
	}
	
	@Override
	public int deleteTheUserForFailedValidation(int id) {
		return registrationdao.deleteTheUserForFailedValidation(id);
	}

}
