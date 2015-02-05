package com.kjt.service.common.dao.ibatis;

import java.util.Map;

import javax.sql.DataSource;

import com.kjt.service.common.dao.IDAO;
import com.kjt.service.common.dao.IModel;

/**
 * ibatis 操作接口
 * @author alexzhu
 *
 * @param <T>
 */
public interface IBatisDAO<T> extends IDAO<T>{
	
	public Class<? extends IMapper<T>> getMapperClass();

	public Class<? extends IModel> getModelClass();

	public DataSource getMasterDataSource();

	public DataSource getSlaveDataSource();

	public DataSource getMapQueryDataSource();
	/**
	 * 该属性名是否是外键属性
	 * 
	 * @param property
	 * @return
	 */
	public boolean isFk(String property);
	
	/**
	 * 表名或者分表机制实现
	 * @return
	 */
	public String get$TKjtTabName(Map<String,Object> params);
	
	/**
	 * 表名或者分表机制实现
	 * @return
	 */
	public String get$TKjtTabName(T params);
		
}
