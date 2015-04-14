package com.kjt.service.common.job.impl;

import java.util.List;

import com.kjt.service.common.job.DataProcessException;
import com.kjt.service.common.job.GetPageException;
import com.kjt.service.common.job.IPageableJob;
import com.kjt.service.common.job.PageLoadException;
import com.kjt.service.common.log.LogUtils;
import com.kjt.service.common.util.RequestID;

/**
 * 所有分页处理job必须实现该类<br>
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

    private int pageIdx=1;
    /**
     * 获取 页面序号，从1开始
     * @return
     */
    public int getPageIdx(){
        return pageIdx;
    }
    private int pageSize=50;
    public void setPageSize(int pageSize){
        this.pageSize = pageSize;
    }
    
    public int getPageSize(){
        return pageSize;
    }
    
    private boolean lastedpage=false;

    synchronized final public void start() {
        if (logger.isInfoEnabled()) {
            logger.info("start() - start"); //$NON-NLS-1$
        }

        pageIdx = 1;
        lastedpage = false;
        this.failedReset();
        this.successedReset();
        RequestID.set(null);
        long start = System.currentTimeMillis();
        long tmpStart = start;
        int pages = 0;
        try {
            pages = this.getPages();
            if (logger.isInfoEnabled()) {
                LogUtils.timeused(logger, "execute", tmpStart);
            }
            for (int i = 0; !lastedpage && i < pages; i++) {
                pageProcess();
                pageIdx++;
            }
            if(lastedpage){
                logger.info("lastedpage: "+lastedpage);
            }
            this.onSuccessed();
        } catch (PageLoadException ex) {
            throw ex;
        } catch (DataProcessException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("start()", ex); //$NON-NLS-1$

            this.onError(new GetPageException(ex));
        } finally {
            if (logger.isInfoEnabled()) {
                LogUtils.timeused(logger, "start", start);
            }
            logger.info(
                    "start() - end totalPage=%d,pageProcessed=%d,total=%d,success=%d,failed=%d",
                    pages, pageIdx-1, (this.getSuccessed() + this.getFailed()),
                    this.getSuccessed(), this.getFailed());
        }
    }

    private void pageProcess() {
        List<T> pageDatas = null;
        long start = System.currentTimeMillis();
        long tmpStart = start;
        try {
            pageDatas = this.pageLoad();
            if (logger.isInfoEnabled()) {
                LogUtils.timeused(logger, "pageLoad", tmpStart);
            }
            int size = pageDatas.size();
            
            this.pageDataProcess(pageDatas);
            
            if(size<this.getPageSize()){
                lastedpage=true;
            }
        } catch (DataProcessException ex) {
            throw ex;
        } catch (Exception ex) {
            this.onError(new PageLoadException(ex));
        } finally {
            if (logger.isInfoEnabled()) {
                LogUtils.timeused(logger, "pageProcess", tmpStart);
            }
        }
    }

    private void pageDataProcess(List<T> datas) {

        int total = datas == null ? 0 : datas.size();
        long start = System.currentTimeMillis();
        long tmpStart = start;
        try {
            for (int i = 0; i < total; i++) {
                T data = datas.get(i);
                try {
                    tmpStart = System.currentTimeMillis();
                    doProcess(data);
                    this.increaseSuccessNum();
                    if (logger.isInfoEnabled()) {
                        LogUtils.timeused(logger, "doProcess", tmpStart);
                    }
                } catch (Exception ex) {
                    this.increaseErrorNum();
                    if (logger.isInfoEnabled()) {
                        LogUtils.timeused(logger, "doProcess", tmpStart);
                    }
                    this.logger.error("error process: "+data.toString());
                    this.onError(new DataProcessException(ex));
                }
            }
        } catch (DataProcessException ex) {
            throw ex;
        } finally {
            if (logger.isInfoEnabled()) {
                LogUtils.timeused(logger, "pageDataProcess", start);
            }
        }
    }
}
