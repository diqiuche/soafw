package com.kjt.service.common.rabbitmq;

import org.apache.commons.configuration.Configuration;

import com.kjt.service.common.mq.IMessage;
import com.kjt.service.common.mq.ISubscriber;

public class RabbitMQSubscriber extends RabbitMQClient implements ISubscriber {

    @Override
    public void onMessage(IMessage message) {
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
        String brokerAddressesString = config.getString(prefix_ + "brokerAddressesString");

    }
}
