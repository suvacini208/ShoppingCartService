package com.suva.cart.domain;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

public class User {
	
	@NotBlank(message = "Username cant be blank")
	private String userName;
	
	@NotBlank(message = "EmailId cant be blank")
	@Email(message="EmailId format is incorrect")
	private String emailId;
	
	@NotBlank(message = "Phone cant be blank")
	@Length(min=10, max=10)
	private String phone;
	
	@Valid
	@NotEmpty(message = "Address cant be emtpy")
	private List<Address> address = new ArrayList<>();
	public User() {}
	public User(@NotBlank(message = "Username cant be blank") String userName,
			@NotBlank(message = "EmailId cant be blank") @Email(message = "EmailId format is incorrect") String emailId,
			@NotBlank(message = "Phone cant be blank") @Length(min = 10, max = 10) String phone,
			@Valid @NotEmpty(message = "Address cant be emtpy") List<Address> address) {
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((emailId == null) ? 0 : emailId.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (emailId == null) {
			if (other.emailId != null)
				return false;
		} else if (!emailId.equals(other.emailId))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "User [userName=" + userName + ", emailId=" + emailId + ", phone=" + phone + ", address=" + address
				+ "]";
	}
}
