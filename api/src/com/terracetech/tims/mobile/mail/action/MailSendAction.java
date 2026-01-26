package com.terracetech.tims.mobile.mail.action;

import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.service.manager.MailServiceManager;
import com.terracetech.tims.service.tms.vo.SendCondVO;
import com.terracetech.tims.service.tms.vo.SendResultVO;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringUtils;

public class MailSendAction extends BaseAction {
	private static final long serialVersionUID = 2907725847926765382L;

	private MailServiceManager mailServiceManager = null;
	private SendResultVO sendResultVO = null;


	public MailServiceManager getMailServiceManager() {
		return mailServiceManager;
	}

	public void setMailServiceManager(MailServiceManager mailServiceManager) {
		this.mailServiceManager = mailServiceManager;
	}

	public SendResultVO getSendResultVO() {
		return sendResultVO;
	}

	public void setSendResultVO(SendResultVO sendResultVO) {
		this.sendResultVO = sendResultVO;
	}

	@Override
	public String execute() throws Exception {

		try{
			makeFolderInfo(FOLDER_AND_QUOTA);

			SendCondVO sendVO = new SendCondVO();
			String signSeq = request.getParameter("signSeq");

			String sendType = request.getParameter("sendType");
			sendVO.setSendType(sendType);
			sendVO.setLocale(user.get(User.LOCALE));

			sendVO.setToAddr(this.getAddrArray(request.getParameter("to")));
			sendVO.setCcAddr(this.getAddrArray(request.getParameter("cc")));
			sendVO.setBccAddr(this.getAddrArray(request.getParameter("bcc")));

			sendVO.setSubject(request.getParameter("subject"));
			sendVO.setEncode(request.getParameter("encode"));

			sendVO.setReceivnoti("on".equalsIgnoreCase(request.getParameter("receivnoti")));
			sendVO.setOnesend("on".equalsIgnoreCase(request.getParameter("onesend")));
			sendVO.setSavesent("on".equalsIgnoreCase(request.getParameter("savesent")));
			sendVO.setSignUse("on".equalsIgnoreCase(request.getParameter("signUse")));
			sendVO.setSignSeq((signSeq != null)?Integer.parseInt(signSeq):-1);

			sendVO.setContent(StringUtils.textToHTML(request.getParameter("content")));
			sendVO.setSenderEmail(request.getParameter("senderEmail"));
			sendVO.setSenderName(request.getParameter("senderName"));
			sendVO.setAttachListStr(request.getParameter("attachList"));
			sendVO.setSendFlag(request.getParameter("sendFlag"));

			String remoteIP = request.getRemoteAddr();
			sendVO.setRemoteIp(remoteIP);
			String port = EnvConstants.getBasicSetting("web.port");
			port = (port != null && port.length() > 0)?port:Integer.toString(request.getServerPort());
			String mdnHost = EnvConstants.getMailSetting("mdn.host");
			String localhost = request.getScheme() + "://"
					+ request.getServerName() + ":" + port;
			mdnHost = (mdnHost != null)?mdnHost:localhost;
			sendVO.setMdnHost(mdnHost);
			sendVO.setLocalhost(localhost);

			sendVO.setEditMode(request.getParameter("editMode"));
			sendVO.setCharset(request.getParameter("charset"));
			sendVO.setUid(request.getParameter("uid"));
			sendVO.setFolder(request.getParameter("folder"));
			sendVO.setDraftMid(request.getParameter("draftMid"));
			this.sendResultVO = this.mailServiceManager.doSimpleMailSend(sendVO, super.user);

			LogManager.writeMailLogMsg(true, log, user.get(User.EMAIL), remoteIp, "action_mobile_send_message", sendVO.getFolder(), request.getParameter("to"), null, Long.parseLong("0"), EnvConstants.DEFAULT_CHARSET, sendVO.getSubject(), sendResultVO.getMessageId()); //TCUSTOM-2155
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			return "errorMobile";
		}
		return "success";
	}

	private String[] getAddrArray(String addr){
		String[] addrArray = null;
		if(addr != null && !addr.trim().equals("")){
			addrArray = addr.split("[;,\r\n]");
		}
		return addrArray;
	}
}
