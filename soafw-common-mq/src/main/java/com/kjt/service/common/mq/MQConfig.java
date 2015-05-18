package com.kjt.service.common.mq;

import com.kjt.service.common.config.DynamicConfig;
import com.kjt.service.common.config.dict.ConfigFileDict;
import com.kjt.service.common.config.dict.ConfigFileTypeDict;

public class MQConfig extends DynamicConfig{
    public MQConfig(){
        this.setFileName(System.getProperty(ConfigFileDict.DEFAULT_MQ_CONFIG_FILE,
            ConfigFileDict.DEFAULT_MQ_CONFIG_NAME));
        this.setType(ConfigFileTypeDict.XML);
        super.init();
    }
}
