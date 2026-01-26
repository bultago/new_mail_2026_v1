package com.terracetech.tims.webmail.addrbook.controller;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.SocketException;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.manager.vendors.EmailVendorFactory;
import com.terracetech.tims.webmail.addrbook.manager.vendors.IEmailVendor;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * 주소록 내보내기 Controller
 * 
 * 주요 기능:
 * 1. 주소록을 CSV 파일로 내보내기
 * 2. 인코딩 처리 (shift-jis, euc-kr, utf-8)
 * 3. 벤더별 포맷 지원 (Outlook, Gmail 등)
 * 4. 초성 필터링
 * 5. 개인/공유 주소록 지원
 * 6. 파일 다운로드
 * 
 * Struts2 Action: ExportAddrAction
 * 변환 일시: 2025-10-20
 */
@Controller("exportAddrController")
public class ExportAddrController {

	@Autowired
	private AddressBookManager addressBookManager;

	/**
	 * 주소록 내보내기
	 * 
	 * @param theGroupSeq 그룹 시퀀스
	 * @param theBookSeq 주소록 시퀀스 (0: 개인, 그 외: 공유)
	 * @param theVendor 벤더 타입
	 * @param theEncoding 인코딩
	 * @param leadingPattern 초성 패턴
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param model Model
	 * @return null (파일 다운로드 직접 처리)
	 * @throws Exception
	 */
	public String execute(
			@RequestParam("theGroupSeq") int theGroupSeq,
			@RequestParam("theBookSeq") int theBookSeq,
			@RequestParam("theVendor") int theVendor,
			@RequestParam(value = "theEncoding", required = false) String theEncoding,
			@RequestParam(value = "leadingPattern", required = false) String leadingPattern,
			HttpServletRequest request,
			HttpServletResponse response,
			Model model) throws Exception {
		
		// 사용자 정보 조회
		User user = SessionUtil.getUser(request);
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		// 인코딩 및 로케일 결정
		EncodingInfo encodingInfo = determineEncodingAndLocale(theEncoding, user);
		theEncoding = encodingInfo.encoding;
		Locale locale = encodingInfo.locale;
		
		// 리소스 생성
		I18nResources resource = new I18nResources(locale, "addr");
		
		// 멤버 목록 조회
		List<AddressBookMemberVO> members = getMemberList(theBookSeq, theGroupSeq, 
				userSeq, leadingPattern);
		
		// CSV 데이터 생성
		IEmailVendor vendor = EmailVendorFactory.getEmailVendor(resource, theEncoding, theVendor);
		StringBuffer result = vendor.getAddrCSVDownload(members);
		
		// 파일 다운로드
		downloadCSV(result, theEncoding, response);
		
		return null;
	}

	/**
	 * 인코딩 및 로케일 결정
	 * 
	 * @param theEncoding 지정된 인코딩
	 * @param user User
	 * @return EncodingInfo
	 */
	private EncodingInfo determineEncodingAndLocale(String theEncoding, User user) {
		EncodingInfo info = new EncodingInfo();
		
		if (StringUtils.isEmpty(theEncoding)) {
			// 사용자 로케일 기반 인코딩 결정
			Locale locale = new Locale(user.get(User.LOCALE));
			String localeStr = user.get(User.LOCALE);
			
			if ("jp".equals(localeStr)) {
				info.encoding = "shift-jis";
			} else if ("ko".equals(localeStr)) {
				info.encoding = "euc-kr";
			} else {
				info.encoding = "utf-8";
			}
			info.locale = locale;
		} else {
			// 인코딩 기반 로케일 결정
			info.encoding = theEncoding;
			
			if ("shift-jis".equals(theEncoding)) {
				info.locale = new Locale("jp");
			} else if ("euc-kr".equals(theEncoding)) {
				info.locale = new Locale("ko");
			} else {
				info.locale = new Locale("en");
			}
		}
		
		return info;
	}

	/**
	 * 멤버 목록 조회
	 * 
	 * @param theBookSeq 주소록 시퀀스
	 * @param theGroupSeq 그룹 시퀀스
	 * @param userSeq 사용자 시퀀스
	 * @param leadingPattern 초성 패턴
	 * @return 멤버 목록
	 * @throws Exception
	 */
	private List<AddressBookMemberVO> getMemberList(int theBookSeq, int theGroupSeq, 
			int userSeq, String leadingPattern) throws Exception {
		
		if (theBookSeq == 0) {
			// 개인 주소록
			return addressBookManager.readPrivateMemberListByIndex(userSeq, theGroupSeq, 
					leadingPattern, 1, 900000, "", "");
		} else {
			// 공유 주소록
			return addressBookManager.readSharedMemberListByIndex(theBookSeq, theGroupSeq, 
					leadingPattern, 1, 900000, "", "");
		}
	}

	/**
	 * CSV 파일 다운로드
	 * 
	 * @param result CSV 데이터
	 * @param theEncoding 인코딩
	 * @param response HttpServletResponse
	 * @throws Exception
	 */
	private void downloadCSV(StringBuffer result, String theEncoding, HttpServletResponse response) 
			throws Exception {
		
		Reader in = null;
		Writer out = null;
		
		try {
			// Reader, Writer 생성
			in = new InputStreamReader(
					new ByteArrayInputStream(result.toString().getBytes("utf-8")), "utf-8");
			out = new OutputStreamWriter(
					new BufferedOutputStream(response.getOutputStream()), theEncoding);
			
			// 파일명 생성
			String fileName = "Addr_" + theEncoding + "_" + FormatUtil.getBasicDateStr() + ".csv";
			
			// 응답 헤더 설정
			response.setCharacterEncoding(theEncoding);
			response.setHeader("Content-Type", "application/download; name=\"" + fileName + "\"");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			
			// UTF-8 BOM 추가
			if ("utf-8".equalsIgnoreCase(theEncoding)) {
				out.write(65279);
			}
			
			// 파일 쓰기
			int c;
			while ((c = in.read()) != -1) {
				out.write(c);
			}
			
		} catch (Exception e) {
			if (e.getCause() instanceof SocketException) {
				// 소켓 예외는 무시 (사용자가 다운로드 취소)
			} else {
				LogManager.writeErr(this, e.getMessage(), e);
			}
		} finally {
			closeResources(in, out);
		}
	}

	/**
	 * Reader, Writer 닫기
	 * 
	 * @param in Reader
	 * @param out Writer
	 */
	private void closeResources(Reader in, Writer out) {
		if (in != null) {
			try {
				in.close();
			} catch (Exception e) {
				LogManager.writeErr(this, e.getMessage(), e);
			}
		}
		if (out != null) {
			try {
				out.close();
			} catch (Exception e) {
				LogManager.writeErr(this, e.getMessage(), e);
			}
		}
	}

	/**
	 * 인코딩 정보 클래스
	 */
	private static class EncodingInfo {
		String encoding;
		Locale locale;
	}
}

