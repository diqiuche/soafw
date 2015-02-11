package com.kjt.service.common.config;

import org.apache.commons.configuration.Configuration;

import com.kjt.service.common.log.Logger;
import com.kjt.service.common.log.LoggerFactory;
import com.kjt.service.common.util.MD5Util;
import com.kjt.service.common.util.StringUtils;

public abstract class PoolableObjDynamicConfig extends DynamicConfig {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(PoolableObjDynamicConfig.class);
	
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
	
	@Override
	public final synchronized void onUpdate(Configuration config) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("onUpdate() - start"); //$NON-NLS-1$
		}

		
		String old_ = this.getConfigMd5Sig();
		
		String new_ = MD5Util.md5Hex(this.configToString(config));
		/**
		 * 当前配置项没有变化
		 */
		if(old_.equals(new_)){
			if (logger.isDebugEnabled()) {
				logger.debug("onUpdate(当前配置项没有变化) - end"); //$NON-NLS-1$
			}
			return;
		}
		String oldStr = this.configToString(this.getConfig());
		String newStr = this.configToString(config);
		/**
		 * 配置变化
		 */
		build(config);
		/**
		 * 更新老配置
		 */
		super.onUpdate(config);
		
		if (logger.isInfoEnabled()) {
			logger.info("onUpdate(String old={}, Integer new={}) - end", oldStr,newStr); //$NON-NLS-1$
		}
	}
	
	private String getConfigMd5Sig() {

		String returnString = MD5Util.md5Hex(this.configToString(this.getConfig()));
		
		return returnString;
	}
	
	protected abstract String configToString(Configuration config);
	/**
	 * 构造新的对象：datasource、memcache
	 * @param config
	 */
	protected abstract void build(Configuration config);
}
