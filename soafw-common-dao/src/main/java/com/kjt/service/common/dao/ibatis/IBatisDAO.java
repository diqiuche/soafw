package com.kjt.service.common.dao.ibatis;

import javax.sql.DataSource;

import com.kjt.service.common.dao.IBatchDAO;
import com.kjt.service.common.dao.IDAO;
import com.kjt.service.common.dao.IModel;

/**
 * ibatis 操作接口
 * 
 * @author alexzhu
 *
 * @param <T>
 */
public interface IBatisDAO<T> extends IDAO<T>,IBatchDAO<T> {

  public Class<? extends IMapper<T>> getMapperClass();

  public Class<? extends IModel> getModelClass();

  public DataSource getMasterDataSource();

  public DataSource getSlaveDataSource();

  public DataSource getMapQueryDataSource();
  
  public void validate(IModel model);
  /**
   * 该属性名是否是外键属性
   * 
   * @param property
   * @return
   */
  public boolean isFk(String property);
}
