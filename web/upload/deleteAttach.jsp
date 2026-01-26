<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<html>
	<body>
	<script language="javascript">
		var size = "${fileSize}";
		var name = "${fileName}";
		var file = "${filePath}";
		var type = "${type}";

		if(type == "attach"){		
			parent.delattlist(file, parseInt(name), size,"");
		} else if(type == "massrcpt"){
			parent.clearMassRcpt();
		}		
	</script>		
	</body>
</html>