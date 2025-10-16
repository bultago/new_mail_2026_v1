package com.terracetech.tims.webmail.scheduler.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.scheduler.vo.SchedulerAssetCategoryVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerAssetPlanVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerDataVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerHolidayVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerShareExtVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerShareTargetVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerShareVO;

public class SchedulerDao extends SqlMapClientDaoSupport implements IschedulerDao {

    public void saveSchedule(SchedulerDataVO schedulerDataVo) {
        getSqlMapClientTemplate().insert("Scheduler.saveSchedule", schedulerDataVo);
    }

    public void modifySchedule(SchedulerDataVO schedulerDataVo) {
        getSqlMapClientTemplate().update("Scheduler.modifySchedule", schedulerDataVo);
    }

    public void deleteSchedule(int scheduleId) {
        getSqlMapClientTemplate().delete("Scheduler.deleteSchedule", scheduleId);
    }

    public List<SchedulerDataVO> readScheduleList(int mailUserSeq, int[] schedulerIds, String firstDay, String lastDay) {
        Map paramMap = new HashMap();
        paramMap.put("mailUserSeq", mailUserSeq);
        paramMap.put("schedulerIds", schedulerIds);
        paramMap.put("firstDay", firstDay);
        paramMap.put("lastDay", lastDay);

        return getSqlMapClientTemplate().queryForList("Scheduler.readScheduleList", paramMap);
    }

    public SchedulerDataVO readSchedule(int schedulerId) {
        return (SchedulerDataVO) getSqlMapClientTemplate().queryForObject("Scheduler.readSchedule", schedulerId);
    }

    @SuppressWarnings("unchecked")
    public List<SchedulerDataVO> searchScheduleList(int mailUserSeq, String searchType, String keyWord, int skipResult,
            int maxResult) {
        keyWord = "%" + keyWord + "%";
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("mailUserSeq", mailUserSeq);
        paramMap.put("searchType", searchType);
        paramMap.put("keyWord", keyWord);

        paramMap.put("skipResult", skipResult);
        paramMap.put("maxResult", maxResult);

        return getSqlMapClientTemplate().queryForList("Scheduler.searchSchedule", paramMap);
    }

    public int searchScheduleListCount(int mailUserSeq, String searchType, String keyWord) {
        keyWord = "%" + keyWord + "%";
        Map paramMap = new HashMap();
        paramMap.put("mailUserSeq", mailUserSeq);
        paramMap.put("searchType", searchType);
        paramMap.put("keyWord", keyWord);
        return (Integer) getSqlMapClientTemplate().queryForObject("Scheduler.searchScheduleCount", paramMap);
    }

    public List<SchedulerHolidayVO> readScheduleHoliday(int mailDomainSeq) {
        return getSqlMapClientTemplate().queryForList("Scheduler.readScheduleHoliday", mailDomainSeq);
    }

    // schedulerShare

    public void saveShareGroup(SchedulerShareVO schedulerShareVo) {
        getSqlMapClientTemplate().insert("Scheduler.saveShareGroup", schedulerShareVo);
    }

    public void saveShareGroupTarget(SchedulerShareTargetVO schedulerShareTargetVo) {
        getSqlMapClientTemplate().insert("Scheduler.saveShareGroupTarget", schedulerShareTargetVo);
    }

    public void modifyShareGroup(SchedulerShareVO schedulerShareVo) {
        getSqlMapClientTemplate().update("Scheduler.modifyShareGroup", schedulerShareVo);
    }

    public List<SchedulerShareVO> readShareGroupList(int mailUserSeq) {
        return getSqlMapClientTemplate().queryForList("Scheduler.readShareGroupList", mailUserSeq);
    }

    public SchedulerShareVO readShareGroup(int shareSeq) {
        return (SchedulerShareVO) getSqlMapClientTemplate().queryForObject("Scheduler.readShareGroup", shareSeq);
    }

    public List<SchedulerShareTargetVO> readShareTarget(int shareSeq) {
        return getSqlMapClientTemplate().queryForList("Scheduler.readShareTarget", shareSeq);
    }

    public void deleteShareGroup(int shareSeq) {
        getSqlMapClientTemplate().delete("Scheduler.deleteShareGroup", shareSeq);
    }

