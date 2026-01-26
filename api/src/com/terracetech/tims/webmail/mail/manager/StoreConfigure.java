/**
 * StoreConfigure.java 2008. 11. 20.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager;

import java.util.Properties;

import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p><strong>StoreConfigure.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li>���� Store �� ���ϱ����� Properties ������ ������������ Ŭ����</li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class StoreConfigure {
	
	private Properties storeProperties = null;
	private SystemConfigManager systemConfigManager = null;
		
	public StoreConfigure() {
		storeProperties = new Properties();
		setStoreConfigure();
	}
	
	public StoreConfigure(String clientIp) {
		storeProperties = new Properties();
		setStoreConfigure();
		
		storeProperties.setProperty("remote.address", clientIp);
	}
	
	/**
	 * <p>Store ��� Properties ���� ��ȯ.</p>
	 *
	 * @return
	 * @return Properties
	 */
	public Properties getStoreProperties(){
		return storeProperties;
	}
	
	/**
	 * <p>ȯ�漳�� �������� Store�� ��õ� ������ �̾� Properties �� ����.</p>
	 *
	 * @return void
	 */
	private void setStoreConfigure(){		
		storeProperties.setProperty("mail.debug", 
				EnvConstants.getMailSetting("mail.debug"));
		storeProperties.setProperty("mail.imap.connectionpool.debug", 
				EnvConstants.getMailSetting("mail.connectionpool.debug"));
		storeProperties.setProperty("mail.transport.protocol", "smtp");
		storeProperties.setProperty("mail.store.protocol", 
				EnvConstants.getMailSetting("mail.store.protocol"));
		storeProperties.setProperty("mail.imap.port", 
				EnvConstants.getMailSetting("mail.imap.port"));
		storeProperties.setProperty("mail.imap.partialfetch", "true");
		storeProperties.setProperty("mail.imap.fetchsize", 
				FormatUtil.toByteString(
						EnvConstants.getMailSetting("mail.imap.fetchsize")));
		storeProperties.setProperty("mail.imap.appendbuffersize", 
				FormatUtil.toByteString(
						EnvConstants.getMailSetting("mail.imap.appendbuffersize")));
		storeProperties.setProperty("mail.imap.connectionpoolsize", 
				EnvConstants.getMailSetting("mail.connectionpoolsize"));
		storeProperties.setProperty("mail.imap.connectionpooltimeout", 
				EnvConstants.getMailSetting("mail.connectionpooltimeout"));
		storeProperties.setProperty("mail.imap.connectiontimeout", 
				EnvConstants.getMailSetting("mail.connectiontimeout"));
		
		// mail.imap.timeout : IMAP Socket I/O timeout value in milliseconds. Default is infinite timeout.
		//  TCUSTOM-2262 IMAP Connection Time Out 20161031 : 5 minutes(60 * 5) setting  
		storeProperties.setProperty("mail.imap.timeout", 
				StringUtils.isEmpty(EnvConstants.getMailSetting("mail.imap.timeout")) ? "30000" : EnvConstants.getMailSetting("mail.imap.timeout"));
		
		storeProperties.setProperty("mail.pop3.connectiontimeout", 
				EnvConstants.getMailSetting("mail.connectiontimeout"));
		storeProperties.setProperty("mail.pop3.timeout", 
				EnvConstants.getMailSetting("mail.connectiontimeout"));
		
		storeProperties.setProperty("mail.smtp.host", 
				EnvConstants.getMailSetting("smtp.serv"));
		storeProperties.setProperty("mail.smtp.port", 
				EnvConstants.getMailSetting("smtp.port"));
		storeProperties.setProperty("mail.smtp.connectiontimeout", 
				EnvConstants.getMailSetting("smtp.connectiontimeout"));
		
		if(systemConfigManager == null){
			systemConfigManager = (SystemConfigManager)ApplicationBeanUtil.getApplicationBean("systemConfigManager");
		}
		storeProperties.setProperty("mail.imap.port", systemConfigManager.readImapPort());
	}
	
}
