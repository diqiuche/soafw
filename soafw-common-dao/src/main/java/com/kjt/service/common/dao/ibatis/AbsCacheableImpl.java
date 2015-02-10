package com.kjt.service.common.dao.ibatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.cache.CacheManager;

import redis.clients.jedis.exceptions.JedisDataException;

import com.kjt.service.common.cache.mem.impl.DynamicMemCache;
import com.kjt.service.common.cache.redis.impl.DynamicRedisCache;
import com.kjt.service.common.cache.spring.DynamicMemcacheManager;
import com.kjt.service.common.dao.ICacheable;
import com.kjt.service.common.dao.IModel;
import com.kjt.service.common.log.Logger;
import com.kjt.service.common.log.LoggerFactory;

public abstract class AbsCacheableImpl<T extends IModel> implements ICacheable<T> {
  /**
   * Logger for this class
   */
  private static final Logger logger = LoggerFactory.getLogger(AbsCacheableImpl.class);

  protected DynamicMemCache defaultCache;
  protected DynamicRedisCache redisCache;

  public void init() {
    if (logger.isDebugEnabled()) {
      logger.debug("init() - start"); //$NON-NLS-1$
    }

    defaultCache = (DynamicMemCache) this.getCacheManager().getCache(
        DynamicMemCache.DEFAULT_CACHE_NAME);
    redisCache = (DynamicRedisCache) this.getCacheManager().getCache(
        DynamicRedisCache.DEFAULT_CACHE_NAME);

    if (logger.isDebugEnabled()) {
      logger.debug("init() - end"); //$NON-NLS-1$
    }
  }

  @Resource(name = "cacheManager")
  protected DynamicMemcacheManager cacheManager;

  public CacheManager getCacheManager() {
    return cacheManager;
  }

  @Override
  public boolean cacheable() {
    if (logger.isDebugEnabled()) {
      logger.debug("cacheable() - start"); //$NON-NLS-1$
    }

    String cacheable = System.getProperty(CACHE_FLG, "true");

    boolean returnboolean = Boolean.valueOf(cacheable);

    if (logger.isDebugEnabled()) {
      logger.debug("cacheable() - end - return value={}", returnboolean); //$NON-NLS-1$
    }
    return returnboolean; // 缓存开关
  }

  public boolean pkCacheable() {
    if (logger.isDebugEnabled()) {
      logger.debug("pkCacheable() - start"); //$NON-NLS-1$
    }

    String query_cacheable = System.getProperty(QUERY_CACHE_FLG, "true");

    boolean returnboolean = cacheable() // 缓存开关
        && Boolean.valueOf(query_cacheable) // 查询缓存开关
        && Boolean.valueOf(System.getProperty(PK_CACHE_FLG, query_cacheable));

    if (logger.isDebugEnabled()) {
      logger.debug("pkCacheable() - end - return value={}", returnboolean); //$NON-NLS-1$
    }
    return returnboolean; // 主键缓存
  }

  /**
   * 表级缓存keyPrefix tabName+tabNameSuffix@Tn@TabVersion
   * 
   * @return
   */
  public final String getTabCacheKeyPrefix(String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger.debug("getTabCacheKeyPrefix(String tabNameSuffix={}) - start", tabNameSuffix); //$NON-NLS-1$
    }

    String returns = getTabVersionKey(tabNameSuffix) + "@TV" + this.getTabVersion(tabNameSuffix);

