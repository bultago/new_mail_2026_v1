package com.terracetech.tims.webmail.addrbook.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.addrbook.vo.AddressBookConfigVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookGroupVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookModeratorVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookReaderVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookVO;
import com.terracetech.tims.webmail.addrbook.vo.GroupMemberRelationVO;
import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;

/**
 * SharedAddressBookDao MyBatis Mapper Interface
 * 
 * 원본 클래스: SharedAddressBookDao extends SqlSessionDaoSupport
 * 변환 내용: iBATIS → MyBatis Mapper 인터페이스
 * 변환일: 2025-10-20
 * 총 메서드 수: 45개 (원본 기준)
 */
@Mapper
public interface SharedAddressBookDao {

    /** 원본: public List<AddressBookVO> readAddressBookList(int usrSeq, int domainSeq) */
    List<AddressBookVO> readAddressBookList(@Param("usrSeq") int usrSeq, @Param("domainSeq") int domainSeq);

    /** 원본: public void saveSharedAddressBook(AddressBookVO vo) */
    void saveSharedAddressBook(AddressBookVO vo);

    /** 원본: public void updateSharedAddressBook(AddressBookVO vo) */
    void updateSharedAddressBook(AddressBookVO vo);

    /** 원본: public boolean isAddressBookModerator(int bookSeq, int userSeq) */
    boolean isAddressBookModerator(@Param("bookSeq") int bookSeq, @Param("userSeq") int userSeq);

    /** 원본: public void updateReaderType(int bookSeq, String type) */
    void updateReaderType(@Param("bookSeq") int bookSeq, @Param("type") String type);

    /** 원본: public void deleteSharedAddressBook(int bookSeq) */
    void deleteSharedAddressBook(@Param("bookSeq") int bookSeq);

    /** 원본: public List<AddressBookGroupVO> readGroupList(int bookSeq) */
    List<AddressBookGroupVO> readGroupList(@Param("bookSeq") int bookSeq);

    /** 원본: public void saveGroup(AddressBookGroupVO group) */
    void saveGroup(AddressBookGroupVO group);

    /** 원본: public void updateGroup(AddressBookGroupVO group) */
    void updateGroup(AddressBookGroupVO group);

    /** 원본: public void deleteGroup(int bookSeq, int groupSeq) */
    void deleteGroup(@Param("bookSeq") int bookSeq, @Param("groupSeq") int groupSeq);

    /** 원본: public List<AddressBookMemberVO> readAddressListByIndex(int bookSeq, String startChar, int skipResult, int maxResult, String sortBy, String sortDir) */
    List<AddressBookMemberVO> readAddressListByIndex(@Param("bookSeq") int bookSeq, @Param("startChar") String startChar, 
                                                      @Param("skipResult") int skipResult, @Param("maxResult") int maxResult, 
                                                      @Param("sortBy") String sortBy, @Param("sortDir") String sortDir);

    /** 원본: public int readAddressListByIndexCount(int bookSeq, String startChar) */
    int readAddressListByIndexCount(@Param("bookSeq") int bookSeq, @Param("startChar") String startChar);

    /** 원본: public List<AddressBookMemberVO> readAddressListByGroup(int groupSeq, int bookSeq, String startChar, int skipResult, int maxResult, String sortBy, String sortDir) */
    List<AddressBookMemberVO> readAddressListByGroup(@Param("groupSeq") int groupSeq, @Param("bookSeq") int bookSeq, 
                                                     @Param("startChar") String startChar, @Param("skipResult") int skipResult, 
                                                     @Param("maxResult") int maxResult, @Param("sortBy") String sortBy, 
                                                     @Param("sortDir") String sortDir);

    /** 원본: public int readAddressListByGroupCount(int groupSeq, int bookSeq, String startChar) */
    int readAddressListByGroupCount(@Param("groupSeq") int groupSeq, @Param("bookSeq") int bookSeq, @Param("startChar") String startChar);

    /** 원본: public void saveMember(AddressBookMemberVO member) */
    void saveMember(AddressBookMemberVO member);

    /** 원본: public AddressBookMemberVO readLastInsertMember(int bookSeq) */
    AddressBookMemberVO readLastInsertMember(@Param("bookSeq") int bookSeq);

    /** 원본: public void saveGroupMemberRelation(GroupMemberRelationVO vo) */
    void saveGroupMemberRelation(GroupMemberRelationVO vo);

    /** 원본: public void deleteGroupMemberRelation(int bookSeq, int memberSeq, int groupSeq) */
    void deleteGroupMemberRelation(@Param("bookSeq") int bookSeq, @Param("memberSeq") int memberSeq, 
                                   @Param("groupSeq") int groupSeq);

    /** 원본: public List<AddressBookMemberVO> readAddressMembers(int bookseq) */
    List<AddressBookMemberVO> readAddressMembers(@Param("bookseq") int bookseq);

    /** 원본: public void updateMember(AddressBookMemberVO member) */
    void updateMember(AddressBookMemberVO member);

    /** 원본: public void deleteMember(int bookSeq, int memberSeq) */
    void deleteMember(@Param("bookSeq") int bookSeq, @Param("memberSeq") int memberSeq);

    /** 원본: public AddressBookMemberVO readAddressMember(int bookSeq, int memberSeq) */
    AddressBookMemberVO readAddressMember(@Param("bookSeq") int bookSeq, @Param("memberSeq") int memberSeq);

