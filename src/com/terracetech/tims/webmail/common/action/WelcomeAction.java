/**
 * 
 */
package com.terracetech.tims.webmail.common.action;

import java.security.Security;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.LogoManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.common.vo.CommonLogoVO;
import com.terracetech.tims.webmail.common.vo.LogoVO;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.mailuser.vo.MailDomainVO;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.util.BrowserUtil;
import com.terracetech.tims.webmail.util.CookieUtils;
import com.terracetech.tims.webmail.util.StringUtils;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

/**
 * @author ������
 *
 */
public class WelcomeAction extends BaseAction {	
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	private static final long serialVersionUID = 824137111108651791L;
	
	public Logger log = Logger.getLogger(this.getClass());
	
	private MailUserManager mailUserManager = null;
	private SettingManager userSettingManager = null;
	private SystemConfigManager systemManager = null;
	private LogoManager logoManager = null;
	
	private String url;
	
	public WelcomeAction(boolean authcheck) {
		setAuthCheck(authcheck);
	}
	
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	public void setUserSettingManager(SettingManager userSettingManager) {
		this.userSettingManager = userSettingManager;
	}	
	public void setSystemManager(SystemConfigManager systemManager) {
		this.systemManager = systemManager;
	}
	public void setLogoManager(LogoManager logoManager) {
		this.logoManager = logoManager;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String load() throws Exception{
		LogManager.printElapsedTime("welcome", "WelcomeAction START");
		
		String forward = "main";		
		String timeout = request.getParameter("timeout");
		String stime = request.getParameter("stime");
		String debugUse = request.getParameter("debug");
		boolean isDebugUse = ("enable".equals(debugUse))?true:false;
		timeout = (timeout != null)?timeout:"off";
		
		HttpSession session = request.getSession();
		
		log.debug("WelcomeAction Step-1 :" + user);
		String installLocale = EnvConstants.getBasicSetting("setup.state");
		//System.out.println("###########################################");
		String agent = request.getHeader("user-agent");
		//System.out.println(agent);
		String mailMode = null;
		String mobileMailUse = systemManager.getMobileMailConfig();
		boolean isMobileMailUse = "enabled".equals(mobileMailUse);
		boolean isMobile = BrowserUtil.isMoblieBrowser(agent);
		if(isMobile){
			Object object = session.getAttribute("mailMode");
			if (object!= null) {
				mailMode = (String)object;
			}
		} else {
			CookieUtils cookieUtil = new CookieUtils(request);
			mailMode = cookieUtil.getValue("TSMODE");
		}
		//System.out.println("isMobieBrowser : "+ isMobile);
		//System.out.println("###########################################");
		
		LogManager.printElapsedTime("welcome", "WelcomeAction Cookie Check");
		if (user == null) {
			LogManager.printElapsedTime("welcome", "WelcomeAction.checkUser1");
			
			String defaultDomain = mailUserManager.readDefaultDomain();
			
			if (isMobileMailUse && 
					("mobile".equalsIgnoreCase(mailMode) || 
					(isMobile && !"pc".equalsIgnoreCase(mailMode)))) {
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("attachPath", context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir"));
				paramMap.put("attachUrl", EnvConstants.getAttachSetting("attach.url"));
				paramMap.put("localUrl", request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort());
				paramMap.put("logoType", "mobile");
				Map<String, CommonLogoVO> commonLogoList = logoManager.readCommonLogoList(paramMap);

				request.setAttribute("installLocale", installLocale);
				request.setAttribute("commonLogoList", commonLogoList);
				forward = "mobileLogin";
			} 
			else {
				Map<String, String> loginConfigList = systemManager.getLoginConfig();
				String domainType = loginConfigList.get("domain_type");
				String domainInputType = loginConfigList.get("domain_input_type");
				
				String domain = mailUserManager.readDefaultDomain();
				int defaultDomainSeq = mailUserManager.searchMailDomainSeq(domain);
				String noticeContent = mailUserManager.getNoticeContent(defaultDomainSeq);
				
				log.debug("WelcomeAction.domainType : " + domainType);
				log.debug("WelcomeAction.domainInputType : " + domainInputType);
				
				Map<String, Integer> domainSeqMap = systemManager.getSsnNotUseDomainSeqMap();
				
				List<MailDomainVO> domainList = null;
					
				if ("single".equalsIgnoreCase(domainType)) {
					if (domainSeqMap != null) {
						if (domainSeqMap.containsValue(defaultDomainSeq)) {
							request.setAttribute("ssnNotUse", true);
						}
					}
					request.setAttribute("selectDomainName", domain);
				}
				else {
					if (!"input".equalsIgnoreCase(domainInputType)) {
						domainList = mailUserManager.readMailDomainList();
						if (domainSeqMap != null) {
							int count = 0;
							for (int i=0; i<domainList.size(); i++) {
								if (domainSeqMap.containsValue(domainList.get(i).getMailDomainSeq())) {
									count++;
								}
							}
							if (count == domainList.size()) {
								request.setAttribute("ssnNotUse", true);
							}
						}
						request.setAttribute("domainList", domainList);
					}
				}

				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("attachPath", context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir"));
				paramMap.put("attachUrl", EnvConstants.getAttachSetting("attach.url"));
				paramMap.put("localUrl", request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort());
				
				Map<String, CommonLogoVO> commonLogoList = logoManager.readCommonLogoList(paramMap);
				
				request.setAttribute("commonLogoList", commonLogoList);
				request.setAttribute("noticeContent", noticeContent);
				request.setAttribute("loginConfigList", loginConfigList);
				String loginPage = systemManager.getLoginPage(request.getServerName());
				
				if(StringUtils.isNotEmpty(loginPage)){
					request.setAttribute("url", loginPage);
					setUrl(loginPage);
					if(loginPage.startsWith("http"))
					{
						forward = "redirect";
					}else{
						forward = "forward";	
					}
				} else {
					String asp = systemManager.getAspService("asp");
					String aspLogginPage = systemManager.getAspLoginPage("asp_login_page");
					if("enabled".equalsIgnoreCase(asp) && "on".equalsIgnoreCase(aspLogginPage)){
						String requestServerName = request.getServerName();
						String mailDomain = null;
						int mailDomainSeq = 0;
						if(domainList != null){
							String domainUrl;
							for (MailDomainVO vo : domainList){
								domainUrl = vo.getUrlAddress();
								if(domainUrl != null && !"".equals(domainUrl.trim())){
									if(requestServerName.equalsIgnoreCase(domainUrl)){
										mailDomain = vo.getMailDomain();
										mailDomainSeq = vo.getMailDomainSeq();
										break;
									} // end if
								} // end if								
							} // end for
						} // end if
						
						
						if(mailDomain == null){
							if(requestServerName != null){
								int pointCount = StringUtils.getPointCount(requestServerName);
								if(pointCount == 0){
									mailDomain = defaultDomain;
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
									mailDomain = defaultDomain;
								}
								
							} else {
								mailDomain = defaultDomain;
							}

							/**
							 * 2012.02.06 ��μ� 
							 * 
							 * �⺻ �������� ������ �ΰ? �����ϸ� ��� �������� �ΰ? ����Ǵ� ���� ���� 
							 */
							mailDomainSeq = mailUserManager.searchMailDomainSeq(mailDomain);
						}
						
						LogoVO logoVO = null;
						if (mailDomainSeq > 0)
						    logoVO = logoManager.getLogoInfo(mailDomainSeq, paramMap);
						else
						    logoVO = logoManager.getLogoInfo(defaultDomainSeq, paramMap);
						
						request.setAttribute("logoVO", logoVO);
						request.setAttribute("mailDomain", mailDomain);
						forward = "aspLogin";
					}
					else
						forward = "login";
				}
			}
			request.setAttribute("timeout", timeout);		
			request.setAttribute("sessionTime", stime);
			request.setAttribute("mobileMailUse", isMobileMailUse);
			request.setAttribute("defaultDomain",defaultDomain);
			request.setAttribute("installLocale", installLocale);
			
			/* Login ID, PW Parameter RSA Encrypt S */
			boolean loginParamterRSAUse = "true".equalsIgnoreCase(EnvConstants.getMailSetting("login.rsa.encrypt.use"));
			request.setAttribute("loginParamterRSAUse", String.valueOf(loginParamterRSAUse));
			if(loginParamterRSAUse){
				KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
	            generator.initialize(1024);
	            
	            KeyPair keyPair = generator.genKeyPair();
	            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

	            PublicKey publicKey = keyPair.getPublic();
	            PrivateKey privateKey = keyPair.getPrivate();

	            // 세션에 공개키의 문자열을 키로하여 개인키를 저장한다.
	            session.setAttribute("__rsaPrivateKey__", privateKey);

	            // 공개키를 문자열로 변환하여 JavaScript RSA 라이브러리 넘겨준다.
	            RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);

	            String publicKeyModulus = publicSpec.getModulus().toString(16);
	            String publicKeyExponent = publicSpec.getPublicExponent().toString(16);

	            request.setAttribute("publicKeyModulus", publicKeyModulus);
	            request.setAttribute("publicKeyExponent", publicKeyExponent);
			}
			/* Login ID, PW Parameter RSA Encrypt E */
			
		} else {
			LogManager.printElapsedTime("welcome", "WelcomeAction.checkUser2");
			
			if (isMobileMailUse && 
					("mobile".equalsIgnoreCase(mailMode) || 
					(isMobile && !"pc".equalsIgnoreCase(mailMode)))) {
				forward = "mobileIntro";
			}
			else {
				int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
				Map<String, String> menuMap = systemManager.getTopAndLeftMenuUse(mailDomainSeq);
				
				session.setAttribute("autoForwardMenu", menuMap.get("autoForwardMenu")); 		//TCUSTOM-3763 20180129				
				
				Object o = session.getAttribute("SSO_AUTH");
				if(o != null && "T".equals((String)o)){
					session.setAttribute("topMenuUse", menuMap.get("ssotopmenu"));
					session.removeAttribute("SSO_AUTH");
				} else {					
					session.setAttribute("topMenuUse", menuMap.get("nortopmenu"));
				}
				session.setAttribute("leftMenuUse", menuMap.get("leftmenu"));				
				session.setAttribute("debugUse", (isDebugUse)?"enable":"disable");	
				
				int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
				UserEtcInfoVO userSettingVo = userSettingManager.readUserEtcInfo(userSeq);
				log.debug("WelcomeAction.userSettingVo : " + userSettingVo);
				
				if(userSettingVo==null) return "intro";
				
				boolean isHtmlMode = "html".equals(userSettingVo.getRenderMode());
				
				String afterLogin = userSettingVo.getAfterLogin();
				log.debug("WelcomeAction.afterLogin : " + afterLogin);
				
				if(afterLogin != null){				
					forward = afterLogin.toLowerCase();
					if(StringUtils.isEmpty(forward))
						forward = "domain";
				} else {
					forward = "intro";
				}
				
				if (isHtmlMode) forward = "s"+forward;
			}
		}
		
		request.getSession().removeAttribute("dormantUser");
		log.debug("WelcomeAction.forward : " + forward);
		LogManager.printElapsedTime("welcome", "WelcomeAction END");
		return forward;		
	}
}
