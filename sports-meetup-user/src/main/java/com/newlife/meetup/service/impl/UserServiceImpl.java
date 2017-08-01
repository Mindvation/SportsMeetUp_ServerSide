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
import com.newlife.meetup.repository.UserJpaRepository;
import com.newlife.meetup.service.IUserService;
import com.newlife.meetup.util.EncryptionUtil;
import com.newlife.meetup.util.ResponseUtil;

@Service
public class UserServiceImpl implements IUserService {

	private final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserJpaRepository userJpaRepository;
	
	@Autowired
	private ResponseUtil responseUtil;
	
	@Autowired
	private CheckCodeRepository checkCodeRepository;

	
	@Autowired
	private CheckCode checkCode;
	
	//valid phoneNumber
	/**
	 * Y means the number is used.
	 * N means the number is not used.
	 */
	@Override
	public String checkPhoneNumber(String phoneNumber) {
		String isUsed = "N";
		try {
//			 number = this.phoneNumberRepositery.findPhoneNumberByNumber(phoneNumber);
			 List<User> users = this.userJpaRepository.findUserByPhoneNumber(phoneNumber);
			 if(users.size()>0) {
				 isUsed = "Y";
			 }else {
				 isUsed = "N";
			 }
		}catch (Exception e) {
			LOG.debug("Some issue occurred while running method checkUser()");
		}
		return isUsed;
	}
	
	//addUser 
	@Override
	@Transactional
	public ResponseUtil addUser(User user) {
		String isUsed = checkPhoneNumber(user.getPhoneNumber());
		 if(isUsed.equals("Y")) {
			 responseUtil.setResponseCode("USER_ERROR_503");
			 responseUtil.setMessage("用户已存在.");
			 return responseUtil;
		 }
//		 2 校验用户验证码
		 String passed = checkVerificationCode(user);
		 if(passed.equals("N")) {
			 //跟新checkCode数据以用过
			 checkCodeRepository.saveAndFlush(checkCode);
			 responseUtil.setResponseCode("USER_ERROR_505");
			 responseUtil.setMessage("验证码已失效,请重新获取.");
			 return responseUtil;
		 }
		try {
			if(isUsed.equals("N")&&passed.equals("Y")) {
				user.setPassword(EncryptionUtil.getEncryptString(user.getPassword()));
				this.userJpaRepository.save(user);
				checkCode.setIsUsed(true);
				checkCode.setUsingAt(new Timestamp(System.currentTimeMillis()));
				this.checkCodeRepository.saveAndFlush(checkCode);
				responseUtil.setResponseCode("000");
				responseUtil.setMessage("注册成功！");
				
			}
		}catch (Exception e) {
			
		}
		return responseUtil;
	}
	
	/*
	 * 更新密码
	 * 1. 校验用户是否存在
	 * 2.校验验证码
	 * 3.更新数据库
	 * (non-Javadoc)
	 * @see com.newlife.meetup.service.IUserService#updatePassword(com.newlife.meetup.domain.User)
	 */
	@Override
	@Transactional
	public ResponseUtil updatePassword(User user) {
		 List<User> users = this.userJpaRepository.findUserByPhoneNumber(user.getPhoneNumber());
		 if(users.size()==0) {
			 LOG.error("========= 用户未注册 ========");
			 responseUtil.setResponseCode("USER_ERROR_501");
			 responseUtil.setMessage("用户不存在,请注册.");
			 return responseUtil;
		 }
//		1. 校验用户是否存在
		String isUsed = checkPhoneNumber(user.getPhoneNumber());
		 if(isUsed.equals("N")) {
			 LOG.error("========= 用户未注册 ========");
			 responseUtil.setResponseCode("USER_ERROR_501");
			 responseUtil.setMessage("用户不存在,请注册.");
			 return responseUtil;
		 }

//		 校验密码
		 if(EncryptionUtil.getEncryptString(user.getPassword()).equals(users.get(0).getPassword())) {
			 LOG.error("密码和旧密码重复.");
			 responseUtil.setResponseCode("USER_ERROR_506");
			 responseUtil.setMessage("新旧密码不能相同.");
			 return responseUtil;
		 }
//		 2.校验验证码
		 String passed = checkVerificationCode(user);
		 if(passed.equals("N")) {
			 //跟新checkCode数据以用过
			 responseUtil.setResponseCode("USER_ERROR_505");
			 responseUtil.setMessage("验证码已失效,请重新获取.");
			 return responseUtil;
		 }
		 
//		 3.更新数据库()
		 if(isUsed.equals("Y")&&passed.equals("Y")) {
			this.userJpaRepository.delete(users.get(0));
			this.userJpaRepository.save(user);
					responseUtil.setResponseCode("000");
					responseUtil.setMessage("请求成功!");
			}
		return responseUtil;
	}
	 //校验用户验证码
		@Transactional
		public String checkVerificationCode(User user) {
			String result = "N";
			String verificationCode = "";
			try {
				checkCode = checkCodeRepository.findOne(user.getPhoneNumber());
				if(checkCode == null) {
					return result;
				}
				verificationCode = checkCode.getCode();
				if(checkCode.getIsUsed()) {
					return result;
				}
				if(checkCode.getExpireAt().before(new Timestamp(System.currentTimeMillis()))) {
					return result;
				}
				if (EncryptionUtil.getEncryptString(user.getVerificationCode()).equals(verificationCode)) {
					checkCode.setUsingAt(new Timestamp(System.currentTimeMillis()));
					checkCode.setIsUsed(true);
					checkCodeRepository.saveAndFlush(checkCode);
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
		List<User> users = this.userJpaRepository.findUserByPhoneNumber(phoneNumber);
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
