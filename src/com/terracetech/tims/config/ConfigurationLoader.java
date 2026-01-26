package com.terracetech.tims.config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;

/**
 * 설정 로더 인터페이스
 */
public interface ConfigurationLoader {
    
    /**
     * 설정 파일 로드
     * 
     * @param configPath 설정 파일 경로
     * @return Configuration 객체
     * @throws ConfigurationException 설정 로드 실패 시
     */
    Configuration load(String configPath) throws ConfigurationException;
    
    /**
     * 설정 재로드
     * 
     * @throws ConfigurationException 설정 로드 실패 시
     */
    void reload() throws ConfigurationException;
}

