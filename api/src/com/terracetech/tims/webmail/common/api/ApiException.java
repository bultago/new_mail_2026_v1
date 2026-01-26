package com.terracetech.tims.webmail.common.api;

import org.springframework.http.HttpStatus;

/**
 * API 사용자 정의 예외 클래스
 * 
 * 작성일: 2025-10-21
 * Phase: 3.5 - DWR → REST API 전환
 */
public class ApiException extends RuntimeException {
    
    private String errorCode;
    private HttpStatus httpStatus;
    
    /**
     * 기본 생성자
     */
    public ApiException(String message) {
        super(message);
        this.errorCode = "API_ERROR";
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    
    /**
     * 에러 코드 포함 생성자
     */
    public ApiException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    
    /**
     * HTTP 상태 코드 포함 생성자
     */
    public ApiException(String errorCode, String message, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
    
    /**
     * 원인 예외 포함 생성자
     */
    public ApiException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    
    /**
     * 전체 파라미터 생성자
     */
    public ApiException(String errorCode, String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
    
    // Getters
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    
    // Static Factory Methods
    
    /**
     * Bad Request (400) 예외 생성
     */
    public static ApiException badRequest(String message) {
        return new ApiException("BAD_REQUEST", message, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Unauthorized (401) 예외 생성
     */
    public static ApiException unauthorized(String message) {
        return new ApiException("UNAUTHORIZED", message, HttpStatus.UNAUTHORIZED);
    }
    
    /**
     * Forbidden (403) 예외 생성
     */
    public static ApiException forbidden(String message) {
        return new ApiException("FORBIDDEN", message, HttpStatus.FORBIDDEN);
    }
    
    /**
     * Not Found (404) 예외 생성
     */
    public static ApiException notFound(String message) {
        return new ApiException("NOT_FOUND", message, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Internal Server Error (500) 예외 생성
     */
    public static ApiException internalError(String message) {
        return new ApiException("INTERNAL_ERROR", message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
