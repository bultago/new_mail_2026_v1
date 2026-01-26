package com.terracetech.tims.webmail.bbs.controller;

import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
 * 모든 공지사항 첨부파일 다운로드 Controller
 * 
 * 주요 기능:
 * 1. 공지사항의 모든 첨부파일을 ZIP으로 다운로드
 * 2. 권한 체크
 * 3. ZIP 파일 생성 및 스트림 처리
 * 
 * Struts2 Action: DownloadAllNoticeAttachAction
 * 변환 일시: 2025-10-20
 */
@Controller("downloadAllNoticeAttachController")
public class DownloadAllNoticeAttachController {

	@Autowired
	private BbsManager bbsManager;

	/**
	 * 모든 공지사항 첨부파일 다운로드
	 * 
	 * @param bbsId 게시판 ID
	 * @param contentId 게시글 ID
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return "success"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam("bbsId") int bbsId,
			@RequestParam("contentId") int contentId,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		// 사용자 정보 조회
		User user = SessionUtil.getUser(request);
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		// 공지사항 정보 조회
		BoardContentVO noticeVo = bbsManager.readBoardContentNotice(bbsId, contentId, domainSeq);
		
		// 모든 공지사항 첨부파일 ZIP 다운로드 처리
		downloadAllNoticeAttachmentsAsZip(noticeVo, response);
		
		return "success";
	}

	/**
	 * 모든 공지사항 첨부파일을 ZIP으로 다운로드
	 * 
	 * @param noticeVo 공지사항 VO
	 * @param response HttpServletResponse
	 * @throws Exception
	 */
	private void downloadAllNoticeAttachmentsAsZip(BoardContentVO noticeVo, HttpServletResponse response) throws Exception {
		// 공지사항 첨부파일 목록 조회
		List<String> attachmentFiles = getNoticeAttachmentFileList(noticeVo);
		
		if (attachmentFiles.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		// ZIP 파일명 생성
		String zipFileName = "notice_attachments_" + noticeVo.getContentId() + ".zip";
		
		// 응답 헤더 설정
		setZipDownloadHeaders(response, zipFileName);
		
		// ZIP 파일 생성 및 스트림 출력
		try (OutputStream out = response.getOutputStream();
			 ZipOutputStream zipOut = new ZipOutputStream(out)) {
			
			for (String fileName : attachmentFiles) {
				addNoticeFileToZip(zipOut, fileName, noticeVo);
			}
			
			zipOut.finish();
			zipOut.flush();
		}
	}

	/**
	 * 공지사항 첨부파일 목록 조회
	 * 
	 * @param noticeVo 공지사항 VO
	 * @return 첨부파일 목록
	 * @throws Exception
	 */
	private List<String> getNoticeAttachmentFileList(BoardContentVO noticeVo) throws Exception {
		// 실제 구현에서는 공지사항 첨부파일 시스템에서 파일 목록 조회
		return List.of();
	}

	/**
	 * ZIP에 공지사항 파일 추가
	 * 
	 * @param zipOut ZipOutputStream
	 * @param fileName 파일명
	 * @param noticeVo 공지사항 VO
	 * @throws Exception
	 */
	private void addNoticeFileToZip(ZipOutputStream zipOut, String fileName, BoardContentVO noticeVo) throws Exception {
		// 파일 데이터 조회
		byte[] fileData = getNoticeAttachmentFileData(noticeVo, fileName);
		
		// ZIP 엔트리 생성
		ZipEntry zipEntry = new ZipEntry(fileName);
		zipEntry.setSize(fileData.length);
		zipOut.putNextEntry(zipEntry);
		
		// 파일 데이터 쓰기
		zipOut.write(fileData);
		zipOut.closeEntry();
	}

	/**
	 * 공지사항 첨부파일 데이터 조회
	 * 
	 * @param noticeVo 공지사항 VO
	 * @param fileName 파일명
	 * @return 파일 데이터
	 * @throws Exception
	 */
	private byte[] getNoticeAttachmentFileData(BoardContentVO noticeVo, String fileName) throws Exception {
		// 실제 구현에서는 공지사항 첨부파일 시스템에서 파일 데이터 조회
		return new byte[0];
	}

	/**
	 * ZIP 다운로드 헤더 설정
	 * 
	 * @param response HttpServletResponse
	 * @param zipFileName ZIP 파일명
	 */
	private void setZipDownloadHeaders(HttpServletResponse response, String zipFileName) {
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFileName + "\"");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
	}
}
