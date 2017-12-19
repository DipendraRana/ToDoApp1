package com.bridgelabz.dao;

import java.io.IOException;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bridgelabz.model.User;

@Repository
public class LoginDaoImplement implements LoginDao {

	@Autowired
	private SessionFactory sessionFactory;

	public User validateTheUser(String emailId) throws IOException, NullPointerException {
		Query<User> query;
		query = sessionFactory.getCurrentSession().createQuery(
				"from User where emailId =:Email_Id and validToken=:Valid_Token", User.class);
		query.setParameter("Email_Id", emailId);
		query.setParameter("Valid_Token", true);
		User user = (User) query.uniqueResult();
		return user;
	}
}
