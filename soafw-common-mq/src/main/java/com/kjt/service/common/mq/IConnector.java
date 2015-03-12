package com.kjt.service.common.mq;


public interface IConnector {
	
    public boolean isConnected();
    
	public void connect();

	public void disconn();
}
