package com.terracetech.tims.webmail.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.tidy.Tidy;

import com.sun.mail.util.LineInputStream;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.ImageInfoBean;

public class StringReplaceUtil {

    private static Hashtable tag2xtag = new Hashtable();
    private static boolean isIgnoreCase = true;

	public StringReplaceUtil() {
		new StringReplaceUtil(true);
	}

	public StringReplaceUtil(boolean isIgnoreCase) {
		this.isIgnoreCase = isIgnoreCase;
        InputStream is = StringReplaceUtil.class.getResourceAsStream("/htmlfilter.map");

        if (is != null) {
            loadMappings( new LineInputStream(is), tag2xtag);
		}
		else {
			tag2xtag.put("<embed", "<x-embed");
			tag2xtag.put("<iframe", "<x-iframe");
			tag2xtag.put("<script", "<x-script");
			tag2xtag.put("<object", "<x-object");
		}
	}

    private static void loadMappings(LineInputStream is, Hashtable table) {
        String currLine;

        while (true) {
            try {
                currLine = is.readLine();
            }
            catch (IOException ioex) {
                break; // error in reading, stop
            }

            if (currLine == null) {
                break; // end of file, stop
            }

            if (currLine.startsWith("--") && currLine.endsWith("--")) {
                break; // end of this table
            }

            if (currLine.trim().length() == 0 || currLine.startsWith("#")) {
                continue; // ignore empty lines and comments
            }

            // A valid entry is of the form <key><separator><value>
            // where, <separator> := SPACE | HT | '='. Parse this
			// Modified by isle4 2008.03.18
			/*
            StringTokenizer tk = new StringTokenizer(currLine, " \t=");
            try {
                String key = tk.nextToken();
                String value = tk.nextToken();

                table.put(key.toLowerCase(), value);
            }
            catch (NoSuchElementException nex) {nex.printStackTrace(); }
			*/

			// = 또는 \t를 구분자로 두개만 나눈다.
			String[] strArray = currLine.split("[=\t]", 2);
			try {
				String key = strArray[0];
				String value = strArray[1];
				table.put(key.toLowerCase(), value);
			} catch (Exception e) {				
				LogManager.writeErr(StringReplaceUtil.class, e.getMessage(), e);
			}
        }
    }

    public int getEndPosition(String text, int start) {
		int endpos1 = text.indexOf("\n", start);
		int endpos2 = text.indexOf("\t", start);
		int endpos3 = text.indexOf(" ", start);
		int endpos4 = text.indexOf("<br>", start);

		int endpos = text.length();

		endpos = Math.min(
			(endpos1 < 0) ? endpos : endpos1,
			(endpos2 < 0) ? endpos : endpos2);
		endpos = Math.min(endpos, (endpos3 < 0) ? endpos : endpos3);
		endpos = Math.min(endpos, (endpos4 < 0) ? endpos : endpos4);

		return endpos;
	}

	/*
	 * added by doyoung
	 * 2006.06.23
	 */
    public String replace(String oldtext, 
		boolean isHiddenImg,
		boolean isHiddenTag,
		boolean isHtml) {

		if (isHtml) {
			return replace(oldtext, isHiddenImg, isHiddenTag);
		}
		else {
			return replaceText2Html(oldtext);
		}
	}

	public String replaceHtml2Text(String oldtext, boolean isHtml) {

		if (isHtml) {
			return replaceHtml2Text(oldtext);
		}
		else {
			return oldtext;
		}
	}

