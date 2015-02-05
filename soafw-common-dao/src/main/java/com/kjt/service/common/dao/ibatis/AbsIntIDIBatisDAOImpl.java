package com.kjt.service.common.dao.ibatis;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.kjt.service.common.dao.IModel;
import com.kjt.service.common.exception.DataAccessException;

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
 * @author alexzhu
 *
 * @param <T>
 */
public abstract class AbsIntIDIBatisDAOImpl<T extends IModel> extends
		AbsFKIBatisDAOImpl<T> implements IIBatisDAO<T> {

	@Cacheable(value = "defaultCache", key = "#root.target.getPKRecCacheKeyPrefix().concat('@').concat('id').concat('=').concat(#id)", unless = "#result == null", condition = "#root.target.cacheable()")
	@Override
	public T queryById(Integer id) {
		return queryById(id, false);
	}

	@Cacheable(value = "defaultCache", key = "#root.target.getPKRecCacheKeyPrefix().concat('@').concat('id').concat('=').concat(#id)", unless = "#result == null", condition = "!#master and #root.target.cacheable()")
	@Override
	public T queryById(Integer id, Boolean master) {

		validate(id);

		SqlSession session = SqlmapUtils.openSession(master ? this
				.getMasterDataSource() : getSlaveDataSource());
		try {
			IIMapper<T> mapper = session.getMapper(getMapperClass());
			return mapper.queryById(id);
		} catch (Exception t) {
			t.printStackTrace();
			throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
		} finally {
			session.commit();
			session.close();
		}
	}

	@Override
	public Integer insert(T model) {

		SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
		try {
			IIMapper<T> mapper = session.getMapper(getMapperClass());
			Integer id = mapper.insert(model);
			if (id > 0) {
				this.incrTabVersion();
			}
			return id;
		} catch (Exception t) {
			throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
		} finally {
			session.commit();
			session.close();
		}
	}

	@CacheEvict(value = "defaultCache", key = "#root.target.getPKRecCacheKeyPrefix().concat('@').concat('id').concat('=').concat(#id)")
	@Override
	public Integer deleteById(Integer id) {

		validate(id);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);

		SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
		try {
			IMapper<T> mapper = session.getMapper(getMapperClass());
			Integer eft = mapper.deleteByMap(params);
			if (eft > 0) {
				this.incrTabVersion();
			}
			return eft;
		} catch (Exception t) {
			throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
		} finally {
			session.commit();
			session.close();
		}
	}

	@CacheEvict(value = "defaultCache", key = "#root.target.getPKRecCacheKeyPrefix().concat('@').concat('id').concat('=').concat(#id)")
	@Override
	public Integer updateById(Integer id, Map<String, Object> newValue) {

		validate(id);

		if (newValue == null || newValue.isEmpty()) {
			throw new DataAccessException(IBatisDAOException.MSG_1_0007);
		}
		newValue.put("id", id);
		SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
		try {
			IMapper<T> mapper = session.getMapper(getMapperClass());
			Integer eft = mapper.updateByMap(newValue);
			if (eft > 0) {
				this.incrTabVersion();
			}
			return eft;
		} catch (Exception t) {
			throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
		} finally {
			session.commit();
			session.close();
		}
	}

	protected void validate(Integer id) {
		if (id == null) {
			throw new DataAccessException(IBatisDAOException.MSG_1_0005);
		}
		if (id.intValue() <= 0) {
			throw new DataAccessException(IBatisDAOException.MSG_1_0006);
		}
	}

}
