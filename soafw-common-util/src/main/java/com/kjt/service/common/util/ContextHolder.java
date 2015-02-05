package com.kjt.service.common.util;

/**
 * 
 * 
 */
public class ContextHolder {
	private static InheritableThreadLocal<String> reqIds = new InheritableThreadLocal<String>();
	private ContextHolder(){}
	
	public static String getReqId(){
		String id = reqIds.get();
		if(id==null){
			id = IDUtil.nextUuid();
			setReqId(id);
		}
		return id+" ";
	}
	
	public static void setReqId(String reqId){
		reqIds.set(reqId);
	}
	
	private static InheritableThreadLocal<Boolean> debug = new InheritableThreadLocal<Boolean>();
	public static void setDebug(Boolean data){
		debug.remove();
		debug.set(data);
	}
	public static Boolean getDebug(){
		return debug.get();
	}
}
