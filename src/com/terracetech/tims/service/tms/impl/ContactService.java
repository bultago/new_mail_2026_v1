package com.terracetech.tims.service.tms.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.terracetech.tims.service.tms.IContactService;
import com.terracetech.tims.service.tms.vo.ContactBookVO;
import com.terracetech.tims.service.tms.vo.ContactCondVO;
import com.terracetech.tims.service.tms.vo.ContactGroupVO;
import com.terracetech.tims.service.tms.vo.ContactInfoVO;
import com.terracetech.tims.service.tms.vo.ContactMemberVO;
import com.terracetech.tims.service.tms.vo.PublicAuthVO;
import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookAuthVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookGroupVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookVO;
import com.terracetech.tims.webmail.exception.SaveFailedException;
import com.terracetech.tims.webmail.exception.UserNotFoundException;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.util.StringUtils;

public class ContactService implements IContactService{
	
	public Logger log = Logger.getLogger(this.getClass());
	
	private AddressBookManager manager = null;
	
	private MailUserManager mailUserManager = null;
	
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	
	public void setManager(AddressBookManager manager) {
		this.manager = manager;
	}

	public int addContact(ContactMemberVO member){
		
		if(member.getDomainSeq()==0){
			return FAILED;
		}
		
		int domainSeq = member.getDomainSeq();
		int groupSeq = member.getGroupSeq();
		
		if(member.getAddrbookSeq()==0){
			try {
				manager.savePrivateAddressMemberWithTransactional(Convert.converVO(member), groupSeq, domainSeq);
			} catch (SaveFailedException e) {
				log.error(e.getMessage(), e);
				
				return FAILED;
			}	
		}else{
			try {
				manager.saveSharedAddressMemberWithTransactional(Convert.converVO(member), groupSeq, domainSeq);
			} catch (SaveFailedException e) {
				log.error(e.getMessage(), e);
				
				return FAILED;
			}
		}
		
		
		return SUCCESS;
	}
	
	public int modContact(ContactMemberVO member){
		if(member.getDomainSeq()==0){
			return FAILED;
		}
		
		if(member.getAddrbookSeq()==0){
			manager.updatePrivateAddressMember(Convert.converVO(member));
		}else{
			manager.updateSharedAddressMember(Convert.converVO(member));
		}
		return SUCCESS;
	}
	
	public int delContact(ContactMemberVO member){
		if(member.getDomainSeq()==0){
			return FAILED;
		}
		
		int[] memberSeqs = {member.getMemberSeq()};
		if(member.getAddrbookSeq()==0){
			manager.deletePrivateMember(member.getUserSeq(), memberSeqs);
		}else{
			manager.deleteSharedMember(member.getAddrbookSeq(), memberSeqs);
		}
		
		return SUCCESS;
	}
	
	public int delContact(ContactMemberVO member, int[] deleteMemberSeqs){
		if(member.getDomainSeq()==0){
			return FAILED;
		}
		if(member.getAddrbookSeq()==0){
			manager.deletePrivateMember(member.getUserSeq(), deleteMemberSeqs);
		}else{
			manager.deleteSharedMember(member.getAddrbookSeq(), deleteMemberSeqs);
		}
		
		return SUCCESS;
	}
	
	
	public int moveContact(ContactCondVO cond,int[] memberSeqs, int sourceGroupSeq, int targetGroupSeq) 
	throws Exception{
		
		int userSeq = cond.getUserSeq();
		int bookSeq = cond.getBookSeq();
		if(userSeq == 0 && bookSeq == 0){
			String[] userParams = null;
			try {
				userParams = cond.getUserEmail().split("@");
				
				userSeq = mailUserManager.readUserSeq(userParams[0], userParams[1]);
				if(userSeq <=0){
					throw new UserNotFoundException();
				}
			} catch (Exception e) {
				throw new Exception("User Info Not Found");
			}
		}
		if(bookSeq == 0){
			manager.movePrivateMember(userSeq, memberSeqs, sourceGroupSeq, targetGroupSeq);
		} else {
			manager.moveSharedMember(bookSeq, memberSeqs, sourceGroupSeq, targetGroupSeq);
		}
		
		
		return SUCCESS;
	}
	
