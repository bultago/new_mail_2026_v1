package com.terracetech.tims.mobile.mail.action;

import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.service.manager.MailServiceManager;
import com.terracetech.tims.service.tms.vo.MailWorkCondVO;
import com.terracetech.tims.service.tms.vo.MailWorkResultVO;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringUtils;

public class MailWorkAction extends BaseAction{

	private static final long serialVersionUID = 521931096370606887L;
	private MailServiceManager mailServiceManager = null;

	private String workMode = null;
	private String uids = null;
	private String folderNames = null;
	private String targetFolderName = null;
	private MailWorkResultVO mailWorkResultVO = null;
	private String flagType = null;
	private boolean flagUse = false;

	public void setMailServiceManager(MailServiceManager mailServiceManager) {
		this.mailServiceManager = mailServiceManager;
	}

	@Override
	public String execute() throws Exception{
		String forward = "success";
		try{
			String[] uidArray = {};
			if (StringUtils.isNotEmpty(uids)) {
				uidArray = uids.split("\\|");
			}

			targetFolderName = StringUtils.doubleUrlDecode(targetFolderName);

			folderNames = StringUtils.doubleUrlDecode(folderNames);

			String[] folderNameArray = {};
			if (StringUtils.isNotEmpty(folderNames)) {
				folderNameArray = folderNames.split("\\|");
				if (folderNameArray.length > 0) {
					for (int i=0; i<folderNameArray.length; i++) {
						folderNameArray[i] = TMailUtility.IMAPFolderEncode(folderNameArray[i]);
					}
				}
			}

			MailWorkCondVO mailWorkCondVO = new MailWorkCondVO();
			mailWorkCondVO.setRemoteIp(request.getRemoteAddr());
			mailWorkCondVO.setLocale(user.get(User.LOCALE));
			mailWorkCondVO.setUserEmail(user.get(User.EMAIL));
			mailWorkCondVO.setWorkMode(workMode);
			mailWorkCondVO.setUid(uidArray);
			mailWorkCondVO.setFolderName(folderNameArray);
			mailWorkCondVO.setTargetFolderName(targetFolderName);
			mailWorkCondVO.setFlagType(flagType);
			mailWorkCondVO.setFlagUse(flagUse);

			mailWorkResultVO = mailServiceManager.doSimpleMailWork(mailWorkCondVO, user);

			request.setAttribute("page", request.getParameter("page"));

			if("empty".equals(workMode)){
				forward = "folderlist";
			}

			LogManager.writeMailLogMsg(true, log, user.get(User.EMAIL), request.getRemoteAddr(), "action_mobile_work"); //TCUSTOM-2155
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			return "errorMobile";
		}

		return forward;
	}

	public void setWorkMode(String workMode) {
		this.workMode = workMode;
	}

	public void setUids(String uids) {
		this.uids = uids;
	}

	public void setFolderNames(String folderNames) {
		this.folderNames = folderNames;
	}

	public void setTargetFolderName(String targetFolderName) {
		this.targetFolderName = targetFolderName;
	}

	public MailWorkResultVO getMailWorkResultVO() {
		return mailWorkResultVO;
	}

	public String getFlagType() {
		return flagType;
	}

	public void setFlagType(String flagType) {
		this.flagType = flagType;
	}

	public boolean isFlagUse() {
		return flagUse;
	}

	public void setFlagUse(boolean flagUse) {
		this.flagUse = flagUse;
	}
}
