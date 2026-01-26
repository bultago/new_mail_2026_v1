package com.terracetech.tims.webmail.mailuser;

import java.util.*;

public class UserInfo implements User {
	private final static Set<String> sharedKeys = new HashSet<String>(5); 
	
	static {
		sharedKeys.add(User.MAIL_SERVICES);
		sharedKeys.add(User.QUOTA_OVERLOOK_RATIO);
		sharedKeys.add(User.QUOTA_WARNING_MODE);
		sharedKeys.add(User.QUOTA_WARNING_RATIO);
		sharedKeys.add(User.QUOTA_VIOLATION_ACTION);
		sharedKeys.add(User.MAX_FORWARDING);
	}
	
	Map<String, Object> userInfo = new HashMap<String, Object>();
	
    public String get(String key) {
    	Object tmp = userInfo.get(key); 
    	return tmp != null ? tmp.toString() : "";
    }

    public void put(String key, Object value) {
        userInfo.put(key.toUpperCase(), value);
    }
    
    public void setUserInfoMap(Map<String, Object> infoMap){
    	if (infoMap == null)
    		return;

//    	µ¥ÀÌÅÍ º£ÀÌ½º¿¡ µû¶ó¼­ fieldÁ¤º¸°¡ ¼Ò¹®ÀÚ·Î ¿Ã ¼ö µµ ÀÖ±â¶§¹®¿¡ ¹®Á¶°Ç ´ë¹®ÀÚ·Î ¼¼ÆÃ
    	Iterator<String> keys = infoMap.keySet().iterator();
    	Map<String, Object> tmp = new HashMap<String, Object>();
    	while (keys.hasNext()) {
    		String key = keys.next();
    		Object value = infoMap.get(key);
    		tmp.put(key.toUpperCase(), value);
    	}
    	
    	for (String key : sharedKeys) {
    		String value = this.get(key); 
    		if (!("0".equals(value) || "".equals(value)))
    			tmp.remove(key);
    	}
    	
    	userInfo.putAll(tmp);
    }
    
    public String toString() {
    	return userInfo != null ? userInfo.toString() : null;
    }

	public String[] getKeys() {
		Set<String> keySet = userInfo.keySet();
		return keySet.toArray(new String[userInfo.size()]);
	}

	public void removeKey(String key) {
		userInfo.remove(key);
	}
}