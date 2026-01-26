package com.terracetech.tims.webmail.bbs.controller;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.bbs.exception.BoardContentNotFoundException;
import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.bbs.vo.BoardContentVO;
import com.terracetech.tims.webmail.bbs.vo.BoardVO;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MessageParserInfoBean;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringReplaceUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * 게시글 조회 Controller
 * 
 * 주요 기능:
 * 1. 게시글 상세 조회
 * 2. 이전/다음 게시글 조회
 * 3. 조회수 증가
 * 4. 권한 체크
 * 5. 팝업/인쇄 모드 지원
 * 
 * Struts2 Action: ViewContentAction
 * 변환 일시: 2025-10-20
 */
@Controller("viewContentController")
public class ViewContentController {

	@Autowired
	private BbsManager bbsManager;

	/**
	 * 게시글 조회
	 * 
	 * @param bbsId 게시판 ID
	 * @param contentId 게시글 ID
	 * @param parentId 부모 ID
	 * @param orderNo 순서 번호
	 * @param searchType 검색 타입
	 * @param keyWord 검색 키워드
	 * @param currentPage 현재 페이지
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success", "popup", "print"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam("bbsId") int bbsId,
			@RequestParam("contentId") int contentId,
			@RequestParam(value = "parentId", defaultValue = "0") int parentId,
			@RequestParam(value = "orderNo", defaultValue = "0") int orderNo,
			@RequestParam(value = "searchType", required = false) String searchType,
			@RequestParam(value = "keyWord", required = false) String keyWord,
			@RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
			HttpServletRequest request,
			Model model) throws Exception {
		
		// 사용자 정보 조회
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "bbs");
		
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		// 기본값 설정
		searchType = (searchType == null) ? "s" : searchType;
		String pbaseStr = user.get(User.PAGE_LINE_CNT);
		if (StringUtils.isEmpty(pbaseStr)) {
			pbaseStr = "15";
		}
		int pageBase = Integer.parseInt(pbaseStr);
		
		// 뷰 타입 결정
		String returnType = determineViewType(request);
		
		// 게시판 목록 및 정보 조회
		List<BoardVO> boardList = bbsManager.readBoardList(domainSeq);
		BoardVO boardVo = bbsManager.readBoard(bbsId, domainSeq);
		
		// 게시글 조회
		BoardContentVO contentVo = bbsManager.readBoardContent(bbsId, contentId, domainSeq);
		
		// 작성자 체크
		if (userSeq == contentVo.getCreatorSeq()) {
			contentVo.setCreator(true);
		}
		
		// 권한 체크
		boolean bbsAdmin = checkBbsAdmin(bbsId, domainSeq, userSeq);
		boolean bbsCreator = checkBbsCreator(bbsId, domainSeq, userSeq);
		
		// 게시글 내용 처리
		processContent(bbsId, contentVo, boardVo, user, request, bbsAdmin);
		
		// 이전/다음 게시글 조회
		List<BoardContentVO> prevContent = getPrevContent(bbsId, contentId, domainSeq, userSeq, bbsAdmin, boardVo.getBbsType());
		List<BoardContentVO> nextContent = getNextContent(bbsId, contentId, domainSeq, userSeq, bbsAdmin, boardVo.getBbsType());
		
		// Model에 데이터 추가
		model.addAttribute("boardList", boardList);
		model.addAttribute("boardVo", boardVo);
		model.addAttribute("contentVo", contentVo);
		model.addAttribute("prevContent", prevContent);
		model.addAttribute("nextContent", nextContent);
		model.addAttribute("bbsAdmin", bbsAdmin);
		model.addAttribute("bbsCreator", bbsCreator);
		
		return returnType;
	}

	/**
	 * 뷰 타입 결정
	 * 
	 * @param request HttpServletRequest
	 * @return 뷰 타입
	 */
	private String determineViewType(HttpServletRequest request) {
		String readType = request.getParameter("readType");
		
		if (StringUtils.isNotEmpty(readType)) {
			if ("popup".equals(readType) || "preview".equals(readType)) {
				return "popup";
			}
			if ("print".equals(readType)) {
				return "print";
			}
		}
		
		return "success";
	}

	/**
	 * BBS 관리자 체크
	 * 
	 * @param bbsId 게시판 ID
	 * @param domainSeq 도메인 시퀀스
	 * @param userSeq 사용자 시퀀스
	 * @return 관리자 여부
	 * @throws Exception
	 */
	private boolean checkBbsAdmin(int bbsId, int domainSeq, int userSeq) throws Exception {
		Map<String, String> bbsAuth = bbsManager.getBoardAuth(bbsId, domainSeq, userSeq);
		return "T".equals(bbsAuth.get("isAdmin"));
	}

	/**
	 * BBS 생성자 체크
	 * 
	 * @param bbsId 게시판 ID
	 * @param domainSeq 도메인 시퀀스
	 * @param userSeq 사용자 시퀀스
	 * @return 생성자 여부
	 * @throws Exception
	 */
	private boolean checkBbsCreator(int bbsId, int domainSeq, int userSeq) throws Exception {
		Map<String, String> bbsAuth = bbsManager.getBoardAuth(bbsId, domainSeq, userSeq);
		return "T".equals(bbsAuth.get("isCreator"));
	}

