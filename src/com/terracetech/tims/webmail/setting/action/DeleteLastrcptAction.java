package com.terracetech.tims.webmail.setting.action;


import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.LastrcptManager;
import com.terracetech.tims.webmail.util.StringUtils;

public class DeleteLastrcptAction extends BaseAction{
	private static final long serialVersionUID = 20090109L;
	
	private LastrcptManager rcptManager = null;
	
	public void setRcptManager(LastrcptManager rcptManager) {
		this.rcptManager = rcptManager;
	}
	
	public String execute() throws Exception{
		
		I18nResources resource = getMessageResource("setting");
		
		User user = UserAuthManager.getUser(request);
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		String msg = null;
		String all = request.getParameter("all");
				
		try {
			if("true".equalsIgnoreCase(all)){
				rcptManager.deleteLastRcptAll(mailUserSeq);
			}else{
				String deleteList = request.getParameter("deleteList");
				if(StringUtils.isNotEmpty(deleteList)){
					String[] rcptSeqs = deleteList.split(",");
					if(rcptSeqs != null && rcptSeqs.length > 0){
						rcptManager.deleteLastRcpt(mailUserSeq, rcptSeqs);
					}
				}
			}
			msg = resource.getMessage("save.ok");
			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			msg = resource.getMessage("update.fail");
		}
		
		request.setAttribute("msg", msg);
		return "success";
	}	
}
