package com.newlife.meetup.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.exceptions.ClientException;
import com.newlife.meetup.service.ISendSmsService;
import com.newlife.meetup.util.ResponseUtil;

@RestController
@RequestMapping({"/sports-meetup/users", "/sports-meetup/users/v1.0"})
public class SendSmsController {
	
	@Autowired
	private ISendSmsService sendSmsService;
	
	@Autowired
	private ResponseUtil responseUtil;
	
	@GetMapping(value="/getVerificationCode/{phoneNumber}")
	public ResponseUtil sendVerificationCode( @PathVariable  String phoneNumber) throws ClientException {
		
		if(phoneNumber.length()!=11) {
			responseUtil.setResponseCode("BAD_REQUEST_400");
			responseUtil.setMessage("请输入有效手机号.");
			return responseUtil;
		}
		return sendSmsService.getVerificationCode(phoneNumber);
	}
	
	@GetMapping(value="/getVerificationCode/{phoneNumber}/{flag}")
	public ResponseUtil sendVerificationCode(@PathVariable String phoneNumber, @PathVariable int flag) throws ClientException {
		if(phoneNumber.length()!=11) {
			responseUtil.setResponseCode("BAD_REQUEST_400");
			responseUtil.setMessage("请输入有效手机号.");
			return responseUtil;
		}
		return sendSmsService.getVerificationCode(phoneNumber, flag);
	}
}
