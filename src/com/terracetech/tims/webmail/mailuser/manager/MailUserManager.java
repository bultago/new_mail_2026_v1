/**
 * MailUserDao.java 2008  2008-09-26
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mailuser.manager;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.log4j.Logger;

import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.secure.crypto.SymmetricCrypt;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.advice.Transactional;
import com.terracetech.tims.webmail.common.dao.SystemConfigDao;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.vo.SearchVO;
import com.terracetech.tims.webmail.exception.SaveFailedException;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.UserInfo;
import com.terracetech.tims.webmail.mailuser.dao.MailDomainDao;
import com.terracetech.tims.webmail.mailuser.dao.MailUserDao;
import com.terracetech.tims.webmail.mailuser.dao.UserInfoDao;
import com.terracetech.tims.webmail.mailuser.vo.MailDomainCodeVO;
import com.terracetech.tims.webmail.mailuser.vo.MailDomainVO;
import com.terracetech.tims.webmail.mailuser.vo.MailUserInfoVO;
import com.terracetech.tims.webmail.mailuser.vo.MailUserVO;
import com.terracetech.tims.webmail.mailuser.vo.SearchUserVO;
import com.terracetech.tims.webmail.setting.dao.ISettingUserEtcInfoDao;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.util.QuotaUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p><strong>MailUserManager.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li>����� ���� ������ ����ϴ� ���� Manage Class.</li>
 * <li>Dao �� ���� ������� �ý��� ����, �ΰ� ���� ���� ����.</li>
 * <li>������ �ʿ��� ����� ���� Object �� �����Ͽ� ����</li>
 * </ul>
 * @author ysko
 * @since Tims7
 * @version 7.0 
 */
public class MailUserManager {
	Logger log = Logger.getLogger(MailUserManager.class);
	
	private MailUserDao mailUserDao = null;
	private MailDomainDao mailDomainDao = null;
	private UserInfoDao userInfoDao = null;
	private SystemConfigDao systemConfigDao = null;
	private ISettingUserEtcInfoDao etcInfoDao = null;
	
	public void setEtcInfoDao(ISettingUserEtcInfoDao etcInfoDao) {
		this.etcInfoDao = etcInfoDao;
	}

	public void setMailUserDao(MailUserDao mailUserDao){
		this.mailUserDao = mailUserDao;
	}
	
	public void setMailDomainDao(MailDomainDao mailDomainDao) {
		this.mailDomainDao = mailDomainDao;
	}
	
	public void setUserInfoDao(UserInfoDao userInfoDao) {
		this.userInfoDao = userInfoDao;
	}
	
	public void setSystemConfigDao(SystemConfigDao systemConfigDao) {
		this.systemConfigDao = systemConfigDao;
	}

	public SearchUserVO[] searchSimpleUserInfo (String key, String compareValue, int skip, int max) {
		
		SearchVO vo = new  SearchVO ();
		vo.setSearchKey(key.equals("user_name") ? key : "mail_uid");
		vo.setCompareValue(compareValue);
		vo.setSortKey("user_name");
		vo.setAllLikeSymbolUsed(true);
		
		return this.searchSimpleUserInfo(vo, skip, max);
	}
	
	public SearchUserVO[] searchSimpleUserInfo (SearchVO vo, int skip, int max) {
		max = max <= 0 ? Integer.MAX_VALUE : max;
		return mailUserDao.searchSimpleUserInfo(vo, skip, max);
	}
	
	private UserEtcInfoVO getInitUserEtcVO(int mailUseSeq){
		UserEtcInfoVO vo = new UserEtcInfoVO();
		
		String locale = EnvConstants.getBasicSetting("setup.state");
		
		vo.setWriteMode("jp".equals(locale) ? "text":"html");
		vo.setHiddenImg("off");
		vo.setHiddenTag("on");
		vo.setSignAttach("off");
		vo.setNotiInterval(0);
		vo.setUserLocale(locale);
		vo.setForwardingMode("attached");
		vo.setUserSkin("0");
		vo.setSaveSendBox("on");
		vo.setPageLineCnt(15);
		vo.setAfterLogin("domain");
		vo.setAutoSaveMode("on");
		vo.setAutoSaveTerm(0);
		vo.setReceiveNoti("jp".equals(locale) ? "off":"on");
		vo.setCharSet("jp".equals(locale) ? "ISO-2022-JP":"EUC-KR");
		
		return vo;
	}
	
