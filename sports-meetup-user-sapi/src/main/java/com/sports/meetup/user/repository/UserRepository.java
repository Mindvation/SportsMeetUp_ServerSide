package com.sports.meetup.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sports.meetup.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User getUserByPhoneNumber(String phoneNumber);

}
