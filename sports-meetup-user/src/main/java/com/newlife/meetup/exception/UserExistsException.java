package com.newlife.meetup.exception;

public class UserExistsException extends Exception {
	static final String USER_EXISTS = "USER_EXISTS_ERROR";

	
	public static String getUserExists() {
		return USER_EXISTS;
	}
	
	
	
	
}
