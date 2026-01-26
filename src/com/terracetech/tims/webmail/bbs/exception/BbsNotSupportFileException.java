package com.terracetech.tims.webmail.bbs.exception;

/**
 * 게시판 지원하지 않는 파일 예외
 */
public class BbsNotSupportFileException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public BbsNotSupportFileException() {
        super();
    }
    
    public BbsNotSupportFileException(String message) {
        super(message);
    }
    
    public BbsNotSupportFileException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public BbsNotSupportFileException(Throwable cause) {
        super(cause);
    }
}

