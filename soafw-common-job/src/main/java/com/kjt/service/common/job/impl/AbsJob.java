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
import com.kjt.service.common.job.IJob;

/**
 * 所有job必须实现该类<br>
 * 该类有个线程负责提交监控数据<br>
 * 该线程会提交正常量、出错量、性能、及心跳数据到监控中心
 * 
 * @author alexzhu
 *
 * @param <T>
 */
public abstract class AbsJob<T> extends PoolableObjDynamicConfig
        implements 
            IJob<T>,
            IConfigListener {
    private String id;
    private int successed;
    private int failed;

    public AbsJob(String id) {
        this.id = id;
        this.setPrefix(id);
        this.setFileName(System.getProperty(JOB_CONFIG_FILE, DEFAULT_JOB_CONFIG_NAME));
        this.setType(ConfigFileTypeDict.XML);
        super.init();
        this.build(this.getConfig());
        createMonitor();
    }
    
    private void regist(){
        try {
            // 触发器  
            ((CronTriggerImpl)trigger).setCronExpression(this.getString(this.getPrefix() + "CronExpression"));// 触发器时间设定  
            scheduler.scheduleJob(jobDetail, trigger);  
            // 启动  
            if (!scheduler.isShutdown()){  
                scheduler.start();  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
    
    /** 
     * 移除一个任务(使用默认的任务组名，触发器名，触发器组名) 
     * 
     * @param jobName 
     */  
    public void unregist() {  
        try {  
            TriggerKey triggerKey = trigger.getKey();
            scheduler.pauseTrigger(triggerKey);// 停止触发器  
            scheduler.unscheduleJob(triggerKey);// 移除触发器  
            scheduler.deleteJob(jobDetail.getKey());// 删除任务  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
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

    @Override
    final public void doProcess(List<T> datas) {

        int total = datas == null ? 0 : datas.size();

        for (int i = 0; i < total; i++) {
            try {
                doProcess(datas.get(i));
                this.onSuccessed();
            } catch (Exception ex) {
                this.onError(ex);
            }
        }
    }

    /**
     * 单数据处理
     * 
     * @param datas
     */
    protected abstract void doProcess(T datas);

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
        Field field = null;
        try {
            System.out.println("before: " + trigger.getCronExpression());
            Class cls = trigger.getClass();
            field = cls.getDeclaredField("cronEx");
            field.setAccessible(true);
            field.set(trigger, new CronExpression(expression));
            System.out.println("after: " + trigger.getCronExpression());
            unregist();
            regist();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
