package com.terracetech.tims.webmail.addrbook.ibean;

import org.json.simple.JSONObject;

import com.terracetech.tims.webmail.addrbook.vo.AddressBookDupMemberVO;
import com.terracetech.tims.webmail.util.StringUtils;

public class AddressBookDupCheckMemberBean {
	private final AddressBookDupMemberVO member;

    public AddressBookDupCheckMemberBean(AddressBookDupMemberVO member) {
        super();
        this.member = member;
    }
    
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        if (member == null)
            return json;

        json.put("memberName", getValue(member.getMemberName()));
        json.put("memberEmail", getValue(member.getMemberEmail()));
        json.put("orgCode", getValue(member.getOrgCode()));
        json.put("userSeq", member.getUserSeq());
        json.put("dupCnt", member.getDupCnt());
        json.put("groupSeq", member.getGroupSeq());

        return json;
    }
    private String getValue(String val) {
        if (StringUtils.isEmpty(val))
            return "";

        return val;
    }
}
