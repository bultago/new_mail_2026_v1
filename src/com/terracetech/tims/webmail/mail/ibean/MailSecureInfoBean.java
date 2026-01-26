package com.terracetech.tims.webmail.mail.ibean;

import java.util.Iterator;
import java.util.Map;

import com.terracetech.tims.webmail.common.EnvConstants;

public class MailSecureInfoBean {
	
	
	private String secureFormFile = EnvConstants.DIR_SEPARATOR + "securemail" +
									EnvConstants.DIR_SEPARATOR + "secureForm.html";
	private String htmlContentFile = EnvConstants.DIR_SEPARATOR + "securemail" +
									EnvConstants.DIR_SEPARATOR + "contentForm.html";
	private String textContentFile = EnvConstants.DIR_SEPARATOR + "securemail" +
									EnvConstants.DIR_SEPARATOR + "contentForm.txt";	
	private String contentPKIFile = EnvConstants.DIR_SEPARATOR + "securemail" +
									EnvConstants.DIR_SEPARATOR + "contentPKIForm.html";
	
	private String jpFont = "\"MS PGothic\",\"Osaka\", \"Sans-serif\"";
	private String etcFont = "dotum, Arial, Helvetica, sans-serif";	
	private Map<String, String> msgMap = null;
	private String secureMailPassword = null;
	private String secureMailHint = null;
	
	private String sslHost = null; 	
	private String webRootPath = null;
	private String cryptMethod = "AES";
	private String module = null;
	private boolean htmlMode = false;
	private String locale = null;
	
	public String getSecureMailPassword() {
		return secureMailPassword;
	}
	public void setSecureMailPassword(String secureMailPassword) {
		this.secureMailPassword = secureMailPassword;
	}
	public String getSecureMailHint() {
		return secureMailHint;
	}
	public void setSecureMailHint(String secureMailHint) {
		this.secureMailHint = secureMailHint;
	}	
	public String getSslHost() {
		return sslHost;
	}
	public void setSslHost(String sslHost) {
		this.sslHost = sslHost;
	}
	public String getWebRootPath() {
		return webRootPath;
	}
	public void setWebRootPath(String webRootPath) {
		this.webRootPath = webRootPath;
	}
	public String getSecureFormFile() {
		return webRootPath+secureFormFile;
	}
	public String getContentFile() {
		return webRootPath+((htmlMode)?htmlContentFile:textContentFile);
	}
	
	public String getContentPKIFile() {
		return webRootPath+contentPKIFile;
	}
	public String getCryptMethod() {
		return cryptMethod;
	}
	public void setCryptMethod(String cryptMethod) {
		this.cryptMethod = cryptMethod;
	}
	public void setMsgMap(Map<String, String> msgMap) {
		this.msgMap = msgMap;
	}
	
	public String getSecureMailMsg(String content){
		Iterator<String> keyIterator = msgMap.keySet().iterator();
		String key = null;
		while (keyIterator.hasNext()) {			
			key = keyIterator.next();
			content = content.replaceAll("\\{"+key+"\\}",msgMap.get(key));
		}
		
		return content;	
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public void setHtmlMode(boolean htmlMode) {
		this.htmlMode = htmlMode;
	}
	
	public void setLocale(String locale){
		this.locale = locale;
	}
	
	public String getFontStyle(){
		if("jp".equals(locale) || "ja".equals(locale)){
			return jpFont;
		} else {
			return etcFont;
		}
	}	
	
}
