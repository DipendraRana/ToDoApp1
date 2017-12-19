package com.bridgelabz.dao;

import javax.persistence.PersistenceException;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bridgelabz.model.User;

@Repository("registrationDao")
public class RegistrationDaoImplement implements RegistrationDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public int register(User user) throws PersistenceException {
		return (int) sessionFactory.getCurrentSession().save(user);
	}

	@Override
	public int updateTheValidationToken(int id) {
		return sessionFactory.getCurrentSession().createQuery("update User set validToken=:Valid_Token where id=:Id")
				.setParameter("Valid_Token", true).setParameter("Id", id).executeUpdate();
	}

	@Override
	public int deleteTheUserForFailedValidation(int id) {
		return sessionFactory.getCurrentSession().createQuery("delete from User where id=:User_Id").setParameter("User_Id", id)
				.executeUpdate();
	}

}
