package ${package}.dao.ibatis;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.sql.DataSource;

<#if tab.pkFieldNum==1>
	<#if tab.pkFieldType.javaType="Integer">	
import com.kjt.service.common.dao.ibatis.AbsIntIDIBatisDAOImpl;
	<#else>
import com.kjt.service.common.dao.ibatis.AbsLongIDIBatisDAOImpl;
	</#if>
</#if>
import ${package}.dao.I${name}DAO;
import ${package}.dao.ibatis.mapper.${name}Mapper;
import ${package}.dao.model.${name};

@Repository("${name}")
<#if tab.pkFieldNum==1>
	<#if tab.pkFieldType.javaType="Integer">	
public class ${name}IbatisDAOImpl extends AbsIntIDIBatisDAOImpl<${name}> implements I${name}DAO<${name}> {
	<#else>
public class ${name}IbatisDAOImpl extends AbsLongIDIBatisDAOImpl<${name}> implements I${name}DAO<${name}> {
	</#if>
</#if>

	@Resource(name = "${masterDataSource}")
	private DataSource masterDataSource;
	
	@Resource(name = "${slaveDataSource}")
	private DataSource slaveDataSource;
	
	@Resource(name = "${mapQueryDataSource}")
	private DataSource mapQueryDataSource;
	
	@Override
	public Class<${name}Mapper> getMapperClass() {
		
		return ${name}Mapper.class;
	}
	
	@Override
	public Class<${name}> getModelClass() {
		
		return ${name}.class;
	}
	
	@Override
	public boolean isFk(String property) {
	
		return ${name}.isFk(property);
	}
	
	<#if tab.pkFieldNum != 1>
	@Override
	public ${name} queryById(Long id){
		<#if tab.pkFieldNum==0>
		throw new RuntimeException("该表没有主键定义，该方法不能适用，请重新实现！");
		<#else>
		throw new RuntimeException("复合主键，该方法不能适用，请重新实现！");
		</#if>		
	}
	</#if>
	
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
