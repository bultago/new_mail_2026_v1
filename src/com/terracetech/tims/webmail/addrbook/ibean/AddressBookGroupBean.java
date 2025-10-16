package com.terracetech.tims.webmail.addrbook.ibean;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.addrbook.vo.AddressBookGroupVO;

public class AddressBookGroupBean {

	private AddressBookGroupVO group;
	
	public AddressBookGroupBean(AddressBookGroupVO group) {
		super();
		this.group = group;
	}

	@SuppressWarnings("all")
	public JSONObject toJson(){
		JSONObject json = new JSONObject();
		json.put("id", group.getGroupSeq());
		json.put("name", group.getGroupName());
		
		return json;
	}
	
}
