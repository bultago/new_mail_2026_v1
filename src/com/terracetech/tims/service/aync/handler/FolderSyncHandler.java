package com.terracetech.tims.service.aync.handler;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import com.terracetech.tims.service.aync.data.FolderSyncData;
import com.terracetech.tims.service.aync.util.AbstractState;
import com.terracetech.tims.service.aync.util.XMLParserHandler;

public class FolderSyncHandler extends XMLParserHandler {
	
	private FolderSyncData model;
	public Logger log = Logger.getLogger(this.getClass());
	public FolderSyncData getModel() {
		return model;
	}

	@Override
	public AbstractState createStartState() {
		model = new FolderSyncData();
		
		return new FolderSyncState(this);
	}

	class FolderSyncState extends AbstractState {
		public FolderSyncState(XMLParserHandler handler) {
			super(handler);
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			if (tagName.equalsIgnoreCase("FolderSync")){
				FolderSyncState a = new FolderSyncState(getHandler());
                return a;
			}
			if (tagName.equalsIgnoreCase("Status")){
				StatusSyncState a = new StatusSyncState(getHandler());
                return a;
			}
			
			if (tagName.equalsIgnoreCase("SyncKey")){
				SyncKeyState a = new SyncKeyState(getHandler());
                return a;
			}
			if (tagName.equalsIgnoreCase("Changes")){
				ChangesState a = new ChangesState(getHandler());
                return a;
			}
			
			return super.startElement(tagName);
		}
	}

	class StatusSyncState extends AbstractState {
		public StatusSyncState(XMLParserHandler handler) {
			super(handler);
		}
		
		public void end() throws SAXException{
			String value = getText().toString().trim();
			try {
				model.setStatus(Integer.parseInt(value));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	class SyncKeyState extends AbstractState {
		public SyncKeyState(XMLParserHandler handler) {
			super(handler);
		}
		
		public void end() throws SAXException{
			String value = getText().toString().trim();
			model.setSyncKey(value);
		}
	}
	
	class ChangesState extends AbstractState {
		public ChangesState(XMLParserHandler handler) {
			super(handler);
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			if (tagName.equalsIgnoreCase("Count")){
				CountState a = new CountState(getHandler());
                return a;
			}
			return super.startElement(tagName);
		}
	}
	
	class CountState extends AbstractState {
		public CountState(XMLParserHandler handler) {
			super(handler);
		}
	}
}
