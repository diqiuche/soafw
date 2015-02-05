package com.kjt.service.rpc.server;

import com.kjt.service.common.IService;

public interface IRPCExporter<T> {

	public IService<T> getService();
	
	public String getSvcInterface();
	
}
