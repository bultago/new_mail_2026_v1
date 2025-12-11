package com.terracetech.tims.webmail.addrbook.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Locale;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.exception.SaveFailedException;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.CSVReader;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * 주소록 가져오기 Controller
 * 
 * 주요 기능:
 * 1. CSV 파일 업로드
 * 2. 인코딩 처리 (shift-jis, euc-kr, utf-8)
 * 3. 벤더별 포맷 지원 (Outlook, Gmail 등)
 * 4. 중복 주소 처리
 * 5. 라이선스 체크
 * 6. 파일 파싱 및 주소록 저장
 * 
 * Struts2 Action: ImportAddrAction
 * 변환 일시: 2025-10-20
 */
@Controller("importAddrController")
public class ImportAddrController {

	@Autowired
	private AddressBookManager addressBookManager;

	/**
	 * 주소록 가져오기
	 * 
	 * @param theGroupSeq 그룹 시퀀스
	 * @param theBookSeq 주소록 시퀀스
	 * @param theVendor 벤더 타입
	 * @param encoding 인코딩
	 * @param dupAddrType 중복 주소 처리 타입
	 * @param theFile 업로드 파일
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success" - 결과 화면
	 * @throws Exception
	 */
	public String execute(
			@RequestParam("theGroupSeq") int theGroupSeq,
			@RequestParam("theBookSeq") int theBookSeq,
			@RequestParam("theVendor") int theVendor,
			@RequestParam(value = "encoding", required = false) String encoding,
			@RequestParam(value = "dupAddrType", required = false) String dupAddrType,
			@RequestParam(value = "theFile", required = false) MultipartFile theFile,
			HttpServletRequest request,
			Model model) throws Exception {
		
		// 사용자 정보 조회
		User user = SessionUtil.getUser(request);
		Locale locale = new Locale(user.get(User.LOCALE));
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		// 리소스 설정
		I18nResources resource = new I18nResources(locale, "addr");
		addressBookManager.setResource(resource);
		
		// 인코딩 기본값 설정
		encoding = determineEncoding(encoding, user);
		
		// 결과 변수
		String result;
		
		// 파일 처리
		if (theFile != null && !theFile.isEmpty()) {
			result = processImportFile(theFile, encoding, theGroupSeq, theBookSeq, 
					theVendor, dupAddrType, userSeq, domainSeq);
		} else {
			result = "failed";
		}
		
		// Model에 추가
		model.addAttribute("result", result);
		
		return "success";
	}

	/**
	 * 인코딩 결정
	 * 
	 * @param encoding 지정된 인코딩
	 * @param user User
	 * @return 결정된 인코딩
	 */
	private String determineEncoding(String encoding, User user) {
		if (StringUtils.isEmpty(encoding)) {
			String locale = user.get(User.LOCALE);
			if ("jp".equals(locale)) {
				return "shift-jis";
			} else if ("ko".equals(locale)) {
				return "euc-kr";
			} else {
				return "";
			}
		}
		return encoding;
	}

	/**
	 * 가져오기 파일 처리
	 * 
	 * @param theFile 업로드 파일
	 * @param encoding 인코딩
	 * @param theGroupSeq 그룹 시퀀스
	 * @param theBookSeq 주소록 시퀀스
	 * @param theVendor 벤더 타입
	 * @param dupAddrType 중복 주소 처리 타입
	 * @param userSeq 사용자 시퀀스
	 * @param domainSeq 도메인 시퀀스
	 * @return 결과 ("success", "license", "failed")
	 */
	private String processImportFile(MultipartFile theFile, String encoding, 
			int theGroupSeq, int theBookSeq, int theVendor, String dupAddrType, 
			int userSeq, int domainSeq) {
		
		CSVReader csv = null;
		try {
			// 임시 파일 생성
			File attFile = convertMultipartFile(theFile);
			
			// 파일 내용 읽기
			String fileContents = getContents(attFile, encoding);
			
			// CSV 파싱
			BufferedReader reader = new BufferedReader(
					new StringReader(fileContents.replaceAll("\"\"", "").replaceAll("\"", "")));
			csv = new CSVReader(reader, ',', '\"');
			
			String[] results = csv.getAllFieldsInLine();
			
			// 주소록 가져오기
			addressBookManager.importAddressMember(encoding, results, userSeq, 
					theBookSeq, theGroupSeq, domainSeq, theVendor, dupAddrType);
			
			return "success";
			
		} catch (SaveFailedException e) {
			return "license";
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			return "failed";
		} finally {
			closeCSVReader(csv);
		}
	}

	/**
	 * MultipartFile을 File로 변환
	 * 
	 * @param multipartFile MultipartFile
	 * @return File
	 * @throws IOException
	 */
	private File convertMultipartFile(MultipartFile multipartFile) throws IOException {
		File file = File.createTempFile("import_", ".csv");
		multipartFile.transferTo(file);
		return file;
	}

	/**
	 * 파일 내용 읽기
	 * 
	 * @param attFile 첨부 파일
	 * @param encoding 인코딩
	 * @return 파일 내용
	 * @throws IOException
	 */
	private String getContents(File attFile, String encoding) throws IOException {
		InputStreamReader isr = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			if (StringUtils.isNotEmpty(encoding)) {
				isr = new InputStreamReader(new FileInputStream(attFile), encoding);
			} else {
				isr = new InputStreamReader(new FileInputStream(attFile));
			}
			
			BufferedReader reader = new BufferedReader(isr);
			String line = reader.readLine(); // 헤더 스킵
			
			while ((line = reader.readLine()) != null) {
				if (StringUtils.isNotEmpty(encoding)) {
					// 인코딩 지정된 경우
					if (line.startsWith("\",\"")) {
						sb.append(line);
					} else {
						sb.append('\n').append(line);
					}
				} else {
					// 인코딩 없는 경우
					line = line.trim();
					// Outlook 2007 CSV 처리
					if (line.endsWith(",\"")) {
						sb.append(line);
					} else {
						sb.append(line).append('\n');
					}
				}
			}
			
			try {
				reader.close();
			} catch (Exception e) {
				LogManager.writeErr(this, e.getMessage(), e);
			}
			
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		} finally {
			if (isr != null) {
				try {
					isr.close();
				} catch (Exception ignore) {
					LogManager.writeErr(this, ignore.getMessage(), ignore);
				}
			}
		}
		
		return sb.toString();
	}

	/**
	 * CSV Reader 닫기
	 * 
	 * @param csv CSVReader
	 */
	private void closeCSVReader(CSVReader csv) {
		if (csv != null) {
			try {
				csv.close();
			} catch (Exception ignore) {
				LogManager.writeErr(this, ignore.getMessage(), ignore);
			}
		}
	}
}

