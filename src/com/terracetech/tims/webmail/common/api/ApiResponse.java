package com.terracetech.tims.webmail.common.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * REST API 공통 응답 클래스
 * 
 * @param <T> 응답 데이터 타입
 * 
 * 작성일: 2025-10-21
 * Phase: 3.5 - DWR → REST API 전환
 */
public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    private String timestamp;
    private String errorCode;
    
    /**
     * 기본 생성자
     */
    public ApiResponse() {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
    
    /**
     * 성공 응답 생성
     */
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.data = data;
        response.message = "Success";
        return response;
    }
    
    /**
     * 성공 응답 생성 (메시지 포함)
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.data = data;
        response.message = message;
        return response;
    }
    
    /**
     * 실패 응답 생성
     */
    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.message = message;
        return response;
    }
    
    /**
     * 실패 응답 생성 (에러 코드 포함)
     */
    public static <T> ApiResponse<T> error(String errorCode, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.message = message;
        response.errorCode = errorCode;
        return response;
    }
    
    /**
     * 실패 응답 생성 (에러 코드, 메시지, 데이터 포함)
     */
    public static <T> ApiResponse<T> error(String errorCode, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.message = message;
        response.errorCode = errorCode;
        response.data = data;
        return response;
    }
    
    // Getters and Setters
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
