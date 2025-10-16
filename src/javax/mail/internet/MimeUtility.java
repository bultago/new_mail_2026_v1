/*
 * @(#)MimeUtility.java	1.32 00/10/17
 *
 * Copyright 1997-2000 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */

package javax.mail.internet;

import javax.mail.MessagingException;
import javax.activation.*;
import java.util.*;
import java.io.*;
import com.sun.mail.util.*;

/**
 * This is a utility class that provides various MIME related
 * functionality. <p>
 *
 * There are a set of methods to encode and decode MIME headers as 
 * per RFC 2047. A brief description on handling such headers is
 * given below: <p>
 *
 * RFC 822 mail headers <strong>must</strong> contain only US-ASCII
 * characters. Headers that contain non US-ASCII characters must be
 * encoded so that they contain only US-ASCII characters. Basically,
 * this process involves using either BASE64 or QP to encode certain
 * characters. RFC 2047 describes this in detail. <p>
 *
 * In Java, Strings contain (16 bit) Unicode characters. ASCII is a
 * subset of Unicode (and occupies the range 0 - 127). A String
 * that contains only ASCII characters is already mail-safe. If the
 * String contains non US-ASCII characters, it must be encoded. An
 * additional complexity in this step is that since Unicode is not
 * yet a widely used charset, one might want to first charset-encode
 * the String into another charset and then do the transfer-encoding.
 * <p>
 * Note that to get the actual bytes of a mail-safe String (say,
 * for sending over SMTP), one must do 
 * <p><blockquote><pre>
 *
 *	byte[] bytes = string.getBytes("iso-8859-1");	
 *
 * </pre></blockquote><p>
 * 
 * The <code>setHeader()</code> and <code>addHeader()</code> methods
 * on MimeMessage and MimeBodyPart assume that the given header values
 * are Unicode strings that contain only US-ASCII characters. Hence
 * the callers of those methods must insure that the values they pass
 * do not contain non US-ASCII characters. The methods in this class 
 * help do this. <p>
 *
 * The <code>getHeader()</code> family of methods on MimeMessage and
 * MimeBodyPart return the raw header value. These might be encoded
 * as per RFC 2047, and if so, must be decoded into Unicode Strings.
 * The methods in this class help to do this.
 *
 * @version 1.32, 00/10/17
 * @author  John Mani
 */

public class MimeUtility {

    // This class cannot be instantiated
    private MimeUtility() { }

    public static final int ALL = -1;

    /**
     * Get the content-transfer-encoding that should be applied
     * to the input stream of this datasource, to make it mailsafe. <p>
     *
     * The algorithm used here is: <br>
     * <ul>
     * <li>
     * If the primary type of this datasource is "text" and if all
     * the bytes in its input stream are US-ASCII, then the encoding
     * is "7bit". If more than half of the bytes are non-US-ASCII, then
     * the encoding is "base64". If less than half of the bytes are
     * non-US-ASCII, then the encoding is "quoted-printable".
     * <li>
     * If the primary type of this datasource is not "text", then if
     * all the bytes of its input stream are US-ASCII, the encoding
     * is "7bit". If there is even one non-US-ASCII character, the
     * encoding is "base64".
     * </ul>
     *
     * @param	ds	DataSource
     * @return		the encoding. This is either "7bit",
     *			"quoted-printable" or "base64"
     */ 
    public static String getEncoding(DataSource ds) {
	ContentType cType = null;
	InputStream is = null;
	String encoding = null;

	try {
	    cType = new ContentType(ds.getContentType());
	    is = ds.getInputStream();
	} catch (Exception ex) {
	    return "base64"; // what else ?!
	}

	if (cType.match("text/*")) {
	    // Check all of the available bytes
	    int i = checkAscii(is, ALL, false);
	    switch (i) {
		case ALL_ASCII:
		    encoding = "7bit"; // all ascii
		    break;
		case MOSTLY_ASCII:
		    encoding = "quoted-printable"; // mostly ascii
		    break;
		default:
	   	    encoding = "base64"; // mostly binary
		    break;
	    }
	} else { // not "text"
	    // Check all of available bytes, break out if we find
	    // at least one non-US-ASCII character
	    if (checkAscii(is, ALL, true) == ALL_ASCII) // all ascii
		encoding = "7bit";
	    else // found atleast one non-ascii character, use b64 
		encoding = "base64";
	}
	
	// Close the input stream
	try {
	    is.close();
	} catch (IOException ioex) { }

	return encoding;
    }

