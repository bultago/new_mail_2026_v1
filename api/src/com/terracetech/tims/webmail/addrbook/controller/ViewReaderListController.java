package com.terracetech.tims.webmail.addrbook.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.common.PageManager;
import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookReaderVO;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * 주소록 열람자 목록 조회 Controller
 * 
 * 주요 기능:
 * 1. 주소록 열람자(Reader) 목록 조회
 * 2. 페이징 처리
 * 3. 검색 기능 (이름, 이메일)
 * 4. 이메일 도메인 자동 제거
 * 
 * Struts2 Action: ViewReaderListAction
 * 변환 일시: 2025-10-20
 */
@Controller("viewReaderListController")
public class ViewReaderListController {

	@Autowired
	private AddressBookManager addressBookManager;

	private static final int PAGE_BASE = 8;

	/**
	 * 열람자 목록 조회
	 * 
	 * @param bookSeq 주소록 시퀀스
	 * @param page 페이지 번호
	 * @param searchType 검색 타입
	 * @param keyWord 검색 키워드
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success" - 목록 화면
	 * @throws Exception
	 */
	public String execute(
			@RequestParam("bookSeq") int bookSeq,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "searchType", required = false) String searchType,
			@RequestParam(value = "keyWord", required = false) String keyWord,
			HttpServletRequest request,
			Model model) throws Exception {
		
		return executePart(bookSeq, page, searchType, keyWord, request, model);
	}

	/**
	 * 열람자 목록 부분 조회
	 * 
	 * @param bookSeq 주소록 시퀀스
	 * @param page 페이지 번호
	 * @param searchType 검색 타입
	 * @param keyWord 검색 키워드
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success" - 목록 화면
	 * @throws Exception
	 */
	public String executePart(
			@RequestParam("bookSeq") int bookSeq,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "searchType", required = false) String searchType,
			@RequestParam(value = "keyWord", required = false) String keyWord,
			HttpServletRequest request,
			Model model) throws Exception {
		
		// 키워드 정제
		keyWord = cleanKeyWord(keyWord);
		
		// 열람자 목록 조회
		int total = getReaderTotal(bookSeq, searchType, keyWord);
		List<AddressBookReaderVO> list = getReaderList(bookSeq, page, searchType, keyWord);
		AddressBookReaderVO[] readerList = list.toArray(new AddressBookReaderVO[list.size()]);
		
		// 페이징 정보 생성
		PageManager pm = createPageManager(total, page);
		
		// Model에 추가
		model.addAttribute("readerList", readerList);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageBase", PAGE_BASE);
		model.addAttribute("total", total);
		model.addAttribute("pm", pm);
		
		return "success";
	}

	/**
	 * 키워드 정제 (이메일 도메인 제거)
	 * 
	 * @param keyWord 원본 키워드
	 * @return 정제된 키워드
	 */
	private String cleanKeyWord(String keyWord) {
		if (StringUtils.isEmpty(keyWord)) {
			return "";
		}
		
		// @ 이후 도메인 제거
		if (keyWord.indexOf("@") > 1) {
			try {
				keyWord = keyWord.split("@")[0];
			} catch (Exception ignore) {
				LogManager.writeErr(this, ignore.getMessage(), ignore);
			}
		}
		
		return keyWord;
	}

	/**
	 * 열람자 총 개수 조회
	 * 
	 * @param bookSeq 주소록 시퀀스
	 * @param searchType 검색 타입
	 * @param keyWord 검색 키워드
	 * @return 총 개수
	 * @throws Exception
	 */
	private int getReaderTotal(int bookSeq, String searchType, String keyWord) 
			throws Exception {
		return addressBookManager.readAddressBookReaderListCount(bookSeq, searchType, keyWord);
	}

	/**
	 * 열람자 목록 조회
	 * 
	 * @param bookSeq 주소록 시퀀스
	 * @param page 페이지 번호
	 * @param searchType 검색 타입
	 * @param keyWord 검색 키워드
	 * @return 열람자 목록
	 * @throws Exception
	 */
	private List<AddressBookReaderVO> getReaderList(int bookSeq, int page, 
			String searchType, String keyWord) throws Exception {
		return addressBookManager.readAddressBookReaderList(bookSeq, page, PAGE_BASE, 
				searchType, keyWord);
	}

	/**
	 * PageManager 생성
	 * 
	 * @param total 총 개수
	 * @param page 현재 페이지
	 * @return PageManager
	 */
	private PageManager createPageManager(int total, int page) {
		PageManager pm = new PageManager();
		pm.initParameter(total, PAGE_BASE, 5);
		pm.setPage(page);
		return pm;
	}
}

