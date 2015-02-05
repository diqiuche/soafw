package com.kjt.service.common.config;

import org.apache.commons.configuration.Configuration;

import com.kjt.service.common.util.MD5Util;
import com.kjt.service.common.util.StringUtils;

public abstract class PoolableObjDynamicConfig extends DynamicConfig {
	
	private String prefix;

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getPrefix(){
		String prefix_ = prefix;
		if (!StringUtils.isEmpty(prefix) && !prefix.endsWith(".")) {
			prefix_ = prefix + ".";
		}
		else{
			prefix_="";
		}
		return prefix_;
	}
	
	@Override
	public final synchronized void onUpdate(Configuration config) {
		
		String old_ = this.getConfigMd5Sig();
		
		String new_ = MD5Util.md5Hex(this.configToString(config));
		/**
		 * 当前配置项没有变化
		 */
		if(old_.equals(new_)){
			return;
		}
		/**
		 * 配置变化
		 */
		build(config);
		/**
		 * 更新老配置
		 */
		super.onUpdate(config);
	}
	
	private String getConfigMd5Sig() {
		return MD5Util.md5Hex(this.configToString(this.getConfig()));
	}
	
	protected abstract String configToString(Configuration config);
	/**
	 * 构造新的对象：datasource、memcache
	 * @param config
	 */
	protected abstract void build(Configuration config);
}
