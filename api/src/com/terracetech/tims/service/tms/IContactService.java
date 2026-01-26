package com.terracetech.tims.service.tms;

import com.terracetech.tims.service.tms.vo.ContactBookVO;
import com.terracetech.tims.service.tms.vo.ContactCondVO;
import com.terracetech.tims.service.tms.vo.ContactGroupVO;
import com.terracetech.tims.service.tms.vo.ContactInfoVO;
import com.terracetech.tims.service.tms.vo.ContactMemberVO;
import com.terracetech.tims.service.tms.vo.PublicAuthVO;

public interface IContactService {

	public final static int SUCCESS = 1;
	
	public final static int FAILED = -1;

	public int addContact(ContactMemberVO member);

	public int modContact(ContactMemberVO param);

	public int delContact(ContactMemberVO param);

	public int addContactGroup(ContactGroupVO group);
	
	public int modContactGroup(ContactGroupVO group);
	
	public int moveContact(ContactCondVO cond,int[] memberSeqs, int sourceGroupSeq, int targetGroupSeq) throws Exception;
	
	public int delContactGroup(ContactGroupVO group);
	
	public ContactGroupVO[] readContactGroupList(ContactCondVO cond);
	
	public ContactInfoVO readContactMemberListByIndex(ContactCondVO cond);
	
	public ContactBookVO[] readContactBookList(ContactCondVO cond);
	
	public int readAddressMemberSeqByClientId(int userSeq,String clientId);
	
	public ContactMemberVO readContactMember(ContactCondVO cond) throws Exception;
	
	public PublicAuthVO readPublicBookAuth(ContactCondVO cond) throws Exception;
	
}
