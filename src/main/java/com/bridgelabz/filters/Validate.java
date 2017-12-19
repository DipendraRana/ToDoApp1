package com.bridgelabz.filters;

import java.util.regex.Pattern;

public class Validate {

	private static final String regularExpressionForEmailId = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";

	private static final String regularExpressionForUserName = "^[A-Za-z ]+$";

	private static final String regularExpressionForMobileNumber = "^[0-9]+$";

	public static boolean validateUserName(String userName) {
		if (userName != null) {
			if (userName.equals(""))
				return false;
			else if (!Pattern.matches(regularExpressionForUserName, userName))
				return false;
			else
				return true;
		} else
			return false;
	}

	public static boolean validateEmailId(String emailId) {
		if (emailId != null) {
			if (emailId.equals(""))
				return false;
			else if (!Pattern.matches(regularExpressionForEmailId, emailId))
				return false;
			else
				return true;
		} else 
			return false;
	}

	public static boolean validatePassword(String password) {
		if (password != null) {
			if (password.equals(""))
				return false;
			else if (password.length() <= 5)
				return false;
			else
				return true;
		} else
			return false;

	}

	public static boolean validateMobileNumber(long mobileNumber) {
		if (mobileNumber != 0L) {
			String stringMobileNumber = String.valueOf(mobileNumber);
			if (stringMobileNumber != null) {
				if (stringMobileNumber.equals("0"))
					return false;
				else if (!Pattern.matches(regularExpressionForMobileNumber, stringMobileNumber))
					return false;
				else if (stringMobileNumber.length() != 10)
					return false;
				else
					return true;
			} else
				return false;
		} else
			return false;
	}

}
