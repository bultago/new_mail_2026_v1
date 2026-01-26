package com.terracetech.tims.service.aync.handler;

import org.xml.sax.SAXException;

import com.terracetech.tims.service.aync.data.CalendarData;
import com.terracetech.tims.service.aync.data.ContactData;
import com.terracetech.tims.service.aync.data.EmailData;
import com.terracetech.tims.service.aync.data.ISyncData;
import com.terracetech.tims.service.aync.data.SyncData;
import com.terracetech.tims.service.aync.data.TaskData;
import com.terracetech.tims.service.aync.util.AbstractState;
import com.terracetech.tims.service.aync.util.XMLParserHandler;
import com.terracetech.tims.webmail.util.StringUtils;

public class SyncHandler extends XMLParserHandler {
	
	private SyncData model;
	
	public SyncData getModel() {
		return model;
	}

	@Override
	public AbstractState createStartState() {
		model = new SyncData();
		
		return new SyncState(this);
	}

	class SyncState extends AbstractState {
		public SyncState(XMLParserHandler handler) {
			super(handler);
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			if (tagName.equalsIgnoreCase("Sync")){
				SyncState a = new SyncState(getHandler());
                return a;
			}
			if (tagName.equalsIgnoreCase("Collections")){
				CollectionState a = new CollectionState(getHandler());
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
			model.setTarget(value);
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
			if (tagName.equalsIgnoreCase("SyncKey")){
				SyncKeyState a = new SyncKeyState(getHandler());
                return a;
			}
			if (tagName.equalsIgnoreCase("CollectionId")){
				CollectionIdState a = new CollectionIdState(getHandler());
                return a;
			}
			if (tagName.equalsIgnoreCase("DeletesAsMoves")){
				DeletesAsMovesState a = new DeletesAsMovesState(getHandler());
                return a;
			}
			if (tagName.equalsIgnoreCase("WindowSize")){
				WindowSizeState a = new WindowSizeState(getHandler());
                return a;
			}
			if (tagName.equalsIgnoreCase("Options")){
				OptionsState a = new OptionsState(getHandler());
                return a;
			}
			if (tagName.equalsIgnoreCase("GetChanges")){
				SkipState a = new SkipState(getHandler());
                return a;
			}
			if (tagName.equalsIgnoreCase("Commands")){
				CommandsState a = new CommandsState(getHandler());
                return a;
			}
			if (tagName.equalsIgnoreCase("Supported")){
				SupportedState a = new SupportedState(getHandler());
                return a;
			}
			if (tagName.equalsIgnoreCase("Status")){
				SkipState a = new SkipState(getHandler());
                return a;
			}
			return super.startElement(tagName);
		}
	}
	
	class DeletesAsMovesState extends AbstractState {
		public DeletesAsMovesState(XMLParserHandler handler) {
			super(handler);
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			return new SkipState(getHandler());
		}
	}
	
	class SupportedState extends AbstractState {
		public SupportedState(XMLParserHandler handler) {
			super(handler);
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			return new SkipState(getHandler());
		}
	}
	
	class WindowSizeState extends AbstractState {
		public WindowSizeState(XMLParserHandler handler) {
			super(handler);
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			return new SkipState(getHandler());
		}
		
		public void end() throws SAXException {
			super.end();
			String value = getText().toString().trim();
			if(StringUtils.isNotEmpty(value)){
				model.setCount(Integer.parseInt(value));	
			}
			
		}
	}
	
	class OptionsState extends AbstractState {
		public OptionsState(XMLParserHandler handler) {
			super(handler);
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			return new SkipState(getHandler());
		}
	}
	
	class CommandsState extends AbstractState {
		public CommandsState(XMLParserHandler handler) {
			super(handler);
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			if (tagName.equalsIgnoreCase("Add")){
				AddCommandsState a = new AddCommandsState(getHandler());
                return a;
			}
			
			if (tagName.equalsIgnoreCase("Change")){
				ModifyCommandsState a = new ModifyCommandsState(getHandler());
                return a;
			}
			
			if (tagName.equalsIgnoreCase("Delete")){
				DeleteCommandsState a = new DeleteCommandsState(getHandler());
                return a;
			}
			
			return new SkipState(getHandler());
		}
	}
	
	
	class AddCommandsState extends AbstractState {
		private ISyncData addData = null;
		
