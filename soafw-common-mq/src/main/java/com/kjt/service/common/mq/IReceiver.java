package com.kjt.service.common.mq;

public interface IReceiver {

	public IMessage receive();

	public IMessage receive(long timeout);
}