	public User readUserAuthInfo (String userDn) {
		Map<String, Object> userMap = mailUserDao.readMailUserAuthDn(userDn);
		if (userMap == null || userMap.isEmpty()) {
			return null;
		}	
		UserInfo user = new UserInfo();
		user.setUserInfoMap(userMap);
		
		int mailUseSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		Map<String, Object> etcMap = etcInfoDao.readUserEtcInfoMap(mailUseSeq);
		if(etcMap==null){
			etcInfoDao.saveUserEtcInfo(getInitUserEtcVO(mailUseSeq));
			etcMap = etcInfoDao.readUserEtcInfoMap(mailUseSeq);
		}
		user.setUserInfoMap(etcMap);
		
		user.put(User.MAIL_HOST, systemConfigDao.getHostName(user.get(User.MAIL_HOST)));		
		user.put(User.EMAIL, user.get(User.MAIL_UID)+ "@" + user.get(User.MAIL_DOMAIN));		
		user.put(User.QUOTA_STR, QuotaUtil.getQuotaStr(user));
		user.put(User.IMAP_LOGIN_ARGS, user.get(User.MESSAGE_STORE) + 
				 " 0 0 0 0 0 0 "+ 
				user.get(User.MAIL_USER_SEQ) + " " + 
				user.get(User.MAIL_DOMAIN_SEQ));		
		return user;
	}

	public User readUserAuthInfo (String id , String domain) {
		Map<String, Object> userMap = mailUserDao.readMailUserAuthInfo(id, domain);
		if (userMap == null || userMap.isEmpty()) {
			return null;
		}	
		UserInfo user = new UserInfo();
		user.setUserInfoMap(userMap);
		
		int mailUseSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		Map<String, Object> etcMap = etcInfoDao.readUserEtcInfoMap(mailUseSeq);
		if(etcMap==null){
			etcInfoDao.saveUserEtcInfo(getInitUserEtcVO(mailUseSeq));
			etcMap = etcInfoDao.readUserEtcInfoMap(mailUseSeq);
		}
		user.setUserInfoMap(etcMap);
		
		user.put(User.MAIL_HOST, systemConfigDao.getHostName(user.get(User.MAIL_HOST)));		
		user.put(User.EMAIL, user.get(User.MAIL_UID)+ "@" + user.get(User.MAIL_DOMAIN));		
		user.put(User.QUOTA_STR, QuotaUtil.getQuotaStr(user));
		user.put(User.IMAP_LOGIN_ARGS, user.get(User.MESSAGE_STORE) + 
				 " 0 0 0 0 0 0 "+ 
				user.get(User.MAIL_USER_SEQ) + " " + 
				user.get(User.MAIL_DOMAIN_SEQ));
		return user;
	}
	
	
	public User readUserConnectionInfo (String id , String domain) {
		Map<String, Object> userMap = mailUserDao.readMailUserAuthInfo(id, domain);
		if (userMap == null || userMap.isEmpty()) {
			return null;
		}	
		UserInfo user = new UserInfo();
		user.setUserInfoMap(userMap);		
		
		user.put(User.MAIL_HOST, systemConfigDao.getHostName(user.get(User.MAIL_HOST)));		
		user.put(User.EMAIL, user.get(User.MAIL_UID)+ "@" + user.get(User.MAIL_DOMAIN));		
		user.put(User.QUOTA_STR, QuotaUtil.getQuotaStr(user));
		user.put(User.IMAP_LOGIN_ARGS, user.get(User.MESSAGE_STORE) + 
				 " 0 0 0 0 0 0 "+ 
				user.get(User.MAIL_USER_SEQ) + " " + 
				user.get(User.MAIL_DOMAIN_SEQ));
		return user;
	}
	
	public User readUserConnectionInfoByEmpno (String empno , String domain) {
		Map<String, Object> userMap = mailUserDao.readMailUserAuthInfoByEmpno(empno, domain);
		if (userMap == null || userMap.isEmpty()) {
			return null;
		}	
		UserInfo user = new UserInfo();
		user.setUserInfoMap(userMap);		
		
		user.put(User.MAIL_HOST, systemConfigDao.getHostName(user.get(User.MAIL_HOST)));		
		user.put(User.EMAIL, user.get(User.MAIL_UID)+ "@" + user.get(User.MAIL_DOMAIN));		
		user.put(User.QUOTA_STR, QuotaUtil.getQuotaStr(user));
		user.put(User.IMAP_LOGIN_ARGS, user.get(User.MESSAGE_STORE) + 
				 " 0 0 0 0 0 0 "+ 
				user.get(User.MAIL_USER_SEQ) + " " + 
				user.get(User.MAIL_DOMAIN_SEQ));
		return user;
	}
	
