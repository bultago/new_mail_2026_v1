package com.terracetech.tims.webmail.mailuser.sso;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.ParseException;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import jakarta.servlet.http.HttpServletRequest;

import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.secure.crypto.SecureWebUtil;
import com.terracetech.secure.crypto.SymmetricCrypt;
import com.terracetech.tims.webmail.common.dao.SystemConfigDao;
import com.terracetech.tims.webmail.common.vo.MailConfigVO;
import com.terracetech.tims.webmail.exception.UnSupportedSSOException;
import com.terracetech.tims.webmail.exception.UserNotFoundException;
import com.terracetech.tims.webmail.mailuser.AuthUser;
import com.terracetech.tims.webmail.mailuser.dao.MailUserDao;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.mailuser.vo.SsoInfoVO;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class SsoManager {
	public final static String SSO_PARAM = "paramSso";
	public final static String SSO_COOKIE = "cookieSso";
	public final static String SSO_CUSTOM = "customSso";

	public final static String SSN = "ssn";
	public final static String EMPNO = "empno";
	public final static String MAIL_UID = "mail_uid";
	public final static String MAIL_DOMAIN = "mail_domain";
	
	private final String SSO_CONFIG_NAME = "sso";
	private final String DELIM = ",";
	private final String SECOND_DELIM = "=";
	private final String TRUE_STR = "true";
	
	private final String APPLY = "apply";
	private final String METHOD = "method";
	private final String SSO_KEY = "sso_key";
	private final String KEY = "key";
	private final String CRYPTO_ALGORITHM = "crypto_algorithm";
	
	private UserAuthManager userAuthManager = null;
	
	private SystemConfigDao configDao = null;
	
	private MailUserDao mailUserDao = null;
	
	public void setMailUserDao(MailUserDao mailUserDao) {
		this.mailUserDao = mailUserDao;
	}

	public void setConfigDao(SystemConfigDao configDao) {
		this.configDao = configDao;
	}

	public void setUserAuthManager(UserAuthManager userAuthManager) {
		this.userAuthManager = userAuthManager;
	}
	
	public AuthUser validate(HttpServletRequest request)
			throws UserNotFoundException, InvalidKeyException,
			UnsupportedEncodingException, IllegalBlockSizeException,
			BadPaddingException, NoSuchAlgorithmException,
			NoSuchPaddingException, NoSuchProviderException, ParseException, UnSupportedSSOException {
		
		AuthUser user = new AuthUser();
		String domainSeq = request.getParameter("d");
		if(StringUtils.isEmpty(domainSeq))
			throw new InvalidKeyException("d");
		
		SsoInfoVO infoVo = readSsoInfo(Integer.parseInt(domainSeq));
		if(infoVo ==null){
			throw new UnSupportedSSOException();
		}
		
		SsoAuth result = (SsoAuth)ApplicationBeanUtil.getApplicationBean(infoVo.getMethod() + "Sso");
		Map<String, String> param = result.getValue(infoVo, request);
		
		if(param==null){
			throw new UserNotFoundException();
		}
		
		String mailUid = null;
		//id, empno, ssn
		if("id".equals(infoVo.getSsoKey())){
			if(StringUtils.isEmpty(param.get("mailUid"))){
				throw new UserNotFoundException();
			}
			
			mailUid = mailUserDao.readMailUserSsoInfoByUid(param);
		}else if("empno".equals(infoVo.getSsoKey())){
			if(StringUtils.isEmpty(param.get("empno"))){
				throw new UserNotFoundException();
			}
			//���
			mailUid = mailUserDao.readMailUserSsoInfoByEmpno(param);
		}else if("ssn".equals(infoVo.getSsoKey())){
			if(StringUtils.isEmpty(param.get("ssn"))){
				throw new UserNotFoundException();
			}
			
			//�ֹε�Ϲ�ȣ
			param.put("ssn", getEncryptedSSN(param.get("ssn")));
			mailUid = mailUserDao.readMailUserSsoInfoBySsn(param);
		}
		
		if(mailUid==null){
			throw new UserNotFoundException();
		}
			
		
		String mailDomainSeq = param.get("domain");
		user = userAuthManager.validateSso(mailUid, mailDomainSeq);
		if(user ==null){
			throw new UserNotFoundException();
		}
		
		return user;
	}
	
	private String getEncryptedSSN(String ssn) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			NoSuchProviderException, UnsupportedEncodingException,
			IllegalBlockSizeException, BadPaddingException {
		if (StringUtils.isEmpty(ssn))
			return "";

		return SecureUtil.encrypt(SymmetricCrypt.AES, "terrace-12345678", ssn);
	}
	
	public AuthUser test (String algorithm, String method, HttpServletRequest request) throws ParseException {
		SsoAuth result = (SsoAuth)ApplicationBeanUtil.getApplicationBean(method);
		//result.getTestValue(algorithm, request)
		return userAuthManager.validateSso("", "");
	}
	
	public static String getCurrentAlgorithm() {
		return SecureWebUtil.getSsoAlgorithm();
	}

	public static void setCurrentAlgorithm(String currentAlgorithm) {
		SecureWebUtil.setSsoAlgorithm(currentAlgorithm);
	}
	
	public void sync () {
		
	}
	
	private SsoInfoVO readSsoInfo(int domainSeq) {
		SsoInfoVO resultVo = new SsoInfoVO();
		MailConfigVO vo = configDao.readDomainConfig(domainSeq, SSO_CONFIG_NAME);
		if(vo==null)
			return null;
		
		String[] optionList = vo.getConfigValue().split(DELIM);
		String[] values = null;
		
		for (int i = 0, cnt = optionList.length; i < cnt; i++) {
			values = optionList[i].split(SECOND_DELIM);
			if (values.length != 2)
				continue;
			
			if (APPLY.equalsIgnoreCase(values[0])) {
				resultVo.setApplied(TRUE_STR.equalsIgnoreCase(values[1]));
			} else if (METHOD.equalsIgnoreCase(values[0])) {
				resultVo.setMethod(values[1]);
			} else if (SSO_KEY.equalsIgnoreCase(values[0])) {
				resultVo.setSsoKey(values[1]);
			} else if (CRYPTO_ALGORITHM.equalsIgnoreCase(values[0])) {
				resultVo.setAlgorithm(values[1]);
			}else if (KEY.equalsIgnoreCase(values[0])) {
				resultVo.setKey(values[1]);
			}
		}
		
		return resultVo;
	}
}