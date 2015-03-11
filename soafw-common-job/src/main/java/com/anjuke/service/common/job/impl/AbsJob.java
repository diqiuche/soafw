package com.anjuke.service.common.job.impl;

import java.util.List;

import org.apache.commons.configuration.Configuration;

import com.anjuke.service.common.job.IJob;
import com.anjuke.service.common.job.IScheduler;
import com.anjuke.service.common.job.ITrigger;
import com.kjt.service.common.config.DynamicConfig;
import com.kjt.service.common.config.dict.ConfigFileDict;

/**
 * 所有job必须实现该类<br>
 * 该类有个线程负责提交监控数据<br>
 * 该线程会提交正常量、出错量、性能、及心跳数据到监控中心
 * 
 * @author alexzhu
 *
 * @param <T>
 */
public abstract class AbsJob<T> implements IJob<T>,ITrigger,IScheduler {
    
    private static DynamicConfig config = new DynamicConfig();

    static {
        config.setFileName(System.getProperty(ConfigFileDict.JOB_CONFIG_FILE,
                ConfigFileDict.DEFAULT_JOB_CONFIG_NAME));
        config.init();
    }
    
    /**
     * 获取数据访问层acc.xml配置信息
     * 
     * @return
     */
    protected Configuration getConfig() {
        return config;
    }
    
	private String cronExpression;
	private String id;
	private int successed;
	private int failed;

	public AbsJob(String id) {
		this.id = id;
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
	
	public void setCronExpression(String cronExpression){
		this.cronExpression = cronExpression;
	}
	
	@Override
	public String getCronExpression() {
		return cronExpression;
	}
	
	protected void init(){
		
	}
	
	@Override
	public void doStart() {
		init();
	}
	/**
	 * 单数据处理
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
}
