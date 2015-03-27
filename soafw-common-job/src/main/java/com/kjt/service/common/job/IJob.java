package com.kjt.service.common.job;


/**
 * 
 * @author alexzhu
 *
 * @param <T>
 * 
 */
public interface IJob<T> {

    /**
     * 获取job的名称，必须唯一
     * 
     * @return
     */
    public String getId();
    /**
     * job程序运行入口
     */
    public void start();

    /**
     * 单数据处理实现
     * 
     * @param datas
     */
    public void doProcess(T datas);

}
