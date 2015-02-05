package com.kjt.service.common.dao;

import java.util.List;
import java.util.Map;

/**
 * 以外健操作的数据访问层接口
 * 
 * @author alexzhu
 *
 * @param <T>
 */
public interface IFKDAO<T> extends ICacheable<T> {

	/**
	 * 该属性名是否是外键属性
	 * 
	 * @param property
	 * @return
	 */
	public boolean isFk(String property);

	/**
	 * 通过外键property及其fkValue删除对象
	 * 
	 * @param property
	 *            对象属性名称
	 * @param fkValue
	 *            外键值
	 * @return
	 */
	public Integer deleteByFK(String property, Integer fkValue);

	/**
	 * 
	 * @param property
	 *            对象属性名称
	 * @param fkValue
	 *            外键值
	 * @param attchParams
	 *            附加参数
	 * @return
	 */
	public Integer deleteByFK(String property, Integer fkValue,
			Map<String, Object> attchParams);

	/**
	 * 通过外键property及其fkValue更新对象
	 * 
	 * 更新执行步骤:<br>
	 * 
	 * @param property
	 *            对象属性名称
	 * @param fkValue
	 *            外键值
	 * @param newValue
	 *            新属性值
	 * @return
	 */
	public Integer updateByFK(String property, Integer fkValue,
			Map<String, Object> newValue);

	/**
	 * 通过外键property及其fkValue更新对象
	 * 
	 * 更新执行步骤:<br>
	 * 
	 * @param property
	 *            对象属性名称
	 * @param fkValue
	 *            外键值
	 * @param attchParams
	 *            附加参数
	 * @param newValue
	 *            新属性值
	 * @return
	 */
	public Integer updateByFK(String property, Integer fkValue,
			Map<String, Object> attchParams, Map<String, Object> newValue);

	/**
	 * 通过外键property及其fkValue查询对象，默认从slave中查询
	 * 
	 * @param property
	 *            对象属性名称
	 * @param fkValue
	 *            外键值
	 * @return
	 */
	public List<T> queryByFK(String property, Integer fkValue);

	/**
	 * 通过外键property及其fkValue查询对象，默认从slave中查询
	 * 
	 * @param property
	 *            对象属性名称
	 * @param fkValue
	 *            外键值
	 * @param attchParams
	 *            附加参数
	 * @return
	 */
	public List<T> queryByFK(String property, Integer fkValue,
			Map<String, Object> attchParams);

	/**
	 * 通过外键property及其fkValue查询对象
	 * 
	 * @param property
	 *            对象属性名称
	 * @param fkValue
	 *            外键值
	 * @param master
	 *            是否从master中查询,master＝true时从master库中查询,同时重新刷新缓存
	 * @return
	 */
	public List<T> queryByFK(String property, Integer fkValue, Boolean master);

	/**
	 * 
	 * @param property
	 *            对象属性名称
	 * @param fkValue
	 *            外键值
	 * @param attchParams
	 *            附加参数
	 * @param master
	 *            是否从master中查询,master＝true时从master库中查询,同时重新刷新缓存
	 * @return
	 */
	public List<T> queryByFK(String property, Integer fkValue,
			Map<String, Object> attchParams, Boolean master);

	/**
	 * 通过外键property及其fkValue删除对象
	 * 
	 * @param property
	 * @param fkValue
	 * @return
	 */
	public Integer deleteByFK(String property, Long fkValue);

	/**
	 * 
	 * @param property
	 *            对象属性名称
	 * @param fkValue
	 *            外键值
	 * @param attchParams
	 *            附加参数
	 * @return
	 */
	public Integer deleteByFK(String property, Long fkValue,
			Map<String, Object> attchParams);

	/**
	 * 通过外键property及其fkValue更新对象
	 * 
	 * @param property
	 *            对象属性名称
	 * @param fkValue
	 *            外键值
	 * @param newValue
	 *            新属性值
	 * @return
	 */
	public Integer updateByFK(String property, Long fkValue,
			Map<String, Object> newValue);

	/**
	 * 
	 * 通过外键property及其fkValue更新对象
	 * 
	 * @param property
	 *            对象属性名称
	 * @param fkValue
	 *            外键值
	 * @param attchParams
	 *            附加参数
	 * @param newValue
	 *            新属性值
	 * @return
	 */
	public Integer updateByFK(String property, Long fkValue,
			Map<String, Object> attchParams, Map<String, Object> newValue);

	/**
	 * 通过外键property及其fkValue查询对象，默认从slave中查询
	 * 
	 * @param property
	 *            对象属性名称
	 * @param fkValue
	 *            外键值
	 * @return
	 */
	public List<T> queryByFK(String property, Long fkValue);

	/**
	 * 
	 * 通过外键property及其fkValue查询对象，默认从slave中查询
	 * 
	 * @param property
	 *            对象属性名称
	 * @param fkValue
	 *            外键值
	 * @param attchParams
	 *            附加参数
	 * @return
	 */
	public List<T> queryByFK(String property, Long fkValue,
			Map<String, Object> attchParams);

	/**
	 * 通过外键property及其fkValue查询对象
	 * 
	 * @param property
	 *            对象属性名称
	 * @param fkValue
	 *            外键值
	 * @param master
	 *            是否从master中查询,master＝true时从master库中查询,同时重新刷新缓存
	 * @return
	 */
	public List<T> queryByFK(String property, Long fkValue, Boolean master);

	/**
	 * 通过外键property及其fkValue查询对象
	 * 
	 * @param property
	 *            对象属性名称
	 * @param fkValue
	 *            外键值
	 * @param attchParams
	 *            附加参数
	 * @param master
	 *            是否从master中查询,master＝true时从master库中查询,同时重新刷新缓存
	 * @return
	 */
	public List<T> queryByFK(String property, Long fkValue,
			Map<String, Object> attchParams, Boolean master);
}
