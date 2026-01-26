package com.terracetech.tims.webmail.setting.action;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class UpdateUserInfoAction extends BaseAction {

	private static final long serialVersionUID = 5293641662594688878L;
	
	private SettingManager settingManager = null;
	
	private UserAuthManager userAuthManager = null;
	
	private SystemConfigManager systemManager = null;
		
	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}
	
	public void setUserAuthManager(UserAuthManager userAuthManager) {
		this.userAuthManager = userAuthManager;
	}
	
	public void setSystemManager(SystemConfigManager systemManager) {
		this.systemManager = systemManager;
	}
	
	public String execute() throws Exception{
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));		
		
		String locale = request.getParameter("locale");
		int notiInterval = (request.getParameter("notiInterval") != null)?
							Integer.parseInt(request.getParameter("notiInterval")):-1;
							
		int pageLineCnt = (request.getParameter("pageLineCnt") != null)?
							Integer.parseInt(request.getParameter("pageLineCnt")):-1;
		String afterLogin = request.getParameter("afterLogin");
		String hiddenImg = request.getParameter("hiddenImg");
		String hiddenTag = request.getParameter("hiddenTag");
		String wmode = request.getParameter("wmode"); //html or text
		String forwardingMode = request.getParameter("forwardingMode"); //attached or parsed
		String encoding = request.getParameter("encoding"); //NIL or EUC-KR or US-ASCII or ISO-2022-JP or GB2312 or BIG5 or UTF-8
		String saveSendBox = request.getParameter("saveSendBox");
		String receiveNoti = request.getParameter("receiveNoti");
		String vcardApply = request.getParameter("vcardApply");
		String senderName = request.getParameter("senderName");
		String senderEmail = request.getParameter("senderEmail");
		String searchAllFolder = request.getParameter("searchAllFolder");
		String signAttach = request.getParameter("signAttach");
		String composeMode = request.getParameter("composeMode");
		
		JSONObject jObj = new JSONObject();
		try {
			UserEtcInfoVO vo = new UserEtcInfoVO();
			vo.setUserLocale(locale);
			vo.setPageLineCnt(pageLineCnt);
			vo.setNotiInterval(notiInterval);
			vo.setAfterLogin(afterLogin);
			vo.setSearchAllFolder(searchAllFolder);

			UserEtcInfoVO readUserEtcInfo = settingManager.readUserEtcInfo(userSeq); 

            vo.setHiddenImg(StringUtils.isNotEmpty(hiddenImg)?hiddenImg:readUserEtcInfo.getHiddenImg()); 
            vo.setHiddenTag(StringUtils.isNotEmpty(hiddenTag)?hiddenTag:readUserEtcInfo.getHiddenTag()); 
            vo.setWriteMode(StringUtils.isNotEmpty(wmode)?wmode:readUserEtcInfo.getWriteMode()); 

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
			
			settingManager.modifyUserEtcInfo(userSeq, vo);
			
			if(vo.getPageLineCnt() > -1){
				user.put(User.PAGE_LINE_CNT,Integer.toString(vo.getPageLineCnt()));
			}			
			if(locale != null){
				user.put(User.LOCALE,vo.getUserLocale());
			}		
			userAuthManager.updateUserCookieProcess(request, response, user, systemManager.getCryptMethod());			
						
			jObj.put("result", "success");
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			jObj.put("result", "error");
		}
		
		ResponseUtil.processResponse(response, jObj);
		
		return null;
	}

	
}
