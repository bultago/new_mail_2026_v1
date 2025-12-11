/**
 * SettingManager.java 2008. 9. 26.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.setting.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.terracetech.secure.crypto.PasswordEty;
import com.terracetech.secure.crypto.PasswordUtil;
import com.terracetech.tims.logging.LogManagerBean;
import com.terracetech.tims.webmail.common.dao.SystemConfigDao;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.common.vo.MailConfigVO;
import com.terracetech.tims.webmail.mailuser.dao.MailDomainDao;
import com.terracetech.tims.webmail.mailuser.dao.MailUserDao;
import com.terracetech.tims.webmail.mailuser.manager.SettingSecureManager;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.dao.AttachSettingDao;
import com.terracetech.tims.webmail.setting.dao.SettingFilterDao;
import com.terracetech.tims.webmail.setting.dao.SettingPop3Dao;
import com.terracetech.tims.webmail.setting.dao.SettingUserEtcInfoDao;
import com.terracetech.tims.webmail.setting.dao.SettingAutoReplyDao;
import com.terracetech.tims.webmail.setting.dao.SettingForwardDao;
import com.terracetech.tims.webmail.setting.dao.SettingSpamDao;
import com.terracetech.tims.webmail.setting.vo.AttachInfoVO;
import com.terracetech.tims.webmail.setting.vo.AutoReplyListVO;
import com.terracetech.tims.webmail.setting.vo.AutoReplyVO;
import com.terracetech.tims.webmail.setting.vo.DefineForwardingInfoVO;
import com.terracetech.tims.webmail.setting.vo.FilterCondVO;
import com.terracetech.tims.webmail.setting.vo.FilterRuleVO;
import com.terracetech.tims.webmail.setting.vo.FilterSubCondVO;
import com.terracetech.tims.webmail.setting.vo.FilterVO;
import com.terracetech.tims.webmail.setting.vo.ForwardingInfoVO;
import com.terracetech.tims.webmail.setting.vo.PSpamRuleVO;
import com.terracetech.tims.webmail.setting.vo.PSpameListItemVO;
import com.terracetech.tims.webmail.setting.vo.Pop3VO;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.setting.vo.UserInfoVO;
import com.terracetech.tims.webmail.setting.vo.UserPhotoVO;
import com.terracetech.tims.webmail.setting.vo.ZipcodeVO;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p>
 * <strong>SettingManager.java</strong> Class Description
 * </p>
 * <p>
 * �ֿ伳��
 * </p>
 * <ul>
 * <li>����� ȯ�� ���� ������ ���� Manage Ŭ����. ȯ�漳���� ���� ������ �ڵ鸵 �ϱ� ���� Ŭ����</li>
 * </ul>
 * 
 * @author sshyun
 * @since Tims7
 * @version 7.0
 */

@Service
@Transactional
public class SettingManager {
	
	public static final String SUBJECT = "subject";
	
	public static final String SENDER = "from";
	
	public static final String RECEIVER = "to";
	
	public final static String EXPIRE_DAY = "bigattach_expireday";
	public final static String DOWNLOAD = "bigattach_download";
	public final static String MAX_MB_SIZE = "attach_maxsize";
	public final static String BIG_MAX_MB_SIZE = "bigattach_maxsize";

	/**
	 * <p>
	 * ����� ���� ���� Dao ��ü
	 * </p>
	 */
	private MailUserDao mailUserDao = null;
	/**
	 * <p>
	 * logging ��ü
	 * </p>
	 */
	private LogManagerBean logManager = null;

	private SettingSpamDao spamDao = null;

	private SettingAutoReplyDao replyDao = null;

	private SettingForwardDao forwardDao = null;

	private SettingFilterDao filterDao = null;
	
	private SettingPop3Dao popDao = null;
	
	private SettingUserEtcInfoDao etcDao = null;
	
	private MailDomainDao mailDomainDao = null;
	
	private AttachSettingDao attachDao = null;
	
	private SystemConfigDao systemConfigDao = null;
	
	private SettingSecureManager settingSecureManager = null;
	
	private SystemConfigManager systemManager = null;
	
	public void setSystemManager(SystemConfigManager systemManager) {
		this.systemManager = systemManager;
	}
	
	public void setSettingSecureManager(SettingSecureManager settingSecureManager) {
		this.settingSecureManager = settingSecureManager;
	}
	
