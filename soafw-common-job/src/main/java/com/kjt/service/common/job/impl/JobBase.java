package com.kjt.service.common.job.impl;

import java.lang.reflect.Field;

import javax.annotation.PostConstruct;

import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;

import com.kjt.service.common.config.IConfigChangeListener;
import com.kjt.service.common.job.IJob;
import com.kjt.service.common.job.JobException;
import com.kjt.service.common.log.Logger;
import com.kjt.service.common.log.LoggerFactory;
import com.kjt.service.common.reflection.MetaObject;
import com.kjt.service.common.reflection.factory.DefaultObjectFactory;

public abstract class JobBase<T> extends JobConfig implements IJob<T>,IConfigChangeListener{
    
    /**
     * Logger for this class
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String id;

    private int successed;

    private int failed;
    
    public JobBase() {
        this.id = this.getClass().getSimpleName();
    }

    public JobBase(String id) {
        this.id = id;
    }
    
    @PostConstruct
    public void init(){
        this.setPrefix(id);
        addChangeListenre(this);
        super.init();
    }
    
    @Override
    public String getId() {
        return id;
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
            logger.info("onSuccessed()"); //$NON-NLS-1$
        }
    }

    final public int getSuccessed() {
        return successed;
    }

    private JobDetail jobDetail;

    public void setJobDetail(JobDetail jobDetail) {
        this.jobDetail = jobDetail;
    }

    private CronTrigger trigger;

    public void setTrigger(CronTrigger trigger) {
        this.trigger = trigger;
    }

    private Scheduler scheduler;

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void configChanged() {
        String cronExpression = trigger.getCronExpression();
        String currentCronExpression = getString("CronExpression");
        if (currentCronExpression != null && currentCronExpression.trim().length() > 0
                && !currentCronExpression.equalsIgnoreCase(cronExpression)) {
            this.updateCronTriggerExp(currentCronExpression);
        }
    }

    protected void updateCronTriggerExp(String expression) {
        if (logger.isInfoEnabled()) {
            logger.info("updateCronTriggerExp(String expression={}) - start", expression); //$NON-NLS-1$
        }
        
        Field field = null;
        try {
            MetaObject metaObject = DefaultObjectFactory.getMetaObject(trigger);
            metaObject.setValue("cronEx",new CronExpression(expression));
            unregist();
            regist();
        } catch (Exception e) {
            logger.error("updateCronTriggerExp(String)", e); //$NON-NLS-1$
        }

        if (logger.isInfoEnabled()) {
            logger.info("updateCronTriggerExp(String expression={}) - end", expression); //$NON-NLS-1$
        }
    }

    private void regist() {
        if (logger.isInfoEnabled()) {
            logger.info("regist() - start"); //$NON-NLS-1$
        }

        try {
            // 触发器
            ((CronTriggerImpl) trigger).setCronExpression(getString("CronExpression"));// 触发器时间设定
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            logger.error("regist()", e); //$NON-NLS-1$

            throw new RuntimeException(e);
        }

        if (logger.isInfoEnabled()) {
            logger.info("regist() - end"); //$NON-NLS-1$
        }
    }

    /**
     * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
     * 
     * @param jobName
     */
    private void unregist() {
        if (logger.isInfoEnabled()) {
            logger.info("unregist() - start"); //$NON-NLS-1$
        }

        try {
            TriggerKey triggerKey = trigger.getKey();
            scheduler.pauseTrigger(triggerKey);// 停止触发器
            scheduler.unscheduleJob(triggerKey);// 移除触发器
            scheduler.deleteJob(jobDetail.getKey());// 删除任务
        } catch (Exception e) {
            logger.error("unregist()", e); //$NON-NLS-1$

            throw new RuntimeException(e);
        }

        if (logger.isInfoEnabled()) {
            logger.info("unregist() - end"); //$NON-NLS-1$
        }
    }

    /**
     * 创建监控线程，实现每分钟进行提交监控数据
     */
    private void createMonitor() {
        /**
         * 
         */
    }
}
