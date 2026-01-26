package com.terracetech.tims.webmail.bbs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.bbs.vo.BoardVO;

/**
 * BoardDao MyBatis Mapper Interface
 * 
 * 원본 클래스: BoardDao extends SqlSessionDaoSupport
 * 변환 내용: iBATIS → MyBatis Mapper 인터페이스
 * 변환일: 2025-10-20
 * 총 메서드 수: 9개 (원본 기준)
 */
@Mapper
public interface BoardDao {

    /** 원본: public void saveDefaultBoard(List<BoardVO> defaultBoardList) */
    void saveDefaultBoard(List<BoardVO> defaultBoardList);

    /** 원본: public List<BoardVO> readBoardList(int domainSeq) */
    List<BoardVO> readBoardList(@Param("domainSeq") int domainSeq);

    /** 원본: public BoardVO readBoard(int bbsId, int domainSeq) */
    BoardVO readBoard(@Param("bbsId") int bbsId, @Param("domainSeq") int domainSeq);

    /** 원본: public BoardVO readNoticeBoard(int bbsId, int domainSeq) */
    BoardVO readNoticeBoard(@Param("bbsId") int bbsId, @Param("domainSeq") int domainSeq);

    /** 원본: public void saveBoard(BoardVO boardVo) */
    void saveBoard(BoardVO boardVo);

    /** 원본: public void modifyBoard(BoardVO boardVo) */
    void modifyBoard(BoardVO boardVo);

    /** 원본: public void deleteBoard(int bbsId, int domainSeq) */
    void deleteBoard(@Param("bbsId") int bbsId, @Param("domainSeq") int domainSeq);

    /** 원본: public List<Integer> readBoardAdiminList(int bbsId, int mailDomainSeq) */
    List<Integer> readBoardAdiminList(@Param("bbsId") int bbsId, @Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public List<BoardVO> readNoticeBbsList(int domainSeq) */
    List<BoardVO> readNoticeBbsList(@Param("domainSeq") int domainSeq);
}