<%@page import="com.terracetech.tims.webmail.util.CryptoSession"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" 	pageEncoding="UTF-8"
%><%@ page import="java.security.KeyFactory"
%><%@ page import="java.security.KeyPair"
%><%@ page import="java.security.KeyPairGenerator"
%><%@ page import="java.security.PrivateKey"
%><%@ page import="java.security.PublicKey"
%><%@ page import="java.security.spec.RSAPublicKeySpec"
%><%@ page import="org.json.simple.JSONObject" 
%><%@ page import="com.terracetech.tims.webmail.util.ResponseUtil"  
%><%    
	//비밀번호 변경 전 유효성 검사용 
	JSONObject jsonObj = new JSONObject();	
		
	KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
	generator.initialize(1024);
	
	KeyPair keyPair = generator.genKeyPair();
	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	
	PublicKey publicKey = keyPair.getPublic();
	PrivateKey privateKey = keyPair.getPrivate();
	
	// 세션에 공개키의 문자열을 키로하여 개인키를 저장한다.
	session.setAttribute("__rsaPasswordCheckPrivateKey__", privateKey);

	// 공개키를 문자열로 변환하여 JavaScript RSA 라이브러리 넘겨준다.
    RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
    
    String rsaPublicKeyModulus = publicSpec.getModulus().toString(16);
    String rsaPublicKeyExponent = publicSpec.getPublicExponent().toString(16);
	
	System.out.println("rsaPublicKeyModulus : "+rsaPublicKeyModulus);
	System.out.println("rsaPublicKeyExponent : "+rsaPublicKeyExponent);
	
	jsonObj.put("existPrivateKey", "false");
	jsonObj.put("rsaPublicKeyModulus", rsaPublicKeyModulus);
	jsonObj.put("rsaPublicKeyExponent", rsaPublicKeyExponent);  
	
	ResponseUtil.processResponse(response, jsonObj);
%>