    /**
     * Same as <code>getEncoding(DataSource)</code> except that instead
     * of reading the data from an <code>InputStream</code> it uses the
     * <code>writeTo</code> method to examine the data.  This is more
     * efficient in the common case of a <code>DataHandler</code>
     * created with an object and a MIME type (for example, a
     * "text/plain" String) because all the I/O is done in this
     * thread.  In the case requiring an <code>InputStream</code> the
     * <code>DataHandler</code> uses a thread, a pair of pipe streams,
     * and the <code>writeTo</code> method to produce the data. <p>
     *
     * @since	JavaMail 1.2
     */
    public static String getEncoding(DataHandler dh) {
	ContentType cType = null;
	String encoding = null;

	/*
	 * Try to pick the most efficient means of determining the
	 * encoding.  If this DataHandler was created using a DataSource,
	 * the getEncoding(DataSource) method is typically faster.  If
	 * the DataHandler was created with an object, this method is
	 * much faster.  To distinguish the two cases, we use a heuristic.
	 * A DataHandler created with an object will always have a null name.
	 * A DataHandler created with a DataSource will usually have a
	 * non-null name.
	 *
	 * XXX - This is actually quite a disgusting hack, but it makes
	 *	 a common case run over twice as fast.
	 */
	if (dh.getName() != null)
	    return getEncoding(dh.getDataSource());

	try {
	    cType = new ContentType(dh.getContentType());
	} catch (Exception ex) {
	    return "base64"; // what else ?!
	}

	if (cType.match("text/*")) {
	    // Check all of the available bytes
	    AsciiOutputStream aos = new AsciiOutputStream(false);
	    try {
		dh.writeTo(aos);
	    } catch (IOException ex) { }	// ignore it
	    switch (aos.getAscii()) {
	    case ALL_ASCII:
		encoding = "7bit"; // all ascii
		break;
	    case MOSTLY_ASCII:
		encoding = "quoted-printable"; // mostly ascii
		break;
	    default:
		encoding = "base64"; // mostly binary
		break;
	    }
	} else { // not "text"
	    // Check all of available bytes, break out if we find
	    // at least one non-US-ASCII character
	    AsciiOutputStream aos = new AsciiOutputStream(true);
	    try {
		dh.writeTo(aos);
	    } catch (IOException ex) { }	// ignore it
	    if (aos.getAscii() == ALL_ASCII) // all ascii
		encoding = "7bit";
	    else // found atleast one non-ascii character, use b64 
		encoding = "base64";
	}

	return encoding;
    }

    /**
     * Decode the given input stream. The Input stream returned is
     * the decoded input stream. All the encodings defined in RFC 2045
     * are supported here. They include "base64", "quoted-printable",
     * "7bit", "8bit", and "binary". In addition, "uuencode" is also
     * supported.
     *
     * @param	is		input stream
     * @param	encoding	the encoding of the stream.
     * @return			decoded input stream.
     */
    public static InputStream decode(InputStream is, String encoding)
		throws MessagingException {
	if (encoding.equalsIgnoreCase("base64"))
	    return new BASE64DecoderStream(is);
	else if (encoding.equalsIgnoreCase("quoted-printable"))
	    return new QPDecoderStream(is);
	else if (encoding.equalsIgnoreCase("uuencode") ||
		 encoding.equalsIgnoreCase("x-uuencode"))
	    return new UUDecoderStream(is);
	else if (encoding.equalsIgnoreCase("binary") ||
		 encoding.equalsIgnoreCase("7bit") ||
		 encoding.equalsIgnoreCase("8bit"))
	    return is;
	else
        return is;

        /* original part by kennis 2004.01.15
	    throw new MessagingException("Unknown encoding: " + encoding);
        */
    }

    /**
     * Wrap an encoder around the given output stream. 
     * All the encodings defined in RFC 2045 are supported here. 
     * They include "base64", "quoted-printable", "7bit", "8bit" and
     * "binary". In addition, "uuencode" is also supported.
     *
     * @param	os		output stream
     * @param	encoding	the encoding of the stream. 
     * @return			output stream that applies the
     *				specified encoding.
     */
    public static OutputStream encode(OutputStream os, String encoding)
		throws MessagingException {
        if (encoding == null)
	    return os;
	else if (encoding.equalsIgnoreCase("base64"))
	    return new BASE64EncoderStream(os);
	else if (encoding.equalsIgnoreCase("quoted-printable"))
	    return new QPEncoderStream(os);
	else if (encoding.equalsIgnoreCase("uuencode") ||
		 encoding.equalsIgnoreCase("x-uuencode"))
	    return new UUEncoderStream(os);
	else if (encoding.equalsIgnoreCase("binary") ||
		 encoding.equalsIgnoreCase("7bit") ||
		 /* add by doyoung 2004.06.11 */
		 encoding.equalsIgnoreCase("plain") ||
		 /* add end */
		 encoding.equalsIgnoreCase("8bit"))
	    return os;
	else
	    throw new MessagingException("Unknown encoding: " +encoding);
    }

    /**
     * Wrap an encoder around the given output stream.
     * All the encodings defined in RFC 2045 are supported here.
     * They include "base64", "quoted-printable", "7bit", "8bit" and
     * "binary". In addition, "uuencode" is also supported.
     * The <code>filename</code> parameter is used with the "uuencode"
     * encoding and is included in the encoded output.
     *
     * @param   os              output stream
     * @param   encoding        the encoding of the stream.
     * @param   filename        name for the file being encoded (only used
     *                          with uuencode)
     * @return                  output stream that applies the
     *                          specified encoding.
     * @since                   JavaMail 1.2
     */
    public static OutputStream encode(OutputStream os, String encoding,
                                      String filename)
                throws MessagingException {
        if (encoding == null)
            return os;
        else if (encoding.equalsIgnoreCase("base64"))
            return new BASE64EncoderStream(os);
        else if (encoding.equalsIgnoreCase("quoted-printable"))
            return new QPEncoderStream(os);
        else if (encoding.equalsIgnoreCase("uuencode") ||
                 encoding.equalsIgnoreCase("x-uuencode"))
            return new UUEncoderStream(os, filename);
        else if (encoding.equalsIgnoreCase("binary") ||
                 encoding.equalsIgnoreCase("7bit") ||
                 encoding.equalsIgnoreCase("8bit"))
            return os;
        else
            throw new MessagingException("Unknown encoding: " +encoding);
    }

