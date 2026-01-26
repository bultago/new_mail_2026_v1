package com.terracetech.tims.webmail.scheduler.service;

import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseService;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerDataVO;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class SchedulerService extends BaseService {

    private SchedulerManager schedulerManager = null;

    public void setSchedulerManager(SchedulerManager schedulerManager) {
        this.schedulerManager = schedulerManager;
    }

    public JSONArray getJsonMonthScheduleList(int year, int month) throws Exception {
        int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        JSONArray arrayList = null;
        try {
            arrayList = schedulerManager.getJsonMonthScheduleList(year, month, mailDomainSeq, mailUserSeq,
                    user.get(User.EMAIL));
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
        }

        return arrayList;
    }

    public JSONArray getJsonWeekScheduleList(int year, int month, int day) throws Exception {
        int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        JSONArray arrayList = null;
        try {
            arrayList = schedulerManager.getJsonWeekScheduleList(year, month, day, mailDomainSeq, mailUserSeq,
                    user.get(User.EMAIL));
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            throw e;
        }

        return arrayList;
    }

    public JSONArray getJsonDayScheduleList(int year, int month, int day) throws Exception {

        int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        JSONArray arrayList = null;
        try {
            arrayList = schedulerManager.getJsonDayScheduleList(year, month, day, mailDomainSeq, mailUserSeq,
                    user.get(User.EMAIL));
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            throw e;
        }

        return arrayList;
    }

    public JSONObject saveSchedule(SchedulerDataVO schedulerDataVo) throws Exception {
        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        JSONObject result = new JSONObject();
        Map map = null;

        try {
            schedulerDataVo.setMailUserSeq(mailUserSeq);
            map = schedulerManager.saveSchedule(schedulerDataVo);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            throw e;
        }

        result.put("isSuccess", map.get("isSuccess"));
        result.put("schedulerId", map.get("schedulerId"));

        return result;
    }

    public JSONObject modifySchedule(SchedulerDataVO schedulerDataVo) throws Exception {
        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        JSONObject result = new JSONObject();
        Map map = null;

        try {
            schedulerDataVo.setMailUserSeq(mailUserSeq);
            map = schedulerManager.modifySchedule(schedulerDataVo);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            throw e;
        }

        result.put("isSuccess", map.get("isSuccess"));
        result.put("schedulerId", map.get("schedulerId"));

        return result;
    }

    public JSONObject repeatModifySchedule(SchedulerDataVO schedulerDataVo) throws Exception {
        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        JSONObject result = new JSONObject();
        Map map = null;
        try {
            int schedulerId = schedulerDataVo.getSchedulerId();
            schedulerDataVo.setMailUserSeq(mailUserSeq);
            schedulerDataVo.setCreateTime(FormatUtil.getBasicDateStr());
            schedulerDataVo.setModifyTime(FormatUtil.getBasicDateStr());
            schedulerDataVo.setOutlookSync("unsync");
            map = schedulerManager.saveSchedule(schedulerDataVo);
            if ("success".equals(map.get("isSuccess"))) {
                schedulerManager.saveRepeatIgnore(schedulerId, schedulerDataVo.getIgnoreTime());
            }
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            throw e;
        }

        result.put("isSuccess", map.get("isSuccess"));

        return result;
    }

    public JSONObject deleteSchedule(int schedulerId, String sendMail) {
        JSONObject result = new JSONObject();
        Map map = null;
        String deleteResult = "fail";
        try {
            deleteResult = schedulerManager.deleteSchedule(schedulerId, sendMail);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
        }

        result.put("isSuccess", deleteResult);
        result.put("schedulerId", schedulerId);

        return result;
    }

    public String deleteAssetSchedule(int planSeq) {
        String result = null;
        try {
            result = schedulerManager.deleteAssetSchedule(planSeq);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
        }
        return result;
    }

    public String deleteRepeatSchedule(int schedulerId, String repeatStartDate) {
        String result = null;
        try {
            result = schedulerManager.deleteRepeatSchedule(schedulerId, repeatStartDate);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
        }
        return result;
    }

    public JSONObject getJsonSchedule(int schedulerId) {
        int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
        I18nResources resource = getMessageResource("scheduler");
        JSONObject jObj = null;
        String installLocale = EnvConstants.getBasicSetting("setup.state");
        String userLocale = user.get(User.LOCALE);
        String orgLocale = userLocale;
        if ("jp".equals(installLocale)) {
            orgLocale = "ko".equals(userLocale) ? "en" : userLocale;
        } else if ("ko".equals(installLocale)) {
            orgLocale = "jp".equals(userLocale) ? "en" : userLocale;
        }
        try {
            jObj = schedulerManager.getJsonSchedule(mailDomainSeq, schedulerId, orgLocale,
                    resource.getMessage("scheduler.share.target.domain"));
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
        }
        return jObj;
    }

    public JSONArray getJsonScheduleList(int schedulerId, String firstDate, String endDate) {
        JSONArray arrayList = null;
        try {
            arrayList = schedulerManager.getJsonScheduleList(schedulerId, firstDate, endDate);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
        }
        return arrayList;
    }

    public JSONArray getJsonSearchScheduleList(String searchType, String keyWord, int currentPage) throws Exception {
        JSONArray arrayList = null;
        int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        int pageBase = 15;
        String pageBaseStr = user.get(User.PAGE_LINE_CNT);

        try {
            if (StringUtils.isNotEmpty(pageBaseStr)) {
                pageBase = Integer.parseInt(pageBaseStr);
            }
            int skipResult = 0;
            skipResult = (currentPage - 1) * pageBase;
            arrayList = schedulerManager.getJsonSearchScheduleList(mailDomainSeq, mailUserSeq, user.get(User.EMAIL),
                    searchType, keyWord, currentPage, skipResult, pageBase);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            throw e;
        }

        return arrayList;
    }

    public JSONArray getDnDJsonSchedule(int year, int month, int schedulerId, String startDate, String firstDate,
            String lastDate) {
        JSONArray arrayList = null;
        try {
            arrayList = schedulerManager.getDnDJsonSchedule(year, month, schedulerId, startDate, firstDate, lastDate);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
        }
        return arrayList;
    }

    public JSONArray getJsonHoliday(int thisYear, String startDate) {
        int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
        JSONArray arrayList = null;
        try {
            arrayList = schedulerManager.getJsonHoliday(mailDomainSeq, thisYear, startDate);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
        }
        return arrayList;
    }

    public JSONObject getMonthInfo(int thisYear, int thisMonth) {
        JSONObject jObj = null;
        try {
            jObj = schedulerManager.getMonthInfo(thisYear, thisMonth);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
        }
        return jObj;
    }

    public JSONObject getWeekInfo(int thisYear, int thisMonth, int thisDay) {
        JSONObject jObj = null;
        try {
            jObj = schedulerManager.getWeekInfo(thisYear, thisMonth, thisDay);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
        }
        return jObj;
    }

    public JSONObject getDayInfo(int thisYear, int thisMonth, int thisDay) {
        JSONObject jObj = null;
        try {
            jObj = schedulerManager.getDayInfo(thisYear, thisMonth, thisDay);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
        }
        return jObj;
    }

    public JSONObject getCalendarInfo(int thisYear, int thisMonth) {
        JSONObject jObj = null;
        try {
            jObj = schedulerManager.getMonthInfo(thisYear, thisMonth);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
        }
        return jObj;
    }

    public void sendMailProcess(String type, int schedulerId, int shareSeq, String subject, String content) {
        schedulerManager.sendMailProcess(user, getMessageResource(), type, schedulerId, shareSeq, subject, content);
    }
}
