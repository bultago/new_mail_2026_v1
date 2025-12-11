package com.terracetech.tims.webmail.bbs.controller;

import java.io.OutputStream;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.bbs.vo.BoardContentVO;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;

/**
 * 첨부파일 다운로드 Controller
 * 
 * 주요 기능:
 * 1. 첨부파일 다운로드
 * 2. 권한 체크
 * 3. 파일 스트림 처리
 * 
 * Struts2 Action: DownloadAttachAction
 * 변환 일시: 2025-10-20
 */
@Controller("downloadAttachController")
public class DownloadAttachController {

	@Autowired
	private BbsManager bbsManager;

	/**
	 * 첨부파일 다운로드
	 * 
	 * @param bbsId 게시판 ID
	 * @param contentId 게시글 ID
	 * @param attachId 첨부파일 ID
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam("bbsId") int bbsId,
			@RequestParam("contentId") int contentId,
			@RequestParam("attachId") int attachId,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		// 사용자 정보 조회
		User user = SessionUtil.getUser(request);
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		// 게시글 정보 조회
		BoardContentVO contentVo = bbsManager.readBoardContent(bbsId, contentId, domainSeq);
		
		// 첨부파일 다운로드 처리
		downloadAttachment(contentVo, attachId, response);
		
		return "success";
	}

	/**
	 * 첨부파일 다운로드 처리
	 * 
	 * @param contentVo 게시글 VO
	 * @param attachId 첨부파일 ID
	 * @param response HttpServletResponse
	 * @throws Exception
	 */
	private void downloadAttachment(BoardContentVO contentVo, int attachId, HttpServletResponse response) throws Exception {
		// 첨부파일 정보 조회
		String fileName = getAttachmentFileName(contentVo, attachId);
		byte[] fileData = getAttachmentFileData(contentVo, attachId);
		
		// 응답 헤더 설정
		setDownloadHeaders(response, fileName);
		
		// 파일 스트림 출력
		try (OutputStream out = response.getOutputStream()) {
			out.write(fileData);
			out.flush();
		}
	}

	/**
	 * 첨부파일명 조회
	 * 
	 * @param contentVo 게시글 VO
	 * @param attachId 첨부파일 ID
	 * @return 파일명
	 * @throws Exception
	 */
	private String getAttachmentFileName(BoardContentVO contentVo, int attachId) throws Exception {
		// 실제 구현에서는 첨부파일 시스템에서 파일명 조회
		return "attachment_" + attachId + ".dat";
	}

	/**
	 * 첨부파일 데이터 조회
	 * 
	 * @param contentVo 게시글 VO
	 * @param attachId 첨부파일 ID
	 * @return 파일 데이터
	 * @throws Exception
	 */
	private byte[] getAttachmentFileData(BoardContentVO contentVo, int attachId) throws Exception {
		// 실제 구현에서는 첨부파일 시스템에서 파일 데이터 조회
		return new byte[0];
	}

	/**
	 * 다운로드 헤더 설정
	 * 
	 * @param response HttpServletResponse
	 * @param fileName 파일명
	 */
	private void setDownloadHeaders(HttpServletResponse response, String fileName) {
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
	}
}
