package com.terracetech.tims.webmail.setting.controller;

import java.security.PrivateKey;

import javax.crypto.Cipher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserInfoVO;
import com.terracetech.tims.webmail.util.AESUtils;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * 사용자 정보 수정 Controller
 * 
 * 주요 기능:
 * 1. 사용자 기본 정보 수정
 * 2. 비밀번호 변경
 * 3. 개인 정보 수정
 * 4. 회사 정보 수정
 * 5. 암호화 처리
 * 
 * Struts2 Action: ModifyUserInfoAction
 * 변환 일시: 2025-10-20
 */
@Controller("modifyUserInfoController")
public class ModifyUserInfoController {

	@Autowired
	private SettingManager settingManager;
	
	@Autowired
	private UserAuthManager userAuthManager;
	
	@Autowired
	private SystemConfigManager systemManager;

	/**
	 * 사용자 정보 수정
	 * 
	 * @param firstName 이름
	 * @param middleName 중간 이름
	 * @param lastName 성
	 * @param userName 사용자명
	 * @param mobileNo 휴대폰 번호
	 * @param mailPassword 메일 비밀번호
	 * @param passChgTime 비밀번호 변경 시간
	 * @param passQuestionCode 비밀번호 질문 코드
	 * @param passAnswer 비밀번호 답변
	 * @param birthday 생년월일
	 * @param homePostalCode 집 우편번호
	 * @param homeCountry 집 국가
	 * @param homeState 집 주/도
	 * @param homeCity 집 도시
	 * @param homeStreet 집 거리
	 * @param homeBasicAddress 집 기본 주소
	 * @param homeExtAddress 집 확장 주소
	 * @param homeTel 집 전화번호
	 * @param homeFax 집 팩스번호
	 * @param privateHomepage 개인 홈페이지
	 * @param companyName 회사명
	 * @param departmentName 부서명
	 * @param jobTitle 직책
	 * @param officePostalCode 사무실 우편번호
	 * @param officeCountry 사무실 국가
	 * @param officeState 사무실 주/도
	 * @param officeCity 사무실 도시
	 * @param officeStreet 사무실 거리
	 * @param officeBasicAddress 사무실 기본 주소
	 * @param officeExtAddress 사무실 확장 주소
	 * @param officeTel 사무실 전화번호
	 * @param officeFax 사무실 팩스번호
	 * @param officeHomepage 사무실 홈페이지
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return "success" or "fail"
	 * @throws Exception
	 */
	public String execute(
			@RequestParam(value = "firstName", required = false) String firstName,
			@RequestParam(value = "middleName", required = false) String middleName,
			@RequestParam(value = "lastName", required = false) String lastName,
			@RequestParam(value = "userName", required = false) String userName,
			@RequestParam(value = "mobileNo", required = false) String mobileNo,
			@RequestParam(value = "mailPassword", required = false) String mailPassword,
			@RequestParam(value = "passChgTime", required = false) String passChgTime,
			@RequestParam(value = "passQuestionCode", required = false) String passQuestionCode,
			@RequestParam(value = "passAnswer", required = false) String passAnswer,
			@RequestParam(value = "birthday", required = false) String birthday,
			@RequestParam(value = "homePostalCode", required = false) String homePostalCode,
			@RequestParam(value = "homeCountry", required = false) String homeCountry,
			@RequestParam(value = "homeState", required = false) String homeState,
			@RequestParam(value = "homeCity", required = false) String homeCity,
			@RequestParam(value = "homeStreet", required = false) String homeStreet,
			@RequestParam(value = "homeBasicAddress", required = false) String homeBasicAddress,
			@RequestParam(value = "homeExtAddress", required = false) String homeExtAddress,
			@RequestParam(value = "homeTel", required = false) String homeTel,
			@RequestParam(value = "homeFax", required = false) String homeFax,
			@RequestParam(value = "privateHomepage", required = false) String privateHomepage,
			@RequestParam(value = "companyName", required = false) String companyName,
			@RequestParam(value = "departmentName", required = false) String departmentName,
			@RequestParam(value = "jobTitle", required = false) String jobTitle,
			@RequestParam(value = "officePostalCode", required = false) String officePostalCode,
			@RequestParam(value = "officeCountry", required = false) String officeCountry,
			@RequestParam(value = "officeState", required = false) String officeState,
			@RequestParam(value = "officeCity", required = false) String officeCity,
			@RequestParam(value = "officeStreet", required = false) String officeStreet,
			@RequestParam(value = "officeBasicAddress", required = false) String officeBasicAddress,
			@RequestParam(value = "officeExtAddress", required = false) String officeExtAddress,
			@RequestParam(value = "officeTel", required = false) String officeTel,
			@RequestParam(value = "officeFax", required = false) String officeFax,
			@RequestParam(value = "officeHomepage", required = false) String officeHomepage,
			HttpServletRequest request,
			Model model) throws Exception {
		
		// 사용자 정보 조회
		User user = SessionUtil.getUser(request);
		I18nResources resource = getMessageResource(user, "setting");
		
		try {
			// 사용자 정보 VO 생성
			UserInfoVO userInfoVo = createUserInfoVO(user, firstName, middleName, lastName, userName, mobileNo,
					mailPassword, passChgTime, passQuestionCode, passAnswer, birthday, homePostalCode, homeCountry,
					homeState, homeCity, homeStreet, homeBasicAddress, homeExtAddress, homeTel, homeFax,
					privateHomepage, companyName, departmentName, jobTitle, officePostalCode, officeCountry,
					officeState, officeCity, officeStreet, officeBasicAddress, officeExtAddress, officeTel,
					officeFax, officeHomepage);
			
			// 사용자 정보 수정
			boolean updateResult = updateUserInfo(userInfoVo, user);
			
			if (updateResult) {
				model.addAttribute("result", "success");
				model.addAttribute("message", resource.getMessage("user.info.update.success"));
			} else {
				model.addAttribute("result", "fail");
				model.addAttribute("message", resource.getMessage("user.info.update.failed"));
			}
			
		} catch (Exception e) {
			LogManager.writeErr(this, "사용자 정보 수정 중 오류 발생: " + e.getMessage(), e);
			model.addAttribute("result", "error");
			model.addAttribute("message", resource.getMessage("user.info.update.error"));
		}
		
		return "success";
	}

