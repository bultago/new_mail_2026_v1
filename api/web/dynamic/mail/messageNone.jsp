<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>

<div class="TM_content_wrapper">
<div class="TM_mail_content" >
	<div style="height:300px; width:100%">	
		<div style="padding-top:100px;">
				<div id="notMessageComment" class="TM_comment_content">					
					<tctl:msg key="mail.nomessage"/>					
				</div>				
		</div>
	</div>
	
	<!-- 
	 -->	
</div>
</div>

<script type="text/javascript">
	jQuery("#notMessageComment").corner();
</script>