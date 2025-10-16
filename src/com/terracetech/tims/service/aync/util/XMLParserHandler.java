package com.terracetech.tims.service.aync.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class XMLParserHandler extends DefaultHandler
{
	protected String currentElement;
    protected List<Object> modelList;
    protected Stack<AbstractState> stateStack;
    
    public XMLParserHandler()
    {
        currentElement = null;
        modelList = new ArrayList<Object>();
        stateStack = new Stack<AbstractState>();
    }

    public void startDocument()
        throws SAXException
    {
        super.startDocument();
        if(!stateStack.isEmpty())
        {
            throw new AssertionError();
        } else
        {
            pushState(createStartState());
            return;
        }
    }

    protected void pushState(AbstractState state)
    {
        if(state == null)
        {
            throw new AssertionError();
        } else
        {
            stateStack.push(state);
            return;
        }
    }

    public void endDocument()
        throws SAXException
    {
        super.endDocument();
        if(stateStack.size() != 1)
        {
            throw new AssertionError();
        } else
        {
            topState().end();
            popState();
            return;
        }
    }

    private AbstractState popState()
        throws SAXException
    {
        if(stateStack.isEmpty())
            throw new AssertionError();
        AbstractState state = (AbstractState)stateStack.pop();
        if(stateStack.size() > 0)
            topState().endElement(state);
        return state;
    }

    protected AbstractState topState()
    {
        if(stateStack.isEmpty())
            throw new AssertionError();
        else
            return (AbstractState)stateStack.lastElement();
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
        throws SAXException
    {
        currentElement = qName;
        AbstractState newState = topState().startElement(qName);
        pushState(newState);
        newState.parseAttrs(atts);
    }

    public void endElement(String namespaceURI, String localName, String qName)
        throws SAXException
    {
        AbstractState state = topState();
        state.end();
        popState();
        if(!stateStack.isEmpty())
            topState().endElement(state);
    }

    public List<Object> getModels()
    {
        return modelList;
    }

    public void setModel(Object model)
    {
        modelList.add(model);
    }

    public void characters(char ch[], int start, int length)
        throws SAXException
    {
        if(!stateStack.isEmpty())
            topState().getText().append(ch, start, length);
    }

    public abstract AbstractState createStartState();


    

   
}
