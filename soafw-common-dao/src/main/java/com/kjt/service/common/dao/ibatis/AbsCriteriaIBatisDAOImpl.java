package com.kjt.service.common.dao.ibatis;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.configuration.Configuration;
import org.apache.ibatis.session.SqlSession;

import com.kjt.service.common.config.DynamicConfig;
import com.kjt.service.common.config.dict.ConfigFileDict;
import com.kjt.service.common.dao.ICriteria;
import com.kjt.service.common.dao.ICriteriaDAO;
import com.kjt.service.common.dao.IModel;
import com.kjt.service.common.exception.DataAccessException;

public abstract class AbsCriteriaIBatisDAOImpl<T extends IModel> extends AbsCacheableImpl<T>
    implements IBatisDAO<T>,ICriteriaDAO<T> {

  /**
   * Logger for this class
   */

  private static DynamicConfig config = new DynamicConfig();

  static {
    config.setFileName(System.getProperty(ConfigFileDict.ACCESS_CONTROL_CONFIG_FILE,
        ConfigFileDict.DEFAULT_ACCESS_CONTROL_CONFIG_NAME));
    config.init();
  }

  /**
   * 获取数据访问层acc.xml配置信息
   * 
   * @return
   */
  protected Configuration getConfig() {
    return config;
  }

  @PostConstruct
  public void init() {
    if (logger.isDebugEnabled()) {
      logger.debug("init() - start"); //$NON-NLS-1$
    }

    SqlmapUtils.addMapper(getMapperClass(), getMasterDataSource());
    SqlmapUtils.addMapper(getMapperClass(), getSlaveDataSource());
    SqlmapUtils.addMapper(getMapperClass(), getMapQueryDataSource());
    super.init();

    if (logger.isDebugEnabled()) {
      logger.debug("init() - end"); //$NON-NLS-1$
    }
  }

  public int countByCriteria(ICriteria criteria, String tabNameSuffix) {

    validate(criteria);

    criteria.setTKjtTabName(this.get$TKjtTabName(tabNameSuffix));

    SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
    try {
      IMapper<T> mapper = session.getMapper(getMapperClass());
      
      Integer eft = 0;//mapper.countByCriteria(criteria);

      return eft;
    } catch (Exception t) {
      throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
    } finally {
      session.commit();
      session.close();
    }
  }

  public List<T> queryByCriteria(ICriteria criteria, String tabNameSuffix) {
    validate(criteria);

    criteria.setTKjtTabName(this.get$TKjtTabName(tabNameSuffix));

    SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
    try {
      IMapper<T> mapper = session.getMapper(getMapperClass());
      
      List<T> datas = null;//mapper.queryByCriteria(criteria);

      return datas;
    } catch (Exception t) {
      throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
    } finally {
      session.commit();
      session.close();
    }
  }

  public int deleteByCriteria(ICriteria criteria, String tabNameSuffix) {
    validate(criteria);

    criteria.setTKjtTabName(this.get$TKjtTabName(tabNameSuffix));

    SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
    try {
      IMapper<T> mapper = session.getMapper(getMapperClass());
      
      Integer eft = 0;//mapper.deleteByCriteria(criteria);

      return eft;
    } catch (Exception t) {
      throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
    } finally {
      session.commit();
      session.close();
    }
  }

  public int updateByCriteria(T record, ICriteria criteria, String tabNameSuffix) {
    
    validate(record);
    
    validate(criteria);

    criteria.setTKjtTabName(this.get$TKjtTabName(tabNameSuffix));

    SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
    try {
      IMapper<T> mapper = session.getMapper(getMapperClass());
      
      Integer eft = 0;//mapper.updateByCriteria(record,criteria);

      return eft;
    } catch (Exception t) {
      throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
    } finally {
      session.commit();
      session.close();
    }
  }

  public int updateByCriteria(Map<String, Object> record, ICriteria criteria, String tabNameSuffix) {
    
    validate(record);
    
    validate(criteria);

    criteria.setTKjtTabName(this.get$TKjtTabName(tabNameSuffix));

    SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
    try {
      
      IMapper<T> mapper = session.getMapper(getMapperClass());
      
      Integer eft = 0;//mapper.updateByCriteria(record,criteria);

      return eft;
    } catch (Exception t) {
      throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
    } finally {
      session.commit();
      session.close();
    }
  }

  public void validate(ICriteria criteria) {
    if (criteria == null) {
      throw new DataAccessException(IBatisDAOException.MSG_1_0007);
    }
  }
  
  /**
   * 参数验证
   * 
   * @param params
   */
  protected void validate(Map<String, Object> params) {
    if (logger.isDebugEnabled()) {
      logger.debug("validate(Map<String,Object> params={}) - start", params); //$NON-NLS-1$
    }

    if (params == null || params.isEmpty()) {
      throw new DataAccessException(IBatisDAOException.MSG_1_0004);
    }

    if (logger.isDebugEnabled()) {
      logger.debug("validate(Map<String,Object> params={}) - end", params); //$NON-NLS-1$
    }
  }
}
