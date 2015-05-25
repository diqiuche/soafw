package com.kjt.service.common.jms;

import org.apache.commons.configuration.Configuration;

import com.kjt.service.common.mq.IConnector;
import com.kjt.service.common.mq.MQClient;

public class JmsMQClient extends MQClient {
    public JmsMQClient(String id) {
        super(id);
    }

    protected IConnector connector;

    @Override
    public boolean isConnected() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void connect() {
        // TODO Auto-generated method stub
    }

    @Override
    public void disconn() {
        // TODO Auto-generated method stub

    }

    @Override
    protected String configToString(Configuration config) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void build(Configuration config) {
        // TODO Auto-generated method stub

    }

}
