package com.kjt.service.concurrent;



import com.kjt.service.common.config.utils.Executor;
import com.kjt.service.common.log.LogUtils;
import com.kjt.service.common.log.Logger;
import com.kjt.service.common.log.LoggerFactory;
import com.kjt.service.common.util.ContextHolder;

/**
 * 高性能控制器
 * 
 * @author Administrator
 *
 */
public abstract class AsynBizExecutor implements Runnable {

    protected Logger logger = LoggerFactory.getLogger("trace");
    private String reqId = (String) ContextHolder.getReqId();
    private String biz = "";

    public AsynBizExecutor(String biz) {
        this.biz = biz;
        start();
    }

    private void start() {
        Executor.execute(this);
    }

    @Override
    public void run() {
        final long start = System.currentTimeMillis();
        ContextHolder.setReqId(this.getReqId());
        try {
            execute();
        } catch (Exception ex) {
            LogUtils.error(logger, ex);
            onErrors(new RuntimeException(ex));
        } finally {
            LogUtils.timeused(logger, "AsynBizExecutor.execute(" + biz + ")", start);
        }
    }

    public abstract void execute();

    public void onErrors(RuntimeException ex) {

    }

    public String getReqId() {
        return reqId;
    }

}
