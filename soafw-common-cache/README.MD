### 缓存包

+ 支持mem cache机制
+ 配置支持动态加载
+ 配置文件默认为 cache.properties,可以通过java 属性机制进行改变-Dcache.config=xxx机制进行改变
+ 配置文件目录默认为： /config，可以通过java属性机制进行改变-Dconfig.file.dir=iiii进行设置
+ 默认情况数据访问层集成了缓存；
	+ 缓存启用开关，可以通过-Ddao.query.cacheable=false机制进行查询cache的关闭，更新时缓存会同步，比如实时性要求较高的job；
	+ 缓存支持多种级别：主键、外键、表级，都支持动态开关；可以分别采用-Ddao.query.cacheable.pk=false、-Ddao.query.cacheable.fk=false、-Ddao.query.cacheable.tb=false，进行主键、外键、表级缓存的关闭操作；备注：外键缓存开关依赖关系如下：
	+ -Ddao.query.cacheable
	+ -Ddao.query.cacheable.pk 
	+ -Ddao.query.cacheable.fk
	+ -Ddao.query.cacheable.tb