	public User readUserOtherInfo (int userSeq, int domainSeq) {
		Map<String, Object> userMap = mailUserDao.readMailUserOtherInfo(userSeq, domainSeq);
		if (userMap == null || userMap.isEmpty()) {
			return null;
		}	
		UserInfo user = new UserInfo();
		user.setUserInfoMap(userMap);		
		
		user.put(User.MAIL_HOST, systemConfigDao.getHostName(user.get(User.MAIL_HOST)));
		user.put(User.EMAIL, user.get(User.MAIL_UID)+ "@" + user.get(User.MAIL_DOMAIN));		
		user.put(User.QUOTA_STR, QuotaUtil.getQuotaStr(user));
		user.put(User.IMAP_LOGIN_ARGS, user.get(User.MESSAGE_STORE) + " " + 
				" 0 0 0 0 0 0 " +
				userSeq + " " + 
				domainSeq);
				
		return user;
	}
	
	public User readUserMailConnectionInfo (int userSeq, int domainSeq) {
		Map<String, Object> userMap = mailUserDao.readMailUserConnectionInfo(userSeq, domainSeq);
		if (userMap == null || userMap.isEmpty()) {
			return null;
		}	
		UserInfo user = new UserInfo();
		user.setUserInfoMap(userMap);		
		
		user.put(User.MAIL_HOST, systemConfigDao.getHostName(user.get(User.MAIL_HOST)));
		user.put(User.EMAIL, user.get(User.MAIL_UID)+ "@" + user.get(User.MAIL_DOMAIN));		
		user.put(User.QUOTA_STR, QuotaUtil.getQuotaStr(user));
		user.put(User.IMAP_LOGIN_ARGS, user.get(User.MESSAGE_STORE) + " " + 
				" 0 0 0 0 0 0 " +
				userSeq + " " + 
				domainSeq);
				
		return user;
	}
	
	@SuppressWarnings("unchecked")
	public void setUserSystemInfo (User user) {
		
		int mailDomainGroupSeq = Integer.parseInt(user.get(User.MAIL_GROUP_SEQ));
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		Map groupInfo = mailDomainDao.readMailDomainGroup(mailDomainSeq, mailDomainGroupSeq);
		
		int userAuth = Integer.parseInt((String) user.get(User.MAIL_SERVICES));
		int groupAuth = 0;
		
		Object groupAuthObj = null;
		Object noteUseObj = null;
		Iterator<String> keys = groupInfo.keySet().iterator();
    	while (keys.hasNext()) {
    		String key = keys.next();
    		if (User.MAIL_SERVICES.equalsIgnoreCase(key)) {
    			groupAuthObj = groupInfo.get(key);
    		} else if (User.NOTE_USE.equalsIgnoreCase(key)) {
    			noteUseObj = groupInfo.get(key);
    		}
    	}
		
		if(groupAuthObj instanceof Long){
			groupAuth = ((Long)groupAuthObj).intValue();
		}else if(groupAuthObj instanceof Integer){
			groupAuth = ((Integer)groupAuthObj).intValue();
		}else if(groupAuthObj instanceof String){
			groupAuth = Integer.parseInt((String)groupAuthObj);
		}else if (groupAuthObj instanceof BigDecimal) {
			groupAuth = ((BigDecimal)groupAuthObj).intValue();
		}
		int auth = userAuth | groupAuth;
		
		((UserInfo)user).setUserInfoMap(groupInfo);
		
		user.put(User.MAIL_SERVICES, auth);
		
		String noteLicense = systemConfigDao.readNoteLicense();
		String noteUse = (noteUseObj != null) ? (String)noteUseObj : "off";
		String flag = "F";
		if ("enabled".equalsIgnoreCase(noteLicense) && "on".equalsIgnoreCase(noteUse)) {
			flag = "T";
		}
		user.put(User.NOTE_USE, flag);
	}
	
	public Map<String,Long> getLocalDomainMap(){
		return mailDomainDao.getLocalDomainMap();	
	}
	
	public String getDoaminName(int mailDomainSeq){
		return mailDomainDao.readMailDomainName(mailDomainSeq);
	}
	
	public List<MailDomainCodeVO> getMailDomainCode(int mailDomainSeq, String codeClass, String codeLocale) {
		return mailDomainDao.readDomainCode(mailDomainSeq, codeClass, codeLocale);
	}
	
