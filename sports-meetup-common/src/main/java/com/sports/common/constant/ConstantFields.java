package com.sports.common.constant;

public class ConstantFields {
	private final static String SUCCESS_RESPONSE_CODE ="000";
	private final static String SUCCESS_RESPONSE_MSG ="请求成功!";
	private final static String BAD_REQUEST_CODE = "BAD_REQUEST_400";
	private final static String USER_ERROR501_CODE = "USER_ERROR_501";
	private final static String USER_ERROR501_MSG = "用户不存在,请注册.";
	private final static String USER_ERROR502_CODE = "USER_ERROR_502";
	private final static String USER_ERROR502_MSG = "用户名或密码错误.";
	private final static String USER_ERROR503_CODE = "USER_ERROR_503";
	private final static String USER_ERROR503_MSG ="用户已存在.";
	private final static String NOT_FOUND_CODE = "NOT_FOUND_404"; 
	private final static String NOT_FOUND_MSG = "请求有误，请核对链接地址."; 
	private final static String USER_ERROR_504 = "USER_ERROR_504";
	private final static String USER_ERROR504_MSG = "验证码发送失败,请重新获取.";
	private final static String USER_ERROR505_CODE = "USER_ERROR_505";
	private final static String USER_ERROR505_MSG = "验证码已失效,请重新获取.";
	private final static String USER_ERROR506_CODE = "USER_ERROR_506";
	private final static String USER_ERROR506_MSG = "新旧密码不能相同.";
	private final static String USER_ERROR507_CODE = "USER_ERROR_507";
	private final static String USER_ERROR507_MSG = "用户账户异常.";
	
	
	public static String getUserError507Code() {
		return USER_ERROR507_CODE;
	}
	public static String getUserError507Msg() {
		return USER_ERROR507_MSG;
	}
	public static String getUserError506Code() {
		return USER_ERROR506_CODE;
	}
	public static String getUserError506Msg() {
		return USER_ERROR506_MSG;
	}
	public static String getUserError504() {
		return USER_ERROR_504;
	}
	public static String getUserError504Msg() {
		return USER_ERROR504_MSG;
	}
	public static String getUserError505Code() {
		return USER_ERROR505_CODE;
	}
	public static String getUserError505Msg() {
		return USER_ERROR505_MSG;
	}
	public static String getUserError503Code() {
		return USER_ERROR503_CODE;
	}
	public static String getUserError503Msg() {
		return USER_ERROR503_MSG;
	}
	public static String getSuccessResponseMsg() {
		return SUCCESS_RESPONSE_MSG;
	}
	public static String getNotFoundCode() {
		return NOT_FOUND_CODE;
	}
	public static String getNotFoundMsg() {
		return NOT_FOUND_MSG;
	}
	public static String getSuccessResponseCode() {
		return SUCCESS_RESPONSE_CODE;
	}

	public static String getBadRequestCode() {
		return BAD_REQUEST_CODE;
	}
	public static String getUserError501Code() {
		return USER_ERROR501_CODE;
	}
	public static String getUserError501Msg() {
		return USER_ERROR501_MSG;
	}
	public static String getUserError502Code() {
		return USER_ERROR502_CODE;
	}
	public static String getUserError502Msg() {
		return USER_ERROR502_MSG;
	}
	
	
}
