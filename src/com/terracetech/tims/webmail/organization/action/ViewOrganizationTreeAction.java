package com.terracetech.tims.webmail.organization.action;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.organization.manager.OrganizationManager;
import com.terracetech.tims.webmail.organization.vo.DeptVO;

public class ViewOrganizationTreeAction extends BaseAction{

	private static final long serialVersionUID = 20081229L;
	
	private OrganizationManager manager = null;
	
	private DeptVO root = null;
	
	private String isPopup = null;
	
	public void setManager(OrganizationManager manager) {
		this.manager = manager;
	}
	

	public DeptVO getRoot() {
		return root;
	}

	public String execute() throws Exception {
		
		
		return executePart();
	}

	public String executePart() throws Exception {

		return "success";
	}

	public String getIsPopup() {
		return isPopup;
	}

	public void setIsPopup(String isPopup) {
		this.isPopup = isPopup;
	}
	
}
