package com.terracetech.tims.hybrid.addr.action;

import org.json.simple.JSONObject;

import com.terracetech.tims.hybrid.common.HybridErrorCode;
import com.terracetech.tims.hybrid.common.action.BaseAction;
import com.terracetech.tims.service.tms.impl.ContactService;
import com.terracetech.tims.service.tms.vo.ContactMemberVO;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class AddrMemberDeleteServiceAction extends BaseAction {
    
    private int[] memberSeqList;
    private int bookSeq;
    private int groupSeq;
    
    private ContactService contactService = null;
    
    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }
    
    @SuppressWarnings("unchecked")
    public String execute() throws Exception {
        int errorCode = HybridErrorCode.SUCCESS;
        JSONObject result = new JSONObject();
        int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
        int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        
        ContactMemberVO contactMemberVO = new ContactMemberVO();
        contactMemberVO.setDomainSeq(mailDomainSeq);
        contactMemberVO.setUserSeq(mailUserSeq);
        contactMemberVO.setAddrbookSeq(bookSeq);
        contactMemberVO.setGroupSeq(groupSeq);
        
        int resultCode = 0;
        try {
            resultCode = contactService.delContact(contactMemberVO, memberSeqList);
        }catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            errorCode = HybridErrorCode.ERROR;
        }
        
        errorCode = (resultCode <= 0) ? HybridErrorCode.ERROR : errorCode;
        result.put(HybridErrorCode.CODE_NAME, errorCode);
        String returnName = request.getParameter("addrDelete");
        ResponseUtil.makeAjaxMessage(response, returnName+"("+result+")");
        
        return null;
    }

    public void setMemberSeqList(int[] memberSeqList) {
        this.memberSeqList = memberSeqList;
    }

    public void setBookSeq(int bookSeq) {
        this.bookSeq = bookSeq;
    }

    public void setGroupSeq(int groupSeq) {
        this.groupSeq = groupSeq;
    }
}