	public void setSystemConfigDao(SystemConfigDao systemConfigDao) {
		this.systemConfigDao = systemConfigDao;
	}

	public void setFilterDao(SettingFilterDao filterDao) {
		this.filterDao = filterDao;
	}

	public void setForwardDao(SettingForwardDao forwardDao) {
		this.forwardDao = forwardDao;
	}

	public void setReplyDao(SettingAutoReplyDao replyDao) {
		this.replyDao = replyDao;
	}

	public void setMailUserDao(MailUserDao mailUserDao) {
		this.mailUserDao = mailUserDao;
	}
	
	public void setMailDomainDao(MailDomainDao mailDomainDao) {
		this.mailDomainDao = mailDomainDao;
	}

	public void setLogManager(LogManagerBean logManager) {
		this.logManager = logManager;
	}

	public void setSpamDao(SettingSpamDao spamDao) {
		this.spamDao = spamDao;
	}
	
	public void setPopDao(SettingPop3Dao popDao) {
		this.popDao = popDao;
	}
	
	public void setEtcDao(SettingUserEtcInfoDao etcDao) {
		this.etcDao = etcDao;
	}
	
	public void setAttachDao(AttachSettingDao attachDao) {
		this.attachDao = attachDao;
	}

	@Transactional
	public void saveSpamRule(PSpamRuleVO vo) {

		spamDao.deletePSpameRule(vo.getUserSeq());
		spamDao.deletePSpamWhiteList(vo.getUserSeq());
		spamDao.deletePSpamBlackList(vo.getUserSeq());

		spamDao.savePSpamRule(vo);
		
		if (vo.getWhiteList() != null && vo.getWhiteList().length > 0) {
			for (PSpameListItemVO item : vo.getWhiteList()) {
				try {
					spamDao.savePSpamWhiteListItem(item);	
				} catch (Exception ignore) {
				}
			}
		}
		
		if (vo.getBlackList() != null && vo.getBlackList().length > 0) {
			for (PSpameListItemVO item : vo.getBlackList()) {
				try {
					spamDao.savePSpamBlackListItem(item);	
				} catch (Exception ignore) {
				}
			}
		}
	}
	
	public void saveBlackList(int userSeq, String[] fromEmails) {
		if(fromEmails==null)
			return;
		
		spamDao.deletePSpamWhiteList(userSeq, fromEmails);
		spamDao.deletePSpamBlackList(userSeq, fromEmails);
		
		if (fromEmails != null && fromEmails.length > 0) {
			for (String email : fromEmails) {
				try {
					PSpameListItemVO vo = new PSpameListItemVO();
					vo.setUserSeq(userSeq);
				vo.setEmail(email);
				vo.setModTime(FormatUtil.getBasicDateStr());
				spamDao.savePSpamBlackListItem(vo);
				} catch (Exception ignore) {
				}
			}
		}		
	}
	
	public void saveWhiteList(int userSeq, String[] fromEmails) {		
		if(fromEmails==null)
			return;
		
		spamDao.deletePSpamWhiteList(userSeq, fromEmails);
		spamDao.deletePSpamBlackList(userSeq, fromEmails);
		
		if (fromEmails != null && fromEmails.length > 0) {
			for (String email : fromEmails) {
				try {
					PSpameListItemVO vo = new PSpameListItemVO();
					vo.setUserSeq(userSeq);
				vo.setEmail(email);
				vo.setModTime(FormatUtil.getBasicDateStr());
				spamDao.savePSpamWhiteListItem(vo);
				} catch (Exception ignore) {
				}
			}
		}	
	}

	public PSpamRuleVO readSpamRule(int userSeq) {
		List<PSpameListItemVO> whiteList = spamDao.readPSpamWhiteList(userSeq);
		List<PSpameListItemVO> blackList = spamDao.readPSpamBlackList(userSeq);

		PSpamRuleVO rule = spamDao.readPSpamRule(userSeq);
		
		if (rule != null) {
			if (whiteList != null)
				rule.setWhiteList((PSpameListItemVO[]) whiteList
						.toArray(new PSpameListItemVO[whiteList.size()]));
			if (blackList != null)
				rule.setBlackList((PSpameListItemVO[]) blackList
						.toArray(new PSpameListItemVO[blackList.size()]));
		}

		return rule;
	}

	public AutoReplyVO readAutoReply(int userSeq) {

		AutoReplyVO vo = replyDao.readAutoReply(userSeq);

		return vo;
	}

