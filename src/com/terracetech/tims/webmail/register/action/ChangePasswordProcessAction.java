package com.terracetech.tims.webmail.register.action;

import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.PrivateKey;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;

import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.secure.crypto.SymmetricCrypt;
import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.MakeMessage;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.util.StringUtils;

public class ChangePasswordProcessAction extends BaseAction {
	
	private MailUserManager mailUserManager = null;
	private SettingManager settingManager = null;
	private int mailDomainSeq = 0;
	private int mailUserSeq = 0;
	private String mailPassword = null;
	
	public ChangePasswordProcessAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}
	
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	
	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}
	
	public String execute() throws Exception {
		
		I18nResources resource = getMessageResource("common");
		
		response.setHeader("Content-Type", "text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();	
		boolean isSuccess = false;
		
		String closeJQpopupFunction = request.getParameter("closeJQpopupFunction") == null ? "parent.modalPopupFindPassClose();\n parent.jQpopupClear();" : request.getParameter("closeJQpopupFunction");
		
		try {
			/* Login ID, PW Parameter RSA Encrypt S */
			boolean loginParamterRSAUse = "true".equalsIgnoreCase(EnvConstants.getMailSetting("login.rsa.encrypt.use"));
			if(loginParamterRSAUse){
				PrivateKey privateKey = (PrivateKey) request.getSession().getAttribute("__rsaChangePasswordPrivateKey__");
				request.getSession().removeAttribute("__rsaChangePasswordPrivateKey__"); // 키의 재사용을 막는다. 항상 새로운 키를 받도록 강제.
		        if (privateKey == null) {
		            //throw new RuntimeException("암호화 비밀키 정보를 찾을 수 없습니다.");
		        }
		        String securedPassword = request.getParameter("securedPassword");
		        try {
		        	mailPassword = decryptRsa(privateKey, securedPassword);
		        } catch (Exception ex) {
		            throw new ServletException(ex.getMessage(), ex);
		        }
			}
			/* Login ID, PW Parameter RSA Encrypt E */
			
			if (StringUtils.isNotEmpty(mailPassword)) {
				String cookieValue = null;
				String compareId = null;
				String compareDomain = null;
				int compareDomainSeq = 0;
				int compareUserSeq = 0;
				
				StringTokenizer st = null;
				
				Cookie[] cookies = request.getCookies();
				
				if (cookies != null) {
					for (int i=0; i<cookies.length; i++) {
						if ("PSID".equalsIgnoreCase(cookies[i].getName())) {
							cookieValue = cookies[i].getValue();
							cookieValue = URLDecoder.decode(cookieValue);
							cookieValue = SecureUtil.decrypt(SymmetricCrypt.AES, "terrace-12345678", cookieValue);
							if (cookieValue.indexOf("@") != -1) {
								st = new StringTokenizer(cookieValue, "@");
								compareId = st.nextToken();
								compareDomain = st.nextToken();
								
								compareDomainSeq = mailUserManager.searchMailDomainSeq(compareDomain);
								if (mailDomainSeq == compareDomainSeq) {
									compareUserSeq = mailUserManager.readUserSeq(compareId, compareDomainSeq);
									if (mailUserSeq == compareUserSeq) {
										settingManager.setMyPassword(mailDomainSeq, mailUserSeq, mailPassword);
										isSuccess = true;
									}
								}
							}
							cookies[i].setMaxAge(0);
							response.addCookie(cookies[i]);
							break;
						}
					}
				}
			}
			
			if (!isSuccess) {
				throw new Exception();
			}
			
			out.print(MakeMessage.printAlertCloseOnlyWithLayer(resource.getMessage("register.018"), closeJQpopupFunction));
		    out.flush();
		}
		catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			out.print(MakeMessage.printAlertCloseOnlyWithLayer(resource.getMessage("register.019"), closeJQpopupFunction));
			out.flush();
		}
		return null;
	}

	public int getMailDomainSeq() {
		return mailDomainSeq;
	}

	public void setMailDomainSeq(int mailDomainSeq) {
		this.mailDomainSeq = mailDomainSeq;
	}

	public int getMailUserSeq() {
		return mailUserSeq;
	}

	public void setMailUserSeq(int mailUserSeq) {
		this.mailUserSeq = mailUserSeq;
	}

	public String getMailPassword() {
		return mailPassword;
	}

	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}
	
	private String decryptRsa(PrivateKey privateKey, String securedValue) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        byte[] encryptedBytes = hexToByteArray(securedValue);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의.
        return decryptedValue;
    }
	
	/**
     * 16진 문자열을 byte 배열로 변환한다.
     */
	private static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() % 2 != 0) {
            return new byte[]{};
        }

        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            byte value = (byte)Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[(int) Math.floor(i / 2)] = value;
        }
        return bytes;
    }
}