    /**
     * Encode a RFC 822 "text" token into mail-safe form as per
     * RFC 2047. <p>
     *
     * The given Unicode string is examined for non US-ASCII
     * characters. If the string contains only US-ASCII characters,
     * it is returned as-is.  If the string contains non US-ASCII
     * characters, it is first character-encoded using the platform's
     * default charset, then transfer-encoded using either the B or 
     * Q encoding. The resulting bytes are then returned as a Unicode 
     * string containing only ASCII  characters. <p>
     *
     * Note that this method should be used to encode only 
     * "unstructured" RFC 822 headers. <p>
     *
     * Example of usage:
     * <p><blockquote><pre>
     *
     *  MimePart part = ...
     *  String rawvalue = "FooBar Mailer, Japanese version 1.1"
     *  try {
     *    // If we know for sure that rawvalue contains only US-ASCII 
     *    // characters, we can skip the encoding part
     *    part.setHeader("X-mailer", MimeUtility.encodeText(rawvalue));
     *  } catch (UnsupportedEncodingException e) {
     *    // encoding failure
     *  } catch (MessagingException me) {
     *   // setHeader() failure
     *  }
     *
     * </pre></blockquote><p>
     * 
     * @param	text	unicode string
     * @return	Unicode string containing only US-ASCII characters
     * @exception UnsupportedEncodingException if the encoding fails
     */
    public static String encodeText(String text)
			throws UnsupportedEncodingException {
	return encodeText(text, null, null);
    }

    /**
     * Encode a RFC 822 "text" token into mail-safe form as per
     * RFC 2047. <p>
     *
     * The given Unicode string is examined for non US-ASCII
     * characters. If the string contains only US-ASCII characters,
     * it is returned as-is.  If the string contains non US-ASCII
     * characters, it is first character-encoded using the specified
     * charset, then transfer-encoded using either the B or Q encoding.
     * The resulting bytes are then returned as a Unicode string 
     * containing only ASCII characters. <p>
     *
     * Note that this method should be used to encode only 
     * "unstructured" RFC 822 headers. 
     * 
     * @param	text	the header value
     * @param	charset	the charset. If this parameter is null, the
     *		platform's default chatset is used.
     * @param	encoding the encoding to be used. Currently supported
     *		values are "B" and "Q". If this parameter is null, then
     *		the "Q" encoding is used if most of characters to be
     *		encoded are in the ASCII charset, otherwise "B" encoding
     *		is used.
     * @return	Unicode string containing only US-ASCII characters
     */
    public static String encodeText(String text, String charset,
				    String encoding)
			throws UnsupportedEncodingException {
	return encodeWord(text, charset, encoding, false);
    }

    /**
     * Decode "unstructured" headers, that is, headers that are defined
     * as '*text' as per RFC 822. <p>
     *
     * The string is decoded using the algorithm specified in
     * RFC 2047, Section 6.1.1. If the charset-conversion fails
     * for any sequence, an UnsupportedEncodingException is thrown.
     * If the String is not an RFC 2047 style encoded header, it is
     * returned as-is <p>
     *
     * Example of usage:
     * <p><blockquote><pre>
     *
     *  MimePart part = ...
     *  String rawvalue = null;
     *  String  value = null;
     *  try {
     *    if ((rawvalue = part.getHeader("X-mailer")[0]) != null)
     *      value = MimeUtility.decodeText(rawvalue);
     *  } catch (UnsupportedEncodingException e) {
     *      // Don't care
     *      value = rawvalue;
     *  } catch (MessagingException me) { }
     *
     *  return value;
     *
     * </pre></blockquote><p>
     *
     * @param	etext	the possibly encoded value
     * @exception       UnsupportedEncodingException if the charset
     *			conversion failed.
     */
    public static String decodeText(String etext)
		throws UnsupportedEncodingException {
	/*
	 * We look for sequences separated by "linear-white-space".
	 * (as per RFC 2047, Section 6.1.1)
	 * RFC 822 defines "linear-white-space" as SPACE | HT | CR | NL.
	 */
	String lwsp = " \t\n\r";
	StringTokenizer st;

	/*
	 * First, lets do a quick run thru the string and check
	 * whether the sequence "=?"  exists at all. If none exists,
	 * we know there are no encoded-words in here and we can just
	 * return the string as-is, without suffering thru the later 
	 * decoding logic. 
	 * This handles the most common case of unencoded headers 
	 * efficiently.
	 */
	if (etext.indexOf("=?") == -1)
	    return etext;

	// Encoded words found. Start decoding ...

        /* added part 2004.08.05 */
        try {
            return decodeWord(etext);
        }
        catch (ParseException e) {
            return etext;
        }
        /* added part by kennis 2004.08.05 */

        /* by kennis 2004.08.05
	st = new StringTokenizer(etext, lwsp, true);
	StringBuffer sb = new StringBuffer();  // decode buffer
	StringBuffer wsb = new StringBuffer(); // white space buffer
	boolean prevWasEncoded = false;

	while (st.hasMoreTokens()) {
	    char c;
	    String s = st.nextToken();
	    // If whitespace, append it to the whitespace buffer
	    if (((c = s.charAt(0)) == ' ') || (c == '\t') ||
		(c == '\r') || (c == '\n'))
		wsb.append(c);
	    else {
                // added part by kennis 2004.07.19 //
                while (!s.endsWith("?=") && st.hasMoreTokens()) {
                    s += st.nextToken();
                }
                // added part by kennis 2004.07.19 //
		// Check if token is an 'encoded-word' ..
		String word;
		try {
		    word = decodeWord(s);
		    // Yes, this IS an 'encoded-word'.
		    if (!prevWasEncoded && wsb.length() > 0) {
			// if the previous word was also encoded, we
			// should ignore the collected whitespace. Else
			// we include the whitespace as well.
			sb.append(wsb);
		    }
		    prevWasEncoded = true;
		} catch (ParseException pex) {
		    // This is NOT an 'encoded-word'.
		    word = s;
		    // include colleced whitespace ..
		    if (wsb.length() > 0)
			sb.append(wsb);
		    prevWasEncoded = false;
		}
		sb.append(word); // append the actual word
		wsb.setLength(0); // reset wsb for reuse
	    }
	}
	return sb.toString();
        */
    }

