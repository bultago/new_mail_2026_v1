package com.terracetech.tims.webmail.scheduler.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseService;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MessageParserInfoBean;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerAssetPlanVO;
import com.terracetech.tims.webmail.util.FormatUtil;

public class SchedulerAssetService extends BaseService {

    private SchedulerManager schedulerManager = null;

    public void setSchedulerManager(SchedulerManager schedulerManager) {
        this.schedulerManager = schedulerManager;
    }

    public JSONArray readAssetCategoryList() throws Exception {
        int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));

        JSONArray arrayList = null;
        try {
            arrayList = schedulerManager.getJsonAssetCategoryList(mailDomainSeq);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            throw e;
        }

        return arrayList;
    }

    public JSONObject readAssetCategoryDescription(int categorySeq) throws Exception {

        String attachesDir = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
        String hostStr = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        MessageParserInfoBean infoBean = new MessageParserInfoBean();
        infoBean.setAttachesDir(attachesDir);
        infoBean.setAttachesUrl(EnvConstants.getAttachSetting("attach.url"));
        infoBean.setStrLocalhost(hostStr);
        infoBean.setUserId(user.get(User.MAIL_UID));

        JSONObject result = null;

        try {
            result = schedulerManager.getJsonAssetCategoryDescription(categorySeq, infoBean);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            throw e;
        }

        return result;
    }

    public JSONArray readAssetScheduleList(int year, int month, int day, int assetSeq) {
        JSONArray arrayList = null;
        try {
            arrayList = schedulerManager.getAssetScheduleList(year, month, day, assetSeq);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
        }
        return arrayList;
    }

    public JSONObject readAssetSchedule(int planSeq) {
        JSONObject jObj = null;
        try {
            jObj = schedulerManager.getJsonAssetSchedule(planSeq);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
        }
        return jObj;
    }

    public JSONArray readAssetNotDuplicateList(String startDate, String endDate) throws Exception {
        int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));

        JSONArray arrayList = null;
        try {
            arrayList = schedulerManager.getAssetNotDuplicateList(mailDomainSeq, 0, startDate, endDate);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            throw e;
        }

        return arrayList;
    }

    public JSONArray readAssetNotDuplicateIgnoreMyList(String startDate, String endDate) throws Exception {
        int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        JSONArray arrayList = null;
        try {
            arrayList = schedulerManager.getAssetNotDuplicateList(mailDomainSeq, mailUserSeq, startDate, endDate);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            throw e;
        }

        return arrayList;
    }

    public String saveAssetSchedule(SchedulerAssetPlanVO scheAssetPlanVo) throws Exception {
        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        String result = null;
        try {
            scheAssetPlanVo.setMailUserSeq(mailUserSeq);
            scheAssetPlanVo.setCreateTime(FormatUtil.getBasicDateStr());
            result = schedulerManager.saveAssetSchedule(scheAssetPlanVo);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            throw e;
        }
        return result;
    }

    public String modifyAssetSchedule(SchedulerAssetPlanVO scheAssetPlanVo) throws Exception {
        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        String result = null;
        try {
            scheAssetPlanVo.setMailUserSeq(mailUserSeq);
            scheAssetPlanVo.setCreateTime(FormatUtil.getBasicDateStr());
            result = schedulerManager.modifyAssetSchedule(scheAssetPlanVo);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            throw e;
        }
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

    @SuppressWarnings("unchecked")
    public JSONObject checkAssetScheduleDuplicate(int assetSeq, int planSeq, String startDate, String endDate) {
        JSONObject result = new JSONObject();
        try {
            boolean isDup = false;
            int count = schedulerManager.checkAssetScheduleDuplicateCount(assetSeq, planSeq, startDate, endDate);
            if (count > 0) {
                isDup = true;
            }
            result.put("isSuccess", true);
            result.put("isDup", isDup);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            result.put("isSuccess", false);
        }

        return result;
    }

    public JSONObject checkMyScheduleOrShareSchedule(int planSeq) {
        int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        JSONObject result = new JSONObject();
        boolean isMime = false;
        SchedulerAssetPlanVO schedulerAssetPlanVo = null;
        int schedulerId = 0;
        try {
            schedulerAssetPlanVo = schedulerManager.checkMyScheduleOrShareSchedule(mailDomainSeq, mailUserSeq, planSeq,
                    user.get(User.EMAIL));
            if (schedulerAssetPlanVo != null && schedulerAssetPlanVo.getSchedulerId() > 0) {
                isMime = true;
                schedulerId = schedulerAssetPlanVo.getSchedulerId();
            }
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
        }
        result.put("isMime", isMime);
        result.put("schedulerId", schedulerId);
        return result;
    }
}
