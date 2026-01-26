package com.terracetech.tims.webmail.organization.action;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.organization.manager.OrganizationManager;
import com.terracetech.tims.webmail.organization.vo.DeptVO;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class ViewOranizationTreeJsonAction extends BaseAction {
	
	private OrganizationManager manager = null;
	
	private String root = null;
	
	public void setManager(OrganizationManager manager) {
		this.manager = manager;
	}

	public String execute() throws Exception {
		
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		String installLocale = EnvConstants.getBasicSetting("setup.state");
		String userLocale = user.get(User.LOCALE);
		String orgLocale = userLocale;
		if ("jp".equals(installLocale)) {
			orgLocale = "ko".equals(userLocale) ? "en" : userLocale;
		} else if ("ko".equals(installLocale)) {
			orgLocale = "jp".equals(userLocale) ? "en" : userLocale;
		}

		JSONArray treeArray = new JSONArray();
		boolean isRoot = false;
		try {
			
			if (StringUtils.isEmpty(root)) {
				root = manager.readRootDept(mailDomainSeq);
				isRoot = true;
			}
			
			List<DeptVO> childList = manager.readDeptChildList(mailDomainSeq, root);
			
			if (childList != null && childList.size() > 0) {
				DeptVO deptVo = null;
				JSONObject tree = null;
				StringBuffer sb = null;
				for (int i=0; i <childList.size(); i++) {
					deptVo = childList.get(i);
					if ("jp".equals(orgLocale)) {
						deptVo.setOrgName(StringUtils.isEmpty(deptVo.getOrgJpName()) ? "&nbsp;" : deptVo.getOrgJpName());
					} else if ("en".equals(orgLocale)) {
						deptVo.setOrgName(StringUtils.isEmpty(deptVo.getOrgEnName()) ? "&nbsp;" : deptVo.getOrgEnName());
					}
					tree = new JSONObject();
					sb = new StringBuffer();
					sb.append("<table class='treeNodeWrapperTable' cellspacing='0' cellpadding='0'>");
					sb.append("<tr><td class='treeNodeWrapperContents'>");
					sb.append("<a id='link_"+deptVo.getOrgCode()+"' onclick='viewOrgMember(\""+deptVo.getOrgCode()+"\", \""+deptVo.getOrgName()+"\", \""+deptVo.getOrgFullcode()+"\");' href='javascript:;'>"+deptVo.getOrgName()+"</a>");
					sb.append("</td></tr></table>");
					tree.put("text", sb.toString());
					if (manager.readHasChild(mailDomainSeq, deptVo.getOrgCode()) > 0) {
						tree.put("hasChildren", true);
					} else {
						tree.put("hasChildren", false);
						tree.put("classes", "closed");
					}
					tree.put("isRoot", isRoot);
					tree.put("expanded", false);
					tree.put("id", deptVo.getOrgCode());

					treeArray.add(tree);
				}
			}
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}
		
		ResponseUtil.processResponse(response, treeArray);
		
		return null;
	}

	public void setRoot(String root) {
		this.root = root;
	}
	
}
