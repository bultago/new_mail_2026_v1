package com.terracetech.tims.hybrid.mail.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.hybrid.common.HybridErrorCode;
import com.terracetech.tims.hybrid.common.action.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class MailSearchEmailServiceAction extends BaseAction {
    private static final int MAX_IN_COUNT = 500;
    private String keyword = null;
    private String[] emailArray = null;
    
    private MailManager mailManager = null;

    public void setMailManager(MailManager mailManager) {
        this.mailManager = mailManager;
    }
    
    @SuppressWarnings("unchecked")
    public String execute() throws Exception {
        JSONObject result = new JSONObject();
        int errorCode = HybridErrorCode.SUCCESS;
        int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        int groupSeq = Integer.parseInt(user.get(User.MAIL_GROUP_SEQ));
        int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
        keyword = (keyword != null) ? StringUtils.EscapeSqlChar(keyword) : "";
        
        JSONArray addrList = new JSONArray();
        
        try {
            MailAddressBean[] alllastRcpts = mailManager.getUserMailAddressList(userSeq, domainSeq,
                    user.get(User.LOCALE), 15, keyword, true, false);

            if (alllastRcpts.length > 0) {
                for (MailAddressBean mailAddressBean : alllastRcpts) {
                    addrList.add(StringUtils.EscapeHTMLTag(mailAddressBean.getAddress()));
                }
            }
            result.put("addrList", addrList);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            errorCode = HybridErrorCode.ERROR;
        }

        result.put(HybridErrorCode.CODE_NAME, errorCode);
        String returnName = request.getParameter("autoAddr");
        ResponseUtil.makeAjaxMessage(response, returnName+"("+result+")");
        
        return null;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setEmailArray(String[] emailArray) {
        this.emailArray = emailArray;
    }
}
