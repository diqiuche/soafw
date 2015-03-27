package com.kjt.service.concurrent;



import com.kjt.service.common.config.utils.Executor;
import com.kjt.service.common.log.LogUtils;
import com.kjt.service.common.log.Logger;
import com.kjt.service.common.log.LoggerFactory;
import com.kjt.service.common.util.RequestID;

/**
 * 高性能控制器
 * 
 * @author Administrator
 *
 */
public abstract class AsynBizExecutor implements Runnable {

    protected Logger logger = LoggerFactory.getLogger("trace");
    private String reqId = (String) RequestID.get();
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
        RequestID.set(this.getReqId());
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
