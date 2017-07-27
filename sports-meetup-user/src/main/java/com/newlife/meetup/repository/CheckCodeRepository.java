package com.newlife.meetup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newlife.meetup.domain.CheckCode;

public interface CheckCodeRepository extends JpaRepository<CheckCode, String> {

	CheckCode findCheckCodeByCode(String verificationCode);


	

}
