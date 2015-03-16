### job规范

+ 必须继承实现com.kjt.service.common.job.impl.AbsJob,同时必须实现IJob接口
+ 应用配置必须放在对应项目的resources/META-INF/config/spring/spring-job.xml中
+ 运行时配置必须放在job.xml
	