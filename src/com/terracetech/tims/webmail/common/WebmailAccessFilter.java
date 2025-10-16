package com.terracetech.tims.webmail.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.terracetech.tims.webmail.util.AccessCheckUtil;

public class WebmailAccessFilter implements Filter {

    @Override
	public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
        ServletException {

        HttpServletRequest httpRequest = null;
        HttpServletResponse httpResponse = null;
        String remoteAddr = null;
        String url = null;
        if (request instanceof HttpServletRequest){
            httpRequest = (HttpServletRequest) request;
            remoteAddr = httpRequest.getRemoteAddr();
            url = httpRequest.getRequestURI();
        }
        if (response instanceof HttpServletResponse){
            httpResponse = (HttpServletResponse) response;
        }

        /* TCUSTOM-TCUSTOM-3037 Defence Password Haking 20170407 S */
        if (url.endsWith("/common/login.do")){
    		if (!"POST".equalsIgnoreCase(httpRequest.getMethod())){
    			System.out.println("### uri[/common/login.do], requestIp["+httpRequest.getRemoteAddr()+"], httpMethod["+httpRequest.getMethod()+"] => Not Support request method not post");
    			httpResponse.sendRedirect("/common/error/error403.jsp");
    			return;
    		}
    		String referer = httpRequest.getHeader("Referer");
    		if (referer == null){
    			System.out.println("### referer["+referer+"], requestIp["+httpRequest.getRemoteAddr()+"], httpMethod["+httpRequest.getMethod()+"] => Referer is null");
    			httpResponse.sendRedirect("/common/error/error403.jsp");
    			return;
    		}
    		if(referer.indexOf("?") != -1){
    			referer = referer.substring(0, referer.indexOf("?"));
    		}
    		if (!referer.endsWith("/common/welcome.do") && !referer.endsWith("/mobile/common/login.do")){
    			System.out.println("### referer["+referer+"], requestIp["+httpRequest.getRemoteAddr()+"], httpMethod["+httpRequest.getMethod()+"] => Referer is not /common/welcome.do");
    			httpResponse.sendRedirect("/common/error/error403.jsp");
    			return;
    		}
        }
        /* TCUSTOM-TCUSTOM-3037 Defence Password Haking 20170407 E */

        if(!AccessCheckUtil.isHybridUrl(url) && !AccessCheckUtil.checkAccessAllowIp(remoteAddr, url)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else {
            chain.doFilter(request, response);
        }

    }

    @Override
	public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

}