    /**
     * Encode a RFC 822 "word" token into mail-safe form as per
     * RFC 2047. <p>
     *
     * The given Unicode string is examined for non US-ASCII
     * characters. If the string contains only US-ASCII characters,
     * it is returned as-is.  If the string contains non US-ASCII
     * characters, it is first character-encoded using the platform's
     * default charset, then transfer-encoded using either the B or 
     * Q encoding. The resulting bytes are then returned as a Unicode 
     * string containing only ASCII  characters. <p>
     * 
     * This method is meant to be used when creating RFC 822 "phrases".
     * The InternetAddress class, for example, uses this to encode
     * it's 'phrase' component.
     *
     * @param	text	unicode string
     * @return	Array of Unicode strings containing only US-ASCII 
     *		characters.
     * @exception UnsupportedEncodingException if the encoding fails
     */
    public static String encodeWord(String word) 
			throws UnsupportedEncodingException {
	return encodeWord(word, null, null);
    }

    /**
     * Encode a RFC 822 "word" token into mail-safe form as per
     * RFC 2047. <p>
     *
     * The given Unicode string is examined for non US-ASCII
     * characters. If the string contains only US-ASCII characters,
     * it is returned as-is.  If the string contains non US-ASCII
     * characters, it is first character-encoded using the specified
     * charset, then transfer-encoded using either the B or Q encoding.
     * The resulting bytes are then returned as a Unicode string 
     * containing only ASCII characters. <p>
     * 
     * @param	text	unicode string
     * @param	charset	the MIME charset
     * @param	encoding the encoding to be used. Currently supported
     *		values are "B" and "Q". If this parameter is null, then
     *		the "Q" encoding is used if most of characters to be
     *		encoded are in the ASCII charset, otherwise "B" encoding
     *		is used.
     * @return	Unicode string containing only US-ASCII characters
     * @exception UnsupportedEncodingException if the encoding fails
     */
    public static String encodeWord(String word, String charset, 
				    String encoding)
    			throws UnsupportedEncodingException {
	return encodeWord(word, charset, encoding, true);
    }


	/*
	 * check Japanese character set
	 * added by doyoung
	 * 2007.05.11
	 */
	private static boolean isJapaneseCharset(String s) {
		String s2 = s.toUpperCase();

		if ((s2.indexOf("JP") != -1) || (s2.indexOf("JAP") != -1) || (s2.indexOf("JIS") != -1) || (s2.indexOf("CP932") != -1)) {
			return true;
		}

		if (isDefaultJavaCharsetIsJapanese()) {
			if (s2.equals("EUC")) {
				return true;
			}
		}
		
		return false;
	}

    /*
     * Encode the given string. The parameter 'encodingWord' should
     * be true if a RFC 822 "word" token is being encoded and false if a
     * RFC 822 "text" token is being encoded. This is because the 
     * "Q" encoding defined in RFC 2047 has more restrictions when
     * encoding "word" tokens. (Sigh)
     */ 
    private static String encodeWord(String string, String charset,
				     String encoding, boolean encodingWord)
			throws UnsupportedEncodingException {

	// If 'string' contains only US-ASCII characters, just
	// return it.
	if (checkAscii(string) == ALL_ASCII)
	    return string;

	// Else, apply the specified charset conversion.
	String jcharset;
	if (charset == null) { // use default charset
	    jcharset = getDefaultJavaCharset(); // the java charset
	    charset = getDefaultMIMECharset(); // the MIME equivalent
	} 
	else { // MIME charset -> java charset
	/*
	* original part
	* modified by doyoung
	* 2007.05.17
	    jcharset = javaCharset(charset);
	*/
	    jcharset = encJavaCharset(charset);
	}

	// If no transfer-encoding is specified, figure one out.
	if (encoding == null) {
	    byte[] bytes = string.getBytes(jcharset);
	    if (checkAscii(bytes) != MOSTLY_NONASCII)
		encoding = "Q";
	    else
		encoding = "B";
	}

	boolean b64;
	if (encoding.equalsIgnoreCase("B")) 
	    b64 = true;
	else if (encoding.equalsIgnoreCase("Q"))
	    b64 = false;
	else
	    throw new UnsupportedEncodingException(
			"Unknown transfer encoding: " + encoding);

	StringBuffer outb = new StringBuffer(); // the output buffer
	doEncode(string, b64, jcharset, 
		 // As per RFC 2047, size of an encoded string should not
		 // exceed 75 bytes.
		 // 7 = size of "=?", '?', 'B'/'Q', '?', "?="
		 75 - 7 - charset.length(), // the available space
		 "=?" + charset + "?" + encoding + "?", // prefix
		 true, encodingWord, outb);

	return outb.toString();
    }

