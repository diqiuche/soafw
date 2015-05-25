package com.kjt.service.common.web.impl;

import org.springframework.stereotype.Component;

import com.kjt.service.common.config.DynamicConfig;
import com.kjt.service.common.config.dict.ConfigComponent;
import com.kjt.service.common.config.dict.ConfigFileDict;
import com.kjt.service.common.config.dict.ConfigFileTypeDict;

@Component(ConfigComponent.WebAppConfig)
public class WebAppConfig extends DynamicConfig {
    
    public WebAppConfig(){
    }
    
    public WebAppConfig(boolean delimiterParsingDisabled){
    }
    
    public void init(){
        this.setFileName(System.getProperty(ConfigFileDict.WEB_CONTROLLER_CONFIG_FILE,
            ConfigFileDict.DEFAULT_WEB_CONTROLLER_CONFIG_NAME));
        this.setType(ConfigFileTypeDict.XML);
        super.init();
    }
    
}
