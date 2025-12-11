/**
 * MDNResponsesRcptVO.java 2008. 12. 8.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.ibean;


/**
 * <p><strong>MDNResponsesRcptVO.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author jpjung
 * @since Tims7
 * @version 7.0 
 */
public class MDNResponsesRcptBean {

	private boolean isLocalDomain = false;
	
	private String code = null;
	
	private String personal = null;
	
	private String address = null;
	
	private String status = null;
	
	private String message = null;
	
	private String processDate = null;

	public boolean isLocalDomain() {
		return isLocalDomain;
	}

	public void setLocalDomain(boolean isLocalDomain) {
		this.isLocalDomain = isLocalDomain;
	}	

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getProcessDate() {
		return processDate;
	}

	public void setProcessDate(String processDate) {
		this.processDate = processDate;
	}

	/**
	 * @return code °ª ¹ÝÈ¯
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code ÆÄ¶ó¹ÌÅÍ¸¦ code°ª¿¡ ¼³Á¤
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return personal °ª ¹ÝÈ¯
	 */
	public String getPersonal() {
		return personal;
	}

	/**
	 * @param personal ÆÄ¶ó¹ÌÅÍ¸¦ personal°ª¿¡ ¼³Á¤
	 */
	public void setPersonal(String personal) {
		this.personal = personal;
	}
	
	public String getPrintAddress(){
		StringBuffer sb = new StringBuffer();
		if(personal != null && personal.length() > 0){
			sb.append("\"");
			sb.append(personal);
			sb.append("\"<");
			sb.append(address);
			sb.append(">");
		} else {
			sb.append(address);
		}
		
		return sb.toString();
	}
	
}
