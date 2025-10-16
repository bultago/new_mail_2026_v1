/**
 * SettingSecureDao.java 2009. 1. 7.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mailuser.dao;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.common.vo.MailConfigVO;

/**
 * <p><strong>SettingSecureDao.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author ysko
 * @since Tims7
 * @version 7.0 
 */
@SuppressWarnings("unchecked")
public class SettingSecureDao extends SqlMapClientDaoSupport {

	
	public MailConfigVO[] readPasswordPolicy () {
		String param = "password_policy_webmail%";
		
		List<MailConfigVO> returnValue = getSqlMapClientTemplate().queryForList("SystemConfig.readMailConfigWithLike", param);
		return returnValue.toArray(new MailConfigVO[returnValue.size()]);
	}
	
}
