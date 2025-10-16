/**
 * DBConfigurationXmlLoaderImpl.java 2008. 10. 13.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.persistent;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;

import com.terracetech.tims.config.ConfigHandler;
import com.terracetech.tims.config.ConfigurationLoader;

/**
 * <p><strong>DBConfigurationXmlLoaderImpl.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li>TIms DB ������ XML ����� ���� ���Ͽ��� �о� Pool�� ���� ���ִ� Ŭ����</li>
 * <li>ConfigLoader �������̽��� ����</li>
 * <li>���Ǳ��� �� �������� �о�鿩 DataSourceManager�� �����ϰ� �̸����� DBPool ����.</li>
 * 
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class DBConfigurationXmlLoaderImpl extends ConfigurationLoader {
	
	/**
	 * <p>�⺻ ������. ConfigurationLoader ������ �̿�</p>
	 *
	 */
	public DBConfigurationXmlLoaderImpl() {
		super();
	}
		
		
	/**
	 * <p>Config ������ �о�鿩 DataSourceManager �� DBPool�� ����.</p>
	 * <li>XML ���� ������ �о�鿩 �� �׸��� �ε��ѵ� �̸� ����</li>
	 * <li> collectionMap�� �����ϰ� �̸� DataSourceManager ��  initConfigLoad �� ����.</li>
	 *
	 * @throws ConfigurationException
	 * @see com.terracetech.tims.config.ConfigurationLoader#processSettingConfig() 
	 */
	public void processSettingConfig(){
		
		
		try {			
			String defaultBindName = null;
			Map<String, DataSourceCollection> collectionMap = 
							new HashMap<String, DataSourceCollection>();
			Map<String, String> bindMap = new HashMap<String, String>();
			
			int sourceSize = 0;
			int entrySize = 0;
			String sArrayStr = "";
			String eArrayStr = "";
			
			configLog.info("===== DB CONFIG LOAD =====");
			
			ConfigHandler config = configMap.get("db");			
			
			String identity = "datasource-list.datasources";
			defaultBindName = config
					.getString("datasource-list.default-bindname");
			sourceSize = config.
					getCollectionSize("datasource-list.datasources.bindname");
			sourceSize = (sourceSize > 0)?sourceSize:1;

			for (int j = 0; j < sourceSize; j++) {
				if (sourceSize > 1) {
					sArrayStr = "(" + j + ")";
				}

				Map<String, DataSourceEntry> entryMap = new HashMap<String, DataSourceEntry>();
				
				entrySize = config.getCollectionSize(identity + sArrayStr + ".datasource.user");
				entrySize = (entrySize > 0)?entrySize:1;
				
				String bindName = config.getString(identity + sArrayStr
						+ ".bindname");
				String databaseType = config.getString(identity + sArrayStr
						+ "[@database]");
				
				configLog.info("===== DB Context =====");
				configLog.info("-- BindName : " + defaultBindName);
				configLog.info("-- DatabaseType : " + databaseType);

				for (int i = 0; i < entrySize; i++) {

					if (entrySize > 1) {
						eArrayStr = "(" + i + ")";
					}

					DataSourceEntry dsEntry = new DataSourceEntry();
					String sourceId = config.getString(identity + sArrayStr
							+ ".datasource" + eArrayStr + "[@id]");

					dsEntry.setSourceID(sourceId);
					dsEntry.setSourceMode(config.getString(identity + sArrayStr
							+ ".datasource" + eArrayStr + "[@modetype]"));
					dsEntry.setDriverName(config.getString(identity + sArrayStr
							+ ".datasource" + eArrayStr + ".driver"));
					dsEntry.setUser(config.getString(identity + sArrayStr
							+ ".datasource" + eArrayStr + ".user"));
					dsEntry.setPasswd(config.getString(identity + sArrayStr
							+ ".datasource" + eArrayStr + ".passwd"));
					dsEntry.setHost(config.getString(identity + sArrayStr
							+ ".datasource" + eArrayStr + ".host"));
					dsEntry.setPort(config.getString(identity + sArrayStr
							+ ".datasource" + eArrayStr + ".port"));
					dsEntry.setUrl(config.getString(identity + sArrayStr
							+ ".datasource" + eArrayStr + ".url"));
					dsEntry.setMaxActive(config.getString(identity + sArrayStr
							+ ".datasource" + eArrayStr + ".maxactive"));
					dsEntry.setMaxIdle(config.getString(identity + sArrayStr
							+ ".datasource" + eArrayStr + ".maxidle"));
					dsEntry.setMaxWait(config.getString(identity + sArrayStr
							+ ".datasource" + eArrayStr + ".maxwait"));
					dsEntry.setInitSize(config.getString(identity + sArrayStr
							+ ".datasource" + eArrayStr + ".initsize"));					
					dsEntry.setValidationQuery(config.getString(identity
							+ sArrayStr + ".datasource" + eArrayStr
							+ ".validationquery"));
					dsEntry.setSlaveActive(config.getString(identity
							+ sArrayStr + ".datasource" + eArrayStr
							+ ".slave-active"));
					dsEntry.setRelationID(config.getString(identity + sArrayStr
							+ ".datasource" + eArrayStr + ".relation"));
					
					// TCUSTOM-2564 20170102
					dsEntry.setLogAbandoned(
							(config.getString(identity + sArrayStr
							+ ".datasource" + eArrayStr + ".logAbandoned") != null 
							&& "true".equalsIgnoreCase(config.getString(identity + sArrayStr
									+ ".datasource" + eArrayStr + ".logAbandoned"))) ? true : false);
					dsEntry.setRemoveAbandoned(
							(config.getString(identity + sArrayStr
							+ ".datasource" + eArrayStr + ".removeAbandoned") != null 
							&& "true".equalsIgnoreCase(config.getString(identity + sArrayStr
									+ ".datasource" + eArrayStr + ".removeAbandoned"))) ? true : false);
					dsEntry.setRemoveAbandonedTimeout((config.getString(identity + sArrayStr
							+ ".datasource" + eArrayStr + ".removeAbandonedTimeout") == null) ? "60" :
								config.getString(identity + sArrayStr
										+ ".datasource" + eArrayStr + ".removeAbandonedTimeout")
								);

					configLog.info("===== DB Entry =====");					
					configLog.info(dsEntry.toString());

					if (!dsEntry.isSlaveMode()) {
						bindMap.put(bindName, sourceId);						
					}

					entryMap.put(sourceId, dsEntry);
					sourceId = null;
				}

				collectionMap.put(bindName, new DataSourceCollection(entryMap,
						databaseType, bindName));
			
			}			
			
			DataSourceManager.initConfigLoad(collectionMap, defaultBindName, bindMap);
			

		} catch (Exception e) {
			configLog.error("DB_PROPERTY_LOAD_FAIL", e);
			e.printStackTrace();
		}
		
		configLog.info("===== DB Config Load Start =====");
	}	

}
