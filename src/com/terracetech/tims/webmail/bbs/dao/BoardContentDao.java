package com.terracetech.tims.webmail.bbs.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.bbs.vo.AttachFileVO;
import com.terracetech.tims.webmail.bbs.vo.BoardContentReplyVO;
import com.terracetech.tims.webmail.bbs.vo.BoardContentVO;

/**
 * <p><strong>BoardContentDao.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li>DB ���� �Խ����� �Խù��� ���� ������ �������� DAO ��ü.</li> 
 * </ul>
 * @author jhlee
 * @since Tims7
 * @version 7.0 
 */

public class BoardContentDao extends SqlMapClientDaoSupport {
	
	/**
	 * <p>�Խù� ����.</p>
	 *
	 * @param BoardContentVO
	 * @return void
	 */
	public void saveBoardContent(BoardContentVO boardContentVO) {
		getSqlMapClientTemplate().insert("Bbs.saveBoardContent", boardContentVO);
	}
	
	/**
	 * <p>�Խù��� ������ ������.</p>
	 *
	 * @param bbsId		�Խ����� ID
	 * @param mailDomainSeq		������ ������
	 * @return int 
	 */
	public int readBoardContentCount(int bbsId, int mailDomainSeq) {
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("bbsId", bbsId);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		return (Integer)getSqlMapClientTemplate().queryForObject("Bbs.readBoardContentCount", paramMap);
	}
	
	/**
	 * <p>�Խù� ������ ������.</p>
	 *
	 * @param bbsId		�Խ����� ID
	 * @param contentId		�Խù��� ID
	 * @param mailDomainSeq		������ ������
	 * @return BoardContentVO 
	 */
	public BoardContentVO readBoardContent(int bbsId, int contentId, int mailDomainSeq) {
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("bbsId", bbsId);
		paramMap.put("contentId", contentId);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		return (BoardContentVO)getSqlMapClientTemplate().queryForObject("Bbs.readBoardContent", paramMap);
	}
	
	/**
	 * <p>�Խù��� ÷������ ������ ������.</p>
	 *
	 * @param bbsId		�Խ����� ID
	 * @param contentId		�Խù��� ID
	 * @param mailDomainSeq		������ ������
	 * @return List 
	 */
	public List<AttachFileVO> readBoardContentAttachFileList(int bbsId, int contentId, int mailDomainSeq) {
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("bbsId", bbsId);
		paramMap.put("contentId", contentId);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		return getSqlMapClientTemplate().queryForList("Bbs.readBoardContentAttachFileList", paramMap);
	}
	
	/**
	 * <p>�Խù��� ����.</p>
	 *
	 * @param boardContentVO
	 * @return void 
	 */
	
	public void modifyBoardContent(BoardContentVO boardContentVO) {
		getSqlMapClientTemplate().update("Bbs.modifyBoardContent", boardContentVO);
	}
	
	/**
	 * <p>�Խù��� ����.</p>
	 *
	 * @param contentIds		�Խù��� ID (1,2,3,4)
	 * @param bbsId		�Խ����� ID
	 * @param mailDomainSeq		������ ������
	 * @return void 
	 */
	@SuppressWarnings("unchecked")
	public void deleteBoardContent (int bbsId, int[] contentIds, int mailDomainSeq) {
		Map paramMap = new HashMap();
		paramMap.put("bbsId", bbsId);
		paramMap.put("contentIds", contentIds);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		getSqlMapClientTemplate().delete("Bbs.deleteBoardContent", paramMap);
	}
	
	/**
	 * <p>�Խù��� ÷�������� ����.</p>
	 *
	 * @param bbsId		�Խ����� ID
	 * @param contentIds		�Խù��� ID
	 * @param mailDomainSeq		������ ������
	 * @return int 
	 */
	@SuppressWarnings("unchecked")
	public int deleteBoardContentAttachFile(int bbsId, int[] contentIds, int mailDomainSeq) {
		Map paramMap = new HashMap();
		paramMap.put("bbsId", bbsId);
		paramMap.put("contentIds", contentIds);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		return (Integer)getSqlMapClientTemplate().delete("Bbs.deleteBoardContentAttachFile", paramMap);
	}
	
