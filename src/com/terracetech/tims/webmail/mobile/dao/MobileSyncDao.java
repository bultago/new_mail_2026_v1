package com.terracetech.tims.webmail.mobile.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.mobile.vo.MobileSyncLogVO;
import com.terracetech.tims.webmail.mobile.vo.MobileSyncVO;

/**
 * MobileSyncDao MyBatis Mapper Interface
 * 원본: MobileSyncDao extends SqlSessionDaoSupport
 * 총 메서드 수: 19개
 */
@Mapper
public interface MobileSyncDao {
    /** 원본: public List<MobileSyncVO> selectMobileSyncList(int mailUserSeq) */
    List<MobileSyncVO> selectMobileSyncList(@Param("mailUserSeq") int mailUserSeq);

    /** 원본: public List<MobileSyncVO> selectMobileSync() */
    List<MobileSyncVO> selectMobileSync();
    
    /** 원본: public MobileSyncVO selectMobileSync(int mailUserSeq, String syncKey) */
    MobileSyncVO selectMobileSync(@Param("mailUserSeq") int mailUserSeq, @Param("syncKey") String syncKey);
    
    /** 원본: public int countMobileSync(int mailUserSeq, String deviceId) */
    int countMobileSync(@Param("mailUserSeq") int mailUserSeq, @Param("deviceId") String deviceId);
    
    /** 원본: public int countMobileSync(int mailUserSeq) */
    int countMobileSync(@Param("mailUserSeq") int mailUserSeq);
    
    /** 원본: public void deleteMobileSync(int mailUserSeq) */
    void deleteMobileSync(@Param("mailUserSeq") int mailUserSeq);
    
    /** 원본: public void deleteMobileSyncByDeviceId(int mailUserSeq, String deviceId) */
    void deleteMobileSyncByDeviceId(@Param("mailUserSeq") int mailUserSeq, @Param("deviceId") String deviceId);
    
    /** 원본: public void insertMobileSync(MobileSyncVO vo) */
    void insertMobileSync(MobileSyncVO vo);
    
    /** 원본: public void updateMobileSync(MobileSyncVO vo) */
    void updateMobileSync(MobileSyncVO vo);
    
    /** 원본: public void insertMobileSyncLog(MobileSyncLogVO vo) */
    void insertMobileSyncLog(MobileSyncLogVO vo);
    
    /** 원본: public void insertContactsMobileSyncLog(int mailUserSeq, int memberSeq, String eventType) */
    void insertContactsMobileSyncLog(@Param("mailUserSeq") int mailUserSeq, @Param("memberSeq") int memberSeq, 
                                    @Param("eventType") String eventType);
    
    void insertCalendarMobileSyncLog(@Param("mailUserSeq") int mailUserSeq, @Param("schedulerId") int schedulerId, 
                                    @Param("eventType") String eventType);
    
    void insertMailMobileSyncLog(@Param("mailUserSeq") int mailUserSeq, @Param("messageId") String messageId, 
                                @Param("eventType") String eventType);
    
    List<MobileSyncLogVO> selectContactsMobileSyncLog(@Param("mailUserSeq") int mailUserSeq, @Param("syncTime") String syncTime);
    List<MobileSyncLogVO> selectCalendarMobileSyncLog(@Param("mailUserSeq") int mailUserSeq, @Param("syncTime") String syncTime);
    List<MobileSyncLogVO> selectMailMobileSyncLog(@Param("mailUserSeq") int mailUserSeq, @Param("syncTime") String syncTime);
    
    void deleteMobileSyncLog(@Param("mailUserSeq") int mailUserSeq);
    void deleteMobileSyncLogByType(@Param("mailUserSeq") int mailUserSeq, @Param("syncType") String syncType);
    void deleteMobileSyncLogOlderThan(@Param("mailUserSeq") int mailUserSeq, @Param("syncTime") String syncTime);
    int countMobileSyncLog(@Param("mailUserSeq") int mailUserSeq);
    
    // 추가 메서드 (2025-10-23)
    MobileSyncVO selectMobileSyncBySyncKey(@Param("mailUserSeq") int mailUserSeq, @Param("syncKey") String syncKey);
    MobileSyncVO selectMobileSyncByDeviceId(@Param("mailUserSeq") int mailUserSeq, @Param("deviceId") String deviceId);
    List<MobileSyncLogVO> selectMobileSyncLog(@Param("mailUserSeq") int mailUserSeq, @Param("target") String target, @Param("fromDate") String fromDate);
    List<MobileSyncLogVO> selectMobileSyncLogByDate(@Param("mailUserSeq") int mailUserSeq, @Param("fromDate") String fromDate);
    int countCaledarEvent(@Param("mailUserSeq") int mailUserSeq, MobileSyncVO syncVo);
    int countContactsEvent(@Param("mailUserSeq") int mailUserSeq, MobileSyncVO syncVo);
}