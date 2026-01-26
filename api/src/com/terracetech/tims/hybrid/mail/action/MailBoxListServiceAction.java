package com.terracetech.tims.hybrid.mail.action;

import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.hybrid.common.HybridErrorCode;
import com.terracetech.tims.hybrid.common.action.BaseAction;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.mail.tag.TMailTag;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class MailBoxListServiceAction extends BaseAction {
    
    private MailManager mailManager = null;
    private String type = null;
    
    
    public void setMailManager(MailManager mailManager) {
        this.mailManager = mailManager;
    }

    @SuppressWarnings("unchecked")
    public String execute() throws Exception {
        int errorCode = HybridErrorCode.SUCCESS;
        int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        JSONObject jObj = new JSONObject();
        TMailStore store = null;
        TMailStoreFactory factory = new TMailStoreFactory();

        type = (StringUtils.isEmpty(type)) ? "default" : type;
        type = type.toLowerCase();
        try {
            store = factory.connect(remoteIp, user);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            
            Thread.sleep(2000);
            try {
                store = factory.connect(remoteIp, user);
            } catch (Exception e2) {
                LogManager.writeErr(this, e.getMessage(), e);
                errorCode = HybridErrorCode.IMAP_CONNECT_ERROR;
            }
        }
        
        try {
            mailManager.setProcessResource(store, getMessageResource());
            JSONArray tmpList = null;
            
            if ("quota".equals(type)) {                
                jObj.put("quotaInfo", mailManager.getQuotaRootInfo("Inbox").toJson());
            } else if ("default".equals(type)) {
                tmpList = mailManager.getJsonFolderList(EnvConstants.DEFAULT_FOLDER, false, userSeq);
                JSONArray newBoxList = convertMailBoxInfo(tmpList);
                jObj.put("boxNames", newBoxList);
            } else if ("user".equals(type)) {
                tmpList = mailManager.getJsonFolderList(EnvConstants.USER_FOLDER, false, userSeq);
                JSONArray newBoxList = new JSONArray();
                convertChildMailBoxInfo(tmpList, newBoxList);               
                jObj.put("boxNames", newBoxList);                
            } else if ("search".equals(type)) {
            	JSONArray folderList = mailManager.getJsonSearchFolders(null);
            	JSONArray reFolderList = new JSONArray();
            	if (folderList != null && folderList.size() > 0) {
            		for (int i=0; i<folderList.size(); i++) {
            			JSONObject folderInfo = (JSONObject)folderList.get(i);
            			folderInfo.put("folderId", folderInfo.get("id"));
            			folderInfo.remove("id");
            			folderInfo.put("folderName", folderInfo.get("name"));
            			folderInfo.remove("name");
            			folderInfo.put("folderQuery", folderInfo.get("query"));
            			folderInfo.remove("query");
            			reFolderList.add(folderInfo);
            		}
            		folderList.clear();
            	}
                jObj.put("searchNames", reFolderList);                
            } else if ("tag".equals(type)) {
                jObj.put("tagList", convertJsonTagList(mailManager.getTagList(null)));
            }
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            if (errorCode != HybridErrorCode.IMAP_CONNECT_ERROR) {
                errorCode = HybridErrorCode.GET_FOLDER_INFO_ERROR;
            }
        } finally {
            if (store != null && store.isConnected()) {
                store.close();
            }
        }

        jObj.put(HybridErrorCode.CODE_NAME, errorCode);
        ResponseUtil.makeJsonpResponse(request, response, jObj);
        
        return null;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    @SuppressWarnings("unchecked")
    private JSONArray convertMailBoxInfo(JSONArray jsonArray) throws Exception {
        if (jsonArray == null) {
            return null;
        }
        JSONArray newBoxList = new JSONArray();
        JSONObject mailBox = null;
        JSONObject mdnBox = null;
        for (int i=0; i<jsonArray.size(); i++) {
            mailBox = (JSONObject)jsonArray.get(i);
            String mailBoxName = (String)mailBox.get("fullName");
            if ("Reserved".equalsIgnoreCase(mailBoxName)) {
                continue;
            }
            newBoxList.add(convertMailBoxInfo(mailBox));
            if ("sent".equalsIgnoreCase(mailBoxName) 
                    && "link".equalsIgnoreCase(readMailNotiMode())) {
                mdnBox = new JSONObject();
                mdnBox.put("name", "mdnBox");
                mdnBox.put("display", "mdnBox");
                newBoxList.add(mdnBox);
            }
        }
        
        return newBoxList;
    }
    
    @SuppressWarnings("unchecked")
    private JSONObject convertMailBoxInfo(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        
        JSONObject newMailBox = new JSONObject();
        newMailBox.put("name", jsonObject.get("fullName"));
        newMailBox.put("display", jsonObject.get("name"));
        newMailBox.put("depth", jsonObject.get("depth"));
        newMailBox.put("total", jsonObject.get("totalCnt"));
        newMailBox.put("unseen", jsonObject.get("unseenCnt"));
        
        return newMailBox;
    }
    
    @SuppressWarnings("unchecked")
    private void convertChildMailBoxInfo(JSONArray orgArray, JSONArray newArray) {
        if (orgArray == null || newArray == null) {
            return;
        }
        JSONObject orgObj = null;
        for (int i=0; i<orgArray.size(); i++) {
            orgObj = (JSONObject)orgArray.get(i);
            newArray.add(convertMailBoxInfo(orgObj));
            if (orgObj.containsKey("child")) {
                convertChildMailBoxInfo((JSONArray)orgObj.get("child"), newArray);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private JSONArray convertJsonTagList(TMailTag[] tagList) {
        JSONArray list = new JSONArray();
        int size = 0;
        if (tagList != null) {
            Arrays.sort(tagList);
            size = tagList.length;
        }
        JSONObject tempObj = null;
        for (int i = 0; i < size; i++) {
            tempObj = new JSONObject();
            tempObj.put("tagId", tagList[i].getId());
            tempObj.put("tagName", StringUtils.EscapeHTMLTag(tagList[i].getName()));
            tempObj.put("tagColor", tagList[i].getColor().replaceAll("#", ""));
            list.add(tempObj);
        }
        return list;
    }
    
}
