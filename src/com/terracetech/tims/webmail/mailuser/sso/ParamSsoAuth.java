package com.terracetech.tims.webmail.mailuser.sso;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.util.encoders.Base64;

import com.terracetech.secure.SecureManager;
import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.tims.webmail.mailuser.vo.SsoInfoVO;

public class ParamSsoAuth implements SsoAuth {
//	TODO 파라미터 상수 처리 
	public final static String SSO_PARAM_NAME = "ssoParam";
	public final static String FIRST_DELIM = ",";
	public final static String SECOND_DELIM = "=";
	
	public Map<String, String> getValue(SsoInfoVO infoVo,
			HttpServletRequest request) throws InvalidKeyException,
			UnsupportedEncodingException, IllegalBlockSizeException,
			BadPaddingException, NoSuchAlgorithmException,
			NoSuchPaddingException, NoSuchProviderException {
		String paramValue = request.getParameter(SSO_PARAM_NAME);
		if (!isValidParam(paramValue))
			return null;
		
		if("none".equals(infoVo.getAlgorithm().toLowerCase())){
			return decryptStr(paramValue);	
		}else{
			//ZqAzX1IuYw76ieLHdUCOEE9pIFDBm+tFmLBrFHGuomIVqsUcA9MBfZC/qBXD+CEB
			String text = new String(Base64.decode(paramValue));
			String decrypted = SecureUtil.decrypt(infoVo.getAlgorithm(), infoVo.getKey(), text);
			return decryptStr(decrypted);	
		}
		
	}
	
	public Map<String, String> getTestValue (String algorithm, HttpServletRequest request) {

		String paramValue = request.getParameter(SSO_PARAM_NAME);
		if (!isValidParam(paramValue))
			return null;
		
		String decrypted = SecureManager.getDecryptedSsoStr(algorithm, paramValue);
		return decryptStr(decrypted);
	}

	private Map<String, String> decryptStr(String decrypted) {
		String[] valueSet = decrypted.split(FIRST_DELIM);
		Map<String, String> param = new HashMap<String, String>();
		for (String valueStr : valueSet) {
			if (!valueStr.contains(SECOND_DELIM))
				continue;
			
			String key = valueStr.split(SECOND_DELIM)[0];
			String value = valueStr.split(SECOND_DELIM)[1];
			param.put(key, value);
		}
		return param;
	}
	
	public boolean isValidParam (String paramValue) {
		return paramValue != null && paramValue.trim().length() > 0 ;
	}
}