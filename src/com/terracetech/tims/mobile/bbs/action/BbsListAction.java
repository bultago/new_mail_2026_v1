package com.terracetech.tims.mobile.bbs.action;

import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.service.tms.impl.BbsService;
import com.terracetech.tims.service.tms.vo.BbsInfoVO;

public class BbsListAction extends BaseAction {

	private static final long serialVersionUID = -4437503388139421534L;
	
	private BbsInfoVO[] bbsInfoList = null;
	private BbsService bbsService = null;
	
	public void setBbsService(BbsService bbsService) {
		this.bbsService = bbsService;
	}

	public String execute() throws Exception {
		
		bbsInfoList = bbsService.readBbsList(null, user);
		
		return "success";
	}

	public BbsInfoVO[] getBbsInfoList() {
		return bbsInfoList;
	}
}
