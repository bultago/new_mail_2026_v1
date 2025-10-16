package com.terracetech.tims.webmail.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.terracetech.tims.webmail.common.manager.OcxAutoUpdateManager;

public class OcxCheckThread extends Thread {
	
	private Log log = LogFactory.getLog(this.getClass());	
	private boolean stopFlag = false;	
	private OcxAutoUpdateManager manager = null;
	private String webRootPath = null;
	
	public OcxCheckThread(String webRootPath) {
		this.webRootPath = webRootPath;
		start();		
	}
	
	public void run() {		
		String ocxUrl = EnvConstants.getUtilSetting("ocx.url");
		String ocxFileConfig = EnvConstants.getUtilSetting("ocx.file");		
		String[] ocxFileValues = ocxFileConfig.split("\\|");
		Map<String, String> ocxDownFileMap = new HashMap<String, String>();
		Map<String, String> jsFileMap = new HashMap<String, String>();
		String[] mapValue = null;
		for (int i = 0; i < ocxFileValues.length; i++) {
			mapValue = ocxFileValues[i].split("=");
			ocxDownFileMap.put(mapValue[0], ocxUrl+mapValue[1]);			
		}
		ocxDownFileMap.put("version",ocxUrl+"version");
		jsFileMap.put("org", EnvConstants.getUtilSetting("ocx.orgjs"));
		jsFileMap.put("temp", EnvConstants.getUtilSetting("ocx.tempjs"));
		
		int periodTime = Integer.parseInt(EnvConstants.getUtilSetting("ocx.check.interval"));
		
		if(manager == null){
			manager = new OcxAutoUpdateManager();			
		}		
		manager.setResource(webRootPath, 
							ocxDownFileMap,
							jsFileMap,
							EnvConstants.getBasicSetting("tmpdir"));
		
		if(periodTime==0)
			stopFlag = true;
				
		try {
			while (!stopFlag) {

				log.debug("===== Start OCX AutoUpdate Check =====");			

				try {
					manager.updateOcxInfo();
				} catch (HttpClientError e) {
					log.error("===== OCX AutoUpdate HttpClientError Error ["+e.getMessage()+"]=====");								
				} catch (IOException e) {
					log.error("===== OCX AutoUpdate IOException Error ["+e.getMessage()+"]=====");								
				}
				
				try {
					Thread.sleep(periodTime * 1000);
				} catch (InterruptedException e) {
					log.error("===== OCX AutoUpdate Thread Error ["+e.getMessage()+"]=====");				
				}	
				
				log.debug("===== End OCX AutoUpdate =====");			
			}
			
			log.debug("===== Stop OCX AutoUpdate =====");
		} catch (Exception e) {
			log.error("===== OCX AutoUpdate Thread Error ["+e.getMessage()+"]=====");
			stopFlag = true;
		}
		
	}
}
