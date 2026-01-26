package com.terracetech.tims.service.aync;

public interface ICalendar {

	/**
	 * <pre>
	 * 0 : Recurs daily
	 * 1 : Recurs weekly
	 * 2 : Recurs monthly
	 * 3 : Recurs monthly on the nth day
	 * 5 : Recurs yearly
	 * 6 : Recurs yearly on the nth day
	 * </pre>
	 */
	public int RECURRENCE_TYPE = 0;
	
	/**
	 * date/time
	 */
	public String RECURRENCE_UNTIL = "";
	
	/**
	 * max 999
	 */
	public int RECURRENCE_INTERVAL = 0;
	
	/**
	 * 1 ~ 5
	 */
	public int RECURRENCE_WEEKOFMONTH = 0;
	

	/**
	 * 1 ~ 31
	 */
	public int RECURRENCE_DAYOFMONTH = 0;
	
	/**
	 * 1 : Sunday
	 * 2 : Monday
	 * 4 : Tuesday
	 * 8 : Wednesday
	 * 16 : Thursday
	 * 32 : Friday
	 * 64 : Saturday
	 * 127 : The last day of the month. Used as a special value in monthly or yearly recurrences.
	 */
	public int RECURRENCE_DAYOFWEEK = 0;
	
	/**
	 * 
	 */
	public int RECURRENCE_OCCURRENCES = 0;
	
	/**
	 * 1 ~ 12
	 */
	public int RECURRENCE_MONTHOFYEAR = 0;
	
	/**
	 * 
	 */
	public int REMINDER_MINSBEFORE = 0;
	
	/**
	 * 
	 */
	public int SENSITIVITY = 0;
	

}
