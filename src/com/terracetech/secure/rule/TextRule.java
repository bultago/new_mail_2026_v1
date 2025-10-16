/**
 * LengthRule.java 2009. 1. 21.
 *
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 *
 */
package com.terracetech.secure.rule;

import java.io.UnsupportedEncodingException;

/**
 * <p><strong>LengthRule.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author ysko
 * @since Tims7
 * @version 7.0
 */
public class TextRule implements Rule {

	public final static String RULE_NAME = "TEXT";

	private int minAllowCnt;

	@Override
	public String getRuleName() {
		return RULE_NAME;
	}

	/**
	 * ���ڰ� �ּ� minAllowCnt��ŭ�� �־�� �Ѵ�.
	 */
	@Override
	public boolean validate(String input) throws UnsupportedEncodingException {
		if (input == null)
			return false;

		int successCnt = 0;
		byte[] byteValue = RuleUtils.getByteCode(input);
		for (byte b : byteValue) {
			if (65 <= b && 90 >= b) {
				successCnt++;
			}

			if (97 <= b && 122 >= b) {
				successCnt++;
			}
		}

		if(successCnt < minAllowCnt)
		{
			errCode = "RE100";
			return false;
		}

		return true ;
	}

	private String errCode = "";
	@Override
	public String getErrorCode() {
		return errCode;
	}

	public int getMinAllowCnt() {
		return minAllowCnt;
	}

	public void setMinAllowCnt(int minAllowCnt) {
		this.minAllowCnt = minAllowCnt;
	}

}