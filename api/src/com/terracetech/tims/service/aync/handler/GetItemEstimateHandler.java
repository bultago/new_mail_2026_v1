package com.terracetech.tims.service.aync.handler;

import org.xml.sax.SAXException;

import com.terracetech.tims.service.aync.data.GetItemEstimateData;
import com.terracetech.tims.service.aync.util.AbstractState;
import com.terracetech.tims.service.aync.util.XMLParserHandler;

public class GetItemEstimateHandler extends XMLParserHandler {
	
	private GetItemEstimateData model;
	
	public GetItemEstimateData getModel() {
		return model;
	}

	@Override
	public AbstractState createStartState() {
		model = new GetItemEstimateData();
		
		return new GetItemEstimateState(this);
	}

	class GetItemEstimateState extends AbstractState {
		public GetItemEstimateState(XMLParserHandler handler) {
			super(handler);
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			if (tagName.equalsIgnoreCase("GetItemEstimate")){
				GetItemEstimateState a = new GetItemEstimateState(getHandler());
                return a;
			}
			if (tagName.equalsIgnoreCase("Version")){
				TextDataState a = new TextDataState(getHandler(), tagName);
                return a;
			}
			
			if (tagName.equalsIgnoreCase("Collections")){
				CollectionState a = new CollectionState(getHandler());
                return a;
			}
			
			return super.startElement(tagName);
		}
	}
	
	class CollectionState extends AbstractState {
		public CollectionState(XMLParserHandler handler) {
			super(handler);
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			if (tagName.equalsIgnoreCase("Collection")){
				CollectionState a = new CollectionState(getHandler());
                return a;
			}
			if (tagName.equalsIgnoreCase("Class")){
				ClassState a = new ClassState(getHandler());
                return a;
			}
			if (tagName.equalsIgnoreCase("CollectionId")){
				CollectionIdState a = new CollectionIdState(getHandler());
                return a;
			}
			
			if (tagName.equalsIgnoreCase("FilterType")){
				SkipState a = new SkipState(getHandler());
                return a;
			}
			
			if (tagName.equalsIgnoreCase("SyncKey")){
				SkipState a = new SkipState(getHandler());
                return a;
			}
			
			return super.startElement(tagName);
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
	
	class ClassState extends AbstractState {
		public ClassState(XMLParserHandler handler) {
			super(handler);
		}
		
		public void end() throws SAXException{
			String value = getText().toString().trim();
			model.setTarget(value);
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
	
	class TextDataState extends AbstractState {
		private String key = null;
		
		public TextDataState(XMLParserHandler handler, String key) {
			super(handler);
			this.key = key;
		}
		
		public void end() throws SAXException{
			String value = getText().toString().trim();
			model.setDataValue(key, value);
		}
	}
}