	public List<AutoReplyListVO> readAutoReplyWhiteList(int userSeq) {
		return replyDao.readAutoReplyWhiteList(userSeq);
	}

	public void saveAutoReply(AutoReplyVO autoReplyVO, String[] emails) {
		replyDao.deleteAutoReplyWhiteList(autoReplyVO.getUserSeq());

		if (emails != null) {
			List<AutoReplyListVO> list = new ArrayList<AutoReplyListVO>();
			for (String email : emails) {
				AutoReplyListVO vo = new AutoReplyListVO();
				vo.setUserSeq(autoReplyVO.getUserSeq());
				vo.setReplyAddress(email);

				list.add(vo);
			}

			replyDao.saveAutoReplyWhiteList(list
					.toArray(new AutoReplyListVO[list.size()]));
		}

		replyDao.modifyAutoReply(autoReplyVO);
	}

	public ForwardingInfoVO readForwardInfo(int userSeq) {
		return forwardDao.readForwardInfo(userSeq);
	}

	public void modifyForwardInfo(ForwardingInfoVO vo) {
		forwardDao.modifyForwardInfo(vo);
	}

	public FilterRuleVO[] readFilterCondList(int userSeq) {
		List<FilterCondVO> filters = filterDao.readFilterCondList(userSeq);
		for (FilterCondVO filterCondVO : filters) {
			List<FilterSubCondVO> subFilters = filterDao.readFilterSubcondList(
					userSeq, filterCondVO.getCondSeq());
			filterCondVO.setSubConds(subFilters
					.toArray(new FilterSubCondVO[subFilters.size()]));
		}

		List<FilterRuleVO> result = new ArrayList<FilterRuleVO>();
		
		for (FilterCondVO filterCondVO : filters) {
			FilterRuleVO rule = new FilterRuleVO();
			for (FilterSubCondVO subCond : filterCondVO.getSubConds()) {
				if(SUBJECT.equals(subCond.getField())){
					rule.setSubject(subCond.getPattern());
				}
				if(SENDER.equals(subCond.getField())){
					rule.setSender(subCond.getPattern());
				}
				if(RECEIVER.equals(subCond.getField())){
					rule.setReceiver(subCond.getPattern());
				}
			}
			rule.setIndex(filterCondVO.getCondSeq());
			rule.setName(filterCondVO.getCondName());
			rule.setPolicy(StringUtils.IMAPFolderDecode(filterCondVO.getCondPolicy()));
			
			result.add(rule);
		}

		return result.toArray(new FilterRuleVO[result.size()]);
	}
	
	public FilterVO readFilterApply(int mailUserSeq) {
		return filterDao.readFilter(mailUserSeq);
	}
	
	public void saveFilterApply(FilterVO filterVo) {
		filterDao.saveFilter(filterVo);
	}
	
	public void modifyFilterApply(FilterVO filterVo) {
		filterDao.modifyFilter(filterVo);
	}
	
	public void saveFilterCond(int userSeq, FilterRuleVO rule){
		FilterCondVO filterCondVO = new FilterCondVO();
		filterCondVO.setUserSeq(userSeq);
		filterCondVO.setCondName(rule.getName());
		filterCondVO.setCondPolicy(rule.getPolicy());
		filterCondVO.setCondApply("on");
		filterCondVO.setCondOp("or");
		filterDao.saveFilterCond(filterCondVO);
		
		int condSeq = filterDao.readMaxFilterCondSeq(userSeq);
		String today = FormatUtil.getBasicDateStr();
		
		if(StringUtils.isNotEmpty(rule.getSubject())){
			FilterSubCondVO subjectSubCond = getFilterSubCondVO(userSeq, condSeq, today, SUBJECT, rule.getSubject());
			filterDao.saveFilterSubcond(subjectSubCond);
		}
		
		if(StringUtils.isNotEmpty(rule.getSender())){
			FilterSubCondVO subjectSubCond = getFilterSubCondVO(userSeq, condSeq, today, SENDER, rule.getSender());
			filterDao.saveFilterSubcond(subjectSubCond);
		}
		
		if(StringUtils.isNotEmpty(rule.getReceiver())){
			FilterSubCondVO subjectSubCond = getFilterSubCondVO(userSeq, condSeq, today, RECEIVER, rule.getReceiver());
			filterDao.saveFilterSubcond(subjectSubCond);
		}
		
	}
	
