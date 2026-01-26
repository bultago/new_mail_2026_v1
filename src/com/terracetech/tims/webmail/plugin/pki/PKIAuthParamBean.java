package com.terracetech.tims.webmail.plugin.pki;

public class PKIAuthParamBean {
	private String signedText = null;
	private String signedVid = null;
	private String ssn = null;
	private String userDN = null;
	public String getSignedText() {
		return signedText;
	}
	public void setSignedText(String signedText) {
		this.signedText = signedText;
	}
	public String getSignedVid() {
		return signedVid;
	}
	public void setSignedVid(String signedVid) {
		this.signedVid = signedVid;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getUserDN() {
		return userDN;
	}
	public void setUserDN(String userDN) {
		this.userDN = userDN;
	}
	
	
	
}
