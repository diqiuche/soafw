### job规范

+ 需要动态加载配置必须继承实现com.kjt.service.common.impl.AbsDynamicService同时实现其对应的服务接口
+ 框架配置必须放在对应项目的resources/META-INF/config/spring/spring-service.xml中
+ 运行时配置必须放在service.xml
	