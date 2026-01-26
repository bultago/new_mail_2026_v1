/**
 * BigAttachFileManageAction.java 2009. 4. 6.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.action;

import java.io.PrintWriter;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.BigattachManager;
import com.terracetech.tims.webmail.mail.vo.MailBigAttachVO;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ResponseUtil;

/**
 * <p><strong>BigAttachFileManageAction.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class BigAttachFileManageAction extends BaseAction {

	private static final long serialVersionUID = 8123031389719513501L;
	
	private BigattachManager bigAttachManager = null;
	
	public void setBigAttachManager(BigattachManager bigAttachManager){
		this.bigAttachManager = bigAttachManager;
	}
	
	
	public String viewAttachList() throws Exception{
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		MailBigAttachVO[] attList = null;		
		try {
			attList = bigAttachManager.getBigAttachList(userSeq);			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}		
		
		request.setAttribute("attList", attList);		
		return "attList";		
	}
	
	public String deleteAttachList() throws Exception{
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));		
		JSONObject jObj = new JSONObject();		
		String[] muids = request.getParameterValues("fuid");
		String useAgeQuota = null;
		try {			
			for (int i = 0; i < muids.length; i++) {				
				bigAttachManager.deleteBigattachInfo(userSeq, muids[i]);				
			}
			useAgeQuota = Long.toString(bigAttachManager.getBigAttachQuotaUsage(userSeq));
			jObj.put("result", "success");
			jObj.put("useage", useAgeQuota);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			jObj.put("result", "fail");
		}
		
		ResponseUtil.processResponse(response, jObj);
		
		return null;
	}

}
