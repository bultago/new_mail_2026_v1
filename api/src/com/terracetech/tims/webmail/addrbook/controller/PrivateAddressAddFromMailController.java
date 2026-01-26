package com.terracetech.tims.webmail.addrbook.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.addrbook.manager.AddressBookManager;
import com.terracetech.tims.webmail.addrbook.vo.AddressBookMemberVO;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.ResponseUtil;

/**
 * 메일에서 개인 주소록 추가 Controller
 * 
 * 주요 기능:
 * 1. 메일 수신자/발신자를 개인 주소록에 추가
 * 2. 배치 추가 지원 (여러 주소 동시 추가)
 * 3. JSON 응답 반환
 * 4. 그룹별 추가 지원
 * 
 * Struts2 Action: PrivateAddressAddFromMailAction
 * 변환 일시: 2025-10-20
 */
@Controller("privateAddressAddFromMailController")
public class PrivateAddressAddFromMailController {

	private static final Logger log = LoggerFactory.getLogger(PrivateAddressAddFromMailController.class);

	@Autowired
	private AddressBookManager addressBookManager;

	/**
	 * 메일에서 주소록 추가
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param model Model
	 * @return null (JSON 응답 직접 처리)
	 * @throws Exception
	 */
	public String execute(HttpServletRequest request, HttpServletResponse response, Model model) 
			throws Exception {
		
		// 사용자 정보 조회
		User user = SessionUtil.getUser(request);
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		// JSON 응답 객체
		JSONObject jObj = new JSONObject();
		
		// 이메일 리스트 파라미터 조회
		String[] emailList = request.getParameterValues("addrs");
		
		try {
			// 리소스 설정
			I18nResources resource = getMessageResource(user, "addr");
			addressBookManager.setResource(resource);
			
			// 이메일 리스트 처리
			if (emailList != null) {
				for (String str : emailList) {
					log.debug("add privateAddress : " + str);
					
					// 파싱: name|email|groupSeq
					String[] value = str.split("\\|");
					
					// 멤버 VO 생성 및 저장
					AddressBookMemberVO member = createMemberVO(userSeq, value);
					int groupSeq = Integer.parseInt(value[2]);
					
					savePrivateAddressMember(member, groupSeq, domainSeq);
				}
			}
			
			jObj.put("result", "success");
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			jObj.put("result", "fail");
			jObj.put("errMsg", e.getMessage());
		}
		
		// JSON 응답 전송
		ResponseUtil.processResponse(response, jObj);
		
		return null;
	}

	/**
	 * 부분 실행 (execute와 동일)
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param model Model
	 * @return null
	 * @throws Exception
	 */
	public String executePart(HttpServletRequest request, HttpServletResponse response, Model model) 
			throws Exception {
		return execute(request, response, model);
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

	/**
	 * 멤버 VO 생성
	 * 
	 * @param userSeq 사용자 시퀀스
	 * @param value 파싱된 값 배열 [name, email, groupSeq]
	 * @return AddressBookMemberVO
	 */
	private AddressBookMemberVO createMemberVO(int userSeq, String[] value) {
		AddressBookMemberVO member = new AddressBookMemberVO();
		member.setUserSeq(userSeq);
		member.setMemberName(value[0]);
		member.setMemberEmail(value[1]);
		member.setGroupSeq(Integer.parseInt(value[2]));
		return member;
	}

	/**
	 * 개인 주소록 멤버 저장
	 * 
	 * @param member 멤버 VO
	 * @param groupSeq 그룹 시퀀스
	 * @param domainSeq 도메인 시퀀스
	 * @throws Exception
	 */
	private void savePrivateAddressMember(AddressBookMemberVO member, int groupSeq, int domainSeq) 
			throws Exception {
		addressBookManager.savePrivateAddressMemberWithTransactional(member, groupSeq, domainSeq);
	}
}

