package com.terracetech.tims.webmail.mail.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;

public class CacheEmailDao extends SqlMapClientDaoSupport{
	public Logger log = Logger.getLogger(this.getClass());
	
	@SuppressWarnings("unchecked")
	public List<MailAddressBean> readPrivateEmailList(int domainSeq, int userSeq, String keyWord, boolean isAutoComplte){
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("mailDomain", domainSeq);
		param.put("userSeq", userSeq);
		param.put("keyWord", keyWord);
		param.put("staticParam", (isAutoComplte)?"T":"F");
		
		List<MailAddressBean> result = null;
		try {
			result = getSqlMapClientTemplate().queryForList("MailUser.readPrivateAddrAddressList", param);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = new ArrayList<MailAddressBean>();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<MailAddressBean> readSharedEmailList(int domainSeq, int userSeq, String keyWord, boolean isAutoComplte){
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("mailDomain", domainSeq);
		param.put("userSeq", userSeq);
		param.put("keyWord", keyWord);
		param.put("staticParam", (isAutoComplte)?"T":"F");
		
		List<MailAddressBean> result = null;
		try {
			result = getSqlMapClientTemplate().queryForList("MailUser.readSharedAddrAddressList", param);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = new ArrayList<MailAddressBean>();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<MailAddressBean> readOrgEmailList(int domainSeq, int userSeq, String locale, String keyWord, boolean isAutoComplte){
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("mailDomain", domainSeq);
		param.put("locale", locale);
		param.put("keyWord", keyWord);
		param.put("staticParam", (isAutoComplte)?"T":"F");
		
		List<MailAddressBean> result = null;
		try {
			result = getSqlMapClientTemplate().queryForList("MailUser.readOrgAddressList", param);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = new ArrayList<MailAddressBean>();
		}
		return result;
	}
	
	/**
	 * "´Ù¿ìÀçÆÒ"<#000100.all.all.true>
	 * 
	 * @param domainSeq
	 * @param userSeq
	 * @param locale
	 * @param keyWord
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MailAddressBean> readDeptList(int domainSeq, String keyWord, boolean isAutoComplte){
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("mailDomain", domainSeq);
		param.put("deptName", (isAutoComplte)? keyWord + "%" : keyWord);
		//param.put("staticParam", (isAutoComplte)?"T":"F");
		
		List<MailAddressBean> result = null;
		try {
			result = getSqlMapClientTemplate().queryForList("MailUser.readDeptAddressList", param);
			if(result != null){
				for (MailAddressBean dept : result) {
					dept.setEmail("#" + dept.getDeptCode() + ".all.all.true");
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = new ArrayList<MailAddressBean>();
		}
		return result;
	}
	
	/**
	 * "°³¹ßÆÀ"<#¿¬±¸¼Ò/°³¹ßÆÀ>
	 * 
	 * @param domainSeq
	 * @param userSeq
	 * @param locale
	 * @param keyWord
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MailAddressBean> readDeptFullNameList(int domainSeq, String keyWord, boolean isAutoComplte){
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("mailDomain", domainSeq);
		param.put("deptName", (isAutoComplte)? keyWord + "%" : keyWord);
		//param.put("staticParam", (isAutoComplte)?"T":"F");
		
		List<MailAddressBean> result = getSqlMapClientTemplate().queryForList("MailUser.readDeptFullCodeList", param);
		if(result != null){
			Map<String, String> deptMap = getSqlMapClientTemplate().queryForMap("MailUser.getDeptMap", domainSeq, "key", "value");
			
			if(result.size()==1){
				MailAddressBean dept = result.get(0);
				dept.setName(result.get(0).getName());
				dept.setEmail("#" + result.get(0).getName());
				dept.setDeptName("");
			}else if(result.size()>1){
				for (MailAddressBean dept : result) {
					String fullCode = dept.getDeptCode();
					String[] codes = fullCode.split(":");
					String value = "#";
					for (int i = 1; i < codes.length; i++) {
						String code = codes[i];
						if(value.equals("#")){
							value = value + deptMap.get(code);	
						}else{
							value = value + "/" + deptMap.get(code);
						}
					}
						
					dept.setEmail(value);
					dept.setName(dept.getName());
					dept.setDeptName("");
				}
			}
		}
		
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<MailAddressBean> readRecentEmailList(int domainSeq, int userSeq, String keyWord, boolean isAutoComplte){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("mailDomain", domainSeq);
		param.put("userSeq", userSeq);
		param.put("keyWord", keyWord);
		param.put("staticParam", (isAutoComplte)?"T":"F");
		
		List<MailAddressBean> result = null;
		try {
			result = getSqlMapClientTemplate().queryForList("MailUser.readRecentRcptAddressList", param);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = new ArrayList<MailAddressBean>();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<MailAddressBean> readDomainEmailList(int domainSeq, String keyWord, boolean isAutoComplte) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("mailDomain", domainSeq);
		param.put("keyWord", keyWord);
		param.put("staticParam", (isAutoComplte)?"T":"F");
		
		List<MailAddressBean> result = null;
		try {
			result = getSqlMapClientTemplate().queryForList("MailUser.readDomainAddressList", param);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = new ArrayList<MailAddressBean>();
		}
		return result;
	}
	
	public String readSearchRcptOption(int mailDomainSeq){
		String searchRcptOption = null;
	
		try {
			Object o = getSqlMapClientTemplate().queryForObject("MailUser.readRcptSearchOption", mailDomainSeq);
			if(o != null){
				searchRcptOption = (String)o;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		if(searchRcptOption != null && searchRcptOption.length() > 0){
			searchRcptOption = searchRcptOption.toUpperCase();
		} else {
			searchRcptOption = "RIPUO";
		}
		
		return searchRcptOption;
	
	}
}