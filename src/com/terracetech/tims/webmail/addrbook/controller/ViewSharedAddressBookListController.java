package com.terracetech.tims.webmail.addrbook.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookVO;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;

/**
 * 공유 주소록 목록 조회 Controller
 * 
 * 주요 기능:
 * 1. 공유 주소록 목록 조회
 * 2. 부분 화면 로드 (executePart)
 * 
 * Struts2 Action: ViewSharedAddressBookListAction
 * 변환 일시: 2025-10-20
 */
@Controller("viewSharedAddressBookListController")
public class ViewSharedAddressBookListController {

	@Autowired
	private AddressBookManager addressBookManager;

	/**
	 * 공유 주소록 목록 화면 로드
	 * 
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success" - 목록 화면
	 * @throws Exception
	 */
	public String execute(HttpServletRequest request, Model model) throws Exception {
		return "success";
	}

	/**
	 * 공유 주소록 목록 부분 로드
	 * 
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success" - 부분 화면
	 * @throws Exception
	 */
	public String executePart(HttpServletRequest request, Model model) throws Exception {
		// 사용자 정보 조회
		User user = SessionUtil.getUser(request);
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		// 주소록 목록 조회
		List<AddressBookVO> bookList = readAddressBookList(userSeq, domainSeq);
		
		// Model에 추가
		model.addAttribute("bookList", bookList);
		
		return "success";
	}

	/**
	 * 주소록 목록 조회
	 * 
	 * @param userSeq 사용자 시퀀스
	 * @param domainSeq 도메인 시퀀스
	 * @return 주소록 목록
	 * @throws Exception
	 */
	private List<AddressBookVO> readAddressBookList(int userSeq, int domainSeq) throws Exception {
		return addressBookManager.readAddressBookList(userSeq, domainSeq);
	}
}

