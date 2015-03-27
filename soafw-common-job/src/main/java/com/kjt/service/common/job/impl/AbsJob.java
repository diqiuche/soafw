package com.kjt.service.common.job.impl;

import java.util.List;

import com.kjt.service.common.job.IJob;
import com.kjt.service.common.util.RequestID;

/**
 * 所有job必须实现该类<br>
 * 该类有个线程负责提交监控数据<br>
 * 该线程会提交正常量、出错量、性能、及心跳数据到监控中心
 * 
 * @author alexzhu
 *
 * @param <T>
 * @deprecated
 */
public abstract class AbsJob<T> extends AbsDynamicJob<T> implements IJob<T>{

    public AbsJob(String id) {
        super(id);
    }
    
    final public void start(){
        if (logger.isInfoEnabled()) {
            logger.info("start() - start"); //$NON-NLS-1$
        }

        RequestID.set(null);
        execute();

        if (logger.isInfoEnabled()) {
            logger.info("start() - end"); //$NON-NLS-1$
        }
    }

    final public void doProcess(List<T> datas) {

        int total = datas == null ? 0 : datas.size();
        
        if (logger.isInfoEnabled()) {
            logger.info("doProcess(List<T> datas.size={}) - start", total); //$NON-NLS-1$
        }

        for (int i = 0; i < total; i++) {
            try {
                doProcess(datas.get(i));
                this.onSuccessed();
            } catch (Exception ex) {
                logger.error("doProcess(List<T>)", ex); //$NON-NLS-1$
                this.onError(ex);
            }
        }

        if (logger.isInfoEnabled()) {
            logger.info("doProcess(List<T> total={},successed={},failed=) - end", total,this.getSuccessed(),this.getFailed()); //$NON-NLS-1$
        }
    }

    /**
     * 实现job的执行
     * eg:调用service获取数据
     */
    public abstract void execute();
}
