package com.terracetech.tims.mobile.bbs.action;

import java.text.SimpleDateFormat;
import java.util.Date;


import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.mobile.common.manager.PageManager;
import com.terracetech.tims.service.tms.impl.BbsService;
import com.terracetech.tims.service.tms.vo.BbsContentCondVO;
import com.terracetech.tims.service.tms.vo.BbsContentInfoVO;
import com.terracetech.tims.service.tms.vo.BbsInfoVO;
import com.terracetech.tims.webmail.util.StringUtils;

public class BbsContentListAction extends BaseAction {

	private static final long serialVersionUID = -6113894593078363468L;
	
	private int bbsId = 0;
	private int pageBase = 15;
	private int noticeCount = 5;
	private int currentCount = 0;
	private String searchType = null;
	private String keyWord = null;
	private int page = 0;
	private String today = "";
	
	private BbsInfoVO[] bbsInfoList = null;
	private BbsContentInfoVO bbsContentInfo = null;
	private BbsService bbsService = null;
	
	private PageManager pm;
	
	public void setBbsService(BbsService bbsService) {
		this.bbsService = bbsService;
	}

	public String execute() throws Exception {
		
		page = (page == 0) ? 1 : page;
		
		keyWord = StringUtils.doubleUrlDecode(keyWord);
		keyWord = StringUtils.isEmpty(keyWord) ? "" : keyWord;
		
		BbsContentCondVO bbsContentCondVo = new BbsContentCondVO();
		bbsContentCondVo.setBbsId(bbsId);
		bbsContentCondVo.setCurrentPage(page);
		bbsContentCondVo.setPageBase(pageBase);
		bbsContentCondVo.setNoticeCount(noticeCount);
		bbsContentCondVo.setSearchType(searchType);
		bbsContentCondVo.setKeyWord(keyWord);
		bbsContentCondVo.setRemoteIp(request.getRemoteAddr());
		
		bbsInfoList = bbsService.readBbsList(null, user);
		
		bbsContentInfo = bbsService.readBbsContentList(bbsContentCondVo, user);
		if (bbsContentInfo.getBbsContentList() != null && bbsContentInfo.getBbsContentList().length > 0) {
			currentCount = bbsContentInfo.getBbsContentList().length;
		}
		
		pm = new PageManager();
		pm.initParameter(bbsContentInfo.getTotalCount(), pageBase, 5);
		pm.setPage(page);
		
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
		today = DATE_FORMAT.format(new Date());
		
		
		return "success";
	}
	
	public String getToday() {
		return today;
	}

	public BbsContentInfoVO getBbsContentInfo() {
		return bbsContentInfo;
	}

	public BbsInfoVO[] getBbsInfoList() {
		return bbsInfoList;
	}
	
	public int getPageBase() {
		return pageBase;
	}

	public PageManager getPm() {
		return pm;
	}

	public void setBbsId(int bbsId) {
		this.bbsId = bbsId;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getCurrentCount() {
		return currentCount;
	}
}
