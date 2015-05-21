package com.kjt.service.common.mq.impl;

import javax.annotation.PostConstruct;

import com.kjt.service.common.config.PoolableObjDynamicConfig;
import com.kjt.service.common.config.dict.ConfigFileDict;
import com.kjt.service.common.config.dict.ConfigFileTypeDict;

public class MQConfig extends PoolableObjDynamicConfig {
    public MQConfig() {
    }

    @PostConstruct
    public void init() {
        this.setFileName(System.getProperty(DEFAULT_MQ_CONFIG_FILE, DEFAULT_MQ_CONFIG_NAME));
        this.setType(ConfigFileTypeDict.PROPERTIES);
        super.init();
        this.build(this.getConfig());
    }
}
