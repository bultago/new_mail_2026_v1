package com.terracetech.tims.webmail.common.action;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.bouncycastle.util.encoders.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.secure.crypto.SymmetricCrypt;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mail.ibean.MailFolderBean;
import com.terracetech.tims.webmail.mail.ibean.MailSortMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.AuthUser;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.ResponseUtil;

@SuppressWarnings("all")
public class AlimInfoAction extends BaseAction {

	private static final long serialVersionUID = 4425082793091048797L;
	private static final int USER_NOT_FOUND = -20;
	private static final int SYSTEM_ERROR = -400;
	private static final int SUCCESS = 1;
	private String alimCryptMethod = EnvConstants.getBasicSetting("alim.crypt.method");
	
	public AlimInfoAction() {
		setAuthCheck(false);
	}
	
	private UserAuthManager userAuthManager = null;
	private MailUserManager mailUserManager = null;
	private MailManager mailManager = null;
	private SystemConfigManager systemManager = null;
	
	public void setUserAuthManager(UserAuthManager userAuthManager) {
		this.userAuthManager = userAuthManager;
	}
	
	
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	
	public void setSystemManager(SystemConfigManager systemManager) {
		this.systemManager = systemManager;
	}


	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}


	public String executeSystemInfo() throws Exception{		
		JSONObject jObj = new JSONObject();
		jObj.put("cryptMethod", alimCryptMethod);
		jObj.put("code", SUCCESS);
		ResponseUtil.alimCryptResponse(response, jObj, null);		
		return null;
	}
	
	public String executeAuth() throws Exception{		
		JSONObject jObj = new JSONObject();
		try {
			Map<String, String> paramMap = parseSecureRequest();
			String email = paramMap.get("email");
			String password = paramMap.get("password");
			String[] emailStrs = email.split("@");
			String id = emailStrs[0];
			String domain = emailStrs[1];		
			AuthUser auth = userAuthManager.validateUser(id, password, domain);
			
			
			int result = auth.getAuthResult();
			jObj.put("code", auth.getAuthResult());
			if(UserAuthManager.SUCCESS == result){
				jObj.put("domainSeq", auth.get(User.MAIL_DOMAIN_SEQ));
				jObj.put("userSeq", auth.get(User.MAIL_USER_SEQ));
				jObj.put("locale", auth.get(User.USER_LOCALE));
			}
		} catch (RuntimeException re) {
			jObj.put("code", SYSTEM_ERROR);
		} catch (Exception e) {
			jObj.put("code", SYSTEM_ERROR);
		}
		
		ResponseUtil.alimCryptResponse(response, jObj, alimCryptMethod);		
		return null;
	}
	
	public String executeMailInfo() throws Exception{
		JSONObject jObj = new JSONObject();
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		try {
			Map<String, String> paramMap = parseSecureRequest();
			String domainSeqStr = paramMap.get("domainSeq");
			String userSeqStr = paramMap.get("userSeq");
			int domainSeq = Integer.parseInt(domainSeqStr);
			int userSeq = Integer.parseInt(userSeqStr);
			String jobType = paramMap.get("type");	
			
			User user = null;
			try{
				user = mailUserManager.readUserOtherInfo(userSeq, domainSeq);
			}catch (Exception e) {
				jObj.put("code", USER_NOT_FOUND);
			}	
			
			store = factory.connect(false, null, request.getRemoteAddr(), user);			
			if(jobType.equals("CNT")){
				TMailFolder folder = store.getFolder("Inbox");
				Map statusMap = folder.xstatus(new String[]{"RECENT", "UNSEEN"});
				long resent = Long.parseLong((String)(((Map)statusMap.get("Inbox")).get("RECENT")));
				long unseen = Long.parseLong((String)(((Map)statusMap.get("Inbox")).get("UNSEEN")));
				jObj.put("resentCnt", resent);
				jObj.put("unseenCnt", unseen);							
			} else if(jobType.equals("UNSEEN") || jobType.equals("RESENT")){				
				Locale locale = new Locale(user.get(User.USER_LANGUAGE));
				mailManager.setProcessResource(store, new I18nResources(locale));
				char flagType = (jobType.equals("UNSEEN"))?'U':'N';
				
				MessageSortInfoBean sortBean = new MessageSortInfoBean();
				sortBean.setSortBy("arrival");
				sortBean.setSortDir("desc");
				sortBean.setPage("1");
				sortBean.setPageBase("30");
				sortBean.setSearchFlag(flagType);
				
				MailSortMessageBean[] messageBeans = mailManager.getXSortMessageBeans("Inbox", null, sortBean);
				JSONArray messageList = new JSONArray();
				if(messageBeans != null){
					for (int i = 0; i < messageBeans.length; i++) {
						messageBeans[i].setLocale(locale);
						messageList.add(messageBeans[i].toJsonSimple());
					}
				}
				jObj.put("mailList",messageList);
			}		
			
			jObj.put("code", SUCCESS);
		} catch (RuntimeException re) {
			jObj.put("code", SYSTEM_ERROR);
		} catch (Exception e) {
			jObj.put("code", SYSTEM_ERROR);
		} finally {
			if(store !=null && store.isConnected())
				store.close();		
		}
		
		ResponseUtil.alimCryptResponse(response, jObj, alimCryptMethod);		
		return null;	
	}
	
	public String executeGoMail() throws Exception{
		Map<String, String> paramMap = parseSecureRequest();
		String domainSeqStr = paramMap.get("domainSeq");
		String userSeqStr = paramMap.get("userSeq");
		int domainSeq = Integer.parseInt(domainSeqStr);
		int userSeq = Integer.parseInt(userSeqStr);
		
		String attachPath = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
		String strLocalhost = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort();
		
		User user = mailUserManager.readUserOtherInfo(userSeq, domainSeq);		
		AuthUser auth = userAuthManager.validateSso(user.get(User.MAIL_UID), user.get(User.MAIL_DOMAIN));
		
		Map<String, String> authParamMap = new HashMap<String, String>();
		authParamMap.put("attachPath", attachPath);
		authParamMap.put("attachUrl", EnvConstants.getAttachSetting("attach.url"));
		authParamMap.put("localUrl", strLocalhost);
		authParamMap.put("cookieAlgorithm", systemManager.getCryptMethod());
		
		String sessionTime = systemManager.getSessionTimeConfig();
		auth.getUser().put(User.SESSION_CHECK_TIME, sessionTime);
		userAuthManager.doLoginProcess(request, response, auth.getUser(),authParamMap);
		
		request.getSession().setAttribute(EnvConstants.COOKIE_ALGORITHM_METHOD, systemManager.getCryptMethod());
		request.setAttribute("user", auth.getUser());//변수 상수화		
		request.setAttribute("url", "/dynamic/mail/mailCommon.do?workName=golist&folder=Inbox");		
		return "success";
	}	
	
	private Map<String, String> parseSecureRequest() throws Exception{
		String encParam = request.getParameter("param");
		String decodeParam = null; 
		encParam = encParam.replaceAll(",","+");
		encParam = encParam.replaceAll("-","/");
		encParam = encParam.replaceAll("@","=");			
		
		if(alimCryptMethod != null && !"normal".equalsIgnoreCase(alimCryptMethod)){
			decodeParam = SecureUtil.decrypt(alimCryptMethod, EnvConstants.ENCRYPT_KEY, encParam);
		} else {
			decodeParam = new String(Base64.decode(encParam));
		}
		
		Map<String, String> map = new HashMap<String, String>();
		String[] paramList = decodeParam.split("&");
		String[] params = null;
		for (int i = 0; i < paramList.length; i++) {
			params = paramList[i].split("=");
			map.put(params[0], params[1]);
		}		
		
		return map;
	}
}
