package com.terracetech.tims.hybrid.mail.action;

import org.json.simple.JSONObject;

import com.terracetech.tims.hybrid.common.HybridErrorCode;
import com.terracetech.tims.hybrid.common.action.BaseAction;
import com.terracetech.tims.service.manager.MailServiceManager;
import com.terracetech.tims.service.tms.vo.WriteCondVO;
import com.terracetech.tims.service.tms.vo.WriteResultVO;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class MailWriteServiceAction extends BaseAction {

    private String wtype = null;
    private String folderName = null;
    private String uid = null;
    private String to = null;
    private String cc = null;
    private String bcc = null;
    private String subject = null;
    private String content = null;
    private String fwmode = null;

    private MailServiceManager mailServiceManager = null;

    public void setMailServiceManager(MailServiceManager mailServiceManager) {
        this.mailServiceManager = mailServiceManager;
    }

    public void setWtype(String wtype) {
        this.wtype = wtype;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFwmode(String fwmode) {
        this.fwmode = fwmode;
    }

    @SuppressWarnings("unchecked")
    public String execute() throws Exception {
        int errorCode = HybridErrorCode.SUCCESS;
        JSONObject result = new JSONObject();
        
        WriteCondVO writeVO = new WriteCondVO();
        writeVO.setRemoteIp(remoteIp);
        writeVO.setUserEmail(user.get(User.EMAIL));
        writeVO.setLocale(user.get(User.LOCALE));
        writeVO.setMobileMode(true);
        writeVO.setWriteType(wtype);
        writeVO.setUids(new String[] { uid });
        writeVO.setFolderName(folderName);
        writeVO.setReqTo(to);
        writeVO.setReqCc(cc);
        writeVO.setReqBcc(bcc);
        writeVO.setReqSubject(subject);
        writeVO.setReqContent(content);
        writeVO.setForwardingMode(fwmode);
        
        try {
        WriteResultVO writeResultVO = mailServiceManager.doSimpleMailWrite(writeVO, user);
        JSONObject writeInfo = convertWriteJson(writeResultVO);
        result.put("writeInfo", writeInfo);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            errorCode = HybridErrorCode.ERROR;
        }

        result.put(HybridErrorCode.CODE_NAME, errorCode);
        ResponseUtil.makeJsonpResponse(request, response, result);
        return null;
    }
    
    @SuppressWarnings("unchecked")
    private JSONObject convertWriteJson(WriteResultVO writeResultVO) throws Exception {
        JSONObject result = new JSONObject();
        
        if (writeResultVO != null) {
            result.put("wtype", writeResultVO.getWriteType());
            result.put("encode", "utf-8");
            result.put("savesent", "on");
            result.put("senderEmail", StringUtils.EscapeHTMLTag(writeResultVO.getSenderEmail()));
            result.put("senderName", StringUtils.EscapeHTMLTag(writeResultVO.getSenderName()));
            result.put("editMode", "text");
            result.put("uid", writeResultVO.getUidsValue());
            result.put("draftMid", writeResultVO.getDraftMsgId());
            result.put("sendFlag", writeResultVO.getSendFlag());
            result.put("folder", StringUtils.EscapeHTMLTag(writeResultVO.getFolderName()));
            result.put("receiveNoti", writeResultVO.getReceiveNoti());
            result.put("signAttach", writeResultVO.getSignAttach());
            result.put("to", StringUtils.EscapeHTMLTag(writeResultVO.getTo()));
            result.put("cc", StringUtils.EscapeHTMLTag(writeResultVO.getCc()));
            result.put("bcc", StringUtils.EscapeHTMLTag(writeResultVO.getBcc()));
            result.put("subject", StringUtils.EscapeHTMLTag(writeResultVO.getSubject()));
            result.put("sendFlag", writeResultVO.getSendFlag());
            result.put("fwmode", writeResultVO.getForwardingMode());
            result.put("content", StringUtils.EscapeHTMLTag(writeResultVO.getTextNormalContent()));
        }
        return result;
    }
}
