package com.sports.meetup.user.service;

import org.springframework.stereotype.Service;

import com.sports.meetup.user.domain.User;

@Service
public interface IUserService {

	User getUser(String phoneNumber);

}
