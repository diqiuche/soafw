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

	<context:component-scan base-package="com.kjt.service.#{artifactId}.*" />
	
	<!-- 消费方应用名，用于计算依赖关系，不是匹配条件，不要与提供方一样 -->
	<!--
	<dubbo:application name="kjt-#{artifactId}-web"/>
	 -->
    <!-- 使用zookeeper发现服务地址 --> 
    <!--
    <dubbo:registry address="${registry.address}"/>
    -->
    <!--
    <dubbo:consumer timeout="10000" init="true" check="false"/>
    -->
    <!-- 使用监控中心 
    <dubbo:monitor protocol="registry"/>
    -->
    <!--
	<dubbo:reference id="xxxService" interface="com.kjt.service.#{artifactId}.IXxxxService" />
	-->
	
</beans>
