package com.kjt.service.common;

import java.util.List;

public interface IService<T> {
	
	List<T> findByIds(List<Long> ids, int scope);

	T findById(long id, int scope);
}
