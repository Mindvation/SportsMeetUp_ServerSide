package com.newlife.meetup.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.newlife.meetup.domain.PhoneNumber;
import com.newlife.meetup.domain.User;
import com.newlife.meetup.repository.PhoneNumberRepositery;
import com.newlife.meetup.repository.UserRepository;
import com.newlife.meetup.service.IUserService;
import com.newlife.meetup.util.ResponseUtil;

@Service
public class UserServiceImpl implements IUserService {

	private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PhoneNumberRepositery phoneNumberRepositery;
	
	@Autowired
	private PhoneNumber number;
	
	@Autowired
	private ResponseUtil responseUtil;
	
	//valid phoneNumber
	/**
	 * N means the number is used.
	 * Y means the number is usable.
	 */
	@Override
	public String checkPhoneNumber(String phoneNumber) {
		String isUsable = "N";
		try {
			 number = this.phoneNumberRepositery.findPhoneNumberByNumber(phoneNumber);
//			 List<User> users = this.userRepository.findUserByPhoneNumber(phoneNumber);
			 if(number.getNumber().equals(phoneNumber)) {
				 isUsable = "N";
			 }else {
				 isUsable = "Y";
			 }
		}catch (Exception e) {
			LOGGER.debug("Some issue occurred while running method checkUser()");
			isUsable = "Y";
		}
		return isUsable;
	}
	
	//addUser 
	@Override
	public ResponseUtil addUser(User user) {
		Object result = null;
		String isUsable = checkPhoneNumber(user.getPhoneNumber());
		try {
			if(isUsable.equals("Y")) {
				this.userRepository.save(user);
				if(number==null) {
					number = new PhoneNumber(user.getPhoneNumber());
				}
				this.phoneNumberRepositery.save(number);
				responseUtil.setResponseCode("RS100");
				responseUtil.setMessage("注册成功！");
			}else{
				responseUtil.setResponseCode("RE001");
				responseUtil.setMessage("账户已经存在！");
			}
		}catch (Exception e) {
			
		}
		return responseUtil;
	}

	@Override
	public String checkUser(User user) {
		User user2 = findUserByPhoneNumber(user.getPhoneNumber());
		if(user.equals(user2)) {
			return "Y";
		}else {
			return "N";
		}
	}
	
	public User findUserByPhoneNumber(String phoneNumber){
		List<User> users = this.userRepository.findUserByPhoneNumber(phoneNumber);
		if(users.size()!=0) {
			return users.get(0);
		}else {
			return null;
		}
	}

	@Override
	public String checkUser(String phoneNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String checkPhoneNumber(User user) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
