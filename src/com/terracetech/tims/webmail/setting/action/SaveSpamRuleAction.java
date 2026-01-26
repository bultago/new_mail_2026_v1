package com.terracetech.tims.webmail.setting.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.PSpamRuleVO;
import com.terracetech.tims.webmail.setting.vo.PSpameListItemVO;
import com.terracetech.tims.webmail.util.FormatUtil;

public class SaveSpamRuleAction extends BaseAction {

	private static final long serialVersionUID = 20090109L;

	private SettingManager manager = null;

	private String[] whiteList;

	private String[] blackList;

	private String rulelevel;

	private String applyAllowedlistOnly;

	private String applyBlacklist;

	private String applyWhitelist;

	private String applyRuleLevel;
	
	private String policy;

	public String execute() throws Exception {
		I18nResources resource = getMessageResource("setting");
		
		User user = UserAuthManager.getUser(request);
		
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		String msg = null;
		try {
			PSpamRuleVO vo = new PSpamRuleVO();
			vo.setUserSeq(mailUserSeq);
			vo.setApplyAllowedlistOnly(applyAllowedlistOnly);
			vo.setApplyRuleLevel(applyRuleLevel);
			vo.setApplyBlacklist(applyBlacklist);
			vo.setApplyWhitelist(applyWhitelist);
			vo.setApplyPbaysian("off");
			vo.setPspamRuleLevel(rulelevel);
			vo.setPspamPolicy(policy);
			
			vo.setBlackList(getEmailList(mailUserSeq, blackList));
			vo.setWhiteList(getEmailList(mailUserSeq, whiteList));
			
			manager.saveSpamRule(vo);
			msg = resource.getMessage("update.ok");
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			msg = resource.getMessage("update.fail");
		}
		
		request.setAttribute("msg", msg);
		return "success";
	}
	
	private PSpameListItemVO[] getEmailList(int userSeq, String[] emails)
	{
		if(emails==null)
			return null;
		
		List<PSpameListItemVO> list = new ArrayList<PSpameListItemVO>();
		for (String email : emails) {
			PSpameListItemVO vo = new PSpameListItemVO();
			vo.setUserSeq(userSeq);
			vo.setModTime(FormatUtil.getBasicDateStr());
			vo.setEmail(email);
			
			list.add(vo);
		}
		
		return list.toArray(new PSpameListItemVO[list.size()]);
	}

	public void setManager(SettingManager manager) {
		this.manager = manager;
	}

	public void setWhiteList(String[] whiteList) {
		this.whiteList = whiteList;
	}

	public void setBlackList(String[] blackList) {
		this.blackList = blackList;
	}

	public void setRulelevel(String rulelevel) {
		this.rulelevel = rulelevel;
	}

	public void setApplyAllowedlistOnly(String applyAllowedlistOnly) {
		this.applyAllowedlistOnly = applyAllowedlistOnly;
	}

	public void setApplyBlacklist(String applyBlacklist) {
		this.applyBlacklist = applyBlacklist;
	}

	public void setApplyWhitelist(String applyWhitelist) {
		this.applyWhitelist = applyWhitelist;
	}

	public void setApplyRuleLevel(String applyRuleLevel) {
		this.applyRuleLevel = applyRuleLevel;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}
	
}
