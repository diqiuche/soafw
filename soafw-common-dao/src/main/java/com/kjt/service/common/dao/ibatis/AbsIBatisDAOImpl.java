package com.kjt.service.common.dao.ibatis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.configuration.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.kjt.service.common.annotation.JField;
import com.kjt.service.common.cache.annotation.CacheOpParams;
import com.kjt.service.common.config.DynamicConfig;
import com.kjt.service.common.config.dict.ConfigFileDict;
import com.kjt.service.common.dao.IModel;
import com.kjt.service.common.dao.MapPage;
import com.kjt.service.common.exception.DataAccessException;
import com.kjt.service.common.util.BeanUtil;

/**
 * 该接口为非主键、外键操作接口
 * 
 * 
 * 表级缓存<br>
 * 
 * 应用场景:查询既不是主键查询，也不是外键查询的查询，使用该缓存。<br>
 * 
 * 查询执行步骤:<br>
 * 
 * 1. select * from loupan_basic where status=?;<br>
 * 
 * 2. 拼接mc缓存key, {表名}+{tabVersion}+{查询条件参数}<br>
 * 
 * 3. 是否存在该缓存,如果存在直接返回结果，否则查询数据设置mc缓存，返回结果。<br>
 * 
 * 更新执行步骤：<br>
 * 
 * 改变tabVersion<br>
 * 
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
 * @author alexzhu
 *
 * @param <T>
 */
