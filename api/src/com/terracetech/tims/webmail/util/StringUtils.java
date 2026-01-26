package com.terracetech.tims.webmail.util;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.StringTokenizer;

import org.eclipse.angus.mail.imap.protocol.*;
import com.terracetech.secure.SecureManager;
import com.terracetech.tims.webmail.common.EnvConstants;


public class StringUtils {

	
	public static String UTF8URLEncode(String str) {
		if (str == null) return null;

		String strTmp = str.trim();

		try {
			strTmp = URLEncoder.encode(strTmp, "UTF-8");
		} catch(UnsupportedEncodingException e) {
			System.out.println("-- UTF8URLEncode Error ["+str+"] ["+e.getMessage()+"]");
		}
		return strTmp;
	}

	public static String UTF8URLDecode(String str) {
		if (str == null) return null;

		String strTmp = str.trim();

		try {
			strTmp = URLDecoder.decode(strTmp, "UTF-8");
		} catch(UnsupportedEncodingException e) {
			System.out.println("-- UTF8URLDecode Error ["+str+"] ["+e.getMessage()+"]");
		}
		return strTmp;
	}

	/*
	* added by doyoung
	* 2006.12.18
	*/
	public static String IMAPFolderEncode(String str) {
		return (str != null) ? BASE64MailboxEncoder.encode(str) : null;
	}

	public static String IMAPFolderDecode(String str) {
		return (str != null) ? BASE64MailboxDecoder.decode(str) : null;
	}

	public static String char2latin(String str, String charset) {
		if (str == null) return null;

		String strTmp = str.trim();

		try {
			if (charset.equalsIgnoreCase("EUC-KR")) {
				strTmp = new String(strTmp.getBytes("8859_1"));
			}
			else {
				strTmp = new String(strTmp.getBytes("8859_1"), charset);
			}
		} catch(UnsupportedEncodingException e) {
			System.out.println("-- char2latin Error ["+str+"] ["+charset+"] ["+e.getMessage()+"]");
		}

		return strTmp;
	}

	public static String uni2latin(String str) {
		return char2latin(str, "UTF-8");
	}

	/*
	 * added by doyoung
	 * 2006.08.25
	 */
	public static String DBEncode(String str) {
		if (str == null) return null;

		String strTmp = str.trim();

		/*
		* USE ORACLE US7ASCII
		try {
			strTmp = new String(strTmp.getBytes("EUC-KR"), "8859_1");
		} catch(UnsupportedEncodingException e) {
			System.out.println("-- DBEncode Error ["+str+"] ["+e.getMessage()+"]");
		}
		*/

		return strTmp;
	}

	public static String DBDecode(String str) {
		if (str == null) return null;

		String strTmp = str.trim();

		/*
		* USE ORACLE US7ASCII
		try {
			strTmp = new String(strTmp.getBytes("8859_1"), "EUC-KR");
		} catch(UnsupportedEncodingException e) {
			System.out.println("-- DBDecode Error ["+str+"] ["+e.getMessage()+"]");
		}
		*/

		return strTmp;
	}

	/*
	* added by doyoung
	* 2006.07.03
	* 2006.10.10
	*/
	public static String getDownloadFileName(String str)
		throws UnsupportedEncodingException {

		return getDownloadFileName(str, null);
	}

	public static String getDownloadFileName(String str, String agent)
		throws UnsupportedEncodingException {

		if (Validation.isNull(str)) {
			return "unknown";
		}

//		if (Validation.isIncWin32InvalidChar(str)) {
//			System.out.println("getURLEncFileName");
//			return getURLEncFileName(str);
//
//		}
		return getLatinEncFileName(str, agent);
	}

	public static String getLatinEncFileName(String str)
		throws UnsupportedEncodingException {
		return getLatinEncFileName(str, null);
	}

