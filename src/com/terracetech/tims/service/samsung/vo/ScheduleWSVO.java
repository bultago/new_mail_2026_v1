package com.terracetech.tims.service.samsung.vo;

import java.io.Serializable;

public class ScheduleWSVO implements Serializable{

	private static final long serialVersionUID = 20091102L;

	/**
	 * 일정종류
	 * 약속 : 1, 할일 : 2, 행사 : 3, 기념일 : 4
	 */
	private String category = null;
	
	/**
	 * 회사코드
	 */
	private String compID = null;
	
	/**
	 * 부서코드
	 */
	private String deptID = null;
	
	/**
	 * 사용자 ID
	 * 사용자 고유 UID (로그인ID아님)
	 */
	private String userID = null;
	
	/**
	 * 사용자명
	 */
	private String userName = null;
	
	/**
	 * 타임존
	 * 사용자의 로컬 타임존 정보
	 */
	private String userTimeZone = null;
	
	/**
	 * 일정ID
	 * 수정, 삭제, 조회시 필요한 일정 ID
	 */
	private String calseq = null;
	
	/**
	 * 일정명
	 */
	private String subject = null;
	
	/**
	 * 일정내용
	 */
	private String content = null;
	
	/**
	 * 일정위치
	 */
	private String location = null;
	
	/**
	 * 현재연도
	 */
	private String thisYear = null;
	
	/**
	 * 일정 시작 일시
	 * 시작 날짜 + 시간
	 */
	private String startTime = null;
	
	/**
	 * 일정 종료 일시
	 * 종료 날짜 + 시간
	 */
	private String endTime = null;
	
	/**
	 * 일정 완료 일시
	 * 완료 날짜 + 시간 (종료와는 다름)
	 */
	private String completeDate = null;
	
	/**
	 * 반복여부
	 */
	private String repeatFlag = null;
	
	/**
	 * 반복시작
	 * 반복이 사작되는 날짜
	 */
	private String repeatStart = null;
	
	/**
	 * 반복종료
	 * 반복이 종료되는 날짜
	 */
	private String repeatEnd = null;
	
	/**
	 * 반복순서
	 * 반복되는 일정간의 순서
	 */
	private String repeatSeq = null;
	
	/**
	 * 반복 수정삭제 적용 모드
	 * Y일 경우 수정/삭제시 반복일정 전체 수정/삭제 
	 * N일 경우 선택된 일정만 수정 삭제
	 */
	private String repeatUpdateDeleteAll = null;

	/**
	 * 첨부ID
	 * 첨부파일의 FID (일정 수정, 삭제시 필요)
	 */
	private String fid = null;
	
	/**
	 * 공개등급
	 * 1 : 전체 공개, 2 : 참석자 공개, 3 : 비공개
	 */
	private String classtp = null;
	
	/**
	 * 상태정보
	 * 약속 : T(보류), C(참석), N(불참), D(약속취소)
	 * 할일 : N(시작안함), I(진행중), Y(완료), D(할일취소) 
	 */
	private String status = null;

	/**
	 * 우선순위
	 * 1~3
	 */
	private String priority = null;
	
	/**
	 * 에디터종류
	 * 나모 : 2, 일반 웹에디터 또는 텍스트 에디터 : 5
	 */
	private String editorType = null;
	
	/**
	 * 검색어
	 * 일정 검색시 검색어
	 */
	private String searchString = null;
	
	/**
	 * 일정검색 시작일
	 * 기간별 일정을 구할때 기간의 시작일
	 */
	private String searchDate_S = null;
	
	/**
	 * 일정검색 종료일
	 * 기간별 일정을 구할때 기간의 종료일
	 */
	private String searchDate_E = null;
	
	/**
	 * 참석자정보
	 * 일정에 참석자가 있을 경우 참석자 정보
	 * 참석자간 구분자 : |
	 * 참석자 정보간 구분자 : ^
	 * uid^이름^직급^부서^회사^Y^로그인ID^메일주소^메일발송여부(Y/N)^compID | 반복 
	 */
	private String attendant = null;

	/**
	 * 반복코드 1
	 * 1:일간 2:주간 3:월간 4:년간
	 */
	private String rcode1 = null;

