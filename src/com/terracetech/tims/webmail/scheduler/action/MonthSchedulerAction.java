package com.terracetech.tims.webmail.scheduler.action;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerVO;

public class MonthSchedulerAction extends BaseAction {

    /**
     * @since 2012. 6. 21.
     */
    private static final long serialVersionUID = 1226403236910743991L;
    private SchedulerManager schedulerManager = null;
    private SchedulerVO schedulerVo = null;
    private int year = 0;
    private int month = 0;
    private int lunar = 0;

    @Override
    public String execute() throws Exception {

        schedulerVo = schedulerManager.getMonthScheduler(year, month);
        schedulerVo = schedulerManager.getTodayDate(schedulerVo);
        lunar = schedulerManager.getLunar(schedulerVo.getTodayYear(), schedulerVo.getTodayMonth(),
                schedulerVo.getTodayDay());

        return "success";
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

    public int getLunar() {
        return lunar;
    }

    public void setLunar(int lunar) {
        this.lunar = lunar;
    }

    public SchedulerVO getSchedulerVo() {
        return schedulerVo;
    }

    public void setSchedulerVo(SchedulerVO schedulerVo) {
        this.schedulerVo = schedulerVo;
    }

    public void setSchedulerManager(SchedulerManager schedulerManager) {
        this.schedulerManager = schedulerManager;
    }
}
