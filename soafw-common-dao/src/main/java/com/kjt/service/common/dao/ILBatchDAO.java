package com.kjt.service.common.dao;

import java.util.List;
/**
 * 批处理数据访问层接口
 * @author alexzhu
 *
 * @param <T>
 */
public interface ILBatchDAO<T>{
	/**
	 * 批量插入
	 * @param datas
	 * @return
	 */
	public int[] batchInsert(List<T> datas);
	
	/**
	 * 批量更新
	 * @param datas
	 * @return
	 */
	public int[] batchUpdate(List<T> datas);
	
	/**
	 * 批量更新
	 * @param datas
	 * @return
	 */
	public int[] batchDelete(List<T> datas);
}
