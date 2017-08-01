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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sports.common.exception.BadRequestException;
import com.sports.meetup.user.domain.User;
import com.sports.meetup.user.service.IUserService;

@RestController
@RequestMapping(value={"/users", "/v1.0/users"})
public class UserController {
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	private static final String CLASS = "UserController";
	
	@Autowired
	private IUserService userService;
	
	@PostMapping(value="/login")
	public ResponseEntity<?> login(@Validated @RequestBody User user, BindingResult bindResult) throws Exception{
		
		if(LOG.isDebugEnabled()) {
			LOG.debug("========  Enter {}, method login() ========" + UserController.CLASS);
		}
		if(bindResult.hasErrors()) {
			String errorMsg = bindResult.getAllErrors().get(0).getDefaultMessage();
			LOG.error(errorMsg);
			throw new BadRequestException(errorMsg);
		}
		return userService.login(user);
	}
	
	@PutMapping(value="/updatePassword")
	public ResponseEntity<?> updatePassword(@Validated @RequestBody User user, BindingResult bindResult) throws BadRequestException{
		if(bindResult.hasErrors()) {
			String errorMsg = bindResult.getAllErrors().get(0).getDefaultMessage();
			LOG.error(errorMsg);
			throw new BadRequestException(errorMsg);
		}
		return userService.updatePassword(user);
	}
	
	@GetMapping(value="/getVerificationCode/{phoneNumber}")
	public ResponseEntity<?> getVerificationCode(@PathVariable String phoneNumber){
		
		return userService.getVerificationCode(phoneNumber);
	}
	
	@PostMapping(value="/register")
	public ResponseEntity<?> register(@RequestBody @Validated User user){
		
		 return userService.register(user);
	}
	
	
}
