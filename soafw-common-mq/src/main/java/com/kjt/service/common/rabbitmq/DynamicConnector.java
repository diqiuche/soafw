package com.kjt.service.common.rabbitmq;

import org.apache.commons.configuration.Configuration;

import com.kjt.service.common.mq.DynamicMQClient;
import com.kjt.service.common.mq.IConnector;

public class DynamicConnector extends DynamicMQClient implements IConnector {

	@Override
	public void connect() {

	}

	@Override
	public void disconn() {

	}

	@Override
	protected String configToString(Configuration config) {
		return null;
	}

	@Override
	protected void build(Configuration config) {
		
	}

}
