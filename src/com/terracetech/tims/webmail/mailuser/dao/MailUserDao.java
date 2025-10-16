/**
 * MailUserDao.java 2008  2008-09-26
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

import com.terracetech.tims.webmail.bbs.vo.BoardQuotaVO;
import com.terracetech.tims.webmail.common.vo.SearchVO;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.vo.MailUserVO;
import com.terracetech.tims.webmail.mailuser.vo.MailUserInfoVO;
import com.terracetech.tims.webmail.mailuser.vo.SearchUserVO;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.webfolder.vo.WebfolderQuotaVO;

/**
 * <p><strong>MailUserDao.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li>DB ���� ���� ����� ������ �������� DAO ��ü.</li> 
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
@SuppressWarnings("unchecked")
public class MailUserDao extends SqlMapClientDaoSupport {

	public SearchUserVO[] searchSimpleUserInfo (SearchVO vo, int skip, int max) {
		vo.setSkipResult(skip);
		vo.setMaxResult(max);
		
		List<SearchUserVO> result = getSqlMapClientTemplate().queryForList("MailUser.searchSimpleUserInfoList", vo);
		return result.toArray(new SearchUserVO[result.size()]);
	}
	
	public Map<String, Object> readMailUserAuthInfo (String id, String domain) {
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(User.MAIL_UID, id);
		paramMap.put(User.MAIL_DOMAIN, domain);
		Object result = getSqlMapClientTemplate().queryForObject("MailUser.readMailUserAuthInfo", paramMap);
		return result instanceof Map ? (Map)result : null;
	}
	
	public Map<String, Object> readMailUserAuthInfoByEmpno (String empno, String domain) {
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(User.EMPNO, empno);
		paramMap.put(User.MAIL_DOMAIN, domain);
		Object result = getSqlMapClientTemplate().queryForObject("MailUser.readMailUserAuthInfoByEmpno", paramMap);
		return result instanceof Map ? (Map)result : null;
	}
	
	public Map<String, Object> readMailUserAuthDn (String userDN) {
		Map<String, String> paramMap = new HashMap<String, String>(1);
		paramMap.put("userDN", userDN);		
		Object result = getSqlMapClientTemplate().queryForObject("MailUser.readMailUserAuthInfoByDN", paramMap);
		return result instanceof Map ? (Map)result : null;
	}
	
	public Map<String, Object> readMailUserOtherInfo (int userSeq, int domainSeq) {
		Map<String, Integer> paramMap = new HashMap<String, Integer>(2);
		paramMap.put("userSeq", userSeq);
		paramMap.put("domainSeq", domainSeq);
		Object result = getSqlMapClientTemplate().queryForObject("MailUser.readMailUserOtherInfo", paramMap);
		return result instanceof Map ? (Map)result : null;
	}
	
	public Map<String, Object> readMailUserConnectionInfo (int userSeq, int domainSeq) {
		Map<String, Integer> paramMap = new HashMap<String, Integer>(2);
		paramMap.put("userSeq", userSeq);
		paramMap.put("domainSeq", domainSeq);
		Object result = getSqlMapClientTemplate().queryForObject("MailUser.readMailUserConnectionInfo", paramMap);
		return result instanceof Map ? (Map)result : null;
	}
	
	public Map<String, ?> readMailUser (int mailUserSeq) {
		Object result = getSqlMapClientTemplate().queryForObject("MailUser.readMailUserBySeq", mailUserSeq);
		return result instanceof Map ? (Map)result : null;
	} 
	
	public boolean deleteMailUser (int mailUserSeq) {
		return getSqlMapClientTemplate().delete("MailUser.deleteMailUser", mailUserSeq) == 1;
	}
	
	public void saveMailUser (MailUserVO vo) {
		getSqlMapClientTemplate().insert("MailUser.saveMailUser", vo);
	}
	
	public boolean modifyMailUser (MailUserVO vo) {
		return getSqlMapClientTemplate().update("MailUser.modifyMailUser", vo) == 1;
	}
	
	public String readMailUserSsoInfoByUid (Map paramMap) {
		Object result = getSqlMapClientTemplate().queryForObject("MailUser.readMailuserSsoInfoByUid", paramMap);
		return result instanceof String ? (String)result : null;
	} 
	
	public String readMailUserSsoInfoByEmpno (Map paramMap) {
		Object result = getSqlMapClientTemplate().queryForObject("MailUser.readMailuserSsoInfoByEmpno", paramMap);
		return result instanceof String ? (String)result : null;
	}
	
	public String readMailUserSsoInfoBySsn (Map paramMap) {
		Object result = getSqlMapClientTemplate().queryForObject("MailUser.readMailuserSsoInfoBySsn", paramMap);
		return result instanceof String ? (String)result : null;
	}
	
	public MailUserInfoVO readMailUserInfo(int domainSeq, String userId){
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("userId", userId);
		paramMap.put("domainSeq", domainSeq);
		
		return (MailUserInfoVO) getSqlMapClientTemplate().queryForObject("MailUser.readMailUserInfoByUid", paramMap); 
	}
	
	public MailUserInfoVO readMailUserInfo(String userId, String domain){
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put("userId", userId);
		paramMap.put("mailDomain", domain);
		
		return (MailUserInfoVO) getSqlMapClientTemplate().queryForObject("MailUser.readMailUserInfoByEmail", paramMap); 
	}
	
	public Map<String, String> readUserInfoByUid (int userSeq) {
		return (Map<String, String>)getSqlMapClientTemplate().queryForObject("MailUser.readUserInfoByUid", userSeq);
	}
	
	public WebfolderQuotaVO readWebfolderInfo (int userSeq) {
		return (WebfolderQuotaVO)getSqlMapClientTemplate().queryForObject("MailUser.readWebfolderInfo", userSeq);
	}
	
	public BoardQuotaVO readBbsInfo (int userSeq) {
		return (BoardQuotaVO)getSqlMapClientTemplate().queryForObject("MailUser.readBbsInfo", userSeq);
	}
	
	public String readUserPass (int userSeq) {
		return (String)getSqlMapClientTemplate().queryForObject("MailUser.readUserPass", userSeq);
	}
	
	public int readUserIdDupCheck(String userId, int mailDomainSeq) {
		Map paramMap = new HashMap();
		paramMap.put("userId", userId);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		return (Integer)getSqlMapClientTemplate().queryForObject("MailUser.readUserIdDupCheck", paramMap);
	}
	
	public int readUserSeq(String userId, int mailDomainSeq) {
		Map paramMap = new HashMap();
		paramMap.put("userId", userId);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		
		Object seq = getSqlMapClientTemplate().queryForObject("MailUser.readUserSeq", paramMap);
		return (seq != null)?(Integer)seq:-1;
	}
	
	public String searchUserId(String userName, String ssn, int mailDomainSeq) {
		Map paramMap = new HashMap();
		paramMap.put("userName", userName);
		paramMap.put("ssn", ssn);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		
		return (String)getSqlMapClientTemplate().queryForObject("MailUser.searchUserId", paramMap);
 	}
	
	public String searchUserIdByJpInfo(String userName, String postalCode, String birthday, int mailDomainSeq) {
		Map paramMap = new HashMap();
		paramMap.put("userName", userName);
		paramMap.put("postalCode", postalCode);
		paramMap.put("birthday", birthday);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		
		return (String)getSqlMapClientTemplate().queryForObject("MailUser.searchUserIdByJpInfo", paramMap);
 	}

	public String searchUserIdByEmpno(String userName, String empno, int mailDomainSeq) {
		Map paramMap = new HashMap();
		paramMap.put("userName", userName);
		paramMap.put("empno", empno);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		
		return (String)getSqlMapClientTemplate().queryForObject("MailUser.searchUserIdByEmpno", paramMap);
 	}
	
	public int searchPassword(String userId, String passCode, String passAnswer, int mailDomainSeq) {
		Map paramMap = new HashMap();
		paramMap.put("userId", userId);
		paramMap.put("passCode", passCode);
		paramMap.put("passAnswer", passAnswer);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		
		return (Integer)getSqlMapClientTemplate().queryForObject("MailUser.searchPassword", paramMap) == null ? 0 : (Integer)getSqlMapClientTemplate().queryForObject("MailUser.searchPassword", paramMap);
	}
	
	public void modifyUserPassword(int mailUserSeq, String mailPassword) {
		Map paramMap = new HashMap();
		paramMap.put("mailUserSeq", mailUserSeq);
		paramMap.put("mailPassword", mailPassword);
		getSqlMapClientTemplate().update("MailUser.changeUserPassword", paramMap);
	}
	
	public List<SearchUserVO> searchMailUser(String userId, String userName, int mailDomainSeq, String equal) {
		Map paramMap = new HashMap();
		userId = (userId != null)?userId:null;
		userName = (userName != null)?userName:null;
		paramMap.put("userId", userId);
		paramMap.put("userName", userName);
		paramMap.put("mailDomainSeq", mailDomainSeq);
		
		String sql = ("true".equalsIgnoreCase(equal)) ? "MailUser.searchMailUser2" : "MailUser.searchMailUser";
		
		return getSqlMapClientTemplate().queryForList(sql, paramMap);
 	}
	
	public int readUserPassFailCount(int mailUserSeq){
		return (Integer) getSqlMapClientTemplate().queryForObject("MailUser.readUserPassFailCount", mailUserSeq);
	}
	
	public void updateUserPassFailCount(int mailUserSeq, int count){
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("userSeq", String.valueOf(mailUserSeq));
		param.put("count", String.valueOf(count));
		
		getSqlMapClientTemplate().update("MailUser.updateUserPassFailCount", param);
	}
	
	public String readMailUid(int mailUserSeq){
		return (String) getSqlMapClientTemplate().queryForObject("MailUser.readMailUid", mailUserSeq);
	}
	
	public String readMailUserName(int mailUserSeq){
		return (String) getSqlMapClientTemplate().queryForObject("MailUser.readMailUsername", mailUserSeq);
	}
	
	public void updateLoginInfo(String mailUserSeq, String loginTimeInfo){
		Map<String, String> param = new HashMap<String, String>();
		param.put("userSeq", mailUserSeq);
		param.put("loginInfo", loginTimeInfo);
		getSqlMapClientTemplate().update("MailUser.updateLoginInfo", param);
	}
	
	public String readAlternateId(String mailUid, String domainName){
		Map param = new HashMap();
		param.put("uid", mailUid);
		param.put("domain", domainName);
		return (String) getSqlMapClientTemplate().queryForObject("MailUser.readUserIdByAlternate", param);
	}

	public String readMassSend(int userSeq) {
		String result = (String) getSqlMapClientTemplate().queryForObject("MailUser.readMassSend", userSeq);
		if(StringUtils.isEmpty(result)){
			return "off";
		}
		return result;
	}
	
	public String readMassUpload(int userSeq) {
            String result = (String) getSqlMapClientTemplate().queryForObject("MailUser.readMassUpload", userSeq);
            if(StringUtils.isEmpty(result)){
                    return "off";
            }
            return result;
       }
	
	public String readUserDN(int userSeq) {		
		return (String) getSqlMapClientTemplate().queryForObject("MailUser.readUserDN", userSeq);
	}
	
	public String readAccountDN(String uid, String domain) {
		Map param = new HashMap();
		param.put("uid", uid);
		param.put("domain", domain);
		return (String) getSqlMapClientTemplate().queryForObject("MailUser.readAccountDN", param);
	}
	
	public Map<String, String> readUserSetting(int domainSeq, int groupSeq, int userSeq){
		
		Map param = new HashMap();
		param.put("domainSeq", domainSeq);
		param.put("userSeq", userSeq);
		param.put("groupSeq", groupSeq);
		
		Map<String, String> configMap = getSqlMapClientTemplate().queryForMap("MailUser.getUserSetting", param, "key", "value");
		
		if(configMap==null)
			configMap = new HashMap<String, String>();
		
		return configMap;
	}

	public int readUserSeq(String userId, String mailDomain) {
		Map paramMap = new HashMap();
		paramMap.put("userId", userId);
		paramMap.put("mailDomain", mailDomain);
		
		Object seq = getSqlMapClientTemplate().queryForObject("MailUser.readUserSeq2", paramMap);
		return (seq != null)?(Integer)seq:-1;
	}
	
	public SearchUserVO readUserIdAndName(int mailUserSeq) {
		return (SearchUserVO)getSqlMapClientTemplate().queryForObject("MailUser.readUserIdAndName", mailUserSeq);
	}
	
	public String readUserInfoMobileNo(int mailUserSeq) {
		Object result = getSqlMapClientTemplate().queryForObject("MailUser.readUserInfoMobileNo", mailUserSeq);
		if (result == null) return "";
		
		return (String) result;
	}
	
	public List<String> readMailUids(int mailDomainSeq) {
		return getSqlMapClientTemplate().queryForList("MailUser.readMailUids", mailDomainSeq);
	}
	
	public void changeUserAccount(int mailUserSeq,String status) {
		Map paramMap = new HashMap();
		paramMap.put("mailUserSeq", mailUserSeq);
		paramMap.put("status", status);
		getSqlMapClientTemplate().update("MailUser.changeUserAccount", paramMap);
	}
	
	public int readNoteSaveCount(int mailDomainSeq, int mailGroupSeq, int mailUserSeq) {
		Map paramMap = new HashMap();
		paramMap.put("mailDomainSeq", mailDomainSeq);
		paramMap.put("mailGroupSeq", mailGroupSeq);
		paramMap.put("mailUserSeq", mailUserSeq);
		
		return (Integer)getSqlMapClientTemplate().queryForObject("MailUser.readNoteSaveCount", paramMap);
	}
	
	public int readNoteSaveDate(int mailDomainSeq, int mailGroupSeq, int mailUserSeq) {
		Map paramMap = new HashMap();
		paramMap.put("mailDomainSeq", mailDomainSeq);
		paramMap.put("mailGroupSeq", mailGroupSeq);
		paramMap.put("mailUserSeq", mailUserSeq);
		
		return (Integer)getSqlMapClientTemplate().queryForObject("MailUser.readNoteSaveDate", paramMap);
	}
	public String readMailUserConfig(int mailUserSeq, String configName) {
	    	Map<String, Object> paramMap = new HashMap<String, Object>();
	    	paramMap.put("mailUserSeq", mailUserSeq);
	    	paramMap.put("configName", configName);
	    	return (String) getSqlMapClientTemplate().queryForObject("MailUser.readMailUserConfig", paramMap);
    }
	    
    public void saveMailUserConfig(int mailUserSeq, String configName, String configValue) {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("mailUserSeq", mailUserSeq);
    	paramMap.put("configName", configName);
    	paramMap.put("configValue", configValue);
    	getSqlMapClientTemplate().insert("MailUser.saveMailUserConfig", paramMap);
    }
    
    public void deleteMailUserConfig(int mailUserSeq, String configName) {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("mailUserSeq", mailUserSeq);
    	paramMap.put("configName", configName);
    	getSqlMapClientTemplate().delete("MailUser.deleteMailUserConfig", paramMap);
    }
    
    public String readMassSenderUser(int mailDomainSeq){        
        Object result = getSqlMapClientTemplate().queryForObject("MailUser.readMassSenderUser", mailDomainSeq);
        if (result == null) return "";        
        return (String) result;
    }
    
    public String readFileUploadUser(int mailDomainSeq){        
        Object result = getSqlMapClientTemplate().queryForObject("MailUser.readFileUploadUser", mailDomainSeq);
        if (result == null) return "";        
        return (String) result;
    }
}
