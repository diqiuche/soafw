package com.kjt.service.common.mq;

import com.kjt.service.common.config.PoolableObjDynamicConfig;
import com.kjt.service.common.config.dict.ConfigFileTypeDict;

public abstract class DynamicMQClient extends PoolableObjDynamicConfig{

	@Override
	public void init() {
		this.setFileName(System.getProperty(DEFAULT_MQ_CONFIG_FILE, DEFAULT_MQ_CONFIG_NAME));
		this.setType(ConfigFileTypeDict.PROPERTIES);
		super.init();
		this.build(this.getConfig());
	}
	
}
