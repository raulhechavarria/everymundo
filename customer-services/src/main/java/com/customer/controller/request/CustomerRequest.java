package com.customer.controller.request;

import java.util.Date;

import org.springframework.data.web.JsonPath;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CustomerRequest{

	/**
	 * 
	 */
	private long id;
	
	private String name;
	
	private String lastName;
	
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date dob;
	
	
	private boolean active;
	
	private String email;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}


	
	

}
