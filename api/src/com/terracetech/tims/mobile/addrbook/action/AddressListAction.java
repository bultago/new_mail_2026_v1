package com.terracetech.tims.mobile.addrbook.action;

import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.mobile.common.manager.PageManager;
import com.terracetech.tims.service.tms.impl.ContactService;
import com.terracetech.tims.service.tms.vo.ContactBookVO;
import com.terracetech.tims.service.tms.vo.ContactCondVO;
import com.terracetech.tims.service.tms.vo.ContactGroupVO;
import com.terracetech.tims.service.tms.vo.ContactInfoVO;
import com.terracetech.tims.service.tms.vo.PublicAuthVO;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringReplaceUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class AddressListAction extends BaseAction {

	private static final long serialVersionUID = 201003081150L;

	private ContactService contactService = null;
	
	private int groupSeq = 0;
	private int bookSeq = 0;
	private int page = 0;
	private int pageBase = 15;
	private int currentCount = 0;
	
	private String searchType = "fnameOrEmail";
	private String keyWord = null;
	private ContactInfoVO contactInfoVo = null;
	private ContactGroupVO[] contactGroupVos = null;
	private ContactBookVO[] contactBookVos = null;
	private PageManager pm = null;
	private PublicAuthVO authority = null;

	public void setContactService(ContactService contactService) {
		this.contactService = contactService;
	}
	
	public String executePrivate() throws Exception {
		
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		if (page == 0) {
			page = 1;
		}
		
		try {
			
			keyWord = StringUtils.doubleUrlDecode(keyWord);

			ContactCondVO contactCondVo = new ContactCondVO();
			contactCondVo.setDomainSeq(mailDomainSeq);
			contactCondVo.setUserEmail(user.get(User.EMAIL));		
			contactCondVo.setGroupSeq(groupSeq);
			contactCondVo.setCurrentPage(page);
			contactCondVo.setMaxResult(pageBase);
			contactCondVo.setSearchType(searchType);
			contactCondVo.setKeyWord(keyWord);	
			
			contactInfoVo = contactService.readContactMemberListByIndex(contactCondVo);
			int total = contactInfoVo.getTotalCount();
			
			if (contactInfoVo != null && contactInfoVo.getMemberList().length > 0) {
				currentCount = contactInfoVo.getMemberList().length;
			}
			
			contactGroupVos = contactService.readContactGroupList(contactCondVo);
			
			pm = new PageManager();
			pm.initParameter(total, pageBase, 5);
			pm.setPage(page);
			
			request.setAttribute("addrType", "private");
			keyWord = StringReplaceUtil.replaceHtml(keyWord);
			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}
		return "success";
	}
	
	public String executePublic() throws Exception {
		
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		if (page == 0) {
			page = 1;
		}
		
		try {
			
			keyWord = StringUtils.doubleUrlDecode(keyWord);
			
			ContactCondVO contactCondVo = new ContactCondVO();
			contactCondVo.setDomainSeq(mailDomainSeq);
			contactCondVo.setUserEmail(user.get(User.EMAIL));
			contactCondVo.setUserSeq(userSeq);
			contactCondVo.setGroupSeq(groupSeq);
			contactCondVo.setBookSeq(bookSeq);
			contactCondVo.setCurrentPage(page);
			contactCondVo.setMaxResult(pageBase);
			contactCondVo.setSearchType(searchType);
			contactCondVo.setKeyWord(keyWord);

			contactBookVos = contactService.readContactBookList(contactCondVo);
			if (contactBookVos != null && contactBookVos.length > 0) {
				if (bookSeq == 0) {
					bookSeq = contactBookVos[0].getAdrbookSeq();
					contactCondVo.setBookSeq(bookSeq);
				}
				
				contactInfoVo = contactService.readContactMemberListByIndex(contactCondVo);
				int total = contactInfoVo.getTotalCount();
				
				if (contactInfoVo != null && contactInfoVo.getMemberList().length > 0) {
					currentCount = contactInfoVo.getMemberList().length;
				}
				
				contactGroupVos = contactService.readContactGroupList(contactCondVo);
				
				pm = new PageManager();
				pm.initParameter(total, pageBase, 5);
				pm.setPage(page);
			}
			
			authority = contactService.readPublicBookAuth(contactCondVo);			
			
			request.setAttribute("addrType", "public");
			keyWord = StringReplaceUtil.replaceHtml(keyWord);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}
		return "success";
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

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getSearchType() {
		return searchType;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public ContactInfoVO getContactInfoVo() {
		return contactInfoVo;
	}

	public ContactGroupVO[] getContactGroupVos() {
		return contactGroupVos;
	}

	public ContactBookVO[] getContactBookVos() {
		return contactBookVos;
	}

	public PageManager getPm() {
		return pm;
	}

	public int getPageBase() {
		return pageBase;
	}

	public int getCurrentCount() {
		return currentCount;
	}

	public PublicAuthVO getAuthority() {
		return authority;
	}
}
