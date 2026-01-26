package com.terracetech.tims.service.aync;

import java.io.IOException;

import org.kxml2.wap.WbxmlParser;
import org.kxml2.wap.WbxmlSerializer;

public class Wbxml  {
	
	public static final int Parent = 0;
	public static final int Inbox = 2;
	public static final int Trash = 3;
	public static final int Junk = 4;
	public static final int Sent = 5;
	public static final int Drafts = 6;
	public static final int Contacts = 7;
	public static final int Calendar = 8;
	public static final int Chats = 14;
	public static final int Tasks = 15;
	
    
	public static WbxmlParser createParser () throws IOException {
		
		WbxmlParser parser = new WbxmlParser();

		parser.setTagTable (0, Wbxml.tagTablePage0);
		parser.setTagTable (1, Wbxml.tagTablePage1);
		parser.setTagTable (2, Wbxml.tagTablePage2);
		parser.setTagTable (3, Wbxml.tagTablePage3);
		parser.setTagTable (4, Wbxml.tagTablePage4);
		parser.setTagTable (5, Wbxml.tagTablePage5);
		parser.setTagTable (6, Wbxml.tagTablePage6);
		parser.setTagTable (7, Wbxml.tagTablePage7);
		parser.setTagTable (8, Wbxml.tagTablePage8);
		parser.setTagTable (9, Wbxml.tagTablePage9);
		parser.setTagTable (10, Wbxml.tagTablePage10);
		parser.setTagTable (11, Wbxml.tagTablePage11);
		parser.setTagTable (12, Wbxml.tagTablePage12);
		parser.setTagTable (13, Wbxml.tagTablePage13);
		parser.setTagTable (14, Wbxml.tagTablePage14);
		parser.setTagTable (15, Wbxml.tagTablePage15);
		parser.setTagTable (16, Wbxml.tagTablePage16);
		parser.setTagTable (17, Wbxml.tagTablePage17);

		parser.setAttrStartTable (0, Wbxml.attrStartTable);
        
		parser.setAttrValueTable (0, Wbxml.attrValueTable);

		return parser;
	}
    
	public static WbxmlSerializer createSerializer() {
		WbxmlSerializer serializer = new WbxmlSerializer();
		
		serializer.setTagTable (0, Wbxml.tagTablePage0);
		serializer.setTagTable (1, Wbxml.tagTablePage1);
		serializer.setTagTable (2, Wbxml.tagTablePage2);
		serializer.setTagTable (3, Wbxml.tagTablePage3);
		serializer.setTagTable (4, Wbxml.tagTablePage4);
		serializer.setTagTable (5, Wbxml.tagTablePage5);
		serializer.setTagTable (6, Wbxml.tagTablePage6);
		serializer.setTagTable (7, Wbxml.tagTablePage7);
		serializer.setTagTable (8, Wbxml.tagTablePage8);
		serializer.setTagTable (9, Wbxml.tagTablePage9);
		serializer.setTagTable (10, Wbxml.tagTablePage10);
		serializer.setTagTable (11, Wbxml.tagTablePage11);
		serializer.setTagTable (12, Wbxml.tagTablePage12);
		serializer.setTagTable (13, Wbxml.tagTablePage13);
		serializer.setTagTable (14, Wbxml.tagTablePage14);
		serializer.setTagTable (15, Wbxml.tagTablePage15);
		serializer.setTagTable (16, Wbxml.tagTablePage16);
		serializer.setTagTable (17, Wbxml.tagTablePage17);

		serializer.setAttrStartTable (0, Wbxml.attrStartTable);
        
		serializer.setAttrValueTable (0, Wbxml.attrValueTable);
		
		return serializer;
	}
	
