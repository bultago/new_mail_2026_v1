package com.terracetech.tims.webmail.scheduler.ibean;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.scheduler.vo.SchedulerAssetPlanVO;

public class SchedulerAssetBean {

    private int planSeq = 0;
    private int schedulerId = 0;
    private int assetSeq = 0;
    private int mailUserSeq = 0;
    private int planSize = 0;
    private int timeSize = 0;

    private int drowStartDate = 0;
    private int drowEndDate = 0;
    private int drowStartTime = 0;
    private int drowEndTime = 0;

    private String assetName = null;
    private String startDate = null;
    private String endDate = null;
    private String userName = null;
    private String contect = null;
    private String createTime = null;

    private List<String> drowList = null;

    public SchedulerAssetBean(SchedulerAssetPlanVO schedulerAssetPlanVo) {
        this.planSeq = schedulerAssetPlanVo.getPlanSeq();
        this.schedulerId = schedulerAssetPlanVo.getSchedulerId();
        this.assetSeq = schedulerAssetPlanVo.getAssetSeq();
        this.mailUserSeq = schedulerAssetPlanVo.getMailUserSeq();
        this.planSize = schedulerAssetPlanVo.getPlanSize();
        this.timeSize = schedulerAssetPlanVo.getTimeSize();
        this.drowStartDate = schedulerAssetPlanVo.getDrowStartDate();
        this.drowEndDate = schedulerAssetPlanVo.getDrowEndDate();
        this.drowStartTime = schedulerAssetPlanVo.getDrowStartTime();
        this.drowEndTime = schedulerAssetPlanVo.getDrowEndTime();
        this.assetName = checkNull(schedulerAssetPlanVo.getAssetName());
        this.startDate = checkNull(schedulerAssetPlanVo.getStartDate());
        this.endDate = checkNull(schedulerAssetPlanVo.getEndDate());
        this.userName = checkNull(schedulerAssetPlanVo.getUserName());
        this.contect = checkNull(schedulerAssetPlanVo.getContect());
        this.createTime = checkNull(schedulerAssetPlanVo.getCreateTime());

        this.drowList = schedulerAssetPlanVo.getDrowList();
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("planSeq", planSeq);
        json.put("schedulerId", schedulerId);
        json.put("assetSeq", assetSeq);
        json.put("mailUserSeq", mailUserSeq);
        json.put("planSize", planSize);
        json.put("timeSize", timeSize);
        json.put("drowStartDate", drowStartDate);
        json.put("drowEndDate", drowEndDate);
        json.put("drowStartTime", drowStartTime);
        json.put("drowEndTime", drowEndTime);
        json.put("assetName", assetName);
        json.put("startDate", startDate);
        json.put("endDate", endDate);
        json.put("userName", userName);
        json.put("contect", contect);
        json.put("createTime", createTime);

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
