package com.terracetech.tims.webmail.organization.manager;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.simple.JSONObject;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseService;
import com.terracetech.tims.webmail.exception.InvalidParameterException;
import com.terracetech.tims.webmail.exception.UserAuthException;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.organization.ibean.OrgMemberBean;
import com.terracetech.tims.webmail.organization.vo.DeptVO;
import com.terracetech.tims.webmail.organization.vo.MemberVO;
import com.terracetech.tims.webmail.util.StringUtils;

public class OrganizationService extends BaseService{
	
	public Logger log = LoggerFactory.getLogger(this.getClass());

	private OrganizationManager manager = null;
	
	private I18nResources resource = null;
	
	public void setManager(OrganizationManager manager) {
		this.manager = manager;
	}
	
	public void loadHttpResource() throws UserAuthException{
		super.loadHttpResource();
		
		Locale locale = new Locale(user.get(User.LOCALE));
		resource = new I18nResources(locale,"addr");
	}

	public JSONObject getJsonMember(int memberSeq, String orgCode){
		log.debug("OrganizationService.getJsonMember memberSeq=" + memberSeq);
		log.debug("OrganizationService.getJsonMember orgCode=" + orgCode);
		
		Locale locale = new Locale(user.get(User.LOCALE));
		
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		MemberVO member = manager.readMember(locale.getLanguage(),orgCode,domainSeq, memberSeq); 
		
		OrgMemberBean bean = new OrgMemberBean(member);
		return bean.toJson(); 
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getJsonDeptAddress(String orgCode){
		log.debug("OrganizationService.getJsonMember orgCode=" + orgCode);
		
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		DeptVO dept = manager.readDept(domainSeq, orgCode);
		if(dept ==null)
			throw new InvalidParameterException(resource.getMessage("addr.error.msg.05"));
		
		String address = "";
		List<DeptVO> depts = manager.findDept(domainSeq, dept.getOrgName());
		if(depts.size() >= 2){
			address = dept.getOrgName() + "." + dept.getOrgCode();
		}else{
			address = dept.getOrgName();
		}
		
		JSONObject json = new JSONObject();
		json.put("address", address);
		return json;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getDeptFullName(String orgCode) {
		JSONObject json = new JSONObject();
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		String orgFullName = manager.readDeptFullNameList(mailDomainSeq, orgCode);
		
		if (StringUtils.isEmpty(orgFullName)) {
			json.put("orgFullName", "");
		} else {
			json.put("orgFullName", orgFullName);
		}
		return json;
	}
}