    public static final String [] tagTablePage0 = {
        /*AirSync*/
        "Sync",              	//  0x00, 0x05
        "Responses",    	   	//  0x00, 0x06
        "Add", 					//  0x00, 0x07
        "Change",				//  0x00, 0x08
        "Delete",				//  0x00, 0x09
        "Fetch",				//  0x00, 0x0A
        "SyncKey",				//  0x00, 0x0B
        "ClientId",     		//  0x00, 0x0C
        "ServerId",	            //  0x00, 0x0D
        "Status",          		//  0x00, 0x0E
        "Collection",			//  0x00, 0x0F
        "Class", 	         	//  0x00, 0x10
        "Version",           	//  0x00, 0x11
        "CollectionId",	    	//  0x00, 0x12
        "GetChanges",	    	//  0x00, 0x13
        "MoreAvailable",        //  0x00, 0x14
        "WindowSize",	        //  0x00, 0x15
        "Commands",			    //  0x00, 0x16
        "Options",			    //  0x00, 0x17
        "FilterType",		    //  0x00, 0x18
        "Truncation",	    	//  0x00, 0x19
        "RtfTruncation",		//  0x00, 0x1A
        "Conflict",		    	//  0x00, 0x1B
        "Collections", 		    //  0x00, 0x1C
        "ApplicationData",	    //  0x00, 0x1D
        "DeletesAsMoves",		//  0x00, 0x1E
        "P00T1F",	        	//  0x00, 0x1F
        "Supported",         	//  0x00, 0x20
        "SoftDelete", 	        //  0x00, 0x21
        "MIMESupport", 			//  0x00, 0x22
        "MIMETruncation",       //  0x00, 0x23
        "Wait",      		    //  0x00, 0x24
        "Limit",		        //  0x00, 0x25
        "Partial",            	//  0x00, 0x26
        "ConversationMode",     //  0x00, 0x27
        "MaxItems",      		//  0x00, 0x28
        "HeartbeatInterval",	//  0x00, 0x29
    };
    
    public static final String [] tagTablePage1 = {
        /* Contacts */
        "Anniversary",              //  0x01, 0x05
        "AssistantName",    	    //  0x01, 0x06
        "AssistantTelephoneNumber", //  0x01, 0x07
        "Birthday",					//  0x01, 0x08
        "Body",						//  0x01, 0x09
        "P01T0A",					//  0x01, 0x0A
        "P01T0B",					//  0x01, 0x0B
        "Business2PhoneNumber",     //  0x01, 0x0C
        "BusinessCity",	            //  0x01, 0x0D
        "BusinessCountry",          //  0x01, 0x0E
        "BusinessPostalCode",		//  0x01, 0x0F
        "BusinessState",            //  0x01, 0x10
        "BusinessStreet",           //  0x01, 0x11
        "BusinessFaxNumber",	    //  0x01, 0x12
        "BusinessPhoneNumber",	    //  0x01, 0x13
        "CarPhoneNumber",           //  0x01, 0x14
        "Categories",	            //  0x01, 0x15
        "Category",			        //  0x01, 0x16
        "Children",			        //  0x01, 0x17
        "Child",		            //  0x01, 0x18
        "CompanyName",			    //  0x01, 0x19
        "Department",				//  0x01, 0x1A
        "Email1Address",		    //  0x01, 0x1B
        "Email2Address", 		    //  0x01, 0x1C
        "Email3Address",	        //  0x01, 0x1D
        "FileAs",			        //  0x01, 0x1E
        "FirstName",		        //  0x01, 0x1F
        "Home2PhoneNumber",         //  0x01, 0x20
        "HomeCity", 	            //  0x01, 0x21
        "HomeCountry", 			    //  0x01, 0x22
        "HomePostalCode",           //  0x01, 0x23
        "HomeState",      		    //  0x01, 0x24
        "HomeStreet",		        //  0x01, 0x25
        "HomeFaxNumber",            //  0x01, 0x26
        "HomePhoneNumber",          //  0x01, 0x27
        "JobTitle",      		    //  0x01, 0x28
        "LastName",					//  0x01, 0x29
        "MiddleName",   		    //  0x01, 0x2A
        "MobilePhoneNumber",        //  0x01, 0x2B
        "OfficeLocation",		    //  0x01, 0x2C
        "OtherCity",		        //  0x01, 0x2D
        "OtherCountry",			    //  0x01, 0x2E
        "OtherPostalCode",		    //  0x01, 0x2F
        "OtherState",		        //  0x01, 0x30
        "OtherStreet",			    //  0x01, 0x31
        "PagerNumber",		        //  0x01, 0x32
        "RadioPhoneNumber",         //  0x01, 0x33
        "Spouse",     			    //  0x01, 0x34
        "Suffix",                   //  0x01, 0x35
        "Title",		            //  0x01, 0x36
        "Webpage",			        //  0x01, 0x37
        "YomiCompanyName",		    //  0x01, 0x38
        "YomiFirstName",		    //  0x01, 0x39
        "YomiLastName",				//  0x01, 0x3A
        "Rtf",						//  0x01, 0x3B
        "Picture",			        //  0x01, 0x3C
        "Alias",					//  0x01, 0x3D
        "WeightedRank",		        //  0x01, 0x3E
    };
    
