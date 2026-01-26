<%@ page contentType="text/html; charset=euc-kr" %>
<%@ page pageEncoding="euc-kr" %>

<%

java.util.Enumeration e = System.getProperties().propertyNames();

java.util.Properties props = System.getProperties();

while(e.hasMoreElements()) {

   String obj = (String)e.nextElement();
   String value = (String)props.get(obj);
   out.println(obj +" ==> " + value + " <br>");
}
%>
