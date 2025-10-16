package com.terracetech.tims.webmail.common.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.common.vo.BannerVO;
import com.terracetech.tims.webmail.common.vo.MailConfigVO;
import com.terracetech.tims.webmail.util.StringUtils;


public class SystemConfigDao extends SqlMapClientDaoSupport {
	
	public String getIntegrityInfo() {		
		Object result = getSqlMapClientTemplate().queryForObject("SystemConfig.readIntegrity");
		return (result != null)?(String)result:"off";
	}
	
	public String getCryptMrthodInfo() {		
		Object result = getSqlMapClientTemplate().queryForObject("SystemConfig.readCryptMethod");
		return (result != null)?(String)result:"aes";
	}
	
	public Map<String,String> getMailConfig(List<String> configNames){		
		return getSqlMapClientTemplate().queryForMap("SystemConfig.readSystemConfig", configNames, "key", "value");
	}
	
	public Map<String,String> getDomainConfig(Map<String,Object> configParam){		
		return getSqlMapClientTemplate().queryForMap("SystemConfig.readDomainSystemConfig", configParam, "key", "value");
	}
	
	public List<MailConfigVO> getMailConfigLikeList(String configName) {
	        configName = configName + "%";
	        return getSqlMapClientTemplate().queryForList("SystemConfig.readMailConfigWithLike", configName);
	}
	
	public Map<String, String> getWebAccessConfig() {
	        return (Map)getSqlMapClientTemplate().queryForMap("SystemConfig.readWebAccessConfig",null,"key","value");
	}
	
	public BannerVO getDomainBanner(int mailDomainSeq) {
		return (BannerVO)getSqlMapClientTemplate().queryForObject("SystemConfig.readDomainBanner", mailDomainSeq);
	}
	
	@SuppressWarnings("unchecked")
	public String getHostName(String hostId){
		
		List<MailConfigVO> list = getSqlMapClientTemplate().queryForList("SystemConfig.readHostId", hostId);
		
		String ip = null;
		String virtualip = null;
		
		for (MailConfigVO mailConfigVO : list) {
			if("ip".equalsIgnoreCase(mailConfigVO.getConfigName())){
				ip = mailConfigVO.getConfigValue();
			}else if("virtualip".equalsIgnoreCase(mailConfigVO.getConfigName())){
				virtualip = mailConfigVO.getConfigValue();
			}
		}
		
		if(StringUtils.isNotEmpty(virtualip))
			return virtualip;
		
		return ip;
	}
	
	public String getReservedMaxDay(){
		return (String)getSqlMapClientTemplate().queryForObject("SystemConfig.readMaxReserved");
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,String> getMailAdvanceSearchConfig(){
		List<MailConfigVO> list = getSqlMapClientTemplate().queryForList("SystemConfig.readSearchOption");
		
		String bodySearch = null;
		String attachSearch = null;
		
		for (MailConfigVO mailConfigVO : list) {
			if("bodysearch".equalsIgnoreCase(mailConfigVO.getConfigName())){
				bodySearch = mailConfigVO.getConfigValue();
			}else if("attsearch".equalsIgnoreCase(mailConfigVO.getConfigName())){
				attachSearch = mailConfigVO.getConfigValue();
			}
		}
		
		bodySearch = (bodySearch != null)?bodySearch:"off";
		attachSearch = (attachSearch != null)?attachSearch:"off";
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("bodySearch", bodySearch);
		map.put("attachSearch", attachSearch);
		
		return map;		
	}
	
	public MailConfigVO readConfig(String key) {
		Object result = getSqlMapClientTemplate().queryForObject("SystemConfig.readMailConfig", key);
		return result instanceof MailConfigVO ? (MailConfigVO)result : null;
	}

	public MailConfigVO readDomainConfig(int domainSeq, String configName) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("domainSeq", domainSeq);
		param.put("configName", configName);
		
		Object result = getSqlMapClientTemplate().queryForObject("SystemConfig.readDomainConfig", param);
		return result instanceof MailConfigVO ? (MailConfigVO)result : null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> readDomainSeqByConfigInfoList(String configName, String configValue) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("configName", configName);
		paramMap.put("configValue", configValue);
		
		return getSqlMapClientTemplate().queryForList("SystemConfig.readDomainSeqByConfigInfo", paramMap);
	}
	
	public MailConfigVO readDomainGroupConfig(int domainSeq, int groupSeq, String configName) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("domainSeq", domainSeq);
		param.put("groupSeq", groupSeq);
		param.put("configName", configName);
		
		Object result = getSqlMapClientTemplate().queryForObject("SystemConfig.readDomainGroupConfig", param);
		return result instanceof MailConfigVO ? (MailConfigVO)result : null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> getDomainSystemConfigByValue(String configValue){		
		return getSqlMapClientTemplate().queryForList("SystemConfig.readDomainSystemConfigByValue", configValue);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,String> readArchiveConfigFile(int domainSeq, int groupSeq) {
		
		Map<String, Integer> param = new HashMap<String, Integer>();
		param.put("domainSeq", domainSeq);
		param.put("groupSeq", groupSeq);
			
		return getSqlMapClientTemplate().queryForMap("SystemConfig.readArchiveConfig",param, "key", "value");
	}
	
	public String readBayesianOption(){
		Object used = getSqlMapClientTemplate().queryForObject("SystemConfig.readBayesianOption");
		return (used != null)?(String)used:"off";
	}
	
	public String readImapPort(){
		Object result = getSqlMapClientTemplate().queryForObject("SystemConfig.readIMapPort");
		return (result != null) ? (String) result : "";
	}
	public String readMdnPort(){
		Object result = getSqlMapClientTemplate().queryForObject("SystemConfig.readMdnPort");
		return (result != null) ? (String) result : "";
	}
	public void deleteCache(String param){
		getSqlMapClientTemplate().queryForObject("SystemConfig.allCacheDelete");
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> readConfigFile(String configName) throws SQLException {
		return getSqlMapClient().queryForMap("SystemConfig.readConfigFile",configName,"key","value");
	}
	
	public String readNoteLicense() {
		String license = "disabled";
		Object licenseObj = getSqlMapClientTemplate().queryForObject("SystemConfig.readNoteLicense");
		
		if (licenseObj != null) license = (String)licenseObj;
		
		return license;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> readIpGroup(String groupName) throws SQLException {
	        Map<String, String> param = new HashMap<String, String>();
	        param.put("groupValue", groupName);

	        return getSqlMapClient().queryForList("SystemConfig.readIpGroup", param);
	}

	public MailConfigVO readDomainConfigDuplicateLogin(int mailDomainSeq) {
		Object result = getSqlMapClientTemplate().queryForObject("SystemConfig.readDomainConfigDuplicateLogin", mailDomainSeq);
		return result instanceof MailConfigVO ? (MailConfigVO)result : null;
	}
}