    public static final String [] tagTablePage2 = {
        /* Email */
    	"Attachment",  				//  0x01, 0x05
    	"Attachments",     			//  0x01, 0x06
    	"AttName",					//  0x01, 0x07
    	"AttSize",					//  0x01, 0x08
        "P02T09",					//  0x01, 0x09
        "AttMethod",				//  0x01, 0x0A
        "P02T0B",					//  0x01, 0x0B
        "Body",					    //  0x01, 0x0C
        "BodySize",		            //  0x01, 0x0D
        "BodyTruncated",	        //  0x01, 0x0E
        "DateReceived",				//  0x01, 0x0F
        "DisplayName",	            //  0x01, 0x10
        "DisplayTo",     	        //  0x01, 0x11
        "Importance",	  		    //  0x01, 0x12
        "MessageClass",	  			//  0x01, 0x13
        "Subject",   		        //  0x01, 0x14
        "Read",	  			        //  0x01, 0x15
        "To",				        //  0x01, 0x16
        "CC",				        //  0x01, 0x17
        "From",			            //  0x01, 0x18
        "ReplyTo",				    //  0x01, 0x19
        "AllDayEvent",				//  0x01, 0x1A
        "Categories",			    //  0x01, 0x1B
        "Category", 			    //  0x01, 0x1C
        "DTStamp",	  		        //  0x01, 0x1D
        "EndTime",			        //  0x01, 0x1E
        "InstanceType",		        //  0x01, 0x1F
        "BusyStatus",  		        //  0x01, 0x20
        "Location", 	            //  0x01, 0x21
        "MeetingRequest", 		    //  0x01, 0x22
        "Organizer",    	        //  0x01, 0x23
        "RecurrenceId",    		    //  0x01, 0x24
        "Reminder",			        //  0x01, 0x25
        "ResponseRequested",        //  0x01, 0x26
        "Recurrences",	            //  0x01, 0x27
        "Recurrence",      		    //  0x01, 0x28
        "Recurrence_Type",			//  0x01, 0x29
        "Recurrence_Until",		    //  0x01, 0x2A
        "Recurrence_Occurrences",   //  0x01, 0x2B
        "Recurrence_Interval",	    //  0x01, 0x2C
        "Recurrence_DayOfWeek",     //  0x01, 0x2D
        "Recurrence_DayOfMonth",    //  0x01, 0x2E
        "Recurrence_WeekOfMonth",   //  0x01, 0x2F
        "Recurrence_MonthOfYear",   //  0x01, 0x30
        "StartTime",			    //  0x01, 0x31
        "Sensitivity",		        //  0x01, 0x32
        "TimeZone",			        //  0x01, 0x33
        "GlobalObjId", 			    //  0x01, 0x34
        "ThreadTopic",              //  0x01, 0x35
        "MIMEData",		            //  0x01, 0x36
        "MIMETruncated",	        //  0x01, 0x37
        "MIMESize",				    //  0x01, 0x38
        "InternetCPID",			    //  0x01, 0x39
        "Flag",						//  0x01, 0x3A
        "FlagStatus",				//  0x01, 0x3B
        "ContentClass",		        //  0x01, 0x3C
        "FlagType",					//  0x01, 0x3D
        "CompleteTime",		        //  0x01, 0x3E
        "DisallowNewTimeProposal",  //  0x01, 0x3F
    };
    
