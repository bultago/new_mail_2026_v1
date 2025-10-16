/**
 * ILastrcptDao.java 2008. 12. 3.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.setting.dao;

import java.util.List;

import javax.mail.internet.InternetAddress;

import com.terracetech.tims.webmail.setting.vo.RcptVO;

/**
 * <p><strong>ILastrcptDao.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author jpjung
 * @since Tims7
 * @version 7.0 
 */
public interface ILastrcptDao {

	public void saveLastRcpt(RcptVO[] vos);
	
	public void saveLastRcpt(RcptVO vo);
	
	public void deleteLastRcpt(int userSeq);
	
	public List<RcptVO> readLastRcpt(int userSeq);
	
	public List<RcptVO> readLastRcptByMaxRcptCount(int userSeq, int maxRcptCount);
	
	public boolean isExistLastRcpt(int userSeq, String email);

	public void deleteLastRcpt(int userSeq, InternetAddress[] addressList);
	
	public void deleteLastRcpt(int userSeq, String[] rcptSeq);
}
