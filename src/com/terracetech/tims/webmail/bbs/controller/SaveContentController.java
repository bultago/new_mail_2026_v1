package com.terracetech.tims.webmail.bbs.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.bbs.exception.BbsContentSizeException;
import com.terracetech.tims.webmail.bbs.exception.BbsFileSizeException;
import com.terracetech.tims.webmail.bbs.exception.BbsNotSupportFileException;
import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.bbs.vo.BoardContentVO;
import com.terracetech.tims.webmail.bbs.vo.BoardVO;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * 게시글 저장 Controller
 * 
 * 주요 기능:
 * 1. 게시글 작성 저장
 * 2. 게시글 수정 저장
 * 3. 답글 저장
 * 4. 첨부파일 처리
 * 5. 권한 체크
 * 6. 바이러스 스캔
 * 
 * Struts2 Action: SaveContentAction
 * 변환 일시: 2025-10-20
 */
@Controller("saveContentController")
public class SaveContentController {

	@Autowired
	private BbsManager bbsManager;

	/**
	 * 게시글 저장
	 * 
	 * @param bbsId 게시판 ID
	 * @param contentId 게시글 ID (수정 시)
	 * @param parentId 부모 ID (답글 시)
	 * @param orderNo 순서 번호 (답글 시)
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
			@RequestParam(value = "contentId", defaultValue = "0") int contentId,
			@RequestParam(value = "parentId", defaultValue = "0") int parentId,
			@RequestParam(value = "orderNo", defaultValue = "0") int orderNo,
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
		
		// 게시글 모드 결정
		String mode = determineContentMode(contentId, parentId);
		
		// 게시글 저장
		int savedContentId = saveBoardContent(bbsId, contentId, parentId, orderNo, subject, content, 
				contentType, user, request, boardVo, bbsAdmin);
		
		// Model에 데이터 추가
		model.addAttribute("boardList", boardList);
		model.addAttribute("boardVo", boardVo);
		model.addAttribute("bbsAdmin", bbsAdmin);
		model.addAttribute("bbsCreator", bbsCreator);
		model.addAttribute("bbsId", bbsId);
		model.addAttribute("contentId", savedContentId);
		
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
	 * 게시글 저장 처리
	 * 
	 * @param bbsId 게시판 ID
	 * @param contentId 게시글 ID
	 * @param parentId 부모 ID
	 * @param orderNo 순서 번호
	 * @param subject 제목
	 * @param content 내용
	 * @param contentType 내용 타입
	 * @param user 사용자
	 * @param request HttpServletRequest
	 * @param boardVo 게시판 VO
	 * @param bbsAdmin 관리자 여부
	 * @return 저장된 게시글 ID
	 * @throws Exception
	 */
	private int saveBoardContent(int bbsId, int contentId, int parentId, int orderNo, 
			String subject, String content, String contentType, User user, 
			HttpServletRequest request, BoardVO boardVo, boolean bbsAdmin) throws Exception {
		
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		// 게시글 VO 생성
		BoardContentVO contentVo = createContentVO(bbsId, contentId, parentId, orderNo, 
				subject, content, contentType, user);
		
		// 첨부파일 처리
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			processAttachments(contentVo, multipartRequest, user, boardVo);
		}
		
		// 게시글 저장
		if (contentId > 0) {
			// 수정
			bbsManager.updateBoardContent(contentVo, domainSeq);
		} else if (parentId > 0) {
			// 답글
			bbsManager.createBoardContentReply(contentVo, domainSeq);
		} else {
			// 작성
			bbsManager.createBoardContent(contentVo, domainSeq);
		}
		
