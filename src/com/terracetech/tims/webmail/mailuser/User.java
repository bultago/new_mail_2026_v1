package com.terracetech.tims.webmail.mailuser;

public interface User {
	//db mail_user
	public final static String MAIL_USER_SEQ = "MAIL_USER_SEQ";
	public final static String MAIL_UID = "MAIL_UID";
	public final static String MAIL_DOMAIN_SEQ = "MAIL_DOMAIN_SEQ";
	public final static String MAIL_GROUP_SEQ = "MAIL_GROUP_SEQ";
	public final static String MAIL_PASSWORD = "MAIL_PASSWORD";
	public final static String MAIL_HOST = "MAIL_HOST";
	public final static String MESSAGE_STORE = "MESSAGE_STORE";
	public final static String ACCOUNT_EXPIRE_DATE = "ACCOUNT_EXPIRE_DATE";
	public final static String ACCOUNT_STATUS = "ACCOUNT_STATUS";
	public final static String USER_TYPE = "USER_TYPE";
	public final static String MAIL_SERVICES = "MAIL_SERVICES";
	public final static String MAIL_ADD_QUOTA = "MAIL_ADD_QUOTA";
	public final static String QUOTA_OVERLOOK_RATIO = "QUOTA_OVERLOOK_RATIO";
	public final static String QUOTA_WARNING_MODE = "QUOTA_WARNING_MODE";
	public final static String QUOTA_WARNING_RATIO = "QUOTA_WARNING_RATIO";
	public final static String QUOTA_VIOLATION_ACTION = "QUOTA_VIOLATION_ACTION";
	public final static String LOGIN_TIMESTAMP = "LOGIN_TIMESTAMP";
	//db mail_domain
	public final static String MAIL_DOMAIN = "MAIL_DOMAIN";
	
	//db mail_domain_group
	public final static String MAIL_QUOTA = "MAIL_QUOTA";
	public final static String WEB_FOLDER_QUOTA = "WEB_FOLDER_QUOTA";
	public final static String BIG_ATTACH_QUOTA = "BIG_ATTACH_QUOTA";
	public final static String MAX_FORWARDING = "MAX_FORWARDING";
	
	//db wcf_mailuseretc
	public final static String PAGE_LINE_CNT = "PAGE_LINE_CNT";
	public final static String SENDER_NAME = "SENDER_NAME";
	public final static String SAVE_SEND_BOX = "SAVE_SEND_BOX";
	public final static String RECEIVE_NOTI = "RECEIVE_NOTI";
	public final static String VCARD_ATTACH = "VCARD_ATTACH";
	public final static String USER_LOCALE = "USER_LOCALE";
	public final static String WRITE_MODE = "WRITE_MODE";
	public final static String CHAR_SET = "CHAR_SET";
	public final static String HIDDEN_IMG = "HIDDEN_IMG";
	public final static String SIGN_ATTACH = "SIGN_ATTACH";
	public final static String AFTER_LOGIN = "AFTER_LOGIN";
	public final static String HIDDEN_TAG = "HIDDEN_TAG";
	public final static String NOTI_INTERVAL = "NOTI_INTERVAL";
	public final static String USER_SKIN = "USER_SKIN";
	public final static String SENDER_EMAIL = "SENDER_EMAIL";
	public final static String FORWARDING_MODE = "FORWARDING_MODE";
	public final static String AUTO_SAVE_MODE = "AUTO_SAVE_MODE";
	public final static String AUTO_SAVE_TERM = "AUTO_SAVE_TERM";
	public final static String RENDER_MODE = "RENDER_MODE";
	
