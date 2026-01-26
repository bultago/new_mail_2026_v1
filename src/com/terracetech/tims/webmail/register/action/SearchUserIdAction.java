package com.terracetech.tims.webmail.register.action;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class SearchUserIdAction extends BaseAction {
	
	private MailUserManager mailUserManager = null;
	private String ssn = null;
	private String empno = null;
	private String userName = null;
	private String mailDomain = null;
	private String mailUid = null;
	private String checkType = null;
	private String postalCode = null;
	private String birthday = null;

	public SearchUserIdAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}
	
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	
	public String execute() throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		boolean isSuccess = true;
		String msg = "";
		
		try {
			if (StringUtils.isNotEmpty(mailDomain)) {
				int domainSeq = mailUserManager.searchMailDomainSeq(mailDomain);
				if (domainSeq <= 0) {
					isSuccess = false;
					msg = "empty";
				} else {
					if ("jp".equalsIgnoreCase(checkType)) {
						mailUid = mailUserManager.searchUserIdByJpInfo(userName, postalCode, birthday, domainSeq);
					} else if (StringUtils.isNotEmpty(empno)) {
						mailUid = mailUserManager.searchUserIdByEmpno(userName, empno, domainSeq);
					} else {
						mailUid = mailUserManager.searchUserId(userName, ssn, domainSeq);
					}
					if (StringUtils.isEmpty(mailUid)) {
						isSuccess = false;
						msg = "fail";
					} else {
						isSuccess = true;
						msg = mailUid;
					}
				}
			}
		} catch (Exception e) {
			isSuccess = false;
			msg = "error";
		}
		jsonObj.put("isSuccess", isSuccess);
		jsonObj.put("msg", msg);
		
		ResponseUtil.processResponse(response, jsonObj);
		
		return null;
	}
	
	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMailDomain() {
		return mailDomain;
	}

	public void setMailDomain(String mailDomain) {
		this.mailDomain = mailDomain;
	}

	public String getMailUid() {
		return mailUid;
	}

	public void setMailUid(String mailUid) {
		this.mailUid = mailUid;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	public String getEmpno() {
		return empno;
	}

	public void setEmpno(String empno) {
		this.empno = empno;
	}
}
