<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
	<%@include file="/mobile/basic/common/header.jsp"%>
	<script type="text/javascript">
			function goBack(){
					var addrType="${fn:escapeXml(addrType)}";
					location.href="/mobile/addr/"+addrType+"AddrList.do"
				}
	</script>
</head>

<body>
	<div class="m_tms_wrap">
		<%@include file="/mobile/basic/addrbook/addr_body_top.jsp"%>		
			
		<div class="container">
				<div id="title_top_box" class="title_box">
				<div class="btn_l">&nbsp;</div>				
			</div>			
			<div class="mail_error">
				<div class="error_icon"></div>
			
				<p class="scrip">
					${fn:escapeXml(msg)}
				</p>
				<p class="btn_area">
					<a href="javascript:goBack();" class="btn2"><span><tctl:msg key="mail.goback"/></span></a>
				</p>
			</div>
			
		</div>
		<%@include file="/mobile/basic/common/footer.jsp"%>
		
	</div>
</body>
</html>
