package com.kjt.service.common.dao.ibatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kjt.service.common.dao.ICriteria;

/**
 * ibatis 基础数据访问接口
 * 
 * @author alexzhu
 *
 * @param <T>
 */
public interface ICriteriaMapper<T> {

  public int countByCriteria(ICriteria criteria);

  public List<T> queryByCriteria(ICriteria criteria);

  public int deleteByCriteria(ICriteria criteria);

  public int updateByCriteria(@Param("record") T record, @Param("criteria") ICriteria criteria);

  public int updateByCriteria(@Param("record") Map<String, Object> record,
      @Param("criteria") ICriteria criteria);
}