    /** 원본: public int readAddressBookReaderListCount(int bookSeq, String searchType, String keyWord) */
    int readAddressBookReaderListCount(@Param("bookSeq") int bookSeq, @Param("searchType") String searchType, @Param("keyWord") String keyWord);

    /** 원본: public List<AddressBookReaderVO> readAddressBookReaderList(int bookSeq, int skipResult, int maxResult, String searchType, String keyWord) */
    List<AddressBookReaderVO> readAddressBookReaderList(@Param("bookSeq") int bookSeq, @Param("skipResult") int skipResult, 
                                                        @Param("maxResult") int maxResult, @Param("searchType") String searchType, 
                                                        @Param("keyWord") String keyWord);

    /** 원본: public void saveAddressBookReader(AddressBookReaderVO reader) */
    void saveAddressBookReader(AddressBookReaderVO reader);

    /** 원본: public void deleteAddressBookReader(int bookSeq, int memberSeq) */
    void deleteAddressBookReader(@Param("bookSeq") int bookSeq, @Param("memberSeq") int memberSeq);

    /** 원본: public void saveAddressBookModerator(AddressBookModeratorVO moderator) */
    void saveAddressBookModerator(AddressBookModeratorVO moderator);

    /** 원본: public int readAddressBookModeratorListCount(int bookSeq, String searchType, String keyWord) */
    int readAddressBookModeratorListCount(@Param("bookSeq") int bookSeq, @Param("searchType") String searchType, @Param("keyWord") String keyWord);

    /** 원본: public List<AddressBookModeratorVO> readAddressBookModerator(int bookSeq, int skipResult, int maxResult, String searchType, String keyWord) */
    List<AddressBookModeratorVO> readAddressBookModerator(@Param("bookSeq") int bookSeq, @Param("skipResult") int skipResult, 
                                                          @Param("maxResult") int maxResult, @Param("searchType") String searchType, 
                                                          @Param("keyWord") String keyWord);

    /** 원본: public int getCountSharedAddressBookReader(int book, String email) */
    int getCountSharedAddressBookReader(@Param("book") int book, @Param("email") String email);

    /** 원본: public int getCountSharedAddressBookModerator(int book, String email) */
    int getCountSharedAddressBookModerator(@Param("book") int book, @Param("email") String email);

    /** 원본: public void deleteAddressBookModerator(int bookSeq, int memberSeq) */
    void deleteAddressBookModerator(@Param("bookSeq") int bookSeq, @Param("memberSeq") int memberSeq);

    /** 원본: public int getGroupCount(int bookSeq) */
    int getGroupCount(@Param("bookSeq") int bookSeq);

    /** 원본: public boolean isAddressBookCreator(int domainSeq, int userSeq) */
    boolean isAddressBookCreator(@Param("domainSeq") int domainSeq, @Param("userSeq") int userSeq);

    /** 원본: public String readBookType(int addrbookSeq) */
    String readBookType(@Param("addrbookSeq") int addrbookSeq);

    /** 원본: public List<MailAddressBean> readAddressListByGroup(String searchStr, String bookSeq) */
    List<MailAddressBean> readAddressListByGroup(@Param("searchStr") String searchStr, @Param("bookSeq") String bookSeq);

    /** 원본: public List<AddressBookConfigVO> readAddressBookAuth(int domainSeq, int bookSeq, int userSeq) */
    List<AddressBookConfigVO> readAddressBookAuth(@Param("domainSeq") int domainSeq, @Param("bookSeq") int bookSeq, 
                                                  @Param("userSeq") int userSeq);

    /** 원본: public AddressBookGroupVO findGroup(String groupName, int domainSeq, int bookSeq) */
    AddressBookGroupVO findGroup(@Param("groupName") String groupName, @Param("domainSeq") int domainSeq, 
                                @Param("bookSeq") int bookSeq);

    /** 원본: public boolean hasGroupMember(int bookSeq, int groupSeq, int memberSeq) */
    boolean hasGroupMember(@Param("bookSeq") int bookSeq, @Param("groupSeq") int groupSeq, @Param("memberSeq") int memberSeq);

    /** 원본: public int readSharedAddressBookCount(int domainSeq) */
    int readSharedAddressBookCount(@Param("domainSeq") int domainSeq);

    /** 원본: public int searchMemberCount(Map<String, Object> param) */
    int searchMemberCount(Map<String, Object> param);

    /** 원본: public List<AddressBookMemberVO> searchMember(Map<String, Object> param, int skipResult, int maxResult) */
    List<AddressBookMemberVO> searchMember(Map<String, Object> param, @Param("skipResult") int skipResult, @Param("maxResult") int maxResult);

    /** 원본: public AddressBookGroupVO readGroupInfo(int bookSeq, int groupSeq) */
    AddressBookGroupVO readGroupInfo(@Param("bookSeq") int bookSeq, @Param("groupSeq") int groupSeq);

    /** 원본: public AddressBookVO readBookInfo(int domainSeq, int bookSeq, int userSeq) */
    AddressBookVO readBookInfo(@Param("domainSeq") int domainSeq, @Param("bookSeq") int bookSeq, @Param("userSeq") int userSeq);

    /** 원본: public List<AddressBookMemberVO> getShareAddressAllList(int bookSeq, int groupSeq, int userSeq, String sortBy, String sortDir) */
    List<AddressBookMemberVO> getShareAddressAllList(@Param("bookSeq") int bookSeq, @Param("groupSeq") int groupSeq, 
                                                     @Param("userSeq") int userSeq, @Param("sortBy") String sortBy, 
                                                     @Param("sortDir") String sortDir);
}