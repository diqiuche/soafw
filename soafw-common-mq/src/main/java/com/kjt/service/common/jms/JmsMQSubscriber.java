package com.kjt.service.common.jms;

import com.kjt.service.common.mq.IMessage;
import com.kjt.service.common.mq.ISubscriber;

public class JmsMQSubscriber extends JmsMQClient implements ISubscriber {

    public JmsMQSubscriber(String id) {
        super(id);
    }

    @Override
    public void onMessage(IMessage message) {
        // TODO Auto-generated method stub
        
    }

}
