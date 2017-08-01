package com.newlife.meetup.service;

import org.springframework.stereotype.Service;

import com.newlife.meetup.domain.User;
import com.newlife.meetup.util.ResponseUtil;

@Service
public interface IUserService {

	String checkUser(String phoneNumber);

	ResponseUtil addUser(User user);

	String checkPhoneNumber(User user);

	String checkUser(User user);

	String checkPhoneNumber(String phoneNumber);

	ResponseUtil updatePassword(User user);

}
