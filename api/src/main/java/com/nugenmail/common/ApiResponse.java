package com.nugenmail.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Standard API Response Wrapper.
 * Used for all JSON responses to ensure consistent structure.
 * 
 * @param <T> Data type
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private int status;
    private String message;
    private T data;
    private boolean success;

    /**
     * Creates a success response (HTTP 200).
     * 
     * @param data Payload data
     * @param <T>  Type
     * @return ApiResponse with success status
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .success(true)
                .data(data)
                .build();
    }

    /**
     * Creates an error response.
     * 
     * @param status  HTTP Status code
     * @param message Error message
     * @param <T>     Type
     * @return ApiResponse with error status
     */
    public static <T> ApiResponse<T> error(int status, String message) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .success(false)
                .build();
    }
}
