package com.kjt.service.common.web.impl;

import javax.annotation.Resource;

import com.kjt.service.common.config.dict.ConfigComponent;
import com.kjt.service.common.resource.IResource;


public abstract class AbsResource implements IResource {
    
    @Resource(name=ConfigComponent.WebAppConfig)
    protected WebAppConfig config;
}