    private static void doEncode(String string, boolean b64, 
		String jcharset, int avail, String prefix, 
		boolean first, boolean encodingWord, StringBuffer buf) 
			throws UnsupportedEncodingException {

	// First find out what the length of the encoded version of
	// 'string' would be.
	byte[] bytes = string.getBytes(jcharset);
	int len;
	if (b64) // "B" encoding
	    len = BEncoderStream.encodedLength(bytes);
	else // "Q"
	    len = QEncoderStream.encodedLength(bytes, encodingWord);
	
	int size;
	if ((len > avail) && ((size = string.length()) > 1)) { 
	    // If the length is greater than 'avail', split 'string'
	    // into two and recurse.
	    doEncode(string.substring(0, size/2), b64, jcharset, 
		     avail, prefix, first, encodingWord, buf);
	    doEncode(string.substring(size/2, size), b64, jcharset,
		     avail, prefix, false, encodingWord, buf);
	} else {
	    // length <= than 'avail'. Encode the given string
	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    OutputStream eos; // the encoder
	    if (b64) // "B" encoding
		eos = new BEncoderStream(os);
	    else // "Q" encoding
		eos = new QEncoderStream(os, encodingWord);
	    
	    try { // do the encoding
		eos.write(bytes);
		eos.close();
	    } catch (IOException ioex) { }

	    byte[] encodedBytes = os.toByteArray(); // the encoded stuff
	    // Now write out the encoded (all ASCII) bytes into our
	    // StringBuffer
	    if (!first) // not the first line of this sequence
		buf.append("\r\n "); // start a continuation line

	    buf.append(prefix);
	    for (int i = 0; i < encodedBytes.length; i++)
		buf.append((char)encodedBytes[i]);
	    buf.append("?="); // terminate the current sequence
	}
    }

    /**
     * The string is parsed using the rules in RFC 2047 for parsing
     * an "encoded-word". If the parse fails, a ParseException is 
     * thrown. Otherwise, it is transfer-decoded, and then 
     * charset-converted into Unicode. If the charset-conversion
     * fails, an UnsupportedEncodingException is thrown.<p>
     *
     * @param	eword	the possibly encoded value
     * @exception       ParseException if the string is not an
     *			encoded-word as per RFC 2047.
     * @exception       UnsupportedEncodingException if the charset
     *			conversion failed.
     */
    public static String decodeWord(String eword)
		throws ParseException, UnsupportedEncodingException {

    /* by kennis 2004.02.09
	if (!eword.startsWith("=?")) // not an encoded word
	    throw new ParseException();
    */
	
    /* added part by kennis 2004.02.09 */
    StringBuffer sb = new StringBuffer();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    boolean prevWasEncoded = false;
    String lastcharset = null;
    String charset = null;
    int pos = 0;

  while (pos < eword.length()) {
    int start = eword.indexOf("=?", pos);

    try {
        if (start == -1) {
            baos.write(eword.substring(pos).getBytes());
            break;
        }
        else if (pos < start) {
            String s = eword.substring(pos, start);

            if (prevWasEncoded) {
                s = s.trim();
            }

            baos.write(s.getBytes());
            pos = start;
            prevWasEncoded = false;
            continue;
        }
        else {
            start = pos + 2;
            prevWasEncoded = true;
        }
    }
    catch (IOException e) {
        throw new ParseException();
    }
    /* added part by kennis 2004.02.09 */

	// get charset
    /* by kennis 2004.02.09
	int start = 2; int pos; 
    */
	if ((pos = eword.indexOf('?', start)) == -1) {
	    throw new ParseException();
	}

	charset = javaCharset(eword.substring(start, pos));

    /* added part by kennis 2004.02.25 */
    if (charset.equalsIgnoreCase("ISO-8859-1")) {
        charset = getDefaultJavaCharset();
    }
    /* added part by kennis 2004.02.25 */

	// get encoding
	start = pos+1;
	if ((pos = eword.indexOf('?', start)) == -1)
	    throw new ParseException();
	String encoding = eword.substring(start, pos);

	// get encoded-sequence
	start = pos+1;
	if ((pos = eword.indexOf("?=", start)) == -1)
	    throw new ParseException();
	String word = eword.substring(start, pos);

	try {
	    // Extract the bytes from word
	    ByteArrayInputStream bis = 
		new ByteArrayInputStream(ASCIIUtility.getBytes(word));

	    // Get the appropriate decoder
	    InputStream is;
	    if (encoding.equalsIgnoreCase("B")) 
		is = new BASE64DecoderStream(bis);
	    else if (encoding.equalsIgnoreCase("Q"))
		is = new QDecoderStream(bis);
	    else
		throw new UnsupportedEncodingException(
				"unknown encoding: " + encoding);

	    // For b64 & q, size of decoded word <= size of word. So
	    // the decoded bytes must fit into the 'bytes' array. This
	    // is certainly more efficient than writing bytes into a
	    // ByteArrayOutputStream and then pulling out the byte[]
	    // from it.
	    int count = bis.available();
	    byte[] bytes = new byte[count];
	    // count is set to the actual number of decoded bytes 
	    count = is.read(bytes, 0, count);

        if (lastcharset != null && !lastcharset.equals(charset)) {
            try {
                sb.append(baos.toString(lastcharset));
            }
            catch (UnsupportedEncodingException e) {
                if (lastcharset.equalsIgnoreCase("UTF-7") ||
                    lastcharset.equalsIgnoreCase("UTF7")) {

                    try {
                        ByteArrayInputStream bais =
                            new ByteArrayInputStream(baos.toByteArray());
                        sb.append(UTF7Decoder.decode(bais));
                        bais.close();
                    }
                    catch (IOException ioe) {
                        sb.append(baos.toString(getDefaultJavaCharset()));
                    }
                }
                else {
                    sb.append(baos.toString(getDefaultJavaCharset()));
                }
            }

            baos.reset();
        }

        baos.write(bytes, 0, count);

        /* original part by kennis 2003.11.27
	    // Finally, convert the decoded bytes into a String using
	    // the specified charset
	    return new String(bytes, 0, count, charset);
        */
	} catch (UnsupportedEncodingException uex) {
	    // explicitly catch and rethrow this exception, otherwise
	    // the below IOException catch will swallow this up!
	    throw uex;
	} catch (IOException ioex) {
	    // Shouldn't happen.
	    throw new ParseException();
	} catch (IllegalArgumentException iex) {
	    /* An unknown charset of the form ISO-XXX-XXX, will cause
	     * the JDK to throw an IllegalArgumentException ... Since the
	     * JDK will attempt to create a classname using this string,
	     * but valid classnames must not contain the character '-',
	     * and this results in an IllegalArgumentException, rather than
	     * the expected UnsupportedEncodingException. Yikes
	     */
	    throw new UnsupportedEncodingException();
	}

    /* added part by kennis 2004.02.09 */
    pos += 2;
    lastcharset = charset;
  }

    try {
        sb.append(baos.toString(charset));
    }
    catch (UnsupportedEncodingException e) {
        if (charset.equalsIgnoreCase("UTF-7") ||
            charset.equalsIgnoreCase("UTF7")) {

            try {
                ByteArrayInputStream bais =
                    new ByteArrayInputStream(baos.toByteArray());
                sb.append(UTF7Decoder.decode(bais));
                bais.close();
            }
            catch (IOException ioe) {
                sb.append(baos.toString(getDefaultJavaCharset()));
            }
        }
        else {
            sb.append(baos.toString(getDefaultJavaCharset()));
        }
    }

    return sb.toString();

    /* added part by kennis 2004.02.09 */

    }

