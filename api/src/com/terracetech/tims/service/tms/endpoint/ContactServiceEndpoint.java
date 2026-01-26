package com.terracetech.tims.service.tms.endpoint;

import com.terracetech.tims.service.tms.IContactService;
import com.terracetech.tims.service.tms.vo.ContactBookVO;
import com.terracetech.tims.service.tms.vo.ContactCondVO;
import com.terracetech.tims.service.tms.vo.ContactGroupVO;
import com.terracetech.tims.service.tms.vo.ContactInfoVO;
import com.terracetech.tims.service.tms.vo.ContactMemberVO;
import com.terracetech.tims.service.tms.vo.PublicAuthVO;

public class ContactServiceEndpoint implements IContactService {

	public int addContact(ContactMemberVO member) {
		return 0;
	}

	public int delContact(ContactMemberVO param) {
		return 0;
	}

	public int modContact(ContactMemberVO param) {
		return 0;
	}

	public ContactInfoVO readContactMemberListByIndex(ContactCondVO cond) {
		return null;
	}

	public ContactGroupVO[] readContactGroupList(ContactCondVO cond) {
		return null;
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
	
	public int moveContact(ContactCondVO cond,int[] memberSeqs, int sourceGroupSeq, int targetGroupSeq) throws Exception{
		return SUCCESS;
	}

	public ContactBookVO[] readContactBookList(ContactCondVO cond) {
		return null;
	}
	
	public int readAddressMemberSeqByClientId(int userSeq,String clientId){
		return 0;
	}

	public ContactInfoVO readModContactsByDate(int userSeq, String fromDate, int skipResult, int maxResult) {
		return null;
	}

	public ContactInfoVO readAddContactsByDate(int userSeq, String fromDate, int skipResult, int maxResult) {
		return null;
	}

	public ContactMemberVO readContactMember(ContactCondVO cond)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public PublicAuthVO readPublicBookAuth(ContactCondVO cond) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
