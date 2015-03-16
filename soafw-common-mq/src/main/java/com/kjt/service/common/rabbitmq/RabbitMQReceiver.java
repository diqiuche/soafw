package com.kjt.service.common.rabbitmq;

import org.apache.catalina.connector.Connector;
import org.apache.commons.configuration.Configuration;

import com.kjt.service.common.mq.IMessage;
import com.kjt.service.common.mq.IReceiver;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQReceiver extends RabbitMQClient implements IReceiver {

    @Override
    public IMessage receive() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IMessage receive(long timeout) {
        // TODO Auto-generated method stub
        return null;
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
        String queue = config.getString(prefix_ + "queue");
        String receiveRoutingKey = config.getString(prefix_ + "receiveRoutingKey");
//        MessageReceiver receive = new MessageReceiver();
//        ConnectionFactory fac = new ConnectionFactory(new ConnectionParameters());
//        Connector c = new Connector();
//        c.setConnectionFactory(fac);
//        receive.setConnector(c);
//
//        c.setBrokerAddressesString(brokerAddressesString);
//        receive.setDurable(durable);
//        receive.setAutoDelete(autoDelete);
//        receive.setQueueName(queueName);
//        receive.setRoutingPattern(routingPattern);
    }

}
