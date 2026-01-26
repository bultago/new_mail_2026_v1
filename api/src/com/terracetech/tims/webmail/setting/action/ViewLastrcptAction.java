package com.terracetech.tims.webmail.setting.action;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.LastrcptManager;
import com.terracetech.tims.webmail.setting.vo.RcptVO;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

@SuppressWarnings("all")
public class ViewLastrcptAction extends BaseAction{
private static final long serialVersionUID = 20090109L;
	
	private LastrcptManager rcptManager = null;
	
	public void setRcptManager(LastrcptManager rcptManager) {
		this.rcptManager = rcptManager;
	}
		
	public String execute() throws Exception{
		
		User user = UserAuthManager.getUser(request);
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int maxRcptCount = StringUtils.isEmpty(EnvConstants.getMailSetting("mail.write.last.rcpt.count")) ? 100 : Integer.parseInt(EnvConstants.getMailSetting("mail.write.last.rcpt.count"));
		try{
			//List<RcptVO> lastRcpts = rcptManager.getLastRcptAddress(mailUserSeq);
			List<RcptVO> lastRcpts = rcptManager.getLastRcptAddressByMaxRcptCount(mailUserSeq, maxRcptCount);
			request.setAttribute("lastRcpts", lastRcpts);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}
		return "success";
	}
	
	public String executeAjax() throws Exception {
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		JSONObject rcptInfo = new JSONObject();
		JSONArray rcptList = new JSONArray();
		JSONObject rcptData = null;
		int maxRcptCount = StringUtils.isEmpty(EnvConstants.getMailSetting("mail.write.last.rcpt.count")) ? 100 : Integer.parseInt(EnvConstants.getMailSetting("mail.write.last.rcpt.count"));
		try{
			//List<RcptVO> lastRcpts = rcptManager.getLastRcptAddress(mailUserSeq);
			List<RcptVO> lastRcpts = rcptManager.getLastRcptAddressByMaxRcptCount(mailUserSeq, maxRcptCount);
			if (lastRcpts != null && lastRcpts.size() > 0) {
				for (int i=0; i<lastRcpts.size(); i++) {
					rcptData = new JSONObject();					
					rcptData.put("email", StringUtils.EscapeHTMLTag(lastRcpts.get(i).getAddress()));
					rcptList.add(rcptData);
				}
			}
			rcptInfo.put("isSuccess", true);
			rcptInfo.put("rcptList", rcptList);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			rcptInfo.put("isSuccess", false);
		}
		
		ResponseUtil.processResponse(response, rcptInfo);
		
		return null;
	}
}
