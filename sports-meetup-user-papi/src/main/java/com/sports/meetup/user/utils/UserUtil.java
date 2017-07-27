package com.sports.meetup.user.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sports.meetup.user.domain.User;

public class UserUtil {
	private static final Logger LOG = LoggerFactory.getLogger(UserUtil.class);
	
	public static String checkLoginUser(User loginUser, User dbUser) {
		String result = "N";
		if(loginUser.getPhoneNumber().equals(dbUser.getPhoneNumber())) {
			result="Y";
		}else {
			LOG.error("======== 登录用户名不存在 ========");
			return result;
		}
		if(loginUser.getPassword().equals(dbUser.getPassword())) {
			result="Y";
		}else {
			LOG.error("======== 登录密码错误 ========");
			result = "N";
		}
		return result;
	}
	
	public String checkPhoneNumber(String phoneNumber){
		
		return "";
	}
}