	//db mail_user_info
	public final static String SSN = "SSN";
	public final static String EMPNO = "EMPNO";
	public final static String USER_NAME = "USER_NAME";
	public final static String USER_LANGUAGE = "USER_LOCALE";
	public final static String WEB_FOLDER_ADD_QUOTA = "WEB_FOLDER_ADD_QUOTA";
	public final static String BIG_ATTACH_ADD_QUOTA = "BIG_ATTACH_ADD_QUOTA";
	public final static String REGISTER_STATUS = "REGISTER_STATUS";
	public final static String LAST_LOGIN_TIME = "LAST_LOGIN_TIME";
	public final static String WEBMAIL_LOGIN_TIME = "WEBMAIL_LOGIN_TIME";
	public final static String POP_LOGIN_TIME = "POP_LOGIN_TIME";
	public final static String IMAP_LOGIN_TIME = "IMAP_LOGIN_TIME";
	
	//etc
	public final static String EMAIL = "EMAIL";
	public final static String LOCALE = "LOCALE";
	public final static String ACCESS_TIME = "ACCESS_TIME";
	public final static String QUOTA_STR = "QUOTA_STR";
	public final static String IMAP_LOGIN_ARGS = "IMAP_LOGIN_ARGS";
	public final static String WEBFOLDER_QUOTA_STR = "WEBFOLDER_QUOTA_STR";
	public final static String WEBFOLDER_LOGIN_ARGS = "WEBFOLDER_LOGIN_ARGS";
	public final static String WEBFOLDER_SUB_ARGS = "WEBFOLDER_SUB_ARGS";
	
	public final static String SESSION_CHECK_TIME = "SCTIME";
	public final static String FIRST_LOGIN = "FL";
	
	public final static String USE_ACTIVE_X = "USE_ACTIVEX_USE";
	public final static String ACTIVE_X = "ACTIVEX_USE";
	public final static String NOTE_USE = "NOTE_USE";
	public final static String MOBILE_UID = "MOBILE_UID";
	
	//abbreviation
	public final static String SM_MAIL_USER_SEQ = "MUS";
	public final static String SM_MAIL_UID = "MU";
	public final static String SM_MAIL_DOMAIN_SEQ = "MDS";
	public final static String SM_MAIL_GROUP_SEQ = "MGS";
	public final static String SM_MAIL_HOST = "MH";
	public final static String SM_MESSAGE_STORE = "MS";
	public final static String SM_MAIL_DOMAIN = "MD";
	public final static String SM_MAIL_QUOTA = "MQ";
	public final static String SM_WEB_FOLDER_QUOTA = "WFQ";
	public final static String SM_BIG_ATTACH_QUOTA = "BAQ";
	public final static String SM_PAGE_LINE_CNT = "PLC";
	public final static String SM_WEBMAIL_LOGIN_TIME = "WLT";
	public final static String SM_USER_SKIN = "US";
	public final static String SM_USER_NAME = "UN";
	public final static String SM_EMAIL = "EM";
	public final static String SM_LOCALE = "LC";
	public final static String SM_ACCESS_TIME = "AT";
	public final static String SM_IMAP_LOGIN_ARGS = "ILA";
	public final static String SM_WEBFOLDER_LOGIN_ARGS = "WLA";
	public final static String SM_MAX_FORWARDING = "MF";
	public final static String SM_SESSION_CHECK_TIME = "SCT";
	public final static String SM_FIRST_LOGIN = "FL";
	public final static String SM_USE_ACTIVE_X = "UAX";
	public final static String SM_ACTIVE_X = "AX";
	public final static String SM_RENDER_MODE = "RM";
	public final static String SM_NOTE_USE = "NU";
	public final static String SM_LOGIN_TIMESTAMP = "LT";
	public final static String SM_MOBILE_UID = "MOU";
	
	public final static String JSESSIONID = "JSESSIONID";
	public final static String SM_JSESSIONID = "JSESSIONID";
	
	public final static String LOGINIP = "LOGINIP";
	public final static String SM_LOGINIP = "LOGINIP";
	
    public String get(String key);

    public void put(String key, Object value);
    
    public String[] getKeys () ;
    
    public void removeKey (String key);
}