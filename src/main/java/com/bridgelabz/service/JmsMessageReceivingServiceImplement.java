package com.bridgelabz.service;

import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.bridgelabz.model.JmsData;

@Service
public class JmsMessageReceivingServiceImplement implements JmsMessageReceivingService {
	
	@Autowired
	private EmailService emailService;

	@Override
	@JmsListener(destination="MessageStorage")
	public void messageReceive(JmsData jmsData) {
		try {
			emailService.sendMail(jmsData.getEmailId(), "Link to actvate your account", jmsData.getMessage());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
