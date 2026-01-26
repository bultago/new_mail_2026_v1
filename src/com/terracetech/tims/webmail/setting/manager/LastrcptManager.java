/**
 * LastrcptManager.java 2008. 12. 3.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.setting.manager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.mail.internet.InternetAddress;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;
import com.terracetech.tims.webmail.setting.dao.LastrcptDao;
import com.terracetech.tims.webmail.setting.vo.RcptVO;

/**
 * <p><strong>LastrcptManager.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author jpjung
 * @since Tims7
 * @version 7.0 
 */
public class LastrcptManager {

	private LastrcptDao dao;
	
	public void setDao(LastrcptDao dao) {
		this.dao = dao;
	}
	
	@Transactional
	public void saveLastRcpt(int userSeq, InternetAddress[] addressList){
		
		RcptVO vo = null;
		if(addressList != null){
			dao.deleteLastRcpt(userSeq, addressList);
			for (InternetAddress address : addressList){
				vo = new RcptVO();
				vo.setUserSeq(userSeq);
				vo.setLastrcptEmail(address.getAddress());
				vo.setLastrcptPersonnel(address.getPersonal());
				try {
					//addressList�������� �ߺ��� üũ�ϴ� �ͺ��� �������� �����ϴ°� ���ƺ���
					dao.saveLastRcpt(vo);	
				} catch (Exception ignore) {
				}
			}
		}
	}
	
	public List<RcptVO> readLastRcpt(int userSeq){
		return dao.readLastRcpt(userSeq);
	}
	public List<RcptVO> readLastRcptByMaxRcptCount(int userSeq, int maxRcptCount){
		return dao.readLastRcptByMaxRcptCount(userSeq, maxRcptCount);
	}
	
	public MailAddressBean[] getLastRcptAddrs(int userSeq){
		List<RcptVO> list = readLastRcpt(userSeq);
		int size = list.size();
		MailAddressBean[] beans = new MailAddressBean[size];
		RcptVO tmpVo = null;
		for (int i = 0; i < size; i++) {
			tmpVo = list.get(i);
			beans[i] = new MailAddressBean();
			beans[i].setEmail(tmpVo.getLastrcptEmail());
			beans[i].setName(tmpVo.getLastrcptPersonnel());
		}
		
		return beans;	
	}
	
	public void deleteLastRcpt(int userSeq, String[] rcptSeq){				
		if(rcptSeq != null){
			dao.deleteLastRcpt(userSeq, rcptSeq);			
		}
	}
	public void deleteLastRcptAll(int userSeq){				
		dao.deleteLastRcpt(userSeq);			
	}
	public List<RcptVO> getLastRcptAddress(int userSeq){
		List<RcptVO> list = readLastRcpt(userSeq);
		return list;
	}
	public List<RcptVO> getLastRcptAddressByMaxRcptCount(int userSeq, int maxRcptCount){
		List<RcptVO> list = readLastRcptByMaxRcptCount(userSeq, maxRcptCount);
		return list;
	}
}
