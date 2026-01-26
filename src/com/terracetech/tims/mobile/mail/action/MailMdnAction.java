package com.terracetech.tims.mobile.mail.action;

import com.terracetech.tims.common.PageManager;
import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.service.manager.MailServiceManager;
import com.terracetech.tims.service.tms.vo.ListCondVO;
import com.terracetech.tims.service.tms.vo.MdnContentVO;
import com.terracetech.tims.service.tms.vo.MdnListVO;
import com.terracetech.tims.service.tms.vo.MdnRecallVO;
import com.terracetech.tims.service.tms.vo.ReadCondVO;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;

public class MailMdnAction extends BaseAction{
	/**
	 *
	 */

	private long uid = 0;
	private int page = 0;
	private int listPage = 0;
	private int pageBase = 15;
	private int currentCount = 0;
	private String recallEmails = null;
	private String mid = null;
	private PageManager pm = null;
	private MdnListVO mdnListInfo = null;
	private MdnContentVO mdnContent = null;


	private static final long serialVersionUID = -8248351268910794857L;

	private MailServiceManager mailServiceManager = null;

	public void setMailServiceManager(MailServiceManager mailServiceManager) {
		this.mailServiceManager = mailServiceManager;
	}

	public String executeMdnList() throws Exception{

		if (page == 0) {
			page = 1;
		}

		try {

			makeFolderInfo(FOLDER_AND_QUOTA);

			ListCondVO listVO = new ListCondVO();
			listVO.setRemoteIp(request.getRemoteAddr());
			listVO.setPageBase(pageBase);
			listVO.setPage(page);
			listVO.setLocale(user.get(User.LOCALE));
			listVO.setEmail(user.get(User.EMAIL));

			mdnListInfo = mailServiceManager.doSimpleMdnList(listVO, user);
			if (mdnListInfo.getMdnList() != null) {
				currentCount = mdnListInfo.getMdnList().length;
			}
			pm = new PageManager();
			pm.initParameter(mdnListInfo.getTotalCnt(), pageBase, 5);
			pm.setPage(page);

		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			return "errorMobile";
		}


		return "success";
	}

	public String executeMdnView(){

		if (page == 0) {
			page = 1;
		}

		try {
			makeFolderInfo(FOLDER_AND_QUOTA);

			ReadCondVO readVO = new ReadCondVO();
			readVO.setRemoteIp(request.getRemoteAddr());
			readVO.setUid(uid);
			readVO.setPage(page);
			readVO.setPageBase(pageBase);
			readVO.setLocale(user.get(User.LOCALE));
			readVO.setEmail(user.get(User.EMAIL));
			mdnContent = mailServiceManager.doSimpleMdnView(readVO, user);

			if (mdnContent.getRcptVos() != null) {
				currentCount = mdnContent.getRcptVos().length;
			}
			pm = new PageManager();
			pm.initParameter(mdnContent.getMdnResponseTotal(), pageBase, 5);
			pm.setPage(page);

			LogManager.writeMailLogMsg(true, log, user.get(User.EMAIL), request.getRemoteAddr(), "action_mobile_mdn_message"); //TCUSTOM-2155
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			return "errorMobile";
		}

		return "success";
	}

	public String executeMdnRecall() throws Exception {

		if (page == 0) {
			page = 1;
		}

		try {
			makeFolderInfo(FOLDER_AND_QUOTA);

			MdnRecallVO mdnRecallVO = new MdnRecallVO();
			mdnRecallVO.setRemoteIp(request.getRemoteAddr());
			mdnRecallVO.setLocale(user.get(User.LOCALE));
			mdnRecallVO.setEmail(user.get(User.EMAIL));
			mdnRecallVO.setMid(mid);
			mdnRecallVO.setUid(uid);
			mdnRecallVO.setRecallEmails(recallEmails);

			mailServiceManager.doSimpleMdnRecall(mdnRecallVO, user);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			return "errorMobile";
		}
		return "success";
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getListPage() {
		return listPage;
	}

	public void setListPage(int listPage) {
		this.listPage = listPage;
	}

	public int getPageBase() {
		return pageBase;
	}

	public void setPageBase(int pageBase) {
		this.pageBase = pageBase;
	}

	public int getCurrentCount() {
		return currentCount;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public void setRecallEmails(String recallEmails) {
		this.recallEmails = recallEmails;
	}

	public MdnListVO getMdnListInfo() {
		return mdnListInfo;
	}


	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public PageManager getPm() {
		return pm;
	}

	public void setPm(PageManager pm) {
		this.pm = pm;
	}

	public MdnContentVO getMdnContent() {
		return mdnContent;
	}

}
