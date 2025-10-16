/**
 * SettingUserInfoDao.java 2008. 12. 3.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mailuser.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.mailuser.vo.MailUserInfoVO;

/**
 * <p><strong>SettingUserInfoDao.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author ysko
 * @since Tims7
 * @version 7.0 
 */
public class UserInfoDao extends SqlMapClientDaoSupport {

	public MailUserInfoVO readUserInfo(int userSeq) {
		Object result = getSqlMapClientTemplate().queryForObject("MailUser.readUserInfo", userSeq);
		return result instanceof MailUserInfoVO ? (MailUserInfoVO)result : null;
	}
	
	public boolean modifyUserInfo (MailUserInfoVO vo) {
		return getSqlMapClientTemplate().update("MailUser.modifyUserInfo", vo) == 1;
	}
	
	public void saveUserInfo (MailUserInfoVO vo) {
		getSqlMapClientTemplate().insert("MailUser.saveUserInfo", vo);
	}
	
	public boolean deleteUserInfo (int userSeq) {
		return getSqlMapClientTemplate().delete("MailUser.deleteUserInfo", userSeq) == 1;
	}
	
	public int readUserInfoBySsn (String domain, String ssn) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("domain", domain);
		param.put("ssn", ssn);
		
		return (Integer)getSqlMapClientTemplate().queryForObject("MailUser.readUserInfoBySsn", param);
	}
	
	public int readUserInfoByEmpno(String domain, String empno) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("domain", domain);
		param.put("empno", empno);
		
		return (Integer)getSqlMapClientTemplate().queryForObject("MailUser.readUserInfoByEmpno", param);
	}	
	
	public int readUserInfoByJpInfo(String domain, String name, String postalCode, String birthday) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("domain", domain);
		param.put("name", name);
		param.put("postalCode", postalCode);
		param.put("birthday", birthday);
		
		return (Integer)getSqlMapClientTemplate().queryForObject("MailUser.readUserInfoByJpInfo", param);
	}
}