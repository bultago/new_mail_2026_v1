package com.terracetech.tims.webmail.mailuser.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.terracetech.tims.webmail.addrbook.dao.PrivateAddressBookDao;
import com.terracetech.tims.webmail.addrbook.dao.SharedAddressBookDao;
import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;
import com.terracetech.tims.webmail.organization.dao.OrganizationDao;
import com.terracetech.tims.webmail.organization.vo.DeptVO;
import com.terracetech.tims.webmail.util.StringUtils;

public class SearchEmailManager {

	private PrivateAddressBookDao privateAddrDao;
	
	private SharedAddressBookDao sharedAddrDao;
	
	private OrganizationDao orgDao;
	
	public void setPrivateAddrDao(PrivateAddressBookDao privateAddrDao) {
		this.privateAddrDao = privateAddrDao;
	}

	public void setSharedAddrDao(SharedAddressBookDao sharedAddrDao) {
		this.sharedAddrDao = sharedAddrDao;
	}
	
	public void setOrgDao(OrganizationDao orgDao) {
		this.orgDao = orgDao;
	}

	/**
	 * °³ÀÎÁÖ¼Ò·ÏÀÇ prefix : $
	 * $test
	 * @param userSeq
	 * @param searchStr
	 * @return
	 */
	public List<MailAddressBean> readPrivateAddrEmailList(int userSeq, String searchStr){
		return privateAddrDao.readAddressListByGroup(searchStr, userSeq);
	}

	/**
	 * °ø¿ëÁÖ¼Ò·ÏÀÇ prefix : &
	 * &test
	 * @param userSeq
	 * @param searchStr
	 * @return
	 */
	public List<MailAddressBean> readSharedAddrEmailList(int userSeq, String searchStr){
		List<MailAddressBean> list = null;
		try {
			StringTokenizer st = new StringTokenizer(searchStr, ".");
			String bookSeq = st.nextToken(); 
			String groupName = st.nextToken();
			list = sharedAddrDao.readAddressListByGroup(groupName, bookSeq);	
		} catch (Exception e) {
			return new ArrayList<MailAddressBean>();
		}
		
		
		return list;
	}
	
	/**
	 * Á¶Á÷µµÀÇ prefix : #
	 * :00000.title.002.true Á÷À§
	 * :00000.class.001.true Á÷±Þ
	 * :00000.class.001.false Á÷±Þ
	 * :00000.all.all.false ºÎ¼­
	 * :00000.all.all.true ºÎ¼­
	 * 
	 * @param domainSeq
	 * @param orgCode
	 * @param codeType title, class, all
	 * @param code Á÷±Þ, all
	 * @param isSearchHierarchy
	 * @return
	 */
	public List<MailAddressBean> readAddressList(int domainSeq, String orgCode, String codeType, String code, boolean isSearchHierarchy){
		List<MailAddressBean> list = null;
		DeptVO dept = orgDao.readDept(domainSeq, orgCode);
		
		if(isSearchHierarchy){
			String fullCode = dept.getOrgFullcode();
			String[] depts = orgDao.readDeptListWithHierarchy(domainSeq, fullCode);
			list = orgDao.readAddressList(domainSeq, depts, codeType, code);
		}else{
			String[] depts = {dept.getOrgCode()};
			list = orgDao.readAddressList(domainSeq, depts, codeType, code);
		}
		return list; 
	}

	public List<MailAddressBean> readAddressList(int domainSeq, String orgFullCode){
		List<MailAddressBean> list = null;
		String orgCode = orgDao.findOrgCode(domainSeq, orgFullCode);
		String[] depts = {orgCode};
		list = orgDao.readAddressList(domainSeq, depts, "all", "all");

		return list; 
	}

	public List<MailAddressBean> readDeptAddressList(int domainSeq, String orgName) {
		List<MailAddressBean> list = null;
		List<DeptVO> depts = orgDao.findDept(domainSeq, orgName);
		if(depts==null || depts.size()==0)
			return new ArrayList<MailAddressBean>();
		
		String[] deptCodes = {depts.get(0).getOrgCode()};
		list = orgDao.readAddressList(domainSeq, deptCodes, "all", "all");
		
		return list;
	}
	
