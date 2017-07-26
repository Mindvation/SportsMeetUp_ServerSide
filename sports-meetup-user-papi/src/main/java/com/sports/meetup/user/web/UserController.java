package com.sports.meetup.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sports.meetup.user.domain.User;
import com.sports.meetup.user.service.impl.UserServiceImpl;

@RestController
@RequestMapping(value="{papi/users, /papi/v1.0/users}")
public class UserController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@GetMapping(value="/login")
	public ResponseEntity<?> login(@RequestBody User user){
		
		return userService.login(user);
	}
	
}
