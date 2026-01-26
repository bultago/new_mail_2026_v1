package com.terracetech.tims.webmail.addrbook.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.common.PageManager;
import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookAuthVO;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * 주소록 멤버 목록 조회 Controller
 * 
 * 주요 기능:
 * 1. 개인/공유 주소록 멤버 목록 조회
 * 2. 페이징 처리
 * 3. 검색 기능
 * 4. 정렬 기능
 * 5. 초성 인덱스 필터링
 * 6. 인쇄 모드 지원
 * 7. 권한 체크
 * 
 * Struts2 Action: ViewAddressMemberListAction
 * 변환 일시: 2025-10-20
 */
@Controller("viewAddressMemberListController")
public class ViewAddressMemberListController {

	@Autowired
	private AddressBookManager addressBookManager;

	/**
	 * 주소록 멤버 목록 화면 로드 (초기 로드)
	 * 
	 * @param groupSeq 그룹 시퀀스
	 * @param leadingPattern 초성 패턴
	 * @param sortBy 정렬 기준
	 * @param sortDir 정렬 방향
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success" - 목록 화면
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "groupSeq", defaultValue = "0") int groupSeq,
			@RequestParam(value = "leadingPattern", required = false) String leadingPattern,
			@RequestParam(value = "sortBy", required = false) String sortBy,
			@RequestParam(value = "sortDir", required = false) String sortDir,
			HttpServletRequest request,
			Model model) throws Exception {
		
		// 기본값 설정
		sortBy = (sortBy == null) ? "name" : sortBy;
		sortDir = (sortDir == null) ? "asc" : sortDir;
		leadingPattern = StringUtils.isEmpty(leadingPattern) ? "all" : leadingPattern;
		
		LogManager.writeDebug(this, "PrivateAddressListAction.leadingPattern = " + leadingPattern);
		
		// 멤버 목록 조회
		List<AddressBookMemberVO> members = addressBookManager.readPrivateMemberListByIndex(
				5, groupSeq, leadingPattern, 0, 10, sortBy, sortDir);
		
		model.addAttribute("members", members);
		
		return "success";
	}

	/**
	 * 주소록 멤버 목록 부분 로드 (Ajax)
	 * 
	 * @param groupSeq 그룹 시퀀스
	 * @param bookSeq 주소록 시퀀스 (0: 개인, 그 외: 공유)
	 * @param page 페이지 번호
	 * @param leadingPattern 초성 패턴
	 * @param searchType 검색 타입
	 * @param keyWord 검색 키워드
	 * @param sortBy 정렬 기준
	 * @param sortDir 정렬 방향
	 * @param paneMode 패널 모드
	 * @param addrType 주소록 타입 (popup, 등)
	 * @param pagePrint 인쇄 모드
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success", "tab", "print", "japan"
	 * @throws Exception
	 */
	public String executePart(
			@RequestParam(value = "groupSeq", defaultValue = "0") int groupSeq,
			@RequestParam(value = "bookSeq", defaultValue = "0") int bookSeq,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "leadingPattern", required = false) String leadingPattern,
			@RequestParam(value = "searchType", required = false) String searchType,
			@RequestParam(value = "keyWord", required = false) String keyWord,
			@RequestParam(value = "sortBy", required = false) String sortBy,
			@RequestParam(value = "sortDir", required = false) String sortDir,
			@RequestParam(value = "paneMode", required = false) String paneMode,
			@RequestParam(value = "addrType", required = false) String addrType,
			@RequestParam(value = "pagePrint", required = false) String pagePrint,
			HttpServletRequest request,
			Model model) throws Exception {
		
		// 사용자 정보 및 설정 조회
		User user = SessionUtil.getUser(request);
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int pageBase = Integer.parseInt(user.get(User.PAGE_LINE_CNT));
		
		// 팝업 모드일 경우 페이지 크기 조정
		if ("popup".equals(addrType)) {
			pageBase = 10;
		}
		
		// 기본값 설정
		leadingPattern = StringUtils.isEmpty(leadingPattern) ? "all" : leadingPattern;
		sortBy = (sortBy == null) ? "name" : sortBy;
		sortDir = (sortDir == null) ? "asc" : sortDir;
		
		LogManager.writeDebug(this, "PrivateAddressListAction.leadingPattern = " + leadingPattern);
		
		// 리소스 및 초성 문자 설정
		I18nResources resource = getMessageResource(user, "addr");
		String[] leadingCharaters = addressBookManager.getAlphabet(resource.getMessage("addr.table.index"));
		
		// 멤버 목록 및 권한 조회
		List<AddressBookMemberVO> members;
		AddressBookAuthVO auth;
		int total;
		
		if (bookSeq == 0) {
			// 개인 주소록
			total = getMemberTotal(userSeq, groupSeq, searchType, keyWord, leadingPattern, false, 0);
			page = initPageNavigation(page, total, pageBase);
			members = getPrivateMembers(userSeq, groupSeq, searchType, keyWord, leadingPattern, 
					page, pageBase, sortBy, sortDir);
			auth = addressBookManager.readPrivateAddressBookAuth(domainSeq, bookSeq, userSeq);
		} else {
			// 공유 주소록
			total = getMemberTotal(userSeq, groupSeq, searchType, keyWord, leadingPattern, true, bookSeq);
			page = initPageNavigation(page, total, pageBase);
			members = getSharedMembers(bookSeq, groupSeq, userSeq, searchType, keyWord, leadingPattern, 
					page, pageBase, sortBy, sortDir);
			auth = addressBookManager.readSharedAddressBookAuth(domainSeq, bookSeq, userSeq);
		}
		
		// 멤버명에서 ' 제거
		cleanMemberNames(members);
		
		// 페이징 정보 설정
		PageManager pm = createPageManager(total, pageBase, page);
		
		// Model에 추가
		model.addAttribute("members", members);
		model.addAttribute("auth", auth);
		model.addAttribute("leadingCharaters", leadingCharaters);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageBase", pageBase);
		model.addAttribute("total", total);
		model.addAttribute("pm", pm);
		
		// 뷰 결정
		return determineView(paneMode, pagePrint, addrType, user);
	}

	/**
	 * I18n 리소스 조회
	 * 
	 * @param user User
	 * @param module 모듈명
	 * @return I18nResources
	 */
	private I18nResources getMessageResource(User user, String module) {
		return new I18nResources(user.get(User.LOCALE), module);
	}

	/**
	 * 멤버 총 개수 조회
	 * 
	 * @param userSeq 사용자 시퀀스
	 * @param groupSeq 그룹 시퀀스
	 * @param searchType 검색 타입
	 * @param keyWord 검색 키워드
	 * @param leadingPattern 초성 패턴
	 * @param isShared 공유 주소록 여부
	 * @param bookSeq 주소록 시퀀스
	 * @return 총 개수
	 * @throws Exception
	 */
	private int getMemberTotal(int userSeq, int groupSeq, String searchType, String keyWord, 
			String leadingPattern, boolean isShared, int bookSeq) throws Exception {
		if (StringUtils.isNotEmpty(keyWord)) {
			return isShared 
					? addressBookManager.readSharedSearchMemberCount(bookSeq, groupSeq, userSeq, searchType, keyWord, leadingPattern)
					: addressBookManager.readPrivateSearchMemberCount(userSeq, groupSeq, searchType, keyWord, leadingPattern);
		} else {
			return isShared 
					? addressBookManager.readSharedMemberListCount(bookSeq, groupSeq, leadingPattern)
					: addressBookManager.readPrivateMemberListCount(userSeq, groupSeq, leadingPattern);
		}
	}

	/**
	 * 개인 주소록 멤버 조회
	 * 
	 * @param userSeq 사용자 시퀀스
	 * @param groupSeq 그룹 시퀀스
	 * @param searchType 검색 타입
	 * @param keyWord 검색 키워드
	 * @param leadingPattern 초성 패턴
	 * @param page 페이지
	 * @param pageBase 페이지 크기
	 * @param sortBy 정렬 기준
	 * @param sortDir 정렬 방향
	 * @return 멤버 목록
	 * @throws Exception
	 */
	private List<AddressBookMemberVO> getPrivateMembers(int userSeq, int groupSeq, String searchType, 
			String keyWord, String leadingPattern, int page, int pageBase, String sortBy, String sortDir) 
			throws Exception {
		if (StringUtils.isNotEmpty(keyWord)) {
			return addressBookManager.readPrivateSearchMember(userSeq, groupSeq, searchType, keyWord, 
					leadingPattern, page, pageBase, sortBy, sortDir);
		} else {
			return addressBookManager.readPrivateMemberListByIndex(userSeq, groupSeq, leadingPattern, 
					page, pageBase, sortBy, sortDir);
		}
	}

	/**
	 * 공유 주소록 멤버 조회
	 * 
	 * @param bookSeq 주소록 시퀀스
	 * @param groupSeq 그룹 시퀀스
	 * @param userSeq 사용자 시퀀스
	 * @param searchType 검색 타입
	 * @param keyWord 검색 키워드
	 * @param leadingPattern 초성 패턴
	 * @param page 페이지
	 * @param pageBase 페이지 크기
	 * @param sortBy 정렬 기준
	 * @param sortDir 정렬 방향
	 * @return 멤버 목록
	 * @throws Exception
	 */
	private List<AddressBookMemberVO> getSharedMembers(int bookSeq, int groupSeq, int userSeq, 
			String searchType, String keyWord, String leadingPattern, int page, int pageBase, 
			String sortBy, String sortDir) throws Exception {
		if (StringUtils.isNotEmpty(keyWord)) {
			return addressBookManager.readSharedSearchMember(bookSeq, groupSeq, userSeq, searchType, keyWord, 
					leadingPattern, page, pageBase, sortBy, sortDir);
		} else {
			return addressBookManager.readSharedMemberListByIndex(bookSeq, groupSeq, leadingPattern, 
					page, pageBase, sortBy, sortDir);
		}
	}

	/**
	 * 멤버명에서 작은따옴표 제거
	 * 
	 * @param members 멤버 목록
	 */
	private void cleanMemberNames(List<AddressBookMemberVO> members) {
		if (members != null) {
			for (AddressBookMemberVO member : members) {
				member.setMemberName(StringUtils.replace(member.getMemberName(), "'", ""));
			}
		}
	}

	/**
	 * 페이지 번호 초기화 및 검증
	 * 
	 * @param page 페이지 번호
	 * @param total 총 개수
	 * @param pageBase 페이지 크기
	 * @return 검증된 페이지 번호
	 */
	private int initPageNavigation(int page, int total, int pageBase) {
		page = (page == 0) ? 1 : page;
		
		int npages = (int) Math.ceil((double) total / pageBase);
		if (0 < npages && npages < page) {
			page = npages;
		}
		
		return page;
	}

	/**
	 * PageManager 생성
	 * 
	 * @param total 총 개수
	 * @param pageBase 페이지 크기
	 * @param page 현재 페이지
	 * @return PageManager
	 */
	private PageManager createPageManager(int total, int pageBase, int page) {
		PageManager pm = new PageManager();
		pm.initParameter(total, pageBase, 5);
		pm.setPage(page);
		return pm;
	}

	/**
	 * 뷰 결정
	 * 
	 * @param paneMode 패널 모드
	 * @param pagePrint 인쇄 모드
	 * @param addrType 주소록 타입
	 * @param user User
	 * @return 뷰 이름
	 */
	private String determineView(String paneMode, String pagePrint, String addrType, User user) {
		// 탭 모드
		if ("v".equals(paneMode)) {
			return "tab";
		}
		
		// 인쇄 모드가 아닐 경우
		if (StringUtils.isEmpty(pagePrint)) {
			String installLocale = EnvConstants.getBasicSetting("setup.state");
			if ("jp".equalsIgnoreCase(installLocale) && !"popup".equals(addrType)) {
				return "japan";
			}
			return "success";
		}
		
		// 인쇄 모드
		return "print";
	}
}

