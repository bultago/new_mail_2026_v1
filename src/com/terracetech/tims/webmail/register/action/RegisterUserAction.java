package com.terracetech.tims.webmail.register.action;

import java.security.PrivateKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.List;
import java.util.Locale;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.mailuser.vo.MailDomainCodeVO;
import com.terracetech.tims.webmail.util.CryptoSession;
import com.terracetech.tims.webmail.util.StringUtils;

public class RegisterUserAction extends BaseAction {

	private MailUserManager mailUserManager = null;
	
	private String userName = null;
	private String ssn = null;
	private String mailDomain = null;
	private String empno = null;
	private String postalCode = null;
	private String birthday = null;
	private int mailDomainSeq = 0;
	private List<MailDomainCodeVO> passCodeList = null;
	private final static String PASSWORD_CODE = "30000";
	private String installLocale = null;
	
	public RegisterUserAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}
	
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	
	public String execute() throws Exception {
		
		Locale locale = I18nConstants.getBundleUserLocale(request);
		mailDomainSeq = mailUserManager.searchMailDomainSeq(mailDomain);
		passCodeList = mailUserManager.getMailDomainCode(mailDomainSeq, PASSWORD_CODE, locale.getLanguage());
		installLocale = EnvConstants.getBasicSetting("setup.state");
		
		/* Login ID, PW Parameter RSA Encrypt S */
		boolean loginParamterRSAUse = "true".equalsIgnoreCase(EnvConstants.getMailSetting("login.rsa.encrypt.use"));
		request.setAttribute("loginParamterRSAUse", String.valueOf(loginParamterRSAUse));
		if(loginParamterRSAUse){
			CryptoSession cryptoSession =  (CryptoSession)request.getSession().getAttribute(CryptoSession.CRYPTO_SESSION_KEY_NAME_PREFIX + "register");
			PrivateKey privateKey = cryptoSession.getPrivateKey();
			
			//TCUSTOM-2617
			if(StringUtils.isNotEmpty(ssn)){
				ssn = cryptoSession.decrypt(privateKey, ssn);
			}
			
			RSAPublicKeySpec publicSpec = cryptoSession.getPublicKeySpec();

			request.setAttribute(CryptoSession.PUBLIC_KEY_MODULUS_NAME_PREFIX + "register", publicSpec.getModulus().toString(16));
			request.setAttribute(CryptoSession.PUBLIC_KEY_EXPONENT_NAME_PREFIX + "register", publicSpec.getPublicExponent().toString(16));
		}
		
		return "success";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getMailDomain() {
		return mailDomain;
	}

	public void setMailDomain(String mailDomain) {
		this.mailDomain = mailDomain;
	}

	public String getEmpno() {
		return empno;
	}

	public void setEmpno(String empno) {
		this.empno = empno;
	}

	public List<MailDomainCodeVO> getPassCodeList() {
		return passCodeList;
	}

	public void setPassCodeList(List<MailDomainCodeVO> passCodeList) {
		this.passCodeList = passCodeList;
	}

	public int getMailDomainSeq() {
		return mailDomainSeq;
	}

	public void setMailDomainSeq(int mailDomainSeq) {
		this.mailDomainSeq = mailDomainSeq;
	}
	
	public String getInstallLocale() {
		return installLocale;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
		
}