    if (logger.isDebugEnabled()) {
      logger.debug("getTabCacheKeyPrefix(String tabNameSuffix={}) - end - return value={}", tabNameSuffix, returns); //$NON-NLS-1$
    }
    return returns;
  }

  /**
   * 表级缓存版本key tabName+tabNameSuffix@Tn
   * 
   * @return
   */
  private String getTabVersionKey(String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger.debug("getTabVersionKey(String tabNameSuffix={}) - start", tabNameSuffix); //$NON-NLS-1$
    }

    String returns = this.get$TKjtTabName(tabNameSuffix) + "@T"
        + redisCache.getInteger(redisCache.getPrefix() + "tab.cache.tag", 0);

    if (logger.isDebugEnabled()) {
      logger.debug("getTabVersionKey(String tabNameSuffix={}) - end - return value={}", tabNameSuffix, returns); //$NON-NLS-1$
    }
    return returns;
  }

  /**
   * 主键缓存keyPrefix tabName+tabNameSuffix@Rn@RecVersion
   * 
   * @return
   */
  public String getPKRecCacheKeyPrefix(String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger.debug("getPKRecCacheKeyPrefix(String tabNameSuffix={}) - start", tabNameSuffix); //$NON-NLS-1$
    }

    String returns = getRecVersionKey(tabNameSuffix) + "@RV" + this.getRecVersion(tabNameSuffix);

    if (logger.isDebugEnabled()) {
      logger.debug("getPKRecCacheKeyPrefix(String tabNameSuffix={}) - end - return value={}", tabNameSuffix, returns); //$NON-NLS-1$
    }
    return returns;
  }

  /**
   * 外键缓存keyPrefix tabName+tabNameSuffix@Rn@RecVersion@TabVersion
   */
  public String getFKRecCacheKeyPrefix(String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger.debug("getFKRecCacheKeyPrefix(String tabNameSuffix={}) - start", tabNameSuffix); //$NON-NLS-1$
    }

    String returns = getRecVersionKey(tabNameSuffix) + "@RV" + this.getRecVersion(tabNameSuffix)
        + "@TV" + getTabVersion(tabNameSuffix);

    if (logger.isDebugEnabled()) {
      logger.debug("getFKRecCacheKeyPrefix(String tabNameSuffix={}) - end - return value={}", tabNameSuffix, returns); //$NON-NLS-1$
    }
    return returns;
  }

  /**
   * 记录级缓存版本key tabName+tabNameSuffix@Rn
   * 
   * @return
   */
  protected String getRecVersionKey(String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger.debug("getRecVersionKey(String tabNameSuffix={}) - start", tabNameSuffix); //$NON-NLS-1$
    }

    String returns = this.get$TKjtTabName(tabNameSuffix) + "@R"
        + redisCache.getInteger(redisCache.getPrefix() + "rec.cache.tag", 0);

    if (logger.isDebugEnabled()) {
      logger.debug("getRecVersionKey(String tabNameSuffix={}) - end - return value={}", tabNameSuffix, returns); //$NON-NLS-1$
    }
    return returns;
  }

  /**
   * 表级缓存
   * 
   * 更新执行步骤：<br>
   * 
   * 改变tabVersion<br>
   * 
   * 改变recVersion<br>
   * 
   * @param property
   * @param value
   */
  protected void synCache(String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger.debug("synCache(String tabNameSuffix={}) - start", tabNameSuffix); //$NON-NLS-1$
    }

    if(!cacheable()){
      if (logger.isDebugEnabled()) {
        logger.debug("synCache(String tabNameSuffix={}) - end", tabNameSuffix); //$NON-NLS-1$
      }
      return;
    }
    /**
     * 改变表版本号：表级缓存、外键缓存实效
     */
    this.incrTabVersion(tabNameSuffix);
    /**
     * 改变记录版本号：主键缓存、外键缓存实效
     */
    this.incrRecVersion(tabNameSuffix);

    if (logger.isDebugEnabled()) {
      logger.debug("synCache(String tabNameSuffix={}) - end", tabNameSuffix); //$NON-NLS-1$
    }
  }

  /**
   * ［表、外键］缓存版本号升级
   */
  @Override
  public long incrTabVersion(String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger.debug("incrTabVersion(String tabNameSuffix={}) - start", tabNameSuffix); //$NON-NLS-1$
    }

    if(!cacheable()){
      if (logger.isDebugEnabled()) {
        logger.debug("incrTabVersion(String tabNameSuffix={}) - end - return value={}", tabNameSuffix, 0); //$NON-NLS-1$
      }
      return 0;
    }
    try {
      long returnlong = redisCache.incr(getTabVersionKey(tabNameSuffix), 1);

      if (logger.isDebugEnabled()) {
        logger.debug("incrTabVersion(String tabNameSuffix={}) - end - return value={}", tabNameSuffix, returnlong); //$NON-NLS-1$
      }
      return returnlong;
    } catch (JedisDataException ex) {
      logger.error("incrTabVersion(String)", ex); //$NON-NLS-1$

      redisCache.set(getTabVersionKey(tabNameSuffix), "0");

      if (logger.isDebugEnabled()) {
        logger.debug("incrTabVersion(String tabNameSuffix={}) - end - return value={}", tabNameSuffix, 0); //$NON-NLS-1$
      }
      return 0;
    }
  }

  /**
   * 集合级缓存［表级、外键］版本号
   */
  @Override
  public Long getTabVersion(String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger.debug("getTabVersion(String tabNameSuffix={}) - start", tabNameSuffix); //$NON-NLS-1$
    }

    String vStr = redisCache.get(getTabVersionKey(tabNameSuffix));
    if (vStr == null) {
      if (logger.isDebugEnabled()) {
        logger.debug("getTabVersion(String tabNameSuffix={}) - end - return value={}", tabNameSuffix, 0l); //$NON-NLS-1$
      }
      return 0l;
    }
    Long version = Long.valueOf(vStr);

    if (logger.isDebugEnabled()) {
      logger.debug("getTabVersion(String tabNameSuffix={}) - end - return value={}", tabNameSuffix, version); //$NON-NLS-1$
    }
    return version;
  }

  /**
   * ［主键、外键］缓存版本号升级
   */
  @Override
  public long incrRecVersion(String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger.debug("incrRecVersion(String tabNameSuffix={}) - start", tabNameSuffix); //$NON-NLS-1$
    }

    if(!cacheable()){
      if (logger.isDebugEnabled()) {
        logger.debug("incrRecVersion(String tabNameSuffix={}) - end - return value={}", tabNameSuffix, 0); //$NON-NLS-1$
      }
      return 0;
    }
    try {
      long returnlong = redisCache.incr(getRecVersionKey(tabNameSuffix), 1);

      if (logger.isDebugEnabled()) {
        logger.debug("incrRecVersion(String tabNameSuffix={}) - end - return value={}", tabNameSuffix, returnlong); //$NON-NLS-1$
      }
      return returnlong;
    } catch (JedisDataException ex) {
      logger.error("incrRecVersion(String)", ex); //$NON-NLS-1$

      redisCache.set(getRecVersionKey(tabNameSuffix), "0");

      if (logger.isDebugEnabled()) {
        logger.debug("incrRecVersion(String tabNameSuffix={}) - end - return value={}", tabNameSuffix, 0l); //$NON-NLS-1$
      }
      return 0l;
    }

  }

  /**
   * 记录［主键、外键］级缓存版本号
   */
  @Override
  public Long getRecVersion(String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger.debug("getRecVersion(String tabNameSuffix={}) - start", tabNameSuffix); //$NON-NLS-1$
    }

    String vStr = redisCache.get(getRecVersionKey(tabNameSuffix));
    if (vStr == null) {
      if (logger.isDebugEnabled()) {
        logger.debug("getRecVersion(String tabNameSuffix={}) - end - return value={}", tabNameSuffix, 0l); //$NON-NLS-1$
      }
      return 0l;
    }
    Long version = Long.valueOf(vStr);

    if (logger.isDebugEnabled()) {
      logger.debug("getRecVersion(String tabNameSuffix={}) - end - return value={}", tabNameSuffix, version); //$NON-NLS-1$
    }
    return version;
  }

  private void synPKCache(int eft, Map<String, Object> params, String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger.debug("synPKCache(int eft={}, Map<String,Object> params={}, String tabNameSuffix={}) - start", eft, params, tabNameSuffix); //$NON-NLS-1$
    }

    /**
     * 查询主键ids
     */
    if (params == null || params.isEmpty()) {
      if (logger.isDebugEnabled()) {
        logger.debug("synPKCache(int eft={}, Map<String,Object> params={}, String tabNameSuffix={}) - end", eft, params, tabNameSuffix); //$NON-NLS-1$
      }
      return;
    }
    if (eft >= getThresholds()) {
      /**
       * >主键缓存数量超过threshold_for_delete_pk_by_where＝100值时， 更新recVersion版本号<br>
       * 当前表主键缓存、外键缓存实效
       */
      incrRecVersion(tabNameSuffix);
    } else {
      List<String> ids = queryIdsByMap(params, tabNameSuffix);
      this.pkCacheEvict(ids, tabNameSuffix);
    }

    if (logger.isDebugEnabled()) {
      logger.debug("synPKCache(int eft={}, Map<String,Object> params={}, String tabNameSuffix={}) - end", eft, params, tabNameSuffix); //$NON-NLS-1$
    }
  }

  /**
   * 
   * 
   * 外键缓存
   * 
   * 添加当前外键查询条件到keyGroup
   * 
   * 执行步骤:<br>
   * 
   * 1. select * from house_types where loupan_id=?;<br>
   * 
   * 2. 拼接mc缓存key，{表名}+{recVersion}+{查询条件参数}。ex:house_types@4@{loupan_id=?}, 缓存为expire为1天<br>
   * 
   * 3. 将mc缓存key添加到redis的keyGroup［hset结构］中。redis key:{表名}+{recVersion}+{外键名}+{外键值}
   * [keyGroup:key，value［set］]，同时expire调整到一天<br>
   * 
   * 4. 判断是否存在mc缓存key,如果存在直接返回结果，否则查询数据设置mc缓存，返回结果。<br>
   * 
   * > 注意：本次redis缓存的key每次都需要调整expire为一天
   * 
   * @param property
   * @param value
   * @param result
   */
  protected void addKey2FKGroupCache(String property, Object value,
      final Map<String, Object> attchParams, final List<T> result, String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger.debug("addKey2FKGroupCache(String property={}, Object value={}, Map<String,Object> attchParams={}, List<T> result={}, String tabNameSuffix={}) - start", property, value, attchParams, result, tabNameSuffix); //$NON-NLS-1$
    }

    /**
     * 外键缓存keyPrefix tabName+tabNameSuffix@Rn@RecVersion@TabVersion
     */
    final String fKeyPrefix = getFKRecCacheKeyPrefix(tabNameSuffix);

    final StringBuffer keyGroupKeys = new StringBuffer(fKeyPrefix);
    keyGroupKeys.append("@");
    keyGroupKeys.append(property);
    keyGroupKeys.append("=");
    keyGroupKeys.append(value);

    final int size = result == null ? 0 : result.size();
    if (size > 0) {

      String keyGroupKey = keyGroupKeys.toString();

      if (attchParams != null && !attchParams.isEmpty()) {
        keyGroupKeys.append("@");
        keyGroupKeys.append(attchParams);
      }

      /**
       * 将mc的缓存key纪录到redis中,每添加一个key到外键keyGroup中，其过期时间顺后延一天
       */
      redisCache.sadd(keyGroupKey, keyGroupKeys.toString(), ONE_DAY);
    }

    if (logger.isDebugEnabled()) {
      logger.debug("addKey2FKGroupCache(String property={}, Object value={}, Map<String,Object> attchParams={}, List<T> result={}, String tabNameSuffix={}) - end", property, value, attchParams, result, tabNameSuffix); //$NON-NLS-1$
    }
  }

  /**
   * 外键缓存
   * 
   * 更新执行步骤:<br>
   * 
   * 1. update house_types where loupan_id=?;<br>
   * 
   * 2. 找出house_types对应的主键缓存,删除主键缓存。<br>
   * >主键缓存数量超过threshold_for_delete_pk_by_where＝100值时，更新recVersion版本号<br>
   * 
   * 3. 删除外键缓存。<br>
   * > 查出redis中对应的外键缓存。(set结构，其中的值对应mc中的key)<br>
   * > 删除mckey对应的值。<br>
   * 4. 更新updated版本号，删除cache_tags表缓存<br>
   * 
   * @param property
   * @param value
   */
  protected void synCache(int eft, final String property, final Object value,
      final Map<String, Object> attchParams, String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger.debug("synCache(int eft={}, String property={}, Object value={}, Map<String,Object> attchParams={}, String tabNameSuffix={}) - start", eft, property, value, attchParams, tabNameSuffix); //$NON-NLS-1$
    }

    if(!cacheable()){
      if (logger.isDebugEnabled()) {
        logger.debug("synCache(int eft={}, String property={}, Object value={}, Map<String,Object> attchParams={}, String tabNameSuffix={}) - end", eft, property, value, attchParams, tabNameSuffix); //$NON-NLS-1$
      }
      return;
    }
    /**
     * 外键缓存keyPrefix tabName+tabNameSuffix@Rn@RecVersion@TabVersion
     */
    final String fKeyPrefix = getFKRecCacheKeyPrefix(tabNameSuffix);

    final StringBuffer keyGroupKeys = new StringBuffer(fKeyPrefix);
    keyGroupKeys.append("@");
    keyGroupKeys.append(property);
    keyGroupKeys.append("=");
    keyGroupKeys.append(value);

    String keyGroupKey = keyGroupKeys.toString();

    Set<String> keyGroup = redisCache.sget(keyGroupKey);

    fkCacheEvict(keyGroup);

    Map<String, Object> params = new HashMap<String, Object>();
    params.put(property, value);
    if (attchParams != null) {
      params.putAll(attchParams);
    }

    synPKCache(eft, params, tabNameSuffix);

    /**
     * 表级缓存实效
     */
    incrTabVersion(tabNameSuffix);
    // 删除外键缓存

    if (logger.isDebugEnabled()) {
      logger.debug("synCache(int eft={}, String property={}, Object value={}, Map<String,Object> attchParams={}, String tabNameSuffix={}) - end", eft, property, value, attchParams, tabNameSuffix); //$NON-NLS-1$
    }
  }

  /**
   * 删除相关外键缓存
   * 
   * @param key
   */
  private void fkCacheEvict(Set<String> keyGroup) {
    if (logger.isDebugEnabled()) {
      logger.debug("fkCacheEvict(Set<String> keyGroup={}) - start", keyGroup); //$NON-NLS-1$
    }

    int size = keyGroup == null ? 0 : keyGroup.size();
    if (size > 0) {
      String[] keyGroups = new String[size];
      keyGroup.toArray(keyGroups);
      /**
       * 删除某一外键对应的所有外键缓存
       */
      for (int i = 0; i < size; i++) {
        defaultCache.evict(keyGroups[i]);
      }
    }

    if (logger.isDebugEnabled()) {
      logger.debug("fkCacheEvict(Set<String> keyGroup={}) - end", keyGroup); //$NON-NLS-1$
    }
  }

  /**
   * 清空相关主键缓存
   * 
   * @param key
   */
  protected void pkCacheEvict(List<String> ids, String tabNameSuffix) {
    if (logger.isDebugEnabled()) {
      logger.debug("pkCacheEvict(List<String> ids={}, String tabNameSuffix={}) - start", ids, tabNameSuffix); //$NON-NLS-1$
    }

    String pkPrefix = this.getPKRecCacheKeyPrefix(tabNameSuffix);
    int size = ids.size();
    if (size > 0) {

      Long[] idsa = new Long[size];
      ids.toArray(idsa);

      for (int i = 0; i < size; i++) {
        try {
          // 删除主键缓存
          /**
           * mc pkPrefix: tabName+tabNameSuffix@Rn@RV@id=value
           */
          String pk = idsa[i].toString();
          String key = pkPrefix + "@id=" + pk;
          defaultCache.evict(key);
        } catch (Exception ex) {
          logger.warn("pkCacheEvict(List<String>, String) - exception ignored", ex); //$NON-NLS-1$
        }
      }
    }

    if (logger.isDebugEnabled()) {
      logger.debug("pkCacheEvict(List<String> ids={}, String tabNameSuffix={}) - end", ids, tabNameSuffix); //$NON-NLS-1$
    }
  }

  public abstract List<String> queryIdsByMap(Map<String, Object> params, String tabNameSuffix);
}
