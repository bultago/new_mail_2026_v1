package com.terracetech.tims.webmail.mail.ibean;

import java.io.Serializable;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.util.StringUtils;

public class MailAddressBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name = null;
	private String email = null;	
	private String deptCode = null;	
	private String deptName = null;
	private String title = null;
	private boolean isOrgInfo = false;
	
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = StringUtils.replace(name, "\"", "");;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isOrgInfo(){
		return isOrgInfo;		
	}
	
	public String getAddress() {
		StringBuffer sb = new StringBuffer();
		if(name != null && name.length() > 0){
			sb.append("\"");
			
			sb.append(StringUtils.EscapeQuot(name));
			if(StringUtils.isNotEmpty(title)){
				sb.append( "/" + StringUtils.EscapeQuot(title));
			}
			if(StringUtils.isNotEmpty(deptName)){
				sb.append( "/" + StringUtils.EscapeQuot(deptName));
			}
			sb.append("\" <");
			sb.append(email);
			sb.append(">");
		} else {
			sb.append(email);
		}
		
		return sb.toString();
	}
	
	public JSONObject toJson() {
		JSONObject jObj = new JSONObject();
		String emailParam = "";
		if(name != null && name.length() > 0){
			emailParam = "\"";
			emailParam += StringUtils.EscapeQuot(name);
			jObj.put("name", StringUtils.EscapeQuot(name));
			if(StringUtils.isNotEmpty(title)){				
				jObj.put("title",StringUtils.EscapeQuot(title));				
				emailParam += "/"+StringUtils.EscapeQuot(title);
				isOrgInfo = true;
			}
			if(StringUtils.isNotEmpty(deptName)){				
//				jObj.put("dept",StringUtils.EscapeQuot(dept));				
//				emailParam += "/"+StringUtils.EscapeQuot(dept);
				isOrgInfo = true;
			}
			emailParam += "\" <" + email +">";
			jObj.put("emailValue",emailParam);
			jObj.put("email",email);
		} else {
			emailParam = "<"+email+">";
			jObj.put("orginfo","false");
			jObj.put("emailValue",emailParam);
		}		
		return jObj;
	}
	
	public String toString() {
		return "\""+ name + "\"" + " <" + email + ">";
	}
	
	
}