	/**
	 * 반복코드 2
	 * rcode1에서 설정된 코드에 따라서 매 rocde2 일/주/월/년로 설정됨
	 * rcode1이 1이고 rcode2가 1이면 매1일마다 반복
	 * rocde1이 1이고 rcode2가 2이면 매2일마다 반복
	 * rcode1이 2이고 rcode2가 1이면 매1주마다 반복
	 */
	private String rcode2 = null;

	/**
	 * 반복코드 3
	 * rcode2에서 설정된 코드에 따라서 정의됨
	 * 주간 반복 요일 직접 설정시
	 * 1~7 (1 : 일요일, 2 : 월요일, ~ 7 : 토요일)
	 * rcode1 :2, rcode2 : 2, rcode3 : 25 이면  매2주마다 월요일 목요일날 반복 
	 *  월간반복 날짜 직접 설정시 또는 주차 설정시
	 *  rcode1 :3, rcode2 : 1, rcode3 : 6 이면  매1월마다 6일날 반복 
	 *  rcode1 :3, rcode2 : 1, rcode3 : 43 이면 매1월마다 3째주 특정 요일에반복  
	 */
	private String rcode3 = null;

	/**
	 * 반복코드 4
	 * 여기서 요일에 해당하는 값은 rcode4에서 지정 
	 */
	private String rcode4 = null;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCompID() {
		return compID;
	}

	public void setCompID(String compID) {
		this.compID = compID;
	}

	public String getDeptID() {
		return deptID;
	}

	public void setDeptID(String deptID) {
		this.deptID = deptID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserTimeZone() {
		return userTimeZone;
	}

	public void setUserTimeZone(String userTimeZone) {
		this.userTimeZone = userTimeZone;
	}

	public String getCalseq() {
		return calseq;
	}

	public void setCalseq(String calseq) {
		this.calseq = calseq;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getThisYear() {
		return thisYear;
	}

	public void setThisYear(String thisYear) {
		this.thisYear = thisYear;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}

	public String getRepeatFlag() {
		return repeatFlag;
	}

	public void setRepeatFlag(String repeatFlag) {
		this.repeatFlag = repeatFlag;
	}

	public String getRepeatStart() {
		return repeatStart;
	}

	public void setRepeatStart(String repeatStart) {
		this.repeatStart = repeatStart;
	}

	public String getRepeatEnd() {
		return repeatEnd;
	}

	public void setRepeatEnd(String repeatEnd) {
		this.repeatEnd = repeatEnd;
	}

	public String getRepeatSeq() {
		return repeatSeq;
	}

	public void setRepeatSeq(String repeatSeq) {
		this.repeatSeq = repeatSeq;
	}

	public String getRepeatUpdateDeleteAll() {
		return repeatUpdateDeleteAll;
	}

	public void setRepeatUpdateDeleteAll(String repeatUpdateDeleteAll) {
		this.repeatUpdateDeleteAll = repeatUpdateDeleteAll;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getClasstp() {
		return classtp;
	}

	public void setClasstp(String classtp) {
		this.classtp = classtp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getEditorType() {
		return editorType;
	}

	public void setEditorType(String editorType) {
		this.editorType = editorType;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getSearchDate_S() {
		return searchDate_S;
	}

	public void setSearchDate_S(String searchDateS) {
		searchDate_S = searchDateS;
	}

	public String getSearchDate_E() {
		return searchDate_E;
	}

	public void setSearchDate_E(String searchDateE) {
		searchDate_E = searchDateE;
	}

	public String getAttendant() {
		return attendant;
	}

	public void setAttendant(String attendant) {
		this.attendant = attendant;
	}

	public String getRcode1() {
		return rcode1;
	}

	public void setRcode1(String rcode1) {
		this.rcode1 = rcode1;
	}

	public String getRcode2() {
		return rcode2;
	}

	public void setRcode2(String rcode2) {
		this.rcode2 = rcode2;
	}

	public String getRcode3() {
		return rcode3;
	}

	public void setRcode3(String rcode3) {
		this.rcode3 = rcode3;
	}

	public String getRcode4() {
		return rcode4;
	}

	public void setRcode4(String rcode4) {
		this.rcode4 = rcode4;
	}
}
