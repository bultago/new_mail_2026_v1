package com.terracetech.tims.hybrid.mail.action;

import org.json.simple.JSONObject;

import com.terracetech.tims.hybrid.common.HybridErrorCode;
import com.terracetech.tims.hybrid.common.action.BaseAction;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.service.manager.MailServiceManager;
import com.terracetech.tims.service.tms.vo.MailWorkCondVO;
import com.terracetech.tims.service.tms.vo.MailWorkResultVO;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class MailWorkServiceAction extends BaseAction {

    private String workMode = null;
    private String uids = null;
    private String folderNames = null;
    private String targetFolderName = null;
    private String flagType = null;
    private String flagUse = null;
    
    private MailServiceManager mailServiceManager = null;

    public void setMailServiceManager(MailServiceManager mailServiceManager) {
        this.mailServiceManager = mailServiceManager;
    }

    @SuppressWarnings("unchecked")
    public String execute() throws Exception {
        JSONObject result = new JSONObject();
        int errorCode = HybridErrorCode.SUCCESS;
        
        try {
            String[] uidArray = {};
            if (StringUtils.isNotEmpty(uids)) {
                uidArray = uids.split("\\|");
            }
            
            String[] folderNameArray = {};
            if (StringUtils.isNotEmpty(folderNames)) {
                folderNameArray = folderNames.split("\\|");
                if (folderNameArray.length > 0) {
                    for (int i = 0; i < folderNameArray.length; i++) {
                        folderNameArray[i] = TMailUtility.IMAPFolderEncode(folderNameArray[i]);
                    }
                }
            }
            
            boolean isFlagUse = "on".equalsIgnoreCase(flagUse);
            
            MailWorkCondVO mailWorkCondVO = new MailWorkCondVO();
            mailWorkCondVO.setRemoteIp(remoteIp);
            mailWorkCondVO.setLocale(user.get(User.LOCALE));
            mailWorkCondVO.setUserEmail(user.get(User.EMAIL));
            mailWorkCondVO.setWorkMode(workMode);
            mailWorkCondVO.setUid(uidArray);
            mailWorkCondVO.setFolderName(folderNameArray);
            mailWorkCondVO.setTargetFolderName(targetFolderName);
            mailWorkCondVO.setFlagType(flagType);
            mailWorkCondVO.setFlagUse(isFlagUse);
    
            MailWorkResultVO mailWorkResultVO = mailServiceManager.doSimpleMailWork(mailWorkCondVO, user);
            if (mailWorkResultVO.isErrorOccur()) {
               errorCode = HybridErrorCode.ERROR; 
            }
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            errorCode = HybridErrorCode.ERROR;
        }
        
        result.put(HybridErrorCode.CODE_NAME, errorCode);
        String returnName = request.getParameter("mailwork");
        ResponseUtil.makeAjaxMessage(response, returnName+"("+result+")");
        return null;
    }

    public void setWorkMode(String workMode) {
        this.workMode = workMode;
    }

    public void setUids(String uids) {
        this.uids = uids;
    }

    public void setFolderNames(String folderNames) {
        this.folderNames = folderNames;
    }

    public void setTargetFolderName(String targetFolderName) {
        this.targetFolderName = targetFolderName;
    }

    public void setFlagType(String flagType) {
        this.flagType = flagType;
    }

    public void setFlagUse(String flagUse) {
        this.flagUse = flagUse;
    }
    
}
