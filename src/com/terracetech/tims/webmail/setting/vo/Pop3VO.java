package com.terracetech.tims.webmail.setting.vo;

public class Pop3VO {

	private int userSeq;
	
	private String pop3Host = null;
	
	private int pop3Port;
	
	private String pop3Id = null;
	
	private String pop3Passwd = null;
	
	private String pop3Boxname = null;
	
	private String pop3Del = null;
	
	private String pop3Msgname = null;
		
	//ssl »ç¿ë ¿©ºÎ
    private String usedSsl = null;

	public int getUserSeq() {
		return userSeq;
	}

	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}

	public String getPop3Host() {
		return pop3Host;
	}

	public void setPop3Host(String pop3Host) {
		this.pop3Host = pop3Host;
	}

	public int getPop3Port() {
		return pop3Port;
	}

	public void setPop3Port(int pop3Port) {
		this.pop3Port = pop3Port;
	}

	public String getPop3Id() {
		return pop3Id;
	}

	public void setPop3Id(String pop3Id) {
		this.pop3Id = pop3Id;
	}

	public String getPop3Passwd() {
		return pop3Passwd;
	}

	public void setPop3Passwd(String pop3Passwd) {
		this.pop3Passwd = pop3Passwd;
	}

	public String getPop3Boxname() {
		return pop3Boxname;
	}

	public void setPop3Boxname(String pop3Boxname) {
		this.pop3Boxname = pop3Boxname;
	}

	public String getPop3Del() {
		return pop3Del;
	}

	public void setPop3Del(String pop3Del) {
		this.pop3Del = pop3Del;
	}

	public String getPop3Msgname() {
		return pop3Msgname;
	}

	public void setPop3Msgname(String pop3Msgname) {
		this.pop3Msgname = pop3Msgname;
	}

	public String getUsedSsl() {
		return usedSsl;
	}

	public void setUsedSsl(String usedSsl) {
		this.usedSsl = usedSsl;
	}
	
	
	
}
