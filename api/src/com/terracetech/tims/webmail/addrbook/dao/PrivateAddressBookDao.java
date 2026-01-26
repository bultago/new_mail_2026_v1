package com.terracetech.tims.webmail.addrbook.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.addrbook.vo.AddressBookGroupVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressEnvVO;
import com.terracetech.tims.webmail.addrbook.vo.GroupMemberRelationVO;
import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;
import com.terracetech.tims.webmail.organization.vo.MemberVO;

/**
 * PrivateAddressBookDao MyBatis Mapper Interface
 * 
 * 원본 클래스: PrivateAddressBookDao extends SqlMapClientDaoSupport
 * 변환 내용: iBATIS → MyBatis Mapper 인터페이스
 * 변환일: 2025-10-20
 * 총 메서드 수: 31개 (원본 기준)
 */
@Mapper
public interface PrivateAddressBookDao {

    /**
     * 원본: public List<AddressBookMemberVO> readAddressListByIndex(int groupSeq, int userSeq, 
     *              String startChar, int skipResult, int maxResult, String sortBy, String sortDir)
     */
    List<AddressBookMemberVO> readAddressListByIndex(@Param("groupSeq") int groupSeq, @Param("userSeq") int userSeq, 
                                                      @Param("startChar") String startChar, @Param("skipResult") int skipResult, 
                                                      @Param("maxResult") int maxResult, @Param("sortBy") String sortBy, 
                                                      @Param("sortDir") String sortDir);

    /**
     * 원본: public List<AddressBookMemberVO> getAddPrivateAddressListByDate(int userSeq, String fromDate, 
     *              int skipResult, int maxResult)
     */
    List<AddressBookMemberVO> getAddPrivateAddressListByDate(@Param("userSeq") int userSeq, @Param("fromDate") String fromDate, 
                                                             @Param("skipResult") int skipResult, @Param("maxResult") int maxResult);

    /**
     * 원본: public List<AddressBookMemberVO> getModPrivateAddressListByDate(int userSeq, String fromDate, 
     *              int skipResult, int maxResult)
     */
    List<AddressBookMemberVO> getModPrivateAddressListByDate(@Param("userSeq") int userSeq, @Param("fromDate") String fromDate, 
                                                             @Param("skipResult") int skipResult, @Param("maxResult") int maxResult);

    /**
     * 원본: public List<AddressBookMemberVO> readAddressListByGroup(int groupSeq, int userSeq, 
     *              String startChar, int skipResult, int maxResult, String sortBy, String sortDir)
     */
    List<AddressBookMemberVO> readAddressListByGroup(@Param("groupSeq") int groupSeq, @Param("userSeq") int userSeq, 
                                                     @Param("startChar") String startChar, @Param("skipResult") int skipResult, 
                                                     @Param("maxResult") int maxResult, @Param("sortBy") String sortBy, 
                                                     @Param("sortDir") String sortDir);

    /**
     * 원본: public List<AddressBookGroupVO> readPrivateGroupList(int userSeq)
     */
    List<AddressBookGroupVO> readPrivateGroupList(@Param("userSeq") int userSeq);

    /**
     * 원본: public void saveGroup(AddressBookGroupVO group)
     */
    void saveGroup(AddressBookGroupVO group);

    /**
     * 원본: public void updateGroup(AddressBookGroupVO group)
     */
    void updateGroup(AddressBookGroupVO group);

    /**
     * 원본: public void deleteGroup(int userSeq, int groupSeq)
     */
    void deleteGroup(@Param("userSeq") int userSeq, @Param("groupSeq") int groupSeq);

    /**
     * 원본: public void saveMember(AddressBookMemberVO member)
     */
    void saveMember(AddressBookMemberVO member);

    /**
     * 원본: public int readAddressMemberSeqByClientId(int userSeq, String clientId)
     */
    int readAddressMemberSeqByClientId(@Param("userSeq") int userSeq, @Param("clientId") String clientId);

    /**
     * 원본: public AddressBookMemberVO readLastInsertMember(int userSeq)
     */
    AddressBookMemberVO readLastInsertMember(@Param("userSeq") int userSeq);

    /**
     * 원본: public void saveGroupMemberRelation(GroupMemberRelationVO vo)
     */
    void saveGroupMemberRelation(GroupMemberRelationVO vo);

    /**
     * 원본: public List<AddressBookMemberVO> readAddressMembers(int userSeq)
     */
    List<AddressBookMemberVO> readAddressMembers(@Param("userSeq") int userSeq);