	public static String getLatinEncFileName(String str, String agent) 
			throws UnsupportedEncodingException {

			if(str == null) {
				return "";
			}

			String fileName = str.replaceAll("\\\\", "");
			fileName = fileName.replaceAll("[\t\n\r]", " ");
			fileName = fileName.replaceAll("[/:*?\"<>|]", "_");
			/*
			if(fileName.indexOf("(") > -1 || fileName.indexOf(")") > -1) {
				if(fileName.indexOf("_") > -1){
					fileName = fileName.replaceAll("[/(]", "");
					fileName = fileName.replaceAll("[/)]", "");
				}
			}	
			*/	

			// FIRE FOX & NETSCAPE & WEBKIT		
			if(agent != null 
				&& (agent.toUpperCase().indexOf("FIREFOX") > 0 
				|| agent.toUpperCase().indexOf("NETSCAPE") > 0
				|| agent.toUpperCase().indexOf("WEBKIT") > 0
				|| BrowserUtil.isMoblieBrowser(agent))) {
				fileName = new String(fileName.getBytes("UTF-8"), "8859_1");
			}else if(agent.toUpperCase().indexOf("MSIE 7") > 0){
				fileName = getURLEncFileName(fileName, 1000);
			}else if(agent.toUpperCase().indexOf("MSIE 8") > 0){
				fileName = getURLEncFileName(fileName, 1000);
			}else {			
				String downLoadCharset = EnvConstants.getBasicSetting("file.download.charset");			
				if(downLoadCharset != null && downLoadCharset.length() > 0){
					String encFileName = new String(fileName.getBytes(downLoadCharset));
					if(fileName.equals(encFileName)){
						fileName = new String(fileName.getBytes(downLoadCharset), "ISO8859_1");
					} else {
						if(agent.toUpperCase().indexOf("MSIE 9") > 0 || agent.toUpperCase().indexOf("MSIE 10") > 0 || agent.toUpperCase().indexOf("RV:11") > 0) {
							fileName = getURLEncFileName(fileName, 1000);
						} else {
							fileName = getURLEncFileName(fileName, 170);
						}
					}
				} else {
					if(agent.toUpperCase().indexOf("MSIE 9") > 0 || agent.toUpperCase().indexOf("MSIE 10") > 0 || agent.toUpperCase().indexOf("RV:11") > 0) {
						fileName = getURLEncFileName(fileName, 1000);
					} else {
						fileName = getURLEncFileName(fileName, 170);
					}
				}
			}

			return fileName;
		}

		public static String getURLEncFileName(String str, int len)
			throws UnsupportedEncodingException {

			String strExt 	= "";
			String strName 	= "";
			String strFileName = "";
			int start = 0;

			if(str == null) {
				return "";
			}

			String fileName = str.replaceAll("\\\\", "");		
					
			fileName = fileName.replaceAll("[\t\n\r]", " ");		
			fileName = fileName.replaceAll("[/:*?\"<>|]", "_");
			//fileName = fileName.replaceAll(" ", "_");


			fileName = UTF8URLEncode(fileName);
			fileName = fileName.replaceAll("\\+", "%20");

			if(fileName.length() < len) {			
				return fileName;
			}

			start = str.lastIndexOf('.');

			if(start > 0) {
				strExt = str.substring(start);
				strName = str.substring(0, start);
			} else {
				strName = str;
			}

			while((fileName.length() >= len)) {
				strName = strName.substring(0, (strName.length() - 2));
				fileName = strName + "~" + strExt;
				fileName = fileName.replaceAll("\\\\", "");		
				fileName = fileName.replaceAll("[\t\n\r]", " ");		
				fileName = fileName.replaceAll("[/:*?\"<>|]", "_");
				//fileName = fileName.replaceAll(" ", "_");
				fileName = UTF8URLEncode(fileName);
				
			}
			
			return fileName;
		}
	
	public static String EscapeMailParam(String str) {
		if(str != null){
			str = str.replaceAll("'", "\\\\\'");
			str = str.replaceAll(" ", "");
		}
		return EscapeHTMLTag(str);
	}
	
	public static String escapeImapQuote(String keyword) {
		if (isEmpty(keyword)) return keyword;
		return keyword.replaceAll("\"", "\\\\\"");
	}
	
	public static String unescapeImapQuote(String keyword) {
		if (isEmpty(keyword)) return keyword;
		return keyword.replaceAll("\\\\\"", "\"");
	}
	