public abstract class AbsIBatisDAOImpl<T extends IModel> extends AbsCacheableImpl<T> implements
    IBatisDAO<T> {
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

  @CacheEvict(value = "defaultCache", key = TabCacheKeyPrefixExpress
      + ".concat('@').concat(#params)", condition = "#root.target.cacheable()")
  @Override
  public Integer deleteByMap(Map<String, Object> params, String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger
          .debug(
              "deleteByMap(Map<String,Object> params={}, String tabNameSuffix={}) - start", params, tabNameSuffix); //$NON-NLS-1$
    }

    validate(params);

    nonePK$FKCheck(params);

    params.put("tKjtTabName", this.get$TKjtTabName(tabNameSuffix));

    SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
    try {
      IMapper<T> mapper = session.getMapper(getMapperClass());
      Integer eft = mapper.deleteByMap(params);
      if (eft > 0) {
        this.synCache(tabNameSuffix);
      }

      if (logger.isDebugEnabled()) {
        logger
            .debug(
                "deleteByMap(Map<String,Object> params={}, String tabNameSuffix={}) - end - return value={}", params, tabNameSuffix, eft); //$NON-NLS-1$
      }
      return eft;
    } catch (Exception t) {
      logger.error("deleteByMap(Map<String,Object>, String)", t); //$NON-NLS-1$

      throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
    } finally {
      session.commit();
      session.close();
    }
  }

  @CacheEvict(value = "defaultCache", key = TabCacheKeyPrefixExpress + ".concat('@').concat(#cond)", condition = "#root.target.cacheable()")
  @Override
  public Integer updateByMap(Map<String, Object> new_, Map<String, Object> cond,
      String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger
          .debug(
              "updateByMap(Map<String,Object> new_={}, Map<String,Object> cond={}, String tabNameSuffix={}) - start", new_, cond, tabNameSuffix); //$NON-NLS-1$
    }

    validate(cond);

    if (new_ == null || new_.isEmpty()) {
      throw new DataAccessException(IBatisDAOException.MSG_1_0008);
    }

    nonePK$FKCheck(cond);

    Map<String, Object> params = new HashMap<String, Object>();

    params.put("tKjtTabName", this.get$TKjtTabName(tabNameSuffix));

    params.put("updNewMap", new_);
    params.put("updCondMap", cond);
    SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
    try {
      IMapper<T> mapper = session.getMapper(getMapperClass());
      Integer eft = mapper.cmplxUpdate(params);
      if (eft > 0) {
        this.synCache(tabNameSuffix);
      }

      if (logger.isDebugEnabled()) {
        logger
            .debug(
                "updateByMap(Map<String,Object> new_={}, Map<String,Object> cond={}, String tabNameSuffix={}) - end - return value={}", new_, cond, tabNameSuffix, eft); //$NON-NLS-1$
      }
      return eft;
    } catch (Exception t) {
      logger.error("updateByMap(Map<String,Object>, Map<String,Object>, String)", t); //$NON-NLS-1$

      throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
    } finally {
      session.commit();
      session.close();
    }
  }

  @CacheOpParams(time = ONE_DAY)
  @Cacheable(value = "defaultCache", key = TabCacheKeyPrefixExpress
      + ".concat('@').concat(#params)", unless = "#result == null", condition = "#root.target.tabCacheable()")
  @Override
  public List<T> queryByMap(Map<String, Object> params, String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger
          .debug(
              "queryByMap(Map<String,Object> params={}, String tabNameSuffix={}) - start", params, tabNameSuffix); //$NON-NLS-1$
    }

    validate(params);

    nonePK$FKCheck(params);

    params.put("tKjtTabName", this.get$TKjtTabName(tabNameSuffix));

    SqlSession session = SqlmapUtils.openSession(getMapQueryDataSource());
    try {
      IMapper<T> mapper = (IMapper<T>) session.getMapper(getMapperClass());
      List<T> returnList = mapper.queryByMap(params);

      if (logger.isDebugEnabled()) {
        logger
            .debug(
                "queryByMap(Map<String,Object> params={}, String tabNameSuffix={}) - end - return value={}", params, tabNameSuffix, returnList); //$NON-NLS-1$
      }
      return returnList;
    } catch (Exception t) {
      logger.error("queryByMap(Map<String,Object>, String)", t); //$NON-NLS-1$

      throw new DataAccessException(IBatisDAOException.MSG_1_0007, t);
    } finally {
      session.commit();
      session.close();
    }
  }

  @CacheOpParams(time = ONE_DAY)
  @Cacheable(value = "defaultCache", key = TabCacheKeyPrefixExpress
      + ".concat('@').concat(#params)", unless = "#result == null", condition = "#root.target.tabCacheable()")
  @Override
  public List<String> queryIdsByMap(Map<String, Object> params, String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger
          .debug(
              "queryIdsByMap(Map<String,Object> params={}, String tabNameSuffix={}) - start", params, tabNameSuffix); //$NON-NLS-1$
    }

    validate(params);

    params.put("tKjtTabName", this.get$TKjtTabName(tabNameSuffix));

    SqlSession session = SqlmapUtils.openSession(getMapQueryDataSource());
    try {
      IMapper<T> mapper = (IMapper<T>) session.getMapper(getMapperClass());
      List<String> returnList = mapper.queryIdsByMap(params);

      if (logger.isDebugEnabled()) {
        logger
            .debug(
                "queryIdsByMap(Map<String,Object> params={}, String tabNameSuffix={}) - end - return value={}", params, tabNameSuffix, returnList); //$NON-NLS-1$
      }
      return returnList;
    } catch (Exception t) {
      logger.error("queryIdsByMap(Map<String,Object>, String)", t); //$NON-NLS-1$

      throw new DataAccessException(IBatisDAOException.MSG_1_0007, t);
    } finally {
      session.commit();
      session.close();
    }
  }

  @CacheOpParams(time = ONE_DAY)
  @Cacheable(value = "defaultCache", key = TabCacheKeyPrefixExpress
      + ".concat('@').concat(#params)", unless = "#result == null", condition = "!#master and #root.target.tabCacheable()")
  @Override
  public List queryIdsByMap(Map<String, Object> params, Boolean master, String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger
          .debug(
              "queryIdsByMap(Map<String,Object> params={}, Boolean master={}, String tabNameSuffix={}) - start", params, master, tabNameSuffix); //$NON-NLS-1$
    }

    validate(params);

    params.put("tKjtTabName", this.get$TKjtTabName(tabNameSuffix));

    SqlSession session = SqlmapUtils.openSession(master ? getMasterDataSource()
        : getMapQueryDataSource());
    try {
      IMapper<T> mapper = (IMapper<T>) session.getMapper(getMapperClass());
      List returnList = mapper.queryByMap(params);

      if (logger.isDebugEnabled()) {
        logger
            .debug(
                "queryIdsByMap(Map<String,Object> params={}, Boolean master={}, String tabNameSuffix={}) - end - return value={}", params, master, tabNameSuffix, returnList); //$NON-NLS-1$
      }
      return returnList;
    } catch (Exception t) {
      logger.error("queryIdsByMap(Map<String,Object>, Boolean, String)", t); //$NON-NLS-1$

      throw new DataAccessException(IBatisDAOException.MSG_1_0007, t);
    } finally {
      session.commit();
      session.close();
    }
  }

  @CacheOpParams(time = ONE_DAY)
  @Cacheable(value = "defaultCache", key = TabCacheKeyPrefixExpress
      + ".concat('@').concat(#params)", unless = "#result == null", condition = "!#master and #root.target.tabCacheable()")
  @Override
  public List<T> queryByMap(Map<String, Object> params, Boolean master, String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger
          .debug(
              "queryByMap(Map<String,Object> params={}, Boolean master={}, String tabNameSuffix={}) - start", params, master, tabNameSuffix); //$NON-NLS-1$
    }

    validate(params);

    nonePK$FKCheck(params);

    params.put("tKjtTabName", this.get$TKjtTabName(tabNameSuffix));

    SqlSession session = SqlmapUtils.openSession(master ? getMasterDataSource()
        : getMapQueryDataSource());
    try {
      IMapper<T> mapper = (IMapper<T>) session.getMapper(getMapperClass());
      List<T> returnList = mapper.queryByMap(params);

      if (logger.isDebugEnabled()) {
        logger
            .debug(
                "queryByMap(Map<String,Object> params={}, Boolean master={}, String tabNameSuffix={}) - end - return value={}", params, master, tabNameSuffix, returnList); //$NON-NLS-1$
      }
      return returnList;
    } catch (Exception t) {
      logger.error("queryByMap(Map<String,Object>, Boolean, String)", t); //$NON-NLS-1$

      throw new DataAccessException(IBatisDAOException.MSG_1_0007, t);
    } finally {
      session.commit();
      session.close();
    }
  }

  @CacheOpParams(time = ONE_DAY)
  @Cacheable(value = "defaultCache", key = TabCacheKeyPrefixExpress
      + ".concat('@').concat('cnt:').concat(#params)", unless = "#result == null", condition = "#root.target.tabCacheable()")
  @Override
  public Integer countByMap(Map<String, Object> params, String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger
          .debug(
              "countByMap(Map<String,Object> params={}, String tabNameSuffix={}) - start", params, tabNameSuffix); //$NON-NLS-1$
    }

    validate(params);

    nonePK$FKCheck(params);

    params.put("tKjtTabName", this.get$TKjtTabName(tabNameSuffix));

    SqlSession session = SqlmapUtils.openSession(getMapQueryDataSource());
    try {
      IMapper<T> mapper = session.getMapper(getMapperClass());
      Integer returnInteger = mapper.countByMap(params);

      if (logger.isDebugEnabled()) {
        logger
            .debug(
                "countByMap(Map<String,Object> params={}, String tabNameSuffix={}) - end - return value={}", params, tabNameSuffix, returnInteger); //$NON-NLS-1$
      }
      return returnInteger;
    } catch (Exception t) {
      logger.error("countByMap(Map<String,Object>, String)", t); //$NON-NLS-1$

      throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
    } finally {
      session.commit();
      session.close();
    }
  }

  @CacheOpParams(time = ONE_DAY)
  @Cacheable(value = "defaultCache", key = TabCacheKeyPrefixExpress
      + ".concat('@').concat('cnt:').concat(#params)", unless = "#result == null", condition = "!#master and #root.target.tabCacheable()")
  @Override
  public Integer countByMap(Map<String, Object> params, Boolean master, String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger
          .debug(
              "countByMap(Map<String,Object> params={}, Boolean master={}, String tabNameSuffix={}) - start", params, master, tabNameSuffix); //$NON-NLS-1$
    }

    validate(params);

    nonePK$FKCheck(params);

    params.put("tKjtTabName", this.get$TKjtTabName(tabNameSuffix));

    SqlSession session = SqlmapUtils.openSession(master ? getMasterDataSource()
        : getMapQueryDataSource());
    try {
      IMapper<T> mapper = session.getMapper(getMapperClass());
      Integer returnInteger = mapper.countByMap(params);

      if (logger.isDebugEnabled()) {
        logger
            .debug(
                "countByMap(Map<String,Object> params={}, Boolean master={}, String tabNameSuffix={}) - end - return value={}", params, master, tabNameSuffix, returnInteger); //$NON-NLS-1$
      }
      return returnInteger;
    } catch (Exception t) {
      logger.error("countByMap(Map<String,Object>, Boolean, String)", t); //$NON-NLS-1$

      throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
    } finally {
      session.commit();
      session.close();
    }
  }

  @CacheOpParams(time = ONE_DAY)
  @Cacheable(value = "defaultCache", key = TabCacheKeyPrefixExpress
      + ".concat('@').concat('page:').concat(#params).concat('@').concat(#page).concat('@').concat(#size)", unless = "#result == null", condition = "#root.target.tabCacheable()")
  @Override
  public List<T> pageQuery(Map<String, Object> params, int page, int size, String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger
          .debug(
              "pageQuery(Map<String,Object> params={}, int page={}, int size={}, String tabNameSuffix={}) - start", params, page, size, tabNameSuffix); //$NON-NLS-1$
    }

    validate(params);

    params.put("tKjtTabName", this.get$TKjtTabName(tabNameSuffix));

    SqlSession session = SqlmapUtils.openSession(getMapQueryDataSource());
    try {
      IMapper<T> mapper = session.getMapper(getMapperClass());
      MapPage<Map<String, Object>> cmd = new MapPage<Map<String, Object>>();
      cmd.setPageSize(size);
      cmd.setStart(getPageStart(page, size));
      cmd.setEnd(getPageEnd(page, size));
      cmd.setParams(params);

      List<T> returnList = mapper.pageQuery(cmd);

      if (logger.isDebugEnabled()) {
        logger
            .debug(
                "pageQuery(Map<String,Object> params={}, int page={}, int size={}, String tabNameSuffix={}) - end - return value={}", params, page, size, tabNameSuffix, returnList); //$NON-NLS-1$
      }
      return returnList;
    } catch (Exception t) {
      logger.error("pageQuery(Map<String,Object>, int, int, String)", t); //$NON-NLS-1$

      throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
    } finally {
      session.commit();
      session.close();
    }
  }

  @CacheOpParams(time = ONE_DAY)
  @Cacheable(value = "defaultCache", key = TabCacheKeyPrefixExpress
      + ".concat('@').concat('page:').concat(#params).concat('@').concat(#page).concat('@').concat(#size)", unless = "#result == null", condition = "!#master and #root.target.tabCacheable()")
  public List<T> pageQuery(Map<String, Object> params, int page, int size, Boolean master,
      String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger
          .debug(
              "pageQuery(Map<String,Object> params={}, int page={}, int size={}, Boolean master={}, String tabNameSuffix={}) - start", params, page, size, master, tabNameSuffix); //$NON-NLS-1$
    }

    validate(params);

    params.put("tKjtTabName", this.get$TKjtTabName(tabNameSuffix));

    SqlSession session = SqlmapUtils.openSession(master ? this.getMasterDataSource()
        : getMapQueryDataSource());
    try {
      IMapper<T> mapper = session.getMapper(getMapperClass());
      MapPage<Map<String, Object>> cmd = new MapPage<Map<String, Object>>();
      cmd.setPageSize(size);
      cmd.setStart(getPageStart(page, size));
      cmd.setEnd(getPageEnd(page, size));
      cmd.setParams(params);
      List<T> returnList = mapper.pageQuery(cmd);

      if (logger.isDebugEnabled()) {
        logger
            .debug(
                "pageQuery(Map<String,Object> params={}, int page={}, int size={}, Boolean master={}, String tabNameSuffix={}) - end - return value={}", params, page, size, master, tabNameSuffix, returnList); //$NON-NLS-1$
      }
      return returnList;
    } catch (Exception t) {
      logger.error("pageQuery(Map<String,Object>, int, int, Boolean, String)", t); //$NON-NLS-1$

      throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
    } finally {
      session.commit();
      session.close();
    }
  }

  @CacheOpParams(time = ONE_DAY)
  @Cacheable(value = "defaultCache", key = TabCacheKeyPrefixExpress
      + ".concat('@').concat('page:').concat(#params).concat('@').concat(#page).concat('@').concat(#size).concat('@').concat(#orders)", unless = "#result == null", condition = "#root.target.tabCacheable()")
  @Override
  public List<T> pageQuery(Map<String, Object> params, int page, int size, String orders,
      String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger
          .debug(
              "pageQuery(Map<String,Object> params={}, int page={}, int size={}, String orders={}, String tabNameSuffix={}) - start", params, page, size, orders, tabNameSuffix); //$NON-NLS-1$
    }

    validate(params);

    params.put("tKjtTabName", this.get$TKjtTabName(tabNameSuffix));

    SqlSession session = SqlmapUtils.openSession(getMapQueryDataSource());
    try {
      IMapper<T> mapper = session.getMapper(getMapperClass());
      MapPage<Map<String, Object>> cmd = new MapPage<Map<String, Object>>();
      cmd.setPageSize(size);
      cmd.setStart(getPageStart(page, size));
      cmd.setEnd(getPageEnd(page, size));
      cmd.setParams(params);
      cmd.setOrders(this.convert(this.getModelClass(), orders));
      List<T> returnList = mapper.pageQuery(cmd);

      if (logger.isDebugEnabled()) {
        logger
            .debug(
                "pageQuery(Map<String,Object> params={}, int page={}, int size={}, String orders={}, String tabNameSuffix={}) - end - return value={}", params, page, size, orders, tabNameSuffix, returnList); //$NON-NLS-1$
      }
      return returnList;
    } catch (Exception t) {
      logger.error("pageQuery(Map<String,Object>, int, int, String, String)", t); //$NON-NLS-1$

      throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
    } finally {
      session.commit();
      session.close();
    }
  }

  @CacheOpParams(time = ONE_DAY)
  @Cacheable(value = "defaultCache", key = TabCacheKeyPrefixExpress
      + ".concat('@').concat('page:').concat(#params).concat('@').concat(#page).concat('@').concat(#size).concat('@').concat(#orders)", unless = "#result == null", condition = "!#master and #root.target.tabCacheable()")
  @Override
  public List<T> pageQuery(Map<String, Object> params, int page, int size, String orders,
      Boolean master, String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger
          .debug(
              "pageQuery(Map<String,Object> params={}, int page={}, int size={}, String orders={}, Boolean master={}, String tabNameSuffix={}) - start", params, page, size, orders, master, tabNameSuffix); //$NON-NLS-1$
    }

    validate(params);

    params.put("tKjtTabName", this.get$TKjtTabName(tabNameSuffix));

    SqlSession session = SqlmapUtils.openSession(master ? this.getMasterDataSource()
        : getMapQueryDataSource());
    try {
      IMapper<T> mapper = session.getMapper(getMapperClass());
      MapPage<Map<String, Object>> cmd = new MapPage<Map<String, Object>>();
      cmd.setPageSize(size);
      cmd.setStart(getPageStart(page, size));
      cmd.setEnd(getPageEnd(page, size));
      cmd.setParams(params);
      cmd.setOrders(this.convert(this.getModelClass(), orders));
      List<T> returnList = mapper.pageQuery(cmd);

      if (logger.isDebugEnabled()) {
        logger
            .debug(
                "pageQuery(Map<String,Object> params={}, int page={}, int size={}, String orders={}, Boolean master={}, String tabNameSuffix={}) - end - return value={}", params, page, size, orders, master, tabNameSuffix, returnList); //$NON-NLS-1$
      }
      return returnList;
    } catch (Exception t) {
      logger.error("pageQuery(Map<String,Object>, int, int, String, Boolean, String)", t); //$NON-NLS-1$

      throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
    } finally {
      session.commit();
      session.close();
    }
  }

  /**
   * 把属性名称映射到数据库字段
   * 
   * @param model
   * @param orders
   * @return
   */
  protected String convert(Class model, String orders) {
    if (logger.isDebugEnabled()) {
      logger.debug("convert(Class model={}, String orders={}) - start", model, orders); //$NON-NLS-1$
    }

    if (orders == null || orders.trim().isEmpty()) {
      if (logger.isDebugEnabled()) {
        logger.debug(
            "convert(Class model={}, String orders={}) - end - return value={}", model, orders, ""); //$NON-NLS-1$ //$NON-NLS-2$
      }
      return "";
    }

    String[] order = orders.trim().split(",");

    int len = order == null ? 0 : order.length;
    StringBuffer orders_ = new StringBuffer();
    for (int i = 0; i < len; i++) {
      String[] tmp = order[i].split(" ");
      int tlen = tmp == null ? 0 : tmp.length;

      if (tmp != null && tlen > 0) {
        orders_.append(BeanUtil.getJField(model, tmp[0], JField.class));
      }

      if (tmp != null && tlen > 1) {
        orders_.append(" ").append(tmp[1]);
      }
      if (i < len - 1) {
        orders_.append(",");
      }
    }
    String returnString = orders_.toString();

    if (logger.isDebugEnabled()) {
      logger
          .debug(
              "convert(Class model={}, String orders={}) - end - return value={}", model, orders, returnString); //$NON-NLS-1$
    }
    
    if(returnString.trim().length()==0){
      return null;
    }
    
    return returnString;
  }

  public void preInsert(T model) {
    if (logger.isDebugEnabled()) {
      logger.debug("preInsert(T model={}) - start", model); //$NON-NLS-1$
    }

    model.validate();

    if (logger.isDebugEnabled()) {
      logger.debug("preInsert(T model={}) - end", model); //$NON-NLS-1$
    }
  }

  protected int getPageStart(int page, int size) {
    if (logger.isDebugEnabled()) {
      logger.debug("getPageStart(int page={}, int size={}) - start", page, size); //$NON-NLS-1$
    }

    if (page < 1) {
      page = 1;
    }
    int returnint = (page - 1) * size;

    if (logger.isDebugEnabled()) {
      logger.debug(
          "getPageStart(int page={}, int size={}) - end - return value={}", page, size, returnint); //$NON-NLS-1$
    }
    return returnint;
  }

  protected int getPageEnd(int page, int size) {
    if (logger.isDebugEnabled()) {
      logger.debug("getPageEnd(int page={}, int size={}) - start", page, size); //$NON-NLS-1$
    }

    if (logger.isDebugEnabled()) {
      logger
          .debug("getPageEnd(int page={}, int size={}) - end - return value={}", page, size, size); //$NON-NLS-1$
    }
    return size;
  }

  private void nonePK$FKCheck(Map<String, Object> params) {
    if (logger.isDebugEnabled()) {
      logger.debug("nonePK$FKCheck(Map<String,Object> params={}) - start", params); //$NON-NLS-1$
    }

    if (params.containsKey("id")) {
      throw new DataAccessException(IBatisDAOException.MSG_1_0011);
    }

    Iterator<String> keys = params.keySet().iterator();
    while (keys.hasNext()) {
      String key = keys.next();
      if (isFk(key)) {
        throw new DataAccessException(IBatisDAOException.MSG_1_0011);
      }
    }

    if (logger.isDebugEnabled()) {
      logger.debug("nonePK$FKCheck(Map<String,Object> params={}) - end", params); //$NON-NLS-1$
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

  // ##################################################################################################

  /**
   * 缓存总开关
   */
  public boolean cacheable() {
    if (logger.isDebugEnabled()) {
      logger.debug("cacheable() - start"); //$NON-NLS-1$
    }

    String cacheable = System.getProperty(CACHE_FLG, "false");// 缓存总开关

    boolean returnboolean = Boolean.valueOf(cacheable);

    if (logger.isDebugEnabled()) {
      logger.debug("cacheable() - end - return value={}", returnboolean); //$NON-NLS-1$
    }
    return returnboolean; // 缓存开关
  }

  /**
   * 主键缓存开关
   */
  public boolean pkCacheable() {
    if (logger.isDebugEnabled()) {
      logger.debug("pkCacheable() - start"); //$NON-NLS-1$
    }

    boolean returnboolean = cacheable() // 缓存开关
        && Boolean.valueOf(System.getProperty(PK_CACHE_FLG, String.valueOf(cacheable())));// 主键缓存开关

    if (logger.isDebugEnabled()) {
      logger.debug("pkCacheable() - end - return value={}", returnboolean); //$NON-NLS-1$
    }
    return returnboolean; // 主键缓存
  }

  /**
   * 外键缓存开关
   */
  public boolean fkCacheable() {
    if (logger.isDebugEnabled()) {
      logger.debug("fkCacheable() - start"); //$NON-NLS-1$
    }

    boolean returnboolean = pkCacheable() // 主键缓存
        && Boolean.valueOf(System.getProperty(FK_CACHE_FLG, String.valueOf(pkCacheable())));// 外键缓存开关

    if (logger.isDebugEnabled()) {
      logger.debug("fkCacheable() - end - return value={}", returnboolean); //$NON-NLS-1$
    }
    return returnboolean;// 表级缓存
  }

  /**
   * 表级缓存开关 缓存开关必须开启，主键缓存、外键缓存必须开启
   */
  public boolean tabCacheable() {
    if (logger.isDebugEnabled()) {
      logger.debug("tabCacheable() - start"); //$NON-NLS-1$
    }

    boolean returnboolean = fkCacheable() // 外键缓存开关
        && Boolean.valueOf(System.getProperty(TB_CACHE_FLG, String.valueOf(fkCacheable()))); // 表级缓存

    if (logger.isDebugEnabled()) {
      logger.debug("tabCacheable() - end - return value={}", returnboolean); //$NON-NLS-1$
    }
    return returnboolean;// 表级缓存
  }

  @Override
  public Integer getThresholds() {
    if (logger.isDebugEnabled()) {
      logger.debug("getThresholds() - start"); //$NON-NLS-1$
    }

    Integer returnInteger = getConfig().getInteger("threshold_for_delete_pk_by_where", 100);

    if (logger.isDebugEnabled()) {
      logger.debug("getThresholds() - end - return value={}", returnInteger); //$NON-NLS-1$
    }
    return returnInteger;
  }

  public void validate(IModel model) {
    if (model == null) {
      throw new DataAccessException(IBatisDAOException.MSG_1_0007);
    }
    model.validate();
  }
  
  @Override
  public List<T> batchQuery(List<Map<String, Object>> datas, String tabNameSuffix) {
    validate(datas);

    Map<String, Object> params = new HashMap<String, Object>();

    params.put("list", datas);
    params.put("tKjtTabName", this.get$TKjtTabName(tabNameSuffix));

    SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
    try {
      IBatchMapper<T> mapper = session.getMapper(getMapperClass());
      return mapper.batchQuery(params);
    } catch (Exception t) {
      throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
    } finally {
      session.commit();
      session.close();
    }
  }
  /*
   * (non-Javadoc)
   * @see com.kjt.service.common.dao.IBatchDAO#batchUpdate(java.util.Map, java.util.List, java.lang.String)
   */
  @Override
  public Integer batchUpdate(Map<String, Object> new_, List<Map<String, Object>> datas,
      String tabNameSuffix) {
    validate(datas);

    Map<String, Object> params = new HashMap<String, Object>();

    params.put("list", datas);
    params.put("updNewMap", new_);
    params.put("tKjtTabName", this.get$TKjtTabName(tabNameSuffix));

    SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
    try {
      IBatchMapper<T> mapper = session.getMapper(getMapperClass());
      int eft = mapper.batchUpdate(params);
      if (eft > 0) {
        this.synCache(tabNameSuffix);
      }
      return eft;
    } catch (Exception t) {
      throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
    } finally {
      session.commit();
      session.close();
    }
  }
  /*
   * (non-Javadoc)
   * @see com.kjt.service.common.dao.IBatchDAO#batchDelete(java.util.List, java.lang.String)
   */
  @Override
  public Integer batchDelete(List<Map<String, Object>> datas, String tabNameSuffix) {
    validate(datas);

    Map<String, Object> params = new HashMap<String, Object>();

    params.put("list", datas);
    params.put("tKjtTabName", this.get$TKjtTabName(tabNameSuffix));

    SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
    try {
      IBatchMapper<T> mapper = session.getMapper(getMapperClass());
      int eft = mapper.batchDelete(params);
      if (eft > 0) {
        this.synCache(tabNameSuffix);
      }
      return eft;
    } catch (Exception t) {
      throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
    } finally {
      session.commit();
      session.close();
    }
  }
  
  protected void validate(List<Map<String,Object>> datas) {

    if (datas == null) {
      throw new DataAccessException(IBatisDAOException.MSG_1_0005);
    }
    
  }
  
  @Override
  public String getTableName(){
      throw new RuntimeException(this.getClass().getSimpleName()+".getTableName（）必须实现");
  }
  
  @Override
  public String get$TKjtTabName(String tabNameSuffix) {
    suffixValidate(tabNameSuffix);
    StringBuilder tableName = new StringBuilder(this.getTableName());
    if (tabNameSuffix != null && tabNameSuffix.trim().length() > 0) {
      tableName.append("_");
      tableName.append(tabNameSuffix.trim());
    }
    return tableName.toString();
  }
}
