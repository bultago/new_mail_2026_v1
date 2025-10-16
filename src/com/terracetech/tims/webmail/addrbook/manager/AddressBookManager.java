package com.terracetech.tims.webmail.addrbook.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.addrbook.dao.PrivateAddressBookDao;
import com.terracetech.tims.webmail.addrbook.dao.SharedAddressBookDao;
import com.terracetech.tims.webmail.addrbook.ibean.AddressBookBean;
import com.terracetech.tims.webmail.addrbook.ibean.AddressBookGroupBean;
import com.terracetech.tims.webmail.addrbook.ibean.AddressBookMemberBean;
import com.terracetech.tims.webmail.addrbook.manager.vendors.EmailVendorFactory;
import com.terracetech.tims.webmail.addrbook.manager.vendors.IEmailVendor;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookAuthVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookConfigVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookGroupVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookModeratorVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookReaderVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressEnvVO;
import com.terracetech.tims.webmail.addrbook.vo.GroupMemberRelationVO;
import com.terracetech.tims.webmail.common.advice.Transactional;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.InitialSoundSearcher;
import com.terracetech.tims.webmail.exception.InvalidFileException;
import com.terracetech.tims.webmail.exception.SaveFailedException;
import com.terracetech.tims.webmail.exception.UnauthorizedException;
import com.terracetech.tims.webmail.mailuser.dao.MailUserDao;
import com.terracetech.tims.webmail.mailuser.exception.MailUserNotFoundException;
import com.terracetech.tims.webmail.mailuser.vo.MailUserInfoVO;
import com.terracetech.tims.webmail.mobile.manager.MobileSyncManager;
import com.terracetech.tims.webmail.util.DateUtil;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringUtils;


public class AddressBookManager{
	
	private I18nResources resource;

	private PrivateAddressBookDao privateAddrDao;

	private SharedAddressBookDao sharedAddrDao;
	
	private MailUserDao userDao;
	
	private MobileSyncManager mobileSyncManager;
	
	public void setUserDao(MailUserDao userDao) {
		this.userDao = userDao;
	}

	public void setPrivateAddrDao(PrivateAddressBookDao privateAddrDao) {
		this.privateAddrDao = privateAddrDao;
	}

	public void setSharedAddrDao(SharedAddressBookDao sharedAddrDao) {
		this.sharedAddrDao = sharedAddrDao;
	}
	

	public void setMobileSyncManager(MobileSyncManager mobileSyncManager) {
		this.mobileSyncManager = mobileSyncManager;
	}

	public void setResource(I18nResources resource) {
		this.resource = resource;
	}

	public int readPrivateMemberListCount(int userSeq, int groupSeq, String startChar){
		if(groupSeq ==0){
			return privateAddrDao.readAddressListByIndexCount(groupSeq, userSeq, startChar);	
		}else{
			return privateAddrDao.readAddressListByGroupCount(groupSeq, userSeq, startChar);
		}
	}
	
	public List<AddressBookMemberVO> readPrivateMemberListByIndex(int userSeq, int groupSeq,
			String startChar, int currentPage, int maxResult, String sortBy, String sortDir) {
		if(currentPage==0)
			currentPage = 1;
		
		int skipResult = (currentPage - 1) * maxResult;
		
		if(groupSeq ==0){
			return privateAddrDao.readAddressListByIndex(groupSeq, userSeq, startChar, skipResult, maxResult, sortBy, sortDir);	
		}else{
			return privateAddrDao.readAddressListByGroup(groupSeq, userSeq, startChar, skipResult, maxResult, sortBy, sortDir);
		}
		
	}

	@SuppressWarnings("unchecked")
	public JSONArray readJSonPrivateAddressListByIndex(int userSeq, int groupSeq,
			String startChar, int currentPage, int maxResult, String sortBy, String sortDir) {
		
		JSONArray beanArray = new JSONArray();
		
		JSONObject beanObject = null;
		List<AddressBookMemberVO> list = readPrivateMemberListByIndex(userSeq, groupSeq, startChar, currentPage, maxResult, sortBy, sortDir);
		for (AddressBookMemberVO member : list) {
			beanObject =  new AddressBookMemberBean(member).toJson();
			beanArray.add(beanObject);
		}
		
		return beanArray;
	}
	
	public int readSharedMemberListCount(int bookSeq, int groupSeq, String startChar) {
		if(groupSeq ==0){
			return sharedAddrDao.readAddressListByIndexCount(bookSeq, startChar);	
		}else{
			return sharedAddrDao.readAddressListByGroupCount(groupSeq, bookSeq, startChar);
		}
		
	}
	
