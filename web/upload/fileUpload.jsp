<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>

<html>
    <body>
        <script language=javascript>
            <!--            
            var size = ${fileSize};
            var name = "${fileName}";
            var file = "${filePath}";
            var attachType = "${attatchType}";
            var attachMode = "${attachMode}";
            if(attachType == "attach"){
                parent.addlist(name, size, file, "", attachMode);
            } else if(attachType == "mass"){
                parent.addRcptFile(name, size, file, "");
            }
            //-->
        </script>       
    </body>
</html>
