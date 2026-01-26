<%@ page language="java" contentType="text/html; charset=UTF-8" 	pageEncoding="UTF-8"
%><%@ page import="org.json.simple.JSONObject" 
%><%@ page import="com.terracetech.tims.webmail.util.ResponseUtil"
%><%@ page import="com.terracetech.tims.webmail.setting.manager.*"
%><%@ page import="com.terracetech.tims.webmail.util.*"
%><%@ page import="com.terracetech.secure.policy.*"
%><%@ page import="java.util.*"
%><%@ page import="com.terracetech.tims.webmail.mailuser.*"
%><%@ page import="com.terracetech.tims.common.*"
%><%

	JSONObject jsonObj = new JSONObject();
	SettingManager settingManager = (SettingManager) ApplicationBeanUtil.getApplicationBean("settingManager");
	I18nResources resource = new I18nResources(I18nConstants.getBundleUserLocale(request), "common");
	try{
		int mailDomainSeq = Integer.parseInt(request.getParameter("mailDomainSeq"));
		int mailUserSeq = Integer.parseInt(request.getParameter("mailUserSeq"));
		String password = request.getParameter("password");
		String passwordConfirm = request.getParameter("passwordConfirm");
		if(mailDomainSeq == 0 || mailUserSeq == 0 || password == null || passwordConfirm == null){
			jsonObj.put("status", "fail");
			jsonObj.put("msg", resource.getMessage("mobile.password.change.1"));
			ResponseUtil.processResponse(response, jsonObj);
			return;
		}
		
		if(!password.equals(passwordConfirm)){
			jsonObj.put("status", "fail");
			jsonObj.put("msg", resource.getMessage("mobile.password.change.2"));
			ResponseUtil.processResponse(response, jsonObj);
			return;
		}
		
		request.getSession().setAttribute(User.MAIL_PASSWORD, "");
		settingManager.setMyPassword(mailDomainSeq, mailUserSeq, password);
		
	}catch(Exception e){
		e.printStackTrace();
		jsonObj.put("status", "fail");
		jsonObj.put("msg", e.getMessage());
		ResponseUtil.processResponse(response, jsonObj);
		return;
	}
	jsonObj.put("status", "success");
	jsonObj.put("msg", resource.getMessage("mobile.password.change.3"));
	
	
	ResponseUtil.processResponse(response, jsonObj);
%>