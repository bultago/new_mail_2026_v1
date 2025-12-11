package com.terracetech.tims.webmail.register.action;

import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.http.Cookie;

import org.json.simple.JSONObject;

import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.secure.crypto.SymmetricCrypt;
import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.mailuser.vo.MailDomainCodeVO;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class SearchPasswordProcessAction extends BaseAction {

	private static final long serialVersionUID = -5214109193464174853L;
	private MailUserManager mailUserManager = null;
	private String userId = null;
	private String passCode = null;
	private String passAnswer = null;
	private String mailDomain = null;

	public SearchPasswordProcessAction(boolean authcheck) {
		setAuthCheck(authcheck);
	}

	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}

	@Override
	public String execute() throws Exception {

		JSONObject jsonObj = new JSONObject();
		boolean isExist = false;
		int mailDomainSeq = 0;
		int mailUserSeq = 0;
		String email = null;
		String encodeEmail = null;

		String PASSWORD_CODE = "30000"; //201704
		Locale locale = null; //201704
		List<MailDomainCodeVO> passCodeList = null; //201704
		boolean isPassCode = false;

		try {

			email = userId+"@"+mailDomain;
			mailDomainSeq = mailUserManager.searchMailDomainSeq(mailDomain);

			//201704 S
			locale = I18nConstants.getBundleUserLocale(request);
			passCodeList = mailUserManager.getMailDomainCode(mailDomainSeq, PASSWORD_CODE, locale.getLanguage());
			if(StringUtils.isNotEmpty(passCode) && StringUtils.isNotEmpty(passAnswer)){
				if(passCodeList != null && passCodeList.size() > 0){
					for(MailDomainCodeVO passCodeVO : passCodeList){
						if(StringUtils.isNotEmpty(passCodeVO.getCode())){
							if(passCodeVO.getCode().equals(passCode)){
								isPassCode = true;
								break;
							}
						}
					}
				}

				if(isPassCode){
					mailUserSeq = mailUserManager.searchPassword(userId, passCode, passAnswer, mailDomainSeq);
				}
			} else {
				mailUserSeq = 0;
				isExist = false;
			}
			//201704 E



			if (mailUserSeq > 0)  {
				isExist = true;
				encodeEmail = SecureUtil.encrypt(SymmetricCrypt.AES, "terrace-12345678", email);

				String cookieDomain = EnvConstants.getBasicSetting("cookie.domain");

				encodeEmail = URLEncoder.encode(encodeEmail, "UTF-8");
				Cookie cookie = new Cookie("PSID", encodeEmail);
				if (StringUtils.isNotEmpty(cookieDomain)) {
					cookie.setDomain(cookieDomain);
				}
				cookie.setPath("/");
				response.addCookie(cookie);
			}

		}catch (Exception e) {

		}
		jsonObj.put("isExist", isExist);
		jsonObj.put("mailUserSeq", mailUserSeq);
		jsonObj.put("mailDomainSeq", mailDomainSeq);

		ResponseUtil.processResponse(response, jsonObj);

		return null;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassCode() {
		return passCode;
	}

	public void setPassCode(String passCode) {
		this.passCode = passCode;
	}

	public String getPassAnswer() {
		return passAnswer;
	}

	public void setPassAnswer(String passAnswer) {
		this.passAnswer = passAnswer;
	}

	public String getMailDomain() {
		return mailDomain;
	}

	public void setMailDomain(String mailDomain) {
		this.mailDomain = mailDomain;
	}

}
