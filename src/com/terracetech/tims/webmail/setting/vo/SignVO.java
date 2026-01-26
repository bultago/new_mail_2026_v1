package com.terracetech.tims.webmail.setting.vo;

import java.io.Serializable;

public class SignVO implements Serializable {

	private static final long serialVersionUID = 1L;
    
    private int mailUserSeq;

    private String signApply;
    
    private String signLocation;
    
    private SignDataVO[] signDataVos;

    public Integer getMailUserSeq() {
        return mailUserSeq;
    }

    public void setMailUserSeq(int mailUserSeq) {
        this.mailUserSeq = mailUserSeq;
    }

    public String getSignApply() {
        return signApply;
    }

    public void setSignApply(String signApply) {
        this.signApply = signApply;
    }

	public String getSignLocation() {
		return signLocation;
	}

	public void setSignLocation(String signLocation) {
		this.signLocation = signLocation;
	}

	public SignDataVO[] getSignDataVos() {
		return signDataVos;
	}

	public void setSignDataVos(SignDataVO[] signDataVos) {
		this.signDataVos = signDataVos;
	}
    
}