package com.kjt.service.common.rabbitmq;

import java.util.Properties;

import com.kjt.service.common.mq.IMessage;

public class RabbitMqMessage implements IMessage{
    private byte[] body = null;
    public RabbitMqMessage(byte[] body){
        this.body = body;
    }
    @Override
    public byte[] getBody() {
        // TODO Auto-generated method stub
        return body;
    }

    @Override
    public String getContentType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Properties getProperty() {
        // TODO Auto-generated method stub
        return null;
    }

}
