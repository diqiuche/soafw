package com.kjt.service.common.impl;

import org.apache.commons.configuration.Configuration;

import com.kjt.service.common.IService;
import com.kjt.service.common.config.DynamicConfig;
import com.kjt.service.common.config.dict.ConfigFileDict;
import com.kjt.service.common.log.Logger;
import com.kjt.service.common.log.LoggerFactory;
import com.kjt.service.common.result.IResult;

public abstract class AbsServiceImpl<T extends IResult> implements IService<T> {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(AbsServiceImpl.class);

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
	
	public String hello(String name){
		if (logger.isInfoEnabled()) {
			logger.info("hello(String name={}) - start", name); //$NON-NLS-1$
		}

		String returnString = "hello," + name;

		if (logger.isInfoEnabled()) {
			logger.info("hello(String name={}) - end - return value={}", name, returnString); //$NON-NLS-1$
		}
		return returnString;
	}

}