    /**
     * Code page 3 is no longer in use, however, tokens 05 through 17 have been defined.
     */
    public static final String [] tagTablePage3 = {
        /* AirNotify */
    	"Notify", 
    	"P03T06", 
    	"P03T07", 
    	"Lifetime", 
    	"DeviceInfo", 
    	"Enable", 
    	"Folder", 
    	"ServerId", 
    	"DeviceAddress", 
    	"ValidCarrierProfiles", 
        "P03T0F", 
        "Status", 
        "P03T11", 
        "P03T12", 
        "P03T13", 
        "P03T14", 
        "P03T15", 
        "P03T16", 
        "FriendlyName"
    };
    
    public static final String [] tagTablePage4 = {
        /* Calendar */
        "TimeZone",			            //  0x04, 0x05
        "AllDayEvent",			        //  0x04, 0x06
        "Attendees",				    //  0x04, 0x07
        "Attendee",			            //  0x04, 0x08
        "Email",				        //  0x04, 0x09
        "Name",				            //  0x04, 0x0A
        "Body",			                //  0x04, 0x0B
        "P04T0C",						//  0x04, 0x0C
        "BusyStatus",		            //  0x04, 0x0D
        "Categories",				    //  0x04, 0x0E
        "Category",					    //  0x04, 0x0F
        "Rtf",			                //  0x04, 0x10
        "DTStamp",			            //  0x04, 0x11
        "EndTime",				        //  0x04, 0x12
        "Exception",			        //  0x04, 0x13
        "Exceptions",				    //  0x04, 0x14
        "Exception_Deleted",		    //  0x04, 0x15
        "Exception_StartTime",          //  0x04, 0x16
        "Location",			            //  0x04, 0x17
        "MeetingStatus",				//  0x04, 0x18
        "Organizer_Email",		        //  0x04, 0x19
        "Organizer_Name",	            //  0x04, 0x1A
        "Recurrence",					//  0x04, 0x1B
        "Recurrence_Type",			    //  0x04, 0x1C
        "Recurrence_Until",			    //  0x04, 0x1D
        "Recurrence_Occurrences",       //  0x04, 0x1E
        "Recurrence_Interval",			//  0x04, 0x1F
        "Recurrence_DayOfWeek",			//  0x04, 0x20
        "Recurrence_DayOfMonth",		//  0x04, 0x21
        "Recurrence_WeekOfMonth",		//  0x04, 0x22
        "Recurrence_MonthOfYear",		//  0x04, 0x23
        "Reminder_MinsBefore",			//  0x04, 0x24
        "Sensitivity",					//  0x04, 0x25
        "Subject",						//  0x04, 0x26
        "StartTime",					//  0x04, 0x27
        "UID",							//  0x04, 0x28
        "Attendee_Status",				//  0x04, 0x29
        "Attendee_Type",				//  0x04, 0x2A
        "P04T2B",						//  0x04, 0x2B
        "P04T2C",						//  0x04, 0x2C
        "P04T2D",						//  0x04, 0x2D
        "P04T2E",						//  0x04, 0x2E
        "P04T2F",						//  0x04, 0x2F
        "P04T30",						//  0x04, 0x30
        "P04T31",						//  0x04, 0x31
        "P04T32",						//  0x04, 0x32
        "DisallowNewTimeProposal",		//  0x04, 0x33
        "ResponseRequested",			//  0x04, 0x34
        "AppointmentReplyTime",			//  0x04, 0x35
        "ResponseType",					//  0x04, 0x36
    };
    
