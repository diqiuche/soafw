package com.kjt.service.common.dao.ibatis;

import com.kjt.service.common.dao.IModel;

/**
 * 主键为长整型的 ibatis 数据访问接口
 * 
 * @author alexzhu
 *
 * @param <T>
 */
public interface ISMapper<T extends IModel> extends IMapper<T>,IBatchMapper<T> {

  public T queryById(String id);

  public Long insert(IModel params);

}
