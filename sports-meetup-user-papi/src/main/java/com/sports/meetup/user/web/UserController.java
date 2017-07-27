package com.sports.meetup.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sports.meetup.user.domain.User;
import com.sports.meetup.user.service.impl.UserServiceImpl;
import com.sports.meetup.user.utils.CustomResponse;

@RestController
@RequestMapping(value={"/users", "/v1.0/users"})
public class UserController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private CustomResponse customResponse;
	
	@PostMapping(value="/login")
	public ResponseEntity<?> login(@Validated @RequestBody User user, BindingResult bindResult){
		if(bindResult.hasErrors()) {
			customResponse.setMessage(bindResult.getAllErrors().get(0).getDefaultMessage());
			customResponse.setResponseCode("LE002");
			return new ResponseEntity<CustomResponse>(customResponse, HttpStatus.BAD_REQUEST);
		}
		return userService.login(user);
	}
	
}
