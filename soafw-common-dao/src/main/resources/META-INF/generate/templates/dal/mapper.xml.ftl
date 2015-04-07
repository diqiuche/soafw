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
	</sql>
	<sql id="Normal_Where_Clause">
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
	</sql>
	
	<select id="queryById" parameterType="java.util.Map" resultMap="BaseResultMap">
		SELECT
			<include refid="Base_Column_List" />
		FROM
			${r"${tKjtTabName}"}
		<where>
			<include refid="Id_Where_Clause" />
		</where>
			limit 1
	</select>

	<select id="queryByMap" parameterType="java.util.Map" resultMap="BaseResultMap">				
		SELECT
			<include refid="Base_Column_List" />
		FROM
			${r"${tKjtTabName}"}
		<where>
			<include refid="Normal_Where_Clause" />
		</where>
		
	</select>
	
	<select id="queryIdsByMap" parameterType="java.util.Map" resultType="java.lang.Long">				
		SELECT
			<include refid="Id_Column_List" />
		FROM
			${r"${tKjtTabName}"}
		<where>
			<include refid="Normal_Where_Clause" />
		</where>
		
	</select>
	
	<select id="countByMap" parameterType="java.util.Map" resultType="java.lang.Integer">				
		SELECT
			count(*)
		FROM
			${r"${tKjtTabName}"}
		<where>			
			<include refid="Normal_Where_Clause" />
		</where>
				
	</select>
	
	<select id="pageQuery" parameterType="com.kjt.service.common.dao.Page" resultMap="BaseResultMap" fetchSize="15">
		SELECT
			<include refid="Base_Column_List" />
		FROM
			${r"${params.tKjtTabName}"}
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
			<#elseif tab.pkFieldNum==1 && col.isPK="yes"  &&  col.type.javaType="String">
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
		<where>
			<include refid="Normal_Where_Clause" />
		</where>
		
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
		<where>
			<include refid="Normal_Where_Clause" />		
		</where>
		
	</delete>
	
	<sql id="Helpper_Where_Clause">
    
	    <where>
	      <foreach collection="oredCriteria" item="criteria" separator="or">
	        <if test="criteria.valid">
	          <trim prefix="(" prefixOverrides="and" suffix=")">
	            <foreach collection="criteria.criteria" item="criterion">
	              <choose>
	                <when test="criterion.noValue">
	                  and ${r"${criterion.condition}"}
	                </when>
	                <when test="criterion.singleValue">
	                  and ${r"${criterion.condition}"} ${r"#{criterion.value}"}
	                </when>
	                <when test="criterion.betweenValue">
	                  and ${r"${criterion.condition}"} ${r"#{criterion.value}"} and ${r"#{criterion.secondValue}"}
	                </when>
	                <when test="criterion.listValue">
	                  and ${r"${criterion.condition}"}
	                  <foreach close=")" collection="criterion.value" item="listItem" open="(" separator=",">
	                    ${r"#{listItem}"}
	                  </foreach>
	                </when>
	              </choose>
	            </foreach>
	          </trim>
	        </if>
	      </foreach>
		</where>
 	</sql>
  	
  	<select id="selectByHelpper" parameterType="${package}.dao.model.${name}Helpper" resultMap="BaseResultMap">
	    select
	    	<if test="distinct">
	      		distinct
	    	</if>
	    	<include refid="Base_Column_List" />
	    from 
	    	${r"${tKjtTabName}"}
	    	
	    	<if test="helpper != null">
	      		<include refid="Helpper_Where_Clause" />
	    	</if>
	    	<if test="orderByClause != null">
	      		order by ${r"${orderByClause}"}
	    	</if>
  	</select>
  	
  	<select id="countByHelpper" parameterType="${package}.dao.model.${name}Helpper" resultType="java.lang.Integer">
    	select 
    		count(*) 
    	from 
    		${r"${tKjtTabName}"}
	    <if test="helpper != null">
	      <include refid="Helpper_Where_Clause" />
	    </if>
  	</select>
  
  	<delete id="deleteByHelpper" parameterType="${package}.dao.model.${name}Helpper">
	    delete 
	    from 
	    	${r"${tKjtTabName}"}
		    <if test="helpper != null">
		      <include refid="Helpper_Where_Clause" />
		    </if>
  	</delete>
  
	<sql id="Update_By_Helpper_Where_Clause">
	    <where>
	      <foreach collection="helpper.oredCriteria" item="criteria" separator="or">
	        <if test="criteria.valid">
	          <trim prefix="(" prefixOverrides="and" suffix=")">
	            <foreach collection="criteria.criteria" item="criterion">
	              <choose>
	                <when test="criterion.noValue">
	                  and ${r"${criterion.condition}"}
	                </when>
	                <when test="criterion.singleValue">
	                  and ${r"${criterion.condition}"} ${r"#{criterion.value}"}
	                </when>
	                <when test="criterion.betweenValue">
	                  and ${r"${criterion.condition}"} ${r"#{criterion.value}"} and ${r"#{criterion.secondValue}"}
	                </when>
	                <when test="criterion.listValue">
	                  and ${r"${criterion.condition}"}
	                  <foreach close=")" collection="criterion.value" item="listItem" open="(" separator=",">
	                    ${r"#{listItem}"}
	                  </foreach>
	                </when>
	              </choose>
	            </foreach>
	          </trim>
	        </if>
	      </foreach>
	    </where>
  	</sql>
  	<update id="updateByHelpper" parameterType="java.util.Map">
  		update 
  		${r"${tKjtTabName}"}
		<set>
			<#if tab.pkFieldNum==1>
			<#list colMaps as col>
			<#if tab.pkFieldNum==1 && col.isPK="yes" &&  (col.type.javaType="Integer" || col.type.javaType="Long" || col.type.javaType="Float" || col.type.javaType="Double" || col.type.javaType="java.math.BigInteger" || col.type.javaType="String")>
			<if test="updNewMap.id !=  null">
				${col.name}=${r"#{record.id"}${r"}"},
			</if>
			<#else>
			<if test="${r"record."}${col.fieldName} !=  null">
				${col.name}=${r"#{record."}${col.fieldName}${r"}"},
			</if>
			</#if>			
			</#list>
			<#else>			
			<#list colMaps as col>
			<#if col.isPK="yes">
			<if test="${r"record."}${col.fieldName} !=  null">
				${col.name}=${r"#{record."}${col.fieldName}${r"}"},
			</if>
			</#if>
			</#list>
			</#if>			
		</set>
		<if test="helpper != null">
			<include refid="Update_By_Helpper_Where_Clause" />
    	</if>
  	</update>
</mapper>
