package com.bridgelabz.filters;

import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

public class LoginValidate {

	private static final String regularExpressionForEmailId = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";

	public static boolean validateEmailId(String emailId, HttpSession session) {
		if (emailId != null) {
			if (emailId.equals("")) {
				session.setAttribute("error", "Enter the email-ID");
				return false;
			} else if (!Pattern.matches(regularExpressionForEmailId, emailId)) {
				session.setAttribute("error", "Invalid Email-ID");
				return false;
			} else
				return true;
		} else {
			session.setAttribute("error", "Enter the email-ID");
			return false;
		}
	}

	public static boolean validatePassword(String password, HttpSession session) {
		if (password != null) {
			if (password.equals("")) {
				session.setAttribute("error", "Password Cannot be Empty");
				return false;
			} else if (password.length() <= 5) {
				session.setAttribute("error", "Password must be greater than 4");
				return false;
			} else
				return true;
		} else {
			session.setAttribute("error", "Password Cannot be Empty");
			return false;
		}

	}
}
