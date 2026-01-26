package com.terracetech.tims.service.aync.handler;

import org.xml.sax.SAXException;

import com.terracetech.tims.service.aync.data.PingData;
import com.terracetech.tims.service.aync.util.AbstractState;
import com.terracetech.tims.service.aync.util.XMLParserHandler;

public class PingHandler extends XMLParserHandler {
	
	private PingData model;
	
	public PingData getModel() {
		return model;
	}

	@Override
	public AbstractState createStartState() {
		model = new PingData();
		
		return new PingState(this);
	}

	class PingState extends AbstractState {
		public PingState(XMLParserHandler handler) {
			super(handler);
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			if (tagName.equalsIgnoreCase("Ping")){
				PingState a = new PingState(getHandler());
                return a;
			}
			if (tagName.equalsIgnoreCase("HeartbeatInterval")){
				HeartbeatIntervalState a = new HeartbeatIntervalState(getHandler());
                return a;
			}
			if (tagName.equalsIgnoreCase("Folders")){
				FoldersState a = new FoldersState(getHandler());
                return a;
			}
			
			return super.startElement(tagName);
		}
	}

	class ClassState extends AbstractState {
		public ClassState(XMLParserHandler handler) {
			super(handler);
		}
		
		public void end() throws SAXException{
			String value = getText().toString().trim();
		}
	}
	
	class SyncKeyState extends AbstractState {
		public SyncKeyState(XMLParserHandler handler) {
			super(handler);
		}
		
		public void end() throws SAXException{
			String value = getText().toString().trim();
		}
	}
	
	class SkipState extends AbstractState {
		public SkipState(XMLParserHandler handler) {
			super(handler);
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			return new SkipState(getHandler());
		}
	}
	
	class CollectionIdState extends AbstractState {
		public CollectionIdState(XMLParserHandler handler) {
			super(handler);
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			return new SkipState(getHandler());
		}
	}
	
	class HeartbeatIntervalState extends AbstractState {
		public HeartbeatIntervalState(XMLParserHandler handler) {
			super(handler);
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			return super.startElement(tagName);
		}
	}
	
	class FoldersState extends AbstractState {
		public FoldersState(XMLParserHandler handler) {
			super(handler);
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			
			if (tagName.equalsIgnoreCase("Folder")){
				FoldersState a = new FoldersState(getHandler());
                return a;
			}
			if (tagName.equalsIgnoreCase("Id")){
				IdState a = new IdState(getHandler());
                return a;
			}
			if (tagName.equalsIgnoreCase("Class")){
				ClassState a = new ClassState(getHandler());
                return a;
			}
			
			return super.startElement(tagName);
		}
	}
	
	class IdState extends AbstractState {
		public IdState(XMLParserHandler handler) {
			super(handler);
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			return new SkipState(getHandler());
		}
	}
	
}
