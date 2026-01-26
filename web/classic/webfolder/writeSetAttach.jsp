<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt" %>

<script type="text/javascript">
var attachAddList = new Array();
<c:forEach var="list" items="${attachList}">
	${list}
</c:forEach>
if(parent.opener){
	parent.opener.addWebfolderAttach(attachAddList);
	parent.window.close();
}else if(parent.parent){
	parent.parent.addWebfolderAttach(attachAddList);
	parent.closeWin();
}else{
	parent.opener.addWebfolderAttach(attachAddList);
	parent.window.close();
}
</script>

