package com.terracetech.tims.service.aync.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terracetech.tims.webmail.util.ByteUtil;
import com.terracetech.tims.webmail.util.StringUtils;


public class WbxmlSerializer extends BinarySerializer
{

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
    public WbxmlSerializer(OutputStream out)
        throws IOException, WbxmlException
    {
        super(true);
        pageSwitchPending = false;
        this.out = out;
        startDocument();
    }

    protected void startDocument()
        throws IOException, WbxmlException
    {
        writeByte(3);
        writeByte(1);
        writeByte(106);
        writeByte(0);
        super.startDocument();
    }

    public void openTag(String namespace, String name)
        throws IOException, WbxmlException
    {
        if(pageSwitchPending)
        {
            pageSwitchPending = false;
            writeByte(0);
            writeByte(this.codepage);
        }
        if(eventType == 2)
            writeByte(getCode() | 0x40);
        int codepage = namespaceToCodepage(namespace);
        eventType = 2;
        selectPage(codepage);
        pushElementStack(tagNameToCode(codepage, name));
    }

    public void closeTag()
        throws IOException, WbxmlException
    {
        switch(eventType)
        {
        case 2: // '\002'
            writeByte(getCode());
            break;

        case 3: // '\003'
        case 4: // '\004'
            writeByte(1);
            break;

        default:
            throw new WbxmlException("Invalid context");
        }
        popElementStack();
        if(depth == 0)
            flush();
    }
    
    public void text(String text)
        throws IOException, WbxmlException
    {
        if(eventType != 2)
        {
            throw new WbxmlException("Invalid context");
        } else
        {
            writeByte(getCode() | 0x40);
            writeStrI(text);
            super.text(text);
            return;
        }
    }

    public void text(InputStream in, int limit)
        throws IOException, WbxmlException
    {
        if(eventType != 2)
        {
            throw new WbxmlException("Invalid context");
        } else
        {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ByteUtil.copy(in, false, bao, false, limit >= 4096 ? 4096L : limit);
            byte firstBytes[] = bao.toByteArray();
            writeByte(getCode() | 0x40);
            writeStrI(new SequenceInputStream(new ByteArrayInputStream(firstBytes), in), limit);
            super.text(new String(firstBytes));
            return;
        }
    }

    public void integerContent(int number)
        throws IOException, WbxmlException
    {
        text(Integer.toString(number));
    }

    public void textElement(String namespace, String name, String text)
        throws IOException, WbxmlException
    {
    	if(StringUtils.isEmpty(text))
    		return;
    	
        openTag(namespace, name);
        text(text);
        closeTag();
    }

    public void textElement(String namespace, String name, InputStream in, int limit)
        throws IOException, WbxmlException
    {
        openTag(namespace, name);
        text(in, limit);
        closeTag();
    }

    public void integerElement(String namespace, String name, int number)
        throws IOException, WbxmlException
    {
        openTag(namespace, name);
        integerContent(number);
        closeTag();
    }

    public void emptyElement(String namespace, String name)
        throws IOException, WbxmlException
    {
        openTag(namespace, name);
        closeTag();
    }

    public void flush()
        throws IOException
    {
        out.flush();
    }

    private void writeByte(int b)
        throws IOException
    {
        out.write(b);
        byteCount++;
        if(byteCount <= wbxml.length)
            wbxml[byteCount - 1] = (byte)b;
    }

    private void writeStrI(String text)
        throws IOException
    {
        writeByte(3);
        byte bytes[] = null;
        try
        {
            bytes = text.getBytes("UTF-8");
        }
        catch(UnsupportedEncodingException x)
        {
        	log.warn(x);
            bytes = text.getBytes();
        }
        byte arr$[] = bytes;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            byte b = arr$[i$];
            writeByte(b);
        }

        writeByte(0);
    }

    private void writeStrI(InputStream in, int limit)
        throws IOException
    {
        writeByte(3);
        int b = 0;
        for(int count = 0; (limit == -1 || count++ < limit) && (b = in.read()) != -1;)
            writeByte(b);

        writeByte(0);
    }

    protected void selectPage(int codepage)
        throws IOException, WbxmlException
    {
        if(this.codepage == codepage)
            return;
        if(eventType == 3)
        {
            pageSwitchPending = true;
        } else
        {
            writeByte(0);
            writeByte(codepage);
        }
        super.selectPage(codepage);
    }
    
    public void closeStream(){
    	try {
    		out.close();
    		super.closeStream();
		} catch (Exception e) {
		}
    }

    OutputStream out;
    boolean pageSwitchPending;
}