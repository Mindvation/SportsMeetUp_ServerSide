package com.newlife.meetup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newlife.meetup.domain.PhoneNumber;

public interface PhoneNumberRepositery extends JpaRepository<PhoneNumber, Long> {

	PhoneNumber findPhoneNumberByNumber(String phoneNumber);

}
