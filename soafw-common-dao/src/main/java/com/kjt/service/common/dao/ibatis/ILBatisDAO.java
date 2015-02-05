package com.kjt.service.common.dao.ibatis;

import com.kjt.service.common.dao.ILDAO;
import com.kjt.service.common.dao.IModel;

/**
 * id 为long型的数据访问接口
 * 
 * @author alexzhu
 *
 * @param <T>
 */
public interface ILBatisDAO<T extends IModel> extends IBatisDAO<T>,ILDAO<T>{
	
	public Class<? extends ILMapper<T>> getMapperClass();

}
