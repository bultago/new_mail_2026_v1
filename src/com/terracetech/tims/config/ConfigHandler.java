package com.terracetech.tims.config;

import org.apache.commons.configuration.Configuration;

/**
 * 설정 핸들러 인터페이스
 */
public interface ConfigHandler {
    
    /**
     * 설정 정보 조회
     * 
     * @return Configuration 객체
     */
    Configuration getConfiguration();
    
    /**
     * 설정값 조회
     * 
     * @param key 설정 키
     * @return 설정값
     */
    String getString(String key);
    
    /**
     * 설정값 조회 (기본값 포함)
     * 
     * @param key 설정 키
     * @param defaultValue 기본값
     * @return 설정값
     */
    String getString(String key, String defaultValue);
}

