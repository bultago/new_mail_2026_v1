/**
 * ReceiveMDNAction.java 2009. 3. 3.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.action;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.terracetech.secure.crypto.SecureUtil;
import com.terracetech.secure.crypto.SymmetricCrypt;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p><strong>ReceiveMDNAction.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class ReceiveMDNAction extends BaseAction {
	
	private static final long serialVersionUID = -8610022334143045014L;
	
	private MailUserManager userManager = null;
	
	public ReceiveMDNAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}
	
	/**
	 * @param userManager �Ķ���͸� userManager���� ����
	 */
	public void setUserManager(MailUserManager userManager) {
		this.userManager = userManager;
	}



	public String execute() throws Exception{
		String mdnData = request.getParameter("mdnData");
		if(mdnData == null)
			mdnData = request.getParameter("batchmdnData");
		
		String from ="";
        String to ="";
        String mid ="";        
        String type="";
        		
		if(mdnData != null){
			String decodeMdn = StringUtils.UTF8URLDecode(SecureUtil.decrypt(SymmetricCrypt.AES, EnvConstants.ENCRYPT_KEY, mdnData));			
			Map<String, String> paramMap = new HashMap<String, String>();
			
			StringTokenizer stA = new StringTokenizer(decodeMdn,"&");
		       
	        while(stA.hasMoreElements()){	       	
	        	StringTokenizer stB = new StringTokenizer((String)stA.nextElement(),"=");	    
	        	if(stB.countTokens()==2){
	        		paramMap.put((String)stB.nextElement(),(String)stB.nextElement());
	        	}
	        }
	        
	        from = paramMap.get("from");      
	        to = paramMap.get("to");
	        mid = StringUtils.UTF8URLDecode(paramMap.get("mid") );
	        type = paramMap.get("type");
		} else {
			from = request.getParameter("from");
			to = request.getParameter("to");
			mid = request.getParameter("mid");
			type = request.getParameter("type");
		}
		String[] emailVals = null;
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		
		try {
			if(from == null) return null;
			emailVals = from.split("@");
			User mdnUser = userManager.readUserConnectionInfo(emailVals[0], emailVals[1]);
			String arg = "";
			if ("note".equalsIgnoreCase(type)) {
				arg = EnvConstants.getBasicSetting("note.home");
			}
			if(mdnUser != null){
				mdnUser.put(User.IMAP_LOGIN_ARGS, mdnUser.get(User.MESSAGE_STORE) + arg +
					" 0 0 0 0 0 0 " + mdnUser.get(User.MAIL_USER_SEQ) + " " + 
					mdnUser.get(User.MAIL_DOMAIN_SEQ));
				store = factory.connect(request.getRemoteAddr(), mdnUser);
				TMailFolder folder = store.getDefaultFolder();			
				
				if(isValidAddress(to)) {
	        		folder.xreceiveMDN(mid, to);
	        		if (!"note".equalsIgnoreCase(type)) {
	        			writeMailLog(true, "action_message_receivemdn", "", to, from, 0, "", mid);
	        		}
				}			
			}
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		} finally {
			if(store !=null && store.isConnected())
				store.close();
		}
		
		response.setHeader("Content-Type", "image/gif");

        OutputStream out = response.getOutputStream();
        out.write(EnvConstants.transimage);
        out.close();
		
		
		return null;
	}
	
	private boolean isValidAddress(String str) {
		int s = str.indexOf("@");

		if(s <= 0 || str.indexOf("*", s) > 0) {
			return false;
		}

		return true;
	}


	
}
