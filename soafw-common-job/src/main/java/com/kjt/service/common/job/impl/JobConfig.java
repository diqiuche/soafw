package com.kjt.service.common.job.impl;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.kjt.service.common.config.PrefixPriorityConfig;
import com.kjt.service.common.config.dict.ConfigComponent;
import com.kjt.service.common.config.dict.ConfigFileTypeDict;

@Component(ConfigComponent.JobConfig)
public class JobConfig extends PrefixPriorityConfig{
    
    public JobConfig(){
    }
    
    @PostConstruct
    public void init(){
        this.setFileName(System.getProperty(JOB_CONFIG_FILE, DEFAULT_JOB_CONFIG_NAME));
        this.setType(ConfigFileTypeDict.XML);
        super.init();
    }
}
