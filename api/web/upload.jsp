<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.DataInputStream"%>
<%@page import="java.util.Enumeration"%>	
<%@ page import="com.terracetech.tims.webmail.util.StringUtils"%>
<%! 
public static String byteArrayToHexString(byte[] bytes){ 
		
		StringBuilder sb = new StringBuilder(); 
		
		for(byte b : bytes){ 
			
			sb.append(String.format("%02X", b&0xff)); 
		} 
		
		return sb.toString(); 
}
%>

<%
/*
    FileDrop Revamped - server-side upload handler sample
    in public domain  | http://filedropjs.org

    ***

    This is an example of server-side script that handles both AJAX and IFrame uploads.

    AJAX upload provides raw file data as POST input while IFrame is a POST request
    with Request.Files member set.

    Result is either output as HTML with JavaScript code to invoke the callback
    (like JSONP) or in plain text if none is given (it's usually absent on AJAX).
*/

// Callback name is passed if upload happens via iframe, not AJAX (FileAPI).
String callback = request.getParameter("fd-callback");
String name;
byte[] data;

// Upload data can be POST'ed as raw form data or uploaded via <iframe> and <form>
// using regular multipart/form-data enctype 
String cotentType = request.getContentType();
Enumeration e = request.getHeaderNames();

//헤더 정보 
while(e.hasMoreElements()) {
	String key = (String)e.nextElement();
	String value = request.getHeader(key);
	System.out.println(key + ":" + value + "<br>");
}
DataInputStream fos    = new DataInputStream(request.getInputStream());

//서버의 디스크에 파일 저장
FileOutputStream fs = new FileOutputStream("c:\\test.txt");
int data;
while((data=fos.read()) != -1) {
	fs.write(data);
	fs.flush();
}
fs.close();

String output = "";
//string.Format("{0}; received {1} bytes, MD5 = {2}", name, data.Length, BytesArrayToHexString(md5Hash));
String opt = request.getParameter("upload_option");
if (StringUtils.isNotEmpty(callback)){
    output += "\nReceived upload_option with value " + opt;
}

if (StringUtils.isNotEmpty(callback)){
    response.setHeader("Content-Type", "text/plain; charset=utf-8");
    out.println("<!DOCTYPE html><html><head></head><body><script type=\"text/javascript\">" +
       "try{window.top." + callback + "('" + output + "')}catch(e){}</script></body></html>");
}
else{
	response.setHeader("Content-Type", "text/plain; charset=utf-8");
	out.println(output);
}

%>