    public static final String [] tagTablePage5 = {
        /* Move */
        "Moves",       					//  0x05, 0x05
        "Move",				            //  0x05, 0x06
        "SrcMsgId", 			        //  0x05, 0x07
        "SrcFldId",			            //  0x05, 0x08
        "DstFldId",				        //  0x05, 0x09
        "Response",				        //  0x05, 0x0A
        "Status",			            //  0x05, 0x0B
        "DstMsgId",			            //  0x05, 0x0C
    };
    
    public static final String [] tagTablePage6 = {
        /* ItemEstimate */
        "GetItemEstimate",              //  0x06, 0x05
        "Version",				        //  0x06, 0x06  
        "Collections",		       		//  0x06, 0x07
        "Collection",	 	            //  0x06, 0x08
        "Class",					    //  0x06, 0x09
        "CollectionId",					//  0x06, 0x0A
        "DateTime",						//  0x06, 0x0B
        "Estimate",						//  0x06, 0x0C
        "Response",					    //  0x06, 0x0D
        "Status",						//  0x06, 0x0E
    };
    
    public static final String [] tagTablePage7 = {
        /* FolderHierarchy */
        "Folders", 					//  0x07, 0x05
        "Folder",  					//  0x07, 0x06
        "DisplayName",			    //  0x07, 0x07
        "ServerId",					//  0x07, 0x08
        "ParentId",				    //  0x07, 0x09
        "Type", 					//  0x07, 0x0A
        "P07T0B",  					//  0x07, 0x0B
        "Status",			        //  0x07, 0x0C
        "P07T0D",  					//  0x07, 0x0D
        "Changes",		            //  0x07, 0x0E
        "Add",      	            //  0x07, 0x0F
        "Delete",    		        //  0x07, 0x10
        "Update",		            //  0x07, 0x11
        "SyncKey",			        //  0x07, 0x12
        "FolderCreate",		        //  0x07, 0x13
        "FolderDelete",			    //  0x07, 0x14
        "FolderUpdate",             //  0x07, 0x15
        "FolderSync",    			//  0x07, 0x16
        "Count",    				//  0x07, 0x17
    };
    
    public static final String [] tagTablePage8 = {
        /* MeetingResponse */
    	"CalendarId",				//  0x08, 0x05
        "CollectionId",				//  0x08, 0x06
        "MeetingResponse",		    //  0x08, 0x07
        "RequestId",				//  0x08, 0x08
        "Request",				    //  0x08, 0x09
        "Result", 					//  0x08, 0x0A
        "Status",  					//  0x08, 0x0B
        "UserResponse",		        //  0x08, 0x0C
        "P08T0D",  					//  0x08, 0x0D
        "InstanceId",	            //  0x08, 0x0E
    };
    
