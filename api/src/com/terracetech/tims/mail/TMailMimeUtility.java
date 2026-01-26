package com.terracetech.tims.mail;

import java.io.*;
import java.util.*;
import org.eclipse.angus.mail.util.*;

public class TMailMimeUtility {

    private static Hashtable mime2something;

    static {
	    // Use this class's classloader to load the mapping file
	    InputStream is = 
		    com.terracetech.tims.mail.TMailMimeUtility.class.getResourceAsStream(
		    "/charset.map");

	    if (is != null) {
	        is = new LineInputStream(is);

	        // Load the MIME-to-SOMETHING charset mapping table
	        mime2something = new Hashtable();
	        loadMappings((LineInputStream) is, mime2something);
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

	        if (currLine == null) { // end of file, stop
		        break;
            }

	        // ignore empty lines and comments
	        if (currLine.trim().length() == 0 || currLine.startsWith("#")) {
		        continue;
            }
	    
	        // A valid entry is of the form <key><separator><value>
	        // where, <separator> := SPACE | HT. Parse this
	        StringTokenizer tk = new StringTokenizer(currLine, " \t");

	        try {
		        String key = tk.nextToken();
		        String value = tk.nextToken();
		        table.put(key.toLowerCase(), value);
	        }
            catch (NoSuchElementException nex) { }
	    }
    }

    public static String mimeCharset(String charset) {
	    if (mime2something == null || charset == null) {
	        // no mapping table or charset param is null
	        return charset;
        }

	    String alias = (String) mime2something.get(charset.toLowerCase());

	    return (alias == null)? charset : alias;
    }

}
