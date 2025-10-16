package com.terracetech.tims.service.samsung;

import java.rmi.Remote;

import com.terracetech.tims.service.samsung.vo.AttachWSVO;
import com.terracetech.tims.service.samsung.vo.ContactWSVO;


public interface IContactService extends Remote{
	
	public final static int SUCCESS = 1;
	
	public final static int FAILED = -1;

	public String testConnect();
	
	/**
	 * 연락처추가 (첨부없을 경우)
	 * 
	 * @param licenseKey
	 * @param param
	 * @return 성공시 1, 실패시 -1 리턴
	 */
	public int addContact(String licenseKey, ContactWSVO param);
	
	/**
	 * "연락처수정(첨부없을 경우)
	 * 성공시 1, 실패시 -1 리턴"
	 * @param licenseKey
	 * @param param
	 * @return
	 */
	public int modContact(String licenseKey, ContactWSVO param);

	/**
	 * "연락처삭제(첨부없을 경우)
	 * 성공시 1, 실패시 -1 리턴"
	 * @param licenseKey
	 * @param param
	 * @return
	 */
	public int delContact(String licenseKey, ContactWSVO param);
	/**
	 * 연락처 상세 조회
	 * 
	 * @param licenseKey
	 * @param param
	 * @return 실패시 null 리턴
	 */
	public ContactWSVO getContact(String licenseKey, ContactWSVO param);

	/**
	 * 연락처추가 (첨부가 있는 경우)
	 * 성공시 1, 실패시 -1 리턴
	 * @param licenseKey
	 * @param param
	 * @param attach
	 * @return
	 */
	public int addContactWithAttach(String licenseKey, ContactWSVO param, AttachWSVO[] attach);

	/**
	 * 연락처수정 (첨부가 있는 경우)
	 * 성공시 1, 실패시 -1 리턴
	 * @param licenseKey
	 * @param param
	 * @param attach
	 * @return
	 */
	public int modContactWithAttach(String licenseKey, ContactWSVO param, AttachWSVO[] attach);

	
	/**
	 * 연락처 목록 조회/검색
	 * 검색은 이름과 전화번호 기준으로 검색할 수 있음
	 * param.setSearchKey(""검색어 (이름 또는 전화번호)"");
	 * param.setSearchField(""name||phone"");
	 * 
	 * @param licenseKey
	 * @param param
	 * @return
	 */
	public ContactWSVO[] getContactList(String licenseKey, ContactWSVO param);

}