	public void modifyFilterCond(int userSeq, int condSeq, FilterRuleVO rule){
		FilterCondVO filterCondVO = new FilterCondVO();
		filterCondVO.setUserSeq(userSeq);
		filterCondVO.setCondSeq(condSeq);
		filterCondVO.setCondName(rule.getName());
		filterCondVO.setCondPolicy(rule.getPolicy());
		filterCondVO.setCondApply("on");
		filterCondVO.setCondOp("or");
		filterDao.modifyFilterCond(filterCondVO);
		
		String today = FormatUtil.getBasicDateStr();
		
		filterDao.deleteFilterSubcond(userSeq, condSeq);
		
		if(StringUtils.isNotEmpty(rule.getSubject())){
			FilterSubCondVO subjectSubCond = getFilterSubCondVO(userSeq, condSeq, today, SUBJECT, rule.getSubject());
			filterDao.saveFilterSubcond(subjectSubCond);
		}
		
		if(StringUtils.isNotEmpty(rule.getSender())){
			FilterSubCondVO subjectSubCond = getFilterSubCondVO(userSeq, condSeq, today, SENDER, rule.getSender());
			filterDao.saveFilterSubcond(subjectSubCond);
		}
		
		if(StringUtils.isNotEmpty(rule.getReceiver())){
			FilterSubCondVO subjectSubCond = getFilterSubCondVO(userSeq, condSeq, today, RECEIVER, rule.getReceiver());
			filterDao.saveFilterSubcond(subjectSubCond);
		}
		
	}

	private FilterSubCondVO getFilterSubCondVO(int userSeq, int condSeq, String today, String field, String value) {
		FilterSubCondVO filterSubCondVO = new FilterSubCondVO();
		filterSubCondVO.setCreateTime(FormatUtil.getBasicDateStr());
		filterSubCondVO.setUserSeq(userSeq);
		filterSubCondVO.setCondSeq(condSeq);
		filterSubCondVO.setField(field);
		filterSubCondVO.setInorex("in");
		filterSubCondVO.setRegex("off");
		filterSubCondVO.setPattern(value);
		filterSubCondVO.setCreateTime(today);
		filterSubCondVO.setModifyTime(today);
		
		return filterSubCondVO;
	}
	
	public void deleteFilterCond(int mailUserSeq, int[] condSeqs){
		for (int seq : condSeqs) {
			filterDao.deleteFilterSubcond(mailUserSeq, seq);
		}
		
		filterDao.deleteFilterCond(mailUserSeq, condSeqs);
	}
	
	public Pop3VO[] readPop3List(int userSeq){
		List<Pop3VO> list = popDao.readPop3List(userSeq);
		
		if (list != null) {
			for (int i=0; i<list.size(); i++) {
				list.get(i).setPop3Boxname(StringUtils.IMAPFolderDecode(list.get(i).getPop3Boxname()));
			}
		}
		
		return list.toArray(new Pop3VO[list.size()]);
	}
	
	public void savePop3(Pop3VO pop3VO){
		popDao.savePop3(pop3VO);
	}
	public Pop3VO readPop3(int userSeq, String pop3Host, String pop3Id){
		return popDao.readPop3(userSeq, pop3Host, pop3Id);
	}

	public void deletePop3(int userSeq, String pop3Host, String pop3Id){
		popDao.deletePop3(userSeq, pop3Host, pop3Id);
	}

	public void modifyPop3(Pop3VO pop3Vo){
		popDao.modifyPop3(pop3Vo);
	}
	
	public UserEtcInfoVO readUserEtcInfo (int mailUserSeq){
		UserEtcInfoVO info = etcDao.readUserEtcInfo(mailUserSeq);
		
		return info;
	}
	
	public void saveUserEtcInfo (int userSeq, UserEtcInfoVO info){
		info.setUserSeq(userSeq);
		
		etcDao.saveUserEtcInfo(info);
	}
	
	public void modifyUserEtcInfo (int userSeq, UserEtcInfoVO info) {
		info.setUserSeq(userSeq);
		etcDao.modifyUserEtcInfo(info);
	}
	
