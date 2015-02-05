package com.kjt.service.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kjt.service.common.config.utils.Executor;
import com.kjt.service.common.util.ContextHolder;
import com.kjt.service.common.util.LogUtils;

/**
 * 高性能控制器
 * @author Administrator
 *
 */
public abstract class AsynBizExecutor implements Runnable{
	
	protected Logger _logger = LoggerFactory.getLogger("trace");
	private String reqId = (String)ContextHolder.getReqId();
	private String biz="";
	
	public AsynBizExecutor(String biz){
		this.biz = biz;
		start();
	}
	private void start(){
		Executor.execute(this);
	}
	
	@Override
	public void run() {
		final long start = System.currentTimeMillis();
		ContextHolder.setReqId(this.getReqId());
		try{
			execute();
		}
		catch(Exception ex){
			LogUtils.error(_logger, ex);
			onErrors(new RuntimeException(ex));
		}
		finally{
			LogUtils.timeused(_logger,"AsynBizExecutor.execute("+biz+")", start);
		}		
	}
	
	abstract public void execute();
	
	public void onErrors(RuntimeException ex){
		
	}
	
	public String getReqId(){
		return reqId;
	}
	
}
