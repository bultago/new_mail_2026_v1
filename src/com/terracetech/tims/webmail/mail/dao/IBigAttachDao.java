/**
 * IBigAttachDao.java 2008. 12. 8.
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

import com.terracetech.tims.webmail.mail.vo.MailBigAttachVO;

/**
 * <p><strong>IBigAttachDao.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author jpjung
 * @since Tims7
 * @version 7.0 
 */
public interface IBigAttachDao {

	public List<MailBigAttachVO> getListMailBigAttach(int userSeq) throws SQLException;
	
	public MailBigAttachVO getMailBigAttach(int userSeq, String messageId) throws SQLException;
	
	public void saveMailBigAttach(MailBigAttachVO vo) throws SQLException;
	
	public void updateMailBigAttach(MailBigAttachVO vo) throws SQLException;
	
	public void updateDeleteMailBigAttach(int userSeq, String messageId) throws SQLException;
}
