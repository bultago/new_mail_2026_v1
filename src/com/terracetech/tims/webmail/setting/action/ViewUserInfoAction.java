package com.terracetech.tims.webmail.setting.action;

import java.net.URLDecoder;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;

import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.secure.crypto.SymmetricCrypt;
import com.terracetech.secure.policy.AllowPolicy;
import com.terracetech.secure.policy.LengthPolicy;
import com.terracetech.secure.policy.NotAllowPolicy;
import com.terracetech.secure.policy.Policy;
import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.mailuser.manager.SettingSecureManager;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.mailuser.vo.MailDomainCodeVO;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserInfoVO;
import com.terracetech.tims.webmail.util.StringUtils;

public class ViewUserInfoAction extends BaseAction {
	
	private static final long serialVersionUID = 20090427L;
	
	private SettingManager settingManager = null;
	private MailUserManager mailUserManager = null;
	private SystemConfigManager systemManager = null;
	private SettingSecureManager settingSecureManager = null;
	private String authCheck = null;
	private UserInfoVO userInfoVo = null;
	private List<MailDomainCodeVO> passCodeList = null;
	private List<MailDomainCodeVO> classCodeList = null;
	private final static String PASSWORD_CODE = "30000";
	private final static String CLASS_CODE = "100";
	private String showPasswordInput = null;
	Map<String, String> loginConfigList = null;
	private String installLocale = null;
	
	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}

	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	
	public void setSystemManager(SystemConfigManager systemManager) {
		this.systemManager = systemManager;
	}

	public void setSettingSecureManager(SettingSecureManager settingSecureManager) {
		this.settingSecureManager = settingSecureManager;
	}

	@Override
	public String execute() throws Exception {
		
		User user = UserAuthManager.getUser(request);
		Locale locale = I18nConstants.getBundleUserLocale(request);
		I18nResources resource = getMessageResource("setting");
		
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		String returnPath = "";
		String cookieValue = null;
		String compareId = null;
		String compareDomain = null;
		int compareDomainSeq = 0;
		int compareUserSeq = 0;
		boolean isSuccess = false;
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
								returnPath = "success";
								userInfoVo = settingManager.getUserInfo(mailUserSeq);
								try {
									if(StringUtils.isNotEmpty(userInfoVo.getSsn())){
										userInfoVo.setSsn(SecureUtil.decrypt(SymmetricCrypt.AES, "terrace-12345678", userInfoVo.getSsn()));	
									}
								} catch (Exception e1) {
									LogManager.writeErr(this, e1.getMessage(), e1);
								}
								passCodeList = mailUserManager.getMailDomainCode(mailDomainSeq, PASSWORD_CODE, locale.getLanguage());
								classCodeList = mailUserManager.getMailDomainCode(mailDomainSeq, CLASS_CODE, locale.getLanguage());
								showPasswordInput = settingManager.showPasswordInput(mailDomainSeq);
								loginConfigList = systemManager.getLoginConfig();
								isSuccess = true;
								installLocale = EnvConstants.getBasicSetting("setup.state");
							}
						}
					}
					break;
				}
			}
		}
		
		/* Login ID, PW Parameter RSA Encrypt S */
		HttpSession session = request.getSession();
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
            session.setAttribute("__rsaChangePasswordPrivateKey__", privateKey);

            // 공개키를 문자열로 변환하여 JavaScript RSA 라이브러리 넘겨준다.
            RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);

            String publicKeyModulus = publicSpec.getModulus().toString(16);
            String publicKeyExponent = publicSpec.getPublicExponent().toString(16);

            request.setAttribute("publicKeyModulus", publicKeyModulus);
            request.setAttribute("publicKeyExponent", publicKeyExponent);
		}
		/* Login ID, PW Parameter RSA Encrypt E */


		// 패스워드 정책 문구
		Map<String, Policy> policyMap = null;
		policyMap = settingSecureManager.readPasswordPoliciesMap("webmail");

		String pwInfoMsg = "";
		String lPolicyMsg = "";
		String aPolicyMsg = "";
		String naPolicyMsg = "";

		if(policyMap != null){
			Collection<Policy> policies = policyMap.values();
			for (Policy policy : policies) {

				// 문자열 길이 제한
				if(policy instanceof LengthPolicy){
					LengthPolicy lPolicy = (LengthPolicy)policy;

					if(lPolicy.isPolicyUsed()){
						lPolicyMsg = resource.getMessage("password.info.notice.001",new Object[]{lPolicy.getMinLengh(),lPolicy.getMaxLengh()});
					}
				}

				// 필수문자 설정
				if(policy instanceof AllowPolicy){
					AllowPolicy aPolicy = (AllowPolicy)policy;
					String tmpMsg = "";

					if(aPolicy.isNumberUsed()){
						tmpMsg += StringUtils.isNotEmpty(tmpMsg) ? ", " + resource.getMessage("password.info.notice.003") : resource.getMessage("password.info.notice.003");
					}
					if(aPolicy.isSymbolUsed()){
						tmpMsg += StringUtils.isNotEmpty(tmpMsg) ? ", " + resource.getMessage("password.info.notice.004") : resource.getMessage("password.info.notice.004");
					}
					if(aPolicy.isSpaceUsed()){
						tmpMsg += StringUtils.isNotEmpty(tmpMsg) ? ", " + resource.getMessage("password.info.notice.005") : resource.getMessage("password.info.notice.005");
					}
					if(aPolicy.isTextUsed() ){
						tmpMsg += StringUtils.isNotEmpty(tmpMsg) ? ", " + resource.getMessage("password.info.notice.011") : resource.getMessage("password.info.notice.011");
					}

					if(aPolicy.isPolicyUsed()){
						aPolicyMsg = resource.getMessage("password.info.notice.002",new Object[]{tmpMsg,aPolicy.getMinAllowCnt()});
					}
				}

				// 불허문자 설정
				if(policy instanceof NotAllowPolicy){
					NotAllowPolicy naPolicy = (NotAllowPolicy)policy;
					String tmpMsg = "";

					if(naPolicy.isIdNotAllowed()){
						tmpMsg += StringUtils.isNotEmpty(tmpMsg) ? ", " + resource.getMessage("password.info.notice.007") : resource.getMessage("password.info.notice.007");
					}
					if(naPolicy.isNameNotAllowed()) {
						tmpMsg += StringUtils.isNotEmpty(tmpMsg) ? ", " + resource.getMessage("password.info.notice.008") : resource.getMessage("password.info.notice.008");
					}
					if(naPolicy.isSameCharacterNotAllowed()){
						tmpMsg += StringUtils.isNotEmpty(tmpMsg) ? ", " + resource.getMessage("password.info.notice.009",new Object[]{naPolicy.getSameCharacterCount()}) : resource.getMessage("password.info.notice.009",new Object[]{naPolicy.getSameCharacterCount()});
					}
					if(naPolicy.isContinuousCharacterNotAllowed()){
						tmpMsg += StringUtils.isNotEmpty(tmpMsg) ? ", " + resource.getMessage("password.info.notice.010",new Object[]{naPolicy.getContinuousCharacterCount()}) : resource.getMessage("password.info.notice.010",new Object[]{naPolicy.getContinuousCharacterCount()});
					}

					String customNotAllowList[] = null;
					if(naPolicy.isCustomNotAllowed()){
						customNotAllowList = naPolicy.getCustomNotAllowList();
					}

					if(customNotAllowList != null){
						for(int i = 0; customNotAllowList.length > i; i++){
							tmpMsg += StringUtils.isNotEmpty(tmpMsg) ? ", " + customNotAllowList[i] : customNotAllowList[i];
						}
					}

					if(naPolicy.isPolicyUsed()){
						naPolicyMsg = resource.getMessage("password.info.notice.006",new Object[]{tmpMsg});
					}
				}
			}
		}

		
