package com.kjt.service.common.rabbitmq;

import org.apache.commons.configuration.Configuration;

import com.kjt.service.common.mq.IMessage;
import com.kjt.service.common.mq.IPublisher;
import com.kjt.service.common.mq.ISender;

public class RabbitMQProducer extends RabbitMQClient implements ISender, IPublisher {

    public RabbitMQProducer(String id) {
        super(id);
    }

    @Override
    public void send(IMessage message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void publish(IMessage message) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected String configToString(Configuration config) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void build(Configuration config) {
        String prefix_ = this.getPrefix();
        String brokerAddressesString = config.getString(prefix_+"brokerAddressesString");
        
    }

}
