package com.terracetech.tims.mobile.mail.action;

import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.mobile.common.manager.PageManager;
import com.terracetech.tims.service.manager.MailServiceManager;
import com.terracetech.tims.service.tms.vo.ListCondVO;
import com.terracetech.tims.service.tms.vo.ListInfoVO;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringReplaceUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class MailListAction extends BaseAction {

	private static final long serialVersionUID = 201002191800L;

	private MailServiceManager mailServiceManager = null;

	private int page = 0;
	private final int pageBase = 15;
	private int currentCount = 0;
	private String folderName = null;
	private String sortBy = null;
	private String sortDir = null;
	private String flag = null;
	private String keyWord = null;
	private String thisDate = null;

	private PageManager pm = null;
	private ListInfoVO listInfoVo = null;

	public void setMailServiceManager(MailServiceManager mailServiceManager) {
		this.mailServiceManager = mailServiceManager;
	}

	@Override
	public String execute() throws Exception {

		char flagType = (flag != null && flag.length() > 0)?flag.charAt(0):'x';

		if (StringUtils.isEmpty(sortBy))
			sortBy = "arrival";

		if (StringUtils.isEmpty(sortDir))
			sortDir = "desc";

		if (page == 0) {
			page = 1;
		}

		try {
			folderName = StringUtils.doubleUrlDecode(folderName);

			if (StringUtils.isEmpty(folderName)) {
				folderName = FolderHandler.INBOX;
			}

			makeFolderInfo(FOLDER_AND_QUOTA);

			String folderEncodeName = TMailUtility.IMAPFolderEncode(folderName);

			ListCondVO listVo = new ListCondVO();

			if(StringUtils.isNotEmpty(keyWord)){
				keyWord = StringUtils.doubleUrlDecode(keyWord);

				listVo.setAdvancedSearch(false);
				listVo.setCategory("s");
				listVo.setKeyWord(keyWord);
				listVo.setFromEmailPattern(keyWord);
				listVo.setToEmailPattern(keyWord);
				listVo.setFolderName("all");
				listVo.setOperation("or");
			} else {
				listVo.setFolderName(folderEncodeName);
			}
			listVo.setRemoteIp(request.getRemoteAddr());
			listVo.setPageBase(pageBase);
			listVo.setPage(page);
			listVo.setLocale(user.get(User.LOCALE));
			listVo.setSortBy(sortBy);
			listVo.setSortDir(sortDir);

			listVo.setEmail(user.get(User.EMAIL));
			listVo.setFlagType(flagType);

			listInfoVo = mailServiceManager.doSimpleMailList(listVo, user);

			int total = 0;
			if (listInfoVo != null && listInfoVo.getListContents() != null) {
				currentCount = listInfoVo.getListContents().length;
				total = listInfoVo.getTotalCnt();
				page = listInfoVo.getPageNo();
			}

			pm = new PageManager();
			pm.initParameter(total, pageBase, 5);
			pm.setPage(page);

			thisDate = FormatUtil.getBasicDateStr();
			thisDate = thisDate.substring(0,8);

			request.setAttribute("category", "s");
			request.setAttribute("advancedSearch", "on");

			//XSS script 20120326 seaofnight
			setFolderName(StringReplaceUtil.replaceHtml(folderName));
			setFlag(StringReplaceUtil.replaceHtml(flag));
			setKeyWord(StringReplaceUtil.replaceHtml(keyWord));
			setSortBy(StringReplaceUtil.replaceHtml(sortBy));
			setSortDir(StringReplaceUtil.replaceHtml(sortDir));

			LogManager.writeMailLogMsg(true, log, user.get(User.EMAIL), request.getRemoteAddr(), "action_mobile_list_message"); //TCUSTOM-2155

		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			return "errorMobile";
		}

		return "success";
	}

	public String executeFolderList() throws Exception {

		makeFolderInfo(FOLDER_AND_QUOTA);

		return "success";
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageBase() {
		return pageBase;
	}

	public int getCurrentCount() {
		return currentCount;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getSortDir() {
		return sortDir;
	}

	public void setSortDir(String sortDir) {
		this.sortDir = sortDir;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public ListInfoVO getListInfoVo() {
		return listInfoVo;
	}

	public String getThisDate() {
		return thisDate;
	}

	public PageManager getPm() {
		return pm;
	}

}
