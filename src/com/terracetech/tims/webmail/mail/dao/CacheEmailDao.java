package com.terracetech.tims.webmail.mail.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;

/**
 * CacheEmailDao MyBatis Mapper Interface
 * 
 * 원본 클래스: CacheEmailDao extends SqlSessionDaoSupport
 * 변환 내용: iBATIS → MyBatis Mapper 인터페이스
 * 변환일: 2025-10-20
 * 총 메서드 수: 8개 (원본 기준)
 */
@Mapper
public interface CacheEmailDao {

    /** 원본: public List<MailAddressBean> readPrivateEmailList(int domainSeq, int userSeq, String keyWord, boolean isAutoComplte) */
    List<MailAddressBean> readPrivateEmailList(@Param("domainSeq") int domainSeq, @Param("userSeq") int userSeq, 
                                               @Param("keyWord") String keyWord, @Param("isAutoComplte") boolean isAutoComplte);

    /** 원본: public List<MailAddressBean> readSharedEmailList(int domainSeq, int userSeq, String keyWord, boolean isAutoComplte) */
    List<MailAddressBean> readSharedEmailList(@Param("domainSeq") int domainSeq, @Param("userSeq") int userSeq, 
                                              @Param("keyWord") String keyWord, @Param("isAutoComplte") boolean isAutoComplte);

    /** 원본: public List<MailAddressBean> readOrgEmailList(int domainSeq, int userSeq, String locale, String keyWord, boolean isAutoComplte) */
    List<MailAddressBean> readOrgEmailList(@Param("domainSeq") int domainSeq, @Param("userSeq") int userSeq, 
                                          @Param("locale") String locale, @Param("keyWord") String keyWord, 
                                          @Param("isAutoComplte") boolean isAutoComplte);

    /** 원본: public List<MailAddressBean> readDeptList(int domainSeq, String keyWord, boolean isAutoComplte) */
    List<MailAddressBean> readDeptList(@Param("domainSeq") int domainSeq, @Param("keyWord") String keyWord, 
                                       @Param("isAutoComplte") boolean isAutoComplte);

    /** 원본: public List<MailAddressBean> readDeptFullNameList(int domainSeq, String keyWord, boolean isAutoComplte) */
    List<MailAddressBean> readDeptFullNameList(@Param("domainSeq") int domainSeq, @Param("keyWord") String keyWord, 
                                               @Param("isAutoComplte") boolean isAutoComplte);

    /** 원본: public List<MailAddressBean> readRecentEmailList(int domainSeq, int userSeq, String keyWord, boolean isAutoComplte) */
    List<MailAddressBean> readRecentEmailList(@Param("domainSeq") int domainSeq, @Param("userSeq") int userSeq, 
                                              @Param("keyWord") String keyWord, @Param("isAutoComplte") boolean isAutoComplte);

    /** 원본: public List<MailAddressBean> readDomainEmailList(int domainSeq, String keyWord, boolean isAutoComplte) */
    List<MailAddressBean> readDomainEmailList(@Param("domainSeq") int domainSeq, @Param("keyWord") String keyWord, 
                                              @Param("isAutoComplte") boolean isAutoComplte);

    /** 원본: public String readSearchRcptOption(int mailDomainSeq) */
    String readSearchRcptOption(@Param("mailDomainSeq") int mailDomainSeq);
}