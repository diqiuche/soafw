package com.kjt.service.common.cache.impl;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.kjt.service.common.config.DynamicConfig;
import com.kjt.service.common.config.dict.ConfigComponent;
import com.kjt.service.common.config.dict.ConfigFileDict;

@Component(ConfigComponent.CacheConfig)
public class CacheConfig extends DynamicConfig{
    public CacheConfig(){
    }
    
    @PostConstruct
    public void init(){
        setFileName(System.getProperty(ConfigFileDict.CACHE_CONFIG_FILE,
            ConfigFileDict.DEFAULT_CACHE_CONFIG_NAME));
        init();
    }
}
