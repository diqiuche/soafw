package com.kjt.service.common.job.impl;

import java.lang.reflect.Field;

import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;

import com.kjt.service.common.config.IConfigListener;
import com.kjt.service.common.config.PoolableObjDynamicConfig;
import com.kjt.service.common.config.dict.ConfigFileTypeDict;
import com.kjt.service.common.job.IJob;
import com.kjt.service.common.log.Logger;
import com.kjt.service.common.log.LoggerFactory;

abstract class AbsDynamicJob<T> extends PoolableObjDynamicConfig
        implements
            IJob<T>,
            IConfigListener {

    /**
     * Logger for this class
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String id;

    private int successed;

    private int failed;

    public AbsDynamicJob(String id) {
        this.id = id;
        this.setPrefix(id);
        this.setFileName(System.getProperty(JOB_CONFIG_FILE, DEFAULT_JOB_CONFIG_NAME));
        this.setType(ConfigFileTypeDict.XML);
        super.init();
        this.build(this.getConfig());
        createMonitor();
    }

    @Override
    public String getId() {
        return id;
    }

    public void onError(Exception ex) {
        throw new RuntimeException(ex);
    }
    
    protected void increaseErrorNum(){
        failed++;
    }
    
    protected void increaseSuccessNum(){
        successed++;
    }

    final public int getFailed() {
        return failed;
    }

    public void onSuccessed() {
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

    protected void notifyChanged() {
        String cronExpression = trigger.getCronExpression();
        String currentCronExpression = this.getString(this.getPrefix() + "CronExpression");
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
            Class cls = trigger.getClass();
            field = cls.getDeclaredField("cronEx");
            field.setAccessible(true);
            field.set(trigger, new CronExpression(expression));
            unregist();
            regist();
        } catch (NoSuchFieldException e) {
            logger.error("updateCronTriggerExp(String)", e); //$NON-NLS-1$
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
            ((CronTriggerImpl) trigger).setCronExpression(this.getString(this.getPrefix()
                    + "CronExpression"));// 触发器时间设定
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