	/**
	 * <p>�Խù��� ��ȸ�� ����.</p>
	 *
	 * @param bbsId		�Խ����� ID
	 * @param contentId		�Խù��� ID
	 * @param mailDomainSeq		������ ������
	 * @return void 
	 */
	public void modifyHitCount (int bbsId, int contentId, int mailDomainSeq) {
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("bbsId", bbsId);
		paramMap.put("contentId", contentId);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		getSqlMapClientTemplate().update("Bbs.modifyHitCount", paramMap);
	}
	
	/**
	 * <p>���� �Խù� ����Ʈ ��ȸ.</p>
	 *
	 * @param bbsId		�Խ����� ID
	 * @param mailDomainSeq		������ ������
	 * @return List
	 */
	public List<BoardContentVO> readBoardContentNoticeList (int bbsId, int mailDomainSeq, int pageBase) {
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("bbsId", bbsId);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		
		return getSqlMapClientTemplate().queryForList("Bbs.readBoardContentNoticeList", paramMap);
	}
	
	public int readBoardContentNoticeListCount (int bbsId, int mailDomainSeq) {
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("bbsId", bbsId);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		
		return (Integer)getSqlMapClientTemplate().queryForObject("Bbs.readBoardContentNoticeListCount", paramMap);
	}
	
