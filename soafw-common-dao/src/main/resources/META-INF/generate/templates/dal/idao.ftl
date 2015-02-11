package ${package}.dao;

import com.kjt.service.common.dao.IDAO;
import com.kjt.service.common.dao.IFKDAO;
<#if tab.pkFieldNum==1>
	<#if tab.pkFieldType.javaType="Integer">	
import com.kjt.service.common.dao.IIDAO;
	<#else>
import com.kjt.service.common.dao.IIDAO;
	</#if>
</#if>
import ${package}.dao.model.${name};

<#if tab.pkFieldNum==1>
	<#if tab.pkFieldType.javaType="Integer">	
public interface I${name}DAO<T extends ${name}> extends IIDAO<T>,IFKDAO<T>,IDAO<T> {
<#elseif tab.pkFieldType.javaType="String">
public interface I${name}DAO<T extends ${name}> extends ISDAO<T>,IFKDAO<T>,IDAO<T> {
	<#else>
public interface I${name}DAO<T extends ${name}> extends ILDAO<T>,IFKDAO<T>,IDAO<T> {
	</#if>
</#if>

}
