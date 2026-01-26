/**
 * MailLetterPaperService.java 2009. 4. 13.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.service;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.common.BaseService;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;
import com.terracetech.tims.webmail.mail.manager.LetterManager;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;

/**
 * <p><strong>MailLetterPaperService.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
@SuppressWarnings("all")
public class MailCommonService extends BaseService {
	
	private LetterManager letterManager = null;
	private SettingManager settingManager = null;
	private MailManager mailManager = null;
	private MailUserManager mailUserManager = null;
	
	public void setLetterManager(LetterManager letterManager) {
		this.letterManager = letterManager;
	}
	
	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}
	
	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}
	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}
	
	public JSONObject getLetterList(int page) throws Exception{
		
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		JSONObject jObj = new JSONObject();
		
		int letterCount = 6;
		int startPos = (page - 1) * letterCount;	
		int totalCount = letterManager.getTotalCount(domainSeq);
		
		
		if(startPos < totalCount && totalCount > (startPos+letterCount)){
			jObj.put("next", page+1);
		} else {
			jObj.put("next", -1);			
		}
		
		if( startPos < totalCount && startPos > 0){
			jObj.put("pre", page-1);			
		} else {
			jObj.put("pre", -1);
		}	
		
		String strLocalhost = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort();
		String letterPath = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");		
		String letterUrl = strLocalhost+"/design/common/image/attaches/";
		
		jObj.put("page", page);
		jObj.put("list", 	letterManager.getLetterJsonList(domainSeq, startPos, letterCount,letterPath, letterUrl));
		
		
		return jObj;
	}
	
	public JSONObject updateAutoSaveInfo(String mode, int term) throws Exception{
		JSONObject jObj = new JSONObject();		
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		try {
			settingManager.setAutoSaveInfo(mailUserSeq, term, mode);
			jObj.put("term", term);
			jObj.put("result", "success");
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			jObj.put("result", "fail");
		}		
		return jObj;		
	}
	
	public JSONObject searchAddressByKeyowrd(String[] keywords,boolean isNotOrgSearch) throws Exception{
		JSONObject jObj = new JSONObject();		
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		String locale = user.get(User.LOCALE);
		
		JSONArray singleAddrList = new JSONArray();
		JSONArray multiAddrList = new JSONArray();
		JSONArray emptyAddrList = new JSONArray();
		MailAddressBean[] addrList = null;		
		try {
			boolean isOrgType = false;
			for (int i = 0; i < keywords.length; i++) {
				addrList = mailManager.getUserMailAddressList(userSeq, domainSeq, locale, 100, keywords[i],false,isNotOrgSearch);
				if(addrList != null){
					if(addrList.length > 1){
						for (MailAddressBean mailAddressBean : addrList) {
							multiAddrList.add(mailAddressBean.toJson());
							if(!isOrgType){
								isOrgType = mailAddressBean.isOrgInfo();								
							}
						}
					} else if(addrList.length == 1){
						singleAddrList.add(addrList[0].toJson());
					} else {
						emptyAddrList.add(keywords[i]);
					}
				} else {
					emptyAddrList.add(keywords[i]);
				}
			}
			
			if(singleAddrList.size() > 0 || multiAddrList.size() > 0){
				jObj.put("result", "success");
				jObj.put("single", singleAddrList);
				jObj.put("multi", multiAddrList);
				jObj.put("multiType",(isOrgType)?"org":"normal");				
			} else {
				jObj.put("result", "empty");
			}
			
			jObj.put("empty", emptyAddrList);
			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			jObj.put("result", "fail");
		} finally {
			addrList = null;			
		}
		return jObj;
	}
	
	public JSONObject searchAccountDN(String[] emails) throws Exception{
		JSONObject jObj = new JSONObject();
		JSONArray dnList = new JSONArray();
		String[] dnValues = null;
		String[] emailValue = null;
		String valueDN = null;
		try {
			for (String email : emails) {
				if(email != null && 
					email.length() > 0 && 
					email.indexOf("@") > 0){
					
					emailValue = email.split("@");
					valueDN= mailUserManager.getSearchUserDN(emailValue[0], emailValue[1]);
					if(valueDN != null && 
						valueDN.length() > 0 &&
						!dnList.contains(valueDN)){
						dnList.add(valueDN);
					}					
				}			
			}
			jObj.put("result", "success");
			jObj.put("dnList", dnList);			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			jObj.put("result", "fail");
		} finally{
			emailValue = null;
			valueDN = null;
		}
		return jObj;
	}
}
