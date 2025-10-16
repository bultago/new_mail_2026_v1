package com.terracetech.tims.hybrid.addr.action;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.hybrid.common.HybridErrorCode;
import com.terracetech.tims.hybrid.common.action.BaseAction;
import com.terracetech.tims.service.tms.impl.ContactService;
import com.terracetech.tims.service.tms.vo.ContactBookVO;
import com.terracetech.tims.service.tms.vo.ContactCondVO;
import com.terracetech.tims.service.tms.vo.ContactGroupVO;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ResponseUtil;

public class AddrBookGroupServiceAction extends BaseAction {
    
    private int bookSeq = 0;
    private String bookType = null;
    
    private ContactService contactService = null;
    
    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }

    @SuppressWarnings("unchecked")
    public String executeBook() throws Exception {
        int errorCode = HybridErrorCode.SUCCESS;
        JSONObject result = new JSONObject();
        int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
        
        ContactCondVO contactCondVo = new ContactCondVO();
        contactCondVo.setDomainSeq(mailDomainSeq);
        contactCondVo.setUserEmail(user.get(User.EMAIL));
        try {
            ContactBookVO[] contactBookVOs = contactService.readContactBookList(contactCondVo);
            result.put("bookInfo", convertBookJson(bookType, contactBookVOs));
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            errorCode = HybridErrorCode.ERROR;
        }
        
        result.put(HybridErrorCode.CODE_NAME, errorCode);
        ResponseUtil.makeJsonpResponse(request, response, result);
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public String executeGroup() throws Exception {
        int errorCode = HybridErrorCode.SUCCESS;
        JSONObject result = new JSONObject();
        int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
        
        ContactCondVO contactCondVo = new ContactCondVO();
        contactCondVo.setDomainSeq(mailDomainSeq);
        contactCondVo.setUserEmail(user.get(User.EMAIL));
        contactCondVo.setBookSeq(bookSeq);
        
        try {
            ContactGroupVO[] contactGroupVOs = contactService.readContactGroupList(contactCondVo);
            result.put("groupInfo", convertGroupJson(bookSeq, contactGroupVOs));
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            errorCode = HybridErrorCode.ERROR;
        }
        
        result.put(HybridErrorCode.CODE_NAME, errorCode);
        ResponseUtil.makeJsonpResponse(request, response, result);

        return null;
    }
    
    @SuppressWarnings("unchecked")
    private Object convertGroupJson(int bookSeq, ContactGroupVO[] contactGroupVOs) {
        JSONArray array = new JSONArray();
        JSONObject book = null;
        book = new JSONObject();
        book.put("bookSeq", bookSeq);
        book.put("groupSeq", 0);
        book.put("groupName", "all");
        array.add(book);
        if (contactGroupVOs != null && contactGroupVOs.length > 0) {
            for (ContactGroupVO contactGroupVO : contactGroupVOs) {
                book = new JSONObject();
                book.put("bookSeq", bookSeq);
                book.put("groupSeq", contactGroupVO.getGroupSeq());
                book.put("groupName", contactGroupVO.getGroupName());
                array.add(book);
            }
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    private JSONArray convertBookJson(String bookType, ContactBookVO[] contactBookVOs) {
        JSONArray array = new JSONArray();
        JSONObject book = null;
        if ("private".equalsIgnoreCase(bookType)) {
            book = new JSONObject();
            book.put("bookSeq", 0);
            book.put("bookName", "private");
            array.add(book);
        }
        if (contactBookVOs != null && contactBookVOs.length > 0) {
            for (ContactBookVO contactBookVO : contactBookVOs) {
                book = new JSONObject();
                book.put("bookSeq", contactBookVO.getAdrbookSeq());
                book.put("bookName", contactBookVO.getBookName());
                array.add(book);
            }
        }
        return array;
    }

    public void setBookSeq(int bookSeq) {
        this.bookSeq = bookSeq;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }
    
}
