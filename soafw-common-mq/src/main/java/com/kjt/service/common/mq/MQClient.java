package com.kjt.service.common.mq;

import com.kjt.service.common.config.PoolableObjDynamicConfig;
import com.kjt.service.common.config.dict.ConfigFileTypeDict;

public abstract class MQClient extends PoolableObjDynamicConfig implements IConnector{

	@Override
	public void init() {
		this.setFileName(System.getProperty(DEFAULT_MQ_CONFIG_FILE, DEFAULT_MQ_CONFIG_NAME));
		this.setType(ConfigFileTypeDict.PROPERTIES);
		super.init();
		this.build(this.getConfig());
	}
	
}
