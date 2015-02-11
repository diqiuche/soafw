package com.kjt.common.test.dao.ibatis;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kjt.common.cache.dao.ICacheVersionDAO;
import com.kjt.common.cache.dao.model.CacheVersion;
import com.kjt.service.common.cache.redis.impl.DynamicRedisCache;
import com.kjt.service.common.cache.spring.DynamicMemcacheManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:springtest.xml")
public class Auth {
  @Resource(name = "CacheVersion")
  ICacheVersionDAO<CacheVersion> cacheVersionDAOImpl;
  @Resource(name = "cacheManager")
  protected DynamicMemcacheManager cacheManager;

  @Test
  public void testA() {
    
    long start = System.currentTimeMillis();
    
    DynamicRedisCache redisCache = (DynamicRedisCache) cacheManager.getCache("defaultRedisCache");

    CacheVersion auth = cacheVersionDAOImpl.queryById("", "");

  }

}