	/**
	 * 사용자 정보 VO 생성
	 * 
	 * @param user 사용자 정보
	 * @param params 파라미터들
	 * @return UserInfoVO
	 * @throws Exception
	 */
	private UserInfoVO createUserInfoVO(User user, String... params) throws Exception {
		UserInfoVO userInfoVo = new UserInfoVO();
		
		// 기본 정보 설정
		userInfoVo.setMailUserSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		userInfoVo.setMailUid(user.get(User.MAIL_UID));
		
		// 개인 정보 설정
		if (StringUtils.isNotEmpty(params[0])) userInfoVo.setFirstName(params[0]);
		if (StringUtils.isNotEmpty(params[1])) userInfoVo.setMiddleName(params[1]);
		if (StringUtils.isNotEmpty(params[2])) userInfoVo.setLastName(params[2]);
		if (StringUtils.isNotEmpty(params[3])) userInfoVo.setUserName(params[3]);
		if (StringUtils.isNotEmpty(params[4])) userInfoVo.setMobileNo(params[4]);
		
		// 비밀번호 정보 설정
		if (StringUtils.isNotEmpty(params[5])) {
			// 비밀번호 암호화 처리
			String encryptedPassword = encryptPassword(params[5]);
			userInfoVo.setMailPassword(encryptedPassword);
		}
		if (StringUtils.isNotEmpty(params[6])) userInfoVo.setPassChgTime(params[6]);
		if (StringUtils.isNotEmpty(params[7])) userInfoVo.setPassQuestionCode(params[7]);
		if (StringUtils.isNotEmpty(params[8])) userInfoVo.setPassAnswer(params[8]);
		
		// 집 주소 정보 설정
		if (StringUtils.isNotEmpty(params[9])) userInfoVo.setBirthday(params[9]);
		if (StringUtils.isNotEmpty(params[10])) userInfoVo.setHomePostalCode(params[10]);
		if (StringUtils.isNotEmpty(params[11])) userInfoVo.setHomeCountry(params[11]);
		if (StringUtils.isNotEmpty(params[12])) userInfoVo.setHomeState(params[12]);
		if (StringUtils.isNotEmpty(params[13])) userInfoVo.setHomeCity(params[13]);
		if (StringUtils.isNotEmpty(params[14])) userInfoVo.setHomeStreet(params[14]);
		if (StringUtils.isNotEmpty(params[15])) userInfoVo.setHomeBasicAddress(params[15]);
		if (StringUtils.isNotEmpty(params[16])) userInfoVo.setHomeExtAddress(params[16]);
		if (StringUtils.isNotEmpty(params[17])) userInfoVo.setHomeTel(params[17]);
		if (StringUtils.isNotEmpty(params[18])) userInfoVo.setHomeFax(params[18]);
		if (StringUtils.isNotEmpty(params[19])) userInfoVo.setPrivateHomepage(params[19]);
		
		// 회사 정보 설정
		if (StringUtils.isNotEmpty(params[20])) userInfoVo.setCompanyName(params[20]);
		if (StringUtils.isNotEmpty(params[21])) userInfoVo.setDepartmentName(params[21]);
		if (StringUtils.isNotEmpty(params[22])) userInfoVo.setJobTitle(params[22]);
		if (StringUtils.isNotEmpty(params[23])) userInfoVo.setOfficePostalCode(params[23]);
		if (StringUtils.isNotEmpty(params[24])) userInfoVo.setOfficeCountry(params[24]);
		if (StringUtils.isNotEmpty(params[25])) userInfoVo.setOfficeState(params[25]);
		if (StringUtils.isNotEmpty(params[26])) userInfoVo.setOfficeCity(params[26]);
		if (StringUtils.isNotEmpty(params[27])) userInfoVo.setOfficeStreet(params[27]);
		if (StringUtils.isNotEmpty(params[28])) userInfoVo.setOfficeBasicAddress(params[28]);
		if (StringUtils.isNotEmpty(params[29])) userInfoVo.setOfficeExtAddress(params[29]);
		if (StringUtils.isNotEmpty(params[30])) userInfoVo.setOfficeTel(params[30]);
		if (StringUtils.isNotEmpty(params[31])) userInfoVo.setOfficeFax(params[31]);
		if (StringUtils.isNotEmpty(params[32])) userInfoVo.setOfficeHomepage(params[32]);
		
		return userInfoVo;
	}

