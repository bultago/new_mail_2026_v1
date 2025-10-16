package com.terracetech.tims.webmail.setting.action;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.FilterRuleVO;
import com.terracetech.tims.webmail.setting.vo.FilterVO;
import com.terracetech.tims.webmail.util.StringUtils;

public class ModifyFilterAction extends BaseAction {
	
	private SettingManager manager = null;
	
	private int condSeq;
	
	private String sender;
	
	private String receiver;
	
	private String subject;
	
	private String policy;
	
	private String mbox;
	
	private String boxName;

	public void setManager(SettingManager manager) {
		this.manager = manager;
	}
	
	public String execute() throws Exception {
		I18nResources resource = getMessageResource("setting");
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
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
			msg = (!isExist)?resource.getMessage("update.ok"):resource.getMessage("conf.filter.exist");
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			msg = resource.getMessage("update.fail");
		}
		request.setAttribute("msg", msg);	
		return "success";
	}
	
	private boolean makeRule() throws Exception{		
		boolean isExist = false;
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		
		String tmpSender = null;
		String tmpSubject = null;
		String tmpReceiver = null;
		String tmpBox = null;
		String tmpRule = null;
		String currentRule = ((sender != null)?sender:"")+ "|" +
							((subject != null)?subject:"")+ "|" +
							((receiver != null)?receiver:"") + "|" +
							((policy != null)?policy:"");
		
		try {		
		FilterRuleVO[] filterList = manager.readFilterCondList(mailUserSeq);
		store = factory.connect(request.getRemoteAddr(), user);
		
		for (int i = 0; i < filterList.length; i++) {
			tmpSender = filterList[i].getSender();
			tmpSubject = filterList[i].getSubject();
			tmpReceiver = filterList[i].getReceiver();
			tmpBox = filterList[i].getPolicy();
			
			tmpRule = ((tmpSender != null)?tmpSender:"")+ "|" +
					((tmpSubject != null)?tmpSubject:"")+ "|" +
					((tmpReceiver != null)?tmpReceiver:"")+ "|" +
					((tmpBox != null)?tmpBox:"");
			
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
			
			manager.modifyFilterCond(mailUserSeq, condSeq, rule);
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

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public int getCondSeq() {
		return condSeq;
	}

	public void setCondSeq(int condSeq) {
		this.condSeq = condSeq;
	}
	
	public void setMbox(String mbox) {
		this.mbox = mbox;
	}

	public void setBoxName(String boxName) {
		this.boxName = boxName;
	}

}
