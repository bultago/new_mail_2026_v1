package com.terracetech.tims.service.tms.vo;

public class EmailAddressVO {
	String personal = null;
	String address = null;
	public String getPersonal() {
		return personal;
	}
	public void setPersonal(String personal) {
		this.personal = personal;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getEmailAddress(){
		String email = "";
		if(personal != null){
			email = "\""+personal+"\" <"+address+">";
		} else {
			email = address;
		}		
		return email;
	}
}