	/**
	 * 게시글 내용 처리
	 * 
	 * @param bbsId 게시판 ID
	 * @param contentVo 게시글 VO
	 * @param boardVo 게시판 VO
	 * @param user 사용자
	 * @param request HttpServletRequest
	 * @param bbsAdmin 관리자 여부
	 * @throws Exception
	 */
	private void processContent(int bbsId, BoardContentVO contentVo, BoardVO boardVo, 
			User user, HttpServletRequest request, boolean bbsAdmin) throws Exception {
		
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailStore store = null;
		
		try {
			if ("text".equals(contentVo.getContentType())) {
				// 텍스트 타입 처리
				processTextContent(contentVo);
			} else {
				// HTML 타입 처리
				processHtmlContent(bbsId, contentVo, boardVo, user, request, factory);
			}
		} catch (NumberFormatException e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new BoardContentNotFoundException("게시글을 찾을 수 없습니다.");
		} finally {
			if (store != null && store.isConnected()) {
				store.close();
			}
		}
	}

	/**
	 * 텍스트 내용 처리
	 * 
	 * @param contentVo 게시글 VO
	 * @throws Exception
	 */
	private void processTextContent(BoardContentVO contentVo) throws Exception {
		String content = "";
		if (contentVo.getContent() != null && contentVo.getContent().length > 0) {
			StringReplaceUtil sr = new StringReplaceUtil();
			content = sr.replace(new String(contentVo.getContent(), "UTF-8"));
		}
		contentVo.setHtmlContent(content);
	}

	/**
	 * HTML 내용 처리
	 * 
	 * @param bbsId 게시판 ID
	 * @param contentVo 게시글 VO
	 * @param boardVo 게시판 VO
	 * @param user 사용자
	 * @param request HttpServletRequest
	 * @param factory TMailStoreFactory
	 * @throws Exception
	 */
	private void processHtmlContent(int bbsId, BoardContentVO contentVo, BoardVO boardVo, 
			User user, HttpServletRequest request, TMailStoreFactory factory) throws Exception {
		
		Map<String, String> confMap = bbsManager.getBbsConnectionInfo(user.get(User.MAIL_DOMAIN));
		TMailStore store = factory.connect(request.getRemoteAddr(), confMap);
		TMailFolder folder = store.getFolder(Integer.toString(bbsId));
		
		// 첨부파일 정보 설정
		String attachesDir = request.getServletContext().getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
		String hostStr = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		
		MessageParserInfoBean infoBean = createMessageParserInfo(attachesDir, hostStr, user);
		
		contentVo = bbsManager.readBoardContent(folder, contentVo, infoBean);
	}

	/**
	 * MessageParserInfo 생성
	 * 
	 * @param attachesDir 첨부파일 디렉토리
	 * @param hostStr 호스트 문자열
	 * @param user 사용자
	 * @return MessageParserInfoBean
	 */
	private MessageParserInfoBean createMessageParserInfo(String attachesDir, String hostStr, User user) {
		MessageParserInfoBean infoBean = new MessageParserInfoBean();
		infoBean.setAttachesDir(attachesDir);
		infoBean.setAttachesUrl(EnvConstants.getAttachSetting("attach.url"));
		infoBean.setDefaultCharset(EnvConstants.getMailSetting("default.charset"));
		infoBean.setDefaultImg("/design/common/images/blank.gif");
		infoBean.setHiddenImg(false);
		infoBean.setHiddenTag(false);
		infoBean.setLocale(new Locale(EnvConstants.getMailSetting("default.charset")));
		infoBean.setStrLocalhost(hostStr);
		infoBean.setUserId(user.get(User.MAIL_UID));
		return infoBean;
	}

	/**
	 * 이전 게시글 조회
	 * 
	 * @param bbsId 게시판 ID
	 * @param contentId 게시글 ID
	 * @param domainSeq 도메인 시퀀스
	 * @param userSeq 사용자 시퀀스
	 * @param bbsAdmin 관리자 여부
	 * @param bbsType 게시판 타입
	 * @return 이전 게시글 목록
	 * @throws Exception
	 */
	private List<BoardContentVO> getPrevContent(int bbsId, int contentId, int domainSeq, 
			int userSeq, boolean bbsAdmin, String bbsType) throws Exception {
		
		int creatorSeq = 0;
		if (!bbsAdmin && "secret".equals(bbsType)) {
			creatorSeq = userSeq;
		}
		
		return bbsManager.readPrevBoardContent(bbsId, contentId, domainSeq, creatorSeq);
	}

	/**
	 * 다음 게시글 조회
	 * 
	 * @param bbsId 게시판 ID
	 * @param contentId 게시글 ID
	 * @param domainSeq 도메인 시퀀스
	 * @param userSeq 사용자 시퀀스
	 * @param bbsAdmin 관리자 여부
	 * @param bbsType 게시판 타입
	 * @return 다음 게시글 목록
	 * @throws Exception
	 */
	private List<BoardContentVO> getNextContent(int bbsId, int contentId, int domainSeq, 
			int userSeq, boolean bbsAdmin, String bbsType) throws Exception {
		
		int creatorSeq = 0;
		if (!bbsAdmin && "secret".equals(bbsType)) {
			creatorSeq = userSeq;
		}
		
		return bbsManager.readNextBoardContent(bbsId, contentId, domainSeq, creatorSeq);
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
