package com.kjt.service.common.job;

public class JobException extends RuntimeException {
    public JobException(String message,Throwable ex){
        super(message,ex);
    }
}
