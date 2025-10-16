package com.terracetech.tims.webmail.organization.dao;

import java.util.List;
import java.util.Map;

import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;
import com.terracetech.tims.webmail.organization.vo.DeptVO;
import com.terracetech.tims.webmail.organization.vo.MemberVO;

/**
 * <p>
 * <strong>IOrganizationDao.java</strong> Interface Description
 * </p>
 * <p>
 * 주요설명
 * </p>
 * <ul>
 * <li></li>
 * </ul>
 * 
 * @author jpjung
 * @since Tims7
 * @version 7.0
 */
public interface IOrganizationDao {

	public void getOrgTree();

	public void saveDept(DeptVO dept);

	public DeptVO readDept(int domain, String ordCode);

	public List<DeptVO> readDepts(int domain);

	public void saveMember(MemberVO member);
	
	public MemberVO readMember(String codeLocale, String orgCode, int domainSeq, int memberSeq);
	
	public int readMemberCount(String codeLocale, String orgCode, String orgFullCode, boolean hasSubDept, int domainSeq, String searchType, String keyWord);

	public List<MemberVO> readMemberList(String codeLocale, String orgCode, String orgFullCode, boolean hasSubDept, int domainSeq, int skipResult, int maxResult, String searchType, String keyWord, String sortBy, String sortDir, Map<String, Object> paramMap);

	public void deleteMember(MemberVO member);
	
	public String[] readDeptListWithHierarchy(int domainSeq, String orgCode);

	public List<MailAddressBean> readAddressList(int domainSeq, String[] orgCodes, String codeType, String code);
	
	public List<DeptVO> readDeptChildList(int mailDomainSeq, String orgUpcode);
	
	public DeptVO readRootDept(int mailDomainSeq);
	
	public int readHasChild(int mailDomainSeq, String orgCode);
	
	public List<DeptVO> findDept(int mailDomainSeq, String orgName);

	public List<DeptVO> findDept(int domainSeq, String[] orgNames);
	
	public String findOrgCode(int mailDomainSeq, String orgFullCode);
}