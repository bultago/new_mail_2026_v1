package com.terracetech.tims.webmail.mail.action;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.ladmin.Protocol;
import com.terracetech.tims.webmail.common.manager.LadminManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class RegistBayesianRuleAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5035051765458294563L;
	private LadminManager ladminManager = null;
	
	public void setLadminManager(LadminManager ladminManager) {
		this.ladminManager = ladminManager;
	}
	
	public String execute() throws Exception{		
		String uid = request.getParameter("uid");
		String folderName = request.getParameter("folderName");
		String ruleType = request.getParameter("ruleType");
		
		Protocol ladminProtocol  = null;
		char registType = 0;
		JSONObject jObj = new JSONObject();
		try {
			ladminProtocol = new Protocol(user.get(User.MAIL_HOST));
			ladminManager.setResource(ladminProtocol,getMessageResource());
			
			if(ruleType.equals("spam")){
				registType = 's';
			} else if(ruleType.equals("white")){
				registType = 'w';
			}
			
			ladminManager.registSpamAndWhiteMessage(registType, 
					user.get(User.MESSAGE_STORE), folderName, new String[]{uid});
			jObj.put("result", "success");
			jObj.put("folderName", folderName);
			jObj.put("uid", uid);			
		} catch (Exception e) {
			jObj.put("result", "error");
		}
		
		ResponseUtil.processResponse(response, jObj);
		return null;
	}

}