	/**
	 * 비밀번호 암호화
	 * 
	 * @param password 평문 비밀번호
	 * @return 암호화된 비밀번호
	 * @throws Exception
	 */
	private String encryptPassword(String password) throws Exception {
		// AES 암호화 사용
		String encryptionKey = EnvConstants.getBasicSetting("encryption.key");
		return AESUtils.encrypt(password, encryptionKey);
	}

	/**
	 * 사용자 정보 업데이트
	 * 
	 * @param userInfoVo 사용자 정보 VO
	 * @param user 사용자 정보
	 * @return 업데이트 성공 여부
	 * @throws Exception
	 */
	private boolean updateUserInfo(UserInfoVO userInfoVo, User user) throws Exception {
		// 기본 사용자 정보 업데이트
		boolean basicUpdateResult = settingManager.updateUserInfo(userInfoVo);
		
		// 추가 사용자 정보 업데이트
		boolean etcUpdateResult = settingManager.updateUserEtcInfo(userInfoVo);
		
		// 비밀번호 업데이트 (있는 경우)
		boolean passwordUpdateResult = true;
		if (StringUtils.isNotEmpty(userInfoVo.getMailPassword())) {
			passwordUpdateResult = userAuthManager.updateUserPassword(
				user.get(User.MAIL_USER_SEQ), 
				userInfoVo.getMailPassword()
			);
		}
		
		return basicUpdateResult && etcUpdateResult && passwordUpdateResult;
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
