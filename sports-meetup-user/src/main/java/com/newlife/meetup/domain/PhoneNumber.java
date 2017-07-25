package com.newlife.meetup.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

@Entity
@Component
public class PhoneNumber {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String number;

	
	
	public PhoneNumber(String number) {
		super();
		this.number = number;
	}


	public PhoneNumber() {
		super();
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getNumber() {
		return number;
	}


	public void setNumber(String number) {
		this.number = number;
	}
	
	
}