		public AddCommandsState(XMLParserHandler handler) {
			super(handler);
			if("Contacts".equalsIgnoreCase(model.getTarget())){
				addData = new ContactData();
			}else if("Email".equalsIgnoreCase(model.getTarget())){
				addData = new EmailData();
			}else if("Calendar".equalsIgnoreCase(model.getTarget())){
				addData = new CalendarData();
			}else{
				addData = new TaskData();
			}
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			if (tagName.equalsIgnoreCase("ClientId")){
				TextDataState a = new TextDataState(getHandler(), addData, tagName);
                return a;
			}
			if (tagName.equalsIgnoreCase("ApplicationData")){
				ApplicationDataState a = new ApplicationDataState(getHandler(), addData);
                return a;
			}
			return new SkipState(getHandler());
		}

		public void end() throws SAXException {
			super.end();
			
			model.setAddClientData(addData);
		}
	}
	
	class ModifyCommandsState extends AbstractState {
		private ISyncData modData = null;
		
		public ModifyCommandsState(XMLParserHandler handler) {
			super(handler);
			if("Contacts".equalsIgnoreCase(model.getTarget())){
				modData = new ContactData();
			}else if("Email".equalsIgnoreCase(model.getTarget())){
				modData = new EmailData();
			}else if("Calendar".equalsIgnoreCase(model.getTarget())){
				modData = new CalendarData();
			}else{
				modData = new TaskData();
			}
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			if (tagName.equalsIgnoreCase("ServerId")){
				TextDataState a = new TextDataState(getHandler(), modData, tagName);
                return a;
			}
			if (tagName.equalsIgnoreCase("ApplicationData")){
				ApplicationDataState a = new ApplicationDataState(getHandler(), modData);
                return a;
			}
			return new SkipState(getHandler());
		}

		public void end() throws SAXException {
			super.end();
			
			model.setModClientData(modData);
		}
	}
	
	class DeleteCommandsState extends AbstractState {
		private ISyncData delData = null;
		
		public DeleteCommandsState(XMLParserHandler handler) {
			super(handler);
			if("Contacts".equalsIgnoreCase(model.getTarget())){
				delData = new ContactData();
			}else if("Email".equalsIgnoreCase(model.getTarget())){
				delData = new EmailData();
			}else if("Calendar".equalsIgnoreCase(model.getTarget())){
				delData = new CalendarData();
			}else{
				delData = new TaskData();
			}
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			if (tagName.equalsIgnoreCase("ServerId")){
				TextDataState a = new TextDataState(getHandler(),delData, tagName);
                return a;
			}
			return new SkipState(getHandler());
		}

		public void end() throws SAXException {
			super.end();
			
			model.setDelClientData(delData);
		}
	}
	
	class DataState extends AbstractState {
		private ISyncData data = null;
		
		public DataState(XMLParserHandler handler, ISyncData data) {
			super(handler);
			this.data = data;
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			return new TextDataState(getHandler(), data, tagName);
		}
	}
	
	class ApplicationDataState extends AbstractState {
		private ISyncData data = null;
		
		public ApplicationDataState(XMLParserHandler handler, ISyncData data) {
			super(handler);
			this.data = data;
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			if (tagName.equalsIgnoreCase("Recurrence")){
				RecurrenceState a = new RecurrenceState(getHandler(), data);
                return a;
			}
			
			return new TextDataState(getHandler(), data, tagName);
		}
	}
	
	class TextDataState extends AbstractState {
		private ISyncData data = null;
		private String key = null;
		
		public TextDataState(XMLParserHandler handler, ISyncData data, String key) {
			super(handler);
			this.key = key;
			this.data = data;
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			return new SkipState(getHandler());
		}
		
		public void end() throws SAXException{
			String value = getText().toString().trim();
			if(data==null)
				System.out.println(data);
			data.setDataValue(key, value);
		}
	}
	
	class RecurrenceState extends AbstractState {
		private ISyncData data = null;
		
		public RecurrenceState(XMLParserHandler handler, ISyncData data) {
			super(handler);
			this.data = data;
			data.setDataValue("Recurrence", "on");
		}
		
		public AbstractState startElement(String tagName) throws SAXException{
			return new TextDataState(getHandler(), data, tagName);
		}
		
	}
}