	public List<MailDomainVO> readMailDomainList() {
		return mailDomainDao.readMailDomainList();
	}
	
	public int readMailDomainListCount() {
		return mailDomainDao.readMailDomainListCount();
	}
	
	public boolean readUserInfoBySsn(String domain, String ssn) {
		boolean isExist = false;
		try {
			if (userInfoDao.readUserInfoBySsn(domain, getEncryptedSSN(ssn)) > 0) {
				isExist = true;
			}
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}
		return isExist;
	}
	
	public boolean readUserInfoByEmpno(String domain, String empno) {
		boolean isExist = false;
		try {
			if (userInfoDao.readUserInfoByEmpno(domain, empno) > 0) {
				isExist = true;
			}
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}
		return isExist;
	}	
	
	public int searchMailDomainSeq(String mailDomain) {
		return mailDomainDao.searchDomainSeq(mailDomain);
	}
	
	public boolean readUserIdDupCheck(int mailDomainSeq, String userId) {
		boolean isExist = false;
		if (mailUserDao.readUserIdDupCheck(userId, mailDomainSeq) > 0) {
			isExist = true;
		}
		return isExist;
	}
	
	public int readDomainGroupSeq(int mailDomainSeq, String mailGroup) {
		return mailDomainDao.readDomainGroupSeq(mailDomainSeq, mailGroup);
	}
	
	@Transactional
	public void saveMailUser(MailUserVO mailUserVo, MailUserInfoVO mailUserInfoVo, UserEtcInfoVO userEtcInfoVo) throws SaveFailedException {
		mailUserDao.saveMailUser(mailUserVo);
		
		int userSeq = readUserSeq(mailUserVo.getMailUid(), mailUserVo.getMailDomainSeq());
		if (userSeq == 0) {
			throw new SaveFailedException();
		}
		mailUserInfoVo.setMailUserSeq(userSeq);
		
		userInfoDao.saveUserInfo(mailUserInfoVo);
		
		userEtcInfoVo.setUserSeq(userSeq);
		etcInfoDao.saveUserEtcInfo(userEtcInfoVo);
	}
	
	public void saveUserInfo(MailUserInfoVO mailUserInfoVo) {
		userInfoDao.saveUserInfo(mailUserInfoVo);
	}
	
	public int readUserSeq(String userId, int mailDomainSeq) {
		return mailUserDao.readUserSeq(userId, mailDomainSeq);
	}
	
	public int readUserSeq(String userId, String mailDomain) {
		return mailUserDao.readUserSeq(userId, mailDomain);
	}
	
	public boolean deleteMailUser(int mailUserSeq) {
		return mailUserDao.deleteMailUser(mailUserSeq);
	}
	
	public String searchUserId(String userName, String ssn, int mailDomainSeq) {
		try {
			return mailUserDao.searchUserId(userName, getEncryptedSSN(ssn), mailDomainSeq);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			
			return "";
		}
	}
	
	public String searchUserIdByEmpno(String userName, String empno, int mailDomainSeq) {
		try {
			return mailUserDao.searchUserIdByEmpno(userName, empno, mailDomainSeq);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			
			return "";
		}
	}
	
	public String searchUserIdByJpInfo(String userName, String postalCode, String birthday, int mailDomainSeq) {
		try {
			return mailUserDao.searchUserIdByJpInfo(userName, postalCode, birthday, mailDomainSeq);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			
			return "";
		}
	}
	
	private String getEncryptedSSN(String ssn) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			NoSuchProviderException, UnsupportedEncodingException,
			IllegalBlockSizeException, BadPaddingException {
		if (StringUtils.isEmpty(ssn))
			return "";

		return SecureUtil.encrypt(SymmetricCrypt.AES, "terrace-12345678", ssn);
	}
	
	public int searchPassword(String userId, String passCode, String passAnswer, int mailDomainSeq) {
		return mailUserDao.searchPassword(userId, passCode, passAnswer, mailDomainSeq);
	}
	
	public void modifyUserPassword(int mailUserSeq, String mailPassword) {
		mailUserDao.modifyUserPassword(mailUserSeq, mailPassword);
	}
	
	public String readDefaultDomain() throws Exception {
		return mailDomainDao.readDefaultDomain();
	}
	
	public Map readDomain(int mailDomainSeq) {
		return mailDomainDao.readMailDomain(mailDomainSeq);
	}
	
