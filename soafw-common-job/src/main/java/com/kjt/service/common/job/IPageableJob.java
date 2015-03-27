package com.kjt.service.common.job;

import java.util.List;

/**
 * 
 * @author alexzhu
 *
 * @param <T>
 */
public interface IPageableJob<T> extends IJob<T>{

    /**
     * 发生异常调用该类
     * 
     * @param ex
     */
    public void onError(Exception ex);

    /**
     * 成功是调用该方法
     */
    public void onSuccessed();
    /**
     * 获取总页数
     * @return
     */
    public int getPages();
    /**
     * 实现job的执行
     * eg:调用service获取数据、或者解析文件读取数据
     */
    public List<T> pageLoad();

}
