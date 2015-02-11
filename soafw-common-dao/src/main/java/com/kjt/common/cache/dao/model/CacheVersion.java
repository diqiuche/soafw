package com.kjt.common.cache.dao.model;

import java.util.HashMap;
import java.util.Map;

import com.kjt.service.common.annotation.JField;
import com.kjt.service.common.dao.IModel;

public class CacheVersion implements IModel{
	
	private static final long serialVersionUID = 1L;
	
	static Map<String,Integer> fks = new HashMap<String,Integer>();
	
	static{
		/**
		 * 把该model相关的外键属性字段注册到fks map中
		 */
		//eg:fks.put("cityId",0);
	}
	/**
	 * 是否是外键字段
     */
    public static boolean isFk(String name){
    	return fks.containsKey(name);
    }

	/**
	 * 
	 */
	@JField(name="obj_name")
  	protected String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 
	 */
	@JField(name="rec_version")
  	private java.math.BigInteger recVersion;
  	
  	public void setRecVersion(java.math.BigInteger recVersion){
  		this.recVersion=recVersion;
  	}
  	
  	public java.math.BigInteger getRecVersion(){
  		return this.recVersion;
  	}
	/**
	 * 
	 */
	@JField(name="tab_version")
  	private java.math.BigInteger tabVersion;
  	
  	public void setTabVersion(java.math.BigInteger tabVersion){
  		this.tabVersion=tabVersion;
  	}
  	
  	public java.math.BigInteger getTabVersion(){
  		return this.tabVersion;
  	}
	
    
	/**
	 * 保存时非空数据项校验；
	 */
	public boolean validate(){
		boolean passed = true;
		return passed;
	}
	/**
	 * 保存时对应的分表；
	 */
	private String tKjtTabName;
	
	public String getTKjtTabName(){
		return tKjtTabName;
	}
	
  @Override
  public void setTKjtTabName(String tKjtTabName) {
    this.tKjtTabName = tKjtTabName;
  }
}
