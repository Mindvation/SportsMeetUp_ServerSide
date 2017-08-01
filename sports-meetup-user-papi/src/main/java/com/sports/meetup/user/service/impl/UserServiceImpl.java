package com.sports.meetup.user.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sports.common.constant.ConstantFields;
import com.sports.common.domain.ApiDefaultResponse;
import com.sports.common.exception.LoginException;
import com.sports.meetup.user.config.SpringConfig;
import com.sports.meetup.user.domain.User;
import com.sports.meetup.user.service.IUserService;
import com.sports.meetup.user.utils.UserUtil;

@Service
@Component
public class UserServiceImpl implements IUserService{

	private final static Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
	private final static String CLASS = "UserServiceImpl";
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private SpringConfig config;
	
	@Autowired
	private User dbUser;
	
	
	/**
	 * login: user data passes from FE:
	 * 1.get user detail from sapi in userDB by user;
	 * 2.check whether the user data from FE eaquals user detail from db;
	 * 3.give response to FE;
	 * @param user
	 * @return
	 * @throws LoginFaildException 
	 * @throws Exception 
	 * @throws UserNotExistException 
	 */
	public ResponseEntity<?> login(User user) throws LoginException {
		if(LOG.isDebugEnabled()) {
			LOG.debug("======== enter class {} to call login method ========", UserServiceImpl.CLASS);
		}
		ResponseEntity<ApiDefaultResponse> response = null;
		// 1. get user from sapi by phoneNumber
		dbUser = findUserByPhoneNumber(user.getPhoneNumber());
		if(dbUser==null) {
			LOG.error("======== 登录时, 用户不存在 ========");
			throw new LoginException(ConstantFields.getUserError501Code(),ConstantFields.getUserError501Msg());
		}
		if("Y".equals(UserUtil.checkLoginUser(user, dbUser))) {
			response = new ResponseEntity<ApiDefaultResponse>(
					new ApiDefaultResponse(ConstantFields.getSuccessResponseCode(), ConstantFields.getSuccessResponseMsg(), dbUser), HttpStatus.OK);
		}else {
			LOG.error("======== 登录时, 密码错误 ========");
			throw new LoginException(ConstantFields.getUserError502Code(), ConstantFields.getUserError502Msg());
		}
		
		if(LOG.isDebugEnabled()) {
			LOG.debug("======== out method login ========");
		}
		return response;
	}

	public User findUserByPhoneNumber(String phoneNumber) throws RuntimeException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> requestEntity = new HttpEntity<>(phoneNumber, headers);
		String url = config.getFindUserByPhoneNumberUrl();
		//1.call sapi to get userByPhoneNumber from 
		ResponseEntity<User> responseEntity = null;
		try {
			responseEntity = this.restTemplate.exchange(url, HttpMethod.GET, requestEntity, User.class, phoneNumber);

			if(responseEntity.getStatusCode().equals(HttpStatus.OK)) {
				dbUser = responseEntity.getBody();
			}else {
				throw new RuntimeException();
			}
		}catch (Exception e) {
			LOG.error("调用SAPI异常"+e.getMessage());
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
		
		return dbUser;
	}
	
	/**
	 * 获取验证码
	 * 1. 校验手机号：1.1 是否有效手机号 1.2 是否已注册
	 * 2. 生成验证码：2.1 生成验证码 2.2 保存验证码
	 * 3. 发送验证码给第三方平台：
	 * @param phoneNumber
	 * @return
	 */
	public ResponseEntity<?> getVerificationCode(String phoneNumber) {
		
		
		return null;
	}

	/**
	 * 用户注册：
	 * 1. 校验手机号 1.1 手机号是否为有效号码 1.2查询数据库校验手机号是否已注册
	 * 2. 校验验证码：2.1 验证码非空 
	 * 2.2 验证码有效 2.2.1 非空且 长度为6 2.2.2 isUsed 为 false 2.2.3 expiredAt大于 当前时间
	 * 2.3 更新验证码usingAt, isUsed 属性 到数据库
	 * 3.校验密码非空
	 * 4. 保存用户到user库
	 * @param user
	 * @return
	 */
	public ResponseEntity<?> register(User user) {
		// 1.1 手机号是否为有效号码  
//		1.2查询数据库校验手机号是否已注册
//		String isUsed = checkPhoneNumber(user.getPhoneNumber());
		
		return null;
	}

	/**
	 * 1. 校验用户
	 * 1.1 用户是否存在 
	 * 1.2 用户验证码是否正确
	 * 1.3 新密码是否与旧密码重复
	 */
	public ResponseEntity<?> updatePassword(User user) {
//		this.restTemplate.exchange(, method, requestEntity, responseType);
		return null;
	}
		
}
