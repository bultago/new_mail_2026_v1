package com.terracetech.tims.webmail.mailuser.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.CheckUserExistManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.exception.InvalidPasswordException;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class PasswordChangeAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	public Logger log = LoggerFactory.getLogger(this.getClass());

	private SettingManager settingManager = null;

	private UserAuthManager userAuthManager = null;

	private String oldPassword = null;
	private String newPassword = null;
	private String newConfirmPassword = null;

	public PasswordChangeAction(boolean authcheck) {
		setAuthCheck(authcheck);
	}

	public void setUserAuthManager(UserAuthManager userAuthManager) {
		this.userAuthManager = userAuthManager;
	}

	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}

	@Override
	public String execute() throws Exception {
		I18nResources resource = getMessageResource("setting");
		I18nResources commonResource = getMessageResource("common");

		int mailDomainSeq = Integer.parseInt(request.getParameter("domainSeq"));
		int mailUserSeq = Integer.parseInt(request.getParameter("userSeq"));

		oldPassword = request.getParameter("oldPassword");
		newPassword = request.getParameter("newPassword");
		newConfirmPassword = request.getParameter("newConfirmPassword");

		String msg = null;
		try {
			String savedPassword = (String) request.getSession().getAttribute(User.MAIL_PASSWORD);
			request.getSession().setAttribute(User.MAIL_PASSWORD, "");

			//if (!userAuthManager.checkPasswd(oldPassword, savedPassword)) {
				//throw new InvalidPasswordException();
			//}

			if(!newPassword.equals(newConfirmPassword)){
				throw new InvalidPasswordException();
			}


			//�н����� ��ȣȭ
			if (StringUtils.isNotEmpty(newPassword)) {
				settingManager.setMyPassword(mailDomainSeq, mailUserSeq, newPassword);
			}

			/*
			 * 2016-06-13 수정
			 * 패스워드 변경이후에 다시 로그인 처리
			 */
			User allowUser = (User)request.getSession().getAttribute("allowUser");
			// 로그인시 allowUser 를 session에 저장하고, 패스워드 변경되면, doLoginProcess 를 실행
			// 만약 session에 저장된 allowUser 가 null 이면 세션 타임아웃되었다는 경고창 나온뒤, 다시 로그인창으로 복귀
			if(allowUser == null){
				msg = commonResource.getMessage("auth.fail.timeout");
				request.setAttribute("msg", msg);
				return "success";
			}

			String userName = allowUser.get(User.USER_NAME);
			request.setAttribute("mailDomainSeq", allowUser.get(User.MAIL_DOMAIN_SEQ));
			request.setAttribute("mailUserSeq", allowUser.get(User.MAIL_USER_SEQ));
			request.setAttribute("mailUid", allowUser.get(User.MAIL_UID));
			request.setAttribute("userName", userName);

			SystemConfigManager systemConfigManager = (SystemConfigManager)ApplicationBeanUtil.getApplicationBean("systemConfigManager");
			String sessionTime = systemConfigManager.getSessionTimeConfig();
			allowUser.put(User.SESSION_CHECK_TIME, sessionTime);

			CheckUserExistManager checkUserExistManager = (CheckUserExistManager)ApplicationBeanUtil.getApplicationBean("checkUserExistManager");
			String timestamp = checkUserExistManager.dupCheckProcess(allowUser.get(User.EMAIL),mailDomainSeq);
			allowUser.put(User.LOGIN_TIMESTAMP, timestamp);

			String attachPath = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
			String strLocalhost = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort();
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("attachPath", attachPath);
			paramMap.put("attachUrl", EnvConstants.getAttachSetting("attach.url"));
			paramMap.put("localUrl", strLocalhost);

			userAuthManager.doLoginProcess(request, response, allowUser,paramMap);
			boolean isFirstLogin = "F".equals(allowUser.get(User.FIRST_LOGIN));
			request.setAttribute("user", allowUser);//���� ���ȭ
			LogManager.writeMailLogMsg(true,log, allowUser.get(User.EMAIL),
					request.getRemoteAddr(), "login");

			msg = resource.getMessage("update.ok");
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			msg = resource.getMessage("update.fail");
		}

		HttpSession session = request.getSession();
		session.removeAttribute("userAuthCheck");
		request.setAttribute("msg", msg);
		return "success";
	}



}
