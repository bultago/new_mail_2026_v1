package com.terracetech.tims.webmail.scheduler.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerVO;

/**
 * Scheduler Mapper Interface
 * iBATIS → MyBatis 변환
 */
public interface SchedulerMapper {
    
    /**
     * 스케줄 저장
     * @param scheduler 스케줄 정보
     * @return 영향받은 행 수
     */
    int saveSchedule(SchedulerVO scheduler);
    
    /**
     * 스케줄 목록 조회
     * @param params 검색 조건
     * @return 스케줄 목록
     */
    List<SchedulerVO> selectScheduleList(Map<String, Object> params);
    
    /**
     * 스케줄 상세 조회
     * @param schedulerId 스케줄 ID
     * @return 스케줄 정보
     */
    SchedulerVO selectSchedule(@Param("schedulerId") int schedulerId);
    
    /**
     * 스케줄 수정
     * @param scheduler 스케줄 정보
     * @return 영향받은 행 수
     */
    int updateSchedule(SchedulerVO scheduler);
    
    /**
     * 스케줄 삭제
     * @param schedulerId 스케줄 ID
     * @return 영향받은 행 수
     */
    int deleteSchedule(@Param("schedulerId") int schedulerId);
    
    /**
     * 사용자별 스케줄 조회
     * @param mailUserSeq 메일 사용자 시퀀스
     * @return 스케줄 목록
     */
    List<SchedulerVO> selectScheduleByUser(@Param("mailUserSeq") int mailUserSeq);
    
    /**
     * 날짜 범위별 스케줄 조회
     * @param params 시작일, 종료일
     * @return 스케줄 목록
     */
    List<SchedulerVO> selectScheduleByDateRange(Map<String, Object> params);
}
