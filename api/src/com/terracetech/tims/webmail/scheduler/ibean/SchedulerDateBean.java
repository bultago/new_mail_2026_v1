package com.terracetech.tims.webmail.scheduler.ibean;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.scheduler.vo.SchedulerVO;

public class SchedulerDateBean {

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

    private List monthDayList = null;
    private List weekDayList = null;

    private SchedulerVO schedulerVo = null;

    public SchedulerDateBean(SchedulerVO schedulerVo) {
        this.todayYear = schedulerVo.getTodayYear();
        this.todayMonth = schedulerVo.getTodayMonth();
        this.todayDay = schedulerVo.getTodayDay();
        this.thisYear = schedulerVo.getThisYear();
        this.thisMonth = schedulerVo.getThisMonth();
        this.thisDay = schedulerVo.getThisDay();
        this.thisDayOfWeek = schedulerVo.getThisDayOfWeek();
        this.prevYear = schedulerVo.getPrevYear();
        this.prevMonth = schedulerVo.getPrevMonth();
        this.prevDay = schedulerVo.getPrevDay();
        this.nextYear = schedulerVo.getNextYear();
        this.nextMonth = schedulerVo.getNextMonth();
        this.nextDay = schedulerVo.getNextDay();
        this.firstYear = schedulerVo.getFirstYear();
        this.firstMonth = schedulerVo.getFirstMonth();
        this.firstDay = schedulerVo.getFirstDay();
        this.lastYear = schedulerVo.getLastYear();
        this.lastMonth = schedulerVo.getLastMonth();
        this.lastDay = schedulerVo.getLastDay();
        this.firstdayOfMonth = schedulerVo.getFirstdayOfMonth();
        this.maxdayOfMonth = schedulerVo.getMaxdayOfMonth();
        this.maxdayOfPrevMonth = schedulerVo.getMaxdayOfPrevMonth();
        this.weekCount = schedulerVo.getWeekCount();
        this.firstLunar = schedulerVo.getFirstLunar();
        this.lastLunar = schedulerVo.getLastLunar();
        this.lunar = schedulerVo.getLunar();

        this.todayStr = schedulerVo.getTodayStr();
        this.thisdayStr = schedulerVo.getThisdayStr();
        this.monthDayList = schedulerVo.getMonthDayList();
        this.weekDayList = schedulerVo.getWeekDayList();
    }

    public JSONObject monthToJson() {
        JSONObject json = new JSONObject();

        json.put("thisYear", thisYear);
        json.put("thisMonth", thisMonth);
        json.put("thisDay", thisDay);
        json.put("prevYear", prevYear);
        json.put("prevMonth", prevMonth);
        json.put("nextYear", nextYear);
        json.put("nextMonth", nextMonth);
        json.put("firstdayOfMonth", firstdayOfMonth);
        json.put("maxdayOfMonth", maxdayOfMonth);
        json.put("thisdayStr", thisdayStr);
        json.put("todayYear", todayYear);
        json.put("todayMonth", todayMonth);
        json.put("todayDay", todayDay);
        json.put("todayStr", todayStr);
        json.put("lunar", lunar);

        JSONArray dateList = null;
        if (monthDayList != null && monthDayList.size() > 0) {
            dateList = new JSONArray();
            for (int i = 0; i < monthDayList.size(); i++) {
                JSONObject date = new JSONObject();
                date.put("date", monthDayList.get(i));
                dateList.add(date);
            }
        }

        json.put("dateList", dateList);

        return json;
    }

    public JSONObject weekToJson() {
        JSONObject json = new JSONObject();

        json.put("thisYear", thisYear);
        json.put("thisMonth", thisMonth);
        json.put("thisDay", thisDay);
        json.put("thisdayStr", thisdayStr);
        json.put("firstYear", firstYear);
        json.put("firstMonth", firstMonth);
        json.put("firstDay", firstDay);
        json.put("lastYear", lastYear);
        json.put("lastMonth", lastMonth);
        json.put("lastDay", lastDay);
        json.put("prevYear", prevYear);
        json.put("prevMonth", prevMonth);
        json.put("prevDay", prevDay);
        json.put("nextYear", nextYear);
        json.put("nextMonth", nextMonth);
        json.put("nextDay", nextDay);
        json.put("todayYear", todayYear);
        json.put("todayMonth", todayMonth);
        json.put("todayDay", todayDay);
        json.put("todayStr", todayStr);
        json.put("firstLunar", firstLunar);
        json.put("lastLunar", lastLunar);

        JSONArray dateList = null;
        if (weekDayList != null && weekDayList.size() > 0) {
            dateList = new JSONArray();
            for (int i = 0; i < weekDayList.size(); i++) {
                JSONObject date = new JSONObject();
                date.put("date", weekDayList.get(i));
                dateList.add(date);
            }
        }

        json.put("dateList", dateList);

        return json;
    }

    public JSONObject dayToJson() {
        JSONObject json = new JSONObject();

        json.put("thisYear", thisYear);
        json.put("thisMonth", thisMonth);
        json.put("thisDay", thisDay);
        json.put("prevYear", prevYear);
        json.put("prevMonth", prevMonth);
        json.put("prevDay", prevDay);
        json.put("nextYear", nextYear);
        json.put("nextMonth", nextMonth);
        json.put("nextDay", nextDay);
        json.put("thisdayStr", thisdayStr);
        json.put("thisDayOfWeek", thisDayOfWeek);
        json.put("todayYear", todayYear);
        json.put("todayMonth", todayMonth);
        json.put("todayDay", todayDay);
        json.put("todayStr", todayStr);
        json.put("lunar", lunar);

        return json;
    }
}
