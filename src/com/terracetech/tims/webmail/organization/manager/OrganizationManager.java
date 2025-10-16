package com.terracetech.tims.webmail.organization.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.terracetech.tims.webmail.common.dao.SystemConfigDao;
import com.terracetech.tims.webmail.mail.dao.CacheEmailDao;
import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;
import com.terracetech.tims.webmail.organization.dao.OrganizationDao;
import com.terracetech.tims.webmail.organization.vo.DeptVO;
import com.terracetech.tims.webmail.organization.vo.MemberVO;
import com.terracetech.tims.webmail.util.StringUtils;

public class OrganizationManager {

	private OrganizationDao dao;
	
	private SystemConfigDao systemConfigDao = null;
	
	private CacheEmailDao emailDao = null;

	public void setDao(OrganizationDao dao) {
		this.dao = dao;
	}
	
	public void setSystemConfigDao(SystemConfigDao systemConfigDao) {
		this.systemConfigDao = systemConfigDao;
	}

	public void setEmailDao(CacheEmailDao emailDao) {
		this.emailDao = emailDao;
	}

	public DeptVO readOrganizationTree(int domainSeq, String orgCode) {
		
		List<DeptVO> deptList = dao.readDepts(domainSeq);
		if(deptList!=null){
			for (DeptVO deptVO : deptList) {
				deptVO.setOrgName(StringUtils.replace(deptVO.getOrgName(), "'", ""));
			}	
		}
		
		
		DeptVO root = getRoot(deptList);
		
		if(root!=null)
		{
			deptList.remove(root);
			setChildDept(deptList, root);
		}
		
		return root;
	}
	
	/**
	 * 부모 자식관계가 성립이 되면 목록에서 제거를 함으로써 필요없는 루프를 돌지 않는다.
	 * @param deptList
	 * @param parent
	 */
	private void setChildDept(List<DeptVO> deptList, DeptVO parent){
		DeptVO[] list = deptList.toArray(new DeptVO[deptList.size()]);
		for (DeptVO dept : list){
			if(dept.getOrgCode().equals(parent.getOrgCode())){
				//자기자신이면 Skip
			}else if(parent.getOrgFullcode().length() >= dept.getOrgFullcode().length()){
				//자기보다 상위노드이면 Skip
			}else{
				if(dept.getOrgUpcode().equals(parent.getOrgCode()))
				{
					deptList.remove(dept);
					parent.addChild(dept);
					setChildDept(deptList, dept);
				}
				
			}
		}
	}

	private DeptVO getRoot(List<DeptVO> deptList) {
		DeptVO root = null;
		for (DeptVO dept : deptList) {
			if(dept.getOrgCode().equals(dept.getOrgUpcode()))
				root = dept;
			
		}
		return root;
	}

	public void saveDept(DeptVO dept){
		dao.saveDept(dept);
	}
	
	public boolean hasSubDept(int domainSeq){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("domainSeq",domainSeq);
		String[] configNames = {"org_show_subdept"};
		paramMap.put("configNames",configNames);
		
		Map<String,String> map = systemConfigDao.getDomainConfig(paramMap);
		
		String showSubDept = map.get("org_show_subdept");
		if(StringUtils.isEmpty(showSubDept)){
			showSubDept = "Y";
		}
		
		return showSubDept.equalsIgnoreCase("Y") ? true : false;
	}
	
	public MemberVO[] readMemberList(String codeLocale, String orgCode, String orgFullCode, boolean hasSubDept, int domainSeq, int currentPage, int maxResult, 
				String searchType, String keyWord, String sortBy, String sortDir){
		int skipResult = (currentPage - 1) * maxResult;
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("domainSeq",domainSeq);
		String[] configNames = {"org_dept_order"};
		paramMap.put("configNames",configNames);
		
		Map<String,String> map = systemConfigDao.getDomainConfig(paramMap);
		
		if("init".equals(sortBy) || "dept".equals(sortBy)){
			paramMap = getDeptOrder(map.get("org_dept_order"));
		}
		
		List<MemberVO> list = dao.readMemberList(codeLocale, orgCode, orgFullCode, hasSubDept, domainSeq, skipResult, maxResult, searchType, keyWord, sortBy, sortDir, paramMap);
		
		return list.toArray(new MemberVO[list.size()]);
	}
	
