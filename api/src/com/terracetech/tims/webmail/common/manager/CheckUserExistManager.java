package com.terracetech.tims.webmail.common.manager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;

public class CheckUserExistManager {
	
	private CacheManager cacheManager = null;
	private SettingManager userSettingManager = null;
	private String cacheName = null;
	
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}
	public void setUserSettingManager(SettingManager userSettingManager) {
		this.userSettingManager = userSettingManager;
	}
	
	public void setCheckUserSet(String id, String timestamp,int mailDomainSeq) {
		if (!(isDuplicateCheckOn(mailDomainSeq))) {
			return;
		} else {
			Cache cache = cacheManager.getCache(cacheName);
			Element element = new Element(id, timestamp);
			cache.put(element);
		}
	}



	public void deleteExistUser(String id,int mailDomainSeq) {
		if (!(isDuplicateCheckOn(mailDomainSeq))) {
			return;
		} else {
			cacheManager.getCache(cacheName).remove(id);
		}
	}

	public boolean isUserExist(String id,int mailDomainSeq) {
		if (!(isDuplicateCheckOn(mailDomainSeq))) {
			return true;
		} else {
			if (cacheManager.getCache(cacheName).isKeyInCache(id)) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean isDuplicateCheckOn(int mailDomainSeq) {
		return userSettingManager.enableDuplicateLogin(mailDomainSeq);
	}

	public Map<String, Boolean> checkLoginUserList(String id, String timestamp,int mailDomainSeq) {
		Map<String, Boolean> returnMap = new HashMap<String, Boolean>();
		boolean duplicateCheckOn = isDuplicateCheckOn(mailDomainSeq);
		boolean isUserLogin = true;
		if(duplicateCheckOn){
			Cache cache = cacheManager.getCache(cacheName);
			if (cache.isKeyInCache(id)) {
				Element element = cache.get(id);
				String  value = (String)element.getObjectValue();
					if (value.equalsIgnoreCase(timestamp)) {
						isUserLogin = false;
					}
			}else{
				setCheckUserSet(id,timestamp,mailDomainSeq);
				isUserLogin = false;
			}
		}
		returnMap.put("isDuplicateCheckOn", duplicateCheckOn);
		returnMap.put("isUserLogin", isUserLogin);
		
		return returnMap;
	}
	
	// Áßº¹·Î±×ÀÎ ¹æÁö - ·Î±×ÀÎ½Ã ¹«Á¶°Ç »õ·Î¿î timestamp »ý¼º
	public String dupCheckProcess(String id, int mailDomainSeq) {
		if (isUserExist(id,mailDomainSeq)) {
			deleteExistUser(id,mailDomainSeq);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp = sdf.format(new Date());
		setCheckUserSet(id, timestamp,mailDomainSeq);
		return timestamp;
	}
}
