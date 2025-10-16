package com.terracetech.tims.webmail.mailuser.sso;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Base64;

import com.terracetech.secure.SecureManager;
import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.tims.webmail.mailuser.vo.SsoInfoVO;


public class CookieSsoAuth implements SsoAuth {

	public final static String SSO_COOKIE_NAME = "tims7sso";
	public final static String FIRST_DELIM = ",";
	public final static String SECOND_DELIM = "=";
	
	public Map<String, String> getValue(SsoInfoVO infoVo,
			HttpServletRequest request) throws InvalidKeyException,
			UnsupportedEncodingException, IllegalBlockSizeException,
			BadPaddingException, NoSuchAlgorithmException,
			NoSuchPaddingException, NoSuchProviderException {
		
		String encryptedValue = getStrFromCookie(request);
		
		if("none".equals(infoVo.getAlgorithm().toLowerCase())){
			return parseStr(encryptedValue);
		}else{
			//ZqAzX1IuYw76ieLHdUCOEE9pIFDBm+tFmLBrFHGuomIVqsUcA9MBfZC/qBXD+CEB
			String text = new String(Base64.decode(encryptedValue));
			String decrypted = SecureUtil.decrypt(infoVo.getAlgorithm(), infoVo.getKey(), text);
			return parseStr(decrypted);
		}
	}

	public Map<String, String> getTestValue (String algorithm, HttpServletRequest request) {
		String encryptedValue = getStrFromCookie(request);
		
		String decrypted = SecureManager.getDecryptedSsoStr(algorithm, encryptedValue);
		
		return parseStr(decrypted);
	}
	
	private Map<String, String> parseStr(String decrypted) {
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

	@SuppressWarnings("deprecation")
	private String getStrFromCookie(HttpServletRequest request) {
		String encryptedValue = null;
		for (Cookie cookie : request.getCookies()) {
			String cookieName = cookie.getName();
			if (SSO_COOKIE_NAME.equals(cookieName)) {
				encryptedValue = cookie.getValue();
				break;
			}
		}
		return URLDecoder.decode(encryptedValue);
	}
	
	public void setValue (HttpServletResponse response, Map<String, String> param) {
		
		StringBuffer valueStr = new StringBuffer();
		Iterator<String> keys = param.keySet().iterator();
		while (keys.hasNext()) {
			if (valueStr.length() > 0)
				valueStr.append(FIRST_DELIM);
			
			String key = keys.next();
			valueStr.append(key).append(SECOND_DELIM).append(param.get(key));
		}
		
		String encrypted = SecureManager.getCryptedSsoStr(valueStr.toString());
		encrypted = encrypted.replaceAll("\r", "").replaceAll("\n", "");
		Cookie cookie = new Cookie(SSO_COOKIE_NAME, encrypted);
		
		response.addCookie(cookie);
	}
}