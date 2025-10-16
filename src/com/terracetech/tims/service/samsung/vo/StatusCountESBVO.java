package com.terracetech.tims.service.samsung.vo;

import java.io.Serializable;

public class StatusCountESBVO implements Serializable{

	private static final long serialVersionUID = 20091102L;
	
	/**
	 * 예약발신여부
	 */
	private boolean isScheduled;
	
	/**
	 * 전체
	 */
	private int totalCount;
	
	/**
	 * 배달중 또는 예약중
	 */
	private int deliveryCount;
	
	/**
	 * 미개봉
	 */
	private int unSeenCount;
	
	/**
	 * 개봉
	 */
	private int seenCount;
	
	/**
	 * 기타
	 */
	private int etcCount;

	/**
	 * 취소
	 */
	private int cancelCount;
	
	/**
	 * 메시지 key값
	 *  20070521110840046@hwayoung 
	 */
	private String mtrKey;

	public boolean isScheduled() {
		return isScheduled;
	}

	public void setScheduled(boolean isScheduled) {
		this.isScheduled = isScheduled;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getDeliveryCount() {
		return deliveryCount;
	}

	public void setDeliveryCount(int deliveryCount) {
		this.deliveryCount = deliveryCount;
	}

	public int getUnSeenCount() {
		return unSeenCount;
	}

	public void setUnSeenCount(int unSeenCount) {
		this.unSeenCount = unSeenCount;
	}

	public int getSeenCount() {
		return seenCount;
	}

	public void setSeenCount(int seenCount) {
		this.seenCount = seenCount;
	}

	public int getEtcCount() {
		return etcCount;
	}

	public void setEtcCount(int etcCount) {
		this.etcCount = etcCount;
	}

	public int getCancelCount() {
		return cancelCount;
	}

	public void setCancelCount(int cancelCount) {
		this.cancelCount = cancelCount;
	}

	public String getMtrKey() {
		return mtrKey;
	}

	public void setMtrKey(String mtrKey) {
		this.mtrKey = mtrKey;
	}
}
