### 配置规范

+ 配置
	+ 应用配置
		+ 位置
		
			统一放置在对应模块中src/main/resources/META-INF/config/spring/
			
		+ 配置文件列表
			+ spring-cache.xml
			+ spring-db.xml
			+ spring-rpc.xml
			+ spring-mq.xml
			+ spring-service.xml
			+ spring-job.xml
			+ spring-dubbo.xml
		
	+ 资源&程序开关配置 支持动态加载机制
		+ 位置
			
			默认保存在/config目录中，可以通过-Dconfig.file.dir=/config的方式改变系统配置文件目录，当在该目录中没有找到相应文件时则会在对应模块的src/main/resources/META-INF/config/local/目录中查找相应文件
			
		+ 文件列表
			+ 资源类型
				+ cache-mem.properties
				+ cache-redis.properties
				+ database.properties
			+ 开关类型
				+ acc.xml 数据访问层
				+ service.xml 服务层
				+ rpc.xml 第三方服务远程调用相关配置
				+ mq.xml  mq服务
				+ job.xml job层
				+ webapp.xml web层
	