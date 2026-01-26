package com.terracetech.tims.webmail.addrbook.ibean;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.addrbook.vo.AddressBookVO;

public class AddressBookBean {

	private AddressBookVO book;

	public AddressBookBean(AddressBookVO book) {
		this.book = book;
	}

	@SuppressWarnings("all")
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		json.put("id", book.getAddrbookSeq());
		json.put("name", book.getAddrbookName());

		return json;
	}
}
