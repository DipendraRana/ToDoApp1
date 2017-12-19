package com.bridgelabz.filters;

import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

public class RegistrationFormValidate {
	
	private static final String regularExpressionForEmailId="^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
	
	private static final String regularExpressionForUserName="^[A-Za-z ]+$";
	
	private static final String regularExpressionForMobileNumber="^[0-9]+$";
	
	public static boolean validateUserName(String userName,HttpSession session) {
		if(userName!=null) {
			if(userName.equals("")) {
				session.setAttribute("error", "Enter the User Name");
				return false;
			}
			else if(!Pattern.matches(regularExpressionForUserName, userName)) {
				session.setAttribute("error", "Invalid User Name");
				return false;
			}
			else
				return true;
		}
		else {
			session.setAttribute("error", "Enter the User Name");
			return false;
		}
		
	}
	
	public static boolean validateEmailId(String emailId, HttpSession session) {
		if(emailId!=null) {
			if(emailId.equals("")) {
				session.setAttribute("error", "Enter the email-ID");
				return false;
			}
			else if(!Pattern.matches(regularExpressionForEmailId, emailId)) {
				session.setAttribute("error", "Invalid Email-ID");
				return false;
			}
			else
				return true;
		}
		else {
			session.setAttribute("error", "Enter the email-ID");
			return false;
		}
	}
	
	public static boolean validatePassword(String password, HttpSession session) {
		if(password!=null) {
			if(password.equals("")) {
				session.setAttribute("error", "Password Cannot be Empty");
				return false;
			}
			else if(password.length()<=5){
				session.setAttribute("error", "Password must be greater than 4");
				return false;
			}
			else
				return true;
		}
		else {
			session.setAttribute("error", "Password Cannot be Empty");
			return false;
		}
		
	}
	
	public static boolean validateMobileNumber(long mobileNumber, HttpSession session) {
		if(mobileNumber!=0L) {
			String stringMobileNumber=String.valueOf(mobileNumber);
			if(stringMobileNumber!=null) {
				if(stringMobileNumber.equals("0")) {
					session.setAttribute("error", "Enter the Password");
					return false;
				}
				else if(!Pattern.matches(regularExpressionForMobileNumber, stringMobileNumber)) {
					session.setAttribute("error", "Invalid Mobile Number");
					return false;
				}
				else if(stringMobileNumber.length()!=10) {
					session.setAttribute("error", "Password Length must be 10");
					return false;
				}
				else
					return true;
			}
			else {
				session.setAttribute("error", "Enter the Password");
				return false;
			}
		}
		else {
			session.setAttribute("error", "Enter the Password");
			return false;
		}	
	}

}
