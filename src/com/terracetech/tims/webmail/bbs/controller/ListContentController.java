package com.terracetech.tims.webmail.bbs.controller;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.common.PageManager;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.bbs.vo.BoardContentVO;
import com.terracetech.tims.webmail.bbs.vo.BoardVO;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mail.ibean.MailSortMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * 게시글 목록 조회 Controller
 * 
 * 주요 기능:
 * 1. 게시글 목록 조회
 * 2. 공지사항 조회
 * 3. 검색 기능
 * 4. 페이징 처리
 * 5. 권한 체크
 * 
 * Struts2 Action: ListContentAction
 * 변환 일시: 2025-10-20
 */
@Controller("listContentController")
public class ListContentController {

	@Autowired
	private BbsManager bbsManager;

	/**
	 * 게시글 목록 조회
	 * 
	 * @param searchType 검색 타입
	 * @param keyWord 검색 키워드
	 * @param currentPage 현재 페이지
	 * @param bbsId 게시판 ID
	 * @param readType 읽기 타입
	 * @param bbsUid 게시판 UID
	 * @param noticePageBase 공지사항 페이지 크기
	 * @param pbase 페이지 크기
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success" or "fail"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "searchType", required = false) String searchType,
			@RequestParam(value = "keyWord", required = false) String keyWord,
			@RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
			@RequestParam(value = "bbsId", defaultValue = "0") int bbsId,
			@RequestParam(value = "readType", required = false) String readType,
			@RequestParam(value = "bbsUid", defaultValue = "0") int bbsUid,
			@RequestParam(value = "noticePageBase", defaultValue = "0") int noticePageBase,
			@RequestParam(value = "pbase", defaultValue = "0") int pbase,
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
		bbsId = (bbsId == 0) ? boardList.get(0).getBbsId() : bbsId;
		searchType = (searchType == null) ? "s" : searchType;
		currentPage = (currentPage == 0) ? 1 : currentPage;
		noticePageBase = (noticePageBase == 0) ? 5 : noticePageBase;
		
		// 포틀릿 모드 처리
		if ("portlet".equalsIgnoreCase(readType)) {
			bbsId = bbsUid;
		}
		
		// 권한 체크
		Map<String, String> bbsAuth = bbsManager.getBoardAuth(bbsId, domainSeq, userSeq);
		boolean bbsAdmin = "T".equals(bbsAuth.get("isAdmin"));
		boolean bbsCreator = "T".equals(bbsAuth.get("isCreator"));
		
		// 게시판 정보 조회
		BoardVO boardVO = bbsManager.readBoard(bbsId, domainSeq);
		String bbsType = boardVO.getBbsType();
		String pbaseStr = user.get(User.PAGE_LINE_CNT);
		if (StringUtils.isEmpty(pbaseStr)) {
			pbaseStr = "15";
		}
		int pageBase = Integer.parseInt(pbaseStr);
		pbase = (pbase == 0) ? pageBase : pbase;
		
		// 공지사항 목록 조회
		List<BoardContentVO> contentnoticeList = bbsManager.readBoardContentNoticeList(bbsId, domainSeq, noticePageBase);
		
		// 게시글 목록 조회
		List<BoardContentVO> contentList;
		MailSortMessageBean[] messageBeans = null;
		int contentListCount;
		
		int creatorSeq = 0;
		if (!bbsAdmin && "secret".equals(bbsType)) {
			creatorSeq = userSeq;
		}
		
		if (StringUtils.isEmpty(keyWord) || "secret".equalsIgnoreCase(boardVO.getBbsType())) {
			// 일반 목록 조회
			if ("7".equals(searchType)) {
				keyWord = user.get(User.MAIL_USER_SEQ);
			}
			
			int skipResult = (currentPage - 1) * pbase;
			contentList = bbsManager.readBoardContentList(bbsId, domainSeq, creatorSeq, searchType, keyWord, skipResult, pbase);
			contentListCount = bbsManager.readBoardContentListCount(bbsId, domainSeq, creatorSeq, searchType, keyWord);
		} else {
			// 검색 목록 조회
			contentListCount = performAdvancedSearch(bbsId, domainSeq, user, searchType, keyWord, currentPage, pbase, bbsManager);
			contentList = getContentListFromSearch(bbsId, domainSeq, messageBeans, bbsManager);
		}
		
		// 페이징 정보 생성
		PageManager pm = createPageManager(contentListCount, pbase, currentPage);
		
		// 세션에 게시판 ID 저장
		HttpSession session = request.getSession();
		session.setAttribute("bbsId", bbsId);
		
		// Model에 데이터 추가
		model.addAttribute("boardList", boardList);
		model.addAttribute("contentnoticeList", contentnoticeList);
		model.addAttribute("contentList", contentList);
		model.addAttribute("boardVO", boardVO);
		model.addAttribute("bbsAdmin", bbsAdmin);
		model.addAttribute("bbsCreator", bbsCreator);
		model.addAttribute("pm", pm);
		model.addAttribute("pageBase", pbase);
		model.addAttribute("totalCount", contentListCount);
		model.addAttribute("part", request.getParameter("part"));
		
		return "success";
	}

	/**
	 * 고급 검색 수행
	 * 
	 * @param bbsId 게시판 ID
	 * @param domainSeq 도메인 시퀀스
	 * @param user 사용자
	 * @param searchType 검색 타입
	 * @param keyWord 검색 키워드
	 * @param currentPage 현재 페이지
	 * @param pbase 페이지 크기
	 * @param bbsManager BBS Manager
	 * @return 총 개수
	 * @throws Exception
	 */
	private int performAdvancedSearch(int bbsId, int domainSeq, User user, String searchType, 
			String keyWord, int currentPage, int pbase, BbsManager bbsManager) throws Exception {
		
		TMailStoreFactory factory = new TMailStoreFactory();
		Map<String, String> confMap = bbsManager.getBbsConnectionInfo(user.get(User.MAIL_DOMAIN));
		TMailStore store = factory.connect(null, confMap);
		
		try {
			bbsManager.setCommandResource(store, Integer.toString(bbsId), getMessageResource(user, "bbs"));
			
			MessageSortInfoBean sortBean = new MessageSortInfoBean();
			sortBean.setSortBy("date");
			sortBean.setSortDir("desc");
			sortBean.setAdSearchCategory(searchType);
			sortBean.setAdvanceMode(true);
			sortBean.setAdSearchPattern(keyWord);
			sortBean.setPage(Integer.toString(currentPage));
			sortBean.setPageBase(Integer.toString(pbase));
			
			if ("c".equals(searchType)) {
				sortBean.setAdFromEmailPattern(keyWord);
			}
			
			MailSortMessageBean[] messageBeans = bbsManager.getXSortMessageBeans(Integer.toString(bbsId), sortBean);
			return bbsManager.getXCommandTotal();
		} finally {
			if (store != null && store.isConnected()) {
				store.close();
			}
		}
	}

