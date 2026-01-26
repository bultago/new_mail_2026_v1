package com.terracetech.tims.jmobile.mail.action;

import java.util.HashMap;
import java.util.Map;

import com.terracetech.tims.common.PageManager;
import com.terracetech.tims.jmobile.common.action.BaseAction;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.service.manager.MailServiceManager;
import com.terracetech.tims.service.tms.vo.ListCondVO;
import com.terracetech.tims.service.tms.vo.ListInfoVO;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class MailListAction extends BaseAction {

	private static final long serialVersionUID = 1610141573075345174L;
	private MailServiceManager mailServiceManager = null;
	
	private int page = 0;
	private int pageBase = 10;
	private int currentCount = 0;
	private String folderName = null;
	private String sortBy = null;
	private String sortDir = null;
	private String flag = null;	
	private String keyWord = null;
	private String thisDate = null;
	
	private ListInfoVO listInfoVo = null;
	private PageManager pm;
	
	public void setMailServiceManager(MailServiceManager mailServiceManager) {
		this.mailServiceManager = mailServiceManager;
	}
		
	public String execute() throws Exception{
		Map paramMap = new HashMap();
		
		Object preParam = request.getSession().getAttribute("preParamMap");
		Map preParamMap = null;
		
		if(preParam != null){
			preParamMap = (Map)preParam;
			if(preParamMap.containsKey("keyWord")){
				keyWord = (String)preParamMap.get("keyWord");
			}
			if(preParamMap.containsKey("page")){
				page = (Integer)preParamMap.get("page");
			}
			if(preParamMap.containsKey("folderName")){
				folderName = (String)preParamMap.get("folderName");
			}
			if(preParamMap.containsKey("keyWord")){
				keyWord = (String)preParamMap.get("keyWord");
			}
			if(preParamMap.containsKey("flag")){
				flag = (String)preParamMap.get("flag");
			}
			request.getSession().removeAttribute("preParamMap");
		}
		
		char flagType = (flag != null && flag.length() > 0)?flag.charAt(0):'x';
		
		if (StringUtils.isEmpty(sortBy))
			sortBy = "arrival";
		
		if (StringUtils.isEmpty(sortDir))
			sortDir = "desc";

		if (page == 0) {
			page = 1;
		}	
		
		
		try {					
			if (StringUtils.isEmpty(folderName)) {
				folderName = FolderHandler.INBOX;
			}
			
			makeFolderInfo();
			
			if("etc".equals(folderName)){
				request.setAttribute("action", "list");
				saveCurrentAction("folderSelect");
				return "folderSelect";
			}			

			String folderEncodeName = TMailUtility.IMAPFolderEncode(folderName);
			
			ListCondVO listVo = new ListCondVO();			
			listVo.setKeyWord(keyWord);
			listVo.setFolderName(folderEncodeName);
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
			}
			
			pm = new PageManager();
			pm.initParameter(total, pageBase, 5);
			pm.setPage(page);
			
			thisDate = FormatUtil.getBasicDateStr();
			thisDate = thisDate.substring(0,8);
			
			
			paramMap.put("keyWord", keyWord);
			paramMap.put("page", page);
			paramMap.put("folderName",folderName);
			paramMap.put("flag",flag);
			
			
			savePreAction("maillist", paramMap);
			saveCurrentAction("maillist");
		} catch (Exception e) {			
			LogManager.writeErr(this, e.getMessage(), e);
			return "errorAlert";
		}
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
