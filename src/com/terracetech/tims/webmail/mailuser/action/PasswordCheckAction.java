package com.terracetech.tims.webmail.mailuser.action;

import java.security.PrivateKey;
import java.util.Map;

import javax.crypto.Cipher;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.terracetech.secure.policy.AllowPolicy;
import com.terracetech.secure.policy.LengthPolicy;
import com.terracetech.secure.policy.NotAllowPolicy;
import com.terracetech.secure.policy.Policy;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.exception.PasswordException;
import com.terracetech.tims.webmail.mailuser.manager.SettingSecureManager;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class PasswordCheckAction extends BaseAction {

	private final Logger logger = Logger.getLogger(PasswordCheckAction.class);

	private static final long serialVersionUID = 20090604L;

	private SettingSecureManager settingSecureManager = null;

	public void setSettingSecureManager(SettingSecureManager settingSecureManager) {
		this.settingSecureManager = settingSecureManager;
	}

	public PasswordCheckAction(boolean authcheck) {
		setAuthCheck(authcheck);
	}

	/**
	 * RE001 : �н�������̰� ª�� ���
	 * RE002 : �н����� ���̰� �� ���
	 * RE003 : �ּ� n�� �̻��� ���ڰ� ������ �ȵȰ��
	 * RE004 : �ּ� n�� �̻��� ��ȣ�� ������ �ȵȰ��
	 * RE005 : �ּ� n�� �̻��� ����� ������ �ȵȰ��
	 * RE006 : ���ӵ� ���ڿ��� ���
	 * RE007 : ������ ���ڿ��� ���
	 * RE008 : ������� �ʴ� ����
	 * RE009 :
	 *
	 */
	@Override
	public String execute() throws Exception {
		String mobile = request.getParameter("mobile");
		if(!"true".equalsIgnoreCase(mobile)){
			if (!checkPAID()) {
	             return "paidError";
	        }
		}
		I18nResources resource = getMessageResource("common");

		String password = request.getParameter("password");
		String passwordConfirm = request.getParameter("passwordConfirm");
		String uSeq = request.getParameter("uSeq");
		String uid = request.getParameter("uid");
		String name = request.getParameter("name");

		if(StringUtils.isNotEmpty(uSeq)){
			if(StringUtils.isEmpty(uid)){
				uid = settingSecureManager.readMailUid(Integer.parseInt(uSeq));
			}

			if(StringUtils.isEmpty(name)){
				name = settingSecureManager.readMailUserName(Integer.parseInt(uSeq));
			}
		}

		String resultMessage = "";

		Map<String, Policy> policyMap = null;
		try {
			policyMap = settingSecureManager.readPasswordPoliciesMap("webmail");

			//TCUSTOM-2657
			boolean loginParamterRSAUse = "true".equalsIgnoreCase(EnvConstants.getMailSetting("login.rsa.encrypt.use"));
			if(loginParamterRSAUse){
				try {
					PrivateKey privateKey = (PrivateKey) request.getSession().getAttribute("__rsaPasswordCheckPrivateKey__");
					request.getSession().removeAttribute("__rsaPasswordCheckPrivateKey__"); // 키의 재사용을 막는다. 항상 새로운 키를 받도록 강제.

					if(StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(passwordConfirm)){
						password = decryptRsa(privateKey, password);
						passwordConfirm = decryptRsa(privateKey, passwordConfirm);

						if(!password.trim().equals(passwordConfirm.trim())){
							throw new PasswordException("RE000");
						}
					}

				} catch(Exception ex) {
					throw new ServletException(ex.getMessage(), ex);
				}
			} else {
				if(!password.trim().equals(passwordConfirm.trim())){
					throw new PasswordException("RE000");
				}
			}

			settingSecureManager.validate(policyMap, password, uid, name);

			resultMessage = "success";
		} catch (Exception e) {
			if(e instanceof NullPointerException){
				LengthPolicy policy = (LengthPolicy)policyMap.get(LengthPolicy.NAME);
				String[] param = {String.valueOf(policy.getMinLengh()), String.valueOf(policy.getMaxLengh())};
				resultMessage = resource.getMessage("password.error.001", param);
			}else if("RE000".equals(e.getMessage())){
				resultMessage = resource.getMessage("auth.fail.password");
			}else if("RE001".equals(e.getMessage())){
				logger.debug("Password Length : "+ password.length());

				LengthPolicy policy = (LengthPolicy)policyMap.get(LengthPolicy.NAME);
				String[] param = {String.valueOf(policy.getMinLengh()), String.valueOf(policy.getMaxLengh())};
				resultMessage = resource.getMessage("password.error.001", param);
			}else if("RE002".equals(e.getMessage())){
				LengthPolicy policy = (LengthPolicy)policyMap.get(LengthPolicy.NAME);
				String[] param = {String.valueOf(policy.getMinLengh()), String.valueOf(policy.getMaxLengh())};
				resultMessage = resource.getMessage("password.error.001", param);
			}else if("RE003".equals(e.getMessage())){
				AllowPolicy policy = (AllowPolicy)policyMap.get(AllowPolicy.NAME);
				String[] param = {String.valueOf(policy.getMinAllowCnt())};
				resultMessage = resource.getMessage("password.error.002", param);
			}else if("RE004".equals(e.getMessage())){
				AllowPolicy policy = (AllowPolicy)policyMap.get(AllowPolicy.NAME);
				String[] param = {String.valueOf(policy.getMinAllowCnt())};
				resultMessage = resource.getMessage("password.error.003", param);
			}else if("RE005".equals(e.getMessage())){
				AllowPolicy policy = (AllowPolicy)policyMap.get(AllowPolicy.NAME);
				String[] param = {String.valueOf(policy.getMinAllowCnt())};
				resultMessage = resource.getMessage("password.error.004", param);
			}else if("RE006".equals(e.getMessage())){
				NotAllowPolicy policy = (NotAllowPolicy)policyMap.get(NotAllowPolicy.NAME);
				String[] param = {String.valueOf(policy.getContinuousCharacterCount())};
				resultMessage = resource.getMessage("password.error.008", param);
			}else if("RE007".equals(e.getMessage())){
				NotAllowPolicy policy = (NotAllowPolicy)policyMap.get(NotAllowPolicy.NAME);
				String[] param = {String.valueOf(policy.getSameCharacterCount())};
				resultMessage = resource.getMessage("password.error.007", param);
			}else if("RE008".equals(e.getMessage())){
				NotAllowPolicy policy = (NotAllowPolicy)policyMap.get(NotAllowPolicy.NAME);
				resultMessage = resource.getMessage("password.error.009");
			}
		}
		JSONObject jObj = null;

		jObj = new JSONObject();
		jObj.put("msg", resultMessage);

		ResponseUtil.processResponse(response, jObj);

		return null;
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
