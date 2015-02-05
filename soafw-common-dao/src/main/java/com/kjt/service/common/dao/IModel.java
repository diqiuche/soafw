package com.kjt.service.common.dao;

import java.io.Serializable;

/**
 * 所有数据库持久化对象的父类，其子类的每个属性必须含有
 * @author alexzhu
 *
 */
public interface IModel extends Serializable {
	/**
	 * 表名或者分表机制实现
	 * @return
	 */
	public String get();
	
	/**
	 * 验证接口
	 * @return
	 */
	public boolean validate();
	
}
