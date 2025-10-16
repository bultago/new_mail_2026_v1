/**
 * ISettingForwardDao.java 2008. 12. 2.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.setting.dao;

import java.util.List;

import com.terracetech.tims.webmail.setting.vo.DefineForwardingInfoVO;
import com.terracetech.tims.webmail.setting.vo.ForwardingInfoVO;

/**
 * <p><strong>ISettingForwardDao.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author ysko
 * @since Tims7
 * @version 7.0 
 */
public interface ISettingForwardDao {

	public abstract ForwardingInfoVO readForwardInfo(int userSeq);

	public abstract void modifyForwardInfo(ForwardingInfoVO vo);
	
	public void modifyDefineForwardingInfo(DefineForwardingInfoVO vo);
	
	public List<DefineForwardingInfoVO> readDefineForwarding(int mail_user_seq);
	
	public List<DefineForwardingInfoVO> readDefineForwardingByForwardSeq(int define_forward_seq);

}