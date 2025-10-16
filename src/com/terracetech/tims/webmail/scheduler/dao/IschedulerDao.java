package com.terracetech.tims.webmail.scheduler.dao;

import java.util.List;

import com.terracetech.tims.webmail.scheduler.vo.SchedulerAssetCategoryVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerAssetPlanVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerDataVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerHolidayVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerShareExtVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerShareTargetVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerShareVO;

public interface IschedulerDao {

    public void saveSchedule(SchedulerDataVO schedulerDataVo);

    public void modifySchedule(SchedulerDataVO schedulerDataVo);

    public void deleteSchedule(int scheduleId);

    public List<SchedulerDataVO> readScheduleList(int mailUserSeq, int[] schedulerIds, String firstDay, String lastDay);

    public SchedulerDataVO readSchedule(int scheduleId);

    public List<SchedulerHolidayVO> readScheduleHoliday(int mailDomainSeq);

    public List<SchedulerDataVO> searchScheduleList(int mailUserSeq, String searchType, String keyWord, int skipResult,
            int maxResult);

    // schedulerShare
    public void saveShareGroup(SchedulerShareVO schedulerShareVo);

    public void modifyShareGroup(SchedulerShareVO schedulerShareVo);

    public List<SchedulerShareVO> readShareGroupList(int mailUserSeq);

    public SchedulerShareVO readShareGroup(int shareSeq);

    public List<SchedulerShareTargetVO> readShareTarget(int shareSeq);

    public void deleteShareGroup(int shareSeq);

    public void deleteShareGroupTarget(int shareSeq);

    public void saveShareSchedule(int schedulerId, int shareSeq);

    public void modifyShareSchedule(int schedulerId, int shareSeq);

    public void deleteShareSchedule(int schedulerId);

    public SchedulerShareVO readShareSchedule(int schedulerId);

    public List<SchedulerDataVO> readSchedulerShareInfo(int mailDomainSeq, int mailUserSeq, String email);

    public List<SchedulerDataVO> searchshareScheduleList(int mailUserSeq, int[] schedulerIds, String searchType,
            String keyWord);

    public void schedulerOutlookMark(int[] schedulerIds, String modifyTime, String mark);

    public List<Integer> getSchedulerIdOutlookSyncStatus(int mailUserSeq, String syncStatus, String firstDay);

    public List<SchedulerDataVO> getSchedulerShareModifyTime(int[] schedulerIds, String firstDay);

    public List<SchedulerDataVO> getSchedulerListOutlookSyncStatus(int mailUserSeq, String syncStatus, String firstDay);

    public List<SchedulerDataVO> getSchedulerShareListOutlookSyncStatus(int mailUserSeq, String syncStatus,
            String firstDay, int[] schedulerIds);

    public List<SchedulerDataVO> getSchedulebyIds(int[] schedulerIds);

    public void changeSyncFlag(int schedulerId, String syncStatus);

    public void saveRepeatIgnore(int schedulerId, String repeatStartDate);

    public List<SchedulerDataVO> readRepeatIgnoreList(int schedulerId);

    public void deleteRepeatIgnore(int schedulerId);

    public List<SchedulerAssetCategoryVO> readSchedulerAssetCategoryList(int mailDomainSeq);

    public List<SchedulerAssetCategoryVO> readAssetNotDuplicateList(int mailDomainSeq, int mailUserSeq,
            String startDate, String endDate);

    public SchedulerAssetCategoryVO readSchedulerAssetCategoryDescription(int categorySeq);

    public List<SchedulerAssetPlanVO> readSchedulerAssetPlanList(int assetSeq, String firstDay, String lastDay);

    public void saveAssetSchedule(SchedulerAssetPlanVO schedulerAssetPlanVo);

    public void modifyAssetSchedule(SchedulerAssetPlanVO schedulerAssetPlanVo);

    public SchedulerAssetPlanVO readAssetSchedule(int planSeq);

    public List<SchedulerAssetPlanVO> readAssetPlanInfoBySchedulerId(int schedulerId);

    public void deleteAssetSchedule(int planSeq);

    public void deleteAssetScheduleBySchedulerId(int schedulerId);

    public int checkAssetScheduleDuplicateCount(int assetSeq, int planSeq, String startDate, String endDate);

    public int checkAssetSchedulerCount(int schedulerId);

    public SchedulerAssetPlanVO checkMyScheduleOrShareSchedule(int planSeq, int mailUserSeq, int[] schedulerIds);

    public List<SchedulerDataVO> readScheduleListAfterSyncTime(int mailUserSeq, String syncTime, int count);

    public List<SchedulerDataVO> readDeletedScheduleListAfterSyncTime(int mailUserSeq, String syncTime, int count);

    public List<SchedulerDataVO> readModifiedScheduleListAfterSyncTime(int mailUserSeq, String syncTime, int count);

    public int readUnsyncedScheduleCount(int mailUserSeq, String syncTime);

    public void saveSchedulerShareExt(int schedulerId, int mailUserSeq, String email);

    public void deleteSchedulerShareExt(int schedulerId);

    public List<SchedulerShareExtVO> readSchedulerShareExtList(int scheudlerId);

    public List<SchedulerAssetPlanVO> readAssetUseList(int mailDomainSeq, int schedulerId, int[] assetSeqs,
            String startDate, String endDate);
}
