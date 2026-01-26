package com.terracetech.tims.mobile.bbs.action;

import jakarta.servlet.http.HttpSession;

import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.service.tms.impl.BbsService;
import com.terracetech.tims.service.tms.vo.BoardContentVO;


public class BbsContentDeleteAction extends BaseAction{
	
	private BbsService bbsService = null;
	private int bbsId = 0;
	private int contentId = 0;
	private BoardContentVO bbsContentVo = null;
	
	public void setBbsService(BbsService bbsService) {
		this.bbsService = bbsService;
	}
	
	public String execute() throws Exception {
		bbsContentVo = new BoardContentVO();
		bbsContentVo.setBbsId(bbsId);
		bbsContentVo.setContentId(contentId);
		bbsContentVo.setRequest(request);
		setBbsId(bbsId);
		bbsService.deleteBbsModifyContent(bbsContentVo,user);	
		
		return "success";
	}

	public int getBbsId() {
		return bbsId;
	}

	public void setBbsId(int bbsId) {
		this.bbsId = bbsId;
	}

	public int getContentId() {
		return contentId;
	}

	public void setContentId(int contentId) {
		this.contentId = contentId;
	}
	
	

}
