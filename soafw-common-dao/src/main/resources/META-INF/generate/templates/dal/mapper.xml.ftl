<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
    PUBLIC "-//ibatis.apache.org//DTD Mapper 3.0//EN"
    "http://ibatis.apache.org/dtd/ibatis-3-mapper.dtd">

<mapper namespace="${package}.dao.ibatis.mapper.${name}Mapper">
	<resultMap id="BaseResultMap" type="${package}.dao.model.${name}">		
		
		<#list cols as col>
			<#if col.isPK="yes" && tab.pkFieldNum==1>
			<#if col.type.javaType="Integer" || col.type.javaType="Long" || col.type.javaType="Float" || col.type.javaType="Double" || col.type.javaType="java.math.BigInteger" || col.type.javaType="String">
			<id property="id" column="${col.name}" />
			</#if>
			<#else>
			<result property="${col.fieldName}" column="${col.name}" />
			</#if>
		</#list>
	</resultMap>
	
	<sql id="Column_List_Id_Gen">
		<#if tab.pkFieldNum==1>
			<#list colMaps as col>
			<#if col.isPK="yes" &&  (col.type.javaType="Integer" || col.type.javaType="Long" || col.type.javaType="Float" || col.type.javaType="Double" || col.type.javaType="java.math.BigInteger" || col.type.javaType="String")>
			${col.name}
			</#if>
			</#list>
			<#else>			
			<#list colMaps as col>
			<#if col.isPK="yes">
				${col.name}
			</#if>
			</#list>
		</#if>
	</sql>
	<sql id="Column_List_Base_Gen">
		<#list colMaps as col>
			${col.name}<#if col_has_next>,</#if>
		</#list>
	</sql>
	<sql id="Where_Clause_Id_Gen">
		<#if tab.pkFieldNum==1>
		<#list colMaps as col>
		<#if col.isPK="yes" &&   (col.type.javaType="Integer" || col.type.javaType="Long" || col.type.javaType="Float" || col.type.javaType="Double" || col.type.javaType="java.math.BigInteger" || col.type.javaType="String")>
		and ${col.name}=${r"#{id}"}
		</#if>
		</#list>
		<#else>			
		<#list colMaps as col>
		<#if col.isPK="yes">
		<if test="${col.fieldName} !=  null">			        
			and ${col.name}=${r"#{"}${col.fieldName}${r"}"}
		</if>
		</#if>
		</#list>
		</#if>
		<include refid="Where_Clause_Id_Extend" />
	</sql>
	<sql id="Where_Clause_Normal_Gen">
		<#if tab.pkFieldNum==1>
		<#list colMaps as col>
		<#if tab.pkFieldNum==1 && col.isPK="yes" &&   (col.type.javaType="Integer" || col.type.javaType="Long" || col.type.javaType="Float" || col.type.javaType="Double" || col.type.javaType="java.math.BigInteger" || col.type.javaType="String")>
		<if test="id !=  null">
			and ${col.name}=${r"#{id}"}
		</if>
		<if test="fromId !=  null">
			and ${col.name}>${r"#{fromId"}${r"}"}
		</if>
		<#else>
		<if test="${col.fieldName} !=  null">			        
			and ${col.name}=${r"#{"}${col.fieldName}${r"}"}
		</if>
		</#if>			
		</#list>
		<#else>			
		<#list colMaps as col>
		<#if col.isPK="yes">
		<if test="${col.fieldName} !=  null">			        
			and ${col.name}=${r"#{"}${col.fieldName}${r"}"}
		</if>
		</#if>
		</#list>
		</#if>
		<include refid="Where_Clause_Normal_Extend" />
	</sql>
	<sql id="Update_Set_Normal_Gen">
		<#if tab.pkFieldNum==1>
		<#list colMaps as col>
		<#if tab.pkFieldNum==1 && col.isPK="yes" &&  (col.type.javaType="Integer" || col.type.javaType="Long" || col.type.javaType="Float" || col.type.javaType="Double" || col.type.javaType="java.math.BigInteger" || col.type.javaType="String")>
		<!--
		<if test="id !=  null">			        
			${col.name}=${r"#{id}"},
		</if>
		-->
		<#else>
		<if test="${col.fieldName} !=  null">			        
			${col.name}=${r"#{"}${col.fieldName}${r"}"},
		</if>
		</#if>			
		</#list>
		<#else>			
		<#list colMaps as col>
		<#if col.isPK="yes">
		<if test="${col.fieldName} !=  null">			        
			${col.name}=${r"#{"}${col.fieldName}${r"}"},
		</if>
		</#if>
		</#list>
		</#if>
	</sql>
	<sql id="Update_Set_NewObj_Gen">
		<#if tab.pkFieldNum==1>
		<#list colMaps as col>
		<#if tab.pkFieldNum==1 && col.isPK="yes" &&  (col.type.javaType="Integer" || col.type.javaType="Long" || col.type.javaType="Float" || col.type.javaType="Double" || col.type.javaType="java.math.BigInteger" || col.type.javaType="String")>
		<if test="newObj.id !=  null">
			${col.name}=${r"#{newObj.id"}${r"}"},
		</if>
		<#else>
		<if test="${r"newObj."}${col.fieldName} !=  null">
			${col.name}=${r"#{newObj."}${col.fieldName}${r"}"},
		</if>
		</#if>			
		</#list>
		<#else>			
		<#list colMaps as col>
		<#if col.isPK="yes">
		<if test="${r"newObj."}${col.fieldName} !=  null">
			${col.name}=${r"#{newObj."}${col.fieldName}${r"}"},
		</if>
		</#if>
		</#list>
		</#if>			
	</sql>
	<sql id="Where_Clause_Params_Gen">
		<#if tab.pkFieldNum==1>
		<#list colMaps as col>
		<#if tab.pkFieldNum==1 && col.isPK="yes" &&  (col.type.javaType="Integer" || col.type.javaType="Long" || col.type.javaType="Float" || col.type.javaType="Double" || col.type.javaType="java.math.BigInteger" || col.type.javaType="String")>
		<if test="params.id !=  null">
			and ${col.name}=${r"#{params.id"}${r"}"}
		</if>
		<if test="params.fromId !=  null">
			and ${col.name}>${r"#{params.fromId"}${r"}"}
		</if>
		<#else>
		<if test="${r"params."}${col.fieldName} !=  null">
			and ${col.name}=${r"#{params."}${col.fieldName}${r"}"}
		</if>
		</#if>			
		</#list>
		<#else>			
		<#list colMaps as col>
		<#if col.isPK="yes">
		<if test="${r"params."}${col.fieldName} !=  null">
			and ${col.name}=${r"#{params."}${col.fieldName}${r"}"}
		</if>
		</#if>
		</#list>
		</#if>
		<include refid="Where_Clause_Params_Extend" />
	</sql>
	<sql id="Where_Clause_Batch_Gen">
		<foreach collection="list" item="item" index="index">
			<if test="index == 1">
				<#if tab.pkFieldNum==1>
				<#list colMaps as col>
				<#if tab.pkFieldNum==1 && col.isPK="yes" &&  (col.type.javaType="Integer" || col.type.javaType="Long" || col.type.javaType="Float" || col.type.javaType="Double" || col.type.javaType="java.math.BigInteger" || col.type.javaType="String")>
				<if test="item.id !=  null">
					and ${col.name} in 
					<foreach collection="list" item="element" index="index" open= "(" close =")" separator=",">
						${r"#{element.id"}${r"}"}
					</foreach>
				</if>
				<#else>
				<if test="${r"item."}${col.fieldName} !=  null">
					and ${col.name} in 
					<foreach collection="list" item="element" index="index" open= "(" close =")" separator=",">
						${r"#{element."}${col.fieldName}${r"}"}
					</foreach>
				</if>
				</#if>			
				</#list>
				<#else>			
				<#list colMaps as col>
				<#if col.isPK="yes">
				<if test="${r"item."}${col.fieldName} !=  null">
					and ${col.name} in 
					<foreach collection="list" item="element" index="index" open= "(" close =")" separator=",">
						${r"#{element."}${col.fieldName}${r"}"}
					</foreach>
				</if>
				</#if>
				</#list>
				</#if>
			</if>
		</foreach>
	</sql>
	
	<select id="queryById" parameterType="java.util.Map" resultMap="BaseResultMap">
		SELECT
			<include refid="Column_List_Base_Gen" />
		FROM
			${r"${tKjtTabName}"}<#if db.type="sqlserver"> WITH(NOLOCK)</#if>
		<where>
			<include refid="Where_Clause_Id_Gen" />
		</where>
		<#if db.type="mysql">limit 1</#if>
	</select>

	<select id="queryByMap" parameterType="java.util.Map" resultMap="BaseResultMap" fetchSize="100">				
		SELECT
			<include refid="Column_List_Base_Gen" />
		FROM
			${r"${tKjtTabName}"}<#if db.type="sqlserver"> WITH(NOLOCK)</#if>
		<where>
			<include refid="Where_Clause_Normal_Gen" />
		</where>
	</select>
	
	<select id="queryIdsByMap" parameterType="java.util.Map" resultType="java.lang.Long" fetchSize="100">				
		SELECT
			<include refid="Column_List_Id_Gen" />
		FROM
			${r"${tKjtTabName}"}<#if db.type="sqlserver"> WITH(NOLOCK)</#if>
		<where>
			<include refid="Where_Clause_Normal_Gen" />
		</where>
	</select>
	
	<select id="countByMap" parameterType="java.util.Map" resultType="java.lang.Integer">				
		SELECT
			count(*)
		FROM
			${r"${tKjtTabName}"}<#if db.type="sqlserver"> WITH(NOLOCK)</#if>
		<where>
			<include refid="Where_Clause_Normal_Gen" />
		</where>
	</select>
	
	<#if db.type="sqlserver">
	<select id="pageQuery" parameterType="com.kjt.service.common.dao.Page" resultMap="BaseResultMap"  fetchSize="100">
		SELECT
			TOP (${r"${pageSize}"})
			<include refid="Column_List_Base_Gen" />
		FROM
			${r"${params.tKjtTabName}"}<#if db.type="sqlserver"> WITH(NOLOCK)</#if>
		<where>
			<if test="params !=  null">
				<include refid="Where_Clause_Params_Gen" />
				<#if tab.pkFieldNum==1>
				<#list colMaps as col>
				<#if tab.pkFieldNum==1 && col.isPK="yes" &&  (col.type.javaType="Integer" || col.type.javaType="Long" || col.type.javaType="Float" || col.type.javaType="Double"|| col.type.javaType="java.math.BigInteger" || col.type.javaType="String")>
				and ${col.name} not in ( 
				select 
					top ((${r"${pageIndex}"} -1) * ${r"${pageSize}"}) ${col.name} 
				FROM 
					${r"${params.tKjtTabName}"} WITH(NOLOCK)
				</#if>
				</#list>
				</#if>
				<where>
					<include refid="Where_Clause_Params_Gen" />
				</where>
				<if test="orders !=  null"> order by ${r"${orders}"} </if>
				)
			</if>
		</where>
		<if test="orders !=  null"> order by ${r"${orders}"} </if>
	</select>
	<#else>
	<select id="pageQuery" parameterType="com.kjt.service.common.dao.Page" resultMap="BaseResultMap"  fetchSize="100">
		SELECT
			<include refid="Column_List_Base_Gen" />
		FROM
			${r"${params.tKjtTabName}"}
		<where>
			<if test="params !=  null">
				<include refid="Where_Clause_Params_Gen" />
			</if>
		</where>
			<if test="orders !=  null"> order by ${r"${orders}"} </if> limit ${r"#{start}"},${r"#{end}"}
	</select>
	</#if>
	
	<insert id="insert" parameterType="${package}.dao.model.${name}" useGeneratedKeys="true" keyProperty="id">
		INSERT INTO	${r"${tKjtTabName}"}
		(
		<trim suffix="" suffixOverrides=",">
			<#list colMaps as col>
				<#if col.isPK="no">
			<if test="${col.fieldName} !=null">
				<#elseif col.isPK="yes" && tab.pkFieldNum==1  &&  (col.type.javaType="Integer" || col.type.javaType="Long" || col.type.javaType="Float" || col.type.javaType="Double" || col.type.javaType="java.math.BigInteger" || col.type.javaType="String")>
			<if test="id !=null">
				<#else>
			<if test="${col.fieldName} !=null">
				</#if>
				${col.name}<#if col_has_next>,</#if>
			</if>
			</#list>
		</trim>
		)
		VALUES(
		<trim suffix="" suffixOverrides=",">
				<#list colMaps as col>
				<#if col.isPK="no">
			<if test="${col.fieldName} !=null">
				${r"#{"}${col.fieldName}${r"}"}<#if col_has_next>,</#if>
				<#elseif col.isPK="yes" && tab.pkFieldNum==1  &&  (col.type.javaType="Integer" || col.type.javaType="Long" || col.type.javaType="Float" || col.type.javaType="Double" || col.type.javaType="java.math.BigInteger" || col.type.javaType="String")>
			<if test="id !=null">
				${r"#{id}"}<#if col_has_next>,</#if>
				<#else>
			<if test="${col.fieldName} !=null">
				${r"#{"}${col.fieldName}${r"}"}<#if col_has_next>,</#if>
				</#if>			
			</if>
				</#list>
		</trim>
		)
	</insert>
	
	<update id="updateByMap" parameterType="java.util.Map">
		update 
			${r"${tKjtTabName}"}
		<set>
			<include refid="Update_Set_Normal_Gen" />
		</set>
		<where>
			<include refid="Where_Clause_Normal_Gen" />
		</where>
	</update>
	
	<update id="cmplxUpdate" parameterType="java.util.Map">
		update 
			${r"${tKjtTabName}"}
		<set>
			<include refid="Update_Set_NewObj_Gen" />
		</set>
		<where>
			<include refid="Where_Clause_Params_Gen" />	
		</where>
	</update>
	
	<delete id="deleteByMap" parameterType="java.util.Map">		
		DELETE
		FROM
			${r"${tKjtTabName}"} 
		<where>
			<include refid="Where_Clause_Normal_Gen" />
		</where>
	</delete>
	
	<insert id="batchInsert">
		insert into 
			${r"${tKjtTabName}"}  
			( 
		<foreach collection="list" item="item" index="index">
			<if test="index == 1">
			<trim suffix="" suffixOverrides=",">
				<#list colMaps as col>
				<#if col.isPK="no">
			<if test="item.${col.fieldName} !=null">
				<#elseif col.isPK="yes" && tab.pkFieldNum==1  &&  (col.type.javaType="Integer" || col.type.javaType="Long" || col.type.javaType="Float" || col.type.javaType="Double" || col.type.javaType="java.math.BigInteger" || col.type.javaType="String")>
			<if test="item.id !=null">
				<#else>
			<if test="item.${col.fieldName} !=null">
				</#if>
				${col.name}<#if col_has_next>,</#if>
			</if>
			</#list>
			</trim>
			</if>
		</foreach>
		)  values 
		<foreach collection="list" item="item" index="index" separator=","> 
		(
		<trim suffix="" suffixOverrides=",">
				<#list colMaps as col>
				<#if col.isPK="no">
			<if test="item.${col.fieldName} !=null">
				${r"#{item."}${col.fieldName}${r"}"}<#if col_has_next>,</#if>
				<#elseif col.isPK="yes" && tab.pkFieldNum==1  &&  (col.type.javaType="Integer" || col.type.javaType="Long" || col.type.javaType="Float" || col.type.javaType="Double" || col.type.javaType="java.math.BigInteger" || col.type.javaType="String")>
			<if test="item.id !=null">
				${r"#{item.id}"}<#if col_has_next>,</#if>
				<#else>
			<if test="item.${col.fieldName} !=null">
				${r"#{item."}${col.fieldName}${r"}"}<#if col_has_next>,</#if>
				</#if>			
			</if>
				</#list>
		</trim>
		)
		</foreach>
	</insert>
	
	<update id="batchUpdate" parameterType="java.util.Map">
		update 
			${r"${tKjtTabName}"}
		<set>
			<include refid="Update_Set_NewObj_Gen" />
		</set>
		<where>
			<include refid="Where_Clause_Batch_Gen" />
		</where>
	</update>
	
	<delete id="batchDelete"  parameterType="java.util.List">
		delete 
		from
			${r"${tKjtTabName}"}
		<where>
			<include refid="Where_Clause_Batch_Gen" />
		</where>
	</delete>
	
	<select id="batchQuery"  parameterType="java.util.List" resultMap="BaseResultMap" fetchSize="100">
		select 
			<include refid="Column_List_Base_Gen" />
		from
			${r"${tKjtTabName}"}<#if db.type="sqlserver"> WITH(NOLOCK)</#if>
		<where>
			<include refid="Where_Clause_Batch_Gen" />
		</where>
	</select>
	
	<!--所有Gen结尾的sql是公共的,其表达式都是等值判断表达式，含有Batch的是in值表达式-->
	<sql id="Where_Clause_Id_Extend">
		<!--id扩展表达式; eg: id字段名 between ${r"#{startId}"} and ${r"#{endId}"}-->
	</sql>
	<sql id="Where_Clause_Normal_Extend">
		<!--其他字段扩展表达式; eg: id字段名 between ${r"#{startId}"} and ${r"#{endId}"}-->
	</sql>
	<sql id="Where_Clause_Params_Extend">
		<!--分页&复杂更新条件字段扩展表达式; eg: id字段名 between ${r"#{params.startId}"} and ${r"#{params.endId}"}-->
	</sql>
	
	<!--扩展sql从备注处开始定义，id建议以'有意义的名字'-->
	
</mapper>