    /**
     * A utility method to quote a word, if the word contains any
     * characters from the specified 'specials' list.<p>
     *
     * The <code>HeaderTokenizer</code> class defines two special
     * sets of delimiters - MIME and RFC 822. <p>
     *
     * This method is typically used during the generation of 
     * RFC 822 and MIME header fields.
     *
     * @param	word	word to be quoted
     * @param	specials the set of special characters
     * @return		the possibly quoted word
     * @see	javax.mail.internet.HeaderTokenizer#MIME
     * @see	javax.mail.internet.HeaderTokenizer#RFC822
     */
    public static String quote(String word, String specials) {
	int len = word.length();

	/*
	 * Look for any "bad" characters, Escape and
	 *  quote the entire string if necessary.
	 */
	boolean needQuoting = false;
	for (int i = 0; i < len; i++) {
	    char c = word.charAt(i);
	    if (c == '"' || c == '\\' 
		/* -------------------------------
		* modified by doyoung 2005.05.20
		* not escape '\r', '\n'
		|| c == '\r' || c == '\n' 
		------------------------------- */
		) {
		// need to escape them and then quote the whole string
		StringBuffer sb = new StringBuffer(len + 3);
		sb.append('"');
		for (int j = 0; j < len; j++) {
		    char cc = word.charAt(j);
		    if ((cc == '"') || (cc == '\\') 
			/* -------------------------------
			* modified by doyoung 2005.05.20
			* not escape '\r', '\n'
			|| (cc == '\r') || (cc == '\n') 
			------------------------------- */
			)
			// Escape the character
			sb.append('\\');
		    sb.append(cc);
		}
		sb.append('"');
		return sb.toString();
	    } else if (c < 040 || c >= 0177 || specials.indexOf(c) >= 0)
		// These characters cause the string to be quoted
		needQuoting = true;
	}

	if (needQuoting) {
	    StringBuffer sb = new StringBuffer(len + 2);
	    sb.append('"').append(word).append('"');
	    return sb.toString();
	} else 
	    return word;
    }

    /**
     * Convert a MIME charset name into a valid Java charset name. <p>
     *
     * @param charset	the MIME charset name
     * @return  the Java charset equivalent. If a suitable mapping is
     *		not available, the passed in charset is itself returned.
     */
    public static String javaCharset(String charset) {
		if (mime2java == null || charset == null)
		// no mapping table, or charset parameter is null
		return charset;

		/*
		* added by doyoung
		* 2007.05.16
		*/
		if (isJapaneseCharset(charset)) {
			return "JISAutoDetect";
		}

		String alias = (String)mime2java.get(charset.toLowerCase());
		return alias == null ? charset : alias;
	}

	/*
	* added by doyoung
	* 2007.05.17
	*/
    public static String encJavaCharset(String charset) {
		if (mime2java == null || charset == null)
		// no mapping table, or charset parameter is null
		return charset;

		String alias = (String)mime2java.get(charset.toLowerCase());
		return alias == null ? charset : alias;
	}

    /**
     * Convert a java charset into its MIME charset name. <p>
     *
     * Note that a future version of JDK (post 1.2) might provide
     * this functionality, in which case, we may deprecate this
     * method then.
     *
     * @param   charset    the JDK charset
     * @return      	the MIME/IANA equivalent. If a mapping
     *			is not possible, the passed in charset itself
     *			is returned.
     * @since		JavaMail 1.1
     */
    public static String mimeCharset(String charset) {
	if (java2mime == null || charset == null) 
	    // no mapping table or charset param is null
	    return charset;

	String alias = (String)java2mime.get(charset.toLowerCase());
	return alias == null ? charset : alias;
    }

