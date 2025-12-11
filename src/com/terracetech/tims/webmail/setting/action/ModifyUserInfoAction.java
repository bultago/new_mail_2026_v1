package com.terracetech.tims.webmail.setting.action;

import java.security.PrivateKey;

import javax.crypto.Cipher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserInfoVO;
import com.terracetech.tims.webmail.util.AESUtils;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class ModifyUserInfoAction extends BaseAction {
	
	private SettingManager settingManager = null;
	private UserAuthManager userAuthManager = null;
	private SystemConfigManager systemManager = null;
	private String firstName = null;
	private String middleName = null;
	private String lastName = null;
	private String userName = null;
	private String mobileNo = null;
	private String mailPassword = null;
	private String passChgTime = null;
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
	
	private String companyName = null;
	private String departmentName = null;
	private String classCode = null;
	private String officePostalCode = null;
	private String officeCountry = null;
	private String officeState = null;
	private String officeCity = null;
	private String officeStreet = null;
	private String officeBasicAddress = null;
	private String officeExtAddress = null;
	private String officeTel = null;
	private String officeFax = null;
	private String officeHomepage = null;
	
	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}
	
	public void setUserAuthManager(UserAuthManager userAuthManager) {
		this.userAuthManager = userAuthManager;
	}
	
	public void setSystemManager(SystemConfigManager systemManager) {
		this.systemManager = systemManager;
	}
	
	public String execute() throws Exception {
		I18nResources resource = getMessageResource("setting");
		
		firstName = request.getParameter("firstName");
		middleName = request.getParameter("middleName");
		lastName = request.getParameter("lastName");
		userName = request.getParameter("userName");
		mobileNo = request.getParameter("mobileNo");
		mailPassword = request.getParameter("mailPassword");
		//mailPassword = AESUtils.decryptPass(mailPassword);
		passChgTime = request.getParameter("passChgTime");
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

		companyName = request.getParameter("companyName");
		departmentName = request.getParameter("departmentName");
		classCode = request.getParameter("classCode");
		officePostalCode = request.getParameter("officePostalCode");
		officeCountry = request.getParameter("officeCountry");
		officeState = request.getParameter("officeState");
		officeCity = request.getParameter("officeCity");
		officeStreet = request.getParameter("officeStreet");
		officeBasicAddress = request.getParameter("officeBasicAddress");
		officeExtAddress = request.getParameter("officeExtAddress");
		officeTel = request.getParameter("officeTel");
		officeFax = request.getParameter("officeFax");
		officeHomepage = request.getParameter("officeHomepage");
				
		User user = UserAuthManager.getUser(request);
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		String msg = null;
		try {
			UserInfoVO userInfoVo = new UserInfoVO();
			
			if (StringUtils.isNotEmpty(mailPassword)) {
				/* Login ID, PW Parameter RSA Encrypt S */
				boolean loginParamterRSAUse = "true".equalsIgnoreCase(EnvConstants.getMailSetting("login.rsa.encrypt.use"));
				if(loginParamterRSAUse){
					PrivateKey privateKey = (PrivateKey) request.getSession().getAttribute("__rsaChangePasswordPrivateKey__");
					request.getSession().removeAttribute("__rsaChangePasswordPrivateKey__"); // 키의 재사용을 막는다. 항상 새로운 키를 받도록 강제.
			        if (privateKey == null) {
			            //throw new RuntimeException("암호화 비밀키 정보를 찾을 수 없습니다.");
			        }
			        try {
			        	mailPassword = decryptRsa(privateKey, mailPassword);
			        } catch (Exception ex) {
			            throw new ServletException(ex.getMessage(), ex);
			        }
				}else{
					mailPassword = AESUtils.decryptPass(mailPassword);
				}
				/* Login ID, PW Parameter RSA Encrypt E */
				settingManager.setMyPassword(mailDomainSeq, mailUserSeq, mailPassword);
			}	
			
			userInfoVo.setMailUserSeq(mailUserSeq);
			userInfoVo.setFirstName(firstName);
			userInfoVo.setMiddleName(middleName);
			userInfoVo.setLastName(lastName);
			userInfoVo.setUserName(userName);
			userInfoVo.setMobileNo(mobileNo);
			userInfoVo.setPassChgTime(FormatUtil.getBasicDateStr());
			userInfoVo.setPassQuestionCode(passQuestionCode);
			userInfoVo.setPassAnswer(passAnswer);
			
			userInfoVo.setBirthday(birthday);
			userInfoVo.setHomePostalCode(homePostalCode);
			userInfoVo.setHomeCountry(homeCountry);
			userInfoVo.setHomeState(homeState);
			userInfoVo.setHomeCity(homeCity);
			userInfoVo.setHomeStreet(homeStreet);
			userInfoVo.setHomeBasicAddress(homeBasicAddress);
			userInfoVo.setHomeExtAddress(homeExtAddress);
			userInfoVo.setHomeTel(homeTel);
			userInfoVo.setHomeFax(homeFax);
			userInfoVo.setPrivateHomepage(privateHomepage);
			
			userInfoVo.setCompanyName(companyName);
			userInfoVo.setDepartmentName(departmentName);
			userInfoVo.setClassCode(classCode);
			userInfoVo.setOfficePostalCode(officePostalCode);
			userInfoVo.setOfficeCountry(officeCountry);
			userInfoVo.setOfficeState(officeState);
			userInfoVo.setOfficeCity(officeCity);
			userInfoVo.setOfficeStreet(officeStreet);
			userInfoVo.setOfficeBasicAddress(officeBasicAddress);
			userInfoVo.setOfficeExtAddress(officeExtAddress);
			userInfoVo.setOfficeTel(officeTel);
			userInfoVo.setOfficeFax(officeFax);
			userInfoVo.setOfficeHomepage(officeHomepage);
			
			settingManager.modifyUserInfo(userInfoVo);
			
			user.put("USER_NAME", userName);
			userAuthManager.updateUserCookieProcess(request, response, user, systemManager.getCryptMethod());
			
			msg = resource.getMessage("update.ok");
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			msg = resource.getMessage("update.fail");
		}
		
		HttpSession session = request.getSession();
		session.removeAttribute("userAuthCheck");
		request.setAttribute("msg", msg);
		return "success";
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