    /**
     * 원본: public AddressBookMemberVO readAddressMember(int userSeq, int memberSeq)
     */
    AddressBookMemberVO readAddressMember(@Param("userSeq") int userSeq, @Param("memberSeq") int memberSeq);

    /**
     * 원본: public void deleteGroupMemberRelation(int userSeq, int memberSeq, int groupSeq)
     */
    void deleteGroupMemberRelation(@Param("userSeq") int userSeq, @Param("memberSeq") int memberSeq, 
                                   @Param("groupSeq") int groupSeq);

    /**
     * 원본: public void updateMember(AddressBookMemberVO member)
     */
    void updateMember(AddressBookMemberVO member);

    /**
     * 원본: public void deleteMember(int userSeq, int memberSeq, String delTime)
     */
    void deleteMember(@Param("userSeq") int userSeq, @Param("memberSeq") int memberSeq, @Param("delTime") String delTime);

    /**
     * 원본: public void deleteCompletlyMember(int userSeq, int memberSeq, String delTime)
     */
    void deleteCompletlyMember(@Param("userSeq") int userSeq, @Param("memberSeq") int memberSeq, 
                              @Param("delTime") String delTime);

    /**
     * 원본: public int readAddressListByIndexCount(int groupSeq, int userSeq, String startChar)
     */
    int readAddressListByIndexCount(@Param("groupSeq") int groupSeq, @Param("userSeq") int userSeq, @Param("startChar") String startChar);

    /**
     * 원본: public int readAddressListByGroupCount(int groupSeq, int userSeq, String startChar)
     */
    int readAddressListByGroupCount(@Param("groupSeq") int groupSeq, @Param("userSeq") int userSeq, @Param("startChar") String startChar);

    /**
     * 원본: public List<AddressBookMemberVO> searchMember(Map<String, Object> param, int skipResult, int maxResult)
     */
    List<AddressBookMemberVO> searchMember(Map<String, Object> param, @Param("skipResult") int skipResult, @Param("maxResult") int maxResult);

    /**
     * 원본: public int searchMemberCount(Map<String, Object> param)
     */
    int searchMemberCount(Map<String, Object> param);

    /**
     * 원본: public AddressEnvVO readAddressEnv(int domainSeq)
     */
    AddressEnvVO readAddressEnv(@Param("mailUserSeq") int mailUserSeq);

    /**
     * 원본: public int getGroupCount(int userSeq)
     */
    int getGroupCount(@Param("userSeq") int userSeq);

    /**
     * 원본: public List<MailAddressBean> readAddressListByGroup(String groupName, int userSeq)
     */
    List<MailAddressBean> readAddressListByGroup(@Param("groupName") String groupName, @Param("userSeq") int userSeq);

    /**
     * 원본: public AddressBookGroupVO findGroup(String groupName, int userSeq)
     */
    AddressBookGroupVO findGroup(@Param("groupName") String groupName, @Param("userSeq") int userSeq);

    /**
     * 원본: public AddressBookGroupVO readGroupInfo(int userSeq, int groupSeq)
     */
    AddressBookGroupVO readGroupInfo(@Param("userSeq") int userSeq, @Param("groupSeq") int groupSeq);

    /**
     * 원본: public AddressBookMemberVO readOrgMember(String codeLocale, String orgCode, int domainSeq, int memberSeq)
     */
    AddressBookMemberVO readOrgMember(@Param("codeLocale") String codeLocale, @Param("orgCode") String orgCode, 
                                      @Param("domainSeq") int domainSeq, @Param("memberSeq") int memberSeq);

    /**
     * 원본: public List<AddressBookMemberVO> getDelPrivateAddressListByDate(int userSeq, String fromDate, 
     *              int skipResult, int maxResult)
     */
    List<AddressBookMemberVO> getDelPrivateAddressListByDate(@Param("userSeq") int userSeq, @Param("fromDate") String fromDate, 
                                                             @Param("skipResult") int skipResult, @Param("maxResult") int maxResult);

    /**
     * 원본: public List<AddressBookMemberVO> getPrivateAddressAllList(int groupSeq, int userSeq, String sortBy, String sortDir)
     */
    List<AddressBookMemberVO> getPrivateAddressAllList(@Param("groupSeq") int groupSeq, @Param("userSeq") int userSeq, 
                                                       @Param("sortBy") String sortBy, @Param("sortDir") String sortDir);

    /**
     * 원본: public List<AddressBookMemberVO> getExistEmail(int userSeq, String email, String name)
     */
    List<AddressBookMemberVO> getExistEmail(@Param("userSeq") int userSeq, @Param("email") String email, 
                                           @Param("name") String name);
}