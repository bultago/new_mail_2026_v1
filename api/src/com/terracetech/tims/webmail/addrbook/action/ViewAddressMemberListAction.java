package com.terracetech.tims.webmail.addrbook.action;

import java.util.List;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.common.PageManager;
import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookAuthVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringUtils;

public class ViewAddressMemberListAction extends BaseAction {

	private static final long serialVersionUID = 20081215L;
	
	private AddressBookManager manager;
	
	private List<AddressBookMemberVO> members;
	
	private String leadingPattern;
	
	private int groupSeq;
	
	private int bookSeq;
	
	private int page;
	
	private int total;
	
	private int pageBase = 5;
	
	private String paneMode;
	
	private String addrType = null;
	
	private String pagePrint = null;
	
	private String sortBy = null;
	private String sortDir = null;
	
	private String searchType = null; 
	private String keyWord = null;
	
	private String[] leadingCharaters = null;
	
	public String getPaneMode() {
		return paneMode;
	}

	public void setPaneMode(String paneMode) {
		this.paneMode = paneMode;
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
	

	public void setPagePrint(String pagePrint) {
		this.pagePrint = pagePrint;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getGroupSeq() {
		return groupSeq;
	}

	public void setGroupSeq(int groupSeq) {
		this.groupSeq = groupSeq;
	}
	
	public int getBookSeq() {
		return bookSeq;
	}

	public void setBookSeq(int bookSeq) {
		this.bookSeq = bookSeq;
	}

	public void setLeadingPattern(String leadingPattern) {
		this.leadingPattern = leadingPattern;
	}
	
	public String getLeadingPattern() {
		return leadingPattern;
	}
	
	public String[] getLeadingCharaters() {
		return leadingCharaters;
	}
	
	public void setLeadingCharaters(String[] leadingCharaters) {
		this.leadingCharaters = leadingCharaters;
	}
	
	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public void setManager(AddressBookManager manager) {
		this.manager = manager;
	}
	
	public List<AddressBookMemberVO> getMembers() {
		return members;
	}

	public String getAddrType() {
		return addrType;
	}

	public void setAddrType(String addrType) {
		this.addrType = addrType;
	}

	public String execute() throws Exception{
		
		if(sortBy == null){
			sortBy = "name";
		}
		if(sortDir == null){
			sortDir = "asc";
		}
		
		if(StringUtils.isEmpty(leadingPattern))
			leadingPattern = "all";
		
		LogManager.writeDebug(this, "PrivateAddressListAction.leadingPattern = " + leadingPattern);
			
		members = manager.readPrivateMemberListByIndex(5, groupSeq, leadingPattern, 0, 10, sortBy, sortDir);
		
		return "success";
	}
	
	public String executePart() throws Exception{
		
		I18nResources resource = getMessageResource("addr");
		
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		pageBase =  Integer.parseInt(user.get(User.PAGE_LINE_CNT));
		
		if ("popup".equals(addrType)) {
			pageBase = 10;
		}
		
		if(StringUtils.isEmpty(leadingPattern))
			leadingPattern = "all";
		
		LogManager.writeDebug(this, "PrivateAddressListAction.leadingPattern = " + leadingPattern);
		
		setLeadingCharaters(manager.getAlphabet(resource.getMessage("addr.table.index")));
		
		if(sortBy == null){
			sortBy = "name";
		}
		if(sortDir == null){
			sortDir = "asc";
		}
		
		AddressBookAuthVO auth = null;
		if(getBookSeq()==0){
			if(StringUtils.isNotEmpty(keyWord)){
				total = manager.readPrivateSearchMemberCount(userSeq, groupSeq, searchType,keyWord,leadingPattern);
				initPageNavigation(total);
				members = manager.readPrivateSearchMember(userSeq, groupSeq, searchType,keyWord, leadingPattern, page, pageBase, sortBy, sortDir);
			}else{
				total = manager.readPrivateMemberListCount(userSeq, groupSeq, leadingPattern);
				initPageNavigation(total);
				members = manager.readPrivateMemberListByIndex(userSeq, groupSeq, leadingPattern, page, pageBase, sortBy, sortDir);
			}
			
			auth = manager.readPrivateAddressBookAuth(domainSeq, getBookSeq(), userSeq);
		}else{
			if(StringUtils.isNotEmpty(keyWord)){
				total = manager.readSharedSearchMemberCount(bookSeq, groupSeq, userSeq, searchType,keyWord,leadingPattern);
				initPageNavigation(total);
				members = manager.readSharedSearchMember(bookSeq, groupSeq, userSeq, searchType,keyWord, leadingPattern, page, pageBase, sortBy, sortDir);
			}else{
				total = manager.readSharedMemberListCount(getBookSeq(), groupSeq, leadingPattern);
				initPageNavigation(total);
				members = manager.readSharedMemberListByIndex(bookSeq, groupSeq, leadingPattern, page, pageBase, sortBy, sortDir);
			}
			
			auth = manager.readSharedAddressBookAuth(domainSeq, getBookSeq(), userSeq);
		}
		if(members !=null){
			for (AddressBookMemberVO member : members) {
				member.setMemberName(StringUtils.replace(member.getMemberName(), "'", ""));
			}	
		}
		
		request.setAttribute("auth", auth);
		
		PageManager pm = new PageManager();
		pm.initParameter(total, pageBase, 5);
		pm.setPage(page);
		
		request.setAttribute("currentPage", page);
		request.setAttribute("pageBase", pageBase);
		request.setAttribute("total", total);
		request.setAttribute("pm", pm);
		
		if("v".equals(paneMode))
			return "tab";
		
		if(StringUtils.isEmpty(pagePrint)){
			String installLocale = EnvConstants.getBasicSetting("setup.state");
			if("jp".equalsIgnoreCase(installLocale) && !"popup".equals(addrType)){
				return "japan";
			}
			return "success";
		}
			
		
		return "print";
	}

	private void initPageNavigation(int total) {
		page = (page == 0)? 1 : page;
		
		int npages = (int) Math.ceil((double) total / pageBase);
		if (0 < npages && npages < page) {
			page = npages;				
		}
	}
	
}