	public boolean checkUserPass(int userSeq, String password) {
		String cryptedPass = mailUserDao.readUserPass(userSeq);
		
		if(!UserAuthManager.isClearText(cryptedPass)){
			if (cryptedPass.startsWith("{SHA256}")||cryptedPass.startsWith("{SHA512}")) {
				int endIdx = cryptedPass.indexOf("}");
				String algorithm = cryptedPass.startsWith("{SHA256}")?"{SHA-256}":"{SHA-512}";
				String pwdStr = cryptedPass.substring(endIdx + 1);
				cryptedPass = algorithm+pwdStr;
			}
			return PasswordUtil.certify(password, cryptedPass).isAuthSuccessed();
		}
		
		return cryptedPass.equals(password);
	}
	
	public List<ZipcodeVO> getZipcodeList(String dong, int skipResult, int pageBase) {
		return etcDao.readZipcodeList(dong, skipResult, pageBase);
	}
	
	public int getZipcodeListCount(String dong) {
		return etcDao.readZipcodeListCount(dong);
	}
	
	public UserInfoVO getUserInfo(int mailUserSeq) {
		return etcDao.readUserInfo(mailUserSeq);
	}
	
	public void modifyUserInfo(UserInfoVO userInfoVo) {
		etcDao.modifyUserInfo(userInfoVo);
	}
	
	public void modifyPKIUserDN(int mailUserSeq, String userDN) {
		etcDao.modifyPKIUserDN(mailUserSeq, userDN);
	}
	
	public void setMyPassword(int domainSeq, int mailUserSeq, String password) {
		String mailUid = mailUserDao.readMailUid(mailUserSeq);
		
		String cryptMethod = systemManager.getPasswodCryptMethod(domainSeq);
		
		PasswordUtil.setAlgorithm(cryptMethod);
		
        String salt = mailUid.substring(0, 1);
        PasswordEty ety = PasswordUtil.makePasswordEty(password, salt + salt);
        String encryptedPassword = ety.toDbStr();
        if (encryptedPassword.startsWith("{SHA-")) {
			int endIdx = encryptedPassword.indexOf("}");
			String algorithm = encryptedPassword.startsWith("{SHA-256}")?"{SHA256}":"{SHA512}";
			String pwdStr = encryptedPassword.substring(endIdx + 1);
			encryptedPassword = algorithm+pwdStr;
		}
		String currentTime = FormatUtil.getBasicDateStr();
		etcDao.modifyMyPassword(mailUserSeq,encryptedPassword);
		etcDao.modifyMyPasswordChangeTime(mailUserSeq, currentTime);
	}
	
	public String getCryptMethod(){
		String cryptMethod = systemConfigDao.getCryptMrthodInfo();
		if(cryptMethod == null){
			cryptMethod = "AES";
		} 
		return cryptMethod.toUpperCase();		
	}
	
	public void setAutoSaveInfo(int mailUserSeq, int term, String mode){
		etcDao.modifyAutoSaveInfo(mailUserSeq, term, mode);
	}
	
	public AttachInfoVO readAttachInfo(){
		AttachInfoVO vo = new AttachInfoVO();
		
		vo.setExpireday(attachDao.readAttachInfo(EXPIRE_DAY));
		vo.setDownload(attachDao.readAttachInfo(DOWNLOAD));
		vo.setMaxMBSize(attachDao.readAttachInfo(MAX_MB_SIZE));
		vo.setBigMaxMBSize(attachDao.readAttachInfo(BIG_MAX_MB_SIZE));
		
		return vo;
	}
	
	public UserPhotoVO readPictureInfo(int userSeq) {
		return etcDao.readUserPhoto(userSeq);
	}
	
	public void saveUserPicture(UserPhotoVO userPhotoVo) {
		etcDao.saveUserPhoto(userPhotoVo);
	}
	
	public void deleteUserPicture(int userSeq) {
		etcDao.deleteUserPhoto(userSeq);
	}
	
	public void modifyUserPicture(UserPhotoVO userPhotoVo) {
		etcDao.modifyUserPhoto(userPhotoVo);
	}
	
