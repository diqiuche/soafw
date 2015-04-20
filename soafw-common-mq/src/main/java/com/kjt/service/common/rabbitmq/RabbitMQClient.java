package com.kjt.service.common.rabbitmq;

import java.io.IOException;

import org.apache.commons.configuration.Configuration;

import com.kjt.service.common.mq.MQClient;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public abstract class RabbitMQClient extends MQClient {

    public static final Address[] DEFAULT_BROKER_ADDRESS = new Address[] {new Address("127.0.0.1",
            5672)};
    public static final Class<ConnectionFactory> DEFAULT_CONNECTION_FACTORY_CLASS =
            ConnectionFactory.class;
    
    protected ConnectionFactory connectionFactory;
    
    protected Connection connection;

    protected Channel channel;

    public boolean isConnected() {
        if (connection == null || channel == null) {
            return false;
        }
        return channel.isOpen();
    }

    @Override
    public void connect() {
        disconn();
        try {
            connection = connectionFactory.newConnection(DEFAULT_BROKER_ADDRESS);//"_brokerAddresses");
            channel = connection.createChannel();
        } catch (IOException e) {
            disconn();
            connection = null;
        }
    }

    @Override
    public void disconn() {
        if (channel != null) {
            try {
                channel.close();
            } catch (Throwable e) {} finally {
                channel = null;
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (Throwable e) {} finally {
                connection = null;
            }
        }
    }
}
