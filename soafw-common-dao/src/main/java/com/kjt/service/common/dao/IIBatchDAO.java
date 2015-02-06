package com.kjt.service.common.dao;

import java.util.List;

/**
 * 批处理数据访问层接口
 * 
 * @author alexzhu
 *
 * @param <T>
 */
public interface IIBatchDAO<T> {
  /**
   * 批量插入
   * 
   * @param datas
   * @param tabNameSuffix
   *          表名后缀［用于支持表拆分机制，即：数据库操作时的表名规则为:tableName+"_"+tabNameSuffix］
   * @return
   */
  public long[] batchInsert(List<T> datas, String tabNameSuffix);

  /**
   * 批量更新
   * 
   * @param datas
   * @param tabNameSuffix
   *          表名后缀［用于支持表拆分机制，即：数据库操作时的表名规则为:tableName+"_"+tabNameSuffix］
   * @return
   */
  public int[] batchUpdate(List<T> datas, String tabNameSuffix);

  /**
   * 批量更新
   * 
   * @param datas
   * @param tabNameSuffix
   *          表名后缀［用于支持表拆分机制，即：数据库操作时的表名规则为:tableName+"_"+tabNameSuffix］
   * @return
   */
  public int[] batchDelete(List<T> datas, String tabNameSuffix);

}
