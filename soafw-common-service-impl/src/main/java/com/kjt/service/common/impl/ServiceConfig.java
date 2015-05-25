package com.kjt.service.common.impl;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.kjt.service.common.config.DynamicConfig;
import com.kjt.service.common.config.dict.ConfigComponent;
import com.kjt.service.common.config.dict.ConfigFileDict;
import com.kjt.service.common.config.dict.ConfigFileTypeDict;

@Component(ConfigComponent.ServiceConfig)
public class ServiceConfig extends DynamicConfig {
    
    public ServiceConfig() {}

    @PostConstruct
    public void init() {
        this.setFileName(System.getProperty(ConfigFileDict.SERVICE_CONFIG_FILE,
                ConfigFileDict.DEFAULT_SERVICE_CONFIG_NAME));
        this.setType(ConfigFileTypeDict.XML);
        super.init();
    }
}
