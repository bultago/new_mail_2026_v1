/**
 * BigAttachDao.java 2008. 12. 8.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.mail.vo.MailBigAttachVO;

/**
 * <p><strong>BigAttachDao.java</strong> MyBatis Mapper Interface</p>
 * 
 * 원본 클래스: BigAttachDao extends SqlSessionDaoSupport implements IBigAttachDao
 * 변환 내용: iBATIS → MyBatis Mapper 인터페이스
 * 변환일: 2025-10-20
 * 총 메서드 수: 5개 (원본 기준)
 * 
 * @author jpjung
 * @since Tims7
 * @version 7.0
 */
@Mapper
public interface BigAttachDao {

    /** 원본: public List<MailBigAttachVO> getListMailBigAttach(int userSeq) throws SQLException */
    List<MailBigAttachVO> getListMailBigAttach(@Param("userSeq") int userSeq) throws SQLException;

    /** 원본: public MailBigAttachVO getMailBigAttach(int userSeq, String messageId) throws SQLException */
    MailBigAttachVO getMailBigAttach(@Param("userSeq") int userSeq, @Param("messageId") String messageId) throws SQLException;

    /** 원본: public void saveMailBigAttach(MailBigAttachVO vo) throws SQLException */
    void saveMailBigAttach(MailBigAttachVO vo) throws SQLException;

    /** 원본: public void updateMailBigAttach(MailBigAttachVO vo) throws SQLException */
    void updateMailBigAttach(MailBigAttachVO vo) throws SQLException;

    /** 원본: public void updateDeleteMailBigAttach(int userSeq, String messageId) throws SQLException */
    void updateDeleteMailBigAttach(@Param("userSeq") int userSeq, @Param("messageId") String messageId) throws SQLException;
}