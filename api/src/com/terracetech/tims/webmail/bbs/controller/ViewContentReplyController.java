package com.terracetech.tims.webmail.bbs.controller;

import java.util.List;
import java.util.Map;

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
 * 답글 조회 Controller
 * 
 * 주요 기능:
 * 1. 답글 목록 조회
 * 2. 답글 상세 조회
 * 3. 권한 체크
 * 4. 페이징 처리
 * 
 * Struts2 Action: ViewContentReplyAction
 * 변환 일시: 2025-10-20
 */
@Controller("viewContentReplyController")
public class ViewContentReplyController {

	@Autowired
	private BbsManager bbsManager;

	/**
	 * 답글 조회
	 * 
	 * @param bbsId 게시판 ID
	 * @param contentId 게시글 ID
	 * @param currentPage 현재 페이지
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam("bbsId") int bbsId,
			@RequestParam("contentId") int contentId,
			@RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
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
		
		// 게시판 정보 조회
		BoardVO boardVo = bbsManager.readBoard(bbsId, domainSeq);
		
		// 권한 체크
		Map<String, String> bbsAuth = bbsManager.getBoardAuth(bbsId, domainSeq, userSeq);
		boolean bbsAdmin = "T".equals(bbsAuth.get("isAdmin"));
		boolean bbsCreator = "T".equals(bbsAuth.get("isCreator"));
		
		// 원본 게시글 조회
		BoardContentVO parentContent = bbsManager.readBoardContent(bbsId, contentId, domainSeq);
		
		// 답글 목록 조회
		List<BoardContentVO> replyList = getReplyList(bbsId, contentId, domainSeq, userSeq, bbsAdmin, boardVo.getBbsType());
		
		// 답글 개수 조회
		int replyCount = bbsManager.readBoardContentReplyCount(bbsId, contentId, domainSeq);
		
		// Model에 데이터 추가
		model.addAttribute("boardList", boardList);
		model.addAttribute("boardVo", boardVo);
		model.addAttribute("parentContent", parentContent);
		model.addAttribute("replyList", replyList);
		model.addAttribute("replyCount", replyCount);
		model.addAttribute("bbsAdmin", bbsAdmin);
		model.addAttribute("bbsCreator", bbsCreator);
		model.addAttribute("bbsId", bbsId);
		model.addAttribute("contentId", contentId);
		
		return "success";
	}

	/**
	 * 답글 목록 조회
	 * 
	 * @param bbsId 게시판 ID
	 * @param contentId 게시글 ID
	 * @param domainSeq 도메인 시퀀스
	 * @param userSeq 사용자 시퀀스
	 * @param bbsAdmin 관리자 여부
	 * @param bbsType 게시판 타입
	 * @return 답글 목록
	 * @throws Exception
	 */
	private List<BoardContentVO> getReplyList(int bbsId, int contentId, int domainSeq, 
			int userSeq, boolean bbsAdmin, String bbsType) throws Exception {
		
		int creatorSeq = 0;
		if (!bbsAdmin && "secret".equals(bbsType)) {
			creatorSeq = userSeq;
		}
		
		return bbsManager.readBoardContentReplyList(bbsId, contentId, domainSeq, creatorSeq);
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
