package com.sports.meetup.user.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sports.common.exception.LoginException;
import com.sports.meetup.user.domain.User;

@Service
public interface IUserService {

	ResponseEntity<?> login(User user) throws RuntimeException, LoginException;

	ResponseEntity<?> updatePassword(User user);

	ResponseEntity<?> getVerificationCode(String phoneNumber);

	ResponseEntity<?> register(User user);

}
