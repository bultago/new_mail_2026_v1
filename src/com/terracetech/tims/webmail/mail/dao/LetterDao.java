package com.terracetech.tims.webmail.mail.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.mail.vo.LetterVO;

/**
 * LetterDao MyBatis Mapper Interface
 * 
 * 원본 클래스: LetterDao extends SqlSessionDaoSupport
 * 변환 내용: iBATIS → MyBatis Mapper 인터페이스
 * 변환일: 2025-10-20
 * 총 메서드 수: 3개 (원본 기준)
 */
@Mapper
public interface LetterDao {

    /** 원본: public List<LetterVO> readLetterList(int mailDomainSeq, int skipResults, int maxResults) */
    List<LetterVO> readLetterList(@Param("mailDomainSeq") int mailDomainSeq, @Param("skipResults") int skipResults, 
                                  @Param("maxResults") int maxResults);

    /** 원본: public LetterVO readLetter(int letterSeq) */
    LetterVO readLetter(@Param("letterSeq") int letterSeq);

    /** 원본: public int readLetterTotal(int mailDomainSeq) */
    int readLetterTotal(@Param("mailDomainSeq") int mailDomainSeq);
}