	/*
	* added by doyoung
	* 2007.05.16
	*/
	private static boolean defaultJavaCharsetIsJapanese = false;
	public static boolean isDefaultJavaCharsetIsJapanese() {
		return defaultJavaCharsetIsJapanese;	
	}

    private static String defaultJavaCharset;
    private static String defaultMIMECharset;
    private static String everyTimeLoad = null;

    /**
     * Get the default charset corresponding to the system's current 
     * default locale. <p>
     * 
     * @return	the default charset of the system's default locale, 
     * 		as a Java charset. (NOT a MIME charset)
     * @since	JavaMail 1.1
     */
    public static String getDefaultJavaCharset() {
		boolean isLoaded = true;

		if (defaultJavaCharset == null) {
			isLoaded = false;
		}

		if (!isLoaded || everyTimeLoad != null && everyTimeLoad.equals("true")) {
			try {
				/*
				* added by doyoung
				* 2006.10.04
				* 2007.05.16
				* 2007.08.15
				*/
				defaultJavaCharset = System.getProperty("mail.mime.charset");
				everyTimeLoad = System.getProperty("mail.mime.charset.load");

				if (defaultJavaCharset == null) {
					defaultJavaCharset = System.getProperty("default.charset");
				}

				if (defaultJavaCharset == null) {
					defaultJavaCharset = System.getProperty("file.encoding", 
						"8859_1");
				}
			} catch (SecurityException sex) {

				class NullInputStream extends InputStream {
					public int read() {
						return 0;
					}
				}
				InputStreamReader reader = 
				new InputStreamReader(new NullInputStream());
				defaultJavaCharset = reader.getEncoding();
				if (defaultJavaCharset == null)
				defaultJavaCharset = "8859_1";
			}

			/*
			* added by doyoung
			* 2007.05.11
			*/
			if (isJapaneseCharset(defaultJavaCharset)) {
				defaultJavaCharsetIsJapanese = true;
				defaultJavaCharset = "JISAutoDetect";
			}
		
			if (!isLoaded) {
				System.out.println("== JAVAMAIL defaultJavaCharset : ["+defaultJavaCharset+"] ==");
				System.out.println("== JAVAMAIL everyTimeLoad : ["+everyTimeLoad+"] ==");
			}
		}

		return defaultJavaCharset;
	}

    /*
     * Get the default MIME charset for this locale.
     */
    static String getDefaultMIMECharset() {
	if (defaultMIMECharset == null)
	    defaultMIMECharset = System.getProperty("mail.mime.charset");
	if (defaultMIMECharset == null)
	    defaultMIMECharset = mimeCharset(getDefaultJavaCharset());
	return defaultMIMECharset;
    }

    private static Hashtable mime2java;
    private static Hashtable java2mime;

    static {

	// Use this class's classloader to load the mapping file
	InputStream is = 
		javax.mail.internet.MimeUtility.class.getResourceAsStream(
		"/META-INF/javamail.charset.map");

	if (is != null) {
	    is = new LineInputStream(is);

	    // Load the JDK-to-MIME charset mapping table
	    java2mime = new Hashtable(20);
	    loadMappings((LineInputStream)is, java2mime);

	    // Load the MIME-to-JDK charset mapping table
	    mime2java = new Hashtable(10);
	    loadMappings((LineInputStream)is, mime2java);
	}
    }

    private static void loadMappings(LineInputStream is, Hashtable table) {
	String currLine;

	while (true) {
	    try {
		currLine = is.readLine();
	    } catch (IOException ioex) {
		break; // error in reading, stop
	    }

	    if (currLine == null) // end of file, stop
		break;
	    if (currLine.startsWith("--") && currLine.endsWith("--"))
		// end of this table
		break;	

	    // ignore empty lines and comments
	    if (currLine.trim().length() == 0 || currLine.startsWith("#"))
		continue;
	    
	    // A valid entry is of the form <key><separator><value>
	    // where, <separator> := SPACE | HT. Parse this
	    StringTokenizer tk = new StringTokenizer(currLine, " \t");
	    try {
		String key = tk.nextToken();
		String value = tk.nextToken();
		table.put(key.toLowerCase(), value);
	    } catch (NoSuchElementException nex) { }
	}
    }

    static final int ALL_ASCII 		= 1;
    static final int MOSTLY_ASCII 	= 2;
    static final int MOSTLY_NONASCII 	= 3;

    /** 
     * Check if the given string contains non US-ASCII characters.
     * @param	s	string
     * @return		ALL_ASCII if all characters in the string 
     *			belong to the US-ASCII charset. MOSTLY_NONASCII
     *			if any one character is non-ascii.
     */
    static int checkAscii(String s) {
	int l = s.length();

	for (int i=0; i < l; i++) {
	    if (nonascii((int)s.charAt(i))) // non-ascii
		return MOSTLY_NONASCII;
	}
	
	return ALL_ASCII; // all ascii
    }

    /** 
     * Check if the given byte array contains non US-ASCII characters.
     * @param	b	byte array
     * @return		ALL_ASCII if all characters in the string 
     *			belong to the US-ASCII charset. MOSTLY_ASCII
     *			if more than half of the available characters
     *			are US-ASCII characters. Else MOSTLY_NONASCII.
     */
    static int checkAscii(byte[] b) {
	int ascii = 0, non_ascii = 0;

	for (int i=0; i < b.length; i++) {
	    // The '&' operator automatically causes b[i] to be promoted
	    // to an int, and we mask out the higher bytes in the int 
	    // so that the resulting value is not a negative integer.
	    if (nonascii(b[i] & 0xff)) // non-ascii
		non_ascii++;
	    else
		ascii++;
	}
	
	if (non_ascii == 0)
	    return ALL_ASCII;
	if (ascii > non_ascii)
	    return MOSTLY_ASCII;
	
	return MOSTLY_NONASCII;
    }

