package com.kjt.service.common.filter;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.kjt.service.common.log.Logger;
import com.kjt.service.common.log.LoggerFactory;

public class ServiceFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String reqidKey = "SvcReqId";
    
    public ServiceFilter(){
        System.out.println("ServiceFilter created");
    }
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result = invoker.invoke(invocation);
        return result;
    }

}
