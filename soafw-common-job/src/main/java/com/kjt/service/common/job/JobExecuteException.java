package com.kjt.service.common.job;

public class JobExecuteException extends JobException{
    public JobExecuteException(Throwable ex){
        super("job execute exception @ ",ex);
    }
}
