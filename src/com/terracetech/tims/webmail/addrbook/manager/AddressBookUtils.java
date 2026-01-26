package com.terracetech.tims.webmail.addrbook.manager;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.util.StringUtils;

public class AddressBookUtils {
	
	private static I18nResources resource = null;

	public static void setKorean(Map<String, Object> param) {
		param.put("°¡", "°¡");
		param.put("³ª", "³ª");
		param.put("´Ù", "´Ù");
		param.put("¶ó", "¶ó");
		param.put("¸¶", "¸¶");
		param.put("¹Ù", "¹Ù");
		param.put("»ç", "»ç");
		param.put("¾Æ", "¾Æ");
		param.put("ÀÚ", "ÀÚ");
		param.put("Â÷", "Â÷");
		param.put("Ä«", "Ä«");
		param.put("ÆÄ", "ÆÄ");
		param.put("Å¸", "Å¸");
		param.put("ÇÏ", "ÇÏ");
		param.put("Èþ", "Èþ");
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
