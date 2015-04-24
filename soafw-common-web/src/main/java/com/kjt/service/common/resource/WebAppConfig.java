package com.kjt.service.common.resource;

import com.kjt.service.common.config.DynamicConfig;
import com.kjt.service.common.config.dict.ConfigFileDict;
import com.kjt.service.common.config.dict.ConfigFileTypeDict;

public class WebAppConfig extends DynamicConfig {
    
    public WebAppConfig(){
        this.setFileName(System.getProperty(ConfigFileDict.WEB_CONTROLLER_CONFIG_FILE,
            ConfigFileDict.DEFAULT_WEB_CONTROLLER_CONFIG_NAME));
        this.setType(ConfigFileTypeDict.XML);
        super.init();
    }
    
}
