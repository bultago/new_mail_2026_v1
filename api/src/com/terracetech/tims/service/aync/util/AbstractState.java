package com.terracetech.tims.service.aync.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public abstract class AbstractState {
    private XMLParserHandler handler;
    private StringBuffer text;

	public AbstractState(XMLParserHandler handler)
    {
        text = new StringBuffer();
        this.handler = handler;
    }

    public AbstractState startElement(String tagName)
        throws SAXException
    {
        throw new SAXException("Unknow Tag!" + tagName);
    }

    public void endElement(AbstractState abstractstate)
        throws SAXException
    {
    }

    public void parseAttrs(Attributes attributes)
        throws SAXException
    {
    }

    protected String getAttrib(Attributes attrs, String attrName)
    {
        return attrs.getValue(attrName);
    }

    public int getIntAttrib(Attributes attrs, String attrName)
    {
        return getIntAttrib(attrs, attrName, 0);
    }

    public int getIntAttrib(Attributes attrs, String attrName, int defaultValue)
    {
    	try {
    		String value;
            value = attrs.getValue(attrName);
            if(value == null)
                return defaultValue;
            Integer result = Integer.decode(value);
            return result.intValue();	
		} catch (NumberFormatException e) {
			return 0;
		}
    }

    public void end()
        throws SAXException
    {
    }

    public StringBuffer getText()
    {
        return text;
    }

    protected XMLParserHandler getHandler()
    {
        return handler;
    }

    protected void setHandler(XMLParserHandler handler)
    {
        this.handler = handler;
    }


}
