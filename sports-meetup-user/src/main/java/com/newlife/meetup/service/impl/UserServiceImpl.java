package com.newlife.meetup.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newlife.meetup.domain.CheckCode;
import com.newlife.meetup.domain.User;
import com.newlife.meetup.repository.CheckCodeRepository;
import com.newlife.meetup.repository.UserRepository;
import com.newlife.meetup.service.IUserService;
import com.newlife.meetup.util.ResponseUtil;

@Service
public class UserServiceImpl implements IUserService {

	private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ResponseUtil responseUtil;
	
	@Autowired
	private CheckCodeRepository checkCodeRepository;
	
	@Autowired
	private CheckCode checkCode;
	
	//valid phoneNumber
	/**
	 * N means the number is used.
	 * Y means the number is usable.
	 */
	@Override
	public String checkPhoneNumber(String phoneNumber) {
		String isUsable = "N";
		try {
//			 number = this.phoneNumberRepositery.findPhoneNumberByNumber(phoneNumber);
			 List<User> users = this.userRepository.findUserByPhoneNumber(phoneNumber);
			 if(users.size()>0) {
				 isUsable = "N";
			 }else {
				 isUsable = "Y";
			 }
		}catch (Exception e) {
			LOGGER.debug("Some issue occurred while running method checkUser()");
		}
		return isUsable;
	}
	
	//addUser 
	@Override
	@Transactional
	public ResponseUtil addUser(User user) {
		String isUsable = checkPhoneNumber(user.getPhoneNumber());
		 if(isUsable.equals("N")) {
			 responseUtil.setResponseCode("RE001");
			 responseUtil.setMessage("账户已经存在！");
			 return responseUtil;
		 }
//		 2 校验用户验证码
		 String passed = checkVerificationCode(user);
		 if(passed.equals("N")) {
			 //跟新checkCode数据以用过
			 checkCodeRepository.saveAndFlush(checkCode);
			 responseUtil.setResponseCode("SS001");
			 responseUtil.setMessage("验证码已失效, 请重试获取验证码.");
			 return responseUtil;
		 }
		try {
			if(isUsable.equals("Y")&&passed.equals("Y")) {
				this.userRepository.save(user);
				checkCode.setIsUsed(true);
				checkCode.setUsingAt(new Timestamp(System.currentTimeMillis()));
				this.checkCodeRepository.save(checkCode);
				responseUtil.setResponseCode("RS100");
				responseUtil.setMessage("注册成功！");
				
			}
		}catch (Exception e) {
			
		}
		return responseUtil;
	}
	
	 //2 校验用户验证码
		@Transactional
		public String checkVerificationCode(User user) {
			String result = "N";
			String verificationCode = "";
			try {
				checkCode = checkCodeRepository.findOne(user.getPhoneNumber());
//				checkCodeRepository.updateCheckCode(checkCode.set);
				if(checkCode == null) {
					return result;
				}
				verificationCode = checkCode.getCode();
				if(checkCode.getIsUsed()) {
					return result;
				}
				if(checkCode.getExpireAt().before(new Timestamp(System.currentTimeMillis()))) {
					checkCode.setUsingAt(new Timestamp(System.currentTimeMillis()));
					checkCode.setIsUsed(true);
					checkCodeRepository.saveAndFlush(checkCode);
					return result;
				}
				if (user.getVerificationCode().equals(verificationCode)) {
					result = "Y";
				}
			}catch (Exception e) {
				if(verificationCode==null) {
					result = "N";
				}
			}
			return result;
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
