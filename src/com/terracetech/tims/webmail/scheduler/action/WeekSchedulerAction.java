package com.terracetech.tims.webmail.scheduler.action;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerVO;

public class WeekSchedulerAction extends BaseAction {
    /**
     * @since 2012. 6. 21.
     */
    private static final long serialVersionUID = 6151499765081764414L;
    private SchedulerManager schedulerManager = null;
    private SchedulerVO schedulerVo = null;
    int year = 0;
    int month = 0;
    int day = 0;
    int firstLunar = 0;
    int lastLunar = 0;

    @Override
    public String execute() throws Exception {

        schedulerVo = schedulerManager.getWeekScheduler(year, month, day);
        schedulerVo = schedulerManager.getTodayDate(schedulerVo);
        firstLunar = schedulerManager.getLunar(schedulerVo.getFirstYear(), schedulerVo.getFirstMonth(),
                schedulerVo.getFirstDay());
        lastLunar = schedulerManager.getLunar(schedulerVo.getLastYear(), schedulerVo.getLastMonth(),
                schedulerVo.getLastDay());

        return "success";
    }

    public void setSchedulerManager(SchedulerManager schedulerManager) {
        this.schedulerManager = schedulerManager;
    }

    public SchedulerVO getSchedulerVo() {
        return schedulerVo;
    }

    public void setSchedulerVo(SchedulerVO schedulerVo) {
        this.schedulerVo = schedulerVo;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
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
