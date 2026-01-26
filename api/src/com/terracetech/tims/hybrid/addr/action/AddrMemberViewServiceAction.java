package com.terracetech.tims.hybrid.addr.action;

import org.json.simple.JSONObject;

import com.terracetech.tims.hybrid.common.HybridErrorCode;
import com.terracetech.tims.hybrid.common.action.BaseAction;
import com.terracetech.tims.service.tms.impl.ContactService;
import com.terracetech.tims.service.tms.vo.ContactCondVO;
import com.terracetech.tims.service.tms.vo.ContactMemberVO;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class AddrMemberViewServiceAction extends BaseAction {
    
    private int bookSeq = 0;
    private int memberSeq = 0;
    
    private ContactService contactService = null;
    
    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }
    
    @SuppressWarnings("unchecked")
    public String execute() throws Exception {
        int errorCode = HybridErrorCode.SUCCESS;
        JSONObject result = new JSONObject();
        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        
        ContactCondVO contactCondVo = new ContactCondVO();
        contactCondVo.setUserEmail(user.get(User.EMAIL));
        contactCondVo.setUserSeq(mailUserSeq);
        contactCondVo.setBookSeq(bookSeq);
        contactCondVo.setMemberSeq(memberSeq);
        
        try {
            ContactMemberVO contactMemberVO = contactService.readContactMember(contactCondVo);
            JSONObject memberInfo = null;
            if (contactMemberVO != null) {
                memberInfo = contactMemberVO.toJson();
            }
            result.put("memberInfo", memberInfo);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            errorCode = HybridErrorCode.ERROR;
        }
        
        result.put(HybridErrorCode.CODE_NAME, errorCode);
        ResponseUtil.makeJsonpResponse(request, response, result);

        return null;
    }
      
    public void setBookSeq(int bookSeq) {
        this.bookSeq = bookSeq;
    }

    public void setMemberSeq(int memberSeq) {
        this.memberSeq = memberSeq;
    }
}
