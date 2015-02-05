package com.kjt.service.common.resource;

import org.apache.commons.configuration.Configuration;

import com.kjt.service.common.config.DynamicConfig;
import com.kjt.service.common.config.dict.ConfigFileDict;

public abstract class AbstractDynamicResource  implements IResource{
	private static DynamicConfig config = new DynamicConfig();

	static {
		config.setFileName(System.getProperty(ConfigFileDict.WEB_CONTROLLER_CONFIG_FILE,
				ConfigFileDict.DEFAULT_WEB_CONTROLLER_CONFIG_NAME));
		config.init();
	}

	/**
	 * 获取数据访问层resource.xml配置信息
	 * 
	 * @return
	 */
	protected Configuration getConfig() {
		return config;
	}
}
