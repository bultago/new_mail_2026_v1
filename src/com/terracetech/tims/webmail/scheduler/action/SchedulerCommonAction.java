package com.terracetech.tims.webmail.scheduler.action;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.util.FormatUtil;

public class SchedulerCommonAction extends BaseAction {

    private static final long serialVersionUID = 20090610L;

    private MailUserManager mailUserManager = null;

    private String type = null;
    private int calendarYear = 0;
    private int calendarMonth = 0;
    private int calendarDay = 0;
    private int scheduleId = 0;
    private String calendarStartDate = null;
    private String calendarEndDate = null;
    private String installLocale = null;
    private String userName = null;
    private String mobileNo = null;
    private String currentTime = null;

    public void setMailUserManager(MailUserManager mailUserManager) {
        this.mailUserManager = mailUserManager;
    }

    public String loadPage() throws Exception {

        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        installLocale = EnvConstants.getBasicSetting("setup.state");
        userName = user.get(User.USER_NAME);
        mobileNo = mailUserManager.readUserInfoMobileNo(mailUserSeq);
        currentTime = FormatUtil.getBasicDateStr();

        return "load";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public int getCalendarYear() {
        return calendarYear;
    }

    public void setCalendarYear(int calendarYear) {
        this.calendarYear = calendarYear;
    }

    public int getCalendarMonth() {
        return calendarMonth;
    }

    public void setCalendarMonth(int calendarMonth) {
        this.calendarMonth = calendarMonth;
    }

    public int getCalendarDay() {
        return calendarDay;
    }

    public void setCalendarDay(int calendarDay) {
        this.calendarDay = calendarDay;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getCalendarStartDate() {
        return calendarStartDate;
    }

    public void setCalendarStartDate(String calendarStartDate) {
        this.calendarStartDate = calendarStartDate;
    }

    public String getCalendarEndDate() {
        return calendarEndDate;
    }

    public void setCalendarEndDate(String calendarEndDate) {
        this.calendarEndDate = calendarEndDate;
    }

    public String getInstallLocale() {
        return installLocale;
    }

    public String getUserName() {
        return userName;
    }

    public String getMobileNo() {
        return mobileNo;
    }
}
