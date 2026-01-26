package com.terracetech.tims.persistent;

import javax.sql.DataSource;

/**
 * DataSource 컬렉션 클래스
 */
public class DataSourceCollection {
    
    private DataSource dataSource;
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private int maxActive;
    private int maxIdle;
    private int minIdle;
    
    // Getters and Setters
    public DataSource getDataSource() {
        return dataSource;
    }
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public String getDriverClassName() {
        return driverClassName;
    }
    
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public int getMaxActive() {
        return maxActive;
    }
    
    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }
    
    public int getMaxIdle() {
        return maxIdle;
    }
    
    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }
    
    public int getMinIdle() {
        return minIdle;
    }
    
    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }
}


