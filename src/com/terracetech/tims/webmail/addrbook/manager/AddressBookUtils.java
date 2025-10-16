package com.terracetech.tims.webmail.addrbook.manager;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.util.StringUtils;

public class AddressBookUtils {
	
	private static I18nResources resource = null;

	public static void setKorean(Map<String, Object> param) {
		param.put("가", "가");
		param.put("나", "나");
		param.put("다", "다");
		param.put("라", "라");
		param.put("마", "마");
		param.put("바", "바");
		param.put("사", "사");
		param.put("아", "아");
		param.put("자", "자");
		param.put("차", "차");
		param.put("카", "카");
		param.put("파", "파");
		param.put("타", "타");
		param.put("하", "하");
		param.put("힝", "힝");
	}
	
	public static void setJapaneses(String startChar, HashMap<String, Object> param) {
		if(resource ==null){
			resource = new I18nResources(new Locale("jp"), "addr");	
		}
		
		String japan = resource.getMessage("addr.table.index1");
		if(StringUtils.isEmpty(japan))
			return;
		
		String[] japans = japan.split(",");
		for (int i = 0; i < japans.length; i++) {
			param.put("name" + (i+1), japans[i]);
		}
	}
}
