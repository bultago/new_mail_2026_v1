<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/hybrid/common/header.jsp"%>
		
		<script type="text/javascript">
			function init() {

				if ("${installLocale}" == "jp") {
					setLanguage('jp');
				} 
				else {
					setLanguage('ko');
				}
				
				var f = document.loginForm;
				var emailCookie = getCookie("TSEMAIL");
				var passCookie = getCookie("TSPASS");
				if(emailCookie != ""){
					f.email.value = emailCookie;
					f.saveEmailCheck.checked = true;
				}
				checkInput("id","init");

				if(passCookie != ""){
					f.pass.value = "savedpasstms"; 
					f.encPass.value = passCookie;
					f.savePassCheck.checked = true;
				}
				checkInput("pass","init");
			}
			function loginProcess() {
				var f = document.loginForm;
				var email = trim(f.email.value);
				var pass = trim(f.pass.value);
				var encPass = trim(f.encPass.value);				

				if (email == "") {
					alert('<tctl:msg key="error.login.email" bundle="common"/>');
					f.email.focus();
					return;
				}

				if (pass == "") {
					alert('<tctl:msg key="error.login.password" bundle="common"/>');
					f.pass.focus();
					return;
				}

				if(email.indexOf("@") < 0){
                    email = email+"@"+"${fn:escapeXml(defaultDomain)}";
                }

				if(!isEmail(email)) {
					alert('<tctl:msg key="error.email" bundle="common"/>');
					f.email.select();
					return;
				}

				if(f.saveEmailCheck.checked){
					setCookie("TSEMAIL",email,365);
				} else {
					setCookie("TSEMAIL",email,-1);
				}

				if(!f.savePassCheck.checked){
					setCookie("TSPASS",pass,-1);
				}

				if(f.secureLoginCheck.checked){
					f.action = "https://"+this.location.host+"/hybrid/common/login.do";
				}

				f.email.value = email;

				f.submit();
			}

			function getEncriptString(pass){
				
			}

			function setLanguage(locale) {
				var f = document.loginForm;
				var languageTitle = document.getElementById("select_language");
				var languageElement = "";
				if (locale == 'ko') {
					f.language.value = "ko";
					languageElement = document.createTextNode('<tctl:msg key="conf.profile.23" bundle="setting"/>');
				} else if (locale == 'en') {
					f.language.value = "en";
					languageElement = document.createTextNode('<tctl:msg key="conf.profile.24" bundle="setting"/>');
				} else if (locale == 'jp') {
					f.language.value = "jp";
					languageElement = document.createTextNode('<tctl:msg key="conf.profile.language.japanese" bundle="setting"/>');
				}
				languageTitle.removeChild(languageTitle.firstChild);
				languageTitle.appendChild(languageElement);
				closeLanguage();
			}

			function toggleLanguage() {
				var languageBox = document.getElementById("language_box");				
				if (languageBox.style.display == "block") {
					languageBox.style.display = "none";
				} else {
					languageBox.style.display = "block";
				}
			}

			function closeLanguage() {
				var languageBox = document.getElementById("language_box");
				languageBox.style.display = "none";
			}

			function checkInput(type,focus){
				var inpuObj,textObj;
				if(type == 'id'){
					inpuObj = document.getElementById("email");
					textObj = document.getElementById("emailText");					
				} else if(type == 'pass'){
					inpuObj = document.getElementById("pass");
					textObj = document.getElementById("passText");
				}

				if(focus == 'on'){
					textObj.style.display = "none";
				} else if(focus == 'off'){
					textObj.style.display = (inpuObj.value != "")?"none":"block";
				} else if(focus == 'init'){
					textObj.style.display = (inpuObj.value != "")?"none":"block";
				}
			}
		</script>
	</head>
	<body onload="init();">
		<form name="loginForm" action="/hybrid/common/login.do" method="post">
			<input type="hidden" name="language" value="ko"/>
			<input type="hidden" name="defaultDomain" value="${fn:escapeXml(defaultDomain)}"/>
			<input type="hidden" name="encPass"/>
			<div class="wrapper">
				<div class="header">
					<h1 class="logo">
						<a href="/common/welcome.do">
							<c:if test="${!empty commonLogoList['loginTop']}">
								<img src="${commonLogoList['loginTop'].logoImgUrl}" width="210px" height="35px" alt="<tctl:msg key="comn.logo" bundle="common"/>"/>
							</c:if>
							<c:if test="${empty commonLogoList['loginTop']}">
								<img src="/design/mobile/image/logo.gif" alt="<tctl:msg key="comn.logo" bundle="common"/>"/>
							</c:if>
						</a>
					</h1>
					<div class="hh"><h2><tctl:msg key="comn.login" bundle="common"/></h2></div>
					<div class="title_box">
						<div class="btn_l">							
							<label class="ck" for="lc1">
								<input id="lc1" type="checkbox" name="secureLoginCheck"/>
								<span><tctl:msg key="login.006" bundle="common"/></span>
							</label>
						</div>
						<div class="btn_r">
							<a id="select_language" href="javascript:;" onclick="toggleLanguage()" class="btn_dr3"><tctl:msg key="conf.profile.23" bundle="setting"/></a>
						</div>
						
						<div id="language_box" class="languageBox">
							<div class="languageAbBox" style="right:0px;top:30px;">
								<div class="languageInnerBox">
									<ul>
										<li><a href="javascript:;" onclick="setLanguage('ko')"><tctl:msg key="conf.profile.23" bundle="setting"/></a></li>
										<li><a href="javascript:;" onclick="setLanguage('en')"><tctl:msg key="conf.profile.24" bundle="setting"/></a></li>
										<li><a href="javascript:;" onclick="setLanguage('jp')"><tctl:msg key="conf.profile.language.japanese" bundle="setting"/></a></li>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="container">
					<c:if test="${foward == 'fail'}">						
						<div class="msg">
							<p class="simbol error">x</p>
							<h5>${fn:escapeXml(failMessage)}</h5>
						</div>
					</c:if>
					<div class="login">
						<ul>
							<li style="position: relative;">
								<input type="text" id="email" name="email" onfocus="checkInput('id','on')" onblur="checkInput('id','off')"/>
								<span id='emailText' style="position:absolute;top:5px;left:5px;color:#666666;font-weight:normal;"><tctl:msg key="conf.userinfo.id" bundle="setting"/> or <tctl:msg key="conf.userinfo.email" bundle="setting"/></span>
							</li>
							<li style="position: relative;">
								<input type="password" id="pass" name="pass" value="" onfocus="checkInput('pass','on')" onblur="checkInput('pass','off')" onKeyPress="(keyEvent(event) == 13) ? loginProcess() : '';" autocomplete="off"/>
								<span id='passText' style="position:absolute;top:7px;left:5px;color:#666666;font-weight:normal;"><tctl:msg key="conf.userinfo.pass" bundle="setting"/></span>
							</li>							
						</ul>
						<div><a href="javascript:loginProcess()" class="btn4 reserv_search"><span><tctl:msg key="comn.login" bundle="common"/></span></a></div>
						<div class="lc">
							<label class="ck"><input id="lc2" type="checkbox" name="saveEmailCheck"/><span><tctl:msg key="login.013" bundle="common"/></span></label>
							<label class="ck last"><input id="lc3" type="checkbox" name="savePassCheck" value="on"/><span><tctl:msg key="login.014" bundle="common"/></span></label>
						</div>
						<p class="login_scrip"><span><tctl:msg key="login.015" bundle="common"/></span></p>
					</div>
				</div>
				<!-- footer -->
				<div class="footer">
					<div class="footer_btn">
						<%if (!BrowserUtil.isMobileOpera(agent)) {%>				
						<a href="javascript:chageMailMode('pc');" class="btn2"><span>PC <tctl:msg key="comn.version" bundle="common"/></span></a>
						<%}%>
					</div>					
				</div>
			</div>
		</form>
			<script language="javascript"><c:if test="${foward == 'fail'}">setCookie("TSPASS","",-1);</c:if></script>
	</body>
</html>