	public MailUserInfoVO readUserInfo(int userSeq) {
		return userInfoDao.readUserInfo(userSeq);
	}
	
	public List<SearchUserVO> searchMailUser(String searchId, String searchName, int mailDomainSeq, String equal){
		return mailUserDao.searchMailUser(searchId, searchName, mailDomainSeq, equal);
	}
	
	public int readUserPassFailCount(int mailUserSeq){
		return mailUserDao.readUserPassFailCount(mailUserSeq);
	}
	
	public void updateUserPassFailCount(int mailUserSeq, int count){
		mailUserDao.updateUserPassFailCount(mailUserSeq, count);
	}
	
	public String getNoticeContent(int mailDomainSeq) {
		return mailDomainDao.readNoticeContent(mailDomainSeq);
	}
	
	public String readMailUid(int mailUserSeq){
		return mailUserDao.readMailUid(mailUserSeq);
	}
	
	public void updateLoginTime(String mailUserSeq, String loginTimeInfo){
		mailUserDao.updateLoginInfo(mailUserSeq, loginTimeInfo);
	}

	public String searchUserIdByEmpno(String empno, String domain) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("empno", empno);
		param.put("domain", domain);
		
		return mailUserDao.readMailUserSsoInfoByEmpno(param);
	}
	
	public String getAlternateId(String userId, String domainName){		
		return mailUserDao.readAlternateId(userId, domainName);
	}

	public String getMassSend(int userSeq) {
		return mailUserDao.readMassSend(userSeq);
	}
	
	public String getMassUpload(int userSeq) {
            return mailUserDao.readMassUpload(userSeq);
        }
	
	public String getUserDN(int userSeq){
		return mailUserDao.readUserDN(userSeq);
	}
	
	public String getSearchUserDN(String uid, String domain){
		return mailUserDao.readAccountDN(uid,domain);
	}
	
	public Map<String, String> readUserSetting(int domainSeq, int groupSeq, int userSeq){
		return mailUserDao.readUserSetting(domainSeq, groupSeq, userSeq);
	}
	
	public Map<String, String> readUserSetting(int domainSeq, int userSeq){
		return mailUserDao.readUserSetting(domainSeq, -1, userSeq);
	}
	
	public boolean readUserInfoByJpInfo(String domain, String name, String postalCode, String birthday) {
		boolean isExist = false;
		try {
			if (userInfoDao.readUserInfoByJpInfo(domain, name, postalCode, birthday) > 0) {
				isExist = true;
			}
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}
		return isExist;
	}
	
	public String readUserInfoMobileNo(int mailUserSeq) {
		return mailUserDao.readUserInfoMobileNo(mailUserSeq);
	}
	
	public Map readDormantMonth(int mailDomainSeq) {
		return mailDomainDao.readDormantMonth(mailDomainSeq);
	}
	
	public void changeUserAccount(int mailUserSeq,String status) {
		mailUserDao.changeUserAccount(mailUserSeq,status);
	}

	 public String readMailUserConfig(int mailUserSeq, String configName) {
    	return mailUserDao.readMailUserConfig(mailUserSeq, configName);
    }
    public void saveMailUserConfig(int mailUserSeq, String configName, String configValue) {
    	mailUserDao.saveMailUserConfig(mailUserSeq, configName, configValue);
    }
    public void deleteMailUserConfig(int mailUserSeq, String configName) {
    	mailUserDao.deleteMailUserConfig(mailUserSeq, configName);
    }
    
    /**
     * 대량 메일 발송 권한값을 반환한다.
     * 
     * @since 2012. 8. 13.
     * @author mskim
     * @param mailDomainSeq
     * @return
     */
    public String readMassSenderUser(int mailDomainSeq) {
        return mailUserDao.readMassSenderUser(mailDomainSeq);
    }
    
    /**
     * 대량 메일 수신 파일 업로드값을 반환한다. 
     * 
     * @since 2012. 8. 13.
     * @author mskim
     * @param mailDomainSeq
     * @return
     */
    public String readFileUploadUser(int mailDomainSeq) {
        return mailUserDao.readFileUploadUser(mailDomainSeq);
    }
    
    /*
     * TCUSTOM-2070 2016-10-28 도메인별 MAIL_SERVICES 정수 값을 가져온다. 
     */
    public int readMailServiesFromDomain(int mailDomainSeq) {
        return mailDomainDao.readMailServiesFromDomain(mailDomainSeq);
    }
}