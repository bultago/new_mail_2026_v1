package com.terracetech.tims.mail;

import jakarta.mail.Message;
import jakarta.mail.search.SearchTerm;

public class MyselfSearchTerm extends SearchTerm {
	private String pattern = null;
	
	public MyselfSearchTerm(String pattern) {
		this.pattern = pattern;
	}
	
	/**
	 * @return pattern �� ��ȯ
	 */
	public String getPattern() {
		return pattern;
	}
	
	public String getAtomString(){
		return "X-2ME";
		
	}
	
	@Override
	public boolean match(Message paramMessage) {
		return false;
	}

}
