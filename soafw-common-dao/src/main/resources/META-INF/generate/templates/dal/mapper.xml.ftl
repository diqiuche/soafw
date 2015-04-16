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
	<sql id="Id_Column_List">
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
	<sql id="Base_Column_List">
		<#list colMaps as col>
			${col.name}<#if col_has_next>,</#if>
		</#list>
	</sql>
	<sql id="Id_Where_Clause">
		<where>
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
		</where>
	</sql>
	<sql id="Normal_Where_Clause">
		<where>
			<#if tab.pkFieldNum==1>
			<#list colMaps as col>
			<#if tab.pkFieldNum==1 && col.isPK="yes" &&   (col.type.javaType="Integer" || col.type.javaType="Long" || col.type.javaType="Float" || col.type.javaType="Double" || col.type.javaType="java.math.BigInteger" || col.type.javaType="String")>
			<if test="id !=  null">
				and ${col.name}=${r"#{id}"}
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
		</where>
	</sql>
	
	<select id="queryById" parameterType="java.util.Map" resultMap="BaseResultMap">
		SELECT
			<include refid="Base_Column_List" />
		FROM
			${r"${tKjtTabName}"}<#if db.type="sqlserver"> WITH(NOLOCK)</#if>

			<include refid="Id_Where_Clause" />

			limit 1
	</select>

	<select id="queryByMap" parameterType="java.util.Map" resultMap="BaseResultMap" fetchSize="100">				
		SELECT
			<include refid="Base_Column_List" />
		FROM
			${r"${tKjtTabName}"}<#if db.type="sqlserver"> WITH(NOLOCK)</#if>

		<include refid="Normal_Where_Clause" />
		
	</select>
	
	<select id="queryIdsByMap" parameterType="java.util.Map" resultType="java.lang.Long" fetchSize="100">				
		SELECT
			<include refid="Id_Column_List" />
		FROM
			${r"${tKjtTabName}"}<#if db.type="sqlserver"> WITH(NOLOCK)</#if>
		
		<include refid="Normal_Where_Clause" />
		
	</select>
	
	<select id="countByMap" parameterType="java.util.Map" resultType="java.lang.Integer">				
		SELECT
			count(*)
		FROM
			${r"${tKjtTabName}"}<#if db.type="sqlserver"> WITH(NOLOCK)</#if>

		<include refid="Normal_Where_Clause" />
				
	</select>
	
	<select id="pageQuery" parameterType="com.kjt.service.common.dao.Page" resultMap="BaseResultMap"  fetchSize="100">
		SELECT
			<include refid="Base_Column_List" />
		FROM
			${r"${params.tKjtTabName}"}<#if db.type="sqlserver"> WITH(NOLOCK)</#if>
		<where>
			<if test="params !=  null">
				<#if tab.pkFieldNum==1>
				<#list colMaps as col>
				<#if tab.pkFieldNum==1 && col.isPK="yes" &&  (col.type.javaType="Integer" || col.type.javaType="Long" || col.type.javaType="Float" || col.type.javaType="Double"|| col.type.javaType="java.math.BigInteger" || col.type.javaType="String")>
				<if test="params.id !=  null">
					and ${col.name}=${r"#{params.id"}${r"}"}
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
			</if>
		</where>
			<if test="orders !=  null"> order by ${r"${orders}"} </if> limit ${r"#{start}"},${r"#{end}"}
	</select>

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
			<#if tab.pkFieldNum==1>
			<#list colMaps as col>
			<#if tab.pkFieldNum==1 && col.isPK="yes" &&  (col.type.javaType="Integer" || col.type.javaType="Long" || col.type.javaType="Float" || col.type.javaType="Double" || col.type.javaType="java.math.BigInteger" || col.type.javaType="String")>
				${col.name}=${r"#{"}${col.fieldName}${r"}"},
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
		</set>

		<include refid="Normal_Where_Clause" />
		
	</update>
	
	<update id="cmplxUpdate" parameterType="java.util.Map">
		update 
			${r"${tKjtTabName}"}
		<set>
			<#if tab.pkFieldNum==1>
			<#list colMaps as col>
			<#if tab.pkFieldNum==1 && col.isPK="yes" &&  (col.type.javaType="Integer" || col.type.javaType="Long" || col.type.javaType="Float" || col.type.javaType="Double" || col.type.javaType="java.math.BigInteger" || col.type.javaType="String")>
			<if test="updNewMap.id !=  null">
				${col.name}=${r"#{updNewMap.id"}${r"}"},
			</if>
			<#else>
			<if test="${r"updNewMap."}${col.fieldName} !=  null">
				${col.name}=${r"#{updNewMap."}${col.fieldName}${r"}"},
			</if>
			</#if>			
			</#list>
			<#else>			
			<#list colMaps as col>
			<#if col.isPK="yes">
			<if test="${r"updNewMap."}${col.fieldName} !=  null">
				${col.name}=${r"#{updNewMap."}${col.fieldName}${r"}"},
			</if>
			</#if>
			</#list>
			</#if>			
		</set>
		<where>
			<#if tab.pkFieldNum==1>
			<#list colMaps as col>
			<#if tab.pkFieldNum==1 && col.isPK="yes" &&  (col.type.javaType="Integer" || col.type.javaType="Long" || col.type.javaType="Float" || col.type.javaType="Double" || col.type.javaType="java.math.BigInteger" || col.type.javaType="String")>
			<if test="updCondMap.id !=  null">
				and ${col.name}=${r"#{updCondMap.id"}${r"}"}
			</if>
			<#else>
			<if test="${r"updCondMap."}${col.fieldName} !=  null">
				and ${col.name}=${r"#{updCondMap."}${col.fieldName}${r"}"}
			</if>
			</#if>			
			</#list>
			<#else>			
			<#list colMaps as col>
			<#if col.isPK="yes">
			<if test="${r"updCondMap."}${col.fieldName} !=  null">
				and ${col.name}=${r"#{updCondMap."}${col.fieldName}${r"}"}
			</if>
			</#if>
			</#list>
			</#if>		
		</where>
		
	</update>
	
	<delete id="deleteByMap" parameterType="java.util.Map">		
		DELETE
		FROM
			${r"${tKjtTabName}"} 
			
		<include refid="Normal_Where_Clause" />		
		
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
	
	<sql id="Batch_Where_Clause">
		<where>
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
		</where>
	</sql>
	
	
	<update id="batchUpdate" parameterType="java.util.Map">
		update 
			${r"${tKjtTabName}"}
		<set>
			<#if tab.pkFieldNum==1>
			<#list colMaps as col>
			<#if tab.pkFieldNum==1 && col.isPK="yes" &&  (col.type.javaType="Integer" || col.type.javaType="Long" || col.type.javaType="Float" || col.type.javaType="Double" || col.type.javaType="java.math.BigInteger" || col.type.javaType="String")>
			<if test="updNewMap.id !=  null">
				${col.name}=${r"#{updNewMap.id"}${r"}"},
			</if>
			<#else>
			<if test="${r"updNewMap."}${col.fieldName} !=  null">
				${col.name}=${r"#{updNewMap."}${col.fieldName}${r"}"},
			</if>
			</#if>			
			</#list>
			<#else>			
			<#list colMaps as col>
			<#if col.isPK="yes">
			<if test="${r"updNewMap."}${col.fieldName} !=  null">
				${col.name}=${r"#{updNewMap."}${col.fieldName}${r"}"},
			</if>
			</#if>
			</#list>
			</#if>			
		</set>
		
		<include refid="Batch_Where_Clause" />

	</update>
	
	<delete id="batchDelete"  parameterType="java.util.List">
		delete 
		
		from
			${r"${tKjtTabName}"}
			
		<include refid="Batch_Where_Clause" />
			
	</delete>
	
	<select id="batchQuery"  parameterType="java.util.List" resultMap="BaseResultMap" fetchSize="100">
		select 
			<include refid="Base_Column_List" />
		from
			${r"${tKjtTabName}"}<#if db.type="sqlserver"> WITH(NOLOCK)</#if>
			
		<include refid="Batch_Where_Clause" />
			
	</select>
	
</mapper>
