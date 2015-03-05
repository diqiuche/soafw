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
	
	<context:component-scan base-package="com.kjt.service.#{artifactId}.*,com.kjt.service.#{artifactId}" />
	
	<import resource="classpath*:/META-INF/config/local/spring-cache.xml"/>
	
	<bean id="#{artifactId}DataSourceMaster" class="com.kjt.service.common.datasource.DynamicDataSource"
		init-method="init">
		<property name="prefix" value="#{artifactId}_db" />
	</bean>
	<bean id="#{artifactId}DataSourceSlave" class="com.kjt.service.common.datasource.DynamicDataSource"
		init-method="init">
		<property name="prefix" value="#{artifactId}_db_slave" />
	</bean>
	<bean id="#{artifactId}DataSourceMapQuery" class="com.kjt.service.common.datasource.DynamicDataSource"
		init-method="init">
		<property name="prefix" value="#{artifactId}_db_slave" />
	</bean>
</beans>