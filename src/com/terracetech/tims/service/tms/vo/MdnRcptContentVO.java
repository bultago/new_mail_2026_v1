package com.terracetech.tims.service.tms.vo;

public class MdnRcptContentVO {
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
	 * @return code 값 반환
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code 파라미터를 code값에 설정
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return personal 값 반환
	 */
	public String getPersonal() {
		return personal;
	}

	/**
	 * @param personal 파라미터를 personal값에 설정
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
