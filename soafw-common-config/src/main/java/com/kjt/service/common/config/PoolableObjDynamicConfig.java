package com.kjt.service.common.config;

import java.util.Iterator;

import org.apache.commons.configuration.Configuration;

import com.kjt.service.common.util.MD5Util;
import com.kjt.service.common.util.StringUtils;

public abstract class PoolableObjDynamicConfig extends DynamicConfig {
	/**
	 * Logger for this class
	 */
	private String prefix;

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getPrefix(){
		if (logger.isDebugEnabled()) {
			logger.debug("getPrefix() - start"); //$NON-NLS-1$
		}

		String prefix_ = prefix;
		if (!StringUtils.isEmpty(prefix) && !prefix.endsWith(".")) {
			prefix_ = prefix + ".";
		}
		else{
			prefix_="";
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getPrefix() - end - return value={}", prefix_); //$NON-NLS-1$
		}
		return prefix_;
	}

}
