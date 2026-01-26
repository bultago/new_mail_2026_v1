
<%@page import="com.terracetech.secure.DESede"%><%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="javax.crypto.*,javax.crypto.spec.*" %>
<%@ page import="com.terracetech.tims.common.I18nConstants"%>
<%@ page import="com.terracetech.tims.webmail.mailuser.User"%>
<%@ page import="com.terracetech.tims.webmail.mailuser.manager.UserAuthManager"%>
<%@ page import="java.util.*"%>
<%!
public static final String byteArrayToHexString(byte abyte0[])
{
	boolean flag = false;
	int i = 0;
	if(abyte0 == null || abyte0.length <= 0)
		return null;
	String as[] = {
		"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", 
		"A", "B", "C", "D", "E", "F"
	};
	StringBuffer stringbuffer = new StringBuffer(abyte0.length * 2);
	for(; i < abyte0.length; i++)
	{
		byte byte0 = (byte)(abyte0[i] & 0xf0);
		byte0 >>>= 4;
		byte0 &= 0xf;
		stringbuffer.append(as[byte0]);
		byte0 = (byte)(abyte0[i] & 0xf);
		stringbuffer.append(as[byte0]);
	}

	String s = new String(stringbuffer);
	return s;
}
%>
<%
User user = UserAuthManager.getUser(request);

if(user != null){
	String key = "";
	String email = user.get(User.EMAIL);
	
	byte[] desKeyData = key.getBytes();
	DESKeySpec desKeySpec = new DESKeySpec(desKeyData);
	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	SecretKey deskey = keyFactory.generateSecret(desKeySpec);
	
	Cipher des = Cipher.getInstance("DES/ECB/NOPadding");
	des.init(Cipher.ENCRYPT_MODE, deskey);
	
	int slen = email.length() % 8;
	int i = ( 8 - slen );
	if((i>0) && (i<8)){
		StringBuffer buf = new StringBuffer(email.length()+i);
		buf.insert(0,email);
		for(i=(8-slen);i>0;i--)
			buf.append('\0');
		email = buf.toString();
	}		
	
	byte[] stringBytes = email.getBytes("UTF8");
	byte[] raw = des.doFinal(stringBytes);
	
	String encryptedId = byteArrayToHexString(raw);
%>
<META HTTP-EQUIV=refresh CONTENT='0; URL=http://spam.mtekvision.com/sso.do?email=<%=encryptedId%>'>
<%}%>

