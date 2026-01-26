package com.terracetech.tims.webmail.scheduler.vo;

public class SchedulerHolidayVO {

    private String holidayDate = null;

    private String lunar = null;

    private String holidayName = null;

    private String holiday = null;

    private String repeat = null;

    public String getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(String holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getLunar() {
        return lunar;
    }

    public void setLunar(String lunar) {
        this.lunar = lunar;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }
}
