package com.terracetech.tims.webmail.mail.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * FolderAgingDao MyBatis Mapper Interface
 * 
 * 원본 클래스: FolderAgingDao extends SqlSessionDaoSupport
 * 변환 내용: iBATIS → MyBatis Mapper 인터페이스
 * 변환일: 2025-10-20
 * 총 메서드 수: 5개 (원본 기준)
 */
@Mapper
public interface FolderAgingDao {

    /** 원본: public List getAgingInfo(int userSeq) */
    List getAgingInfo(@Param("userSeq") int userSeq);

    /** 원본: public void addAgingInfo(int userSeq, int agingDay, String folderName) */
    void addAgingInfo(@Param("userSeq") int userSeq, @Param("agingDay") int agingDay, @Param("folderName") String folderName);

    /** 원본: public void setAgingInfo(int userSeq, int agingDay, String folderName) */
    void setAgingInfo(@Param("userSeq") int userSeq, @Param("agingDay") int agingDay, @Param("folderName") String folderName);

    /** 원본: public void updateAgingInfo(int userSeq, String folderName, String changeFolderName) */
    void updateAgingInfo(@Param("userSeq") int userSeq, @Param("folderName") String folderName, 
                        @Param("changeFolderName") String changeFolderName);

    /** 원본: public void deleteAgingInfo(int userSeq, String folderName) */
    void deleteAgingInfo(@Param("userSeq") int userSeq, @Param("folderName") String folderName);
}