package com.kjt.service.common.dao;

import org.springframework.cache.CacheManager;

/**
 * 缓存策略
 * 
 * @author alexzhu
 *
 */
public interface ICacheable<T> {
	/**
	 * 缓存开关通过java -Ddao.query.cacheable方式设置
	 */
	public static String CACHE_FLG = "dao.query.cacheable";
	/**
	 * 主键缓存开关通过java -Ddao.query.cacheable.pk方式设置
	 */
	public static String PK_CACHE_FLG = "dao.query.cacheable.pk";
	/**
	 * 外键缓存开关通过java -Ddao.query.cacheable.fk方式设置
	 */
	public static String FK_CACHE_FLG = "dao.query.cacheable.fk";
	/**
	 * 表级缓存开关通过java -Ddao.query.cacheable.tb方式设置
	 */
	public static String TB_CACHE_FLG = "dao.query.cacheable.tb";
	/**
	 * 一小时,单位为秒
	 */
	public static final int ONE_HOUR = 3600;
	/**
	 * 一天,单位为秒
	 */
	public static final int ONE_DAY = 86400;
	/**
	 * 十天,单位为秒
	 */
	public static final int TEN_DAY = 864000;

	/**
	 * 0秒：永不失效
	 */
	public static final int FOREVER = 0;
	
	/**
	 * 缓存管理器
	 * @return
	 */
	public CacheManager getCacheManager();

	/**
	 * 缓存的ModelClass.sampleName
	 * 
	 * @return
	 */
	public String getCacheIdentify();
	/**
	 * 是否启用缓存
	 * @return
	 */
	public boolean cacheable();

	/**
	 * 主键缓存(pk)<br>
	 * 应用场景:根据主键查询时使用。<br>
	 * 查询执行步骤:<br>
	 * 1. select * from loupan_basic where loupan_id=?;<br>
	 * 2. 是否存在缓存，mc缓存key拼接规则{表名}+{recVersion}+{主键ID},
	 * 如果存在直接返回结果，否则查询数据库，设置缓存并返回结果。例如：loupan_basic表主键mc缓存key:loupan_basic##2##1<br>
	 * 更新执行步骤:<br>
	 * 1. update loupan_basic set loupan_name=? where loupan_id=?;<br>
	 * 2. 删除外键缓存。(如何删除外键缓存，下文有说明)<br>
	 * 3. 删除对应的主键缓存<br>
	 * 4. 更新updated版本号，删除cache_tags表缓存<br>
	 * 
	 * 外键缓存(fk缓存策略)<br>
	 * 
	 * 应用场景:根据外键查询<br>
	 * 
	 * 查询执行步骤:<br>
	 * 
	 * 1. select * from house_types where loupan_id=?;<br>
	 * 
	 * 2. 拼接mc缓存key，{表名}+{recVersion}+{tabVersion}+{查询条件参数}。ex:house_types@4@{loupan_id=?}<br>
	 * 
	 * 3. 将mc缓存key纪录到redis中。redis key:{表名}+{recVersion}+{tabVersion}+{外键名}+{外键值}<br>
	 * 
	 * 4. 判断是否存在mc缓存key,如果存在直接返回结果，否则查询数据设置mc缓存，返回结果。<br>
	 * 
	 * 更新执行步骤:<br>
	 * 
	 * 1. update house_types where loupan_id=?;<br>
	 * 2. 找出house_types对应的主键缓存,删除主键缓存。<br>
	 * >主键缓存数量超过threshold_for_delete_pk_by_where＝100值时，更新recVersion版本号<br>
	 * 
	 * 3. 删除外键缓存。<br>
	 * > 查出redis中对应的外键缓存。(set结构，其中的值对应mc中的key)<br>
	 * > 删除mckey对应的值。<br>
	 * 4. 更新updated版本号，删除cache_tags表缓存<br>
	 * 
	 * @return 记录缓存版本号
	 */
	Long getRecVersion();

	/**
	 * 表级缓存<br>
	 * 
	 * 应用场景:查询既不是主键查询，也不是外键查询的查询，使用该缓存。<br>
	 * 查询执行步骤:<br>
	 * 1. select * from loupan_basic where status=?;<br>
	 * 2. 拼接mc缓存key, {表名}+{tabVersion}+{查询条件参数}<br>
	 * 3. 是否存在该缓存,如果存在直接返回结果，否则查询数据设置mc缓存，返回结果。<br>
	 * 更新执行步骤：<br>
	 * 改变tabVersion<br>
	 * 改变recVersion<br>
	 * 
	 * 外键缓存优化<br>
	 * 
	 * 功能:对于有些外键查询实现一次查询，分开缓存。由于分开缓存，所以只能满足一些特殊查询。<br>
	 * 条件:<br>
	 * 1. 外键用in的查询<br>
	 * 2. 查询结果不排序,也就是查询语句中无order。<br>
	 * 3. 查询无limit和offset限制。<br>
	 * 
	 * @return 表缓存版本号
	 */
	Long getTabVersion();

	/**
	 * threshold_for_delete_pk_by_where
	 */
	Integer getThresholds();
}