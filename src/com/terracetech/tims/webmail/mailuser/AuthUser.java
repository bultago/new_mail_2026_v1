package com.terracetech.tims.webmail.mailuser;

import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;

public class AuthUser implements User {
	private int authResult ;
	private User user;
	private int accessDeniedTerm;
	private int failCount;
	private int failCurrCount;
	
	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

	public int getFailCurrCount() {
		return failCurrCount;
	}

	public void setFailCurrCount(int failCurrCount) {
		this.failCurrCount = failCurrCount;
	}

	public int getAccessDeniedTerm() {
		return accessDeniedTerm;
	}

	public AuthUser () {
		super();
	}
	public AuthUser (User user, int authResult) {
		this();
		this.authResult = authResult;
		this.user = user;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getAuthResult() {
		return authResult;
	}
	public void setAuthResult(int authResult) {
		this.authResult = authResult;
	}
	public void setAccessDeniedTerm(int accessDeniedTerm) {
		this.accessDeniedTerm = accessDeniedTerm;
	}
	public boolean isAuthSuccess () {
		return authResult == UserAuthManager.SUCCESS;
	}

	public String get(String key) {
		return user.get(key);
	}

	public void put(String key, Object value) {
		user.put(key, value);
	}

	public String[] getKeys() {
		return user.getKeys();
	}
	public String toString () {
		if(user == null)
			return "";
		
		return user.toString();
	}
	public void removeKey(String key) {
		user.removeKey(key);
	}
}
