package com.terracetech.tims.hybrid.mail.action;

import java.util.Map;

import org.json.simple.JSONObject;

import com.terracetech.tims.hybrid.common.HybridErrorCode;
import com.terracetech.tims.hybrid.common.action.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class MailSendCheckServiceAction extends BaseAction {

    private MailUserManager mailUserManager = null;

    public void setMailUserManager(MailUserManager mailUserManager) {
        this.mailUserManager = mailUserManager;
    }
    
    @SuppressWarnings("unchecked")
    public String execute() throws Exception {
        int errorCode = HybridErrorCode.SUCCESS;
        JSONObject result = new JSONObject();
        int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
        int mailGroupSeq = Integer.parseInt(user.get(User.MAIL_GROUP_SEQ));
        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        Map<String, String> configMap = null;
        try {
            configMap = mailUserManager.readUserSetting(mailDomainSeq, mailGroupSeq, mailUserSeq);
            result.put("sendCheck", convertSendCheckJson(configMap));
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            errorCode = HybridErrorCode.ERROR;
        }
        
        result.put(HybridErrorCode.CODE_NAME, errorCode);
        ResponseUtil.makeJsonpResponse(request, response, result);
        return null;
    }
    
    @SuppressWarnings("unchecked")
    private JSONObject convertSendCheckJson(Map<String, String> configMap) {
        JSONObject json = new JSONObject();
        json.put("sendCheckApply", "on".equalsIgnoreCase(configMap.get("send_check")));
        json.put("sendEmailCheck", "on".equalsIgnoreCase(configMap.get("send_email_check")));
        json.put("sendAttachCheck", "on".equalsIgnoreCase(configMap.get("send_attach_check")));
        json.put("sendAttachData", ("on".equalsIgnoreCase(configMap.get("send_attach_check"))) ? configMap.get("send_attach_data") : "");
        json.put("sendKeywordCheck", "on".equalsIgnoreCase(configMap.get("send_keyword_check")));
        json.put("sendKeywordData", ("on".equalsIgnoreCase(configMap.get("send_keyword_check"))) ? configMap.get("send_keyword_data") : "");
        
        return json;
    }
}
