package com.terracetech.tims.webmail.bbs.exception;

/**
 * 게시판 파일 크기 초과 예외
 */
public class BbsFileSizeException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public BbsFileSizeException() {
        super();
    }
    
    public BbsFileSizeException(String message) {
        super(message);
    }
    
    public BbsFileSizeException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public BbsFileSizeException(Throwable cause) {
        super(cause);
    }
}