	public List<AddressBookMemberVO> readSharedMemberListByIndex(int bookSeq, int groupSeq,
			String startChar, int currentPage, int maxResult, String sortBy, String sortDir) {
		int skipResult = (currentPage - 1) * maxResult;
		
		if(groupSeq ==0){
			return sharedAddrDao.readAddressListByIndex(bookSeq, startChar, skipResult, maxResult, sortBy, sortDir);	
		}else{
			return sharedAddrDao.readAddressListByGroup(groupSeq, bookSeq, startChar, skipResult, maxResult, sortBy, sortDir);
		}
		
	}
	
	public List<AddressBookGroupVO> getPrivateGroupList(int userSeq){
		return privateAddrDao.readPrivateGroupList(userSeq);
	}
	
	public List<AddressBookGroupVO> readSharedGroupList(int bookSeq, int userSeq){
		return sharedAddrDao.readGroupList(bookSeq);
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray readJSonPrivateGroupList(int userSeq){
		JSONArray beanArray = new JSONArray();
		
		JSONObject beanObject = null;
		List<AddressBookGroupVO> list = getPrivateGroupList(userSeq);
		for (AddressBookGroupVO group : list) {
			beanObject =  new AddressBookGroupBean(group).toJson();
			beanArray.add(beanObject);
		}
		
		return beanArray;
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray readJSonSharedGroupList(int bookSeq, int userSeq){
		JSONArray beanArray = new JSONArray();
		
		JSONObject beanObject = null;
		List<AddressBookGroupVO> list = readSharedGroupList(bookSeq, userSeq);
		for (AddressBookGroupVO group : list) {
			beanObject =  new AddressBookGroupBean(group).toJson();
			beanArray.add(beanObject);
		}
		
		return beanArray;
	}

	@Transactional
	public void savePrivateGroup(int domainSeq, int userSeq, AddressBookGroupVO group) throws SaveFailedException {
		AddressEnvVO envVo = privateAddrDao.readAddressEnv(domainSeq);
		int count = privateAddrDao.getGroupCount(userSeq);
		
		if(envVo.getPrivateAdrMaxGroup() <= count)
			throw new SaveFailedException(resource.getMessage("addr.error.msg.02"));
		
		if(privateAddrDao.findGroup(group.getGroupName(), userSeq) != null){
			throw new SaveFailedException(resource.getMessage("addr.error.msg.03"));
		}
		
		privateAddrDao.saveGroup(group);
	}

	@Transactional
	public void updatePrivateGroup(AddressBookGroupVO group) throws SaveFailedException {
		if(privateAddrDao.findGroup(group.getGroupName(), group.getUserSeq()) != null){
			throw new SaveFailedException(resource.getMessage("addr.error.msg.03"));
		}
		
		privateAddrDao.updateGroup(group);
	}

	@Transactional
	public void deletePrivateGroup(int userSeq, int groupSeq) {
		privateAddrDao.deleteGroup(userSeq, groupSeq);
	}
	
	@Transactional
	public void saveSharedGroup(int domainSeq, int bookSeq, int userSeq, AddressBookGroupVO group) throws SaveFailedException {
		AddressEnvVO envVo = privateAddrDao.readAddressEnv(domainSeq);
		
		//TODO if(envVo.getSharedAdrCreatorType())
		if(!sharedAddrDao.isAddressBookModerator(group.getAdrbookSeq(), userSeq))
			throw new UnauthorizedException(resource.getMessage("addr.error.msg.07"));
		
		int count = sharedAddrDao.getGroupCount(bookSeq);
		if(envVo.getSharedAdrMaxGroup() <= count)
			throw new SaveFailedException(resource.getMessage("addr.error.msg.02"));
		
		if(sharedAddrDao.findGroup(group.getGroupName(), domainSeq, bookSeq) != null){
			throw new SaveFailedException(resource.getMessage("addr.error.msg.03"));
		}
			
		sharedAddrDao.saveGroup(group);
	}

	@Transactional
	public void updateSharedGroup(int userSeq, AddressBookGroupVO group, int domainSeq) throws SaveFailedException {
		if(!sharedAddrDao.isAddressBookModerator(group.getAdrbookSeq(), userSeq))
			throw new UnauthorizedException(resource.getMessage("addr.error.msg.07"));
		
		if(sharedAddrDao.findGroup(group.getGroupName(),domainSeq, group.getAdrbookSeq()) != null){
			throw new SaveFailedException(resource.getMessage("addr.error.msg.03"));
		}
		
		sharedAddrDao.updateGroup(group);
	}

	@Transactional
	public void deleteSharedGroup(int bookSeq, int userSeq, int groupSeq) {
		if(!sharedAddrDao.isAddressBookModerator(bookSeq, userSeq))
			throw new UnauthorizedException(resource.getMessage("addr.error.msg.07"));
		
		sharedAddrDao.deleteGroup(bookSeq, groupSeq);
	}

	@Transactional
	public void savePrivateAddressMemberWithTransactional(AddressBookMemberVO member, int groupSeq, int domainSeq) throws SaveFailedException {
		AddressEnvVO envVo = privateAddrDao.readAddressEnv(domainSeq);
		
		int count = readPrivateMemberListCount(member.getUserSeq(), 0, "all");
		int max = envVo.getPrivateAdrMaxMember();
		
		if(max <= count)
			throw new SaveFailedException(resource.getMessage("addr.error.msg.01"));
		
		savePrivateAddressMember(member, groupSeq);
	}
	
	public void savePrivateAddressMember(AddressBookMemberVO member, int groupSeq) {
		member.setAddTime(DateUtil.getFullDateStr());
		privateAddrDao.saveMember(member);
		
		member = privateAddrDao.readLastInsertMember(member.getUserSeq());
		
		if(groupSeq != 0){
			GroupMemberRelationVO vo = new GroupMemberRelationVO();
			vo.setGroupSeq(groupSeq);
			vo.setMailUserSeq(member.getUserSeq());
			vo.setMemberSeq(member.getMemberSeq());
			
			privateAddrDao.saveGroupMemberRelation(vo);	
		}
		
//		mobileSyncManager.saveContactsMobileSyncLog(member.getUserSeq(), member.getMemberSeq(), "insert");
	}
	
	public void saveAddrDupProcess(AddressBookMemberVO member, int groupSeq, int userSeq, String dupAddrType){
    	List<AddressBookMemberVO> addrMember;
    	int[] memberSeq = new int[1];
    	if("addrOverWrite".equals(dupAddrType)){
    		addrMember = getExistEmail(userSeq,member.getMemberEmail(),member.getMemberName());
    		if(addrMember.size() > 0 && addrMember.size() < 2){
    			memberSeq[0] =  addrMember.get(0).getMemberSeq();
    			deletePrivateMember(userSeq, memberSeq);
    			savePrivateAddressMember(member, groupSeq);
    		}else{
    			savePrivateAddressMember(member, groupSeq);
    		}
    		
    	}else if("addrAddDup".equals(dupAddrType)){
    		 savePrivateAddressMember(member, groupSeq);
    	}else{
    		addrMember = getExistEmail(userSeq,member.getMemberEmail(),member.getMemberName());
    		if(addrMember.size() < 1){
    			savePrivateAddressMember(member, groupSeq);
    		}
    	}
    	
    }
	
	@Transactional
	public void updatePrivateAddressMember(AddressBookMemberVO member) {
		AddressBookMemberVO user = privateAddrDao.readAddressMember(member.getUserSeq(), member.getMemberSeq());
		if(user != null){
			member.setModTime(DateUtil.getFullDateStr());
			privateAddrDao.updateMember(member);
			
//			mobileSyncManager.saveContactsMobileSyncLog(member.getUserSeq(), member.getMemberSeq(), "update");
		}
	}
	
	public List<AddressBookVO> readAddressBookList(int usrSeq, int domainSeq){
		return sharedAddrDao.readAddressBookList(usrSeq, domainSeq);
	}
	
	@Transactional
	public void saveSharedAddressBook(int userSeq, int domainSeq, AddressBookVO vo) throws SaveFailedException{
		if(!sharedAddrDao.isAddressBookCreator(domainSeq, userSeq))
			throw new UnauthorizedException(resource.getMessage("addr.error.msg.07"));
		
		AddressEnvVO envVo = privateAddrDao.readAddressEnv(domainSeq);
		int count = sharedAddrDao.readSharedAddressBookCount(domainSeq);
		if(count >= envVo.getSharedAdrMaxAdr()){
			throw new SaveFailedException(resource.getMessage("addr.error.msg.08"));
		}
		
		sharedAddrDao.saveSharedAddressBook(vo);
	}
	
	@Transactional
	public void updateSharedAddressBook(int userSeq, int bookSeq, String name){
		AddressBookVO book = new AddressBookVO();
		book.setAddrbookSeq(bookSeq);
		book.setAddrbookName(name);
		
		if(!sharedAddrDao.isAddressBookModerator(bookSeq, userSeq))
			throw new UnauthorizedException(resource.getMessage("addr.error.msg.07"));
		
		sharedAddrDao.updateSharedAddressBook(book);
	}
	
	@Transactional
	public void deleteSharedAddressBook(int userSeq, int bookSeq, int domainSeq){
		if(!sharedAddrDao.isAddressBookCreator(domainSeq, userSeq))
			throw new UnauthorizedException(resource.getMessage("addr.error.msg.07"));
		
		sharedAddrDao.deleteSharedAddressBook(bookSeq);
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray readJSonSharedAddressBookList(int domainSeq, int usrSeq){
		
		JSONArray beanArray = new JSONArray();
		
		JSONObject beanObject = null;
		List<AddressBookVO> list = readAddressBookList(usrSeq, domainSeq);
		for (AddressBookVO book : list) {
			beanObject =  new AddressBookBean(book).toJson();
			beanArray.add(beanObject);
		}
		
		return beanArray;
	}
	
	public AddressBookMemberVO readPrivateAddressMember(int userSeq, int memberSeq){
		return privateAddrDao.readAddressMember(userSeq, memberSeq);
	}
	
	public AddressBookMemberVO readSharedAddressMember(int bookSeq, int memberSeq){
		return sharedAddrDao.readAddressMember(bookSeq, memberSeq);
	}
	
	@Transactional
	public void saveSharedAddressMemberWithTransactional(AddressBookMemberVO member, int groupSeq, int domainSeq) throws SaveFailedException {
		AddressEnvVO envVo = privateAddrDao.readAddressEnv(domainSeq);
		int max = envVo.getSharedAdrMaxMember();
		int count = readSharedMemberListCount(member.getAddrbookSeq(), 0, "all");
		
		if(max <= count)
			throw new SaveFailedException(resource.getMessage("addr.error.msg.01"));
		
		saveSharedAddressMember(member, groupSeq, domainSeq);
	}

	public void saveSharedAddressMember(AddressBookMemberVO member, int groupSeq, int domainSeq) throws SaveFailedException {
		String type = sharedAddrDao.readBookType(member.getAddrbookSeq());
		
		if(!"A".equals(type))
			if(!sharedAddrDao.isAddressBookModerator(member.getAddrbookSeq(), member.getUserSeq()))
				throw new UnauthorizedException(resource.getMessage("addr.error.msg.07"));
		
		sharedAddrDao.saveMember(member);
		
		member = sharedAddrDao.readLastInsertMember(member.getAddrbookSeq());
		
		if(groupSeq != 0){
			GroupMemberRelationVO vo = new GroupMemberRelationVO();
			vo.setGroupSeq(groupSeq);
			vo.setMailUserSeq(member.getUserSeq());
			vo.setMemberSeq(member.getMemberSeq());
			vo.setBookSeq(member.getAddrbookSeq());
			
			sharedAddrDao.saveGroupMemberRelation(vo);	
		}
	}
	
	@Transactional
	public void updateSharedAddressMember(AddressBookMemberVO member) {
		AddressBookMemberVO user = sharedAddrDao.readAddressMember(member.getAddrbookSeq(), member.getMemberSeq());
		if(user != null)
			sharedAddrDao.updateMember(member);
	}
	
	@Transactional
	public void deletePrivateMember(int userSeq, int[] memberSeqs){
		long delTime = Long.parseLong(DateUtil.getFullDateStr());
		
		for (int memberSeq : memberSeqs) {
			delTime += 1;
			if(mobileSyncManager.isUseMobileSync(userSeq)){
				privateAddrDao.deleteMember(userSeq, memberSeq, String.valueOf(delTime));	
			}else{
				privateAddrDao.deleteCompletlyMember(userSeq, memberSeq, String.valueOf(delTime));	
			}
			
		}
	}
	
	@Transactional
	public void deleteSharedMember(int bookSeq, int[] memberSeqs){
		for (int memberSeq : memberSeqs) {
			sharedAddrDao.deleteMember(bookSeq, memberSeq);	
		}
		
	}
	
	@Transactional
	public void deletePrivateMemberRelation(int userSeq, int[] memberSeqs, int groupSeq){
		for (int memberSeq : memberSeqs) {
			privateAddrDao.deleteGroupMemberRelation(userSeq, memberSeq, groupSeq);	
		}
	}
	
	@Transactional
	public void movePrivateMember(int userSeq, int[] memberSeqs, int source, int target){
		for (int memberSeq : memberSeqs) {
			AddressBookMemberVO member = privateAddrDao.readAddressMember(userSeq, memberSeq);
			if(member != null){
				List<AddressBookMemberVO> members = privateAddrDao.readAddressListByGroup(target, userSeq, "all", 0, 10, "", "");
				
				if(! isExistMember(members, member)){
					privateAddrDao.deleteGroupMemberRelation(userSeq, memberSeq, source);
					
					if(target!=0){
						privateAddrDao.deleteGroupMemberRelation(userSeq, memberSeq, target);
						
						GroupMemberRelationVO vo = new GroupMemberRelationVO();
						vo.setGroupSeq(target);
						vo.setMailUserSeq(userSeq);
						vo.setMemberSeq(memberSeq);
						
						privateAddrDao.saveGroupMemberRelation(vo);	
					}
				}else{
					privateAddrDao.deleteGroupMemberRelation(userSeq, memberSeq, source);
				}
				
			}
		}
		
	}
	
	@Transactional
	public void moveSharedMember(int bookSeq, int[] memberSeqs, int source, int target){
		for (int memberSeq : memberSeqs) {
			AddressBookMemberVO member = sharedAddrDao.readAddressMember(bookSeq, memberSeq);
			if(member != null){
				List<AddressBookMemberVO> members = sharedAddrDao.readAddressListByGroup(target, bookSeq, "all", 0, 10, "", "");
				if(! isExistMember(members, member)){
					sharedAddrDao.deleteGroupMemberRelation(bookSeq, memberSeq, source);
					if(target!=0){
						GroupMemberRelationVO vo = new GroupMemberRelationVO();
						vo.setGroupSeq(target);
						vo.setBookSeq(bookSeq);
						vo.setMemberSeq(memberSeq);
						
						sharedAddrDao.saveGroupMemberRelation(vo);
					}
				}else{
					sharedAddrDao.deleteGroupMemberRelation(bookSeq, memberSeq, source);
				}
			}	
		}
	}
	
	@Transactional
	public void copyPrivateMember(int userSeq, int[] memberSeqs, int groupSeq){
		for (int memberSeq : memberSeqs) {
			AddressBookMemberVO member = privateAddrDao.readAddressMember(userSeq, memberSeq);
			
			if(member != null){
				List<AddressBookMemberVO> members = privateAddrDao.readAddressListByGroup(groupSeq, userSeq, "all", 0, 10, "", "");
				
				if(! isExistMember(members, member)){
					GroupMemberRelationVO vo = new GroupMemberRelationVO();
					vo.setGroupSeq(groupSeq);
					vo.setMailUserSeq(userSeq);
					vo.setMemberSeq(memberSeq);
					try {
						privateAddrDao.saveGroupMemberRelation(vo);	
					} catch (Exception ignore) {
						LogManager.writeErr(this, ignore.getMessage(), ignore);	
					}
						
				}
			}
		}
		
	}
	
	private boolean isExistMember(List<AddressBookMemberVO> members, AddressBookMemberVO member){
		for (AddressBookMemberVO addressBookMemberVO : members) {
			if(member.getMemberSeq()==addressBookMemberVO.getMemberSeq())
				return true;
		}
		
		return false;
	}
	
	@Transactional
	public void copySharedMember(int bookSeq, int[] memberSeqs, int groupSeq){
		for (int memberSeq : memberSeqs) {
			AddressBookMemberVO member = sharedAddrDao.readAddressMember(bookSeq, memberSeq);
			if(member != null){
				if(!sharedAddrDao.hasGroupMember(bookSeq, groupSeq, memberSeq)){
					GroupMemberRelationVO vo = new GroupMemberRelationVO();
					vo.setGroupSeq(groupSeq);
					vo.setBookSeq(bookSeq);
					vo.setMemberSeq(memberSeq);
					try {
						sharedAddrDao.saveGroupMemberRelation(vo);	
					} catch (Exception e) {
						LogManager.writeErr(this, e.getMessage(), e);
					}
						
				}
			}
		}
		
	}
	
	@Transactional
	public void deleteSharedMemberRelation(int bookSeq, int[] memberSeqs, int groupSeq){
		for (int memberSeq : memberSeqs) {
			sharedAddrDao.deleteGroupMemberRelation(bookSeq, memberSeq, groupSeq);	
		}
	}
	
	public List<AddressBookReaderVO> readAddressBookReaderList(int bookSeq, int currentPage, int maxResult, String searchType, String keyWord){
		if(currentPage <=0)
			currentPage = 1;
		
		int skipResult = (currentPage - 1) * maxResult;
		
		return sharedAddrDao.readAddressBookReaderList(bookSeq, skipResult, maxResult, searchType, keyWord);
	}
	
	public int readAddressBookModeratorListCount(int bookSeq, String searchType, String keyWord) {
		return sharedAddrDao.readAddressBookModeratorListCount(bookSeq, searchType, keyWord);
	}
	
	public List<AddressBookModeratorVO> readAddressBookModeratorList(int bookSeq, int currentPage, int maxResult, String searchType, String keyWord){
		if(currentPage <=0)
			currentPage = 1;
		
		int skipResult = (currentPage - 1) * maxResult;
		
		return sharedAddrDao.readAddressBookModerator(bookSeq, skipResult, maxResult, searchType, keyWord);
	}
	
	@Transactional
	public void saveAddressBookReader(int domainSeq, int userSeq, AddressBookReaderVO reader) throws MailUserNotFoundException {
		//TODO �Է��� ����� �����ּ� Ȥ�� �ٸ� ������ mail_user�� mail_user_seq�� ���ؾ��Ѵ�.
		if(!sharedAddrDao.isAddressBookModerator(reader.getAddrbookSeq(), userSeq))
			throw new UnauthorizedException(resource.getMessage("addr.error.msg.07"));
		
		String email = reader.getUserEmail();
		MailUserInfoVO user = null;
		String mailUid = null;
		if(email.indexOf("@") > 1){
			StringTokenizer st = new StringTokenizer(email, "@");
			mailUid = st.nextToken();
			user = userDao.readMailUserInfo(mailUid, st.nextToken());	
		}else{
			user = userDao.readMailUserInfo(domainSeq, email);
			mailUid = email;
		}
		
		if(user == null)
			throw new MailUserNotFoundException(email);
			
		if(user != null)
		{
			int count = sharedAddrDao.getCountSharedAddressBookReader(reader.getAddrbookSeq(), mailUid);
			if(count ==0 ){
				reader.setUserName(user.getUserName());
				reader.setUserSeq(user.getMailUserSeq());
				sharedAddrDao.saveAddressBookReader(reader);	
			}
		}
	}
	
	@Transactional
	public void saveAddressBookModerator(int domainSeq, int userSeq, AddressBookModeratorVO moderator) throws MailUserNotFoundException {
		if(!sharedAddrDao.isAddressBookCreator(moderator.getAddrbookSeq(), userSeq))
			if(!sharedAddrDao.isAddressBookModerator(moderator.getAddrbookSeq(), userSeq))
				throw new UnauthorizedException(resource.getMessage("addr.error.msg.07"));
		
		//TODO �Է��� ����� �����ּ� Ȥ�� �ٸ� ������ mail_user�� mail_user_seq�� ���ؾ��Ѵ�.
		String email = moderator.getUserEmail();
		MailUserInfoVO user = null;
		if(email.indexOf("@") > 1){
			StringTokenizer st = new StringTokenizer(email, "@");
			user = userDao.readMailUserInfo(st.nextToken(), st.nextToken());	
		}else{
			user = userDao.readMailUserInfo(domainSeq, email);	
		}
		
		if(user == null)
			throw new MailUserNotFoundException(email);
			
		if(user != null)
		{
			int count = sharedAddrDao.getCountSharedAddressBookModerator(moderator.getAddrbookSeq(), moderator.getUserEmail());
			if(count ==0 ){
				moderator.setUserName(user.getUserName());
				moderator.setUserSeq(user.getMailUserSeq());
				sharedAddrDao.saveAddressBookModerator(moderator);	
			}
		}
	}
	
	@Transactional
	public void deleteAddressBookReader(int bookSeq, int userSeq, int[] memberSeqs){
		if(!sharedAddrDao.isAddressBookModerator(bookSeq, userSeq))
			throw new UnauthorizedException(resource.getMessage("addr.error.msg.07"));
		
		for (int memberSeq : memberSeqs) {
			sharedAddrDao.deleteAddressBookReader(bookSeq, memberSeq);	
		}
	}
	
	@Transactional
	public void deleteAddressBookModerator(int bookSeq, int userSeq, int[] memberSeqs){
		if(!sharedAddrDao.isAddressBookModerator(bookSeq, userSeq))
			throw new UnauthorizedException(resource.getMessage("addr.error.msg.07"));
		
		for (int memberSeq : memberSeqs) {
			sharedAddrDao.deleteAddressBookModerator(bookSeq, memberSeq);	
		}
	}
	
	/**
	 * 
	 * @param encoding
	 * @param results
	 * @param userSeq
	 * @param bookSeq
	 * @param groupSeq
	 * @param domainSeq
	 * @param vendorSeq
	 * 		1 : TMS
	 * 		2 : Outlook Express
	 * 		3 : Outlook
	 * 		4 : Thunderbird
	 * @throws IOException
	 * @throws InvalidFileException
	 * @throws SaveFailedException
	 */
	@Transactional
	public void importAddressMember(String encoding, String[] results, int userSeq, int bookSeq, int groupSeq, int domainSeq, int vendorSeq , String dupAddrType) throws IOException, InvalidFileException, SaveFailedException {
		AddressEnvVO envVo = privateAddrDao.readAddressEnv(domainSeq);
		AddressBookMemberVO[] list = null;
		
		IEmailVendor vendor = EmailVendorFactory.getEmailVendor(resource, encoding, vendorSeq);
		list = vendor.getAddressBookMemberVO(results, userSeq);
		
		importAddressMember(envVo, list, userSeq, bookSeq, groupSeq, domainSeq, dupAddrType);
	}

	private void importAddressMember(AddressEnvVO envVo, AddressBookMemberVO[] list, int userSeq, int bookSeq, int groupSeq, int domainSeq, String dupAddrType) throws IOException, InvalidFileException, SaveFailedException {
		
		AddressBookMemberVO member;
			
		if(bookSeq==0){
			int count = readPrivateMemberListCount(userSeq, 0, "all");
			int max = envVo.getPrivateAdrMaxMember();
			if(max <= count+ list.length)
				throw new SaveFailedException(resource.getMessage("addr.error.msg.01"));
			
			for (int i = 0; i < list.length; i++) {
				member = list[i];
				if(max > count + i){
					saveAddrDupProcess(member, groupSeq, userSeq, dupAddrType);	
				}
			}
		}else{
			int count = readSharedMemberListCount(bookSeq, 0, "all");
			int max = envVo.getSharedAdrMaxMember();
			
			if(max <= count+ list.length)
				throw new SaveFailedException(resource.getMessage("addr.error.msg.01"));
			
			for (int i = 0; i < list.length; i++) {
				member = list[i];
				if(max > count + i){
					member.setAddrbookSeq(bookSeq);
					saveSharedAddressMember(member, groupSeq, domainSeq);	
				}
			}
		}	
	}
	
	public String[] getAlphabet(String txt){
		List<String> list = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(txt, ",");
		while (st.hasMoreTokens()) {
			String alphabet = (String) st.nextToken();
			list.add(alphabet.trim());
		}
		
		return list.toArray(new String[list.size()]);
	}

	public List<AddressBookMemberVO> readPrivateSearchMember(int userSeq, int groupSeq,
			String searchType, String keyWord, String startChar,
			int currentPage, int maxResult, String sortBy, String sortDir) {
		int skipResult = (currentPage - 1) * maxResult;
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userSeq", userSeq);
		param.put("groupSeq", groupSeq);
		param.put("name", startChar);
		param.put(searchType, "%" +keyWord + "%");
		param.put("sortBy", sortBy);
		param.put("sortDir", sortDir);

		return privateAddrDao.searchMember(param, skipResult, maxResult);
	}
	
	public int readPrivateSearchMemberCount(int userSeq, int groupSeq,
			String searchType, String keyWord, String startChar) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userSeq", userSeq);
                param.put("groupSeq", groupSeq);
		param.put("name", startChar);
		param.put(searchType, "%" +keyWord + "%");
		
		return privateAddrDao.searchMemberCount(param);
	}
	
	public List<AddressBookMemberVO> readSharedSearchMember(int bookSeq, int groupSeq, int userSeq,
			String searchType, String keyWord, String startChar,
			int currentPage, int maxResult, String sortBy, String sortDir) {
		int skipResult = (currentPage - 1) * maxResult;
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("bookSeq", String.valueOf(bookSeq));
		param.put("groupSeq", String.valueOf(groupSeq));
		param.put("userSeq", String.valueOf(userSeq));
		param.put("name", startChar);
		param.put(searchType, "%" +keyWord + "%");
		param.put("sortBy", sortBy);
		param.put("sortDir", sortDir);

		return sharedAddrDao.searchMember(param, skipResult, maxResult);
	}
	
	public int readSharedSearchMemberCount(int bookSeq, int groupSeq, int userSeq,
			String searchType, String keyWord, String startChar) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("bookSeq", String.valueOf(bookSeq));
		param.put("groupSeq", String.valueOf(groupSeq));
		param.put("userSeq", String.valueOf(userSeq));
		param.put("name", startChar);
		param.put(searchType, "%" +keyWord + "%");
		
		return sharedAddrDao.searchMemberCount(param);
	}
	
	public boolean isAddressBookModerator(int bookSeq, int userSeq){
		return sharedAddrDao.isAddressBookModerator(bookSeq, userSeq);
	}
	
	public AddressBookAuthVO readSharedAddressBookAuth(int domainSeq, int bookSeq, int userSeq){
		AddressBookAuthVO result = new AddressBookAuthVO();
		
		List<AddressBookConfigVO> list = sharedAddrDao.readAddressBookAuth(domainSeq, bookSeq, userSeq);
		if(list != null){
			for (AddressBookConfigVO vo : list) {
				if("creator_auth".equals(vo.getAttrName()))
					result.setCreatorAuth(vo.getAttrValue());
				if("addr_env".equals(vo.getAttrName())){
					if("A".equals(vo.getAttrValue()))
						result.setCreatorAuth("Y");	
				}
					
				if("write_auth".equals(vo.getAttrName())){
					result.setWriteAuth(vo.getAttrValue());
				}
					
				if("book_auth".equals(vo.getAttrName())){
					if("N".equals(result.getWriteAuth())){
						result.setWriteAuth(vo.getAttrValue());
					}
				}
				
				if("read_auth".equals(vo.getAttrName()))
					result.setReadAuth(vo.getAttrValue());
				if("reader_type".equals(vo.getAttrName()))
					if("A".equals(vo.getAttrValue()))
						result.setReadAuth("Y");
					
					
			}
		}
		
		return result;
	}

	public AddressBookAuthVO readPrivateAddressBookAuth(int domainSeq,int bookSeq, int userSeq) {
		AddressBookAuthVO result = new AddressBookAuthVO();
		result.setCreatorAuth("N");
		result.setWriteAuth("Y");
		result.setReadAuth("Y");
		
		return result;
	}

	public AddressBookAuthVO getAddrAuth(int domainSeq, int bookSeq, int userSeq) {
		
		AddressBookAuthVO auth = readSharedAddressBookAuth(domainSeq, bookSeq, userSeq);
		return auth;
	}

	public int readAddressBookReaderListCount(int bookSeq, String searchType, String keyWord) {
		return sharedAddrDao.readAddressBookReaderListCount(bookSeq, searchType, keyWord);
	}

	public AddressBookGroupVO readPrivateGroupInfo(int userSeq, int groupSeq) {
		return privateAddrDao.readGroupInfo(userSeq, groupSeq);
	}
	
	public AddressBookGroupVO readSharedGroupInfo(int bookSeq, int groupSeq) {
		return sharedAddrDao.readGroupInfo(bookSeq, groupSeq);
	}
	
	public AddressBookVO readBookInfo(int domainSeq, int bookSeq, int userSeq){
		return sharedAddrDao.readBookInfo(domainSeq, bookSeq, userSeq);
	}
	
	public int readAddressMemberSeqByClientId(int userSeq,String clientId){
		return privateAddrDao.readAddressMemberSeqByClientId(userSeq, clientId);
	}
	
	public List<AddressBookMemberVO> getAddPrivateAddressListByDate(int userSeq, String fromDate, int skipResult, int maxResult){
		return privateAddrDao.getAddPrivateAddressListByDate(userSeq, fromDate, skipResult, maxResult);
	}
	
	public List<AddressBookMemberVO> getModPrivateAddressListByDate(int userSeq, String fromDate, int skipResult, int maxResult){
		return privateAddrDao.getModPrivateAddressListByDate(userSeq, fromDate, skipResult, maxResult);
	}
	
	public AddressBookMemberVO readOrgMember(String codeLocale,String orgCode, int domainSeq, int memberSeq){
		return privateAddrDao.readOrgMember(codeLocale, orgCode, domainSeq, memberSeq);
	}

	public List<AddressBookMemberVO> readAddContactsByDate(int userSeq, String fromDate, int windowSize) {
		if(StringUtils.isEmpty(fromDate))
			return readPrivateMemberListByIndex(userSeq, 0, "", 0, windowSize, "seq", "asc");;
		
		return privateAddrDao.getAddPrivateAddressListByDate(userSeq, fromDate, 0, windowSize);
	}

	public List<AddressBookMemberVO> readModContactsByDate(int userSeq, String fromDate, int windowSize) {
		if(StringUtils.isEmpty(fromDate))
			return new ArrayList<AddressBookMemberVO>();
		
		return privateAddrDao.getModPrivateAddressListByDate(userSeq, fromDate, 0, windowSize);
	}

	public List<AddressBookMemberVO> readDelContactsByDate(int userSeq, String fromDate, int windowSize) {
		if(StringUtils.isEmpty(fromDate))
			return new ArrayList<AddressBookMemberVO>();
		
		return privateAddrDao.getDelPrivateAddressListByDate(userSeq, fromDate, 0, windowSize);
	}

	public Map<String,Object> readAddrMemberInitialSearch(int bookSeq, int groupSeq, int userSeq, String searchType, String keyWord,
	            int currentPage, int maxResult, String sortBy, String sortDir) {
	        Map<String, Object> memberListMap = new HashMap<String, Object>();
	        int skipResult = (currentPage - 1) * maxResult;

	        Map<String, Object> param = new HashMap<String, Object>();
	        param.put("userSeq", String.valueOf(userSeq));
	        param.put("groupSeq", String.valueOf(groupSeq));
	        param.put("sortBy", sortBy);
	        param.put("sortDir", sortDir);

	        List<AddressBookMemberVO> memberList = null;
	                
	        if (bookSeq == 0) {
	            memberList = privateAddrDao.getPrivateAddressAllList(groupSeq, userSeq, sortBy, sortDir);
	        } else {
	            memberList = sharedAddrDao.getShareAddressAllList(bookSeq, groupSeq, userSeq, sortBy, sortDir);
	        }
	        InitialSoundSearcher iss = null;
	        int memberSize = 0;
	        if (memberList != null && memberList.size() > 0) {
	            iss = new InitialSoundSearcher();
	            for (int i=memberList.size()-1; i >= 0; i--) {
	                AddressBookMemberVO addressBookMemberVO = memberList.get(i);
	                if (!iss.initialSoundSearchMatch(addressBookMemberVO.getMemberName(), keyWord)
	                        && !iss.initialSoundSearchMatch(addressBookMemberVO.getMemberEmail(), keyWord)
	                        && !iss.initialSoundSearchMatch(addressBookMemberVO.getMobileNo(), keyWord)
	                        && !iss.initialSoundSearchMatch(addressBookMemberVO.getHomeTel(), keyWord)
	                        && !iss.initialSoundSearchMatch(addressBookMemberVO.getOfficeTel(), keyWord)) {
	                    memberList.remove(i);
	                }
	            }
	            memberSize = memberList.size();
	            if (memberSize > skipResult) {
	                int resultSize = skipResult+maxResult;
	                resultSize = (memberSize > resultSize) ? resultSize : memberSize;
	                memberList = memberList.subList(skipResult, resultSize);
	            }
	        }
	        
	        memberListMap.put("memberList", memberList);
	        memberListMap.put("totalCount", memberSize);
	        
	        return memberListMap;
	    }
	
	public List<AddressBookMemberVO> getExistEmail(int userSeq, String email, String name){
    	return privateAddrDao.getExistEmail(userSeq,email,name);    	
    }
}
