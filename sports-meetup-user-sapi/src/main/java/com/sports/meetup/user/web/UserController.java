package com.sports.meetup.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sports.meetup.user.domain.User;
import com.sports.meetup.user.service.IUserService;

@RestController
@RequestMapping({"/users", "/v1.0/users"})
/*@RequestMapping({"/sports-meetup-sapi/users", "/sports-meetup/users/v1.0"})*/
public class UserController {
	
	@Autowired
	private IUserService userService;
	
	@GetMapping("/getUser/{phoneNumber}")
	public User getUser(@PathVariable String phoneNumber) {
		return this.userService.getUser(phoneNumber);
	}
	
}	