	public String showPasswordInput(int mailDomainSeq) {
		
		List<String> paramList = new ArrayList<String>(1);
		paramList.add("show_password_input");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("domainSeq",mailDomainSeq);
		paramMap.put("configNames",paramList);
		
		Map map = systemConfigDao.getDomainConfig(paramMap);
		String showPasswordInput = "";
		if(map != null && map.containsKey("show_password_input")){
			showPasswordInput = (String)map.get("show_password_input");
		}
		
		return showPasswordInput;
	}
	public boolean isApiAccessAllow(String ip) {

		MailConfigVO accessType = systemConfigDao.readConfig("webservice_use");
		
		if(accessType == null)
			return false;
		if(StringUtils.isEmpty(ip))
			return  false;
		
		String accessUse = accessType.getConfigValue();
		if("off".equals(accessUse)){
			return false;
		}else if("allallow".equals(accessUse)){
			return true;
		}else if("partallow".equals(accessUse)){
			MailConfigVO allowIp = systemConfigDao.readConfig("webservice_allowip");
			if(allowIp == null)
				return false;
			
			String[] allowIps = allowIp.getConfigValue().split(",");
			for (String settingIp : allowIps) {
				if(settingIp.equals(ip)){
					return true;
				}
			}			
		}else{
			return false;	
		}		
		return false;
	}
	
	public void modifiyDefineForwarding(DefineForwardingInfoVO vo){
		this.forwardDao.modifyDefineForwardingInfo(vo);
	}
	
	public ArrayList<DefineForwardingInfoVO> readDefineForwarding(int mail_user_seq){
		ArrayList<DefineForwardingInfoVO> list = new ArrayList<DefineForwardingInfoVO>();
		
		List<DefineForwardingInfoVO> dbList = this.forwardDao.readDefineForwarding(mail_user_seq);
		
		DefineForwardingInfoVO defineVO = null;
		int newDefineForwardSeq = 0;
		int oldDefineForwardSeq = 0;
		ArrayList<String> addressArr = null;
		
		int count = 0;
		for(DefineForwardingInfoVO vo: dbList){
			newDefineForwardSeq = vo.getDefine_forwarding_seq();
			if(count == 0 || newDefineForwardSeq != oldDefineForwardSeq){
				oldDefineForwardSeq = vo.getDefine_forwarding_seq();
				defineVO = new DefineForwardingInfoVO();
				defineVO.setMail_user_seq(vo.getMail_user_seq());
				defineVO.setDefine_forwarding_seq(vo.getDefine_forwarding_seq());
				defineVO.setFrom_address(vo.getFrom_address());		
				defineVO.setFrom_domain(vo.getFrom_domain());
				addressArr = new ArrayList<String>();
				defineVO.setForwarding_address_list(addressArr);
				list.add(defineVO);
			}				
			addressArr.add(vo.getForwarding_address());			
			count++;
		}		
		return list;
	}
	
	public DefineForwardingInfoVO readDefineForwardingByForwardSeq(int define_forward_seq){
		List<DefineForwardingInfoVO> dbList = this.forwardDao.readDefineForwardingByForwardSeq(define_forward_seq);
		DefineForwardingInfoVO defineVO = null;
		if(dbList == null || dbList.size() <= 0){
			return null;
		}
		
		defineVO = new DefineForwardingInfoVO();
		ArrayList<String> addressArr = new ArrayList<String>();
		defineVO.setForwarding_address_list(addressArr);
		
		for(DefineForwardingInfoVO vo: dbList){
			defineVO.setMail_user_seq(vo.getMail_user_seq());
			defineVO.setDefine_forwarding_seq(vo.getDefine_forwarding_seq());
			defineVO.setFrom_address(vo.getFrom_address());		
			defineVO.setFrom_domain(vo.getFrom_domain());			
			addressArr.add(vo.getForwarding_address());			
		}
		
		return defineVO;
	}
	
	public void deleteDefineForwading(int[] defineForwadingSeq){
		this.forwardDao.deleteDefineForwarding(defineForwadingSeq);
	}
	
	public boolean checkValidationDefineForwarding(int mailUserSeq, String defineValue){
		boolean check = true;
		int checkInt = this.forwardDao.checkValidationDefineForwarding(mailUserSeq, defineValue);
		if(checkInt > 0)check=false;
		return check;
	}
	public boolean enableCcview(int mailDomainSeq){
		MailConfigVO domainConfig = systemManager.getDomainConfig(mailDomainSeq, "ccView");
		return domainConfig == null? false:"on".equalsIgnoreCase(domainConfig.getConfigValue()); 
	}
	public boolean enableDuplicateLogin(int mailDomainSeq){
		MailConfigVO domainConfig = systemManager.getDomainConfigDuplicateLogin(mailDomainSeq);
		return domainConfig == null? false:"on".equalsIgnoreCase(domainConfig.getConfigValue()); 
	}
	
}