    /** 
     * Check if the given input stream contains non US-ASCII characters.
     * Upto <code>max</code> bytes are checked. If <code>max</code> is
     * set to <code>ALL</code>, then all the bytes available in this
     * input stream are checked. If <code>breakOnNonAscii</code> is true
     * the check terminates when the first non-US-ASCII character is
     * found and MOSTLY_NONASCII is returned. Else, the check continues
     * till <code>max</code> bytes or till the end of stream.
     *
     * @param	is	the input stream
     * @param	max	maximum bytes to check for. The special value
     *			ALL indicates that all the bytes in this input
     *			stream must be checked.
     * @param	breakOnNonAscii if <code>true</code>, then terminate the
     *			the check when the first non-US-ASCII character
     *			is found.
     * @return		ALL_ASCII if all characters in the string 
     *			belong to the US-ASCII charset. MOSTLY_ASCII
     *			if more than half of the available characters
     *			are US-ASCII characters. Else MOSTLY_NONASCII.
     */
    static int checkAscii(InputStream is, int max, boolean breakOnNonAscii) {
	int ascii = 0, non_ascii = 0;
	int len;
	int block = 4096;
	int linelen = 0;
	boolean longLine = false;
	byte buf[] = null;
	if (max != 0) {
	    block = (max == ALL) ? 4096 : Math.min(max, 4096);
	    buf = new byte[block]; 
	}
	while (max != 0) {
	    try {
		if ((len = is.read(buf, 0, block)) == -1)
		    break;
		for (int i = 0; i < len; i++) {
	    	    // The '&' operator automatically causes b[i] to 
		    // be promoted to an int, and we mask out the higher
		    // bytes in the int so that the resulting value is 
		    // not a negative integer.
		    int b = buf[i] & 0xff;
		    if (b == '\r' || b == '\n')
			linelen = 0;
		    else {
			linelen++;
			if (linelen > 998)	// 1000 - CRLF
			    longLine = true;
		    }
		    if (nonascii(b)) {	// non-ascii
		        if (breakOnNonAscii) // we are done
			    return MOSTLY_NONASCII;
		        else
			    non_ascii++;
		    } else
		        ascii++;
		}
	    } catch (IOException ioex) {
		break;
	    }
	    if (max != ALL)
		max -= len;
	}

	if (max == 0 && breakOnNonAscii)
	    // We have been told to break on the first non-ascii character.
	    // We haven't got any non-ascii character yet, but then we
	    // have not checked all of the available bytes either. So we
	    // cannot say for sure that this input stream is ALL_ASCII,
	    // and hence we must play safe and return MOSTLY_NONASCII

	    return MOSTLY_NONASCII;

	if (non_ascii == 0) { // no non-us-ascii characters so far
	    // if we've seen a long line, we degrade to mostly ascii
	    if (longLine)
		return MOSTLY_ASCII;
	    else
		return ALL_ASCII;
	}
	if (ascii > non_ascii) // mostly ascii
	    return MOSTLY_ASCII;
	return MOSTLY_NONASCII;
    }

    private static final boolean nonascii(int b) {
	return b >= 0177 || (b < 040 && b != '\r' && b != '\n' && b != '\t');
    }
}

/**
 * An OutputStream that determines whether the data written to
 * it is all ASCII, mostly ASCII, or mostly non-ASCII.
 */
class AsciiOutputStream extends OutputStream {
    private boolean breakOnNonAscii;
    private int ascii = 0, non_ascii = 0;
    private int linelen = 0;
    private boolean longLine = false;
    private int ret = 0;

    public AsciiOutputStream(boolean breakOnNonAscii) {
	this.breakOnNonAscii = breakOnNonAscii;
    }

    public void write(int b) throws IOException {
	check(b);
    }

    public void write(byte b[]) throws IOException {
	write(b, 0, b.length);
    }

    public void write(byte b[], int off, int len) throws IOException {
	len += off;
	for (int i = off; i < len ; i++)
	    check(b[i]);
    }

    private final void check(int b) throws IOException {
	b &= 0xff;
	if (b == '\r' || b == '\n')
	    linelen = 0;
	else {
	    linelen++;
	    if (linelen > 998)	// 1000 - CRLF
		longLine = true;
	}
	if (b > 0177) { // non-ascii
	    non_ascii++;
	    if (breakOnNonAscii) {	// we are done
		ret = MimeUtility.MOSTLY_NONASCII;
		throw new EOFException();
	    }
	} else
	    ascii++;
    }

    /**
     * Return ASCII-ness of data stream.
     */
    public int getAscii() {
	if (ret != 0)
	    return ret;
	if (non_ascii == 0) { // no non-us-ascii characters so far
	    // if we've seen a long line, we degrade to mostly ascii
	    if (longLine)
		return MimeUtility.MOSTLY_ASCII;
	    else
		return MimeUtility.ALL_ASCII;
	}
	if (ascii > non_ascii) // mostly ascii
	    return MimeUtility.MOSTLY_ASCII;
	return MimeUtility.MOSTLY_NONASCII;
    }
}

