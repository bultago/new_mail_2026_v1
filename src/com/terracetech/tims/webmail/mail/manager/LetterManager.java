/**
 * LetterManager.java 2009. 4. 10.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;

import com.terracetech.tims.webmail.mail.dao.LetterDao;
import com.terracetech.tims.webmail.mail.vo.LetterVO;
import com.terracetech.tims.webmail.util.FileUtil;

/**
 * <p><strong>LetterManager.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class LetterManager {
	
	private LetterDao letterDao = null;

	/**
	 * @param letterDao 파라미터를 letterDao값에 설정
	 */
	public void setLetterDao(LetterDao letterDao) {
		this.letterDao = letterDao;
	}
	
	public int getTotalCount(int mailDomainSeq){
		return letterDao.readLetterTotal(mailDomainSeq);
	}
	
	public List<LetterVO> getLetterList(int mailDomainSeq, 
			int startPos, int letterCount, String src, String url) {
		
		List<LetterVO> letterList = letterDao.readLetterList(mailDomainSeq, startPos, letterCount);
		LetterVO vo = null;
		for (int i=0; i < letterList.size(); i++) {
			vo = letterList.get(i);
			vo = getMakeImgFile(vo, src, url);			
			letterList.set(i, vo);
		}		
		return letterList;
	}
	
	public JSONArray getLetterJsonList(int mailDomainSeq, 
			int startPos, int letterCount,
			String src, String url) {
		
		JSONArray letterArray = new JSONArray();
		List<LetterVO> letterList = getLetterList(mailDomainSeq, startPos, letterCount, src, url);
		for (Iterator iterator = letterList.iterator(); iterator.hasNext();) {
			letterArray.add(((LetterVO) iterator.next()).toJson());		
		}
		
		return letterArray;		
	}
		
	public LetterVO readLetter(int letterSeq, String src, String url) {
		
		LetterVO vo = letterDao.readLetter(letterSeq);
		vo = getMakeImgFile(vo, src, url);		
		return vo;
	}
	
	private LetterVO getMakeImgFile(LetterVO vo, String src, String url){
		
		String tmpFileName = vo.getThumbnailImgName();
		if (makeImg(vo.getThumbnailImg(), src, tmpFileName)) {
			vo.setThumbnailImgUrl(url+tmpFileName);			
		}		 
		tmpFileName = vo.getHeadImgName(); 
		if(makeImg(vo.getHeadImg(), src, tmpFileName)){
			vo.setLetterHeaderUrl(url+tmpFileName);
			vo.setLetterHeaderPath(src+tmpFileName);
		}
		
		tmpFileName = vo.getBodyImgName();
		if(makeImg(vo.getBodyImg(), src, tmpFileName)){
			vo.setLetterBodyUrl(url+tmpFileName);
			vo.setLetterBodyPath(src+tmpFileName);
		}
		
		tmpFileName = vo.getTailImgName();
		if(makeImg(vo.getTailImg(), src, tmpFileName)){
			vo.setLetterTailUrl(url+tmpFileName);
			vo.setLetterTailPath(src+tmpFileName);
		}
		
		return vo;
	}
	
	public boolean makeImg(byte[] imgData, String src, String fileName) {

		File file = new File(src+fileName);				
		if (file.exists()) {
			return true;
		}
		return FileUtil.writeFile(imgData, file);
	}
	

}
