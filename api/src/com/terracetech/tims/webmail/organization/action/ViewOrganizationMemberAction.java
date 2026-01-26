package com.terracetech.tims.webmail.organization.action;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.PageManager;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.mailuser.vo.MailDomainCodeVO;
import com.terracetech.tims.webmail.organization.manager.OrganizationManager;
import com.terracetech.tims.webmail.organization.vo.MemberVO;

public class ViewOrganizationMemberAction extends BaseAction {

	private static final long serialVersionUID = 20081229L;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private OrganizationManager manager = null;
	
	private MailUserManager mailUserManager = null;
	
	private List<MailDomainCodeVO> classCodeList = null;
	
	private List<MailDomainCodeVO> titleCodeList = null;

	private MemberVO[] members = null;

	private String orgCode = null;
	
	private String orgFullCode = null;

	private int page;

	private int total;

	private int pageBase = 5;
	
	private String sortBy = null;
	private String sortDir = null;
	
	private String searchType = null; 
	private String keyWord = null;
	
	private String orgType = null;
	
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

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	
	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
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

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public void setManager(OrganizationManager manager) {
		this.manager = manager;
	}

	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}

	public MemberVO[] getMembers() {
		return members;
	}

	public List<MailDomainCodeVO> getClassCodeList() {
		return classCodeList;
	}

	public List<MailDomainCodeVO> getTitleCodeList() {
		return titleCodeList;
	}

	public void setOrgFullCode(String orgFullCode) {
		this.orgFullCode = orgFullCode;
	}

	public String execute() throws Exception {

		return "success";
	}

	public String executePart() throws Exception {
		
		try {
			if(sortBy == null){
				sortBy = "init";
			}
			if(sortDir == null){
				sortDir = "asc";
			}
			
			pageBase =  Integer.parseInt(user.get(User.PAGE_LINE_CNT));
			
			if ("popup".equals(orgType)) {
				pageBase = 10;
			}
			
			int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
			boolean hasSubDept = manager.hasSubDept(domainSeq);
			
			String installLocale = EnvConstants.getBasicSetting("setup.state");
			String userLocale = user.get(User.LOCALE);
			String orgLocale = userLocale;
			if ("jp".equals(installLocale)) {
				orgLocale = "ko".equals(userLocale) ? "en" : userLocale;
			} else if ("ko".equals(installLocale)) {
				orgLocale = "jp".equals(userLocale) ? "en" : userLocale;
			}
			
			total = manager.readMemberCount(orgLocale, orgCode, orgFullCode, hasSubDept, domainSeq, searchType, keyWord);
			initPageNavigation(total);
			members = manager.readMemberList(orgLocale, orgCode, orgFullCode, hasSubDept, domainSeq, page, pageBase, searchType, keyWord, sortBy, sortDir);
			
			if("init".equals(sortBy)){
				sortBy = manager.initSortType(domainSeq);
			}			
			titleCodeList = mailUserManager.getMailDomainCode(domainSeq, "101", orgLocale);
			classCodeList = mailUserManager.getMailDomainCode(domainSeq, "100", orgLocale);
			
			PageManager pm = new PageManager();
			pm.initParameter(total, pageBase, 5);
			pm.setPage(page);
			
			request.setAttribute("currentPage", page);
			request.setAttribute("pageBase", pageBase);
			request.setAttribute("total", total);
			request.setAttribute("pm", pm);

			return "success";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "subError";
		}
		
	}
	
	private void initPageNavigation(int total) {
		page = (page == 0)? 1 : page;
		
		int npages = (int) Math.ceil((double) total / pageBase);
		if (0 < npages && npages < page) {
			page = npages;
		}
	}
}
