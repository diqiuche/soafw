package com.alibaba.dubbo.rpc;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Activate;

@Activate
public class DubboProtocoWapper implements Protocol {
    Protocol impl;
    @Override
    public int getDefaultPort() {
        return impl.getDefaultPort();
    }

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
        System.out.println("export ..........");
        return impl.export(invoker);
    }

    @Override
    public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {
        System.out.println("refer ..........");
        return impl.refer(type, url);
    }

    @Override
    public void destroy() {
        impl.destroy();
    }
}
