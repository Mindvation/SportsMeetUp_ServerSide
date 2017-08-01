package com.sports.common.domain;

import org.springframework.stereotype.Component;

@Component
public class ApiDefaultResponse {
	
	private String responseCode;
	private String message;
	private Object responseBody;
	
	
	public ApiDefaultResponse(String responseCode, String message) {
		super();
		this.responseCode = responseCode;
		this.message = message;
	}
	public ApiDefaultResponse(String responseCode, String message, Object responseBody) {
		super();
		this.responseCode = responseCode;
		this.message = message;
		this.responseBody = responseBody;
	}
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
