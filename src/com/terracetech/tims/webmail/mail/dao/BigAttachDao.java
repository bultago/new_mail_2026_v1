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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.mail.vo.MailBigAttachVO;

/**
 * <p>
 * <strong>BigAttachDao.java</strong> Class Description
 * </p>
 * <p>
 * 주요설명
 * </p>
 * <ul>
 * <li></li>
 * </ul>
 * 
 * @author jpjung
 * @since Tims7
 * @version 7.0
 */
@SuppressWarnings("unchecked")
public class BigAttachDao extends SqlMapClientDaoSupport implements
		IBigAttachDao {

	public List<MailBigAttachVO> getListMailBigAttach(int userSeq)
			throws SQLException {
		Map map = new HashMap();
		map.put("mailUserSeq", userSeq);

		return getSqlMapClientTemplate().queryForList(
				"MailAttach.listMailAttach", map);
	}
	
	public MailBigAttachVO getMailBigAttach(int userSeq, String messageId) throws SQLException{
		
		Map map = new HashMap();
		map.put("mailUserSeq", userSeq);
		map.put("messageUid", messageId);

		return (MailBigAttachVO)getSqlMapClientTemplate().queryForObject(
				"MailAttach.selectMailAttach", map);
	}
	
	public void saveMailBigAttach(MailBigAttachVO vo) throws SQLException{		
		getSqlMapClientTemplate().insert("MailAttach.insertMailAttach", vo);		
	}
	
	public void updateMailBigAttach(MailBigAttachVO vo) throws SQLException {
		getSqlMapClientTemplate().insert("MailAttach.updateMailAttach", vo);
	}
	
	public void updateDeleteMailBigAttach(int userSeq, String messageId) throws SQLException{
		Map map = new HashMap();
		map.put("mailUserSeq", userSeq);
		map.put("messageUid", messageId);		
		getSqlMapClientTemplate().update("MailAttach.updateDeleteMailAttach", map);		
	}
	
	
}