    public void deleteShareGroupTarget(int shareSeq) {
        getSqlMapClientTemplate().delete("Scheduler.deleteShareGroupTarget", shareSeq);
    }

    public void saveShareSchedule(int schedulerId, int shareSeq) {
        Map<String, Integer> paramMap = new HashMap<String, Integer>();
        paramMap.put("schedulerId", schedulerId);
        paramMap.put("shareSeq", shareSeq);
        getSqlMapClientTemplate().insert("Scheduler.saveShareSchedule", paramMap);
    }

    public void modifyShareSchedule(int schedulerId, int shareSeq) {
        Map<String, Integer> paramMap = new HashMap<String, Integer>();
        paramMap.put("schedulerId", schedulerId);
        paramMap.put("shareSeq", shareSeq);
        getSqlMapClientTemplate().update("Scheduler.modifyShareSchedule", paramMap);
    }

    public void deleteShareSchedule(int schedulerId) {
        getSqlMapClientTemplate().delete("Scheduler.deleteShareSchedule", schedulerId);
    }

    public SchedulerShareVO readShareSchedule(int schedulerId) {
        return (SchedulerShareVO) getSqlMapClientTemplate().queryForObject("Scheduler.readShareSchedule", schedulerId);
    }

    public List<SchedulerDataVO> readSchedulerShareInfo(int mailDomainSeq, int mailUserSeq, String email) {
        Map paramMap = new HashMap();
        paramMap.put("mailDomainSeq", mailDomainSeq);
        paramMap.put("mailUserSeq", mailUserSeq);
        paramMap.put("email", email);
        return getSqlMapClientTemplate().queryForList("Scheduler.readSchedulerShareInfo", paramMap);
    }

    public List<SchedulerDataVO> searchshareScheduleList(int mailUserSeq, int[] schedulerIds, String searchType,
            String keyWord) {
        keyWord = "%" + keyWord + "%";
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("mailUserSeq", mailUserSeq);
        paramMap.put("searchType", searchType);
        paramMap.put("keyWord", keyWord);
        paramMap.put("schedulerIds", schedulerIds);

        return getSqlMapClientTemplate().queryForList("Scheduler.searchShareSchedule", paramMap);
    }

    public void schedulerOutlookMark(int[] schedulerIds, String modifyTime, String mark) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("schedulerIds", schedulerIds);
        paramMap.put("modifyTime", modifyTime);
        paramMap.put("mark", mark);

