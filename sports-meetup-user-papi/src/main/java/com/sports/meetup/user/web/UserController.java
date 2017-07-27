package com.sports.meetup.user.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sports.meetup.user.domain.User;
import com.sports.meetup.user.exception.BadRequestException;
import com.sports.meetup.user.exception.LoginFaildException;
import com.sports.meetup.user.service.impl.UserServiceImpl;

@RestController
@RequestMapping(value={"/users", "/v1.0/users"})
public class UserController {
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	private static final String CLASS = "UserController";
	
	@Autowired
	private UserServiceImpl userService;
	
	
	
	@PostMapping(value="/login")
	public ResponseEntity<?> login(@Validated @RequestBody User user, BindingResult bindResult) throws Exception{
		
		if(LOG.isDebugEnabled()) {
			LOG.debug("========  Enter {}, method login() ========" + this.CLASS);
		}
		if(bindResult.hasErrors()) {
			String errorMsg = bindResult.getAllErrors().get(0).getDefaultMessage();
			LOG.error(errorMsg);
			throw new BadRequestException(errorMsg);
		}
		return userService.login(user);
	}
	
	@GetMapping(value="/getVerificationCode")
	public ResponseEntity<?> getVerificationCode(@PathVariable String phoneNumber){
		
		return userService.getVerificationCode(phoneNumber);
	}
	
	@PostMapping(value="/register")
	public ResponseEntity<?> register(@RequestBody @Validated User user){
		
		 return userService.register(user);
	}
	
	
}
