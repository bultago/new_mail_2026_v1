package com.terracetech.tims.webmail.common.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import com.terracetech.secure.DESUtils;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.dao.SystemConfigDao;
import com.terracetech.tims.webmail.common.vo.BannerVO;
import com.terracetech.tims.webmail.common.vo.LogoVO;
import com.terracetech.tims.webmail.common.vo.MailConfigVO;
import com.terracetech.tims.webmail.mailuser.dao.MailDomainDao;
import com.terracetech.tims.webmail.util.StringUtils;

@SuppressWarnings("unchecked")
public class SystemConfigManager {
	
	public Logger log = Logger.getLogger(this.getClass());
	
	private SystemConfigDao systemConfigDao = null;
	
	private MailDomainDao domainDao = null;

	public void setSystemConfigDao(SystemConfigDao systemConfigDao) {
		this.systemConfigDao = systemConfigDao;
	}
	
	public void setDomainDao(MailDomainDao domainDao) {
		this.domainDao = domainDao;
	}

	public String getMailIntegrityInfo(){
		String integrity = systemConfigDao.getIntegrityInfo();
		if(integrity == null){
			integrity = "off";
		} 
		
		return integrity;
	}
	
	public MailConfigVO getDomainConfig(int mailDomainSeq, String configName) {
	        return systemConfigDao.readDomainConfig(mailDomainSeq, configName);
	    }
	
	public String getSecureMailConfig(){
		List<String> paramList = new ArrayList<String>(1);
		paramList.add("securemail");
		Map map = systemConfigDao.getMailConfig(paramList);
		String securemail = "disabled";
		if(map != null && map.containsKey("securemail")){
			securemail = (String)map.get("securemail");
		}
		return securemail;		
	}
	
	public String getLocalMailConfig(){
		List<String> paramList = new ArrayList<String>(1);
		paramList.add("localmail");
		Map map = systemConfigDao.getMailConfig(paramList);
		String localmail = "disabled";
		if(map != null && map.containsKey("localmail")){
			localmail = (String)map.get("localmail");
		}
		return localmail;		
	}
	
	public String getMobileMailConfig(){
		List<String> paramList = new ArrayList<String>(1);
		paramList.add("mobile");
		Map map = systemConfigDao.getMailConfig(paramList);
		String mobile = "disabled";
		if(map != null && map.containsKey("mobile")){
			mobile = (String)map.get("mobile");
		}
		return mobile;		
	}
	
	public Map getBatchSmtpConfig(){
		List<String> paramList = new ArrayList<String>(2);
		paramList.add("send_allowmassmail");
		paramList.add("send_maxrcpts");		
		return systemConfigDao.getMailConfig(paramList);
	}
	
	public String getCryptMethod(){
		//TODO OTHER CRYPT METHOD 
		String cryptMethod = systemConfigDao.getCryptMrthodInfo();
		if(cryptMethod == null){
			cryptMethod = "AES";
		} 
		return cryptMethod.toUpperCase();		
	}
	
	public String getPasswodCryptMethod(int domainSeq){
		//TODO OTHER CRYPT METHOD 
		Map resultMap = domainDao.readMailDomain(domainSeq);
		
		String cryptMethod = (String) resultMap.get("password_method");
		if(cryptMethod == null){
			cryptMethod = "AES";
		} 
		cryptMethod = StringUtils.replace(cryptMethod, "{", "");
		cryptMethod = StringUtils.replace(cryptMethod, "}", "");
		return cryptMethod.toUpperCase();		
	}
	
	public Map getAttachConfig(){
		List<String> paramList = new ArrayList<String>(4);
		paramList.add("bigattach_expireday");
		paramList.add("bigattach_download");
		paramList.add("bigattach_maxsize");
		paramList.add("attach_maxsize");
		paramList.add("bigattach_download_limited");
		
		Map map = systemConfigDao.getMailConfig(paramList);
		if(map == null){
			map = new HashMap();
		}
		if(!map.containsKey("bigattach_expireday")){
			map.put("bigattach_expireday", "7");
		} else if(!map.containsKey("bigattach_download")){
			map.put("bigattach_download", "10");
		} else if(!map.containsKey("bigattach_maxsize")){
			map.put("bigattach_maxsize", "500");			
		} else if(!map.containsKey("attach_maxsize")){
			map.put("attach_maxsize", "10");
		} else if(!map.containsKey("bigattach_download_limited")){
			map.put("bigattach_download_limited", "off");
		}
		
		return map;
	}
	
