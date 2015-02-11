package com.kjt.common.cache.dao.ibatis;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.kjt.common.cache.dao.ICacheVersionDAO;
import com.kjt.common.cache.dao.ibatis.mapper.CacheVersionMapper;
import com.kjt.common.cache.dao.model.CacheVersion;
import com.kjt.service.common.cache.annotation.CacheOpParams;
import com.kjt.service.common.dao.ibatis.AbsStrIDIBatisDAOImpl;
import com.kjt.service.common.dao.ibatis.IBatisDAOException;
import com.kjt.service.common.dao.ibatis.SqlmapUtils;
import com.kjt.service.common.exception.DataAccessException;

@Repository("CacheVersion")
public class CacheVersionIbatisDAOImpl extends AbsStrIDIBatisDAOImpl<CacheVersion> implements
    ICacheVersionDAO<CacheVersion> {

  @Resource(name = "oper_db")
  private DataSource masterDataSource;

  @Resource(name = "oper_dbSlave")
  private DataSource slaveDataSource;

  @Resource(name = "oper_dbMapQuery")
  private DataSource mapQueryDataSource;

  @Override
  public Class<CacheVersionMapper> getMapperClass() {

    return CacheVersionMapper.class;
  }

  @Override
  public Class<CacheVersion> getModelClass() {

    return CacheVersion.class;
  }

  @Override
  public boolean isFk(String property) {

    return CacheVersion.isFk(property);
  }

  @Override
  public String get$TKjtTabName(String tabNameSuffix) {
    suffixValidate(tabNameSuffix);
    StringBuilder tableName = new StringBuilder("cache_version");
    if (tabNameSuffix != null && tabNameSuffix.trim().length() > 0) {
      tableName.append("_");
      tableName.append(tabNameSuffix.trim());
    }
    return tableName.toString();
  }

  @Override
  public DataSource getMasterDataSource() {
    return masterDataSource;
  }

  @Override
  public DataSource getSlaveDataSource() {
    if (slaveDataSource == null) {
      return masterDataSource;
    }
    return slaveDataSource;
  }

  @Override
  public DataSource getMapQueryDataSource() {
    if (mapQueryDataSource == null) {
      return getSlaveDataSource();
    }
    return mapQueryDataSource;
  }
  
  @Cacheable(value = "defaultCache", key = CacheKeyPrefixExpress, unless = "#result == null", condition = "#root.target.cacheable()")
  @Override
  public CacheVersion queryById(String id, String tabNameSuffix) {
    return super.queryById(id, tabNameSuffix);
  }
  @Cacheable(value = "defaultCache", key = CacheKeyPrefixExpress + "", unless = "#result == null", condition = "!#master and #root.target.cacheable()")
  @Override
  public CacheVersion queryById(String id, Boolean master, String tabNameSuffix) {
    return super.queryById(id, master, tabNameSuffix);
  }
  
  @CacheEvict(value = "defaultCache", key = CacheKeyPrefixExpress, condition = "#root.target.cacheable()")
  public int incrObjVersion(String id, String tabNameSuffix) {
    validate(id);
    CacheVersion model = new CacheVersion();
    model.setId(id);
    model.setTKjtTabName(this.get$TKjtTabName(tabNameSuffix));
    SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
    try {
      CacheVersionMapper mapper = session.getMapper(getMapperClass());
      Integer eft = mapper.incrVersion(model);

      return eft;
    } catch (Exception t) {
      throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
    } finally {
      session.commit();
      session.close();
    }
  }
  @CacheEvict(value = "defaultCache", key = CacheKeyPrefixExpress, condition = "#root.target.cacheable()")
  public int incrObjRecVersion(String id, String tabNameSuffix) {
    validate(id);

    CacheVersion model = new CacheVersion();
    model.setId(id);
    model.setTKjtTabName(this.get$TKjtTabName(tabNameSuffix));

    SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
    try {
      CacheVersionMapper mapper = session.getMapper(getMapperClass());
      Integer eft = mapper.incrRecVersion(model);

      return eft;
    } catch (Exception t) {
      throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
    } finally {
      session.commit();
      session.close();
    }
  }
  @CacheEvict(value = "defaultCache", key = CacheKeyPrefixExpress, condition = "#root.target.cacheable()")
  public int incrObjTabVersion(String id, String tabNameSuffix) {
    validate(id);

    CacheVersion model = new CacheVersion();
    model.setId(id);
    model.setTKjtTabName(this.get$TKjtTabName(tabNameSuffix));

    SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
    try {
      CacheVersionMapper mapper = session.getMapper(getMapperClass());
      Integer eft = mapper.incrTabVersion(model);

      return eft;
    } catch (Exception t) {
      throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
    } finally {
      session.commit();
      session.close();
    }
  }

  protected void validate(String id) {
    if (id == null || id.trim().length() == 0) {
      throw new DataAccessException(IBatisDAOException.MSG_1_0004);
    }
  }

}
