package com.sports.meetup.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "sapi.config")
@Component
public class SpringConfig {
	
	private String findUserByPhoneNumberUrl;

	public String getFindUserByPhoneNumberUrl() {
		return findUserByPhoneNumberUrl;
	}

	public void setFindUserByPhoneNumberUrl(String findUserByPhoneNumberUrl) {
		this.findUserByPhoneNumberUrl = findUserByPhoneNumberUrl;
	}

}
