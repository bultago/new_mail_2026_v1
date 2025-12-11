package com.terracetech.tims.webmail.mailuser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.common.vo.CommonLogoVO;
import com.terracetech.tims.webmail.common.vo.LogoVO;
import com.terracetech.tims.webmail.mailuser.vo.MailDomainCodeVO;
import com.terracetech.tims.webmail.mailuser.vo.MailDomainGroupVO;
import com.terracetech.tims.webmail.mailuser.vo.MailDomainVO;

/**
 * MailDomainDao MyBatis Mapper Interface
 * 원본: MailDomainDao extends SqlSessionDaoSupport
 * 총 메서드 수: 21개
 */
@Mapper
public interface MailDomainDao {
    /** 원본: public int searchDomainSeq(String domain) */
    int searchDomainSeq(@Param("domain") String domain);
    
    /** 원본: public Map readMailDomain(int mailDomainSeq) */
    Map readMailDomain(@Param("mailDomainSeq") int mailDomainSeq);
    
    /** 원본: public String readMailDomainName(int mailDomainSeq) */
    String readMailDomainName(@Param("mailDomainSeq") int mailDomainSeq);
    
    /** 원본: public boolean modifyMailDomain(MailDomainVO vo) */
    boolean modifyMailDomain(MailDomainVO vo);
    
    /** 원본: public boolean deleteMailDomain(int mailDomainSeq) */
    boolean deleteMailDomain(@Param("mailDomainSeq") int mailDomainSeq);
    
    /** 원본: public Map readMailDomainGroup(int mailDomainSeq, int mailDomainGroupSeq) */
    Map readMailDomainGroup(@Param("mailDomainSeq") int mailDomainSeq, @Param("mailDomainGroupSeq") int mailDomainGroupSeq);
    
    /** 원본: public boolean modifyMailDomainGroup(MailDomainGroupVO vo) */
    boolean modifyMailDomainGroup(MailDomainGroupVO vo);
    
    /** 원본: public boolean deleteMailDomainGroup(int mailDomainGroupSeq) */
    boolean deleteMailDomainGroup(@Param("mailDomainGroupSeq") int mailDomainGroupSeq);
    
    /** 원본: public int readMailServiesFromDomain(int mailDomainSeq) */
    int readMailServiesFromDomain(@Param("mailDomainSeq") int mailDomainSeq);
    
    /** 원본: public List<MailDomainCodeVO> readDomainCode(int mailDomainSeq, String codeClass, String codeLocale) */
    List<MailDomainCodeVO> readDomainCode(@Param("mailDomainSeq") int mailDomainSeq, @Param("codeClass") String codeClass, 
                                         @Param("codeLocale") String codeLocale);
    
    /** 원본: public Map<String,Long> getLocalDomainMap() */
    Map<String,Long> getLocalDomainMap();
    
    /** 원본: public List<MailDomainVO> readMailDomainList() */
    List<MailDomainVO> readMailDomainList();
    
    /** 원본: public int readMailDomainListCount() */
    int readMailDomainListCount();
    
    /** 원본: public int readDomainGroupSeq(int mailDomainSeq, String mailGroup) */
    int readDomainGroupSeq(@Param("mailDomainSeq") int mailDomainSeq, @Param("mailGroup") String mailGroup);
    
    /** 원본: public LogoVO readLogoInfo(int mailDomainSeq) */
    LogoVO readLogoInfo(@Param("mailDomainSeq") int mailDomainSeq);
    
    /** 원본: public LogoVO readMobileLogoInfo(int mailDomainSeq) */
    LogoVO readMobileLogoInfo(@Param("mailDomainSeq") int mailDomainSeq);
    
    /** 원본: public String readDefaultDomain() */
    String readDefaultDomain();
    
    /** 원본: public String readNoticeContent(int mailDomainSeq) */
    String readNoticeContent(@Param("mailDomainSeq") int mailDomainSeq);
    
    /** 원본: public List<CommonLogoVO> readCommonLogo() */
    List<CommonLogoVO> readCommonLogo();
    
    /** 원본: public List<CommonLogoVO> readMobileCommonLogo() */
    List<CommonLogoVO> readMobileCommonLogo();
    
    /** 원본: public Map readDormantMonth(int mailDomainSeq) */
    Map readDormantMonth(@Param("mailDomainSeq") int mailDomainSeq);
}