package com.kjt.service.common.impl;

import com.kjt.service.common.IService;
import com.kjt.service.common.config.DynamicConfig;
import com.kjt.service.common.config.dict.ConfigFileDict;
import com.kjt.service.common.config.dict.ConfigFileTypeDict;
import com.kjt.service.common.result.IResult;

public abstract class AbsDynamicService<T extends IResult> extends DynamicConfig implements IService<T>{
    
    public AbsDynamicService() {
        this.setFileName(System.getProperty(ConfigFileDict.SERVICE_CONFIG_FILE,
                ConfigFileDict.DEFAULT_SERVICE_CONFIG_NAME));
        this.setType(ConfigFileTypeDict.XML);
        super.init();
    }
    
}
