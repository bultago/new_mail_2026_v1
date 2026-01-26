package com.terracetech.tims.hybrid.mail.action;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.hybrid.common.HybridErrorCode;
import com.terracetech.tims.hybrid.common.action.BaseAction;
import com.terracetech.tims.service.manager.MailServiceManager;
import com.terracetech.tims.service.tms.vo.ListCondVO;
import com.terracetech.tims.service.tms.vo.MdnContentVO;
import com.terracetech.tims.service.tms.vo.MdnInfoVO;
import com.terracetech.tims.service.tms.vo.MdnListVO;
import com.terracetech.tims.service.tms.vo.MdnRcptContentVO;
import com.terracetech.tims.service.tms.vo.MdnRecallVO;
import com.terracetech.tims.service.tms.vo.ReadCondVO;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class MailMdnServiceAction extends BaseAction {

	private String folder = null;
    private long uid = 0;
    private String mid = null;
    private String recallEmails = null;
    private int page;
    private int pageBase;
    private String keyWord;
    
    private MailServiceManager mailServiceManager = null;

    public void setMailServiceManager(MailServiceManager mailServiceManager) {
        this.mailServiceManager = mailServiceManager;
    }
    
    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public void setRecallEmails(String recallEmails) {
        this.recallEmails = recallEmails;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPageBase(int pageBase) {
        this.pageBase = pageBase;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void setFolder(String folder) {
		this.folder = folder;
	}

	@SuppressWarnings("unchecked")
    public String executeMdnList() throws Exception {
        JSONObject result = new JSONObject();
        int errorCode = HybridErrorCode.SUCCESS;
        page = (page == 0) ? 1 : page;
        pageBase = (pageBase == 0) ? 15 : pageBase;
        
        ListCondVO listVO = new ListCondVO();
        listVO.setRemoteIp(remoteIp);
        listVO.setPageBase(pageBase);
        listVO.setPage(page);
        listVO.setKeyWord(keyWord);
        listVO.setLocale(user.get(User.LOCALE));
        listVO.setEmail(user.get(User.EMAIL));

        try {
            MdnListVO mdnListInfo = mailServiceManager.doSimpleMdnList(listVO, user);
            result = convertMdnInfo(mdnListInfo);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            errorCode = HybridErrorCode.ERROR;
        }
        result.put(HybridErrorCode.CODE_NAME, errorCode);
        ResponseUtil.makeJsonpResponse(request, response, result); 

        return null;
    }
    
    @SuppressWarnings("unchecked")
    public String executeMdnView() throws Exception {
        JSONObject result = new JSONObject();
        int errorCode = HybridErrorCode.SUCCESS;
        page = (page == 0) ? 1 : page;
        pageBase = (pageBase == 0) ? 15 : pageBase;
        
        ReadCondVO readVO = new ReadCondVO();
        readVO.setRemoteIp(remoteIp);
        readVO.setUid(uid);
        readVO.setPage(page);
        readVO.setPageBase(pageBase);
        readVO.setLocale(user.get(User.LOCALE));
        readVO.setEmail(user.get(User.EMAIL));

        try {
            MdnContentVO mdnContent = mailServiceManager.doSimpleMdnView(readVO, user);
            result.put("mdnView", convertMdnView(mdnContent));
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            
        }
        result.put(HybridErrorCode.CODE_NAME, errorCode);
        ResponseUtil.makeJsonpResponse(request, response, result); 

        return null;
    }
    
    @SuppressWarnings("unchecked")
    public String executeMdnRecall() throws Exception {
        JSONObject result = new JSONObject();
        int errorCode = HybridErrorCode.SUCCESS;
        MdnRecallVO mdnRecallVO = new MdnRecallVO();
        mdnRecallVO.setRemoteIp(remoteIp);
        mdnRecallVO.setLocale(user.get(User.LOCALE));
        mdnRecallVO.setEmail(user.get(User.EMAIL));
        mdnRecallVO.setMid(mid);
        mdnRecallVO.setRecallEmails(recallEmails);
        try {
            mailServiceManager.doSimpleMdnRecall(mdnRecallVO, user);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            errorCode = HybridErrorCode.ERROR;
        }
        result.put(HybridErrorCode.CODE_NAME, errorCode);
        String returnName = request.getParameter("mdnrecall");
        ResponseUtil.makeAjaxMessage(response, returnName+"("+result+")");
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public String executeMdnSendMail() throws Exception {
        JSONObject result = new JSONObject();
        int errorCode = HybridErrorCode.SUCCESS;
        ReadCondVO readVO = new ReadCondVO();
        readVO.setFolder(folder);
        readVO.setUid(uid);
        readVO.setRemoteIp(remoteIp);
        readVO.setLocale(user.get(User.LOCALE));
        readVO.setEmail(user.get(User.EMAIL));
        
        try {
        mailServiceManager.doSimpleMdnSendMail(readVO, user);
        } catch (Exception e) {
                LogManager.writeErr(this, e.getMessage(), e);
            errorCode = HybridErrorCode.ERROR;
                }
        result.put(HybridErrorCode.CODE_NAME, errorCode);
        String returnName = request.getParameter("mdnsend");
        ResponseUtil.makeAjaxMessage(response, returnName+"("+result+")");
        
        return null;
    }
    
    @SuppressWarnings("unchecked")
    private JSONObject convertMdnInfo(MdnListVO mdnListVO) {
        JSONObject result = new JSONObject();
        if (mdnListVO != null) {
            result.put("total", mdnListVO.getTotalCnt());
            result.put("mdnList", convertMdnList(mdnListVO.getMdnList()));
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    private JSONArray convertMdnList(MdnInfoVO[] mdnInfoVOs) {
        JSONArray mdnList = new JSONArray();
        if (mdnInfoVOs != null && mdnInfoVOs.length > 0) {
            JSONObject mdnJson = null;
            for (MdnInfoVO mdnInfo : mdnInfoVOs) {
                mdnJson = new JSONObject();
                mdnJson.put("uid", mdnInfo.getUid());
                mdnJson.put("subject", StringUtils.EscapeHTMLTag(mdnInfo.getSubject()));
                mdnJson.put("to", StringUtils.EscapeHTMLTag(mdnInfo.getTo()));
                mdnJson.put("date", StringUtils.EscapeHTMLTag(mdnInfo.getDate()));
                mdnJson.put("code", StringUtils.EscapeHTMLTag(mdnInfo.getCode()));
                mdnJson.put("rdate", StringUtils.EscapeHTMLTag(mdnInfo.getRdate()));
                mdnJson.put("readCount", mdnInfo.getReadCnt());
                mdnJson.put("mdnCount", mdnInfo.getMdnCnt());
                mdnList.add(mdnJson);
            }
        }
        return mdnList;
    }
    
    @SuppressWarnings("unchecked")
    private JSONObject convertMdnView(MdnContentVO mdnContent) {
        JSONObject result = new JSONObject();
        if (mdnContent != null) {
            result.put("uid", uid);
            result.put("messageId", mdnContent.getMessageID());
            result.put("subject", StringUtils.EscapeHTMLTag(mdnContent.getMessageTitle()));
            result.put("sendDate", StringUtils.EscapeHTMLTag(mdnContent.getSendDate()));
            result.put("rcptList", convertMdnViewRcpt(mdnContent.getRcptVos()));
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    private JSONArray convertMdnViewRcpt(MdnRcptContentVO[] mdnRcptContentVOs) {
        JSONArray mdnList = new JSONArray();
        if (mdnRcptContentVOs != null && mdnRcptContentVOs.length > 0) {
            JSONObject mdnJson = null;
            for (MdnRcptContentVO mdnRcptContent : mdnRcptContentVOs) {
                mdnJson = new JSONObject();
                mdnJson.put("isLocal", mdnRcptContent.isLocalDomain());
                mdnJson.put("code", StringUtils.EscapeHTMLTag(mdnRcptContent.getCode()));
                mdnJson.put("address", StringUtils.EscapeHTMLTag(mdnRcptContent.getAddress()));
                mdnJson.put("printAddress", StringUtils.EscapeHTMLTag(mdnRcptContent.getPrintAddress()));
                mdnJson.put("message", StringUtils.EscapeHTMLTag(mdnRcptContent.getMessage()));
                mdnJson.put("status", StringUtils.EscapeHTMLTag(mdnRcptContent.getStatus()));
                mdnList.add(mdnJson);
            }
        }
        return mdnList;
    }
}
