package com.terracetech.tims.mobile.mail.action;

import org.apache.log4j.Logger;

import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.service.manager.MailServiceManager;
import com.terracetech.tims.service.tms.vo.ReadCondVO;
import com.terracetech.tims.service.tms.vo.ViewContentVO;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringUtils;

public class MailReadAction extends BaseAction{

	private static final long serialVersionUID = -510530030416305126L;

	private MailServiceManager mailServiceManager = null;

	private int page = 0;
	private String uid = null;
	private String folderName = null;
	private String sortBy = null;
	private String sortDir = null;
	private String flag = null;
	private String keyWord = null;
	private String advancedSearch = null;
	private String category = null;

	public Logger log = Logger.getLogger(this.getClass());


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

		try {
			folderName = StringUtils.doubleUrlDecode(folderName);

			if (StringUtils.isEmpty(folderName)) {
				folderName = FolderHandler.INBOX;
			}

			if(uid == null)throw new Exception("USER["+user.get(User.MAIL_UID)+"] ReadMessage UID is null!");
			if(folderName == null)throw new Exception("USER["+user.get(User.MAIL_UID)+"] ReadMessage FolderName is null!");

			makeFolderInfo(FOLDER_AND_QUOTA);

			String lfolderName = request.getParameter("lfolderName");
			String attachesDir = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
			String hostStr = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort();

			String folderEncodeName = TMailUtility.IMAPFolderEncode(folderName);

			ReadCondVO readVO = new ReadCondVO();
			readVO.setEmail(user.get(User.EMAIL));
			readVO.setUid(Long.parseLong(uid));
			readVO.setFolder(folderEncodeName);
			readVO.setViewImage(false);
			readVO.setLocale(user.get(User.LOCALE));
			readVO.setLocalURL(hostStr);
			readVO.setAttachDir(attachesDir);
			readVO.setSortBy(sortBy);
			readVO.setSortDir(sortDir);
			readVO.setRemoteIp(request.getRemoteAddr());
			if(StringUtils.isNotEmpty(keyWord)){
				keyWord = StringUtils.doubleUrlDecode(keyWord);
				readVO.setKeyWord(keyWord);
			}
			readVO.setFlagType(flagType);

			ViewContentVO viewContents = mailServiceManager.doSimpleMailRead(readVO, user);
			String contents = viewContents.getContents();
		        contents = StringUtils.removeStyleTag(contents);

			request.setAttribute("contents", viewContents);
			request.setAttribute("lfolderName", lfolderName);
			request.setAttribute("userLocale", user.get(User.LOCALE));

			LogManager.writeMailLogMsg(true, log, user.get(User.EMAIL), request.getRemoteAddr(), "action_mobile_read_message", viewContents.getFolderEncName(), viewContents.getFrom().getEmailAddress(), "", viewContents.getSize(), EnvConstants.DEFAULT_CHARSET,  viewContents.getSubject(), viewContents.getMessageId()); //TCUSTOM-2155
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

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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

	public String getAdvancedSearch() {
		return advancedSearch;
	}

	public void setAdvancedSearch(String advancedSearch) {
		this.advancedSearch = advancedSearch;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}


}
