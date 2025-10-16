/**
 * ISignDataDao.java 2008. 11. 26.
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
import java.util.List;

import com.terracetech.tims.webmail.setting.vo.SignDataVO;
import com.terracetech.tims.webmail.setting.vo.SignVO;

/**
 * <p>
 * <strong>ISignDataDao.java</strong> Class Description
 * </p>
 * <p>
 * 주요설명
 * </p>
 * <ul>
 * <li></li>
 * </ul>
 * 
 * @author jpjung
 * @since Tims7
 * @version 7.0
 */
public interface ISignDataDao {

	public File readSignImageFile(int memberSeq, String path, String signName,
			String fileName);

	public void saveSignData(SignDataVO data) throws IOException;

	public List<SignDataVO> readSignDataList(int userSeq) throws Exception;
	
	public List<SignDataVO> readSignSimpleDataList(int userSeq) throws Exception;

	public void deleteSignData(int userSeq, int[] signSeqs) throws Exception;

	/**
	 * 
	 * <p>Sign을 수정한다.</p>
	 *
	 * @param data
	 * @throws IOException
	 * @return void
	 */
	public void modifySignData(SignDataVO data) throws IOException;
	
	/**
	 * 
	 * <p>Sign중 기본 사용으로 세팅된 Sign을 가져온다.</p>
	 *
	 * @param userSeq
	 * @return
	 * @throws Exception
	 * @return SignDataVO
	 */
	public SignDataVO readDefaultSignData(int userSeq) throws Exception;
	
	public SignDataVO readSignData(int userSeq, int signSeq) throws IOException;
	
	public void saveSign(SignVO sign) throws Exception;
	
	public SignVO readSign(int userSeq) throws Exception;
	
	public void updateCheckAllNotDefault(int userSeq) throws Exception;
	
	public void updateSign(SignVO signVo) throws Exception;
}
