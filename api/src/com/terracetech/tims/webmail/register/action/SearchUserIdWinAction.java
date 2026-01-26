package com.terracetech.tims.webmail.register.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.mailuser.vo.MailDomainVO;
import com.terracetech.tims.webmail.util.StringUtils;

public class SearchUserIdWinAction extends BaseAction {

	private MailUserManager mailUserManager = null;
	private SystemConfigManager systemManager = null;
	private String installLocale = null;
	
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	
	public void setSystemManager(SystemConfigManager systemManager) {
		this.systemManager = systemManager;
	}
	
	public SearchUserIdWinAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}
	
	public String execute() throws Exception {
		
		installLocale = EnvConstants.getBasicSetting("setup.state");
		
		Map<String, String> loginConfigList = systemManager.getLoginConfig();
		String domainType = loginConfigList.get("domain_type");
		String domainInputType = loginConfigList.get("domain_input_type");
		
		Map<String, Integer> domainSeqMap = systemManager.getSsnNotUseDomainSeqMap();
		Map<String, Integer> domainInfoMap = null;
		
		String domain = mailUserManager.readDefaultDomain();
		List<MailDomainVO> domainList = null;
		if ("single".equalsIgnoreCase(domainType)) {		
			int defaultDomainSeq = mailUserManager.searchMailDomainSeq(domain);
			if (domainSeqMap != null) {
				domainInfoMap = new HashMap<String, Integer>();
				if (domainSeqMap.containsValue(defaultDomainSeq)) {
					domainInfoMap.put(domain, defaultDomainSeq);
				}
			}
			
			request.setAttribute("selectDomainName", domain);
		} else {
			if (!"input".equalsIgnoreCase(domainInputType)) {
				domainList = mailUserManager.readMailDomainList();
				if (domainSeqMap != null) {
					domainInfoMap = new HashMap<String, Integer>();
					for (int i=0; i<domainList.size(); i++) {
						if (domainSeqMap.containsValue(domainList.get(i).getMailDomainSeq())) {
							domainInfoMap.put(domainList.get(i).getMailDomain(), domainList.get(i).getMailDomainSeq());
						}
					}
					/*for (int i=domainList.size(); i > 0; i--) {
						if (domainSeqMap.containsValue(domainList.get(i-1).getMailDomainSeq())) {
							domainList.remove(i-1);
						}
					}*/
				}
				request.setAttribute("domainList", domainList);
			}
		}
		
		request.setAttribute("domainType", domainType);
		request.setAttribute("domainInputType", domainInputType);
		request.setAttribute("domainInfoMap", domainInfoMap);
		
		if ("jp".equalsIgnoreCase(installLocale)) {
			return "jpSuccess";
		}
		String asp = systemManager.getAspService("asp");
		String aspLogginPage = systemManager.getAspLoginPage("asp_login_page");
		if("enabled".equalsIgnoreCase(asp) && "on".equalsIgnoreCase(aspLogginPage)){
			String requestServerName = request.getServerName();
			String mailDomain = request.getParameter("domain");
			if(mailDomain == null){
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
			}
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
		
			return "success";
	}
	
	public String executePart() throws Exception {
		
		installLocale = EnvConstants.getBasicSetting("setup.state");
		
		Map<String, String> loginConfigList = systemManager.getLoginConfig();
		String domainType = loginConfigList.get("domain_type");
		String domainInputType = loginConfigList.get("domain_input_type");
		
		String domain = mailUserManager.readDefaultDomain();
		List<MailDomainVO> domainList = null;
		if ("single".equalsIgnoreCase(domainType)) {
		        domain = mailUserManager.readDefaultDomain();
			request.setAttribute("selectDomainName", domain);
		} else {
			if (!"input".equalsIgnoreCase(domainInputType)) {
			        domainList = mailUserManager.readMailDomainList();
				request.setAttribute("domainList", domainList);
			}
		}
		
		request.setAttribute("domainType", domainType);
		request.setAttribute("domainInputType", domainInputType);
		
		if ("jp".equalsIgnoreCase(installLocale)) {
			return "jpSuccess";
		}
		String asp = systemManager.getAspService("asp");
		String aspLogginPage = systemManager.getAspLoginPage("asp_login_page");
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
		return "success";
	}
	
	public String getInstallLocale() {
		return installLocale;
	}
}
