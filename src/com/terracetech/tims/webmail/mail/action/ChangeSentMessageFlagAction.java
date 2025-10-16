package com.terracetech.tims.webmail.mail.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class ChangeSentMessageFlagAction extends BaseAction {
    
    private String messageId = null;
    private MailManager mailManager = null;

    public void setMailManager(MailManager mailManager) {
        this.mailManager = mailManager;
    }

    public String execute() throws Exception {
    	JSONObject jsonObject = new JSONObject();
    	HttpSession session = request.getSession();
    	 if (!checkPAIDAndMobile()) {
    		jsonObject.put("isSuccess", "false");
 			ResponseUtil.processResponse(response, jsonObject);
         	return null;
         }
		
        TMailStore store = null;
        TMailFolder folder = null;
        TMailStoreFactory factory = new TMailStoreFactory();
        try {
            store = factory.connect(request.getRemoteAddr(), user);
            folder = store.getFolder(FolderHandler.SENT);             
            folder.open(false);
            int auid = folder.xsearchMID(messageId);
            folder.close(false);
            if(auid < 1){
            	jsonObject.put("isSuccess", "false");
    			ResponseUtil.processResponse(response, jsonObject);
            	return null;
            }
            mailManager.setProcessResource(store, getMessageResource());
            mailManager.switchMessagesFlags(new long[]{auid}, FolderHandler.SENT, "S", true); 
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
        } finally {
            try {
                if(store !=null && store.isConnected()) store.close();         
            } catch (Exception e2) {}
        }
        jsonObject.put("isSuccess", "true");
        ResponseUtil.processResponse(response, jsonObject);
        return null;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
