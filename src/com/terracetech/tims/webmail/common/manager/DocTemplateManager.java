package com.terracetech.tims.webmail.common.manager;

import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;

import com.terracetech.tims.webmail.common.dao.DocTemplateDao;
import com.terracetech.tims.webmail.common.vo.DocTemplateVO;

public class DocTemplateManager {
	
	private DocTemplateDao templateDao = null;
	
	public void setTemplateDao(DocTemplateDao templateDao) {
		this.templateDao = templateDao;
	}

	public List<DocTemplateVO> getDocTemplateList(int mailDomainSeq, int start, int limit){
		return templateDao.readDocTemplateList(mailDomainSeq,start,limit);
	}
	
	@SuppressWarnings("all")
	public JSONArray getDocTemplateJsonList(int mailDomainSeq, 
			int start, int limit){
		
		List<DocTemplateVO> list = getDocTemplateList(mailDomainSeq,start,limit);		
		JSONArray docListArray = new JSONArray();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			docListArray.add(((DocTemplateVO) iterator.next()).toJson());		
		}
		
		return docListArray;
	}
	
	public DocTemplateVO getDocTemplate(int templateSeq){
		return templateDao.readDocTemplate(templateSeq);
	}

	public int getTotalCount(int domainSeq) { 
		return templateDao.readDocTemplateTotal(domainSeq);
	}
	

}
