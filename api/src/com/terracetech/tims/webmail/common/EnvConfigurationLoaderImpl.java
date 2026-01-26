/**
 * 
 */
package com.terracetech.tims.webmail.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.configuration.ConfigurationException;

import com.daou.charset.ConvertUniJis;
import com.terracetech.tims.config.ConfigHandler;
import com.terracetech.tims.config.ConfigurationLoader;
import com.terracetech.tims.webmail.common.log.LogManager;

/**
 * @author ������
 *
 */
public class EnvConfigurationLoaderImpl extends ConfigurationLoader {

	/* (non-Javadoc)
	 * @see com.terracetech.tims.config.ConfigurationLoader#processSettingConfig()
	 */
	@Override
	public void processSettingConfig() {
		
		ConfigHandler config = null;
		try {
			config = configMap.get("env");
			EnvConstants.setBasicMap(getSettingMapFactory(config,"basic-setting"));
			EnvConstants.setReportMap(getSettingMapFactory(config,"report-setting"));
			EnvConstants.setMailMap(getSettingMapFactory(config,"mail-setting"));
			EnvConstants.setLadminMap(getSettingMapFactory(config,"ladmin-setting"));
			EnvConstants.setAttachMap(getSettingMapFactory(config,"attach-setting"));
			EnvConstants.setUtilMap(getSettingMapFactory(config,"util-setting"));
			EnvConstants.setVirusMap(getSettingMapFactory(config,"virus-setting"));
			if("true".equalsIgnoreCase(EnvConstants.getUtilSetting("plugin.useage"))){
				ExtPartConstants.setExtPartEnv(getSettingMapFactory(config,"plugin-setting"));		
			}
							
			System.setProperty("mail.mime.charset", EnvConstants.getMailSetting("default.charset"));
			
			ConvertUniJis.Setup();
			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			configLog.error("Environment Confiuration Loading Error !! ["+e.getMessage()+"]");
		}
	}
	
	private Map<String, String> getSettingMapFactory(ConfigHandler config, String name) 
	throws ConfigurationException {
		
		Map<String, String> settingMap = new ConcurrentHashMap<String, String>();		
		int settingSize = config.getCollectionSize(name + ".property[@name]");
		
		String key = null;
		String value = null;		
		for (int i = 0; i < settingSize; i++) {
			key = config.getString(name + ".property(" + i + ")[@name]");
			value = config.getString(name + ".property(" + i + ")[@value]");
			settingMap.put(key, value);
		}		
		return settingMap;
	}

}
