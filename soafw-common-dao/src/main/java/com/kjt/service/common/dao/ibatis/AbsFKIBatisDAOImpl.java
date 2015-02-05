package com.kjt.service.common.dao.ibatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.kjt.service.common.cache.annotation.CacheOpParams;
import com.kjt.service.common.dao.IFKDAO;
import com.kjt.service.common.dao.IModel;
import com.kjt.service.common.exception.DataAccessException;

/**
 * 外键缓存(fk缓存策略)<br>
 * 
 * 应用场景:根据外键查询<br>
 * 
 * 查询执行步骤:<br>
 * 
 * 1. select * from house_types where loupan_id=?;<br>
 * 
 * 2. 拼接mc缓存key，{表名}+{recVersion}+{tabVersion}+{查询条件参数}。ex:house_types@4@{
 * loupan_id=?}<br>
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
 * @todo job缓存开关［查询建议屏蔽缓存，更新需要同步缓存］
 * 
 * @author alexzhu
 *
 * @param <T>
 */
public abstract class AbsFKIBatisDAOImpl<T extends IModel> extends
		AbsIBatisDAOImpl<T> implements IFKDAO<T> {

	@CacheEvict(value = "defaultCache", key = "#root.target.getFKRecCacheKeyPrefix().concat('@').concat(#property).concat('=').concat(#fkValue)")
	@Override
	public Integer deleteByFK(String property, Integer fkValue) {

		validate(property, fkValue);

		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put(property, fkValue);
		cond.put("$TKjtTabName", this.get$TKjtTabName());
		
		SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
		try {
			IMapper<T> mapper = session.getMapper(getMapperClass());
			Integer eft = mapper.deleteByMap(cond);
			if (eft > 0) {
				synCache(eft, property, fkValue, null);
			}
			return eft;
		} catch (Exception t) {
			throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
		} finally {
			session.commit();
			session.close();
		}
	}

	@CacheEvict(value = "defaultCache", key = "#root.target.getFKRecCacheKeyPrefix().concat('@').concat(#property).concat('=').concat(#fkValue).concat('@').concat(#attchParams)")
	@Override
	public Integer deleteByFK(String property, Integer fkValue,
			Map<String, Object> attchParams) {

		validate(property, fkValue);

		validate(attchParams);

		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put(property, fkValue);
		cond.putAll(attchParams);
		cond.put("$TKjtTabName", this.get$TKjtTabName());
		
		SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
		try {
			IMapper<T> mapper = session.getMapper(getMapperClass());
			Integer eft = mapper.deleteByMap(cond);
			if (eft > 0) {
				synCache(eft, property, fkValue, attchParams);
			}
			return eft;
		} catch (Exception t) {
			throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
		} finally {
			session.commit();
			session.close();
		}
	}

	@CacheEvict(value = "defaultCache", key = "#root.target.getFKRecCacheKeyPrefix().concat('@').concat(#property).concat('=').concat(#fkValue).concat('@').concat(#attchParams)")
	@Override
	public Integer deleteByFK(String property, Long fkValue) {

		validate(property, fkValue);

		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put(property, fkValue);
		
		cond.put("$TKjtTabName", this.get$TKjtTabName());
		
		SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
		try {
			IMapper<T> mapper = session.getMapper(getMapperClass());
			Integer eft = mapper.deleteByMap(cond);
			if (eft > 0) {
				synCache(eft, property, fkValue, null);
			}
			return eft;
		} catch (Exception t) {
			throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
		} finally {
			session.commit();
			session.close();
		}
	}

	@CacheEvict(value = "defaultCache", key = "#root.target.getFKRecCacheKeyPrefix().concat('@').concat(#property).concat('=').concat(#fkValue)")
	@Override
	public Integer deleteByFK(String property, Long fkValue,
			Map<String, Object> attchParams) {

		validate(property, fkValue);

		validate(attchParams);

		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put(property, fkValue);

		cond.putAll(attchParams);
		
		cond.put("$TKjtTabName", this.get$TKjtTabName());

		SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
		try {
			IMapper<T> mapper = session.getMapper(getMapperClass());
			Integer eft = mapper.deleteByMap(cond);
			if (eft > 0) {
				synCache(eft, property, fkValue, attchParams);
			}
			return eft;
		} catch (Exception t) {
			throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
		} finally {
			session.commit();
			session.close();
		}
	}

	@CacheOpParams(time = ONE_DAY)
	@Cacheable(value = "defaultCache", key = "#root.target.getFKRecCacheKeyPrefix().concat('@').concat(#property).concat('=').concat(#fkValue)", unless = "#result == null", condition = "#root.target.fkCacheable()")
	@Override
	public List<T> queryByFK(String property, Integer fkValue) {

		validate(property, fkValue);
		
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put(property, fkValue);
		cond.put("$TKjtTabName", this.get$TKjtTabName());
		
		SqlSession session = SqlmapUtils.openSession(getMapQueryDataSource());
		try {
			IMapper<T> mapper = (IMapper<T>) session
					.getMapper(getMapperClass());
			List<T> result = mapper.queryByMap(cond);
			addKey2FKGroupCache(property, fkValue, null, result);
			return result;
		} catch (Exception t) {
			throw new DataAccessException(IBatisDAOException.MSG_1_0007, t);
		} finally {
			session.commit();
			session.close();
		}

	}

	@CacheOpParams(time = ONE_DAY)
	@Cacheable(value = "defaultCache", key = "#root.target.getFKRecCacheKeyPrefix().concat('@').concat(#property).concat('=').concat(#fkValue).concat('@').concat(#attchParams)", unless = "#result == null", condition = "#root.target.fkCacheable()")
	@Override
	public List<T> queryByFK(String property, Integer fkValue,
			Map<String, Object> attchParams) {

		validate(property, fkValue);

		validate(attchParams);

		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put(property, fkValue);
		cond.putAll(attchParams);
		cond.put("$TKjtTabName", this.get$TKjtTabName());

		SqlSession session = SqlmapUtils.openSession(getMapQueryDataSource());
		try {
			IMapper<T> mapper = (IMapper<T>) session
					.getMapper(getMapperClass());
			List<T> result = mapper.queryByMap(cond);
			addKey2FKGroupCache(property, fkValue, attchParams, result);
			return result;
		} catch (Exception t) {
			throw new DataAccessException(IBatisDAOException.MSG_1_0007, t);
		} finally {
			session.commit();
			session.close();
		}
	}

	@CacheOpParams(time = ONE_DAY)
	@Cacheable(value = "defaultCache", key = "#root.target.getFKRecCacheKeyPrefix().concat('@').concat(#property).concat('=').concat(#fkValue)", unless = "#result == null", condition = "!#master and #root.target.fkCacheable()")
	@Override
	public List<T> queryByFK(String property, Integer fkValue, Boolean master) {

		validate(property, fkValue);

		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put(property, fkValue);
		cond.put("$TKjtTabName", this.get$TKjtTabName());
		
		SqlSession session = SqlmapUtils.openSession(master ? this
				.getMasterDataSource() : getMapQueryDataSource());
		try {
			IMapper<T> mapper = (IMapper<T>) session
					.getMapper(getMapperClass());
			List<T> result = mapper.queryByMap(cond);
			addKey2FKGroupCache(property, fkValue, null, result);
			return result;
		} catch (Exception t) {
			throw new DataAccessException(IBatisDAOException.MSG_1_0007, t);
		} finally {
			session.commit();
			session.close();
		}
	}

	@CacheOpParams(time = ONE_DAY)
	@Cacheable(value = "defaultCache", key = "#root.target.getFKRecCacheKeyPrefix().concat('@').concat(#property).concat('=').concat(#fkValue).concat('@').concat(#attchParams)", unless = "#result == null", condition = "!#master and #root.target.fkCacheable()")
	@Override
	public List<T> queryByFK(String property, Integer fkValue,
			Map<String, Object> attchParams, Boolean master) {
		validate(property, fkValue);
		validate(attchParams);
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put(property, fkValue);
		cond.putAll(attchParams);
		cond.put("$TKjtTabName", this.get$TKjtTabName());
		
		SqlSession session = SqlmapUtils.openSession(master ? this
				.getMasterDataSource() : getMapQueryDataSource());
		try {
			IMapper<T> mapper = (IMapper<T>) session
					.getMapper(getMapperClass());
			List<T> result = mapper.queryByMap(cond);
			addKey2FKGroupCache(property, fkValue, attchParams, result);
			return result;
		} catch (Exception t) {
			throw new DataAccessException(IBatisDAOException.MSG_1_0007, t);
		} finally {
			session.commit();
			session.close();
		}
	}

	@CacheOpParams(time = ONE_DAY)
	@Cacheable(value = "defaultCache", key = "#root.target.getFKRecCacheKeyPrefix().concat('@').concat(#property).concat('=').concat(#fkValue)", unless = "#result == null", condition = "#root.target.fkCacheable()")
	@Override
	public List<T> queryByFK(String property, Long fkValue) {

		validate(property, fkValue);

		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put(property, fkValue);
		cond.put("$TKjtTabName", this.get$TKjtTabName());
		
		SqlSession session = SqlmapUtils.openSession(getMapQueryDataSource());
		try {
			IMapper<T> mapper = (IMapper<T>) session
					.getMapper(getMapperClass());
			List<T> result = mapper.queryByMap(cond);
			addKey2FKGroupCache(property, fkValue, null, result);
			return result;
		} catch (Exception t) {
			throw new DataAccessException(IBatisDAOException.MSG_1_0007, t);
		} finally {
			session.commit();
			session.close();
		}
	}

	@CacheOpParams(time = ONE_DAY)
	@Cacheable(value = "defaultCache", key = "#root.target.getFKRecCacheKeyPrefix().concat('@').concat(#property).concat('=').concat(#fkValue).concat('@').concat(#attchParams)", unless = "#result == null", condition = "#root.target.fkCacheable()")
	@Override
	public List<T> queryByFK(String property, Long fkValue,
			Map<String, Object> attchParams) {
		validate(property, fkValue);
		validate(attchParams);
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put(property, fkValue);
		cond.putAll(attchParams);
		cond.put("$TKjtTabName", this.get$TKjtTabName());
		
		SqlSession session = SqlmapUtils.openSession(getMapQueryDataSource());
		try {
			IMapper<T> mapper = (IMapper<T>) session
					.getMapper(getMapperClass());
			List<T> result = mapper.queryByMap(cond);
			addKey2FKGroupCache(property, fkValue, attchParams, result);
			return result;
		} catch (Exception t) {
			throw new DataAccessException(IBatisDAOException.MSG_1_0007, t);
		} finally {
			session.commit();
			session.close();
		}
	}

	@CacheOpParams(time = ONE_DAY)
	@Cacheable(value = "defaultCache", key = "#root.target.getFKRecCacheKeyPrefix().concat('@').concat(#property).concat('=').concat(#fkValue)", unless = "#result == null", condition = "!#master and #root.target.fkCacheable()")
	@Override
	public List<T> queryByFK(String property, Long fkValue, Boolean master) {

		validate(property, fkValue);

		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put(property, fkValue);
		cond.put("$TKjtTabName", this.get$TKjtTabName());
		
		SqlSession session = SqlmapUtils.openSession(master ? this
				.getMasterDataSource() : getMapQueryDataSource());
		try {
			IMapper<T> mapper = (IMapper<T>) session
					.getMapper(getMapperClass());
			List<T> result = mapper.queryByMap(cond);
			addKey2FKGroupCache(property, fkValue, null, result);
			return result;
		} catch (Exception t) {
			throw new DataAccessException(IBatisDAOException.MSG_1_0007, t);
		} finally {
			session.commit();
			session.close();
		}
	}

	@CacheOpParams(time = ONE_DAY)
	@Cacheable(value = "defaultCache", key = "#root.target.getFKRecCacheKeyPrefix().concat('@').concat(#property).concat('=').concat(#fkValue).concat('@').concat(#attchParams)", unless = "#result == null", condition = "!#master and #root.target.fkCacheable()")
	@Override
	public List<T> queryByFK(String property, Long fkValue,
			Map<String, Object> attchParams, Boolean master) {
		validate(property, fkValue);
		validate(attchParams);
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put(property, fkValue);
		cond.putAll(attchParams);
		cond.put("$TKjtTabName", this.get$TKjtTabName());
		
		SqlSession session = SqlmapUtils.openSession(master ? this
				.getMasterDataSource() : getMapQueryDataSource());
		try {
			IMapper<T> mapper = (IMapper<T>) session
					.getMapper(getMapperClass());
			List<T> result = mapper.queryByMap(cond);
			addKey2FKGroupCache(property, fkValue, attchParams, result);
			return result;
		} catch (Exception t) {
			throw new DataAccessException(IBatisDAOException.MSG_1_0007, t);
		} finally {
			session.commit();
			session.close();
		}
	}

	@CacheEvict(value = "defaultCache", key = "#root.target.getFKRecCacheKeyPrefix().concat('@').concat(#property).concat('=').concat(#fkValue)")
	@Override
	public Integer updateByFK(String property, Integer fkValue,
			Map<String, Object> newValue) {

		if (newValue == null || newValue.isEmpty()) {
			throw new DataAccessException(IBatisDAOException.MSG_1_0008);
		}

		validate(property, fkValue);

		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put(property, fkValue);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("updNewMap", newValue);
		params.put("updCondMap", cond);
		params.put("$TKjtTabName", this.get$TKjtTabName());
		
		SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
		try {
			IMapper<T> mapper = session.getMapper(getMapperClass());
			Integer eft = mapper.cmplxUpdate(params);
			if (eft > 0) {
				synCache(eft, property, fkValue, null);
			}
			return eft;
		} catch (Exception t) {
			throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
		} finally {
			session.commit();
			session.close();
		}
	}

	@CacheEvict(value = "defaultCache", key = "#root.target.getFKRecCacheKeyPrefix().concat('@').concat(#property).concat('=').concat(#fkValue).concat('@').concat(#attchParams)")
	@Override
	public Integer updateByFK(String property, Integer fkValue,
			Map<String, Object> attchParams, Map<String, Object> newValue) {
		if (newValue == null || newValue.isEmpty()) {
			throw new DataAccessException(IBatisDAOException.MSG_1_0008);
		}

		validate(property, fkValue);

		validate(attchParams);

		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put(property, fkValue);
		cond.putAll(attchParams);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("updNewMap", newValue);
		params.put("updCondMap", cond);
		params.put("$TKjtTabName", this.get$TKjtTabName());
		
		SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
		try {
			IMapper<T> mapper = session.getMapper(getMapperClass());
			Integer eft = mapper.cmplxUpdate(params);
			if (eft > 0) {
				synCache(eft, property, fkValue, attchParams);
			}
			return eft;
		} catch (Exception t) {
			throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
		} finally {
			session.commit();
			session.close();
		}
	}

	@CacheEvict(value = "defaultCache", key = "#root.target.getFKRecCacheKeyPrefix().concat('@').concat(#property).concat('=').concat(#fkValue)")
	@Override
	public Integer updateByFK(String property, Long fkValue,
			Map<String, Object> newValue) {
		if (newValue == null || newValue.isEmpty()) {
			throw new DataAccessException(IBatisDAOException.MSG_1_0008);
		}

		validate(property, fkValue);

		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put(property, fkValue);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("updNewMap", newValue);
		params.put("updCondMap", cond);
		params.put("$TKjtTabName", this.get$TKjtTabName());
		
		SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
		try {
			IMapper<T> mapper = session.getMapper(getMapperClass());
			Integer eft = mapper.cmplxUpdate(params);
			if (eft > 0) {
				synCache(eft, property, fkValue, null);
			}
			return eft;
		} catch (Exception t) {
			throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
		} finally {
			session.commit();
			session.close();
		}
	}

	@CacheEvict(value = "defaultCache", key = "#root.target.getFKRecCacheKeyPrefix().concat('@').concat(#property).concat('=').concat(#fkValue).concat('@').concat(#attchParams)")
	@Override
	public Integer updateByFK(String property, Long fkValue,
			Map<String, Object> attchParams, Map<String, Object> newValue) {
		if (newValue == null || newValue.isEmpty()) {
			throw new DataAccessException(IBatisDAOException.MSG_1_0008);
		}

		validate(property, fkValue);
		validate(attchParams);

		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put(property, fkValue);
		cond.putAll(attchParams);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("updNewMap", newValue);
		params.put("updCondMap", cond);
		params.put("$TKjtTabName", this.get$TKjtTabName());
		
		SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
		try {
			IMapper<T> mapper = session.getMapper(getMapperClass());
			Integer eft = mapper.cmplxUpdate(params);
			if (eft > 0) {
				synCache(eft, property, fkValue, attchParams);
			}
			return eft;
		} catch (Exception t) {
			throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
		} finally {
			session.commit();
			session.close();
		}
	}

	/**
	 * 参数验证
	 * 
	 * @param property
	 * @param fkValue
	 */
	protected void validate(String property, Object fkValue) {

		if (fkValue == null
				|| Long.valueOf(fkValue.toString()).longValue() <= 0) {
			throw new DataAccessException(IBatisDAOException.MSG_1_0010);
		}

		if (!isFk(property)) {
			throw new DataAccessException(IBatisDAOException.MSG_1_0009,property);
		}
	}

	// ##################################################################################################

	/**
	 * 外键缓存开关
	 */
	public boolean fkCacheable() {

		String cacheable = System.getProperty(CACHE_FLG, "true");

		return Boolean.valueOf(cacheable) // 缓存开关
				&& Boolean.valueOf(System.getProperty(PK_CACHE_FLG, cacheable)) // 主键缓存
				&& Boolean.valueOf(System.getProperty(FK_CACHE_FLG, cacheable));// 表级缓存
	}

}
