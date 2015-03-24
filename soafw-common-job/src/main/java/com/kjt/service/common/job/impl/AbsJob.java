package com.kjt.service.common.job.impl;

import java.lang.reflect.Field;
import java.util.List;

import org.quartz.CronExpression;
import org.quartz.CronTrigger;

import com.kjt.service.common.config.IConfigListener;
import com.kjt.service.common.config.PoolableObjDynamicConfig;
import com.kjt.service.common.config.dict.ConfigFileTypeDict;
import com.kjt.service.common.job.IJob;
import com.kjt.service.common.job.IScheduler;
import com.kjt.service.common.job.ITrigger;

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
            ITrigger,
            IScheduler,
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

    @Override
    public IJob getJobDetail() {
        return this;
    }


    @Override
    public void doStart() {

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

    private CronTrigger trigger;

    public void setTrigger(CronTrigger trigger) {
        this.trigger = trigger;
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
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
