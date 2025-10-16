/**
 * AllowPolicy.java 2009. 1. 12.
 *
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 *
 */
package com.terracetech.secure.policy;

import java.util.ArrayList;
import java.util.List;

import com.terracetech.secure.rule.NumberRule;
import com.terracetech.secure.rule.Rule;
import com.terracetech.secure.rule.SpaceRule;
import com.terracetech.secure.rule.SymbolRule;
import com.terracetech.secure.rule.TextRule;

/**
 * <p><strong>AllowPolicy.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author ysko
 * @since Tims7
 * @version 7.0
 */
public class AllowPolicy extends Policy {

	public final static String NAME = "allow" ;

	public final static String CASE_SENSITIVE = "case_sensitive";
	public final static String NUMBER = "number";
	public final static String SYMBOL = "symbol";
	public final static String SPACE = "space";
	public final static String MIN_ALLOW_COUNT = "min_allow_count";

	private boolean caseSensitived, numberUsed, symbolUsed, spaceUsed;

	private int minAllowCnt;

	private boolean textUsed; 	//20160421
	public final static String TEXT = "text"; //20160421

	public static AllowPolicy getDefault () {
		AllowPolicy policy = new AllowPolicy();
		policy.policyUsed = true;
		policy.minAllowCnt = 1;
		policy.numberUsed = true;
		policy.textUsed = true; //20160421
		return policy;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getValue() {
		StringBuffer tmpValue = new StringBuffer();

		makeStr(tmpValue, true, POLICY_USED + SECOND_DELIM + policyUsed);
		makeStr(tmpValue, caseSensitived, CASE_SENSITIVE);
		makeStr(tmpValue, numberUsed, NUMBER);
		makeStr(tmpValue, symbolUsed, SYMBOL);
		makeStr(tmpValue, spaceUsed, SPACE);
		makeStr(tmpValue, policyUsed, MIN_ALLOW_COUNT + SECOND_DELIM + minAllowCnt);
		makeStr(tmpValue, textUsed, TEXT); //20150421

		return tmpValue.toString();
	}

	@Override
	public void setValue(String value) {
		if (value == null || value.equals(""))
			return;

		String[] tmpStrings = value.split(DELIM);
		for (int i = 0, cnt = tmpStrings.length; i < cnt; i++) {
			if (tmpStrings[i].contains(POLICY_USED)) {
				policyUsed = "true".equals(tmpStrings[i].split(SECOND_DELIM)[1]);
			} else if (tmpStrings[i].contains(CASE_SENSITIVE)) {
				caseSensitived = true;
			} else if (tmpStrings[i].contains(NUMBER)) {
				numberUsed = true;
			} else if (tmpStrings[i].contains(SYMBOL)) {
				symbolUsed = true;
			} else if (tmpStrings[i].contains(SPACE)) {
				spaceUsed = true;
			} else if (tmpStrings[i].contains(MIN_ALLOW_COUNT)) {
				minAllowCnt = Integer.parseInt(tmpStrings[i].split(SECOND_DELIM)[1]);
			} else if (tmpStrings[i].contains(SPACE)) {
				spaceUsed = true;
			} else if (tmpStrings[i].contains(TEXT)) { //20160421
				textUsed = true;
			}
		}
	}

	@Override
	protected Rule[] getRules(){
		List<Rule> rules = new ArrayList<Rule>();

		if(isNumberUsed()){
			NumberRule rule1 = new NumberRule();
			rule1.setMinAllowCnt(minAllowCnt);

			rules.add(rule1);
		}

		if(isSymbolUsed()){
			SymbolRule rule2 = new SymbolRule();
			rule2.setMinAllowCnt(minAllowCnt);

			rules.add(rule2);
		}

		if(isSpaceUsed()){
			SpaceRule rule3 = new SpaceRule();
			rule3.setMinAllowCnt(minAllowCnt);

			rules.add(rule3);
		}

		if(isTextUsed()){ //20160421
			TextRule rule4 = new TextRule();
			rule4.setMinAllowCnt(minAllowCnt);

			rules.add(rule4);
		}

		return rules.toArray(new Rule[rules.size()]);
	}

	public boolean isCaseSensitived() {
		return caseSensitived;
	}

	public void setCaseSensitived(boolean caseSensitived) {
		this.caseSensitived = caseSensitived;
	}

	public boolean isNumberUsed() {
		return numberUsed;
	}

	public void setNumberUsed(boolean numberUsed) {
		this.numberUsed = numberUsed;
	}

	public boolean isSymbolUsed() {
		return symbolUsed;
	}

	public void setSymbolUsed(boolean symbolUsed) {
		this.symbolUsed = symbolUsed;
	}

	public boolean isSpaceUsed() {
		return spaceUsed;
	}

	public void setSpaceUsed(boolean spaceUsed) {
		this.spaceUsed = spaceUsed;
	}

	public int getMinAllowCnt() {
		return minAllowCnt;
	}

	public void setMinAllowCnt(int minAllowCnt) {
		this.minAllowCnt = minAllowCnt;
	}

	//20160421
	public boolean isTextUsed() {
		return textUsed;
	}

	//20160421
	public void setTextUsed(boolean textUsed) {
		this.textUsed = textUsed;
	}
}