	public String replaceHtml2Text(String oldtext) {

		StringBuffer newtext = new StringBuffer();
		int cnt = 0;

		if(oldtext == null) return null;

		String strTmp = oldtext;
		int idx = (strTmp.toLowerCase()).indexOf("<body");

		if (idx > 0) {
			strTmp = strTmp.substring(idx);
		}

		strTmp = strTmp.replaceAll("<BR>", "\n");
		strTmp = strTmp.replaceAll("<br>", "\n");
		strTmp = strTmp.replaceAll("<br/>", "\n");
		strTmp = strTmp.replaceAll("<br />", "\n");
		strTmp = strTmp.replaceAll("&nbsp;", " ");
		strTmp = strTmp.replaceAll("&lt;", "<");
		strTmp = strTmp.replaceAll("&gt;", ">");

		StringTokenizer st = new StringTokenizer(strTmp, ">");

		while(st.hasMoreElements()) {
			String strElem = (String)st.nextElement();
			int lastIndex = strElem.indexOf("<");

			if(lastIndex <= 0) continue;

			newtext.append(strElem.substring(0, lastIndex));

			if (cnt > 1000) break;
			cnt++;
		}

		newtext.append("\n");

		return newtext.toString();
	}
	
	public String getTextToHTML(String oldtext) {
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
	
	public String getHtmlToText(String oldtext) {

		StringBuffer newtext = new StringBuffer();
		int cnt = 0;
		
		if(oldtext == null) {
			return "";
		}
		
		int s = oldtext.indexOf("style>") + 6;
		
		String strTmp = (s < 6) ? oldtext : oldtext.substring(s);
		
		strTmp = strTmp.replaceAll("<BR>", "\n");
		strTmp = strTmp.replaceAll("<br/>", "\n");
		strTmp = strTmp.replaceAll("<br />", "\n");
		strTmp = strTmp.replaceAll("<br>", "\n");
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
		} else {
			newtext.append(strTmp);
		}	
		
		return newtext.toString();
	}

	public String replaceText2Wml(String oldtext) {

		if(oldtext == null) return null;

		String strTmp = oldtext.trim();

		strTmp = strTmp.replaceAll("<", "&lt;");
		strTmp = strTmp.replaceAll(">", "&gt;");
		strTmp = strTmp.replaceAll("\"", "&quot;");

		// System.out.println("-- strTmp : ["+strTmp+"]");

		String[] strArray = strTmp.split("\n");
		boolean isBeforeIsNull = false;

		StringBuffer newtext = new StringBuffer();

		for (int i = 0; i < strArray.length; i++) {
			String line = strArray[i].trim();

			if (!Validation.isNull(line)) {
			
				if (isBeforeIsNull) {
					newtext.append("\n<p>&nbsp;</p><p></p>" + line);
				}
				else {
					newtext.append("\n<p></p>" + line);
				}

				isBeforeIsNull = false;
			}
			else {
				isBeforeIsNull = true;
			}
		}

		return newtext.toString();
	}

    public String replaceDrafts(String oldtext, 
		boolean isHiddenImg,
		boolean isHiddenTag) {
		String newtext = oldtext;

    	Hashtable ht = new Hashtable();

		if (isHiddenImg) {
			ht.put("<img", "<x-img");
			ht.put("background", "x-background");
		}

		if (isHiddenTag) {
			Enumeration enumer = tag2xtag.keys();

        	while (enumer.hasMoreElements()) {
            	String key = (String) enumer.nextElement();
				ht.put(key, tag2xtag.get(key));
        	}
		}

		ht.put("<a ", "<a target='_blank' ");
		ht.put("<area ", "<area target='_blank' ");

		return replace(newtext, ht, isIgnoreCase);
	}

    public String replace(String oldtext, 
		boolean isHiddenImg,
		boolean isHiddenTag) {
		String newtext = replaceHTMLBodyTag(oldtext);

    	Hashtable ht = new Hashtable();

		if (isHiddenImg) {
			ht.put("<img", "<x-img");
			ht.put("<link", "<x-link");
			ht.put("<style", "<!--x-style");
			ht.put("</style", "--></x-style");
			ht.put("background", "x-background");
		}

		if (isHiddenTag) {
			Enumeration enumer = tag2xtag.keys();

        	while (enumer.hasMoreElements()) {
            	String key = (String) enumer.nextElement();
				ht.put(key, tag2xtag.get(key));
        	}
		}

		ht.put("</body>", "</td></tr></table>");
		ht.put("<a ", "<a target='_blank' ");
		ht.put("<area ", "<area target='_blank' ");

			
		return filterMdnUrl(isHiddenImg , replace(newtext, ht, isIgnoreCase));
	}
    