	/**
	 * <p>�Խù� ����Ʈ ��ȸ.</p>
	 *
	 * @param bbsId		�Խ����� ID
	 * @param mailDomainSeq		������ ������
	 * @param searchType	�˻� ����: 1-����,2-�ۼ���,3-����,4-����+�ۼ���,5-����+����,6-��ΰ˻�
	 * @param keyWord	�˻���
	 * @param skipResult 	�ǳʶٱ�
	 * @param maxResult		����
	 * @return List 
	 */
	@SuppressWarnings("unchecked")
	public List<BoardContentVO> readBoardContentList(int bbsId, int mailDomainSeq, int creatorSeq, String searchType, String keyWord, int skipResult, int maxResult) {
		if (!"7".equals(searchType)) keyWord = "%"+keyWord+"%";
		Map paramMap = new HashMap();
		paramMap.put("bbsId", bbsId);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		paramMap.put("creatorSeq", creatorSeq);
		paramMap.put("searchType", searchType);
		
		if (!"7".equals(searchType)) {
		    paramMap.put("keyWord", keyWord);
		} else {
		  paramMap.put("keyWord", Integer.parseInt(keyWord));
		}
		
		paramMap.put("skipResult", skipResult);
		paramMap.put("maxResult", maxResult);
		
		return getSqlMapClientTemplate().queryForList("Bbs.readBoardContentList", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<BoardContentVO> readBoardContentListByMessageIds(int bbsId, int mailDomainSeq, String[] messageIds) {
		Map paramMap = new HashMap();
		paramMap.put("bbsId", bbsId);
		paramMap.put("messageIds", messageIds);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		
		return getSqlMapClientTemplate().queryForList("Bbs.readBoardContentListByMessageIds", paramMap);
	}
	
	/**
	 * <p>�Խù� ����Ʈ�� ����.</p>
	 *
	 * @param bbsId		�Խ����� ID
	 * @param mailDomainSeq		������ ������
	 * @param searchType	�˻� ����: 1-����,2-�ۼ���,3-����,4-����+�ۼ���,5-����+����,6-��ΰ˻�
	 * @param keyWord	�˻���
	 * @return int 
	 */
	@SuppressWarnings("unchecked")
	public int readBoardContentListCount(int bbsId, int mailDomainSeq, int creatorSeq, String searchType, String keyWord) {
		if (!"7".equals(searchType)) keyWord = "%"+keyWord+"%";
		Map paramMap = new HashMap();
		paramMap.put("bbsId", bbsId);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		paramMap.put("creatorSeq", creatorSeq);
		paramMap.put("searchType", searchType);
		
		if (!"7".equals(searchType)) {
                    paramMap.put("keyWord", keyWord);
                } else {
                  paramMap.put("keyWord", Integer.parseInt(keyWord));
                }
		
		return (Integer)getSqlMapClientTemplate().queryForObject("Bbs.readBoardContentListCount", paramMap);
	}
	
	public int readBoardContentMaxOrderNo(int bbsId, int parentId, int mailDomainSeq) {
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("bbsId", bbsId);
		paramMap.put("parentId", parentId);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		
		return (Integer)getSqlMapClientTemplate().queryForObject("Bbs.readBoardContentMaxOrderNo", paramMap);
	}
	
	public void modifyOrderNo (int bbsId, int parentId, int orderNo, int mailDomainSeq) {
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("bbsId", bbsId);
		paramMap.put("parentId", parentId);
		paramMap.put("orderNo", orderNo);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		
		getSqlMapClientTemplate().update("Bbs.modifyOrderNo", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<BoardContentVO> readPrevBoardContent(int bbsId, int contentId, int parentId, int orderNo, int mailDomainSeq, int creatorSeq, String searchType, String keyWord) {
		if (!"7".equals(searchType)) keyWord = "%"+keyWord+"%";
		Map paramMap = new HashMap();
		paramMap.put("bbsId", bbsId);
		paramMap.put("contentId", contentId);
		paramMap.put("parentId", parentId);
		paramMap.put("orderNo", orderNo);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		paramMap.put("creatorSeq", creatorSeq);
		paramMap.put("searchType", searchType);
		
		if (!"7".equals(searchType)) {
                    paramMap.put("keyWord", keyWord);
                } else {
                  paramMap.put("keyWord", Integer.parseInt(keyWord));
                }
		
		return getSqlMapClientTemplate().queryForList("Bbs.readPrevBoardContent", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<BoardContentVO> readPrevBoardContentByMessageId(int bbsId, int contentId, int parentId, int orderNo, int mailDomainSeq, int creatorSeq, String[] messageIds) {
		Map paramMap = new HashMap();
		paramMap.put("bbsId", bbsId);
		paramMap.put("contentId", contentId);
		paramMap.put("parentId", parentId);
		paramMap.put("orderNo", orderNo);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		paramMap.put("creatorSeq", creatorSeq);
		paramMap.put("messageIds", messageIds);
		
		return getSqlMapClientTemplate().queryForList("Bbs.readPrevBoardContentByMessageId", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<BoardContentVO> readPrevIfNullBoardContentByMessageId(int bbsId, int contentId, int parentId, int orderNo, int mailDomainSeq, int creatorSeq, String[] messageIds) {
		Map paramMap = new HashMap();
		paramMap.put("bbsId", bbsId);
		paramMap.put("contentId", contentId);
		paramMap.put("parentId", parentId);
		paramMap.put("orderNo", orderNo);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		paramMap.put("creatorSeq", creatorSeq);
		paramMap.put("messageIds", messageIds);
		
		return getSqlMapClientTemplate().queryForList("Bbs.readPrevIfNullBoardContentByMessageId", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<BoardContentVO> readNextBoardContent(int bbsId, int contentId, int parentId, int orderNo, int mailDomainSeq, int creatorSeq, String searchType, String keyWord) {
		if (!"7".equals(searchType)) keyWord = "%"+keyWord+"%";
		Map paramMap = new HashMap();
		paramMap.put("bbsId", bbsId);
		paramMap.put("contentId", contentId);
		paramMap.put("parentId", parentId);
		paramMap.put("orderNo", orderNo);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		paramMap.put("creatorSeq", creatorSeq);
		paramMap.put("searchType", searchType);
		
		if (!"7".equals(searchType)) {
                    paramMap.put("keyWord", keyWord);
                } else {
                  paramMap.put("keyWord", Integer.parseInt(keyWord));
                }
		
		return getSqlMapClientTemplate().queryForList("Bbs.readNextBoardContent", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<BoardContentVO> readNextBoardContentByMessageId(int bbsId, int contentId, int parentId, int orderNo, int mailDomainSeq, int creatorSeq, String[] messageIds) {
		Map paramMap = new HashMap();
		paramMap.put("bbsId", bbsId);
		paramMap.put("contentId", contentId);
		paramMap.put("parentId", parentId);
		paramMap.put("orderNo", orderNo);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		paramMap.put("creatorSeq", creatorSeq);
		paramMap.put("messageIds", messageIds);
		
		return getSqlMapClientTemplate().queryForList("Bbs.readNextBoardContentByMessageId", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<BoardContentVO> readNextIfNullBoardContentByMessageId(int bbsId, int contentId, int parentId, int orderNo, int mailDomainSeq, int creatorSeq, String[] messageIds) {
		Map paramMap = new HashMap();
		paramMap.put("bbsId", bbsId);
		paramMap.put("contentId", contentId);
		paramMap.put("parentId", parentId);
		paramMap.put("orderNo", orderNo);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		paramMap.put("creatorSeq", creatorSeq);
		paramMap.put("messageIds", messageIds);
		
		return getSqlMapClientTemplate().queryForList("Bbs.readNextIfNullBoardContentByMessageId", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<BoardContentVO> readPrevIfNullBoardContent(int bbsId, int contentId, int parentId, int orderNo, int mailDomainSeq, int creatorSeq, String searchType, String keyWord) {
		if (!"7".equals(searchType)) keyWord = "%"+keyWord+"%";
		Map paramMap = new HashMap();
		paramMap.put("bbsId", bbsId);
		paramMap.put("contentId", contentId);
		paramMap.put("parentId", parentId);
		paramMap.put("orderNo", orderNo);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		paramMap.put("creatorSeq", creatorSeq);
		paramMap.put("searchType", searchType);
	
		if (!"7".equals(searchType)) {
                    paramMap.put("keyWord", keyWord);
                } else {
                  paramMap.put("keyWord", Integer.parseInt(keyWord));
                }
		
		return getSqlMapClientTemplate().queryForList("Bbs.readPrevIfNullBoardContent", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<BoardContentVO> readNextIfNullBoardContent(int bbsId, int contentId, int parentId, int orderNo, int mailDomainSeq, int creatorSeq, String searchType, String keyWord) {
		Map paramMap = new HashMap();
		paramMap.put("bbsId", bbsId);
		paramMap.put("contentId", contentId);
		paramMap.put("parentId", parentId);
		paramMap.put("orderNo", orderNo);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		paramMap.put("creatorSeq", creatorSeq);
		paramMap.put("searchType", searchType);
		paramMap.put("keyWord", keyWord);
		
		return getSqlMapClientTemplate().queryForList("Bbs.readNextIfNullBoardContent", paramMap);
	}
	
	public void saveBoardContentAttachFile(List<AttachFileVO> attList) {
		AttachFileVO attachFileVo = null;
		for(int i=0; i < attList.size(); i++) {
			attachFileVo = (AttachFileVO)attList.get(i);
			getSqlMapClientTemplate().insert("Bbs.saveBoardContentAttachFile", attachFileVo);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> readBoardContentMsgIds(int bbsId, int[] contentIds, int mailDomainSeq) {
		Map paramMap = new HashMap();
		paramMap.put("bbsId", bbsId);
		paramMap.put("contentIds", contentIds);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		
		return getSqlMapClientTemplate().queryForList("Bbs.readBoardContentMsgIds", paramMap);
	}
	
	public void modifyBoardContentAttCnt(int bbsId, int contentId, int mailDomainSeq, int attCnt) {
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("bbsId", bbsId);
		paramMap.put("contentId", contentId);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		paramMap.put("attCnt", attCnt);
		
		getSqlMapClientTemplate().update("Bbs.modifyBoardContentAttCnt", paramMap);
	}
	
	public List<BoardContentReplyVO> readContentReplyList(int bbsId, int contentId, int isNotice, int mailDomainSeq, int skipResult, int pbase) {
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("bbsId", bbsId);
		paramMap.put("contentId", contentId);
		paramMap.put("isNotice", isNotice);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		
		paramMap.put("skipResult", skipResult);
		paramMap.put("maxResult", pbase);
		
		return getSqlMapClientTemplate().queryForList("Bbs.readContentReplyList", paramMap);
	}
	
	public int readContentReplyListCount(int bbsId, int contentId, int isNotice, int mailDomainSeq) {
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("bbsId", bbsId);
		paramMap.put("contentId", contentId);
		paramMap.put("isNotice", isNotice);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		return (Integer)getSqlMapClientTemplate().queryForObject("Bbs.readContentReplyListCount", paramMap);
	}
	
	public void saveContentReply(BoardContentReplyVO boardContentReplyVo) {
		getSqlMapClientTemplate().insert("Bbs.saveContentReply", boardContentReplyVo);
	}
	
	public void deleteContentReply(BoardContentReplyVO boardContentReplyVo) {
		getSqlMapClientTemplate().delete("Bbs.deleteContentReply", boardContentReplyVo);
	}
	
	@SuppressWarnings("unchecked")
	public int existNewContent(int bbsId, int doaminSeq, String today){
		Map paramMap = new HashMap();
		paramMap.put("bbsId", bbsId);
		paramMap.put("doaminSeq", doaminSeq);
		paramMap.put("today", today+"%");
		return (Integer)getSqlMapClientTemplate().queryForObject("Bbs.existNewContentCnt", paramMap);
	}
}
