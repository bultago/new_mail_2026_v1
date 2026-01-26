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
 * 공지사항 조회 Controller
 * 
 * 주요 기능:
 * 1. 공지사항 상세 조회
 * 2. 이전/다음 공지사항 조회
 * 3. 권한 체크
 * 
 * Struts2 Action: ViewNoticeContentAction
 * 변환 일시: 2025-10-20
 */
@Controller("viewNoticeContentController")
public class ViewNoticeContentController {

	@Autowired
	private BbsManager bbsManager;

	/**
	 * 공지사항 조회
	 * 
	 * @param bbsId 게시판 ID
	 * @param contentId 게시글 ID
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam("bbsId") int bbsId,
			@RequestParam("contentId") int contentId,
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
		
		// 공지사항 조회
		BoardContentVO noticeVo = bbsManager.readBoardContentNotice(bbsId, contentId, domainSeq);
		
		// 작성자 체크
		if (userSeq == noticeVo.getCreatorSeq()) {
			noticeVo.setCreator(true);
		}
		
		// 이전/다음 공지사항 조회
		List<BoardContentVO> prevNotice = bbsManager.readPrevBoardContentNotice(bbsId, contentId, domainSeq);
		List<BoardContentVO> nextNotice = bbsManager.readNextBoardContentNotice(bbsId, contentId, domainSeq);
		
		// Model에 데이터 추가
		model.addAttribute("boardList", boardList);
		model.addAttribute("boardVo", boardVo);
		model.addAttribute("noticeVo", noticeVo);
		model.addAttribute("prevNotice", prevNotice);
		model.addAttribute("nextNotice", nextNotice);
		model.addAttribute("bbsAdmin", bbsAdmin);
		model.addAttribute("bbsCreator", bbsCreator);
		model.addAttribute("bbsId", bbsId);
		model.addAttribute("contentId", contentId);
		
		return "success";
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
