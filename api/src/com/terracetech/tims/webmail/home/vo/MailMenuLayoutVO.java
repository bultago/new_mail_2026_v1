package com.terracetech.tims.webmail.home.vo;

import java.io.Serializable;

public class MailMenuLayoutVO implements Serializable{
    
	private static final long serialVersionUID = 1L;

	private Integer menuSeq;
	
    private Integer mailDomainSeq;

    private String menuId;
    
    private String menuUrl;

    private String menuName;

    private String menuTarget;

    private Integer orderSeq;

    private String menuApply;
    
	public Integer getMenuSeq() {
		return menuSeq;
	}

	public void setMenuSeq(Integer menuSeq) {
		this.menuSeq = menuSeq;
	}
	
	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public Integer getMailDomainSeq() {
		return mailDomainSeq;
	}

	public void setMailDomainSeq(Integer mailDomainSeq) {
		this.mailDomainSeq = mailDomainSeq;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuTarget() {
		return menuTarget;
	}

	public void setMenuTarget(String menuTarget) {
		this.menuTarget = menuTarget;
	}

	public Integer getOrderSeq() {
		return orderSeq;
	}

	public void setOrderSeq(Integer orderSeq) {
		this.orderSeq = orderSeq;
	}

	public String getMenuApply() {
		return menuApply;
	}

	public void setMenuApply(String menuApply) {
		this.menuApply = menuApply;
	}

    
}