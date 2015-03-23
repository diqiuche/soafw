package com.kjt.service.rpc.client;

import java.util.Map;

import org.apache.commons.httpclient.params.HttpMethodParams;

public interface ISigner {
    public HttpMethodParams sign(Map<String,Object> params);
}
