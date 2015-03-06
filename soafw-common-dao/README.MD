### dao包

新建一张表 

CREATE TABLE `cache_version` (
  `obj_name` varchar(32) DEFAULT NULL,
  `rec_version` bigint(20) NOT NULL,
  `tab_version` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8


在spring-db.xml中新增一组cache_db配置


### 缓存开关，开启：-Ddao.query.cacheable=true；关闭：-Ddao.query.cacheable=false；但是会刷新缓存；
### 查询时当master＝true时，查询不启用缓存，但是会刷新缓存；
### 主键操作dao
### 外键操作dao
### 其它操作dao
### 
### 