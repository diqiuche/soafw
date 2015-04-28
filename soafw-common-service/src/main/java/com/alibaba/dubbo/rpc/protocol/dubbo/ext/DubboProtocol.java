package com.alibaba.dubbo.rpc.protocol.dubbo.ext;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Protocol;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.kjt.service.common.log.Logger;
import com.kjt.service.common.log.LoggerFactory;
import com.kjt.service.common.util.RequestID;

public class DubboProtocol extends com.alibaba.dubbo.rpc.protocol.dubbo.DubboProtocol implements Protocol{
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String reqidKey = "SvcReqId";
    public DubboProtocol(){
        System.out.println("dubbo ext");
    }
    @Override
    public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {
        URL newUrl = url;
        boolean service = RpcContext.getContext().isProviderSide();
        if(service){
            String tmpReqId =url.getParameter(reqidKey);
            RequestID.set(tmpReqId);
        }
        else{
            String tmpReqId = RequestID.get();
            newUrl = url.addParameter(reqidKey, tmpReqId);
        }
        return super.refer(type, newUrl);
    }

}
