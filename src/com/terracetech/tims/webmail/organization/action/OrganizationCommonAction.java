package com.terracetech.tims.webmail.organization.action;

import java.util.List;
import java.util.Locale;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.mailuser.vo.MailDomainCodeVO;

public class OrganizationCommonAction extends BaseAction{
	
	private static final long serialVersionUID = 20081229L;
	
	private MailUserManager mailUserManager = null;
	
	private List<MailDomainCodeVO> titleCodeList;
	
	private List<MailDomainCodeVO> classCodeList;

	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	
	public List<MailDomainCodeVO> getTitleCodeList() {
		return titleCodeList;
	}

	public List<MailDomainCodeVO> getClassCodeList() {
		return classCodeList;
	}

	/**
	 * class 100 직급
	 * title 101 직위
	 */
	public String loadPage() throws Exception {
		
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		String installLocale = EnvConstants.getBasicSetting("setup.state");
		String userLocale = user.get(User.LOCALE);
		String orgLocale = userLocale;
		if ("jp".equals(installLocale)) {
			orgLocale = "ko".equals(userLocale) ? "en" : userLocale;
		} else if ("ko".equals(installLocale)) {
			orgLocale = "jp".equals(userLocale) ? "en" : userLocale;
		}
		
		titleCodeList = mailUserManager.getMailDomainCode(domainSeq, "101", orgLocale);
		classCodeList = mailUserManager.getMailDomainCode(domainSeq, "100", orgLocale);
		
		return "load";
	}
}