    private String filterMdnUrl(boolean isHiddenImg, String contents){
    	
    	if(isHiddenImg){    	
	    	int idx = contents.indexOf("TMSMDN",0);
			int urlIdx = 0;
	    	String head,body,body1,body2;
	    	while(idx > -1){
	    		head = contents.substring(0, idx);
	    		body = contents.substring(idx, contents.length());
	    		urlIdx = body.indexOf("x-img");
	    		if(urlIdx > -1){
		    		body1 = body.substring(0, urlIdx);
		    		body2 = body.substring(urlIdx + 5, body.length());
		    		body = body1 + "img" + body2;
	    		}
	    		
	    		contents = head + body;
	    		idx = contents.indexOf("TMSMDN" , idx + 6);
	    	}
	    	head = null;
	    	body = null;
	    	body1 = null;
	    	body2 = null;
    	}
    	
    	return contents;
    }
    
    public static String removeMdnUrl(String contents){
    	String head = "";
    	String tail = "";
    	if("true".equalsIgnoreCase(EnvConstants.getMailSetting("mail.debug")))
    		System.out.println("### removeMdnUrl - contents : "+contents);
    	
    	if(StringUtils.isEmpty(contents)) return contents;
    	if(contents != null){
    		try{
    		int idx = contents.indexOf("TMSMDN",0);
    		if(idx == -1) return contents;
    		int tableStartIdx = idx - 11;
    		
    		head = contents.substring(0, tableStartIdx);
    		if("true".equalsIgnoreCase(EnvConstants.getMailSetting("mail.debug")))
    			System.out.println("### removeMdnUrl - head : "+head);
    		
    		int tableEndIdx = contents.indexOf("</table>",idx+6);
    		tail = contents.substring(tableEndIdx+8);
    		if("true".equalsIgnoreCase(EnvConstants.getMailSetting("mail.debug")))
    			System.out.println("### removeMdnUrl - tail : "+tail);
    		
    		}catch(Exception e){
    			e.printStackTrace();
    			return contents;
    		}
    	}
    	
    	if("true".equalsIgnoreCase(EnvConstants.getMailSetting("mail.debug")))
    		System.out.println("### removeMdnUrl - head + tail : "+head + tail);
    	
    	return head + tail;
    }

    public String replaceText2Html(String oldtext) {
		String newtext	= oldtext.replaceAll("<", "&lt;");
		newtext = newtext.replaceAll("\n", "\n<br>");
		return replaceLink(newtext);
	}

    public String replace(String oldtext, String oldstr, String newstr,
        boolean ignoreCase) {

        String worktext = (ignoreCase)? oldtext.toLowerCase() : oldtext;
        StringBuffer sb = new StringBuffer();
        int start = 0;
        int end = 0;

        if (ignoreCase) {
            oldstr = oldstr.toLowerCase();
        }

        while ((end = worktext.indexOf(oldstr, start)) != -1) {
            sb.append(oldtext.substring(start, end) + newstr);

        }

        sb.append(oldtext.substring(start));

        return sb.toString();
    }

