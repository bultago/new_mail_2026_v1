package com.terracetech.tims.webmail.bbs.controller;

import java.util.Date;
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
 * 답글 저장 Controller
 * 
 * 주요 기능:
 * 1. 답글 작성 저장
 * 2. 답글 수정 저장
 * 3. 권한 체크
 * 4. 순서 번호 처리
 * 
 * Struts2 Action: SaveContentReplyAction
 * 변환 일시: 2025-10-20
 */
@Controller("saveContentReplyController")
public class SaveContentReplyController {

	@Autowired
	private BbsManager bbsManager;

	/**
	 * 답글 저장
	 * 
	 * @param bbsId 게시판 ID
	 * @param contentId 게시글 ID
	 * @param replyId 답글 ID (수정 시)
	 * @param subject 제목
	 * @param content 내용
	 * @param contentType 내용 타입
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success" or "fail"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam("bbsId") int bbsId,
			@RequestParam("contentId") int contentId,
			@RequestParam(value = "replyId", defaultValue = "0") int replyId,
			@RequestParam("subject") String subject,
			@RequestParam("content") String content,
			@RequestParam(value = "contentType", defaultValue = "html") String contentType,
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
		
		// 답글 저장 처리
		int savedReplyId = saveReply(bbsId, contentId, replyId, subject, content, 
				contentType, user, domainSeq);
		
		// Model에 데이터 추가
		model.addAttribute("boardList", boardList);
		model.addAttribute("boardVo", boardVo);
		model.addAttribute("bbsAdmin", bbsAdmin);
		model.addAttribute("bbsCreator", bbsCreator);
		model.addAttribute("bbsId", bbsId);
		model.addAttribute("contentId", contentId);
		model.addAttribute("replyId", savedReplyId);
		
		return "success";
	}

	/**
	 * 답글 저장 처리
	 * 
	 * @param bbsId 게시판 ID
	 * @param contentId 게시글 ID
	 * @param replyId 답글 ID
	 * @param subject 제목
	 * @param content 내용
	 * @param contentType 내용 타입
	 * @param user 사용자
	 * @param domainSeq 도메인 시퀀스
	 * @return 저장된 답글 ID
	 * @throws Exception
	 */
	private int saveReply(int bbsId, int contentId, int replyId, String subject, 
			String content, String contentType, User user, int domainSeq) throws Exception {
		
		BoardContentVO replyVo = createReplyVO(bbsId, contentId, replyId, subject, 
				content, contentType, user);
		
		if (replyId > 0) {
			// 답글 수정
			bbsManager.updateBoardContentReply(replyVo, domainSeq);
		} else {
			// 답글 작성
			bbsManager.createBoardContentReply(replyVo, domainSeq);
		}
		
		return replyVo.getContentId();
	}

	/**
	 * 답글 VO 생성
	 * 
	 * @param bbsId 게시판 ID
	 * @param contentId 게시글 ID
	 * @param replyId 답글 ID
	 * @param subject 제목
	 * @param content 내용
	 * @param contentType 내용 타입
	 * @param user 사용자
	 * @return BoardContentVO
	 */
	private BoardContentVO createReplyVO(int bbsId, int contentId, int replyId, 
			String subject, String content, String contentType, User user) {
		
		BoardContentVO replyVo = new BoardContentVO();
		
		replyVo.setBbsId(bbsId);
		replyVo.setContentId(replyId);
		replyVo.setParentId(contentId);
		replyVo.setSubject(subject);
		replyVo.setContent(content.getBytes());
		replyVo.setContentType(contentType);
		replyVo.setCreatorSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		replyVo.setCreatorName(user.get(User.MAIL_USER_NAME));
		replyVo.setCreatorEmail(user.get(User.MAIL_UID));
		replyVo.setCreateDate(new Date());
		replyVo.setModifyDate(new Date());
		
		return replyVo;
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
