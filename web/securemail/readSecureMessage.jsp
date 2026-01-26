<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@page import="com.terracetech.tims.common.I18nConstants"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%
	String locale = (I18nConstants.getBundleUserLocale(request)).getLanguage();
%>
<html>
	<head>		
	<script type="text/javascript" src="/js/core-lib/prototype.js"></script>
	<script type="text/javascript" src="/js/core-lib/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="/js/common-lib/common.js"></script>	
		

		<style type="text/css">
			body {
				<%if(locale.equals("jp") || locale.equals("ja")){%>
				font-family:"MS PGothic","Osaka", "Sans-serif";
				<%} else {%>
				font-family:dotum, Arial, Helvetica, sans-serif;
				<%}%>
				margin-top:10px;
				margin-left:10px;
				font-size: 12px;
				color:#555555;
				background: #f0fbff;
			}

			td {
				<%if(locale.equals("jp") || locale.equals("ja")){%>
				font-family:"MS PGothic","Osaka", "Sans-serif";
				<%} else {%>
				font-family:dotum, Arial, Helvetica, sans-serif;
				<%}%>
				font-size: 12px;
				color:#444444;	
			}

			img {
				border:0px;
			}
			.main_body {
				border:0px;
				background-repeat:no-repeat;	
			}
			.pass_table{
				border:1px solid #D1D1D1;
				background-color:#ffffff;
				padding: 10px;
			}
			a:link      {color:#333333; letter-spacing:normal; text-decoration:none;}
			a:active    {color:#333333; letter-spacing:normal; text-decoration:underline;}
			a:visited   {color:#333333; letter-spacing:normal; text-decoration:none;}
			a:hover     {color:#333333; letter-spacing:normal; text-decoration:underline;}
		</style>

		<script language="javascript">
		function downLoadAttach(uid, folder, part){
			var cryptMethod = '${cryptMethod}';
			var domainParam = '${domainParam}';
			var userParam = '${userParam}';
			
			var param = {"folder":folder, 
					"uid":uid, "part":part,"secure":"on",
					"cryptMethod":cryptMethod,
					"domainParam":domainParam,
					"userParam":userParam,
					"type":"normal"};			
			$("reqFrame").src = "/mail/downloadAttach.do?"+jQuery.param(param);	
		}

		function downLoadTnefAttach(uid, folder, part, tnefKey){
			var cryptMethod = '${cryptMethod}';
			var domainParam = '${domainParam}';
			var userParam = '${userParam}';
			
			var param = {"folder":folder, 
					"uid":uid, "part":part,"secure":"on",
					"cryptMethod":cryptMethod,
					"domainParam":domainParam,
					"userParam":userParam,
					"type":"tnef",
					"tnefKey":tnefKey};			
			$("reqFrame").src = "/mail/downloadAttach.do?"+jQuery.param(param);
		}
		</script>
	</head>

<body style="margin:10;">
	
	<table border="0" cellpadding="10" cellspacing="0" width="100%" bgcolor="#f0fbff">
		<tr>
			<td align="center">
				<table border="0" cellpadding="0" cellspacing="0" width="100%" >
					<tr><td height="10"></td></tr>					
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td><img src="/design/common/image/secure/smail_title_left.gif"></td>
									<td background="/design/common/image/secure/smail_title_bg.gif" style="font-size:15px; color:#fff; font-weight:bold; width:100%; text-align:left">
										<tctl:msg key="mail.secure.title"/>
									</td>
									<td><img src="/design/common/image/secure/smail_title_right.gif"></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr><td height="10"></td></tr>					
					<tr>
						<td style="border:1px solid #8dacdc; background:#fff; text-align:left">
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td width="90" valign="top" style="padding:8px; border-bottom:1px solid #e6e6e6; border-right:1px solid #e6e6e6; background:#f7f7f7; width:100px; " nowrap><tctl:msg key="mail.subject"/></td>
									<td width="100%" style="padding:8px; border-bottom:1px solid #e6e6e6;">
										<span style="margin-left:10px; font-size:20px; font-weight:bold;">
											${subject}
										</span>
									</td>									
								</tr>
								<tr>
									<td width="90" valign="top" style="padding:8px; border-bottom:1px solid #e6e6e6; border-right:1px solid #e6e6e6; background:#f7f7f7; width:100px; " nowrap><tctl:msg key="mail.attach"/></td>
									<td width="100%" style="padding:8px; border-bottom:1px solid #e6e6e6;" valign="top">										
										<c:forEach var="fileData" items="${files}" varStatus="loop">				
											<c:forTokens var="file" items="${fileData.fileName}" delims=".">
												<c:set var="fileType" value="${fn:toLowerCase(file)}"/>								 
											</c:forTokens>	  
											
											<c:choose>
									   			<c:when test="${fileType=='doc' || fileType=='docx' || fileType=='gif' || fileType=='pdf' || fileType=='html' || fileType=='hwp' || 
									   			fileType=='jpg' || fileType=='ppt' || fileType=='pptx' || fileType=='txt' || fileType=='xls' || fileType=='xlsx' || fileType=='zip'|| 
									   			fileType=='eml'}">							   				
									   				<c:set var="attachImgName" value="ic_att_${fileType}"/>
									   				<c:set var="fileAlt" value="${fileType}"/>
									   			</c:when>								   			
									   			<c:otherwise>
									   				<c:set var="attachImgName" value="ic_att_unknown"/>
									   				<c:set var="fileAlt" value="${fileType}"/>							   				
									   			</c:otherwise>
								   			</c:choose>
								   			
								   			<c:if test="${fileData.size75 > 0 }">
								   		    
												<a href="javascript:;" onclick="downLoadAttach('${uid}','${folderName}','${fileData.path}')">
												<img src="/design/common/image/icon/${attachImgName}.gif" alt="${fileAlt}" align="absmiddle"/>&nbsp
													${fileData.fileName} [<tctl:formatSize>${fileData.size75}</tctl:formatSize>]</a>
													
												<c:if test="${fileData.tnefType}">
													<c:if test="${not empty fileData.tnefFiles}">
													[
													<c:forEach var="tnefFile" items="${fileData.tnefFiles}" varStatus="idx">
														<c:if test="${idx.index > 0}">
														,
														</c:if>
														<a href="javascript:;" onclick="downLoadTnefAttach('${message.uid}','${message.folderEncName}','${fileData.path}','${tnefFile.attachKey}')" 
															class="TM_attfile_downlink">
														${tnefFile.fileName}
														</a>
													</c:forEach>
													]
													</c:if>
												</c:if>
												<br>		   		    		   		    
								   		    </c:if>
										</c:forEach>
										&nbsp;
									</td>									
								</tr>								
								<tr>
									<td colspan="2" style="padding:10px; line-height:180%">
										${htmlContent}
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr><td height="10"></td></tr>
				</table>
			</td>
		</tr>
	</table>
	
	<iframe name="hidden_frame" id="reqFrame" src="about:blank" frameborder="0" width="0" height="0"></iframe>
</body>
</html>

