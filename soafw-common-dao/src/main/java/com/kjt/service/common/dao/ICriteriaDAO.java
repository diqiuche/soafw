package com.kjt.service.common.dao;

import java.util.List;
import java.util.Map;

public interface ICriteriaDAO<T> {
  /**
   * 获取实践操作的表对象名
   * 
   * @param tabNameSuffix
   *          表名后缀［用于支持表拆分机制，即：数据库操作时的表名规则为:tableName+"_"+tabNameSuffix］
   * @return
   */
  public String get$TKjtTabName(String tabNameSuffix);
  
  /**
   * 复杂count支持表达式
   * @param criteria 条件
   * @param tabNameSuffix 表名后缀
   * @return
   */
  public int countByCriteria(ICriteria criteria, String tabNameSuffix);
  
  /**
   * 复杂query支持表达式
   * @param criteria 条件
   * @param tabNameSuffix 表名后缀
   * @return
   */
  public List<T> queryByCriteria(ICriteria criteria, String tabNameSuffix);

  /**
   * 复杂delete支持表达式
   * @param criteria 条件
   * @param tabNameSuffix 表名后缀
   * @return
   */
  public int deleteByCriteria(ICriteria criteria, String tabNameSuffix);

  /**
   * 复杂update支持表达式
   * @param record model结构
   * @param criteria 条件
   * @param tabNameSuffix 表名后缀
   * @return
   */
  public int updateByCriteria(T record, ICriteria criteria, String tabNameSuffix);
  /**
   * 复杂update支持表达式
   * @param record map结构
   * @param criteria 条件
   * @param tabNameSuffix 表名后缀
   * @return
   */
  public int updateByCriteria(Map<String, Object> record,ICriteria criteria, String tabNameSuffix);
}
