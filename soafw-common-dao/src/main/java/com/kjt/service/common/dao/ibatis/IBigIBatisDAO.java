package com.kjt.service.common.dao.ibatis;

import com.kjt.service.common.dao.IBIDAO;
import com.kjt.service.common.dao.IModel;

/**
 * id 为long型的数据访问接口
 * 
 * @author alexzhu
 *
 * @param <T>
 */
public interface IBigIBatisDAO<T extends IModel> extends IBatisDAO<T>, IBIDAO<T> {

  public Class<? extends IBigIMapper<T>> getMapperClass();

}
