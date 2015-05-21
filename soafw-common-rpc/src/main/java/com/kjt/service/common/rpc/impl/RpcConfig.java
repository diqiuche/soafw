package com.kjt.service.common.rpc.impl;

import javax.annotation.PostConstruct;

import com.kjt.service.common.config.DynamicConfig;
import com.kjt.service.common.config.dict.ConfigFileDict;
import com.kjt.service.common.config.dict.ConfigFileTypeDict;

public class RpcConfig extends DynamicConfig{
    public RpcConfig(){
    }
    @PostConstruct
    public void init(){
        this.setFileName(System.getProperty(ConfigFileDict.DEFAULT_RPC_CONFIG_FILE,
            ConfigFileDict.DEFAULT_RPC_CONFIG_NAME));
        this.setType(ConfigFileTypeDict.XML);
        super.init();
    }
}
