/**
 * LastrcptDao.java 2008. 12. 3.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.setting.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.InternetAddress;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.setting.vo.RcptVO;

/**
 * <p><strong>LastrcptDao.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author jpjung
 * @since Tims7
 * @version 7.0 
 */
public class LastrcptDao extends SqlMapClientDaoSupport implements ILastrcptDao{

	/**
	 * <p></p>
	 *
	 * @param vos
	 * @return void
	 */
	public void saveLastRcpt(RcptVO[] vos) {
		for(RcptVO vo : vos)
			getSqlMapClientTemplate().insert("MailUser.insertLastRcpt", vo);
	}
	
	public void saveLastRcpt(RcptVO vo) {
		getSqlMapClientTemplate().insert("MailUser.insertLastRcpt", vo);
	}
	
	public boolean isExistLastRcpt(int userSeq, String email){
		Map<String, String> param = new HashMap<String, String>(2);
		param.put("userSeq", String.valueOf(userSeq));
		param.put("email", String.valueOf(email));
		
		int result = (Integer)getSqlMapClientTemplate().queryForObject("MailUser.getLastRcptCount", param); 
		
		return result==0 ? false : true;
	}

	/**
	 * <p></p>
	 *
	 * @param userSeq
	 * @return void
	 */
	public void deleteLastRcpt(int userSeq) {
		getSqlMapClientTemplate().delete("MailUser.deleteLastRcpt", userSeq);
	}
	

	public void deleteLastRcpt(int userSeq, InternetAddress[] addressList) {
		List<String> list = new ArrayList<String>();
		for (InternetAddress internetAddress : addressList) {
			list.add(internetAddress.getAddress());
		}
		
		Map<String, Object> param = new HashMap<String, Object>(2);
		//param.put("userSeq", String.valueOf(userSeq));
		param.put("userSeq", userSeq);
		param.put("emails", list.toArray(new String[list.size()]));
		
		getSqlMapClientTemplate().delete("MailUser.deleteLastRcpt2", param);
	}
	
	public void deleteLastRcpt(int userSeq, String[] rcptSeqs) {
		List<String> list = new ArrayList<String>();
		
		int[] rcptSequence = new int[rcptSeqs.length]; 
		for (String rcpt : rcptSeqs) {		    
		    list.add(rcpt);
		}
		
		for (int i = 0; i < list.size(); i++) {
		    rcptSequence[i] = Integer.parseInt(list.get(i));
		}
		
		Map<String, Object> param = new HashMap<String, Object>(2);
		param.put("userSeq", String.valueOf(userSeq));
		param.put("rcptSeqs", rcptSequence);
		
		getSqlMapClientTemplate().delete("MailUser.deleteLastRcptSeqs", param);
	}

	/**
	 * <p></p>
	 *
	 * @param userSeq
	 * @return
	 * @return RcptVO[]
	 */
	public List<RcptVO> readLastRcpt(int userSeq) {
		return getSqlMapClientTemplate().queryForList("MailUser.getLastRcpt", userSeq);
	}
	
	public List<RcptVO> readLastRcptByMaxRcptCount(int mailUserSeq, int maxRcptCount){
		Map<String, Object> param = new HashMap<String, Object>(2);
		param.put("mailUserSeq", mailUserSeq);
		param.put("maxRcptCount", maxRcptCount);
		
		return getSqlMapClientTemplate().queryForList("MailUser.getLastRcptByMaxRcptCount", param);
	}

	
}
