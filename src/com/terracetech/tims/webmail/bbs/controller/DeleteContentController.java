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
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * 게시글 삭제 Controller
 * 
 * 주요 기능:
 * 1. 게시글 삭제
 * 2. 답글 삭제
 * 3. 권한 체크
 * 4. 첨부파일 삭제
 * 5. 하위 게시글 처리
 * 
 * Struts2 Action: DeleteContentAction
 * 변환 일시: 2025-10-20
 */
@Controller("deleteContentController")
public class DeleteContentController {

	@Autowired
	private BbsManager bbsManager;

	/**
	 * 게시글 삭제
	 * 
	 * @param bbsId 게시판 ID
	 * @param contentId 게시글 ID
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success" or "fail"
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
		
		// 게시글 정보 조회
		BoardContentVO contentVo = bbsManager.readBoardContent(bbsId, contentId, domainSeq);
		
		// 삭제 권한 체크
		validateDeletePermission(contentVo, userSeq, bbsAdmin, resource);
		
		// 게시글 삭제 처리
		deleteBoardContent(bbsId, contentId, domainSeq, contentVo, bbsAdmin);
		
		// Model에 데이터 추가
		model.addAttribute("boardList", boardList);
		model.addAttribute("boardVo", boardVo);
		model.addAttribute("bbsAdmin", bbsAdmin);
		model.addAttribute("bbsCreator", bbsCreator);
		model.addAttribute("bbsId", bbsId);
		
		return "success";
	}

	/**
	 * 삭제 권한 검증
	 * 
	 * @param contentVo 게시글 VO
	 * @param userSeq 사용자 시퀀스
	 * @param bbsAdmin 관리자 여부
	 * @param resource I18n 리소스
	 * @throws BbsAuthException
	 */
	private void validateDeletePermission(BoardContentVO contentVo, int userSeq, 
			boolean bbsAdmin, I18nResources resource) throws BbsAuthException {
		
		// 관리자는 모든 게시글 삭제 가능
		if (bbsAdmin) {
			return;
		}
		
		// 작성자만 자신의 게시글 삭제 가능
		if (contentVo.getCreatorSeq() != userSeq) {
			throw new BbsAuthException(resource.getMessage("bbs.delete.permission.denied"));
		}
	}

	/**
	 * 게시글 삭제 처리
	 * 
	 * @param bbsId 게시판 ID
	 * @param contentId 게시글 ID
	 * @param domainSeq 도메인 시퀀스
	 * @param contentVo 게시글 VO
	 * @param bbsAdmin 관리자 여부
	 * @throws Exception
	 */
	private void deleteBoardContent(int bbsId, int contentId, int domainSeq, 
			BoardContentVO contentVo, boolean bbsAdmin) throws Exception {
		
		// 하위 게시글 체크
		boolean hasChildContents = checkChildContents(bbsId, contentId, domainSeq);
		
		if (hasChildContents && !bbsAdmin) {
			// 하위 게시글이 있는 경우 관리자만 삭제 가능
			throw new BbsAuthException("하위 게시글이 있어 삭제할 수 없습니다.");
		}
		
		// 첨부파일 삭제
		deleteAttachments(contentVo);
		
		// 게시글 삭제
		bbsManager.deleteBoardContent(bbsId, contentId, domainSeq);
	}

	/**
	 * 하위 게시글 존재 여부 체크
	 * 
	 * @param bbsId 게시판 ID
	 * @param contentId 게시글 ID
	 * @param domainSeq 도메인 시퀀스
	 * @return 하위 게시글 존재 여부
	 * @throws Exception
	 */
	private boolean checkChildContents(int bbsId, int contentId, int domainSeq) throws Exception {
		// 하위 게시글 조회
		List<BoardContentVO> childContents = bbsManager.readChildBoardContents(bbsId, contentId, domainSeq);
		return childContents != null && childContents.size() > 0;
	}

	/**
	 * 첨부파일 삭제
	 * 
	 * @param contentVo 게시글 VO
	 * @throws Exception
	 */
	private void deleteAttachments(BoardContentVO contentVo) throws Exception {
		// 첨부파일 삭제 로직 구현
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
