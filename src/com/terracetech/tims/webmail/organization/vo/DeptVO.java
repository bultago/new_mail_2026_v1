/**
 * DeptVO.java 2008. 11. 27.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.organization.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * <strong>DeptVO.java</strong> Class Description
 * </p>
 * <p>
 * ÁÖ¿ä¼³¸í
 * </p>
 * <ul>
 * <li></li>
 * </ul>
 * 
 * @author jpjung
 * @since Tims7
 * @version 7.0
 */
public class DeptVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer mailDomainSeq;

	private String orgCode;

	private String orgName;
	
	private String orgEnName;
	
	private String orgJpName;

	private Integer orgSortOrder;

	private String orgUpcode;

	private String orgFullcode;

	private String representativeEmail;
	
	private List<DeptVO> children = null;
	
	private DeptVO parent = null;
	
	public DeptVO() {
		children = new ArrayList<DeptVO>();
	}

	public Integer getMailDomainSeq() {
		return mailDomainSeq;
	}

	public void setMailDomainSeq(Integer mailDomainSeq) {
		this.mailDomainSeq = mailDomainSeq;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgEnName() {
		return orgEnName;
	}

	public void setOrgEnName(String orgEnName) {
		this.orgEnName = orgEnName;
	}

	public String getOrgJpName() {
		return orgJpName;
	}

	public void setOrgJpName(String orgJpName) {
		this.orgJpName = orgJpName;
	}

	public Integer getOrgSortOrder() {
		return orgSortOrder;
	}

	public void setOrgSortOrder(Integer orgSortOrder) {
		this.orgSortOrder = orgSortOrder;
	}

	public String getOrgUpcode() {
		return orgUpcode;
	}

	public void setOrgUpcode(String orgUpcode) {
		this.orgUpcode = orgUpcode;
	}

	public String getOrgFullcode() {
		return orgFullcode;
	}

	public void setOrgFullcode(String orgFullcode) {
		this.orgFullcode = orgFullcode;
	}

	public String getRepresentativeEmail() {
		return representativeEmail;
	}

	public void setRepresentativeEmail(String representativeEmail) {
		this.representativeEmail = representativeEmail;
	}
	
	public DeptVO getParent() {
		return parent;
	}

	public void setParent(DeptVO parent) {
		this.parent = parent;
	}

	public void addChild(DeptVO dept)
	{
		this.children.add(dept);
		dept.setParent(this);
	}

	public List<DeptVO> getChildren() {
		return children;
	}
	
}
