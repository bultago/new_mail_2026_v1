package com.terracetech.tims.webmail.setting.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.setting.vo.VCardVO;

/**
 * VCardDao MyBatis Mapper Interface
 * 
 * 원본 클래스: VCardDao extends SqlSessionDaoSupport
 * 변환 내용: iBATIS → MyBatis Mapper 인터페이스
 * 변환일: 2025-10-20
 * 총 메서드 수: 3개 (원본 기준)
 */
@Mapper
public interface VCardDao {

    /** 원본: public VCardVO readVcard(int userSeq) throws Exception */
    VCardVO readVcard(@Param("userSeq") int userSeq) throws Exception;

    /** 원본: public void saveVcard(VCardVO vcard) throws Exception */
    void saveVcard(VCardVO vcard) throws Exception;

    /** 원본: public void modifyVcard(VCardVO vcard) throws Exception */
    void modifyVcard(VCardVO vcard) throws Exception;
}