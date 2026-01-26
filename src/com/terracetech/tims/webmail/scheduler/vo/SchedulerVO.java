package com.terracetech.tims.webmail.scheduler.vo;

import java.util.List;

/**
 * 월력(Calendar) VO - 월별 일정 정보
 */
public class SchedulerVO {
    
    // 현재 날짜 관련
    private String thisdayStr;
    private int thisYear;
    private int thisMonth;
    private int thisDay;
    private int thisDayOfWeek;
    
    // 이전/다음 월
    private int prevYear;
    private int prevMonth;
    private int prevDay;
    private int nextYear;
    private int nextMonth;
    private int nextDay;
    
    // 월력 정보
    private int firstYear;
    private int firstMonth;
    private int firstDay;
    private int firstdayOfMonth;
    private int lastYear;
    private int lastMonth;
    private int lastDay;
    private int maxdayOfMonth;
    
    // 오늘 날짜
    private String todayStr;
    private int todayYear;
    private int todayMonth;
    private int todayDay;
    
    // 리스트
    private List monthDayList;
    private List weekDayList;
    
    // 음력 여부
    private int lunar;
    private int firstLunar;
    private int lastLunar;
    
    // Getters and Setters
    public String getThisdayStr() {
        return thisdayStr;
    }
    
    public void setThisdayStr(String thisdayStr) {
        this.thisdayStr = thisdayStr;
    }
    
    public int getThisYear() {
        return thisYear;
    }
    
    public void setThisYear(int thisYear) {
        this.thisYear = thisYear;
    }
    
    public int getThisMonth() {
        return thisMonth;
    }
    
    public void setThisMonth(int thisMonth) {
        this.thisMonth = thisMonth;
    }
    
    public int getThisDay() {
        return thisDay;
    }
    
    public void setThisDay(int thisDay) {
        this.thisDay = thisDay;
    }
    
    public int getThisDayOfWeek() {
        return thisDayOfWeek;
    }
    
    public void setThisDayOfWeek(int thisDayOfWeek) {
        this.thisDayOfWeek = thisDayOfWeek;
    }
    
    public int getPrevYear() {
        return prevYear;
    }
    
    public void setPrevYear(int prevYear) {
        this.prevYear = prevYear;
    }
    
    public int getPrevMonth() {
        return prevMonth;
    }
    
    public void setPrevMonth(int prevMonth) {
        this.prevMonth = prevMonth;
    }
    
    public int getPrevDay() {
        return prevDay;
    }
    
    public void setPrevDay(int prevDay) {
        this.prevDay = prevDay;
    }
    
    public int getNextYear() {
        return nextYear;
    }
    
    public void setNextYear(int nextYear) {
        this.nextYear = nextYear;
    }
    
    public int getNextMonth() {
        return nextMonth;
    }
    
    public void setNextMonth(int nextMonth) {
        this.nextMonth = nextMonth;
    }
    
    public int getNextDay() {
        return nextDay;
    }
    
    public void setNextDay(int nextDay) {
        this.nextDay = nextDay;
    }
    
    public int getFirstYear() {
        return firstYear;
    }
    
    public void setFirstYear(int firstYear) {
        this.firstYear = firstYear;
    }
    
    public int getFirstMonth() {
        return firstMonth;
    }
    
    public void setFirstMonth(int firstMonth) {
        this.firstMonth = firstMonth;
    }
    
    public int getFirstDay() {
        return firstDay;
    }
    
    public void setFirstDay(int firstDay) {
        this.firstDay = firstDay;
    }
    
    public int getFirstdayOfMonth() {
        return firstdayOfMonth;
    }
    
    public void setFirstdayOfMonth(int firstdayOfMonth) {
        this.firstdayOfMonth = firstdayOfMonth;
    }
    
    public int getLastYear() {
        return lastYear;
    }
    
    public void setLastYear(int lastYear) {
        this.lastYear = lastYear;
    }
    
    public int getLastMonth() {
        return lastMonth;
    }
    
    public void setLastMonth(int lastMonth) {
        this.lastMonth = lastMonth;
    }
    
    public int getLastDay() {
        return lastDay;
    }
    
    public void setLastDay(int lastDay) {
        this.lastDay = lastDay;
    }
    
    public int getMaxdayOfMonth() {
        return maxdayOfMonth;
    }
    
    public void setMaxdayOfMonth(int maxdayOfMonth) {
        this.maxdayOfMonth = maxdayOfMonth;
    }
    
    public String getTodayStr() {
        return todayStr;
    }
    
    public void setTodayStr(String todayStr) {
        this.todayStr = todayStr;
    }
    
    public int getTodayYear() {
        return todayYear;
    }
    
    public void setTodayYear(int todayYear) {
        this.todayYear = todayYear;
    }
    
    public int getTodayMonth() {
        return todayMonth;
    }
    
    public void setTodayMonth(int todayMonth) {
        this.todayMonth = todayMonth;
    }
    
    public int getTodayDay() {
        return todayDay;
    }
    
    public void setTodayDay(int todayDay) {
        this.todayDay = todayDay;
    }
    
    public List getMonthDayList() {
        return monthDayList;
    }
    
    public void setMonthDayList(List monthDayList) {
        this.monthDayList = monthDayList;
    }
    
    public List getWeekDayList() {
        return weekDayList;
    }
    
    public void setWeekDayList(List weekDayList) {
        this.weekDayList = weekDayList;
    }
    
    public int getLunar() {
        return lunar;
    }
    
    public void setLunar(int lunar) {
        this.lunar = lunar;
    }
    
    public int getFirstLunar() {
        return firstLunar;
    }
    
    public void setFirstLunar(int firstLunar) {
        this.firstLunar = firstLunar;
    }
    
    public int getLastLunar() {
        return lastLunar;
    }
    
    public void setLastLunar(int lastLunar) {
        this.lastLunar = lastLunar;
    }
}
