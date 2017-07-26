package com.sports.meetup.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sports.meetup.user.domain.User;
import com.sports.meetup.user.repository.UserRepository;
import com.sports.meetup.user.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User getUser(String phoneNumber) {
		return userRepository.getUserByPhoneNumber(phoneNumber);
	}

}
