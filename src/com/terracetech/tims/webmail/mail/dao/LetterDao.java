package com.terracetech.tims.webmail.mail.dao;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.mail.vo.LetterVO;


public class LetterDao extends SqlMapClientDaoSupport {

	@SuppressWarnings("unchecked")
	public List<LetterVO> readLetterList(int mailDomainSeq, int skipResults, int maxResults) {
		return getSqlMapClientTemplate().queryForList("Letter.readLetterList", mailDomainSeq, skipResults, maxResults);
	}
	
	public LetterVO readLetter(int letterSeq) {
		return (LetterVO)getSqlMapClientTemplate().queryForObject("Letter.readLetter", letterSeq);
	}
	
	public int readLetterTotal(int mailDomainSeq) {
		return (Integer)getSqlMapClientTemplate().queryForObject("Letter.totalLetterCount", mailDomainSeq);
	}
	
}
