package com.terracetech.tims.service.aync.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.io.StringWriter;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.xml.sax.SAXException;

import com.terracetech.tims.service.aync.Wbxml;

public abstract class BinarySerializer
{
	private Logger log = Logger.getLogger(this.getClass());

    public static final boolean isValidCodepage(int codepage)
    {
        return codepage >= 0 && codepage < Wbxml.NAMESPACES.length;
    }

    public static final String codepageToNamespace(int codepage)
    {
        return Wbxml.NAMESPACES[codepage];
    }

    public static final int namespaceToCodepage(String namespace)
        throws WbxmlException
    {
        for(int i = 0; i < Wbxml.NAMESPACES.length; i++)
            if(Wbxml.NAMESPACES[i] == namespace)
                return i;

        throw new WbxmlException("Unknown namespace");
    }

    public static final String codeToTagName(int codepage, int code)
    {
        try
        {
            return Wbxml.TAGTABLES[codepage][code - 5];
        }
        catch(Exception t)
        {
            t.printStackTrace();
        }
        return "UNKNOWN";
    }

    public static final int tagNameToCode(int codepage, String tagName)
    {
        return ((Integer)TAGTABLE_MAPS[codepage].get(tagName)).intValue();
    }

    protected BinarySerializer()
    {
        this(false);
    }

    public BinarySerializer(boolean isDebugTraceOn)
    {
        byteCount = 0;
        eventType = 0;
        codepage = 0;
        rootCodepage = 0;
        depth = 0;
        elementStack = new int[16];
        degenerated = false;
        wbxml = new byte[4096];
        this.isDebugTraceOn = isDebugTraceOn;
        if(isDebugTraceOn)
        {
            xmlWriter = new StringWriter();
            xmlOut = new XMLSerializer(xmlWriter, new OutputFormat("xml", "utf-8", true));
        }
    }

    public String getText()
    {
        return text;
    }

    public int getEventType()
    {
        return eventType;
    }

    public int getDepth()
    {
        return depth;
    }

    protected int getCode()
    {
        if(eventType == 3)
            return elementStack[depth] & 0xff;
        else
            return elementStack[depth - 1] & 0xff;
    }

    public String getName()
    {
        return codeToTagName(codepage, getCode());
    }

    public String getNamespace()
    {
        return codepageToNamespace(codepage);
    }

    public void logCodecError(InputStream remainder, int contentLength, Exception x)
        throws WbxmlException, IOException
    {
        InputStream is = new SequenceInputStream(new ByteArrayInputStream(wbxml, 0, byteCount), remainder);
        if(x.getMessage() != null)
            log.warn((new StringBuilder()).append("wbxml error: ").append(x.getMessage()).toString());
        log.warn(formatBytes(is, contentLength, contentLength <= 4096 ? contentLength : 4096, byteCount - 1));
    }

    protected void startDocument()
        throws IOException, WbxmlException
    {
        if(isDebugTraceOn)
        {
            xmlWriter.getBuffer().append("\n");
            try
            {
                xmlOut.startDocument();
            }
            catch(SAXException x)
            {
                throw new WbxmlException(x);
            }
        }
    }

    protected void endDocument()
        throws IOException, WbxmlException
    {
        if(isDebugTraceOn)
            try
            {
                xmlOut.endDocument();
                xmlWriter.flush();
            }
            catch(SAXException x)
            {
                throw new WbxmlException(x);
            }
    }

    private static String escapeCharacters(String text)
    {
        StringBuffer buf = new StringBuffer(text.length());
        for(int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);
            if(c != '\n' && c != '\r' && c != '\t' && Character.isISOControl(c))
                buf.append("##x").append(Integer.toHexString(c)).append(';');
            else
                buf.append(c);
        }

