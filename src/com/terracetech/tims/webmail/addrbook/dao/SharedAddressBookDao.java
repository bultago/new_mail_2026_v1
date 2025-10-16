package com.terracetech.tims.webmail.addrbook.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.addrbook.manager.AddressBookUtils;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookConfigVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookGroupVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookModeratorVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookReaderVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookVO;
import com.terracetech.tims.webmail.addrbook.vo.GroupMemberRelationVO;
import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;

@SuppressWarnings("unchecked")
public class SharedAddressBookDao extends SqlMapClientDaoSupport{

	/**
	 * 조회자 혹은 관리자 권한을 조회해서 주소록 목록을 가져온다.
	 * 
	 * @param usrSeq
	 * @return
	 * @throws SQLException
	 */
	
	public List<AddressBookVO> readAddressBookList(int usrSeq, int domainSeq){
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("userSeq", usrSeq);
		map.put("domainSeq", domainSeq);
		
		return getSqlMapClientTemplate().queryForList(
				"Addrbook.getSharedAddressBookAllListWithAuth", map);
	}
	
	public void saveSharedAddressBook(AddressBookVO vo) {
		getSqlMapClientTemplate().insert("Addrbook.insertSharedAddressBook", vo);
	}
	
	public void updateSharedAddressBook(AddressBookVO vo) {
		getSqlMapClientTemplate().insert("Addrbook.updateSharedAddressBook", vo);
	}
	
	public boolean isAddressBookModerator(int bookSeq, int userSeq){
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("userSeq", userSeq);
		map.put("bookSeq", bookSeq);
		
		int result = (Integer)getSqlMapClientTemplate().queryForObject("Addrbook.getSharedAddressBookModeratorCheck", map);
		
		return result ==0 ? false : true;
	}
	
	/**
	 * 
	 * @param bookSeq
	 * @param type
	 * A : 모든 사용자
	 * C : 지정한 사용자
	 * D : 생성된 도메인
	 */
	public void updateReaderType(int bookSeq, String type) {
		Map<String, String> param = new HashMap<String, String>(2);
		param.put("readerType", type);
		param.put("addrbookSeq", String.valueOf(bookSeq));
		
		getSqlMapClientTemplate().insert("Addrbook.updateReaderType", param);
	}
	
	public void deleteSharedAddressBook(int bookSeq){
		getSqlMapClientTemplate().delete("Addrbook.deleteSharedAddressBook", bookSeq);
	}
	
	public List<AddressBookGroupVO> readGroupList(int bookSeq){
		return getSqlMapClientTemplate().queryForList("Addrbook.getSharedGroupList", bookSeq);
	}
	
	public void saveGroup(AddressBookGroupVO group) {
		getSqlMapClientTemplate().insert("Addrbook.insertSharedGroup", group);
	}

	public void updateGroup(AddressBookGroupVO group) {
		getSqlMapClientTemplate().update("Addrbook.updateSharedGroup", group);
	}

	public void deleteGroup(int bookSeq, int groupSeq) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("adrbookSeq", String.valueOf(bookSeq));
		param.put("groupSeq", String.valueOf(groupSeq));
		
