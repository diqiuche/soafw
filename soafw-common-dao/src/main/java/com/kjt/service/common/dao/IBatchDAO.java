package com.kjt.service.common.dao;

import java.util.List;
import java.util.Map;

/**
 * 数据访问层基础接口
 * 
 * @todo 建议添加一个nocache访问接口［for关键性业务］
 * @author alexzhu
 *
 * @param <T>
 */
public interface IBatchDAO<T> extends ICacheable<T> {

  public List<T> batchQuery(List<Map<String, Object>> datas, String tabNameSuffix);
  
  public Integer batchUpdate(Map<String, Object> new_, List<Map<String, Object>> datas,
      String tabNameSuffix);

  public Integer batchDelete(List<Map<String, Object>> datas, String tabNameSuffix);

  public String get$TKjtTabName(String tabNameSuffix);
  
}
