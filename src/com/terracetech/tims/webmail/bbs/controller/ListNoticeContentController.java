package com.terracetech.tims.webmail.bbs.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.bbs.vo.BoardContentVO;
import com.terracetech.tims.webmail.bbs.vo.BoardVO;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;

/**
 * 공지사항 목록 조회 Controller
 * 
 * 주요 기능:
 * 1. 공지사항 목록 조회
 * 2. 권한 체크
 * 3. 페이징 처리
 * 
 * Struts2 Action: ListNoticeContentAction
 * 변환 일시: 2025-10-20
 */
@Controller("listNoticeContentController")
public class ListNoticeContentController {

	@Autowired
	private BbsManager bbsManager;

	/**
	 * 공지사항 목록 조회
	 * 
	 * @param bbsId 게시판 ID
	 * @param currentPage 현재 페이지
	 * @param pbase 페이지 크기
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "bbsId", defaultValue = "0") int bbsId,
			@RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
			@RequestParam(value = "pbase", defaultValue = "10") int pbase,
			HttpServletRequest request,
			Model model) throws Exception {
		
		// 사용자 정보 조회
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "bbs");
		
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		// 게시판 목록 조회
		List<BoardVO> boardList = bbsManager.readBoardList(domainSeq);
		
		if (boardList == null || boardList.size() == 0) {
			model.addAttribute("errMsg", resource.getMessage("bbs.empty"));
			return "fail";
		}
		
		// 기본값 설정
		if (bbsId == 0) {
			bbsId = boardList.get(0).getBbsId();
		}
		
		// 게시판 정보 조회
		BoardVO boardVo = bbsManager.readBoard(bbsId, domainSeq);
		
		// 공지사항 목록 조회
		List<BoardContentVO> noticeList = getNoticeList(bbsId, domainSeq, currentPage, pbase);
		
		// 공지사항 개수 조회
		int noticeCount = bbsManager.readBoardContentNoticeCount(bbsId, domainSeq);
		
		// Model에 데이터 추가
		model.addAttribute("boardList", boardList);
		model.addAttribute("boardVo", boardVo);
		model.addAttribute("noticeList", noticeList);
		model.addAttribute("noticeCount", noticeCount);
		model.addAttribute("bbsId", bbsId);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("pbase", pbase);
		
		return "success";
	}

	/**
	 * 공지사항 목록 조회
	 * 
	 * @param bbsId 게시판 ID
	 * @param domainSeq 도메인 시퀀스
	 * @param currentPage 현재 페이지
	 * @param pbase 페이지 크기
	 * @return 공지사항 목록
	 * @throws Exception
	 */
	private List<BoardContentVO> getNoticeList(int bbsId, int domainSeq, int currentPage, int pbase) throws Exception {
		int skipResult = (currentPage - 1) * pbase;
		return bbsManager.readBoardContentNoticeList(bbsId, domainSeq, skipResult, pbase);
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
}
