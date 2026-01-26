package com.terracetech.tims.webmail.home.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.home.vo.MailHomeLayoutVO;
import com.terracetech.tims.webmail.home.vo.MailHomePortletVO;
import com.terracetech.tims.webmail.home.vo.MailMenuLayoutVO;

/**
 * MailHomePortletDao MyBatis Mapper Interface
 * 원본: MailHomePortletDao extends SqlSessionDaoSupport implements IMailHomePortletDao
 * 총 메서드 수: 9개
 */
@Mapper
public interface MailHomePortletDao {
    /** 원본: public List<MailHomePortletVO> readPortlets(int domainSeq) */
    List<MailHomePortletVO> readPortlets(@Param("domainSeq") int domainSeq);
    
    /** 원본: public MailHomePortletVO readPortlet(int domainSeq, String portletName) */
    MailHomePortletVO readPortlet(@Param("domainSeq") int domainSeq, @Param("portletName") String portletName);
    
    /** 원본: public void saveLayoutPortlet(MailHomePortletVO vo) */
    void saveLayoutPortlet(MailHomePortletVO vo);
    
    /** 원본: public void deleteLayoutPortlet(int userSeq) */
    void deleteLayoutPortlet(@Param("userSeq") int userSeq);
    
    /** 원본: public List<MailHomePortletVO> readLayoutPortlet(int userSeq) */
    List<MailHomePortletVO> readLayoutPortlet(@Param("userSeq") int userSeq);
    
    /** 원본: public List<MailHomeLayoutVO> readLayout() */
    List<MailHomeLayoutVO> readLayout();
    
    /** 원본: public void saveLayout(MailHomeLayoutVO layout) */
    void saveLayout(MailHomeLayoutVO layout);
    
    /** 원본: public void savePortlet(MailHomePortletVO vo) */
    void savePortlet(MailHomePortletVO vo);
    
    /** 원본: public List<MailMenuLayoutVO> readMenusList(int mailDomainSeq) */
    List<MailMenuLayoutVO> readMenusList(@Param("mailDomainSeq") int mailDomainSeq);
}