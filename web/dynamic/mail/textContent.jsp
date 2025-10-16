<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%
    String locale = request.getParameter("locale");
    locale = StringEscapeUtils.escapeHtml(locale);
%>
<html>
<head>
<script type="text/javascript" src="/js/core-lib/jquery-1.3.2.js"></script>
<link rel="stylesheet" type="text/css" href="/design/common/css/font_<%=locale%>.css"/>
<link rel="stylesheet" type="text/css" href="/design/common/css/common.css"/>

<script type="text/javascript">	
function setContent(txt){
	document.textContentForm.content_text.value = txt;
}
function getContent(){
	return document.textContentForm.content_text.value;
}
function setFrameFocus(){
    document.textContentForm.content_text.focus();
}

function init(){
    	try{
	    	if(parent)
	        	parent.contentDataCheck();
    	}catch(e){}
        setTimeout(function(){
        	try {
	            if (parent.getWriteType() == "text" && parent.getWriteMode() != "forward" && getContent() != "") {
	                setSelRange(document.textContentForm.content_text,0,0);
	            }
        	} catch (e) {}
        },1000);    
}

function setSelRange(inputEl, selStart, selEnd) { 
     if (inputEl.setSelectionRange) { 
      inputEl.focus(); 
      inputEl.setSelectionRange(selStart, selEnd); 
     } else if (inputEl.createTextRange) { 
      var range = inputEl.createTextRange(); 
      range.collapse(true); 
      range.moveEnd('character', selEnd); 
      range.moveStart('character', selStart); 
      range.select(); 
     } 
    }
</script>
</head>

<body onload="init();">
<form name="textContentForm" id="textContentForm">
<textarea name="content_text" id="content_text" style="width:100%;height:360px;border:none; background:#ffffff"></textarea>
</form>

</body>
</html>