    public static final String [] tagTablePage9 = {
        /* Tasks */
    	"Body",				            //  0x09, 0x05
    	"BodySize",				        //  0x09, 0x06
    	"BodyTruncated",			    //  0x09, 0x07
        "Categories",		            //  0x09, 0x08
        "Category",				        //  0x09, 0x09
        "Complete",			            //  0x09, 0x0A
        "DateCompleted",                //  0x09, 0x0B
        "DueDate",						//  0x09, 0x0C
        "UTCDueDate",		            //  0x09, 0x0D
        "Importance",				    //  0x09, 0x0E
        "Recurrence",				    //  0x09, 0x0F
        "Recurrence_Type",              //  0x09, 0x10
        "Recurrence_Start",	            //  0x09, 0x11
        "Recurrence_Until",		        //  0x09, 0x12
        "Recurrence_Occurrences",       //  0x09, 0x13
        "Recurrence_Interval",		    //  0x09, 0x14
        "Recurrence_DayOfMonth",	    //  0x09, 0x15
        "Recurrence_DayOfWeek",         //  0x09, 0x16
        "Recurrence_WeekOfMonth",       //  0x09, 0x17
        "Recurrence_MonthOfYear",		//  0x09, 0x18
        "Recurrence_Regenerate",        //  0x09, 0x19
        "Recurrence_DeadOccur",         //  0x09, 0x1A
        "ReminderSet",					//  0x09, 0x1B
        "ReminderTime",				    //  0x09, 0x1C
        "Sensitivity",				    //  0x09, 0x1D
        "StartDate",			        //  0x09, 0x1E
        "UTCStartDate",					//  0x09, 0x1F
        "Subject",						//  0x09, 0x20
        "Rtf",							//  0x09, 0x21
        "OrdinalDate",					//  0x09, 0x22
        "SubOrdinalDate",				//  0x09, 0x23
        "CalendarType",					//  0x09, 0x24
        "IsLeapMonth",					//  0x09, 0x25
        "FirstDayOfWeek",				//  0x09, 0x26
    };
    
    public static final String [] tagTablePage10 = {
        /* ResolveRecipients */
    	"ResolveRecipients",            //  0x0A, 0x05
    	"Response",				        //  0x0A, 0x06
    	"Status",					    //  0x0A, 0x07
        "Type",				            //  0x0A, 0x08
        "Recipient",			        //  0x0A, 0x09
        "DisplayName",		            //  0x0A, 0x0A
        "EmailAddress",	                //  0x0A, 0x0B
        "Certificates",					//  0x0A, 0x0C
        "Certificate",		            //  0x0A, 0x0D
        "MiniCertificate",			    //  0x0A, 0x0E
        "Options",					    //  0x0A, 0x0F
        "To",				            //  0x0A, 0x10
        "CertificateRetrieval",         //  0x0A, 0x11
        "RecipientCount",		        //  0x0A, 0x12
        "MaxCertificates",		        //  0x0A, 0x13
        "MaxAmbiguousRecipients",	    //  0x0A, 0x14
        "CertificateCount",			    //  0x0A, 0x15
        "Availability",			        //  0x0A, 0x16
        "StartTime",			        //  0x0A, 0x17
        "EndTime",						//  0x0A, 0x18
        "MergedFreeBusy",   		    //  0x0A, 0x19
        "Picture",				        //  0x0A, 0x1A
        "MaxSize",						//  0x0A, 0x1B
        "Data",					        //  0x0A, 0x1C
        "MaxPictures",					//  0x0A, 0x1D
    };
    
    public static final String [] tagTablePage11 = {
        /* ValidateCert */
    	
    };
    
    public static final String [] tagTablePage12 = {
        /* Contacts2 */
    	"CustomerId",           		 //  0x0A, 0x05
    	"GovernmentId",				     //  0x0A, 0x06
    	"IMAddress",					 //  0x0A, 0x07
        "IMAddress2",				     //  0x0A, 0x08
        "IMAddress3",			         //  0x0A, 0x09
        "ManagerName",		             //  0x0A, 0x0A
        "CompanyMainPhone",	             //  0x0A, 0x0B
        "AccountName",					 //  0x0A, 0x0C
        "NickName",		            	 //  0x0A, 0x0D
        "MMS",		           		 	 //  0x0A, 0x0D
    };
    
    public static final String [] tagTablePage13 = {
        /* Ping */
    	"Ping",        				    //  0x0C, 0x05
    	"AutdState",			        //  0x0C, 0x06
    	"Status",					    //  0x0C, 0x07
        "HeartbeatInterval",            //  0x0C, 0x08
        "Folders",				        //  0x0C, 0x09
        "Folder",			            //  0x0C, 0x0A
        "Id",			                //  0x0C, 0x0B
        "Class",						//  0x0C, 0x0C
        "MaxFolders",		            //  0x0C, 0x0D
    };
    
