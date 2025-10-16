package com.terracetech.tims.hybrid.mail.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.hybrid.common.HybridErrorCode;
import com.terracetech.tims.hybrid.common.action.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.home.manager.MailHomeManager;
import com.terracetech.tims.webmail.home.vo.MailMenuLayoutVO;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerDataVO;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class MailHomeServiceAction extends BaseAction {
    
    private MailHomeManager homeManager = null;
    private SchedulerManager schedulerManager = null;

    public void setHomeManager(MailHomeManager homeManager) {
        this.homeManager = homeManager;
    }

    public void setSchedulerManager(SchedulerManager schedulerManager) {
        this.schedulerManager = schedulerManager;
    }

    @SuppressWarnings("unchecked")
    public String execute() throws Exception {
        int errorCode = HybridErrorCode.SUCCESS;
        JSONObject result = new JSONObject();
        JSONObject userInfo = makeUserInfo();
        userInfo = makeMenuStatus(homeManager, userInfo);
        userInfo.put("installLocale", EnvConstants.getBasicSetting("setup.state").toLowerCase());
        result.put("userInfo", userInfo);
        result.put(HybridErrorCode.CODE_NAME, errorCode);
        ResponseUtil.makeJsonpResponse(request, response, result);
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public String executeSchedule() throws Exception {
        int errorCode = HybridErrorCode.SUCCESS;
        JSONObject result = new JSONObject();
        int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        String email = user.get(User.EMAIL);
        try {
            List<SchedulerDataVO> todayScheduleList = schedulerManager.getDayScheduleList(0, 0, 0, mailDomainSeq, mailUserSeq, email);
            result.put("scheduleList", makeTodaySchedule(todayScheduleList));
        } catch (Exception e) {
            errorCode = HybridErrorCode.ERROR;
            LogManager.writeErr(this, e.getMessage(), e);
        }
        result.put(HybridErrorCode.CODE_NAME, errorCode);
        ResponseUtil.makeJsonpResponse(request, response, result);
        return null;
    }
    
    @SuppressWarnings("unchecked")
    private JSONObject makeUserInfo() {
        JSONObject userJson = new JSONObject();
        userJson.put("name", StringUtils.EscapeHTMLTag(user.get(User.USER_NAME)));
        userJson.put("email", StringUtils.EscapeHTMLTag(user.get(User.EMAIL)));
        
        String loginInfo = user.get(User.WEBMAIL_LOGIN_TIME);
        String[] loginData = loginInfo.split("\\|"); 
        userJson.put("time", loginData[0]);
        userJson.put("ip", loginData[1]);
        
        return userJson;
    }
    
    @SuppressWarnings("unchecked")
    private JSONArray makeTodaySchedule(List<SchedulerDataVO> todayScheduleList) {
        JSONArray array = new JSONArray();
        JSONObject obj = null;
        String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
        if (todayScheduleList != null && todayScheduleList.size() > 0) {
            for (SchedulerDataVO schedulerDataVO : todayScheduleList) {
                obj = new JSONObject();
                obj.put("title", schedulerDataVO.getTitle());
                obj.put("schedulerId", schedulerDataVO.getSchedulerId());
                obj.put("startTime", schedulerDataVO.getStartDate().substring(8));
                obj.put("endTime", schedulerDataVO.getEndDate().substring(8));
                obj.put("allDay", schedulerDataVO.getAllDay());
                obj.put("today", today);
                array.add(obj);
            }
        }
        return array;
    }
    
    @SuppressWarnings("unchecked")
    private JSONObject makeMenuStatus(MailHomeManager homeManager, JSONObject jsonObject) {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
        int mailGroupSeq = Integer.parseInt(user.get(User.MAIL_GROUP_SEQ));
        
        MailMenuLayoutVO[] topMenus = homeManager.readMenusList(getMessageResource("common"), mailDomainSeq, mailGroupSeq);
        
        String useMail = "off";
        String useAddr = "off";
        String useCal = "off";
        String useBbs = "off";
        if (topMenus != null) {
            for (MailMenuLayoutVO mailMenuLayoutVO : topMenus) {
                if ("mail".equals(mailMenuLayoutVO.getMenuId())) {
                    useMail = "on";
                    continue;
                }
                if ("addr".equals(mailMenuLayoutVO.getMenuId())) {
                    useAddr = "on";
                    continue;
                }
                if ("calendar".equals(mailMenuLayoutVO.getMenuId())) {
                    useCal = "on";
                    continue;
                }
                if ("bbs".equals(mailMenuLayoutVO.getMenuId())) {
                    useBbs = "on";
                    continue;
                }
            }
        }
        
        jsonObject.put("useMail", useMail);
        jsonObject.put("useAddr", useAddr);
        jsonObject.put("useCal", useCal);
        jsonObject.put("useBbs", useBbs);
        
        return jsonObject;
    }
}
