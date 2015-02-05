package com.kjt.service.common.config.dict;

/**
 * 约定的配置文件
 * 
 * @author zxj
 * 
 */
public class ConfigFileDict {
	
	/**
	 * 配置目录位置
	 * 默认为：/config
	 */
	public static String CONFIG_DIR = "config.file.dir";
	/**
	 * 默认配置文件目录
	 */
	public static String CONFIG_DIR_DEF = "/config";
	
	/**
	 * 数据库配置 默认为：database.properties
	 */
	public static final String DB_CONFIG_FILE = "database.config";

	/**
	 * 数据库默认配置文件
	 */
	public static final String DEFAULT_DB_CONFIG_NAME = "database";

	/**
	 * 访问控制层 默认为：acc.xml
	 */
	public static final String ACCESS_CONTROL_CONFIG_FILE = "acc.config";

	/**
	 * 
	 */
	public static final String DEFAULT_ACCESS_CONTROL_CONFIG_NAME = "acc";

	/**
	 * 访问控制层 默认为：acc.xml
	 */
	public static final String CACHE_MEM_CONFIG_FILE = "cache-mem.config";

	/**
	 * 
	 */
	public static final String DEFAULT_CACHE_MEM_CONFIG_NAME = "cache-mem";
	
	/**
	 * 缓存配置文件名
	 * 默认为：cache-redis.properties
	 */
	public static final String CACHE_REDIS_CONFIG = "cache.redis.config";
	
	/**
	 * 
	 */
	public static final String DEFAULT_CACHE_REDIS_CONFIG_NAME = "cache-redis";

	/**
	 * 服务层 默认为：service.xml
	 */
	public static final String SERVICE_CONFIG_FILE = "service.config";

	/**
	 * 服务层 默认为：service.xml
	 */
	public static final String DEFAULT_SERVICE_CONFIG_NAME = "service";
	/**
	 * web控制层 默认为：web
	 */
	public static final String WEB_CONTROLLER_CONFIG_FILE = "weblayer.config";
	/**
	 * web控制层 默认为：web
	 */
	public static final String DEFAULT_WEB_CONTROLLER_CONFIG_NAME = "weblayer";
	
	/**
	 * mq配置文件
	 * 默认为：acc.xml
	 */
	public static String DEFAULT_MQ_CONFIG_FILE = "mq.config";
	
	/**
	 * 
	 */
	public static String DEFAULT_MQ_CONFIG_NAME = "mq";
}