    public String replace(String oldtext, Hashtable ht, boolean ignoreCase) {
        String worktext = (ignoreCase)? oldtext.toLowerCase() : oldtext;
        StringBuffer newtext = new StringBuffer();
        int start = 0;
        Vector keys = new Vector();
        Enumeration enumer = ht.keys();
       
        while (enumer.hasMoreElements()) {
            String key = (String) enumer.nextElement();
            if (ignoreCase) {
                key = key.toLowerCase();
            }

			// System.out.println("-- key ["+key+"]["+ht.get(key)+"]");

            keys.addElement(key);
        }

        while (true) {
            int end = oldtext.length();
            String keystr = null;
            int keylen = 0;

            for (int i = 0; i < keys.size();) {
                String k = (String) keys.elementAt(i);
                int idx = worktext.indexOf(k, start);

                if (idx == -1) {
                    keys.removeElementAt(i);
                    continue;
                }

                if (idx < end) {
                    end = idx;
                    keystr = k;
                    keylen = k.length();
                }

                i++;
            }

            if (keystr == null) {
                break;
            }

            newtext.append(oldtext.substring(start, end));
            newtext.append(ht.get(keystr));
            start = end + keylen;
        }

        if (start < oldtext.length()) {
            newtext.append(oldtext.substring(start));
        }

        return newtext.toString();
    }
    
    public String replaceResizeImg(String oldtext, Map<String, ImageInfoBean> resizeImgMap) {
    	String workStr = oldtext;
    	if(resizeImgMap != null && resizeImgMap.size() > 0){
	    	
	    	Iterator<String> iter = resizeImgMap.keySet().iterator();
	    	String cid = null;
	    	int start,end,pos;
	    	String sStr,eStr;
	    	String resizeImg = "";
	    	ImageInfoBean tmpImg = null;
	    	while (iter.hasNext()) {
	    		cid = iter.next();
	    		tmpImg = resizeImgMap.get(cid);	    			    		
	    		start =  workStr.indexOf(cid);	    		
	    		if(start > -1){	    			
	    			sStr = workStr.substring(0, start-1);
	    			end = sStr.lastIndexOf("<");
	    			sStr = workStr.substring(0, end-1);
	    			
	    			eStr = workStr.substring(start + cid.length());
	    			end = eStr.indexOf(">");
	    			eStr = eStr.substring(end+1);
	    			
	    			resizeImg = "<a href=\""+tmpImg.getOrgImgName()+"\" target=\"_blank\"><img src=\""+tmpImg.getResizeImgName()+"\" width=\""+tmpImg.getWidth()+"\" height=\""+tmpImg.getHeight()+"\" style='border:none;'></a>";
	    			
	    			workStr = sStr +resizeImg+ eStr;
	    		} 		
	    		
			}
	    	
    	} 
    	return workStr;
    }

    public String replaceLink(String text) {

        String worktext = text;
        StringBuffer sb = new StringBuffer();
        int start = 0;
        int end = 0;

        while ((end = worktext.indexOf("http", start)) != -1) {
			int endpos = getEndPosition(worktext, end+1);
			String newstr = "";

			if(endpos > end) {
				newstr = text.substring(end, endpos).trim();

				boolean isLink = newstr.matches(
					"((http|ftp|https)://([a-zA-Z0-9]|-|[_,.@/\\~:?%%=&])+)");

				if(isLink) {
					newstr = "<a href=\""+newstr+"\" target=_blank>"+newstr+"</a>";
				}

            	sb.append(text.substring(start, end) + newstr);
            	start = end + (endpos - end);
			}
			else {
				break;
			}
        }

        sb.append(text.substring(start));

        return sb.toString();
    }

    public String replaceHTMLBodyTag(String oldtext) {
        String worktext = oldtext.toLowerCase();
        StringBuffer newtext = new StringBuffer();
        int start = 0;
        int end = 0;

        while ((end = worktext.indexOf("<body", start)) != -1) {
            newtext.append(oldtext.substring(start, end));
            newtext.append("<table width=100% cellpadding='0' cellspacing='0' border=0 ");
            start = end + 5;

            end = worktext.indexOf(">", start);
            newtext.append(oldtext.substring(start, end));
            newtext.append("><tr><td valign='top'>");
            start = end + 1;
        }

        newtext.append(oldtext.substring(start));

        return newtext.toString();
    }

