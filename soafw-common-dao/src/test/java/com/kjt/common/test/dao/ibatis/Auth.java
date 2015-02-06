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
  IAuthorizationDAO<Authorization> authDAOImpl;

  @Test
  public void testA() {
    long start = System.currentTimeMillis();
    Authorization auth = authDAOImpl.queryById(41, "");
    System.out.println("0,timeused: " + (System.currentTimeMillis() - start));
    int i=1;
    while(i<100){
      start = System.currentTimeMillis();
      auth = authDAOImpl.queryById(41, "");
      System.out.println(i+",timeused: " + (System.currentTimeMillis() - start));
      i++;
    }
    System.out.println();
    System.out.println(auth.getAppid());
  }

}
