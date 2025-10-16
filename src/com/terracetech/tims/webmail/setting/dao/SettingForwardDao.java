/**
 * SettingForwardDao.java 2008. 12. 2.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.setting.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.setting.vo.DefineForwardingInfoVO;
import com.terracetech.tims.webmail.setting.vo.ForwardingInfoVO;

/**
 * <p><strong>SettingForwardDao.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author ysko
 * @since Tims7
 * @version 7.0 
 */
public class SettingForwardDao extends SqlMapClientDaoSupport implements ISettingForwardDao {

	@SuppressWarnings("unchecked")
	public ForwardingInfoVO readForwardInfo(int userSeq) {
		List<String> forwardingAddress = getSqlMapClientTemplate().queryForList("Setting.readForwardAddressList", userSeq); 
		Object result = getSqlMapClientTemplate().queryForObject("Setting.readForwardMode", userSeq);
		String forwardingMode = result instanceof String ? (String)result : "none";
		
		ForwardingInfoVO vo = new ForwardingInfoVO();
		vo.setUserSeq(userSeq);
		vo.setForwardingMode(forwardingMode);
		vo.setForwardingAddress(forwardingAddress.toArray(new String[forwardingAddress.size()]));
		return vo;
	}
	
	@SuppressWarnings("unchecked")
	public void modifyForwardInfo(ForwardingInfoVO vo) {

		getSqlMapClientTemplate().update("Setting.modifyForwardMode", vo);
		
		getSqlMapClientTemplate().delete("Setting.deleteForwardAddressList", vo.getUserSeq());
		
		Map param = new HashMap();
		param.put("userSeq", vo.getUserSeq());
		
		if (vo.getForwardingAddress() != null && vo.getForwardingAddress().length > 0) {
			for (String forwardAddress: vo.getForwardingAddress()) {
				param.put("forwardingAddress", forwardAddress);
				getSqlMapClientTemplate().insert("Setting.saveForwardAddress", param);
			}
		}
	}

	public void modifyDefineForwardingInfo(DefineForwardingInfoVO vo) {			
		if(vo.getDefine_forwarding_seq() > 0){
			super.getSqlMapClientTemplate().delete("Setting.deleteDefineForwardingAddress", vo.getDefine_forwarding_seq());
		}
		
		int defineForwardSeq = (Integer)super.getSqlMapClientTemplate().queryForObject("Setting.selectDefineForwardSeq", null);
		
		Map paramMap = new HashMap();
		paramMap.put("mail_user_seq", vo.getMail_user_seq());
		paramMap.put("define_forwarding_seq", defineForwardSeq);
		paramMap.put("from_address", vo.getFrom_address());	
		paramMap.put("from_domain", vo.getFrom_domain());	
		
		for(String addr: vo.getForwarding_address_arr()){
			paramMap.put("forwarding_address", addr);
			super.getSqlMapClientTemplate().insert("Setting.saveDefineForwardingAddress", paramMap);
		}		
	}
	
	public List<DefineForwardingInfoVO> readDefineForwarding(int mail_user_seq){
		return super.getSqlMapClientTemplate().queryForList("Setting.selectDefineForwardingAddress", mail_user_seq);
	}
	
	public List<DefineForwardingInfoVO> readDefineForwardingByForwardSeq(int define_forward_seq){
		return super.getSqlMapClientTemplate().queryForList("Setting.selectDefineForwardingAddressForForwardSeq",define_forward_seq);
	}
	
	public void deleteDefineForwarding(int[] defineForwardingSeq){
		for(int i = 0; i < defineForwardingSeq.length; i++){
			super.getSqlMapClientTemplate().delete("Setting.deleteDefineForwardingAddress", defineForwardingSeq[i]);
		}
	}
	
	public int checkValidationDefineForwarding(int mailUserSeq, String defineValue){
		Map paramMap = new HashMap();
		paramMap.put("mail_user_seq", mailUserSeq);
		paramMap.put("defineValue", defineValue);
		return (Integer)super.getSqlMapClientTemplate().queryForObject("Setting.checkValidationDefineForwarding", paramMap);
	}
}