	public boolean isInImage(String text) {
        String worktext = text.toLowerCase();

		if (worktext.indexOf("<img") >= 0
			|| worktext.indexOf("background") > 0
			|| worktext.indexOf("background-image") > 0) {
			return true;
		}
		return false;
	}
	
	public static String convertHtmlBr(String comment) {
        if (comment == null)
            return "";
        StringReader sr = new StringReader(comment);
        BufferedReader dataIn = new BufferedReader(sr);
        StringBuffer buffer = new StringBuffer();

        try{
        String line = "";
        while((line = dataIn.readLine()) != null){
            if(line.endsWith("<br>"))
            buffer.append(line + "\r\n");
            else
            buffer.append(line + "<br>\r\n");
        }
        }catch(IOException e1){}
        return buffer.toString();
    }

	
	public static String replaceHtml(String oldtext) {
		
		if(oldtext == null) return null;
		
		StringBuffer result = new StringBuffer();
		String strTmp = oldtext;
		
		try{
			strTmp = URLDecoder.decode(strTmp, "utf-8");
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		if(strTmp != null){
			StringCharacterIterator iterator = new StringCharacterIterator(strTmp);
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

					case '%':                
						result.append("\\\\%");
					    break;
					    
					case '_':                
						result.append("\\_");
					    break;  
					    
					default :
						result.append(character);
						break;
						
				}
				character = iterator.next();
			}
		}
		
		strTmp = result.toString();
				
