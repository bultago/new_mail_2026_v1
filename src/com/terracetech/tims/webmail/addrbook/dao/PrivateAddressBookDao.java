package com.terracetech.tims.webmail.addrbook.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.addrbook.manager.AddressBookUtils;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookGroupVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressEnvVO;
import com.terracetech.tims.webmail.addrbook.vo.GroupMemberRelationVO;
import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;
import com.terracetech.tims.webmail.organization.vo.MemberVO;

public class PrivateAddressBookDao extends SqlMapClientDaoSupport{

	@SuppressWarnings("unchecked")
	public List<AddressBookMemberVO> readAddressListByIndex(int groupSeq,
			int userSeq, String startChar, int skipResult, int maxResult, String sortBy, String sortDir) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("user", String.valueOf(userSeq));
		if (startChar != null){
			param.put("name", startChar);
			AddressBookUtils.setJapaneses(startChar, param);
		}
			
		if (groupSeq != 0)
			param.put("book", String.valueOf(groupSeq));
		
		param.put("sortBy", sortBy);
		param.put("sortDir", sortDir);
		
		param.put("skipResult", skipResult);
		param.put("maxResult", maxResult);
		
		AddressBookUtils.setKorean(param);

		return getSqlMapClientTemplate().queryForList("Addrbook.getPrivateAddressList", param);
	}


	@SuppressWarnings("unchecked")
	public List<AddressBookMemberVO> getAddPrivateAddressListByDate(int userSeq, String fromDate, int skipResult, int maxResult){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("user", userSeq);
		param.put("fromDate", fromDate);
		param.put("skipResult", skipResult);
		param.put("maxResult", maxResult);
		
		return getSqlMapClientTemplate().queryForList("Addrbook.getAddPrivateAddressListByDate", param);
	}
	
	@SuppressWarnings("unchecked")
	public List<AddressBookMemberVO> getModPrivateAddressListByDate(int userSeq, String fromDate, int skipResult, int maxResult){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("user", userSeq);
		param.put("fromDate", fromDate);
		param.put("skipResult", skipResult);
		param.put("maxResult", maxResult);
		
		return getSqlMapClientTemplate().queryForList("Addrbook.getModPrivateAddressListByDate", param);
	}

	
	@SuppressWarnings("unchecked")
	public List<AddressBookMemberVO> readAddressListByGroup(int groupSeq,
			int userSeq, String startChar, int skipResult, int maxResult, String sortBy, String sortDir) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("user", String.valueOf(userSeq));
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
			
		return getSqlMapClientTemplate().queryForList("Addrbook.getPrivateAddressListByGroup", param);
	}
	
	@SuppressWarnings("unchecked")
	public List<AddressBookGroupVO> readPrivateGroupList(int userSeq){

		return getSqlMapClientTemplate().queryForList( "Addrbook.getPrivateGroupList", userSeq);
	}

	public void saveGroup(AddressBookGroupVO group) {
		getSqlMapClientTemplate().insert("Addrbook.insertPrivateGroup", group);
	}

	public void updateGroup(AddressBookGroupVO group) {
		getSqlMapClientTemplate().update("Addrbook.updatePrivateGroup", group);
	}

	public void deleteGroup(int userSeq, int groupSeq) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("userSeq", String.valueOf(userSeq));
		param.put("groupSeq", String.valueOf(groupSeq));
		
		getSqlMapClientTemplate().delete("Addrbook.deletePrivateGroup", param);
	}

	public void saveMember(AddressBookMemberVO member) {
		getSqlMapClientTemplate().insert("Addrbook.insertPrivateMember", member);
	}
	
	@SuppressWarnings("unchecked")
	public int readAddressMemberSeqByClientId(int userSeq,String clientId){
		Map<String, String> param = new HashMap<String, String>();
		param.put("userSeq", String.valueOf(userSeq));
		param.put("clientId", String.valueOf(clientId));
		
		List<Integer> list = getSqlMapClientTemplate().queryForList("Addrbook.getPrivateMemberByClientId", param);
		if(list ==null)
			return 0;
		if(list.size() >= 1)
			return list.get(0);
		
		return 0;
	}

	public AddressBookMemberVO readLastInsertMember(int userSeq) {
		
		return (AddressBookMemberVO) getSqlMapClientTemplate().queryForObject("Addrbook.getLastInsertMember", userSeq);
	}

	public void saveGroupMemberRelation(GroupMemberRelationVO vo) {
		getSqlMapClientTemplate().insert("Addrbook.insertPrivateGroupMemberRelation", vo);
	}

	@SuppressWarnings("unchecked")
	public List<AddressBookMemberVO> readAddressMembers(int userSeq){
		return getSqlMapClientTemplate().queryForList("Addrbook.getPrivateMembers", userSeq);
	}
	
	public AddressBookMemberVO readAddressMember(int userSeq, int memberSeq) {
		Map<String, Integer> param = new HashMap<String, Integer>();
		param.put("userSeq", userSeq);
		param.put("memberSeq", memberSeq);
		
		return (AddressBookMemberVO) getSqlMapClientTemplate().queryForObject(
				"Addrbook.getPrivateMember", param);
	}
	
	public void deleteGroupMemberRelation(int userSeq, int memberSeq, int groupSeq) {
		Map<String, Integer> param = new HashMap<String, Integer>();
		param.put("userSeq", userSeq);
		param.put("memberSeq", memberSeq);
		param.put("groupSeq", groupSeq);
		
		getSqlMapClientTemplate().delete("Addrbook.deletePrivateGroupMemberRelation", param);		
	}

	public void updateMember(AddressBookMemberVO member) {
		getSqlMapClientTemplate().update("Addrbook.updatePrivateMember", member);
	}

	public void deleteMember(int userSeq, int memberSeq, String delTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userSeq", userSeq);
		param.put("memberSeq", memberSeq);
		param.put("delTime", delTime);
		
		getSqlMapClientTemplate().delete("Addrbook.deletePrivateGroupMemberRelationAll", param);
		getSqlMapClientTemplate().delete("Addrbook.deletePrivateMember", param);
	}
	
	public void deleteCompletlyMember(int userSeq, int memberSeq, String delTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userSeq", userSeq);
		param.put("memberSeq", memberSeq);
		param.put("delTime", delTime);
		
		getSqlMapClientTemplate().delete("Addrbook.deletePrivateGroupMemberRelationAll", param);
		getSqlMapClientTemplate().delete("Addrbook.deleteCompletlyPrivateMember", param);
	}

	public int readAddressListByIndexCount(int groupSeq, int userSeq, String startChar) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("user", String.valueOf(userSeq));
		if (startChar != null){
			param.put("name", startChar);
			AddressBookUtils.setJapaneses(startChar, param);
		}
		
		AddressBookUtils.setKorean(param);

		return (Integer)getSqlMapClientTemplate().queryForObject( "Addrbook.getPrivateAddressListCount", param);
	}

	public int readAddressListByGroupCount(int groupSeq, int userSeq, String startChar) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("user", String.valueOf(userSeq));
		if (startChar != null){
			param.put("name", startChar);
			AddressBookUtils.setJapaneses(startChar, param);
		}
		
		param.put("groupSeq", String.valueOf(groupSeq));
		
		AddressBookUtils.setKorean(param);
			
		return (Integer)getSqlMapClientTemplate().queryForObject("Addrbook.getPrivateAddressListByGroupCount", param);
	}

	@SuppressWarnings("unchecked")
	public List<AddressBookMemberVO> searchMember(Map<String, Object> param, int skipResult, int maxResult) {
		param.put("skipResult", skipResult);
		param.put("maxResult", maxResult);
		
		AddressBookUtils.setKorean(param);
		
		return getSqlMapClientTemplate().queryForList("Addrbook.getPrivateSearchMember", param);
	}

	public int searchMemberCount(Map<String, Object> param) {
		
		AddressBookUtils.setKorean(param);
		
		return (Integer)getSqlMapClientTemplate().queryForObject("Addrbook.getPrivateSearchMemberCount", param);
	}
	
	public AddressEnvVO readAddressEnv(int domainSeq) {
		return (AddressEnvVO) getSqlMapClientTemplate().queryForObject("Addrbook.selectAddressEnv", domainSeq);
	}
	
	public int getGroupCount(int userSeq) {
		return (Integer)getSqlMapClientTemplate().queryForObject("Addrbook.getPrivateGroupCount", userSeq);
	}

	@SuppressWarnings("unchecked")
	public List<MailAddressBean> readAddressListByGroup(String groupName, int userSeq) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("groupName", groupName);
		param.put("userSeq", String.valueOf(userSeq));
		
		return getSqlMapClientTemplate().queryForList("Addrbook.getPrivateAddressEmailList", param);
	}

	public AddressBookGroupVO findGroup(String groupName, int userSeq) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("groupName", groupName);
		param.put("userSeq", String.valueOf(userSeq));
		AddressBookGroupVO group = (AddressBookGroupVO)getSqlMapClientTemplate().queryForObject("Addrbook.findPrivateAddressGroup", param);
		
		return group;
	}
	
	public AddressBookGroupVO readGroupInfo(int userSeq, int groupSeq) {
		HashMap<String, Integer> param = new HashMap<String, Integer>();
		param.put("userSeq", userSeq);
		param.put("groupSeq", groupSeq);
		
		Object result = getSqlMapClientTemplate().queryForObject("Addrbook.getPrivateGroup", param);
		if(result != null){
			return (AddressBookGroupVO)result;
		}
		return null;
	}


	public AddressBookMemberVO readOrgMember(String codeLocale, String orgCode, int domainSeq, int memberSeq) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mailDomain", domainSeq);
		param.put("userSeq", memberSeq);
		param.put("orgCode", orgCode);
		param.put("locale", codeLocale==null ? "ko" : codeLocale);
		
		return (AddressBookMemberVO) getSqlMapClientTemplate().queryForObject("Addrbook.getOrgMember", param);
	}


	@SuppressWarnings("unchecked")
	public List<AddressBookMemberVO> getDelPrivateAddressListByDate( int userSeq, String fromDate, int skipResult, int maxResult) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("user", userSeq);
		param.put("fromDate", fromDate);
		param.put("skipResult", skipResult);
		param.put("maxResult", maxResult);
		
		return getSqlMapClientTemplate().queryForList("Addrbook.getDelPrivateAddressListByDate", param);
	}
	

    @SuppressWarnings("unchecked")
    public List<AddressBookMemberVO> getPrivateAddressAllList(int groupSeq, int userSeq, String sortBy, String sortDir) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("userSeq", userSeq);
        param.put("groupSeq", groupSeq);
        param.put("sortBy", sortBy);
        param.put("sortDir", sortDir);
            
        return getSqlMapClientTemplate().queryForList("Addrbook.getPrivateAddressAllList", param);
    }
    
    @SuppressWarnings("unchecked")
	public List<AddressBookMemberVO> getExistEmail(int userSeq, String email, String name){
   	 Map<String, Object> param = new HashMap<String, Object>();
        param.put("userSeq", userSeq);
        param.put("email", email);
        param.put("name", name);
   	return getSqlMapClientTemplate().queryForList("Addrbook.readExistEmail", param);
   }

}
