package com.newlife.meetup.service;

import org.springframework.stereotype.Service;

import com.aliyuncs.exceptions.ClientException;
import com.newlife.meetup.util.ResponseUtil;

@Service
public interface ISendSmsService {

	ResponseUtil getVerificationCode(String phoneNumber);
	
}
