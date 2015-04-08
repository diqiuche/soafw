package com.kjt.service.common.dao.ibatis;

import com.kjt.service.common.dao.IModel;

/**
 * 主键为整型的 ibatis 数据访问接口
 * 
 * @author alexzhu
 *
 * @param <T>
 */
public interface IIMapper<T extends IModel> extends IMapper<T>,IBatchMapper<T> {

  public T queryById(Integer id);

  public Long insert(T model);

}
