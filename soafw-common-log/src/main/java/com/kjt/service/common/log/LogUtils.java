package com.kjt.service.common.log;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import com.kjt.service.common.util.ContextHolder;



public class LogUtils {
	
	public static void error(Logger logger,String message){
		logger.error(ContextHolder.getReqId() + message);
	}
	
	public static void error(Logger logger,Exception ex){
		logger.error(ContextHolder.getReqId() + estacktack2Str(ex));
	}
	
	public static String estacktack2Str(Exception ex){
		PrintStream ps = null;
		try {
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			ps = new PrintStream(bao);
			ex.printStackTrace(ps);
			return bao.toString("utf-8");
		} catch (UnsupportedEncodingException e) {
			return e.getMessage();
		} finally {
			if(ps!=null){
				ps.close();
			}
		}
	}
	
	public static void trace(Logger logger,String message){
		logger.info(ContextHolder.getReqId() + message);
	}
	
	public static void timeused(Logger logger,String point,long start){
		logger.info(ContextHolder.getReqId() + point + " timeused: "+(System.currentTimeMillis()-start));
	}
}
