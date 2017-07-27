package com.sports.meetup.user.utils;

import org.springframework.stereotype.Component;

@Component
public class CustomResponse {
	private static final String SUCCESS_CODE = "OK";
	private static final String SUCCESS_MESSAGE = "SUCCESS";
	
	public CustomResponse responseHandler(Object obj) {
		CustomResponse response = new CustomResponse();
		response.setResponseCode(SUCCESS_CODE);
		response.setMessage(SUCCESS_MESSAGE);
		response.setResponseBody(obj);
		return response;
	}
	
	private String responseCode;
	
	private String message;
	
	private Object responseBody;

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(Object responseBody) {
		this.responseBody = responseBody;
	}
}
