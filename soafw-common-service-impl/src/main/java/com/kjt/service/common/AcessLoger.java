package com.kjt.service.common;

import java.util.Map;

import javax.xml.ws.Response;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.alibaba.dubbo.rpc.RpcContext;
import com.kjt.service.common.aop.LoggerPoint;
import com.kjt.service.common.log.Logger;
import com.kjt.service.common.log.LoggerFactory;
import com.kjt.service.common.result.IResult;
import com.kjt.service.common.util.RequestID;
import com.kjt.service.common.util.StringUtils;
import com.kjt.service.concurrent.AsynBizExecutor;
public class AcessLoger {

    public static Object process(ProceedingJoinPoint pjp) throws Throwable{
        RequestID.set(null);
        _logger.info("start process");
        Object target = pjp.getTarget();
        String ip = RpcContext.getContext().getRemoteAddressString();
        LoggerPoint point = getLoggerPoint(pjp);
        String keys = null;
        if(point!=null){
            keys = point.key();
            if("none".equalsIgnoreCase(keys)){
                return pjp.proceed();   
            }
        }
        long time = System.currentTimeMillis();     
        Map svcDef = AliasUtil.getAlias(keys,pjp, true);
        try {
            time = System.currentTimeMillis();
            return pjp.proceed(); 
        } 
        catch(Exception ex){
            _logger.error(ex);
            throw ex;
        }
        finally {
            time = System.currentTimeMillis() - time;
            _logger.info("%s %,dms", ip+svcDef.get("className")+"."+svcDef.get("methodName")+svcDef.get("detail"),time);
            _logger.info("end process");
            RequestID.unset();
        }
    }
    
    private static boolean monitor(ProceedingJoinPoint pjp){        
        LoggerPoint loggerPoint = getLoggerPoint(pjp);
        if(loggerPoint==null){
            return false;
        }
        return true;
    }
    
    private static LoggerPoint getLoggerPoint(ProceedingJoinPoint pjp){
        return ((MethodSignature)pjp.getSignature()).getMethod().getAnnotation(LoggerPoint.class);
    }
    
    /**
     * 
     * @param pjp
     * @return
     * @throws Throwable
     */
    private static Object processAroundInternal(Map svcDef,final ProceedingJoinPoint pjp,LoggerPoint point)
            throws Throwable {
        
        long _time = System.currentTimeMillis();
        IService service = (IService) pjp.getTarget();
        String _ip = service.getRemoteHost();
        Integer _logId = 1;//logBiz.add(Log4Monitor.getClientIpAddress(resource.getHttpServletRequest()), cls, mtd, svcId, (String)svcDef.get("detail"));       
        Object retVal=null;
        Integer _code = 200;//成功
        boolean _error = true;
        try {               
            if(point!=null){
                String keys = point.key();
                if("none".equalsIgnoreCase(keys)){
                    return pjp.proceed();   
                }
            }
            retVal = pjp.proceed();
            _error = false;
            if (retVal == null
                    || !Response.class.isAssignableFrom(retVal.getClass())) {               
                return retVal;
            }
        } catch (Exception e) {
            
        }
        finally{
            
        }
        
        IResult _response = (IResult) retVal;
        
        String _resourceid = getService(pjp,service,point,(Map)svcDef.get(AliasUtil.KEYMAP_KEY));
        
        StringBuffer tmp = new StringBuffer((String)svcDef.get("className"));
        String _svc = tmp.append(".").append((String)svcDef.get("methodName")).append((String)svcDef.get("detail")).toString();
        _resourceid=_resourceid==null?"":"resourceid="+_resourceid;
        
        final String ip = _ip;
        final String svc = _svc;
        final Integer logId = _logId;
        final String resourceid = _resourceid;
        final boolean error = _error;
        final Integer code = _code;     
        _time = System.currentTimeMillis() - _time;
        final long time = _time;
        //20101223 10:42:22.683 INFO  access - PropResource.useful(uid=1,orderId=2,type="3",targetId=4) code=404 resourceid=1  timeused=69ms
        final String id = null;
        new AsynBizExecutor("logger.info"){
            public void execute() {
                String logInfo = null;  
                
                String svc_ = svc;
                if(id!=null){
                    svc_ = id+" "+svc_;
                }
                _logger.info("%s %s code=%d %s %s timeused=%,dms",ip,svc_,code,resourceid,logInfo,time);
            };
        };
        return _response;
    }
    
    private static String getService(ProceedingJoinPoint pjp,IService service,LoggerPoint point,Map<String,String> keyMaps){
        _logger.debug("keyMaps"+keyMaps);       
        if(point!=null){
            String keys = point.key();
            if(StringUtils.isEmpty(keys))
                return null;
            if(keys.trim().indexOf("result")==0){
                Object key = null;//Holder.get();
                if(key==null)
                    return null;
                return String.valueOf(key);
            }           
            int size = keyMaps ==null?0:keyMaps.size();
            if(size>0){
                String[] keys_ = new String[size];
                keyMaps.keySet().toArray(keys_);
                StringBuffer resourceIds = new StringBuffer();
                boolean first = true;
                for(int i=0;i<size;i++){
                    String value = keyMaps.get(keys_[i]);
                    if(!StringUtils.isEmpty(value)){
                        if(!first){
                            resourceIds.append(";");
                        }
                        resourceIds.append(value);
                        first = false;
                    }
                }
                return resourceIds.toString();
            }
        }
        return null;
    }
    
    private transient static Logger _logger = LoggerFactory.getLogger("trace");
}
