package com.terracetech.tims.webmail.bbs.exception;

/**
 * 게시판 컨텐츠 크기 초과 예외
 */
public class BbsContentSizeException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public BbsContentSizeException() {
        super();
    }
    
    public BbsContentSizeException(String message) {
        super(message);
    }
    
    public BbsContentSizeException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public BbsContentSizeException(Throwable cause) {
        super(cause);
    }
}

