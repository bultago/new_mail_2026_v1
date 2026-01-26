package com.terracetech.tims.hybrid.common.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * HybridMobileDao MyBatis Mapper Interface
 * 원본: HybridMobileDao extends SqlSessionDaoSupport
 * 총 메서드 수: 5개
 */
@Mapper
public interface HybridMobileDao {
    /** 원본: public void insertHybridAuth(String authKey, String authValue) */
    void insertHybridAuth(@Param("authKey") String authKey, @Param("authValue") String authValue);
    
    /** 원본: public void deleteHybridAuth(String authKey) */
    void deleteHybridAuth(@Param("authKey") String authKey);
    
    /** 원본: public String readHybridAuth(String authKey) */
    String readHybridAuth(@Param("authKey") String authKey);
    
    /** 원본: public String readMobileAccessConfig(int mailDomainSeq) */
    String readMobileAccessConfig(@Param("mailDomainSeq") int mailDomainSeq);
    
    /** 원본: public Map<String, String> readUserMobileAccessKey(int mailUserSeq) */
    Map<String, String> readUserMobileAccessKey(@Param("mailUserSeq") int mailUserSeq);
}