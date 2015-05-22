package com.kjt.service.common.job.impl;

import javax.annotation.PostConstruct;

import com.kjt.service.common.config.PoolableObjDynamicConfig;
import com.kjt.service.common.config.dict.ConfigFileTypeDict;

public class JobConfig extends PoolableObjDynamicConfig{
    
    public JobConfig(){
    }
    
    @PostConstruct
    public void init(){
        this.setFileName(System.getProperty(JOB_CONFIG_FILE, DEFAULT_JOB_CONFIG_NAME));
        this.setType(ConfigFileTypeDict.XML);
        super.init();
    }
}
