package com.kjt.service.common.mq;

public interface ISubscriber {
	
	public void onMessage(IMessage message);
	
}