        getSqlMapClientTemplate().update("Scheduler.schedulerOutlookMark", paramMap);
    }

    public List<Integer> getSchedulerIdOutlookSyncStatus(int mailUserSeq, String syncStatus, String firstDay) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("mailUserSeq", mailUserSeq);
        paramMap.put("syncStatus", syncStatus);
        paramMap.put("firstDay", firstDay);

        return getSqlMapClientTemplate().queryForList("Scheduler.schedulerIdOutlookSyncStatus", paramMap);
    }

    public List<SchedulerDataVO> getSchedulerShareModifyTime(int[] schedulerIds, String firstDay) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("schedulerIds", schedulerIds);
        paramMap.put("firstDay", firstDay);

        return getSqlMapClientTemplate().queryForList("Scheduler.schedulerShareModifyTime", paramMap);
    }

    public List<SchedulerDataVO> getSchedulerListOutlookSyncStatus(int mailUserSeq, String syncStatus, String firstDay) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("mailUserSeq", mailUserSeq);
        paramMap.put("syncStatus", syncStatus);
        paramMap.put("firstDay", firstDay);

        return getSqlMapClientTemplate().queryForList("Scheduler.schedulerListOutlookSyncStatus", paramMap);
    }

    public List<SchedulerDataVO> getSchedulerShareListOutlookSyncStatus(int mailUserSeq, String syncStatus,
            String firstDay, int[] schedulerIds) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("mailUserSeq", mailUserSeq);
        paramMap.put("syncStatus", syncStatus);
        paramMap.put("firstDay", firstDay);
        paramMap.put("schedulerIds", schedulerIds);

        return getSqlMapClientTemplate().queryForList("Scheduler.schedulerShareListOutlookSyncStatus", paramMap);
    }

    public List<SchedulerDataVO> getSchedulebyIds(int[] schedulerIds) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("schedulerIds", schedulerIds);

        return getSqlMapClientTemplate().queryForList("Scheduler.readSchedulebyIds", paramMap);
    }

    public void changeSyncFlag(int schedulerId, String syncStatus) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("schedulerId", schedulerId);
        paramMap.put("syncStatus", syncStatus);

        getSqlMapClientTemplate().update("Scheduler.changeSyncFlag", paramMap);
    }

    @SuppressWarnings("unchecked")
    public void saveRepeatIgnore(int schedulerId, String repeatStartDate) {
        Map paramMap = new HashMap();
        paramMap.put("schedulerId", schedulerId);
        paramMap.put("repeatStartDate", repeatStartDate);
        getSqlMapClientTemplate().insert("Scheduler.saveRepeatIgnore", paramMap);
    }

    @SuppressWarnings("unchecked")
    public List<SchedulerDataVO> readRepeatIgnoreList(int schedulerId) {
        return getSqlMapClientTemplate().queryForList("Scheduler.readRepeatIgnoreList", schedulerId);
    }

    public void deleteRepeatIgnore(int schedulerId) {
        getSqlMapClientTemplate().delete("Scheduler.deleteRepeatIgnore", schedulerId);
    }

    @SuppressWarnings("unchecked")
    public List<SchedulerAssetCategoryVO> readSchedulerAssetCategoryList(int mailDomainSeq) {
        return getSqlMapClientTemplate().queryForList("Scheduler.readSchedulerAssetCategoryList", mailDomainSeq);
    }

    public List<SchedulerAssetCategoryVO> readAssetNotDuplicateList(int mailDomainSeq, int mailUserSeq,
            String startDate, String endDate) {
        Map paramMap = new HashMap();
        paramMap.put("mailDomainSeq", mailDomainSeq);
        paramMap.put("mailUserSeq", mailUserSeq);
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);

        return getSqlMapClientTemplate().queryForList("Scheduler.readAssetNotDuplicateList", paramMap);
    }

    public SchedulerAssetCategoryVO readSchedulerAssetCategoryDescription(int categorySeq) {
        return (SchedulerAssetCategoryVO) getSqlMapClientTemplate().queryForObject(
                "Scheduler.readSchedulerAssetCategoryDescription", categorySeq);
    }

    @SuppressWarnings("unchecked")
    public List<SchedulerAssetPlanVO> readSchedulerAssetPlanList(int assetSeq, String firstDay, String lastDay) {
        Map paramMap = new HashMap();
        paramMap.put("assetSeq", assetSeq);
        paramMap.put("firstDay", firstDay);
        paramMap.put("lastDay", lastDay);
        return getSqlMapClientTemplate().queryForList("Scheduler.readSchedulerAssetPlanList", paramMap);
    }

    public void saveAssetSchedule(SchedulerAssetPlanVO schedulerAssetPlanVo) {
        getSqlMapClientTemplate().insert("Scheduler.saveAssetSchedule", schedulerAssetPlanVo);
    }

    public void modifyAssetSchedule(SchedulerAssetPlanVO schedulerAssetPlanVo) {
        getSqlMapClientTemplate().update("Scheduler.modifyAssetSchedule", schedulerAssetPlanVo);
    }

    public SchedulerAssetPlanVO readAssetSchedule(int planSeq) {
        return (SchedulerAssetPlanVO) getSqlMapClientTemplate().queryForObject("Scheduler.readAssetSchedule", planSeq);
    }

    @SuppressWarnings("unchecked")
    public List<SchedulerAssetPlanVO> readAssetPlanInfoBySchedulerId(int schedulerId) {
        return getSqlMapClientTemplate().queryForList("Scheduler.readAssetPlanInfoBySchedulerId", schedulerId);
    }

    public void deleteAssetSchedule(int planSeq) {
        getSqlMapClientTemplate().delete("Scheduler.deleteAssetSchedule", planSeq);
    }

    public void deleteAssetScheduleBySchedulerId(int schedulerId) {
        getSqlMapClientTemplate().delete("Scheduler.deleteAssetScheduleBySchedulerId", schedulerId);
    }

    @SuppressWarnings("unchecked")
    public int checkAssetScheduleDuplicateCount(int assetSeq, int planSeq, String startDate, String endDate) {
        Map paramMap = new HashMap();
        paramMap.put("assetSeq", assetSeq);
        paramMap.put("planSeq", planSeq);
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);

        return (Integer) getSqlMapClientTemplate().queryForObject("Scheduler.checkAssetScheduleDuplicateCount",
                paramMap);
    }

    public int checkAssetSchedulerCount(int schedulerId) {
        return (Integer) getSqlMapClientTemplate().queryForObject("Scheduler.checkAssetSchedulerCount", schedulerId);
    }

    public SchedulerAssetPlanVO checkMyScheduleOrShareSchedule(int planSeq, int mailUserSeq, int[] schedulerIds) {
        Map paramMap = new HashMap();
        paramMap.put("planSeq", planSeq);
        paramMap.put("mailUserSeq", mailUserSeq);
        paramMap.put("schedulerIds", schedulerIds);

        return (SchedulerAssetPlanVO) getSqlMapClientTemplate().queryForObject(
                "Scheduler.checkMyScheduleOrShareSchedule", paramMap);
    }

    public List<SchedulerDataVO> readScheduleListAfterSyncTime(int mailUserSeq, String syncTime, int count) {
        Map paramMap = new HashMap();
        paramMap.put("mailUserSeq", mailUserSeq);
        paramMap.put("syncTime", syncTime);
        paramMap.put("skipResult", 0);
        paramMap.put("maxResult", count);

        return getSqlMapClientTemplate().queryForList("Scheduler.readScheduleListAfterSyncTime", paramMap);
    }

    public List<SchedulerDataVO> readDeletedScheduleListAfterSyncTime(int mailUserSeq, String syncTime, int count) {
        Map paramMap = new HashMap();
        paramMap.put("mailUserSeq", mailUserSeq);
        paramMap.put("syncTime", syncTime);
        paramMap.put("skipResult", 0);
        paramMap.put("maxResult", count);

        return getSqlMapClientTemplate().queryForList("Scheduler.readDeletedScheduleListAfterSyncTime", paramMap);
    }

    public List<SchedulerDataVO> readModifiedScheduleListAfterSyncTime(int mailUserSeq, String syncTime, int count) {
        Map paramMap = new HashMap();
        paramMap.put("mailUserSeq", mailUserSeq);
        paramMap.put("syncTime", syncTime);
        paramMap.put("skipResult", 0);
        paramMap.put("maxResult", count);

        return getSqlMapClientTemplate().queryForList("Scheduler.readModifiedScheduleListAfterSyncTime", paramMap);
    }

    public int readUnsyncedScheduleCount(int mailUserSeq, String syncTime) {
        Map paramMap = new HashMap();
        paramMap.put("mailUserSeq", mailUserSeq);
        paramMap.put("syncTime", syncTime);

        return (Integer) getSqlMapClientTemplate().queryForObject("Scheduler.readUnsyncedScheduleCount", paramMap);
    }

    public void saveSchedulerShareExt(int schedulerId, int mailUserSeq, String email) {
        Map paramMap = new HashMap();
        paramMap.put("schedulerId", schedulerId);
        paramMap.put("mailUserSeq", mailUserSeq);
        paramMap.put("email", email);

        getSqlMapClientTemplate().insert("Scheduler.saveSchedulerShareExt", paramMap);
    }

    public void deleteSchedulerShareExt(int schedulerId) {
        getSqlMapClientTemplate().delete("Scheduler.deleteSchedulerShareExt", schedulerId);
    }

    public List<SchedulerShareExtVO> readSchedulerShareExtList(int schedulerId) {
        return getSqlMapClientTemplate().queryForList("Scheduler.readSchedulerShareExtList", schedulerId);
    }

    public List<SchedulerAssetPlanVO> readAssetUseList(int mailDomainSeq, int schedulerId, int[] assetSeqs,
            String startDate, String endDate) {
        Map paramMap = new HashMap();
        paramMap.put("mailDomainSeq", mailDomainSeq);
        paramMap.put("schedulerId", schedulerId);
        paramMap.put("assetSeqs", assetSeqs);
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        return getSqlMapClientTemplate().queryForList("Scheduler.readAssetUseList", paramMap);
    }

}
