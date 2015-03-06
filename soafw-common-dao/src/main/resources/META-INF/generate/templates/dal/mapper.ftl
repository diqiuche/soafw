package ${package}.dao.ibatis.mapper;

<#if tab.pkFieldNum==1>
	<#if tab.pkFieldType.javaType="Integer">	
import com.kjt.service.common.dao.ibatis.IIMapper;
	<#elseif tab.pkFieldType.javaType="String">
import com.kjt.service.common.dao.ibatis.ISMapper;
	<#else>
import com.kjt.service.common.dao.ibatis.ILMapper;
	</#if>
</#if>

import ${package}.dao.model.${name};

<#if tab.pkFieldNum==1>
	<#if tab.pkFieldType.javaType="Integer">	
public interface ${name}Mapper extends IIMapper<${name}>{
	<#elseif tab.pkFieldType.javaType="String">
public interface ${name}Mapper extends ISMapper<${name}>{
	<#else>
public interface ${name}Mapper extends ILMapper<${name}>{
	</#if>
</#if>

<#if tab.pkFieldNum==1>
	<#if tab.pkFieldType.javaType="Integer">	
public interface ${name}Mapper extends IIMapper<${name}>{
	<#else>
public interface ${name}Mapper extends ILMapper<${name}>{
	</#if>
</#if>

}
