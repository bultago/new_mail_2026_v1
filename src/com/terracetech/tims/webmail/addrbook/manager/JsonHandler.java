package com.terracetech.tims.webmail.addrbook.manager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.addrbook.ibean.AddressBookGroupBean;
import com.terracetech.tims.webmail.addrbook.ibean.AddressBookMemberBean;

@SuppressWarnings("unchecked")
public class JsonHandler {

	public JSONArray getJsonGroupList(AddressBookGroupBean[] groups) {
		JSONArray beanArray = new JSONArray();
		
		JSONObject jsObj = null;
		for (AddressBookGroupBean bean : groups) {
			jsObj = bean.toJson();
			
			beanArray.add(jsObj);
		}
		
		return beanArray;
	}

	public JSONArray getJsonMemberList(AddressBookMemberBean[] members) {
		JSONArray beanArray = new JSONArray();
		
		JSONObject jsObj = null;
		for (AddressBookMemberBean bean : members) {
			jsObj = bean.toJson();
			
			beanArray.add(jsObj);
		}
		
		return beanArray;
	}

}
