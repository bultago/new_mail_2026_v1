package com.terracetech.tims.hybrid.mail.action;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.hybrid.common.HybridErrorCode;
import com.terracetech.tims.hybrid.common.action.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SignManager;
import com.terracetech.tims.webmail.setting.vo.SignDataVO;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class MailSignListServiceAction extends BaseAction {
    
    private SignManager signManager = null;
    
    public void setSignManager(SignManager signManager) {
        this.signManager = signManager;
    }
    
    @SuppressWarnings("unchecked")
    public String execute() throws Exception {
        int errorCode = HybridErrorCode.SUCCESS;
        JSONObject result = new JSONObject();
        try {
            List<SignDataVO> signList = signManager.getSignSimpleDataList(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
            result.put("signList", convertSignList(signList));
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            errorCode = HybridErrorCode.ERROR;
        }
        result.put(HybridErrorCode.CODE_NAME, errorCode);
        ResponseUtil.makeJsonpResponse(request, response, result);
        return null;
    }
    
    @SuppressWarnings("unchecked")
    private JSONArray convertSignList(List<SignDataVO> signList) {
        JSONArray array = new JSONArray();
        JSONObject signJson = new JSONObject();
        signJson.put("signSeq", 0);
        signJson.put("defaultSign", "F");
        signJson.put("signName", "none");
        array.add(signJson);
        
        if (signList != null) {
            for (SignDataVO signDataVO : signList) {
                signJson = new JSONObject();
                signJson.put("signSeq", signDataVO.getSignSeq());
                signJson.put("defaultSign", signDataVO.getDefaultSign());
                signJson.put("signName", StringUtils.EscapeHTMLTag(signDataVO.getSignName()));
                array.add(signJson);
            }
        }
        return array;
    }
}
