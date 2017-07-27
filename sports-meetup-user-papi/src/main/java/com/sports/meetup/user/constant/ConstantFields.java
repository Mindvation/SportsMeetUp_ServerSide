package com.sports.meetup.user.constant;

public class ConstantFields {
	private final static String SUCCESS_RESPONSE_CODE ="000";
	private final static String SUCCESS_RESPONSE_MSG ="请求成功!";
	private final static String BAD_REQUEST_CODE = "BAD_REQUEST_400";
	private final static String LOGIN_FAILD_ERROR501_CODE = "LOGIN_FAILD_ERROR_501";
	private final static String LOGIN_FAILD_ERROR501_MSG = "用户名不存在,请注册后登录.";
	private final static String LOGIN_FAILD_ERROR502_CODE = "LOGIN_FAILD_ERROR_502";
	private final static String LOGIN_FAILD_ERROR502_MSG = "用户名或密码错误.";
	private final static String BAD_REQUEST_ERROR_CODE = "BAD_REQUEST_ERROR_400";
	
	
	public static String getSuccessResponseMsg() {
		return SUCCESS_RESPONSE_MSG;
	}
	public static String getSuccessResponseCode() {
		return SUCCESS_RESPONSE_CODE;
	}
	public static String getBadRequestErrorCode() {
		return BAD_REQUEST_ERROR_CODE;
	}
	public static String getBadRequestCode() {
		return BAD_REQUEST_CODE;
	}
	public static String getLoginFaildError501Code() {
		return LOGIN_FAILD_ERROR501_CODE;
	}
	public static String getLoginFaildError501Msg() {
		return LOGIN_FAILD_ERROR501_MSG;
	}
	public static String getLoginFaildError502Code() {
		return LOGIN_FAILD_ERROR502_CODE;
	}
	public static String getLoginFaildError502Msg() {
		return LOGIN_FAILD_ERROR502_MSG;
	}
	
}
