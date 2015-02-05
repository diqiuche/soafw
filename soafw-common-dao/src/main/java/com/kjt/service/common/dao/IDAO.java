package com.kjt.service.common.dao;

import java.util.List;
import java.util.Map;

/**
 * 数据访问层基础接口
 * 
 * @todo 建议添加一个nocache访问接口［for关键性业务］
 * @author alexzhu
 *
 * @param <T>
 */
public interface IDAO<T> extends ICacheable<T> {

	/**
	 * 根据map条件删除业务对象，返回影响记录数
	 * 
	 * @param params
	 * @return
	 */
	public Integer deleteByMap(Map<String, Object> params);

	/**
	 * 根据条件更新业务对象，返回影响纪录数
	 * 
	 * @param new_
	 *            更新后值
	 * @param cond
	 *            更新条件
	 * @return
	 */
	public Integer updateByMap(Map<String, Object> new_,
			Map<String, Object> cond);

	/**
	 * 根据条件从slave中查询纪录列表
	 * 
	 * @param params
	 *            查询条件
	 * @return
	 */
	public List<T> queryByMap(Map<String, Object> params);

	/**
	 * 根据条件查询纪录id列表
	 * 
	 * @param params
	 * @return
	 */
	public List queryIdsByMap(Map<String, Object> params);

	/**
	 * 根据条件查询纪录列表
	 * 
	 * @param params
	 *            查询条件
	 * @param master
	 *            是否从master查询,master＝true时从master库中查询,同时重新刷新缓存
	 * @return
	 */
	public List<T> queryByMap(Map<String, Object> params, Boolean master);

	/**
	 * 根据条件查询纪录id列表
	 * 
	 * @param params
	 * @param master
	 *            是否在master中查询,master＝true时从master库中查询,同时重新刷新缓存
	 * @return
	 */
	public List queryIdsByMap(Map<String, Object> params, Boolean master);

	/**
	 * 从slave中通过条件进行统计记录数
	 * 
	 * @param params
	 * @return
	 */
	public Integer countByMap(Map<String, Object> params);

	/**
	 * 通过条件进行统计记录数
	 * 
	 * @param params
	 * @param master
	 *            是否在master中查询,master＝true时从master库中查询,同时重新刷新缓存
	 * @return
	 */
	public Integer countByMap(Map<String, Object> params, Boolean master);

	/**
	 * 根据条件从slave中分页查询纪录列表
	 * 
	 * @param params
	 *            查询条件
	 * @param page
	 *            页面位置
	 * @param size
	 *            页面纪录最大数量
	 * @return
	 */
	public List<T> pageQuery(Map<String, Object> params, int page, int size);

	/**
	 * 
	 * @param params
	 * @param page
	 * @param size
	 * @param master
	 *            是否在master中查询 ,master＝true时从master库中查询,同时重新刷新缓存
	 * @return
	 */
	public List<T> pageQuery(Map<String, Object> params, int page, int size,
			Boolean master);

	/**
	 * 
	 * 必须在子类中实现
	 * 
	 * @param params
	 * @param page
	 * @param size
	 * @param orders
	 *            有对象格式的字符串eg：name asc,age desc
	 * @return
	 */
	public List<T> pageQuery(Map<String, Object> params, int page, int size,
			String orders);

	/**
	 * 必须在子类中实现
	 * 
	 * @param params
	 * @param page
	 * @param size
	 * @param orders
	 *            有对象格式的字符串eg：name asc,age desc
	 * @param master
	 *            是否在master中查询 ,master＝true时从master库中查询,同时重新刷新缓存
	 * @return
	 */
	public List<T> pageQuery(Map<String, Object> params, int page, int size,
			String orders, Boolean master);
}
