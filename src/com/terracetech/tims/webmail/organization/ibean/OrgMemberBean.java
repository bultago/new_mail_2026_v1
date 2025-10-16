package com.terracetech.tims.webmail.organization.ibean;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.organization.vo.MemberVO;
import com.terracetech.tims.webmail.util.StringUtils;

public class OrgMemberBean {
	
	private MemberVO member;
	

	public OrgMemberBean(MemberVO member) {
		super();
		this.member = member;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJson(){
		JSONObject json = new JSONObject();
		if(member != null){
			json.put("name", getValue(member.getMemberName()));
			json.put("email", getValue(member.getMemberEmail()));
			json.put("mobile", getValue(member.getMobileNo()));
			json.put("officeTel", getValue(member.getOfficeTel()));
			json.put("titleName", getValue(member.getTitleName()));
			json.put("className", getValue(member.getClassName()));
			json.put("deptName", getValue(member.getDeptName()));
			json.put("empno", getValue(member.getEmpno()));
		}
		
		return json;
	}
	
	private String getValue(String val){
		if(StringUtils.isEmpty(val))
			return "";
		
		return val;
	}
}