	private ContactMemberVO[] converAddressBookMemberVO(List<AddressBookMemberVO> result) {
		List<ContactMemberVO> list = new ArrayList<ContactMemberVO>();
		if(result==null || result.size()==0)
			return list.toArray(new ContactMemberVO[0]);
		
		for (AddressBookMemberVO member : result) {
			list.add(Convert.converVO(member));
		}
		return list.toArray(new ContactMemberVO[list.size()]);
	}
	
	
	
	
	
	private List<ContactGroupVO> converAddressBookGroupVO(List<AddressBookGroupVO> result) {
		List<ContactGroupVO> list = new ArrayList<ContactGroupVO>();
		if(result==null || result.size()==0)
			return list;
		
		for (AddressBookGroupVO group : result) {
			list.add(converVO(group));
		}
		return list;
	}
	
	private ContactGroupVO converVO(AddressBookGroupVO result) {
		ContactGroupVO vo = new ContactGroupVO();
		vo.setAdrbookSeq(result.getAdrbookSeq());
		vo.setGroupName(result.getGroupName());
		vo.setGroupSeq(result.getGroupSeq());
		
		return vo;
	}
	
	public ContactMemberVO readContactMember(ContactCondVO cond) throws Exception{
		
		int userSeq = cond.getUserSeq();		
		if(userSeq == 0){
			String[] userParams = null;
			try {
				userParams = cond.getUserEmail().split("@");
				
				userSeq = mailUserManager.readUserSeq(userParams[0], userParams[1]);
				if(userSeq <=0){
					throw new UserNotFoundException();
				}
			} catch (Exception e) {
				throw new Exception("User Info Not Found");
			}
		}
		AddressBookMemberVO member = null;
		ContactMemberVO memberVO = null;
		try{
			if(cond.getBookSeq() == 0){
				member = manager.readPrivateAddressMember(userSeq,cond.getMemberSeq());
			} else {
				member = manager.readSharedAddressMember(cond.getBookSeq(), cond.getMemberSeq());
			}
		} catch (Exception e) {
			throw e;
		}
		
		if(member != null){
			memberVO = Convert.converVO(member);
		}
		
		
		return memberVO;
	}
	
