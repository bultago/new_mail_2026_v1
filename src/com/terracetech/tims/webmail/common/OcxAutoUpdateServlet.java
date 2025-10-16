package com.terracetech.tims.webmail.common;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.terracetech.tims.webmail.mobile.manager.SyncCheckThread;

public class OcxAutoUpdateServlet extends HttpServlet {
	
	private static final long serialVersionUID = -872127057107338798L;	
	
	public void init() throws ServletException {
		ServletContext context = getServletContext();		
		new OcxCheckThread(context.getRealPath("/"));
		new SyncCheckThread();
	}

}