		getSqlMapClientTemplate().delete("Addrbook.deleteSharedGroup", param);
	}

	public List<AddressBookMemberVO> readAddressListByIndex(int bookSeq, String startChar, int skipResult, int maxResult, String sortBy, String sortDir) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("book", String.valueOf(bookSeq));
		if (startChar != null){
			param.put("name", startChar);
			AddressBookUtils.setJapaneses(startChar, param);
		}
		
		param.put("sortBy", sortBy);
		param.put("sortDir", sortDir);
		
		param.put("skipResult", skipResult);
		param.put("maxResult", maxResult);
		
		AddressBookUtils.setKorean(param);

		return getSqlMapClientTemplate().queryForList("Addrbook.getSharedAddressList", param);
	}
	
	public int readAddressListByIndexCount(int bookSeq, String startChar) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("book", String.valueOf(bookSeq));
		if (startChar != null){
			param.put("name", startChar);
			AddressBookUtils.setJapaneses(startChar, param);
		}
		
		AddressBookUtils.setKorean(param);

		return (Integer)getSqlMapClientTemplate().queryForObject("Addrbook.getSharedAddressListCount", param);
	}

	public List<AddressBookMemberVO> readAddressListByGroup(int groupSeq,
			int bookSeq, String startChar, int skipResult, int maxResult, String sortBy, String sortDir) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("book", String.valueOf(bookSeq));
		if (startChar != null){
			param.put("name", startChar);
			AddressBookUtils.setJapaneses(startChar, param);
		}
		
		param.put("groupSeq", String.valueOf(groupSeq));
		param.put("sortBy", sortBy);
		param.put("sortDir", sortDir);
		
		param.put("skipResult", skipResult);
		param.put("maxResult", maxResult);
		
		AddressBookUtils.setKorean(param);
			
		return getSqlMapClientTemplate().queryForList("Addrbook.getSharedAddressListByGroup", param);
	}
	
	public int readAddressListByGroupCount(int groupSeq, int bookSeq, String startChar) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("book", String.valueOf(bookSeq));
		if (startChar != null){
			param.put("name", startChar);
			AddressBookUtils.setJapaneses(startChar, param);
		}
		
		param.put("groupSeq", String.valueOf(groupSeq));
		
		AddressBookUtils.setKorean(param);
		
		return (Integer)getSqlMapClientTemplate().queryForObject("Addrbook.getSharedAddressListByGroupCount", param);
	}

	public void saveMember(AddressBookMemberVO member) {
		getSqlMapClientTemplate().insert("Addrbook.insertSharedMember", member);
	}

	public AddressBookMemberVO readLastInsertMember(int bookSeq) {
		return (AddressBookMemberVO) getSqlMapClientTemplate().queryForObject("Addrbook.getLastInsertSharedMember", bookSeq);
	}

	public void saveGroupMemberRelation(GroupMemberRelationVO vo) {
		getSqlMapClientTemplate().insert("Addrbook.insertSharedGroupMemberRelation", vo);
	}
	
	public void deleteGroupMemberRelation(int bookSeq, int memberSeq, int groupSeq) {
		Map<String, Integer> param = new HashMap<String, Integer>();
		param.put("bookSeq", bookSeq);
		param.put("groupSeq", groupSeq);
		param.put("memberSeq", memberSeq);
		
		getSqlMapClientTemplate().delete("Addrbook.deleteSharedGroupMemberRelation", param);		
	}
	
	public List<AddressBookMemberVO> readAddressMembers(int bookseq){
		return getSqlMapClientTemplate().queryForList("Addrbook.getSharedMembers", bookseq);
	}

	public void updateMember(AddressBookMemberVO member) {
		getSqlMapClientTemplate().update("Addrbook.updateSharedMember", member);
	}

	public void deleteMember(int bookSeq, int memberSeq) {
		Map<String, Integer> param = new HashMap<String, Integer>();
		param.put("bookSeq", bookSeq);
		param.put("memberSeq", memberSeq);
		
		getSqlMapClientTemplate().delete("Addrbook.deleteSharedGroupMemberRelationAll", param);
		getSqlMapClientTemplate().delete("Addrbook.deleteSharedMember", param);
		
	}

	public AddressBookMemberVO readAddressMember(int bookSeq, int memberSeq) {
		Map<String, Integer> param = new HashMap<String, Integer>();
		param.put("bookSeq", bookSeq);
		param.put("memberSeq", memberSeq);
		
		return (AddressBookMemberVO)getSqlMapClientTemplate().queryForObject("Addrbook.getSharedMember", param);
	}
	
	public int readAddressBookReaderListCount(int bookSeq, String searchType, String keyWord) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("bookSeq", String.valueOf(bookSeq));
		param.put("searchType", searchType);
		param.put("keyWord", "%" +keyWord + "%");
		
		return (Integer)getSqlMapClientTemplate().queryForObject("Addrbook.getSharedAddressBookReaderListCount", param);
	}
	
	public List<AddressBookReaderVO> readAddressBookReaderList(int bookSeq, int skipResult, int maxResult,  String searchType, String keyWord) {
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("bookSeq", String.valueOf(bookSeq));
		param.put("searchType", searchType);
		param.put("keyWord", "%" +keyWord + "%");
		
		param.put("skipResult", skipResult);
		param.put("maxResult", maxResult);
		
		return getSqlMapClientTemplate().queryForList("Addrbook.getSharedAddressBookReaderList", param);
	}

	public void saveAddressBookReader(AddressBookReaderVO reader) {
		getSqlMapClientTemplate().insert("Addrbook.insertAddressBookReader", reader);
	}
	
	public void deleteAddressBookReader(int bookSeq, int memberSeq) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("bookSeq", bookSeq);
		map.put("memberSeq", memberSeq);
		
		getSqlMapClientTemplate().delete("Addrbook.deleteAddressBookReader", map);
	}

	public void saveAddressBookModerator(AddressBookModeratorVO moderator) {
		getSqlMapClientTemplate().insert("Addrbook.insertAddressBookModerator", moderator);
	}
	
	public int readAddressBookModeratorListCount(int bookSeq, String searchType, String keyWord) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("bookSeq", String.valueOf(bookSeq));
		param.put("searchType", searchType);
		param.put("keyWord", "%" +keyWord + "%");
		
		return (Integer)getSqlMapClientTemplate().queryForObject("Addrbook.getSharedAddressBookModeratorListCount", param);
	}

	public List<AddressBookModeratorVO> readAddressBookModerator(int bookSeq, int skipResult, int maxResult,  String searchType, String keyWord) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("bookSeq", String.valueOf(bookSeq));
		param.put("searchType", searchType);
		param.put("keyWord", "%" +keyWord + "%");
		
		param.put("skipResult", skipResult);
		param.put("maxResult", maxResult);
		
		return getSqlMapClientTemplate().queryForList("Addrbook.getSharedAddressBookModeratorList", param);
	}

	public int getCountSharedAddressBookReader(int book, String email) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("bookSeq", String.valueOf(book));
		param.put("email", email);
		
		return (Integer)getSqlMapClientTemplate().queryForObject("Addrbook.getSharedAddressBookReaderCount", param);
	}

	public int getCountSharedAddressBookModerator(int book, String email) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("bookSeq", String.valueOf(book));
		param.put("email", email);
		
		return (Integer)getSqlMapClientTemplate().queryForObject("Addrbook.getSharedAddressBookModeratorCount", param);
	}

	public void deleteAddressBookModerator(int bookSeq, int memberSeq) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("bookSeq", bookSeq);
		map.put("memberSeq", memberSeq);
		
		getSqlMapClientTemplate().delete("Addrbook.deleteAddressBookModerator", map);
	}

	public int getGroupCount(int bookSeq) {
		return (Integer)getSqlMapClientTemplate().queryForObject("Addrbook.getSharedGroupCount", bookSeq);
	}

	public boolean isAddressBookCreator(int domainSeq, int userSeq) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("domainSeq", domainSeq);
		map.put("userSeq", userSeq);
		
		int result = (Integer)getSqlMapClientTemplate().queryForObject("Addrbook.getCreatorListCount", map);
		
		return result ==0 ? false : true;
	}

	public String readBookType(int addrbookSeq) {
		return (String) getSqlMapClientTemplate().queryForObject("Addrbook.readBookReaderType", addrbookSeq);
	}

	public List<MailAddressBean> readAddressListByGroup(String searchStr, String bookSeq) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("groupName", searchStr);
		param.put("bookSeq", bookSeq);
		
		return getSqlMapClientTemplate().queryForList("Addrbook.getSharedAddressEmailList", param);
	}
	
	public List<AddressBookConfigVO> readAddressBookAuth(int domainSeq, int bookSeq, int userSeq){
		HashMap<String, Integer> param = new HashMap<String, Integer>();
		param.put("domainSeq", domainSeq);
		param.put("bookSeq", bookSeq);
		param.put("userSeq", userSeq);
		
		return getSqlMapClientTemplate().queryForList("Addrbook.getAddressBookAuth", param);
	}

	public AddressBookGroupVO findGroup(String groupName, int domainSeq, int bookSeq) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("domainSeq", String.valueOf(domainSeq));
		param.put("bookSeq", String.valueOf(bookSeq));
		param.put("groupName", groupName);
		
		return (AddressBookGroupVO)getSqlMapClientTemplate().queryForObject("Addrbook.findSharedAddressGroup", param);
	}

	public boolean hasGroupMember(int bookSeq, int groupSeq, int memberSeq) {
		HashMap<String, Integer> param = new HashMap<String, Integer>();
		param.put("bookSeq", bookSeq);
		param.put("groupSeq", groupSeq);
		param.put("memberSeq", memberSeq);
		
		Object result = getSqlMapClientTemplate().queryForObject("Addrbook.getSharedAddressMemberCount", param);
		if(result == null)
			return false;
		
		return ((Integer)result) ==0 ? false : true;
	}

	public int readSharedAddressBookCount(int domainSeq) {
		Object result = getSqlMapClientTemplate().queryForObject("Addrbook.getSharedAddressBookCount", domainSeq);
		if(result == null)
			return 0;
		
		return (Integer)result;
	}
	
	public int searchMemberCount(Map<String, Object> param) {
		
		AddressBookUtils.setKorean(param);
		
		return (Integer)getSqlMapClientTemplate().queryForObject("Addrbook.getSharedSearchMemberCount", param);
	}

	public List<AddressBookMemberVO> searchMember(Map<String, Object> param, int skipResult, int maxResult) {
		param.put("skipResult", skipResult);
		param.put("maxResult", maxResult);
		
		AddressBookUtils.setKorean(param);
		
		return getSqlMapClientTemplate().queryForList("Addrbook.getSharedSearchMember", param);
	}
	
	public AddressBookGroupVO readGroupInfo(int bookSeq, int groupSeq) {
		HashMap<String, Integer> param = new HashMap<String, Integer>();
		param.put("bookSeq", bookSeq);
		param.put("groupSeq", groupSeq);
		
		Object result = getSqlMapClientTemplate().queryForObject("Addrbook.getSharedGroup", param);
		if(result != null){
			return (AddressBookGroupVO)result;
		}
		return null;
	}
	
	public AddressBookVO readBookInfo(int domainSeq, int bookSeq, int userSeq) {
		HashMap<String, Integer> param = new HashMap<String, Integer>();
		param.put("bookSeq", bookSeq);
		param.put("domainSeq", domainSeq);
		param.put("userSeq", userSeq);
		
		Object result = getSqlMapClientTemplate().queryForObject("Addrbook.getSharedBook", param);
		if(result != null){
			return (AddressBookVO)result;
		}
		return null;
	}
	
	public List<AddressBookMemberVO> getShareAddressAllList(int bookSeq, int groupSeq, int userSeq, String sortBy,
	        String sortDir) {
	        HashMap<String, Object> param = new HashMap<String, Object>();
	        param.put("userSeq", userSeq);
	        param.put("bookSeq", bookSeq);
	        param.put("groupSeq", groupSeq);
	        param.put("sortBy", sortBy);
	        param.put("sortDir", sortDir);
	            
	        return getSqlMapClientTemplate().queryForList("Addrbook.getShareAddressAllList", param);
	}
}
