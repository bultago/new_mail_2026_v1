package com.terracetech.tims.webmail.scheduler.action;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.json.simple.JSONObject;

import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.secure.crypto.SymmetricCrypt;
import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.AuthUser;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.webmail.webfolder.manager.WebFolderUtils;

public class SchedulerOutlookAuthAction extends BaseAction {

	private static final long serialVersionUID = 20091215L;
	private String email = null;
	private String pass = null;
	private String enc = null;
	
	private UserAuthManager userAuthManager = null;
	
	public SchedulerOutlookAuthAction(boolean authCheck) {
		setAuthCheck(authCheck);
	}
	
	public void setUserAuthManager(UserAuthManager userAuthManager) {
		this.userAuthManager = userAuthManager;
	}

	public String execute() throws Exception {
		
		Locale locale = I18nConstants.getUserLocale(request);
		I18nResources resource = new I18nResources(locale,"common");

		StringTokenizer st = null;
		AuthUser auth = null;
		
		String userId = null;
		String domain = null;
		String failMsg = null;
		
		request.getRemoteAddr();
		
		JSONObject jsonObject = new JSONObject();
		String msg = "";
		String authKey = "";
		
		try {
			if ("b".equalsIgnoreCase(enc)) {
				email = WebFolderUtils.base64Decode(email);
				pass = WebFolderUtils.base64Decode(pass);
			}
			if (email.indexOf("@") != -1) {
				st = new StringTokenizer(email, "@");
				if (st.countTokens() == 2) {
					userId = st.nextToken();
					domain = st.nextToken();
					
					auth= userAuthManager.validateUser(request.getSession(), userId, pass, domain);
					
					if (auth.isAuthSuccess()) {
						authKey = getAuthKey(email);
						jsonObject.put("authKey", authKey);
						msg = "Login Success";
						
					} else {
						String failMsgProperty = userAuthManager.getFailProperty(auth.getAuthResult());
						failMsg = resource.getMessage(failMsgProperty);
						msg = failMsg;
					}
				}
			} else {
				msg = resource.getMessage("auth.fail");
			}
		}catch (Exception e) {
			msg = resource.getMessage("error.default");
		}
		
		jsonObject.put("authMsg", msg);
		ResponseUtil.processResponse(response, jsonObject);
		
		return null;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public void setEnc(String enc) {
		this.enc = enc;
	}

	@SuppressWarnings("unused")
	private String getAuthKey(String auth) throws InvalidKeyException,
		NoSuchAlgorithmException, NoSuchPaddingException,
		NoSuchProviderException, UnsupportedEncodingException,
		IllegalBlockSizeException, BadPaddingException {
		if (StringUtils.isEmpty(auth))
			return "";
		
		return SecureUtil.encrypt(SymmetricCrypt.AES, "terrace-12345678", auth);
	}
}
