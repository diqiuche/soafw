<?xml version="1.0" encoding="UTF-8"?>
<beans
	xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:aop="http://www.springframework.org/schema/aop" 
	xmlns:tx="http://www.springframework.org/schema/tx" 
	xmlns:dubbo="http://code.alibabatech.com/schema/dubbo" 
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	 http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
	 http://www.springframework.org/schema/aop
	 http://www.springframework.org/schema/aop/spring-aop-3.0.xsd
     http://www.springframework.org/schema/tx 
     http://www.springframework.org/schema/tx/spring-tx-3.0.xsd
     http://www.springframework.org/schema/context 
     http://www.springframework.org/schema/context/spring-context-3.0.xsd
 	 http://code.alibabatech.com/schema/dubbo  
 	 http://code.alibabatech.com/schema/dubbo/dubbo.xsd">
	
	<context:component-scan base-package="com.kjt.service.#{artifactId}.*" />
	
	<!-- 消费方应用名，用于计算依赖关系，不是匹配条件，不要与提供方一样 -->
	<!--
	<dubbo:application name="kjt-#{artifactId}-job"/>
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