System.out.println(pwInfoMsg);		
		
		pwInfoMsg += StringUtils.isNotEmpty(pwInfoMsg) && StringUtils.isNotEmpty(lPolicyMsg) ? "<br>" + lPolicyMsg : lPolicyMsg;
		pwInfoMsg += StringUtils.isNotEmpty(pwInfoMsg) && StringUtils.isNotEmpty(aPolicyMsg) ? "<br>" + aPolicyMsg : aPolicyMsg;
		pwInfoMsg += StringUtils.isNotEmpty(pwInfoMsg) && StringUtils.isNotEmpty(naPolicyMsg) ? "<br>" + naPolicyMsg : naPolicyMsg;

		request.setAttribute("pwInfoMsg", pwInfoMsg);



		if (!isSuccess) {
			String msg = resource.getMessage("conf.alert.userinfo.badconnect");
			request.setAttribute("msg", msg);
			returnPath = "fail";
		}

		return returnPath;
	}

	public String getShowPasswordInput() {
		return showPasswordInput;
	}

	public String getAuthCheck() {
		return authCheck;
	}

	public void setAuthCheck(String authCheck) {
		this.authCheck = authCheck;
	}

	public List<MailDomainCodeVO> getPassCodeList() {
		return passCodeList;
	}

	public void setPassCodeList(List<MailDomainCodeVO> passCodeList) {
		this.passCodeList = passCodeList;
	}

	public List<MailDomainCodeVO> getClassCodeList() {
		return classCodeList;
	}

	public void setClassCodeList(List<MailDomainCodeVO> classCodeList) {
		this.classCodeList = classCodeList;
	}

	public UserInfoVO getUserInfoVo() {
		return userInfoVo;
	}

	public void setUserInfoVo(UserInfoVO userInfoVo) {
		this.userInfoVo = userInfoVo;
	}

	public Map<String, String> getLoginConfigList() {
		return loginConfigList;
	}

	public String getInstallLocale() {
		return installLocale;
	}
}
