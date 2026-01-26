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
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * 게시글 작성 Controller
 * 
 * 주요 기능:
 * 1. 게시글 작성 폼 표시
 * 2. 답글 작성 폼 표시
 * 3. 수정 폼 표시
 * 4. 권한 체크
 * 5. 게시판 정보 조회
 * 
 * Struts2 Action: WriteContentAction
 * 변환 일시: 2025-10-20
 */
@Controller("writeContentController")
public class WriteContentController {

	@Autowired
	private BbsManager bbsManager;

	/**
	 * 게시글 작성 폼
	 * 
	 * @param bbsId 게시판 ID
	 * @param contentId 게시글 ID (수정 시)
	 * @param parentId 부모 ID (답글 시)
	 * @param orderNo 순서 번호 (답글 시)
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "bbsId", defaultValue = "0") int bbsId,
			@RequestParam(value = "contentId", defaultValue = "0") int contentId,
			@RequestParam(value = "parentId", defaultValue = "0") int parentId,
			@RequestParam(value = "orderNo", defaultValue = "0") int orderNo,
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
		
		// 권한 체크
		Map<String, String> bbsAuth = bbsManager.getBoardAuth(bbsId, domainSeq, userSeq);
		boolean bbsAdmin = "T".equals(bbsAuth.get("isAdmin"));
		boolean bbsCreator = "T".equals(bbsAuth.get("isCreator"));
		
		// 게시글 모드 결정
		String mode = determineContentMode(contentId, parentId);
		
		// 게시글 정보 설정
		BoardContentVO contentVo = createContentVO(mode, contentId, parentId, orderNo, bbsId, domainSeq, user);
		
		// Model에 데이터 추가
		model.addAttribute("boardList", boardList);
		model.addAttribute("boardVo", boardVo);
		model.addAttribute("contentVo", contentVo);
		model.addAttribute("bbsAdmin", bbsAdmin);
		model.addAttribute("bbsCreator", bbsCreator);
		model.addAttribute("mode", mode);
		model.addAttribute("bbsId", bbsId);
		
		return "success";
	}

	/**
	 * 게시글 모드 결정
	 * 
	 * @param contentId 게시글 ID
	 * @param parentId 부모 ID
	 * @return 모드 ("write", "reply", "modify")
	 */
	private String determineContentMode(int contentId, int parentId) {
		if (contentId > 0) {
			return "modify";
		} else if (parentId > 0) {
			return "reply";
		} else {
			return "write";
		}
	}

	/**
	 * 게시글 VO 생성
	 * 
	 * @param mode 모드
	 * @param contentId 게시글 ID
	 * @param parentId 부모 ID
	 * @param orderNo 순서 번호
	 * @param bbsId 게시판 ID
	 * @param domainSeq 도메인 시퀀스
	 * @param user 사용자
	 * @return BoardContentVO
	 * @throws Exception
	 */
	private BoardContentVO createContentVO(String mode, int contentId, int parentId, 
			int orderNo, int bbsId, int domainSeq, User user) throws Exception {
		
		BoardContentVO contentVo = new BoardContentVO();
		
		switch (mode) {
			case "modify":
				// 수정 모드 - 기존 게시글 조회
				contentVo = bbsManager.readBoardContent(bbsId, contentId, domainSeq);
				break;
				
			case "reply":
				// 답글 모드 - 부모 게시글 정보 설정
				BoardContentVO parentContent = bbsManager.readBoardContent(bbsId, parentId, domainSeq);
				contentVo.setParentId(parentId);
				contentVo.setOrderNo(orderNo);
				contentVo.setSubject("RE: " + parentContent.getSubject());
				break;
				
			case "write":
			default:
				// 작성 모드 - 기본값 설정
				contentVo.setBbsId(bbsId);
				break;
		}
		
		// 공통 정보 설정
		contentVo.setCreatorSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		contentVo.setCreatorName(user.get(User.MAIL_USER_NAME));
		
		return contentVo;
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
