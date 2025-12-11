package com.terracetech.tims.webmail.scheduler.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.scheduler.vo.SchedulerAssetCategoryVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerAssetPlanVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerDataVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerHolidayVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerShareExtVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerShareTargetVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerShareVO;

/**
 * SchedulerDao MyBatis Mapper Interface
 * 
 * 원본 클래스: SchedulerDao extends SqlSessionDaoSupport implements IschedulerDao
 * 변환 내용: iBATIS → MyBatis Mapper 인터페이스
 * 변환일: 2025-10-20
 * 총 메서드 수: 53개 (원본 기준)
 */
@Mapper
public interface SchedulerDao {

    /** 원본: public void saveSchedule(SchedulerDataVO schedulerDataVo) */
    void saveSchedule(SchedulerDataVO schedulerDataVo);

    /** 원본: public void modifySchedule(SchedulerDataVO schedulerDataVo) */
    void modifySchedule(SchedulerDataVO schedulerDataVo);

    /** 원본: public void deleteSchedule(int scheduleId) */
    void deleteSchedule(@Param("scheduleId") int scheduleId);

    /** 원본: public List<SchedulerDataVO> readScheduleList(int mailUserSeq, int[] schedulerIds, String firstDay, String lastDay) */
    List<SchedulerDataVO> readScheduleList(@Param("mailUserSeq") int mailUserSeq, @Param("schedulerIds") int[] schedulerIds, 
                                          @Param("firstDay") String firstDay, @Param("lastDay") String lastDay);

    /** 원본: public SchedulerDataVO readSchedule(int schedulerId) */
    SchedulerDataVO readSchedule(@Param("schedulerId") int schedulerId);

    /** 원본: public List<SchedulerDataVO> searchScheduleList(int mailUserSeq, String searchType, String keyWord, int skipResult, int maxResult) */
    List<SchedulerDataVO> searchScheduleList(@Param("mailUserSeq") int mailUserSeq, @Param("searchType") String searchType, 
                                            @Param("keyWord") String keyWord, @Param("skipResult") int skipResult, 
                                            @Param("maxResult") int maxResult);

    /** 원본: public int searchScheduleListCount(int mailUserSeq, String searchType, String keyWord) */
    int searchScheduleListCount(@Param("mailUserSeq") int mailUserSeq, @Param("searchType") String searchType, 
                               @Param("keyWord") String keyWord);

