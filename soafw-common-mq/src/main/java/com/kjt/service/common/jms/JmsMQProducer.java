package com.kjt.service.common.jms;

import com.kjt.service.common.mq.IMessage;
import com.kjt.service.common.mq.IPublisher;
import com.kjt.service.common.mq.ISender;

public class JmsMQProducer extends JmsMQClient implements ISender, IPublisher {

    public JmsMQProducer(String id) {
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

}
