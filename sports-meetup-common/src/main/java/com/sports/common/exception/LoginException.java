package com.sports.common.exception;

public class LoginException extends Exception {
	
	private static final long serialVersionUID = 1L;

	private String errorCode;
	
	private String message;

	public LoginException(String errorCode, String message) {
		super();
		this.errorCode = errorCode;
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
