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
			+ 支持3级配置机制
				+ 1，系统及配置优先级最高，系统级配置目录默认为/config/，可以通过-Dsystem.config.dir=/config的方式进行调整
				+ 2，应用配置优先级次之，通过-Dapp.home.dir=xxx的方式进行设置
        			/home/apps/tsl/current/config/ 优先级次之
				+ 3，优先级最低的配置［对应模块配置］，在对应模块的META-INF/config/local/目录下
			
		+ 文件列表
			+ 资源类型
				+ cache-mem.properties
				+ cache-redis.properties
				+ database.properties
				+ dubbo.properties
				
			+ 开关类型
				+ acc.xml 数据访问层
				+ service.xml 服务层
				+ rpc.xml 第三方服务远程调用相关配置
				+ mq.xml  mq服务
				+ job.xml job层
				+ webapp.xml web层