    public static final String [] tagTablePage14 = {
        /* Provision */
    	"Provision",  							//  0x01, 0x05
    	"Policies",     						//  0x01, 0x06
    	"Policy",								//  0x01, 0x07
    	"PolicyType",							//  0x01, 0x08
        "PolicyKey",							//  0x01, 0x09
        "Data",									//  0x01, 0x0A
        "Status",								//  0x01, 0x0B
        "RemoteWipe",					    	//  0x01, 0x0C
        "EASProvisionDoc",		           		//  0x01, 0x0D
        "DevicePasswordEnabled",	        	//  0x01, 0x0E
        "AlphanumericDevicePasswordRequired",	//  0x01, 0x0F
        "DeviceEncryptionEnabled",	            //  0x01, 0x10
        "RequireStorageCardEncryption",     	//  0x01, 0x11
        "PasswordRecoveryEnabled",	  		    //  0x01, 0x12
        "AttachmentsEnabled",	  				//  0x01, 0x13
        "MinDevicePasswordLength",   		    //  0x01, 0x14
        "MaxInactivityTimeDeviceLock",	  		//  0x01, 0x15
        "MaxDevicePasswordFailedAttempts",		//  0x01, 0x16
        "MaxAttachmentSize",				    //  0x01, 0x17
        "AllowSimpleDevicePassword",			//  0x01, 0x18
        "DevicePasswordExpiration",				//  0x01, 0x19
        "DevicePasswordHistory",				//  0x01, 0x1A
        "AllowStorageCard",			    		//  0x01, 0x1B
        "AllowCamera", 			    			//  0x01, 0x1C
        "RequireDeviceEncryption",	  		    //  0x01, 0x1D
        "AllowUnsignedApplications",			//  0x01, 0x1E
        "AllowUnsignedInstallationPackages",	//  0x01, 0x1F
        "MinDevicePasswordComplexCharacters",  	//  0x01, 0x20
        "AllowWiFi", 	           		 		//  0x01, 0x21
        "AllowTextMessaging", 		    		//  0x01, 0x22
        "AllowPOPIMAPEmail",    	        	//  0x01, 0x23
        "AllowBluetooth",    		    		//  0x01, 0x24
        "AllowIrDA",			        		//  0x01, 0x25
        "RequireManualSyncWhenRoaming",         //  0x01, 0x26
        "AllowDesktopSync",	            		//  0x01, 0x27
        "MaxCalendarAgeFilter",      		    //  0x01, 0x28
        "AllowHTMLEmail",						//  0x01, 0x29
        "MaxEmailAgeFilter",		    		//  0x01, 0x2A
        "MaxEmailBodyTruncationSize",   		//  0x01, 0x2B
        "MaxEmailHTMLBodyTruncationSize",	    //  0x01, 0x2C
        "RequireSignedSMIMEMessages",     		//  0x01, 0x2D
        "RequireEncryptedSMIMEMessages",    	//  0x01, 0x2E
        "RequireSignedSMIMEAlgorithm",   		//  0x01, 0x2F
        "RequireEncryptionSMIMEAlgorithm",   	//  0x01, 0x30
        "AllowSMIMEEncryptionAlgorithmNegotiation",	//  0x01, 0x31
        "AllowSMIMESoftCerts",		        	//  0x01, 0x32
        "AllowBrowser",			        		//  0x01, 0x33
        "AllowConsumerEmail", 			    	//  0x01, 0x34
        "AllowRemoteDesktop",              		//  0x01, 0x35
        "AllowInternetSharing",		            //  0x01, 0x36
        "UnapprovedInROMApplicationList",	    //  0x01, 0x37
        "ApplicationName",				    	//  0x01, 0x38
        "ApprovedApplicationList",			    //  0x01, 0x39
        "Hash",									//  0x01, 0x3A
    };
    
