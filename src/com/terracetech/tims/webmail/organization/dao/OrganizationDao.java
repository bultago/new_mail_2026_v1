package com.terracetech.tims.webmail.organization.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;
import com.terracetech.tims.webmail.organization.vo.DeptVO;
import com.terracetech.tims.webmail.organization.vo.MemberVO;

/**
 * OrganizationDao MyBatis Mapper Interface
 * 원본: OrganizationDao extends SqlSessionDaoSupport implements IOrganizationDao
 * 총 메서드 수: 17개
 */
@Mapper
public interface OrganizationDao {
    /** 원본: public void getOrgTree() */
    void getOrgTree();
    
    /** 원본: public void saveDept(DeptVO dept) */
    void saveDept(DeptVO dept);
    
    /** 원본: public DeptVO readDept(int domain, String ordCode) */
    DeptVO readDept(@Param("domain") int domain, @Param("ordCode") String ordCode);
    
    /** 원본: public List<DeptVO> readDepts(int domain) */
    List<DeptVO> readDepts(@Param("domain") int domain);
    
    /** 원본: public void saveMember(MemberVO member) */
    void saveMember(MemberVO member);
    
    /** 원본: public MemberVO readMember(String codeLocale, String orgCode, int domainSeq, int memberSeq) */
    MemberVO readMember(@Param("codeLocale") String codeLocale, @Param("orgCode") String orgCode, 
                       @Param("domainSeq") int domainSeq, @Param("memberSeq") int memberSeq);
    
    /** 원본: public List<MemberVO> readMemberList(String codeLocale, String orgCode, String orgFullCode, String hasSubDept, int domainSeq, int skipResult, int maxResult, String searchType, String keyWord, String sortBy, String sortDir, Map paramMap) */
    List<MemberVO> readMemberList(@Param("codeLocale") String codeLocale, @Param("orgCode") String orgCode, 
                                   @Param("orgFullCode") String orgFullCode, @Param("hasSubDept") String hasSubDept, 
                                   @Param("domainSeq") int domainSeq, @Param("skipResult") int skipResult, 
                                   @Param("maxResult") int maxResult, @Param("searchType") String searchType, 
                                   @Param("keyWord") String keyWord, @Param("sortBy") String sortBy, 
                                   @Param("sortDir") String sortDir, @Param("paramMap") Map<String, Object> paramMap);
    
    /** 원본: public void deleteMember(MemberVO member) */
    void deleteMember(MemberVO member);
    
    /** 원본: public int readMemberCount(String codeLocale, String orgCode, String orgFullCode, String hasSubDept, int domainSeq, String searchType, String keyWord) */
    int readMemberCount(@Param("codeLocale") String codeLocale, @Param("orgCode") String orgCode, 
                       @Param("orgFullCode") String orgFullCode, @Param("hasSubDept") String hasSubDept, 
                       @Param("domainSeq") int domainSeq, @Param("searchType") String searchType, 
                       @Param("keyWord") String keyWord);
    
    /** 원본: public String[] readDeptListWithHierarchy(int domainSeq, String orgCode) */
    String[] readDeptListWithHierarchy(@Param("domainSeq") int domainSeq, @Param("orgCode") String orgCode);
    
    /** 원본: public List<MailAddressBean> readAddressList(int domainSeq, String[] orgCodes, String codeType, String code) */
    List<MailAddressBean> readAddressList(@Param("domainSeq") int domainSeq, @Param("orgCodes") String[] orgCodes, 
                                         @Param("codeType") String codeType, @Param("code") String code);
    
    /** 원본: public List<DeptVO> readDeptChildList(int mailDomainSeq, String orgUpcode) */
    List<DeptVO> readDeptChildList(@Param("mailDomainSeq") int mailDomainSeq, @Param("orgUpcode") String orgUpcode);
    
    /** 원본: public DeptVO readRootDept(int mailDomainSeq) */
    DeptVO readRootDept(@Param("mailDomainSeq") int mailDomainSeq);
    
    /** 원본: public int readHasChild(int mailDomainSeq, String orgCode) */
    int readHasChild(@Param("mailDomainSeq") int mailDomainSeq, @Param("orgCode") String orgCode);
    
    /** 원본: public List<DeptVO> findDept(int mailDomainSeq, String orgName) */
    List<DeptVO> findDept(@Param("mailDomainSeq") int mailDomainSeq, @Param("orgName") String orgName);
    
    /** 원본: public List<DeptVO> findDept(int mailDomainSeq, String[] orgNames) */
    List<DeptVO> findDeptByNames(@Param("mailDomainSeq") int mailDomainSeq, @Param("orgNames") String[] orgNames);
    
    /** 원본: public String findOrgCode(int mailDomainSeq, String orgFullCode) */
    String findOrgCode(@Param("mailDomainSeq") int mailDomainSeq, @Param("orgFullCode") String orgFullCode);
}