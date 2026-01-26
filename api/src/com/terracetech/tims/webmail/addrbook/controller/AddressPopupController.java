package com.terracetech.tims.webmail.addrbook.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;

/**
 * 주소록 팝업 Controller
 * 
 * 주요 기능:
 * 1. 주소록 팝업 화면 로드
 * 
 * Struts2 Action: AddressPopupAction
 * 변환 일시: 2025-10-20
 */
@Controller("addressPopupController")
public class AddressPopupController {

	@Autowired
	private AddressBookManager addressBookManager;

	/**
	 * 주소록 팝업 화면 로드
	 * 
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success" - 팝업 화면
	 * @throws Exception
	 */
	public String execute(HttpServletRequest request, Model model) throws Exception {
		return "success";
	}
}

