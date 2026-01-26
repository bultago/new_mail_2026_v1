
<%@page import="com.terracetech.tims.webmail.mailuser.User"%>
<%@page import="com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO"%>
<%@page import="com.terracetech.tims.webmail.common.manager.SystemConfigManager"%>
<%@page import="com.terracetech.tims.webmail.util.ApplicationBeanUtil"%>
<%@page import="com.terracetech.tims.webmail.setting.manager.SettingManager"%>
<%
int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
int groupSeq = Integer.parseInt(user.get(User.MAIL_GROUP_SEQ));
int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
SettingManager settingManager = (SettingManager)ApplicationBeanUtil.getApplicationBean("settingManager");
SystemConfigManager systemConfigManager = (SystemConfigManager)ApplicationBeanUtil.getApplicationBean("systemConfigManager");

String localmail = systemConfigManager.getLocalMailConfig();
UserEtcInfoVO vo = settingManager.readUserEtcInfo(userSeq);
boolean isPopupWriteMode = ("popup".equals(vo.getComposeMode()));
boolean isWriteNoti = ("enable".equals(vo.getWriteNoti()));
boolean isLocalMail = ("enabled".equals(localmail));
boolean isSearchAllFolder = "on".equalsIgnoreCase(vo.getSearchAllFolder());
String[] vaultValues = systemConfigManager.archiveUseInfoApi(domainSeq, groupSeq, USER_EMAIL);
boolean useArchive = "on".equalsIgnoreCase(vaultValues[0]);
String archiveSSOUrl = vaultValues[1];
Map<String, String> searchConfigMap = systemConfigManager.getMailAdvanceSearchConfig();
String mailSearchConfig = "{bodySearch:'"+searchConfigMap.get("bodySearch")+"',attachSearch:'"+searchConfigMap.get("attachSearch")+"'}";
%>

<%@page import="java.util.Map"%><c:set var="localmail" value="<%=isLocalMail%>"/>
<c:set var="useArchive" value="<%=useArchive%>"/>
<c:set var="archiveSSOUrl" value="<%=archiveSSOUrl%>"/>

var IS_USE_EXPRESS_E = (isMsie && <%=ExtPartConstants.isXecureExpressE()%>);
var IS_USE_WECURE_WEB = (isMsie && <%=ExtPartConstants.isXecureWebUse()%>);
var mailSearchConfig = <%=mailSearchConfig%>;
var isPopupWrite = <%=isPopupWriteMode%>;
var isWriteNoti = <%=isWriteNoti%>;
var isLocalMail = <%=isLocalMail%>;
var isSearchAllFolder = <%=isSearchAllFolder%>;