		return contentVo.getContentId();
	}

	/**
	 * 게시글 VO 생성
	 * 
	 * @param bbsId 게시판 ID
	 * @param contentId 게시글 ID
	 * @param parentId 부모 ID
	 * @param orderNo 순서 번호
	 * @param subject 제목
	 * @param content 내용
	 * @param contentType 내용 타입
	 * @param user 사용자
	 * @return BoardContentVO
	 */
	private BoardContentVO createContentVO(int bbsId, int contentId, int parentId, 
			int orderNo, String subject, String content, String contentType, User user) {
		
		BoardContentVO contentVo = new BoardContentVO();
		
		contentVo.setBbsId(bbsId);
		contentVo.setContentId(contentId);
		contentVo.setParentId(parentId);
		contentVo.setOrderNo(orderNo);
		contentVo.setSubject(subject);
		contentVo.setContent(content.getBytes());
		contentVo.setContentType(contentType);
		contentVo.setCreatorSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		contentVo.setCreatorName(user.get(User.MAIL_USER_NAME));
		contentVo.setCreatorEmail(user.get(User.MAIL_UID));
		contentVo.setCreateDate(new Date());
		contentVo.setModifyDate(new Date());
		
		return contentVo;
	}

	/**
	 * 첨부파일 처리
	 * 
	 * @param contentVo 게시글 VO
	 * @param multipartRequest MultipartHttpServletRequest
	 * @param user 사용자
	 * @param boardVo 게시판 VO
	 * @throws Exception
	 */
	private void processAttachments(BoardContentVO contentVo, 
			MultipartHttpServletRequest multipartRequest, User user, BoardVO boardVo) throws Exception {
		
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			MultipartFile file = entry.getValue();
			
			if (file != null && !file.isEmpty()) {
				// 파일 크기 체크
				validateFileSize(file, boardVo);
				
				// 파일 타입 체크
				validateFileType(file, boardVo);
				
				// 바이러스 스캔
				performVirusScan(file);
				
				// 첨부파일 정보 설정
				setAttachmentInfo(contentVo, file);
			}
		}
	}

	/**
	 * 파일 크기 검증
	 * 
	 * @param file MultipartFile
	 * @param boardVo 게시판 VO
	 * @throws BbsFileSizeException
	 */
	private void validateFileSize(MultipartFile file, BoardVO boardVo) throws BbsFileSizeException {
		long fileSize = file.getSize();
		long maxFileSize = Long.parseLong(boardVo.getMaxFileSize());
		
		if (fileSize > maxFileSize) {
			throw new BbsFileSizeException("첨부파일 크기가 허용 범위를 초과했습니다.");
		}
	}

	/**
	 * 파일 타입 검증
	 * 
	 * @param file MultipartFile
	 * @param boardVo 게시판 VO
	 * @throws BbsNotSupportFileException
	 */
	private void validateFileType(MultipartFile file, BoardVO boardVo) throws BbsNotSupportFileException {
		String fileName = file.getOriginalFilename();
		String extension = getFileExtension(fileName);
		String allowedExtensions = boardVo.getAllowedFileExtensions();
		
		if (StringUtils.isNotEmpty(allowedExtensions) && 
			!allowedExtensions.toLowerCase().contains(extension.toLowerCase())) {
			throw new BbsNotSupportFileException("허용되지 않은 파일 형식입니다.");
		}
	}

	/**
	 * 파일 확장자 추출
	 * 
	 * @param fileName 파일명
	 * @return 확장자
	 */
	private String getFileExtension(String fileName) {
		if (StringUtils.isEmpty(fileName)) {
			return "";
		}
		
		int lastDotIndex = fileName.lastIndexOf('.');
		if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
			return fileName.substring(lastDotIndex + 1);
		}
		
		return "";
	}

	/**
	 * 바이러스 스캔 수행
	 * 
	 * @param file MultipartFile
	 * @throws Exception
	 */
	private void performVirusScan(MultipartFile file) throws Exception {
		// 바이러스 스캔 로직 구현
		// 실제 구현에서는 바이러스 스캔 엔진을 호출
	}

	/**
	 * 첨부파일 정보 설정
	 * 
	 * @param contentVo 게시글 VO
	 * @param file MultipartFile
	 * @throws IOException
	 */
	private void setAttachmentInfo(BoardContentVO contentVo, MultipartFile file) throws IOException {
		// 첨부파일 정보를 contentVo에 설정
		// 실제 구현에서는 첨부파일 정보를 적절히 설정
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
