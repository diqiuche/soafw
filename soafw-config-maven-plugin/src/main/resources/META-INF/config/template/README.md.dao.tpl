### dao 实现

+ 实现标准

	+ 配置必须动态加载
	+ 应用配置与资源配置分离
	
	+ memcached

		+ 实现类 com.kjt.service.common.datasource.DynamicDataSource
	
+ ... 

+配置
	
	+ 应用配置
		+ 保存在#{artifactId}-dao/src/main/resource/META-INF/config/spring
		+ spring-dao.xml
	+ 资源配置
		+ 资源文件默认保存在/config目录,当没有找到时，程序会从#{artifactId}-dao/src/main/resource/META-INF/config/local
		+ database.properties