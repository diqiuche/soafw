package com.kjt.service.common.filter;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper;
import com.kjt.service.common.log.Logger;
import com.kjt.service.common.log.LoggerFactory;
import com.kjt.service.common.util.RequestID;

public class ServiceFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String reqidKey = "SvcReqId";
    
    public ServiceFilter(){
        System.out.println("ServiceFilter created");
    }
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        long start = System.currentTimeMillis();
        boolean service = RpcContext.getContext().isProviderSide();
        if(service){
            String tmpReqId = invoker.getUrl().getParameter(reqidKey);
            RequestID.set(tmpReqId);
        }
        else{
            String tmpReqId = RequestID.get();
            URL url = invoker.getUrl();
            //url.getParameters().put(reqidKey, tmpReqId);
        }
        
        String url = invoker.getUrl().toFullString();
        
        Result result = invoker.invoke(invocation);
        logger.info("invoke '"+url+"' timeused:" + (System.currentTimeMillis()-start));
        return result;
    }

}
