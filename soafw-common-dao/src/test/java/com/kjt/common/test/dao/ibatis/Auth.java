package com.kjt.common.test.dao.ibatis;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kjt.common.test.dao.IAuthorizationDAO;
import com.kjt.common.test.dao.model.Authorization;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:springtest.xml")
public class Auth {
  @Resource(name = "Authorization")
  IAuthorizationDAO<Authorization> authorizationDAOImpl;

  @Test
  public void testA() {

    long start = System.currentTimeMillis();

    // DynamicRedisCache redisCache = (DynamicRedisCache)
    // cacheManager.getCache("defaultRedisCache");
    Authorization auth = authorizationDAOImpl.queryById(14, "");
    System.out.println((System.currentTimeMillis()-start));
  }

}