        return buf.toString();
    }

    public void text(String text)
        throws IOException, WbxmlException
    {
        eventType = 4;
        this.text = text;
        if(isDebugTraceOn)
            try
            {
                String escaped = escapeCharacters(text);
                if(escaped.length() > 4093)
                {
                    xmlOut.characters(escaped.toCharArray(), 0, 4093);
                    xmlOut.characters("...".toCharArray(), 0, 3);
                } else
                {
                    xmlOut.characters(escaped.toCharArray(), 0, escaped.length());
                }
            }
            catch(Exception x)
            {
                log.warn(x);
            }
    }

    protected void selectPage(int codepage)
        throws IOException, WbxmlException
    {
        if(!isValidCodepage(codepage))
            throw new WbxmlException("Invalid codepage");
        this.codepage = codepage;
        if(depth == 0)
            try
            {
                rootCodepage = codepage;
                if(isDebugTraceOn)
                    xmlOut.startPrefixMapping("", getNamespace());
            }
            catch(SAXException x)
            {
                throw new WbxmlException(x);
            }
    }

    protected void pushElementStack(int code)
        throws WbxmlException
    {
        eventType = 2;
        elementStack = ensureCapacity(elementStack, depth + 1);
        elementStack[depth++] = codepage << 8 | code;
        if(isDebugTraceOn)
            try
            {
                if(codepage == rootCodepage)
                    xmlOut.startElement(getNamespace(), getName(), null, null);
                else
                    xmlOut.startElement(null, null, (new StringBuilder()).append(getNamespace()).append(":").append(getName()).toString(), null);
            }
            catch(SAXException x)
            {
                throw new WbxmlException(x);
            }
    }

    protected void popElementStack()
        throws IOException, WbxmlException
    {
        eventType = 3;
        selectPage(elementStack[--depth] >> 8);
        if(isDebugTraceOn)
            try
            {
                if(codepage == rootCodepage)
                    xmlOut.endElement(getNamespace(), getName(), null);
                else
                    xmlOut.endElement(null, null, (new StringBuilder()).append(getNamespace()).append(":").append(getName()).toString());
            }
            catch(SAXException x)
            {
                throw new WbxmlException("Debug logging exception");
            }
        if(depth == 0)
            endDocument();
    }

    private final int[] ensureCapacity(int array[], int required)
    {
        if(array.length >= required)
        {
            return array;
        } else
        {
            int bigger[] = new int[(required / 16) * 16 + 16];
            System.arraycopy(array, 0, bigger, 0, array.length);
            return bigger;
        }
    }

    public int getByteCount()
    {
        return byteCount;
    }

    private static String formatBytes(InputStream in, int total, int limit, int errPos)
        throws IOException
    {
        StringBuilder hexBuf = new StringBuilder();
        for(int i = 0; i < (limit <= 0 || total <= limit ? total : limit); i++)
        {
            if(i % 32 == 0)
            {
                hexBuf.append("\n");
                String countStr = Integer.toHexString(i);
                switch(countStr.length())
                {
                case 1: // '\001'
                    hexBuf.append("00000");
                    break;

                case 2: // '\002'
                    hexBuf.append("0000");
                    break;

                case 3: // '\003'
                    hexBuf.append("000");
                    break;

                case 4: // '\004'
                    hexBuf.append("00");
                    break;

                case 5: // '\005'
                    hexBuf.append("0");
                    break;

                default:
                    throw new RuntimeException();

                case 6: // '\006'
                    break;
                }
                hexBuf.append(countStr).append(":  ");
            } else
            if(i % 16 == 0)
                hexBuf.append(" ");
            String hex = Integer.toHexString(in.read()).toUpperCase();
            if(hex.length() == 1)
                hexBuf.append('0');
            hexBuf.append(hex);
            if(i == errPos)
                hexBuf.append('*');
            else
                hexBuf.append(' ');
        }

        if(limit > 0 && total > limit)
            hexBuf.append((new StringBuilder()).append("\n         (").append(total - limit).append(" remaining bytes skipped)").toString());
        hexBuf.append("\n");
        return hexBuf.toString();
    }

    private static String formatBytes(InputStream in, int total, int limit)
        throws IOException
    {
        return formatBytes(in, total, limit, -1);
    }
    
    public void closeStream(){
    	try {
    		xmlWriter.close();
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(), e);
		}
    }
    

    public StringWriter getXmlWriter() {
		return xmlWriter;
	}

	static final int TEXT_LOGGING_LIMIT = 4096;
    static final int WBXML_LOGGING_LIMIT = 4096;
    public static final HashMap TAGTABLE_MAPS[];
    public static final int START_DOCUMENT = 0;
    public static final int END_DOCUMENT = 1;
    public static final int START_TAG = 2;
    public static final int END_TAG = 3;
    public static final int TEXT = 4;
    protected static final String TYPES[] = {
        "START_DOCUMENT", "END_DOCUMENT", "START_TAG", "END_TAG", "TEXT"
    };
    protected int byteCount;
    protected int eventType;
    protected int codepage;
    protected int rootCodepage;
    protected int depth;
    private int elementStack[];
    protected boolean degenerated;
    protected String text;
    byte wbxml[];
    private boolean isDebugTraceOn;
    private StringWriter xmlWriter;
    private XMLSerializer xmlOut;

    static 
    {
        TAGTABLE_MAPS = (new HashMap[] {
            new HashMap(Wbxml.TAGTABLES[0].length), new HashMap(Wbxml.TAGTABLES[1].length), new HashMap(Wbxml.TAGTABLES[2].length), new HashMap(Wbxml.TAGTABLES[3].length), new HashMap(Wbxml.TAGTABLES[4].length), new HashMap(Wbxml.TAGTABLES[5].length), new HashMap(Wbxml.TAGTABLES[6].length), new HashMap(Wbxml.TAGTABLES[7].length), new HashMap(Wbxml.TAGTABLES[8].length), new HashMap(Wbxml.TAGTABLES[9].length), 
            new HashMap(Wbxml.TAGTABLES[10].length), new HashMap(Wbxml.TAGTABLES[11].length), new HashMap(Wbxml.TAGTABLES[12].length), new HashMap(Wbxml.TAGTABLES[13].length), new HashMap(Wbxml.TAGTABLES[14].length), new HashMap(Wbxml.TAGTABLES[15].length), new HashMap(Wbxml.TAGTABLES[16].length), new HashMap(Wbxml.TAGTABLES[17].length)
        });
        for(int i = 0; i < Wbxml.TAGTABLES.length; i++)
        {
            if(Wbxml.TAGTABLES[i] == null)
                continue;
            for(int j = 0; j < Wbxml.TAGTABLES[i].length; j++)
            	TAGTABLE_MAPS[i].put(Wbxml.TAGTABLES[i][j], Integer.valueOf(j + 5));
        }

    }
}