package com.kjt.service.common.rabbitmq;

import com.kjt.service.common.mq.IConnector;
import com.kjt.service.common.mq.IMessage;
import com.kjt.service.common.mq.ISubscriber;

public class Subscriber implements ISubscriber {
	
	private IConnector connector = new DynamicConnector();
	
	@Override
	public void onMessage(IMessage message) {

	}

}
