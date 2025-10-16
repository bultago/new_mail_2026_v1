package com.terracetech.tims.mobile.bbs.action;

import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.service.tms.impl.BbsService;
import com.terracetech.tims.service.tms.vo.BbsContentVO;
import com.terracetech.tims.service.tms.vo.BbsInfoVO;
import com.terracetech.tims.service.tms.vo.BbsViewContentCondVO;
import com.terracetech.tims.webmail.util.StringUtils;

public class BbsContentViewAction extends BaseAction{
	
	private static final long serialVersionUID = -6977436884879330833L;
	
	private BbsService bbsService = null;
	private int bbsId = 0;
	private int contentId = 0;
	private int parentId = 0;
	private int orderNo = 0;
	private String bbsType = null;
	private boolean bbsAdmin = false;
	private boolean bbsCreator = false;
	private String keyWord = null;
	private int pageBase = 15;
	private String searchType = null;	
	private int page = 0;
	
	private BbsInfoVO[] bbsInfoList = null;
	private BbsContentVO contentVo = null;
	
	public void setBbsService(BbsService bbsService) {
		this.bbsService = bbsService;
	}
	public String execute() throws Exception {
		
		
		page = (page == 0) ? 1 : page;
		
		keyWord = StringUtils.doubleUrlDecode(keyWord);
		keyWord = StringUtils.isEmpty(keyWord) ? "" : keyWord;
		
		bbsInfoList = bbsService.readBbsList(null, user);
		BbsViewContentCondVO bbsViewContentCondVo = new BbsViewContentCondVO();
		
		bbsViewContentCondVo.setBbsId(bbsId);
		bbsViewContentCondVo.setContentId(contentId);
		bbsViewContentCondVo.setParentId(parentId);
		bbsViewContentCondVo.setOrderNo(orderNo);
		bbsViewContentCondVo.setBbsType(bbsType);
		bbsViewContentCondVo.setKeyWord(keyWord);
		bbsViewContentCondVo.setRequest(request);
		bbsViewContentCondVo.setSearchType(searchType);
		bbsViewContentCondVo.setPageBase(pageBase);
		bbsViewContentCondVo.setCurrentPage(page);
		bbsViewContentCondVo.setContext(context);
		
		
		contentVo = bbsService.readBoardContent(bbsViewContentCondVo,user);
		String content = contentVo.getHtmlContent();
		if(content != null && 
				content.indexOf("<") == -1 && 
				content.indexOf("TerraceMsg") == -1){
			content = TMailUtility.getTextToHtml(content);
		}
		contentVo.setHtmlContent(content);
		
		return "success";
	}
	
	public int getPageBase() {
		return pageBase;
	}
	public void setPageBase(int pageBase) {
		this.pageBase = pageBase;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public BbsContentVO getContentVo() {
		return contentVo;
	}
	public void setContentVo(BbsContentVO contentVo) {
		this.contentVo = contentVo;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public BbsInfoVO[] getBbsInfoList() {
		return bbsInfoList;
	}
	public void setBbsInfoList(BbsInfoVO[] bbsInfoList) {
		this.bbsInfoList = bbsInfoList;
	}
	public String getBbsType() {
		return bbsType;
	}
	public void setBbsType(String bbsType) {
		this.bbsType = bbsType;
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
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public boolean isBbsAdmin() {
		return bbsAdmin;
	}
	public void setBbsAdmin(boolean bbsAdmin) {
		this.bbsAdmin = bbsAdmin;
	}
	public boolean isBbsCreator() {
		return bbsCreator;
	}
	public void setBbsCreator(boolean bbsCreator) {
		this.bbsCreator = bbsCreator;
	}
	public BbsService getBbsService() {
		return bbsService;
	}		
}
