package com.sports.common.domain;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

@Component
public class VerificationCode {
	
	private String phoneNumber;
	
	private String code;
	
	private String ip;
	
	private Timestamp crateAt;
	
	private Timestamp ExpireAt;
	
	private Timestamp usingAt;
	
	private Boolean isUsed;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Timestamp getCrateAt() {
		return crateAt;
	}

	public void setCrateAt(Timestamp crateAt) {
		this.crateAt = crateAt;
	}

	public Timestamp getExpireAt() {
		return ExpireAt;
	}

	public void setExpireAt(Timestamp expireAt) {
		ExpireAt = expireAt;
	}

	public Timestamp getUsingAt() {
		return usingAt;
	}

	public void setUsingAt(Timestamp usingAt) {
		this.usingAt = usingAt;
	}

	public Boolean getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}

}
