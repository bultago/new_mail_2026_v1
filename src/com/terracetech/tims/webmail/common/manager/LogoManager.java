package com.terracetech.tims.webmail.common.manager;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.terracetech.tims.webmail.common.vo.CommonLogoVO;
import com.terracetech.tims.webmail.common.vo.LogoVO;
import com.terracetech.tims.webmail.mailuser.dao.MailDomainDao;
import com.terracetech.tims.webmail.util.FileUtil;

public class LogoManager {
	public Logger log = Logger.getLogger(this.getClass());
	private MailDomainDao mailDomainDao = null;

	public void setMailDomainDao(MailDomainDao mailDomainDao) {
		this.mailDomainDao = mailDomainDao;
	}
	
	public LogoVO getLogoInfo(int mailDomainSeq, Map<String, String> paramMap) {
		
		boolean haveImage = false;
		String attachImgSrc = paramMap.get("attachPath");
		String attachImgUrl = paramMap.get("attachUrl");
		String strLocalhost = paramMap.get("localUrl");
		String logoType = paramMap.get("logoType");
		attachImgUrl = strLocalhost + attachImgUrl;
		
		log.debug("attachImgSrc :" + attachImgSrc);
		log.debug("attachImgUrl :" + attachImgUrl);
		log.debug("strLocalhost :" + strLocalhost);
		log.debug("attachImgUrl :" + attachImgUrl);
		
		LogoVO logoVo = null;
		
		if ("mobile".equalsIgnoreCase(logoType)) {
			logoVo = mailDomainDao.readMobileLogoInfo(mailDomainSeq);
		} else {
			logoVo = mailDomainDao.readLogoInfo(mailDomainSeq);
		}
		
		if (logoVo != null) {
			haveImage = makeImg(logoVo.getLogoImg(), attachImgSrc, logoVo.getLogoImgName());
		}
		
		if (haveImage) {
			logoVo.setLogoImgUrl(attachImgUrl+"/"+logoVo.getLogoImgName());
		}
		return logoVo;
	}
	
	private boolean makeImg(byte[] imgData, String src, String fileName) {

		File file = new File(src+"/"+fileName);
		if (file.exists()) {
			return true;
		}
		return FileUtil.writeFile(imgData, file);
	}
	
	private String makeNewFileName(String fileName, String part) {
		
		int extIndex = fileName.lastIndexOf(".");
		String ext = fileName.substring(extIndex);
		
		String time = Long.toString(System.nanoTime());
		fileName = time + "_" + part + ext;
		
		return fileName;
	}
	public Map<String, CommonLogoVO> readCommonLogoList(Map<String, String> paramMap) {
		boolean haveImage = false;
		String attachImgSrc = paramMap.get("attachPath");
		String attachImgUrl = paramMap.get("attachUrl");
		String strLocalhost = paramMap.get("localUrl");
		String logoType = paramMap.get("logoType");
		attachImgUrl = strLocalhost + attachImgUrl;
		
		log.debug("attachImgSrc :" + attachImgSrc);
		log.debug("attachImgUrl :" + attachImgUrl);
		log.debug("strLocalhost :" + strLocalhost);
		log.debug("attachImgUrl :" + attachImgUrl);
		
		List<CommonLogoVO> commonLogoList = null;
		
		if ("mobile".equalsIgnoreCase(logoType)) {
			commonLogoList = mailDomainDao.readMobileCommonLogo();
		} else {
			commonLogoList = mailDomainDao.readCommonLogo();
		}
		
		Map<String, CommonLogoVO> logoMap = null;
		if (commonLogoList != null && commonLogoList.size() > 0) {
			logoMap = new HashMap<String, CommonLogoVO>();
			for(CommonLogoVO logoVo : commonLogoList){
				haveImage = false;
				if (logoVo != null) {
					haveImage = makeImg(logoVo.getLogoImg(), attachImgSrc, logoVo.getLogoImgName());
				}
				if (haveImage) {
					logoVo.setLogoImgUrl(attachImgUrl+"/"+logoVo.getLogoImgName());
				}				
				logoMap.put(logoVo.getImgMode().trim(), logoVo);
			}
		}
		return logoMap;
	}
}
