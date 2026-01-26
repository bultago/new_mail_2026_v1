package com.terracetech.tims.webmail.register.action;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import jakarta.servlet.http.Cookie;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.mailuser.vo.MailDomainCodeVO;
import com.terracetech.tims.webmail.mailuser.vo.MailDomainVO;
import com.terracetech.tims.webmail.util.StringUtils;

public class SearchPasswordAction extends BaseAction {
	
	private final static String PASSWORD_CODE = "30000";
	private MailUserManager mailUserManager = null;
	private SystemConfigManager systemManager = null;
	private List<MailDomainCodeVO> passCodeList = null;
	private String mailDomain = null;
	
	public SearchPasswordAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}
	
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	
	public void setSystemManager(SystemConfigManager systemManager) {
		this.systemManager = systemManager;
	}

	public String execute() throws Exception {
		
		Locale locale = I18nConstants.getBundleUserLocale(request);
		
		int mailDomainSeq = mailUserManager.searchMailDomainSeq(mailDomain);
		passCodeList = mailUserManager.getMailDomainCode(mailDomainSeq, PASSWORD_CODE, locale.getLanguage());
		
		Map<String, String> loginConfigList = systemManager.getLoginConfig();
		String domainType = loginConfigList.get("domain_type");
		String domainInputType = loginConfigList.get("domain_input_type");
		List<MailDomainVO> domainList = null;
	    String domain = null;
		if ("single".equalsIgnoreCase(domainType)) {
			domain = mailUserManager.readDefaultDomain();
			request.setAttribute("selectDomainName", domain);
		} else {
			if (!"input".equalsIgnoreCase(domainInputType)) {
				domainList = mailUserManager.readMailDomainList();
				request.setAttribute("domainList", domainList);
			}
		}
		
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {			
			if ("PSID".contains(cookie.getName())) {					
				cookie.setPath("/");
				cookie.setMaxAge(0);					
				response.addCookie(cookie);
				break;
			}
		}
		
		request.setAttribute("domainType", domainType);
		request.setAttribute("domainInputType", domainInputType);
		
		String asp = systemManager.getAspService("asp");
		String aspLogginPage = systemManager.getAspLoginPage("asp_login_page");
		if("enabled".equalsIgnoreCase(asp) && "on".equalsIgnoreCase(aspLogginPage)){
			String requestServerName = request.getServerName();
			if(domainList != null){
				String domainUrl;
				for(MailDomainVO vo : domainList){
					domainUrl = vo.getUrlAddress();
					if(domainUrl != null && !"".equals(domainUrl.trim())){
						if(requestServerName.equalsIgnoreCase(domainUrl)){
							mailDomain = vo.getMailDomain();
							break;
						} // end if
					} // end if
				} // end for
			} // end if
			
			if(mailDomain == null){
				if(requestServerName != null){
					int pointCount = StringUtils.getPointCount(requestServerName);
					if(pointCount == 0){
						mailDomain = domain;
					}else if(pointCount == 1){
						mailDomain = requestServerName;
					}else if(pointCount == 2 || pointCount == 3){
						mailDomain = requestServerName.substring(requestServerName.indexOf(".")+1);
					}else if(pointCount == 4){
						mailDomain = requestServerName.substring(requestServerName.indexOf(".")+1);
						mailDomain = mailDomain.substring(mailDomain.indexOf(".")+1);
					}else if(pointCount == 5){
						mailDomain = requestServerName.substring(requestServerName.indexOf(".")+1);
						mailDomain = mailDomain.substring(mailDomain.indexOf(".")+1);
						mailDomain = mailDomain.substring(mailDomain.indexOf(".")+1);
					}else if(pointCount == 6){
						mailDomain = requestServerName.substring(requestServerName.indexOf(".")+1);
						mailDomain = mailDomain.substring(mailDomain.indexOf(".")+1);
						mailDomain = mailDomain.substring(mailDomain.indexOf(".")+1);
						mailDomain = mailDomain.substring(mailDomain.indexOf(".")+1);
					}else{
						mailDomain = domain;
					}
					
				}else{
					mailDomain = domain;
				}
			}
			
			mailDomainSeq = mailUserManager.searchMailDomainSeq(mailDomain);
			passCodeList = mailUserManager.getMailDomainCode(mailDomainSeq, PASSWORD_CODE, locale.getLanguage());
			
			
			request.setAttribute("domainType", "asp");
			request.setAttribute("selectDomainName", mailDomain);

		}
		
		return "success";
	}

	public List<MailDomainCodeVO> getPassCodeList() {
		return passCodeList;
	}

	public void setPassCodeList(List<MailDomainCodeVO> passCodeList) {
		this.passCodeList = passCodeList;
	}

	public String getMailDomain() {
		return mailDomain;
	}

	public void setMailDomain(String mailDomain) {
		this.mailDomain = mailDomain;
	}
	
}
