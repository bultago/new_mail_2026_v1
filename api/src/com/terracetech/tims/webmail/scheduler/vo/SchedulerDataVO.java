package com.terracetech.tims.webmail.scheduler.vo;

import java.util.List;

import org.json.simple.JSONArray;

public class SchedulerDataVO {
    private int schedulerId = 0;
    private int mailUserSeq = 0;

    /**
     * yyyyMMddHHmm
     */
    private String startDate = null;
    private String endDate = null;
    private String title = null;
    private String location = null;
    private String content = null;
    private String checkSendMail = null;

    /**
     * <pre>
     * on / off
     * </pre>
     */
    private String allDay = null;

    /**
     * <pre>
     * on / off
     * </pre>
     */
    private String holiday = null;

    @Deprecated
    private String lunar = null;

    /**
     * <pre>
     * on / off
     * </pre>
     */
    private String repeatFlag = null;

    /**
     * <pre>
     * �ϰ� : 01
     * �ְ� : 02
     * �� : 03
     * ���� : 04
     * 
     *  ex)
     * �ϰ� 010105 -> 5�� ���� (��� 01�� �ǹ̾���)
     * �ְ� 020603 -> 6�ָ��� ȭ���� (1�� �Ͽ���, 3�� ȭ����)
     * �� 030203 -> 2����� 3�Ͽ�
     * �� 03010203 -> 1����� ��°�� ȭ����
     * ���� 040501  -> �ų� 5�� 1�Ͽ�
     * </pre>
     */
    private String repeatTerm = null;

    /**
     * yyyyMMdd 20100727
     */
    private String repeatEndDate = null;
    private String createTime = null;
    private String modifyTime = null;
    private String checkShare = null;
    private String shareName = null;
    private String shareValue = null;
    private String shareColor = null;
    private String outlookSync = null;
    private String ignoreTime = null;
    private String userName = null;

    private String checkAsset = null;
    private String assetReserveValue = null;

    /**
     * ���࿡ �ִ� ����ó
     */
    private String contect = null;

    private int shareSeq = 0;
    private int planSize = 0;
    private int timeSize = 0;

    private int drowStartDate = 0;
    private int drowEndDate = 0;
    private int drowStartTime = 0;
    private int drowEndTime = 0;

    private String checkSelfTarget = null;
    private String[] selfTargetList = null;
    private List<String> drowList = null;
    private List<String> ignoreTimeList = null;
    private JSONArray shareTagetNameList = null;
    private JSONArray shareSelfTargetList = null;
    private JSONArray assetPlanInfoList = null;

    public int getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(int schedulerId) {
        this.schedulerId = schedulerId;
    }

    public int getMailUserSeq() {
        return mailUserSeq;
    }

    public void setMailUserSeq(int mailUserSeq) {
        this.mailUserSeq = mailUserSeq;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCheckSendMail() {
        return checkSendMail;
    }

    public void setCheckSendMail(String checkSendMail) {
        this.checkSendMail = checkSendMail;
    }

    public String getAllDay() {
        return allDay;
    }

    public void setAllDay(String allDay) {
        this.allDay = allDay;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getLunar() {
        return lunar;
    }

    public void setLunar(String lunar) {
        this.lunar = lunar;
    }

    public String getRepeatFlag() {
        return repeatFlag;
    }

    public void setRepeatFlag(String repeatFlag) {
        this.repeatFlag = repeatFlag;
    }

    public String getRepeatTerm() {
        return repeatTerm;
    }

    public void setRepeatTerm(String repeatTerm) {
        this.repeatTerm = repeatTerm;
    }

    public String getRepeatEndDate() {
        return repeatEndDate;
    }

    public void setRepeatEndDate(String repeatEndDate) {
        this.repeatEndDate = repeatEndDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getCheckShare() {
        return checkShare;
    }

    public void setCheckShare(String checkShare) {
        this.checkShare = checkShare;
    }

    public int getShareSeq() {
        return shareSeq;
    }

    public void setShareSeq(int shareSeq) {
        this.shareSeq = shareSeq;
    }

    public String getShareName() {
        return shareName;
    }

    public void setShareName(String shareName) {
        this.shareName = shareName;
    }

    public String getShareValue() {
        return shareValue;
    }

    public void setShareValue(String shareValue) {
        this.shareValue = shareValue;
    }

    public String getShareColor() {
        return shareColor;
    }

    public void setShareColor(String shareColor) {
        this.shareColor = shareColor;
    }

    public int getPlanSize() {
        return planSize;
    }

    public void setPlanSize(int planSize) {
        this.planSize = planSize;
    }

    public int getTimeSize() {
        return timeSize;
    }

    public void setTimeSize(int timeSize) {
        this.timeSize = timeSize;
    }

    public int getDrowStartDate() {
        return drowStartDate;
    }

    public void setDrowStartDate(int drowStartDate) {
        this.drowStartDate = drowStartDate;
    }

    public int getDrowEndDate() {
        return drowEndDate;
    }

    public void setDrowEndDate(int drowEndDate) {
        this.drowEndDate = drowEndDate;
    }

    public int getDrowStartTime() {
        return drowStartTime;
    }

    public void setDrowStartTime(int drowStartTime) {
        this.drowStartTime = drowStartTime;
    }

    public int getDrowEndTime() {
        return drowEndTime;
    }

    public void setDrowEndTime(int drowEndTime) {
        this.drowEndTime = drowEndTime;
    }

    public List<String> getDrowList() {
        return drowList;
    }

    public void setDrowList(List<String> drowList) {
        this.drowList = drowList;
    }

    public String getOutlookSync() {
        return outlookSync;
    }

    public void setOutlookSync(String outlookSync) {
        this.outlookSync = outlookSync;
    }

    public String getIgnoreTime() {
        return ignoreTime;
    }

    public void setIgnoreTime(String ignoreTime) {
        this.ignoreTime = ignoreTime;
    }

    public List<String> getIgnoreTimeList() {
        return ignoreTimeList;
    }

    public void setIgnoreTimeList(List<String> ignoreTimeList) {
        this.ignoreTimeList = ignoreTimeList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public JSONArray getShareTagetNameList() {
        return shareTagetNameList;
    }

    public void setShareTagetNameList(JSONArray shareTagetNameList) {
        this.shareTagetNameList = shareTagetNameList;
    }

    public String getCheckAsset() {
        return checkAsset;
    }

    public void setCheckAsset(String checkAsset) {
        this.checkAsset = checkAsset;
    }

    public String getAssetReserveValue() {
        return assetReserveValue;
    }

    public void setAssetReserveValue(String assetReserveValue) {
        this.assetReserveValue = assetReserveValue;
    }

    public String getContect() {
        return contect;
    }

    public void setContect(String contect) {
        this.contect = contect;
    }

    public JSONArray getAssetPlanInfoList() {
        return assetPlanInfoList;
    }

    public void setAssetPlanInfoList(JSONArray assetPlanInfoList) {
        this.assetPlanInfoList = assetPlanInfoList;
    }

    public String[] getSelfTargetList() {
        return selfTargetList;
    }

    public void setSelfTargetList(String[] selfTargetList) {
        this.selfTargetList = selfTargetList;
    }

    public String getCheckSelfTarget() {
        return checkSelfTarget;
    }

    public void setCheckSelfTarget(String checkSelfTarget) {
        this.checkSelfTarget = checkSelfTarget;
    }

    public JSONArray getShareSelfTargetList() {
        return shareSelfTargetList;
    }

    public void setShareSelfTargetList(JSONArray shareSelfTargetList) {
        this.shareSelfTargetList = shareSelfTargetList;
    }
}
