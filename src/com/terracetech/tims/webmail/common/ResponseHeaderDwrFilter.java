package com.terracetech.tims.webmail.common;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResponseHeaderDwrFilter implements Filter {
	FilterConfig fc;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;

		String requestUrl = request.getRequestURI();
		if(!requestUrl.endsWith(".js") && !requestUrl.endsWith(".dwr") && !requestUrl.endsWith(".swf")){
			System.out.println("### request.getRequestURI() ["+request.getRequestURI()+"] => is not js file!");
			return;
		}


		// set the provided HTTP response parameters
		for (Enumeration e = fc.getInitParameterNames(); e.hasMoreElements();) {
			String headerName = (String) e.nextElement();
			response.addHeader(headerName, fc.getInitParameter(headerName));
		}
		// pass the request/response on
		chain.doFilter(req, response);
	}

	@Override
	public void init(FilterConfig filterConfig) {
		this.fc = filterConfig;
	}

	@Override
	public void destroy() {
		this.fc = null;
	}
}
