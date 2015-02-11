package com.kjt.service.common.dao.ibatis;

import com.kjt.service.common.dao.IModel;
import com.kjt.service.common.dao.ISDAO;

/**
 * id 为long型的数据访问接口
 * 
 * @author alexzhu
 *
 * @param <T>
 */
public interface ISBatisDAO<T extends IModel> extends IBatisDAO<T>, ISDAO<T> {

  public Class<? extends ISMapper<T>> getMapperClass();

}