	public String initSortType(int domainSeq){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String sortType = "dept";
		paramMap.put("domainSeq",domainSeq);
		String[] configNames = {"org_dept_order"};
		paramMap.put("configNames",configNames);
		Map<String,String> map = systemConfigDao.getDomainConfig(paramMap);
		paramMap = getDeptOrder(map.get("org_dept_order"));
		if(paramMap != null)
			return (String) paramMap.get("level1");
		
		return sortType;
	}

	private Map<String, Object> getDeptOrder(String orderDesc) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(orderDesc)){
			String[] optionList = orderDesc.split(",");
			for (int i = 0; i < optionList.length; i++) {
				String[] values = optionList[i].split("=");
				if("LEVEL1".equals(values[0])){
					paramMap.put("level1",values[1]);
				}else if("LEVEL2".equals(values[0])){
					paramMap.put("level2",values[1]);
				}else if("LEVEL3".equals(values[0])){
					paramMap.put("level3",values[1]);
				}else if("LEVEL4".equals(values[0])){
					paramMap.put("level4",values[1]);
				}else if("LEVEL5".equals(values[0])){
					paramMap.put("level5",values[1]);
				}else if("LEVEL6".equals(values[0])){
					paramMap.put("level6",values[1]);
				}else if("LEVEL7".equals(values[0])){
					paramMap.put("level7",values[1]);
				}
			}
		}else{
			paramMap.put("level1","dept");
			paramMap.put("level2","class");
			paramMap.put("level3","title");
			paramMap.put("level4","name");
			paramMap.put("level5","email");
			paramMap.put("level6","dept");
			paramMap.put("level7","grade");
		}
		
		return paramMap;
	}
	
	public int readMemberCount(String codeLocale, String orgCode, String orgFullCode, boolean hasSubDept, int domainSeq,
			String searchType, String keyWord) {
		return dao.readMemberCount(codeLocale, orgCode, orgFullCode, hasSubDept, domainSeq, searchType, keyWord);
	}

	public MemberVO readMember(String codeLocale,String orgCode, int domainSeq, int memberSeq){
		return dao.readMember(codeLocale, orgCode, domainSeq, memberSeq);
	}
	
	public List<DeptVO> readDeptChildList(int mailDomainSeq, String orgUpcode) {
		return dao.readDeptChildList(mailDomainSeq, orgUpcode);
	}
	
	public String readRootDept(int mailDomainSeq) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("domainSeq",mailDomainSeq);
		String[] configNames = {"org_dept_root"};
		paramMap.put("configNames",configNames);
		
		Map<String,String> map = systemConfigDao.getDomainConfig(paramMap);
		
		if(map == null) return "";
		
		String rootCode = (String)map.get("org_dept_root");

		return StringUtils.isNotEmpty(rootCode) ? rootCode : "";
	}
	
	public int readHasChild(int mailDomainSeq, String orgCode) {
		return dao.readHasChild(mailDomainSeq, orgCode);
	}
	
	public DeptVO readDept(int domain, String orgCode) {
		DeptVO dept = dao.readDept(domain, orgCode);

		return dept;
	}

	public List<DeptVO> findDept(int domainSeq, String orgName) {
		List<DeptVO> depts = dao.findDept(domainSeq, orgName); 
		if(depts ==null)
			depts = new ArrayList<DeptVO>();
		
		return depts;
	}
	
	public String readDeptFullNameList(int domainSeq, String orgCode) {
		DeptVO dept = readDept(domainSeq, orgCode);
		String fullName = null;
		if (dept == null) {
			return null;
		}
		
		List<MailAddressBean> deptFullNameList = emailDao.readDeptFullNameList(domainSeq, dept.getOrgName(), false);
		if(deptFullNameList != null){
			String fullCode = null;
			String[] codes = null;
			for (MailAddressBean email : deptFullNameList) {
				fullCode = email.getDeptCode();
				codes = fullCode.split(":");
				if (codes != null && codes.length > 0) {
					if (orgCode.equals(codes[codes.length-1])) {
						fullName = email.getEmail();
					}
				}
			}	
		}
		return fullName;
	}
}
