/**
 * MailDomainDao.java 2008. 12. 4.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mailuser.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.common.vo.CommonLogoVO;
import com.terracetech.tims.webmail.common.vo.LogoVO;
import com.terracetech.tims.webmail.mailuser.vo.MailDomainCodeVO;
import com.terracetech.tims.webmail.mailuser.vo.MailDomainGroupVO;
import com.terracetech.tims.webmail.mailuser.vo.MailDomainVO;

/**
 * <p><strong>MailDomainDao.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author ysko
 * @since Tims7
 * @version 7.0 
 */
public class MailDomainDao extends SqlMapClientDaoSupport {

	@SuppressWarnings("unchecked")
	public int searchDomainSeq (String domain) {
		
		List<Integer> resultList = getSqlMapClientTemplate().queryForList("MailDomain.searchDomainSeq", domain);
		
		return resultList.size() == 1 ? resultList.get(0).intValue() : -1;
	}
	
	@SuppressWarnings("unchecked")
	public Map readMailDomain (int mailDomainSeq) {
		Object result = getSqlMapClientTemplate().queryForObject("MailDomain.readMailDomain", mailDomainSeq);
		return result instanceof Map ? (Map)result : null;
	}	
	
	public String readMailDomainName (int mailDomainSeq) {
		Object result = getSqlMapClientTemplate().queryForObject("MailDomain.readMailDomainName", mailDomainSeq);
		return result instanceof String ? (String)result : null;
	}
	
	public boolean modifyMailDomain (MailDomainVO vo) {
		return getSqlMapClientTemplate().update("MailDomain.modifyMailDomain", vo) == 1;
	}
	
	public boolean deleteMailDomain (int mailDomainSeq) {
		return getSqlMapClientTemplate().delete("MailDomain.deleteMailDomain", mailDomainSeq) == 1;
	}
	
	@SuppressWarnings("unchecked")
	public Map readMailDomainGroup (int mailDomainSeq, int mailDomainGroupSeq) {
		Map param = new HashMap();
		param.put("mail_domain_seq", mailDomainSeq);
		param.put("mail_group_seq", mailDomainGroupSeq);
		
		Object result = getSqlMapClientTemplate().queryForObject("MailDomain.readMailDomainGroup", param);
		return result instanceof Map ? (Map)result : null;
	}
	
	public boolean modifyMailDomainGroup (MailDomainGroupVO vo) {
		return getSqlMapClientTemplate().update("MailDomain.modifyMailDomainGroup", vo) == 1;
	}
	
	public boolean deleteMailDomainGroup (int mailDomainGroupSeq) {
		return getSqlMapClientTemplate().delete("MailDomain.deleteMailDomainGroup", mailDomainGroupSeq) == 1;
	}
	
	public int readMailServiesFromDomain (int mailDomainSeq) {
		Object result = getSqlMapClientTemplate().queryForObject("MailDomain.readMailServiesFromDomain", mailDomainSeq);
		return result instanceof Integer ? ((Integer)result).intValue() : 0;
	}
	
	@SuppressWarnings("unchecked")
	public List<MailDomainCodeVO> readDomainCode(int mailDomainSeq, String codeClass, String codeLocale) {
		Map paramMap = new HashMap();
		paramMap.put("mailDomainSeq", mailDomainSeq);
		paramMap.put("codeClass", codeClass);
		paramMap.put("codeLocale", codeLocale);
		
		return getSqlMapClientTemplate().queryForList("MailDomain.readDomainCode", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Long> getLocalDomainMap(){
		return getSqlMapClientTemplate().queryForMap("MailDomain.readLocalDomain",null,"key","value");
	}
	
	@SuppressWarnings("unchecked")
	public List<MailDomainVO> readMailDomainList() {
		return getSqlMapClientTemplate().queryForList("MailDomain.readMailDomainList");
	}
	
	public int readMailDomainListCount() {
		return (Integer)getSqlMapClientTemplate().queryForObject("MailDomain.readMailDomainListCount");
	}
	
	@SuppressWarnings("unchecked")
	public int readDomainGroupSeq(int mailDomainSeq, String mailGroup) {
		Map paramMap = new HashMap();
		paramMap.put("mailDomainSeq", mailDomainSeq);
		paramMap.put("mailGroup", mailGroup);
		return (Integer)getSqlMapClientTemplate().queryForObject("MailDomain.readDomainGroupSeq", paramMap);
	}
	
	public LogoVO readLogoInfo(int mailDomainSeq) {
		return (LogoVO)getSqlMapClientTemplate().queryForObject("MailDomain.readLogoInfo", mailDomainSeq);
	}
	
	public LogoVO readMobileLogoInfo(int mailDomainSeq) {
		return (LogoVO)getSqlMapClientTemplate().queryForObject("MailDomain.readMobileLogoInfo", mailDomainSeq);
	}
	
	public String readDefaultDomain() {
		return (String)getSqlMapClientTemplate().queryForObject("MailDomain.readDefaultDomain");
	}
	
	public String readNoticeContent(int mailDomainSeq) {
		return (String)getSqlMapClientTemplate().queryForObject("MailDomain.readNoticeContent", mailDomainSeq);
	}
	
	@SuppressWarnings("unchecked")
	public List<CommonLogoVO> readCommonLogo() {
		return getSqlMapClientTemplate().queryForList("MailDomain.readCommonLogo");				
	}
	
	@SuppressWarnings("unchecked")
	public List<CommonLogoVO> readMobileCommonLogo() {
		return getSqlMapClientTemplate().queryForList("MailDomain.readMobileCommonLogo");				
	}
	
	public Map readDormantMonth(int mailDomainSeq) {
		Object result = getSqlMapClientTemplate().queryForObject("MailDomain.readDormantMonth", mailDomainSeq);
		return result instanceof Map ? (Map)result : null;
	}
}