package com.kjt.service.common.job.impl;

import java.util.List;

import com.kjt.service.common.job.IPageableJob;
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
public abstract class AbsPageableJob<T> extends AbsDynamicJob<T> implements IPageableJob<T> {

    public AbsPageableJob(String id) {
        super(id);
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
                    "doProcess(List<T> total={},successed={},failed=) - end", total, this.getSuccessed(), this.getFailed()); //$NON-NLS-1$
        }
    }
}
