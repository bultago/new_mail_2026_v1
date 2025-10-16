/**
 * ApplicationResourceServlet.java 2008. 12. 8.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.common;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p><strong>ApplicationResourceServlet.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class ApplicationResourceServlet extends HttpServlet {

	private static final long serialVersionUID = -5281935317429094851L;
	
	/**
	 * <p></p>
	 *
	 * @see javax.servlet.GenericServlet#init()
	 * @throws ServletException 
	 */
	@Override
	public void init() throws ServletException {		
		ApplicationBeanUtil.setContext(getServletContext());
		String version = getServletContext().getInitParameter("version");
		if(StringUtils.isNotEmpty(version)){
			System.out.println("Welcome to TMS " + version);	
		}
	}

}
