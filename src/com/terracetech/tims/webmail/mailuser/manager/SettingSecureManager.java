/**
 * SecureManager.java 2009. 1. 7.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mailuser.manager;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.terracetech.secure.exception.RuleException;
import com.terracetech.secure.policy.BasePolicy;
import com.terracetech.secure.policy.NotAllowPolicy;
import com.terracetech.secure.policy.Policy;
import com.terracetech.secure.policy.PolicyFactory;
import com.terracetech.tims.webmail.common.vo.MailConfigVO;
import com.terracetech.tims.webmail.mailuser.dao.MailUserDao;
import com.terracetech.tims.webmail.mailuser.dao.SettingSecureDao;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p><strong>SecureManager.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author ysko
 * @since Tims7
 * @version 7.0 
 */
public class SettingSecureManager {
	
	private SettingSecureDao settingSecureDao = null;
	
	private MailUserDao mailUserDao = null;

	public final static String DEFAULT_TARGET = "default";
	
	public final static String CRYPTO_ALGORITHM_PREPEND = "crypto_algorithm_";
	public final static String PASSWORD_POLICY_PREPEND = "password_policy_";
	public final static String WEB_MAIL = "webmail";
	public final static String ADMIN = "admin";
	public final static String PREPEND = "_";
	
	public void setSettingSecureDao(SettingSecureDao settingSecureDao) {
		this.settingSecureDao = settingSecureDao;
	}
	
	public void setMailUserDao(MailUserDao mailUserDao) {
		this.mailUserDao = mailUserDao;
	}

	public Policy[] readPasswordPolicies (String target) {
		
		return setPoliciesFromMailConfig(settingSecureDao.readPasswordPolicy());
	}
	
	private Policy[] setPoliciesFromMailConfig (MailConfigVO[] vos) {
		if (vos.length == 0)
			return PolicyFactory.getDefaultPolicies();
		
		Policy[] policies = new Policy[vos.length];
		for (int i = 0, cnt = vos.length; i < cnt; i++) {
			policies[i] = PolicyFactory.getPolicy(getPolicyNameFromConfigName(vos[i].getConfigName()));
			policies[i].setValue(vos[i].getConfigValue());
		}
		
		return policies;
	}
	
	private String getPolicyNameFromConfigName (String confingName) {
		return confingName.substring(confingName.lastIndexOf(PREPEND) + 1);
	}
	
	public Map<String, Policy> readPasswordPoliciesMap(String target){
		Map<String, Policy> map = new HashMap<String, Policy>();
		Policy[] policies = readPasswordPolicies(target);
		if(policies != null){
			for (Policy policy : policies) {
				map.put(policy.getName(), policy);
			}
		}
		BasePolicy bPolicy = (BasePolicy) map.get(BasePolicy.NAME);
		if(!bPolicy.isPolicyUsed())
			map.clear();
		
		return map;
	}
	
	public void validate(Map<String, Policy> map, String input, String id, String name) throws RuleException, UnsupportedEncodingException{
		if(map != null){
			Collection<Policy> policies = map.values();
			for (Policy policy : policies) {
				if(policy instanceof NotAllowPolicy){
					NotAllowPolicy naPolicy = (NotAllowPolicy)policy;
					if(StringUtils.isNotEmpty(id))
						naPolicy.setUserId(id);
					
					if(StringUtils.isNotEmpty(name))
						naPolicy.setUserName(name);
				}
				policy.validate(input);
			}
		}
	}
	
	public String readMailUid(int mailUserSeq){
		return mailUserDao.readMailUid(mailUserSeq);
	}
	
	public String readMailUserName(int mailUserSeq){
		return mailUserDao.readMailUserName(mailUserSeq);
	}
	
}