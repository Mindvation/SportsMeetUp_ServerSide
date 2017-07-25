package com.newlife.meetup.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newlife.meetup.domain.User;


public interface UserRepository extends JpaRepository<User, Long> {


	List<User> findUserByPhoneNumber(String phoneNumber);


}
