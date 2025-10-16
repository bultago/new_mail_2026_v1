package com.terracetech.tims.webmail.util;

import java.util.*;

import java.net.URLDecoder;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {
	private HttpServletRequest request		= null;
	private HttpServletResponse response	= null;

	private Cookie[] cookies 		= null;

	private String charset			= "UTF-8";
	private boolean isIgnoreCase	= true;

	private HashMap<String, String> map = new HashMap<String, String>();

	public CookieUtils(HttpServletRequest request, HttpServletResponse response) {
		this.request	= request;
		this.response	= response;

		parseCookie();
	}

	public CookieUtils(HttpServletRequest request) {
		this.request	= request;

		parseCookie();
	}

	public CookieUtils(HttpServletRequest request, String charset) {
		this.request	= request;
		this.charset	= charset;

		parseCookie();
	}

	public CookieUtils(HttpServletRequest request, boolean sIgnoreCase) {
		this.request	= request;
		this.isIgnoreCase	= isIgnoreCase;

		parseCookie();
	}

	public CookieUtils(HttpServletRequest request, 
			String charset, boolean sIgnoreCase) {
		this.request 	= request;
		this.charset	= charset;
		this.isIgnoreCase	= isIgnoreCase;

		parseCookie();
	}

	public void setIgnoreCase(boolean isIgnoreCase) {
		this.isIgnoreCase	= isIgnoreCase;
	}

	public void setCharset(String  charset) {
		this.charset	= charset;
	}

	private void parseCookie() {
		this.cookies = request.getCookies();

		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				String name	 	= cookies[i].getName();
				String value	= cookies[i].getValue();

				if (charset != null) {
					try {
						value = URLDecoder.decode(value, charset);
					} catch (Exception e) { 
						System.out.println("-- ERR URLDecoder ["+value+"] : ["+e.getMessage()+"]");
					}
				}

				if (isIgnoreCase) {
					map.put(name.toUpperCase(), value);
				}
				else {
					map.put(name, value);
				}
            }
        }
	}

	public Cookie getCookie(String name) {
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(name) || 
					(isIgnoreCase && 
						cookies[i].getName().equalsIgnoreCase(name))) {
					return cookies[i];
				}
			}
        }

		return null;
	}

	public String getValue(String name) {
		if (isIgnoreCase) {
			return map.get(name.toUpperCase());
		}

		return map.get(name);
	}

	public static String getValue(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equalsIgnoreCase(name)) {
					return cookies[i].getValue();
				}
			}
        }
		return null;
    }
}

