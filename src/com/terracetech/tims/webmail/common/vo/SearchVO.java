/**
 * SearchVO.java 2009. 2. 5.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.common.vo;

/**
 * <p><strong>SearchVO.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author ysko
 * @since Tims7
 * @version 7.0 
 */
public class SearchVO {

	private int seqNumber;
	
	private String seqKey, searchKey, compareValue, sortKey, addedCompareValue;
	
	private boolean preLikeSymbolAppended, postLikeSymbolAppended, descending;
	
	private int skipResult;
	private int maxResult;

	public int getSeqNumber() {
		return seqNumber;
	}

	public void setSeqNumber(int seqNumber) {
		this.seqNumber = seqNumber;
	}

	public String getSeqKey() {
		return seqKey;
	}

	public void setSeqKey(String seqKey) {
		this.seqKey = seqKey;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public String getCompareValue() {
		String pre = preLikeSymbolAppended ? "%" : "";
		String post = postLikeSymbolAppended ? "%" : "";
		return compareValue != null ?  pre + compareValue + post : null;
	}

	public void setCompareValue(String compareValue) {
		this.compareValue = compareValue;
	}

	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

	public boolean isPreLikeSymbolAppended() {
		return preLikeSymbolAppended;
	}

	public void setPreLikeSymbolAppended(boolean preLikeSymbolAppended) {
		this.preLikeSymbolAppended = preLikeSymbolAppended;
	}

	public boolean isPostLikeSymbolAppended() {
		return postLikeSymbolAppended;
	}

	public void setPostLikeSymbolAppended(boolean postLikeSymbolAppended) {
		this.postLikeSymbolAppended = postLikeSymbolAppended;
	}

	public boolean isDescending() {
		return descending;
	}

	public void setDescending(boolean descending) {
		this.descending = descending;
	}

	public void setAllLikeSymbolUsed (boolean allLikeSymbolUsed) {
		this.preLikeSymbolAppended = this.postLikeSymbolAppended = allLikeSymbolUsed;
	}
	
	public boolean isLikeUsed () {
		return this.preLikeSymbolAppended && this.postLikeSymbolAppended;
	}
	
	public String getSqlCompareValueStr () {
		String pre = preLikeSymbolAppended ? "%" : "";
		String post = postLikeSymbolAppended ? "%" : "";
		return pre + compareValue + post;
	}
	
	public String getAddedCompareValue() {
		return addedCompareValue;
	}

	public void setAddedCompareValue(String addedCompareValue) {
		this.addedCompareValue = addedCompareValue;
	}
	
	public int getSkipResult() {
		return skipResult;
	}

	public void setSkipResult(int skipResult) {
		this.skipResult = skipResult;
	}

	public int getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}

	@Override
	public String toString() {
		StringBuffer tmpStr = new StringBuffer();
		tmpStr.append("\n");
		tmpStr.append("seqNumber = [").append(seqNumber).append("]");
		tmpStr.append("\n");
		tmpStr.append("seqKey = [").append(seqKey).append("]");
		tmpStr.append("\n");
		tmpStr.append("searchKey = [").append(searchKey).append("]");
		tmpStr.append("\n");
		tmpStr.append("compareValue = [").append(compareValue).append("]");
		tmpStr.append("\n");
		tmpStr.append("sortKey = [").append(sortKey).append("]");
		tmpStr.append("\n");
		tmpStr.append("preLikeSymbolAppended = [").append(preLikeSymbolAppended).append("]");
		tmpStr.append("\n");
		tmpStr.append("postLikeSymbolAppended = [").append(postLikeSymbolAppended).append("]");
		tmpStr.append("\n");
		tmpStr.append("descending = [").append(descending).append("]");
		tmpStr.append("\n");
	
		return tmpStr.toString();
	}
}