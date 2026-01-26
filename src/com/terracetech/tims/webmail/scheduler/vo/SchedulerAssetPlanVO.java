package com.terracetech.tims.webmail.scheduler.vo;

import java.util.List;

public class SchedulerAssetPlanVO {

    private int categorySeq = 0;
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

    private String categoryName = null;
    private String assetName = null;
    private String startDate = null;
    private String endDate = null;
    private String userName = null;
    private String contect = null;
    private String createTime = null;

    private List<String> drowList = null;

    public int getCategorySeq() {
        return categorySeq;
    }

    public void setCategorySeq(int categorySeq) {
        this.categorySeq = categorySeq;
    }

    public int getPlanSeq() {
        return planSeq;
    }

    public void setPlanSeq(int planSeq) {
        this.planSeq = planSeq;
    }

    public int getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(int schedulerId) {
        this.schedulerId = schedulerId;
    }

    public int getAssetSeq() {
        return assetSeq;
    }

    public void setAssetSeq(int assetSeq) {
        this.assetSeq = assetSeq;
    }

    public int getMailUserSeq() {
        return mailUserSeq;
    }

    public void setMailUserSeq(int mailUserSeq) {
        this.mailUserSeq = mailUserSeq;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getContect() {
        return contect;
    }

    public void setContect(String contect) {
        this.contect = contect;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<String> getDrowList() {
        return drowList;
    }

    public void setDrowList(List<String> drowList) {
        this.drowList = drowList;
    }

}
