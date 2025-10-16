package com.terracetech.tims.webmail.setting.action;

import java.util.Locale;

import org.json.simple.JSONObject;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.FilterRuleVO;
import com.terracetech.tims.webmail.setting.vo.FilterVO;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;


public class SaveFilterAction extends BaseAction{
	
	private static final long serialVersionUID = 20090112L;
	
	private SettingManager manager = null;
	
	private String filterApply;
	
	private String sender;
	
	private String receiver;
	
	private String subject;
	
	private String policy;
	
	private String mbox;
	
	private String boxName;
	
	
	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public void setPolicy(String policy) {
		this.policy = policy;
	}
	
	public void setFilterApply(String filterApply) {
		this.filterApply = filterApply;
	}

	public void setMbox(String mbox) {
		this.mbox = mbox;
	}

	public void setBoxName(String boxName) {
		this.boxName = boxName;
	}

	public void setManager(SettingManager manager) {
		this.manager = manager;
	}
	

	public String execute() throws Exception{
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		I18nResources resource = getMessageResource("setting");		
		String msg = null;
		try {
			FilterVO tempVo = manager.readFilterApply(mailUserSeq);
			if (tempVo == null) {
				FilterVO filterVo = new FilterVO();
				filterVo.setUserSeq(mailUserSeq);
				filterVo.setApply("off");
				manager.saveFilterApply(filterVo);
			}
			boolean isExist = makeRule();
			msg = (!isExist)?resource.getMessage("save.ok"):resource.getMessage("conf.filter.exist");			
		}catch (Exception e) {
			msg = resource.getMessage("save.fail");
		}
		request.setAttribute("msg", msg);
		return "success";
	}
	
	public String executeAjax() throws Exception{
		this.sender = request.getParameter("ruleSender");
		this.receiver = request.getParameter("ruleReceiver");
		this.subject = request.getParameter("ruleSubject");
		User user = UserAuthManager.getUser(request);		
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		JSONObject jObj = new JSONObject();
		try {
			boolean isExist = makeRule();
			if(!isExist){
				FilterVO filterVo = new FilterVO();
				filterVo.setUserSeq(mailUserSeq);
				filterVo.setApply("on");
				
				FilterVO tempVo = manager.readFilterApply(mailUserSeq);
				if (tempVo == null) {
					manager.saveFilterApply(filterVo);
				} else if ("off".equals(tempVo.getApply())){
					manager.modifyFilterApply(filterVo);
				}
			}
			jObj.put("result", (!isExist)?"success":"exist");
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			jObj.put("result", "fail");
		}
		
		ResponseUtil.processResponse(response, jObj);
        
		return null;
	}
	
	private boolean makeRule() throws Exception{		
		boolean isExist = false;
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		
		String tmpSender = null;
		String tmpSubject = null;
		String tmpReceiver = null;
		String tmpRule = null;
		String currentRule = ((sender != null)?sender:"")+ "|" +
							((subject != null)?subject:"")+ "|" +
							((receiver != null)?receiver:"");
		
		try {		
		FilterRuleVO[] filterList = manager.readFilterCondList(mailUserSeq);
		store = factory.connect(request.getRemoteAddr(), user);
		
		for (int i = 0; i < filterList.length; i++) {
			tmpSender = filterList[i].getSender();
			tmpSubject = filterList[i].getSubject();
			tmpReceiver = filterList[i].getReceiver();
			
			tmpRule = ((tmpSender != null)?tmpSender:"")+ "|" +
					((tmpSubject != null)?tmpSubject:"")+ "|" +
					((tmpReceiver != null)?tmpReceiver:"");
			
			if(currentRule.equals(tmpRule)){
				isExist = true;
			}			
		}
		
		if(!isExist){
			
			if ("on".equals(mbox)) {
				policy = "move " + boxName;
			
				TMailFolder folder = store.getFolder(boxName);
				if (!folder.exists()) {
					folder.create();
				}
			}
			FilterRuleVO rule = new FilterRuleVO();
			rule.setName(Long.toString((System.currentTimeMillis())));
			
			policy = StringUtils.IMAPFolderEncode(policy);
			rule.setPolicy(policy);
			rule.setSender(sender);
			rule.setSubject(subject);
			rule.setReceiver(receiver);
			
			manager.saveFilterCond(mailUserSeq, rule);		
		}
		
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}finally {
			if (store != null && store.isConnected()) {
				store.close();
			}
			
			tmpSender = null;
			tmpSubject = null;
			tmpReceiver = null;
			tmpRule = null;
			currentRule = null;
		}
		
		return isExist;
		
	}
	
	public String executeApply() throws Exception {
		I18nResources resource = getMessageResource("setting");
		
		User user = UserAuthManager.getUser(request);
		
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		String msg = null;
		try {
			FilterVO filterVo = new FilterVO();
			filterVo.setUserSeq(mailUserSeq);
			filterVo.setApply(filterApply);
			
			FilterVO tempVo = manager.readFilterApply(mailUserSeq);
			if (tempVo == null) {
				manager.saveFilterApply(filterVo);
			}
			else {
				manager.modifyFilterApply(filterVo);
			}
			msg = resource.getMessage("update.ok");
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			msg = resource.getMessage("update.fail");
		}
		request.setAttribute("msg", msg);
		return "success";
	}
}
