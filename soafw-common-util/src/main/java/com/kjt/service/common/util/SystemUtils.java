package com.kjt.service.common.util;

public class SystemUtils {

	/**
	 * 获取当前应用的进程id号
	 * 
	 * @return
	 */
	public static String getProcessId() {
		return java.lang.management.ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
	}
}
