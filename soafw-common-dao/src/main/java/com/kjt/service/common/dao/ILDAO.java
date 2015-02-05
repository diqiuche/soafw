package com.kjt.service.common.dao;

import java.util.Map;
/**
 * 主键为长整型操作接口
 * 
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
 * @author alexzhu
 *
 * @param <T>
 */
public interface ILDAO<T> extends ICacheable<T> {
	/**
	 * 持久化数据对象，返回当前对象的id
	 * 
	 * @param model
	 * @return
	 */
	public Long insert(T model);

	/**
	 * 通过主键删除业务对象
	 * 
	 * @param id
	 * @return
	 */
	public Integer deleteById(Long id);
	
	/**
	 * 通过id，更新业务对象
	 * 
	 * @param id
	 * @param newValue
	 * @return
	 */
	public Integer updateById(Long id, Map<String, Object> newValue);
	
	/**
	 * 通过id查询对象，默认从slave中查询
	 * 
	 * @param id
	 * @return
	 */
	public T queryById(Long id);
	
	/**
	 * 通过id查询对象
	 * 
	 * @param id
	 *            对象id
	 * @param master
	 *            是否从master中查询,master＝true时从master库中查询,同时重新刷新缓存
	 * @return
	 */
	public T queryById(Long id, Boolean master);
}