    public static final String [] tagTablePage15 = {
        /* Search */
    	"Search",  						//  0x01, 0x05
    	"",     						//  0x01, 0x06
    	"Store",						//  0x01, 0x07
    	"Name",							//  0x01, 0x08
        "Query",						//  0x01, 0x09
        "Options",						//  0x01, 0x0A
        "Range",						//  0x01, 0x0B
        "Status",					    //  0x01, 0x0C
        "Response",		           		//  0x01, 0x0D
        "Result",	        			//  0x01, 0x0E
        "Properties",					//  0x01, 0x0F
        "Total",	            		//  0x01, 0x10
        "EqualTo",     					//  0x01, 0x11
        "Value",	  		    		//  0x01, 0x12
        "And",	  						//  0x01, 0x13
        "Or",   		    			//  0x01, 0x14
        "FreeText",	  					//  0x01, 0x15
        "",								//  0x01, 0x16
        "DeepTraversal",				//  0x01, 0x17
        "LongId",						//  0x01, 0x18
        "RebuildResults",				//  0x01, 0x19
        "LessThan",						//  0x01, 0x1A
        "GreaterThan",			    	//  0x01, 0x1B
        "Schema", 			    		//  0x01, 0x1C
        "",	  		    				//  0x01, 0x1D
        "UserName",						//  0x01, 0x1E
        "Password",						//  0x01, 0x1F
        "ConversationId",  				//  0x01, 0x20
        "Picture", 	           		 	//  0x01, 0x21
        "MaxSize", 		    			//  0x01, 0x22
        "MaxPictures",    	        	//  0x01, 0x23
    };
    
    public static final String [] tagTablePage16 = {
    	
    };
    
    public static final String [] tagTablePage17 = {
    	/* AirSyncBase */
    	"BodyPreference",				//  0x01, 0x05
    	"Type",    						//  0x01, 0x06
    	"StoTruncationSizere",			//  0x01, 0x07
    	"AllOrNone",					//  0x01, 0x08
        "",								//  0x01, 0x09
        "Body",							//  0x01, 0x0A
        "Data",							//  0x01, 0x0B
        "EstimatedDataSize",		    //  0x01, 0x0C
        "Truncated",	           		//  0x01, 0x0D
        "Attachments",        			//  0x01, 0x0E
        "Attachment",					//  0x01, 0x0F
        "DisplayName",            		//  0x01, 0x10
        "FileReference",				//  0x01, 0x11
        "Method",	  		    		//  0x01, 0x12
        "ContentId",					//  0x01, 0x13
        "ContentLocation",    			//  0x01, 0x14
        "IsInline",	  					//  0x01, 0x15
        "NativeBodyType",				//  0x01, 0x16
        "ContentType",					//  0x01, 0x17
        "Preview",						//  0x01, 0x18
        "BodyPartPreference",			//  0x01, 0x19
        "BodyPart",						//  0x01, 0x1A
        "Status",				    	//  0x01, 0x1B
    };
    
    public static final String [] attrStartTable = {
    };
    
    public static final String [] attrValueTable = {
    };

	public static final String NAMESPACES[] = {
	    "AirSync", "Contacts", "Email", "AirNotify", "Calendar", "Move", "ItemEstimate", "FolderHierarchy", "MeetingResponse", "Tasks", 
	    "ResolveRecipients", "ValidateCert", "Contacts2", "Ping", "Provision", "Search", "GAL", "AirSyncBase"
	};

	public static final String TAGTABLES[][] = {
		tagTablePage0, tagTablePage1, tagTablePage2, tagTablePage3, tagTablePage4, tagTablePage5, tagTablePage6, tagTablePage7, tagTablePage8, tagTablePage9, 
		tagTablePage10, tagTablePage11, tagTablePage12, tagTablePage13, tagTablePage14, tagTablePage15, tagTablePage16, tagTablePage17
	};
    
    
}
