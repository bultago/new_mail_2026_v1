<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.terracetech.tims.common.*"%>
<%@ page import="com.terracetech.tims.webmail.util.*"%>
<%@ page import="com.terracetech.tims.webmail.common.*"%>
<%@ page import="com.terracetech.tims.webmail.common.manager.*"%>
<%@ page import="com.terracetech.tims.webmail.mailuser.*"%>
<%@ page import="com.terracetech.tims.webmail.mailuser.manager.*"%>
<%@ page import="com.terracetech.tims.webmail.common.log.LogManager"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="org.apache.log4j.Logger"%>

<%@ page import="java.util.*"%>
<%!public I18nResources getMessageResource(HttpServletRequest request, String bundle){
	return new I18nResources(I18nConstants.getBundleUserLocale(request), bundle);
}%>
<%
Logger log = Logger.getLogger(this.getClass());

	I18nResources commonResource = getMessageResource(request, "common");
	
	int mailDomainSeq = Integer.parseInt(request.getParameter("domainSeq"));
	
	String msg = null;
	
	ServletContext context = request.getSession().getServletContext();
	try {
		/*
		 * 2016-06-13 수정
		 * 패스워드 변경이후에 다시 로그인 처리
		 */
		User allowUser = (User)request.getSession().getAttribute("allowUser");
		// 로그인시 allowUser 를 session에 저장하고, 패스워드 변경되면, doLoginProcess 를 실행
		// 만약 session에 저장된 allowUser 가 null 이면 세션 타임아웃되었다는 경고창 나온뒤, 다시 로그인창으로 복귀
		if(allowUser == null){
	msg = commonResource.getMessage("auth.fail.timeout");
%>
			<script>
				alert("<%=msg%>"); 
				location.href = "/common/welcome.do";
			</script>
			<%
			return;
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
					
						UserAuthManager userAuthManager = (UserAuthManager)ApplicationBeanUtil.getApplicationBean("userAuthManager");
						
						userAuthManager.doLoginProcess(request, response, allowUser,paramMap);
					
						request.setAttribute("user", allowUser);//���� ���ȭ
						LogManager.writeMailLogMsg(true,log, allowUser.get(User.EMAIL),
						request.getRemoteAddr(), "login");
					
					}catch (Exception e) {
						e.printStackTrace();
					}
			%>
<script>location.href = "/common/welcome.do";</script>