package com.kjt.service.common.rabbitmq;

import com.kjt.service.common.mq.IMessage;
import com.kjt.service.common.mq.IPublisher;
import com.kjt.service.common.mq.ISender;

public class RabbitMQProducer extends RabbitMQClient implements ISender, IPublisher {

    @Override
    public void send(IMessage message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void publish(IMessage message) {
        // TODO Auto-generated method stub
        
    }

}
