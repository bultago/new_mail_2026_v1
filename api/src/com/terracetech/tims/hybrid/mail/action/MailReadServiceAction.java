package com.terracetech.tims.hybrid.mail.action;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.hybrid.common.HybridErrorCode;
import com.terracetech.tims.hybrid.common.action.BaseAction;
import com.terracetech.tims.service.manager.IMailServiceManager;
import com.terracetech.tims.service.tms.vo.AttachFileVO;
import com.terracetech.tims.service.tms.vo.EmailAddressVO;
import com.terracetech.tims.service.tms.vo.ReadCondVO;
import com.terracetech.tims.service.tms.vo.ViewContentVO;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class MailReadServiceAction extends BaseAction {

    private String uid = null;
    private String folderName = null;
    private String sortBy = null;
    private String sortDir = null;
    private String flag = null;
    private String adv = null;
    private String category = null;
    private String keyWord = null;
    private String faddr = null;
    private String taddr = null;
    
    private IMailServiceManager mailServiceManager = null;
    
    public void setMailServiceManager(IMailServiceManager mailServiceManager) {
        this.mailServiceManager = mailServiceManager;
    }
    
    @SuppressWarnings({ "unchecked"})
    public String execute() throws Exception {
        int errorCode = HybridErrorCode.SUCCESS;
        JSONObject result = new JSONObject();
        char flagType = (flag != null && flag.length() > 0) ? flag.charAt(0) : 'x';
        String hostStr = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String attachesDir = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
        boolean isAdSearch = ("on".equals(adv)) ? true : false;
        
        ReadCondVO readVO = new ReadCondVO();
        readVO.setRemoteIp(remoteIp);
        readVO.setEmail(user.get(User.EMAIL));
        readVO.setUid(Long.parseLong(uid));
        readVO.setFolder(folderName);
        readVO.setViewImage(false);
        readVO.setLocale(user.get(User.LOCALE));
        readVO.setLocalURL(hostStr);
        readVO.setAttachDir(attachesDir);
        readVO.setSortBy(sortBy);
        readVO.setSortDir(sortDir);
        readVO.setAdvancedSearch(isAdSearch);
        readVO.setCategory(category);
        readVO.setFromEmailPattern(faddr);
        readVO.setToEmailPattern(taddr);
        
        readVO.setKeyWord(keyWord);
        readVO.setFlagType(flagType);
        
        try {
            ViewContentVO viewContents = mailServiceManager.doSimpleMailRead(readVO, user);
            result.put("readInfo", convertMailListInfo(viewContents));
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            errorCode = HybridErrorCode.ERROR;
        }
        result.put(HybridErrorCode.CODE_NAME, errorCode);
        ResponseUtil.makeJsonpResponse(request, response, result);
        
        return null;
    }
    
    @SuppressWarnings("unchecked")
    private JSONObject convertMailListInfo(ViewContentVO viewContentVO) {
        JSONObject result = new JSONObject();
        if (viewContentVO != null) {
            result.put("uid", viewContentVO.getUid());
            result.put("preUid", viewContentVO.getPreUid());
            result.put("nextUid", viewContentVO.getNextUid());
            result.put("folderName", viewContentVO.getFolderEncName());
            result.put("subject", StringUtils.EscapeHTMLTag(viewContentVO.getSubject()));
            result.put("date", StringUtils.EscapeHTMLTag(viewContentVO.getDate()));
            result.put("from", StringUtils.EscapeHTMLTag(viewContentVO.getFrom().getEmailAddress()));
            result.put("toList", makeAddrList(viewContentVO.getTos()));
            result.put("ccList", makeAddrList(viewContentVO.getCcs()));
            result.put("attachCount", viewContentVO.getAttachsCnt());
            result.put("attachList", makeAttachList(viewContentVO.getAttachs()));
            result.put("mdnCheck", viewContentVO.getMdnCheck());
            
            String content = viewContentVO.getContents();
            content = StringUtils.removeStyleTag(content);
            result.put("content", content);
        }
        return result;
    }
    
    private String makeAddrList(EmailAddressVO[] emailAddressVOs) {
        StringBuffer sb = new StringBuffer();
        if (emailAddressVOs != null && emailAddressVOs.length > 0) {
            for (EmailAddressVO emailAddressVO : emailAddressVOs) {
                sb.append(emailAddressVO.getEmailAddress()).append(",");
            }
        }
        return StringUtils.EscapeHTMLTag(sb.toString());
    }
    
    @SuppressWarnings("unchecked")
    private JSONArray makeAttachList(AttachFileVO[] attachFileVOs) {
        JSONArray result = new JSONArray();
        JSONObject attach = null;
        if (attachFileVOs != null && attachFileVOs.length > 0) {
            for (AttachFileVO attachFileVO : attachFileVOs) {
                attach = new JSONObject();
                attach.put("name", StringUtils.EscapeHTMLTag(attachFileVO.getName()));
                attach.put("size", attachFileVO.getSize());
                attach.put("depth", attachFileVO.getDepth());
                result.add(attach);
            }
        }
        return result;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void setAdv(String adv) {
        this.adv = adv;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setFaddr(String faddr) {
        this.faddr = faddr;
    }

    public void setTaddr(String taddr) {
        this.taddr = taddr;
    }
    
}
