package com.terracetech.tims.webmail.bbs.exception;

/**
 * 게시판 권한 예외
 */
public class BbsAuthException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public BbsAuthException() {
        super();
    }
    
    public BbsAuthException(String message) {
        super(message);
    }
    
    public BbsAuthException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public BbsAuthException(Throwable cause) {
        super(cause);
    }
}