    /** 원본: public List<SchedulerHolidayVO> readScheduleHoliday(int mailDomainSeq) */
    List<SchedulerHolidayVO> readScheduleHoliday(@Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public void saveShareGroup(SchedulerShareVO schedulerShareVo) */
    void saveShareGroup(SchedulerShareVO schedulerShareVo);

    /** 원본: public void saveShareGroupTarget(SchedulerShareTargetVO schedulerShareTargetVo) */
    void saveShareGroupTarget(SchedulerShareTargetVO schedulerShareTargetVo);

    /** 원본: public void modifyShareGroup(SchedulerShareVO schedulerShareVo) */
    void modifyShareGroup(SchedulerShareVO schedulerShareVo);

    /** 원본: public List<SchedulerShareVO> readShareGroupList(int mailUserSeq) */
    List<SchedulerShareVO> readShareGroupList(@Param("mailUserSeq") int mailUserSeq);

    /** 원본: public SchedulerShareVO readShareGroup(int shareSeq) */
    SchedulerShareVO readShareGroup(@Param("shareSeq") int shareSeq);

    /** 원본: public List<SchedulerShareTargetVO> readShareTarget(int shareSeq) */
    List<SchedulerShareTargetVO> readShareTarget(@Param("shareSeq") int shareSeq);

    /** 원본: public void deleteShareGroup(int shareSeq) */
    void deleteShareGroup(@Param("shareSeq") int shareSeq);

    /** 원본: public void deleteShareGroupTarget(int shareSeq) */
    void deleteShareGroupTarget(@Param("shareSeq") int shareSeq);

    /** 원본: public void saveShareSchedule(int schedulerId, int shareSeq) */
    void saveShareSchedule(@Param("schedulerId") int schedulerId, @Param("shareSeq") int shareSeq);

    /** 원본: public void modifyShareSchedule(int schedulerId, int shareSeq) */
    void modifyShareSchedule(@Param("schedulerId") int schedulerId, @Param("shareSeq") int shareSeq);

    /** 원본: public void deleteShareSchedule(int schedulerId) */
    void deleteShareSchedule(@Param("schedulerId") int schedulerId);

    /** 원본: public SchedulerShareVO readShareSchedule(int schedulerId) */
    SchedulerShareVO readShareSchedule(@Param("schedulerId") int schedulerId);

    /** 원본: public List<SchedulerDataVO> readSchedulerShareInfo(int mailDomainSeq, int mailUserSeq, String email) */
    List<SchedulerDataVO> readSchedulerShareInfo(@Param("mailDomainSeq") int mailDomainSeq, @Param("mailUserSeq") int mailUserSeq, 
                                                 @Param("email") String email);

    /** 원본: public List<SchedulerDataVO> searchshareScheduleList(int mailUserSeq, int[] schedulerIds, String searchType, String keyWord, int skipResult, int maxResult) */
    List<SchedulerDataVO> searchshareScheduleList(@Param("mailUserSeq") int mailUserSeq, @Param("schedulerIds") int[] schedulerIds, 
                                                  @Param("searchType") String searchType, @Param("keyWord") String keyWord, 
                                                  @Param("skipResult") int skipResult, @Param("maxResult") int maxResult);

    /** 원본: public void schedulerOutlookMark(int[] schedulerIds, String modifyTime, String mark) */
    void schedulerOutlookMark(@Param("schedulerIds") int[] schedulerIds, @Param("modifyTime") String modifyTime, 
                             @Param("mark") String mark);

    /** 원본: public List<Integer> getSchedulerIdOutlookSyncStatus(int mailUserSeq, String syncStatus, String firstDay) */
    List<Integer> getSchedulerIdOutlookSyncStatus(@Param("mailUserSeq") int mailUserSeq, @Param("syncStatus") String syncStatus, 
                                                  @Param("firstDay") String firstDay);

    /** 원본: public List<SchedulerDataVO> getSchedulerShareModifyTime(int[] schedulerIds, String firstDay) */
    List<SchedulerDataVO> getSchedulerShareModifyTime(@Param("schedulerIds") int[] schedulerIds, @Param("firstDay") String firstDay);

    /** 원본: public List<SchedulerDataVO> getSchedulerListOutlookSyncStatus(int mailUserSeq, String syncStatus, String firstDay) */
    List<SchedulerDataVO> getSchedulerListOutlookSyncStatus(@Param("mailUserSeq") int mailUserSeq, @Param("syncStatus") String syncStatus, 
                                                            @Param("firstDay") String firstDay);

    /** 원본: public List<SchedulerDataVO> getSchedulerShareListOutlookSyncStatus(int mailUserSeq, String syncStatus, String firstDay, int[] schedulerIds) */
    List<SchedulerDataVO> getSchedulerShareListOutlookSyncStatus(@Param("mailUserSeq") int mailUserSeq, @Param("syncStatus") String syncStatus, 
                                                                 @Param("firstDay") String firstDay, @Param("schedulerIds") int[] schedulerIds);

    /** 원본: public List<SchedulerDataVO> getSchedulebyIds(int[] schedulerIds) */
    List<SchedulerDataVO> getSchedulebyIds(@Param("schedulerIds") int[] schedulerIds);

    /** 원본: public void changeSyncFlag(int schedulerId, String syncStatus) */
    void changeSyncFlag(@Param("schedulerId") int schedulerId, @Param("syncStatus") String syncStatus);

    /** 원본: public void saveRepeatIgnore(int schedulerId, String repeatStartDate) */
    void saveRepeatIgnore(@Param("schedulerId") int schedulerId, @Param("repeatStartDate") String repeatStartDate);

    /** 원본: public List<SchedulerDataVO> readRepeatIgnoreList(int schedulerId) */
    List<SchedulerDataVO> readRepeatIgnoreList(@Param("schedulerId") int schedulerId);

    /** 원본: public void deleteRepeatIgnore(int schedulerId) */
    void deleteRepeatIgnore(@Param("schedulerId") int schedulerId);

    /** 원본: public List<SchedulerAssetCategoryVO> readSchedulerAssetCategoryList(int mailDomainSeq) */
    List<SchedulerAssetCategoryVO> readSchedulerAssetCategoryList(@Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public List<SchedulerAssetCategoryVO> readAssetNotDuplicateList(int mailDomainSeq, int mailUserSeq, String firstDay, String lastDay) */
    List<SchedulerAssetCategoryVO> readAssetNotDuplicateList(@Param("mailDomainSeq") int mailDomainSeq, @Param("mailUserSeq") int mailUserSeq, 
                                                             @Param("firstDay") String firstDay, @Param("lastDay") String lastDay);

    /** 원본: public SchedulerAssetCategoryVO readSchedulerAssetCategoryDescription(int categorySeq) */
    SchedulerAssetCategoryVO readSchedulerAssetCategoryDescription(@Param("categorySeq") int categorySeq);

    /** 원본: public List<SchedulerAssetPlanVO> readSchedulerAssetPlanList(int assetSeq, String firstDay, String lastDay) */
    List<SchedulerAssetPlanVO> readSchedulerAssetPlanList(@Param("assetSeq") int assetSeq, @Param("firstDay") String firstDay, 
                                                          @Param("lastDay") String lastDay);

    /** 원본: public void saveAssetSchedule(SchedulerAssetPlanVO schedulerAssetPlanVo) */
    void saveAssetSchedule(SchedulerAssetPlanVO schedulerAssetPlanVo);

    /** 원본: public void modifyAssetSchedule(SchedulerAssetPlanVO schedulerAssetPlanVo) */
    void modifyAssetSchedule(SchedulerAssetPlanVO schedulerAssetPlanVo);

    /** 원본: public SchedulerAssetPlanVO readAssetSchedule(int planSeq) */
    SchedulerAssetPlanVO readAssetSchedule(@Param("planSeq") int planSeq);

    /** 원본: public List<SchedulerAssetPlanVO> readAssetPlanInfoBySchedulerId(int schedulerId) */
    List<SchedulerAssetPlanVO> readAssetPlanInfoBySchedulerId(@Param("schedulerId") int schedulerId);

    /** 원본: public void deleteAssetSchedule(int planSeq) */
    void deleteAssetSchedule(@Param("planSeq") int planSeq);

    /** 원본: public void deleteAssetScheduleBySchedulerId(int schedulerId) */
    void deleteAssetScheduleBySchedulerId(@Param("schedulerId") int schedulerId);

    /** 원본: public int checkAssetScheduleDuplicateCount(int assetSeq, int planSeq, String startDate, String endDate) */
    int checkAssetScheduleDuplicateCount(@Param("assetSeq") int assetSeq, @Param("planSeq") int planSeq, 
                                        @Param("startDate") String startDate, @Param("endDate") String endDate);

    /** 원본: public int checkAssetSchedulerCount(int schedulerId) */
    int checkAssetSchedulerCount(@Param("schedulerId") int schedulerId);

    /** 원본: public SchedulerAssetPlanVO checkMyScheduleOrShareSchedule(int planSeq, int mailUserSeq, int[] schedulerIds) */
    SchedulerAssetPlanVO checkMyScheduleOrShareSchedule(@Param("planSeq") int planSeq, @Param("mailUserSeq") int mailUserSeq, 
                                                        @Param("schedulerIds") int[] schedulerIds);

    /** 원본: public List<SchedulerDataVO> readScheduleListAfterSyncTime(int mailUserSeq, String syncTime, int count) */
    List<SchedulerDataVO> readScheduleListAfterSyncTime(@Param("mailUserSeq") int mailUserSeq, @Param("syncTime") String syncTime, 
                                                        @Param("count") int count);

    /** 원본: public List<SchedulerDataVO> readDeletedScheduleListAfterSyncTime(int mailUserSeq, String syncTime, int count) */
    List<SchedulerDataVO> readDeletedScheduleListAfterSyncTime(@Param("mailUserSeq") int mailUserSeq, @Param("syncTime") String syncTime, 
                                                               @Param("count") int count);

    /** 원본: public List<SchedulerDataVO> readModifiedScheduleListAfterSyncTime(int mailUserSeq, String syncTime, int count) */
    List<SchedulerDataVO> readModifiedScheduleListAfterSyncTime(@Param("mailUserSeq") int mailUserSeq, @Param("syncTime") String syncTime, 
                                                                @Param("count") int count);

    /** 원본: public int readUnsyncedScheduleCount(int mailUserSeq, String syncTime) */
    int readUnsyncedScheduleCount(@Param("mailUserSeq") int mailUserSeq, @Param("syncTime") String syncTime);

    /** 원본: public void saveSchedulerShareExt(int schedulerId, int mailUserSeq, String email) */
    void saveSchedulerShareExt(@Param("schedulerId") int schedulerId, @Param("mailUserSeq") int mailUserSeq, 
                              @Param("email") String email);

    /** 원본: public void deleteSchedulerShareExt(int schedulerId) */
    void deleteSchedulerShareExt(@Param("schedulerId") int schedulerId);

    /** 원본: public List<SchedulerShareExtVO> readSchedulerShareExtList(int schedulerId) */
    List<SchedulerShareExtVO> readSchedulerShareExtList(@Param("schedulerId") int schedulerId);

    /** 원본: public List<SchedulerAssetPlanVO> readAssetUseList(int mailDomainSeq, int schedulerId, int[] assetSeqs, String firstDay, String lastDay) */
    List<SchedulerAssetPlanVO> readAssetUseList(@Param("mailDomainSeq") int mailDomainSeq, @Param("schedulerId") int schedulerId, 
                                               @Param("assetSeqs") int[] assetSeqs, @Param("firstDay") String firstDay, 
                                               @Param("lastDay") String lastDay);
}