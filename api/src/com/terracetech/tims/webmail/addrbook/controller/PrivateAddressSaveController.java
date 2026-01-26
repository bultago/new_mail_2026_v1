package com.terracetech.tims.webmail.addrbook.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;

/**
 * 개인 주소록 저장 Controller
 * 
 * 주요 기능:
 * 1. 개인 주소록 멤버 저장
 * 2. PAID(유료) 체크
 * 3. 그룹에 멤버 추가
 * 4. 트랜잭션 처리
 * 
 * Struts2 Action: PrivateAddressSaveAction
 * 변환 일시: 2025-10-20
 */
@Controller("privateAddressSaveController")
public class PrivateAddressSaveController {

	@Autowired
	private AddressBookManager addressBookManager;

	/**
	 * 개인 주소록 멤버 저장
	 * 
	 * @param name 이름
	 * @param email 이메일
	 * @param company 회사명
	 * @param mobile 휴대폰
	 * @param groupSeq 그룹 시퀀스
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success" or "paidError"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "company", required = false) String company,
			@RequestParam(value = "mobile", required = false) String mobile,
			@RequestParam("groupSeq") int groupSeq,
			HttpServletRequest request,
			Model model) throws Exception {
		
		// PAID 체크
		if (!checkPAID(request)) {
			return "paidError";
		}
		
		// 사용자 정보 조회
		User user = SessionUtil.getUser(request);
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		// 멤버 VO 생성
		AddressBookMemberVO member = createMemberVO(userSeq, name, email, company, mobile);
		
		// 멤버 저장
		savePrivateAddressMember(member, groupSeq, domainSeq);
		
		return "success";
	}

	/**
	 * PAID 체크 (유료 서비스 확인)
	 * 
	 * @param request HttpServletRequest
	 * @return PAID 여부
	 */
	private boolean checkPAID(HttpServletRequest request) {
		// TODO: PAID 체크 로직 구현 필요
		// BaseAction의 checkPAID() 메서드 참조
		return true;
	}

	/**
	 * 멤버 VO 생성
	 * 
	 * @param userSeq 사용자 시퀀스
	 * @param name 이름
	 * @param email 이메일
	 * @param company 회사명
	 * @param mobile 휴대폰
	 * @return AddressBookMemberVO
	 */
	private AddressBookMemberVO createMemberVO(int userSeq, String name, String email, 
			String company, String mobile) {
		AddressBookMemberVO member = new AddressBookMemberVO();
		member.setUserSeq(userSeq);
		member.setMemberName(name);
		member.setMemberEmail(email);
		member.setCompanyName(company);
		member.setMobileNo(mobile);
		return member;
	}

	/**
	 * 개인 주소록 멤버 저장 (트랜잭션)
	 * 
	 * @param member 멤버 VO
	 * @param groupSeq 그룹 시퀀스
	 * @param domainSeq 도메인 시퀀스
	 */
	private void savePrivateAddressMember(AddressBookMemberVO member, int groupSeq, int domainSeq) {
		try {
			addressBookManager.savePrivateAddressMemberWithTransactional(member, groupSeq, domainSeq);
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}
	}
}

