<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns="http://www.springframework.org/schema/beans" xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:cache="http://www.springframework.org/schema/cache"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
    http://www.springframework.org/schema/aop
    http://www.springframework.org/schema/aop/spring-aop-4.0.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context-4.0.xsd">
	
	<context:component-scan base-package="com.kjt.service.#{artifactId}.*" />
	
	<import resource="classpath*:/META-INF/config/spring/spring-cache.xml"/>
	
	<bean id="cache_db" class="com.kjt.service.common.datasource.DynamicDataSource"
		init-method="init">
		<property name="prefix" value="cache_db" />
	</bean>
	<bean id="cache_db_slave" class="com.kjt.service.common.datasource.DynamicDataSource"
		init-method="init">
		<property name="prefix" value="cache_db_slave" />
	</bean>
	<bean id="cache_db_map_query" class="com.kjt.service.common.datasource.DynamicDataSource"
		init-method="init">
		<property name="prefix" value="cache_db_map_query" />
	</bean>
	
	<bean id="#{artifactId}" class="com.kjt.service.common.datasource.DynamicDataSource"
		init-method="init">
		<property name="prefix" value="#{artifactId}" />
	</bean>
	<bean id="#{artifactId}_slave" class="com.kjt.service.common.datasource.DynamicDataSource"
		init-method="init">
		<property name="prefix" value="#{artifactId}_slave" />
	</bean>
	<bean id="#{artifactId}_map_query" class="com.kjt.service.common.datasource.DynamicDataSource"
		init-method="init">
		<property name="prefix" value="#{artifactId}_map_query" />
	</bean>
	<!--新增数据库是需要添加 master、slave、map_query三个-->
</beans>