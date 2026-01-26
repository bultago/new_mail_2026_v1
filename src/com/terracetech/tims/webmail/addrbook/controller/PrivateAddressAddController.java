package com.terracetech.tims.webmail.addrbook.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookGroupVO;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;

/**
 * 개인 주소록 추가 Controller
 * 
 * 주요 기능:
 * 1. 개인 주소록 추가 화면 로드
 * 2. 사용자의 그룹 목록 조회
 * 3. 부분 화면 로드 (executePart)
 * 
 * Struts2 Action: PrivateAddressAddAction
 * 변환 일시: 2025-10-20
 */
@Controller("privateAddressAddController")
public class PrivateAddressAddController {

	@Autowired
	private AddressBookManager addressBookManager;

	/**
	 * 개인 주소록 추가 화면 로드
	 * 
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success" - 개인 주소록 추가 화면
	 * @throws Exception
	 */
	public String execute(HttpServletRequest request, Model model) throws Exception {
		// 사용자 정보 조회
		User user = SessionUtil.getUser(request);
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		// 개인 그룹 목록 조회
		List<AddressBookGroupVO> groups = getPrivateGroupList(userSeq);
		
		// Model에 추가
		model.addAttribute("groups", groups);
		
		return "success";
	}

	/**
	 * 부분 화면 로드
	 * 
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success" - 부분 화면
	 * @throws Exception
	 */
	public String executePart(HttpServletRequest request, Model model) throws Exception {
		return "success";
	}

	/**
	 * 개인 그룹 목록 조회
	 * 
	 * @param userSeq 사용자 시퀀스
	 * @return 그룹 목록
	 * @throws Exception
	 */
	private List<AddressBookGroupVO> getPrivateGroupList(int userSeq) throws Exception {
		return addressBookManager.getPrivateGroupList(userSeq);
	}
}

