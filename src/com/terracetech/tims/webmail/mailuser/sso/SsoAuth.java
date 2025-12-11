/**
 * SsoAuth.java 2009. 2. 2.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mailuser.sso;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import jakarta.servlet.http.HttpServletRequest;

import com.terracetech.tims.webmail.mailuser.vo.SsoInfoVO;

/**
 * <p><strong>SsoAuth.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author ysko
 * @since Tims7
 * @version 7.0 
 */
public interface SsoAuth {

	public Map<String, String> getValue(SsoInfoVO infoVo,
			HttpServletRequest request) throws InvalidKeyException,
			UnsupportedEncodingException, IllegalBlockSizeException,
			BadPaddingException, NoSuchAlgorithmException,
			NoSuchPaddingException, NoSuchProviderException;

	public Map<String, String> getTestValue(String algorithm, HttpServletRequest request);  
}