package com.bridgelabz.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImplement implements EmailService {
	
	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void sendMail(String to, String subject, String message) throws MessagingException {
		mailSender.send(new MimeMessagePreparator(){

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage, true);
				mimeMessageHelper.setTo(to);
				mimeMessageHelper.setSubject(subject);
				mimeMessageHelper.setText(message, true);
			}
		});
	}

}
