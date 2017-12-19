package com.bridgelabz.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bridgelabz.model.User;

@Repository
public class UserDaoImplement implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllUser() {
		return sessionFactory.getCurrentSession().createQuery("select user.emailId from User user").list();
	}

	@Override
	public User getUserByEmail(String emailId) {
		Query<User> query;
		query = sessionFactory.getCurrentSession().createQuery("from User where emailId =:Email_Id", User.class);
		query.setParameter("Email_Id", emailId);
		User user = (User) query.uniqueResult();
		return user;
	}

	@Override
	public int updatePasswordOfUser(String password, int id) {
		return sessionFactory.getCurrentSession().createQuery("update User set password=:Password where id=:User_Id")
				.setParameter("Password", password).setParameter("User_Id", id).executeUpdate();
	}

	@Override
	public void updateUser(User user) {
		sessionFactory.getCurrentSession().update(user);
	}

	public String getUserPassword(String emailId) {
		@SuppressWarnings("unchecked")
		List<String> listOfPassword = sessionFactory.getCurrentSession()
				.createQuery("select user.password from User user where emailId=:EmailId")
				.setParameter("EmailId", emailId).list();
		return listOfPassword.get(0);
		
	}

}
