package com.terracetech.tims.service.samsung;

import java.rmi.Remote;

import com.terracetech.tims.service.samsung.vo.AttachWSVO;
import com.terracetech.tims.service.samsung.vo.ScheduleWSVO;


public interface IScheduleService extends Remote{

	/**
	 * 정추가 (첨부없을 경우)
	 * 성공시 1, 실패시 -1 리턴
	 * 
	 * @param licenseKey
	 * @param param
	 * @return
	 */
	public int addSchedule(String licenseKey, ScheduleWSVO param);
	
	/**
	 * 일정추가 (첨부가 있는 경우)
	 * 성공시 1, 실패시 -1 리턴
	 * 
	 * @param licenseKey
	 * @param param
	 * @param attach
	 * @return
	 */
	public int addScheduleWithAttach(String licenseKey, ScheduleWSVO param, AttachWSVO[] attach);
	
	/**
	 * 일정수정(첨부없을 경우)
	 * 성공시 1, 실패시 -1 리턴
	 * 
	 * @param licenseKey
	 * @param param
	 * @return
	 */
	public int modSchedule(String licenseKey, ScheduleWSVO param);
	
	/**
	 * 일정 수정
	 * 성공시 1, 실패시 -1 리턴
	 * 
	 * @param licenseKey
	 * @param param
	 * @param attach
	 * @return
	 */
	public int modScheduleWithAttach(String licenseKey, ScheduleWSVO param, AttachWSVO[] attach);
	
	/**
	 * 일정삭제
	 * 성공시 1, 실패시 -1 리턴
	 * 
	 * @param licenseKey
	 * @param param
	 * @return
	 */
	public int delSchedule(String licenseKey, ScheduleWSVO param);
	
	/**
	 * 일정 상세 조회
	 * 해당 일정의 상세정보(참석자, 첨부)를 리턴, 실패시 null 리턴
	 * @param licenseKey
	 * @param param
	 * @return
	 */
	public ScheduleWSVO getSchedule(String licenseKey, ScheduleWSVO param);
	
	/**
	 * 일정 목록 조회/검색 (검색시 setSearchString() 사용)
	 * 입력된 시작일부터 종료일까의 일정 전부(약속, 할일, 행사, 기념일)를 리턴
	 * 반복 일정의 경우 반복여부(repeatFlag)만 Y로 표시됨
	 * 일정제목, 위치, 등록자, 일정 시작/종료일, 반복여부 정보 리턴
	 * 
	 * @param licenseKey
	 * @param param
	 * @return
	 */
	public ScheduleWSVO[] getScheduleList(String licenseKey, ScheduleWSVO param);
	

}
