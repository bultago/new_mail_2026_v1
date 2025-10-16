package com.terracetech.tims.webmail.scheduler.ibean;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.scheduler.vo.SchedulerDataVO;

public class SchedulerBean {
    private int schedulerId = 0;
    private int mailUserSeq = 0;
    private String startDate = null;
    private String endDate = null;
    private String title = null;
    private String location = null;
    private String content = null;
    private String allDay = null;
    private String holiday = null;
    private String lunar = null;
    private String checkSendMail = null;
    private String repeatFlag = null;
    private String repeatTerm = null;
    private String repeatEndDate = null;
    private String createTime = null;
    private String checkShare = null;
    private String shareName = null;
    private String shareValue = null;
    private String shareColor = null;
    private String userName = null;
    private String checkAsset = null;

    private int planSize = 0;
    private int timeSize = 0;

    private int drowStartDate = 0;
    private int drowEndDate = 0;
    private int drowStartTime = 0;
    private int drowEndTime = 0;

    private List<String> drowList = null;
    private JSONArray shareSelfTargetList = null;
    private JSONArray shareTagetNameList = null;
    private JSONArray assetPlanInfoList = null;

    public SchedulerBean(SchedulerDataVO schedulerDataVo) {
        this.schedulerId = schedulerDataVo.getSchedulerId();
        this.mailUserSeq = schedulerDataVo.getMailUserSeq();
        this.startDate = checkNull(schedulerDataVo.getStartDate());
        this.endDate = checkNull(schedulerDataVo.getEndDate());
        this.title = checkNull(schedulerDataVo.getTitle());
        this.location = checkNull(schedulerDataVo.getLocation());
        this.content = checkNull(schedulerDataVo.getContent());
        this.allDay = checkNull(schedulerDataVo.getAllDay());
        this.holiday = checkNull(schedulerDataVo.getHoliday());
        this.lunar = checkNull(schedulerDataVo.getLunar());
        this.checkSendMail = checkNull(schedulerDataVo.getCheckSendMail());
        this.repeatFlag = checkNull(schedulerDataVo.getRepeatFlag());
        this.repeatTerm = checkNull(schedulerDataVo.getRepeatTerm());
        this.repeatEndDate = checkNull(schedulerDataVo.getRepeatEndDate());
        this.createTime = checkNull(schedulerDataVo.getCreateTime());
        this.planSize = schedulerDataVo.getPlanSize();
        this.timeSize = schedulerDataVo.getTimeSize();
        this.drowStartDate = schedulerDataVo.getDrowStartDate();
        this.drowEndDate = schedulerDataVo.getDrowEndDate();
        this.drowStartTime = schedulerDataVo.getDrowStartTime();
        this.drowEndTime = schedulerDataVo.getDrowEndTime();
        this.drowList = schedulerDataVo.getDrowList();
        this.checkShare = schedulerDataVo.getCheckShare();
        this.shareName = schedulerDataVo.getShareName();
        this.shareValue = schedulerDataVo.getShareValue();
        this.shareColor = schedulerDataVo.getShareColor();
        this.userName = schedulerDataVo.getUserName();
        this.shareTagetNameList = schedulerDataVo.getShareTagetNameList();
        this.assetPlanInfoList = schedulerDataVo.getAssetPlanInfoList();
        this.shareSelfTargetList = schedulerDataVo.getShareSelfTargetList();
        this.checkAsset = schedulerDataVo.getCheckAsset();
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("schedulerId", schedulerId);
        json.put("mailUserSeq", mailUserSeq);
        json.put("startDate", startDate);
        json.put("endDate", endDate);
        json.put("title", title);
        json.put("location", location);
        json.put("content", content);
        json.put("allDay", allDay);
        json.put("holiday", holiday);
        json.put("lunar", lunar);
        json.put("checkSendMail", checkSendMail);
        json.put("repeatFlag", repeatFlag);
        json.put("repeatTerm", repeatTerm);
        json.put("repeatEndDate", repeatEndDate);
        json.put("createTime", createTime);
        json.put("planSize", planSize);
        json.put("timeSize", timeSize);
        json.put("drowStartDate", drowStartDate);
        json.put("drowEndDate", drowEndDate);
        json.put("drowStartTime", drowStartTime);
        json.put("drowEndTime", drowEndTime);
        json.put("checkShare", checkShare);
        json.put("shareName", shareName);
        json.put("shareValue", shareValue);
        json.put("shareColor", shareColor);
        json.put("userName", userName);
        json.put("checkAsset", checkAsset);
        json.put("shareTagetNameList", shareTagetNameList);
        json.put("assetPlanInfoList", assetPlanInfoList);
        json.put("shareSelfTargetList", shareSelfTargetList);

        JSONArray drowplanList = null;
        if (drowList != null && drowList.size() > 0) {
            drowplanList = new JSONArray();
            for (int i = 0; i < drowList.size(); i++) {
                drowplanList.add(drowList.get(i));
            }
        }

        json.put("drowplanList", drowplanList);

        return json;
    }

    private String checkNull(String source) {
        if (source == null) {
            source = "";
        }
        return source;
    }
}
