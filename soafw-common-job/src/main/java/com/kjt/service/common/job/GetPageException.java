package com.kjt.service.common.job;

public class GetPageException extends JobException{
    public GetPageException(Throwable ex){
        super("get page exception",ex);
    }
}
