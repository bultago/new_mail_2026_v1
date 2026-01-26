package com.terracetech.tims.mobile.addrbook.action;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.service.tms.IContactService;
import com.terracetech.tims.service.tms.impl.ContactService;
import com.terracetech.tims.service.tms.vo.ContactCondVO;
import com.terracetech.tims.service.tms.vo.ContactMemberVO;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class AddressWorkAction extends BaseAction {
	
	private static final long serialVersionUID = 7586928802958342494L;
	
	private ContactService contactService = null;
	
	public void setContactService(ContactService contactService) {
		this.contactService = contactService;
	}
	
	private ContactMemberVO getWorkMemeber(){
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		String addrBookSeqStr = request.getParameter("bookSeq");
		int addrBookSeq = (addrBookSeqStr != null)?Integer.parseInt(addrBookSeqStr):0;
		
		ContactMemberVO member =  new ContactMemberVO();
		member.setDomainSeq(domainSeq);
		member.setUserSeq(userSeq);
		member.setAddrbookSeq(addrBookSeq);
		
		return member;
		
	}
	
	private void setListParam() throws Exception{
		String pageStr = request.getParameter("page");
		String keyWordStr = request.getParameter("keyWord");
		String groupSeq = request.getParameter("groupSeq");
		String bookSeq = request.getParameter("bookSeq");
		pageStr = (pageStr == null)?"1":pageStr;
		int page = Integer.parseInt(pageStr);
		if (page <= 0) {
			page = 1;
		}
		String keyWord = (keyWordStr != null)?StringUtils.doubleUrlDecode(keyWordStr):"";		
		request.setAttribute("page", page);
		request.setAttribute("pageBase", "15");
		request.setAttribute("keyWord", keyWord);
		request.setAttribute("groupSeq", groupSeq);
		request.setAttribute("bookSeq", bookSeq);
	}
	public String executeDel() throws Exception{
		I18nResources resource = getMessageResource("common");
		setListParam();
		String forward = null;
		String addrType = request.getParameter("addrType");
		try {
			String mseqs = request.getParameter("mseqs");
			String[] memberSeqStrs = mseqs.split("\\|");
			ContactMemberVO member = getWorkMemeber();
			member = settingMemberVO(member);
			contactService.delContact(member,
					FormatUtil.parseStringToInt(memberSeqStrs));
			forward = ("public".equals(addrType)) ? "publicAddr"
					: "privateAddr";
		} catch (Exception e) {
			request.setAttribute("msg", resource.getMessage("error.msg.001"));
			request.setAttribute("addrType", addrType);
			forward = "error";
		}
		return forward;
	}
	
	public String executeMove() throws Exception{
	    I18nResources resource = getMessageResource("common");
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		String addrType = request.getParameter("addrType");
		String addrBookSeqStr = request.getParameter("bookSeq");
		int addrBookSeq = (addrBookSeqStr != null)?Integer.parseInt(addrBookSeqStr):0;
		
		setListParam();
		String forward = null;
		
		try {
		    String mseqs = request.getParameter("mseqs");
		    String[] memberSeqStrs = mseqs.split("\\|");	
		    String sourceGroupSeqStr = request.getParameter("sourceGroupSeq");
		    String targetGroupSeqStr = request.getParameter("targetGroupSeq");
		
		    ContactCondVO cond = new ContactCondVO();
		
		    cond.setUserSeq(userSeq);
		    cond.setBookSeq(addrBookSeq);
		
		    contactService.moveContact(cond, FormatUtil.parseStringToInt(memberSeqStrs), 
										Integer.parseInt(sourceGroupSeqStr), 
										Integer.parseInt(targetGroupSeqStr));
		    forward = ("public".equals(addrType))?"publicAddr":"privateAddr";
		}catch (Exception e) {
		    request.setAttribute("msg", resource.getMessage("error.msg.001"));
		    request.setAttribute("addrType", addrType);
		    forward = "error";
                }
		return forward;
	}
	
	public String executeAdd() throws Exception{
		I18nResources resource = getMessageResource("common");
		I18nResources addrResource = getMessageResource("addr");
		setListParam();
		String forward = null;
		String addrType = request.getParameter("addrType");

		try {
			ContactMemberVO member = getWorkMemeber();
			member = settingMemberVO(member);
			member.setLocale(request.getLocale());

			int returnCode = contactService.addContact(member);

			if (returnCode == IContactService.SUCCESS) {
				forward = ("public".equals(addrType)) ? "publicAddr"
						: "privateAddr";
			} else {
				request.setAttribute("msg",
						addrResource.getMessage("addr.error.msg.01"));
				request.setAttribute("addrType", addrType);
				forward = "error";
			}
		} catch (Exception e) {
			request.setAttribute("msg", resource.getMessage("error.msg.001"));
			request.setAttribute("addrType", addrType);
			forward = "error";
		}
		return forward;
	}
	
	public String executeMod() throws Exception{
	    I18nResources resource = getMessageResource("common");
		setListParam();
		String forward = null;
		String addrType = request.getParameter("addrType");
		try {
		    ContactMemberVO member = getWorkMemeber();
		    member = settingMemberVO(member);
		    contactService.modContact(member);
		    forward = ("public".equals(addrType))?"publicAddr":"privateAddr";
		} catch (Exception e) {
		    request.setAttribute("msg", resource.getMessage("error.msg.001"));
		    request.setAttribute("addrType", addrType);
                    forward= "error";
                }
		return forward;
	}
	
	private ContactMemberVO settingMemberVO(ContactMemberVO member){
		String mseq = request.getParameter("memberSeq");
		int memberSeq = (mseq != null)?Integer.parseInt(mseq):0;
		member.setMemberSeq(memberSeq);
		member.setMemberName(request.getParameter("mName"));
		member.setMemberEmail(request.getParameter("mEmail"));
		member.setMobileNo(request.getParameter("mMobileNo"));
		member.setCompanyName(request.getParameter("mCompanyName"));
		member.setDepartmentName(request.getParameter("mDepartmentName"));
		member.setTitleName(request.getParameter("mTitleName"));
		member.setHomeTel(request.getParameter("mHomeTel"));
		member.setOfficeTel(request.getParameter("mOfficeTel"));
		String greoupSeqStr = request.getParameter("groupSeq");
		member.setGroupSeq((greoupSeqStr != null)?Integer.parseInt(greoupSeqStr):0);
		member.setBirthDay(request.getParameter("mBirthDay"));
		member.setAnniversaryDay(request.getParameter("mAnniversaryDay"));
		member.setHomePostalCode(request.getParameter("mPostalCode"));
		member.setHomeCountry(request.getParameter("mHomeCountry"));
		member.setHomeCity(request.getParameter("mHomeCity"));
		member.setHomeState(request.getParameter("mHomeState"));
		member.setHomeStreet(request.getParameter("mHomeStreet"));
		member.setHomeExtAddress(request.getParameter("mHomeExtAddress"));
		member.setHomeFax(request.getParameter("mHomeFax"));
		member.setPrivateHomepage(request.getParameter("mPrivateHomepage"));
		
		member.setOfficePostalCode(request.getParameter("mOfficePostalCode"));
		member.setOfficeCountry(request.getParameter("mOfficeCountry"));
		member.setOfficeCity(request.getParameter("mOfficeCity"));
		member.setOfficeState(request.getParameter("mOfficeState"));
		member.setOfficeStreet(request.getParameter("mOfficeStreet"));
		member.setOfficeExtAddress(request.getParameter("mOfficeExtAddress"));
		member.setOfficeFax(request.getParameter("mOfficeFax"));
		member.setOfficeHomepage(request.getParameter("mOfficeHomepage"));		
		
		return member;
	}
}
