### cache 实现

+ 实现标准

	+ 配置必须动态加载
	+ 应用配置与资源配置分离
	
	+ memcached

		+ 实现类 com.kjt.service.common.cache.mem.impl.DynamicMemCache
	+ redis
		+ 实现类 com.kjt.service.common.cache.mem.impl.DynamicRedisCache
	
+ ... 

+配置

	+ 应用配置
		+ 保存在#{artifactId}-cache/src/main/resource/META-INF/config/spring
		+ spring-cache.xml
	+ 资源配置
		+ 资源文件默认保存在/config目录,当没有找到时，程序会从#{artifactId}-cache/src/main/resource/META-INF/config/local读取配置信息
		+ cache－mem.properties
		+ cache-redis.properties