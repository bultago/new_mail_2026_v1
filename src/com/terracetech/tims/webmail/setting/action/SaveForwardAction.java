package com.terracetech.tims.webmail.setting.action;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.DefineForwardingInfoVO;
import com.terracetech.tims.webmail.setting.vo.ForwardingInfoVO;

public class SaveForwardAction extends BaseAction {

	private static final long serialVersionUID = 20090112L;

	private SettingManager manager = null;
	
	private String forwardMode;
	
	private String[] forwardingAddress;
	
	private String defineType;
	
	private String defineValue;
	
	private int defineForwardingSeq;
	
	private String[] defineForwardingAddress;
	
	public void setManager(SettingManager manager) {
		this.manager = manager;
	}
	
	public void setForwardMode(String forwardMode) {
		this.forwardMode = forwardMode;
	}

	public void setForwardingAddress(String[] forwardingAddress) {
		this.forwardingAddress = forwardingAddress;
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

	public String[] getDefineForwardingAddress() {
		return defineForwardingAddress;
	}

	public void setDefineForwardingAddress(String[] defineForwardingAddress) {
		this.defineForwardingAddress = defineForwardingAddress;
	}

	public SettingManager getManager() {
		return manager;
	}

	public String getForwardMode() {
		return forwardMode;
	}

	public String[] getForwardingAddress() {
		return forwardingAddress;
	}
	
	public int getDefineForwardingSeq() {
		return defineForwardingSeq;
	}

	public void setDefineForwardingSeq(int defineForwardingSeq) {
		this.defineForwardingSeq = defineForwardingSeq;
	}

	public String execute() throws Exception {		
		I18nResources resource = getMessageResource("setting");
		
		User user = UserAuthManager.getUser(request);
		
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		String msg = null;
		try {
			ForwardingInfoVO vo = new ForwardingInfoVO();
			vo.setUserSeq(mailUserSeq);
			vo.setForwardingMode(forwardMode);
			vo.setForwardingAddress(forwardingAddress);
			
			this.manager.modifyForwardInfo(vo);
			
			if(this.defineForwardingAddress != null && this.defineForwardingAddress.length > 0){
				if(this.defineForwardingSeq <= 0){
					if(!this.manager.checkValidationDefineForwarding(mailUserSeq, this.defineValue)){
						msg = resource.getMessage("conf.forward.31");
						request.setAttribute("msg", msg);
						return "success";
					}
				}
				DefineForwardingInfoVO defineVO = new DefineForwardingInfoVO();
				defineVO.setMail_user_seq(mailUserSeq);
				defineVO.setDefine_forwarding_seq(this.defineForwardingSeq);						
				if(this.defineType != null && this.defineType.equals("mail")){
					defineVO.setFrom_address(this.defineValue);
				}
				else if(this.defineType != null && this.defineType.equals("domain")){
					this.defineValue = this.defineValue.replaceAll("@", "");		
					defineVO.setFrom_domain(this.defineValue);
				}
				defineVO.setForwarding_address_arr(this.defineForwardingAddress);
				
				this.manager.modifiyDefineForwarding(defineVO);
			}
			
			msg = resource.getMessage("save.ok");
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			msg = resource.getMessage("save.fail");
		}
		request.setAttribute("msg", msg);
		return "success";
	}
}
