package com.terracetech.tims.webmail.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terracetech.tims.webmail.common.vo.DocTemplateVO;

/**
 * DocTemplateDao MyBatis Mapper Interface
 * 원본: DocTemplateDao extends SqlSessionDaoSupport
 * 총 메서드 수: 3개
 */
@Mapper
public interface DocTemplateDao {
    /** 원본: public List<DocTemplateVO> readDocTemplateList(int mailDomainSeq, int start, int limit) */
    List<DocTemplateVO> readDocTemplateList(@Param("mailDomainSeq") int mailDomainSeq, @Param("start") int start, 
                                           @Param("limit") int limit);
    
    /** 원본: public DocTemplateVO readDocTemplate(int templateSeq) */
    DocTemplateVO readDocTemplate(@Param("templateSeq") int templateSeq);
    
    /** 원본: public int readDocTemplateTotal(int mailDomainSeq) */
    int readDocTemplateTotal(@Param("mailDomainSeq") int mailDomainSeq);
}