package com.terracetech.tims.mobile.mail.action;

import java.util.List;
import java.util.Map;

import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.service.manager.MailServiceManager;
import com.terracetech.tims.service.tms.vo.WriteCondVO;
import com.terracetech.tims.service.tms.vo.WriteResultVO;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.setting.manager.SignManager;
import com.terracetech.tims.webmail.setting.vo.SignDataVO;
import com.terracetech.tims.webmail.util.StringUtils;

public class MailWriteAction extends BaseAction {
	private MailServiceManager mailServiceManager = null;
	private SignManager signManager = null;
	private WriteResultVO writeResultVO = null;
	private List<SignDataVO> signDataList = null;
	private MailUserManager mailUserManager = null;

	public List<SignDataVO> getSignDataList() {
		return signDataList;
	}

	public void setSignDataList(List<SignDataVO> signDataList) {
		this.signDataList = signDataList;
	}

	public MailServiceManager getMailServiceManager() {
		return mailServiceManager;
	}

	public void setMailServiceManager(MailServiceManager mailServiceManager) {
		this.mailServiceManager = mailServiceManager;
	}

	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}

	public SignManager getSignManager() {
		return signManager;
	}

	public void setSignManager(SignManager signManager) {
		this.signManager = signManager;
	}

	public WriteResultVO getWriteResultVO() {
		return writeResultVO;
	}

	public void setWriteResultVO(WriteResultVO writeResultVO) {
		this.writeResultVO = writeResultVO;
	}

	@Override
	public String execute() throws Exception {
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		try {
			makeFolderInfo(FOLDER_AND_QUOTA);
			//int domainSeq = Integer.parseInt(super.user.get(User.MAIL_DOMAIN_SEQ));
			//UserEtcInfoVO userSettingVo = this.mailServiceManager.getUserSettingManager().readUserEtcInfo(userSeq);


			Map<String, String> configMap = mailUserManager.readUserSetting(domainSeq, userSeq);


			String folderName = request.getParameter("folderName");

			folderName = StringUtils.doubleUrlDecode(folderName);

			WriteCondVO writeVO = new WriteCondVO();
			writeVO.setRemoteIp(request.getRemoteAddr());
			writeVO.setUserEmail(user.get(User.EMAIL));
			writeVO.setLocale(user.get(User.LOCALE));
			writeVO.setMobileMode(true);
			writeVO.setWriteType(request.getParameter("wtype"));
			//writeVO.setEditorMode(userSettingVo.getWriteMode());
			writeVO.setUids(new String[]{request.getParameter("uid")});
			writeVO.setFolderName(folderName);
			writeVO.setReqTo(StringUtils.doubleUrlDecode(request.getParameter("to")));
			writeVO.setReqCc(StringUtils.doubleUrlDecode(request.getParameter("cc")));
			writeVO.setReqBcc(StringUtils.doubleUrlDecode(request.getParameter("bcc")));
			writeVO.setReqSubject(request.getParameter("subject"));
			writeVO.setReqContent(request.getParameter("content"));
			writeVO.setReturl(request.getParameter("returl"));
			writeVO.setForwardingMode(request.getParameter("fwmode"));
			//writeVO.setSignInside(isSignInside);
			this.writeResultVO = this.mailServiceManager.doSimpleMailWrite(writeVO, user);
			this.signDataList = signManager.getSignSimpleDataList(userSeq);

			String maxAttach = "10";
			if(configMap.containsKey("attach_maxsize")){
				maxAttach = configMap.get("attach_maxsize");
			}

			request.setAttribute("maxAttachSize", maxAttach);

			LogManager.writeMailLogMsg(true, log, user.get(User.EMAIL), request.getRemoteAddr(), "action_mobile_write_message", writeResultVO.getFolderName(), writeResultVO.getFolderName(), writeResultVO.getUidsValue()); //TCUSTOM-2155

		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}
		return "success";
	}
}
