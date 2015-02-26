package com.kjt.service.concurrent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kjt.service.common.config.utils.Executor;
import com.kjt.service.common.util.ContextHolder;
import com.kjt.service.common.util.LogUtils;

public abstract class SynMAsynBizExecutor implements Runnable {

    protected static Logger _logger = LoggerFactory.getLogger("trace");

    private String reqId = (String) ContextHolder.getReqId();
    protected String bizMethod = null;
    private Integer index = 0;
    protected Map<Integer, Object> map = null;
    private Object resultRef = null;

    public Object getResultRef() {
        resultRef = resultRef == null ? true : resultRef;
        return resultRef;
    }

    public void setResultRef(Object resultRef) {
        this.resultRef = resultRef;
    }

    /**
     * 
     * @param map
     * @param bizMethod
     * @param index >0
     */
    public SynMAsynBizExecutor(ConcurrentHashMap<Integer, Object> map, String bizMethod,
            Integer index) {
        this.map = map;
        Integer size = 1;
        if (this.map.get(0) != null) {
            size = (Integer) map.get(0);
            size = size + 1;
        }
        map.put(0, size);
        this.bizMethod = bizMethod;
        this.index = index;
        /**
         * 异步执行
         */
        Executor.execute(this);
    }

    @Override
    public void run() {
        final long start = System.currentTimeMillis();
        ContextHolder.setReqId(this.getReqId());
        try {
            execute();
        } catch (Exception ex) {
            this.setResultRef(ex);
            onError(new RuntimeException(ex));
            LogUtils.error(_logger, ex);
        } finally {
            finished();
            LogUtils.timeused(_logger, "SynMAsynBizExecutor.execute(" + bizMethod + ")", start);
        }
    }

    public abstract void execute();

    public void onError(RuntimeException ex) {

    }

    public final synchronized void finished() {
        synchronized (map) {
            map.put(index, getResultRef());
            map.notifyAll();
        }
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getReqId() {
        return reqId;
    }

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        System.setProperty("batch.asyn", "true");

        ConcurrentHashMap<Integer, Object> map = new ConcurrentHashMap<Integer, Object>();
        new SynMAsynBizExecutor(map, "test1", 1) {
            @Override
            public void execute() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {}
                this.setResultRef(getIndex());
            }
        };

        new SynMAsynBizExecutor(map, "test2", 2) {
            @Override
            public void execute() {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {}
                this.setResultRef(true);
            }
        };

        new SynMAsynBizExecutor(map, "test3", 3) {
            @Override
            public void execute() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {}
                this.setResultRef(getIndex());
            }
        };

        new SynMAsynBizExecutor(map, "test4", 4) {
            @Override
            public void execute() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
                this.setResultRef(this.getIndex());
                // throw new DomainException(123, "TEST");
            }
        };
        SynMAsynBizHelper.errorCheck(start, map);
        LogUtils.timeused(_logger, "test", start);
    }

}
