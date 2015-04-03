package com.kjt.service.common.job;

public class DataProcessException extends JobException{
    public DataProcessException(Throwable ex){
        super("data process exception",ex);
    }
}
