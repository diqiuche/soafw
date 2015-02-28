<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns="http://www.springframework.org/schema/beans" xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:context="http://www.springframework.org/schema/context" xmlns:tx="http://www.springframework.org/schema/tx"
	xmlns:mvc="http://www.springframework.org/schema/mvc" xmlns:cache="http://www.springframework.org/schema/cache"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
    http://www.springframework.org/schema/aop
    http://www.springframework.org/schema/aop/spring-aop-4.0.xsd
    http://www.springframework.org/schema/tx
    http://www.springframework.org/schema/tx/spring-tx-4.0.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context-4.0.xsd"
	default-autowire="byName">

	<context:component-scan base-package="com.kjt.service.#{artifactId}.*,com.kjt.service.#{artifactId}" />

	<aop:aspectj-autoproxy />
	<tx:annotation-driven />


	<import resource="classpath*:/META-INF/config/spring/spring-db.xml" />
	<import resource="classpath*:/META-INF/config/spring-cache.xml" />
	<import resource="classpath*:/META-INF/config/spring-rpc.xml" />
	<import resource="classpath*:/META-INF/config/spring-service.xml" />
	<import resource="classpath*:/META-INF/config/spring-aop.xml" />
</beans>
