package ${package}.dao.ibatis;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
<#if tab.pkFieldNum==1>
	<#if tab.pkFieldType.javaType="Integer">	
import com.kjt.service.common.dao.ibatis.AbsIntIDIBatisDAOImpl;
import com.kjt.service.common.dao.ibatis.IIMapper;
	<#elseif tab.pkFieldType.javaType="java.math.BigInteger">
import com.kjt.service.common.dao.ibatis.AbsBigIIDIBatisDAOImpl;
import com.kjt.service.common.dao.ibatis.IBigIMapper;	
	<#elseif tab.pkFieldType.javaType="String">
import com.kjt.service.common.dao.ibatis.AbsStrIDIBatisDAOImpl;
import com.kjt.service.common.dao.ibatis.ISMapper;
	<#else>
import com.kjt.service.common.dao.ibatis.AbsLongIDIBatisDAOImpl;
import com.kjt.service.common.dao.ibatis.ILMapper;
	</#if>
</#if>

import com.kjt.service.common.dao.ibatis.IBatisDAOException;

import ${package}.dao.I${name}DAO;
import ${package}.dao.ibatis.mapper.${name}Mapper;
import ${package}.dao.model.${name};
import com.kjt.service.common.dao.ibatis.SqlmapUtils;
import com.kjt.service.common.exception.DataAccessException;

@Repository("${name}")
<#if tab.pkFieldNum==1>
	<#if tab.pkFieldType.javaType="Integer">	
public class ${name}IbatisDAOImpl extends AbsIntIDIBatisDAOImpl<${name}> implements I${name}DAO<${name}> {
	<#elseif tab.pkFieldType.javaType="java.math.BigInteger">
public class ${name}IbatisDAOImpl extends AbsBigIIDIBatisDAOImpl<${name}> implements I${name}DAO<${name}> {
<#elseif tab.pkFieldType.javaType="String">
public class ${name}IbatisDAOImpl extends AbsStrIDIBatisDAOImpl<${name}> implements I${name}DAO<${name}> {	
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
	public int getVersion(){
		//代码生成器版本号，请不要手动改
	    return 2;
	}
  
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
	
	public String getTableName() {
    	return "${tab.name}";
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
	
	<#if tab.pkFieldNum != 1>
	@Override
	public ${name} queryById(Long id){
		<#if tab.pkFieldNum==0>
		throw new RuntimeException("该表没有主键定义，该方法不能适用，请重新实现！");
		<#else>
		throw new RuntimeException("复合主键，该方法不能适用，请重新实现！");
		</#if>		
	}
	<#else>
	
		<#if tab.pkFieldType.javaType="Integer">	
	public Integer insert(${name} model, String tabNameSuffix) {
		<#elseif tab.pkFieldType.javaType="java.math.BigInteger">
	public java.math.BigInteger insert(${name} model, String tabNameSuffix) {
		<#elseif tab.pkFieldType.javaType="String">
	public String insert(${name} model, String tabNameSuffix) {	
		<#else>
	public Long insert(${name} model, String tabNameSuffix) {
		</#if>
		if (logger.isDebugEnabled()) {
      		logger.debug("insert(T model={}, String tabNameSuffix={}) - start", model, tabNameSuffix); //$NON-NLS-1$
    	}
    	
    	validate(model);
    	
    	model.setTKjtTabName(this.get$TKjtTabName(tabNameSuffix));
    
    	SqlSession session = SqlmapUtils.openSession(getMasterDataSource());
    	try {
    		<#if tab.pkFieldType.javaType="Integer">
      		IIMapper<${name}> mapper = session.getMapper(getMapperClass());
      		<#elseif tab.pkFieldType.javaType="java.math.BigInteger">
      		IBigIMapper<${name}> mapper = session.getMapper(getMapperClass());
      		<#elseif tab.pkFieldType.javaType="String">
      		ISMapper<${name}> mapper = session.getMapper(getMapperClass());
      		<#else>
      		ILMapper<${name}> mapper = session.getMapper(getMapperClass());
      		</#if>
      		Long id = mapper.insert(model);
      		if (id !=null) {
        		this.incrTabVersion(tabNameSuffix);
      		}

      		if (logger.isDebugEnabled()) {
        		logger.debug("insert(T model={}, String tabNameSuffix={}) - end - return value={}", model, tabNameSuffix, id); //$NON-NLS-1$
      		}
      		return model.getId();
    	} catch (Exception t) {
      		logger.error("insert(T, String)", t); //$NON-NLS-1$
      		throw new DataAccessException(IBatisDAOException.MSG_2_0001, t);
    	} finally {
     		session.commit();
     		session.close();
    	}
  	}
	</#if>
	
}
