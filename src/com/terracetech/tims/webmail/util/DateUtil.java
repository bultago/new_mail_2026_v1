package com.terracetech.tims.webmail.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
	
	private final static SimpleDateFormat BASIC_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	private final static SimpleDateFormat FULL_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	/**
	 * 
	 * @param format yyyyMMddHHmmss
	 * @return
	 */
	public static String getBasicDateStr(String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		
		return sdf.format(new Date());
	}
	
	/**
	 * 
	 * @param format yyyyMMddHHmm
	 * @return
	 * @throws ParseException 
	 */
	public static String findDateStr(int term) throws ParseException{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		String endDate = sdf.format(new Date());
		String startDate = getNextTime("DAY", endDate, term);
		
		
		return startDate;
	}
	
	public static String getNextTime(String type, String timeStr, int term) throws ParseException
	{
		Date nextDate = null;
		SimpleDateFormat sdf = null;
		Calendar calendar = new GregorianCalendar();
		
		if (timeStr.length() > 0){
			if (type.toUpperCase().equals("MIN")){
				sdf = new SimpleDateFormat("yyyyMMddHHmm");
				nextDate = sdf.parse(timeStr);
				calendar.setTime(nextDate);
				calendar.add(Calendar.MINUTE,term);			
			}else if (type.toUpperCase().equals("HOUR")){
				sdf = new SimpleDateFormat("yyyyMMddHH");
				nextDate = sdf.parse(timeStr);
				calendar.setTime(nextDate);
				calendar.add(Calendar.HOUR,term);			
			}else if (type.toUpperCase().equals("DAY")){
				sdf = new SimpleDateFormat("yyyyMMdd");
				nextDate = sdf.parse(timeStr);
				calendar.setTime(nextDate);
				calendar.add(Calendar.DAY_OF_MONTH,term);			
			}else if (type.toUpperCase().equals("MONTH")){
				sdf = new SimpleDateFormat("yyyyMM");
				nextDate = sdf.parse(timeStr);
				calendar.setTime(nextDate);
				calendar.add(Calendar.MONTH,term);
			}
		}
		

		nextDate = calendar.getTime();

		return sdf.format(nextDate);
	}
	
	/**
	 * 
	 * @param timeStr 20100622101344 -> 20100622T101344Z
	 * @return
	 */
	public static String getActiveSyncFormat(String timeStr){
		if(StringUtils.isEmpty(timeStr))
			return "";
		
		if(timeStr.length() != 14)
			return "";
		
		String pDate = timeStr.substring(0, 8);
		String eDate =  timeStr.substring(8, timeStr.length());
		
		return pDate + "T" + eDate + "Z";
	}

	/**
	 * 20100622T101344Z -> 20100622101344
	 * @param timeStr 20100622T101344Z
	 * @param format yyyyMMddHHmm00
	 * @return
	 * @throws ParseException
	 */
	public static String getTmsFormat(String timeStr, String format) throws ParseException{
		if(StringUtils.isEmpty(timeStr))
			return "";
		
		if(timeStr.length() != 16)
			return "";
		
		timeStr = StringUtils.replace(timeStr, "T", "");
		timeStr = StringUtils.replace(timeStr, "Z", "");
		
		SimpleDateFormat activeSyncFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat tmsFormat = new SimpleDateFormat(format);
		Date date = activeSyncFormat.parse(timeStr);
		
		return tmsFormat.format(date);
	}
	
	public static String getBasicDateStr(){
		return BASIC_DATE_FORMAT.format(new Date());
	}
	
	public static String getFullDateStr(){
		return FULL_DATE_FORMAT.format(new Date());
	}
	
	/**
	 * TMS Æ÷¸äÀ» AciveSync Æ÷¸äÀ¸·Î
	 * @return
	 */
	public static String convertDayofWeekForActiveSync(String val){
		if("01".equals(val)){
			//ÀÏ¿äÀÏ
			return "1";
		}
		
		if("02".equals(val)){
			//¿ù¿äÀÏ
			return "2";
		}
		
		if("03".equals(val)){
			//È­¿äÀÏ
			return "4";
		}
		
		if("04".equals(val)){
			//¼ö¿äÀÏ
			return "8";
		}
		
		if("05".equals(val)){
			//¸ñ¿äÀÏ
			return "16";
		}
		
		if("06".equals(val)){
			//±Ý¿äÀÏ
			return "32";
		}
		
		if("07".equals(val)){
			//Åä¿äÀÏ
			return "64";
		}
		
		return "";
	}
	
	public static String convertDayofWeekForTms(String val){
		if("1".equals(val)){
			//ÀÏ¿äÀÏ
			return "01";
		}
		
		if("2".equals(val)){
			//¿ù¿äÀÏ
			return "02";
		}
		
		if("4".equals(val)){
			//È­¿äÀÏ
			return "03";
		}
		
		if("8".equals(val)){
			//¼ö¿äÀÏ
			return "04";
		}
		
		if("16".equals(val)){
			//¸ñ¿äÀÏ
			return "05";
		}
		
		if("32".equals(val)){
			//±Ý¿äÀÏ
			return "06";
		}
		
		if("64".equals(val)){
			//Åä¿äÀÏ
			return "07";
		}
		
		return "";
	}
}
