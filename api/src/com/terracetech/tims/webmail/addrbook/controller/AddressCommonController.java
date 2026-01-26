package com.terracetech.tims.webmail.addrbook.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.terracetech.tims.webmail.common.EnvConstants;

/**
 * 주소록 공통 Controller
 * 
 * 주요 기능:
 * 1. 주소록 페이지 로드
 * 2. 설치 로케일 정보 제공
 * 
 * Struts2 Action: AddressCommonAction
 * 변환 일시: 2025-10-20
 */
@Controller("addressCommonController")
public class AddressCommonController {

	/**
	 * 주소록 페이지 로드
	 * 
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "load" - 주소록 메인 페이지
	 * @throws Exception
	 */
	public String loadPage(HttpServletRequest request, Model model) throws Exception {
		// 설치 로케일 정보 설정
		String installLocale = getInstallLocale();
		model.addAttribute("installLocale", installLocale);
		
		return "load";
	}

	/**
	 * 설치 로케일 정보 조회
	 * 
	 * @return 설치 로케일
	 */
	private String getInstallLocale() {
		return EnvConstants.getBasicSetting("setup.state");
	}
}