		return strTmp;
	}
	
	public static String replaceEventAttr(String str) {
		String EVENTS = "(?i)onload|onunload|onchange|onsubmit|onreset|onerror"
				+ "|onselect|onblur|onfocus|onkeydown|onkeypress|onkeyup|onclick|ondblclick|onmousedown|onmousemove|onmouseout|onmouseover|onmouseup";

		String XSS_REGEX_ONEVENT = "(" + EVENTS + ".*)=s*(\"|')[^\"|']*(\"|')";
		String XSS_REGEX_ONEVENT_OTHER = "(" + EVENTS + ".*)";
		String XSS_REGEX_SCRIPT = "<(\\/?)(script)([^<>]*)>";

		if (str == null) {
			return null;
		}
		
		str = str.replaceAll(XSS_REGEX_ONEVENT, "");
		str = str.replaceAll(XSS_REGEX_ONEVENT_OTHER, "");
		str = str.replaceAll(XSS_REGEX_SCRIPT, "");
		
		return str;
	}
	
	//20151203
    public static String replaceMsPasteWord(String content){

    	try {
			content = formatHTML(content, true, false, false);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			
			content = content.replaceAll("<o:p>\\s*</o:p>", "");
			content = content.replaceAll("<o:p>[\\s\\S]*?</o:p>", "&nbsp;");
			content = content.replaceAll("<O:P>\\s*</O:P>", "");
			content = content.replaceAll("<O:P>[\\s\\S]*?</O:P>", "&nbsp;");

			// Remove mso-xxx styles.
			content = content.replaceAll("(?i)\\s*mso-[^:]+:[^;\"']+;?","");

			// Remove class Mso*
			content = content.replaceAll("(?i)<(\\w[^>]*) class=\"([^\"]*)Mso[A-Za-z]*([^\"]*)\"([^>]*)", "<$1 class=\"$2 $3\" $4");
			content = content.replaceAll("(?i)<(\\w[^>]*) class=\"([^\"]*)SpellE([^\"]*)\"([^>]*)", "<$1 class=\"$2 $3\" $4");
			content = content.replaceAll("(?i)<(\\w[^>]*) class=Mso[A-Za-z]*([^>]*)", "<$1$2");
			content = content.replaceAll("(?i)<(\\w[^>]*) class=SpellE([^>]*)", "<$1$2");

			// Remove Tag Comments
			content = content.replaceAll("<w:.*?>", "");
			content = content.replaceAll("</w:.*?>", "");
			content = content.replaceAll("<v:.*?>", "");
			content = content.replaceAll("</v:.*?>", "");
			content = content.replaceAll("<o:.*?>", "");
			content = content.replaceAll("</o:.*?>", "");
			
			// 카카오에서 보내는 알림 메일에서 붙어오는 테그 삭제 - TCUSTOM-2418 - 20161019 CLONE
			content = content.replaceAll("<!--\\[if \\(gte mso 9\\)\\|\\(IE\\)\\]", "");
			
			// 특정 태그 범위 인식오류 문제 수정  - TCUSTOM-2216 - 20161019 CLONE
			content = content.replaceAll("<!--StartFragment-->", "");

			// 특정 태그 범위 삭제
			content = content.replaceAll("<!--.*?\\]>", "");
			content = content.replaceAll("<!.*?\\]-->", "");
			content = content.replaceAll("<!\\[.*?\\]>", "");
		}
    			
		return content;
    }

	/**
	 * Formats the given HTML source.
	 * origin url : https://github.com/sakim/snownote/blob/master/org.eclipse.epf.richtext/src/org/eclipse/epf/common/html/HTMLFormatter.java
	 *
	 * @param html The HTML source.
	 * @param forceOutput if true, return cleaned HTML even if errors. if false, will clean minor problems and return clean HTML, but on a major error, will set getLastErrorStr() and return passed-in html
	 * @param makeBare set to true for cleaning MS HTML
	 * @param word2000 set to true for cleaning MS Word 2000 HTML
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String formatHTML(String html, boolean forceOutput, boolean makeBare, boolean word2000) throws UnsupportedEncodingException {
		if (html == null || html.length() == 0) {
			return html;
		}

		Tidy tidy = new Tidy();
		tidy.setXHTML(true);
		tidy.setDropEmptyParas(false);
		tidy.setDropFontTags(false);
		tidy.setQuiet(true);
		tidy.setShowWarnings(false);
		tidy.setSmartIndent(false);
		tidy.setTidyMark(false);
		tidy.setIndentAttributes(false);
		tidy.setHideComments(true);
		tidy.setMakeClean(true);

		//tidy.setWraplen(lineWidth);
		//tidy.setIndentContent(indent);
		//tidy.setSpaces(indentSize);
		//tidy.setInputEncoding("UTF-16"); //$NON-NLS-1$
		//tidy.setOutputEncoding("UTF-16"); //$NON-NLS-1$

		// this will add <p> around each text block (?that isn't in a block already?)
		//tidy.setEncloseBlockText(true);

		// setting this seemed to prevent JTidy from indenting the source
		//tidy.setPrintBodyOnly(true);

		if (forceOutput) {
			// output document even if errors are present
			tidy.setForceOutput(true);
		}
		if (makeBare) {
			// remove MS clutter
			tidy.setMakeBare(true);
		}
		if (word2000) {
			// draconian Word2000 cleaning
			tidy.setWord2000(true);
		}

		Reader input = new StringReader(html);
		Writer output = new StringWriter();

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		tidy.setErrout(pw);
		tidy.parse(input, output);

		String error = sw.getBuffer().toString();
		if (error != null && error.length() > 0 && error.startsWith("line") && error.indexOf("column") > 0) { //$NON-NLS-1$ //$NON-NLS-2$
			System.out.println("[StringReplaceUtil.formatHTML] ERROR MSG ### " + error);

			if (!forceOutput) {
				System.out.println("[StringReplaceUtil.formatHTML] if forceOutput is true, JTidy will return clean HTML so don't return here ### ");
				// if forceOutput is true, JTidy will return clean HTML so don't return here
				return html;
			}
		}

		String formattedHTML = output.toString();

		return formattedHTML;
	}    
    
}
