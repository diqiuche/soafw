package com.kjt.service.common.config;

public interface IConfiguration {
    
    String getString(String key);

    Object getProperty(String key);
}
