package com.terracetech.tims.hybrid.addr.action;

import org.json.simple.JSONObject;

import com.terracetech.tims.hybrid.common.HybridErrorCode;
import com.terracetech.tims.hybrid.common.action.BaseAction;
import com.terracetech.tims.service.tms.impl.ContactService;
import com.terracetech.tims.service.tms.vo.ContactCondVO;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class AddrMemberMoveServiceAction extends BaseAction {

    private int bookSeq = 0;
    private int[] memberSeqList;
    private int sourceGroupSeq = 0;
    private int targetGroupSeq = 0;
    
    private ContactService contactService = null;
    
    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }
    
    @SuppressWarnings("unchecked")
    public String execute() throws Exception {
        int errorCode = HybridErrorCode.SUCCESS;
        JSONObject result = new JSONObject();
        
        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        ContactCondVO contactCondVO = new ContactCondVO();
        contactCondVO.setUserSeq(mailUserSeq);
        contactCondVO.setUserEmail(user.get(User.EMAIL));
        contactCondVO.setBookSeq(bookSeq);
        
        try{
            contactService.moveContact(contactCondVO, memberSeqList, sourceGroupSeq, targetGroupSeq);
        }catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            errorCode = HybridErrorCode.ERROR;
        }

        result.put(HybridErrorCode.CODE_NAME, errorCode);
        String returnName = request.getParameter("addrMove");
        ResponseUtil.makeAjaxMessage(response, returnName+"("+result+")");
        return null;
    }

    public void setBookSeq(int bookSeq) {
        this.bookSeq = bookSeq;
    }

    public void setMemberSeqList(int[] memberSeqList) {
        this.memberSeqList = memberSeqList;
    }

    public void setSourceGroupSeq(int sourceGroupSeq) {
        this.sourceGroupSeq = sourceGroupSeq;
    }

    public void setTargetGroupSeq(int targetGroupSeq) {
        this.targetGroupSeq = targetGroupSeq;
    }
    
}
