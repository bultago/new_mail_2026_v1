/**
 * SignImageDao.java 2008. 11. 26.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.setting.dao;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.setting.vo.SignDataVO;
import com.terracetech.tims.webmail.setting.vo.SignVO;

/**
 * <p>
 * <strong>SignImageDao.java</strong> Class Description
 * </p>
 * <p>
 * 사용자 Sign 이미지를 DB로 관리를 하는 DAO
 * </p>
 * <ul>
 * <li></li>
 * </ul>
 * 
 * @author jpjung
 * @since Tims7
 * @version 7.0
 */
public class SignDataDao extends SqlMapClientDaoSupport implements ISignDataDao {

	public File readSignImageFile(int memberSeq, String path, String signName,
			String fileName) {

		return null;
	}

	public void saveSignData(SignDataVO data) throws IOException {
		getSqlMapClientTemplate().insert("Sign.insertSignData", data);
	}

	public List<SignDataVO> readSignDataList(int userSeq) throws Exception {

		return getSqlMapClientTemplate().queryForList("Sign.getSignDataList", userSeq);
	}
	
	public List<SignDataVO> readSignSimpleDataList(int userSeq) throws Exception {

		return getSqlMapClientTemplate().queryForList("Sign.getSignSimpleDataList", userSeq);
	}
	
	public void deleteSignData(int userSeq, int[] signSeqs) throws Exception {
		Map paramMap = new HashMap();
		paramMap.put("userSeq", userSeq);
		paramMap.put("signSeqs", signSeqs);
		
		getSqlMapClientTemplate().delete("Sign.deleteSignData", paramMap);
	}

	public void modifySignData(SignDataVO data) throws IOException {
		getSqlMapClientTemplate().update("Sign.updateSignData", data);
	}
	
	public SignDataVO readSignData(int userSeq, int signSeq) throws IOException {
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("userSeq", userSeq);
		paramMap.put("signSeq", signSeq);
		
		return (SignDataVO)getSqlMapClientTemplate().queryForObject("Sign.readSignData", paramMap);
	}

	public SignDataVO readDefaultSignData(int userSeq) throws Exception {

		return (SignDataVO) getSqlMapClientTemplate().queryForObject(
				"Sign.getDefaultSignData", userSeq);
	}

	public void saveSign(SignVO sign) throws Exception {
		getSqlMapClientTemplate().insert("Sign.insertSign", sign);
	}
	
	public void updateSign(SignVO signVo) throws Exception {
		getSqlMapClientTemplate().update("Sign.updateSign", signVo);
	}

	public SignVO readSign(int userSeq) throws Exception {
		SignVO sign = (SignVO)getSqlMapClientTemplate().queryForObject("Sign.getSign", userSeq);
		List<SignDataVO> datas = readSignDataList(userSeq);
		if (sign != null && datas != null && datas.size() > 0) {
			sign.setSignDataVos(datas.toArray(new SignDataVO[datas.size()]));
		}
		return sign;
	}
	
	public void updateCheckAllNotDefault(int userSeq) throws Exception {
		getSqlMapClientTemplate().update("Sign.updateCheckAllNotDefault", userSeq);
	}
}
