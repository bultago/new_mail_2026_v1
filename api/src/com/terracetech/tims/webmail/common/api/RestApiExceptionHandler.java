package com.terracetech.tims.webmail.common.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.servlet.http.HttpServletRequest;

/**
 * REST API 전역 예외 처리기
 * 
 * 작성일: 2025-10-21
 * Phase: 3.5 - DWR → REST API 전환
 */
@ControllerAdvice
public class RestApiExceptionHandler {
    
    private static final Logger log = LoggerFactory.getLogger(RestApiExceptionHandler.class);
    
    /**
     * 일반 예외 처리
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiResponse<Void> handleException(Exception ex, HttpServletRequest request) {
        log.error("API 처리 중 예외 발생: {} - {}", request.getRequestURI(), ex.getMessage(), ex);
        return ApiResponse.error("INTERNAL_ERROR", "서버 오류가 발생했습니다: " + ex.getMessage());
    }
    
    /**
     * IllegalArgumentException 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse<Void> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("잘못된 요청: {} - {}", request.getRequestURI(), ex.getMessage());
        return ApiResponse.error("INVALID_ARGUMENT", ex.getMessage());
    }
    
    /**
     * NullPointerException 처리
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiResponse<Void> handleNullPointerException(NullPointerException ex, HttpServletRequest request) {
        log.error("Null 참조 오류: {} - {}", request.getRequestURI(), ex.getMessage(), ex);
        return ApiResponse.error("NULL_POINTER", "필수 데이터가 없습니다");
    }
    
    /**
     * RuntimeException 처리
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiResponse<Void> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        log.error("런타임 예외 발생: {} - {}", request.getRequestURI(), ex.getMessage(), ex);
        return ApiResponse.error("RUNTIME_ERROR", "처리 중 오류가 발생했습니다: " + ex.getMessage());
    }
    
    /**
     * 사용자 정의 예외 처리 (추가 가능)
     */
    @ExceptionHandler(ApiException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException ex, HttpServletRequest request) {
        log.warn("API 예외: {} - {} ({})", request.getRequestURI(), ex.getMessage(), ex.getErrorCode());
        ApiResponse<Void> response = ApiResponse.error(ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }
}
