package com.sports.meetup.user.utils;

import com.sports.meetup.user.domain.User;

public class UserUtil {
	
	public static String checkLoginUser(User loginUser, User dbUser) {
		String result = "N";
		if(loginUser.getPhoneNumber().equals(dbUser.getPhoneNumber())) {
			result="Y";
		}
		if(loginUser.getPassword().equals(dbUser.getPassword())) {
			result="Y";
		}
		return result;
	}
}
