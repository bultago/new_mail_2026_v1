package com.terracetech.tims.webmail.setting.action;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.util.StringUtils;


public class SaveUserEtcInfoAction extends BaseAction{
	
	private static final long serialVersionUID = 20090112L;
	
	private SettingManager settingManager = null;
	
	private UserAuthManager userAuthManager = null;
	
	private SystemConfigManager systemManager = null;
	
	private String locale; // NIL or ko or en
	
	private int notiInterval;
	
	private int pageLineCnt;
	
	private String afterLogin;
	
	private String hiddenImg;
	
	private String hiddenTag;
	
	private String wmode; //html or text
	
	private String forwardingMode; //attached or parsed
	
	private String encoding; //NIL or EUC-KR or US-ASCII or ISO-2022-JP or GB2312 or BIG5 or UTF-8
	
	private String saveSendBox;
	
	private String receiveNoti;
	
	private String vcardApply;
	
	private String senderName;
	
	private String senderEmail;
	
	private String searchAllFolder;
	
	private String signAttach;
	
	private String composeMode;
	
	private String writeNoti;
	
	private String activeXUse;
	
	private String renderMode;
	
	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}
	
	public void setUserAuthManager(UserAuthManager userAuthManager) {
		this.userAuthManager = userAuthManager;
	}
	
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public void setNotiInterval(int notiInterval) {
		this.notiInterval = notiInterval;
	}

	public void setPageLineCnt(int pageLineCnt) {
		this.pageLineCnt = pageLineCnt;
	}

	public void setAfterLogin(String afterLogin) {
		this.afterLogin = afterLogin;
	}

	public void setHiddenImg(String hiddenImg) {
		this.hiddenImg = hiddenImg;
	}

	public void setHiddenTag(String hiddenTag) {
		this.hiddenTag = hiddenTag;
	}

	public void setWmode(String wmode) {
		this.wmode = wmode;
	}

	public void setForwardingMode(String forwardingMode) {
		this.forwardingMode = forwardingMode;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setSaveSendBox(String saveSendBox) {
		this.saveSendBox = saveSendBox;
	}

	public void setReceiveNoti(String receiveNoti) {
		this.receiveNoti = receiveNoti;
	}

	public void setVcardApply(String vcardApply) {
		this.vcardApply = vcardApply;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}
	
	public void setSearchAllFolder(String searchAllFolder) {
		this.searchAllFolder = searchAllFolder;
	}
	
	public void setComposeMode(String composeMode) {
		this.composeMode = composeMode;
	}

	public void setWriteNoti(String writeNoti) {
		this.writeNoti = writeNoti;
	}

	public void setSignAttach(String signAttach) {
		this.signAttach = signAttach;
	}
	
	public void setSystemManager(SystemConfigManager systemManager) {
		this.systemManager = systemManager;
	}
	
	public void setActiveXUse(String activeXUse) {
		this.activeXUse = activeXUse;
	}

	public void setRenderMode(String renderMode) {
		this.renderMode = renderMode;
	}

	public String execute() throws Exception{
		
		User user = UserAuthManager.getUser(request);		
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));		
		UserEtcInfoVO info = getInfoVO();
		
		try {
			settingManager.saveUserEtcInfo(userSeq, info);
			user.put(User.PAGE_LINE_CNT,Integer.toString(info.getPageLineCnt()));
			user.put(User.LOCALE,info.getUserLocale());
			user.put(User.ACTIVE_X, ("enable".equals(info.getActiveXUse()))?"T":"F");
			user.put(User.RENDER_MODE, ("html".equals(info.getRenderMode()))?"html":"ajax");
			userAuthManager.updateUserCookieProcess(request, response, user, systemManager.getCryptMethod());			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}
		
		return "success";
	}
	
	private UserEtcInfoVO getInfoVO(){
		UserEtcInfoVO vo = new UserEtcInfoVO();
		vo.setUserLocale(locale);
		vo.setPageLineCnt(pageLineCnt);
		vo.setNotiInterval(notiInterval);
		vo.setAfterLogin(afterLogin);
		vo.setSearchAllFolder(searchAllFolder);
		vo.setHiddenImg(hiddenImg);
		vo.setHiddenTag(hiddenTag);
		vo.setWriteMode(wmode);
		vo.setForwardingMode(forwardingMode);
		vo.setSaveSendBox(saveSendBox);
		vo.setReceiveNoti(receiveNoti);
		vo.setCharSet(encoding);
		vo.setSenderEmail(senderEmail);
		vo.setSenderName(senderName);
		vo.setSignAttach(signAttach);
		vo.setVcardAttach(vcardApply);
		vo.setEncSenderName(StringUtils.IMAPFolderEncode(senderName));		
		vo.setComposeMode(composeMode);
		vo.setWriteNoti(writeNoti);
		vo.setActiveXUse(activeXUse);
		vo.setRenderMode(renderMode);
		return vo;
	}
}
