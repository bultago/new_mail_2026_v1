/**
 * VCardDao.java 2008. 11. 27.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.setting.dao;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.setting.vo.VCardVO;

/**
 * <p>
 * <strong>VCardDao.java</strong> Class Description
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
public class VCardDao extends SqlMapClientDaoSupport implements IVCardDao {

	public VCardVO readVcard(int userSeq) throws Exception {
		return (VCardVO) getSqlMapClientTemplate().queryForObject(
				"VCard.getVCard", userSeq);
	}

	public void saveVcard(VCardVO vcard) throws Exception {
		getSqlMapClientTemplate().insert("VCard.insertVCard", vcard);
	}
	
	public void modifyVcard(VCardVO vcard) throws Exception {
		getSqlMapClientTemplate().update("VCard.updateVCard", vcard);
	}
	
}
