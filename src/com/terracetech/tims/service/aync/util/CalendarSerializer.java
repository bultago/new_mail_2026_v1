package com.terracetech.tims.service.aync.util;

import java.io.IOException;

import com.terracetech.tims.webmail.util.DateUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class CalendarSerializer {

	private WbxmlSerializer serializer = null;

	public CalendarSerializer(WbxmlSerializer serializer) {
		this.serializer = serializer;
	}
	
	public void openTag(String namespace, String name)throws IOException, WbxmlException{
		serializer.openTag(namespace, name);
	}
	
	public void closeTag() throws IOException, WbxmlException{
		serializer.closeTag();
	}
	
	public void textElement(String namespace, String name, String text)throws IOException, WbxmlException{
		serializer.textElement(namespace, name, text);
	}
	
	public void integerElement(String namespace, String name, int number)throws IOException, WbxmlException{
		serializer.integerElement(namespace, name, number);
	}
	
	public void writeRepeatTerm(String repeatTerm, String endDate) throws IOException, WbxmlException{
		serializer.openTag("Calendar", "Recurrence");
		
		if(StringUtils.isNotEmpty(repeatTerm)){
			String type = repeatTerm.substring(0, 2);
			if("01".equals(type)){
				//Recurs daily 010103
				String interval = repeatTerm.substring(4, 6);
				serializer.integerElement("Calendar", "Recurrence_Type", 0);
				serializer.integerElement("Calendar", "Recurrence_Interval", Integer.parseInt(interval));
			}else if("02".equals(type)){
				//Recurs weekly 020603 -> 6ÁÖ¸¶´Ù È­¿äÀÏ
				String day  = DateUtil.convertDayofWeekForActiveSync(repeatTerm.substring(2, 4));
				serializer.integerElement("Calendar", "Recurrence_Type", 1);
				serializer.integerElement("Calendar", "Recurrence_DayOfWeek", Integer.parseInt(day));
			}else if("03".equals(type)){
				//Recurs monthly 030203 or 03010203
				String interval = repeatTerm.substring(2, 4);
				serializer.integerElement("Calendar", "Recurrence_Type", 2);
				serializer.integerElement("Calendar", "Recurrence_Interval", Integer.parseInt(interval));
				if(repeatTerm.length()==6){
					String day = repeatTerm.substring(4, 6);
					serializer.integerElement("Calendar", "Recurrence_DayOfMonth", Integer.parseInt(day));
				}else if(repeatTerm.length()==8){
					//03010203 -> 1°³¿ù¸¶´Ù µÑÂ°ÁÖ È­¿äÀÏ
					String week = repeatTerm.substring(4, 6);
					String day  = DateUtil.convertDayofWeekForActiveSync(repeatTerm.substring(6, 8));
					serializer.integerElement("Calendar", "Recurrence_WeekOfMonth", Integer.parseInt(week));
					serializer.integerElement("Calendar", "Recurrence_DayOfWeek", Integer.parseInt(day));
				}
			}else if("04".equals(type)){
				//Recurs yearly 040501 ¸Å³â 5¿ù 1ÀÏ
				String month = repeatTerm.substring(2, 4);
				String day  = repeatTerm.substring(4, 6);
				serializer.integerElement("Calendar", "Recurrence_Type", 5);
				serializer.integerElement("Calendar", "Recurrence_MonthOfYear", Integer.parseInt(month));
				serializer.integerElement("Calendar", "Recurrence_DayOfMonth", Integer.parseInt(day));
			}
		}
		
		if(StringUtils.isNotEmpty(endDate)){
			//20100722
			String repeatEndDate = DateUtil.getActiveSyncFormat(endDate + "000000");
			serializer.textElement("Calendar", "Recurrence_Until", repeatEndDate);
		}
		
		serializer.closeTag();
	}
	
}
