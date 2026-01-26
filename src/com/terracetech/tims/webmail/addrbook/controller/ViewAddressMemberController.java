package com.terracetech.tims.webmail.addrbook.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;

/**
 * 주소록 멤버 상세 조회 Controller
 * 
 * 주요 기능:
 * 1. 개인 주소록 멤버 상세 조회
 * 2. 공유 주소록 멤버 상세 조회
 * 3. 탭 모드 및 일반 모드 지원
 * 4. 패널 모드별 뷰 분기
 * 
 * Struts2 Action: ViewAddressMemberAction
 * 변환 일시: 2025-10-20
 */
@Controller("viewAddressMemberController")
public class ViewAddressMemberController {

	@Autowired
	private AddressBookManager addressBookManager;

	/**
	 * 주소록 멤버 상세 조회
	 * 
	 * @param memberSeq 멤버 시퀀스
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success" - 상세 화면
	 * @throws Exception
	 */
	public String execute(
			@RequestParam("memberSeq") int memberSeq,
			HttpServletRequest request,
			Model model) throws Exception {
		
		// 사용자 정보 조회
		User user = SessionUtil.getUser(request);
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		// 개인 주소록 멤버 조회
		AddressBookMemberVO member = readPrivateAddressMember(userSeq, memberSeq);
		
		// Model에 추가
		model.addAttribute("member", member);
		
		return "success";
	}

	/**
	 * 주소록 멤버 상세 조회 (부분)
	 * 
	 * @param memberSeq 멤버 시퀀스
	 * @param bookSeq 주소록 시퀀스 (0: 개인, 그 외: 공유)
	 * @param paneMode 패널 모드 (h: horizontal, v: vertical)
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "tab" or "success"
	 * @throws Exception
	 */
	public String executePart(
			@RequestParam(value = "memberSeq", defaultValue = "0") int memberSeq,
			@RequestParam(value = "bookSeq", defaultValue = "0") int bookSeq,
			@RequestParam(value = "paneMode", required = false) String paneMode,
			HttpServletRequest request,
			Model model) throws Exception {
		
		// 사용자 정보 조회
		User user = SessionUtil.getUser(request);
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		// 멤버 조회
		AddressBookMemberVO member = null;
		if (memberSeq != 0) {
			member = (bookSeq == 0) 
					? readPrivateAddressMember(userSeq, memberSeq)
					: readSharedAddressMember(bookSeq, memberSeq);
		}
		
		// Model에 추가
		model.addAttribute("member", member);
		
		// 뷰 결정
		return determineView(paneMode);
	}

	/**
	 * 개인 주소록 멤버 조회
	 * 
	 * @param userSeq 사용자 시퀀스
	 * @param memberSeq 멤버 시퀀스
	 * @return AddressBookMemberVO
	 * @throws Exception
	 */
	private AddressBookMemberVO readPrivateAddressMember(int userSeq, int memberSeq) 
			throws Exception {
		return addressBookManager.readPrivateAddressMember(userSeq, memberSeq);
	}

	/**
	 * 공유 주소록 멤버 조회
	 * 
	 * @param bookSeq 주소록 시퀀스
	 * @param memberSeq 멤버 시퀀스
	 * @return AddressBookMemberVO
	 * @throws Exception
	 */
	private AddressBookMemberVO readSharedAddressMember(int bookSeq, int memberSeq) 
			throws Exception {
		return addressBookManager.readSharedAddressMember(bookSeq, memberSeq);
	}

	/**
	 * 뷰 결정
	 * 
	 * @param paneMode 패널 모드
	 * @return 뷰 이름
	 */
	private String determineView(String paneMode) {
		if (paneMode == null || "h".equals(paneMode)) {
			return "tab";
		}
		return "success";
	}
}

