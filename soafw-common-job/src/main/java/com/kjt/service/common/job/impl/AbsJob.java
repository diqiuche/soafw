package com.kjt.service.common.job.impl;

import java.util.List;

import com.kjt.service.common.config.IConfigListener;
import com.kjt.service.common.job.DataProcessException;
import com.kjt.service.common.job.IJob;
import com.kjt.service.common.job.INormalJob;
import com.kjt.service.common.job.JobExecuteException;
import com.kjt.service.common.log.LogUtils;
import com.kjt.service.common.util.RequestID;

/**
 * 所有除分页job之外的job必须实现该类<br>
 * 该类有个线程负责提交监控数据<br>
 * 该线程会提交正常量、出错量、性能、及心跳数据到监控中心
 * 
 * @author alexzhu
 *
 * @param <T>
 */
public abstract class AbsJob<T> extends JobBase<T> implements INormalJob<T> {

    public AbsJob() {
        super();

    }

    public AbsJob(String id) {
        super(id);
    }


    synchronized final public void start() {
        if (logger.isInfoEnabled()) {
            logger.info("start() - start"); //$NON-NLS-1$
        }

        List<T> datas = null;
        long start = System.currentTimeMillis();
        long tmpStart = start;
        int total = 0;
        try {
            RequestID.set(null);
            datas = execute();
            if (logger.isInfoEnabled()) {
                LogUtils.timeused(logger, "execute", tmpStart);
            }
            total = datas == null ? 0 : datas.size();
            for (int i = 0; i < total; i++) {
                T data = datas.get(i);
                try {
                    tmpStart = System.currentTimeMillis();
                    this.doProcess(data);
                    if (logger.isInfoEnabled()) {
                        LogUtils.timeused(logger, "doProcess", tmpStart);
                    }
                    this.increaseSuccessNum();
                } catch (Exception ex) {
                    logger.error("start()", ex); //$NON-NLS-1$
                    this.increaseErrorNum();
                    this.logger.error("error process: " + data.toString());
                    this.onError(new DataProcessException(ex));
                }
            }
            this.onSuccessed();

        } catch (DataProcessException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("start()", ex); //$NON-NLS-1$

            this.onError(new JobExecuteException(ex));
        } finally {
            if (logger.isInfoEnabled()) {
                LogUtils.timeused(logger, "doProcess", start);
            }
            logger.info("start() - end total={},success={},failed={}", total, this.getSuccessed(),
                    this.getFailed());
            if (logger.isInfoEnabled()) {
                logger.info("start() - end"); //$NON-NLS-1$
            }
        }
    }
}
