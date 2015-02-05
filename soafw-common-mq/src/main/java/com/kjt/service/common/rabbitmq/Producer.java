package com.kjt.service.common.rabbitmq;

import com.kjt.service.common.mq.IConnector;
import com.kjt.service.common.mq.IMessage;
import com.kjt.service.common.mq.IPublisher;
import com.kjt.service.common.mq.ISender;

public class Producer implements ISender, IPublisher {
	
	private IConnector connector = new DynamicConnector();

	@Override
	public void publish(IMessage message) {

	}

	@Override
	public void send(IMessage message) {

	}
	
}
