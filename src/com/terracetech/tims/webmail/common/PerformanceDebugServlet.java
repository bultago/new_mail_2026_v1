package com.terracetech.tims.webmail.common;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.terracetech.tims.webmail.common.log.PerformanceLogManager;

public class PerformanceDebugServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ServletContext context = getServletContext();
		String mode = req.getParameter("mode");		
		mode = (mode != null)?mode:"s";
		String debugUse = req.getParameter("debug");
		debugUse = (debugUse != null)?debugUse:"disable";
		
		if(mode.equals("s")){
			context.setAttribute("PDEBUG", debugUse);
			PrintWriter out = resp.getWriter();
			out.print("<script>history.back();</script>");
		}else if(mode.equals("l")){
			String agent = req.getHeader("user-agent").toUpperCase();			
			PerformanceLogManager.writeLog(req.getRemoteAddr(),req.getParameter("user"),agent, req.getParameter("action"), req.getParameter("data"));
		}		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req,resp);
	}
	

}
