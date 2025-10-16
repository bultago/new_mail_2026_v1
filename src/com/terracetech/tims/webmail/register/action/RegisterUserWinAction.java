package com.terracetech.tims.webmail.register.action;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.mailuser.vo.MailDomainVO;
import com.terracetech.tims.webmail.util.CryptoSession;
import com.terracetech.tims.webmail.util.StringUtils;

public class RegisterUserWinAction extends BaseAction {

	private MailUserManager mailUserManager = null;
	private SystemConfigManager systemManager = null;
	private String installLocale = null;
	
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	
	public void setSystemManager(SystemConfigManager systemManager) {
		this.systemManager = systemManager;
	}

	public RegisterUserWinAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}
	
	public String execute() throws Exception {
		
		installLocale = EnvConstants.getBasicSetting("setup.state");
		
		Map<String, String> loginConfigList = systemManager.getLoginConfig();
		String domainType = loginConfigList.get("domain_type");
		String domainInputType = loginConfigList.get("domain_input_type");
		
		Map<String, Integer> domainSeqMap = systemManager.getSsnNotUseDomainSeqMap();
		Map<String, Integer> domainInfoMap = null;

                List<MailDomainVO> domainList = null;
                String domain = null;
		if ("single".equalsIgnoreCase(domainType)) {
		        domain = mailUserManager.readDefaultDomain();
			int defaultDomainSeq = mailUserManager.searchMailDomainSeq(domain);
			if (domainSeqMap != null) {
				domainInfoMap = new HashMap<String, Integer>();
				if (domainSeqMap.containsValue(defaultDomainSeq)) {
					domainInfoMap.put(domain, defaultDomainSeq);
				}
			}
			request.setAttribute("selectDomainName", domain);
		} 
		else {
			if (!"input".equalsIgnoreCase(domainInputType)) {
			        domainList = mailUserManager.readMailDomainList();
				if (domainSeqMap != null) {
					domainInfoMap = new HashMap<String, Integer>();
					for (int i=0; i<domainList.size(); i++) {
						if (domainSeqMap.containsValue(domainList.get(i).getMailDomainSeq())) {
							domainInfoMap.put(domainList.get(i).getMailDomain(), domainList.get(i).getMailDomainSeq());
						}
					}
				}
				request.setAttribute("domainList", domainList);
			}
		}
		String asp = systemManager.getAspService("asp");
		String aspLogginPage = systemManager.getAspLoginPage("asp_login_page");
		if(!"enabled".equalsIgnoreCase(asp) && "on".equalsIgnoreCase(aspLogginPage)){
			request.setAttribute("domainType", domainType);
			request.setAttribute("domainInputType", domainInputType);
		}
		
		request.setAttribute("domainInfoMap", domainInfoMap);
		
		if ("jp".equalsIgnoreCase(installLocale)) {
			return "jpSuccess";
		}
		
		if("enabled".equalsIgnoreCase(asp) && "on".equalsIgnoreCase(aspLogginPage)){
			String requestServerName = request.getServerName();
			String mailDomain = null;
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
			
			request.setAttribute("domainType", "asp");
			request.setAttribute("selectDomainName", mailDomain);

		}

		boolean loginParamterRSAUse = "true".equalsIgnoreCase(EnvConstants.getMailSetting("login.rsa.encrypt.use"));
		request.setAttribute("loginParamterRSAUse", String.valueOf(loginParamterRSAUse));
		if (loginParamterRSAUse) {
			CryptoSession cryptoSession = new CryptoSession();
			/* Login ID, PW Parameter RSA Encrypt */
			HttpSession session = request.getSession();
			session.setAttribute(CryptoSession.CRYPTO_SESSION_KEY_NAME_PREFIX + "register", cryptoSession);

			RSAPublicKeySpec publicSpec = cryptoSession.getPublicKeySpec();

			request.setAttribute(CryptoSession.PUBLIC_KEY_MODULUS_NAME_PREFIX + "register", publicSpec.getModulus().toString(16));
			request.setAttribute(CryptoSession.PUBLIC_KEY_EXPONENT_NAME_PREFIX + "register", publicSpec.getPublicExponent().toString(16));
		}

		return "success";
	}
	
	public String getInstallLocale() {
		return installLocale;
	}
}