	/**
	 * 검색 결과에서 게시글 목록 조회
	 * 
	 * @param bbsId 게시판 ID
	 * @param domainSeq 도메인 시퀀스
	 * @param messageBeans 메시지 빈 배열
	 * @param bbsManager BBS Manager
	 * @return 게시글 목록
	 * @throws Exception
	 */
	private List<BoardContentVO> getContentListFromSearch(int bbsId, int domainSeq, 
			MailSortMessageBean[] messageBeans, BbsManager bbsManager) throws Exception {
		
		if (messageBeans != null && messageBeans.length > 0) {
			String[] messageIds = new String[messageBeans.length];
			for (int i = 0; i < messageBeans.length; i++) {
				messageIds[i] = messageBeans[i].getMessage().getMessageID();
			}
			return bbsManager.readBoardContentListByMessageIds(bbsId, domainSeq, messageIds);
		}
		
		return null;
	}

	/**
	 * PageManager 생성
	 * 
	 * @param totalCount 총 개수
	 * @param pageBase 페이지 크기
	 * @param currentPage 현재 페이지
	 * @return PageManager
	 */
	private PageManager createPageManager(int totalCount, int pageBase, int currentPage) {
		PageManager pm = new PageManager();
		pm.initParameter(totalCount, pageBase, 10);
		pm.setPage(currentPage);
		return pm;
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
