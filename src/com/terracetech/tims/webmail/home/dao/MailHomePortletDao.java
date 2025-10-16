/**
 * MailHomePortletDao.java 2008. 12. 10.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.home.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.home.vo.MailHomeLayoutVO;
import com.terracetech.tims.webmail.home.vo.MailHomePortletVO;
import com.terracetech.tims.webmail.home.vo.MailMenuLayoutVO;

/**
 * <p>
 * <strong>MailHomePortletDao.java</strong> Class Description
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
public class MailHomePortletDao extends SqlMapClientDaoSupport implements IMailHomePortletDao{

	@SuppressWarnings("unchecked")
	public List<MailHomePortletVO> readPortlets(int domainSeq){
		return getSqlMapClientTemplate().queryForList("Home.selectMailHomePortlets", domainSeq);
	}

	public MailHomePortletVO readPortlet(int domainSeq, String portletName){
		Map<String, String> param = new HashMap<String, String>();
		param.put("domainSeq", String.valueOf(domainSeq));
		param.put("name", portletName);
		
		return (MailHomePortletVO) getSqlMapClientTemplate().queryForObject(
				"Home.selectMailHomePortlet", param);
	}

	public void saveLayoutPortlet(MailHomePortletVO vo) {
		getSqlMapClientTemplate().insert("Home.insertLayoutPortlet", vo);
	}

	public void deleteLayoutPortlet(int userSeq){
		getSqlMapClientTemplate().delete("Home.deleteLayoutPortlet", userSeq);
	}

	@SuppressWarnings("unchecked")
	public List<MailHomePortletVO> readLayoutPortlet(int userSeq) {
		return getSqlMapClientTemplate().queryForList(
				"Home.selectMailHomeLayoutPortlet", userSeq);
	}
	
	@SuppressWarnings("unchecked")
	public List<MailHomeLayoutVO> readLayout() {
		return getSqlMapClientTemplate().queryForList("Home.selectMailHomeLayout");
	}
	
	public void saveLayout(MailHomeLayoutVO layout) {
		getSqlMapClientTemplate().insert("Home.insertMailHomeLayout", layout);
	}
	
	public void savePortlet(MailHomePortletVO vo) {
		getSqlMapClientTemplate().insert("Home.insertMailHomePortlet", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<MailMenuLayoutVO> readMenusList(int mailDomainSeq) {
		return getSqlMapClientTemplate().queryForList("Home.selectMailMenuList", mailDomainSeq);
	}
}
