<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<script type="text/javascript">
var isSuccess = ${isSuccess};
var type = "${job}";
if(type == "save"){
	parent.saveReplyMessageResult(isSuccess);
} else if(type == "delete"){
	parent.deleteReplyMessageResult(isSuccess);
}
</script>