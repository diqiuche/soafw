<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns="http://www.springframework.org/schema/beans" xmlns:mvc="http://www.springframework.org/schema/mvc"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:util="http://www.springframework.org/schema/util"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context-4.0.xsd
    http://www.springframework.org/schema/mvc
    http://www.springframework.org/schema/mvc/spring-mvc-4.0.xsd
    http://www.springframework.org/schema/util
    http://www.springframework.org/schema/util/spring-util-4.0.xsd">

	<!-- 把标记了@Controller注解的类转换为bean -->
	<context:component-scan
		base-package="com.kjt.service.#{artifactId}.*"
		use-default-filters="false">
		<context:include-filter type="annotation"
			expression="org.springframework.stereotype.Controller" />

	</context:component-scan>
	
	<mvc:annotation-driven>
		<mvc:message-converters>
			<bean
				class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
				<property name="objectMapper" ref="objectMapper">
				</property>
			</bean>
		</mvc:message-converters>

	</mvc:annotation-driven>

	<bean id="objectMapper"
		class="org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean">
		<property name="featuresToDisable">
			<array>
				<util:constant
					static-field="com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES" />
				<util:constant
					static-field="com.fasterxml.jackson.databind.SerializationFeature.WRITE_NULL_MAP_VALUES" />
			</array>
		</property>
		<property name="propertyNamingStrategy">
			<util:constant
				static-field="com.fasterxml.jackson.databind.PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES" />
		</property>
		<property name="simpleDateFormat" value="yyyy-MM-dd HH:mm:ss">
		</property>
		<property name="serializationInclusion">
			<util:constant
				static-field="com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL" />
		</property>
	</bean>
	<mvc:interceptors>
		<bean class="com.kjt.service.common.web.interceptor.RequestIDInterceptor" />
	</mvc:interceptors>


	<!-- 定义视图分解器 -->
	<bean id="viewResolver"
		class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<property name="viewClass">
			<value>org.springframework.web.servlet.view.InternalResourceView
			</value>
		</property>
		<!-- 设置前缀，即视图所在的路径 -->
		<property name="prefix" value="/WEB-INF/jsp/" />
		<!-- 设置后缀，即视图的扩展名 -->
		<property name="suffix" value=".jsp" />
	</bean>


	<!-- <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver" 
		p:defaultEncoding="utf-8" /> -->

	<!-- 静态资源映射 -->
	<mvc:resources mapping="/css/**" location="/css/" />
	<mvc:resources mapping="/images/**" location="/images/" />
	<mvc:resources mapping="/js/**" location="/js/" />
	<mvc:resources mapping="/static/**" location="/static/" />

	<!-- 控制器异常处理 -->
	<bean id="responseStatusBasicExceptionResolver"
		class="com.kjt.service.common.web.spring.ResponseStatusBasicExceptionResolver" />
	<!-- <import resource="classpath*:/META-INF/config/application-config.xml" 
		/> -->

</beans>
