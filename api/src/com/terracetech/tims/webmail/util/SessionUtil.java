package com.terracetech.tims.webmail.util;

import java.security.MessageDigest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import com.terracetech.tims.webmail.mailuser.User;

public class SessionUtil {

	public static String getToken(HttpServletRequest req){

		HttpSession session = req.getSession(true);
		
		byte[] time = new Long(session.getCreationTime()).toString().getBytes();
		
		byte[] id = session.getId().getBytes();
		
		byte[] browser = req.getHeader("User-Agent").getBytes();
		
		byte[] ipaddr = req.getRemoteAddr().getBytes();
		
		StringBuffer buf = new StringBuffer();
			
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(id);
			md5.update(time);
			md5.update(browser);
			md5.update(ipaddr);
						
			byte[] digest = md5.digest();
			
			for(int i = 0 ; i < digest.length ; i++){
				buf.append(Integer.toHexString((int)digest[i] & 0x00ff));
			}
			
		} catch( Exception e){
			System.err.println("Unable to calculate MD5 Diguests");
		}		
		
		return buf.toString();
		
	}
	
	public static User getUser(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			return (User) session.getAttribute("user");
		}
		return null;
	}
	
}
