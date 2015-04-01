### resource规范

+ 需要动态加载配置加载必须继承实现com.kjt.service.common.resource.AbstractDynamicResource同时实现其对应的服务接口
+ 框架配置必须放在对应项目的resources/META-INF/config/spring/spring-dubbo.xml中
+ 运行时配置必须放在webapp.xml
	