package com.suva.cart.entity;

import java.util.ArrayList;
import java.util.List;

public class User {
	
	private String userName;
	private String emailId;
	private String phone;
	private List<Address> address = new ArrayList<>();
	public User() {}
	public User(String userName, String emailId, String phone, List<Address> address) {
		super();
		this.userName = userName;
		this.emailId = emailId;
		this.phone = phone;
		this.address = address;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public List<Address> getAddress() {
		return address;
	}
	public void setAddress(List<Address> address) {
		this.address = address;
	}
}
