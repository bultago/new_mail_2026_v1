/**
 * AttachTerm.java 2008. 12. 1.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.mail;

import jakarta.mail.Message;
import jakarta.mail.search.SearchTerm;

/**
 * <p><strong>AttachTerm.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class AttachTerm extends SearchTerm {
	
	public static final int FILE_NAME = 0;
	public static final int FILE_SIZE_SMALL = 1;
	public static final int FILE_SIZE_LARGER= 2;
	public static final int FILE_COUNT = 3;
	public static final int FILE_CONTENT = 4;
	
	private String pattern = null;
	private int type = 0;
	/**
	 * <p></p>
	 *
	 */
	public AttachTerm(String pattern, int type) {
		this.pattern = pattern;
		this.type = type;
	}
	
	
	/**
	 * @return pattern �� ��ȯ
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @return type �� ��ȯ
	 */
	public int getType() {
		return type;
	}
	
	public String getAtomString(){
		String atomStr = null;
		switch (type) {
		case FILE_NAME:
			atomStr = "X-ATT-NAME";
			break;
		case FILE_SIZE_SMALL:
			atomStr = "X-ATT-SIZE SMALLER";
			break;
		case FILE_SIZE_LARGER:
			atomStr = "X-ATT-SIZE LARGER";
			break;
		case FILE_COUNT:
			atomStr = "X-ATT-COUNT";
			break;
		case FILE_CONTENT:
			atomStr = "X-ATT";
			break;

		default:
			break;
		}
		
		return atomStr;
	}


	/**
	 * <p></p>
	 *
	 * @see javax.mail.search.SearchTerm#match(javax.mail.Message)
	 * @param arg0
	 * @return 
	 */
	@Override
	public boolean match(Message arg0) {		
		return false;
	}

	
}
