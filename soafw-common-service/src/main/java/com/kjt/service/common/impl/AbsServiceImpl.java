package com.kjt.service.common.impl;

import org.apache.commons.configuration.Configuration;

import com.kjt.service.common.IService;
import com.kjt.service.common.config.DynamicConfig;
import com.kjt.service.common.config.dict.ConfigFileDict;
import com.kjt.service.common.result.IResult;

public abstract class AbsServiceImpl<T extends IResult> implements IService<T> {

	private static DynamicConfig config = new DynamicConfig();

	static {
		config.setFileName(System.getProperty(ConfigFileDict.SERVICE_CONFIG_FILE,
				ConfigFileDict.DEFAULT_SERVICE_CONFIG_NAME));
		config.init();
	}

	/**
	 * 获取数据访问层acc.xml配置信息
	 * 
	 * @return
	 */
	protected Configuration getConfig() {
		return config;
	}

}
