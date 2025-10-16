package com.terracetech.tims.persistent;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameAlreadyBoundException;
import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * <p><strong>DataSourceManager.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li>Pooling DataSource �� �����ϴ� Ŭ����.</li>
 * <li>DataSource�� ���� ������ �޾� JNDI�� ���ε� ���ش�.</li>
 * <li>�� DataSource Entity ������ ã�±�ɰ� DataSource�� ��� ��Ȳ�� health ����͸� �� �� �� �ִ�.</li>
 * <li>Jndi ���� DB Connection�� �����ü� �ִ�.</li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class DataSourceManager {	
	
	/**
	 * <p>DataSource Entity ���� Map</p>
	 */
	protected static Map<String, DataSourceCollection> sourceMap = 
		new HashMap<String, DataSourceCollection>();	
	/**
	 * <p>Master�� Ȱ��ȭ ����. ��ֽ� false</p>
	 */
	private static boolean isMasterLive = false;
	/**
	 * <p>Health check Thead�� ��������</p>
	 */
	private static boolean isTestRunning = false;
	/**
	 * <p>�⺻ Bind �̸�</p>
	 */
	private static String DEFAULT_BINDNAME = null;
	/**
	 * <p>JNDI Context </p>
	 */
	private static Context initCtx = null;
	
	/**
	 * <p>Config Loader�� ���� ���� ������ �޾� sourceMap �� �����ϰ� ���� bind ���ִ� ���</p>
	 *
	 * @param collectionMap			��ü DataSourceCollection map����
	 * @param defaultBindName		�⺻ Bind��
	 * @param bindingMap				Bind�� ������ ����Ʈ
	 * @return void
	 */
	public static void initConfigLoad(Map<String, DataSourceCollection> collectionMap,
			String defaultBindName, Map<String, String> bindingMap){	
		
		
		Iterator<String> iter = null;
		String bindName = null;
		String initSourceId = null;
		
		try {
			
			if(initCtx == null){
				initCtx = new InitialContext();
			}
			
			sourceMap = collectionMap;
			DEFAULT_BINDNAME = defaultBindName;			
			iter = bindingMap.keySet().iterator();
			bindName = null;
			initSourceId = null;
			while(iter.hasNext()){
				bindName = iter.next();
				initSourceId = bindingMap.get(bindName);
				bindDataSource(bindName, initSourceId);
			}	
		
		} catch (Exception e) {
			 e.printStackTrace();
		} finally {
			bindingMap = null;
			iter = null;
			bindName = null;
			initSourceId = null;
		}
			
	}
	
	/**
	 * <p>�⺻ Bind�� ����</p>
	 *
	 * @return
	 * @return String
	 */
	public static String getDefaultBindName(){
		return DEFAULT_BINDNAME;
	}
	
	/**
	 * <p>Health �˻�� Master ��ֿ��� ����</p>
	 *
	 * @param status
	 * @return void
	 */
	public static void setMasterStatus(boolean status){
		isMasterLive = status;
	}
	
	/**
	 * <p>Health �˻�� Test Thread ����</p>
	 *
	 * @param status
	 * @return void
	 */
	public static void setTestDataSource(boolean status){
		isTestRunning = status;
	}
	
	/**
	 * <p>Master ��ֿ��� ��ȯ</p>
	 *
	 * @return
	 * @return boolean
	 */
	synchronized public static boolean getMasterStatus(){
		return isMasterLive;
	}
	
	/**
	 * <p>Test Thread ���� ���� ��ȯ</p>
	 *
	 * @return
	 * @return boolean
	 */
	synchronized public static boolean getTestDataSource(){
		return isTestRunning;
	}
	
	/**
	 * <p>bind ��� key ������ sourceMap���� �ش� DataSourceEntity ��ü�� ��ȯ</p>
	 *
	 * @param bindName		Bind ��
	 * @param key				��ϵ�Source id��
	 * @return
	 * @return DataSourceEntry
	 */
	protected static DataSourceEntry getDataSourceEntry(String bindName,String key){				
		return (sourceMap.get(bindName)).getEntry(key);
	}
	
	/**
	 * <p>bind �� ���� DataSourceCollection�� DatabaseType ������ ������</p>
	 *
	 * @param bindName
	 * @return
	 * @return String
	 */
	public static String getDBType(String bindName){		
		return (sourceMap.get(bindName)).getDatabaseType();
	}
	
	/**
	 * <p>�⺻ bind �� ���� DataSourceCollection�� DatabaseType ������ ������</p>
	 *
	 * @return
	 * @return String
	 */
	public static String getDBType(){				
		return (sourceMap.get(DEFAULT_BINDNAME)).getDatabaseType();
	}
	
	/**
	 * <p>bind �� ���� DataSourceCollection�� DatabaseType ������ ������</p>
	 *
	 * @param bindName
	 * @return
	 * @return String
	 */
	public static String getDatabaseType(String bindName){
		return (sourceMap.get(bindName)).getDatabaseType();
	}
	
	/**
	 * <p>�⺻ bind �� ���� DataSourceCollection�� DatabaseType ������ ������</p>
	 *
	 * @return
	 * @return String
	 */
	public static String getDatabaseType(){
		return (sourceMap.get(DEFAULT_BINDNAME)).getDatabaseType();
	}
	
	/**
	 * <p>bind �� ���� JNDI ���� DB Connection�� ������</p>
	 *
	 * @param bindName
	 * @return
	 * @throws Exception
	 * @return Connection
	 */
	public static Connection getDBConnection(String bindName)throws Exception{
		return ((TDataSource)initCtx.lookup(bindName)).getConnection();		
	}
	
	/**
	 * <p>�⺻ bind �� ���� JNDI ���� DB Connection�� ������</p>
	 *
	 * @return
	 * @throws Exception
	 * @return Connection
	 */
	public static Connection getDBConnection()throws Exception{
		return ((TDataSource)initCtx.lookup(DEFAULT_BINDNAME)).getConnection();		
	}
	
	/**
	 * <p>bind ���  key�� �Է¹޾� DataSourceCollection���� entity �� ã�� DataSource�� �����Ͽ� �̸�  JNDI�� ���</p>
	 *
	 * @param bindName		bind�� �̸�
	 * @param key				key ��
	 * @throws Exception
	 * @return void
	 */
	private static void bindDataSource(String bindName,String key) throws Exception{
		DataSource dataSource = getDataSource(bindName,key);		
		TDataSource bindSource = new TDataSource(getDataSourceEntry(bindName,key),bindName);
		bindSource.setDataSource(dataSource);	
		
		try {
			initCtx.bind(bindName,bindSource);	
		} catch (NameAlreadyBoundException ne) {
			System.out.println("===== ["+initCtx.lookup(bindName).getClass().getName()+"] =====");
			try {
				bindSource = ((TDataSource)initCtx.lookup(bindName));
				bindSource.setDataSource(dataSource);
			} catch (ClassCastException ce) {}			
			initCtx.rebind(bindName, bindSource);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
	}
	
	/**
	 * <p>
	 * bind ���  key�� �Է¹޾� DataSourceCollection���� entity �� ã�� entity ������ DataSource�� ���� �Ͽ� ��ȯ.<br>
	 * �̶� Oracle�ΰ�� OracleDataSource �� �����ϰ� �̿ܿ��� BasicDataSource�� ��ȯ
	 * �̿�����, 2012.08.08 ����Ŭ�� �����ϰ� BasicDataSource �� ��ȯ�ϵ��� ����
	 * </p>
	 *
	 * @param bindName
	 * @param key
	 * @return
	 * @throws SQLException
	 * @return DataSource
	 */
	protected static DataSource getDataSource(String bindName, String key) throws SQLException {
		DataSource dataSource = null;
		DataSourceEntry dsEntry = getDataSourceEntry(bindName,key);		
		
		try {
			BasicDataSource basicSource = new BasicDataSource();

			basicSource.setUrl(dsEntry.getUrl());
			basicSource.setDriverClassName(dsEntry.getDriverName());
			basicSource.setUsername(dsEntry.getUser());
			basicSource.setPassword(dsEntry.getPasswd());
			basicSource.setMaxActive(dsEntry.getMaxActive());
			basicSource.setMaxIdle(dsEntry.getMaxIdle());
			basicSource.setMaxWait(dsEntry.getMaxWait());
			basicSource.setInitialSize(dsEntry.getInitSize());
			
			// TCUSTOM-2564 20170102
			basicSource.setLogAbandoned(dsEntry.isLogAbandoned());
			basicSource.setRemoveAbandoned(dsEntry.isRemoveAbandoned());
			basicSource.setRemoveAbandonedTimeout(dsEntry.getRemoveAbandonedTimeout());

			if (!(sourceMap.get(bindName)).getDatabaseType().equals("oracle")){
				basicSource.setValidationQuery(dsEntry.getValidationQuery());
			}
			dataSource = (DataSource) basicSource;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("ERROR GET DATABASE CONNECTION INFO!!");
		}
		
		return dataSource;
	}
	
	/**
	 * <p>bind ���  key�� �Է¹޾� DataSourceCollection���� entity �� ã�� �� ������ ������ Health Thread ����</p>
	 *
	 * @param bindName
	 * @param key
	 * @return void
	 */
	protected static void startTest(String bindName,String key) {
		DataSourceEntry dsEntry = getDataSourceEntry(bindName,key);
		CheckHealthThread.invokeThread(dsEntry.getHost(),dsEntry.getPort());
	}
	
	/**
	 * <p>�ܺ� JNDI Context ����</p>
	 *
	 * @param context
	 * @return void
	 */
	public static void setContext(Context context){
		initCtx = context;
	}	
}
