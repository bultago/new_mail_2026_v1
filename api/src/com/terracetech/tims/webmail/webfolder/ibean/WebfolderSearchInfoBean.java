/**
 * WebfolderSortInfoBean.java 2009. 02. 18.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.webfolder.ibean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.mail.Message;
import jakarta.mail.search.AndTerm;
import jakarta.mail.search.ReceivedDateTerm;
import jakarta.mail.search.RecipientStringTerm;
import jakarta.mail.search.SearchTerm;
import jakarta.mail.search.SubjectTerm;

import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.Validation;

/**
 * <p><strong>WebfolderSortInfoBean.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li>������ ����Ʈ�� Sort�� ���� ���� Ŭ����</li>
 * <li>�� ������ ������ Sort �ϱ����� SearchTerm ��ü ��ȯ</li>
 * </ul>
 * @author jhlee
 * @since Tims7
 * @version 7.0 
 */
public class WebfolderSearchInfoBean {
	
	private String searchType = null;
	private String pattern = null;
	private SearchTerm searchTerm = null;
	
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
		
		/**
	 * @return pattern �� ��ȯ
	 */
	public String getPattern() {
		return pattern;
	}
	/**
	 * @param pattern �Ķ���͸� pattern���� ����
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	/**
	 * @return searchTerm �� ��ȯ
	 */
	public SearchTerm getSearchTerm() {
		parseSortInfo();
		return searchTerm;
	}
	
	/**
	 * <p>SearchTerm �� ����</p>
	 *
	 * @return void
	 */
	public void parseSortInfo(){
		searchTerm = null;
		List<SearchTerm> searchList = new ArrayList<SearchTerm>();		
		
		if (!Validation.isNull(pattern)) {
			if ("name".equals(searchType)) {
				searchList.add(new SubjectTerm(pattern));
			}
			else if ("ext".equals(searchType)) {
				searchList.add(new RecipientStringTerm(Message.RecipientType.CC,pattern));			
			}
			else if ("size".equals(searchType)) {
				searchList.add(new RecipientStringTerm(Message.RecipientType.TO,pattern));
			}
			else if ("date".equals(searchType)) {
				Date d = FormatUtil.strToDate(pattern);
				if (d != null) {				
					searchList.add(new ReceivedDateTerm(ReceivedDateTerm.GE, d));
				}
			}
		}
		
		int size = searchList.size();
		SearchTerm[] searchTerms = new SearchTerm[size];		
		
		if (size > 0) {
			searchList.toArray(searchTerms);
			searchTerm = new AndTerm(searchTerms);
		}
	}	
}
