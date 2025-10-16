package com.terracetech.tims.webmail.bbs.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.bbs.vo.BoardVO;

@SuppressWarnings("deprecation")
public class BoardDao extends SqlMapClientDaoSupport {

	/**
	 * <p>�⺻ �Խ��� ��.</p>
	 *
	 * @param defaultBoardList	�⺻ �Խ��� ����Ʈ
	 */
	@SuppressWarnings("deprecation")
	public void saveDefaultBoard(List<BoardVO> defaultBoardList) {
		BoardVO boardVo = null;
		for (int i=0; i < defaultBoardList.size(); i++) {
			boardVo = defaultBoardList.get(i);
			getSqlMapClientTemplate().insert("Bbs.saveBoard", boardVo);
		}
	}

	/**
	 * <p>�Խ��� ����Ʈ.</p>
	 *
	 * @param domainSeq		�������� ������
	 * @return List
	 */
	public List<BoardVO> readBoardList(int domainSeq) {
		return getSqlMapClientTemplate().queryForList("Bbs.readBoardList", domainSeq);
	}

	/**
	 * <p>�Խ��� ��ȸ.</p>
	 *
	 * @param bbsId		�Խ��� ID
	 * @param domainSeq		�������� ������
	 * @return BoardVO
	 */
	public BoardVO readBoard(int bbsId, int domainSeq) {
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("bbsId", bbsId);
		paramMap.put("domainSeq", domainSeq);
		return (BoardVO)getSqlMapClientTemplate().queryForObject("Bbs.readBoard", paramMap);
	}

	public BoardVO readNoticeBoard(int bbsId, int domainSeq) {
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("bbsId", bbsId);
		paramMap.put("domainSeq", domainSeq);
		return (BoardVO)getSqlMapClientTemplate().queryForObject("Bbs.readNoticeBoard", paramMap);
	}

	/**
	 * <p>�Խ��� ��.</p>
	 *
	 * @param boardVo
	 * @return void
	 */
	public void saveBoard(BoardVO boardVo) {
		getSqlMapClientTemplate().insert("Bbs.saveBoard", boardVo);
	}

	/**
	 * <p>�Խ��� ���� ����.</p>
	 *
	 * @param boardVo
	 * @return void
	 */
	public void modifyBoard(BoardVO boardVo) {
		getSqlMapClientTemplate().insert("Bbs.modifyBoard", boardVo);
	}

	/**
	 * <p>�Խ��� ����.</p>
	 *
	 * @param bbsId �Խ��� ID
	 * @param domainSeq ������ ������
	 * @return void
	 */
	public void deleteBoard(int bbsId, int domainSeq) {
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("bbsId", bbsId);
		paramMap.put("domainSeq", domainSeq);
		getSqlMapClientTemplate().delete("Bbs.deleteBoard", paramMap);
	}

	/**
	 * <p>�Խ��� ���� ��ȸ.</p>
	 *
	 * @param bbsId
	 * @return List
	 */
	public List<Integer> readBoardAdiminList(int bbsId, int mailDomainSeq) {
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("bbsId", bbsId);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		return getSqlMapClientTemplate().queryForList("Bbs.readBoardAdiminList", paramMap);
	}

	public List<BoardVO> readNoticeBbsList(int domainSeq) {
		return getSqlMapClientTemplate().queryForList("Bbs.readNoticeBbsList", domainSeq);
	}
}
