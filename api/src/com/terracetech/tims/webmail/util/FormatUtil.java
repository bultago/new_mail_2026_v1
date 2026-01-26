/**
 * FormatUtil.java 2008. 11. 20.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p><strong>FormatUtil.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class FormatUtil {
	
	private final static SimpleDateFormat BASIC_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	private final static SimpleDateFormat SESSION_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmm");
	
	private final static int TEN_MIN = 60 * 10 * 1000;
	private static int lastCheckTime = -1;
	private static String lastChekTimeStr = null;
	 
	public static String toByteString(String formatStr){
		return Long.toString(toByteValue(formatStr));
	}
	
	public static long toByteValue(String formatStr){
		long byteValue = 0;
		
		formatStr = formatStr.toUpperCase();
		try {
			if(formatStr.indexOf("KB") > -1){
				byteValue = getFormatParse("KB",formatStr);
				byteValue = byteValue * 1024;
			} else if(formatStr.indexOf("MB") > -1){
				byteValue = getFormatParse("MB",formatStr);
				byteValue = byteValue * 1024 * 1024;
			} else if(formatStr.indexOf("GB") > -1){
				byteValue = getFormatParse("GB",formatStr);
				byteValue = byteValue * 1024 * 1024 * 1024;
			} else if(formatStr.indexOf("BYTE") > -1){
				byteValue = getFormatParse("BYTE",formatStr);			
			} else {
				byteValue = Long.parseLong(formatStr.trim());
			}
		} catch (Exception e) {}
		
		return byteValue;
	}	
	
	public static long getFormatParse(String format, String parseStr){
		long parseLong = 0;
		
		int idxPos = parseStr.indexOf(format);				
		parseLong = Long.parseLong((parseStr.substring(0, idxPos)).trim());		
		
		return parseLong;		
	}
	
	public static String toUnitString(long byteVal){		
		return toUnitString(byteVal,0);
	}
	
	public static String toUnitString(long byteVal, int decimalCount){		
	
		String unitStr = null;
		DecimalFormat df = null;
		
		double bytesize = Double.parseDouble(Long.toString(byteVal));
		
		if (bytesize >0 && decimalCount > 0){
			String format = ".";
			for (int i = 1; i <= decimalCount; i++) {
				format += "#";
			}
			df = new DecimalFormat(format);
			format = null;
		} else {
			df = new DecimalFormat();
		}
		
	    if (bytesize > (1048576 * 1024)) {    // 1M * 1024	    	
	        unitStr = df.format(bytesize/(1048576*1024)) + "GB";
	    }
	    else if (bytesize > 1048576) {    // 1048576 = 1024 * 1024
	        unitStr = df.format(bytesize/1048576) + "MB";
	    }
	    else if (bytesize > 1024) {            
	        unitStr = df.format(bytesize/1024) + "KB";
	    }
	    else {            
	        unitStr = df.format(bytesize) + "Byte";
	    }
	    
	    df = null;
		return unitStr;
	}
	
	public static String getBasicDateStr(){
		return BASIC_DATE_FORMAT.format(new Date());
	}
	
	public static String getDateStr(String s) {
		return BASIC_DATE_FORMAT.format(strToDate(s));
	}
	
	public static String getSessionDateStr(){
		return SESSION_DATE_FORMAT.format(new Date());
	}
	
	/**
	 * <p>
	 * ÀÓ½Ã µð·ºÅä¸®¸íÀ» ¹ÝÈ¯ÇÑ´Ù.<br>
	 * 
	 * <li>ÀÓ½Ã µð·ºÅä¸® Æ÷¸äÀº "20081211121"°°Àº Çü½ÄÀ¸·Î 10ºÐ ´ÜÀ§ÀÇ ³¯Â¥ Ç¥Çö½ÄÀÌ´Ù.</li>
	 * <li>ÇÏ·ç ÃÖ´ë 144°³ÀÇ ÀÓ½Ã µð·ºÅä¸®°¡ »ý¼º µÉ ¼ö ÀÖ´Ù.</li>
	 * <li>¼º´É Çâ»óÀ» À§ÇØ¼­ ÇöÀç ½Ã°£À» Ã¼Å©ÇØ¼­ ¸¶Áö¸· Ã¼Å© ½Ã°£º¸´Ù 10ºÐÀÌ»ó °æ°úµÇ¾úÀ» °æ¿ì
	 * 	»õ·Î ½Ã°£À» ¼³Á¤ÇØ¼­ dir¸íÀ» ¸¸µé°í 10ºÐÀÌ ÃÊ°ú ÇÏÁö ¾Ê¾ÒÀ» °æ¿ì¿¡´Â ±âÁ¸¿¡ »ý¼ºÇÑ dir¸íÀ» ¹ÝÈ¯ÇÑ´Ù.
	 * </li>
	 * <p> 
	 * @return lastChekTimeStr ÀÓ½Ãµð·ºÅä¸® ¸í;
	 */
	public static String getTempDirStr(){
		int now = (int)(System.currentTimeMillis() / TEN_MIN );

		if (now > lastCheckTime){
			lastCheckTime = now;
			lastChekTimeStr = getBasicDateStr().substring(0, 11);
		}
		return lastChekTimeStr;
	}
	
	public static Date strToDate (String s)
	{
		if (s == null || s.length() < 4) {
			return null;
		}

		if (s.equalsIgnoreCase("NOW") || s.equalsIgnoreCase("TODAY")) {
			return new Date();
		}
		
		if (s.equalsIgnoreCase("YESTERDAY")) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DAY_OF_MONTH, -1);
			return cal.getTime();
		}
		
		if (s.equalsIgnoreCase("TODAYS")) {
			Date d = new Date();
			Calendar cal = Calendar.getInstance();			
			String dstr = BASIC_DATE_FORMAT.format(d);
			cal.clear();
			cal.set(Integer.parseInt(dstr.substring(0, 4)), 
					Integer.parseInt(dstr.substring(4, 6))-1, 
					Integer.parseInt(dstr.substring(6, 8)), 0, 0);
			
			return cal.getTime();
		}
		
		if (s.equalsIgnoreCase("TODAYE")) {
			return new Date();
		}
		
		if (s.equalsIgnoreCase("YESTERDAYS")) {
			Date d = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			d = cal.getTime();
			String dstr = BASIC_DATE_FORMAT.format(d);
			cal.clear();
			cal.set(Integer.parseInt(dstr.substring(0, 4)), 
					Integer.parseInt(dstr.substring(4, 6))-1, 
					Integer.parseInt(dstr.substring(6, 8)), 0, 0);
			return cal.getTime();
		}
		
		if (s.equalsIgnoreCase("YESTERDAYE")) {
			Date d = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			d = cal.getTime();
			String dstr = BASIC_DATE_FORMAT.format(d);
			cal.clear();
			cal.set(Integer.parseInt(dstr.substring(0, 4)), 
					Integer.parseInt(dstr.substring(4, 6))-1, 
					Integer.parseInt(dstr.substring(6, 8)), 23, 59);
			return cal.getTime();
		}

		try {
			String[] array = s.split("[-/ :]");

			int year    = Integer.parseInt(s.substring(0, 4));
			int month   = 0;
			int day     = 1;
			int hrs     = 0;
			int min     = 0;
			int sec     = 0;

			if (array.length > 1) {
				return strToDate (array);
			}
			else {
				if (s.length() >= 6) {
					month   = Integer.parseInt(s.substring(4, 6))-1;
				}
				if (s.length() >= 8) {
					day     = Integer.parseInt(s.substring(6, 8));
				}
				if (s.length() >= 10) {
					hrs     = Integer.parseInt(s.substring(8, 10));
				}
				if (s.length() >= 12) {
					min     = Integer.parseInt(s.substring(10, 12));
				}
				if (s.length() >= 14) {
					sec     = Integer.parseInt(s.substring(12, 14));
				}
			}
			Calendar cal = Calendar.getInstance();
			cal.set(year, month, day, hrs, min, sec);
			return cal.getTime();
		} catch (NumberFormatException ex) {
			System.out.println("-- ERR NumberFormatException ["+s+"] : ["+ex.getMessage()+"]");

			return null;
		}
	}

	public static Date strToDate (String array[])
	{
		if (array == null || array.length < 1) {
			return null;
		}

		try {
			int year    = 0;
			int month   = 0;
			int day     = 1;
			int hrs     = 0;
			int min     = 0;
			int sec     = 0;

			if (array.length >= 1) {
				year    = Integer.parseInt(array[0]);
			}
			if (array.length >= 2) {
				month   = Integer.parseInt(array[1])-1;
			}
			if (array.length >= 3) {
				day     = Integer.parseInt(array[2]);
			}
			if (array.length >= 4) {
				hrs     = Integer.parseInt(array[3]);
			}
			if (array.length >= 5) {
				min     = Integer.parseInt(array[4]);
			}
			if (array.length >= 6) {
				sec     = Integer.parseInt(array[5]);
			}

			Calendar cal = Calendar.getInstance();
			cal.set(year, month, day, hrs, min, sec);
			return cal.getTime();
		} catch (NumberFormatException ex) {
			System.out.println("-- ERR NumberFormatException ["+array+"] : ["+ex.getMessage()+"]");

			return null;
		}
	}

	public static Date getSearchBeforeDate(String s)
	{
		return getSearchBeforeDate(strToDate(s));
	}

	public static Date getSearchBeforeDate(Date d)
	{
		if (d == null) {
			return null;
		}

		return new Date(d.getTime() + 24 * 60 * 60 * 1000);
	}
	
	public static String getFileFormat(String fileName){
		String type = null;
		if(fileName != null){
			int startPos = fileName.lastIndexOf('.');
			if(startPos > 0){
				type = fileName.substring(startPos+1, fileName.length());
			} else {
				type = "unknown";
			}
		}
		
		return type;
	}
	
	public static String getContentType(String fileName) {
	        String contentType = "application/download";
	        if (fileName != null) {
	            int pos = fileName.lastIndexOf(".");
	            if (pos > -1) {
	                String ext = fileName.substring(pos + 1);
	                if ("docx".equalsIgnoreCase(ext)) {
	                    contentType = "application/msword";
	                } else if ("xlsx".equalsIgnoreCase(ext)) {
	                    contentType = "application/vnd.ms-excel";
	                } else if ("pptx".equalsIgnoreCase(ext)) {
	                    contentType = "application/vnd.ms-powerpoint";
	                }
	            }

	        }
	        return contentType;
	}
	
	public static long[] parseStringToLong(String[] strs){
		long[] convert = null;
		if(strs != null){
			convert = new long[strs.length];
			for (int i = 0; i < strs.length ; i++) {
				convert[i] = Long.parseLong(strs[i]);
			}
		}
		
		return convert;
	}
	
	public static int[] parseStringToInt(String[] strs){
		int[] convert = null;
		if(strs != null){
			convert = new int[strs.length];
			for (int i = 0; i < strs.length ; i++) {
				convert[i] = Integer.parseInt(strs[i]);
			}
		}
		
		return convert;
	}
}
