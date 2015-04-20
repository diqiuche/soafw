package com.kjt.service.common.job.cluster.impl;

import java.util.List;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.kjt.service.common.job.DataProcessException;
import com.kjt.service.common.job.IJob;
import com.kjt.service.common.job.JobException;
import com.kjt.service.common.job.JobExecuteException;
import com.kjt.service.common.log.LogUtils;
import com.kjt.service.common.log.Logger;
import com.kjt.service.common.log.LoggerFactory;
import com.kjt.service.common.util.RequestID;

public abstract class AbsClusterJob<T> extends QuartzJobBean implements IJob<T> {
    protected JobDataMap dataMap = null;
    private String id;
    private int successed;
    private int failed;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AbsClusterJob(String id) {
        this.id = id;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        dataMap = context.getJobDetail().getJobDataMap();
        start();
    }

    final public void start() {
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

    public void onError(JobException ex) {
        if (logger.isInfoEnabled()) {
            logger.info("onError(Exception ex={}) - start", ex); //$NON-NLS-1$
        }
        throw ex;
    }

    protected void increaseErrorNum() {
        failed++;
    }

    protected void failedReset() {
        failed = 0;
    }

    protected void successedReset() {
        successed = 0;
    }

    protected void increaseSuccessNum() {
        successed++;
    }

    final public int getFailed() {
        return failed;
    }

    public void onSuccessed() {
        if (logger.isInfoEnabled()) {
            logger.info("onSuccessed()"); 
        }
    }

    final public int getSuccessed() {
        return successed;
    }

    public abstract List<T> execute();
}
