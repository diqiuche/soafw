package com.kjt.service.rpc.client;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpUtil {
    public Map doGet(String url,Map<String,Object> params,ISigner signer,Integer connTimeOut, Integer readTimeOut){
        HttpClient client = null;
        return null;
    }
    public Map doPost(String url,Map<String,Object> params,ISigner signer,Integer connTimeOut, Integer readTimeOut){
        HttpClient client = new HttpClient();
        HttpMethod method = new PostMethod(url);
        method.setParams(signer.sign(params));
        try {
            client.executeMethod(method);
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args){
        
    }
}
