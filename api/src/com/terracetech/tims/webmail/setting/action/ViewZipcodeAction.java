package com.terracetech.tims.webmail.setting.action;

import java.util.List;

import com.terracetech.tims.common.PageManager;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.ZipcodeVO;
import com.terracetech.tims.webmail.util.StringReplaceUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class ViewZipcodeAction extends BaseAction {
	
	private SettingManager settingManager = null;
	private String dong = null;
	private List<ZipcodeVO> zipcodeList = null;
	private int currentPage = 0;
	private PageManager pm = null;
	private String type = null;
	
	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}
	
	public ViewZipcodeAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}
	
	public String execute() throws Exception {
		
		int pageBase = 10;
		
		currentPage = (currentPage == 0) ? 1 : currentPage;
		dong = (dong == null) ? "" : dong;
		
		int skipResult = (currentPage - 1) * pageBase;
		int zipcodeListCount = 0;
		
		if (StringUtils.isNotEmpty(dong)) {
			zipcodeListCount = settingManager.getZipcodeListCount(dong);
			zipcodeList = settingManager.getZipcodeList(dong, skipResult, pageBase);
			pm = new PageManager();
			pm.initParameter(zipcodeListCount, pageBase, 10);
			pm.setPage(currentPage);
			
			//20120321 seaofnight XSS
			setDong(StringReplaceUtil.replaceHtml(dong));
		}
		
		return "success";
	}

	public String getDong() {
		return dong;
	}

	public void setDong(String dong) {
		this.dong = dong;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	public PageManager getPm() {
		return pm;
	}

	public void setPm(PageManager pm) {
		this.pm = pm;
	}

	public List<ZipcodeVO> getZipcodeList() {
		return zipcodeList;
	}

	public void setZipcodeList(List<ZipcodeVO> zipcodeList) {
		this.zipcodeList = zipcodeList;
	}
}
