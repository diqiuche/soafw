package com.kjt.service.common.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.kjt.service.common.log.Logger;
import com.kjt.service.common.log.LoggerFactory;
import com.kjt.service.common.util.RequestID;

@Activate(group = {Constants.PROVIDER,Constants.CONSUMER})
public class ServiceFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String reqidKey = "ReqId";
    
    public ServiceFilter(){
        logger.info("ServiceFilter created");
    }
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext context = RpcContext.getContext();
        String remoteIp = context.getRemoteHost();
        boolean provider = context.isProviderSide();
        if(provider){
            String reqId = context.getAttachment(reqidKey);
            RequestID.set(reqId);
            logger.info(reqidKey+" from client@"+remoteIp);
        }
        else{
            String reqId = RequestID.get();
            context.setAttachment(reqidKey, reqId);
            logger.info(reqidKey+" to service@"+remoteIp);
        }
        Result result = invoker.invoke(invocation);
        return result;
    }

}
