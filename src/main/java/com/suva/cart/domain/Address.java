package com.suva.cart.domain;

import javax.validation.constraints.NotBlank;

public class Address {

	@NotBlank(message = "AddressLine1 can't be blank")
	private String addressLine1;
	
	private String addressLine2;
	
	@NotBlank(message = "City can't be blank")
	private String city;
	
	@NotBlank(message = "State can't be blank")
	private String state;
	
	@NotBlank(message = "Zip can't be blank")
	private String zip;
	
	public Address() {}
	
	public Address(@NotBlank(message = "AddressLine1 can't be blank") String addressLine1, String addressLine2,
			@NotBlank(message = "City can't be blank") String city,
			@NotBlank(message = "State can't be blank") String state,
			@NotBlank(message = "Zip can't be blank") String zip) {
		super();
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}
	
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
