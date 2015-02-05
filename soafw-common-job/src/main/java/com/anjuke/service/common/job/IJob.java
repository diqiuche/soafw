package com.anjuke.service.common.job;

import java.util.List;

/**
 * 
 * @author alexzhu
 *
 * @param <T>
 */
public interface IJob<T> {
	
	/**
	 * 获取job的名称，必须唯一
	 * @return
	 */
	public String getId();
	/**
	 * 发生异常调用该类
	 * @param ex
	 */
	public void onError(Exception ex);
	/**
	 * 成功是调用该方法
	 */
	public void onSuccessed();
	/**
	 * 批处理实现
	 * @param datas
	 */
	public void doProcess(List<T> datas);
	
}
