package com.terracetech.tims.webmail.common.manager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClientError;

import com.terracetech.tims.webmail.util.FileUtil;

public class OcxAutoUpdateManager {	
	private String localPath = null;
	private Map<String, String> ocxDownFileMap = null;
	private Map<String, String> jsFileMap = null;
	private String tempPath = null;
	private String separator = System.getProperty("file.separator");
	
	public void setResource(String localPath,
			Map<String, String> ocxFileMap,
			Map<String, String> jsFileMap,
			String tempPath){		
		
		this.localPath = localPath;
		this.ocxDownFileMap = ocxFileMap;
		this.jsFileMap = jsFileMap;
		this.tempPath = tempPath;	
		
	}
	
	
	public void updateOcxInfo() throws HttpClientError, IOException{
		String localOcxFilePath = localPath + separator + "ocxcab" + separator;
		String localVersionFilePath = localOcxFilePath +"version";
		String remoteVersionfilePath = tempPath + separator + "version.txt";  
		Map<String, String> localVersionMap = getVersionInfo(true, localVersionFilePath, null);
		Map<String, String> remoteVersionMap = 
			getVersionInfo(false, remoteVersionfilePath, ocxDownFileMap.get("version"));
		Map<String, String> updateOcxMap = new HashMap<String, String>();
		Iterator<String> iterator = null;
		
		iterator = localVersionMap.keySet().iterator();
		String ocxName = null;
		String localVer = null;
		String remoteVer = null;
		boolean isUpdate = false;
		while (iterator.hasNext()) {
			ocxName = iterator.next();
			localVer = localVersionMap.get(ocxName);
			remoteVer = remoteVersionMap.get(ocxName);			
			if(!localVer.equalsIgnoreCase(remoteVer)){
				updateOcxMap.put(ocxName, ocxDownFileMap.get(ocxName));
				localVersionMap.put(ocxName, remoteVer);
				isUpdate = true;
			}
		}
		
		if(isUpdate){
			updateLocalOcx(updateOcxMap,localOcxFilePath);
			updateOcxjs(localVersionMap);
			updateVersion(localVersionMap, localVersionFilePath);		
		}	
	}
	
	private Map<String, String> getVersionInfo(boolean isLocal, String filePath, String url) throws HttpClientError, IOException {
		Map<String, String> versionMap = new HashMap<String, String>();
		String[] versionContents = null;
		if(!isLocal){			
			FileUtil.remoteHttpFileDown(url, filePath);
		}
		
		versionContents = FileUtil.readFileLine(new File(filePath));		
		String[] tempStrs = null;
		for (String version : versionContents) {
			tempStrs = version.split("=");
			versionMap.put(tempStrs[0], tempStrs[1]);
		}
		
		versionContents = null;
		return versionMap;
	}
	
	private void updateLocalOcx(Map<String, String> updateOcxMap, 
			String localOcxFilePath) throws HttpClientError, IOException{		
		Iterator<String> iterator = updateOcxMap.keySet().iterator();
		String fileName = null;
		String fileUrl = null;			
		String downPath = null;
		String localPath = null;
		while (iterator.hasNext()) {
			fileName = iterator.next();
			fileUrl = updateOcxMap.get(fileName);
			downPath = tempPath + separator + fileName+".cab";
			localPath = localOcxFilePath + fileName + ".cab";
			
			FileUtil.remoteHttpFileDown(fileUrl,downPath);
			FileUtil.remove(new File(localPath));				
			FileUtil.copy(downPath, localPath);
		}
		
		fileName = null;
		fileUrl = null;			
		downPath = null;
		localPath = null;
		iterator = null;
	}
	
	private void updateOcxjs(Map<String, String> localVersionMap) throws IOException {	
		File orgJS = new File(localPath + jsFileMap.get("org"));
		File tempJS = new File(localPath + jsFileMap.get("temp"));		
		byte[] bytes = FileUtil.readFile(tempJS);
		String contents = new String(bytes);
		
		String fileName = null;
		String version = null;
		Iterator<String> iterator = localVersionMap.keySet().iterator();
		while (iterator.hasNext()) {
			fileName = iterator.next();
			version = localVersionMap.get(fileName);
			contents = contents.replaceAll("\\{"+fileName+"\\}", version);
		}
		FileUtil.remove(orgJS);
		FileUtil.copy(new ByteArrayInputStream(contents.getBytes()),orgJS);
		
		orgJS = null;
		tempJS = null;
		contents = null;
		fileName = null;
		version = null;
		iterator = null;
	}
	
	
	private void updateVersion(Map<String, String> localVersionMap, String localVersionFilePath){
		File localVersionFile = new File(localVersionFilePath);
		StringBuffer sb = new StringBuffer();
		String fileName = null;
		String version = null;
		Iterator<String> iterator = localVersionMap.keySet().iterator();
		while (iterator.hasNext()) {
			fileName = iterator.next();
			version = localVersionMap.get(fileName);
			sb.append(fileName);
			sb.append("=");
			sb.append(version);
			sb.append("\n");
		}
		
		FileUtil.remove(localVersionFile);		
		FileUtil.copy(new ByteArrayInputStream(sb.toString().getBytes()),localVersionFile);				
	}	
	
}
