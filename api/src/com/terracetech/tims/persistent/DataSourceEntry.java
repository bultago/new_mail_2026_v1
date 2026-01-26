/**
 * DataSourceEntry.java 2008  2008-09-30
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.persistent;

import org.bouncycastle.util.encoders.Base64;

import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.secure.crypto.SymmetricCrypt;

/**
 * <p><strong>DataSourceEntry.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li>DB Pool DataSource �ְ��� ȯ������ ���� Ŭ����</li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class DataSourceEntry {	
	/**
	 * <p>DataSource �� Id����</p>
	 */
	private String sourceID = null;
	/**
	 * <p>Mode ����. single / master / slave ���</p>
	 */
	private String sourceMode = null;	
	/**
	 * <p>DB ����̹��� ����</p>
	 */
	private String driverName = null;
	/**
	 * <p>����� ���� ����</p>
	 */
	private String user = null;
	/**
	 * <p>��� ��ȣ ����</p>
	 */
	private String passwd = null;
	/**
	 * <p>DB Host ��</p>
	 */
	private String host = null;
	/**
	 * <p>DB Port ��</p>
	 */
	private String port = null;
	/**
	 * <p>DB Url ����</p>
	 */
	private String url = null;
	/**
	 * <p>maxActive ����</p>
	 */
	private String maxActive = null;
	/**
	 * <p>maxIdle ����</p>
	 */
	private String maxIdle = null;
	/**
	 * <p>maxWait ����</p>
	 */
	private String maxWait = null;
	
	private String initSize = null;
	/**
	 * <p>validationQuery ����</p>
	 */
	private String validationQuery = null;
	/**
	 * <p>slaveActive ����</p>
	 */
	private String slaveActive = null;	
	/**
	 * <p>relationID ����</p>
	 */
	private String relationID = null;
	/**
	 * <p>alive ����. ���� ���� DB�� Ȱ��ȭ �Ǿ� �ִ����� ����</p>
	 */
	private boolean alive = true;	
		
	// TCUSTOM-2564 20170102
	private boolean removeAbandoned = true;
	private String removeAbandonedTimeout = "60";
	private boolean logAbandoned = true;
	
	/**
	 * <p>sourceID ���� ��ȯ</p>
	 *
	 * 
	 * @return String
	 */
	public String getSourceID() {
		return sourceID;
	}

	/**
	 * <p>sourceID ���� ����</p>
	 *
	 * @param sourceID
	 * @return void
	 */
	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}

	/**
	 * <p>sourceMode ���� ��ȯ</p>
	 *
	 * @return String
	 */
	public String getSourceMode() {
		return sourceMode;
	}

	/**
	 * <p>sourceMode ���� ����</p>
	 *
	 * @param sourceMode
	 * @return void
	 */
	public void setSourceMode(String sourceMode) {
		this.sourceMode = sourceMode;
	}	

	/**
	 * <p>driverName ���� ��ȯ</p>
	 *
	 * @return String
	 */
	public String getDriverName() {
		return driverName;
	}

	/**
	 * <p>driverName ���� ����</p>
	 *
	 * @param driverName
	 * @return void
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	/**
	 * <p>user ���� ��ȯ</p>
	 *
	 * @return String
	 */
	public String getUser() throws Exception{
		String decryptUser = "";
		if(user != null){			
			if(user.indexOf("==") > -1){
				decryptUser = SecureUtil.decrypt(SymmetricCrypt.AES, 
						"terrace-maenoops",user);
			} else {
				decryptUser = user;
			}
			
		}
		return decryptUser;		
	}

	/**
	 * <p>user ���� ����</p>
	 *
	 * @param user
	 * @return void
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * <p>passwd ���� ��ȯ</p>
	 *
	 * @return String
	 */
	public String getPasswd() throws Exception{
		String decryptPassword = "";
		if(passwd != null){
			if(passwd.indexOf("==") > -1){
				decryptPassword = SecureUtil.decrypt(SymmetricCrypt.AES, 
					"terrace-maenoops",passwd);
			} else {
				decryptPassword = passwd;
			}
		}
		return decryptPassword;
	}

	/**
	 * <p>passwd ���� ����</p>
	 *
	 * @param passwd
	 * @return void
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	/**
	 * <p>url ���� ��ȯ</p>
	 *
	 * @return String
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * <p>url ���� ����</p>
	 *
	 * @param url
	 * @return void
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * <p>maxActive ���� ��ȯ</p>
	 *
	 * @return
	 * @return int
	 */
	public int getMaxActive() {
		int returnVal = 0;
		if(maxActive != null){
			returnVal = Integer.parseInt(maxActive);
		}
		return returnVal;
	}

	/**
	 * <p>maxActive ���� ����</p>
	 *
	 * @param maxActive
	 * @return void
	 */
	public void setMaxActive(String maxActive) {
		this.maxActive = maxActive;
	}

	/**
	 * <p>maxIdle ���� ��ȯ</p>
	 *
	 * @return int
	 */
	public int getMaxIdle() {
		int returnVal = 0;
		if(maxIdle != null){
			returnVal = Integer.parseInt(maxIdle);
		}
		return returnVal;
	}

	/**
	 * <p>maxIdle ���� ����</p>
	 *
	 * @param maxIdle
	 * @return void
	 */
	public void setMaxIdle(String maxIdle) {
		this.maxIdle = maxIdle;
	}

	/**
	 * <p>maxWait ���� ��ȯ</p>
	 *
	 * @return int
	 */
	public int getMaxWait() {
		int returnVal = 0;
		if(maxWait != null){
			returnVal = Integer.parseInt(maxWait);
		}
		return returnVal;	
	}

	/**
	 * <p>maxWait ���� ����</p>
	 *
	 * @param maxWait
	 * @return void
	 */
	public void setMaxWait(String maxWait) {
		this.maxWait = maxWait;
	}
	
	

	public int getInitSize() {
		int returnVal = 10;
		if(initSize != null){
			returnVal = Integer.parseInt(initSize);
		}
		return returnVal;		
	}

	public void setInitSize(String initSize) {
		this.initSize = initSize;
	}

	/**
	 * <p>validationQuery ���� ��ȯ</p>
	 *
	 * @return String
	 */
	public String getValidationQuery() {
		return validationQuery;
	}

	/**
	 * <p>validationQuery ���� ����</p>
	 *
	 * @param validationQuery
	 * @return void
	 */
	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}

	/**
	 * <p>slaveActive ���� ��ȯ</p>
	 *
	 * @return String
	 */
	public String getSlaveActive() {
		return slaveActive;
	}

	/**
	 * <p>slaveActive ���� ����</p>
	 *
	 * @param slaveActive
	 * @return void
	 */
	public void setSlaveActive(String slaveActive) {
		this.slaveActive = slaveActive;
	}

	/**
	 * <p>relationID ���� ��ȯ</p>
	 *
	 * @return String
	 */
	public String getRelationID() {
		return relationID;
	}

	/**
	 * <p>relationID ���� ����</p>
	 *
	 * @param relationID
	 * @return void
	 */
	public void setRelationID(String relationID) {
		this.relationID = relationID;
	}

	/**
	 * <p>alive ���� ��ȯ</p>
	 *
	 * @return boolean
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * <p>alive ���� ����</p>
	 *
	 * @param alive
	 * @return void
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public boolean isRemoveAbandoned() {
		return removeAbandoned;
	}
	
	public void setRemoveAbandoned(boolean removeAbandoned) {
		this.removeAbandoned = removeAbandoned;
	}
	
	public int getRemoveAbandonedTimeout() {
		int returnVal = 60;
		if(removeAbandonedTimeout != null){
			returnVal = Integer.parseInt(removeAbandonedTimeout);
		}
		return returnVal;		
	}

	public void setRemoveAbandonedTimeout(String removeAbandonedTimeout) {
		this.removeAbandonedTimeout = removeAbandonedTimeout;
	}
	
	public boolean isLogAbandoned() {
		return logAbandoned;
	}
	
	public void setLogAbandoned(boolean logAbandoned) {
		this.logAbandoned = logAbandoned;
	}
	
	/**
	 * <p>slave mode ���� ���θ� �Ǵ�.</p>
	 *
	 * @return
	 * @return boolean
	 */
	public boolean isSlaveMode(){
		return ("slave".equals(sourceMode))?true:false;
	}
	
	/**
	 * <p>single mode ���� ���θ� �Ǵ�.</p>
	 *
	 * @return
	 * @return boolean		
	 */
	public boolean isSingleMode(){
		return ("single".equals(sourceMode))?true:false;
	}
	
	/**
	 * <p>slave mode �̸鼭 readonly���� �Ǵ� </p>
	 *
	 * @return
	 * @return boolean		true (�б� ����) false (�б�/����)
	 */
	public boolean isSlaveUpdateActive(){
		return ("readonly".equals(slaveActive))?false:true;
	}
	
	/**
	 * <p>host ���� ��ȯ</p>
	 *
	 * @return String
	 */
	public String getHost() {
		return host;
	}

	/**
	 * <p>host ���� ����</p>
	 *
	 * @param host
	 * @return void
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * <p>port ���� ��ȯ</p>
	 *
	 * @return String
	 */
	public String getPort() {
		return port;
	}

	/**
	 * <p>port ���� ����</p>
	 *
	 * @param port
	 * @return void
	 */
	public void setPort(String port) {
		this.port = port;
	}
	
	/**
	 * <p>Entity �� ���� ��� ������ ���.</p>
	 *
	 * @see java.lang.Object#toString()
	 * @return 
	 */
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("-- sourceID :");
		sb.append(sourceID);
		sb.append("\n");		
		sb.append("-- sourceMode :");
		sb.append(sourceMode);
		sb.append("\n");
		sb.append("-- driverName :");
		sb.append(driverName);
		sb.append("\n");
		sb.append("-- user :");
		sb.append(user);
		sb.append("\n");
		sb.append("-- passwd :");		
		sb.append(passwd);
		sb.append("\n");
		sb.append("-- url :"); 
		sb.append(url);
		sb.append("\n");
		sb.append("-- maxActive :");
		sb.append(maxActive);
		sb.append("\n");
		sb.append("-- maxIdle :");
		sb.append(maxIdle);
		sb.append("\n");
		sb.append("-- maxWait :");
		sb.append(maxWait);		
		sb.append("\n");
		sb.append("-- initSize :");
		sb.append(initSize);		
		sb.append("\n");
		sb.append("-- validationQuery :");
		sb.append(validationQuery);
		sb.append("\n");
		sb.append("-- slaveActive :");
		sb.append(slaveActive);
		sb.append("\n");
		sb.append("-- relationID :");
		sb.append(relationID);
		sb.append("\n");
		sb.append("-- logAbandoned :");
		sb.append(logAbandoned);
		sb.append("\n");
		sb.append("-- removeAbandoned :");
		sb.append(removeAbandoned);
		sb.append("\n");
		sb.append("-- removeAbandonedTimeout :");
		sb.append(removeAbandonedTimeout);
		sb.append("\n");
		
		return sb.toString();
		
	}

	
	
}
