package com.terracetech.tims.webmail.organization.action;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.organization.manager.OrganizationManager;
import com.terracetech.tims.webmail.organization.vo.DeptVO;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class ViewOrganizationListJsonAction extends BaseAction {

	private OrganizationManager manager = null;
	private JSONArray orgListArray = null;
	private JSONObject orgObject = null;

	public void setManager(OrganizationManager manager) {
		this.manager = manager;
	}
	
	public String execute() throws Exception {
		
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		String installLocale = EnvConstants.getBasicSetting("setup.state");
		String userLocale = user.get(User.LOCALE);
		String orgLocale = userLocale;
		if ("jp".equals(installLocale)) {
			orgLocale = "ko".equals(userLocale) ? "en" : userLocale;
		} else if ("ko".equals(installLocale)) {
			orgLocale = "jp".equals(userLocale) ? "en" : userLocale;
		}
		
		boolean isSuccess = true;
		JSONObject jsonObj = new JSONObject();
		DeptVO root = null;
		try {
			root = manager.readOrganizationTree(domainSeq, null);
			if (root == null) {
				root = new DeptVO();
			}
			
			makeDeptList(root, orgLocale);
			
		} catch (Exception e) {
			isSuccess = false;
		}
		
		jsonObj.put("isSuccess", isSuccess);
		jsonObj.put("orgList", orgListArray);
		
		ResponseUtil.processResponse(response, jsonObj);
		
		return null;
	}
	
	private void makeDeptList(DeptVO root, String orgLocale) {
		orgListArray = new JSONArray();
		JSONObject firstChild = null;
		JSONArray nameArray = null;
		JSONArray codeArray = null;
		for(DeptVO dept : root.getChildren()){
			firstChild = new JSONObject();
			nameArray = new JSONArray();
			codeArray = new JSONArray();
			if ("jp".equals(orgLocale)) {
				nameArray.add(StringUtils.isEmpty(dept.getOrgJpName()) ? "" : dept.getOrgJpName());
			} else if ("en".equals(orgLocale)) {
				nameArray.add(StringUtils.isEmpty(dept.getOrgEnName()) ? "&nbsp;" : dept.getOrgEnName());
			} else {
				nameArray.add(dept.getOrgName());
			}
			codeArray.add(dept.getOrgFullcode());
			firstChild.put("nameArray", nameArray);
			firstChild.put("codeArray", codeArray);
			orgListArray.add(firstChild);
						
			makeChildDeptList(dept, firstChild, orgLocale);
		}
	}
	
	private void makeChildDeptList(DeptVO parent, JSONObject child, String orgLocale) {
		JSONObject childObject = null;
		JSONArray newNameArray = null;
		JSONArray newCodeArray = null;
		for(DeptVO dept : parent.getChildren()){
			childObject = new JSONObject();
			newNameArray = new JSONArray();
			newCodeArray = new JSONArray();
			JSONArray nameArray = (JSONArray)child.get("nameArray");
			JSONArray codeArray = (JSONArray)child.get("codeArray");
			newNameArray.addAll(nameArray);
			newCodeArray.addAll(codeArray);
			
			if ("jp".equals(orgLocale)) {
				newNameArray.add(StringUtils.isEmpty(dept.getOrgJpName()) ? "" : dept.getOrgJpName());
			} else if ("en".equals(orgLocale)) {
				newNameArray.add(StringUtils.isEmpty(dept.getOrgEnName()) ? "&nbsp;" : dept.getOrgEnName());
			} else {
				newNameArray.add(dept.getOrgName());
			}
			newCodeArray.add(dept.getOrgFullcode());
			childObject.put("nameArray", newNameArray);
			childObject.put("codeArray", newCodeArray);
			orgListArray.add(childObject);
			if (dept.getChildren().size() > 0) {
				makeChildDeptList(dept, childObject, orgLocale);
			}
		}
	}
}
