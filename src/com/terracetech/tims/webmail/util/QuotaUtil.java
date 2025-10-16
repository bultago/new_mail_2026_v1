package com.terracetech.tims.webmail.util;

import javax.servlet.ServletContext;

import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mailuser.User;

public class QuotaUtil {

    public static String getString(User user, ServletContext context) {
    	/* 
		 * modified by hkkim
		 * size type int -> long
		 * 2008.03.13
     	 */
        long size = 0;

        try {
            size += Long.parseLong(user.get("wfoldquota"));
        }
        catch (NumberFormatException e) { }

        try {
            size += Long.parseLong(EnvConstants.getUtilSetting("webfolder.quota"));
        }
        catch (NumberFormatException e) {
            size += 10000000;
        }

		/*
		 * modified by isle4
		 * quota overlook ratio: 25 -> 50
		 * 2006.10.18
		 */
        return  size / (1024*1024) + " " + size % (1024*1024) +
            " 100000 0 90 50";
    }
    
    
    public static long[] parseQuotaStr(String quotaStr){
    	long[] quotaVaues = {0,0};
    	
    	if(quotaStr != null){
    		quotaStr = quotaStr.trim();    		
    		quotaVaues[0] = getQuotaByString('S',0,quotaStr);
    		quotaVaues[1] = getQuotaByString('C',(quotaStr.indexOf(',')+1),quotaStr);
    	} 
    	
    	return quotaVaues;    	
    }
    
    public static long getQuotaByString(char ch, int startPos , String quota){
    	long size = 0;
    	int i = 0;
    	if (quota != null && ( i = quota.indexOf(ch)) > -1 && startPos > -1) {
			String tmpLong = quota.substring(startPos, i);			
			size +=  (tmpLong != null)? Long.parseLong(tmpLong.trim()):0;
			tmpLong = null;
        }
    	return size;
    }
    
    public static String getQuotaStr(User user){
    	
    	long[] quotaValues = QuotaUtil.parseQuotaStr(user.get(User.MAIL_QUOTA));
		
		String addQuotaStr = user.get(User.MAIL_ADD_QUOTA);
		long[] addQuotaValues = null;
		if (!"".equals(addQuotaStr)) {
			addQuotaValues = QuotaUtil.parseQuotaStr(addQuotaStr);
			for (int i = 0, cnt = quotaValues.length; i < cnt; i++) {
				quotaValues[i] = quotaValues[i] + addQuotaValues[i];
			}
		}
		user.put(User.MAIL_QUOTA, Long.toString(quotaValues[0]));
		
		String delim = " ";
		StringBuffer quotaValueStr = new StringBuffer();
        quotaValueStr
        			.append(quotaValues[0] / FileUtil.SIZE_MEGA).append(delim)
        			.append(quotaValues[0] % FileUtil.SIZE_MEGA).append(delim)
        			.append(quotaValues[1]).append(delim)
        			.append("ON".equalsIgnoreCase(user.get(User.QUOTA_WARNING_MODE)) ? 1 : 0 ).append(delim)
        			.append(user.get(User.QUOTA_WARNING_RATIO)).append(delim)
        			.append(user.get(User.QUOTA_OVERLOOK_RATIO));
        
        return quotaValueStr.toString();
    }
    
    public static String calculQuota (long defaultValue, String baseValue, String addValue) {
		long returnValue = defaultValue;
		
		if (baseValue != null && !"".equals(baseValue))
			returnValue = Long.parseLong(baseValue);
		if (addValue != null && !"".equals(addValue))
			returnValue += Long.parseLong(addValue);
		
		return Long.toString(returnValue);
	}
}