	public static String EscapeQuot(String aTagFragment) {
		final StringBuffer result = new StringBuffer();

		final StringCharacterIterator iterator = new StringCharacterIterator(aTagFragment);
		char character =  iterator.current();
		while (character != CharacterIterator.DONE ){

			if (character == '\"') {
				result.append("\\\"");
			}
			else if (character == '\'') {
				result.append("\\\'");
			}
			else {
				//the char is not a special one
				//add it to the result as is
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}
	
	public static String EscapeSqlChar(String aTagFragment) {
		StringBuffer result = new StringBuffer();
		final StringCharacterIterator iterator = new StringCharacterIterator(aTagFragment);
		char character =  iterator.current();
		while (character != CharacterIterator.DONE ){

			if (character == '\"') {
				result.append("\\\"");
			}
			else if (character == '\'') {
				result.append("\\\'");
			}else if (character == '%') {
				result.append("\\\\%");
			}else if (character == '_') {
				result.append("\\_");
			}
			else {				
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}
	public static String EscapeSqlCharByPostgresql(String aTagFragment) {
		StringBuffer result = new StringBuffer();
		final StringCharacterIterator iterator = new StringCharacterIterator(aTagFragment);
		char character =  iterator.current();
		while (character != CharacterIterator.DONE ){
			
			if (character == '\"') {
				result.append("\\\"");
			}
			else if (character == '\'') {
				result.append("''");
			}else if (character == '%') {
				result.append("\\\\%");
			}else if (character == '_') {
				result.append("\\_");
			}
			else {				
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}
public static void main(String[] args) {
	String test ="'test'@test";
	System.out.println(EscapeSqlCharByPostgresql(test));
	System.out.println(EscapeSqlChar(test));
}
	public static String EscapeHTMLTag(String aTagFragment) {
		StringBuffer result = new StringBuffer();
		
		if(aTagFragment != null){
			StringCharacterIterator iterator = new StringCharacterIterator(aTagFragment);
			char character =  iterator.current();
			while (character != CharacterIterator.DONE ){
				
				switch (character) {
					case '<':
					result.append("&lt;");
					    break;
					
					case '>':
					result.append("&gt;");
					    break;
					
					case '&':                
					result.append("&amp;");
					    break;
					
					case '"':
					result.append("&quot;");
					    break;
					
					case '\'':                
					result.append("&#39;");
					    break;
					    
					case '\\':                
					result.append("&#092;");
					    break;
					    
					case '{':                
						result.append("&#123;");
					    break;
					    
					case '}':                
						result.append("&#125;");
					    break;
					    
					case '[':                
						result.append("&#91;");
					    break;
					    
					case ']':                
						result.append("&#93;");
					    break;
					    
					case ':':                
						result.append("&#58;");
					    break;
					    
					case ' ':
						result.append("");
						break;					    
					    
					default :
						result.append(character);
						break;
						
				}
				character = iterator.next();
			}
		}
		return getCRLFEscape(result.toString());
	}

	public static String EscapeBackSlash(String str) {
		if (Validation.isNull(str)) {
			return "";
		}

		return str.replaceAll("\\\\", "\\\\\\\\");
	}

	public static boolean eq (String s1, String s2)
	{
		if (Validation.isNull(s1) || Validation.isNull(s2)) {
			return false;
		}

		return s1.equals(s2);
	}

	public static boolean eqIgn (String s1, String s2)
	{
		if (Validation.isNull(s1) || Validation.isNull(s2)) {
			return false;
		}

		return s1.equalsIgnoreCase(s2);
	}

	public static boolean inc (String s1, String s2)
	{
		if (Validation.isNull(s1) || Validation.isNull(s2)) {
			return false;
		}

		return (s1.indexOf(s2) >= 0);
	}
	/*public static String contentTagReplace(String content,boolean tagDisable)
	{
		StringReplacer sr = new StringReplacer();
		if(tagDisable)
		{
			content=content.replaceAll("<", "&lt;");
			content=content.replaceAll(">", "&gt;");
		}
		
		//content=sr.replace(content, tagDisable, tagDisable);		//data img 
		return content;
	}*/	
	
	public static boolean isEmpty(String txt)
	{
		if(txt ==null)
			return true;
		
		if("".equals(txt.trim()))
			return true;
		
		return false;
	}
	
	public static boolean isNotEmpty(String txt){
		return ! isEmpty(txt);
	}
	
	public static long[] getStringsToLongs(String[] longStrs){
		long[] longValues = null;
		if(longStrs != null){
			int size = longStrs.length;
			longValues = new long[size];
			for (int i = 0 ; i < size ; i++) {
				longValues[i] = Long.parseLong(longStrs[i]);
			}
		}
		return longValues;
	}
	
	public static String getLongsToString(long[] longs){
		StringBuffer sb = new StringBuffer();
		if(longs != null){
			int size = longs.length;			
			for (int i = 0 ; i < size ; i++) {
				if(i > 0){
					sb.append(",");
				}
				sb.append(longs[i]);
			}
		}
		return sb.toString();
	}
	
	public static int[] getStringsToInteger(String[] intStrs){
		int[] intValues = null;
		if(intStrs != null){
			int size = intStrs.length;
			intValues = new int[size];
			for (int i = 0 ; i < size ; i++) {
				intValues[i] = Integer.parseInt(intStrs[i]);
			}
		}
		return intValues;
	}
	
	public static String getCRLFEscape(String str){
		StringBuffer result = new StringBuffer();
		
		if(str != null){
			StringCharacterIterator iterator = new StringCharacterIterator(str);
			char character =  iterator.current();
			while (character != CharacterIterator.DONE ){
				if(Character.getType(character) != 15){
						switch (character) {
						case '\n':
						result.append("\n");
						    break;
						
						case '\t':
						result.append(" ");
						    break;
						
						case '\r':                
						result.append(" ");
						    break;
						    
						default :
							result.append(character);
							break;
							
						}
				} else {
					if(character == '\n'){
						result.append("\n");
					} else if(character == '\r'){
						
					}else {
						result.append(" ");
					}
				}
				character = iterator.next();
			}
		}
		return result.toString();
	}
	
	public static String getCRLFEscapeOnly(String str){
		StringBuffer result = new StringBuffer();
		
		if(str != null){
			int strLength = str.length();
			char character;
			for (int i = 0; i < strLength; i++) {
				character = str.charAt(i);
				switch (character) {
				case '\n':
				result.append("");
				    break;
				
				case '\t':
				result.append("");
				    break;
				
				case '\r':                
				result.append("");
				    break;
				    
				case ' ':                
				result.append("");
				    break;
				    
				default :
					result.append(character);
					break;
				}
			}	
		}
		return result.toString();
	}
	
	public static int getPointCount(String str){
		int count = 0;
		if(str != null){
			int strLength = str.length();
			char character;
			for (int i = 0; i < strLength; i++) {
				character = str.charAt(i);
				switch (character) {
				case '.':
					count++;
				    break;
				default :
					break;
				}
			}	
		}
		return count;
	}
	
	public static String replace(String src, String oldstr, String newstr)
    {
        if (src == null)
            return null;

        StringBuffer dest = new StringBuffer("");
        int  len = oldstr.length();
        int  srclen = src.length();
        int  pos = 0;
        int  oldpos = 0;

        while ((pos = src.indexOf(oldstr, oldpos)) >= 0) {
            dest.append(src.substring(oldpos, pos));
            dest.append(newstr);
            oldpos = pos + len;
        }

        if (oldpos < srclen)
            dest.append(src.substring(oldpos, srclen));

        return dest.toString();
    }
	
	public static int javaToHtml( char ch[], int start, int length, char[] out ,boolean isHtmlEscape) {
		
		boolean[] specialChars = new boolean[128];

		for ( int i=0; i<128; i++ )
		{
		  specialChars[i] = false;
		}

		specialChars['<'] = isHtmlEscape;
		specialChars['>'] = isHtmlEscape;
		specialChars['&'] = isHtmlEscape;
		specialChars['\''] = isHtmlEscape;
		specialChars['\"'] = isHtmlEscape;

		int o = 0;
		for (int i = start; i < start+length; i++){
			if ( ch[i]<=0x7f && !specialChars[ch[i]] ){
				out[o]=ch[i];
				o++;
			} else {
				if (ch[i]=='<'){("&lt;").getChars(0,4,out,o);  o+=4;}
				else if (ch[i]=='>')  {("&gt;").getChars(0,4,out,o);  o+=4;}
				else if (ch[i]=='&')  {("&amp;").getChars(0,5,out,o); o+=5;}
				else if (ch[i]=='\"') {("&#34;").getChars(0,5,out,o); o+=5;}
				else if (ch[i]=='\'') {("&#39;").getChars(0,5,out,o); o+=5;}
				else {
					String dec = "&#" + Integer.toString((int)ch[i]) + ';';
					dec.getChars(0, dec.length(), out, o);
					o+=dec.length();
				}
			}
		}
		return o;
	}

	public static String javaToHtml(String source )
	{
		if(source == null) return "";
		char[] dest = new char[ source.length()*8 ];
		int newlen = javaToHtml( source.toCharArray(), 0, source.length(), dest, true);
		return new String( dest, 0, newlen );
	}
	
	public static String javaToHtml(String source , boolean isHtmlEscape)
	{
		char[] dest = new char[ source.length()*8 ];
		int newlen = javaToHtml( source.toCharArray(), 0, source.length(), dest, isHtmlEscape);
		return new String( dest, 0, newlen );
	}
	
	public static String doubleUrlDecode(String encodeString) throws UnsupportedEncodingException {
		return isEmpty(encodeString) ? "" : URLDecoder.decode(encodeString, "UTF-8");
	}
	
	public static String getSystemDecriptString(String cryptMethod, String encriptStr) throws Exception{
		if(StringUtils.isEmpty(cryptMethod)){
			cryptMethod = "AES";
		}
		SecureManager.setCookieAlgorithm(cryptMethod);
		return SecureManager.getDeCryptedCookieStr(encriptStr);
	}
	
	public static String getSystemEcriptString(String cryptMethod, String criptStr) throws Exception{
		if(StringUtils.isEmpty(cryptMethod)){
			cryptMethod = "AES";
		}
		SecureManager.setCookieAlgorithm(cryptMethod);
		return SecureManager.getCryptedCookieStr(criptStr);
	}
	
	public static String replaceString(String str, String dummy) {
		String newStr = str == null ? "" : str;
		// TODO TimsV6������ �� ���ڵ� ��ȯ�� ������?
		if (newStr.endsWith("\",")) {
			newStr = newStr.replaceAll("\",", "");
		}

		newStr = newStr.replaceAll("\"", "\"\"");

		if (dummy.equalsIgnoreCase("outlook") || newStr.indexOf(',') >= 0
				|| newStr.indexOf('\n') >= 0) {
			newStr = "\"" + newStr + "\"";
		}
		return newStr;
	}
	
	public static String getDecodingUTF(String word){ 

		String decodedWord = ""; 

		if (word == null) 
			return null; 
		else { 
			byte[] b; 
			try { 
				b = word.getBytes("8859_1"); 
				CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder(); 
				try { 
					CharBuffer r = decoder.decode(ByteBuffer.wrap(b)); 
					decodedWord = r.toString(); 
				} catch (CharacterCodingException e) {
					decodedWord = word;
					//System.out.println(new String(b, "EUC-KR")); 
				} 
			} catch (UnsupportedEncodingException e1) { 
				decodedWord = word; 
			} 
		} 
		return decodedWord; 
	}
	
	
	public static String join(String[] strs, String separator){
		String joinStr = null;
		if(strs != null){
			StringBuffer sb = new StringBuffer(); 
			for (int i = 0; i < strs.length; i++) {
				if(i > 0)sb.append(separator);
				sb.append(strs[i]);
			}
			joinStr = sb.toString();
		}
		
		return joinStr;
	}
	
	public static String join(String delimiter, short[] array) {
        if (array == null) {
            return null;
        }
        
        StringBuilder buf = new StringBuilder();
        
        for (int i = 0; i < array.length; i++) {
            buf.append(array[i]);
            if (i + 1 < array.length) {
                buf.append(delimiter);
            }
        }
        return buf.toString();
    }

    /**
     * Joins an array of objects, separated by a delimiter.
     */
    public static String join(String delimiter, Object[] array) {
        return (array == null ? null : join(delimiter, array, 0, array.length));
    }

    public static String join(String delimiter, Object[] array, final int start, final int count) {
        if (array == null)
            return null;

        StringBuilder buf = new StringBuilder();
        for (int i = start, end = start + count; i < end; i++) {
            buf.append(array[i]);
            if (i + 1 < end)
                buf.append(delimiter);
        }
        return buf.toString();
    }

    public static String join(String delimiter, Iterable<? extends Object> array) {
        if (array == null)
            return null;

        boolean firstTime = true;
        StringBuilder buf = new StringBuilder();
        for (Object obj : array) {
            if (firstTime) {
                firstTime = false;
            } else {
                buf.append(delimiter);
            }
            buf.append(obj);
        }
        return buf.toString();
    }
    
    public static void printByteToHex(byte byteArray[]){
    	for(int i = 0; i < byteArray.length; i += 16){
			for (int j = i; j < Math.min(i + 16, byteArray.length); j ++) {
				int b = ((int) byteArray[j]) & 0x0ff;
				System.out.print(Integer.toHexString(b / 16));
				System.out.print(Integer.toHexString(b % 16));
				System.out.print(' ');
			}
			
			for (int j = i; j < Math.min(i + 16, byteArray.length); j ++) {
				int b = byteArray[j];
				System.out.print(b >= 32 && b <= 127  ? (char) b : '?');
			}
			
			System.out.println();
		}
    	
    	System.out.println();
    }
    
    public  static byte[] hexToByteArray(String hex) {
    	if (hex == null || hex.length() == 0) {
    	        return null;
    	    }
    	 
    	    byte[] ba = new byte[hex.length() / 2];
    	    for (int i = 0; i < ba.length; i++) {
    	        ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
    	    }
    	    return ba;
    	} 
    
    public static void printHexToByte(String hex){
    	String[] hexData = hex.split(" ", -1);
		for (String string : hexData) {
			byte byteValue = (byte) Integer.parseInt(string, 16);;
			
			System.out.print(byteValue);
			System.out.print(",");
		}
		
		System.out.println();
    }
    
    public static String convertStringToHex(String str){
        
        char[] chars = str.toCharArray();
      
        StringBuffer hex = new StringBuffer();
        for(int i = 0; i < chars.length; i++){
          hex.append(Integer.toHexString((int)chars[i]));
        }
      
        return hex.toString();
    }
    
    public static String getValue(String src, String replace){
    	if(isEmpty(src))
    		return replace;
    	
    	return src;
    }
    
    public static String removeStyleTag(String content) {
    	if(content == null) return content;
        content = content.replaceAll("<( )*style([^>])*?>","<style>");
        content = content.replaceAll("<( )*STYLE([^>])*?>","<style>");
        content = content.replaceAll("(<( )*(/)( )*?style( )*>)","</style>");
        content = content.replaceAll("(<( )*(/)( )*?STYLE( )*>)","</style>");
        content = content.replaceAll("(<style>)[\\s\\S]*?(</style>)","");
        return content;
    }
    
    public static String html2Text(String oldtext) {

		StringBuffer newtext = new StringBuffer();
		int cnt = 0;

		if(oldtext == null) return null;

		String strTmp = oldtext;
		int idx = (strTmp.toLowerCase()).indexOf("<body");

		if (idx > 0) {
			strTmp = strTmp.substring(idx);
		}
		
		strTmp = strTmp.replaceAll("\n","");
		strTmp = strTmp.replaceAll("<BR>", "\n");
		strTmp = strTmp.replaceAll("<br>", "\n");
		strTmp = strTmp.replaceAll("<br/>", "\n");
		strTmp = strTmp.replaceAll("<br />", "\n");
		strTmp = strTmp.replaceAll("&nbsp;", " ");
		strTmp = strTmp.replaceAll("&quot;", "\"");
		strTmp = strTmp.replaceAll("&lt;", "<");
		strTmp = strTmp.replaceAll("&gt;", ">");

		StringTokenizer st = new StringTokenizer(strTmp, ">");
		
		if(strTmp.indexOf(">") > -1){
			String checkStr;
			while(st.hasMoreElements()) {
				String strElem = (String)st.nextElement();
				int lastIndex = strElem.indexOf("<");
	
				if(lastIndex <= 0){
					if(!st.hasMoreElements()){
						newtext.append(strElem);
					}
					continue;										
				}
				
				newtext.append(strElem.substring(0, lastIndex));
				checkStr = strElem.substring(lastIndex+1);
				if(checkStr.indexOf("@") > -1){
					newtext.append("<"+checkStr+">");
				}
	
				if (cnt > 1000) break;
				cnt++;
			}
			newtext.append("\n");
		} else {
			newtext.append(strTmp);
		}
		
		return newtext.toString();
	}
    
    public static String textToHTML(String oldtext) {
		StringBuffer result = new StringBuffer();
		
		if(oldtext != null){
			StringCharacterIterator iterator = new StringCharacterIterator(oldtext);
			char character =  iterator.current();
			while (character != CharacterIterator.DONE ){
				
				switch (character) {
					case '\n':
						result.append("<br/>");
					    break;
					case ' ':
						result.append("&nbsp;");
					    break;
					case '<':
					result.append("&lt;");
					    break;
					
					case '>':
					result.append("&gt;");
					    break;
					
					case '&':                
					result.append("&amp;");
					    break;
					
					case '"':
					result.append("&quot;");
					    break;
					
					case '\'':                
					result.append("&#39;");
					    break;
					    
					case '\\':                
					result.append("&#092;");
					    break;
					    
					default :
						result.append(character);
						break;
						
				}
				character = iterator.next();
			}
		}
		return result.toString();		
	}
}