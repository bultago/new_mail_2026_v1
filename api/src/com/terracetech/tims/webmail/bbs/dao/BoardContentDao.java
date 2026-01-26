package com.terracetech.tims.webmail.bbs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.bbs.vo.AttachFileVO;
import com.terracetech.tims.webmail.bbs.vo.BoardContentVO;
import com.terracetech.tims.webmail.bbs.vo.BoardContentReplyVO;

/**
 * BoardContentDao MyBatis Mapper Interface
 * 
 * 원본 클래스: BoardContentDao extends SqlSessionDaoSupport
 * 변환 내용: iBATIS → MyBatis Mapper 인터페이스
 * 변환일: 2025-10-20
 * 총 메서드 수: 31개 (원본 기준)
 */
@Mapper
public interface BoardContentDao {

    /** 원본: public void saveBoardContent(BoardContentVO boardContentVO) */
    void saveBoardContent(BoardContentVO boardContentVO);

    /** 원본: public int readBoardContentCount(int bbsId, int mailDomainSeq) */
    int readBoardContentCount(@Param("bbsId") int bbsId, @Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public BoardContentVO readBoardContent(int bbsId, int contentId, int mailDomainSeq) */
    BoardContentVO readBoardContent(@Param("bbsId") int bbsId, @Param("contentId") int contentId, 
                                   @Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public List<AttachFileVO> readBoardContentAttachFileList(int bbsId, int contentId, int mailDomainSeq) */
    List<AttachFileVO> readBoardContentAttachFileList(@Param("bbsId") int bbsId, @Param("contentId") int contentId, 
                                                      @Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public void modifyBoardContent(BoardContentVO boardContentVO) */
    void modifyBoardContent(BoardContentVO boardContentVO);

    /** 원본: public void deleteBoardContent(int bbsId, int[] contentIds, int mailDomainSeq) */
    void deleteBoardContent(@Param("bbsId") int bbsId, @Param("contentIds") int[] contentIds, 
                           @Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public int deleteBoardContentAttachFile(int bbsId, int[] contentIds, int mailDomainSeq) */
    int deleteBoardContentAttachFile(@Param("bbsId") int bbsId, @Param("contentIds") int[] contentIds, 
                                     @Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public void modifyHitCount(int bbsId, int contentId, int mailDomainSeq) */
    void modifyHitCount(@Param("bbsId") int bbsId, @Param("contentId") int contentId, 
                       @Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public List<BoardContentVO> readBoardContentNoticeList(int bbsId, int mailDomainSeq, int pageBase) */
    List<BoardContentVO> readBoardContentNoticeList(@Param("bbsId") int bbsId, @Param("mailDomainSeq") int mailDomainSeq, 
                                                    @Param("pageBase") int pageBase);

    /** 원본: public int readBoardContentNoticeListCount(int bbsId, int mailDomainSeq) */
    int readBoardContentNoticeListCount(@Param("bbsId") int bbsId, @Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public List<BoardContentVO> readBoardContentList(int bbsId, int mailDomainSeq, int creatorSeq, String searchType, String keyWord, int skipResult, int maxResult) */
    List<BoardContentVO> readBoardContentList(@Param("bbsId") int bbsId, @Param("mailDomainSeq") int mailDomainSeq, 
                                              @Param("creatorSeq") int creatorSeq, @Param("searchType") String searchType, 
                                              @Param("keyWord") String keyWord, @Param("skipResult") int skipResult, 
                                              @Param("maxResult") int maxResult);

    /** 원본: public List<BoardContentVO> readBoardContentListByMessageIds(int bbsId, int mailDomainSeq, String[] messageIds) */
    List<BoardContentVO> readBoardContentListByMessageIds(@Param("bbsId") int bbsId, @Param("mailDomainSeq") int mailDomainSeq, 
                                                          @Param("messageIds") String[] messageIds);

    /** 원본: public int readBoardContentListCount(int bbsId, int mailDomainSeq, int creatorSeq, String searchType, String keyWord) */
    int readBoardContentListCount(@Param("bbsId") int bbsId, @Param("mailDomainSeq") int mailDomainSeq, 
                                   @Param("creatorSeq") int creatorSeq, @Param("searchType") String searchType, 
                                   @Param("keyWord") String keyWord);

    /** 원본: public int readBoardContentMaxOrderNo(int bbsId, int parentId, int mailDomainSeq) */
    int readBoardContentMaxOrderNo(@Param("bbsId") int bbsId, @Param("parentId") int parentId, 
                                   @Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public void modifyOrderNo(int bbsId, int parentId, int orderNo, int mailDomainSeq) */
    void modifyOrderNo(@Param("bbsId") int bbsId, @Param("parentId") int parentId, @Param("orderNo") int orderNo, 
                      @Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public List<BoardContentVO> readPrevBoardContent(int bbsId, int contentId, int parentId, int orderNo, int mailDomainSeq, int creatorSeq, String searchType, String keyWord) */
    List<BoardContentVO> readPrevBoardContent(@Param("bbsId") int bbsId, @Param("contentId") int contentId, 
                                              @Param("parentId") int parentId, @Param("orderNo") int orderNo, 
                                              @Param("mailDomainSeq") int mailDomainSeq, @Param("creatorSeq") int creatorSeq, 
                                              @Param("searchType") String searchType, @Param("keyWord") String keyWord);

    /** 원본: public List<BoardContentVO> readPrevBoardContentByMessageId(int bbsId, int contentId, int parentId, int orderNo, int mailDomainSeq, int creatorSeq, String[] messageIds) */
    List<BoardContentVO> readPrevBoardContentByMessageId(@Param("bbsId") int bbsId, @Param("contentId") int contentId, 
                                                         @Param("parentId") int parentId, @Param("orderNo") int orderNo, 
                                                         @Param("mailDomainSeq") int mailDomainSeq, @Param("creatorSeq") int creatorSeq, 
                                                         @Param("messageIds") String[] messageIds);

    /** 원본: public List<BoardContentVO> readPrevIfNullBoardContentByMessageId(int bbsId, int contentId, int parentId, int orderNo, int mailDomainSeq, int creatorSeq, String[] messageIds) */
    List<BoardContentVO> readPrevIfNullBoardContentByMessageId(@Param("bbsId") int bbsId, @Param("contentId") int contentId, 
                                                               @Param("parentId") int parentId, @Param("orderNo") int orderNo, 
                                                               @Param("mailDomainSeq") int mailDomainSeq, @Param("creatorSeq") int creatorSeq, 
                                                               @Param("messageIds") String[] messageIds);

    /** 원본: public List<BoardContentVO> readNextBoardContent(int bbsId, int contentId, int parentId, int orderNo, int mailDomainSeq, int creatorSeq, String searchType, String keyWord) */
    List<BoardContentVO> readNextBoardContent(@Param("bbsId") int bbsId, @Param("contentId") int contentId, 
                                              @Param("parentId") int parentId, @Param("orderNo") int orderNo, 
                                              @Param("mailDomainSeq") int mailDomainSeq, @Param("creatorSeq") int creatorSeq, 
                                              @Param("searchType") String searchType, @Param("keyWord") String keyWord);

    /** 원본: public List<BoardContentVO> readNextBoardContentByMessageId(int bbsId, int contentId, int parentId, int orderNo, int mailDomainSeq, int creatorSeq, String[] messageIds) */
    List<BoardContentVO> readNextBoardContentByMessageId(@Param("bbsId") int bbsId, @Param("contentId") int contentId, 
                                                         @Param("parentId") int parentId, @Param("orderNo") int orderNo, 
                                                         @Param("mailDomainSeq") int mailDomainSeq, @Param("creatorSeq") int creatorSeq, 
                                                         @Param("messageIds") String[] messageIds);

    /** 원본: public List<BoardContentVO> readNextIfNullBoardContentByMessageId(int bbsId, int contentId, int parentId, int orderNo, int mailDomainSeq, int creatorSeq, String[] messageIds) */
    List<BoardContentVO> readNextIfNullBoardContentByMessageId(@Param("bbsId") int bbsId, @Param("contentId") int contentId, 
                                                               @Param("parentId") int parentId, @Param("orderNo") int orderNo, 
                                                               @Param("mailDomainSeq") int mailDomainSeq, @Param("creatorSeq") int creatorSeq, 
                                                               @Param("messageIds") String[] messageIds);

    /** 원본: public List<BoardContentVO> readPrevIfNullBoardContent(int bbsId, int contentId, int parentId, int orderNo, int mailDomainSeq, int creatorSeq, String searchType, String keyWord) */
    List<BoardContentVO> readPrevIfNullBoardContent(@Param("bbsId") int bbsId, @Param("contentId") int contentId, 
                                                    @Param("parentId") int parentId, @Param("orderNo") int orderNo, 
                                                    @Param("mailDomainSeq") int mailDomainSeq, @Param("creatorSeq") int creatorSeq, 
                                                    @Param("searchType") String searchType, @Param("keyWord") String keyWord);

    /** 원본: public List<BoardContentVO> readNextIfNullBoardContent(int bbsId, int contentId, int parentId, int orderNo, int mailDomainSeq, int creatorSeq, String searchType, String keyWord) */
    List<BoardContentVO> readNextIfNullBoardContent(@Param("bbsId") int bbsId, @Param("contentId") int contentId, 
                                                    @Param("parentId") int parentId, @Param("orderNo") int orderNo, 
                                                    @Param("mailDomainSeq") int mailDomainSeq, @Param("creatorSeq") int creatorSeq, 
                                                    @Param("searchType") String searchType, @Param("keyWord") String keyWord);

    /** 원본: public void saveBoardContentAttachFile(List<AttachFileVO> attList) */
    void saveBoardContentAttachFile(List<AttachFileVO> attList);

    /** 원본: public List<String> readBoardContentMsgIds(int bbsId, int[] contentIds, int mailDomainSeq) */
    List<String> readBoardContentMsgIds(@Param("bbsId") int bbsId, @Param("contentIds") int[] contentIds, 
                                       @Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public void modifyBoardContentAttCnt(int bbsId, int contentId, int mailDomainSeq, int attCnt) */
    void modifyBoardContentAttCnt(@Param("bbsId") int bbsId, @Param("contentId") int contentId, 
                                  @Param("mailDomainSeq") int mailDomainSeq, @Param("attCnt") int attCnt);

    /** 원본: public List<BoardContentReplyVO> readContentReplyList(int bbsId, int contentId, int isNotice, int mailDomainSeq, int skipResult, int pbase) */
    List<BoardContentReplyVO> readContentReplyList(@Param("bbsId") int bbsId, @Param("contentId") int contentId, 
                                                    @Param("isNotice") int isNotice, @Param("mailDomainSeq") int mailDomainSeq, 
                                                    @Param("skipResult") int skipResult, @Param("pbase") int pbase);

    /** 원본: public int readContentReplyListCount(int bbsId, int contentId, int isNotice, int mailDomainSeq) */
    int readContentReplyListCount(@Param("bbsId") int bbsId, @Param("contentId") int contentId, 
                                  @Param("isNotice") int isNotice, @Param("mailDomainSeq") int mailDomainSeq);

    /** 원본: public void saveContentReply(BoardContentReplyVO boardContentReplyVo) */
    void saveContentReply(BoardContentReplyVO boardContentReplyVo);

    /** 원본: public void deleteContentReply(BoardContentReplyVO boardContentReplyVo) */
    void deleteContentReply(BoardContentReplyVO boardContentReplyVo);

    /** 원본: public int existNewContent(int bbsId, int doaminSeq, String today) */
    int existNewContent(@Param("bbsId") int bbsId, @Param("doaminSeq") int doaminSeq, @Param("today") String today);
}