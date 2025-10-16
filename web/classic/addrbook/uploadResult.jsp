<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>

<script type="text/javascript">

<c:if test="${result == 'success'}">
	parent.uploadResult();
</c:if>
<c:if test="${result == 'license'}">
	parent.licenseFailed();
</c:if>
<c:if test="${result == 'failed'}">
	parent.uploadFailed();
</c:if>

</script>