	public String getSessionTimeConfig(){
		String sessiontime = "0";
		List<String> paramList = new ArrayList<String>(1);
		paramList.add("auto_logout_time");
		
		Map map = systemConfigDao.getMailConfig(paramList);		
		
		if(map != null && map.containsKey("auto_logout_time")){			
			sessiontime = (String)map.get("auto_logout_time");
		}
		
		return sessiontime;
	}
	
	public BannerVO getBannerInfo(int mailDomainSeq) {
		return systemConfigDao.getDomainBanner(mailDomainSeq);
	}
	
	public String getHostInfo(String hostId) {
		return systemConfigDao.getHostName(hostId);
	}
	
	public int getMaxReservedDay(){
		int maxday = 30;
		String maxReservedDay = systemConfigDao.getReservedMaxDay();
		if(maxReservedDay != null){
			maxReservedDay = maxReservedDay.replaceAll("day", "");
			try {
				maxReservedDay = maxReservedDay.replaceAll("day", "");
				maxReservedDay = maxReservedDay.replaceAll(" ", "");
				maxday = Integer.parseInt(maxReservedDay.trim());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			
		}
		
		return maxday;
	}
	
	public Map<String, String> getMailAdvanceSearchConfig() {
		return systemConfigDao.getMailAdvanceSearchConfig();
	}
	
	public Map<String,String> getLoginConfig(){
		List<String> paramList = new ArrayList<String>(1);
		paramList.add("domain_type");
		paramList.add("domain_input_type");
		paramList.add("signup_option");
		paramList.add("search_id_option");
		paramList.add("search_password_option");
		paramList.add("announcements_option");
		paramList.add("faq_option");
		paramList.add("simple_info");
		paramList.add("secure_access_display");
		return systemConfigDao.getMailConfig(paramList);
	}
	
	/**
	 * �ִ� ���� �����ϼ�
	 * @param domainSeq
	 * @return
	 */
	public String getRcptModeConfig(int domainSeq){
		List<String> paramList = new ArrayList<String>(1);
		paramList.add("rcpt_mode");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("domainSeq",domainSeq);
		paramMap.put("configNames",paramList);
		
		Map map = systemConfigDao.getDomainConfig(paramMap);
		String rcptMode = "normal";
		if(map != null && map.containsKey("rcpt_mode")){
			rcptMode = (String)map.get("rcpt_mode");
		}
		return rcptMode;
	}
	
	public String readDomainAfterLogin(int domainSeq) {
		List<String> paramList = new ArrayList<String>(1);
		paramList.add("after_login");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("domainSeq",domainSeq);
		paramMap.put("configNames",paramList);
		
		Map map = systemConfigDao.getDomainConfig(paramMap);
		String afterLogin = "intro";
		if(map != null && map.containsKey("after_login")){
			afterLogin = (String)map.get("after_login");
		}
		
		return afterLogin;
	}
	
	public String getLoginPage(String domainAddress){
		
		List<Integer> domainSeqs = systemConfigDao.getDomainSystemConfigByValue(domainAddress);
		if(domainSeqs==null || domainSeqs.size()==0)
			return null;
		
		List<String> paramList = new ArrayList<String>(1);
		paramList.add("login_page");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("domainSeq",domainSeqs.get(0));
		paramMap.put("configNames",paramList);
		
		Map map = systemConfigDao.getDomainConfig(paramMap);
		String loginpage = "";
		if(map != null && map.containsKey("login_page")){
			loginpage = (String)map.get("login_page");
		}
		return loginpage;
	}
	
	public String readSkin(int domainSeq) {
		List<String> paramList = new ArrayList<String>(1);
		paramList.add("skin");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("domainSeq",domainSeq);
		paramMap.put("configNames",paramList);
		
		Map map = systemConfigDao.getDomainConfig(paramMap);
		String skin = "default";
		if(map != null && map.containsKey("skin")){
			skin = (String)map.get("skin");
		}
		
		return skin;
	}
	
	public boolean isPasswordChange(int domainSeq){
		List<String> paramList = new ArrayList<String>(1);
		paramList.add("firstlogin_changepass");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("domainSeq",domainSeq);
		paramMap.put("configNames",paramList);
		
		Map map = systemConfigDao.getDomainConfig(paramMap);
		if(map != null && map.containsKey("firstlogin_changepass")){
			String value = (String)map.get("firstlogin_changepass");
			if(value.equalsIgnoreCase("on")){
				return true;
			}
		}
		
		return false;
	}
	
	public String getCopyRight(int domainSeq){
		LogoVO logoVo = domainDao.readLogoInfo(domainSeq);
		if(logoVo != null){
			return logoVo.getCopyright();
		}
		
		return "";
	}
	
	public MailConfigVO getMailConfig(String key) {
		return systemConfigDao.readConfig(key);
	}
	
	public String getAspService(String key) {
		MailConfigVO vo = systemConfigDao.readConfig(key);
		if(vo == null)
			return "disabled";
		else
			return vo.getConfigValue();
		 
	}
	
	public String getAspLoginPage(String key) {
		MailConfigVO vo = systemConfigDao.readConfig(key);
		if(vo == null)
			return "off";
		else
			return vo.getConfigValue();
		 
	}
	
	public Map<String, Integer> getSsnNotUseDomainSeqMap() {
		String configName = "ssn_use";
		String configValue = "off";
		List<Integer> mailDomainSeqList = systemConfigDao.readDomainSeqByConfigInfoList(configName, configValue);
		
		Map<String, Integer> domainSeqMap = null;
		if (mailDomainSeqList != null && mailDomainSeqList.size() > 0) {
			domainSeqMap = new HashMap<String, Integer>();
			for (int i=0; i<mailDomainSeqList.size(); i++) {
				domainSeqMap.put(Integer.toString(mailDomainSeqList.get(i)), mailDomainSeqList.get(i));
			}
		}
		return domainSeqMap;
	}
	
	public Map<String, String> getTopAndLeftMenuUse(int mailDomainSeq) {
		Map<String, String> menuMap = new HashMap<String, String>();
		String ssoTopMenuConfig = "ssotopmenu";
		String normalTopMenuConfig = "nortopmenu";
		String leftMenuConfig = "leftmenu";
		String showAutoForwardConfig = "show_auto_forward"; 		//TCUSTOM-3763 20180129
		
		MailConfigVO leftmenuConfigVo = systemConfigDao.readDomainConfig(mailDomainSeq, leftMenuConfig);
		MailConfigVO ssoTopmenuConfigVo = systemConfigDao.readDomainConfig(mailDomainSeq, ssoTopMenuConfig);
		MailConfigVO normalTopmenuConfigVo = systemConfigDao.readDomainConfig(mailDomainSeq, normalTopMenuConfig);
		MailConfigVO showAutoForwardConfigVo = systemConfigDao.readDomainConfig(mailDomainSeq, showAutoForwardConfig); 		//TCUSTOM-3763 20180129
		
		if (ssoTopmenuConfigVo != null) {			
			if ("on".equalsIgnoreCase(ssoTopmenuConfigVo.getConfigValue())) {
				menuMap.put("ssotopmenu", "enable");
			} else {
				menuMap.put("ssotopmenu", "disable");
			}
		} else {
			menuMap.put("ssotopmenu", "disable");
		}
		
		if (normalTopmenuConfigVo != null) {			
			if ("on".equalsIgnoreCase(normalTopmenuConfigVo.getConfigValue())) {
				menuMap.put("nortopmenu", "enable");
			} else {
				menuMap.put("nortopmenu", "disable");
			}
		} else {
			menuMap.put("nortopmenu", "enable");
		}
		
		if (leftmenuConfigVo != null) {
			String leftMenuUse = leftmenuConfigVo.getConfigValue();
			if ("on".equalsIgnoreCase(leftMenuUse)) {
				menuMap.put("leftmenu", "enable");
			} else {
				menuMap.put("leftmenu", "disable");
			}
		} else {
			menuMap.put("leftmenu", "disable");
		}
		
		//TCUSTOM-3763 20180129 S
		if (showAutoForwardConfigVo != null) {
			String leftMenuUse = showAutoForwardConfigVo.getConfigValue();
			if ("on".equalsIgnoreCase(leftMenuUse)) {
				menuMap.put("autoForwardMenu", "enable");
			} else {
				menuMap.put("autoForwardMenu", "disable");
			}
		} else {
			menuMap.put("autoForwardMenu", "disable");
		}
		//TCUSTOM-3763 20180129 E

		return menuMap;
	}
	
	public String[] archiveUseInfoApi(int domainSeq, int groupSeq, String email) {

	        Map<String, String> systemArchiveUse = systemConfigDao.readArchiveConfigFile(domainSeq, groupSeq);

	        if ("on".equalsIgnoreCase(systemArchiveUse.get("archiveUseBySystem"))
	                && "on".equalsIgnoreCase(systemArchiveUse.get("vaultSsoUse"))) {
	            if ("enabled".equalsIgnoreCase(systemArchiveUse.get("archiveUseByGroup"))) {
	                String key = systemArchiveUse.get("deskey");
	                if (StringUtils.isNotEmpty(key)) {
	                    String desSsoAccount;
	                    try {
	                        String vaultUrl = systemArchiveUse.get("vault");
	                        // desSsoAccount = DESUtils.encode(email, key);
	                        String sslUse = systemArchiveUse.get("sslUse");

	                        desSsoAccount = DESUtils.encodeByStrKey(email, key);

	                        String url = "";
	                        if ("on".equalsIgnoreCase(sslUse)) {
	                            url = "https://" + vaultUrl + ":443" + "/sso.do?email=" + desSsoAccount;
	                        } else {
	                            url = "http://" + vaultUrl + "/sso.do?email=" + desSsoAccount;
	                        }

	                        String[] result = { "on", url };
	                        return result;
	                    } catch (Exception e) {
	                        log.error(e.getMessage(), e);
	                    }

	                }
	            }
	        }

	        String[] result = { "off", "" };
	        return result;
	    }
	
	public String getBayesianOption(){
		return systemConfigDao.readBayesianOption();	
	}
	
	public String readImapPort(){
		String result = systemConfigDao.readImapPort();
		if(StringUtils.isEmpty(result))
			result = EnvConstants.getMailSetting("mail.imap.port");
		
		return result; 	
	}
	
	public void deleteCache(String param){
		systemConfigDao.deleteCache(param);
	}
	
	
	public Map getForceEachSendConfig(int domainSeq){
		
		List<String> paramList = new ArrayList<String>(3);
		paramList.add("force_each_receiver");
		paramList.add("force_each_receiver_cnt");
		paramList.add("force_each_out_domain");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("domainSeq",domainSeq);
		paramMap.put("configNames",paramList);
		
		Map map = systemConfigDao.getDomainConfig(paramMap);
		Map configMap = new HashMap();
		if(map.containsKey("force_each_receiver")){
			configMap.put("forceEachUse", "on".equalsIgnoreCase((String)map.get("force_each_receiver")));
		} else {
			configMap.put("forceEachUse", false);
		}
		
		if(map.containsKey("force_each_receiver_cnt")){
			configMap.put("forceEachRcptCnt", (String)map.get("force_each_receiver_cnt"));
		} else {
			configMap.put("forceEachRcptCnt", "0");
		}
		
		if(map.containsKey("force_each_out_domain")){
			configMap.put("forceEachOutDomain", "on".equalsIgnoreCase((String)map.get("force_each_out_domain")));
		} else {
			configMap.put("forceEachOutDomain", false);
		}

		return configMap;
		
	}
	
	public Map<String, String> readVirusOption() throws Exception {
		return systemConfigDao.readConfigFile("virus");
	}

	public String getMaxRecipient() {
		String maxRecipient = "0";
        try {
            maxRecipient = systemConfigDao.readConfigFile("smtp/mtaadmin").get("maxrecipient");
        } catch (SQLException e) {
            log.error(e);
        }

	    return maxRecipient;
	}
	public String readMdnPort(){
		return systemConfigDao.readMdnPort();
	}

	public MailConfigVO getDomainConfigDuplicateLogin(int mailDomainSeq) {
		return systemConfigDao.readDomainConfigDuplicateLogin(mailDomainSeq);
	}
}
