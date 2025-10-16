package com.terracetech.tims.webmail.home.dao;

import java.sql.SQLException;
import java.util.List;

import com.terracetech.tims.webmail.home.vo.MailHomeLayoutVO;
import com.terracetech.tims.webmail.home.vo.MailHomePortletVO;
import com.terracetech.tims.webmail.home.vo.MailMenuLayoutVO;

public interface IMailHomePortletDao {
	
	public void saveLayout(MailHomeLayoutVO layout);
	
	public void savePortlet(MailHomePortletVO vo);

	public List<MailHomePortletVO> readPortlets(int domainSeq);
	
	public List<MailHomePortletVO> readLayoutPortlet(int userSeq);
	
	public void saveLayoutPortlet(MailHomePortletVO vo);
	
	public void deleteLayoutPortlet(int layout);
	
	public List<MailHomeLayoutVO> readLayout();
	
	/**
	 * 포틀릿을 실제 사용할때 이름으로 포틀릿을 찾아야 한다.
	 * @param portletName
	 * @return
	 * @throws SQLException
	 */
	public MailHomePortletVO readPortlet(int domainSeq, String portletName);
	
	public List<MailMenuLayoutVO> readMenusList(int mailDomainSeq);
}
