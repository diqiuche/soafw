package com.kjt.service.common.dao.ibatis;

import java.util.Map;

import com.kjt.service.common.dao.IModel;

/**
 * 主键为长整型的 ibatis 数据访问接口
 * @author alexzhu
 *
 * @param <T>
 */
public interface ILMapper<T extends IModel> extends IMapper<T> {

	public T queryById(Long id);

	public Long insert(Map<String,Object> params);

}
