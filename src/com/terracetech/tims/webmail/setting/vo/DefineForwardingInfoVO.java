package com.terracetech.tims.webmail.setting.vo;

import java.util.ArrayList;

public class DefineForwardingInfoVO{
	private int mail_user_seq;
	private int define_forwarding_seq;
	private String from_address;
	private String from_domain;
	
	private String forwarding_address;
	
	private String[] forwarding_address_arr;
	
	private ArrayList<String> forwarding_address_list;
	
	public int getMail_user_seq() {
		return mail_user_seq;
	}
	public void setMail_user_seq(int mail_user_seq) {
		this.mail_user_seq = mail_user_seq;
	}	
	public int getDefine_forwarding_seq() {
		return define_forwarding_seq;
	}
	public void setDefine_forwarding_seq(int define_forwarding_seq) {
		this.define_forwarding_seq = define_forwarding_seq;
	}
	public String getFrom_address() {
		return from_address;
	}
	public void setFrom_address(String from_address) {
		this.from_address = from_address;
	}
	public String getFrom_domain() {
		return from_domain;
	}
	public void setFrom_domain(String from_domain) {
		this.from_domain = from_domain;
	}
	public String getForwarding_address() {
		return forwarding_address;
	}
	public void setForwarding_address(String forwarding_address) {
		this.forwarding_address = forwarding_address;
	}
	public String[] getForwarding_address_arr() {
		return forwarding_address_arr;
	}
	public void setForwarding_address_arr(String[] forwarding_address_arr) {
		this.forwarding_address_arr = forwarding_address_arr;
	}
	public ArrayList<String> getForwarding_address_list() {
		return forwarding_address_list;
	}
	public void setForwarding_address_list(ArrayList<String> forwarding_address_list) {
		this.forwarding_address_list = forwarding_address_list;
	}		
}