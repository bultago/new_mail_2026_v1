package com.terracetech.tims.webmail.common;

import com.terracetech.tims.webmail.util.StringUtils;

public class MakeMessage {

	public MakeMessage() {
	}

	public static String printAlert(String message) {
        String str = ""
            +"<script language=javascript>\n"
            +"alert(\""+ StringUtils.EscapeQuot(message) +"\");\n"
            +"</script>\n"
            ;

        return str;
    }

    public static String printAlertRedirect(String message, String url) {
        String str = ""
            +"<script language=javascript>\n"
            +"alert(\""+ StringUtils.EscapeQuot(message) +"\");\n"
            +"this.location = \""+ url +"\";\n"
            +"</script>\n"
            ;

        return str;
    }

	public static String printBack() {
        String str = ""
            +"<script language=javascript>\n"
			+"history.back(-1);\n"
            +"</script>\n"
            ;

        return str;
    }

	public static String printUrl(String url) {
        String str = ""
            +"<script language=javascript>\n"
			+"this.location=\""+url+"\";\n"
            +"</script>\n"
            ;

        return str;
    }

	public static String printAlertBack(String message) {
        String str = ""
            +"<script language=javascript>\n"
            +"alert(\""+ StringUtils.EscapeQuot(message) +"\");\n"
			+"history.back(-1);\n"
            +"</script>\n"
            ;

        return str;
    }

	public static String printAlertClose(String message) {
        String str = ""
            +"<script language=javascript>\n"
            +"alert(\""+ StringUtils.EscapeQuot(message) +"\");\n"
			+"opener.location.reload()\n"
			+"window.close();\n"
            +"</script>\n"
            ;

        return str;
    }

	public static String printAlertUrl(String message, String url) {
        String str = ""
            +"<script language=javascript>\n"
            +"alert(\""+ message +"\");\n"
			+"this.location=\""+url+"\";\n"
            +"</script>\n"
            ;

        return str;
    }

	public static String printAlertOpenerUrl(String message, String url) {
        String str = ""
            +"<script language=javascript>\n"
            +"alert(\""+ message +"\");\n"
			+"this.window.close();\n"
			+"opener.location=\""+url+"\";\n"
            +"</script>\n"
            ;

        return str;
    }

    public static String printAlertTopUrl(String message, String url) {
        String str = ""
            +"<script language=javascript>\n"
            +"alert(\""+ message +"\");\n"
            +"this.window.close();\n"
            +"opener.top.location=\""+url+"\";\n"
            +"</script>\n"
            ;

        return str;
    }

    public static String printAlertParentUrl(String message, String url) {
        String str = ""
            +"<script language=javascript>\n"
            +"alert(\""+ message +"\");\n"
            +"this.window.close();\n"
            +"parent.location=\""+url+"\";\n"
            +"</script>\n"
            ;

        return str;
    }

	public static String printAlertOpenerUrl(String message, String url,
		String opener_url) {
        String str = ""
            +"<script language=javascript>\n"
            +"alert(\""+ message +"\");\n"
			+"this.location=\""+url+"\";\n"
			+"opener.location=\""+opener_url+"\";\n"
            +"</script>\n"
            ;

        return str;
    }

	public static String printClose() {
        String str = ""
            +"<script language=javascript>\n"
			+"opener.location.reload()\n"
			+"window.close();\n"
            +"</script>\n"
            ;

        return str;
    }

    public static String printAlertReload(String message) {
        String str = ""
            +"<script language=javascript>\n"
            +"alert(\""+ message +"\");\n"
            +"window.reload();\n"
            +"</script>\n"
            ;

        return str;
    }

	public static String printAlertCloseOnly(String message) {
        String str = ""
            +"<script language=javascript>\n"
            +"alert(\""+ message +"\");\n"
			+"window.close();\n"
            +"</script>\n"
            ;

        return str;
    }
	
	public static String printAlertCloseOnlyWithLayer(String message, String parentFunction) {
        String str = ""
            +"<script language=javascript>\n"
            +"alert(\""+ message +"\");\n"
            +"try{\n";
        if(parentFunction != null && !"".equals(parentFunction)){
        	str += parentFunction;
        }
        str += "window.close();\n";
        str += "}catch(e){}";
        str += "</script>\n"
            ;

        return str;
    }
	
	public static String printAlertParentCloseOnly(String message) {
        String str = ""
            +"<script language=javascript>\n"
            +"alert(\""+ message +"\");\n"
			+"parent.window.close();\n"
            +"</script>\n"
            ;

        return str;
    }

    public static String printConfirm(String message, String ok, String fail) {
        String str = 
            "<script language=javascript>\n"
            +"if(confirm(\""+ message +"\")){\n"
			+"this.location=\""+fail+"\";\n"
			+"}else{\n"
			+"this.location=\""+ok+"\";\n"
			+"}\n"
            +"</script>\n"
            ;

        return str;
    }
    
    public static String printAlertOpenerReload(String message, String url) {
    	 String str = ""
             +"<script language=javascript>\n"
             +"alert(\""+ message +"\");\n"
             +"document.location=\""+url+"\";\n"
             +"opener.location.reload();\n"
             +"</script>\n"
             ;

        return str;
    }
    
    public static String printAlertProcessUrl(String message, String url, String process) {
        String str = ""
            +"<script language=javascript>\n"
            +"alert(\""+ message +"\");\n"
            +process
			+"this.location=\""+url+"\";\n"			
            +"</script>\n"
            ;

        return str;
    }
    
    public static String printParentInnerHTML(String id, String text) {
        String str = ""
            +"<script language=javascript>"
			+"parent.document.getElementById(\""+id+"\").innerHTML = \""+text+"\";"
            +"</script>\n"
            ;


        return str;
    }
    
    public static String printParentStyleWidth(String id, String text) {
        String str = ""
            +"<script language=javascript>"
			+"parent.document.getElementById(\""+id+"\").style.width = \""+text+"\";"
            +"</script>\n"
            ;


        return str;
    }
    
    public static String printParentStyleDisplay(String id, String text) {
        String str = ""
            +"<script language=javascript>"
			+"parent.document.getElementById(\""+id+"\").style.display = \""+text+"\";"
            +"</script>\n"
            ;


        return str;
    }
}

