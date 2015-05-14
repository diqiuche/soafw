## 框架

#### soa服务框架包图

![soa服务框架包图](http://gitlab.corp.kjt.com/_code_doc/_tech/raw/master/arch_design/doc/soafw-package.png)

##### kjt－framwork
+ 跨境通统一的java框架
+ j2ee、spring、mybaties、springmvc、freemarker、memchecache、redis、mysql等

###### common－util
+ 框架中的公共类，eg：工具类 

###### common－concurrent
+ 框架中多线程处理模版类
+ 同步锁处理类

###### common－log
+ 日志处理相关类

###### common－config
+ 具体模块配置文件，通过java 属性机制进行改变-Dxxx=yyy
+ 配置文件存储目录：默认存放在： /config，可以通过java属性机制进行改变-Dconfig.file.dir=iiii进行设置，当在当前目录找不到指定文件时，会到classpath:META-INF/config/local/ 目录中查找；
+ 配置文件格式支持
	+ xml
	+ properties
+ 支持动态加载机制
+ 相关设计模式：代理设计模式
+ 相关配置文件：从框架级别进行解耦
	+ 资源类型：支持多组机制
		+ database.properties
		+ cache-mem.properties
		+ cache-redis.properties
		+ mq.properties
	+ 开关类型：按层管理
		+ acc.xml
		+ service.xml
		+ weblayer.xml
		+ rpc
		
###### common－mq
+ 负责消息中间件通信的模块

###### common－rpc
+ rpc通信模块
	+ http
	+ rmi
	+ hessian

###### common－cache
+ 缓存机制模块
	+ mem cache
	+ redis
	+ 。。。no sql

###### common－dao
+ 数据访问层框架模块
+ 关系型数据库访问层接口及默认实现机制（mybaties）

###### common－model
+ 业务模型对象模块
	+ DTO
	+ BO

###### common－job
+ job框架模块
+ job监控信息采集

###### common－service
+ service框架模块
+ service服务监控信息采集

##### 相关工具
+ 服务项目框架生成器
	+ 项目生成工具
		+ cd soafw/tool
		+ ./genAll.sh hello
	+ job模块项目生成工具
		+ cd soafw/tool
		+ ./genJob.sh hello trade
	+ 数据访问层代码生成器
		+ import hello-dao as maven 项目
		+ 在src/test/java中找到com.kjt.service.hello.dao.DaoGen
		+ 按照提示修改DaoGen.java内容
		+ 运行DaoGen
	+ exception管理工具
+ checkstyle
+ 测试框架代码生成器插件
	+ 默认是关闭的,可以通过-Dsoafw.tester.gen=true的方式开启

##### 相关标准
+ 编程规范
+ 测试规范
+ RESTful
+ dubbo

##### deploy

+ mvn deploy

