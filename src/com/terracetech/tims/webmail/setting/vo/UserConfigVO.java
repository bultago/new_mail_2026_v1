/**
 * UserConfigVO.java 2008  2008-09-26
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.setting.vo;

/**
 * <p><strong>UserConfigVO.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li>사용자 환경 설정 정보를 가져오기 위한 VO. 메일 서비스를 사용하기 위한 설정 정보를 표현.</li>
 * <li>페이지 갯수, 언어 송신이름등의 서비스 사용시의 설정 정보포함.</li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class UserConfigVO {
	
	/**
	 * <p>페이징 단위 갯수</p>
	 */
	private int pageLineCnt = 0;
	/**
	 * <p>메일 송신시 보내는 이름</p>
	 */
	private String senderName = null;
	/**
	 * <p>수신확인 함 저장</p>
	 */
	private String saveSendBox = null;
	/**
	 * <p>수신확인 기능 설정</p>
	 * <p>속성 : on/off</p>
	 */
	private String receiveNoti = null;
	/**
	 * <p>Vcard 사용여부</p>
	 */
	private String vcardAttach = null;
	/**
	 * <p>사용자 페이지 언어 설정</p>
	 */
	private String userLocale = null;
	/**
	 * <p>쓰기 모드 설정</p>
	 * <p>속성 : text/html</p>
	 */
	private String writeMode = null;
	/**
	 * <p>메일 작성시 언어셋</p>
	 */
	private String charSet = null;
	/**
	 * <p>메일 읽기시 이미지 보기 설정</p>
	 * <p>속성 : on/off</p>
	 */
	private String hiddenImg = null;
	/**
	 * <p>서명 첨부 여부</p>
	 * <p>속성 : on/off</p>
	 */
	private String signAttach = null;
	/**
	 * <p>로그인후 이동 페이지 설정</p>
	 */
	private String afterLogin = null;
	/**
	 * <p>메일 읽기시 태그 보기 설정</p>
	 * <p>속성 : on/off</p>
	 */
	private String hiddenTag = null;
	/**
	 * <p>새메일 알림 주기 설정</p>
	 */
	private int notiInterval = 0;
	/**
	 * <p>사용자 스킨 설정. Left 메뉴만 적용</p>
	 */
	private String userSkin = null;
	/**
	 * <p>전달 모드 설정.</p>
	 */
	private String forwardingMode = null;
	/**
	 * <p>송진자 Email  주소 설정.</p>
	 */
	private String senderEmail = null;
	/**
	 * @return pageLineCnt 값 반환
	 */
	public int getPageLineCnt() {
		return pageLineCnt;
	}
	/**
	 * @param pageLineCnt 파라미터를 pageLineCnt값에 설정
	 */
	public void setPageLineCnt(int pageLineCnt) {
		this.pageLineCnt = pageLineCnt;
	}
	/**
	 * @return senderName 값 반환
	 */
	public String getSenderName() {
		return senderName;
	}
	/**
	 * @param senderName 파라미터를 senderName값에 설정
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	/**
	 * @return saveSendBox 값 반환
	 */
	public String getSaveSendBox() {
		return saveSendBox;
	}
	/**
	 * @param saveSendBox 파라미터를 saveSendBox값에 설정
	 */
	public void setSaveSendBox(String saveSendBox) {
		this.saveSendBox = saveSendBox;
	}
	/**
	 * @return receiveNoti 값 반환
	 */
	public String getReceiveNoti() {
		return receiveNoti;
	}
	/**
	 * @param receiveNoti 파라미터를 receiveNoti값에 설정
	 */
	public void setReceiveNoti(String receiveNoti) {
		this.receiveNoti = receiveNoti;
	}
	/**
	 * @return vcardAttach 값 반환
	 */
	public String getVcardAttach() {
		return vcardAttach;
	}
	/**
	 * @param vcardAttach 파라미터를 vcardAttach값에 설정
	 */
	public void setVcardAttach(String vcardAttach) {
		this.vcardAttach = vcardAttach;
	}
	/**
	 * @return userLocale 값 반환
	 */
	public String getUserLocale() {
		return userLocale;
	}
	/**
	 * @param userLocale 파라미터를 userLocale값에 설정
	 */
	public void setUserLocale(String userLocale) {
		this.userLocale = userLocale;
	}
	/**
	 * @return writeMode 값 반환
	 */
	public String getWriteMode() {
		return writeMode;
	}
	/**
	 * @param writeMode 파라미터를 writeMode값에 설정
	 */
	public void setWriteMode(String writeMode) {
		this.writeMode = writeMode;
	}
	/**
	 * @return charSet 값 반환
	 */
	public String getCharSet() {
		return charSet;
	}
	/**
	 * @param charSet 파라미터를 charSet값에 설정
	 */
	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}
	/**
	 * @return hiddenImg 값 반환
	 */
	public String getHiddenImg() {
		return hiddenImg;
	}
	/**
	 * @param hiddenImg 파라미터를 hiddenImg값에 설정
	 */
	public void setHiddenImg(String hiddenImg) {
		this.hiddenImg = hiddenImg;
	}
	/**
	 * @return signAttach 값 반환
	 */
	public String getSignAttach() {
		return signAttach;
	}
	/**
	 * @param signAttach 파라미터를 signAttach값에 설정
	 */
	public void setSignAttach(String signAttach) {
		this.signAttach = signAttach;
	}
	/**
	 * @return afterLogin 값 반환
	 */
	public String getAfterLogin() {
		return afterLogin;
	}
	/**
	 * @param afterLogin 파라미터를 afterLogin값에 설정
	 */
	public void setAfterLogin(String afterLogin) {
		this.afterLogin = afterLogin;
	}
	/**
	 * @return hiddenTag 값 반환
	 */
	public String getHiddenTag() {
		return hiddenTag;
	}
	/**
	 * @param hiddenTag 파라미터를 hiddenTag값에 설정
	 */
	public void setHiddenTag(String hiddenTag) {
		this.hiddenTag = hiddenTag;
	}
	/**
	 * @return notiInterval 값 반환
	 */
	public int getNotiInterval() {
		return notiInterval;
	}
	/**
	 * @param notiInterval 파라미터를 notiInterval값에 설정
	 */
	public void setNotiInterval(int notiInterval) {
		this.notiInterval = notiInterval;
	}
	/**
	 * @return userSkin 값 반환
	 */
	public String getUserSkin() {
		return userSkin;
	}
	/**
	 * @param userSkin 파라미터를 userSkin값에 설정
	 */
	public void setUserSkin(String userSkin) {
		this.userSkin = userSkin;
	}
	/**
	 * @return forwardingMode 값 반환
	 */
	public String getForwardingMode() {
		return forwardingMode;
	}
	/**
	 * @param forwardingMode 파라미터를 forwardingMode값에 설정
	 */
	public void setForwardingMode(String forwardingMode) {
		this.forwardingMode = forwardingMode;
	}
	/**
	 * @return senderEmail 값 반환
	 */
	public String getSenderEmail() {
		return senderEmail;
	}
	/**
	 * @param senderEmail 파라미터를 senderEmail값에 설정
	 */
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}
	
	
	
}
