package com.terracetech.tims.service.tms.portlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.terracetech.tims.webmail.mailuser.User;

public interface IMailPortletService {

	public String doSimpleMailList(HttpServletRequest request, HttpServletResponse response) throws Exception;

	public String doSimpleMailRead(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	public String doSimpleMailSend(HttpServletRequest request, HttpServletResponse response, User user) throws Exception;
	
	public String doReturnError(HttpServletRequest request,HttpServletResponse response, String rootName) throws Exception;
}