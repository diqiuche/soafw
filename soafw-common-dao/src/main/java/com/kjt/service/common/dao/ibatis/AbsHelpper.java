package com.kjt.service.common.dao.ibatis;

import java.util.ArrayList;
import java.util.List;

import com.kjt.service.common.dao.IHelpper;

public abstract class AbsHelpper<T> implements IHelpper<T> {
  
  private String tKjtTabName;
  /*
   * (non-Javadoc)
   * @see com.kjt.service.common.dao.ibatis.IHelpper#getTKjtTabName()
   */
  @Override
  public String getTKjtTabName() {
    return tKjtTabName;
  }
  /*
   * (non-Javadoc)
   * @see com.kjt.service.common.dao.ibatis.IHelpper#setTKjtTabName(java.lang.String)
   */
  @Override
  public void setTKjtTabName(String tKjtTabName) {
    this.tKjtTabName = tKjtTabName;
  }
  
  protected List<T> oredCriteria;

  public AbsHelpper() {
    oredCriteria = new ArrayList<T>();
  }

  private String orderByClause;

  /*
   * (non-Javadoc)
   * @see com.kjt.service.common.dao.ibatis.IHelpper#setOrderByClause(java.lang.String)
   */
  @Override
  public void setOrderByClause(String orderByClause) {
    this.orderByClause = orderByClause;
  }

  /*
   * (non-Javadoc)
   * @see com.kjt.service.common.dao.ibatis.IHelpper#getOrderByClause()
   */
  @Override
  public String getOrderByClause() {
    return orderByClause;
  }

  private boolean distinct;

  /*
   * (non-Javadoc)
   * @see com.kjt.service.common.dao.ibatis.IHelpper#setDistinct(boolean)
   */
  @Override
  public void setDistinct(boolean distinct) {
    this.distinct = distinct;
  }

  /*
   * (non-Javadoc)
   * @see com.kjt.service.common.dao.ibatis.IHelpper#isDistinct()
   */
  @Override
  public boolean isDistinct() {
    return distinct;
  }

  /*
   * (non-Javadoc)
   * @see com.kjt.service.common.dao.ibatis.IHelpper#getOredCriteria()
   */
  @Override
  public List<T> getOredCriteria() {
    return oredCriteria;
  }
  
  /*
   * (non-Javadoc)
   * @see com.kjt.service.common.dao.ibatis.IHelpper#or(java.lang.Object)
   */
  public void or(T criteria) {
    oredCriteria.add(criteria);
  }

  /*
   * (non-Javadoc)
   * @see com.kjt.service.common.dao.ibatis.IHelpper#or()
   */
  public T or() {
      T criteria = createCriteriaInternal();
    oredCriteria.add(criteria);
    return criteria;
  }

  /*
   * (non-Javadoc)
   * @see com.kjt.service.common.dao.ibatis.IHelpper#createCriteria()
   */
  public T createCriteria() {
      T criteria = createCriteriaInternal();
    if (oredCriteria.size() == 0) {
      oredCriteria.add(criteria);
    }
    return criteria;
  }

  /*
   * (non-Javadoc)
   * @see com.kjt.service.common.dao.ibatis.IHelpper#clear()
   */
  @Override
  public void clear() {
    oredCriteria.clear();
    orderByClause = null;
    distinct = false;
  }
}
