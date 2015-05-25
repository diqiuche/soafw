package com.kjt.service.common.mq;

import javax.annotation.PostConstruct;

import com.kjt.service.common.mq.impl.MQConfig;

public abstract class MQClient extends MQConfig implements IConnector{
    private String id;
    public MQClient(String id){
        this.id = id;
    }
    
    @PostConstruct
    public void init(){
        this.setPrefix(id);
        super.init();
    }
    
}
