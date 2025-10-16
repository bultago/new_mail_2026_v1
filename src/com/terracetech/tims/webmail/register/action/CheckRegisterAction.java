package com.terracetech.tims.webmail.register.action;
import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.register.utils.DummyChecker;
import com.terracetech.tims.webmail.register.utils.ICheckSsn;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class CheckRegisterAction extends BaseAction {

	private static final long serialVersionUID = 20091110L;
	
	private MailUserManager mailUserManager = null;
	private ICheckSsn checker = null;
	
	private String ssn = null;
	private String userName = null;
	private String domain = null;
	private String checkType = null;
	private String postalCode = null;
	private String birthday = null;
	private String empno = null;

	public CheckRegisterAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}
	
	public ICheckSsn getChecker() {
		if(checker ==null)
			return new DummyChecker();
		
		return checker;
	}

	public void setChecker(ICheckSsn checker) {
		this.checker = checker;
	}

	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	
	@SuppressWarnings("unchecked")
	public String execute() throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		boolean isSuccess = true;
		String validCheck = "";
		String msg = "";
		
		try {	
			if(StringUtils.isNotEmpty(domain)){
				int domainSeq = mailUserManager.searchMailDomainSeq(domain);
				if (domainSeq <= 0) {
					isSuccess = false;
					msg = "empty";
				} else {
					if ("jp".equalsIgnoreCase(checkType)) {
						if(mailUserManager.readUserInfoByJpInfo(domain, userName, postalCode, birthday)) {
							isSuccess = false;
							msg = "dup";
						}
					} else if (StringUtils.isNotEmpty(empno)) {
						if(mailUserManager.readUserInfoByEmpno(domain, empno)) {
							isSuccess = false;
							msg = "dup";
						}
					} else {
						if(mailUserManager.readUserInfoBySsn(domain, ssn)) {
							isSuccess = false;
							msg = "dup";
						}
					}
					validCheck = getChecker().checkSsn(domain, ssn, userName);
				}
			} else {
				isSuccess = false;
				msg = "empty";
			}
		} catch (Exception e) {
			isSuccess = false;
			msg = "error";
		}
		
		request.getSession().setAttribute("registssn", ssn);
		jsonObj.put("isSuccess", isSuccess);
		jsonObj.put("msg", msg);
		jsonObj.put("validCheck", validCheck);
		
		ResponseUtil.processResponse(response, jsonObj);
		
		return null;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setDomain(String domain) {
		this.domain = domain;
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
