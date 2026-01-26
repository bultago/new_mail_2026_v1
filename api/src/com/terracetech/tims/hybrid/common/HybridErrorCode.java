package com.terracetech.tims.hybrid.common;

public class HybridErrorCode {

    public final static String CODE_NAME = "errorCode";
    public final static int SUCCESS = 0;
    public final static int ERROR = -1;

    public final static int NOT_ALLOW = -100;
    public final static int NOT_FOUND = -101;
    public final static int NOT_AGREE = -102;
    public final static int PASSWORD_FAIL = -110;
    public final static int PASSWORD_LOCK = -111;
    public final static int PASSWORD_CHANGE = -112;
    public final static int WEBMAIL_SERVICE_EXPIRE = -301;
    public final static int WEBMAIL_SERVICE_FAIL = -302;
    
    public final static int AUTH_FAIL = -300;
    
    public final static int SERVICE_FORBIDDEN = 403;
    public final static int MOBILE_VERSION_ERROR = 520;
    public final static int MOBILE_LICENSE_ERROR = 444;
    
    public final static int SERVICE_STOP = -500;
    public final static int IMAP_CONNECT_ERROR = -501;
    public final static int GET_FOLDER_INFO_ERROR = -502;

}
