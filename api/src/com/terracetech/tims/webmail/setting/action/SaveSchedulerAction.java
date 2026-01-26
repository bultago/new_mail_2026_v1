package com.terracetech.tims.webmail.setting.action;

import java.util.Locale;

import org.apache.commons.lang.StringEscapeUtils;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerShareVO;

public class SaveSchedulerAction extends BaseAction {

	private SchedulerManager schedulerManager = null;
	private int shareSeq = 0;
	private String shareGroupName = null;
	private String shareColor = null;
	private String shareType = null;
	private String[] shareTarget = null;

	public void setSchedulerManager(SchedulerManager schedulerManager) {
		this.schedulerManager = schedulerManager;
	}

	public String execute() throws Exception {
		 if (!checkPAID()) {
             return "paidError";
         }
		I18nResources resource = getMessageResource("setting");
		
		String mailDomainSeq = user.get(User.MAIL_DOMAIN_SEQ);
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		String msg = null;
		
		try {
			SchedulerShareVO schedulerShareVo = new SchedulerShareVO();
			schedulerShareVo.setMailUserSeq(mailUserSeq);
			schedulerShareVo.setShareName(StringEscapeUtils.escapeHtml(shareGroupName));
			schedulerShareVo.setShareColor(shareColor);
			
			if (shareSeq == 0) {
				schedulerManager.saveShareGroup(schedulerShareVo, mailDomainSeq, shareType, shareTarget);
				msg = resource.getMessage("save.ok");
			} else {
				schedulerShareVo.setShareSeq(shareSeq);
				schedulerManager.modifyShareGroup(schedulerShareVo, mailDomainSeq, shareType, shareTarget);
				msg = resource.getMessage("update.ok");
			}
			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			if (shareSeq == 0) {
				msg = resource.getMessage("save.fail");
			} else {
				msg = resource.getMessage("update.fail");
			}
		}
		
		request.setAttribute("msg", msg);
		
		return "success";
	}

	public void setShareSeq(int shareSeq) {
		this.shareSeq = shareSeq;
	}

	public void setShareGroupName(String shareGroupName) {
		this.shareGroupName = shareGroupName;
	}

	public void setShareColor(String shareColor) {
		this.shareColor = shareColor;
	}

	public void setShareType(String shareType) {
		this.shareType = shareType;
	}

	public void setShareTarget(String[] shareTarget) {
		this.shareTarget = shareTarget;
	}
	
}
