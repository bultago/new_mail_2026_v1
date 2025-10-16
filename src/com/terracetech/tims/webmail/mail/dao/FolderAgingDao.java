/**
 * FolderAgingDao.java 2009. 4. 29.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * <p><strong>FolderAgingDao.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
@SuppressWarnings("unchecked")
public class FolderAgingDao extends SqlMapClientDaoSupport{
	
	public List getAgingInfo(int userSeq) {
		return getSqlMapClientTemplate().queryForList("MailUser.readUserFolderAging", userSeq);
	}
	
	public void addAgingInfo(int userSeq, int agingDay, String folderName) {
		Map map = new HashMap();
		map.put("mailUserSeq", userSeq);
		map.put("agingDay", agingDay);
		map.put("folderName", folderName);
		
		getSqlMapClientTemplate().insert("MailUser.insertFolderAging", map);
	}
	
	public void setAgingInfo(int userSeq, int agingDay, String folderName) {
		Map map = new HashMap();
		map.put("mailUserSeq", userSeq);
		map.put("agingDay", agingDay);
		map.put("folderName", folderName);
		getSqlMapClientTemplate().update("MailUser.changeFolderAging", map);
	}
	
	public void updateAgingInfo(int userSeq, String folderName, String changeFolderName) {
		Map map = new HashMap();
		map.put("mailUserSeq", userSeq);
		map.put("changeFolderName", changeFolderName);
		map.put("folderName", folderName);
		getSqlMapClientTemplate().update("MailUser.changeFolderName", map);
	}
	
	public void deleteAgingInfo(int userSeq, String folderName) {
		Map map = new HashMap();
		map.put("mailUserSeq", userSeq);
		map.put("folderName", folderName);
		getSqlMapClientTemplate().delete("MailUser.deleteFolderAging", map);
	}
}
