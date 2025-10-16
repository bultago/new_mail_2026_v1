/**
 * OrganizaionDao.java 2008. 11. 27.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.organization.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;
import com.terracetech.tims.webmail.organization.vo.DeptVO;
import com.terracetech.tims.webmail.organization.vo.MemberVO;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p>
 * <strong>OrganizaionDao.java</strong> Class Description
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
public class OrganizationDao extends SqlMapClientDaoSupport implements IOrganizationDao {

	public void getOrgTree() {

	}

	public void saveDept(DeptVO dept){
		getSqlMapClientTemplate().insert("Org.insertDept", dept);
	}

	public DeptVO readDept(int domain, String ordCode) {
		Map map = new HashMap();
		map.put("mailDomainSeq", domain);
		map.put("orgCode", ordCode);

		return (DeptVO) getSqlMapClientTemplate().queryForObject("Org.getDept",
				map);
	}

	public List<DeptVO> readDepts(int domain) {
		return getSqlMapClientTemplate().queryForList("Org.getDepts", domain);
	}

	public void saveMember(MemberVO member) {
		getSqlMapClientTemplate().insert("Org.insertMember", member);
	}

	public MemberVO readMember(String codeLocale, String orgCode, int domainSeq, int memberSeq){
		Map param = new HashMap();
		param.put("mailDomain", domainSeq);
		param.put("userSeq", memberSeq);
		param.put("orgCode", orgCode);
		param.put("locale", codeLocale==null ? "ko" : codeLocale);
		
		return (MemberVO) getSqlMapClientTemplate().queryForObject("Org.getMember", param);
	}
	
	public List<MemberVO> readMemberList(String codeLocale, String orgCode, String orgFullCode,
			boolean hasSubDept, int domainSeq, int skipResult, int maxResult,
			String searchType, String keyWord, String sortBy, String sortDir, Map<String, Object> paramMap) {
		Map param = new HashMap();
		param.put("mailDomain", domainSeq);
		//검색하는 경우 orgcode를 뺀다.
		if(StringUtils.isEmpty(keyWord)){
			if(StringUtils.isNotEmpty(orgCode)){
				if(hasSubDept){
					param.put("orgFullCode", orgFullCode + ":%");
				}
				param.put("orgCode", orgCode);					
			}
		}
		
		param.put("locale", codeLocale);
		param.put("searchType", searchType);
		param.put(searchType, "%" +keyWord + "%");
		param.put("sortBy", sortBy);
		param.put("sortDir", sortDir);
		
		param.put("skipResult", skipResult);
		param.put("maxResult", maxResult);
		
		param.put("level1",replaceColumnName(paramMap.get("level1")));
		param.put("level2",replaceColumnName(paramMap.get("level2")));
		param.put("level3",replaceColumnName(paramMap.get("level3")));
		param.put("level4",replaceColumnName(paramMap.get("level4")));
		param.put("level5",replaceColumnName(paramMap.get("level5")));
		param.put("level6",replaceColumnName(paramMap.get("level6")));
		param.put("level7",replaceColumnName(paramMap.get("level7")));
		
		return getSqlMapClientTemplate().queryForList("Org.getMembers", param);
	}
	
	private String replaceColumnName(Object obj){
		if(obj==null)
			return null;
		String name = (String)obj;
		
		if("dept".equalsIgnoreCase(name)){
			return "dept_name";
		}else if("class".equalsIgnoreCase(name)){
			return "class_code";
		}else if("name".equalsIgnoreCase(name)){
			return "member_name";
		}else if("email".equalsIgnoreCase(name)){
			return "member_email";
		}else if("title".equalsIgnoreCase(name)){
			return "title_code";
		}else if("grade".equalsIgnoreCase(name)){
			return "grade_code";
		}
		
		return name;
	}

	public void deleteMember(MemberVO member) {
		getSqlMapClientTemplate().delete("Org.deleteMember", member);
	}

	public int readMemberCount(String codeLocale, String orgCode, String orgFullCode, boolean hasSubDept, int domainSeq, String searchType, String keyWord) {
		Map param = new HashMap();
		param.put("mailDomain", domainSeq);
		//검색하는 경우 orgcode를 뺀다.
		if(StringUtils.isEmpty(keyWord)){
			if(StringUtils.isNotEmpty(orgCode)){
				if(hasSubDept){
					param.put("orgFullCode", orgFullCode + ":%");
				}
				param.put("orgCode", orgCode);	
			}
		}
			
		param.put("locale", codeLocale);
		param.put("searchType", searchType);
		param.put(searchType, "%" +keyWord + "%");
		
		return (Integer)getSqlMapClientTemplate().queryForObject("Org.getMemberCount", param);
	}
	
	public String[] readDeptListWithHierarchy(int domainSeq, String orgCode){
		List<String> depts = null;
		Map param = new HashMap();
		param.put("mailDomain", domainSeq);
		param.put("orgCode", "%" + orgCode + "%");
		
		depts = getSqlMapClientTemplate().queryForList("Org.getDeptList", param);
		
		return depts.toArray(new String[depts.size()]);
	}
	
	public List<MailAddressBean> readAddressList(int domainSeq, String[] orgCodes, String codeType, String code){
		List<MailAddressBean> result = null;
		Map param = new HashMap();
		param.put("mailDomain", domainSeq);
		param.put("orgCodes", orgCodes);
		param.put("codeType", codeType);
		param.put("code", code);
		
		result = getSqlMapClientTemplate().queryForList("Org.getAddressEmailList", param);
		
		return result;
	}
	
	public List<DeptVO> readDeptChildList(int mailDomainSeq, String orgUpcode) {
		Map param = new HashMap();
		param.put("mailDomainSeq", mailDomainSeq);
		param.put("orgUpcode", orgUpcode);
		
		return getSqlMapClientTemplate().queryForList("Org.readDeptChildList", param);
	}
	
	public DeptVO readRootDept(int mailDomainSeq) {
		return (DeptVO)getSqlMapClientTemplate().queryForObject("Org.readRootDept", mailDomainSeq);
	}
	
	public int readHasChild(int mailDomainSeq, String orgCode) {
		Map param = new HashMap();
		param.put("mailDomainSeq", mailDomainSeq);
		param.put("orgCode", orgCode);
		return (Integer)getSqlMapClientTemplate().queryForObject("Org.readHasChild", param);
	}

	public List<DeptVO> findDept(int mailDomainSeq, String orgName) {
		Map param = new HashMap();
		param.put("mailDomainSeq", mailDomainSeq);
		param.put("orgName", orgName);
		
		return getSqlMapClientTemplate().queryForList("Org.getDeptListByName", param);
	}

	public List<DeptVO> findDept(int mailDomainSeq, String[] orgNames) {
		Map param = new HashMap();
		param.put("mailDomainSeq", mailDomainSeq);
		param.put("orgNames", orgNames);
		
		if(orgNames==null || orgNames.length==0){
			return new ArrayList<DeptVO>();
		}
		
		return getSqlMapClientTemplate().queryForList("Org.getDeptListByNames", param);
	}
	
	public String findOrgCode(int mailDomainSeq, String orgFullCode) {
		Map param = new HashMap();
		param.put("mailDomainSeq", mailDomainSeq);
		param.put("orgFullCode", orgFullCode);
		
		return (String)getSqlMapClientTemplate().queryForObject("Org.findOrgCode", param);
	}
}
