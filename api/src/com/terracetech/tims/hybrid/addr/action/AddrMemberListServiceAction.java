package com.terracetech.tims.hybrid.addr.action;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.hybrid.common.HybridErrorCode;
import com.terracetech.tims.hybrid.common.action.BaseAction;
import com.terracetech.tims.service.tms.impl.ContactService;
import com.terracetech.tims.service.tms.vo.ContactBookVO;
import com.terracetech.tims.service.tms.vo.ContactCondVO;
import com.terracetech.tims.service.tms.vo.ContactGroupVO;
import com.terracetech.tims.service.tms.vo.ContactInfoVO;
import com.terracetech.tims.service.tms.vo.ContactMemberVO;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class AddrMemberListServiceAction extends BaseAction {
    
    private int groupSeq = 0;
    private int bookSeq = 0;
    private int page = 0;
    private int pageBase = 0;
    private String keyword = null;
    private String startChar = null;
    private String sortBy = null;
    private String sortDir = null;
    private String addrType = "hybrid";
    
    private ContactService contactService = null;

    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }
    
    @SuppressWarnings("unchecked")
    public String execute() throws Exception {
        int errorCode = HybridErrorCode.SUCCESS;
        JSONObject result = new JSONObject();
        int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
        page = (page == 0) ? 1 : page;
        pageBase = (pageBase == 0) ? 15 : pageBase;
        keyword = (StringUtils.isEmpty(keyword)) ? "" : keyword;
        sortBy = (StringUtils.isEmpty(sortBy)) ? "name" : sortBy;
        sortDir = (StringUtils.isEmpty(sortDir)) ? "asc" : sortDir;

        ContactCondVO contactCondVo = new ContactCondVO();
        contactCondVo.setDomainSeq(mailDomainSeq);
        contactCondVo.setUserEmail(user.get(User.EMAIL));
        contactCondVo.setBookSeq(bookSeq);
        contactCondVo.setGroupSeq(groupSeq);
        contactCondVo.setCurrentPage(page);
        contactCondVo.setMaxResult(pageBase);
        contactCondVo.setStartChar(startChar);
        contactCondVo.setSearchType("all");
        contactCondVo.setKeyWord(keyword);
        contactCondVo.setSortBy(sortBy);
        contactCondVo.setSortDir(sortDir);
        contactCondVo.setAddrType(addrType);
        
        try {
            ContactInfoVO contactInfoVo = contactService.readContactMemberListByIndex(contactCondVo);
            result = convertAddrJson(contactInfoVo);
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            errorCode = HybridErrorCode.ERROR;
        }
        
        result.put(HybridErrorCode.CODE_NAME, errorCode);
        ResponseUtil.makeJsonpResponse(request, response, result);
        
        return null;
    }
    
    @SuppressWarnings("unchecked")
    private JSONObject convertAddrJson(ContactInfoVO contactInfoVO) {
        JSONObject addr = new JSONObject();
        ContactBookVO contactBookVO = contactInfoVO.getBookInfo();
        ContactGroupVO contactGroupVO = contactInfoVO.getGroupInfo();
        int bookSeq = (contactBookVO == null) ? 0 : contactBookVO.getAdrbookSeq();
        int groupSeq = (contactGroupVO == null) ? 0 : contactGroupVO.getGroupSeq();
        addr.put("total", contactInfoVO.getTotalCount());
        addr.put("addrList", convertAddrListJson(groupSeq, contactInfoVO.getMemberList()));
        
        return addr;
    }
    
    @SuppressWarnings("unchecked")
    private JSONArray convertAddrListJson(int groupSeq, ContactMemberVO[] memberList) {
        JSONArray array = new JSONArray();
        JSONObject jObj = null;
        if (memberList != null && memberList.length > 0) {
            for (ContactMemberVO member : memberList) {
                jObj = new JSONObject();
                jObj.put("bookSeq", member.getAddrbookSeq());
                jObj.put("groupSeq", groupSeq);
                jObj.put("memberSeq", member.getMemberSeq());
                jObj.put("memberName", StringUtils.EscapeHTMLTag(member.getMemberName()));
                jObj.put("memberEmail", StringUtils.EscapeHTMLTag(member.getMemberEmail()));
                array.add(jObj);
            }
        }
        return array;
    }

    public void setGroupSeq(int groupSeq) {
        this.groupSeq = groupSeq;
    }

    public void setBookSeq(int bookSeq) {
        this.bookSeq = bookSeq;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPageBase(int pageBase) {
        this.pageBase = pageBase;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setStartChar(String startChar) {
        this.startChar = startChar;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }
    
}
