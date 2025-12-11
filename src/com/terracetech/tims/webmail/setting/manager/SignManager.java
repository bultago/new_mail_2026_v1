/**
 * SignManager.java 2008. 11. 26.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.setting.manager;

import java.util.List;

import com.terracetech.tims.webmail.setting.dao.SignDataDao;
import com.terracetech.tims.webmail.setting.vo.SignDataVO;
import com.terracetech.tims.webmail.setting.vo.SignVO;

/**
 * <p>
 * <strong>SignManager.java</strong> Class Description
 * </p>
 * <p>
 * ÁÖ¿ä¼³¸í
 * </p>
 * <ul>
 * <li></li>
 * </ul>
 * 
 * @author jpjung
 * @since Tims7
 * @version 7.0
 */
public class SignManager {

	private SignDataDao signDao;

	public void setSignDao(SignDataDao signDao) {
		this.signDao = signDao;
	}
	
	public SignVO getSignInfo(int userSeq) throws Exception {
		return signDao.readSign(userSeq);
	}
	
	public void saveSignInfo(SignVO signVo) throws Exception {
		signDao.saveSign(signVo);
	}
	
	public void updateSignInfo(SignVO signVo) throws Exception {
		signDao.updateSign(signVo);
	}
	
	public void saveSignData(SignDataVO signDataVo) throws Exception {
		if ("T".equals(signDataVo.getDefaultSign())) {
			signDao.updateCheckAllNotDefault(signDataVo.getUserSeq());
		}
		signDao.saveSignData(signDataVo);
	}
	
	public SignDataVO getSignData(int userSeq, int signSeq) throws Exception {
		return signDao.readSignData(userSeq, signSeq);
	}
	
	public void modifySignData(SignDataVO signDataVo) throws Exception {
		if ("T".equals(signDataVo.getDefaultSign())) {
			signDao.updateCheckAllNotDefault(signDataVo.getUserSeq());
		}
		signDao.modifySignData(signDataVo);
	}
	
	public void deleteSignData(int userSeq, int[] signSeqs) throws Exception {
		signDao.deleteSignData(userSeq, signSeqs);
	}
	
	public List<SignDataVO> getSignDataList(int userSeq) throws Exception {
		return signDao.readSignDataList(userSeq);
	}
	
	public List<SignDataVO> getSignSimpleDataList(int userSeq) throws Exception {
		return signDao.readSignSimpleDataList(userSeq);
	}
	
	public SignDataVO getDefaultSignData(int userSeq) throws Exception {
		return signDao.readDefaultSignData(userSeq);
	}
}
