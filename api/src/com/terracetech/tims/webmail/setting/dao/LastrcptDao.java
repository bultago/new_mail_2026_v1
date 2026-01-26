package com.terracetech.tims.webmail.setting.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.setting.vo.RcptVO;
import jakarta.mail.internet.InternetAddress;

/**
 * LastrcptDao MyBatis Mapper Interface
 * 
 * 원본 클래스: LastrcptDao extends SqlSessionDaoSupport
 * 변환 내용: iBATIS → MyBatis Mapper 인터페이스
 * 변환일: 2025-10-20
 * 총 메서드 수: 8개 (원본 기준)
 */
@Mapper
public interface LastrcptDao {

    /** 원본: public void saveLastRcpt(RcptVO[] vos) */
    void saveLastRcptArray(RcptVO[] vos);

    /** 원본: public void saveLastRcpt(RcptVO vo) */
    void saveLastRcpt(RcptVO vo);

    /** 원본: public boolean isExistLastRcpt(int userSeq, String email) */
    boolean isExistLastRcpt(@Param("userSeq") int userSeq, @Param("email") String email);

    /** 원본: public void deleteLastRcpt(int userSeq) */
    void deleteLastRcpt(@Param("userSeq") int userSeq);
    
    /** 원본: public void deleteLastRcpt(int userSeq, InternetAddress[] addressList) */
    void deleteLastRcpt(@Param("userSeq") int userSeq, @Param("addressList") InternetAddress[] addressList);
    
    /** 원본: public void deleteLastRcpt(int userSeq, String[] rcptSeqs) */
    void deleteLastRcpt(@Param("userSeq") int userSeq, @Param("rcptSeqs") String[] rcptSeqs);

    /** 원본: public List<RcptVO> readLastRcpt(int userSeq) */
    List<RcptVO> readLastRcpt(@Param("userSeq") int userSeq);

    /** 원본: public List<RcptVO> readLastRcptByMaxRcptCount(int mailUserSeq, int maxRcptCount) */
    List<RcptVO> readLastRcptByMaxRcptCount(@Param("mailUserSeq") int mailUserSeq, @Param("maxRcptCount") int maxRcptCount);
}