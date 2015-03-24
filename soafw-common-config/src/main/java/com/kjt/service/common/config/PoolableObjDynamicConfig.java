package com.kjt.service.common.config;

import java.util.Iterator;

import org.apache.commons.configuration.Configuration;

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
	
	protected String configToString(Configuration config) {
        if (config == null) {
            return "";
        }
        String prefix = this.getPrefix();
        Iterator<String> keys = config.getKeys();
        if (keys == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        while (keys.hasNext()) {
            String key = keys.next();
            if(key.startsWith(prefix)){
                sb.append(key + "=" + config.getString(key) + "\n");
            }
        }
        return sb.toString();

    }

}
