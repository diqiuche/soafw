package com.kjt.service.common.mq.impl;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.kjt.service.common.config.PrefixPriorityConfig;
import com.kjt.service.common.config.dict.ConfigComponent;
import com.kjt.service.common.config.dict.ConfigFileTypeDict;

@Component(ConfigComponent.MQConfig)
public class MQConfig extends PrefixPriorityConfig {
    public MQConfig() {}

    @PostConstruct
    public void init() {
        this.setFileName(System.getProperty(DEFAULT_MQ_CONFIG_FILE, DEFAULT_MQ_CONFIG_NAME));
        this.setType(ConfigFileTypeDict.PROPERTIES);
        super.init();
    }
}
