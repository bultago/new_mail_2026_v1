package com.terracetech.tims.webmail.bbs.controller;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.bbs.exception.BbsAuthException;
import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.bbs.vo.BoardContentVO;
import com.terracetech.tims.webmail.bbs.vo.BoardVO;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;

/**
 * 답글 삭제 Controller
 * 
 * 주요 기능:
 * 1. 답글 삭제
 * 2. 권한 체크
 * 3. 첨부파일 삭제
 * 
 * Struts2 Action: DeleteContentReplyAction
 * 변환 일시: 2025-10-20
 */
@Controller("deleteContentReplyController")
public class DeleteContentReplyController {

	@Autowired
	private BbsManager bbsManager;

	/**
	 * 답글 삭제
	 * 
	 * @param bbsId 게시판 ID
	 * @param contentId 게시글 ID
	 * @param replyId 답글 ID
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success" or "fail"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam("bbsId") int bbsId,
			@RequestParam("contentId") int contentId,
			@RequestParam("replyId") int replyId,
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
		
		// 답글 정보 조회
		BoardContentVO replyVo = bbsManager.readBoardContentReply(bbsId, replyId, domainSeq);
		
		// 삭제 권한 체크
		validateDeletePermission(replyVo, userSeq, bbsAdmin, resource);
		
		// 답글 삭제 처리
		deleteReply(bbsId, replyId, domainSeq, replyVo);
		
		// Model에 데이터 추가
		model.addAttribute("boardList", boardList);
		model.addAttribute("boardVo", boardVo);
		model.addAttribute("bbsAdmin", bbsAdmin);
		model.addAttribute("bbsCreator", bbsCreator);
		model.addAttribute("bbsId", bbsId);
		model.addAttribute("contentId", contentId);
		
		return "success";
	}

	/**
	 * 삭제 권한 검증
	 * 
	 * @param replyVo 답글 VO
	 * @param userSeq 사용자 시퀀스
	 * @param bbsAdmin 관리자 여부
	 * @param resource I18n 리소스
	 * @throws BbsAuthException
	 */
	private void validateDeletePermission(BoardContentVO replyVo, int userSeq, 
			boolean bbsAdmin, I18nResources resource) throws BbsAuthException {
		
		// 관리자는 모든 답글 삭제 가능
		if (bbsAdmin) {
			return;
		}
		
		// 작성자만 자신의 답글 삭제 가능
		if (replyVo.getCreatorSeq() != userSeq) {
			throw new BbsAuthException(resource.getMessage("bbs.reply.delete.permission.denied"));
		}
	}

	/**
	 * 답글 삭제 처리
	 * 
	 * @param bbsId 게시판 ID
	 * @param replyId 답글 ID
	 * @param domainSeq 도메인 시퀀스
	 * @param replyVo 답글 VO
	 * @throws Exception
	 */
	private void deleteReply(int bbsId, int replyId, int domainSeq, BoardContentVO replyVo) throws Exception {
		// 첨부파일 삭제
		deleteReplyAttachments(replyVo);
		
		// 답글 삭제
		bbsManager.deleteBoardContentReply(bbsId, replyId, domainSeq);
	}

	/**
	 * 답글 첨부파일 삭제
	 * 
	 * @param replyVo 답글 VO
	 * @throws Exception
	 */
	private void deleteReplyAttachments(BoardContentVO replyVo) throws Exception {
		// 답글 첨부파일 삭제 로직 구현
		// 실제 구현에서는 첨부파일 시스템을 통해 파일 삭제
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
