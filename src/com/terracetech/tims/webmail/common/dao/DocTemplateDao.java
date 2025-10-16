package com.terracetech.tims.webmail.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.terracetech.tims.webmail.common.vo.DocTemplateVO;

public class DocTemplateDao extends SqlMapClientDaoSupport {
	
	@SuppressWarnings("unchecked")
	public List<DocTemplateVO> readDocTemplateList(int mailDomainSeq, int start,int limit) {
		Map paramMap = new HashMap();
		paramMap.put("mailDomainSeq", mailDomainSeq);
		paramMap.put("skipResult", start);
		paramMap.put("pageBase", limit);
		return getSqlMapClientTemplate().queryForList("MailDomain.readDocTemplateList", paramMap);		
	}
	
	public DocTemplateVO readDocTemplate(int templateSeq){
		return (DocTemplateVO)getSqlMapClientTemplate().queryForObject("MailDomain.readDocTemplate", templateSeq);
	}
	
	public int readDocTemplateTotal(int mailDomainSeq) {
		return (Integer)getSqlMapClientTemplate().queryForObject("MailDomain.totalDocTemplateCount", mailDomainSeq);
	}
	

}
