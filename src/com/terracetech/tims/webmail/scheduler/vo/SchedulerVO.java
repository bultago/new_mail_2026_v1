package com.terracetech.tims.webmail.scheduler.vo;

import java.util.List;

public class SchedulerVO {

    private int todayYear = 0;
    private int todayMonth = 0;
    private int todayDay = 0;
    private int thisYear = 0;
    private int thisMonth = 0;
    private int thisDay = 0;
    private int thisDayOfWeek = 0;
    private int prevYear = 0;
    private int prevMonth = 0;
    private int prevDay = 0;
    private int nextYear = 0;
    private int nextMonth = 0;
    private int nextDay = 0;
    private int firstYear = 0;
    private int firstMonth = 0;
    private int firstDay = 0;
    private int lastYear = 0;
    private int lastMonth = 0;
    private int lastDay = 0;
    private int firstdayOfMonth = 0;
    private int maxdayOfMonth = 0;
    private int maxdayOfPrevMonth = 0;
    private int weekCount = 0;
    private int firstLunar = 0;
    private int lastLunar = 0;
    private int lunar = 0;

    private String todayStr = null;
    private String thisdayStr = null;

    List monthDayList = null;
    List weekDayList = null;

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

    public int getFirstdayOfMonth() {
        return firstdayOfMonth;
    }

    public void setFirstdayOfMonth(int firstdayOfMonth) {
        this.firstdayOfMonth = firstdayOfMonth;
    }

    public int getMaxdayOfMonth() {
        return maxdayOfMonth;
    }

    public void setMaxdayOfMonth(int maxdayOfMonth) {
        this.maxdayOfMonth = maxdayOfMonth;
    }

    public int getMaxdayOfPrevMonth() {
        return maxdayOfPrevMonth;
    }

    public void setMaxdayOfPrevMonth(int maxdayOfPrevMonth) {
        this.maxdayOfPrevMonth = maxdayOfPrevMonth;
    }

    public int getWeekCount() {
        return weekCount;
    }

    public void setWeekCount(int weekCount) {
        this.weekCount = weekCount;
    }

    public List getWeekDayList() {
        return weekDayList;
    }

    public void setWeekDayList(List weekDayList) {
        this.weekDayList = weekDayList;
    }

    public List getMonthDayList() {
        return monthDayList;
    }

    public void setMonthDayList(List monthDayList) {
        this.monthDayList = monthDayList;
    }

    public String getTodayStr() {
        return todayStr;
    }

    public void setTodayStr(String todayStr) {
        this.todayStr = todayStr;
    }

    public String getThisdayStr() {
        return thisdayStr;
    }

    public void setThisdayStr(String thisdayStr) {
        this.thisdayStr = thisdayStr;
    }

    public int getThisDayOfWeek() {
        return thisDayOfWeek;
    }

    public void setThisDayOfWeek(int thisDayOfWeek) {
        this.thisDayOfWeek = thisDayOfWeek;
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
