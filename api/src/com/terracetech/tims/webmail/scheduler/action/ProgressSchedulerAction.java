package com.terracetech.tims.webmail.scheduler.action;

import java.util.List;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerVO;

public class ProgressSchedulerAction extends BaseAction {
    /**
     * @since 2012. 6. 21.
     */
    private static final long serialVersionUID = 3924932183562432823L;
    private SchedulerManager schedulerManager = null;
    private SchedulerVO schedulerVo = null;
    private List weekDateList = null;
    private int year = 0;
    private int month = 0;
    private int day = 0;

    @Override
    public String execute() throws Exception {

        weekDateList = schedulerManager.getProgressDateList(year, month, day);

        schedulerVo = new SchedulerVO();
        schedulerVo = schedulerManager.getTodayDate(schedulerVo);

        return "success";
    }

    public String executeDay() throws Exception {

        schedulerVo = schedulerManager.getDayScheduler(year, month, day);
        schedulerVo = schedulerManager.getTodayDate(schedulerVo);

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

    public List getWeekDateList() {
        return weekDateList;
    }

    public void setWeekDateList(List weekDateList) {
        this.weekDateList = weekDateList;
    }

}
