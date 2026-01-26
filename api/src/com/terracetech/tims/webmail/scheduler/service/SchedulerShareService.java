package com.terracetech.tims.webmail.scheduler.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseService;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerShareVO;
import com.terracetech.tims.webmail.util.StringUtils;

public class SchedulerShareService extends BaseService {

    private SchedulerManager schedulerManager = null;

    public void setSchedulerManager(SchedulerManager schedulerManager) {
        this.schedulerManager = schedulerManager;
    }

    public String saveShareGroup(String shareName, String shareColor, String targetType, String[] targetValues)
            throws Exception {
        String mailDomainSeq = user.get(User.MAIL_DOMAIN_SEQ);
        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        String result = null;
        try {
            SchedulerShareVO schedulerShareVo = new SchedulerShareVO();
            schedulerShareVo.setMailUserSeq(mailUserSeq);
            schedulerShareVo.setShareName(shareName);
            schedulerShareVo.setShareColor(shareColor);
            result = schedulerManager.saveShareGroup(schedulerShareVo, mailDomainSeq, targetType, targetValues);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            throw e;
        }

        return result;
    }

    public JSONArray getShareGroupList() throws Exception {
        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        JSONArray arrayList = null;
        try {
            arrayList = schedulerManager.getShareGroupJsonList(mailUserSeq);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            throw e;
        }
        return arrayList;
    }

    public JSONObject getShareGroupInfo(String shareSeqStr) throws Exception {
        int shareSeq = 0;
        JSONObject jObj = null;
        try {
            if (StringUtils.isNotEmpty(shareSeqStr)) {
                shareSeq = Integer.parseInt(shareSeqStr);
            }

            jObj = schedulerManager.getShareGroup(shareSeq);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            throw e;
        }
        return jObj;
    }

    public String deleteShareGroup(String shareSeqStr) throws Exception {
        int shareSeq = 0;
        String result = null;
        try {
            if (StringUtils.isNotEmpty(shareSeqStr)) {
                shareSeq = Integer.parseInt(shareSeqStr);
            }
            result = schedulerManager.deleteShareGroup(shareSeq);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            throw e;
        }

        return result;
    }

    public String modifyShareGroup(String shareSeqStr, String shareName, String shareColor, String targetType,
            String[] targetValues) throws Exception {

        String mailDomainSeq = user.get(User.MAIL_DOMAIN_SEQ);
        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        int shareSeq = 0;

        String result = null;
        try {
            if (StringUtils.isNotEmpty(shareSeqStr)) {
                shareSeq = Integer.parseInt(shareSeqStr);
            }

            SchedulerShareVO schedulerShareVo = new SchedulerShareVO();
            schedulerShareVo.setShareSeq(shareSeq);
            schedulerShareVo.setMailUserSeq(mailUserSeq);
            schedulerShareVo.setShareName(shareName);
            schedulerShareVo.setShareColor(shareColor);
            result = schedulerManager.modifyShareGroup(schedulerShareVo, mailDomainSeq, targetType, targetValues);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            throw e;
        }

        return result;
    }

}
