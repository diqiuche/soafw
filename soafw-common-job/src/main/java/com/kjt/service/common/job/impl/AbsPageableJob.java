package com.kjt.service.common.job.impl;

import java.lang.reflect.Field;
import java.util.List;

import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;

import com.kjt.service.common.config.IConfigListener;
import com.kjt.service.common.config.PoolableObjDynamicConfig;
import com.kjt.service.common.config.dict.ConfigFileTypeDict;
import com.kjt.service.common.job.IPageableJob;
import com.kjt.service.common.log.Logger;
import com.kjt.service.common.log.LoggerFactory;
import com.kjt.service.common.util.RequestID;

/**
 * 所有job必须实现该类<br>
 * 该类有个线程负责提交监控数据<br>
 * 该线程会提交正常量、出错量、性能、及心跳数据到监控中心
 * 
 * @author alexzhu
 *
 * @param <T>
 */
public abstract class AbsPageableJob<T> extends PoolableObjDynamicConfig
        implements
            IPageableJob<T>,
            IConfigListener {
    /**
     * Logger for this class
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String id;
    private int successed;
    private int failed;

    public AbsPageableJob(String id) {
        this.id = id;
        this.setPrefix(id);
        this.setFileName(System.getProperty(JOB_CONFIG_FILE, DEFAULT_JOB_CONFIG_NAME));
        this.setType(ConfigFileTypeDict.XML);
        super.init();
        this.build(this.getConfig());
        createMonitor();
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
    public void unregist() {
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

    @Override
    public String getId() {
        return id;
    }

    @Override
    final public void onError(Exception ex) {
        failed++;
    }

    @Override
    final public void onSuccessed() {
        successed++;
    }

    public void start() {
        if (logger.isInfoEnabled()) {
            logger.info("start() - start"); //$NON-NLS-1$
        }
        RequestID.set(null);
        int pages = this.getPages();
        if (logger.isInfoEnabled()) {
            logger.info("pages() ={} ", pages); //$NON-NLS-1$
        }
        for (int i = 0; i < pages; i++) {
            pageProcess();
        }
        if (logger.isInfoEnabled()) {
            logger.info("start() - end"); //$NON-NLS-1$
        }
    }

    private void pageProcess() {
        if (logger.isInfoEnabled()) {
            logger.info("pageProcess() - start"); //$NON-NLS-1$
        }

        List<T> pageDatas = this.pageLoad();
        this.pageDataProcess(pageDatas);

        if (logger.isInfoEnabled()) {
            logger.info("pageProcess() - end"); //$NON-NLS-1$
        }
    }

    private void pageDataProcess(List<T> datas) {

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
            logger.info(
                    "doProcess(List<T> total={},successed={},failed=) - end", total, successed, failed); //$NON-NLS-1$
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

}
