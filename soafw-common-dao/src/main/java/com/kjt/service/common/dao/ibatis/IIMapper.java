package com.kjt.service.common.dao.ibatis;

import java.util.Map;

import com.kjt.service.common.dao.IModel;

/**
 * 主键为整型的 ibatis 数据访问接口
 * @author alexzhu
 *
 * @param <T>
 */
public interface IIMapper<T extends IModel> extends IMapper<T> {

	public T queryById(Integer id);

	public Integer insert(Map<String,Object> model);

}
