package com.terracetech.tims.jmobile.mail.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.jmobile.common.action.BaseAction;
import com.terracetech.tims.jmobile.common.vo.PreworkJobBean;
import com.terracetech.tims.service.manager.MailServiceManager;
import com.terracetech.tims.service.tms.vo.MailWorkCondVO;
import com.terracetech.tims.service.tms.vo.MailWorkResultVO;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringUtils;

@SuppressWarnings("all")
public class MailWorkAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3885602374243113927L;
	private MailServiceManager mailServiceManager;
	
	public void setMailServiceManager(MailServiceManager mailServiceManager) {
		this.mailServiceManager = mailServiceManager;
	}
	
	public String execute() throws Exception{
		
		I18nResources msgResource = getMessageResource("jmail");
		String[] uids = request.getParameterValues("uid");
		String uidsStr = request.getParameter("uidStr");
		if(uidsStr != null){
			uids = uidsStr.split(",");
		}
		
		String folderName = request.getParameter("folderName");		
		Map map = request.getParameterMap();
		String forward = null;		
		if(	map.containsKey("moveSelect")){
			if(uids != null){				
				makeFolderInfo();
				request.setAttribute("action","move");
				request.setAttribute("uids",StringUtils.join(uids,","));
				request.setAttribute("fromFolderName", folderName);
				forward = "folderSelect";
			} else {
				request.setAttribute("alerttitle", msgResource.getMessage("error.title"));
				request.setAttribute("alertmsg", msgResource.getMessage("error.noitem"));				
				forward = "alert";
			}
			
			saveCurrentAction("mailwork");
		} else if(map.containsKey("deleteAction") ||
				map.containsKey("moveAction") ||
				map.containsKey("seenAction") ||
				map.containsKey("unseenAction")){
			if(uids != null){
				String workMode = null;
				String targetFolderName = null;
				String flag = null;
				boolean flagUse = false;
				String successMsg = null;
				if(map.containsKey("deleteAction")){
					workMode = "delete";
					successMsg = "delete.success";
				} else if (map.containsKey("moveAction")){
					workMode = "move";
					targetFolderName = request.getParameter("targetFolder");
					successMsg = "move.success";
				}  else if (map.containsKey("seenAction")){
					workMode = "flag";
					flag = "S";
					flagUse = true;
					successMsg = "flag.seen.success";
				}  else if (map.containsKey("unseenAction")){
					workMode = "flag";
					flag = "S";
					flagUse = false;			
					successMsg = "flag.unseen.success";
				}
				
				MailWorkCondVO mailWorkCondVO = new MailWorkCondVO();
				mailWorkCondVO.setRemoteIp(request.getRemoteAddr());
				mailWorkCondVO.setLocale(user.get(User.LOCALE));
				mailWorkCondVO.setUserEmail(user.get(User.EMAIL));
				mailWorkCondVO.setWorkMode(workMode);
				mailWorkCondVO.setUid(uids);
				mailWorkCondVO.setFolderName(new String[]{folderName});
				mailWorkCondVO.setTargetFolderName(targetFolderName);
				mailWorkCondVO.setFlagType(flag);
				mailWorkCondVO.setFlagUse(flagUse);				
				
				MailWorkResultVO mailWorkResultVO = mailServiceManager.doSimpleMailWork(mailWorkCondVO, user);
				
				if(mailWorkResultVO.isErrorOccur()){
					request.setAttribute("alerttitle", msgResource.getMessage("error.title"));
					request.setAttribute("alertmsg", msgResource.getMessage("error.systemerror"));
				} else {
					request.setAttribute("alerttitle", msgResource.getMessage("work.success"));
					request.setAttribute("alertmsg", msgResource.getMessage(successMsg));					
				}
			} else {
				request.setAttribute("alerttitle", msgResource.getMessage("error.title"));
				request.setAttribute("alertmsg", msgResource.getMessage("error.noitem"));
			}
			forward = "alert";		
			
		} else if(map.containsKey("reply") || 
				map.containsKey("replyall") ||
				map.containsKey("forward")){
			
			String writeType = null;
			if(map.containsKey("reply")){
				writeType = "reply";
			} else if(map.containsKey("replyall")){
				writeType = "replyall";
			} else if(map.containsKey("forward")){			
				writeType = "forward";
			}
			
			Map writeParamMap = new HashMap();
			writeParamMap.put("writeType", writeType);
			writeParamMap.put("uid", uids[0]);
			writeParamMap.put("folderName", folderName);
			
			request.getSession().setAttribute("writeParamMap", writeParamMap);
			forward = "mailwrite";
			
		}
		return forward;
	}
	
	public String preworkAction() throws Exception{
		String forwrad = null;	
		try{
		Stack jobStack = (Stack)request.getSession().getAttribute("jobStack");
		Object currentJob = request.getSession().getAttribute("currentJob");
		String currentAction = (currentJob != null)?(String)currentJob:"none";
		
		PreworkJobBean preJob = (PreworkJobBean)jobStack.pop();		
		String actionName = preJob.getActionName();		
		while(currentAction.equals(actionName)){
			preJob = (PreworkJobBean)jobStack.pop();
			actionName = preJob.getActionName();
		}
		
		Map paramMap = preJob.getParamMap();
		forwrad = actionName;
		if(paramMap != null){
			request.getSession().setAttribute("preParamMap", paramMap);
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return forwrad;
	}

}
