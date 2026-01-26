package com.terracetech.tims.webmail.util;

import java.io.*;
import java.util.*;
import org.eclipse.angus.mail.util.*;
import com.terracetech.tims.webmail.util.Validation;

public class StringReplacer {

    private static Hashtable tag2xtag = new Hashtable();
    private static boolean isIgnoreCase = true;
	private static boolean isBody = false;

	public StringReplacer() {
		new StringReplacer(true);
	}

	public StringReplacer(boolean isIgnoreCase) {
		this.isIgnoreCase = isIgnoreCase;
        InputStream is = StringReplacer.class.getResourceAsStream("/htmlfilter.map");

        if (is != null) {
            loadMappings( new LineInputStream(is), tag2xtag);
		}
		else {
			tag2xtag.put("<embed", "<x-embed");
			tag2xtag.put("<iframe", "<x-iframe");
			tag2xtag.put("<script", "<x-script");
			tag2xtag.put("<object", "<x-object");
			tag2xtag.put("onclick", "x-onclick");
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
            StringTokenizer tk = new StringTokenizer(currLine, " \t=");
            try {
                String key = tk.nextToken();
                String value = tk.nextToken();

                table.put(key.toLowerCase(), value);
            }
            catch (NoSuchElementException nex) {nex.printStackTrace(); }
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

    public static String replace(String oldtext, 
		boolean isHiddenImg,
		boolean isHiddenTag) {
		
		String newtext = replaceHTMLBodyTag(oldtext);

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

		ht.put("</body>", "</div>");
		ht.put("<a ", "<a target='_blank' ");
		ht.put("<area ", "<area target='_blank' ");
		ht.put("onclick", "x-onclick");
		ht.put("<script", "<x-script><!--");
		ht.put("</script>", "--></x-script>");
		ht.put("<iframe", "<x-iframe><!-- ");
		ht.put("</iframe>", "--></x-iframe>");
		

		return replace(newtext, ht, isIgnoreCase);
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

    public static String replace(String oldtext, Hashtable ht, boolean ignoreCase) {

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
				
				/* stackoveflow���� ����ó��
				 * modified by kang
				 * 2010.04.22
				 */
				boolean isLink;
				
				try{
					isLink = newstr.matches("((http|ftp|https)://([a-zA-Z0-9]|-|[_,.@/\\~:?%%=&])+)");
				}catch(StackOverflowError soe){
					isLink = false;
					System.out.println(":::::::::::[StringReplacer-replaceLink]:::[Stackoverflow Error]");
					//soe.printStackTrace();
				}catch(Throwable t){
					isLink = false;
					System.out.println(":::::::::::[StringReplacer-replaceLink]:::[Throwable Error]");
					//t.printStackTrace();
				}

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

    public static String replaceHTMLBodyTag(String oldtext) {
        String worktext = oldtext.toLowerCase();
        StringBuffer newtext = new StringBuffer();
        int start = 0;
        int end = 0;

        while ((end = worktext.indexOf("<body", start)) != -1) {
            newtext.append(oldtext.substring(start, end));
            newtext.append("<div style='width=100%'");
            start = end + 5;

            end = worktext.indexOf(">", start);
            newtext.append(oldtext.substring(start, end));
            newtext.append(">");
            start = end + 1;
			isBody = true;
        }

        newtext.append(oldtext.substring(start));

        return newtext.toString();
    }

	public boolean isInImage(String text) {
        String worktext = text.toLowerCase();

		if (worktext.indexOf("<img") >= 0
			|| worktext.indexOf("background=") > 0
			|| worktext.indexOf("background-image") > 0) {
			return true;
		}
		return false;
	}

	public String replaceXXS(String str){
		String strTmp = str.trim();

		strTmp = strTmp.replaceAll("<", "&lt;");
		strTmp = strTmp.replaceAll(">", "&gt;");
		strTmp = strTmp.replaceAll("\"", "&quot;");
		
		return strTmp;
	}
	
	public String replaceSQLInjection(String str){
		String strTmp = str.trim();

		strTmp = strTmp.replaceAll(",", "");
		strTmp = strTmp.replaceAll("=", "");
		
		return strTmp;
	}
}