	/**
	 * Retail»ç¾÷ºÎ/°­ºÏÁö¿ªº»ºÎ
	 * 
	 * @param domainSeq
	 * @param orgFullPath
	 * @return
	 */
	public List<MailAddressBean> readDeptAddressList2(int domainSeq, String orgFullPath) {
		List<MailAddressBean> list = new ArrayList<MailAddressBean>();
		String[] orgNames = orgFullPath.split("/");
		List<DeptVO> depts = orgDao.findDept(domainSeq, orgNames);
		if(depts==null || depts.size()==0)
			return list;
		
		String code = getDeptCodeByName(orgFullPath, depts);
		if(StringUtils.isNotEmpty(code)){
			String[] deptCodes = {code};
			list = orgDao.readAddressList(domainSeq, deptCodes, "all", "all");
		}
		
		return list;
	}
	
	protected String getDeptCodeByName(String fullPath, List<DeptVO> depts){
		if(depts==null || depts.size()==0)
			return "";
		
		if(StringUtils.isEmpty(fullPath))
			return "";
		
		String[] orgNames = fullPath.split("/");
		
		if(orgNames==null || orgNames.length==0)
			return "";
		
		DeptVO[] candidatedCodes = getDeptCode(orgNames[orgNames.length-1], depts);
		if(candidatedCodes.length==0)
			return "";
		
		if(candidatedCodes.length==1)
			return candidatedCodes[0].getOrgCode();
		
		for (DeptVO deptVO : candidatedCodes) {
			DeptVO topDept = getTopDept(deptVO, depts);
			if(topDept!= null){
				if(fullPath.equals(getFullPath("", deptVO))){
					return deptVO.getOrgCode();
				}
			}
		}
		
		return "";
	}
	
	protected String getFullPath(String path, DeptVO bottom){
		if(bottom==null)
			return "";
		if(bottom.getParent()==null){
			if(StringUtils.isEmpty(path)){
				return bottom.getOrgName();	
			}else{
				return bottom.getOrgName() + "/" + path;
			}
		}
			
		if(StringUtils.isEmpty(path)){
			path = bottom.getOrgName();
		}else{
			path = bottom.getOrgName() + "/"+ path;
		}
		
		return getFullPath(path, bottom.getParent());
	}
	
	protected DeptVO getTopDept(DeptVO dept, List<DeptVO> depts){
		if(dept==null)
			return null;
		
		if(depts==null || depts.size()==0)
			return dept;
		
		for (DeptVO deptVO : depts) {
			if(dept.getOrgUpcode().equals(deptVO.getOrgCode())){
				deptVO.addChild(dept);
				return getTopDept(deptVO, depts);
			}
		}
		
		
		return dept;
	}
	
	protected DeptVO[] getDeptCode(String orgName, List<DeptVO> depts){
		List<DeptVO> result = new ArrayList<DeptVO>();
		
		if(depts==null || depts.size()==0)
			return result.toArray(new DeptVO[result.size()]);
		
		
		for (DeptVO dept : depts) {
			if(dept.getOrgName().equals(orgName)){
				result.add(dept);
			}
		}
		
		return result.toArray(new DeptVO[result.size()]);
	}

	public List<MailAddressBean> readDeptAddressList(int domainSeq, String orgName, String orgCode) {
		List<MailAddressBean> list = null;
		List<DeptVO> depts = orgDao.findDept(domainSeq, orgName);
		if(depts==null || depts.size()==0)
			return new ArrayList<MailAddressBean>();
		
		for (DeptVO deptVO : depts) {
			if(deptVO.getOrgCode().equals(orgCode)){
				String[] deptCodes = {deptVO.getOrgCode()};
				list = orgDao.readAddressList(domainSeq, deptCodes, "all", "all");
				
				return list;
			}
		}
		
		return new ArrayList<MailAddressBean>();
	}
}
