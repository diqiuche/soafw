package com.kjt.service.common.dao.ibatis;

import com.kjt.service.common.dao.IIDAO;
import com.kjt.service.common.dao.IModel;

public interface IIBatisDAO<T extends IModel> extends IBatisDAO<T>, IIDAO<T> {

  public Class<? extends IIMapper<T>> getMapperClass();

}