	/**
	 * 파라미터 ContactCondVO에 따라 그룹정보, 사용자 목록을 반환한다.
	 * This Method return 
	 */
	public ContactInfoVO readContactMemberListByIndex(ContactCondVO cond){
		ContactInfoVO result = new ContactInfoVO();
		result.setCond(cond);
		
		if(cond.getDomainSeq()==0){
			List<ContactMemberVO> list = new ArrayList<ContactMemberVO>();
			result.setTotalCount(0);
			result.setCond(cond);
			result.setMemberList(list.toArray(new ContactMemberVO[list.size()]));
			return result;
		}
		
		int userSeq = 0;
		String[] userParams = null;
		try {
			userParams = cond.getUserEmail().split("@");
			
			userSeq = mailUserManager.readUserSeq(userParams[0], userParams[1]);
			if(userSeq <=0){
				throw new UserNotFoundException();
			}
		} catch (Exception e) {
			List<ContactMemberVO> list = new ArrayList<ContactMemberVO>();
			result.setTotalCount(0);
			result.setCond(cond);
			result.setMemberList(list.toArray(new ContactMemberVO[list.size()]));
			return result;
		}
		
		int domainSeq = cond.getDomainSeq();
		int bookSeq = cond.getBookSeq();
		int groupSeq = cond.getGroupSeq();
		int currentPage = cond.getCurrentPage();
		int maxResult = cond.getMaxResult();
		
		String startChar = cond.getStartChar();
		String sortBy = cond.getSortBy();
		String sortDir = cond.getSortDir();
		
		String searchType = cond.getSearchType();
		String keyWord = cond.getKeyWord();
		
		try {
			List<AddressBookMemberVO> memberList = null;
			int totalCount = 0;
			if(bookSeq==0){
				if (StringUtils.isNotEmpty(keyWord)) {
				    if ("hybrid".equalsIgnoreCase(cond.getAddrType())) {
		                        Map<String, Object> memberListMap = manager.readAddrMemberInitialSearch(bookSeq, groupSeq, userSeq, searchType, keyWord, currentPage, maxResult, sortBy, sortDir);
		                        Object memberListObj = memberListMap.get("memberList");
		                        if (memberListObj != null) {
		                            memberList = (List<AddressBookMemberVO>)memberListObj;
		                        }
		                        Object totalCountObj = memberListMap.get("totalCount");
		                        if (totalCountObj != null) {
		                            totalCount = (Integer)totalCountObj;
		                        }
		                    } else {
		                        memberList = manager.readPrivateSearchMember(userSeq, groupSeq, searchType, keyWord, startChar, currentPage,
		                                maxResult, sortBy, sortDir);
		                        totalCount = manager.readPrivateSearchMemberCount(userSeq, groupSeq, searchType, keyWord, startChar);
		                    }
				} else {
					memberList = manager.readPrivateMemberListByIndex(userSeq, groupSeq, startChar, currentPage, maxResult, sortBy, sortDir);
					totalCount = manager.readPrivateMemberListCount(userSeq, groupSeq, startChar);
				}
				result.setTotalCount(totalCount);
				result.setMemberList(converAddressBookMemberVO(memberList));
				if(groupSeq!=0){
					result.setGroupInfo(convert(manager.readPrivateGroupInfo(userSeq, groupSeq)));	
				}
				
			}else{
				if (StringUtils.isNotEmpty(keyWord)) {
				    
				    if ("hybrid".equalsIgnoreCase(cond.getAddrType())) {
		                        Map<String, Object> memberListMap = manager.readAddrMemberInitialSearch(bookSeq, groupSeq, userSeq, searchType, keyWord, currentPage, maxResult, sortBy, sortDir);
		                        Object memberListObj = memberListMap.get("memberList");
		                        if (memberListObj != null) {
		                            memberList = (List<AddressBookMemberVO>)memberListObj;
		                        }
		                        Object totalCountObj = memberListMap.get("totalCount");
		                        if (totalCountObj != null) {
		                            totalCount = (Integer)totalCountObj;
		                        }
		                    } else {
		                        memberList = manager.readSharedSearchMember(bookSeq, groupSeq, userSeq, searchType, keyWord, startChar,
		                                currentPage, maxResult, sortBy, sortDir);
		                        totalCount = manager.readSharedSearchMemberCount(bookSeq, groupSeq, userSeq, searchType, keyWord, startChar);
		                    }
				} else {
					memberList = manager.readSharedMemberListByIndex(bookSeq, groupSeq, startChar, currentPage, maxResult, sortBy, sortDir);
					totalCount = manager.readSharedMemberListCount(bookSeq, groupSeq, startChar);
				}
				
				result.setTotalCount(totalCount);
				result.setMemberList(converAddressBookMemberVO(memberList));
				
				if(groupSeq!=0){
					result.setGroupInfo(convert(manager.readSharedGroupInfo(bookSeq, groupSeq)));	
				}
				
				result.setBookInfo(convert(manager.readBookInfo(domainSeq, bookSeq, userSeq)));
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			
			List<ContactMemberVO> list = new ArrayList<ContactMemberVO>();
			result.setTotalCount(0);
			result.setCond(cond);
			result.setMemberList(list.toArray(new ContactMemberVO[list.size()]));
			return result;
		}
		
		
		return result;
	}
	
	
	private ContactBookVO convert(AddressBookVO addressBookVO) {
		ContactBookVO vo = new ContactBookVO();
		if(addressBookVO==null)
			return vo;
		
		vo.setAdrbookSeq(addressBookVO.getAddrbookSeq());
		vo.setBookName(addressBookVO.getAddrbookName());
		vo.setDomainSeq(addressBookVO.getMailDomainSeq());
		
		return vo;
	}

	/**
	 * AddressBookGroupVO is converted to ContactGroupVO
	 * @param groupInfo
	 * @return
	 */
	private ContactGroupVO convert(AddressBookGroupVO groupInfo) {
		ContactGroupVO group = new ContactGroupVO();
		if(groupInfo==null)
			return group;
		
		group.setAdrbookSeq(groupInfo.getAdrbookSeq());
		group.setGroupSeq(groupInfo.getGroupSeq());
		group.setGroupName(groupInfo.getGroupName());
		
		return group;
	}

	/**
	 * this method return array with ContactGroupVO from AddressBook
	 * if bookSeq is not zero in ContactCondVO then it is return data from sharedBook
	 * else return data from privateBook
	 */
	public ContactGroupVO[] readContactGroupList(ContactCondVO cond) {
		List<ContactGroupVO> list = new ArrayList<ContactGroupVO>();
		if(cond.getDomainSeq()==0){
			return list.toArray(new ContactGroupVO[list.size()]);
		}
		
		int userSeq = 0;
		int bookSeq = cond.getBookSeq();
		
		String[] userParams = null;
		try {
			userParams = cond.getUserEmail().split("@");
			
			userSeq = mailUserManager.readUserSeq(userParams[0], userParams[1]);
			if(userSeq <=0){
				throw new UserNotFoundException();
			}
		} catch (Exception e) {
			return list.toArray(new ContactGroupVO[list.size()]);
		}
		
		if(bookSeq==0){
			List<AddressBookGroupVO> result = manager.getPrivateGroupList(userSeq);
			list = converAddressBookGroupVO(result);
		}else{
			List<AddressBookGroupVO> result = manager.readSharedGroupList(bookSeq, userSeq);
			list = converAddressBookGroupVO(result);
		}
		
		return list.toArray(new ContactGroupVO[list.size()]);
	}

	public int addContactGroup(ContactGroupVO group) {
		return SUCCESS;
	}

	public int delContactGroup(ContactGroupVO group) {
		return SUCCESS;
	}

	public int modContactGroup(ContactGroupVO group) {
		return SUCCESS;
	}

	public ContactBookVO[] readContactBookList(ContactCondVO cond) {
		List<ContactBookVO> list = new ArrayList<ContactBookVO>();
		if(cond.getDomainSeq()==0){
			return list.toArray(new ContactBookVO[0]);
		}
		
		int userSeq = 0;
		int domainSeq = cond.getDomainSeq();
		
		String[] userParams = null;
		try {
			userParams = cond.getUserEmail().split("@");
			
			userSeq = mailUserManager.readUserSeq(userParams[0], userParams[1]);
			if(userSeq <=0){
				throw new UserNotFoundException();
			}
		} catch (Exception e) {
			return list.toArray(new ContactBookVO[0]);
		}
		
		List<AddressBookVO> result = manager.readAddressBookList(userSeq, domainSeq);
		if(result ==null || result.size()==0){
			return list.toArray(new ContactBookVO[0]);
		}
		
		for (AddressBookVO addressBookVO : result) {
			list.add(convert(addressBookVO));
		}
		
		return list.toArray(new ContactBookVO[list.size()]);
	}

	public int readAddressMemberSeqByClientId(int userSeq,String clientId){
		return manager.readAddressMemberSeqByClientId(userSeq, clientId);
	}
	
	public ContactInfoVO readModContactsByDate(int userSeq, String fromDate, int skipResult, int maxResult){
		List<AddressBookMemberVO> list = manager.getModPrivateAddressListByDate(userSeq, fromDate, skipResult, maxResult);
		ContactInfoVO result = new ContactInfoVO();
		
		if(list==null){
			result.setTotalCount(0);
			list = new ArrayList<AddressBookMemberVO>();
		}
		
		List<ContactMemberVO> list2 = new ArrayList<ContactMemberVO>();
		for (AddressBookMemberVO addressBookMemberVO : list) {
			list2.add(Convert.converVO(addressBookMemberVO));	
		}
		
		result.setTotalCount(list2.size());
		result.setMemberList(list2.toArray(new ContactMemberVO[list2.size()]));
		
		return result;
	}
	
	public PublicAuthVO readPublicBookAuth(ContactCondVO cond) throws Exception{
		
		int userSeq = cond.getUserSeq();
		int domainSeq = cond.getDomainSeq();
		int bookSeq = cond.getBookSeq();
		
		AddressBookAuthVO auth = manager.getAddrAuth(domainSeq,bookSeq, userSeq);
		PublicAuthVO authVO = new PublicAuthVO();
		authVO.setCreatorAuth(auth.getCreatorAuth());
		authVO.setReadAuth(auth.getReadAuth());
		authVO.setWriteAuth(auth.getWriteAuth());
		
		return authVO;
	}
}
