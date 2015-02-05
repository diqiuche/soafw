package com.kjt.service.rpc.server.hessian;

import org.springframework.remoting.rmi.RmiServiceExporter;

import com.kjt.service.common.IService;
import com.kjt.service.rpc.server.IRPCExporter;

public class MyHessianServiceExporter<T> implements IRPCExporter<T> {
	RmiServiceExporter A;
	//HessianServiceExporter b;
	@Override
	public IService<T> getService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSvcInterface() {
		// TODO Auto-generated method stub
		return null;
	}
}
