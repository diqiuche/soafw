package com.kjt.service.common.rabbitmq;

import com.kjt.service.common.mq.IConnector;
import com.kjt.service.common.mq.IMessage;
import com.kjt.service.common.mq.IReceiver;

public class Receiver implements IReceiver {
	
	private IConnector connector = new DynamicConnector();

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

}
