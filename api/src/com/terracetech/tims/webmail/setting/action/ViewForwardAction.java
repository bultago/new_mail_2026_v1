package com.terracetech.tims.webmail.setting.action;

import java.util.ArrayList;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.DefineForwardingInfoVO;
import com.terracetech.tims.webmail.setting.vo.ForwardingInfoVO;


public class ViewForwardAction extends BaseAction{
	
	private static final long serialVersionUID = 20090112L;
	
	private SettingManager manager = null;
	
	private ForwardingInfoVO info;
	private int maxForwarding = 0;
	private ArrayList<DefineForwardingInfoVO> defineForwardingList ;
	private String actionType;
	private String defineType;
	private String defineValue;
	private int defineForwardingSeq;
	private int[] defineCheck;
	private DefineForwardingInfoVO defineForwardInfo;
	

	public void setManager(SettingManager manager) {
		this.manager = manager;
	}
	
	public ForwardingInfoVO getInfo() {
		return info;
	}
	
	public int getMaxForwarding() {
		return maxForwarding;
	}

	public void setMaxForwarding(int maxForwarding) {
		this.maxForwarding = maxForwarding;
	}
		
	public ArrayList<DefineForwardingInfoVO> getDefineForwardingList() {
		return defineForwardingList;
	}

	public void setDefineForwardingList(
			ArrayList<DefineForwardingInfoVO> defineForwardingList) {
		this.defineForwardingList = defineForwardingList;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public DefineForwardingInfoVO getDefineForwardInfo() {
		return defineForwardInfo;
	}

	public void setDefineForwardInfo(DefineForwardingInfoVO defineForwardInfo) {
		this.defineForwardInfo = defineForwardInfo;
	}	
	
	public int getDefineForwardingSeq() {
		return defineForwardingSeq;
	}

	public void setDefineForwardingSeq(int defineForwardingSeq) {
		this.defineForwardingSeq = defineForwardingSeq;
	}	

	public String getDefineType() {
		return defineType;
	}

	public void setDefineType(String defineType) {
		this.defineType = defineType;
	}

	public String getDefineValue() {
		return defineValue;
	}

	public void setDefineValue(String defineValue) {
		this.defineValue = defineValue;
	}	
	
	public int[] getDefineCheck() {
		return defineCheck;
	}

	public void setDefineCheck(int[] defineCheck) {
		this.defineCheck = defineCheck;
	}

	public String execute() throws Exception{		
		User user = UserAuthManager.getUser(request);
		
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		maxForwarding = Integer.parseInt(user.get(User.MAX_FORWARDING));

		info = manager.readForwardInfo(mailUserSeq);
		info.setForwardingMode(info.getForwardingMode().toLowerCase());		
		
		this.defineForwardingList = this.manager.readDefineForwarding(mailUserSeq);
		
		if(this.actionType != null && this.actionType.equals("update")){
			if(this.defineCheck.length == 1){
				this.defineForwardInfo = this.manager.readDefineForwardingByForwardSeq(this.defineCheck[0]);
				if(this.defineForwardInfo != null){
					this.defineType = this.defineForwardInfo.getFrom_address() != null? "mail":"domain";
					this.defineValue = this.defineForwardInfo.getFrom_address() != null? this.defineForwardInfo.getFrom_address():this.defineForwardInfo.getFrom_domain();
					this.defineForwardingSeq = this.defineCheck[0];
				}
			}
		}
		
		return "success";
	}
}
