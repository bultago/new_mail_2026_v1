package com.terracetech.tims.webmail.register.action;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.terracetech.secure.crypto.PasswordEty;
import com.terracetech.secure.crypto.PasswordUtil;
import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.secure.crypto.SymmetricCrypt;
import com.terracetech.secure.exception.EncodingException;
import com.terracetech.secure.policy.AllowPolicy;
import com.terracetech.secure.policy.Policy;
import com.terracetech.secure.policy.SecurePolicy;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.ExtPartConstants;
import com.terracetech.tims.webmail.common.MakeMessage;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.exception.InvalidParameterException;
import com.terracetech.tims.webmail.exception.SaveFailedException;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.mailuser.manager.SettingSecureManager;
import com.terracetech.tims.webmail.mailuser.vo.MailUserInfoVO;
import com.terracetech.tims.webmail.mailuser.vo.MailUserVO;
import com.terracetech.tims.webmail.plugin.pki.PKIAuthParamBean;
import com.terracetech.tims.webmail.plugin.pki.PKIAuthResultBean;
import com.terracetech.tims.webmail.plugin.pki.PKIManager;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.util.CryptoSession;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class SaveUserInfoAction extends BaseAction {

	private static final long serialVersionUID = 20090427L;

	private MailUserManager mailUserManager = null;

	private int mailDomainSeq = 0;
	private String mailUid = null;
	private String ssn = null;
	private String empNo = null;
	private String firstName = null;
	private String middleName = null;
	private String lastName = null;
	private String userName = null;
	private String mobileNo = null;
	private String mailPassword = null;
	private String passQuestionCode = null;
	private String passAnswer = null;
	private String birthday = null;

	private String homePostalCode = null;
	private String homeCountry = null;
	private String homeState = null;
	private String homeCity = null;
	private String homeStreet = null;
	private String homeBasicAddress = null;
	private String homeExtAddress = null;
	private String homeTel = null;
	private String homeFax = null;
	private String privateHomepage = null;

	private SettingSecureManager settingSecureManager = null;

	private SystemConfigManager systemManager = null;

	public void setSettingSecureManager(SettingSecureManager settingSecureManager) {
		this.settingSecureManager = settingSecureManager;
	}

	public void setSystemManager(SystemConfigManager systemManager) {
		this.systemManager = systemManager;
	}

	public SaveUserInfoAction(boolean authcheck) {
		setAuthCheck(authcheck);
	}

	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	@Override
	public String execute() throws Exception {
		 if (!checkPAID()) {
             return "paidError";
         }
		I18nResources resource = getMessageResource("common");
		response.setHeader("Content-Type", "text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		mailDomainSeq = Integer.parseInt(request.getParameter("mailDomainSeq"));

		mailUid = request.getParameter("mailUid");
		ssn = request.getParameter("ssn");
		empNo = request.getParameter("empNo");
		firstName = request.getParameter("firstName");
		middleName = request.getParameter("middleName");
		lastName = request.getParameter("lastName");
		userName = request.getParameter("userName");
		mobileNo = request.getParameter("mobileNo");
		mailPassword = request.getParameter("mailPassword");
		String closeJQpopupFunction = request.getParameter("closeJQpopupFunction") == null ? "parent.modalPopupForRegisterUserClose();\n parent.jQpopupClear();" : request.getParameter("closeJQpopupFunction");

		/*주민등록번호 유효성검사*/
		String realSsn = (String)request.getSession().getAttribute("registssn");
		boolean loginParamterRSAUse = "true".equalsIgnoreCase(EnvConstants.getMailSetting("login.rsa.encrypt.use"));
		if(loginParamterRSAUse){
			try {
				CryptoSession cryptoSession =  (CryptoSession)request.getSession().getAttribute(CryptoSession.CRYPTO_SESSION_KEY_NAME_PREFIX + "register");
				PrivateKey privateKey = cryptoSession.getPrivateKey();
				if(StringUtils.isNotEmpty(realSsn) && StringUtils.isNotEmpty(ssn)){
					realSsn = decryptRsa(privateKey, realSsn);
					ssn = decryptRsa(privateKey, ssn);
				}
				mailPassword = decryptRsa(privateKey, request.getParameter("securedPassword"));
			} catch(Exception ex) {
				throw new ServletException(ex.getMessage(), ex);
			}
		}

		///TCUSTOM-2617
		if(StringUtils.isNotEmpty(realSsn) && StringUtils.isNotEmpty(ssn)){
			if(!ssn.equals(realSsn)){
				out.print(MakeMessage.printAlertCloseOnlyWithLayer(resource.getMessage("error.ext"), closeJQpopupFunction));
				out.flush();
			}
		}

		passQuestionCode = request.getParameter("passQuestionCode");
		passAnswer = request.getParameter("passAnswer");
		birthday = request.getParameter("birthday");

		homePostalCode = request.getParameter("homePostalCode");
		homeCountry = request.getParameter("homeCountry");
		homeState = request.getParameter("homeState");
		homeCity = request.getParameter("homeCity");
		homeStreet = request.getParameter("homeStreet");
		homeBasicAddress = request.getParameter("homeBasicAddress");
		homeExtAddress = request.getParameter("homeExtAddress");
		homeTel = request.getParameter("homeTel");
		homeFax = request.getParameter("homeFax");
		privateHomepage = request.getParameter("privateHomepage");

		Security.addProvider(new BouncyCastleProvider());
		MailUserVO mailUserVo = new MailUserVO();
		MailUserInfoVO mailUserInfoVo = new MailUserInfoVO();

		try {

			if(ExtPartConstants.isPkiLoginUse()){
				PKIManager pkiManager = new PKIManager();
				PKIAuthResultBean resultBean = pkiManager.getRegistCertificate(getPKIParamBean());
				mailUserInfoVo.setUserDN(resultBean.getUserDn());
			}

			String installLocale = EnvConstants.getBasicSetting("setup.state");

			mailUserVo.setMailDomainSeq(mailDomainSeq);
			String mailDomain = (String)mailUserManager.readDomain(mailDomainSeq).get("mail_domain");
			int groupSeq = mailUserManager.readDomainGroupSeq(mailDomainSeq, "default");
			if (groupSeq == 0) groupSeq = 1;

			mailUid = mailUid.toLowerCase();

			mailUserVo.setMailGroupSeq(groupSeq);
			mailUserVo.setMailUid(mailUid);
			mailUserVo.setAccountStatus("disabled");
			mailUserVo.setUserType("mailUser");
			mailUserVo.setQuotaWarningMode("");
			mailUserVo.setQuotaViolationAction("");
			mailUserVo.setQuotaOverlookRatio("10");
            mailUserVo.setQuotaWarningRatio("90");
            String encryptedPassword = getPassword(mailDomainSeq).toDbStr();
            if (encryptedPassword.startsWith("{SHA-")) {
    			int endIdx = encryptedPassword.indexOf("}");
    			String algorithm = encryptedPassword.startsWith("{SHA-256}")?"{SHA256}":"{SHA512}";
    			String pwdStr = encryptedPassword.substring(endIdx + 1);
    			encryptedPassword = algorithm+pwdStr;
    		}
			mailUserVo.setMailPassword(encryptedPassword);
			//TCUSTOM-2617
			if(StringUtils.isNotEmpty(realSsn) && StringUtils.isNotEmpty(ssn)){
				mailUserInfoVo.setSsn(getSSNEnc(ssn));
			}

			if(StringUtils.isNotEmpty(empNo)){
				if(mailUserManager.readUserInfoByEmpno(mailDomain, empNo)) {
				    throw new InvalidParameterException();
				}
			}

			mailUserInfoVo.setEmpno(empNo);
			mailUserInfoVo.setFirstName(firstName);
			mailUserInfoVo.setMiddleName(middleName);
			mailUserInfoVo.setLastName(lastName);
			mailUserInfoVo.setUserName(userName);
			mailUserInfoVo.setMobileNo(mobileNo);
			mailUserInfoVo.setPassQuestionCode(passQuestionCode);
			mailUserInfoVo.setPassAnswer(passAnswer);
			mailUserInfoVo.setRegisterStatus("apply");
			mailUserInfoVo.setBirthday(birthday);
			mailUserInfoVo.setHomeState(homeState);
			mailUserInfoVo.setHomePostalCode(homePostalCode);
			mailUserInfoVo.setHomeCountry(homeCountry);
			mailUserInfoVo.setHomeCity(homeCity);
			mailUserInfoVo.setHomeStreet(homeStreet);
			mailUserInfoVo.setHomeBasicAddress(homeBasicAddress);
			mailUserInfoVo.setHomeExtAddress(homeExtAddress);
			mailUserInfoVo.setHomeTel(homeTel);
			mailUserInfoVo.setHomeFax(homeFax);
			mailUserInfoVo.setUserLanguage(StringUtils.isNotEmpty(installLocale) ? installLocale : "ko");
			mailUserInfoVo.setPrivateHomepage(privateHomepage);
			mailUserInfoVo.setCreateTime(FormatUtil.getBasicDateStr());
			mailUserInfoVo.setApplyTime(FormatUtil.getBasicDateStr());
			mailUserInfoVo.setWebFolderQuota("0");

			UserEtcInfoVO userEtcInfoVo = new UserEtcInfoVO();
			userEtcInfoVo.setWriteMode("jp".equals(installLocale) ? "text":"html");
			userEtcInfoVo.setHiddenImg("off");
			userEtcInfoVo.setHiddenTag("on");
			userEtcInfoVo.setSignAttach("off");
			userEtcInfoVo.setSenderName(userName);
			userEtcInfoVo.setNotiInterval(0);
			userEtcInfoVo.setUserLocale(StringUtils.isNotEmpty(installLocale) ? installLocale : "ko");
			userEtcInfoVo.setForwardingMode("attached");
			userEtcInfoVo.setUserSkin("0");
			userEtcInfoVo.setSaveSendBox("on");
			userEtcInfoVo.setVcardAttach("off");
			userEtcInfoVo.setPageLineCnt(15);
			userEtcInfoVo.setReceiveNoti("jp".equals(installLocale) ? "off":"on");
			userEtcInfoVo.setAutoSaveMode("off");
			userEtcInfoVo.setAutoSaveTerm(0);
			userEtcInfoVo.setSearchAllFolder("off");

			userEtcInfoVo.setAfterLogin("domain");
			userEtcInfoVo.setCharSet("jp".equals(installLocale) ? "ISO-2022-JP":"EUC-KR");

			userEtcInfoVo.setSenderEmail(mailUid + "@" + mailDomain);
			try {
				userEtcInfoVo.setEncSenderName(MimeUtility.encodeWord((userName == null ? "" : userName), "UTF-8", null));
			} catch (UnsupportedEncodingException e) {
				throw new EncodingException(e);
			}

			mailUserManager.saveMailUser(mailUserVo, mailUserInfoVo, userEtcInfoVo);


		    out.print(MakeMessage.printAlertCloseOnlyWithLayer(resource.getMessage("register.012"), closeJQpopupFunction));
		    out.flush();
		}catch (InvalidParameterException e) {
            LogManager.writeErr(this, e.getMessage(), e);
            out.print(MakeMessage.printAlertCloseOnlyWithLayer(resource.getMessage("error.ext"), closeJQpopupFunction));
            out.flush();
        }catch (SaveFailedException e) {
			LogManager.writeErr(this, e.getMessage(), e);
			out.print(MakeMessage.printAlertCloseOnlyWithLayer(resource.getMessage("register.013"), closeJQpopupFunction));
			out.flush();
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			out.print(MakeMessage.printAlertCloseOnlyWithLayer(resource.getMessage("register.013"), closeJQpopupFunction));
			out.flush();
		}

		return null;
	}

	private PasswordEty getPassword(int domainSeq) {
		Map<String, Policy> policyMap = settingSecureManager.readPasswordPoliciesMap(SettingSecureManager.WEB_MAIL);
		AllowPolicy policy1 = (AllowPolicy) policyMap.get(AllowPolicy.NAME);
		SecurePolicy policy2 = (SecurePolicy) policyMap.get(SecurePolicy.NAME);

		String cryptMethod = systemManager.getPasswodCryptMethod(domainSeq);
		cryptMethod = StringUtils.replace(cryptMethod, "{", "");
		cryptMethod = StringUtils.replace(cryptMethod, "}", "");
		PasswordUtil.setAlgorithm(cryptMethod);

		String mailUidStr = mailUid.length() > 1 ? mailUid.substring(0, 2) : mailUid + "X";

		//TODO mailUid�� ���̰� 2���ڰ� �ȵǸ�?
		PasswordEty ety = PasswordUtil.makePasswordEty(mailPassword,mailUidStr);
		return ety;
	}

	private String getSSNEnc(String ssn) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			NoSuchProviderException, UnsupportedEncodingException,
			IllegalBlockSizeException, BadPaddingException {
		if (StringUtils.isEmpty(ssn))
			return "";

		return SecureUtil.encrypt(SymmetricCrypt.AES, "terrace-12345678", ssn);
	}


	private PKIAuthParamBean getPKIParamBean(){
		PKIAuthParamBean paramBean = new PKIAuthParamBean();
		if(ExtPartConstants.VENDER_SOFTFORUM == ExtPartConstants.getPKIVender()){
			paramBean.setSignedText(request.getParameter("pkiSignText"));
			paramBean.setSignedVid(request.getParameter("pkiVidText"));
			paramBean.setSsn(request.getParameter("ssn").replaceAll("-", ""));
		} else if(ExtPartConstants.VENDER_INITECH_V7 == ExtPartConstants.getPKIVender()){
			paramBean.setUserDN(request.getParameter("_shttp_client_cert_subject_"));
		}
		return paramBean;
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
