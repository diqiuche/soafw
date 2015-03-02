package com.kjt.common.test.dao.ibatis;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.kjt.service.common.dao.ibatis.AbsIntIDIBatisDAOImpl;
import com.kjt.common.test.dao.IAuthorizationDAO;
import com.kjt.common.test.dao.ibatis.mapper.AuthorizationMapper;
import com.kjt.common.test.dao.model.Authorization;

@Repository("Authorization")
public class AuthorizationIbatisDAOImpl extends AbsIntIDIBatisDAOImpl<Authorization> implements IAuthorizationDAO<Authorization> {

	@Resource(name = "oper_db")
	private DataSource masterDataSource;
	
	@Resource(name = "oper_dbSlave")
	private DataSource slaveDataSource;
	
	@Resource(name = "oper_dbMapQuery")
	private DataSource mapQueryDataSource;
	
	@Override
	public Class<AuthorizationMapper> getMapperClass() {
		
		return AuthorizationMapper.class;
	}
	
	@Override
	public Class<Authorization> getModelClass() {
		
		return Authorization.class;
	}
	
	@Override
	public boolean isFk(String property) {
	
		return Authorization.isFk(property);
	}
	
	@Override
	public String get$TKjtTabName(String tabNameSuffix) {
	  suffixValidate(tabNameSuffix);
	  StringBuilder tableName = new StringBuilder("authorization");
      if(tabNameSuffix!=null&&tabNameSuffix.trim().length()>0){
        tableName.append("_");
        tableName.append(tabNameSuffix.trim()); 
      }
      return tableName.toString();
    }
  
	
	
	@Override
	public DataSource getMasterDataSource(){
		return masterDataSource;
	}
	
	
	@Override
	public DataSource getSlaveDataSource(){
		if (slaveDataSource == null) {
 			return masterDataSource;
 		}
 		return slaveDataSource;
	}
	
	@Override
	public DataSource getMapQueryDataSource(){
		if (mapQueryDataSource == null) {
 			return getSlaveDataSource();
 		}
 		return mapQueryDataSource;
	}
	
}
