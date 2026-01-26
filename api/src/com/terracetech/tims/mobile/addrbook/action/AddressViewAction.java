package com.terracetech.tims.mobile.addrbook.action;

import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.service.tms.impl.ContactService;
import com.terracetech.tims.service.tms.vo.ContactCondVO;
import com.terracetech.tims.service.tms.vo.ContactGroupVO;
import com.terracetech.tims.service.tms.vo.ContactMemberVO;
import com.terracetech.tims.service.tms.vo.PublicAuthVO;
import com.terracetech.tims.webmail.mailuser.User;

public class AddressViewAction extends BaseAction {

	private static final long serialVersionUID = 1204686380077014775L;
	
	private ContactService contactService = null;
	
	public void setContactService(ContactService contactService) {
		this.contactService = contactService;
	}
	public String executeViewForm() throws Exception{
		
		String keyword = request.getParameter("keyWord");
		String addrType = request.getParameter("addrType");
		String groupSeq = request.getParameter("groupSeq");
		String page = request.getParameter("page");
				
		
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));		
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		String memberSeqStr = request.getParameter("mseq");
		int memberSeq = (memberSeqStr != null)?Integer.parseInt(memberSeqStr):0;
		
		String addrbookSeqStr = request.getParameter("bookSeq");
		int addrbookSeq = (addrbookSeqStr != null)?Integer.parseInt(addrbookSeqStr):0;
		
		ContactCondVO cond = new ContactCondVO();
		cond.setDomainSeq(domainSeq);
		cond.setUserSeq(userSeq);
		cond.setMemberSeq(memberSeq);
		cond.setBookSeq(addrbookSeq);
		ContactMemberVO member = contactService.readContactMember(cond);
		
		PublicAuthVO authority = null;
		if("public".equals(addrType)){
			authority = contactService.readPublicBookAuth(cond);
		} else {
			authority = new PublicAuthVO();
			authority.setCreatorAuth("Y");
			authority.setReadAuth("Y");
			authority.setWriteAuth("Y");
		}
		
		request.setAttribute("authority", authority);
		request.setAttribute("member", member);
		request.setAttribute("keyWord", keyword);
		request.setAttribute("addrType", addrType);
		request.setAttribute("page", page);
		request.setAttribute("bookSeq", addrbookSeq);
		request.setAttribute("groupSeq", groupSeq);		
		request.setAttribute("memberSeq", memberSeq);
					
		return "success";
	}
	
	public String executeAddForm() throws Exception{
		
		String keyword = request.getParameter("keyword");
		String addType = request.getParameter("addType");
		String addrType = request.getParameter("addrType");
		String groupSeq = request.getParameter("groupSeq");
		String page = request.getParameter("page");
		
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));		
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		String memberSeqStr = request.getParameter("mseq");
		int memberSeq = (memberSeqStr != null)?Integer.parseInt(memberSeqStr):0;
		
		String addrbookSeqStr = request.getParameter("bookSeq");
		int addrbookSeq = (addrbookSeqStr != null)?Integer.parseInt(addrbookSeqStr):0;
		
		ContactCondVO cond = new ContactCondVO();
		cond.setDomainSeq(domainSeq);
		cond.setUserSeq(userSeq);
		cond.setMemberSeq(memberSeq);
		cond.setBookSeq(addrbookSeq);
		
		ContactMemberVO member = null;
		
		if("modify".equals(addType)){
			member = contactService.readContactMember(cond);
		} else if("add".equals(addType)){
			member = new ContactMemberVO();
		}
		
		request.setAttribute("member", member);				
		request.setAttribute("keyword", keyword);
		request.setAttribute("addrType", addrType);
		request.setAttribute("page", page);
		request.setAttribute("bookSeq", addrbookSeq);
		request.setAttribute("groupSeq", groupSeq);
		request.setAttribute("addType", addType);
					
		return "success";
	}

}
