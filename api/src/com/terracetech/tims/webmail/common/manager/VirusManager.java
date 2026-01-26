package com.terracetech.tims.webmail.common.manager;

import java.io.File;
import java.util.Map;
import java.util.StringTokenizer;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.dao.SystemConfigDao;
import com.terracetech.tims.webmail.common.vo.VirusCheckVO;
import com.terracetech.tims.webmail.util.StringUtils;

public class VirusManager {
	
	private SystemConfigDao systemConfigDao = null;

	public void setSystemConfigDao(SystemConfigDao systemConfigDao) {
		this.systemConfigDao = systemConfigDao;
	}
	
	public VirusCheckVO checkVirus(String host, int port, String attFiles, I18nResources msgResource) throws Exception {
		StringBuffer msgBuffer = new StringBuffer();
		VirusCheckVO checkVO = new VirusCheckVO();
		String tmpDir = EnvConstants.getBasicSetting("tmpdir");
		Map<String, String> virusOption = systemConfigDao.readConfigFile("virus");
		checkVO.setAttachList(attFiles);
		if (virusOption != null) {
			if ("on".equalsIgnoreCase(virusOption.get("virusenginecheck"))) {
				long maxSize = 0;
				String maxSizeStr = virusOption.get("maxviruschecksize");
				String failPolicy = virusOption.get("viruschkfailpolicy");
				if (StringUtils.isNotEmpty(maxSizeStr)) {
					maxSize = Long.parseLong(maxSizeStr);
					StringTokenizer st = new StringTokenizer(attFiles, "\n");
					String elem = null;
					StringTokenizer stringTokenizer = null;
					String path = null;
					String fileName = null;
					File file = null;
					VirusCheckManager virusManager = null;
					while (st.hasMoreElements()) {
						elem = (String) st.nextElement();	
						stringTokenizer = new StringTokenizer(elem, "\t");
						path = (String) stringTokenizer.nextElement();
						path = tmpDir + EnvConstants.DIR_SEPARATOR + path;
						fileName = (String) stringTokenizer.nextElement();

						file = new File(path);

						if (!file.exists()) {
							continue;
						}
						if(maxSize == 0 || (file.length() > maxSize)){
							continue;
						}
						
						virusManager = new VirusCheckManager(host, port, false);			
						if(!virusManager.makeConnection() || !virusManager.checkVirus(path)){//°Ë»ç ½ÇÆÐ
							if(failPolicy.startsWith("tag")){
								checkVO.setSuccess(false);
								msgBuffer.append(msgResource.getMessage("virus.check.fail", new Object[]{fileName}));
								msgBuffer.append("\\n\\n");
							}
						}
						else{
							if(virusManager.isDetect()){
								checkVO.setSuccess(false);
								msgBuffer.append(msgResource.getMessage("virus.check.detect", new Object[]{fileName, virusManager.getVirusName()}));
								msgBuffer.append("\\n\\n");
							}
						}
						virusManager.closeConnection();
					}
				}
				checkVO.setCheckResultMsg(msgBuffer.toString());
			}
		}
		
		return checkVO;
	}	
}
