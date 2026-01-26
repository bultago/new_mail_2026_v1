/**
 * ResponseUtil.java 2009. 4. 27.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.util;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.secure.Base64;
import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.secure.crypto.SymmetricCrypt;
import com.terracetech.tims.webmail.common.EnvConstants;

/**
 * <p><strong>ResponseUtil.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class ResponseUtil {
	
	public static void processResponse(HttpServletResponse response, JSONObject jObj) throws Exception{
		response.setContentType("text/html; charset=UTF-8");
		response.addHeader("Cache-Control","no-store");
        response.addHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();
        out.println(jObj);		
	}
	
	public static void processResponse(HttpServletResponse response, JSONArray jArray) throws Exception{
		response.setContentType("text/html; charset=UTF-8");
		response.addHeader("Cache-Control","no-store");
        response.addHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();
        out.println(jArray);		
	}
	
	public static void makeAjaxMessage (HttpServletResponse response, String returnValue){
		response.setContentType("text/html; charset=UTF-8");
    	response.addHeader("Cache-Control","no-store");
		response.addHeader("Pragma", "no-cache");
		try {
			response.getWriter().write(returnValue);
		} catch (IOException e) {
			// TODO: handle exception
		}
    }
	
	public static void moveWelcome(HttpServletResponse response){
		response.setContentType("text/html; charset=UTF-8");
    	response.addHeader("Cache-Control","no-store");
		response.addHeader("Pragma", "no-cache");
		try {
			response.getWriter().write("<META HTTP-EQUIV=refresh CONTENT='0; URL=/common/welcome.do'>");
		} catch (IOException e) {
			// TODO: handle exception
		}
    }
	
	public static void alimCryptResponse(HttpServletResponse response, JSONObject jObj,String cryptMethod) throws Exception{
		response.setContentType("text/html; charset=UTF-8");
    	response.addHeader("Cache-Control","no-store");
		response.addHeader("Pragma", "no-cache");
		String responseJsonStr = jObj.toString();
		if(cryptMethod != null && !"normal".equalsIgnoreCase(cryptMethod)){
			responseJsonStr = SecureUtil.encrypt(cryptMethod, EnvConstants.ENCRYPT_KEY, responseJsonStr);
		} else {
			responseJsonStr = Base64.encode(responseJsonStr.getBytes());
		}
		try {
			response.getWriter().write(responseJsonStr);
		} catch (IOException e) {}
	}
	
	public static void makeJsonpResponse(HttpServletRequest request, HttpServletResponse response, JSONObject jObj) 
	        throws Exception {
	        response.setContentType("text/javascript; charset=UTF-8");
	        response.addHeader("Cache-Control", "no-store");
	        response.addHeader("Pragma", "no-cache");
	        String output = request.getParameter("callback") + "(" + jObj + ");";
	        PrintWriter out = response.getWriter();
	        out.println(output);
	}
}
